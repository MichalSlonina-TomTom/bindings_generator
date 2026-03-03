/*
 * © 2026 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

// AUTO-GENERATED FILE. DO NOT MODIFY.

#ifndef PROTOBUF_HELPERS_HPP
#define PROTOBUF_HELPERS_HPP

#include <string>
#include <vector>

// Forward declarations
struct RoadIdentifier;
struct RoadInformation;
struct Roundabout;
struct ExitRoundabout;
struct Signpost;
struct TextWithPhonetic;
struct PhoneticString;
struct PhoneticStringWithPreposition;
struct BorderCrossing;
struct CountryInfo;
struct ChargingStop;
struct AudioInstruction;
struct Lane;

namespace protobuf_helpers {

// Conversion functions for ItineraryPointSide
ItineraryPointSide ToNative(const ItineraryPointSide proto);
ItineraryPointSide ToProto(const ItineraryPointSide native);

// Conversion functions for DrivingSide
DrivingSide ToNative(const DrivingSide proto);
DrivingSide ToProto(const DrivingSide native);

// Conversion functions for AudioInstructionType
AudioInstructionType ToNative(const AudioInstructionType proto);
AudioInstructionType ToProto(const AudioInstructionType native);

// Conversion functions for ExitDirection
ExitDirection ToNative(const ExitDirection proto);
ExitDirection ToProto(const ExitDirection native);

// Conversion functions for RoundaboutDirection
RoundaboutDirection ToNative(const RoundaboutDirection proto);
RoundaboutDirection ToProto(const RoundaboutDirection native);

// Conversion functions for QuantizedAngle
QuantizedAngle ToNative(const QuantizedAngle proto);
QuantizedAngle ToProto(const QuantizedAngle native);

// Conversion functions for RoundaboutType
RoundaboutType ToNative(const RoundaboutType proto);
RoundaboutType ToProto(const RoundaboutType native);

// Conversion functions for TurnDirection
TurnDirection ToNative(const TurnDirection proto);
TurnDirection ToProto(const TurnDirection native);

// Conversion functions for ForkDirection
ForkDirection ToNative(const ForkDirection proto);
ForkDirection ToProto(const ForkDirection native);

// Conversion functions for RoadAttribute
RoadAttribute ToNative(const RoadAttribute proto);
RoadAttribute ToProto(const RoadAttribute native);

// Conversion functions for RoadIdentifierSource
RoadIdentifierSource ToNative(const RoadIdentifierSource proto);
RoadIdentifierSource ToProto(const RoadIdentifierSource native);

// Conversion functions for Landmark
Landmark ToNative(const Landmark proto);
Landmark ToProto(const Landmark native);

// Conversion functions for MergeSide
MergeSide ToNative(const MergeSide proto);
MergeSide ToProto(const MergeSide native);

// Conversion functions for SwitchHighwayDirection
SwitchHighwayDirection ToNative(const SwitchHighwayDirection proto);
SwitchHighwayDirection ToProto(const SwitchHighwayDirection native);

// Conversion functions for AutoTransportType
AutoTransportType ToNative(const AutoTransportType proto);
AutoTransportType ToProto(const AutoTransportType native);

// Conversion functions for EnterHovDirection
EnterHovDirection ToNative(const EnterHovDirection proto);
EnterHovDirection ToProto(const EnterHovDirection native);

// Conversion functions for ExitHovDirection
ExitHovDirection ToNative(const ExitHovDirection proto);
ExitHovDirection ToProto(const ExitHovDirection native);

// Conversion functions for RoadIdentifier
RoadIdentifier ToNative(const RoadIdentifier proto);
RoadIdentifier ToProto(const RoadIdentifier& native);

// Conversion functions for RoadInformation
RoadInformation ToNative(const RoadInformation proto);
RoadInformation ToProto(const RoadInformation& native);

// Conversion functions for Roundabout
Roundabout ToNative(const Roundabout proto);
Roundabout ToProto(const Roundabout& native);

// Conversion functions for ExitRoundabout
ExitRoundabout ToNative(const ExitRoundabout proto);
ExitRoundabout ToProto(const ExitRoundabout& native);

// Conversion functions for Signpost
Signpost ToNative(const Signpost proto);
Signpost ToProto(const Signpost& native);

// Conversion functions for TextWithPhonetic
TextWithPhonetic ToNative(const TextWithPhonetic proto);
TextWithPhonetic ToProto(const TextWithPhonetic& native);

// Conversion functions for PhoneticString
PhoneticString ToNative(const PhoneticString proto);
PhoneticString ToProto(const PhoneticString& native);

// Conversion functions for PhoneticStringWithPreposition
PhoneticStringWithPreposition ToNative(const PhoneticStringWithPreposition proto);
PhoneticStringWithPreposition ToProto(const PhoneticStringWithPreposition& native);

// Conversion functions for BorderCrossing
BorderCrossing ToNative(const BorderCrossing proto);
BorderCrossing ToProto(const BorderCrossing& native);

// Conversion functions for CountryInfo
CountryInfo ToNative(const CountryInfo proto);
CountryInfo ToProto(const CountryInfo& native);

// Conversion functions for ChargingStop
ChargingStop ToNative(const ChargingStop proto);
ChargingStop ToProto(const ChargingStop& native);

// Conversion functions for AudioInstruction
AudioInstruction ToNative(const AudioInstruction proto);
AudioInstruction ToProto(const AudioInstruction& native);

// Conversion functions for Lane
Lane ToNative(const Lane proto);
Lane ToProto(const Lane& native);

// Conversion functions for LaneDirection
LaneDirection ToNative(const Lane_LaneDirection proto);
Lane_LaneDirection ToProto(const LaneDirection native);

}  // namespace protobuf_helpers

#endif // PROTOBUF_HELPERS_HPP
