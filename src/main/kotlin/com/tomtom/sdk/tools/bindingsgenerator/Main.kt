package com.tomtom.sdk.tools.bindingsgenerator

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import java.io.File

fun main(args: Array<String>) {
    val parser = ArgParser("bindings-generator")

    val protoFile by parser.option(ArgType.String, shortName = "p", description = "Path to the proto file").required()
    val outputDir by parser.option(ArgType.String, shortName = "o", description = "Output directory").required()
    val includeDir by parser.option(ArgType.String, shortName = "I", description = "Include directory for imports")

    parser.parse(args)

    val proto = File(protoFile)
    val output = File(outputDir)
    val includes = if (includeDir != null) listOf(File(includeDir!!)) else emptyList()

    output.mkdirs()

    val protoParser = ProtoParser()
    val parsedFile = protoParser.parseProtoFile(proto, includes)

    val cppGenerator = CppGenerator()
    val baseName = proto.nameWithoutExtension
    cppGenerator.generateHeader(parsedFile, File(output, "protobuf_helpers.hpp"))
    cppGenerator.generateImplementation(parsedFile, File(output, "protobuf_helpers.hpp"), File(output, "protobuf_helpers.cpp"))

    val kotlinGenerator = KotlinGenerator()
    kotlinGenerator.generateMapper(parsedFile, output)

    println("Generated bindings for ${proto.name} in ${output.absolutePath}")
}

