/*
 * © 2024 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

// AUTO-GENERATED FILE. DO NOT MODIFY.

@file:Suppress("detekt:TooManyFunctions")
@Suppress("detekt:TooManyFunctions")

package com.tomtom.sdk.navigation.junctionview.protos

import com.tomtom.sdk.navigation.junctionview.JunctionViewInformation
import com.tomtom.sdk.navigation.junctionview.JunctionViewType
import com.tomtom.sdk.navigation.junctionview.TimeOfDayType
import com.tomtom.sdk.navigation.junctionviewengine.common.internal.JunctionViewRequest
import com.tomtom.sdk.navigation.junctionview.protos.JunctionViewInformationOuterClass.JunctionViewDaylightType as ProtoJunctionViewDaylightType
import com.tomtom.sdk.navigation.junctionview.protos.JunctionViewInformationOuterClass.JunctionViewResult
import com.tomtom.sdk.navigation.junctionview.protos.JunctionViewInformationOuterClass.JunctionViewType as ProtoJunctionViewType

fun JunctionViewRequest.toProto(): com.tomtom.sdk.navigation.junctionview.protos.JunctionViewRequestOuterClass.JunctionViewRequest =
    com.tomtom.sdk.navigation.junctionview.protos.JunctionViewRequestOuterClass.JunctionViewRequest.newBuilder()
        .setRouteId(routeId.toString())
        .setRouteWindow(
            com.tomtom.sdk.navigation.junctionview.protos.JunctionViewRequestOuterClass.RouteWindow.newBuilder()
                .addAllRouteArcs(routeWindow.routeArcs.map { routeWindowArc ->
                    com.tomtom.sdk.navigation.junctionview.protos.JunctionViewRequestOuterClass.RouteArc.newBuilder()
                        .setArcKey(routeWindowArc.featureReference.featureId.toLong())
                        .setFeatureVersion(routeWindowArc.featureReference.featureVersion.toLong())
                        .setTailOffsetOnRouteInCentimeters(routeWindowArc.arcTailOffset.inWholeCentimeters().toInt())
                        .also { builder ->
                            routeWindowArc.arrivalOffsetOnArc?.let {
                                builder.setArrivalOffsetOnArcInCentimeters(it.inWholeCentimeters().toInt())
                            }
                        }
                        .build()
                })
                .build()
        )
        .setInstructionOffsetInMeters(instructionOffset.inWholeMeters().toInt())
        .build()

fun JunctionViewResult.toNative(): List<JunctionViewInformation>? =
    if (hasError()) {
        null
    } else {
        junctionViews.junctionViewInformationListOrBuilderList.map {
            JunctionViewInformation(
                image = it.dataPng.toByteArray(),
                type = it.type.toNative(),
                timeOfDayType = it.daylightType.toNative(),
                startRouteOffset = com.tomtom.quantity.Distance.centimeters(it.startRouteOffsetInCentimeters),
                endRouteOffset = com.tomtom.quantity.Distance.centimeters(it.endRouteOffsetInCentimeters),
            )
        }
    }

fun ProtoJunctionViewType.toNative(): JunctionViewType = when (this) {
    ProtoJunctionViewType.kJunction -> JunctionViewType.Junction
    ProtoJunctionViewType.kSignboard -> JunctionViewType.Signboard
    ProtoJunctionViewType.kEtc -> JunctionViewType.ElectronicTollCollection
    else -> throw IllegalArgumentException("Unexpected value $this.")
}

fun JunctionViewType.toProto(): ProtoJunctionViewType = when (this) {
    JunctionViewType.Junction -> ProtoJunctionViewType.kJunction
    JunctionViewType.Signboard -> ProtoJunctionViewType.kSignboard
    JunctionViewType.ElectronicTollCollection -> ProtoJunctionViewType.kEtc
    else -> throw IllegalArgumentException("Unexpected value $this.")
}

fun ProtoJunctionViewDaylightType.toNative(): TimeOfDayType = when (this) {
    ProtoJunctionViewDaylightType.kDay -> TimeOfDayType.Day
    ProtoJunctionViewDaylightType.kNight -> TimeOfDayType.Night
    ProtoJunctionViewDaylightType.kAlways -> TimeOfDayType.Any
    else -> throw IllegalArgumentException("Unexpected value $this.")
}

fun TimeOfDayType.toProto(): ProtoJunctionViewDaylightType = when (this) {
    TimeOfDayType.Day -> ProtoJunctionViewDaylightType.kDay
    TimeOfDayType.Night -> ProtoJunctionViewDaylightType.kNight
    TimeOfDayType.Any -> ProtoJunctionViewDaylightType.kAlways
    else -> throw IllegalArgumentException("Unexpected value $this.")
}

