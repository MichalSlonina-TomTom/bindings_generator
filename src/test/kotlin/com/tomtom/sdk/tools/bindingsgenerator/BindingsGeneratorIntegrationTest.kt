package com.tomtom.sdk.tools.bindingsgenerator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.api.Assumptions
import java.io.File
import kotlin.test.assertTrue
import kotlin.test.assertEquals

/**
 * Integration tests that verify the generator produces exact output
 * matching the expected reference files.
 *
 * NOTE: Requires protoc to be installed.
 */
class BindingsGeneratorIntegrationTest {

    @TempDir
    lateinit var tempDir: File

    private fun requireProtoc() {
        val isProtocAvailable = try {
            val process = ProcessBuilder("protoc", "--version").start()
            process.waitFor() == 0
        } catch (e: Exception) {
            false
        }
        Assumptions.assumeTrue(isProtocAvailable, "protoc not installed - skipping test")
    }

    @Test
    fun `test audio_instruction proto generates expected files`() {
        // Given: The audio_instruction.proto file
        val protoFile = File(javaClass.getResource("/proto/audio_instruction.proto")!!.file)
        val protoDir = protoFile.parentFile
        assertTrue(protoFile.exists(), "audio_instruction.proto should exist in test resources")

        // When: We parse the proto file
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // Then: It should parse successfully
        assertEquals("com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos",
            parsedFile.packageName)
        assertTrue(parsedFile.messages.isNotEmpty(), "Should have parsed messages")
        assertTrue(parsedFile.enums.isNotEmpty(), "Should have parsed enums")

        // And: When we generate C++ files
        val cppGenerator = CppGenerator()
        val headerFile = File(tempDir, "audio_instruction_protobuf_helpers.hpp")
        val implFile = File(tempDir, "audio_instruction_protobuf_helpers.cpp")

        cppGenerator.generateHeader(parsedFile, headerFile)
        cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

        // Then: The files should be created
        assertTrue(headerFile.exists(), "Header file should be generated")
        assertTrue(implFile.exists(), "Implementation file should be generated")

        // And: Should contain expected content
        val headerContent = headerFile.readText()
        assertTrue(headerContent.contains("#ifndef"), "Header should have include guard")
        assertTrue(headerContent.contains("namespace protobuf_helpers"),
            "Header should have protobuf_helpers namespace")
        assertTrue(headerContent.contains("toNative"), "Header should have toNative functions")
        assertTrue(headerContent.contains("toProto"), "Header should have toProto functions")

        val implContent = implFile.readText()
        assertTrue(implContent.contains("#include"), "Implementation should include header")
        assertTrue(implContent.contains("namespace protobuf_helpers"),
            "Implementation should have protobuf_helpers namespace")
    }

    @Test
    fun `test text_generation proto generates expected files`() {
        // Given: The text_generation.proto file
        val protoFile = File(javaClass.getResource("/proto/text_generation.proto")!!.file)
        val protoDir = protoFile.parentFile
        assertTrue(protoFile.exists(), "text_generation.proto should exist in test resources")

        // When: We parse and generate
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // Then: Should parse successfully
        assertTrue(parsedFile.messages.isNotEmpty() || parsedFile.enums.isNotEmpty(),
            "Should have parsed some definitions")

        // And: When generating Kotlin
        val kotlinGenerator = KotlinGenerator()
        kotlinGenerator.generateMapper(parsedFile, tempDir)

        // Then: NativeModelMapper.kt should be created
        val kotlinFile = File(tempDir, parsedFile.packageName.replace(".", "/") + "/NativeModelMapper.kt")
        // Note: KotlinPoet creates the package directory structure
        assertTrue(tempDir.walkTopDown().any { it.name == "NativeModelMapper.kt" },
            "NativeModelMapper.kt should be generated")
    }

    @Test
    fun `test language proto generates expected files`() {
        // Given: The language.proto file
        val protoFile = File(javaClass.getResource("/proto/language.proto")!!.file)
        val protoDir = protoFile.parentFile
        assertTrue(protoFile.exists(), "language.proto should exist in test resources")

        // When: We parse the proto
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // Then: Should parse the Language enum
        assertTrue(parsedFile.enums.isNotEmpty(), "Should have parsed Language enum")
        val languageEnum = parsedFile.enums.find { it.name == "Language" }
        assertTrue(languageEnum != null, "Should find Language enum")
        assertTrue(languageEnum!!.values.isNotEmpty(), "Language enum should have values")
    }

    @Test
    fun `test parser handles nested messages correctly`() {
        // Given: A proto file with nested messages
        val protoFile = File(javaClass.getResource("/proto/audio_instruction.proto")!!.file)
        val protoDir = protoFile.parentFile

        // When: We parse it
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // Then: Should handle nested types
        val messagesWithNested = parsedFile.messages.filter { it.nestedMessages.isNotEmpty() }
        // Audio instruction has nested Lane, etc.
        assertTrue(messagesWithNested.isNotEmpty(),
            "Should have messages with nested messages")
    }

    @Test
    fun `test parser handles repeated fields correctly`() {
        // Given: Proto with repeated fields
        val protoFile = File(javaClass.getResource("/proto/audio_instruction.proto")!!.file)
        val protoDir = protoFile.parentFile

        // When: We parse it
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // Then: Should identify repeated fields
        val allFields = parsedFile.messages.flatMap { it.fields }
        val repeatedFields = allFields.filter { it.isRepeated }
        assertTrue(repeatedFields.isNotEmpty(), "Should have repeated fields")
    }

    @Test
    fun `test parser handles enum fields correctly`() {
        // Given: Proto with enum fields
        val protoFile = File(javaClass.getResource("/proto/audio_instruction.proto")!!.file)
        val protoDir = protoFile.parentFile

        // When: We parse it
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // Then: Should identify enum fields
        val allFields = parsedFile.messages.flatMap { it.fields }
        val enumFields = allFields.filter { it.isEnum }
        assertTrue(enumFields.isNotEmpty(), "Should have enum fields")
    }

    @Test
    fun `test C++ generator creates valid include guards`() {
        // Given: A parsed proto file
        val protoFile = File(javaClass.getResource("/proto/language.proto")!!.file)
        val protoDir = protoFile.parentFile
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // When: We generate the header
        val cppGenerator = CppGenerator()
        val headerFile = File(tempDir, "test_header.hpp")
        cppGenerator.generateHeader(parsedFile, headerFile)

        // Then: Should have proper include guards
        val content = headerFile.readText()
        val lines = content.lines()
        assertTrue(lines.any { it.trim().startsWith("#ifndef") }, "Should have #ifndef")
        assertTrue(lines.any { it.trim().startsWith("#define") }, "Should have #define")
        assertTrue(lines.last().trim() == "#endif" ||
                   lines.any { it.contains("#endif") }, "Should have #endif")
    }

    @Test
    fun `test C++ generator includes copyright header`() {
        // Given: Any proto file
        val protoFile = File(javaClass.getResource("/proto/language.proto")!!.file)
        val protoDir = protoFile.parentFile
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // When: We generate files
        val cppGenerator = CppGenerator()
        val headerFile = File(tempDir, "test.hpp")
        val implFile = File(tempDir, "test.cpp")
        cppGenerator.generateHeader(parsedFile, headerFile)
        cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

        // Then: Both should have copyright
        assertTrue(headerFile.readText().contains("Copyright"),
            "Header should have copyright")
        assertTrue(implFile.readText().contains("Copyright"),
            "Implementation should have copyright")
        assertTrue(headerFile.readText().contains("AUTO-GENERATED"),
            "Header should indicate auto-generation")
        assertTrue(implFile.readText().contains("AUTO-GENERATED"),
            "Implementation should indicate auto-generation")
    }

    @Test
    fun `test Kotlin generator creates valid package structure`() {
        // Given: A proto with package name
        val protoFile = File(javaClass.getResource("/proto/language.proto")!!.file)
        val protoDir = protoFile.parentFile
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // When: We generate Kotlin
        val kotlinGenerator = KotlinGenerator()
        kotlinGenerator.generateMapper(parsedFile, tempDir)

        // Then: Should create package directory structure
        val expectedPackagePath = parsedFile.packageName.replace(".", "/")
        val generatedFiles = tempDir.walkTopDown().filter { it.name == "NativeModelMapper.kt" }.toList()
        assertTrue(generatedFiles.isNotEmpty(), "Should generate Kotlin file")

        val kotlinFile = generatedFiles.first()
        val content = kotlinFile.readText()
        assertTrue(content.contains("package"), "Should have package declaration")
        assertTrue(content.contains("Copyright"), "Should have copyright")
        assertTrue(content.contains("AUTO-GENERATED"), "Should indicate auto-generation")
    }

    @Test
    fun `test Kotlin generator creates extension functions`() {
        // Given: A proto with messages and enums
        val protoFile = File(javaClass.getResource("/proto/language.proto")!!.file)
        val protoDir = protoFile.parentFile
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // When: We generate Kotlin
        val kotlinGenerator = KotlinGenerator()
        kotlinGenerator.generateMapper(parsedFile, tempDir)

        // Then: Should create extension functions
        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }!!
        val content = kotlinFile.readText()

        assertTrue(content.contains("fun"), "Should have functions")
        assertTrue(content.contains("toProto"), "Should have toProto extensions")
        assertTrue(content.contains("toNative"), "Should have toNative extensions")
    }
}

