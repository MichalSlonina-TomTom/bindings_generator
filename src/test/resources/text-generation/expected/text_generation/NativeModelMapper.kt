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

import java.lang.IllegalArgumentException
import kotlin.Suppress

public fun AudioMessage.toProto(): AudioMessage = audioMessage {
  text = this@toProto.text
  locale = this@toProto.locale
}

public fun AudioMessage.toNative(): AudioMessage = AudioMessage {
  text = this@toNative.text
  locale = this@toNative.locale
}

public fun MessageType.toProto(): MessageType = when (this) {
  MessageType.KFOLLOW -> MessageType.kFollow
  MessageType.KFARAWAY -> MessageType.kFarAway
  MessageType.KWARNING -> MessageType.kWarning
  MessageType.KMAIN -> MessageType.kMain
  MessageType.KCONFIRMATION -> MessageType.kConfirmation
  MessageType.KEXTENDEDCONFIRMATION -> MessageType.kExtendedConfirmation
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun MessageType.toNative(): MessageType = when (this) {
  MessageType.kFollow -> MessageType.KFOLLOW
  MessageType.kFarAway -> MessageType.KFARAWAY
  MessageType.kWarning -> MessageType.KWARNING
  MessageType.kMain -> MessageType.KMAIN
  MessageType.kConfirmation -> MessageType.KCONFIRMATION
  MessageType.kExtendedConfirmation -> MessageType.KEXTENDEDCONFIRMATION
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun AnnouncementData.toProto(): AnnouncementData = announcementData {
  distance_in_cm = this@toProto.distanceInCm
  message_type = this@toProto.messageType.toProto()
  instruction = this@toProto.instruction.toProto()
  next_instruction = this@toProto.nextInstruction.toProto()
}

public fun AnnouncementData.toNative(): AnnouncementData = AnnouncementData {
  distanceInCm = this@toNative.distance_in_cm
  messageType = this@toNative.message_type.toNative()
  instruction = this@toNative.instruction.toNative()
  nextInstruction = this@toNative.next_instruction.toNative()
}

public fun Verbosity.toProto(): Verbosity = when (this) {
  Verbosity.KCOMPREHENSIVE -> Verbosity.kComprehensive
  Verbosity.KCOMPACT -> Verbosity.kCompact
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun Verbosity.toNative(): Verbosity = when (this) {
  Verbosity.kComprehensive -> Verbosity.KCOMPREHENSIVE
  Verbosity.kCompact -> Verbosity.KCOMPACT
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun VerbosityLevel.toProto(): VerbosityLevel = verbosityLevel {
  level = this@toProto.level.toProto()
}

public fun VerbosityLevel.toNative(): VerbosityLevel = VerbosityLevel {
  level = this@toNative.level.toNative()
}

public fun Unit.toProto(): Unit = when (this) {
  Unit.KMETRIC -> Unit.kMetric
  Unit.KIMPERIALUK -> Unit.kImperialUK
  Unit.KIMPERIALNORTHAMERICA -> Unit.kImperialNorthAmerica
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun Unit.toNative(): Unit = when (this) {
  Unit.kMetric -> Unit.KMETRIC
  Unit.kImperialUK -> Unit.KIMPERIALUK
  Unit.kImperialNorthAmerica -> Unit.KIMPERIALNORTHAMERICA
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun UnitSystem.toProto(): UnitSystem = unitSystem {
  unit_system = this@toProto.unitSystem.toProto()
}

public fun UnitSystem.toNative(): UnitSystem = UnitSystem {
  unitSystem = this@toNative.unit_system.toNative()
}

public fun Specification.toProto(): Specification = when (this) {
  Specification.KDEFAULT -> Specification.kDefault
  Specification.KHCP3 -> Specification.kHcp3
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun Specification.toNative(): Specification = when (this) {
  Specification.kDefault -> Specification.KDEFAULT
  Specification.kHcp3 -> Specification.KHCP3
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun RoundingSpecification.toProto(): RoundingSpecification = roundingSpecification {
  rounding_specification = this@toProto.roundingSpecification.toProto()
}

public fun RoundingSpecification.toNative(): RoundingSpecification = RoundingSpecification {
  roundingSpecification = this@toNative.rounding_specification.toNative()
}

public fun WarningData.toProto(): WarningData = warningData {
  when {
    this@toProto.dynamicRouteGuidanceData != null -> dynamicRouteGuidanceData =
        this@toProto.dynamicRouteGuidanceData!!.toProto()
    this@toProto.ChargingStopData != null -> ChargingStopData =
        this@toProto.ChargingStopData!!.toProto()
    this@toProto.trafficEventData != null -> trafficEventData =
        this@toProto.trafficEventData!!.toProto()
  }
}

public fun WarningData.toNative(): WarningData = WarningData {
  WarningDataVariant = when (this@toNative.WarningDataVariantCase) {
    WarningData.WarningDataVariantCase.DYNAMICROUTEGUIDANCEDATA ->
        this@toNative.dynamicRouteGuidanceData.toNative()
    WarningData.WarningDataVariantCase.CHARGINGSTOPDATA -> this@toNative.ChargingStopData.toNative()
    WarningData.WarningDataVariantCase.TRAFFICEVENTDATA -> this@toNative.trafficEventData.toNative()
    else -> null
  }
}

public fun Reason.toProto(): Reason = when (this) {
  Reason.KROUTEBLOCKAGE -> Reason.kRouteBlockage
  Reason.KROUTEUNREACHABLE -> Reason.kRouteUnreachable
  Reason.KROUTEDELAY -> Reason.kRouteDelay
  Reason.KALTROUTE -> Reason.kAltRoute
  Reason.KALTROUTETTA -> Reason.kAltRouteTTA
  Reason.KALTROUTETTADUETODELAY -> Reason.kAltRouteTTADueToDelay
  Reason.KALTROUTEDUETOBLOCKAGE -> Reason.kAltRouteDueToBlockage
  Reason.KALTROUTEDUETOUNREACHABLE -> Reason.kAltRouteDueToUnreachable
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun Reason.toNative(): Reason = when (this) {
  Reason.kRouteBlockage -> Reason.KROUTEBLOCKAGE
  Reason.kRouteUnreachable -> Reason.KROUTEUNREACHABLE
  Reason.kRouteDelay -> Reason.KROUTEDELAY
  Reason.kAltRoute -> Reason.KALTROUTE
  Reason.kAltRouteTTA -> Reason.KALTROUTETTA
  Reason.kAltRouteTTADueToDelay -> Reason.KALTROUTETTADUETODELAY
  Reason.kAltRouteDueToBlockage -> Reason.KALTROUTEDUETOBLOCKAGE
  Reason.kAltRouteDueToUnreachable -> Reason.KALTROUTEDUETOUNREACHABLE
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun Mode.toProto(): Mode = when (this) {
  Mode.KAUTOMATIC -> Mode.kAutomatic
  Mode.KSEMIDYNAMIC -> Mode.kSemiDynamic
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun Mode.toNative(): Mode = when (this) {
  Mode.kAutomatic -> Mode.KAUTOMATIC
  Mode.kSemiDynamic -> Mode.KSEMIDYNAMIC
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun WarningMessageType.toProto(): WarningMessageType = when (this) {
  WarningMessageType.KEARLY -> WarningMessageType.kEarly
  WarningMessageType.KAPPROACHING -> WarningMessageType.kApproaching
  WarningMessageType.KAPPROACHINGEXTENDED -> WarningMessageType.kApproachingExtended
  WarningMessageType.KACCEPTING -> WarningMessageType.kAccepting
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun WarningMessageType.toNative(): WarningMessageType = when (this) {
  WarningMessageType.kEarly -> WarningMessageType.KEARLY
  WarningMessageType.kApproaching -> WarningMessageType.KAPPROACHING
  WarningMessageType.kApproachingExtended -> WarningMessageType.KAPPROACHINGEXTENDED
  WarningMessageType.kAccepting -> WarningMessageType.KACCEPTING
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun DynamicRouteGuidanceData.toProto(): DynamicRouteGuidanceData = dynamicRouteGuidanceData {
  reason = this@toProto.reason.toProto()
  mode = this@toProto.mode.toProto()
  travel_time_advantage_seconds = this@toProto.travelTimeAdvantageSeconds
  travel_delay_seconds = this@toProto.travelDelaySeconds
  estimated_travel_duration_seconds = this@toProto.estimatedTravelDurationSeconds
  is_charging_plan_modified = this@toProto.isChargingPlanModified
  this@toProto.currentTimeMilliseconds?.let { current_time_milliseconds = it }
  warning_message_type = this@toProto.warningMessageType.toProto()
}

public fun DynamicRouteGuidanceData.toNative(): DynamicRouteGuidanceData =
    DynamicRouteGuidanceData {
  reason = this@toNative.reason.toNative()
  mode = this@toNative.mode.toNative()
  travelTimeAdvantageSeconds = this@toNative.travel_time_advantage_seconds
  travelDelaySeconds = this@toNative.travel_delay_seconds
  estimatedTravelDurationSeconds = this@toNative.estimated_travel_duration_seconds
  isChargingPlanModified = this@toNative.is_charging_plan_modified
  currentTimeMilliseconds = if (this@toNative.hasCurrentTimeMilliseconds())
      this@toNative.current_time_milliseconds else null
  warningMessageType = this@toNative.warning_message_type.toNative()
}

public fun Type.toProto(): Type = when (this) {
  Type.NEXT_CHARGING_STOP_REPLACED -> Type.NEXT_CHARGING_STOP_REPLACED
  Type.ALL_CHARGING_STOPS_CHANGED -> Type.ALL_CHARGING_STOPS_CHANGED
  Type.NEXT_CHARGING_STOP_REMOVED -> Type.NEXT_CHARGING_STOP_REMOVED
  Type.NEXT_CHARGING_STOP_ADDED -> Type.NEXT_CHARGING_STOP_ADDED
  Type.RETURN_TO_LAST_CHARGING_STOP -> Type.RETURN_TO_LAST_CHARGING_STOP
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun Type.toNative(): Type = when (this) {
  Type.NEXT_CHARGING_STOP_REPLACED -> Type.NEXT_CHARGING_STOP_REPLACED
  Type.ALL_CHARGING_STOPS_CHANGED -> Type.ALL_CHARGING_STOPS_CHANGED
  Type.NEXT_CHARGING_STOP_REMOVED -> Type.NEXT_CHARGING_STOP_REMOVED
  Type.NEXT_CHARGING_STOP_ADDED -> Type.NEXT_CHARGING_STOP_ADDED
  Type.RETURN_TO_LAST_CHARGING_STOP -> Type.RETURN_TO_LAST_CHARGING_STOP
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun ChargerType.toProto(): ChargerType = when (this) {
  ChargerType.DEFAULT -> ChargerType.DEFAULT
  ChargerType.FAST -> ChargerType.FAST
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun ChargerType.toNative(): ChargerType = when (this) {
  ChargerType.DEFAULT -> ChargerType.DEFAULT
  ChargerType.FAST -> ChargerType.FAST
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun ChargingStopData.toProto(): ChargingStopData = chargingStopData {
  type = this@toProto.type.toProto()
  charger_type = this@toProto.chargerType.toProto()
  charger_operator_name = this@toProto.chargerOperatorName.toProto()
  location_name = this@toProto.locationName.toProto()
}

public fun ChargingStopData.toNative(): ChargingStopData = ChargingStopData {
  type = this@toNative.type.toNative()
  chargerType = this@toNative.charger_type.toNative()
  chargerOperatorName = this@toNative.charger_operator_name.toNative()
  locationName = this@toNative.location_name.toNative()
}

public fun TrafficEventType.toProto(): TrafficEventType = when (this) {
  TrafficEventType.UNKNOWN -> TrafficEventType.UNKNOWN
  TrafficEventType.STATIONARY_TRAFFIC -> TrafficEventType.STATIONARY_TRAFFIC
  TrafficEventType.QUEUING_TRAFFIC -> TrafficEventType.QUEUING_TRAFFIC
  TrafficEventType.SLOW_TRAFFIC -> TrafficEventType.SLOW_TRAFFIC
  TrafficEventType.TRAFFIC_JAM -> TrafficEventType.TRAFFIC_JAM
  TrafficEventType.ACCIDENT -> TrafficEventType.ACCIDENT
  TrafficEventType.ROAD_CLOSED -> TrafficEventType.ROAD_CLOSED
  TrafficEventType.EXIT_RESTRICTIONS -> TrafficEventType.EXIT_RESTRICTIONS
  TrafficEventType.ENTRY_RESTRICTIONS -> TrafficEventType.ENTRY_RESTRICTIONS
  TrafficEventType.ROADWORKS -> TrafficEventType.ROADWORKS
  TrafficEventType.NARROW_LANES -> TrafficEventType.NARROW_LANES
  TrafficEventType.INCIDENTS -> TrafficEventType.INCIDENTS
  TrafficEventType.OBSTRUCTION_HAZARDS -> TrafficEventType.OBSTRUCTION_HAZARDS
  TrafficEventType.DANGEROUS_SITUATION -> TrafficEventType.DANGEROUS_SITUATION
  TrafficEventType.VEHICLES_CARRYING_HAZARDOUS_MATERIALS ->
      TrafficEventType.VEHICLES_CARRYING_HAZARDOUS_MATERIALS
  TrafficEventType.SECURITY_INCIDENT -> TrafficEventType.SECURITY_INCIDENT
  TrafficEventType.EXCEPTIONAL_LOADS -> TrafficEventType.EXCEPTIONAL_LOADS
  TrafficEventType.SLIPPERY_ROAD -> TrafficEventType.SLIPPERY_ROAD
  TrafficEventType.DANGER_OF_FLASH_FLOODS -> TrafficEventType.DANGER_OF_FLASH_FLOODS
  TrafficEventType.HAZARDOUS_DRIVING_CONDITIONS -> TrafficEventType.HAZARDOUS_DRIVING_CONDITIONS
  TrafficEventType.TRAFFIC_RESTRICTIONS -> TrafficEventType.TRAFFIC_RESTRICTIONS
  TrafficEventType.STRONG_WINDS -> TrafficEventType.STRONG_WINDS
  TrafficEventType.SNOWFALL -> TrafficEventType.SNOWFALL
  TrafficEventType.SMOG_ALERT -> TrafficEventType.SMOG_ALERT
  TrafficEventType.HEAVY_RAIN -> TrafficEventType.HEAVY_RAIN
  TrafficEventType.REDUCED_VISIBILITY -> TrafficEventType.REDUCED_VISIBILITY
  TrafficEventType.FOG -> TrafficEventType.FOG
  TrafficEventType.DANGEROUS_WEATHER_CONDITIONS -> TrafficEventType.DANGEROUS_WEATHER_CONDITIONS
  TrafficEventType.DRIVER_ON_WRONG_CARRIAGEWAY -> TrafficEventType.DRIVER_ON_WRONG_CARRIAGEWAY
  TrafficEventType.DELAYS -> TrafficEventType.DELAYS
  TrafficEventType.AIR_RAID_DANGER -> TrafficEventType.AIR_RAID_DANGER
  TrafficEventType.GUNFIRE_ON_THE_ROAD_DANGER -> TrafficEventType.GUNFIRE_ON_THE_ROAD_DANGER
  TrafficEventType.EMERGENCY_VEHICLES -> TrafficEventType.EMERGENCY_VEHICLES
  TrafficEventType.POLICE_INTERVENTION_DANGER -> TrafficEventType.POLICE_INTERVENTION_DANGER
  TrafficEventType.HIGH_SPEED_CHASE -> TrafficEventType.HIGH_SPEED_CHASE
  TrafficEventType.BROKEN_DOWN_VEHICLE -> TrafficEventType.BROKEN_DOWN_VEHICLE
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun TrafficEventType.toNative(): TrafficEventType = when (this) {
  TrafficEventType.UNKNOWN -> TrafficEventType.UNKNOWN
  TrafficEventType.STATIONARY_TRAFFIC -> TrafficEventType.STATIONARY_TRAFFIC
  TrafficEventType.QUEUING_TRAFFIC -> TrafficEventType.QUEUING_TRAFFIC
  TrafficEventType.SLOW_TRAFFIC -> TrafficEventType.SLOW_TRAFFIC
  TrafficEventType.TRAFFIC_JAM -> TrafficEventType.TRAFFIC_JAM
  TrafficEventType.ACCIDENT -> TrafficEventType.ACCIDENT
  TrafficEventType.ROAD_CLOSED -> TrafficEventType.ROAD_CLOSED
  TrafficEventType.EXIT_RESTRICTIONS -> TrafficEventType.EXIT_RESTRICTIONS
  TrafficEventType.ENTRY_RESTRICTIONS -> TrafficEventType.ENTRY_RESTRICTIONS
  TrafficEventType.ROADWORKS -> TrafficEventType.ROADWORKS
  TrafficEventType.NARROW_LANES -> TrafficEventType.NARROW_LANES
  TrafficEventType.INCIDENTS -> TrafficEventType.INCIDENTS
  TrafficEventType.OBSTRUCTION_HAZARDS -> TrafficEventType.OBSTRUCTION_HAZARDS
  TrafficEventType.DANGEROUS_SITUATION -> TrafficEventType.DANGEROUS_SITUATION
  TrafficEventType.VEHICLES_CARRYING_HAZARDOUS_MATERIALS ->
      TrafficEventType.VEHICLES_CARRYING_HAZARDOUS_MATERIALS
  TrafficEventType.SECURITY_INCIDENT -> TrafficEventType.SECURITY_INCIDENT
  TrafficEventType.EXCEPTIONAL_LOADS -> TrafficEventType.EXCEPTIONAL_LOADS
  TrafficEventType.SLIPPERY_ROAD -> TrafficEventType.SLIPPERY_ROAD
  TrafficEventType.DANGER_OF_FLASH_FLOODS -> TrafficEventType.DANGER_OF_FLASH_FLOODS
  TrafficEventType.HAZARDOUS_DRIVING_CONDITIONS -> TrafficEventType.HAZARDOUS_DRIVING_CONDITIONS
  TrafficEventType.TRAFFIC_RESTRICTIONS -> TrafficEventType.TRAFFIC_RESTRICTIONS
  TrafficEventType.STRONG_WINDS -> TrafficEventType.STRONG_WINDS
  TrafficEventType.SNOWFALL -> TrafficEventType.SNOWFALL
  TrafficEventType.SMOG_ALERT -> TrafficEventType.SMOG_ALERT
  TrafficEventType.HEAVY_RAIN -> TrafficEventType.HEAVY_RAIN
  TrafficEventType.REDUCED_VISIBILITY -> TrafficEventType.REDUCED_VISIBILITY
  TrafficEventType.FOG -> TrafficEventType.FOG
  TrafficEventType.DANGEROUS_WEATHER_CONDITIONS -> TrafficEventType.DANGEROUS_WEATHER_CONDITIONS
  TrafficEventType.DRIVER_ON_WRONG_CARRIAGEWAY -> TrafficEventType.DRIVER_ON_WRONG_CARRIAGEWAY
  TrafficEventType.DELAYS -> TrafficEventType.DELAYS
  TrafficEventType.AIR_RAID_DANGER -> TrafficEventType.AIR_RAID_DANGER
  TrafficEventType.GUNFIRE_ON_THE_ROAD_DANGER -> TrafficEventType.GUNFIRE_ON_THE_ROAD_DANGER
  TrafficEventType.EMERGENCY_VEHICLES -> TrafficEventType.EMERGENCY_VEHICLES
  TrafficEventType.POLICE_INTERVENTION_DANGER -> TrafficEventType.POLICE_INTERVENTION_DANGER
  TrafficEventType.HIGH_SPEED_CHASE -> TrafficEventType.HIGH_SPEED_CHASE
  TrafficEventType.BROKEN_DOWN_VEHICLE -> TrafficEventType.BROKEN_DOWN_VEHICLE
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun TrafficEventData.toProto(): TrafficEventData = trafficEventData {
  traffic_event_type = this@toProto.trafficEventType.toProto()
  road_number = this@toProto.roadNumber.toProto()
  road_name = this@toProto.roadName.toProto()
  start_road_name = this@toProto.startRoadName.toProto()
  end_road_name = this@toProto.endRoadName.toProto()
  start_exit_number = this@toProto.startExitNumber.toProto()
  end_exit_number = this@toProto.endExitNumber.toProto()
  this@toProto.travelDelaySeconds?.let { travel_delay_seconds = it }
}

public fun TrafficEventData.toNative(): TrafficEventData = TrafficEventData {
  trafficEventType = this@toNative.traffic_event_type.toNative()
  roadNumber = this@toNative.road_number.toNative()
  roadName = this@toNative.road_name.toNative()
  startRoadName = this@toNative.start_road_name.toNative()
  endRoadName = this@toNative.end_road_name.toNative()
  startExitNumber = this@toNative.start_exit_number.toNative()
  endExitNumber = this@toNative.end_exit_number.toNative()
  travelDelaySeconds = if (this@toNative.hasTravelDelaySeconds()) this@toNative.travel_delay_seconds
      else null
}
