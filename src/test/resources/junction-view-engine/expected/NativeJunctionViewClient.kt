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

// TODO (GOSDK-178737): Remove @Suppress() after internalizing StoreAccess.
@file:Suppress("DEPRECATION")

package com.tomtom.sdk.navigation.junctionviewengine.common

import com.tomtom.quantity.Distance
import com.tomtom.sdk.annotations.junctionview.BetaJunctionViewApi
import com.tomtom.sdk.datamanagement.datastore.StoreAccess
import com.tomtom.sdk.datamanagement.mapreferences.MapPosition
import com.tomtom.sdk.datamanagement.mapreferences.unified.UnifiedMapPosition
import com.tomtom.sdk.datamanagement.nds.NdsStoreAccess
import com.tomtom.sdk.logging.logger.Logger
import com.tomtom.sdk.nativelibraryloader.NativeLibraryLoader
import com.tomtom.sdk.navigation.junctionview.JunctionViewInformation
import com.tomtom.sdk.navigation.junctionview.JunctionViewType
import com.tomtom.sdk.navigation.junctionview.TimeOfDayType
import com.tomtom.sdk.navigation.junctionview.protos.JunctionViewInformationOuterClass.JunctionViewResult
import com.tomtom.sdk.navigation.junctionview.protos.junctionViewRequest
import com.tomtom.sdk.navigation.junctionview.protos.routeArc
import com.tomtom.sdk.navigation.junctionview.protos.routeWindow
import com.tomtom.sdk.navigation.junctionviewengine.common.internal.JunctionViewRequest
import com.tomtom.sdk.navigation.junctionview.protos.JunctionViewInformationOuterClass.JunctionViewDaylightType as ProtoJunctionViewDaylightType
import com.tomtom.sdk.navigation.junctionview.protos.JunctionViewInformationOuterClass.JunctionViewType as ProtoJunctionViewType

/**
 * Native [JunctionViewClient] implementation.
 */
internal class NativeJunctionViewClient : JunctionViewClient {
    private val lock = Any()
    private var dataStoreAccess: StoreAccess? = null

    @OptIn(BetaJunctionViewApi::class)
    override fun generateJunctionViews(request: JunctionViewRequest): List<JunctionViewInformation>? =
        synchronized(lock) {
            if (nativeJunctionViewClientHandle != 0L) {
                generateJunctionViews(nativeJunctionViewClientHandle, request.toProto()).fromProto()
            } else {
                Logger.i(TAG) {
                    "Do not generate junction views as the handle is invalid. Most likely the client has been closed."
                }
                null
            }
        }

    override fun adaptToDirectMapAccessHandle(storeAccess: NdsStoreAccess) = synchronized(lock) {
        destroy(nativeJunctionViewClientHandle)
        dataStoreAccess = storeAccess
        nativeJunctionViewClientHandle = createWithDirectMapAccess(storeAccess.handleId)
    }

    override fun adaptTo(storeAccess: StoreAccess) = synchronized(lock) {
        destroy(nativeJunctionViewClientHandle)
        dataStoreAccess = storeAccess
        nativeJunctionViewClientHandle = createWithUnifiedMapAccess(storeAccess.factoryHandleId)
    }

    override fun toUnifiedMapPosition(mapPosition: MapPosition): UnifiedMapPosition? {
        return dataStoreAccess?.lock("$TAG.toUnifiedMapPosition")?.use { lockedAccess ->
            lockedAccess.toUnifiedMapPosition(mapPosition)
        }
    }

    override fun close() = synchronized(lock) {
        if (nativeJunctionViewClientHandle != 0L) {
            destroy(nativeJunctionViewClientHandle)
            nativeJunctionViewClientHandle = 0L
        }
    }

    companion object {
        init {
            NativeLibraryLoader.load("tomtom-navsdk")
        }

        private val TAG = NativeJunctionViewClient::class
    }

    private var nativeJunctionViewClientHandle: Long = 0L

    external fun generateJunctionViews(
        nativeClientHandle: Long,
        request: ByteArray,
    ): ByteArray

    private external fun createWithDirectMapAccess(ndsStoreAccess: Long): Long

    private external fun createWithUnifiedMapAccess(ndsStoreAccess: Long): Long

    private external fun destroy(address: Long)

    private fun JunctionViewRequest.toProto(): ByteArray {
        return junctionViewRequest {
            routeId = this@toProto.routeId.toString()
            routeWindow = routeWindow {
                routeArcs += this@toProto.routeWindow.routeArcs.map { routeWindowArc ->
                    routeArc {
                        arcKey = routeWindowArc.featureReference.featureId.toLong()
                        featureVersion = routeWindowArc.featureReference.featureVersion.toLong()
                        tailOffsetOnRouteInCentimeters = routeWindowArc.arcTailOffset.inWholeCentimeters().toInt()
                        routeWindowArc.arrivalOffsetOnArc?.let {
                            arrivalOffsetOnArcInCentimeters = it.inWholeCentimeters().toInt()
                        }
                    }
                }
            }
            instructionOffsetInMeters = this@toProto.instructionOffset.inWholeMeters().toInt()
        }.toByteArray()
    }

    @OptIn(BetaJunctionViewApi::class)
    fun ByteArray.fromProto(): List<JunctionViewInformation>? {
        val junctionViewResult = JunctionViewResult.parseFrom(this)
        return if (junctionViewResult.hasError()) {
            Logger.w(TAG) {
                "Failed to generate junction views with the following error: ${junctionViewResult.error.message}"
            }
            null
        } else {
            junctionViewResult.junctionViews.junctionViewInformationListOrBuilderList.map {
                JunctionViewInformation(
                    image = it.dataPng.toByteArray(),
                    type = it.type.convertJunctionViewType(),
                    timeOfDayType = it.daylightType.convertJunctionViewDaylightType(),
                    startRouteOffset = Distance.centimeters(it.startRouteOffsetInCentimeters),
                    endRouteOffset = Distance.Companion.centimeters(it.endRouteOffsetInCentimeters),
                )
            }
        }
    }

    @OptIn(BetaJunctionViewApi::class)
    private fun ProtoJunctionViewType.convertJunctionViewType() = when (this) {
        ProtoJunctionViewType.kJunction -> JunctionViewType.Junction
        ProtoJunctionViewType.kSignboard -> JunctionViewType.Signboard
        ProtoJunctionViewType.kEtc -> JunctionViewType.ElectronicTollCollection
        ProtoJunctionViewType.UNRECOGNIZED ->
            throw IllegalArgumentException("Unexpected junction view type $this.")
    }

    @OptIn(BetaJunctionViewApi::class)
    private fun ProtoJunctionViewDaylightType.convertJunctionViewDaylightType() = when (this) {
        ProtoJunctionViewDaylightType.kDay -> TimeOfDayType.Day
        ProtoJunctionViewDaylightType.kNight -> TimeOfDayType.Night
        ProtoJunctionViewDaylightType.kAlways -> TimeOfDayType.Any
        ProtoJunctionViewDaylightType.UNRECOGNIZED ->
            throw IllegalArgumentException("Unexpected junction view time of day type $this.")
    }
}
