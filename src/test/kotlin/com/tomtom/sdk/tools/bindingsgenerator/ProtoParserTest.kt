package com.tomtom.sdk.tools.bindingsgenerator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.api.Assumptions
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for the ProtoParser class
 *
 * NOTE: These tests require protoc to be installed.
 * If protoc is not available, tests will be skipped.
 */
class ProtoParserTest {

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
    fun `test parseProtoFile with simple enum`() {
        requireProtoc()

        // Given: A simple proto file with an enum
        val protoContent = """
            syntax = "proto3";
            package com.test;
            
            enum Status {
              kStatusUnknown = 0;
              kStatusActive = 1;
              kStatusInactive = 2;
            }
        """.trimIndent()

        val protoFile = File(tempDir, "test.proto")
        protoFile.writeText(protoContent)

        // When: We parse it
        val parser = ProtoParser()
        val result = parser.parseProtoFile(protoFile, emptyList())

        // Then: Should parse the enum correctly
        assertEquals("com.test", result.packageName)
        assertEquals(1, result.enums.size)

        val statusEnum = result.enums.first()
        assertEquals("Status", statusEnum.name)
        assertEquals(3, statusEnum.values.size)
        assertEquals("kStatusUnknown", statusEnum.values[0].name)
        assertEquals(0, statusEnum.values[0].number)
        assertEquals("kStatusActive", statusEnum.values[1].name)
        assertEquals(1, statusEnum.values[1].number)
    }

    @Test
    fun `test parseProtoFile with simple message`() {
        // Given: A proto file with a message
        val protoContent = """
            syntax = "proto3";
            package com.test;
            
            message Person {
              string name = 1;
              int32 age = 2;
              bool active = 3;
            }
        """.trimIndent()

        val protoFile = File(tempDir, "test.proto")
        protoFile.writeText(protoContent)

        // When: We parse it
        val parser = ProtoParser()
        val result = parser.parseProtoFile(protoFile, emptyList())

        // Then: Should parse the message correctly
        assertEquals(1, result.messages.size)

        val person = result.messages.first()
        assertEquals("Person", person.name)
        assertEquals(3, person.fields.size)

        val nameField = person.fields.find { it.name == "name" }
        assertNotNull(nameField)
        assertEquals("string", nameField.type)
        assertEquals(1, nameField.number)
        assertFalse(nameField.isRepeated)
    }

    @Test
    fun `test parseProtoFile with repeated fields`() {
        // Given: A proto with repeated fields
        val protoContent = """
            syntax = "proto3";
            package com.test;
            
            message Tags {
              repeated string values = 1;
              repeated int32 numbers = 2;
            }
        """.trimIndent()

        val protoFile = File(tempDir, "test.proto")
        protoFile.writeText(protoContent)

        // When: We parse it
        val parser = ProtoParser()
        val result = parser.parseProtoFile(protoFile, emptyList())

        // Then: Should identify repeated fields
        val message = result.messages.first()
        val valuesField = message.fields.find { it.name == "values" }
        assertNotNull(valuesField)
        assertTrue(valuesField.isRepeated)

        val numbersField = message.fields.find { it.name == "numbers" }
        assertNotNull(numbersField)
        assertTrue(numbersField.isRepeated)
    }

    @Test
    fun `test parseProtoFile with nested message`() {
        // Given: A proto with nested message
        val protoContent = """
            syntax = "proto3";
            package com.test;
            
            message Outer {
              message Inner {
                string value = 1;
              }
              Inner inner = 1;
            }
        """.trimIndent()

        val protoFile = File(tempDir, "test.proto")
        protoFile.writeText(protoContent)

        // When: We parse it
        val parser = ProtoParser()
        val result = parser.parseProtoFile(protoFile, emptyList())

        // Then: Should parse nested message
        val outer = result.messages.first()
        assertEquals("Outer", outer.name)
        assertEquals(1, outer.nestedMessages.size)

        val inner = outer.nestedMessages.first()
        assertEquals("Inner", inner.name)
        assertEquals(1, inner.fields.size)
    }

    @Test
    fun `test parseProtoFile with nested enum`() {
        // Given: A proto with nested enum
        val protoContent = """
            syntax = "proto3";
            package com.test;
            
            message Config {
              enum Level {
                kLevelLow = 0;
                kLevelHigh = 1;
              }
              Level level = 1;
            }
        """.trimIndent()

        val protoFile = File(tempDir, "test.proto")
        protoFile.writeText(protoContent)

        // When: We parse it
        val parser = ProtoParser()
        val result = parser.parseProtoFile(protoFile, emptyList())

        // Then: Should parse nested enum
        val config = result.messages.first()
        assertEquals(1, config.nestedEnums.size)

        val level = config.nestedEnums.first()
        assertEquals("Level", level.name)
        assertEquals(2, level.values.size)
    }

    @Test
    fun `test parseProtoFile identifies enum fields correctly`() {
        // Given: A proto with enum field
        val protoContent = """
            syntax = "proto3";
            package com.test;
            
            enum Status {
              kStatusActive = 0;
            }
            
            message Record {
              Status status = 1;
            }
        """.trimIndent()

        val protoFile = File(tempDir, "test.proto")
        protoFile.writeText(protoContent)

        // When: We parse it
        val parser = ProtoParser()
        val result = parser.parseProtoFile(protoFile, emptyList())

        // Then: Should identify enum field
        val record = result.messages.first()
        val statusField = record.fields.first()
        assertTrue(statusField.isEnum)
        assertEquals("Status", statusField.type)
    }

    @Test
    fun `test parseProtoFile identifies message fields correctly`() {
        // Given: A proto with message field
        val protoContent = """
            syntax = "proto3";
            package com.test;
            
            message Address {
              string street = 1;
            }
            
            message Person {
              Address address = 1;
            }
        """.trimIndent()

        val protoFile = File(tempDir, "test.proto")
        protoFile.writeText(protoContent)

        // When: We parse it
        val parser = ProtoParser()
        val result = parser.parseProtoFile(protoFile, emptyList())

        // Then: Should identify message field
        val person = result.messages.find { it.name == "Person" }
        assertNotNull(person)
        val addressField = person.fields.first()
        assertTrue(addressField.isMessage)
        assertEquals("Address", addressField.type)
    }

    @Test
    fun `test parseProtoFile handles imports`() {
        // Given: Two proto files where one imports another
        val baseProto = """
            syntax = "proto3";
            package com.test;
            
            message Base {
              string value = 1;
            }
        """.trimIndent()

        val mainProto = """
            syntax = "proto3";
            package com.test;
            
            import "base.proto";
            
            message Main {
              Base base = 1;
            }
        """.trimIndent()

        val baseFile = File(tempDir, "base.proto")
        baseFile.writeText(baseProto)

        val mainFile = File(tempDir, "main.proto")
        mainFile.writeText(mainProto)

        // When: We parse the main file with include directory
        val parser = ProtoParser()
        val result = parser.parseProtoFile(mainFile, listOf(tempDir))

        // Then: Should parse successfully
        assertEquals(1, result.messages.size)
        assertEquals("Main", result.messages.first().name)
    }
}

