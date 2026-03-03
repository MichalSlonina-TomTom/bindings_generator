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

**Required action:** Add a namespace configuration option (e.g. a list of namespace segments or a
single dotted string) that the generator uses to emit nested `namespace X {` / `} // namespace X`
blocks.  The default should remain `protobuf_helpers` to preserve backward compatibility with
text-generation.

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

**Required action:** Make the C++ function name style configurable (camelCase vs PascalCase), or
at minimum introduce a naming strategy that can be passed to `CppGenerator`.

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

**Required action:** Add a header-guard style option (`PRAGMA_ONCE` vs `IFNDEF_GUARD`) to
`CppGenerator`.

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

**Required action:** Support extra/user-supplied `#include` lines via a configuration option, so
callers can inject native SDK includes that the generator cannot discover on its own.

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

**Required action:** Decide whether the Kotlin mapper for junction-view-engine should mirror
text-generation (typed objects, no JNI byte boundary) or preserve the `ByteArray` boundary.  If
the latter, the generator needs a "JNI serialisation mode" flag.

---

## Problem 8 — Kotlin mapper uses `UNRECOGNIZED` as an error-throwing default

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

`KotlinGenerator.kt` always emits the silent fallback.

**Required action:** Add a generator option to emit a throwing `else` branch instead of a silent
fallback, controlled per-enum or globally.

---

## Problem 9 — Copyright year and symbol differ

Text-generation expected files use `© 2022 TomTom NV`.
Junction-view-engine expected files use `© 2024 TomTom NV`.
The generator emits `Copyright (C) 2022 TomTom NV` (no Unicode © symbol, hard-coded year).

**Required action:** Make the copyright year (and optionally the copyright symbol style)
configurable via a CLI option or config, defaulting to the current year or to the value already in
the generator.

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
7. **Document and defer** the `ByteArray` JNI mode and throwing-enum questions until the
   structural issues above are resolved (Problems 7–8).

