# 🎯 Bindings Generator - Complete Reference

## ✅ What You Have Now

A **fully functional** Kotlin-based code generator in `/tmp/bindings_generator/` that automatically creates:

1. **C++ Protobuf Helpers** (`.hpp` and `.cpp` files)
2. **Kotlin Native Model Mappers** (`.kt` files with extension functions)

## 📂 Project Files

```
/tmp/bindings_generator/
├── 📖 README.md              - Comprehensive documentation
├── 📋 PROJECT_SUMMARY.md     - Project overview with examples
├── 🚀 quickstart.sh          - Quick start guide (run this first!)
├── 🔧 generate.sh            - Convenience wrapper script
├── 📝 example.sh             - Example using your actual proto files
├── 🧪 test.sh                - Quick test with sample proto
│
├── ⚙️  build.gradle.kts       - Gradle build configuration
├── ⚙️  settings.gradle.kts    - Project settings
├── 🔨 gradlew                - Gradle wrapper (executable)
│
└── src/main/kotlin/com/tomtom/sdk/tools/bindingsgenerator/
    ├── Main.kt               - CLI entry point
    ├── ProtoParser.kt        - Parses proto using protoc descriptors
    ├── CppGenerator.kt       - Generates C++ conversion code
    └── KotlinGenerator.kt    - Generates Kotlin extension functions
```

## 🚀 Quick Start

### 1. Run the Quick Start Guide
```bash
cd /tmp/bindings_generator
./quickstart.sh
```

### 2. Test with a Sample Proto
```bash
./test.sh
```

### 3. Try with Your Actual Proto Files
```bash
./example.sh
```

## 📖 Usage

### Basic Command
```bash
./generate.sh -p path/to/file.proto -o output/directory
```

### With Import Paths
```bash
./generate.sh \
  -p path/to/file.proto \
  -o output/directory \
  -I path/to/imports1 \
  -I path/to/imports2 \
  -v
```

### Using Gradle Directly
```bash
./gradlew run --args="-p file.proto -o output -I imports -v"
```

## 🎛️ Command Line Options

| Option | Short | Type | Required | Default | Description |
|--------|-------|------|----------|---------|-------------|
| `--protoFile` | `-p` | String | ✅ Yes | - | Path to the `.proto` file to process |
| `--outputDir` | `-o` | String | ✅ Yes | - | Directory where generated files will be written |
| `--includeDirs` | `-I` | String[] | ❌ No | `[]` | Include directories for proto imports (can specify multiple) |
| `--cppOutput` | `-c` | Boolean | ❌ No | `true` | Generate C++ helper files |
| `--kotlinOutput` | `-k` | Boolean | ❌ No | `true` | Generate Kotlin mapper files |
| `--protocPath` | - | String | ❌ No | `protoc` | Path to protoc binary (if not in PATH) |
| `--verbose` | `-v` | Boolean | ❌ No | `false` | Enable verbose output |

## 📝 Examples

### Example 1: Generate Both C++ and Kotlin (Default)
```bash
./generate.sh \
  -p /Users/slonina/repo/go-sdk-android/native/src/commonMain/proto/navigation-verbal-message-generation-binding/audio_instruction.proto \
  -o /tmp/generated \
  -I /Users/slonina/repo/go-sdk-android/native/src/commonMain/proto/navigation-verbal-message-generation-binding \
  -v
```

**Output:**
- `audio_instruction_protobuf_helpers.hpp`
- `audio_instruction_protobuf_helpers.cpp`
- `NativeModelMapper.kt`

### Example 2: Generate C++ Only
```bash
./gradlew run --args="
  -p src/proto/my_message.proto
  -o build/generated
  -I src/proto
  --kotlinOutput false
  -v
"
```

### Example 3: Generate Kotlin Only
```bash
./gradlew run --args="
  -p src/proto/my_message.proto
  -o build/generated
  -I src/proto
  --cppOutput false
  -v
"
```

### Example 4: Custom protoc Path
```bash
./gradlew run --args="
  -p src/proto/my_message.proto
  -o build/generated
  --protocPath /usr/local/bin/protoc
  -v
"
```

### Example 5: Multiple Include Directories
```bash
./generate.sh \
  -p my_message.proto \
  -o output \
  -I /path/to/proto1 \
  -I /path/to/proto2 \
  -I /path/to/proto3 \
  -v
```

## 🔧 Integration Examples

### Gradle Build Integration

Add to your `build.gradle.kts`:

```kotlin
val generatedBindingsDir = layout.buildDirectory.dir("generated/bindings")

val generateBindings = tasks.register<JavaExec>("generateBindings") {
    group = "code generation"
    description = "Generate protobuf bindings from .proto files"
    
    // Point to the generator JAR
    classpath = files("/tmp/bindings_generator/build/libs/bindings-generator-1.0.0.jar")
    mainClass.set("com.tomtom.sdk.tools.bindingsgenerator.MainKt")
    
    // Configure arguments
    args(
        "--protoFile", "src/proto/audio_instruction.proto",
        "--outputDir", generatedBindingsDir.get().asFile.absolutePath,
        "--includeDirs", "src/proto",
        "--verbose"
    )
    
    // Declare outputs for up-to-date checking
    outputs.dir(generatedBindingsDir)
    
    // Declare inputs for change detection
    inputs.files(fileTree("src/proto") { include("**/*.proto") })
}

// Make Kotlin compilation depend on generation
tasks.named("compileKotlin") {
    dependsOn(generateBindings)
}

// Add generated sources to source sets
kotlin {
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(generatedBindingsDir)
        }
    }
}
```

### Generate for Multiple Proto Files

```kotlin
val protoFiles = listOf(
    "audio_instruction.proto",
    "text_generation.proto",
    "language.proto"
)

protoFiles.forEach { protoFile ->
    tasks.register<JavaExec>("generate${protoFile.removeSuffix(".proto").capitalize()}Bindings") {
        classpath = files("/tmp/bindings_generator/build/libs/bindings-generator-1.0.0.jar")
        mainClass.set("com.tomtom.sdk.tools.bindingsgenerator.MainKt")
        args(
            "-p", "src/proto/$protoFile",
            "-o", "build/generated/bindings",
            "-I", "src/proto"
        )
    }
}
```

## 🔍 What Gets Generated

### For This Proto Definition:

```protobuf
syntax = "proto3";
package com.example;

enum Status {
  kStatusUnknown = 0;
  kStatusActive = 1;
}

message Person {
  string name = 1;
  int32 age = 2;
  Status status = 3;
  repeated string tags = 4;
}
```

### C++ Header (`person_protobuf_helpers.hpp`):

```cpp
#ifndef PROTOBUF_HELPERS_HPP_COM_EXAMPLE
#define PROTOBUF_HELPERS_HPP_COM_EXAMPLE

namespace protobuf_helpers {
    // Enum conversions
    ::com::example::Status toNative(const ::com::example::Status& proto);
    ::com::example::Status toProto(const ::com::example::Status& native);
    
    // Message conversions
    ::com::example::Person toNative(const ::com::example::Person& proto);
    ::com::example::Person toProto(const ::com::example::Person& native);
}

#endif
```

### Kotlin Mapper (`NativeModelMapper.kt`):

```kotlin
package com.example

// Enum extensions
fun Status.toProto(): ProtoStatus = when (this) {
    Status.UNKNOWN -> ProtoStatus.kStatusUnknown
    Status.ACTIVE -> ProtoStatus.kStatusActive
}

fun ProtoStatus.toNative(): Status = when (this) {
    ProtoStatus.kStatusUnknown -> Status.UNKNOWN
    ProtoStatus.kStatusActive -> Status.ACTIVE
    ProtoStatus.UNRECOGNIZED -> throw IllegalArgumentException("...")
}

// Message extensions
fun Person.toProto(): ProtoPerson = person {
    name = this@toProto.name
    age = this@toProto.age
    status = this@toProto.status.toProto()
    tags.addAll(this@toProto.tags)
}

fun ProtoPerson.toNative(): Person = Person(
    name = name,
    age = age,
    status = status.toNative(),
    tags = tagsList
)
```

## 🛠️ Troubleshooting

### "protoc not found"
```bash
# macOS
brew install protobuf

# Ubuntu/Debian
sudo apt-get install protobuf-compiler

# Or specify custom path
./generate.sh -p file.proto -o output --protocPath /path/to/protoc
```

### "Could not find descriptor"
Make sure all import paths are included:
```bash
./generate.sh -p file.proto -o output -I /path/to/imports1 -I /path/to/imports2
```

### "JVM target compatibility"
Already fixed in the project - uses Java 17.

### See Detailed Errors
Add `-v` flag for verbose output:
```bash
./generate.sh -p file.proto -o output -v
```

## 📚 Additional Documentation

- **`README.md`** - Full user guide with detailed explanations
- **`PROJECT_SUMMARY.md`** - Project overview, architecture, and customization guide
- **Source Code** - Well-commented Kotlin code in `src/main/kotlin/`

## 🎓 Next Steps

1. **Test the generator**: Run `./test.sh` or `./example.sh`
2. **Generate for your protos**: Use `./generate.sh` with your actual proto files
3. **Integrate into build**: Add Gradle tasks to automate generation
4. **Customize if needed**: Modify generator classes to match your conventions
5. **Version control**: Consider adding generated code to `.gitignore` and generating on build

## 💡 Tips

- **Incremental builds**: Use Gradle's up-to-date checking for efficiency
- **Proto changes**: Regenerate whenever proto files change
- **Multiple files**: Create a script to process all protos at once
- **CI/CD**: Add generation step to your build pipeline
- **Review output**: Check generated files initially to ensure correctness

## 📞 Support

The generator is fully functional and ready to use. If you need to customize:

1. Check the source code comments
2. Review example generated output
3. Modify `CppGenerator.kt` or `KotlinGenerator.kt` for custom patterns
4. Test changes with `./test.sh`

---

**Status**: ✅ Ready to use  
**Location**: `/tmp/bindings_generator/`  
**Build**: ✅ Successful  
**Last Updated**: January 13, 2025

