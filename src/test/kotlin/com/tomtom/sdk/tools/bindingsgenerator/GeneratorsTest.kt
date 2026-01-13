package com.tomtom.sdk.tools.bindingsgenerator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
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
        assertTrue(content.contains("toNative"))
        assertTrue(content.contains("toProto"))

        // Should have copyright
        assertTrue(content.contains("Copyright"))
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
        assertTrue(content.contains("toNative"))
        assertTrue(content.contains("toProto"))

        // Should have copyright
        assertTrue(content.contains("Copyright"))
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
        assertTrue(content.contains("Copyright"))

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
    }
}

