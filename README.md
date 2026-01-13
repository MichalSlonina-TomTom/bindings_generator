# Protobuf Bindings Generator

Automatically generates C++ `protobuf_helpers` and Kotlin `NativeModelMapper` files from `.proto` definitions.

## Features

- ✅ Parses `.proto` files using `protoc` descriptors
- ✅ Generates C++ conversion helpers (`.hpp` and `.cpp`)
- ✅ Generates Kotlin extension functions for mapping
- ✅ Supports nested messages and enums
- ✅ Handles repeated fields, optional fields, and primitives
- ✅ Type-safe conversions between proto and native models

## Prerequisites

- JDK 11 or higher
- `protoc` (Protocol Buffers compiler) installed and in PATH
- Gradle 7.0+

## Building

```bash
cd /tmp/bindings_generator
./gradlew build
```

## Usage

### Basic Usage

```bash
./gradlew run --args="-p path/to/your.proto -o output/directory"
```

### With Include Directories

```bash
./gradlew run --args="-p path/to/your.proto -o output/directory -I /path/to/imports -I /another/path"
```

### Command Line Options

| Option | Short | Description | Required | Default |
|--------|-------|-------------|----------|---------|
| `--protoFile` | `-p` | Path to the `.proto` file | Yes | - |
| `--outputDir` | `-o` | Output directory for generated files | Yes | - |
| `--includeDirs` | `-I` | Include directories (can specify multiple) | No | - |
| `--cppOutput` | `-c` | Generate C++ files | No | true |
| `--kotlinOutput` | `-k` | Generate Kotlin files | No | true |
| `--protocPath` | - | Path to protoc binary | No | protoc |
| `--verbose` | `-v` | Verbose output | No | false |

### Example

Generate bindings for the audio_instruction.proto file:

```bash
./gradlew run --args="-p /Users/slonina/repo/go-sdk-android/native/src/commonMain/proto/navigation-verbal-message-generation-binding/audio_instruction.proto \
  -o /tmp/generated \
  -I /Users/slonina/repo/go-sdk-android/native/src/commonMain/proto/navigation-verbal-message-generation-binding \
  -v"
```

## Output

### C++ Files

The generator creates two C++ files:

1. **`<proto_name>_protobuf_helpers.hpp`** - Header with function declarations
2. **`<proto_name>_protobuf_helpers.cpp`** - Implementation with conversion logic

Example generated functions:

```cpp
namespace protobuf_helpers {
    // Enum conversions
    NativeEnum toNative(const ProtoEnum& proto);
    ProtoEnum toProto(const NativeEnum& native);
    
    // Message conversions
    NativeMessage toNative(const ProtoMessage& proto);
    ProtoMessage toProto(const NativeMessage& native);
}
```

### Kotlin Files

The generator creates a **`NativeModelMapper.kt`** file with extension functions:

```kotlin
// Enum extensions
fun NativeEnum.toProto(): ProtoEnum
fun ProtoEnum.toNative(): NativeEnum

// Message extensions
fun NativeMessage.toProto(): ProtoMessage
fun ProtoMessage.toNative(): NativeMessage
```

## How It Works

1. **Parse**: Uses `protoc --descriptor_set_out` to generate a binary descriptor
2. **Analyze**: Parses the descriptor to extract messages, enums, and fields
3. **Generate**: Creates conversion functions based on field types:
   - Primitives: Direct mapping
   - Enums: Type-safe conversion with error handling
   - Messages: Recursive conversion
   - Repeated fields: Collection mapping
   - Optional fields: Null-safe handling

## Integration with Your Build

You can integrate this into your Gradle build:

```kotlin
// build.gradle.kts
tasks.register<JavaExec>("generateBindings") {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.tomtom.sdk.tools.bindingsgenerator.MainKt")
    args = listOf(
        "-p", "path/to/your.proto",
        "-o", "build/generated/bindings"
    )
}

// Make compilation depend on generation
tasks.named("compileKotlin") {
    dependsOn("generateBindings")
}
```

## Customization

You can customize the generators by modifying:

- **`CppGenerator.kt`** - C++ code generation logic
- **`KotlinGenerator.kt`** - Kotlin code generation logic
- **`ProtoParser.kt`** - Proto file parsing logic

## Troubleshooting

### "protoc not found"

Install Protocol Buffers compiler:

```bash
# macOS
brew install protobuf

# Linux
apt-get install protobuf-compiler

# Or specify custom path
./gradlew run --args="--protocPath=/path/to/protoc ..."
```

### "Could not find descriptor"

Make sure all import paths are included with `-I` flags.

## License

Copyright (C) 2024 TomTom NV. All rights reserved.

