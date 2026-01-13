# Protobuf Bindings Generator - Project Summary

## ✅ What Was Created

A complete Kotlin-based code generator that automatically creates:

1. **C++ Protobuf Helpers** (`protobuf_helpers.hpp` and `.cpp`)
   - Type-safe conversion functions between native and proto types
   - Handles enums, messages, nested types, repeated fields, and optional fields

2. **Kotlin Native Model Mappers** (`NativeModelMapper.kt`)
   - Extension functions for `.toProto()` and `.toNative()` conversions
   - Uses Kotlin DSL builders for clean, idiomatic code
   - Null-safe handling of optional fields

## 📁 Project Structure

```
/tmp/bindings_generator/
├── build.gradle.kts           # Gradle build configuration
├── settings.gradle.kts        # Gradle settings
├── gradlew                    # Gradle wrapper script
├── gradle/
│   └── wrapper/              # Gradle wrapper files
├── src/main/kotlin/com/tomtom/sdk/tools/bindingsgenerator/
│   ├── Main.kt               # CLI entry point
│   ├── ProtoParser.kt        # Protobuf descriptor parser
│   ├── CppGenerator.kt       # C++ code generator
│   └── KotlinGenerator.kt    # Kotlin code generator
├── README.md                  # Comprehensive documentation
├── generate.sh               # Convenience script
├── example.sh                # Example usage with your proto files
└── test.sh                   # Quick test script
```

## 🚀 How It Works

### 1. **Parsing Phase**
- Uses `protoc --descriptor_set_out` to generate binary descriptors
- Parses descriptors using `com.google.protobuf:protobuf-java`
- Extracts messages, enums, fields, and type information

### 2. **Generation Phase**
- **C++ Generator**: Creates conversion functions in `protobuf_helpers` namespace
- **Kotlin Generator**: Uses KotlinPoet to generate type-safe extension functions

### 3. **Type Mapping**

| Proto Type | C++ Type | Kotlin Type |
|-----------|----------|-------------|
| `int32` | `int32_t` | `Int` |
| `int64` | `int64_t` | `Long` |
| `string` | `std::string` | `String` |
| `bool` | `bool` | `Boolean` |
| `repeated T` | `std::vector<T>` | `List<T>` |
| `optional T` | `std::optional<T>` | `T?` |
| `enum` | `enum class` | `enum class` |
| `message` | `struct/class` | `data class` |

## 🎯 Usage Examples

### Basic Usage

```bash
cd /tmp/bindings_generator

# Generate bindings for a proto file
./generate.sh -p path/to/your.proto -o output/dir -v
```

### With Your Project

```bash
# Generate bindings for audio_instruction.proto
./example.sh
```

### Command Line Options

```bash
./gradlew run --args="
  -p /path/to/file.proto      # Proto file to parse
  -o /output/directory         # Where to generate files
  -I /include/path1           # Proto import paths (multiple allowed)
  -I /include/path2
  -v                          # Verbose output
  --cppOutput true            # Generate C++ (default: true)
  --kotlinOutput true         # Generate Kotlin (default: true)
  --protocPath /path/protoc   # Custom protoc path (default: protoc)
"
```

## 📝 Example Generated Code

### C++ Output

```cpp
// protobuf_helpers.hpp
namespace protobuf_helpers {
    Status toNative(const ::com::test::Status& proto);
    ::com::test::Status toProto(const Status& native);
    
    Person toNative(const ::com::test::Person& proto);
    ::com::test::Person toProto(const Person& native);
}

// protobuf_helpers.cpp
Person toNative(const ::com::test::Person& proto) {
    Person native;
    native.name = proto.name();
    native.age = proto.age();
    native.status = toNative(proto.status());
    for (const auto& item : proto.hobbies()) {
        native.hobbies.push_back(item);
    }
    return native;
}
```

### Kotlin Output

```kotlin
// NativeModelMapper.kt
fun Status.toProto(): ProtoStatus = when (this) {
    Status.UNKNOWN -> ProtoStatus.kStatusUnknown
    Status.ACTIVE -> ProtoStatus.kStatusActive
    Status.INACTIVE -> ProtoStatus.kStatusInactive
}

fun Person.toProto(): ProtoPerson = person {
    name = this@toProto.name
    age = this@toProto.age
    status = this@toProto.status.toProto()
    hobbies.addAll(this@toProto.hobbies)
}

fun ProtoPerson.toNative(): Person = Person(
    name = name,
    age = age,
    status = status.toNative(),
    hobbies = hobbiesList
)
```

## 🔧 Integration with Your Build System

### Gradle Integration

Add to your `build.gradle.kts`:

```kotlin
val generateBindings = tasks.register<JavaExec>("generateBindings") {
    group = "code generation"
    description = "Generate protobuf bindings"
    
    classpath = files("/tmp/bindings_generator/build/libs/bindings-generator-1.0.0.jar")
    mainClass.set("com.tomtom.sdk.tools.bindingsgenerator.MainKt")
    
    args(
        "-p", "src/proto/audio_instruction.proto",
        "-o", "build/generated/bindings",
        "-I", "src/proto"
    )
}

tasks.named("compileKotlin") {
    dependsOn(generateBindings)
}
```

## 🧪 Testing

Run the included test:

```bash
cd /tmp/bindings_generator
./test.sh
```

This creates a simple test proto and generates bindings to verify everything works.

## 🎨 Customization

### Modify Naming Conventions

Edit `KotlinGenerator.kt` or `CppGenerator.kt`:

```kotlin
// Change how enum values are converted
private fun convertEnumValueToNative(protoValue: String): String {
    // Your custom logic here
    return protoValue.removePrefix("k").uppercase()
}
```

### Add Custom Type Mappings

Extend the `parseField()` method in `ProtoParser.kt` to handle custom types.

### Change Output Format

Modify the `appendMessageImplementations()` methods to generate different code styles.

## 📦 Dependencies

- **Kotlin**: 1.9.21
- **Protobuf Java**: 3.25.1
- **KotlinPoet**: 1.15.3 (for Kotlin code generation)
- **kotlinx-cli**: 0.3.6 (for command-line parsing)

## 🔒 Requirements

- JDK 17 or higher
- `protoc` (Protocol Buffers compiler) in PATH or specified via `--protocPath`
- Gradle 8.5+ (included via wrapper)

## 🚦 Next Steps

1. **Test with your proto files**:
   ```bash
   ./example.sh
   ```

2. **Integrate into your build**:
   - Add as a Gradle task
   - Run during pre-compilation phase

3. **Customize for your needs**:
   - Adjust type mappings
   - Modify naming conventions
   - Add custom validation

4. **Scale up**:
   - Process multiple proto files
   - Generate additional helper functions
   - Add documentation generation

## 💡 Benefits

✅ **Eliminates Manual Work**: No more hand-writing conversion boilerplate  
✅ **Type Safe**: Compile-time guarantees for conversions  
✅ **Consistent**: Same patterns across all proto definitions  
✅ **Maintainable**: Regenerate when proto changes  
✅ **Fast**: Leverages protoc for reliable parsing  

## 📞 Support

For issues or questions:
- Check the README.md for detailed documentation
- Review generated code to understand patterns
- Modify generators for custom requirements

---

**Created**: January 2026  
**Location**: `/tmp/bindings_generator/`  
**License**: Copyright (C) 2024 TomTom NV

