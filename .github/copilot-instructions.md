# Copilot Instructions for Protobuf Bindings Generator

## Project Overview

This is a **Kotlin/JVM tool** that automatically generates C++ `protobuf_helpers` bindings and Kotlin `NativeModelMapper` extension functions from `.proto` file definitions. It is used at TomTom to bridge between Android Kotlin code and native C++ code via Protocol Buffers.

## Tech Stack

- **Language:** Kotlin (JVM 17)
- **Build system:** Gradle with Kotlin DSL (`build.gradle.kts`)
- **Key dependencies:**
  - `com.google.protobuf:protobuf-java` and `protobuf-kotlin` (3.25.x) — parsing `.proto` descriptors
  - `com.squareup:kotlinpoet` — generating Kotlin source files
  - `kotlinx-cli` — CLI argument parsing for `Main.kt`
  - JUnit 5 + MockK — testing
- **External tool required:** `protoc` (Protocol Buffers compiler) must be installed and available on `PATH` (also searched at `/usr/local/bin/protoc`, `/usr/bin/protoc`, `/opt/homebrew/bin/protoc`)

## Project Structure

```
src/
  main/kotlin/com/tomtom/sdk/tools/bindingsgenerator/
    Main.kt              # CLI entry point; -p <proto> -o <output> [-I <include>]
    ProtoParser.kt       # Invokes protoc, parses descriptor, returns ParsedProtoFile
    CppGenerator.kt      # Generates .hpp and .cpp with protobuf_helpers namespace
    KotlinGenerator.kt   # Generates NativeModelMapper.kt using KotlinPoet
  test/kotlin/com/tomtom/sdk/tools/bindingsgenerator/
    ProtoParserTest.kt              # Unit tests for ProtoParser
    BindingsGeneratorIntegrationTest.kt  # Integration tests (require protoc)
    ExactOutputComparisonTest.kt    # Structural content tests (require protoc)
    GeneratorsTest.kt
    ComprehensiveIntegrationTest.kt
  test/resources/
    text-generation/
      proto/             # Sample .proto files (audio_instruction.proto, language.proto, text_generation.proto)
      expected/          # Reference hand-crafted output files to compare against
        protobuf_helpers.hpp
        protobuf_helpers.cpp
        NativeModelMapper.kt
    junction-view-engine/
      proto/             # junction_view_request.proto, junction_view_information.proto
      expected/          # Reference output for junction-view-engine
        protobuf_helpers.hpp
        protobuf_helpers.cpp
        NativeJunctionViewClient.kt  # hand-crafted; mapper functions still embedded here
```

## Core Data Model (`ProtoParser.kt`)

```kotlin
data class ParsedProtoFile(val packageName: String, val protoPackage: String, val messages: List<ParsedMessage>, val enums: List<ParsedEnum>)
data class ParsedMessage(val name: String, val fullName: String, val fields: List<ParsedField>, val nestedMessages: List<ParsedMessage>, val nestedEnums: List<ParsedEnum>)
data class ParsedField(val name: String, val protoName: String, val type: String, val number: Int, val isRepeated: Boolean, val isOptional: Boolean, val isEnum: Boolean, val isMessage: Boolean, val typeName: String)
data class ParsedEnum(val name: String, val fullName: String, val values: List<ParsedEnumValue>)
data class ParsedEnumValue(val name: String, val number: Int)
```

## Generated Output Conventions

### C++ (`CppGenerator.kt`)
- All functions live in the **`protobuf_helpers` namespace**
- Header has an `#ifndef`/`#define`/`#endif` include guard named after the file (e.g. `PROTOBUF_HELPERS_HPP`)
- Functions are named `toNative(...)` and `toProto(...)`
- Every generated file starts with the TomTom copyright header followed by `// AUTO-GENERATED FILE. DO NOT MODIFY.`
- Repeated fields use loops (range-based `for`)
- Brace counts must always balance in both `.hpp` and `.cpp`

### Kotlin (`KotlinGenerator.kt`)
- Generated file is always named **`NativeModelMapper.kt`**
- Uses KotlinPoet to produce idiomatic Kotlin
- Has `@file:Suppress("detekt:TooManyFunctions")` and a plain `@Suppress("detekt:TooManyFunctions")`
- Extension functions are named `.toProto()` and `.toNative()`
- Package name mirrors the proto package name

## Running Tests

```bash
./gradlew test
```

- Tests that require `protoc` use `Assumptions.assumeTrue(isProtocAvailable, ...)` — they are **skipped** (not failed) if `protoc` is not installed.
- Run a single test class: `./gradlew test --tests "com.tomtom.sdk.tools.bindingsgenerator.ProtoParserTest"`

## Running the Generator

```bash
./gradlew run --args="-p path/to/file.proto -o output/dir -I path/to/imports"
```

## Adding a New Feature

1. Update `ProtoParser.kt` if new proto constructs need to be parsed.
2. Update `CppGenerator.kt` and/or `KotlinGenerator.kt` to emit the new construct.
3. Add or update unit tests in the relevant `*Test.kt` file.
4. If the expected reference output changes, update the files under `src/test/resources/text-generation/expected/` or `src/test/resources/junction-view-engine/expected/`.

## Important Notes

- **Never commit generated files** — generated output goes to `build/` or a user-specified output directory.
- The `src/test/resources/*/expected/` files are **hand-crafted reference files**, not generated. They represent the desired final output.
- The `src/test/resources/*/proto/` directories contain **sample proto files** used by tests — treat them as fixtures.
- `protoc` is invoked at runtime (not at build time); the generated `.bin` descriptor is a temp file that is deleted after parsing.
- Copyright header uses `© 2022 TomTom NV` in reference expected files; generated files use `Copyright (C) 2022 TomTom NV`.
- Do not add trailing whitespace to any generated lines — `ExactOutputComparisonTest` validates this.

## Known Limitations & Planned Work

A detailed analysis of what needs to change before the generator can be applied to the
**junction-view-engine** use case is captured in:

> `docs/adr/20260303_adaptations_needed_for_junction_view_engine.md`

Key gaps identified there (do not implement without reading the full ADR):

| # | Area | Gap |
|---|------|-----|
| 1 | Kotlin | Mapper functions are still embedded in `NativeJunctionViewClient`; need extraction to `NativeModelMapper.kt` |
| 2 | C++ | Namespace is deep/nested (`tomtom::sdk::bindings::…`); generator hard-codes flat `protobuf_helpers` |
| 3 | C++ | Function names are PascalCase (`FromProto`/`ToProto`); generator hard-codes camelCase (`toNative`/`toProto`) |
| 4 | C++ | Header guard is `#pragma once`; generator always emits `#ifndef`/`#define`/`#endif` |
| 5 | C++ | Extra native SDK `#include` lines cannot currently be injected by the caller |
| 6 | C++ | Native helper structs (`NativeJunctionViewRequestParams`) are not derivable from proto; treat as hand-written scaffolding |
| 7 | Kotlin | JNI boundary uses `ByteArray` serialisation, not typed proto objects |
| 8 | Kotlin | Unrecognised enum values should throw, not silently fall back |
| 9 | Both | Copyright year/symbol is hard-coded (`Copyright (C) 2022`); junction-view-engine uses `© 2024` |
| 10 | Both | `optional` scalar fields need `has_*()` guards (C++) and nullable `?.let` wrappers (Kotlin) |
| 11 | Both | `oneof` fields are not recognised by `ProtoParser` or either generator |
| 12 | C++ | Nested enum type names must use protobuf's underscore-mangled form (`JunctionViewError_ErrorType`) |

The recommended implementation order is also documented in the ADR.

