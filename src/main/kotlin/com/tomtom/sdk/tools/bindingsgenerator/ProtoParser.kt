package com.tomtom.sdk.tools.bindingsgenerator

import com.google.protobuf.DescriptorProtos
import java.io.File

/**
 * Represents a parsed proto file with all its definitions
 */
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

/**
 * Parser that uses protoc to generate descriptors and parse them
 */
class ProtoParser(private val protocPath: String = "protoc") {

    /**
     * Parse a proto file using protoc descriptor
     */
    fun parseProtoFile(protoFile: File, includeDirectories: List<File> = emptyList()): ParsedProtoFile {
        // Create temp file for descriptor
        val descriptorFile = File.createTempFile("proto_descriptor_", ".desc")
        descriptorFile.deleteOnExit()

        try {
            // Build protoc command
            val command = buildList {
                add(protocPath)
                includeDirectories.forEach { add("-I${it.absolutePath}") }
                add("-I${protoFile.parentFile.absolutePath}")
                add("--descriptor_set_out=${descriptorFile.absolutePath}")
                add("--include_imports")
                add(protoFile.name)
            }

            // Execute protoc
            val process = ProcessBuilder(command)
                .directory(protoFile.parentFile)
                .redirectErrorStream(true)
                .start()

            val exitCode = process.waitFor()
            if (exitCode != 0) {
                val error = process.inputStream.bufferedReader().readText()
                throw RuntimeException("protoc failed with exit code $exitCode: $error")
            }

            // Parse descriptor
            val fileDescriptorSet = descriptorFile.inputStream().use {
                DescriptorProtos.FileDescriptorSet.parseFrom(it)
            }

            // Find the main file descriptor (not imports)
            val mainFileDescriptor = fileDescriptorSet.fileList.find {
                it.name == protoFile.name || it.name.endsWith("/${protoFile.name}")
            } ?: throw RuntimeException("Could not find descriptor for ${protoFile.name}")

            return parseFileDescriptor(mainFileDescriptor)

        } finally {
            descriptorFile.delete()
        }
    }

    private fun parseFileDescriptor(fileDescriptor: DescriptorProtos.FileDescriptorProto): ParsedProtoFile {
        val protoPackage = fileDescriptor.`package`

        // Parse top-level messages
        val messages = fileDescriptor.messageTypeList.map { parseMessage(it, protoPackage) }

        // Parse top-level enums
        val enums = fileDescriptor.enumTypeList.map { parseEnum(it, protoPackage) }

        return ParsedProtoFile(
            packageName = protoPackage,
            protoPackage = protoPackage,
            messages = messages,
            enums = enums
        )
    }

    private fun parseMessage(
        messageDescriptor: DescriptorProtos.DescriptorProto,
        packagePrefix: String
    ): ParsedMessage {
        val fullName = "$packagePrefix.${messageDescriptor.name}"

        // Parse fields
        val fields = messageDescriptor.fieldList.map { parseField(it) }

        // Parse nested messages
        val nestedMessages = messageDescriptor.nestedTypeList.map {
            parseMessage(it, fullName)
        }

        // Parse nested enums
        val nestedEnums = messageDescriptor.enumTypeList.map {
            parseEnum(it, fullName)
        }

        return ParsedMessage(
            name = messageDescriptor.name,
            fullName = fullName,
            fields = fields,
            nestedMessages = nestedMessages,
            nestedEnums = nestedEnums
        )
    }

    private fun parseField(fieldDescriptor: DescriptorProtos.FieldDescriptorProto): ParsedField {
        val isRepeated = fieldDescriptor.label == DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED
        val isOptional = fieldDescriptor.label == DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL

        data class FieldTypeInfo(val type: String, val isEnum: Boolean, val isMessage: Boolean, val typeName: String = "")

        val typeInfo = when (fieldDescriptor.type) {
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE -> FieldTypeInfo("double", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT -> FieldTypeInfo("float", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64 -> FieldTypeInfo("int64", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT64 -> FieldTypeInfo("uint64", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32 -> FieldTypeInfo("int32", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED64 -> FieldTypeInfo("fixed64", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED32 -> FieldTypeInfo("fixed32", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL -> FieldTypeInfo("bool", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING -> FieldTypeInfo("string", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE -> FieldTypeInfo(
                fieldDescriptor.typeName.substringAfterLast("."),
                false,
                true,
                fieldDescriptor.typeName
            )
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES -> FieldTypeInfo("bytes", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT32 -> FieldTypeInfo("uint32", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM -> FieldTypeInfo(
                fieldDescriptor.typeName.substringAfterLast("."),
                true,
                false,
                fieldDescriptor.typeName
            )
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_SFIXED32 -> FieldTypeInfo("sfixed32", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_SFIXED64 -> FieldTypeInfo("sfixed64", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_SINT32 -> FieldTypeInfo("sint32", false, false)
            DescriptorProtos.FieldDescriptorProto.Type.TYPE_SINT64 -> FieldTypeInfo("sint64", false, false)
            else -> FieldTypeInfo("unknown", false, false)
        }

        return ParsedField(
            name = fieldDescriptor.name,
            protoName = fieldDescriptor.name,
            type = typeInfo.type,
            number = fieldDescriptor.number,
            isRepeated = isRepeated,
            isOptional = isOptional,
            isEnum = typeInfo.isEnum,
            isMessage = typeInfo.isMessage,
            typeName = typeInfo.typeName
        )
    }

    private fun parseEnum(
        enumDescriptor: DescriptorProtos.EnumDescriptorProto,
        packagePrefix: String
    ): ParsedEnum {
        val fullName = "$packagePrefix.${enumDescriptor.name}"

        val values = enumDescriptor.valueList.map { valueDescriptor ->
            ParsedEnumValue(
                name = valueDescriptor.name,
                number = valueDescriptor.number
            )
        }

        return ParsedEnum(
            name = enumDescriptor.name,
            fullName = fullName,
            values = values
        )
    }
}



