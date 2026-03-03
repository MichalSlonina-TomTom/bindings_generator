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

/**
 * Configuration for the C++ generator.
 *
 * @param namespaces Ordered list of namespace segments to nest. Defaults to ["protobuf_helpers"].
 * @param usePragmaOnce Emit `#pragma once` instead of `#ifndef`/`#define`/`#endif`. Defaults to false.
 * @param extraIncludes Additional `#include` lines to emit after the standard ones. Defaults to empty.
 */
data class GeneratorConfig(
    val namespaces: List<String> = listOf("protobuf_helpers"),
    val usePragmaOnce: Boolean = false,
    val extraIncludes: List<String> = emptyList()
) {
    companion object {
        val DEFAULT = GeneratorConfig()
    }
}

class CppGenerator(private val config: GeneratorConfig = GeneratorConfig.DEFAULT) {

    // toNative / toProto in PascalCase
    private val toNativeName = "ToNative"
    private val toProtoName = "ToProto"

    fun generateHeader(parsedFile: ParsedProtoFile, outputFile: File) {
        val guardName = outputFile.name
            .uppercase()
            .replace('.', '_')
            .replace('-', '_')

        val content = buildString {
            append(copyrightHeader())
            appendLine()
            if (config.usePragmaOnce) {
                appendLine("#pragma once")
            } else {
                appendLine("#ifndef $guardName")
                appendLine("#define $guardName")
            }
            appendLine()
            appendLine("#include <string>")
            appendLine("#include <vector>")
            if (config.extraIncludes.isNotEmpty()) {
                appendLine()
                config.extraIncludes.forEach { appendLine(it) }
            }
            appendLine()

            val allMessages = collectAllMessages(parsedFile.messages)
            if (allMessages.isNotEmpty()) {
                appendLine("// Forward declarations")
                generateMessageForwardDeclarations(allMessages)
                appendLine()
            }

            openNamespaces()
            appendLine()

            appendEnumConversions(parsedFile.enums)
            appendMessageConversions(parsedFile.messages, parsedFile.enums)

            closeNamespaces()
            if (!config.usePragmaOnce) {
                appendLine()
                appendLine("#endif // $guardName")
            }
        }

        outputFile.writeText(content)
    }

    fun generateImplementation(parsedFile: ParsedProtoFile, headerFile: File, outputFile: File) {
        val content = buildString {
            append(copyrightHeader())
            appendLine()
            appendLine("#include \"${headerFile.name}\"")
            appendLine()

            openNamespaces()
            appendLine()

            appendEnumImplementations(parsedFile.enums)
            appendMessageImplementations(parsedFile.messages, parsedFile.enums)

            closeNamespaces()
        }

        outputFile.writeText(content)
    }

    private fun StringBuilder.openNamespaces() {
        config.namespaces.forEach { ns -> appendLine("namespace $ns {") }
    }

    private fun StringBuilder.closeNamespaces() {
        config.namespaces.reversed().forEach { ns -> appendLine("}  // namespace $ns") }
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
            appendLine("$nativeName $toNativeName(const ${getProtoEnumName(enum)} proto);")
            appendLine("${getProtoEnumName(enum)} $toProtoName(const $nativeName native);")
            appendLine()
        }
    }

    private fun StringBuilder.appendMessageConversions(messages: List<ParsedMessage>, enums: List<ParsedEnum>) {
        messages.forEach { message ->
            val nativeName = message.name
            appendLine("// Conversion functions for $nativeName")
            appendLine("$nativeName $toNativeName(const ${getProtoMessageName(message)} proto);")
            appendLine("${getProtoMessageName(message)} $toProtoName(const $nativeName& native);")
            appendLine()
            appendMessageConversions(message.nestedMessages, message.nestedEnums)
            appendEnumConversions(message.nestedEnums)
        }
    }

    private fun StringBuilder.appendEnumImplementations(enums: List<ParsedEnum>) {
        enums.forEach { enum ->
            val nativeName = enum.name
            val protoName = getProtoEnumName(enum)

            appendLine("$nativeName $toNativeName(const $protoName proto) {")
            appendLine("    switch (proto) {")
            enum.values.forEach { value ->
                val nativeValue = convertToNativeEnumValue(value.name, enum.name)
                appendLine("        case $protoName::${value.name}: return $nativeName::$nativeValue;")
            }
            appendLine("        default: return $nativeName::${convertToNativeEnumValue(enum.values.first().name, enum.name)};")
            appendLine("    }")
            appendLine("}")
            appendLine()

            appendLine("$protoName $toProtoName(const $nativeName native) {")
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

            // ToNative implementation
            appendLine("$nativeName $toNativeName(const $protoName proto) {")
            appendLine("    $nativeName result;")
            message.fields.forEach { field ->
                val fieldAccess = generateToNativeFieldMapping(field, allEnums)
                if (fieldAccess != null) appendLine("    $fieldAccess")
            }
            // oneof fields
            message.oneofs.forEach { oneof ->
                appendLine("    switch (proto.${oneof.name}_case()) {")
                oneof.fields.forEach { field ->
                    val caseLabel = "${protoName}::k${field.name.replaceFirstChar { it.uppercase() }}"
                    val access = generateToNativeFieldMapping(field, allEnums)
                    appendLine("        case $caseLabel: result.${field.name} = proto.${field.protoName}(); break;")
                }
                appendLine("        default: break;")
                appendLine("    }")
            }
            appendLine("    return result;")
            appendLine("}")
            appendLine()

            // ToProto implementation
            appendLine("$protoName $toProtoName(const $nativeName& native) {")
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
            // oneof fields
            message.oneofs.forEach { oneof ->
                oneof.fields.forEach { field ->
                    val nativeCase = "${nativeName}::k${field.name.replaceFirstChar { it.uppercase() }}"
                    appendLine("    if (native.${oneof.name}_case == $nativeCase) {")
                    appendLine("        result.set_${field.protoName}(native.${field.name});")
                    appendLine("    }")
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
            field.isRepeated -> "for (const auto& item : proto.${field.protoName}()) { result.${field.name}.push_back(${getNativeConversion(field, knownEnums, "item")}); }"
            field.isOptional && !field.isEnum && !field.isMessage ->
                "if (proto.has_${field.protoName}()) { result.${field.name} = proto.${field.protoName}(); }"
            field.isEnum -> "result.${field.name} = $toNativeName(proto.${field.protoName}());"
            field.isMessage -> "result.${field.name} = $toNativeName(proto.${field.protoName}());"
            else -> "result.${field.name} = proto.${field.protoName}();"
        }
    }

    private fun generateToProtoFieldMapping(field: ParsedField, knownEnums: List<ParsedEnum>): String? {
        return when {
            field.isRepeated -> "item"
            field.isOptional && !field.isEnum && !field.isMessage ->
                "if (native.${field.name}.has_value()) { result.set_${field.protoName}(native.${field.name}.value()); }"
            field.isEnum -> "result.set_${field.protoName}($toProtoName(native.${field.name}));"
            field.isMessage -> "result.mutable_${field.protoName}()->CopyFrom($toProtoName(native.${field.name}));"
            else -> "result.set_${field.protoName}(native.${field.name});"
        }
    }

    private fun getNativeConversion(field: ParsedField, knownEnums: List<ParsedEnum>, accessor: String): String {
        return when {
            field.isEnum -> "$toNativeName($accessor)"
            field.isMessage -> "$toNativeName($accessor)"
            else -> accessor
        }
    }

    /**
     * Returns the C++ type name for a proto enum.
     * For nested enums protobuf mangles "ParentMessage.EnumName" to "ParentMessage_EnumName".
     * fullName is e.g. "com.test.JunctionViewError.ErrorType" — take the last two segments
     * separated by '.' and join with '_'.
     */
    private fun getProtoEnumName(enum: ParsedEnum): String {
        val parts = enum.fullName.split(".")
        return if (parts.size >= 2 && parts[parts.size - 2][0].isUpperCase()) {
            // nested: ParentMessage_EnumName
            "${parts[parts.size - 2]}_${parts.last()}"
        } else {
            enum.name
        }
    }

    private fun getProtoMessageName(message: ParsedMessage): String = message.name

    private fun convertToNativeEnumValue(protoValueName: String, enumName: String): String {
        val prefix = "k${enumName}"
        return if (protoValueName.startsWith(prefix)) {
            protoValueName.substring(prefix.length).uppercase()
        } else {
            protoValueName.uppercase()
        }
    }
}
