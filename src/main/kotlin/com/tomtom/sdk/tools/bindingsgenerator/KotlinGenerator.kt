package com.tomtom.sdk.tools.bindingsgenerator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File

/**
 * Generates Kotlin NativeModelMapper extensions
 */
class KotlinGenerator {

    fun generateMapper(parsedFile: ParsedProtoFile, outputFile: File) {
        val fileSpec = FileSpec.builder(
            packageName = getKotlinPackageName(parsedFile.protoPackage),
            fileName = "NativeModelMapper"
        ).apply {
            addFileComment("Copyright (C) 2024 TomTom NV. All rights reserved.")
            addFileComment("")
            addFileComment("This software is the proprietary copyright of TomTom NV and its subsidiaries and may be")
            addFileComment("used for internal evaluation purposes or commercial use strictly subject to separate")
            addFileComment("license agreement between you and TomTom NV. If you are the licensee, you are only permitted")
            addFileComment("to use this software in accordance with the terms of your license agreement. If you are")
            addFileComment("not the licensee, you are not authorized to use this software in any manner and should")
            addFileComment("immediately return or destroy it.")
            addFileComment("")
            addFileComment("THIS FILE IS AUTO-GENERATED. DO NOT MODIFY.")

            addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .addMember("%S", "TooManyFunctions")
                    .addMember("%S", "LongMethod")
                    .addMember("%S", "CyclomaticComplexMethod")
                    .build()
            )

            // Generate enum extensions
            parsedFile.enums.forEach { enum ->
                addEnumExtensions(enum, parsedFile.protoPackage)
            }

            // Generate message extensions
            parsedFile.messages.forEach { message ->
                addMessageExtensions(message, parsedFile.protoPackage)
            }

        }.build()

        outputFile.parentFile.mkdirs()
        fileSpec.writeTo(outputFile.parentFile)
    }

    private fun FileSpec.Builder.addEnumExtensions(enum: ParsedEnum, protoPackage: String) {
        val protoClassName = ClassName.bestGuess(enum.fullName)
        val nativeClassName = getNativeEnumClassName(enum)

        // toProto extension
        addFunction(
            FunSpec.builder("toProto")
                .receiver(nativeClassName)
                .returns(protoClassName)
                .addCode(buildCodeBlock {
                    beginControlFlow("return when (this)")
                    enum.values.forEach { value ->
                        val nativeValue = convertEnumValueToNative(value.name)
                        addStatement("${nativeClassName.simpleName}.$nativeValue -> ${protoClassName.simpleName}.${value.name}")
                    }
                    endControlFlow()
                })
                .build()
        )

        // toNative extension
        addFunction(
            FunSpec.builder("toNative")
                .receiver(protoClassName)
                .returns(nativeClassName)
                .addCode(buildCodeBlock {
                    beginControlFlow("return when (this)")
                    enum.values.forEach { value ->
                        val nativeValue = convertEnumValueToNative(value.name)
                        addStatement("${protoClassName.simpleName}.${value.name} -> ${nativeClassName.simpleName}.$nativeValue")
                    }
                    addStatement("${protoClassName.simpleName}.UNRECOGNIZED -> throw IllegalArgumentException(%S)",
                        "Unrecognized proto enum value: \$this")
                    endControlFlow()
                })
                .build()
        )
    }

    private fun FileSpec.Builder.addMessageExtensions(message: ParsedMessage, protoPackage: String) {
        val protoClassName = ClassName.bestGuess(message.fullName)
        val nativeClassName = getNativeMessageClassName(message)

        // toProto extension
        addFunction(
            FunSpec.builder("toProto")
                .receiver(nativeClassName)
                .returns(protoClassName)
                .addCode(buildCodeBlock {
                    addStatement("return %L {", getProtoBuilderName(message.name))
                    indent()

                    message.fields.forEach { field ->
                        generateToProtoFieldMapping(field)
                    }

                    unindent()
                    addStatement("}")
                })
                .build()
        )

        // toNative extension
        addFunction(
            FunSpec.builder("toNative")
                .receiver(protoClassName)
                .returns(nativeClassName)
                .addCode(buildCodeBlock {
                    addStatement("return %T(", nativeClassName)
                    indent()

                    message.fields.forEachIndexed { index, field ->
                        val comma = if (index < message.fields.size - 1) "," else ""
                        generateToNativeFieldMapping(field, comma)
                    }

                    unindent()
                    addStatement(")")
                })
                .build()
        )

        // Generate nested extensions
        message.nestedEnums.forEach { addEnumExtensions(it, protoPackage) }
        message.nestedMessages.forEach { addMessageExtensions(it, protoPackage) }
    }

    private fun CodeBlock.Builder.generateToProtoFieldMapping(field: ParsedField) {
        val fieldName = field.name
        when {
            field.isRepeated -> {
                if (field.isMessage || field.isEnum) {
                    addStatement("$fieldName.addAll(this@toProto.$fieldName.map { it.toProto() })")
                } else {
                    addStatement("$fieldName.addAll(this@toProto.$fieldName)")
                }
            }
            field.isMessage -> {
                addStatement("this@toProto.$fieldName?.let { $fieldName = it.toProto() }")
            }
            field.isEnum -> {
                addStatement("$fieldName = this@toProto.$fieldName.toProto()")
            }
            field.type == "string" -> {
                addStatement("$fieldName = this@toProto.$fieldName")
            }
            else -> {
                addStatement("$fieldName = this@toProto.$fieldName")
            }
        }
    }

    private fun CodeBlock.Builder.generateToNativeFieldMapping(field: ParsedField, comma: String) {
        val fieldName = field.name
        when {
            field.isRepeated -> {
                if (field.isMessage || field.isEnum) {
                    addStatement("$fieldName = ${fieldName}List.map { it.toNative() }$comma")
                } else {
                    addStatement("$fieldName = ${fieldName}List$comma")
                }
            }
            field.isMessage -> {
                addStatement("$fieldName = if (has${fieldName.capitalize()}()) $fieldName.toNative() else null$comma")
            }
            field.isEnum -> {
                addStatement("$fieldName = $fieldName.toNative()$comma")
            }
            else -> {
                addStatement("$fieldName = $fieldName$comma")
            }
        }
    }

    private fun getKotlinPackageName(protoPackage: String): String {
        // Convert proto package to Kotlin package
        return protoPackage.replace(".", ".").lowercase()
    }

    private fun getNativeEnumClassName(enum: ParsedEnum): ClassName {
        // This should be adjusted based on your actual native Kotlin class location
        val parts = enum.fullName.split(".")
        val packageName = parts.dropLast(1).joinToString(".")
        val className = convertEnumNameToNative(parts.last())
        return ClassName(packageName, className)
    }

    private fun getNativeMessageClassName(message: ParsedMessage): ClassName {
        // This should be adjusted based on your actual native Kotlin class location
        val parts = message.fullName.split(".")
        val packageName = parts.dropLast(1).joinToString(".")
        val className = parts.last()
        return ClassName(packageName, className)
    }

    private fun getProtoBuilderName(messageName: String): String {
        // Convert message name to builder function name (camelCase)
        return messageName.replaceFirstChar { it.lowercase() }
    }

    private fun convertEnumValueToNative(protoValue: String): String {
        // Convert kEnumValue to ENUM_VALUE
        if (protoValue.startsWith("k")) {
            return protoValue.substring(1)
                .replace(Regex("([a-z])([A-Z])"), "$1_$2")
                .uppercase()
        }
        return protoValue
    }

    private fun convertEnumNameToNative(protoName: String): String {
        // Keep the same name for enums
        return protoName
    }

    private fun String.capitalize(): String {
        return replaceFirstChar { it.uppercase() }
    }
}

