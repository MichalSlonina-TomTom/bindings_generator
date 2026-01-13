package com.tomtom.sdk.tools.bindingsgenerator

import java.io.File

/**
 * Generates C++ protobuf helper files (.hpp and .cpp)
 */
class CppGenerator {

    fun generateHeader(parsedFile: ParsedProtoFile, outputFile: File) {
        val content = buildString {
            appendLine("/*")
            appendLine(" * Copyright (C) 2024 TomTom NV. All rights reserved.")
            appendLine(" *")
            appendLine(" * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be")
            appendLine(" * used for internal evaluation purposes or commercial use strictly subject to separate")
            appendLine(" * license agreement between you and TomTom NV. If you are the licensee, you are only permitted")
            appendLine(" * to use this software in accordance with the terms of your license agreement. If you are")
            appendLine(" * not the licensee, you are not authorized to use this software in any manner and should")
            appendLine(" * immediately return or destroy it.")
            appendLine(" */")
            appendLine()
            appendLine("// THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.")
            appendLine()

            val guardName = "PROTOBUF_HELPERS_HPP_${parsedFile.packageName.replace(".", "_").uppercase()}"
            appendLine("#ifndef $guardName")
            appendLine("#define $guardName")
            appendLine()
            appendLine("#include <string>")
            appendLine("#include <vector>")
            appendLine("#include <optional>")
            appendLine()

            // Generate forward declarations for all messages and enums
            parsedFile.messages.forEach { message ->
                appendLine("// Forward declarations for ${message.name}")
                generateMessageForwardDeclarations(message)
            }
            appendLine()

            appendLine("namespace protobuf_helpers {")
            appendLine()

            // Generate enum conversions
            parsedFile.enums.forEach { enum ->
                appendEnumConversions(enum)
            }

            // Generate message conversions
            parsedFile.messages.forEach { message ->
                appendMessageConversions(message)
            }

            appendLine("} // namespace protobuf_helpers")
            appendLine()
            appendLine("#endif // $guardName")
        }

        outputFile.parentFile.mkdirs()
        outputFile.writeText(content)
    }

    fun generateImplementation(parsedFile: ParsedProtoFile, headerFile: File, outputFile: File) {
        val content = buildString {
            appendLine("/*")
            appendLine(" * Copyright (C) 2024 TomTom NV. All rights reserved.")
            appendLine(" *")
            appendLine(" * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be")
            appendLine(" * used for internal evaluation purposes or commercial use strictly subject to separate")
            appendLine(" * license agreement between you and TomTom NV. If you are the licensee, you are only permitted")
            appendLine(" * to use this software in accordance with the terms of your license agreement. If you are")
            appendLine(" * not the licensee, you are not authorized to use this software in any manner and should")
            appendLine(" * immediately return or destroy it.")
            appendLine(" */")
            appendLine()
            appendLine("// THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.")
            appendLine()
            appendLine("#include \"${headerFile.name}\"")
            appendLine()

            appendLine("namespace protobuf_helpers {")
            appendLine()

            // Generate enum implementations
            parsedFile.enums.forEach { enum ->
                appendEnumImplementations(enum, parsedFile.protoPackage)
            }

            // Generate message implementations
            parsedFile.messages.forEach { message ->
                appendMessageImplementations(message, parsedFile.protoPackage)
            }

            appendLine("} // namespace protobuf_helpers")
        }

        outputFile.parentFile.mkdirs()
        outputFile.writeText(content)
    }

    private fun StringBuilder.generateMessageForwardDeclarations(message: ParsedMessage, prefix: String = "") {
        val namespace = message.fullName.replace(".", "::")
        appendLine("namespace $namespace { class ${message.name}; }")

        message.nestedMessages.forEach { nested ->
            generateMessageForwardDeclarations(nested, "${prefix}${message.name}::")
        }
    }

    private fun StringBuilder.appendEnumConversions(enum: ParsedEnum) {
        val nativeType = "::${enum.fullName.replace(".", "::")}"
        val protoType = "::${enum.fullName.replace(".", "::")}"

        appendLine("// Enum conversions for ${enum.name}")
        appendLine("$nativeType toNative(const $protoType& proto);")
        appendLine("$protoType toProto(const $nativeType& native);")
        appendLine()
    }

    private fun StringBuilder.appendMessageConversions(message: ParsedMessage) {
        val nativeType = getNativeType(message)
        val protoType = "::${message.fullName.replace(".", "::")}"

        appendLine("// Message conversions for ${message.name}")
        appendLine("$nativeType toNative(const $protoType& proto);")
        appendLine("$protoType toProto(const $nativeType& native);")
        appendLine()

        // Generate nested conversions
        message.nestedEnums.forEach { appendEnumConversions(it) }
        message.nestedMessages.forEach { appendMessageConversions(it) }
    }

    private fun StringBuilder.appendEnumImplementations(enum: ParsedEnum, packageName: String) {
        val nativeType = "::${enum.fullName.replace(".", "::")}"
        val protoType = "::${enum.fullName.replace(".", "::")}"

        // toNative
        appendLine("$nativeType toNative(const $protoType& proto) {")
        appendLine("    return static_cast<$nativeType>(proto);")
        appendLine("}")
        appendLine()

        // toProto
        appendLine("$protoType toProto(const $nativeType& native) {")
        appendLine("    return static_cast<$protoType>(native);")
        appendLine("}")
        appendLine()
    }

    private fun StringBuilder.appendMessageImplementations(message: ParsedMessage, packageName: String) {
        val nativeType = getNativeType(message)
        val protoType = "::${message.fullName.replace(".", "::")}"

        // toNative
        appendLine("$nativeType toNative(const $protoType& proto) {")
        appendLine("    $nativeType native;")

        message.fields.forEach { field ->
            val fieldAccess = "proto.${field.protoName}()"
            when {
                field.isRepeated -> {
                    appendLine("    for (const auto& item : $fieldAccess) {")
                    if (field.isMessage || field.isEnum) {
                        appendLine("        native.${field.name}.push_back(toNative(item));")
                    } else {
                        appendLine("        native.${field.name}.push_back(item);")
                    }
                    appendLine("    }")
                }
                field.isMessage -> {
                    appendLine("    if (proto.has_${field.protoName}()) {")
                    appendLine("        native.${field.name} = toNative($fieldAccess);")
                    appendLine("    }")
                }
                field.isEnum -> {
                    appendLine("    native.${field.name} = toNative($fieldAccess);")
                }
                else -> {
                    appendLine("    native.${field.name} = $fieldAccess;")
                }
            }
        }

        appendLine("    return native;")
        appendLine("}")
        appendLine()

        // toProto
        appendLine("$protoType toProto(const $nativeType& native) {")
        appendLine("    $protoType proto;")

        message.fields.forEach { field ->
            when {
                field.isRepeated -> {
                    appendLine("    for (const auto& item : native.${field.name}) {")
                    if (field.isMessage || field.isEnum) {
                        appendLine("        *proto.add_${field.protoName}() = toProto(item);")
                    } else {
                        appendLine("        proto.add_${field.protoName}(item);")
                    }
                    appendLine("    }")
                }
                field.isMessage -> {
                    appendLine("    if (native.${field.name}.has_value()) {")
                    appendLine("        *proto.mutable_${field.protoName}() = toProto(native.${field.name}.value());")
                    appendLine("    }")
                }
                field.isEnum -> {
                    appendLine("    proto.set_${field.protoName}(toProto(native.${field.name}));")
                }
                else -> {
                    appendLine("    proto.set_${field.protoName}(native.${field.name});")
                }
            }
        }

        appendLine("    return proto;")
        appendLine("}")
        appendLine()

        // Generate nested implementations
        message.nestedEnums.forEach { appendEnumImplementations(it, packageName) }
        message.nestedMessages.forEach { appendMessageImplementations(it, packageName) }
    }

    private fun getNativeType(message: ParsedMessage): String {
        // Convert proto message name to native type
        // This is a simplified version - adjust based on your naming conventions
        return "::${message.fullName.replace(".", "::")}"
    }
}

