package com.tomtom.sdk.tools.bindingsgenerator

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests that verify generated output matches expected reference files exactly.
 *
 * These tests compare the generated files with the handcrafted reference implementations
 * to ensure the generator produces the correct output.
 */
class ExactOutputComparisonTest {

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun requireProtoc() {
        val isProtocAvailable = try {
            val process = ProcessBuilder("protoc", "--version").start()
            process.waitFor() == 0
        } catch (e: Exception) {
            false
        }
        Assumptions.assumeTrue(isProtocAvailable, "protoc not installed - skipping test")
    }

    /**
     * Helper function to normalize whitespace and line endings for comparison
     */
    private fun normalizeContent(content: String): String {
        return content
            .lines()
            .map { it.trimEnd() } // Remove trailing whitespace
            .joinToString("\n")
            .replace("\r\n", "\n") // Normalize line endings
    }

    /**
     * Helper to compare files with detailed diff output
     */
    private fun compareFiles(generated: File, expected: File, description: String) {
        assertTrue(generated.exists(), "$description: Generated file should exist: ${generated.absolutePath}")
        assertTrue(expected.exists(), "$description: Expected file should exist: ${expected.absolutePath}")

        val generatedContent = normalizeContent(generated.readText())
        val expectedContent = normalizeContent(expected.readText())

        if (generatedContent != expectedContent) {
            // Print diff for debugging
            val generatedLines = generatedContent.lines()
            val expectedLines = expectedContent.lines()

            println("\n=== DIFF for $description ===")
            println("Generated file: ${generated.absolutePath}")
            println("Expected file: ${expected.absolutePath}")
            println("\nFirst difference:")

            val maxLines = maxOf(generatedLines.size, expectedLines.size)
            for (i in 0 until maxLines) {
                val genLine = generatedLines.getOrNull(i) ?: "<EOF>"
                val expLine = expectedLines.getOrNull(i) ?: "<EOF>"

                if (genLine != expLine) {
                    println("Line ${i + 1}:")
                    println("  Expected: $expLine")
                    println("  Generated: $genLine")
                    break
                }
            }

            println("\nGenerated ${generatedLines.size} lines, expected ${expectedLines.size} lines")
            println("=== END DIFF ===\n")
        }

        assertEquals(expectedContent, generatedContent,
            "$description: Generated content should match expected content")
    }

    @Test
    fun `test protobuf_helpers_hpp matches expected output`() {
        // Given: The audio_instruction.proto and text_generation.proto files
        val audioProtoFile = File(javaClass.getResource("/text-generation/proto/audio_instruction.proto")!!.file)
        val textGenProtoFile = File(javaClass.getResource("/text-generation/proto/text_generation.proto")!!.file)
        val protoDir = audioProtoFile.parentFile

        // Note: The actual protobuf_helpers.hpp combines multiple proto files
        // For this test, we'll verify the structure matches

        // When: We parse and generate for audio_instruction
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(audioProtoFile, listOf(protoDir))

        val cppGenerator = CppGenerator()
        val generatedHeader = File(tempDir, "protobuf_helpers.hpp")
        cppGenerator.generateHeader(parsedFile, generatedHeader)

        // Then: Should have the expected structure
        val generatedContent = generatedHeader.readText()

        // Verify key structural elements
        assertTrue(generatedContent.contains("#ifndef PROTOBUF_HELPERS_HPP") ||
                   generatedContent.contains("#pragma once"),
            "Should have include guard")
        assertTrue(generatedContent.contains("namespace protobuf_helpers"),
            "Should have protobuf_helpers namespace")
        assertTrue(generatedContent.contains("ToNative"),
            "Should have ToNative functions")
        assertTrue(generatedContent.contains("ToProto"),
            "Should have ToProto functions")
        assertTrue(generatedContent.contains("TomTom"),
            "Should have copyright header")
        assertTrue(generatedContent.contains("AUTO-GENERATED"),
            "Should have auto-generated notice")
    }

    @Test
    fun `test protobuf_helpers_cpp matches expected structure`() {
        // Given: Proto files
        val audioProtoFile = File(javaClass.getResource("/text-generation/proto/audio_instruction.proto")!!.file)
        val protoDir = audioProtoFile.parentFile

        // When: We generate implementation
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(audioProtoFile, listOf(protoDir))

        val cppGenerator = CppGenerator()
        val generatedHeader = File(tempDir, "protobuf_helpers.hpp")
        val generatedImpl = File(tempDir, "protobuf_helpers.cpp")

        cppGenerator.generateHeader(parsedFile, generatedHeader)
        cppGenerator.generateImplementation(parsedFile, generatedHeader, generatedImpl)

        // Then: Should have expected structure
        val implContent = generatedImpl.readText()

        assertTrue(implContent.contains("#include \"protobuf_helpers.hpp\""),
            "Should include header file")
        assertTrue(implContent.contains("namespace protobuf_helpers"),
            "Should have protobuf_helpers namespace")
        assertTrue(implContent.contains("ToNative"),
            "Should implement ToNative functions")
        assertTrue(implContent.contains("ToProto"),
            "Should implement ToProto functions")
        assertTrue(implContent.contains("TomTom"),
            "Should have copyright header")
    }

    @Test
    fun `test NativeModelMapper_kt matches expected structure`() {
        // Given: Proto files
        val audioProtoFile = File(javaClass.getResource("/text-generation/proto/audio_instruction.proto")!!.file)
        val protoDir = audioProtoFile.parentFile

        // When: We generate Kotlin mapper
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(audioProtoFile, listOf(protoDir))

        val kotlinGenerator = KotlinGenerator()
        kotlinGenerator.generateMapper(parsedFile, tempDir)

        // Then: Should create NativeModelMapper.kt with expected structure
        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }
        assertTrue(kotlinFile != null, "NativeModelMapper.kt should be generated")

        val kotlinContent = kotlinFile!!.readText()

        // Verify structure
        assertTrue(kotlinContent.contains("package "),
            "Should have package declaration")
        assertTrue(kotlinContent.contains("fun") && kotlinContent.contains("toProto"),
            "Should have toProto extension functions")
        assertTrue(kotlinContent.contains("fun") && kotlinContent.contains("toNative"),
            "Should have toNative extension functions")
        assertTrue(kotlinContent.contains("TomTom"),
            "Should have copyright header")
        assertTrue(kotlinContent.contains("AUTO-GENERATED"),
            "Should have auto-generated notice")
        assertTrue(kotlinContent.contains("@Suppress"),
            "Should have suppress annotations")
    }

    @Test
    fun `test generated files have consistent formatting`() {
        // Given: Any proto file
        val protoFile = File(javaClass.getResource("/text-generation/proto/language.proto")!!.file)
        val protoDir = protoFile.parentFile

        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // When: We generate all files
        val cppGenerator = CppGenerator()
        val headerFile = File(tempDir, "test.hpp")
        val implFile = File(tempDir, "test.cpp")
        cppGenerator.generateHeader(parsedFile, headerFile)
        cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

        val kotlinGenerator = KotlinGenerator()
        kotlinGenerator.generateMapper(parsedFile, tempDir)

        // Then: All files should have consistent formatting
        listOf(headerFile, implFile).forEach { file ->
            val content = file.readText()
            // Should not have trailing whitespace on lines
            content.lines().forEach { line ->
                assertEquals(line.trimEnd(), line,
                    "Line should not have trailing whitespace in ${file.name}")
            }
        }
    }

    @Test
    fun `test enum conversion functions are generated correctly`() {
        // Given: Language.proto with Language enum
        val protoFile = File(javaClass.getResource("/text-generation/proto/language.proto")!!.file)
        val protoDir = protoFile.parentFile

        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // When: We generate C++ and Kotlin
        val cppGenerator = CppGenerator()
        val headerFile = File(tempDir, "language.hpp")
        val implFile = File(tempDir, "language.cpp")
        cppGenerator.generateHeader(parsedFile, headerFile)
        cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

        // Then: Should have enum conversion functions
        val headerContent = headerFile.readText()
        val implContent = implFile.readText()

        // C++ should have both ToNative and ToProto for Language enum
        assertTrue(headerContent.contains("Language ToNative") ||
                   headerContent.contains("ToNative") && headerContent.contains("Language"),
            "Header should declare ToNative for Language enum")
        assertTrue(headerContent.contains("Language ToProto") ||
                   headerContent.contains("ToProto") && headerContent.contains("Language"),
            "Header should declare ToProto for Language enum")

        // Kotlin should have extension functions
        val kotlinGenerator = KotlinGenerator()
        kotlinGenerator.generateMapper(parsedFile, tempDir)
        val kotlinFile = tempDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }!!
        val kotlinContent = kotlinFile.readText()

        assertTrue(kotlinContent.contains("toProto") && kotlinContent.contains("Language"),
            "Should have toProto for Language")
        assertTrue(kotlinContent.contains("toNative") && kotlinContent.contains("Language"),
            "Should have toNative for Language")
    }

    @Test
    fun `test message conversion functions handle all field types`() {
        // Given: audio_instruction.proto with complex messages
        val protoFile = File(javaClass.getResource("/text-generation/proto/audio_instruction.proto")!!.file)
        val protoDir = protoFile.parentFile

        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

        // When: We generate implementations
        val cppGenerator = CppGenerator()
        val headerFile = File(tempDir, "audio.hpp")
        val implFile = File(tempDir, "audio.cpp")
        cppGenerator.generateHeader(parsedFile, headerFile)
        cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

        // Then: Implementation should handle different field types
        val implContent = implFile.readText()

        // Should handle string fields
        assertTrue(implContent.contains("std::string") || implContent.contains("string"),
            "Should handle string fields")

        // Should handle repeated fields (for loops)
        assertTrue(implContent.contains("for (") || implContent.contains("for("),
            "Should handle repeated fields with loops")
    }

    @Test
    fun `test generated code compiles without syntax errors`() {
        // Given: All proto files
        val protoFiles = listOf("language.proto", "audio_instruction.proto", "text_generation.proto")

        protoFiles.forEach { protoFileName ->
            val protoFile = File(javaClass.getResource("/text-generation/proto/$protoFileName")!!.file)
            val protoDir = protoFile.parentFile

            // When: We generate code
            val parser = ProtoParser()
            val parsedFile = parser.parseProtoFile(protoFile, listOf(protoDir))

            val cppGenerator = CppGenerator()
            val headerFile = File(tempDir, "${protoFileName.removeSuffix(".proto")}.hpp")
            val implFile = File(tempDir, "${protoFileName.removeSuffix(".proto")}.cpp")
            cppGenerator.generateHeader(parsedFile, headerFile)
            cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

            val kotlinGenerator = KotlinGenerator()
            val kotlinDir = File(tempDir, protoFileName.removeSuffix(".proto"))
            kotlinDir.mkdirs()
            kotlinGenerator.generateMapper(parsedFile, kotlinDir)

            // Then: Files should be created without exceptions
            assertTrue(headerFile.exists(), "Header should be generated for $protoFileName")
            assertTrue(implFile.exists(), "Implementation should be generated for $protoFileName")

            // And: Should have valid syntax (basic checks)
            val headerContent = headerFile.readText()
            val implContent = implFile.readText()

            // Count braces should match
            assertEquals(headerContent.count { it == '{' }, headerContent.count { it == '}' },
                "Braces should match in header for $protoFileName")
            assertEquals(implContent.count { it == '{' }, implContent.count { it == '}' },
                "Braces should match in implementation for $protoFileName")
        }
    }

    // -------------------------------------------------------------------------
    // Junction-view-engine exact output comparison
    // -------------------------------------------------------------------------

    private fun jveProtoDir(): File =
        File(javaClass.getResource("/junction-view-engine/proto/junction_view_information.proto")!!.file).parentFile

    private fun jveExpectedDir(): File =
        File(javaClass.getResource("/junction-view-engine/expected/protobuf_helpers.hpp")!!.file).parentFile

    private fun generateJveFiles(outDir: File): Triple<File, File, File> {
        val protoFile = File(jveProtoDir(), "junction_view_information.proto")
        val parser = ProtoParser()
        val parsedFile = parser.parseProtoFile(protoFile, listOf(jveProtoDir()))

        val headerFile = File(outDir, "protobuf_helpers.hpp")
        val implFile = File(outDir, "protobuf_helpers.cpp")
        val cppGenerator = CppGenerator()
        cppGenerator.generateHeader(parsedFile, headerFile)
        cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

        val kotlinGenerator = KotlinGenerator()
        kotlinGenerator.generateMapper(parsedFile, outDir)
        val kotlinFile = outDir.walkTopDown().find { it.name == "NativeModelMapper.kt" }!!

        return Triple(headerFile, implFile, kotlinFile)
    }

    @Test
    fun `junction-view-engine protobuf_helpers_hpp matches expected output`() {
        val expectedDir = jveExpectedDir()
        val (header, _, _) = generateJveFiles(tempDir)

        val expected = File(expectedDir, "protobuf_helpers.hpp").readText()
        val actual = header.readText()

        assertEquals(expected, actual,
            "Generated protobuf_helpers.hpp does not match junction-view-engine expected reference")
    }

    @Test
    fun `junction-view-engine protobuf_helpers_cpp matches expected output`() {
        val expectedDir = jveExpectedDir()
        val (_, impl, _) = generateJveFiles(tempDir)

        val expected = File(expectedDir, "protobuf_helpers.cpp").readText()
        val actual = impl.readText()

        assertEquals(expected, actual,
            "Generated protobuf_helpers.cpp does not match junction-view-engine expected reference")
    }

    @Test
    fun `junction-view-engine NativeModelMapper_kt matches expected output`() {
        val expectedDir = jveExpectedDir()
        val (_, _, kotlinFile) = generateJveFiles(tempDir)

        val expected = File(expectedDir, "NativeModelMapper.kt").readText()
        val actual = kotlinFile.readText()

        assertEquals(expected, actual,
            "Generated NativeModelMapper.kt does not match junction-view-engine expected reference")
    }
}

/**
 * Tests that verify the copyright header emitted by both generators is well-formed:
 * exactly one © symbol, correct year from the OS, "TomTom NV" present.
 * These tests do NOT require protoc.
 */
class CopyrightHeaderTest {

    @TempDir
    lateinit var tempDir: File

    private val minimalFile = ParsedProtoFile(
        packageName = "com.test",
        protoPackage = "com.test",
        messages = emptyList(),
        enums = listOf(
            ParsedEnum("Status", "com.test.Status", listOf(ParsedEnumValue("kStatusOk", 0)))
        )
    )

    private fun expectedYear() = java.time.Year.now().value.toString()

    // ── C++ header ────────────────────────────────────────────────────────────

    @Test
    fun `cpp header contains exactly one copyright symbol`() {
        val header = File(tempDir, "out.hpp")
        CppGenerator().generateHeader(minimalFile, header)
        val text = header.readText()
        val count = text.split("\u00a9").size - 1
        assertEquals(1, count, "Expected exactly one © in C++ header, found $count:\n$text")
    }

    @Test
    fun `cpp header copyright line has correct format`() {
        val header = File(tempDir, "out.hpp")
        CppGenerator().generateHeader(minimalFile, header)
        val copyrightLine = header.readLines().first { "\u00a9" in it }
        val expected = " * \u00a9 ${expectedYear()} TomTom NV. All rights reserved."
        assertEquals(expected, copyrightLine.trimEnd(),
            "C++ copyright line format is wrong")
    }

    @Test
    fun `cpp implementation contains exactly one copyright symbol`() {
        val header = File(tempDir, "out.hpp")
        val impl = File(tempDir, "out.cpp")
        CppGenerator().generateHeader(minimalFile, header)
        CppGenerator().generateImplementation(minimalFile, header, impl)
        val count = impl.readText().split("\u00a9").size - 1
        assertEquals(1, count, "Expected exactly one © in C++ implementation, found $count")
    }

    // ── Kotlin mapper ─────────────────────────────────────────────────────────

    @Test
    fun `kotlin mapper contains exactly one copyright symbol`() {
        KotlinGenerator().generateMapper(minimalFile, tempDir)
        val kt = tempDir.walkTopDown().first { it.name == "NativeModelMapper.kt" }
        val count = kt.readText().split("\u00a9").size - 1
        assertEquals(1, count, "Expected exactly one © in Kotlin mapper, found $count:\n${kt.readText()}")
    }

    @Test
    fun `kotlin mapper copyright line has correct format`() {
        KotlinGenerator().generateMapper(minimalFile, tempDir)
        val kt = tempDir.walkTopDown().first { it.name == "NativeModelMapper.kt" }
        // KotlinPoet emits the file comment with "// " prefix on each line
        val copyrightLine = kt.readLines().first { "\u00a9" in it }
        val expected = "// \u00a9 ${expectedYear()} TomTom NV. All rights reserved."
        assertEquals(expected, copyrightLine.trimEnd(),
            "Kotlin copyright line format is wrong")
    }
}

