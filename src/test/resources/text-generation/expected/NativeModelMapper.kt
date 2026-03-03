/*
 * © 2022 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

@file:Suppress("detekt:TooManyFunctions")

package com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure

import com.tomtom.sdk.featuretoggle.FeatureToggleController
import com.tomtom.sdk.featuretoggle.InternalFeatureToggleApi
import com.tomtom.sdk.location.DrivingSide
import com.tomtom.sdk.navigation.featuretoggle.ImprovedDirectionalInformationFeature
import com.tomtom.sdk.navigation.verbalmessagegeneration.AnnouncementData
import com.tomtom.sdk.navigation.verbalmessagegeneration.AnnouncementData.AnnouncementInstruction
import com.tomtom.sdk.navigation.verbalmessagegeneration.GuidanceUnitSystem
import com.tomtom.sdk.navigation.verbalmessagegeneration.MessageType
import com.tomtom.sdk.navigation.verbalmessagegeneration.VerbosityLevel
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionKt
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.chargingStopData
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.trafficEventData
import com.tomtom.sdk.navigation.verbalmessagegeneration.warnings.ChargerType
import com.tomtom.sdk.navigation.verbalmessagegeneration.warnings.ChargingStopData
import com.tomtom.sdk.navigation.verbalmessagegeneration.warnings.ChargingStopMessageType
import com.tomtom.sdk.navigation.verbalmessagegeneration.warnings.DynamicRouteGuidanceData
import com.tomtom.sdk.navigation.verbalmessagegeneration.warnings.Mode
import com.tomtom.sdk.navigation.verbalmessagegeneration.warnings.Reason
import com.tomtom.sdk.navigation.verbalmessagegeneration.warnings.TrafficEventData
import com.tomtom.sdk.navigation.verbalmessagegeneration.warnings.TrafficEventType
import com.tomtom.sdk.navigation.verbalmessagegeneration.warnings.WarningData
import com.tomtom.sdk.navigation.verbalmessagegeneration.warnings.WarningMessageType
import com.tomtom.sdk.routing.common.BetaTrifurcationApi
import com.tomtom.sdk.routing.common.ExperimentalContinueInterimInstructionApi
import com.tomtom.sdk.routing.route.Signpost
import com.tomtom.sdk.routing.route.instruction.Instruction
import com.tomtom.sdk.routing.route.instruction.PhoneticString
import com.tomtom.sdk.routing.route.instruction.Road
import com.tomtom.sdk.routing.route.instruction.RoadAttribute
import com.tomtom.sdk.routing.route.instruction.RoadIdentifier
import com.tomtom.sdk.routing.route.instruction.RoadIdentifierSource
import com.tomtom.sdk.routing.route.instruction.RoadType
import com.tomtom.sdk.routing.route.instruction.TextWithPhonetics
import com.tomtom.sdk.routing.route.instruction.arrival.ArrivalInstruction
import com.tomtom.sdk.routing.route.instruction.autotransport.AutoTransportType
import com.tomtom.sdk.routing.route.instruction.autotransport.EnterAutoTransportInstruction
import com.tomtom.sdk.routing.route.instruction.autotransport.ExitAutoTransportInstruction
import com.tomtom.sdk.routing.route.instruction.bordercrossing.BorderCrossingInstruction
import com.tomtom.sdk.routing.route.instruction.carpoollane.CarpoolLaneDirection
import com.tomtom.sdk.routing.route.instruction.carpoollane.EnterCarpoolLaneInstruction
import com.tomtom.sdk.routing.route.instruction.carpoollane.ExitCarpoolLaneInstruction
import com.tomtom.sdk.routing.route.instruction.common.BorderCrossing
import com.tomtom.sdk.routing.route.instruction.common.Country
import com.tomtom.sdk.routing.route.instruction.common.ExitDirection2
import com.tomtom.sdk.routing.route.instruction.common.ItineraryPointRelativePosition
import com.tomtom.sdk.routing.route.instruction.common.Landmark
import com.tomtom.sdk.routing.route.instruction.common.QuantizedTurnAngle
import com.tomtom.sdk.routing.route.instruction.common.TurnDirection
import com.tomtom.sdk.routing.route.instruction.departure.DepartureInstruction
import com.tomtom.sdk.routing.route.instruction.fork.ForkDirection2
import com.tomtom.sdk.routing.route.instruction.fork.ForkInstruction
import com.tomtom.sdk.routing.route.instruction.highway.ExitHighwayInstruction
import com.tomtom.sdk.routing.route.instruction.highway.SwitchHighwayInstruction
import com.tomtom.sdk.routing.route.instruction.interim.ContinueInterimInstruction
import com.tomtom.sdk.routing.route.instruction.mandatoryturn.MandatoryTurnInstruction
import com.tomtom.sdk.routing.route.instruction.merge.MergeInstruction
import com.tomtom.sdk.routing.route.instruction.merge.MergeSide
import com.tomtom.sdk.routing.route.instruction.roundabout.ExitRoundaboutInstruction
import com.tomtom.sdk.routing.route.instruction.roundabout.RoundaboutInstruction
import com.tomtom.sdk.routing.route.instruction.roundabout.RoundaboutType
import com.tomtom.sdk.routing.route.instruction.tollgate.TollgateInstruction
import com.tomtom.sdk.routing.route.instruction.turn.TurnInstruction
import com.tomtom.sdk.routing.route.instruction.turnaroundwhenpossible.TurnAroundWhenPossibleInstruction
import com.tomtom.sdk.routing.route.instruction.waypoint.WaypointInstruction
import com.tomtom.sdk.vehicle.ChargingInformation
import com.tomtom.sdk.vehicle.ExperimentalChargingParkApi
import java.util.Locale
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.AudioInstruction as ProtoAudioInstruction
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.AudioInstruction.Lane as ProtoLane
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.AudioInstruction.Lane.LaneDirection as ProtoLaneDirection
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.AudioInstructionType as ProtoAudioInstructionType
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.AutoTransportType as ProtoAutoTransportType
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.BorderCrossing as ProtoBorderCrossing
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.ChargingStop as ProtoChargingStop
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.DrivingSide as ProtoDrivingSide
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.EnterHovDirection as ProtoEnterHovDirection
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.ExitDirection as ProtoExitDirection
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.ExitHovDirection as ProtoExitHovDirection
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.ExitRoundabout as ProtoExitRoundabout
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.ForkDirection as ProtoForkDirection
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.ItineraryPointSide as ProtoItineraryPointSide
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.Landmark as ProtoLandmark
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.MergeSide as ProtoMergeSide
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.PhoneticString as ProtoPhoneticString
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.QuantizedAngle as ProtoQuantizedAngle
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.RoadInformation as ProtoRoad
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.Roundabout as ProtoRoundabout
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.RoundaboutDirection as ProtoRoundaboutDirection
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.RoundaboutType as ProtoRoundaboutType
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.Signpost as ProtoSignpost
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.SwitchHighwayDirection as ProtoSwitchHighwayDirection
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.TextWithPhonetic as ProtoTextWithPhonetic
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.AudioInstructionOuterClass.TurnDirection as ProtoTurnDirection
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.LanguageOuterClass.Language as ProtoLanguage
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration.AnnouncementData as ProtoAnnouncementData
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration.ChargingStopData as ProtoChargingStopData
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration.ChargingStopData.ChargerType as ProtoChargerType
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration.ChargingStopData.Type as ProtoType
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration.DynamicRouteGuidanceData as ProtoDynamicRouteGuidanceData
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration.DynamicRouteGuidanceData.Mode as ProtoMode
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration.DynamicRouteGuidanceData.Reason as ProtoReason
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration.DynamicRouteGuidanceData.WarningMessageType as ProtoWarningMessageType
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration.UnitSystem as ProtoUnitSystem
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration.VerbosityLevel as ProtoVerbosityLevel
import com.tomtom.sdk.navigation.verbalmessagegeneration.infrastructure.protos.TextGeneration.WarningData as ProtoWarningData

private fun MessageType.convertMessageType(): ProtoAnnouncementData.MessageType = when (this) {
    MessageType.Confirmation -> ProtoAnnouncementData.MessageType.kConfirmation
    MessageType.Follow -> ProtoAnnouncementData.MessageType.kFollow
    MessageType.Faraway -> ProtoAnnouncementData.MessageType.kFarAway
    MessageType.Early -> ProtoAnnouncementData.MessageType.kWarning
    MessageType.Main -> ProtoAnnouncementData.MessageType.kMain
    MessageType.ExtendedConfirmation -> ProtoAnnouncementData.MessageType.kExtendedConfirmation
    else -> ProtoAnnouncementData.MessageType.kMain
}

internal fun createProtoAnnouncementData(announcementData: AnnouncementData): ProtoAnnouncementData =
    with(announcementData) {
        ProtoAnnouncementData.newBuilder()
            .setMessageType(messageType.convertMessageType())
            .setDistanceInCm(distance.inCentimeters().toInt())
            .setInstruction(instruction.convertInstruction())
            .also {
                nextInstruction?.convertInstruction()?.let(it::setNextInstruction)
            }
            .build()
    }

internal fun createProtoWarningData(warningData: WarningData): ProtoWarningData {
    val builder = ProtoWarningData.newBuilder()
    when (warningData) {
        is WarningData.DynamicRouteGuidanceWarningData -> builder.setDynamicRouteGuidanceData(
            warningData.dynamicRouteGuidanceData.toProto(),
        )

        is WarningData.ChargingStopWarningData -> builder.setChargingStopData(
            warningData.chargingStopData.toProto(),
        )

        is WarningData.TrafficEventWarningData -> builder.setTrafficEventData(
            warningData.trafficEventData.toProto(),
        )
    }
    return builder.build()
}

internal fun DynamicRouteGuidanceData.toProto(): ProtoDynamicRouteGuidanceData {
    val builder = ProtoDynamicRouteGuidanceData.newBuilder()
    builder.setReason(reason.toProto())
        .setMode(mode.toProto())
        .setTravelTimeAdvantageSeconds(travelTimeAdvantageInSeconds)
        .setTravelDelaySeconds(travelDelayInSeconds)
        .setEstimatedTravelDurationSeconds(estimatedTravelDurationInSeconds)
        .setIsChargingPlanModified(chargingPlanModified)
    currentTime?.let {
        builder.setCurrentTimeMilliseconds(it)
    }
    warningMessageType?.let {
        builder.setWarningMessageType(it.toProto())
    }
    return builder.build()
}

internal fun Reason.toProto(): ProtoReason = when (this) {
    Reason.RouteBlockage -> ProtoReason.kRouteBlockage
    Reason.RouteUnreachable -> ProtoReason.kRouteUnreachable
    Reason.RouteDelay -> ProtoReason.kRouteDelay
    Reason.AltRoute -> ProtoReason.kAltRoute
    Reason.AltRouteTTA -> ProtoReason.kAltRouteTTA
    Reason.AltRouteTTADueToDelay -> ProtoReason.kAltRouteTTADueToDelay
    Reason.AltRouteDueToBlockage -> ProtoReason.kAltRouteDueToBlockage
    Reason.AltRouteDueToUnreachable -> ProtoReason.kAltRouteDueToUnreachable
    else -> throw IllegalArgumentException("Unsupported reason type")
}

internal fun Mode.toProto(): ProtoMode = when (this) {
    Mode.Automatic -> ProtoMode.kAutomatic
    Mode.SemiDynamic -> ProtoMode.kSemiDynamic
    else -> throw IllegalArgumentException("Unsupported mode")
}

internal fun WarningMessageType.toProto(): ProtoWarningMessageType = when (this) {
    WarningMessageType.Early -> ProtoWarningMessageType.kEarly
    WarningMessageType.Accepting -> ProtoWarningMessageType.kAccepting
    WarningMessageType.Approaching -> ProtoWarningMessageType.kApproaching
    WarningMessageType.ApproachingExtended -> ProtoWarningMessageType.kApproachingExtended
    else -> throw IllegalArgumentException("Unsupported warning message type")
}

internal fun Locale.toProtoLanguage(): ProtoLanguage = ProtoLanguage.newBuilder()
    .setIsoLanguageCode(isO3Language)
    .setIsoCountryCode(runCatching { isO3Country }.getOrElse { "" })
    .setIsoScriptCode(script)
    .build()

internal fun GuidanceUnitSystem.toProtoUnitSystem(): ProtoUnitSystem = ProtoUnitSystem.newBuilder()
    .setUnitSystem(
        when (this) {
            GuidanceUnitSystem.Metric -> ProtoUnitSystem.Unit.kMetric
            GuidanceUnitSystem.Us -> ProtoUnitSystem.Unit.kImperialNorthAmerica
            GuidanceUnitSystem.Uk -> ProtoUnitSystem.Unit.kImperialUK
            else -> ProtoUnitSystem.Unit.kMetric
        },
    )
    .build()

internal fun VerbosityLevel.toProtoVerbosityLevel(): ProtoVerbosityLevel = ProtoVerbosityLevel.newBuilder()
    .setLevel(
        when (this) {
            VerbosityLevel.Compact -> ProtoVerbosityLevel.Verbosity.kCompact
            else -> ProtoVerbosityLevel.Verbosity.kComprehensive
        },
    )
    .build()

private fun AnnouncementData.LaneDirection.toProto(): ProtoLaneDirection = when (this) {
    AnnouncementData.LaneDirection.Straight -> ProtoLaneDirection.kStraight
    AnnouncementData.LaneDirection.Right -> ProtoLaneDirection.kRight
    AnnouncementData.LaneDirection.Left -> ProtoLaneDirection.kLeft
    AnnouncementData.LaneDirection.SlightLeft -> ProtoLaneDirection.kSlightLeft
    AnnouncementData.LaneDirection.SlightRight -> ProtoLaneDirection.kSlightRight
    AnnouncementData.LaneDirection.SharpLeft -> ProtoLaneDirection.kSharpLeft
    AnnouncementData.LaneDirection.SharpRight -> ProtoLaneDirection.kSharpRight
    AnnouncementData.LaneDirection.LeftUTurn -> ProtoLaneDirection.kLeftUTurn
    AnnouncementData.LaneDirection.RightUTurn -> ProtoLaneDirection.kRightUTurn
    else -> throw IllegalArgumentException("Unsupported LaneDirection.")
}

private fun AnnouncementData.Lane.toProto(): ProtoLane = AudioInstructionKt.lane {
    this@toProto.followDirection?.let { followDirection = it.toProto() }
    directions.addAll(this@toProto.directions.map { it.toProto() })
}

private fun AnnouncementInstruction.convertInstruction() = instruction?.let {
    with(this.instruction) {
        ProtoAudioInstruction.newBuilder()
            .setType(convertInstructionType())
            .setDrivingSide(drivingSide.convertDrivingSide())
            .setItineraryPointSide(convertItineraryPointSide())
            .also {
                if (this is TurnInstruction) {
                    sideStreetOffset?.inCentimeters()?.toInt()
                        ?.let(it::setSideStreetOffsetInCentimeters)
                }
                previousSignificantRoad?.convertRoad()?.let(it::setIncomingRoadInformation)
                nextSignificantRoad?.convertRoad()?.let(it::setOutgoingRoadInformation)
                landmark?.convertLandmark()?.let(it::setLandmark)
                signpost?.convertSignpost()?.let(it::setSignpost)
                intersectionName?.convertTextWithPhonetic()?.let(it::setIntersectionNameWithPhonetic)
                convertExitDirection()?.let(it::setExitDirection)
                convertForkDirection()?.let(it::setForkDirection)
                convertTurnDirection()?.let(it::setTurnDirection)
                convertSwitchHighwayDirection()?.let(it::setSwitchHighwayDirection)
                convertEnterHovDirection()?.let(it::setEnterHovDirection)
                convertExitHovDirection()?.let(it::setExitHovDirection)
                convertMergeSide()?.let(it::setMergeSide)
                convertRoundabout()?.let(it::setRoundabout)
                convertExitRoundabout()?.let(it::setExitRoundabout)
                convertBorderCrossing()?.let(it::setBorderCrossing)
                convertAutoTransportType()?.let(it::setAutoTransportType)
                laneGuidance?.map { it.toProto() }?.let(it::addAllLaneGuidance)
                val routeStop = when (this) {
                    is WaypointInstruction -> routeStop
                    is ArrivalInstruction -> routeStop
                    else -> null
                }
                routeStop?.chargingInformation?.convertChargingInformation()?.let(it::setChargingStop)
            }
            .build()
    }
}

private fun Instruction.convertItineraryPointSide(): ProtoItineraryPointSide = when (this) {
    is ArrivalInstruction -> arrivalSide.convertItineraryPointSide()
    is WaypointInstruction -> waypointSide.convertItineraryPointSide()
    else -> ProtoItineraryPointSide.kItineraryPointSideUnknown
}

private fun ItineraryPointRelativePosition.convertItineraryPointSide(): ProtoItineraryPointSide = when (this) {
    ItineraryPointRelativePosition.Left -> ProtoItineraryPointSide.kItineraryPointSideLeft
    ItineraryPointRelativePosition.Right -> ProtoItineraryPointSide.kItineraryPointSideRight
    else -> ProtoItineraryPointSide.kItineraryPointSideUnknown
}

private fun Instruction.convertAutoTransportType(): ProtoAutoTransportType? = when (this) {
    is EnterAutoTransportInstruction -> autoTransportType.convertAutoTransportType()
    is ExitAutoTransportInstruction -> autoTransportType.convertAutoTransportType()
    else -> null
}

private fun AutoTransportType.convertAutoTransportType(): ProtoAutoTransportType = when (this) {
    AutoTransportType.CarTrain -> ProtoAutoTransportType.kCartrain
    else -> ProtoAutoTransportType.kFerry
}

private fun Instruction.convertBorderCrossing(): ProtoBorderCrossing? = when (this) {
    is BorderCrossingInstruction -> borderCrossing.convertBorderCrossing()
    is EnterAutoTransportInstruction -> borderCrossing?.convertBorderCrossing()
    is ExitAutoTransportInstruction -> borderCrossing?.convertBorderCrossing()
    else -> null
}

private fun Country.convertCountry(): ProtoBorderCrossing.CountryInfo = ProtoBorderCrossing.CountryInfo.newBuilder()
    .setIsoCountryCode(code)
    .setCountryName(name.convertTextWithPhonetic())
    .build()

private fun BorderCrossing.convertBorderCrossing(): ProtoBorderCrossing = ProtoBorderCrossing.newBuilder()
    .setFromCountry(fromCountry.convertCountry())
    .setToCountry(toCountry.convertCountry())
    .build()

private fun Instruction.convertRoundabout(): ProtoRoundabout? = if (this is RoundaboutInstruction) {
    ProtoRoundabout.newBuilder()
        .setAngleInDegrees(turnAngle.inWholeDegrees().toInt())
        .also {
            exitNumber?.let(it::setExitNumber)
            quantizedTurnAngle.convertDirection()?.let(it::setDirection)
            quantizedTurnAngle.convertTurnAngle()?.let(it::setTurnAngle)
            roundaboutType.convertRoundaboutType()?.let(it::setRoundaboutType)
        }
        .build()
} else {
    null
}

@Suppress("DEPRECATION")
private fun Instruction.convertExitRoundabout(): ProtoExitRoundabout? = if (this is ExitRoundaboutInstruction) {
    val roundabout =
        ProtoRoundabout.newBuilder()
            .setAngleInDegrees(turnAngle.inWholeDegrees().toInt())
            .also {
                exitNumber?.let(it::setExitNumber)
                quantizedTurnAngle.convertDirection()?.let(it::setDirection)
                quantizedTurnAngle.convertTurnAngle()?.let(it::setTurnAngle)
            }
            .build()

    ProtoExitRoundabout.newBuilder()
        .setRoundabout(roundabout)
        .build()
} else {
    null
}

private fun QuantizedTurnAngle.convertDirection(): ProtoRoundaboutDirection? = when (this) {
    QuantizedTurnAngle.Back -> ProtoRoundaboutDirection.kRoundaboutDirectionExitBack
    QuantizedTurnAngle.GoStraight -> ProtoRoundaboutDirection.kRoundaboutDirectionExitCross
    QuantizedTurnAngle.SlightRight,
    QuantizedTurnAngle.SharpRight,
    QuantizedTurnAngle.Right,
    -> ProtoRoundaboutDirection.kRoundaboutDirectionExitRight
    QuantizedTurnAngle.SlightLeft,
    QuantizedTurnAngle.SharpLeft,
    QuantizedTurnAngle.Left,
    -> ProtoRoundaboutDirection.kRoundaboutDirectionExitLeft
    else -> null
}

private fun QuantizedTurnAngle.convertTurnAngle(): ProtoQuantizedAngle? = when (this) {
    QuantizedTurnAngle.Back -> ProtoQuantizedAngle.kBack
    QuantizedTurnAngle.GoStraight -> ProtoQuantizedAngle.kStraight
    QuantizedTurnAngle.SlightRight -> ProtoQuantizedAngle.kSlightRight
    QuantizedTurnAngle.Right -> ProtoQuantizedAngle.kRight
    QuantizedTurnAngle.SharpRight -> ProtoQuantizedAngle.kSharpRight
    QuantizedTurnAngle.SlightLeft -> ProtoQuantizedAngle.kSlightLeft
    QuantizedTurnAngle.Left -> ProtoQuantizedAngle.kLeft
    QuantizedTurnAngle.SharpLeft -> ProtoQuantizedAngle.kSharpLeft
    else -> null
}

private fun RoundaboutType.convertRoundaboutType(): ProtoRoundaboutType? = when (this) {
    RoundaboutType.Default -> ProtoRoundaboutType.kDefault
    RoundaboutType.Small -> ProtoRoundaboutType.kSmall
    else -> null
}

private fun Instruction.convertMergeSide(): ProtoMergeSide? = when ((this as? MergeInstruction)?.mergeSide) {
    MergeSide.TO_LEFT_LANE -> ProtoMergeSide.kMergeToLeftLane
    MergeSide.TO_RIGHT_LANE -> ProtoMergeSide.kMergeToRightLane
    else -> null
}

private fun Instruction.convertEnterHovDirection(): ProtoEnterHovDirection? =
    when ((this as? EnterCarpoolLaneInstruction)?.carpoolLaneDirection) {
        CarpoolLaneDirection.EnterOnLeft -> ProtoEnterHovDirection.kEnterHovLeft
        CarpoolLaneDirection.EnterOnRight -> ProtoEnterHovDirection.kEnterHovRight
        else -> null
    }

private fun Instruction.convertExitHovDirection(): ProtoExitHovDirection? =
    when ((this as? ExitCarpoolLaneInstruction)?.carpoolLaneDirection) {
        CarpoolLaneDirection.ExitOnLeft -> ProtoExitHovDirection.kExitHovLeft
        CarpoolLaneDirection.ExitOnRight -> ProtoExitHovDirection.kExitHovRight
        else -> null
    }

private fun Instruction.convertTurnDirection(): ProtoTurnDirection? {
    val turnDirection =
        when (this) {
            is TurnInstruction -> turnDirection
            is MandatoryTurnInstruction -> turnDirection
            else -> null
        }
    return when (turnDirection) {
        TurnDirection.GoStraight -> ProtoTurnDirection.kTurnDirectionGoStraight
        TurnDirection.BearRight -> ProtoTurnDirection.kTurnDirectionBearRight
        TurnDirection.TurnRight -> ProtoTurnDirection.kTurnDirectionTurnRight
        TurnDirection.SharpRight -> ProtoTurnDirection.kTurnDirectionSharpRight
        TurnDirection.BearLeft -> ProtoTurnDirection.kTurnDirectionBearLeft
        TurnDirection.TurnLeft -> ProtoTurnDirection.kTurnDirectionTurnLeft
        TurnDirection.SharpLeft -> ProtoTurnDirection.kTurnDirectionSharpLeft
        TurnDirection.TurnAround -> ProtoTurnDirection.kTurnDirectionTurnAround
        else -> null
    }
}

@OptIn(BetaTrifurcationApi::class, ExperimentalContinueInterimInstructionApi::class)
private fun Instruction.convertForkDirection(): ProtoForkDirection? = when (this) {
    is ForkInstruction -> when (forkDirection2) {
        ForkDirection2.Left -> ProtoForkDirection.kForkDirectionLeft
        ForkDirection2.Right -> ProtoForkDirection.kForkDirectionRight
        ForkDirection2.Middle -> ProtoForkDirection.kForkDirectionMiddle
        else -> null
    }

    is ContinueInterimInstruction -> when (forkDirection2) {
        ForkDirection2.Left -> ProtoForkDirection.kForkDirectionLeft
        ForkDirection2.Right -> ProtoForkDirection.kForkDirectionRight
        ForkDirection2.Middle -> ProtoForkDirection.kForkDirectionMiddle
        else -> null
    }

    else -> null
}

@OptIn(BetaTrifurcationApi::class)
private fun Instruction.convertExitDirection(): ProtoExitDirection? =
    when ((this as? ExitHighwayInstruction)?.exitDirection2) {
        ExitDirection2.Left -> ProtoExitDirection.kExitDirectionLeft
        ExitDirection2.Right -> ProtoExitDirection.kExitDirectionRight
        ExitDirection2.Middle -> ProtoExitDirection.kExitDirectionMiddle
        else -> null
    }

@OptIn(BetaTrifurcationApi::class)
private fun Instruction.convertSwitchHighwayDirection(): ProtoSwitchHighwayDirection? =
    when ((this as? SwitchHighwayInstruction)?.exitDirection2) {
        ExitDirection2.Left -> ProtoSwitchHighwayDirection.kSwitchHighwayLeft
        ExitDirection2.Right -> ProtoSwitchHighwayDirection.kSwitchHighwayRight
        ExitDirection2.Middle -> ProtoSwitchHighwayDirection.kSwitchHighwayMiddle
        else -> null
    }

private fun Signpost.convertSignpost(): ProtoSignpost? {
    return ProtoSignpost.newBuilder()
        .also {
            exitNumber?.convertTextWithPhonetic()?.let(it::setExitNumber)
            exitName?.convertTextWithPhonetic()?.let(it::setExitName)
            towardName?.convertTextWithPhonetic()?.let(it::setTowardName)
        }
        .build()
}

private fun Landmark.convertLandmark(): ProtoLandmark? = when (this) {
    Landmark.EndOfRoad -> ProtoLandmark.kEndOfRoad
    Landmark.AtTrafficLight -> ProtoLandmark.kAtTrafficLight
    Landmark.OntoBridge -> ProtoLandmark.kOnToBridge
    Landmark.OnBridge -> ProtoLandmark.kOnBridge
    Landmark.AfterBridge -> ProtoLandmark.kAfterBridge
    Landmark.IntoTunnel -> ProtoLandmark.kIntoTunnel
    Landmark.InsideTunnel -> ProtoLandmark.kInsideTunnel
    Landmark.AfterTunnel -> ProtoLandmark.kAfterTunnel
    else -> null
}

@OptIn(InternalFeatureToggleApi::class)
private fun Road.convertRoad(): ProtoRoad = ProtoRoad.newBuilder()
    .addAllRoadNumbers(numberIdentifiers.map { it.convertToRoadIdentifier() })
    .addAllRoadNames(nameIdentifiers.map { it.convertToRoadIdentifier() })
    .setIsUrbanArea(types.contains(RoadType.Urban))
    .setIsControlledAccess(types.contains(RoadType.ControlledAccess))
    .setIsMotorway(types.contains(RoadType.Motorway))
    .also {
        if (FeatureToggleController.isEnabled(ImprovedDirectionalInformationFeature)) {
            it.setCountryCode(isoCountryCode)
        }
    }
    .build()

private fun RoadAttribute.convertToRoadAttribute(): AudioInstructionOuterClass.RoadAttribute = when (this) {
    RoadAttribute.NONE -> AudioInstructionOuterClass.RoadAttribute.kNone
    RoadAttribute.NATIONAL_ROAD -> AudioInstructionOuterClass.RoadAttribute.kNationalRoad
    RoadAttribute.PREFECTURAL_ROAD -> AudioInstructionOuterClass.RoadAttribute.kPrefecturalRoad
    RoadAttribute.EXPRESSWAY -> AudioInstructionOuterClass.RoadAttribute.kExpressway
    RoadAttribute.COUNTY_ROAD -> AudioInstructionOuterClass.RoadAttribute.kCountyRoad
    RoadAttribute.NATIONAL_HIGHWAY -> AudioInstructionOuterClass.RoadAttribute.kNationalHighway
    RoadAttribute.PROVINCIAL_HIGHWAY -> AudioInstructionOuterClass.RoadAttribute.kProvincialHighway
    RoadAttribute.PROVINCIAL_ROAD -> AudioInstructionOuterClass.RoadAttribute.kProvincialRoad
    RoadAttribute.TOWNSHIP_ROAD -> AudioInstructionOuterClass.RoadAttribute.kTownshipRoad
    RoadAttribute.TOKYO_PREFECTURAL_ROAD -> AudioInstructionOuterClass.RoadAttribute.kTokyoPrefecturalRoad
    RoadAttribute.HOKKAIDO_PREFECTURAL_ROAD -> AudioInstructionOuterClass.RoadAttribute.kHokkaidoPrefecturalRoad
    RoadAttribute.OSAKA_AND_KYOTO_PREFECTURAL_ROAD,
    -> AudioInstructionOuterClass.RoadAttribute.kOsakaAndKyotoPrefecturalRoad
}

private fun RoadIdentifierSource.convertToRoadIdentifierSource(): AudioInstructionOuterClass.RoadIdentifierSource =
    when (this) {
        RoadIdentifierSource.SIGNPOST -> AudioInstructionOuterClass.RoadIdentifierSource.kSignpost
        RoadIdentifierSource.ROAD -> AudioInstructionOuterClass.RoadIdentifierSource.kRoad
    }

private fun RoadIdentifier.convertToRoadIdentifier(): AudioInstructionOuterClass.RoadIdentifier {
    val builder = AudioInstructionOuterClass.RoadIdentifier.newBuilder()
    builder.setIdentifier(this.identifier.convertTextWithPhonetic())
    builder.setRoadAttribute(this.attribute.convertToRoadAttribute())
    builder.setSource(this.source.convertToRoadIdentifierSource())
    return builder.build()
}

private fun TextWithPhonetics.convertTextWithPhonetic(): ProtoTextWithPhonetic = ProtoTextWithPhonetic.newBuilder()
    .setText(plainText)
    .setTextLanguage(plainTextLanguage.toProtoLanguage())
    .also {
        phoneticString?.convertPhoneticString()?.let(it::setGenericUseCasePhoneticString)
    }
    .build()

private fun PhoneticString.convertPhoneticString(): ProtoPhoneticString = ProtoPhoneticString.newBuilder()
    .setAlphabet(alphabet)
    .setLanguage(language.toProtoLanguage())
    .setValue(value)
    .build()

@OptIn(ExperimentalContinueInterimInstructionApi::class)
@Suppress("detekt:ComplexMethod")
private fun Instruction.convertInstructionType(): ProtoAudioInstructionType = when (this) {
    is ArrivalInstruction -> ProtoAudioInstructionType.kInstructionTypeArrival
    is DepartureInstruction -> ProtoAudioInstructionType.kInstructionTypeDeparture
    is WaypointInstruction -> ProtoAudioInstructionType.kInstructionTypeWaypoint
    is MandatoryTurnInstruction -> ProtoAudioInstructionType.kInstructionTypeObligatoryTurn
    is TurnInstruction -> ProtoAudioInstructionType.kInstructionTypeTurn
    is RoundaboutInstruction -> ProtoAudioInstructionType.kInstructionTypeRoundabout
    is ExitRoundaboutInstruction -> ProtoAudioInstructionType.kInstructionTypeExitRoundabout
    is BorderCrossingInstruction -> ProtoAudioInstructionType.kInstructionTypeBorderCrossing
    is TurnAroundWhenPossibleInstruction -> ProtoAudioInstructionType.kInstructionTypeTurnAroundWhenPossible
    is EnterAutoTransportInstruction -> ProtoAudioInstructionType.kInstructionTypeEntryAutoTransport
    is ExitAutoTransportInstruction -> ProtoAudioInstructionType.kInstructionTypeExitAutoTransport
    is SwitchHighwayInstruction -> ProtoAudioInstructionType.kInstructionTypeSwitchHighway
    is ExitHighwayInstruction -> ProtoAudioInstructionType.kInstructionTypeExit
    is ForkInstruction -> ProtoAudioInstructionType.kInstructionTypeFork
    is TollgateInstruction -> ProtoAudioInstructionType.kInstructionTypeTollgate
    is EnterCarpoolLaneInstruction -> ProtoAudioInstructionType.kInstructionTypeEnterHov
    is ExitCarpoolLaneInstruction -> ProtoAudioInstructionType.kInstructionTypeExitHov
    is MergeInstruction -> ProtoAudioInstructionType.kInstructionTypeMerge
    is ContinueInterimInstruction -> ProtoAudioInstructionType.kInstructionTypeContinueInterim
    else -> ProtoAudioInstructionType.kInstructionTypeExitHov
}

private fun DrivingSide.convertDrivingSide(): ProtoDrivingSide = when (this) {
    DrivingSide.LEFT -> ProtoDrivingSide.kDrivingSideLeft
    else -> ProtoDrivingSide.kDrivingSideRight
}

private fun ChargingStopData.toProto(): ProtoChargingStopData {
    return chargingStopData {
        type = when (this@toProto.chargingStopMessageType) {
            ChargingStopMessageType.ALL_CHARGING_STOPS_CHANGED -> ProtoType.ALL_CHARGING_STOPS_CHANGED
            ChargingStopMessageType.NEXT_CHARGING_STOP_REPLACED -> ProtoType.NEXT_CHARGING_STOP_REPLACED
            ChargingStopMessageType.NEXT_CHARGING_STOP_REMOVED -> ProtoType.NEXT_CHARGING_STOP_REMOVED
            ChargingStopMessageType.NEXT_CHARGING_STOP_ADDED -> ProtoType.NEXT_CHARGING_STOP_ADDED
            ChargingStopMessageType.RETURN_TO_LAST_CHARGING_STOP -> ProtoType.RETURN_TO_LAST_CHARGING_STOP
        }
        chargerType = when (this@toProto.chargerType) {
            ChargerType.FAST -> ProtoChargerType.FAST
            else -> ProtoChargerType.DEFAULT
        }
        this@toProto.chargerOperatorName?.let {
            chargerOperatorName = it.convertTextWithPhonetic()
        }
        this@toProto.locationName?.let {
            locationName = it.convertTextWithPhonetic()
        }
    }
}

@OptIn(ExperimentalChargingParkApi::class)
private fun ChargingInformation.convertChargingInformation(): ProtoChargingStop = with(ProtoChargingStop.newBuilder()) {
    chargePointOperatorName?.let {
        operatorName = TextWithPhonetics(it).convertTextWithPhonetic()
    }
    build()
}

private fun TrafficEventData.toProto(): TextGeneration.TrafficEventData {
    return trafficEventData {
        trafficEventType = when (this@toProto.trafficEventType) {
            TrafficEventType.TRAFFIC_JAM -> TextGeneration.TrafficEventData.TrafficEventType.TRAFFIC_JAM
        }
        this@toProto.roadNumber?.let {
            roadNumber = it.convertTextWithPhonetic()
        }
        this@toProto.roadName?.let {
            roadName = it.convertTextWithPhonetic()
        }
        this@toProto.startRoadName?.let {
            startRoadName = it.convertTextWithPhonetic()
        }
        this@toProto.endRoadName?.let {
            endRoadName = it.convertTextWithPhonetic()
        }
        this@toProto.startExitNumber?.let {
            startExitNumber = it.convertTextWithPhonetic()
        }
        this@toProto.endExitNumber?.let {
            endExitNumber = it.convertTextWithPhonetic()
        }
        this@toProto.travelDelayInSeconds?.let {
            travelDelaySeconds = it
        }
    }
}
