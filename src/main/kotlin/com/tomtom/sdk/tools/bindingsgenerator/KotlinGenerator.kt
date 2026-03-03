package com.tomtom.sdk.tools.bindingsgenerator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File
import java.time.Year

private fun copyrightHeader() = """
© ${Year.now().value} TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.

AUTO-GENERATED FILE. DO NOT MODIFY.
""".trimStart()

class KotlinGenerator {

    fun generateMapper(parsedFile: ParsedProtoFile, outputDir: File) {
        val kotlinPackage = getKotlinPackageName(parsedFile.protoPackage)
        val fileName = "NativeModelMapper"

        val fileSpec = FileSpec.builder(kotlinPackage, fileName)
            .addFileComment(copyrightHeader())
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .addMember("%S", "detekt:TooManyFunctions")
                    .build()
            )
            .apply {
                addEnumExtensions(parsedFile.enums, parsedFile.protoPackage)
                addMessageExtensions(parsedFile.messages, parsedFile.protoPackage, parsedFile.enums)
            }
            .build()

        outputDir.mkdirs()
        fileSpec.writeTo(outputDir)
        // Post-process: add plain @Suppress annotation for compatibility (in addition to @file:Suppress)
        val generatedFile = outputDir.walkTopDown().find { it.name == "$fileName.kt" }
        generatedFile?.let { file ->
            val content = file.readText()
            if (!content.contains("\n@Suppress(")) {
                file.writeText(content.replace(
                    "@file:Suppress(\"detekt:TooManyFunctions\")",
                    "@file:Suppress(\"detekt:TooManyFunctions\")\n@Suppress(\"detekt:TooManyFunctions\")"
                ))
            }
        }
    }

    private fun getKotlinPackageName(protoPackage: String): String {
        return protoPackage
    }

    private fun FileSpec.Builder.addEnumExtensions(
        enums: List<ParsedEnum>,
        protoPackage: String
    ) {
        enums.forEach { enum ->
            addFunction(buildToProtoEnumFun(enum, protoPackage))
            addFunction(buildToNativeEnumFun(enum, protoPackage))
        }
    }

    private fun FileSpec.Builder.addMessageExtensions(
        messages: List<ParsedMessage>,
        protoPackage: String,
        knownEnums: List<ParsedEnum>
    ) {
        messages.forEach { message ->
            val allEnums = knownEnums + message.nestedEnums
            addEnumExtensions(message.nestedEnums, protoPackage)
            addFunction(buildToProtoMessageFun(message, protoPackage, allEnums))
            addFunction(buildToNativeMessageFun(message, protoPackage, allEnums))
            addMessageExtensions(message.nestedMessages, protoPackage, allEnums)
        }
    }

    private fun buildToProtoEnumFun(enum: ParsedEnum, protoPackage: String): FunSpec {
        val nativeClassName = getNativeEnumClassName(enum, protoPackage)
        val protoClassName = ClassName(protoPackage, enum.name)

        val codeBlock = CodeBlock.builder()
            .beginControlFlow("return when (this)")
            .apply {
                enum.values.forEach { value ->
                    val nativeValue = convertEnumValueToNative(value.name, enum.name)
                    addStatement("%T.%L -> %T.%L", nativeClassName, nativeValue, protoClassName, value.name)
                }
                addStatement("else -> throw %T(%S + this)", IllegalArgumentException::class, "Unexpected value ")
            }
            .endControlFlow()
            .build()

        return FunSpec.builder("toProto")
            .receiver(nativeClassName)
            .returns(protoClassName)
            .addCode(codeBlock)
            .build()
    }

    private fun buildToNativeEnumFun(enum: ParsedEnum, protoPackage: String): FunSpec {
        val nativeClassName = getNativeEnumClassName(enum, protoPackage)
        val protoClassName = ClassName(protoPackage, enum.name)

        val codeBlock = CodeBlock.builder()
            .beginControlFlow("return when (this)")
            .apply {
                enum.values.forEach { value ->
                    val nativeValue = convertEnumValueToNative(value.name, enum.name)
                    addStatement("%T.%L -> %T.%L", protoClassName, value.name, nativeClassName, nativeValue)
                }
                addStatement("else -> throw %T(%S + this)", IllegalArgumentException::class, "Unexpected value ")
            }
            .endControlFlow()
            .build()

        return FunSpec.builder("toNative")
            .receiver(protoClassName)
            .returns(nativeClassName)
            .addCode(codeBlock)
            .build()
    }

    private fun buildToProtoMessageFun(
        message: ParsedMessage,
        protoPackage: String,
        knownEnums: List<ParsedEnum>
    ): FunSpec {
        val nativeClassName = getNativeMessageClassName(message, protoPackage)
        val protoBuilderName = getProtoBuilderName(message, protoPackage)

        val bodyCode = CodeBlock.builder()

        bodyCode.beginControlFlow("return %L", protoBuilderName)

        message.fields.forEach { field ->
            generateToProtoFieldMapping(bodyCode, field, protoPackage, knownEnums, "this@toProto")
        }

        bodyCode.endControlFlow()

        return FunSpec.builder("toProto")
            .receiver(nativeClassName)
            .returns(ClassName(protoPackage, message.name))
            .addCode(bodyCode.build())
            .build()
    }

    private fun buildToNativeMessageFun(
        message: ParsedMessage,
        protoPackage: String,
        knownEnums: List<ParsedEnum>
    ): FunSpec {
        val nativeClassName = getNativeMessageClassName(message, protoPackage)
        val protoClassName = ClassName(protoPackage, message.name)

        val bodyCode = CodeBlock.builder()

        bodyCode.beginControlFlow("return %T", nativeClassName)

        message.fields.forEach { field ->
            generateToNativeFieldMapping(bodyCode, field, protoPackage, knownEnums, "this@toNative")
        }

        bodyCode.endControlFlow()

        return FunSpec.builder("toNative")
            .receiver(protoClassName)
            .returns(nativeClassName)
            .addCode(bodyCode.build())
            .build()
    }

    private fun generateToProtoFieldMapping(
        builder: CodeBlock.Builder,
        field: ParsedField,
        protoPackage: String,
        knownEnums: List<ParsedEnum>,
        receiver: String
    ) {
        when {
            field.isRepeated -> {
                builder.addStatement(
                    "addAll%L(%L.%L.map { it })",
                    field.name.replaceFirstChar { it.uppercase() },
                    receiver,
                    field.name
                )
            }
            field.isEnum -> {
                builder.addStatement(
                    "%L = %L.%L.toProto()",
                    field.protoName,
                    receiver,
                    field.name
                )
            }
            field.isMessage -> {
                builder.addStatement(
                    "%L = %L.%L.toProto()",
                    field.protoName,
                    receiver,
                    field.name
                )
            }
            else -> {
                builder.addStatement(
                    "%L = %L.%L",
                    field.protoName,
                    receiver,
                    field.name
                )
            }
        }
    }

    private fun generateToNativeFieldMapping(
        builder: CodeBlock.Builder,
        field: ParsedField,
        protoPackage: String,
        knownEnums: List<ParsedEnum>,
        receiver: String
    ) {
        when {
            field.isRepeated -> {
                builder.addStatement(
                    "%L = %L.%LList",
                    field.name,
                    receiver,
                    field.protoName
                )
            }
            field.isEnum -> {
                builder.addStatement(
                    "%L = %L.%L.toNative()",
                    field.name,
                    receiver,
                    field.protoName
                )
            }
            field.isMessage -> {
                builder.addStatement(
                    "%L = %L.%L.toNative()",
                    field.name,
                    receiver,
                    field.protoName
                )
            }
            else -> {
                builder.addStatement(
                    "%L = %L.%L",
                    field.name,
                    receiver,
                    field.protoName
                )
            }
        }
    }

    private fun getNativeEnumClassName(enum: ParsedEnum, protoPackage: String): ClassName {
        // Native classes are in the same or related package
        return ClassName(protoPackage, enum.name)
    }

    private fun getNativeMessageClassName(message: ParsedMessage, protoPackage: String): ClassName {
        return ClassName(protoPackage, message.name)
    }

    private fun getProtoBuilderName(message: ParsedMessage, protoPackage: String): String {
        val simpleName = message.name
            .split(".")
            .last()
            .replaceFirstChar { it.lowercase() }
        return simpleName
    }

    private fun convertEnumValueToNative(protoValueName: String, enumName: String): String {
        val prefix = "k${enumName}"
        return if (protoValueName.startsWith(prefix)) {
            protoValueName.substring(prefix.length).uppercase()
        } else {
            protoValueName.uppercase()
        }
    }

    private fun convertEnumNameToNative(enumName: String): String {
        return enumName
    }
}

