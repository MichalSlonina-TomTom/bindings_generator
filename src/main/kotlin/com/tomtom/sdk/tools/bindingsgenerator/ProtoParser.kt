package com.tomtom.sdk.tools.bindingsgenerator

import com.google.protobuf.DescriptorProtos
import java.io.File

data class ParsedProtoFile(
    val packageName: String,
    val protoPackage: String,
    val messages: List<ParsedMessage>,
    val enums: List<ParsedEnum>
)

data class ParsedMessage(
    val name: String,
    val fullName: String,
    val fields: List<ParsedField>,
    val nestedMessages: List<ParsedMessage> = emptyList(),
    val nestedEnums: List<ParsedEnum> = emptyList()
)

data class ParsedField(
    val name: String,
    val protoName: String,
    val type: String,
    val number: Int,
    val isRepeated: Boolean = false,
    val isOptional: Boolean = false,
    val isEnum: Boolean = false,
    val isMessage: Boolean = false,
    val typeName: String = ""
)

data class ParsedEnum(
    val name: String,
    val fullName: String,
    val values: List<ParsedEnumValue>
)

data class ParsedEnumValue(
    val name: String,
    val number: Int
)

class ProtoParser {

    fun parseProtoFile(protoFile: File, includeDirs: List<File>): ParsedProtoFile {
        val tempDescriptor = File.createTempFile("proto_descriptor", ".bin")
        tempDescriptor.deleteOnExit()

        try {
            val protocPath = findProtoc()
            val args = mutableListOf(
                protocPath,
                "--descriptor_set_out=${tempDescriptor.absolutePath}",
                "--include_imports",
                "--include_source_info"
            )
            includeDirs.forEach { args.add("--proto_path=${it.absolutePath}") }
            args.add("--proto_path=${protoFile.parentFile.absolutePath}")
            args.add(protoFile.name)

            val process = ProcessBuilder(args)
                .directory(protoFile.parentFile)
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().readText()
            val exitCode = process.waitFor()

            if (exitCode != 0) {
                throw RuntimeException("protoc failed with exit code $exitCode:\n$output")
            }

            val fileDescriptorSet = tempDescriptor.inputStream().use { input ->
                DescriptorProtos.FileDescriptorSet.parseFrom(input)
            }

            val targetFileName = protoFile.name
            val targetFileDescriptor = fileDescriptorSet.fileList.find { it.name == targetFileName }
                ?: fileDescriptorSet.fileList.last()

            return parseFileDescriptor(targetFileDescriptor)
        } finally {
            tempDescriptor.delete()
        }
    }

    private fun findProtoc(): String {
        val candidates = listOf("protoc", "/usr/local/bin/protoc", "/usr/bin/protoc", "/opt/homebrew/bin/protoc")
        for (candidate in candidates) {
            try {
                val process = ProcessBuilder(candidate, "--version").start()
                if (process.waitFor() == 0) return candidate
            } catch (e: Exception) {
                // try next
            }
        }
        return "protoc"
    }

    private fun parseFileDescriptor(fileDescriptor: DescriptorProtos.FileDescriptorProto): ParsedProtoFile {
        val packageName = fileDescriptor.`package`.replace('.', '/')
            .split("/").joinToString(".") { it }
        val protoPackage = fileDescriptor.`package`

        val topLevelEnumNames = fileDescriptor.enumTypeList.map { it.name }.toSet()
        val allNestedEnumNames = fileDescriptor.messageTypeList
            .flatMap { collectNestedEnumNames(it) }
            .toSet()

        val messages = fileDescriptor.messageTypeList.map { msgDescriptor ->
            parseMessage(msgDescriptor, protoPackage, topLevelEnumNames, allNestedEnumNames)
        }
        val enums = fileDescriptor.enumTypeList.map { enumDescriptor ->
            parseEnum(enumDescriptor, protoPackage)
        }

        return ParsedProtoFile(
            packageName = protoPackage,
            protoPackage = protoPackage,
            messages = messages,
            enums = enums
        )
    }

    private fun collectNestedEnumNames(msg: DescriptorProtos.DescriptorProto): List<String> {
        val names = msg.enumTypeList.map { it.name }.toMutableList()
        msg.nestedTypeList.forEach { names.addAll(collectNestedEnumNames(it)) }
        return names
    }

    private fun parseMessage(
        descriptor: DescriptorProtos.DescriptorProto,
        packageName: String,
        knownEnums: Set<String>,
        allNestedEnums: Set<String>
    ): ParsedMessage {
        val fullName = "$packageName.${descriptor.name}"
        val nestedEnumNames = descriptor.enumTypeList.map { it.name }.toSet()
        val allKnownEnums = knownEnums + nestedEnumNames + allNestedEnums
        val nestedMsgNames = descriptor.nestedTypeList.map { it.name }.toSet()

        val fields = descriptor.fieldList.map { fieldDescriptor ->
            parseField(fieldDescriptor, allKnownEnums, nestedMsgNames)
        }
        val nestedMessages = descriptor.nestedTypeList.map { nested ->
            parseMessage(nested, fullName, allKnownEnums, allNestedEnums)
        }
        val nestedEnums = descriptor.enumTypeList.map { enumDescriptor ->
            parseEnum(enumDescriptor, fullName)
        }

        return ParsedMessage(
            name = descriptor.name,
            fullName = fullName,
            fields = fields,
            nestedMessages = nestedMessages,
            nestedEnums = nestedEnums
        )
    }

    private fun parseField(
        descriptor: DescriptorProtos.FieldDescriptorProto,
        knownEnums: Set<String>,
        knownMessages: Set<String>
    ): ParsedField {
        val isRepeated = descriptor.label == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED
        val isOptional = descriptor.proto3Optional

        data class FieldTypeInfo(val type: String, val isEnum: Boolean, val isMessage: Boolean)

        val typeInfo = when (descriptor.type) {
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING -> FieldTypeInfo("string", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32,
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_SINT32,
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_SFIXED32 -> FieldTypeInfo("int32", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64,
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_SINT64,
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_SFIXED64 -> FieldTypeInfo("int64", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT32,
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED32 -> FieldTypeInfo("uint32", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT64,
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED64 -> FieldTypeInfo("uint64", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL -> FieldTypeInfo("bool", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT -> FieldTypeInfo("float", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE -> FieldTypeInfo("double", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES -> FieldTypeInfo("bytes", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM -> {
                val typeName = descriptor.typeName.substringAfterLast('.')
                FieldTypeInfo(typeName, true, false)
            }
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE -> {
                val typeName = descriptor.typeName.substringAfterLast('.')
                FieldTypeInfo(typeName, false, true)
            }
            else -> FieldTypeInfo("unknown", false, false)
        }

        // Convert proto snake_case field name to camelCase
        val camelCaseName = descriptor.name
            .split("_")
            .mapIndexed { i, part -> if (i == 0) part else part.replaceFirstChar { it.uppercase() } }
            .joinToString("")

        return ParsedField(
            name = camelCaseName,
            protoName = descriptor.name,
            type = typeInfo.type,
            number = descriptor.number,
            isRepeated = isRepeated,
            isOptional = isOptional,
            isEnum = typeInfo.isEnum,
            isMessage = typeInfo.isMessage,
            typeName = descriptor.typeName
        )
    }

    private fun parseEnum(
        descriptor: DescriptorProtos.EnumDescriptorProto,
        packageName: String
    ): ParsedEnum {
        val values = descriptor.valueList.map { value ->
            ParsedEnumValue(name = value.name, number = value.number)
        }
        return ParsedEnum(
            name = descriptor.name,
            fullName = "$packageName.${descriptor.name}",
            values = values
        )
    }
}

