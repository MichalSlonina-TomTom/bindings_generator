package com.tomtom.sdk.tools.bindingsgenerator

import java.io.File
import java.time.Year

private fun copyrightHeader() = """
/*
 * © ${Year.now().value} TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

// AUTO-GENERATED FILE. DO NOT MODIFY.
""".trimStart()

class CppGenerator {

    fun generateHeader(parsedFile: ParsedProtoFile, outputFile: File) {
        val guardName = outputFile.name
            .uppercase()
            .replace('.', '_')
            .replace('-', '_')

        val content = buildString {
            append(copyrightHeader())
            appendLine()
            appendLine("#ifndef $guardName")
            appendLine("#define $guardName")
            appendLine()
            appendLine("#include <string>")
            appendLine("#include <vector>")
            appendLine()

            val allMessages = collectAllMessages(parsedFile.messages)
            if (allMessages.isNotEmpty()) {
                appendLine("// Forward declarations")
                generateMessageForwardDeclarations(allMessages)
                appendLine()
            }

            appendLine("namespace protobuf_helpers {")
            appendLine()

            appendEnumConversions(parsedFile.enums)
            appendMessageConversions(parsedFile.messages, parsedFile.enums)

            appendLine("} // namespace protobuf_helpers")
            appendLine()
            appendLine("#endif // $guardName")
        }

        outputFile.writeText(content)
    }

    fun generateImplementation(parsedFile: ParsedProtoFile, headerFile: File, outputFile: File) {
        val content = buildString {
            append(copyrightHeader())
            appendLine()
            appendLine("#include \"${headerFile.name}\"")
            appendLine()
            appendLine("namespace protobuf_helpers {")
            appendLine()

            appendEnumImplementations(parsedFile.enums)
            appendMessageImplementations(parsedFile.messages, parsedFile.enums)

            appendLine("} // namespace protobuf_helpers")
        }

        outputFile.writeText(content)
    }

    private fun collectAllMessages(messages: List<ParsedMessage>): List<ParsedMessage> {
        val result = mutableListOf<ParsedMessage>()
        for (msg in messages) {
            result.add(msg)
            result.addAll(collectAllMessages(msg.nestedMessages))
        }
        return result
    }

    private fun StringBuilder.generateMessageForwardDeclarations(messages: List<ParsedMessage>) {
        messages.forEach { message ->
            appendLine("struct ${message.name};")
        }
    }

    private fun StringBuilder.appendEnumConversions(enums: List<ParsedEnum>) {
        enums.forEach { enum ->
            val nativeName = enum.name
            appendLine("// Conversion functions for $nativeName")
            appendLine("$nativeName toNative(const ${getProtoEnumName(enum)} proto);")
            appendLine("${getProtoEnumName(enum)} toProto(const $nativeName native);")
            appendLine()
        }
    }

    private fun StringBuilder.appendMessageConversions(messages: List<ParsedMessage>, enums: List<ParsedEnum>) {
        messages.forEach { message ->
            val nativeName = message.name
            appendLine("// Conversion functions for $nativeName")
            appendLine("$nativeName toNative(const ${getProtoMessageName(message)} proto);")
            appendLine("${getProtoMessageName(message)} toProto(const $nativeName& native);")
            appendLine()
            appendMessageConversions(message.nestedMessages, message.nestedEnums)
            appendEnumConversions(message.nestedEnums)
        }
    }

    private fun StringBuilder.appendEnumImplementations(enums: List<ParsedEnum>) {
        enums.forEach { enum ->
            val nativeName = enum.name
            val protoName = getProtoEnumName(enum)

            appendLine("$nativeName toNative(const $protoName proto) {")
            appendLine("    switch (proto) {")
            enum.values.forEach { value ->
                val nativeValue = convertToNativeEnumValue(value.name, enum.name)
                appendLine("        case $protoName::${value.name}: return $nativeName::$nativeValue;")
            }
            appendLine("        default: return $nativeName::${convertToNativeEnumValue(enum.values.first().name, enum.name)};")
            appendLine("    }")
            appendLine("}")
            appendLine()

            appendLine("$protoName toProto(const $nativeName native) {")
            appendLine("    switch (native) {")
            enum.values.forEach { value ->
                val nativeValue = convertToNativeEnumValue(value.name, enum.name)
                appendLine("        case $nativeName::$nativeValue: return $protoName::${value.name};")
            }
            appendLine("        default: return $protoName::${enum.values.first().name};")
            appendLine("    }")
            appendLine("}")
            appendLine()
        }
    }

    private fun StringBuilder.appendMessageImplementations(messages: List<ParsedMessage>, knownEnums: List<ParsedEnum>) {
        messages.forEach { message ->
            val nativeName = message.name
            val protoName = getProtoMessageName(message)
            val allEnums = knownEnums + message.nestedEnums

            // toNative implementation
            appendLine("$nativeName toNative(const $protoName proto) {")
            appendLine("    $nativeName result;")
            message.fields.forEach { field ->
                val fieldAccess = generateToNativeFieldMapping(field, allEnums)
                if (fieldAccess != null) {
                    appendLine("    $fieldAccess")
                }
            }
            appendLine("    return result;")
            appendLine("}")
            appendLine()

            // toProto implementation
            appendLine("$protoName toProto(const $nativeName& native) {")
            appendLine("    $protoName result;")
            message.fields.forEach { field ->
                val fieldAccess = generateToProtoFieldMapping(field, allEnums)
                if (fieldAccess != null) {
                    if (field.isRepeated) {
                        appendLine("    for (const auto& item : native.${field.name}) {")
                        appendLine("        result.add_${field.protoName}($fieldAccess);")
                        appendLine("    }")
                    } else {
                        appendLine("    $fieldAccess")
                    }
                }
            }
            appendLine("    return result;")
            appendLine("}")
            appendLine()

            appendMessageImplementations(message.nestedMessages, allEnums)
            appendEnumImplementations(message.nestedEnums)
        }
    }

    private fun generateToNativeFieldMapping(field: ParsedField, knownEnums: List<ParsedEnum>): String? {
        return when {
            field.isRepeated -> {
                "for (const auto& item : proto.${field.protoName}()) { result.${field.name}.push_back(${getNativeConversion(field, knownEnums, "item")}); }"
            }
            field.isEnum -> "result.${field.name} = toNative(proto.${field.protoName}());"
            field.isMessage -> "result.${field.name} = toNative(proto.${field.protoName}());"
            else -> "result.${field.name} = proto.${field.protoName}();"
        }
    }

    private fun generateToProtoFieldMapping(field: ParsedField, knownEnums: List<ParsedEnum>): String? {
        return when {
            field.isRepeated -> "item" // handled specially in caller
            field.isEnum -> "result.set_${field.protoName}(toProto(native.${field.name}));"
            field.isMessage -> "result.mutable_${field.protoName}()->CopyFrom(toProto(native.${field.name}));"
            else -> when (field.type) {
                "string" -> "result.set_${field.protoName}(native.${field.name});"
                else -> "result.set_${field.protoName}(native.${field.name});"
            }
        }
    }

    private fun getNativeConversion(field: ParsedField, knownEnums: List<ParsedEnum>, accessor: String): String {
        return when {
            field.isEnum -> "toNative($accessor)"
            field.isMessage -> "toNative($accessor)"
            else -> accessor
        }
    }

    private fun getProtoEnumName(enum: ParsedEnum): String {
        return enum.fullName.replace('.', '_').let { it }
            .split(".").last()
            .let { enum.name }
    }

    private fun getProtoMessageName(message: ParsedMessage): String {
        return message.name
    }

    private fun convertToNativeEnumValue(protoValueName: String, enumName: String): String {
        // Proto values are like kEnumNameValue, native are like VALUE
        val prefix = "k${enumName}"
        return if (protoValueName.startsWith(prefix)) {
            protoValueName.substring(prefix.length).uppercase()
        } else {
            protoValueName.uppercase()
        }
    }
}

