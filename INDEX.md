# 📑 Bindings Generator - File Index
## 🎯 Quick Access
| Purpose | File | Description |
|---------|------|-------------|
| **Start Here** | `quickstart.sh` | Interactive quick start guide |
| **Documentation** | `COMPLETE_REFERENCE.md` | Complete usage reference |
| **Overview** | `PROJECT_SUMMARY.md` | Project architecture and examples |
| **Details** | `README.md` | Comprehensive user guide |
## 📁 All Files
### 📖 Documentation
- `INDEX.md` (this file) - File directory
- `COMPLETE_REFERENCE.md` - Complete command reference
- `PROJECT_SUMMARY.md` - Project overview
- `README.md` - User guide
### 🚀 Executable Scripts
- `quickstart.sh` - Interactive quick start
- `generate.sh` - Convenience wrapper for generation
- `example.sh` - Example using your actual proto files
- `test.sh` - Test with sample proto
- `gradlew` - Gradle wrapper
### ⚙️ Configuration
- `build.gradle.kts` - Gradle build configuration
- `settings.gradle.kts` - Project settings
- `gradle/wrapper/gradle-wrapper.properties` - Gradle wrapper config
- `gradle/wrapper/gradle-wrapper.jar` - Gradle wrapper JAR
### 💻 Source Code
Located in `src/main/kotlin/com/tomtom/sdk/tools/bindingsgenerator/`:
- `Main.kt` - Command-line interface
  - Argument parsing with kotlinx-cli
  - Orchestrates parsing and generation
- `ProtoParser.kt` - Proto file parser
  - Uses protoc to generate descriptors
  - Parses binary descriptor format
  - Extracts messages, enums, fields
- `CppGenerator.kt` - C++ code generator
  - Generates `.hpp` header files
  - Generates `.cpp` implementation files
  - Creates conversion functions
- `KotlinGenerator.kt` - Kotlin code generator
  - Uses KotlinPoet for code generation
  - Creates extension functions
  - Handles Kotlin DSL patterns
## 🎬 Getting Started
### Option 1: Interactive Quick Start
```bash
cd /tmp/bindings_generator
./quickstart.sh
```
### Option 2: Run Example
```bash
cd /tmp/bindings_generator
./example.sh
```
### Option 3: Manual Usage
```bash
cd /tmp/bindings_generator
./generate.sh -p your_file.proto -o output_dir -v
```
## 📚 Documentation Flow
1. **First Time?** → Start with `quickstart.sh`
2. **Need Examples?** → Check `COMPLETE_REFERENCE.md`
3. **Want Details?** → Read `README.md`
4. **Need Architecture?** → See `PROJECT_SUMMARY.md`
## 🔧 File Purposes
### Scripts (Executable)
**`quickstart.sh`**
- Checks prerequisites (Java, protoc)
- Shows usage examples
- Displays integration instructions
**`generate.sh`**
- Wrapper around `gradlew run`
- Simplifies common usage
- Passes arguments to Main.kt
**`example.sh`**
- Demonstrates generation with your actual proto files
- Uses audio_instruction.proto from your project
- Outputs to /tmp/generated_bindings
**`test.sh`**
- Creates sample proto file
- Generates bindings
- Shows preview of output
### Source Files
**`Main.kt`** (Entry point)
- Parses command-line arguments
- Validates inputs
- Calls parser and generators
- Reports results
**`ProtoParser.kt`** (Parser)
- Executes `protoc --descriptor_set_out`
- Parses DescriptorProtos
- Returns structured data (messages, enums, fields)
**`CppGenerator.kt`** (C++ Generator)
- `generateHeader()` - Creates .hpp file
- `generateImplementation()` - Creates .cpp file
- Handles enums, messages, nested types
**`KotlinGenerator.kt`** (Kotlin Generator)
- Uses KotlinPoet for type-safe generation
- Creates extension functions
- Handles Kotlin DSL builders
## 📦 Build Artifacts
After running `./gradlew build`:
```
build/
├── libs/
│   └── bindings-generator-1.0.0.jar  # Executable JAR
└── classes/
    └── kotlin/main/  # Compiled classes
```
## 🎯 Example Workflow
```bash
# 1. Navigate to project
cd /tmp/bindings_generator
# 2. Run quick start (first time)
./quickstart.sh
# 3. Test with sample
./test.sh
# 4. Generate for your proto
./generate.sh \
  -p /path/to/your.proto \
  -o /output/dir \
  -I /import/path \
  -v
# 5. Check output
ls -la /output/dir
```
## 🌟 Key Features
✅ **Uses protoc** - Reliable parsing via Protocol Buffers compiler  
✅ **Type-safe** - Generates compile-time safe conversions  
✅ **Kotlin-based** - Easy to modify and extend  
✅ **Well-documented** - Multiple levels of documentation  
✅ **Ready to use** - Pre-built and tested  
---
**Status**: ✅ Fully Functional  
**Location**: `/tmp/bindings_generator/`  
**Version**: 1.0.0  
**Last Updated**: January 13, 2025
