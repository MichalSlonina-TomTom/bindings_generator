# ADR: Adaptations Needed to Apply the Bindings Generator to Junction-View-Engine

**Date:** 2026-03-03
**Status:** Draft

---

## Context

The bindings generator was originally designed and validated against the `text-generation` use case.
The `junction-view-engine` use case has a different shape and exposes several structural differences
that prevent the generator from being applied directly.

The evidence lives in:
- `src/test/resources/junction-view-engine/expected/` — hand-crafted reference files
- `src/test/resources/junction-view-engine/proto/` — `junction_view_request.proto` and
  `junction_view_information.proto`
- `src/test/resources/junction-view-engine/expected/NativeJunctionViewClient.kt` — the real
  Kotlin client that embeds both client logic *and* the mapper functions that should be extracted

---

## Problem 1 — Mapper code is embedded inside `NativeJunctionViewClient`

`NativeJunctionViewClient.kt` is a full Android native client class.  Inside it there are private
extension functions (`JunctionViewRequest.toProto()`, `ByteArray.fromProto()`,
`ProtoJunctionViewType.convertJunctionViewType()`, `ProtoJunctionViewDaylightType.convertJunctionViewDaylightType()`)
that are logically the NativeModelMapper.

**Required action:** Extract those private extension functions into a separate
`NativeModelMapper.kt` file (equivalent to what already exists for text-generation) so that the
generator can own and regenerate that file independently of the client class.

---


## Problem 2 — C++ namespace convention is different

Text-generation uses a **flat, single-level `protobuf_helpers` namespace**:

```cpp
namespace protobuf_helpers {
  NativeLanguage toNative(...);
}
```

Junction-view-engine uses a **deep, five-level nested namespace**:

```cpp
namespace tomtom {
namespace sdk {
namespace bindings {
namespace junction_view_engine {
namespace internal {
  NativeJunctionViewRequestParams FromProto(...);
}}}}}
```

The generator (`CppGenerator.kt`) currently always emits `namespace protobuf_helpers { … }`.
It has no concept of a configurable or multi-level output namespace.

**Required action:** Use namespace from protofile to make it simpler.

---

## Problem 3 — C++ function naming convention is different

Text-generation uses **camelCase** names matching the Kotlin convention:
```cpp
toNative(...)
toProto(...)
```

Junction-view-engine uses **PascalCase**:
```cpp
FromProto(...)
ToProto(...)
```

`CppGenerator.kt` hard-codes `toNative` and `toProto`.

**Required action:** Use PascalCase everywhere.

---

## Problem 4 — C++ header uses `#pragma once` instead of `#ifndef` / `#define` / `#endif`

Text-generation expected header:
```cpp
#ifndef PROTOBUF_HELPERS_HPP
#define PROTOBUF_HELPERS_HPP
…
#endif  // PROTOBUF_HELPERS_HPP
```

Junction-view-engine expected header:
```cpp
#pragma once
```

`CppGenerator.generateHeader()` always emits the `#ifndef` guard.

**Required action:** Use pragma once everywhere.

---

## Problem 5 — C++ header includes native SDK headers that are not derivable from proto alone

The junction-view-engine header includes:
```cpp
#include <tomtom/sdk/junctionview_engine/junctionview_engine.hpp>
#include "junction_view_information.pb.h"
#include "junction_view_request.pb.h"
```

These native SDK includes cannot be inferred purely from a `.proto` file.  The generator currently
derives `#include` statements only from the proto package and file names.

**Required action:** Support extra/user-supplied `#include` lines via a generator configuration json.

---

## Problem 6 — C++ output contains hand-crafted native structs

`protobuf_helpers.hpp` for junction-view-engine declares two native helper structs:
```cpp
struct NativeJunctionViewRequestParams { … };
struct NativeJunctionViewResult { … };
```

These are not derived from the proto schema — they describe native C++ types that map to the proto
messages but use SDK-specific types (`orodoro::quantities::TCentimeters`, etc.).

The generator has no concept of emitting native struct definitions.

**Required action:** Either (a) treat these structs as handwritten scaffolding that lives outside
the generated file, or (b) introduce a mechanism to describe native type mappings (native type
name, fields, their native SDK types) in a sidecar config file that the generator reads.  Option
(a) is the lower-risk starting point.

---

## Problem 7 — Kotlin mapper uses `ByteArray` (raw proto serialisation) instead of typed proto objects

In text-generation the Kotlin mapper receives and returns **typed proto/native objects** directly:

```kotlin
fun Language.toProto(): ProtoLanguage
fun ProtoLanguage.toNative(): Language
```

In `NativeJunctionViewClient` the boundary is a **`ByteArray`** serialised over JNI:

```kotlin
fun JunctionViewRequest.toProto(): ByteArray          // serialises to bytes
fun ByteArray.fromProto(): List<JunctionViewInformation>?  // deserialises from bytes
```

`KotlinGenerator.kt` always generates typed-object extension functions.  It does not know about
the `ByteArray` serialisation pattern.

### Option A — Typed objects (mirror text-generation)

```kotlin
fun JunctionViewRequest.toProto(): ProtoJunctionViewRequest
fun ProtoJunctionViewResult.toNative(): List<JunctionViewInformation>?
```

Pros:
- The generator already knows how to emit this pattern — zero new code paths needed.
- Separation of concerns: serialisation to bytes is a transport detail that belongs in
  `NativeJunctionViewClient`, not in the mapper.
- Typed mappers can be unit-tested without JNI or byte-buffer manipulation.
- Consistent with text-generation: one pattern across all use cases keeps the generator simpler
  and its output predictable.

The byte serialisation becomes the explicit responsibility of the caller:

```kotlin
// inside NativeJunctionViewClient — hand-written, not generated
val bytes = request.toProto().toByteArray()
val result = ProtoJunctionViewResult.parseFrom(responseBytes).toNative()
```

### Option B — `ByteArray` JNI boundary (preserve current pattern)

```kotlin
fun JunctionViewRequest.toProto(): ByteArray
fun ByteArray.fromProto(): List<JunctionViewInformation>?
```

Pros:
- Matches what `NativeJunctionViewClient` currently does — no changes to the client.

Cons:
- Requires a new "JNI serialisation mode" flag in the generator.
- Conflates type conversion with serialisation inside the mapper.
- Harder to unit-test.
- Diverges from text-generation, adding a second code path to maintain.

### Decision

**Option A is adopted.**  The mapper's responsibility is type conversion only.
`NativeJunctionViewClient` is responsible for calling `.toByteArray()` and `parseFrom()` at the
JNI boundary.

**Required action:** Generate typed-object extension functions (Option A) — no generator changes
needed for this problem.  Update `NativeJunctionViewClient` to call `.toByteArray()` /
`parseFrom()` explicitly rather than delegating serialisation to the mapper.

---

## Problem 8 — Kotlin mapper silently falls back on unrecognised enum values

In text-generation, the `else ->` branch of generated `when` blocks silently falls back to a
default proto value:
```kotlin
else -> ProtoLanguage.getDefaultInstance()
```

In `NativeJunctionViewClient`, unrecognised enum values **throw**:
```kotlin
ProtoJunctionViewType.UNRECOGNIZED ->
    throw IllegalArgumentException("Unexpected junction view type $this.")
```

`KotlinGenerator.kt` always emits the silent fallback.  Silent fallbacks hide bugs — a value that
cannot be mapped should never propagate silently as a default.

### Decision

**Always throw on `UNRECOGNIZED`.**  The generator should emit a throwing `else` branch for every
enum `when` block, both in existing text-generation output and in all future use cases.  The
silent fallback in the current text-generation output is a bug that this change will fix as a
side-effect.

```kotlin
else -> throw IllegalArgumentException("Unexpected value $this.")
```

**Required action:** Update `KotlinGenerator.kt` to emit a throwing `else` branch in all enum
`when` blocks.  Update the text-generation reference file
`src/test/resources/text-generation/expected/NativeModelMapper.kt` to reflect the new behaviour.

---

## Problem 9 — Copyright year and symbol differ

Text-generation expected files use `© 2022 TomTom NV`.
Junction-view-engine expected files use `© 2024 TomTom NV`.
The generator emits `Copyright (C) 2022 TomTom NV` (no Unicode © symbol, hard-coded year).

**Required action:** Use current year, get it from the operating system.

---

## Problem 10 — `optional` scalar fields in proto3 (`RouteArc`)

`junction_view_request.proto` uses a proto3 `optional` modifier:

```proto
optional int32 arrival_offset_on_arc_in_centimeters = 3;
```

This generates a `has_*()` presence-checking method on the C++ side, and maps to a nullable
(`Int?`) field on the Kotlin side.  The existing `ParsedField.isOptional` flag is already present
in the data model, but:

- `CppGenerator.kt` does not emit `has_*()` guard checks for optional scalar fields in the
  generated `FromProto`/`toNative` implementations.
- `KotlinGenerator.kt` does not emit nullable types or `?.let { }` wrappers for optional scalars.

The hand-crafted `NativeJunctionViewClient.kt` already demonstrates the correct Kotlin pattern:
```kotlin
routeWindowArc.arrivalOffsetOnArc?.let {
    arrivalOffsetOnArcInCentimeters = it.inWholeCentimeters().toInt()
}
```

**Required action:** Verify and fix both generators to handle optional scalar fields correctly —
emit `has_*()` guards in C++ and nullable `?.let` wrappers in Kotlin.

---

## Problem 11 — `oneof` fields (`JunctionViewResult`)

`junction_view_information.proto` uses a `oneof`:

```proto
message JunctionViewResult {
  oneof Result {
    JunctionViewInformationList junction_views = 1;
    JunctionViewError error = 2;
  }
}
```

`ProtoParser.kt` does not recognise `oneof` field groups — it will likely flatten the two
alternatives into ordinary repeated/message fields, losing the mutual-exclusion semantics.
Neither generator emits a discriminated-union / `when` pattern for `oneof`.

The expected C++ output deals with this by using `has_error()` / `mutable_junction_views()` calls
that are only correct if the `oneof` is understood.

**Required action:** Add `oneof` support to `ProtoParser` (new `ParsedOneof` construct or an
`isOneof` flag on `ParsedField`) and teach both generators to emit the appropriate conditional
logic.

---

## Problem 12 — Nested enum inside a message (`JunctionViewError.ErrorType`)

`junction_view_information.proto` defines an enum *inside* a message:

```proto
message JunctionViewError {
  enum ErrorType {
    kArcVersionMismatch = 0;
    kMoreArcsRequired = 1;
  }
  ErrorType errorType = 1;
  string    message   = 2;
}
```

`ParsedMessage.nestedEnums` exists in the data model and the generators already have code paths
for nested enums, but this case introduces a wrinkle: the C++ type name for the enum is
`JunctionViewError_ErrorType` (protobuf's flat mangling), while the proto name is
`JunctionViewError.ErrorType`.  The expected C++ code uses the mangled form correctly:

```cpp
proto_result.mutable_error()->set_errortype(
    com::…::JunctionViewError_ErrorType::JunctionViewError_ErrorType_kArcVersionMismatch);
```

The generator must produce the underscore-mangled name, not a dot-separated one.

**Required action:** Confirm that `CppGenerator` already uses the underscore-mangled name for
nested enum types in `set_*()` calls; add a test case covering this pattern.

---

## Recommended Approach

1. **Extract the mapper** — move the mapper functions out of `NativeJunctionViewClient.kt` into
   a standalone `NativeModelMapper.kt` so the boundary between hand-written client code and
   generated mapper code is clear (Problem 1).
2. **Verify `optional` scalar handling** — write a targeted test for `RouteArc` to confirm
   `isOptional` round-trips through the parser and both generators emit correct nullable/`has_*()`
   code (Problem 10).
3. **Add `oneof` support** — extend `ProtoParser`, `CppGenerator`, and `KotlinGenerator` to
   recognise and emit correct conditional code for `oneof` fields (Problem 11).
4. **Verify nested-enum C++ name mangling** — confirm or fix underscore-mangled type names for
   nested enums in `set_*()` calls; add a test (Problem 12).
5. **Add a `GeneratorConfig` object** to `CppGenerator` and `KotlinGenerator` that holds
   namespace segments, naming style (camelCase vs PascalCase), header-guard style, extra includes,
   and copyright year — rather than adding individual CLI flags one at a time (Problems 2–5, 9).
6. **Treat native structs as scaffolding** outside generated files for now (Problem 6).
7. **Adopt typed-object mapper (Option A)** — no generator changes needed; update
   `NativeJunctionViewClient` to own `.toByteArray()` / `parseFrom()` calls (Problem 7).
8. **Always throw on `UNRECOGNIZED`** — update `KotlinGenerator.kt` to emit a throwing `else`
   branch in all enum `when` blocks; update the text-generation reference
   `NativeModelMapper.kt` accordingly (Problem 8).

