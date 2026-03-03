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

#include "protobuf_helpers.hpp"

namespace protobuf_helpers {

ItineraryPointSide ToNative(const ItineraryPointSide proto) {
    switch (proto) {
        case ItineraryPointSide::kItineraryPointSideUnknown: return ItineraryPointSide::UNKNOWN;
        case ItineraryPointSide::kItineraryPointSideLeft: return ItineraryPointSide::LEFT;
        case ItineraryPointSide::kItineraryPointSideRight: return ItineraryPointSide::RIGHT;
        default: return ItineraryPointSide::UNKNOWN;
    }
}

ItineraryPointSide ToProto(const ItineraryPointSide native) {
    switch (native) {
        case ItineraryPointSide::UNKNOWN: return ItineraryPointSide::kItineraryPointSideUnknown;
        case ItineraryPointSide::LEFT: return ItineraryPointSide::kItineraryPointSideLeft;
        case ItineraryPointSide::RIGHT: return ItineraryPointSide::kItineraryPointSideRight;
        default: return ItineraryPointSide::kItineraryPointSideUnknown;
    }
}

DrivingSide ToNative(const DrivingSide proto) {
    switch (proto) {
        case DrivingSide::kDrivingSideLeft: return DrivingSide::LEFT;
        case DrivingSide::kDrivingSideRight: return DrivingSide::RIGHT;
        default: return DrivingSide::LEFT;
    }
}

DrivingSide ToProto(const DrivingSide native) {
    switch (native) {
        case DrivingSide::LEFT: return DrivingSide::kDrivingSideLeft;
        case DrivingSide::RIGHT: return DrivingSide::kDrivingSideRight;
        default: return DrivingSide::kDrivingSideLeft;
    }
}

AudioInstructionType ToNative(const AudioInstructionType proto) {
    switch (proto) {
        case AudioInstructionType::kInstructionTypeArrival: return AudioInstructionType::KINSTRUCTIONTYPEARRIVAL;
        case AudioInstructionType::kInstructionTypeWaypoint: return AudioInstructionType::KINSTRUCTIONTYPEWAYPOINT;
        case AudioInstructionType::kInstructionTypeDeparture: return AudioInstructionType::KINSTRUCTIONTYPEDEPARTURE;
        case AudioInstructionType::kInstructionTypeExitRoundabout: return AudioInstructionType::KINSTRUCTIONTYPEEXITROUNDABOUT;
        case AudioInstructionType::kInstructionTypeRoundabout: return AudioInstructionType::KINSTRUCTIONTYPEROUNDABOUT;
        case AudioInstructionType::kInstructionTypeTurn: return AudioInstructionType::KINSTRUCTIONTYPETURN;
        case AudioInstructionType::kInstructionTypeObligatoryTurn: return AudioInstructionType::KINSTRUCTIONTYPEOBLIGATORYTURN;
        case AudioInstructionType::kInstructionTypeExit: return AudioInstructionType::KINSTRUCTIONTYPEEXIT;
        case AudioInstructionType::kInstructionTypeFork: return AudioInstructionType::KINSTRUCTIONTYPEFORK;
        case AudioInstructionType::kInstructionTypeSwitchHighway: return AudioInstructionType::KINSTRUCTIONTYPESWITCHHIGHWAY;
        case AudioInstructionType::kInstructionTypeMerge: return AudioInstructionType::KINSTRUCTIONTYPEMERGE;
        case AudioInstructionType::kInstructionTypeTurnAroundWhenPossible: return AudioInstructionType::KINSTRUCTIONTYPETURNAROUNDWHENPOSSIBLE;
        case AudioInstructionType::kInstructionTypeBorderCrossing: return AudioInstructionType::KINSTRUCTIONTYPEBORDERCROSSING;
        case AudioInstructionType::kInstructionTypeEntryAutoTransport: return AudioInstructionType::KINSTRUCTIONTYPEENTRYAUTOTRANSPORT;
        case AudioInstructionType::kInstructionTypeExitAutoTransport: return AudioInstructionType::KINSTRUCTIONTYPEEXITAUTOTRANSPORT;
        case AudioInstructionType::kInstructionTypeTollgate: return AudioInstructionType::KINSTRUCTIONTYPETOLLGATE;
        case AudioInstructionType::kInstructionTypeEnterHov: return AudioInstructionType::KINSTRUCTIONTYPEENTERHOV;
        case AudioInstructionType::kInstructionTypeExitHov: return AudioInstructionType::KINSTRUCTIONTYPEEXITHOV;
        case AudioInstructionType::kInstructionTypeContinueInterim: return AudioInstructionType::KINSTRUCTIONTYPECONTINUEINTERIM;
        default: return AudioInstructionType::KINSTRUCTIONTYPEARRIVAL;
    }
}

AudioInstructionType ToProto(const AudioInstructionType native) {
    switch (native) {
        case AudioInstructionType::KINSTRUCTIONTYPEARRIVAL: return AudioInstructionType::kInstructionTypeArrival;
        case AudioInstructionType::KINSTRUCTIONTYPEWAYPOINT: return AudioInstructionType::kInstructionTypeWaypoint;
        case AudioInstructionType::KINSTRUCTIONTYPEDEPARTURE: return AudioInstructionType::kInstructionTypeDeparture;
        case AudioInstructionType::KINSTRUCTIONTYPEEXITROUNDABOUT: return AudioInstructionType::kInstructionTypeExitRoundabout;
        case AudioInstructionType::KINSTRUCTIONTYPEROUNDABOUT: return AudioInstructionType::kInstructionTypeRoundabout;
        case AudioInstructionType::KINSTRUCTIONTYPETURN: return AudioInstructionType::kInstructionTypeTurn;
        case AudioInstructionType::KINSTRUCTIONTYPEOBLIGATORYTURN: return AudioInstructionType::kInstructionTypeObligatoryTurn;
        case AudioInstructionType::KINSTRUCTIONTYPEEXIT: return AudioInstructionType::kInstructionTypeExit;
        case AudioInstructionType::KINSTRUCTIONTYPEFORK: return AudioInstructionType::kInstructionTypeFork;
        case AudioInstructionType::KINSTRUCTIONTYPESWITCHHIGHWAY: return AudioInstructionType::kInstructionTypeSwitchHighway;
        case AudioInstructionType::KINSTRUCTIONTYPEMERGE: return AudioInstructionType::kInstructionTypeMerge;
        case AudioInstructionType::KINSTRUCTIONTYPETURNAROUNDWHENPOSSIBLE: return AudioInstructionType::kInstructionTypeTurnAroundWhenPossible;
        case AudioInstructionType::KINSTRUCTIONTYPEBORDERCROSSING: return AudioInstructionType::kInstructionTypeBorderCrossing;
        case AudioInstructionType::KINSTRUCTIONTYPEENTRYAUTOTRANSPORT: return AudioInstructionType::kInstructionTypeEntryAutoTransport;
        case AudioInstructionType::KINSTRUCTIONTYPEEXITAUTOTRANSPORT: return AudioInstructionType::kInstructionTypeExitAutoTransport;
        case AudioInstructionType::KINSTRUCTIONTYPETOLLGATE: return AudioInstructionType::kInstructionTypeTollgate;
        case AudioInstructionType::KINSTRUCTIONTYPEENTERHOV: return AudioInstructionType::kInstructionTypeEnterHov;
        case AudioInstructionType::KINSTRUCTIONTYPEEXITHOV: return AudioInstructionType::kInstructionTypeExitHov;
        case AudioInstructionType::KINSTRUCTIONTYPECONTINUEINTERIM: return AudioInstructionType::kInstructionTypeContinueInterim;
        default: return AudioInstructionType::kInstructionTypeArrival;
    }
}

ExitDirection ToNative(const ExitDirection proto) {
    switch (proto) {
        case ExitDirection::kExitDirectionLeft: return ExitDirection::LEFT;
        case ExitDirection::kExitDirectionRight: return ExitDirection::RIGHT;
        case ExitDirection::kExitDirectionMiddle: return ExitDirection::MIDDLE;
        default: return ExitDirection::LEFT;
    }
}

ExitDirection ToProto(const ExitDirection native) {
    switch (native) {
        case ExitDirection::LEFT: return ExitDirection::kExitDirectionLeft;
        case ExitDirection::RIGHT: return ExitDirection::kExitDirectionRight;
        case ExitDirection::MIDDLE: return ExitDirection::kExitDirectionMiddle;
        default: return ExitDirection::kExitDirectionLeft;
    }
}

RoundaboutDirection ToNative(const RoundaboutDirection proto) {
    switch (proto) {
        case RoundaboutDirection::kRoundaboutDirectionExitCross: return RoundaboutDirection::EXITCROSS;
        case RoundaboutDirection::kRoundaboutDirectionExitRight: return RoundaboutDirection::EXITRIGHT;
        case RoundaboutDirection::kRoundaboutDirectionExitLeft: return RoundaboutDirection::EXITLEFT;
        case RoundaboutDirection::kRoundaboutDirectionExitBack: return RoundaboutDirection::EXITBACK;
        default: return RoundaboutDirection::EXITCROSS;
    }
}

RoundaboutDirection ToProto(const RoundaboutDirection native) {
    switch (native) {
        case RoundaboutDirection::EXITCROSS: return RoundaboutDirection::kRoundaboutDirectionExitCross;
        case RoundaboutDirection::EXITRIGHT: return RoundaboutDirection::kRoundaboutDirectionExitRight;
        case RoundaboutDirection::EXITLEFT: return RoundaboutDirection::kRoundaboutDirectionExitLeft;
        case RoundaboutDirection::EXITBACK: return RoundaboutDirection::kRoundaboutDirectionExitBack;
        default: return RoundaboutDirection::kRoundaboutDirectionExitCross;
    }
}

QuantizedAngle ToNative(const QuantizedAngle proto) {
    switch (proto) {
        case QuantizedAngle::kStraight: return QuantizedAngle::KSTRAIGHT;
        case QuantizedAngle::kSlightRight: return QuantizedAngle::KSLIGHTRIGHT;
        case QuantizedAngle::kRight: return QuantizedAngle::KRIGHT;
        case QuantizedAngle::kSharpRight: return QuantizedAngle::KSHARPRIGHT;
        case QuantizedAngle::kSlightLeft: return QuantizedAngle::KSLIGHTLEFT;
        case QuantizedAngle::kLeft: return QuantizedAngle::KLEFT;
        case QuantizedAngle::kSharpLeft: return QuantizedAngle::KSHARPLEFT;
        case QuantizedAngle::kBack: return QuantizedAngle::KBACK;
        default: return QuantizedAngle::KSTRAIGHT;
    }
}

QuantizedAngle ToProto(const QuantizedAngle native) {
    switch (native) {
        case QuantizedAngle::KSTRAIGHT: return QuantizedAngle::kStraight;
        case QuantizedAngle::KSLIGHTRIGHT: return QuantizedAngle::kSlightRight;
        case QuantizedAngle::KRIGHT: return QuantizedAngle::kRight;
        case QuantizedAngle::KSHARPRIGHT: return QuantizedAngle::kSharpRight;
        case QuantizedAngle::KSLIGHTLEFT: return QuantizedAngle::kSlightLeft;
        case QuantizedAngle::KLEFT: return QuantizedAngle::kLeft;
        case QuantizedAngle::KSHARPLEFT: return QuantizedAngle::kSharpLeft;
        case QuantizedAngle::KBACK: return QuantizedAngle::kBack;
        default: return QuantizedAngle::kStraight;
    }
}

RoundaboutType ToNative(const RoundaboutType proto) {
    switch (proto) {
        case RoundaboutType::kDefault: return RoundaboutType::KDEFAULT;
        case RoundaboutType::kSmall: return RoundaboutType::KSMALL;
        default: return RoundaboutType::KDEFAULT;
    }
}

RoundaboutType ToProto(const RoundaboutType native) {
    switch (native) {
        case RoundaboutType::KDEFAULT: return RoundaboutType::kDefault;
        case RoundaboutType::KSMALL: return RoundaboutType::kSmall;
        default: return RoundaboutType::kDefault;
    }
}

TurnDirection ToNative(const TurnDirection proto) {
    switch (proto) {
        case TurnDirection::kTurnDirectionGoStraight: return TurnDirection::GOSTRAIGHT;
        case TurnDirection::kTurnDirectionBearRight: return TurnDirection::BEARRIGHT;
        case TurnDirection::kTurnDirectionTurnRight: return TurnDirection::TURNRIGHT;
        case TurnDirection::kTurnDirectionSharpRight: return TurnDirection::SHARPRIGHT;
        case TurnDirection::kTurnDirectionBearLeft: return TurnDirection::BEARLEFT;
        case TurnDirection::kTurnDirectionTurnLeft: return TurnDirection::TURNLEFT;
        case TurnDirection::kTurnDirectionSharpLeft: return TurnDirection::SHARPLEFT;
        case TurnDirection::kTurnDirectionTurnAround: return TurnDirection::TURNAROUND;
        default: return TurnDirection::GOSTRAIGHT;
    }
}

TurnDirection ToProto(const TurnDirection native) {
    switch (native) {
        case TurnDirection::GOSTRAIGHT: return TurnDirection::kTurnDirectionGoStraight;
        case TurnDirection::BEARRIGHT: return TurnDirection::kTurnDirectionBearRight;
        case TurnDirection::TURNRIGHT: return TurnDirection::kTurnDirectionTurnRight;
        case TurnDirection::SHARPRIGHT: return TurnDirection::kTurnDirectionSharpRight;
        case TurnDirection::BEARLEFT: return TurnDirection::kTurnDirectionBearLeft;
        case TurnDirection::TURNLEFT: return TurnDirection::kTurnDirectionTurnLeft;
        case TurnDirection::SHARPLEFT: return TurnDirection::kTurnDirectionSharpLeft;
        case TurnDirection::TURNAROUND: return TurnDirection::kTurnDirectionTurnAround;
        default: return TurnDirection::kTurnDirectionGoStraight;
    }
}

ForkDirection ToNative(const ForkDirection proto) {
    switch (proto) {
        case ForkDirection::kForkDirectionLeft: return ForkDirection::LEFT;
        case ForkDirection::kForkDirectionRight: return ForkDirection::RIGHT;
        case ForkDirection::kForkDirectionMiddle: return ForkDirection::MIDDLE;
        default: return ForkDirection::LEFT;
    }
}

ForkDirection ToProto(const ForkDirection native) {
    switch (native) {
        case ForkDirection::LEFT: return ForkDirection::kForkDirectionLeft;
        case ForkDirection::RIGHT: return ForkDirection::kForkDirectionRight;
        case ForkDirection::MIDDLE: return ForkDirection::kForkDirectionMiddle;
        default: return ForkDirection::kForkDirectionLeft;
    }
}

RoadAttribute ToNative(const RoadAttribute proto) {
    switch (proto) {
        case RoadAttribute::kNone: return RoadAttribute::KNONE;
        case RoadAttribute::kNationalRoad: return RoadAttribute::KNATIONALROAD;
        case RoadAttribute::kPrefecturalRoad: return RoadAttribute::KPREFECTURALROAD;
        case RoadAttribute::kExpressway: return RoadAttribute::KEXPRESSWAY;
        case RoadAttribute::kCountyRoad: return RoadAttribute::KCOUNTYROAD;
        case RoadAttribute::kNationalHighway: return RoadAttribute::KNATIONALHIGHWAY;
        case RoadAttribute::kProvincialHighway: return RoadAttribute::KPROVINCIALHIGHWAY;
        case RoadAttribute::kProvincialRoad: return RoadAttribute::KPROVINCIALROAD;
        case RoadAttribute::kTownshipRoad: return RoadAttribute::KTOWNSHIPROAD;
        case RoadAttribute::kTokyoPrefecturalRoad: return RoadAttribute::KTOKYOPREFECTURALROAD;
        case RoadAttribute::kHokkaidoPrefecturalRoad: return RoadAttribute::KHOKKAIDOPREFECTURALROAD;
        case RoadAttribute::kOsakaAndKyotoPrefecturalRoad: return RoadAttribute::KOSAKAANDKYOTOPREFECTURALROAD;
        default: return RoadAttribute::KNONE;
    }
}

RoadAttribute ToProto(const RoadAttribute native) {
    switch (native) {
        case RoadAttribute::KNONE: return RoadAttribute::kNone;
        case RoadAttribute::KNATIONALROAD: return RoadAttribute::kNationalRoad;
        case RoadAttribute::KPREFECTURALROAD: return RoadAttribute::kPrefecturalRoad;
        case RoadAttribute::KEXPRESSWAY: return RoadAttribute::kExpressway;
        case RoadAttribute::KCOUNTYROAD: return RoadAttribute::kCountyRoad;
        case RoadAttribute::KNATIONALHIGHWAY: return RoadAttribute::kNationalHighway;
        case RoadAttribute::KPROVINCIALHIGHWAY: return RoadAttribute::kProvincialHighway;
        case RoadAttribute::KPROVINCIALROAD: return RoadAttribute::kProvincialRoad;
        case RoadAttribute::KTOWNSHIPROAD: return RoadAttribute::kTownshipRoad;
        case RoadAttribute::KTOKYOPREFECTURALROAD: return RoadAttribute::kTokyoPrefecturalRoad;
        case RoadAttribute::KHOKKAIDOPREFECTURALROAD: return RoadAttribute::kHokkaidoPrefecturalRoad;
        case RoadAttribute::KOSAKAANDKYOTOPREFECTURALROAD: return RoadAttribute::kOsakaAndKyotoPrefecturalRoad;
        default: return RoadAttribute::kNone;
    }
}

RoadIdentifierSource ToNative(const RoadIdentifierSource proto) {
    switch (proto) {
        case RoadIdentifierSource::kSignpost: return RoadIdentifierSource::KSIGNPOST;
        case RoadIdentifierSource::kRoad: return RoadIdentifierSource::KROAD;
        default: return RoadIdentifierSource::KSIGNPOST;
    }
}

RoadIdentifierSource ToProto(const RoadIdentifierSource native) {
    switch (native) {
        case RoadIdentifierSource::KSIGNPOST: return RoadIdentifierSource::kSignpost;
        case RoadIdentifierSource::KROAD: return RoadIdentifierSource::kRoad;
        default: return RoadIdentifierSource::kSignpost;
    }
}

Landmark ToNative(const Landmark proto) {
    switch (proto) {
        case Landmark::kEndOfRoad: return Landmark::KENDOFROAD;
        case Landmark::kAtTrafficLight: return Landmark::KATTRAFFICLIGHT;
        case Landmark::kOnToBridge: return Landmark::KONTOBRIDGE;
        case Landmark::kOnBridge: return Landmark::KONBRIDGE;
        case Landmark::kAfterBridge: return Landmark::KAFTERBRIDGE;
        case Landmark::kIntoTunnel: return Landmark::KINTOTUNNEL;
        case Landmark::kInsideTunnel: return Landmark::KINSIDETUNNEL;
        case Landmark::kAfterTunnel: return Landmark::KAFTERTUNNEL;
        default: return Landmark::KENDOFROAD;
    }
}

Landmark ToProto(const Landmark native) {
    switch (native) {
        case Landmark::KENDOFROAD: return Landmark::kEndOfRoad;
        case Landmark::KATTRAFFICLIGHT: return Landmark::kAtTrafficLight;
        case Landmark::KONTOBRIDGE: return Landmark::kOnToBridge;
        case Landmark::KONBRIDGE: return Landmark::kOnBridge;
        case Landmark::KAFTERBRIDGE: return Landmark::kAfterBridge;
        case Landmark::KINTOTUNNEL: return Landmark::kIntoTunnel;
        case Landmark::KINSIDETUNNEL: return Landmark::kInsideTunnel;
        case Landmark::KAFTERTUNNEL: return Landmark::kAfterTunnel;
        default: return Landmark::kEndOfRoad;
    }
}

MergeSide ToNative(const MergeSide proto) {
    switch (proto) {
        case MergeSide::kMergeToLeftLane: return MergeSide::KMERGETOLEFTLANE;
        case MergeSide::kMergeToRightLane: return MergeSide::KMERGETORIGHTLANE;
        default: return MergeSide::KMERGETOLEFTLANE;
    }
}

MergeSide ToProto(const MergeSide native) {
    switch (native) {
        case MergeSide::KMERGETOLEFTLANE: return MergeSide::kMergeToLeftLane;
        case MergeSide::KMERGETORIGHTLANE: return MergeSide::kMergeToRightLane;
        default: return MergeSide::kMergeToLeftLane;
    }
}

SwitchHighwayDirection ToNative(const SwitchHighwayDirection proto) {
    switch (proto) {
        case SwitchHighwayDirection::kSwitchHighwayLeft: return SwitchHighwayDirection::KSWITCHHIGHWAYLEFT;
        case SwitchHighwayDirection::kSwitchHighwayRight: return SwitchHighwayDirection::KSWITCHHIGHWAYRIGHT;
        case SwitchHighwayDirection::kSwitchHighwayMiddle: return SwitchHighwayDirection::KSWITCHHIGHWAYMIDDLE;
        default: return SwitchHighwayDirection::KSWITCHHIGHWAYLEFT;
    }
}

SwitchHighwayDirection ToProto(const SwitchHighwayDirection native) {
    switch (native) {
        case SwitchHighwayDirection::KSWITCHHIGHWAYLEFT: return SwitchHighwayDirection::kSwitchHighwayLeft;
        case SwitchHighwayDirection::KSWITCHHIGHWAYRIGHT: return SwitchHighwayDirection::kSwitchHighwayRight;
        case SwitchHighwayDirection::KSWITCHHIGHWAYMIDDLE: return SwitchHighwayDirection::kSwitchHighwayMiddle;
        default: return SwitchHighwayDirection::kSwitchHighwayLeft;
    }
}

AutoTransportType ToNative(const AutoTransportType proto) {
    switch (proto) {
        case AutoTransportType::kFerry: return AutoTransportType::KFERRY;
        case AutoTransportType::kCartrain: return AutoTransportType::KCARTRAIN;
        default: return AutoTransportType::KFERRY;
    }
}

AutoTransportType ToProto(const AutoTransportType native) {
    switch (native) {
        case AutoTransportType::KFERRY: return AutoTransportType::kFerry;
        case AutoTransportType::KCARTRAIN: return AutoTransportType::kCartrain;
        default: return AutoTransportType::kFerry;
    }
}

EnterHovDirection ToNative(const EnterHovDirection proto) {
    switch (proto) {
        case EnterHovDirection::kEnterHovLeft: return EnterHovDirection::KENTERHOVLEFT;
        case EnterHovDirection::kEnterHovRight: return EnterHovDirection::KENTERHOVRIGHT;
        default: return EnterHovDirection::KENTERHOVLEFT;
    }
}

EnterHovDirection ToProto(const EnterHovDirection native) {
    switch (native) {
        case EnterHovDirection::KENTERHOVLEFT: return EnterHovDirection::kEnterHovLeft;
        case EnterHovDirection::KENTERHOVRIGHT: return EnterHovDirection::kEnterHovRight;
        default: return EnterHovDirection::kEnterHovLeft;
    }
}

ExitHovDirection ToNative(const ExitHovDirection proto) {
    switch (proto) {
        case ExitHovDirection::kExitHovLeft: return ExitHovDirection::KEXITHOVLEFT;
        case ExitHovDirection::kExitHovRight: return ExitHovDirection::KEXITHOVRIGHT;
        default: return ExitHovDirection::KEXITHOVLEFT;
    }
}

ExitHovDirection ToProto(const ExitHovDirection native) {
    switch (native) {
        case ExitHovDirection::KEXITHOVLEFT: return ExitHovDirection::kExitHovLeft;
        case ExitHovDirection::KEXITHOVRIGHT: return ExitHovDirection::kExitHovRight;
        default: return ExitHovDirection::kExitHovLeft;
    }
}

RoadIdentifier ToNative(const RoadIdentifier proto) {
    RoadIdentifier result;
    result.identifier = ToNative(proto.identifier());
    result.source = ToNative(proto.source());
    result.roadAttribute = ToNative(proto.road_attribute());
    return result;
}

RoadIdentifier ToProto(const RoadIdentifier& native) {
    RoadIdentifier result;
    result.mutable_identifier()->CopyFrom(ToProto(native.identifier));
    result.set_source(ToProto(native.source));
    result.set_road_attribute(ToProto(native.roadAttribute));
    return result;
}

RoadInformation ToNative(const RoadInformation proto) {
    RoadInformation result;
    for (const auto& item : proto.road_names()) { result.roadNames.push_back(ToNative(item)); }
    for (const auto& item : proto.road_numbers()) { result.roadNumbers.push_back(ToNative(item)); }
    result.isUrbanArea = proto.is_urban_area();
    result.isControlledAccess = proto.is_controlled_access();
    result.isMotorway = proto.is_motorway();
    result.countryCode = proto.country_code();
    return result;
}

RoadInformation ToProto(const RoadInformation& native) {
    RoadInformation result;
    for (const auto& item : native.roadNames) {
        result.add_road_names(item);
    }
    for (const auto& item : native.roadNumbers) {
        result.add_road_numbers(item);
    }
    result.set_is_urban_area(native.isUrbanArea);
    result.set_is_controlled_access(native.isControlledAccess);
    result.set_is_motorway(native.isMotorway);
    result.set_country_code(native.countryCode);
    return result;
}

Roundabout ToNative(const Roundabout proto) {
    Roundabout result;
    result.direction = ToNative(proto.direction());
    result.angleInDegrees = proto.angle_in_degrees();
    result.turnAngle = ToNative(proto.turn_angle());
    result.exitNumber = proto.exit_number();
    result.roundaboutType = ToNative(proto.roundabout_type());
    return result;
}

Roundabout ToProto(const Roundabout& native) {
    Roundabout result;
    result.set_direction(ToProto(native.direction));
    result.set_angle_in_degrees(native.angleInDegrees);
    result.set_turn_angle(ToProto(native.turnAngle));
    result.set_exit_number(native.exitNumber);
    result.set_roundabout_type(ToProto(native.roundaboutType));
    return result;
}

ExitRoundabout ToNative(const ExitRoundabout proto) {
    ExitRoundabout result;
    result.roundabout = ToNative(proto.roundabout());
    return result;
}

ExitRoundabout ToProto(const ExitRoundabout& native) {
    ExitRoundabout result;
    result.mutable_roundabout()->CopyFrom(ToProto(native.roundabout));
    return result;
}

Signpost ToNative(const Signpost proto) {
    Signpost result;
    result.exitNumber = ToNative(proto.exit_number());
    result.exitName = ToNative(proto.exit_name());
    result.towardName = ToNative(proto.toward_name());
    return result;
}

Signpost ToProto(const Signpost& native) {
    Signpost result;
    result.mutable_exit_number()->CopyFrom(ToProto(native.exitNumber));
    result.mutable_exit_name()->CopyFrom(ToProto(native.exitName));
    result.mutable_toward_name()->CopyFrom(ToProto(native.towardName));
    return result;
}

TextWithPhonetic ToNative(const TextWithPhonetic proto) {
    TextWithPhonetic result;
    result.text = proto.text();
    result.textLanguage = ToNative(proto.text_language());
    result.genericUseCasePhoneticString = ToNative(proto.generic_use_case_phonetic_string());
    result.intoUseCasePhoneticString = ToNative(proto.into_use_case_phonetic_string());
    result.followUseCasePhoneticString = ToNative(proto.follow_use_case_phonetic_string());
    return result;
}

TextWithPhonetic ToProto(const TextWithPhonetic& native) {
    TextWithPhonetic result;
    result.set_text(native.text);
    result.mutable_text_language()->CopyFrom(ToProto(native.textLanguage));
    result.mutable_generic_use_case_phonetic_string()->CopyFrom(ToProto(native.genericUseCasePhoneticString));
    result.mutable_into_use_case_phonetic_string()->CopyFrom(ToProto(native.intoUseCasePhoneticString));
    result.mutable_follow_use_case_phonetic_string()->CopyFrom(ToProto(native.followUseCasePhoneticString));
    return result;
}

PhoneticString ToNative(const PhoneticString proto) {
    PhoneticString result;
    result.value = proto.value();
    result.alphabet = proto.alphabet();
    result.language = ToNative(proto.language());
    return result;
}

PhoneticString ToProto(const PhoneticString& native) {
    PhoneticString result;
    result.set_value(native.value);
    result.set_alphabet(native.alphabet);
    result.mutable_language()->CopyFrom(ToProto(native.language));
    return result;
}

PhoneticStringWithPreposition ToNative(const PhoneticStringWithPreposition proto) {
    PhoneticStringWithPreposition result;
    result.phoneticString = ToNative(proto.phonetic_string());
    result.prefix = proto.prefix();
    return result;
}

PhoneticStringWithPreposition ToProto(const PhoneticStringWithPreposition& native) {
    PhoneticStringWithPreposition result;
    result.mutable_phonetic_string()->CopyFrom(ToProto(native.phoneticString));
    result.set_prefix(native.prefix);
    return result;
}

BorderCrossing ToNative(const BorderCrossing proto) {
    BorderCrossing result;
    result.fromCountry = ToNative(proto.from_country());
    result.toCountry = ToNative(proto.to_country());
    return result;
}

BorderCrossing ToProto(const BorderCrossing& native) {
    BorderCrossing result;
    result.mutable_from_country()->CopyFrom(ToProto(native.fromCountry));
    result.mutable_to_country()->CopyFrom(ToProto(native.toCountry));
    return result;
}

CountryInfo ToNative(const CountryInfo proto) {
    CountryInfo result;
    result.countryName = ToNative(proto.country_name());
    result.isoCountryCode = proto.iso_country_code();
    return result;
}

CountryInfo ToProto(const CountryInfo& native) {
    CountryInfo result;
    result.mutable_country_name()->CopyFrom(ToProto(native.countryName));
    result.set_iso_country_code(native.isoCountryCode);
    return result;
}

ChargingStop ToNative(const ChargingStop proto) {
    ChargingStop result;
    result.operatorName = ToNative(proto.operator_name());
    return result;
}

ChargingStop ToProto(const ChargingStop& native) {
    ChargingStop result;
    result.mutable_operator_name()->CopyFrom(ToProto(native.operatorName));
    return result;
}

AudioInstruction ToNative(const AudioInstruction proto) {
    AudioInstruction result;
    result.type = ToNative(proto.type());
    result.drivingSide = ToNative(proto.driving_side());
    result.itineraryPointSide = ToNative(proto.itinerary_point_side());
    result.incomingRoadInformation = ToNative(proto.incoming_road_information());
    result.outgoingRoadInformation = ToNative(proto.outgoing_road_information());
    result.landmark = ToNative(proto.landmark());
    result.signpost = ToNative(proto.signpost());
    if (proto.has_traffic_light_offset_in_centimeters()) { result.trafficLightOffsetInCentimeters = proto.traffic_light_offset_in_centimeters(); }
    result.exitDirection = ToNative(proto.exit_direction());
    if (proto.has_side_street_offset_in_centimeters()) { result.sideStreetOffsetInCentimeters = proto.side_street_offset_in_centimeters(); }
    result.forkDirection = ToNative(proto.fork_direction());
    result.intersectionNameWithPhonetic = ToNative(proto.intersection_name_with_phonetic());
    result.turnDirection = ToNative(proto.turn_direction());
    result.switchHighwayDirection = ToNative(proto.switch_highway_direction());
    result.enterHovDirection = ToNative(proto.enter_hov_direction());
    result.exitHovDirection = ToNative(proto.exit_hov_direction());
    result.mergeSide = ToNative(proto.merge_side());
    result.roundabout = ToNative(proto.roundabout());
    result.exitRoundabout = ToNative(proto.exit_roundabout());
    result.borderCrossing = ToNative(proto.border_crossing());
    result.autoTransportType = ToNative(proto.auto_transport_type());
    for (const auto& item : proto.lane_guidance()) { result.laneGuidance.push_back(ToNative(item)); }
    result.chargingStop = ToNative(proto.charging_stop());
    return result;
}

AudioInstruction ToProto(const AudioInstruction& native) {
    AudioInstruction result;
    result.set_type(ToProto(native.type));
    result.set_driving_side(ToProto(native.drivingSide));
    result.set_itinerary_point_side(ToProto(native.itineraryPointSide));
    result.mutable_incoming_road_information()->CopyFrom(ToProto(native.incomingRoadInformation));
    result.mutable_outgoing_road_information()->CopyFrom(ToProto(native.outgoingRoadInformation));
    result.set_landmark(ToProto(native.landmark));
    result.mutable_signpost()->CopyFrom(ToProto(native.signpost));
    if (native.trafficLightOffsetInCentimeters.has_value()) { result.set_traffic_light_offset_in_centimeters(native.trafficLightOffsetInCentimeters.value()); }
    result.set_exit_direction(ToProto(native.exitDirection));
    if (native.sideStreetOffsetInCentimeters.has_value()) { result.set_side_street_offset_in_centimeters(native.sideStreetOffsetInCentimeters.value()); }
    result.set_fork_direction(ToProto(native.forkDirection));
    result.mutable_intersection_name_with_phonetic()->CopyFrom(ToProto(native.intersectionNameWithPhonetic));
    result.set_turn_direction(ToProto(native.turnDirection));
    result.set_switch_highway_direction(ToProto(native.switchHighwayDirection));
    result.set_enter_hov_direction(ToProto(native.enterHovDirection));
    result.set_exit_hov_direction(ToProto(native.exitHovDirection));
    result.set_merge_side(ToProto(native.mergeSide));
    result.mutable_roundabout()->CopyFrom(ToProto(native.roundabout));
    result.mutable_exit_roundabout()->CopyFrom(ToProto(native.exitRoundabout));
    result.mutable_border_crossing()->CopyFrom(ToProto(native.borderCrossing));
    result.set_auto_transport_type(ToProto(native.autoTransportType));
    for (const auto& item : native.laneGuidance) {
        result.add_lane_guidance(item);
    }
    result.mutable_charging_stop()->CopyFrom(ToProto(native.chargingStop));
    return result;
}

Lane ToNative(const Lane proto) {
    Lane result;
    for (const auto& item : proto.directions()) { result.directions.push_back(ToNative(item)); }
    result.followDirection = ToNative(proto.follow_direction());
    return result;
}

Lane ToProto(const Lane& native) {
    Lane result;
    for (const auto& item : native.directions) {
        result.add_directions(item);
    }
    result.set_follow_direction(ToProto(native.followDirection));
    return result;
}

LaneDirection ToNative(const Lane_LaneDirection proto) {
    switch (proto) {
        case Lane_LaneDirection::kStraight: return LaneDirection::KSTRAIGHT;
        case Lane_LaneDirection::kSlightRight: return LaneDirection::KSLIGHTRIGHT;
        case Lane_LaneDirection::kRight: return LaneDirection::KRIGHT;
        case Lane_LaneDirection::kSharpRight: return LaneDirection::KSHARPRIGHT;
        case Lane_LaneDirection::kRightUTurn: return LaneDirection::KRIGHTUTURN;
        case Lane_LaneDirection::kSlightLeft: return LaneDirection::KSLIGHTLEFT;
        case Lane_LaneDirection::kLeft: return LaneDirection::KLEFT;
        case Lane_LaneDirection::kSharpLeft: return LaneDirection::KSHARPLEFT;
        case Lane_LaneDirection::kLeftUTurn: return LaneDirection::KLEFTUTURN;
        default: return LaneDirection::KSTRAIGHT;
    }
}

Lane_LaneDirection ToProto(const LaneDirection native) {
    switch (native) {
        case LaneDirection::KSTRAIGHT: return Lane_LaneDirection::kStraight;
        case LaneDirection::KSLIGHTRIGHT: return Lane_LaneDirection::kSlightRight;
        case LaneDirection::KRIGHT: return Lane_LaneDirection::kRight;
        case LaneDirection::KSHARPRIGHT: return Lane_LaneDirection::kSharpRight;
        case LaneDirection::KRIGHTUTURN: return Lane_LaneDirection::kRightUTurn;
        case LaneDirection::KSLIGHTLEFT: return Lane_LaneDirection::kSlightLeft;
        case LaneDirection::KLEFT: return Lane_LaneDirection::kLeft;
        case LaneDirection::KSHARPLEFT: return Lane_LaneDirection::kSharpLeft;
        case LaneDirection::KLEFTUTURN: return Lane_LaneDirection::kLeftUTurn;
        default: return Lane_LaneDirection::kStraight;
    }
}

}  // namespace protobuf_helpers
