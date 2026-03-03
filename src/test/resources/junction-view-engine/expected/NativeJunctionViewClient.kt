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
import com.tomtom.sdk.navigation.junctionviewengine.common.internal.JunctionViewRequest

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
                val responseBytes = generateJunctionViews(nativeJunctionViewClientHandle, request.toProto().toByteArray())
                JunctionViewResult.parseFrom(responseBytes).toNative()
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
}
