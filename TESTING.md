# Test Suite Documentation

## Overview

The bindings generator includes comprehensive unit and integration tests to ensure correctness.

## Test Categories

### 1. **Unit Tests (No Dependencies Required)**

These tests work without `protoc` installed by using manually created test data:

#### GeneratorOutputStructureTest
- ✅ `test C++ generator output structure` - Verifies C++ header and implementation structure
- ✅ `test Kotlin generator output structure` - Verifies Kotlin mapper structure  
- ✅ `test copyright and auto-generated notices` - Ensures all files have proper headers

#### CppGeneratorTest
- ✅ `test generateHeader creates valid C++ header` - Tests header generation
- ✅ `test generateImplementation creates valid C++ implementation` - Tests implementation generation
- ✅ `test header has unique include guard` - Verifies unique include guards
- ✅ `test generated code handles nested messages` - Tests nested type handling

#### KotlinGeneratorTest
- ✅ `test generateMapper creates valid Kotlin file` - Tests Kotlin file generation
- ✅ `test Kotlin generator creates extension for messages` - Tests message extensions
- ✅ `test Kotlin generator handles repeated fields` - Tests repeated field handling
- ✅ `test Kotlin generator handles enum fields` - Tests enum field handling
- ✅ `test Kotlin generator creates when expressions for enums` - Tests enum mapping

### 2. **Integration Tests (Require `protoc`)**

These tests parse actual proto files and verify complete generation:

⚠️ **Requirements**: These tests require `protoc` to be installed:
- macOS: `brew install protobuf`
- Linux: `apt-get install protobuf-compiler`

If `protoc` is not installed, these tests will be **automatically skipped**.

#### RealProtoFilesTest
- ⏭️  `test with real audio_instruction proto` - Tests with actual audio_instruction.proto
- ⏭️  `test with real text_generation proto` - Tests with actual text_generation.proto
- ⏭️  `test with real language proto` - Tests with actual language.proto
- ⏭️  `verify generated files structure matches expected` - Tests all proto files

#### BindingsGeneratorIntegrationTest
- ⏭️  `test audio_instruction proto generates expected files` - Full generation test
- ⏭️  `test text_generation proto generates expected files` - Full generation test
- ⏭️  `test language proto generates expected files` - Full generation test
- ⏭️  `test parser handles nested messages correctly` - Tests nested parsing
- ⏭️  `test parser handles repeated fields correctly` - Tests repeated field parsing
- ⏭️  `test parser handles enum fields correctly` - Tests enum field parsing
- ⏭️  `test C++ generator creates valid include guards` - Tests C++ guards
- ⏭️  `test C++ generator includes copyright header` - Tests copyright
- ⏭️  `test Kotlin generator creates valid package structure` - Tests package structure
- ⏭️  `test Kotlin generator creates extension functions` - Tests extensions

#### ProtoParserTest
- ⏭️  `test parseProtoFile with simple enum` - Tests enum parsing
- ⏭️  `test parseProtoFile with simple message` - Tests message parsing
- ⏭️  `test parseProtoFile with repeated fields` - Tests repeated fields
- ⏭️  `test parseProtoFile with nested message` - Tests nested messages
- ⏭️  `test parseProtoFile with nested enum` - Tests nested enums
- ⏭️  `test parseProtoFile identifies enum fields correctly` - Tests enum field detection
- ⏭️  `test parseProtoFile identifies message fields correctly` - Tests message field detection
- ⏭️  `test parseProtoFile handles imports` - Tests import handling

#### ExactOutputComparisonTest
- ⏭️  `test protobuf_helpers_hpp matches expected output` - Compares C++ header
- ⏭️  `test protobuf_helpers_cpp matches expected structure` - Compares C++ implementation
- ⏭️  `test NativeModelMapper_kt matches expected structure` - Compares Kotlin mapper
- ⏭️  `test generated files have consistent formatting` - Tests formatting
- ⏭️  `test enum conversion functions are generated correctly` - Tests enum conversions
- ⏭️  `test message conversion functions handle all field types` - Tests message conversions
- ⏭️  `test generated code compiles without syntax errors` - Tests syntax validity

## Running Tests

### Run All Tests (including protoc-dependent)
```bash
cd /tmp/bindings_generator
./gradlew test
```

### Run Only Unit Tests (no protoc needed)
```bash
./gradlew test --tests "GeneratorOutputStructureTest"
./gradlew test --tests "CppGeneratorTest"
./gradlew test --tests "KotlinGeneratorTest"
```

### Run Only Integration Tests (requires protoc)
```bash
./gradlew test --tests "RealProtoFilesTest"
./gradlew test --tests "BindingsGeneratorIntegrationTest"
```

### View Test Report
After running tests, open:
```
file:///tmp/bindings_generator/build/reports/tests/test/index.html
```

## Test Resources

### Proto Files (in src/test/resources/proto/)
- ✅ `language.proto` - Language enum definition (21 lines)
- ✅ `audio_instruction.proto` - Audio instruction messages (560 lines)
- ✅ `text_generation.proto` - Text generation messages (257 lines)

### Expected Output Files (in src/test/resources/expected/)
- ✅ `NativeModelMapper.kt` - Expected Kotlin mapper (640 lines)
- ✅ `protobuf_helpers.hpp` - Expected C++ header (93 lines)
- ✅ `protobuf_helpers.cpp` - Expected C++ implementation (882 lines)

## Test Coverage

The test suite covers:

✅ **Proto Parsing**
- Enums (simple and nested)
- Messages (simple and nested)
- All primitive types
- Repeated fields
- Optional fields
- Enum fields
- Message fields
- Import handling

✅ **C++ Generation**
- Header file structure
- Implementation file structure
- Include guards
- Namespaces
- Function declarations
- Function implementations
- Enum conversions
- Message conversions
- Nested type handling

✅ **Kotlin Generation**
- File structure
- Package declaration
- Extension functions
- Enum mapping with `when` expressions
- Message mapping with DSL builders
- Repeated field handling
- Optional field handling
- Null-safety

✅ **Code Quality**
- Copyright headers
- Auto-generated notices
- Consistent formatting
- Valid syntax (no compilation errors)

## Continuous Integration

To run tests in CI without `protoc`:

```yaml
# .github/workflows/test.yml
- name: Run Unit Tests
  run: |
    ./gradlew test --tests "GeneratorOutputStructureTest" \
                    --tests "CppGeneratorTest" \
                    --tests "KotlinGeneratorTest"
```

To run all tests in CI with `protoc`:

```yaml
# .github/workflows/test.yml
- name: Install protoc
  run: |
    brew install protobuf  # macOS
    # apt-get install protobuf-compiler  # Linux

- name: Run All Tests
  run: ./gradlew test
```

## Adding New Tests

### For New Proto Features
1. Add test proto file to `src/test/resources/proto/`
2. Create test in `RealProtoFilesTest`
3. Add expected output to `src/test/resources/expected/`

### For Generator Logic
1. Create test data manually (no protoc needed)
2. Add test to `CppGeneratorTest` or `KotlinGeneratorTest`
3. Verify structure, not exact output

## Test Statistics

- **Total Test Files**: 6
- **Total Tests**: ~40
- **Tests Requiring protoc**: ~28
- **Tests Without protoc**: ~12
- **Test Resources**: 6 files (2,453 lines total)

---

**Status**: ✅ Comprehensive test suite implemented  
**Coverage**: Parser, C++ Generator, Kotlin Generator, Integration  
**Note**: Some tests require `protoc` and will be skipped if not available

