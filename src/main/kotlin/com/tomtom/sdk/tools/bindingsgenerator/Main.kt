package com.tomtom.sdk.tools.bindingsgenerator

import kotlinx.cli.*
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val parser = ArgParser("bindings-generator")

    val protoFile by parser.option(
        ArgType.String,
        shortName = "p",
        description = "Path to the .proto file"
    ).required()

    val outputDir by parser.option(
        ArgType.String,
        shortName = "o",
        description = "Output directory for generated files"
    ).required()

    val includeDirs by parser.option(
        ArgType.String,
        shortName = "I",
        description = "Include directories for proto imports (can be specified multiple times)"
    ).multiple()

    val cppOutput by parser.option(
        ArgType.Boolean,
        shortName = "c",
        description = "Generate C++ files"
    ).default(true)

    val kotlinOutput by parser.option(
        ArgType.Boolean,
        shortName = "k",
        description = "Generate Kotlin files"
    ).default(true)

    val protocPath by parser.option(
        ArgType.String,
        description = "Path to protoc binary"
    ).default("protoc")

    val verbose by parser.option(
        ArgType.Boolean,
        shortName = "v",
        description = "Verbose output"
    ).default(false)

    try {
        parser.parse(args)

        val protoFileObj = File(protoFile)
        if (!protoFileObj.exists()) {
            error("Proto file not found: $protoFile")
        }

        val outputDirObj = File(outputDir)
        outputDirObj.mkdirs()

        val includeDirectories = includeDirs.map { File(it) }

        if (verbose) {
            println("Parsing proto file: ${protoFileObj.absolutePath}")
            println("Output directory: ${outputDirObj.absolutePath}")
            println("Include directories: ${includeDirectories.joinToString(", ") { it.absolutePath }}")
        }

        // Parse proto file
        val parser = ProtoParser(protocPath)
        val parsedFile = parser.parseProtoFile(protoFileObj, includeDirectories)

        if (verbose) {
            println("Parsed proto package: ${parsedFile.packageName}")
            println("Messages: ${parsedFile.messages.size}")
            println("Enums: ${parsedFile.enums.size}")
        }

        // Generate C++ files
        if (cppOutput) {
            val cppGenerator = CppGenerator()
            val baseName = protoFileObj.nameWithoutExtension

            val headerFile = File(outputDirObj, "${baseName}_protobuf_helpers.hpp")
            val implFile = File(outputDirObj, "${baseName}_protobuf_helpers.cpp")

            if (verbose) {
                println("Generating C++ header: ${headerFile.absolutePath}")
            }
            cppGenerator.generateHeader(parsedFile, headerFile)

            if (verbose) {
                println("Generating C++ implementation: ${implFile.absolutePath}")
            }
            cppGenerator.generateImplementation(parsedFile, headerFile, implFile)

            println("✓ Generated C++ files:")
            println("  - ${headerFile.name}")
            println("  - ${implFile.name}")
        }

        // Generate Kotlin files
        if (kotlinOutput) {
            val kotlinGenerator = KotlinGenerator()

            if (verbose) {
                println("Generating Kotlin mapper...")
            }
            kotlinGenerator.generateMapper(parsedFile, outputDirObj)

            println("✓ Generated Kotlin files:")
            println("  - NativeModelMapper.kt")
        }

        println("\n✓ Code generation completed successfully!")

    } catch (e: Exception) {
        System.err.println("Error: ${e.message}")
        if (verbose) {
            e.printStackTrace()
        }
        exitProcess(1)
    }
}

