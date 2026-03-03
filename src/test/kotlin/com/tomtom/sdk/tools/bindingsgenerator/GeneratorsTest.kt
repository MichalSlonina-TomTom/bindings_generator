package com.tomtom.sdk.tools.bindingsgenerator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit tests for CppGenerator
 */
class CppGeneratorTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `test generateHeader creates valid C++ header`() {
        // Given: A parsed proto file
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = listOf(
                ParsedMessage(
                    name = "Person",
                    fullName = "com.test.Person",
                    fields = listOf(
                        ParsedField(
                            name = "name",
                            protoName = "name",
                            type = "string",
                            number = 1
                        )
                    )
                )
            ),
            enums = emptyList()
        )

        // When: We generate the header
        val generator = CppGenerator()
        val headerFile = File(tempDir, "test.hpp")
        generator.generateHeader(parsedFile, headerFile)

        // Then: Should create a valid header
        assertTrue(headerFile.exists())
        val content = headerFile.readText()

        // Should have include guards
        assertTrue(content.contains("#ifndef"))
        assertTrue(content.contains("#define"))
        assertTrue(content.contains("#endif"))

        // Should have namespace
        assertTrue(content.contains("namespace protobuf_helpers"))

        // Should have conversion functions
        assertTrue(content.contains("ToNative"))
        assertTrue(content.contains("ToProto"))

        // Should have copyright
        assertTrue(content.contains("TomTom"))
    }

    @Test
    fun `test generateImplementation creates valid C++ implementation`() {
        // Given: A parsed proto file
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = emptyList(),
            enums = listOf(
                ParsedEnum(
                    name = "Status",
                    fullName = "com.test.Status",
                    values = listOf(
                        ParsedEnumValue("kStatusActive", 0),
                        ParsedEnumValue("kStatusInactive", 1)
                    )
                )
            )
        )

        // When: We generate the implementation
        val generator = CppGenerator()
        val headerFile = File(tempDir, "test.hpp")
        val implFile = File(tempDir, "test.cpp")
        generator.generateHeader(parsedFile, headerFile)
        generator.generateImplementation(parsedFile, headerFile, implFile)

        // Then: Should create a valid implementation
        assertTrue(implFile.exists())
        val content = implFile.readText()

        // Should include the header
        assertTrue(content.contains("#include"))
        assertTrue(content.contains("test.hpp"))

        // Should have namespace
        assertTrue(content.contains("namespace protobuf_helpers"))

        // Should have function implementations
        assertTrue(content.contains("ToNative"))
        assertTrue(content.contains("ToProto"))

        // Should have copyright
        assertTrue(content.contains("TomTom"))
    }

    @Test
    fun `test header has unique include guard`() {
        // Given: Two different proto files
        val proto1 = ParsedProtoFile(
            packageName = "com.test.one",
            protoPackage = "com.test.one",
            messages = emptyList(),
            enums = emptyList()
        )

        val proto2 = ParsedProtoFile(
            packageName = "com.test.two",
            protoPackage = "com.test.two",
            messages = emptyList(),
            enums = emptyList()
        )

        // When: We generate headers for both
        val generator = CppGenerator()
        val header1 = File(tempDir, "test1.hpp")
        val header2 = File(tempDir, "test2.hpp")
        generator.generateHeader(proto1, header1)
        generator.generateHeader(proto2, header2)

        // Then: They should have different include guards
        val content1 = header1.readText()
        val content2 = header2.readText()

        val guard1 = content1.lines().find { it.contains("#ifndef") }
        val guard2 = content2.lines().find { it.contains("#ifndef") }

        assertTrue(guard1 != guard2 || content1.contains("COM_TEST_ONE") && content2.contains("COM_TEST_TWO"),
            "Include guards should be unique for different packages")
    }

    @Test
    fun `test generated code handles nested messages`() {
        // Given: A proto with nested message
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = listOf(
                ParsedMessage(
                    name = "Outer",
                    fullName = "com.test.Outer",
                    fields = emptyList(),
                    nestedMessages = listOf(
                        ParsedMessage(
                            name = "Inner",
                            fullName = "com.test.Outer.Inner",
                            fields = listOf(
                                ParsedField("value", "value", "string", 1)
                            )
                        )
                    )
                )
            ),
            enums = emptyList()
        )

        // When: We generate code
        val generator = CppGenerator()
        val headerFile = File(tempDir, "nested.hpp")
        generator.generateHeader(parsedFile, headerFile)

        // Then: Should generate conversions for nested types
        val content = headerFile.readText()
        assertTrue(content.contains("Inner"))
    }
}

/**
 * Unit tests for KotlinGenerator
 */
class KotlinGeneratorTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `test generateMapper creates valid Kotlin file`() {
        // Given: A parsed proto file with enum
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = emptyList(),
            enums = listOf(
                ParsedEnum(
                    name = "Status",
                    fullName = "com.test.Status",
                    values = listOf(
                        ParsedEnumValue("kStatusActive", 0),
                        ParsedEnumValue("kStatusInactive", 1)
                    )
                )
            )
        )

        // When: We generate Kotlin mapper
        val generator = KotlinGenerator()
        generator.generateMapper(parsedFile, tempDir)

        // Then: Should create NativeModelMapper.kt
        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }
        assertTrue(kotlinFile != null, "Should generate NativeModelMapper.kt")

        val content = kotlinFile!!.readText()

        // Should have package declaration
        assertTrue(content.contains("package"))

        // Should have extension functions
        assertTrue(content.contains("fun"))
        assertTrue(content.contains("toProto"))
        assertTrue(content.contains("toNative"))

        // Should have copyright
        assertTrue(content.contains("TomTom"))

        // Should have suppress annotation
        assertTrue(content.contains("@Suppress") || content.contains("Suppress"))
    }

    @Test
    fun `test Kotlin generator creates extension for messages`() {
        // Given: A proto with a message
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = listOf(
                ParsedMessage(
                    name = "Person",
                    fullName = "com.test.Person",
                    fields = listOf(
                        ParsedField("name", "name", "string", 1),
                        ParsedField("age", "age", "int32", 2)
                    )
                )
            ),
            enums = emptyList()
        )

        // When: We generate Kotlin
        val generator = KotlinGenerator()
        generator.generateMapper(parsedFile, tempDir)

        // Then: Should create extensions for Person
        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }!!
        val content = kotlinFile.readText()

        // Should have Person extensions
        assertTrue(content.contains("Person"))
        assertTrue(content.contains("toProto"))
        assertTrue(content.contains("toNative"))
    }

    @Test
    fun `test Kotlin generator handles repeated fields`() {
        // Given: A message with repeated field
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = listOf(
                ParsedMessage(
                    name = "Tags",
                    fullName = "com.test.Tags",
                    fields = listOf(
                        ParsedField(
                            name = "values",
                            protoName = "values",
                            type = "string",
                            number = 1,
                            isRepeated = true
                        )
                    )
                )
            ),
            enums = emptyList()
        )

        // When: We generate Kotlin
        val generator = KotlinGenerator()
        generator.generateMapper(parsedFile, tempDir)

        // Then: Should handle repeated field
        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }!!
        val content = kotlinFile.readText()

        // Should use addAll or map for repeated fields
        assertTrue(content.contains("addAll") || content.contains("map"))
    }

    @Test
    fun `test Kotlin generator handles enum fields`() {
        // Given: A message with enum field
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = listOf(
                ParsedMessage(
                    name = "Record",
                    fullName = "com.test.Record",
                    fields = listOf(
                        ParsedField(
                            name = "status",
                            protoName = "status",
                            type = "Status",
                            number = 1,
                            isEnum = true
                        )
                    )
                )
            ),
            enums = listOf(
                ParsedEnum(
                    name = "Status",
                    fullName = "com.test.Status",
                    values = listOf(ParsedEnumValue("kStatusActive", 0))
                )
            )
        )

        // When: We generate Kotlin
        val generator = KotlinGenerator()
        generator.generateMapper(parsedFile, tempDir)

        // Then: Should call toProto/toNative on enum field
        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }!!
        val content = kotlinFile.readText()

        assertTrue(content.contains("status"))
        assertTrue(content.contains("toProto") || content.contains("toNative"))
    }

    @Test
    fun `test Kotlin generator creates when expressions for enums`() {
        // Given: An enum
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = emptyList(),
            enums = listOf(
                ParsedEnum(
                    name = "Color",
                    fullName = "com.test.Color",
                    values = listOf(
                        ParsedEnumValue("kColorRed", 0),
                        ParsedEnumValue("kColorBlue", 1),
                        ParsedEnumValue("kColorGreen", 2)
                    )
                )
            )
        )

        // When: We generate Kotlin
        val generator = KotlinGenerator()
        generator.generateMapper(parsedFile, tempDir)

        // Then: Should use when expression for enum mapping
        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }!!
        val content = kotlinFile.readText()

        assertTrue(content.contains("when"))
        assertTrue(content.contains("kColorRed") || content.contains("RED"))
        assertTrue(content.contains("kColorBlue") || content.contains("BLUE"))
        assertTrue(content.contains("kColorGreen") || content.contains("GREEN"))
        // Should throw on unrecognised values, not silently fall back
        assertTrue(content.contains("throw"), "Should throw on unrecognised enum values")
        assertTrue(content.contains("IllegalArgumentException"), "Should throw IllegalArgumentException")
    }

    @Test
    fun `test Kotlin generator throws on unrecognised enum values`() {
        // Given: An enum
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = emptyList(),
            enums = listOf(
                ParsedEnum(
                    name = "Status",
                    fullName = "com.test.Status",
                    values = listOf(
                        ParsedEnumValue("kStatusActive", 0),
                        ParsedEnumValue("kStatusInactive", 1)
                    )
                )
            )
        )

        // When: We generate Kotlin
        val generator = KotlinGenerator()
        generator.generateMapper(parsedFile, tempDir)

        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }!!
        val content = kotlinFile.readText()

        // Then: Both toProto and toNative should throw, not silently fall back
        assertTrue(content.contains("throw"), "else branch must throw")
        assertTrue(content.contains("IllegalArgumentException"), "must throw IllegalArgumentException")
        // Must NOT have a silent fallback to a default enum value
        assertFalse(
            content.contains("else -> Status.") || content.contains("else -> com.test.Status."),
            "Must not silently fall back to a default enum value"
        )
    }
}

/**
 * Unit tests for GeneratorConfig behaviour in CppGenerator
 */
class GeneratorConfigTest {

    @TempDir
    lateinit var tempDir: File

    private val simpleParsedFile = ParsedProtoFile(
        packageName = "com.test",
        protoPackage = "com.test",
        messages = emptyList(),
        enums = listOf(
            ParsedEnum("Status", "com.test.Status", listOf(ParsedEnumValue("kStatusOk", 0)))
        )
    )

    @Test
    fun `default config emits ifndef include guard`() {
        val generator = CppGenerator()
        val header = File(tempDir, "out.hpp")
        generator.generateHeader(simpleParsedFile, header)
        val content = header.readText()
        assertTrue(content.contains("#ifndef OUT_HPP"), "Should have #ifndef guard")
        assertTrue(content.contains("#define OUT_HPP"), "Should have #define")
        assertTrue(content.contains("#endif // OUT_HPP"), "Should have #endif")
        assertFalse(content.contains("#pragma once"), "Should not have #pragma once by default")
    }

    @Test
    fun `pragma once config emits pragma once instead of ifndef guard`() {
        val generator = CppGenerator(GeneratorConfig(usePragmaOnce = true))
        val header = File(tempDir, "out.hpp")
        generator.generateHeader(simpleParsedFile, header)
        val content = header.readText()
        assertTrue(content.contains("#pragma once"), "Should have #pragma once")
        assertFalse(content.contains("#ifndef"), "Should not have #ifndef")
        assertFalse(content.contains("#endif"), "Should not have #endif")
    }

    @Test
    fun `default config emits protobuf_helpers namespace`() {
        val generator = CppGenerator()
        val header = File(tempDir, "out.hpp")
        generator.generateHeader(simpleParsedFile, header)
        assertTrue(header.readText().contains("namespace protobuf_helpers {"))
    }

    @Test
    fun `nested namespaces config emits multiple nested namespace blocks`() {
        val config = GeneratorConfig(namespaces = listOf("tomtom", "sdk", "bindings", "internal"))
        val generator = CppGenerator(config)
        val header = File(tempDir, "out.hpp")
        val impl = File(tempDir, "out.cpp")
        generator.generateHeader(simpleParsedFile, header)
        generator.generateImplementation(simpleParsedFile, header, impl)

        listOf(header, impl).forEach { file ->
            val content = file.readText()
            assertTrue(content.contains("namespace tomtom {"), "${file.name} should open tomtom namespace")
            assertTrue(content.contains("namespace sdk {"), "${file.name} should open sdk namespace")
            assertTrue(content.contains("namespace bindings {"), "${file.name} should open bindings namespace")
            assertTrue(content.contains("namespace internal {"), "${file.name} should open internal namespace")
            assertTrue(content.contains("}  // namespace tomtom"), "${file.name} should close tomtom namespace")
            assertTrue(content.contains("}  // namespace internal"), "${file.name} should close internal namespace")
        }
    }

    @Test
    fun `generator emits PascalCase ToNative and ToProto function names`() {
        val generator = CppGenerator()
        val header = File(tempDir, "out.hpp")
        val impl = File(tempDir, "out.cpp")
        generator.generateHeader(simpleParsedFile, header)
        generator.generateImplementation(simpleParsedFile, header, impl)

        listOf(header, impl).forEach { file ->
            val content = file.readText()
            assertTrue(content.contains("ToNative"), "${file.name} should use PascalCase ToNative")
            assertTrue(content.contains("ToProto"), "${file.name} should use PascalCase ToProto")
            assertFalse(content.contains(" toNative"), "${file.name} must not use camelCase toNative")
            assertFalse(content.contains(" toProto"), "${file.name} must not use camelCase toProto")
        }
    }

    @Test
    fun `extra includes appear in generated header`() {
        val config = GeneratorConfig(extraIncludes = listOf(
            "#include <mylib/native.hpp>",
            "#include \"my_proto.pb.h\""
        ))
        val generator = CppGenerator(config)
        val header = File(tempDir, "out.hpp")
        generator.generateHeader(simpleParsedFile, header)
        val content = header.readText()
        assertTrue(content.contains("#include <mylib/native.hpp>"), "Should have extra system include")
        assertTrue(content.contains("#include \"my_proto.pb.h\""), "Should have extra proto include")
    }

    @Test
    fun `cpp generator emits has_() guard for optional scalar fields`() {
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = listOf(
                ParsedMessage(
                    name = "RouteArc",
                    fullName = "com.test.RouteArc",
                    fields = listOf(
                        ParsedField("arcKey", "arc_key", "uint64", 1),
                        ParsedField(
                            name = "arrivalOffsetOnArcInCentimeters",
                            protoName = "arrival_offset_on_arc_in_centimeters",
                            type = "int32",
                            number = 3,
                            isOptional = true
                        )
                    )
                )
            ),
            enums = emptyList()
        )
        val generator = CppGenerator()
        val header = File(tempDir, "out.hpp")
        val impl = File(tempDir, "out.cpp")
        generator.generateHeader(parsedFile, header)
        generator.generateImplementation(parsedFile, header, impl)

        val implContent = impl.readText()
        assertTrue(implContent.contains("has_arrival_offset_on_arc_in_centimeters"),
            "ToNative should emit has_() guard for optional field")
        assertTrue(implContent.contains("has_value()"),
            "ToProto should emit has_value() check for optional field")
    }

    @Test
    fun `kotlin generator emits let wrapper for optional scalar fields`() {
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = listOf(
                ParsedMessage(
                    name = "RouteArc",
                    fullName = "com.test.RouteArc",
                    fields = listOf(
                        ParsedField("arcKey", "arc_key", "uint64", 1),
                        ParsedField(
                            name = "arrivalOffsetOnArcInCentimeters",
                            protoName = "arrival_offset_on_arc_in_centimeters",
                            type = "int32",
                            number = 3,
                            isOptional = true
                        )
                    )
                )
            ),
            enums = emptyList()
        )
        val generator = KotlinGenerator()
        generator.generateMapper(parsedFile, tempDir)
        val content = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }!!.readText()

        assertTrue(content.contains("?.let"), "toProto should use ?.let for optional field")
        assertTrue(content.contains("hasArrivalOffsetOnArcInCentimeters") ||
                   content.contains("has_arrival") || content.contains("null"),
            "toNative should handle optional field as nullable")
    }
}

