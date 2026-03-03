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

package com.tomtom.sdk.navigation.junctionview.protos

import java.lang.IllegalArgumentException
import kotlin.Suppress

public fun JunctionViewType.toProto(): JunctionViewType = when (this) {
  JunctionViewType.KJUNCTION -> JunctionViewType.kJunction
  JunctionViewType.KSIGNBOARD -> JunctionViewType.kSignboard
  JunctionViewType.KETC -> JunctionViewType.kEtc
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun JunctionViewType.toNative(): JunctionViewType = when (this) {
  JunctionViewType.kJunction -> JunctionViewType.KJUNCTION
  JunctionViewType.kSignboard -> JunctionViewType.KSIGNBOARD
  JunctionViewType.kEtc -> JunctionViewType.KETC
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun JunctionViewDaylightType.toProto(): JunctionViewDaylightType = when (this) {
  JunctionViewDaylightType.KDAY -> JunctionViewDaylightType.kDay
  JunctionViewDaylightType.KNIGHT -> JunctionViewDaylightType.kNight
  JunctionViewDaylightType.KALWAYS -> JunctionViewDaylightType.kAlways
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun JunctionViewDaylightType.toNative(): JunctionViewDaylightType = when (this) {
  JunctionViewDaylightType.kDay -> JunctionViewDaylightType.KDAY
  JunctionViewDaylightType.kNight -> JunctionViewDaylightType.KNIGHT
  JunctionViewDaylightType.kAlways -> JunctionViewDaylightType.KALWAYS
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun JunctionViewInformationList.toProto(): JunctionViewInformationList =
    junctionViewInformationList {
  addAllJunctionViewInformationList(this@toProto.junctionViewInformationList.map { it })
}

public fun JunctionViewInformationList.toNative(): JunctionViewInformationList =
    JunctionViewInformationList {
  junctionViewInformationList = this@toNative.junction_view_information_listList
}

public fun JunctionViewInformation.toProto(): JunctionViewInformation = junctionViewInformation {
  data_png = this@toProto.dataPng
  type = this@toProto.type.toProto()
  daylight_type = this@toProto.daylightType.toProto()
  start_route_offset_in_centimeters = this@toProto.startRouteOffsetInCentimeters
  end_route_offset_in_centimeters = this@toProto.endRouteOffsetInCentimeters
}

public fun JunctionViewInformation.toNative(): JunctionViewInformation = JunctionViewInformation {
  dataPng = this@toNative.data_png
  type = this@toNative.type.toNative()
  daylightType = this@toNative.daylight_type.toNative()
  startRouteOffsetInCentimeters = this@toNative.start_route_offset_in_centimeters
  endRouteOffsetInCentimeters = this@toNative.end_route_offset_in_centimeters
}

public fun ErrorType.toProto(): ErrorType = when (this) {
  ErrorType.KARCVERSIONMISMATCH -> ErrorType.kArcVersionMismatch
  ErrorType.KMOREARCSREQUIRED -> ErrorType.kMoreArcsRequired
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun ErrorType.toNative(): ErrorType = when (this) {
  ErrorType.kArcVersionMismatch -> ErrorType.KARCVERSIONMISMATCH
  ErrorType.kMoreArcsRequired -> ErrorType.KMOREARCSREQUIRED
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun JunctionViewError.toProto(): JunctionViewError = junctionViewError {
  errorType = this@toProto.errorType.toProto()
  message = this@toProto.message
}

public fun JunctionViewError.toNative(): JunctionViewError = JunctionViewError {
  errorType = this@toNative.errorType.toNative()
  message = this@toNative.message
}

public fun JunctionViewResult.toProto(): JunctionViewResult = junctionViewResult {
  when {
    this@toProto.junctionViews != null -> junction_views = this@toProto.junctionViews!!.toProto()
    this@toProto.error != null -> error = this@toProto.error!!.toProto()
  }
}

public fun JunctionViewResult.toNative(): JunctionViewResult = JunctionViewResult {
  Result = when (this@toNative.ResultCase) {
    JunctionViewResult.ResultCase.JUNCTIONVIEWS -> this@toNative.junction_views.toNative()
    JunctionViewResult.ResultCase.ERROR -> this@toNative.error.toNative()
    else -> null
  }
}
