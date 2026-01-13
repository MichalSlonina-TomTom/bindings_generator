package com.tomtom.sdk.tools.bindingsgenerator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Comprehensive tests using real proto files from the project.
 *
 * NOTE: These tests require `protoc` to be installed and available in PATH.
 * To run these tests:
 *   macOS: brew install protobuf
 *   Linux: apt-get install protobuf-compiler
 *
 * To skip these tests: ./gradlew test -x integrationTest
 */
class RealProtoFilesTest {

    @TempDir
    lateinit var tempDir: File

    private fun isProtocAvailable(): Boolean {
        return try {
            val process = ProcessBuilder("protoc", "--version").start()
            process.waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }

    @Test
    fun `test with real audio_instruction proto`() {
        if (!isProtocAvailable()) {
            println("⚠️  Skipping test: protoc not installed")
            return
        }

        // Given: The real audio_instruction.proto file
        val protoFile = File(javaClass.getResource("/proto/audio_instruction.proto")!!.file)
        val protoDir = protoFile.parentFile
        assertTrue(protoFile.exists(), "audio_instruction.proto should exist")

        // When: We parse it
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // Then: Should parse successfully
        assertEquals("com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos",
            parsedFile.packageName)
        assertTrue(parsedFile.messages.isNotEmpty(), "Should have messages")
        assertTrue(parsedFile.enums.isNotEmpty(), "Should have enums")

        println("✓ Parsed ${parsedFile.messages.size} messages and ${parsedFile.enums.size} enums")

        // And: Generate files
        val cppGenerator = CppGenerator()
        val headerFile = File(tempDir, "audio_instruction_protobuf_helpers.hpp")
        val implFile = File(tempDir, "audio_instruction_protobuf_helpers.cpp")

        cppGenerator.generateHeader(parsedFile, headerFile)
        cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

        assertTrue(headerFile.exists())
        assertTrue(implFile.exists())
        println("✓ Generated C++ files")

        // And: Generate Kotlin
        val kotlinGenerator = KotlinGenerator()
        kotlinGenerator.generateMapper(parsedFile, tempDir)

        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }
        assertTrue(kotlinFile != null, "Should generate NativeModelMapper.kt")
        println("✓ Generated Kotlin file")
    }

    @Test
    fun `test with real text_generation proto`() {
        if (!isProtocAvailable()) {
            println("⚠️  Skipping test: protoc not installed")
            return
        }

        // Given: The real text_generation.proto file
        val protoFile = File(javaClass.getResource("/proto/text_generation.proto")!!.file)
        val protoDir = protoFile.parentFile
        assertTrue(protoFile.exists(), "text_generation.proto should exist")

        // When: We parse and generate
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // Then: Should work
        assertTrue(parsedFile.messages.isNotEmpty() || parsedFile.enums.isNotEmpty())
        println("✓ Parsed text_generation.proto: ${parsedFile.messages.size} messages, ${parsedFile.enums.size} enums")
    }

    @Test
    fun `test with real language proto`() {
        if (!isProtocAvailable()) {
            println("⚠️  Skipping test: protoc not installed")
            return
        }

        // Given: The real language.proto file
        val protoFile = File(javaClass.getResource("/proto/language.proto")!!.file)
        val protoDir = protoFile.parentFile
        assertTrue(protoFile.exists(), "language.proto should exist")

        // When: We parse it
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // Then: Should parse the Language enum
        assertTrue(parsedFile.enums.isNotEmpty(), "Should have Language enum")
        val languageEnum = parsedFile.enums.find { it.name == "Language" }
        assertTrue(languageEnum != null, "Should find Language enum")

        println("✓ Parsed Language enum with ${languageEnum!!.values.size} values")
    }

    @Test
    fun `verify generated files structure matches expected`() {
        if (!isProtocAvailable()) {
            println("⚠️  Skipping test: protoc not installed")
            return
        }

        // Given: All proto files
        val protoFiles = listOf("language.proto", "audio_instruction.proto", "text_generation.proto")

        for (protoFileName in protoFiles) {
            val protoFile = File(javaClass.getResource("/proto/$protoFileName")!!.file)
            val protoDir = protoFile.parentFile

            // When: We parse and generate
            val parser = ProtoParser()
            val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

            // Then: Generate all outputs
            val cppGenerator = CppGenerator()
            val headerFile = File(tempDir, "$protoFileName.hpp")
            val implFile = File(tempDir, "$protoFileName.cpp")
            cppGenerator.generateHeader(parsedFile, headerFile)
            cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

            val kotlinGenerator = KotlinGenerator()
            val kotlinDir = File(tempDir, protoFileName.removeSuffix(".proto"))
            kotlinDir.mkdirs()
            kotlinGenerator.generateMapper(parsedFile, kotlinDir)

            // Verify all files created
            assertTrue(headerFile.exists(), "Header should exist for $protoFileName")
            assertTrue(implFile.exists(), "Implementation should exist for $protoFileName")

            println("✓ Generated all files for $protoFileName")
        }
    }
}

/**
 * Tests that work without protoc by using manually created test data
 */
class GeneratorOutputStructureTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `test C++ generator output structure`() {
        // Given: Manually created parsed data (doesn't need protoc)
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = listOf(
                ParsedMessage(
                    name = "TestMessage",
                    fullName = "com.test.TestMessage",
                    fields = listOf(
                        ParsedField("name", "name", "string", 1),
                        ParsedField("count", "count", "int32", 2)
                    )
                )
            ),
            enums = listOf(
                ParsedEnum(
                    name = "TestEnum",
                    fullName = "com.test.TestEnum",
                    values = listOf(
                        ParsedEnumValue("kValueOne", 0),
                        ParsedEnumValue("kValueTwo", 1)
                    )
                )
            )
        )

        // When: We generate C++ files
        val cppGenerator = CppGenerator()
        val headerFile = File(tempDir, "test.hpp")
        val implFile = File(tempDir, "test.cpp")

        cppGenerator.generateHeader(parsedFile, headerFile)
        cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

        // Then: Verify structure
        assertTrue(headerFile.exists())
        assertTrue(implFile.exists())

        val headerContent = headerFile.readText()
        val implContent = implFile.readText()

        // Header checks
        assertTrue(headerContent.contains("#ifndef"), "Should have include guard start")
        assertTrue(headerContent.contains("#define"), "Should define include guard")
        assertTrue(headerContent.contains("#endif"), "Should close include guard")
        assertTrue(headerContent.contains("namespace protobuf_helpers"), "Should have namespace")
        assertTrue(headerContent.contains("TestMessage"), "Should reference TestMessage")
        assertTrue(headerContent.contains("TestEnum"), "Should reference TestEnum")
        assertTrue(headerContent.contains("toNative"), "Should declare toNative")
        assertTrue(headerContent.contains("toProto"), "Should declare toProto")

        // Implementation checks
        assertTrue(implContent.contains("#include \"test.hpp\""), "Should include header")
        assertTrue(implContent.contains("namespace protobuf_helpers"), "Should have namespace")
        assertTrue(implContent.contains("toNative"), "Should implement toNative")
        assertTrue(implContent.contains("toProto"), "Should implement toProto")

        println("✓ C++ generator produces correct structure")
    }

    @Test
    fun `test Kotlin generator output structure`() {
        // Given: Manually created parsed data
        val parsedFile = ParsedProtoFile(
            packageName = "com.test.example",
            protoPackage = "com.test.example",
            messages = listOf(
                ParsedMessage(
                    name = "TestMessage",
                    fullName = "com.test.example.TestMessage",
                    fields = listOf(
                        ParsedField("value", "value", "string", 1)
                    )
                )
            ),
            enums = listOf(
                ParsedEnum(
                    name = "TestEnum",
                    fullName = "com.test.example.TestEnum",
                    values = listOf(
                        ParsedEnumValue("kValueOne", 0)
                    )
                )
            )
        )

        // When: We generate Kotlin file
        val kotlinGenerator = KotlinGenerator()
        kotlinGenerator.generateMapper(parsedFile, tempDir)

        // Then: Verify structure
        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }
        assertTrue(kotlinFile != null, "Should generate NativeModelMapper.kt")

        val content = kotlinFile!!.readText()

        assertTrue(content.contains("package com.test.example"), "Should have package declaration")
        assertTrue(content.contains("fun"), "Should have functions")
        assertTrue(content.contains("toProto"), "Should have toProto functions")
        assertTrue(content.contains("toNative"), "Should have toNative functions")
        assertTrue(content.contains("TestMessage") || content.contains("TestEnum"),
            "Should reference types")

        println("✓ Kotlin generator produces correct structure")
    }

    @Test
    fun `test copyright and auto-generated notices`() {
        // Given: Any parsed data
        val parsedFile = ParsedProtoFile(
            packageName = "com.test",
            protoPackage = "com.test",
            messages = emptyList(),
            enums = listOf(
                ParsedEnum("TestEnum", "com.test.TestEnum",
                    listOf(ParsedEnumValue("kValue", 0)))
            )
        )

        // When: We generate all file types
        val cppGenerator = CppGenerator()
        val headerFile = File(tempDir, "test.hpp")
        val implFile = File(tempDir, "test.cpp")
        cppGenerator.generateHeader(parsedFile, headerFile)
        cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

        val kotlinGenerator = KotlinGenerator()
        kotlinGenerator.generateMapper(parsedFile, tempDir)
        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }!!

        // Then: All should have copyright and auto-generated notice
        for (file in listOf(headerFile, implFile, kotlinFile)) {
            val content = file.readText()
            assertTrue(content.contains("Copyright") || content.contains("©"),
                "${file.name} should have copyright")
            assertTrue(content.contains("AUTO-GENERATED") || content.contains("auto-generated"),
                "${file.name} should have auto-generated notice")
        }

        println("✓ All generated files have proper headers")
    }
}

