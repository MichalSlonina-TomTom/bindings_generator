// © 2026 TomTom NV. All rights reserved.
//
// This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
// used for internal evaluation purposes or commercial use strictly subject to separate
// license agreement between you and TomTom NV. If you are the licensee, you are only permitted
// to use this software in accordance with the terms of your license agreement. If you are
// not the licensee, you are not authorized to use this software in any manner and should
// immediately return or destroy it.
//
// AUTO-GENERATED FILE. DO NOT MODIFY.
//
@file:Suppress("detekt:TooManyFunctions")
@Suppress("detekt:TooManyFunctions")

package com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos

import kotlin.Suppress

public fun Language.toProto(): Language = language {
  iso_language_code = this@toProto.isoLanguageCode
  iso_country_code = this@toProto.isoCountryCode
  iso_script_code = this@toProto.isoScriptCode
}

public fun Language.toNative(): Language = Language {
  isoLanguageCode = this@toNative.iso_language_code
  isoCountryCode = this@toNative.iso_country_code
  isoScriptCode = this@toNative.iso_script_code
}
