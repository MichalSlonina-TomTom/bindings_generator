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

public fun ItineraryPointSide.toProto(): ItineraryPointSide = when (this) {
  ItineraryPointSide.UNKNOWN -> ItineraryPointSide.kItineraryPointSideUnknown
  ItineraryPointSide.LEFT -> ItineraryPointSide.kItineraryPointSideLeft
  ItineraryPointSide.RIGHT -> ItineraryPointSide.kItineraryPointSideRight
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun ItineraryPointSide.toNative(): ItineraryPointSide = when (this) {
  ItineraryPointSide.kItineraryPointSideUnknown -> ItineraryPointSide.UNKNOWN
  ItineraryPointSide.kItineraryPointSideLeft -> ItineraryPointSide.LEFT
  ItineraryPointSide.kItineraryPointSideRight -> ItineraryPointSide.RIGHT
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun DrivingSide.toProto(): DrivingSide = when (this) {
  DrivingSide.LEFT -> DrivingSide.kDrivingSideLeft
  DrivingSide.RIGHT -> DrivingSide.kDrivingSideRight
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun DrivingSide.toNative(): DrivingSide = when (this) {
  DrivingSide.kDrivingSideLeft -> DrivingSide.LEFT
  DrivingSide.kDrivingSideRight -> DrivingSide.RIGHT
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun AudioInstructionType.toProto(): AudioInstructionType = when (this) {
  AudioInstructionType.KINSTRUCTIONTYPEARRIVAL -> AudioInstructionType.kInstructionTypeArrival
  AudioInstructionType.KINSTRUCTIONTYPEWAYPOINT -> AudioInstructionType.kInstructionTypeWaypoint
  AudioInstructionType.KINSTRUCTIONTYPEDEPARTURE -> AudioInstructionType.kInstructionTypeDeparture
  AudioInstructionType.KINSTRUCTIONTYPEEXITROUNDABOUT ->
      AudioInstructionType.kInstructionTypeExitRoundabout
  AudioInstructionType.KINSTRUCTIONTYPEROUNDABOUT -> AudioInstructionType.kInstructionTypeRoundabout
  AudioInstructionType.KINSTRUCTIONTYPETURN -> AudioInstructionType.kInstructionTypeTurn
  AudioInstructionType.KINSTRUCTIONTYPEOBLIGATORYTURN ->
      AudioInstructionType.kInstructionTypeObligatoryTurn
  AudioInstructionType.KINSTRUCTIONTYPEEXIT -> AudioInstructionType.kInstructionTypeExit
  AudioInstructionType.KINSTRUCTIONTYPEFORK -> AudioInstructionType.kInstructionTypeFork
  AudioInstructionType.KINSTRUCTIONTYPESWITCHHIGHWAY ->
      AudioInstructionType.kInstructionTypeSwitchHighway
  AudioInstructionType.KINSTRUCTIONTYPEMERGE -> AudioInstructionType.kInstructionTypeMerge
  AudioInstructionType.KINSTRUCTIONTYPETURNAROUNDWHENPOSSIBLE ->
      AudioInstructionType.kInstructionTypeTurnAroundWhenPossible
  AudioInstructionType.KINSTRUCTIONTYPEBORDERCROSSING ->
      AudioInstructionType.kInstructionTypeBorderCrossing
  AudioInstructionType.KINSTRUCTIONTYPEENTRYAUTOTRANSPORT ->
      AudioInstructionType.kInstructionTypeEntryAutoTransport
  AudioInstructionType.KINSTRUCTIONTYPEEXITAUTOTRANSPORT ->
      AudioInstructionType.kInstructionTypeExitAutoTransport
  AudioInstructionType.KINSTRUCTIONTYPETOLLGATE -> AudioInstructionType.kInstructionTypeTollgate
  AudioInstructionType.KINSTRUCTIONTYPEENTERHOV -> AudioInstructionType.kInstructionTypeEnterHov
  AudioInstructionType.KINSTRUCTIONTYPEEXITHOV -> AudioInstructionType.kInstructionTypeExitHov
  AudioInstructionType.KINSTRUCTIONTYPECONTINUEINTERIM ->
      AudioInstructionType.kInstructionTypeContinueInterim
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun AudioInstructionType.toNative(): AudioInstructionType = when (this) {
  AudioInstructionType.kInstructionTypeArrival -> AudioInstructionType.KINSTRUCTIONTYPEARRIVAL
  AudioInstructionType.kInstructionTypeWaypoint -> AudioInstructionType.KINSTRUCTIONTYPEWAYPOINT
  AudioInstructionType.kInstructionTypeDeparture -> AudioInstructionType.KINSTRUCTIONTYPEDEPARTURE
  AudioInstructionType.kInstructionTypeExitRoundabout ->
      AudioInstructionType.KINSTRUCTIONTYPEEXITROUNDABOUT
  AudioInstructionType.kInstructionTypeRoundabout -> AudioInstructionType.KINSTRUCTIONTYPEROUNDABOUT
  AudioInstructionType.kInstructionTypeTurn -> AudioInstructionType.KINSTRUCTIONTYPETURN
  AudioInstructionType.kInstructionTypeObligatoryTurn ->
      AudioInstructionType.KINSTRUCTIONTYPEOBLIGATORYTURN
  AudioInstructionType.kInstructionTypeExit -> AudioInstructionType.KINSTRUCTIONTYPEEXIT
  AudioInstructionType.kInstructionTypeFork -> AudioInstructionType.KINSTRUCTIONTYPEFORK
  AudioInstructionType.kInstructionTypeSwitchHighway ->
      AudioInstructionType.KINSTRUCTIONTYPESWITCHHIGHWAY
  AudioInstructionType.kInstructionTypeMerge -> AudioInstructionType.KINSTRUCTIONTYPEMERGE
  AudioInstructionType.kInstructionTypeTurnAroundWhenPossible ->
      AudioInstructionType.KINSTRUCTIONTYPETURNAROUNDWHENPOSSIBLE
  AudioInstructionType.kInstructionTypeBorderCrossing ->
      AudioInstructionType.KINSTRUCTIONTYPEBORDERCROSSING
  AudioInstructionType.kInstructionTypeEntryAutoTransport ->
      AudioInstructionType.KINSTRUCTIONTYPEENTRYAUTOTRANSPORT
  AudioInstructionType.kInstructionTypeExitAutoTransport ->
      AudioInstructionType.KINSTRUCTIONTYPEEXITAUTOTRANSPORT
  AudioInstructionType.kInstructionTypeTollgate -> AudioInstructionType.KINSTRUCTIONTYPETOLLGATE
  AudioInstructionType.kInstructionTypeEnterHov -> AudioInstructionType.KINSTRUCTIONTYPEENTERHOV
  AudioInstructionType.kInstructionTypeExitHov -> AudioInstructionType.KINSTRUCTIONTYPEEXITHOV
  AudioInstructionType.kInstructionTypeContinueInterim ->
      AudioInstructionType.KINSTRUCTIONTYPECONTINUEINTERIM
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun ExitDirection.toProto(): ExitDirection = when (this) {
  ExitDirection.LEFT -> ExitDirection.kExitDirectionLeft
  ExitDirection.RIGHT -> ExitDirection.kExitDirectionRight
  ExitDirection.MIDDLE -> ExitDirection.kExitDirectionMiddle
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun ExitDirection.toNative(): ExitDirection = when (this) {
  ExitDirection.kExitDirectionLeft -> ExitDirection.LEFT
  ExitDirection.kExitDirectionRight -> ExitDirection.RIGHT
  ExitDirection.kExitDirectionMiddle -> ExitDirection.MIDDLE
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun RoundaboutDirection.toProto(): RoundaboutDirection = when (this) {
  RoundaboutDirection.EXITCROSS -> RoundaboutDirection.kRoundaboutDirectionExitCross
  RoundaboutDirection.EXITRIGHT -> RoundaboutDirection.kRoundaboutDirectionExitRight
  RoundaboutDirection.EXITLEFT -> RoundaboutDirection.kRoundaboutDirectionExitLeft
  RoundaboutDirection.EXITBACK -> RoundaboutDirection.kRoundaboutDirectionExitBack
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun RoundaboutDirection.toNative(): RoundaboutDirection = when (this) {
  RoundaboutDirection.kRoundaboutDirectionExitCross -> RoundaboutDirection.EXITCROSS
  RoundaboutDirection.kRoundaboutDirectionExitRight -> RoundaboutDirection.EXITRIGHT
  RoundaboutDirection.kRoundaboutDirectionExitLeft -> RoundaboutDirection.EXITLEFT
  RoundaboutDirection.kRoundaboutDirectionExitBack -> RoundaboutDirection.EXITBACK
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun QuantizedAngle.toProto(): QuantizedAngle = when (this) {
  QuantizedAngle.KSTRAIGHT -> QuantizedAngle.kStraight
  QuantizedAngle.KSLIGHTRIGHT -> QuantizedAngle.kSlightRight
  QuantizedAngle.KRIGHT -> QuantizedAngle.kRight
  QuantizedAngle.KSHARPRIGHT -> QuantizedAngle.kSharpRight
  QuantizedAngle.KSLIGHTLEFT -> QuantizedAngle.kSlightLeft
  QuantizedAngle.KLEFT -> QuantizedAngle.kLeft
  QuantizedAngle.KSHARPLEFT -> QuantizedAngle.kSharpLeft
  QuantizedAngle.KBACK -> QuantizedAngle.kBack
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun QuantizedAngle.toNative(): QuantizedAngle = when (this) {
  QuantizedAngle.kStraight -> QuantizedAngle.KSTRAIGHT
  QuantizedAngle.kSlightRight -> QuantizedAngle.KSLIGHTRIGHT
  QuantizedAngle.kRight -> QuantizedAngle.KRIGHT
  QuantizedAngle.kSharpRight -> QuantizedAngle.KSHARPRIGHT
  QuantizedAngle.kSlightLeft -> QuantizedAngle.KSLIGHTLEFT
  QuantizedAngle.kLeft -> QuantizedAngle.KLEFT
  QuantizedAngle.kSharpLeft -> QuantizedAngle.KSHARPLEFT
  QuantizedAngle.kBack -> QuantizedAngle.KBACK
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun RoundaboutType.toProto(): RoundaboutType = when (this) {
  RoundaboutType.KDEFAULT -> RoundaboutType.kDefault
  RoundaboutType.KSMALL -> RoundaboutType.kSmall
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun RoundaboutType.toNative(): RoundaboutType = when (this) {
  RoundaboutType.kDefault -> RoundaboutType.KDEFAULT
  RoundaboutType.kSmall -> RoundaboutType.KSMALL
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun TurnDirection.toProto(): TurnDirection = when (this) {
  TurnDirection.GOSTRAIGHT -> TurnDirection.kTurnDirectionGoStraight
  TurnDirection.BEARRIGHT -> TurnDirection.kTurnDirectionBearRight
  TurnDirection.TURNRIGHT -> TurnDirection.kTurnDirectionTurnRight
  TurnDirection.SHARPRIGHT -> TurnDirection.kTurnDirectionSharpRight
  TurnDirection.BEARLEFT -> TurnDirection.kTurnDirectionBearLeft
  TurnDirection.TURNLEFT -> TurnDirection.kTurnDirectionTurnLeft
  TurnDirection.SHARPLEFT -> TurnDirection.kTurnDirectionSharpLeft
  TurnDirection.TURNAROUND -> TurnDirection.kTurnDirectionTurnAround
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun TurnDirection.toNative(): TurnDirection = when (this) {
  TurnDirection.kTurnDirectionGoStraight -> TurnDirection.GOSTRAIGHT
  TurnDirection.kTurnDirectionBearRight -> TurnDirection.BEARRIGHT
  TurnDirection.kTurnDirectionTurnRight -> TurnDirection.TURNRIGHT
  TurnDirection.kTurnDirectionSharpRight -> TurnDirection.SHARPRIGHT
  TurnDirection.kTurnDirectionBearLeft -> TurnDirection.BEARLEFT
  TurnDirection.kTurnDirectionTurnLeft -> TurnDirection.TURNLEFT
  TurnDirection.kTurnDirectionSharpLeft -> TurnDirection.SHARPLEFT
  TurnDirection.kTurnDirectionTurnAround -> TurnDirection.TURNAROUND
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun ForkDirection.toProto(): ForkDirection = when (this) {
  ForkDirection.LEFT -> ForkDirection.kForkDirectionLeft
  ForkDirection.RIGHT -> ForkDirection.kForkDirectionRight
  ForkDirection.MIDDLE -> ForkDirection.kForkDirectionMiddle
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun ForkDirection.toNative(): ForkDirection = when (this) {
  ForkDirection.kForkDirectionLeft -> ForkDirection.LEFT
  ForkDirection.kForkDirectionRight -> ForkDirection.RIGHT
  ForkDirection.kForkDirectionMiddle -> ForkDirection.MIDDLE
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun RoadAttribute.toProto(): RoadAttribute = when (this) {
  RoadAttribute.KNONE -> RoadAttribute.kNone
  RoadAttribute.KNATIONALROAD -> RoadAttribute.kNationalRoad
  RoadAttribute.KPREFECTURALROAD -> RoadAttribute.kPrefecturalRoad
  RoadAttribute.KEXPRESSWAY -> RoadAttribute.kExpressway
  RoadAttribute.KCOUNTYROAD -> RoadAttribute.kCountyRoad
  RoadAttribute.KNATIONALHIGHWAY -> RoadAttribute.kNationalHighway
  RoadAttribute.KPROVINCIALHIGHWAY -> RoadAttribute.kProvincialHighway
  RoadAttribute.KPROVINCIALROAD -> RoadAttribute.kProvincialRoad
  RoadAttribute.KTOWNSHIPROAD -> RoadAttribute.kTownshipRoad
  RoadAttribute.KTOKYOPREFECTURALROAD -> RoadAttribute.kTokyoPrefecturalRoad
  RoadAttribute.KHOKKAIDOPREFECTURALROAD -> RoadAttribute.kHokkaidoPrefecturalRoad
  RoadAttribute.KOSAKAANDKYOTOPREFECTURALROAD -> RoadAttribute.kOsakaAndKyotoPrefecturalRoad
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun RoadAttribute.toNative(): RoadAttribute = when (this) {
  RoadAttribute.kNone -> RoadAttribute.KNONE
  RoadAttribute.kNationalRoad -> RoadAttribute.KNATIONALROAD
  RoadAttribute.kPrefecturalRoad -> RoadAttribute.KPREFECTURALROAD
  RoadAttribute.kExpressway -> RoadAttribute.KEXPRESSWAY
  RoadAttribute.kCountyRoad -> RoadAttribute.KCOUNTYROAD
  RoadAttribute.kNationalHighway -> RoadAttribute.KNATIONALHIGHWAY
  RoadAttribute.kProvincialHighway -> RoadAttribute.KPROVINCIALHIGHWAY
  RoadAttribute.kProvincialRoad -> RoadAttribute.KPROVINCIALROAD
  RoadAttribute.kTownshipRoad -> RoadAttribute.KTOWNSHIPROAD
  RoadAttribute.kTokyoPrefecturalRoad -> RoadAttribute.KTOKYOPREFECTURALROAD
  RoadAttribute.kHokkaidoPrefecturalRoad -> RoadAttribute.KHOKKAIDOPREFECTURALROAD
  RoadAttribute.kOsakaAndKyotoPrefecturalRoad -> RoadAttribute.KOSAKAANDKYOTOPREFECTURALROAD
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun RoadIdentifierSource.toProto(): RoadIdentifierSource = when (this) {
  RoadIdentifierSource.KSIGNPOST -> RoadIdentifierSource.kSignpost
  RoadIdentifierSource.KROAD -> RoadIdentifierSource.kRoad
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun RoadIdentifierSource.toNative(): RoadIdentifierSource = when (this) {
  RoadIdentifierSource.kSignpost -> RoadIdentifierSource.KSIGNPOST
  RoadIdentifierSource.kRoad -> RoadIdentifierSource.KROAD
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun Landmark.toProto(): Landmark = when (this) {
  Landmark.KENDOFROAD -> Landmark.kEndOfRoad
  Landmark.KATTRAFFICLIGHT -> Landmark.kAtTrafficLight
  Landmark.KONTOBRIDGE -> Landmark.kOnToBridge
  Landmark.KONBRIDGE -> Landmark.kOnBridge
  Landmark.KAFTERBRIDGE -> Landmark.kAfterBridge
  Landmark.KINTOTUNNEL -> Landmark.kIntoTunnel
  Landmark.KINSIDETUNNEL -> Landmark.kInsideTunnel
  Landmark.KAFTERTUNNEL -> Landmark.kAfterTunnel
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun Landmark.toNative(): Landmark = when (this) {
  Landmark.kEndOfRoad -> Landmark.KENDOFROAD
  Landmark.kAtTrafficLight -> Landmark.KATTRAFFICLIGHT
  Landmark.kOnToBridge -> Landmark.KONTOBRIDGE
  Landmark.kOnBridge -> Landmark.KONBRIDGE
  Landmark.kAfterBridge -> Landmark.KAFTERBRIDGE
  Landmark.kIntoTunnel -> Landmark.KINTOTUNNEL
  Landmark.kInsideTunnel -> Landmark.KINSIDETUNNEL
  Landmark.kAfterTunnel -> Landmark.KAFTERTUNNEL
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun MergeSide.toProto(): MergeSide = when (this) {
  MergeSide.KMERGETOLEFTLANE -> MergeSide.kMergeToLeftLane
  MergeSide.KMERGETORIGHTLANE -> MergeSide.kMergeToRightLane
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun MergeSide.toNative(): MergeSide = when (this) {
  MergeSide.kMergeToLeftLane -> MergeSide.KMERGETOLEFTLANE
  MergeSide.kMergeToRightLane -> MergeSide.KMERGETORIGHTLANE
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun SwitchHighwayDirection.toProto(): SwitchHighwayDirection = when (this) {
  SwitchHighwayDirection.KSWITCHHIGHWAYLEFT -> SwitchHighwayDirection.kSwitchHighwayLeft
  SwitchHighwayDirection.KSWITCHHIGHWAYRIGHT -> SwitchHighwayDirection.kSwitchHighwayRight
  SwitchHighwayDirection.KSWITCHHIGHWAYMIDDLE -> SwitchHighwayDirection.kSwitchHighwayMiddle
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun SwitchHighwayDirection.toNative(): SwitchHighwayDirection = when (this) {
  SwitchHighwayDirection.kSwitchHighwayLeft -> SwitchHighwayDirection.KSWITCHHIGHWAYLEFT
  SwitchHighwayDirection.kSwitchHighwayRight -> SwitchHighwayDirection.KSWITCHHIGHWAYRIGHT
  SwitchHighwayDirection.kSwitchHighwayMiddle -> SwitchHighwayDirection.KSWITCHHIGHWAYMIDDLE
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun AutoTransportType.toProto(): AutoTransportType = when (this) {
  AutoTransportType.KFERRY -> AutoTransportType.kFerry
  AutoTransportType.KCARTRAIN -> AutoTransportType.kCartrain
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun AutoTransportType.toNative(): AutoTransportType = when (this) {
  AutoTransportType.kFerry -> AutoTransportType.KFERRY
  AutoTransportType.kCartrain -> AutoTransportType.KCARTRAIN
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun EnterHovDirection.toProto(): EnterHovDirection = when (this) {
  EnterHovDirection.KENTERHOVLEFT -> EnterHovDirection.kEnterHovLeft
  EnterHovDirection.KENTERHOVRIGHT -> EnterHovDirection.kEnterHovRight
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun EnterHovDirection.toNative(): EnterHovDirection = when (this) {
  EnterHovDirection.kEnterHovLeft -> EnterHovDirection.KENTERHOVLEFT
  EnterHovDirection.kEnterHovRight -> EnterHovDirection.KENTERHOVRIGHT
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun ExitHovDirection.toProto(): ExitHovDirection = when (this) {
  ExitHovDirection.KEXITHOVLEFT -> ExitHovDirection.kExitHovLeft
  ExitHovDirection.KEXITHOVRIGHT -> ExitHovDirection.kExitHovRight
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun ExitHovDirection.toNative(): ExitHovDirection = when (this) {
  ExitHovDirection.kExitHovLeft -> ExitHovDirection.KEXITHOVLEFT
  ExitHovDirection.kExitHovRight -> ExitHovDirection.KEXITHOVRIGHT
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun RoadIdentifier.toProto(): RoadIdentifier = roadIdentifier {
  identifier = this@toProto.identifier.toProto()
  source = this@toProto.source.toProto()
  road_attribute = this@toProto.roadAttribute.toProto()
}

public fun RoadIdentifier.toNative(): RoadIdentifier = RoadIdentifier {
  identifier = this@toNative.identifier.toNative()
  source = this@toNative.source.toNative()
  roadAttribute = this@toNative.road_attribute.toNative()
}

public fun RoadInformation.toProto(): RoadInformation = roadInformation {
  addAllRoadNames(this@toProto.roadNames.map { it })
  addAllRoadNumbers(this@toProto.roadNumbers.map { it })
  is_urban_area = this@toProto.isUrbanArea
  is_controlled_access = this@toProto.isControlledAccess
  is_motorway = this@toProto.isMotorway
  country_code = this@toProto.countryCode
}

public fun RoadInformation.toNative(): RoadInformation = RoadInformation {
  roadNames = this@toNative.road_namesList
  roadNumbers = this@toNative.road_numbersList
  isUrbanArea = this@toNative.is_urban_area
  isControlledAccess = this@toNative.is_controlled_access
  isMotorway = this@toNative.is_motorway
  countryCode = this@toNative.country_code
}

public fun Roundabout.toProto(): Roundabout = roundabout {
  direction = this@toProto.direction.toProto()
  angle_in_degrees = this@toProto.angleInDegrees
  turn_angle = this@toProto.turnAngle.toProto()
  exit_number = this@toProto.exitNumber
  roundabout_type = this@toProto.roundaboutType.toProto()
}

public fun Roundabout.toNative(): Roundabout = Roundabout {
  direction = this@toNative.direction.toNative()
  angleInDegrees = this@toNative.angle_in_degrees
  turnAngle = this@toNative.turn_angle.toNative()
  exitNumber = this@toNative.exit_number
  roundaboutType = this@toNative.roundabout_type.toNative()
}

public fun ExitRoundabout.toProto(): ExitRoundabout = exitRoundabout {
  roundabout = this@toProto.roundabout.toProto()
}

public fun ExitRoundabout.toNative(): ExitRoundabout = ExitRoundabout {
  roundabout = this@toNative.roundabout.toNative()
}

public fun Signpost.toProto(): Signpost = signpost {
  exit_number = this@toProto.exitNumber.toProto()
  exit_name = this@toProto.exitName.toProto()
  toward_name = this@toProto.towardName.toProto()
}

public fun Signpost.toNative(): Signpost = Signpost {
  exitNumber = this@toNative.exit_number.toNative()
  exitName = this@toNative.exit_name.toNative()
  towardName = this@toNative.toward_name.toNative()
}

public fun TextWithPhonetic.toProto(): TextWithPhonetic = textWithPhonetic {
  text = this@toProto.text
  text_language = this@toProto.textLanguage.toProto()
  generic_use_case_phonetic_string = this@toProto.genericUseCasePhoneticString.toProto()
  into_use_case_phonetic_string = this@toProto.intoUseCasePhoneticString.toProto()
  follow_use_case_phonetic_string = this@toProto.followUseCasePhoneticString.toProto()
}

public fun TextWithPhonetic.toNative(): TextWithPhonetic = TextWithPhonetic {
  text = this@toNative.text
  textLanguage = this@toNative.text_language.toNative()
  genericUseCasePhoneticString = this@toNative.generic_use_case_phonetic_string.toNative()
  intoUseCasePhoneticString = this@toNative.into_use_case_phonetic_string.toNative()
  followUseCasePhoneticString = this@toNative.follow_use_case_phonetic_string.toNative()
}

public fun PhoneticString.toProto(): PhoneticString = phoneticString {
  value = this@toProto.value
  alphabet = this@toProto.alphabet
  language = this@toProto.language.toProto()
}

public fun PhoneticString.toNative(): PhoneticString = PhoneticString {
  value = this@toNative.value
  alphabet = this@toNative.alphabet
  language = this@toNative.language.toNative()
}

public fun PhoneticStringWithPreposition.toProto(): PhoneticStringWithPreposition =
    phoneticStringWithPreposition {
  phonetic_string = this@toProto.phoneticString.toProto()
  prefix = this@toProto.prefix
}

public fun PhoneticStringWithPreposition.toNative(): PhoneticStringWithPreposition =
    PhoneticStringWithPreposition {
  phoneticString = this@toNative.phonetic_string.toNative()
  prefix = this@toNative.prefix
}

public fun BorderCrossing.toProto(): BorderCrossing = borderCrossing {
  from_country = this@toProto.fromCountry.toProto()
  to_country = this@toProto.toCountry.toProto()
}

public fun BorderCrossing.toNative(): BorderCrossing = BorderCrossing {
  fromCountry = this@toNative.from_country.toNative()
  toCountry = this@toNative.to_country.toNative()
}

public fun CountryInfo.toProto(): CountryInfo = countryInfo {
  country_name = this@toProto.countryName.toProto()
  iso_country_code = this@toProto.isoCountryCode
}

public fun CountryInfo.toNative(): CountryInfo = CountryInfo {
  countryName = this@toNative.country_name.toNative()
  isoCountryCode = this@toNative.iso_country_code
}

public fun ChargingStop.toProto(): ChargingStop = chargingStop {
  operator_name = this@toProto.operatorName.toProto()
}

public fun ChargingStop.toNative(): ChargingStop = ChargingStop {
  operatorName = this@toNative.operator_name.toNative()
}

public fun AudioInstruction.toProto(): AudioInstruction = audioInstruction {
  type = this@toProto.type.toProto()
  driving_side = this@toProto.drivingSide.toProto()
  itinerary_point_side = this@toProto.itineraryPointSide.toProto()
  incoming_road_information = this@toProto.incomingRoadInformation.toProto()
  outgoing_road_information = this@toProto.outgoingRoadInformation.toProto()
  landmark = this@toProto.landmark.toProto()
  signpost = this@toProto.signpost.toProto()
  this@toProto.trafficLightOffsetInCentimeters?.let { traffic_light_offset_in_centimeters = it }
  exit_direction = this@toProto.exitDirection.toProto()
  this@toProto.sideStreetOffsetInCentimeters?.let { side_street_offset_in_centimeters = it }
  fork_direction = this@toProto.forkDirection.toProto()
  intersection_name_with_phonetic = this@toProto.intersectionNameWithPhonetic.toProto()
  turn_direction = this@toProto.turnDirection.toProto()
  switch_highway_direction = this@toProto.switchHighwayDirection.toProto()
  enter_hov_direction = this@toProto.enterHovDirection.toProto()
  exit_hov_direction = this@toProto.exitHovDirection.toProto()
  merge_side = this@toProto.mergeSide.toProto()
  roundabout = this@toProto.roundabout.toProto()
  exit_roundabout = this@toProto.exitRoundabout.toProto()
  border_crossing = this@toProto.borderCrossing.toProto()
  auto_transport_type = this@toProto.autoTransportType.toProto()
  addAllLaneGuidance(this@toProto.laneGuidance.map { it })
  charging_stop = this@toProto.chargingStop.toProto()
}

public fun AudioInstruction.toNative(): AudioInstruction = AudioInstruction {
  type = this@toNative.type.toNative()
  drivingSide = this@toNative.driving_side.toNative()
  itineraryPointSide = this@toNative.itinerary_point_side.toNative()
  incomingRoadInformation = this@toNative.incoming_road_information.toNative()
  outgoingRoadInformation = this@toNative.outgoing_road_information.toNative()
  landmark = this@toNative.landmark.toNative()
  signpost = this@toNative.signpost.toNative()
  trafficLightOffsetInCentimeters = if (this@toNative.hasTrafficLightOffsetInCentimeters())
      this@toNative.traffic_light_offset_in_centimeters else null
  exitDirection = this@toNative.exit_direction.toNative()
  sideStreetOffsetInCentimeters = if (this@toNative.hasSideStreetOffsetInCentimeters())
      this@toNative.side_street_offset_in_centimeters else null
  forkDirection = this@toNative.fork_direction.toNative()
  intersectionNameWithPhonetic = this@toNative.intersection_name_with_phonetic.toNative()
  turnDirection = this@toNative.turn_direction.toNative()
  switchHighwayDirection = this@toNative.switch_highway_direction.toNative()
  enterHovDirection = this@toNative.enter_hov_direction.toNative()
  exitHovDirection = this@toNative.exit_hov_direction.toNative()
  mergeSide = this@toNative.merge_side.toNative()
  roundabout = this@toNative.roundabout.toNative()
  exitRoundabout = this@toNative.exit_roundabout.toNative()
  borderCrossing = this@toNative.border_crossing.toNative()
  autoTransportType = this@toNative.auto_transport_type.toNative()
  laneGuidance = this@toNative.lane_guidanceList
  chargingStop = this@toNative.charging_stop.toNative()
}

public fun LaneDirection.toProto(): LaneDirection = when (this) {
  LaneDirection.KSTRAIGHT -> LaneDirection.kStraight
  LaneDirection.KSLIGHTRIGHT -> LaneDirection.kSlightRight
  LaneDirection.KRIGHT -> LaneDirection.kRight
  LaneDirection.KSHARPRIGHT -> LaneDirection.kSharpRight
  LaneDirection.KRIGHTUTURN -> LaneDirection.kRightUTurn
  LaneDirection.KSLIGHTLEFT -> LaneDirection.kSlightLeft
  LaneDirection.KLEFT -> LaneDirection.kLeft
  LaneDirection.KSHARPLEFT -> LaneDirection.kSharpLeft
  LaneDirection.KLEFTUTURN -> LaneDirection.kLeftUTurn
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun LaneDirection.toNative(): LaneDirection = when (this) {
  LaneDirection.kStraight -> LaneDirection.KSTRAIGHT
  LaneDirection.kSlightRight -> LaneDirection.KSLIGHTRIGHT
  LaneDirection.kRight -> LaneDirection.KRIGHT
  LaneDirection.kSharpRight -> LaneDirection.KSHARPRIGHT
  LaneDirection.kRightUTurn -> LaneDirection.KRIGHTUTURN
  LaneDirection.kSlightLeft -> LaneDirection.KSLIGHTLEFT
  LaneDirection.kLeft -> LaneDirection.KLEFT
  LaneDirection.kSharpLeft -> LaneDirection.KSHARPLEFT
  LaneDirection.kLeftUTurn -> LaneDirection.KLEFTUTURN
  else -> throw IllegalArgumentException("Unexpected value " + this)
}

public fun Lane.toProto(): Lane = lane {
  addAllDirections(this@toProto.directions.map { it })
  follow_direction = this@toProto.followDirection.toProto()
}

public fun Lane.toNative(): Lane = Lane {
  directions = this@toNative.directionsList
  followDirection = this@toNative.follow_direction.toNative()
}
