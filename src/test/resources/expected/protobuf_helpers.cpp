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

#include "protobuf_helpers.hpp"

#include <iterator>
#include <orodoro/quantities/length.hpp>

#include <boost/optional.hpp>
#include <tomtom/navkit2/text_generation/audio_instruction.hpp>
#include <tomtom/navkit2/text_generation/audio_instruction_builder.hpp>
#include "audio_instruction.pb.h"
#include "tomtom/navkit2/text_generation/road_information_builder.hpp"

namespace {

using namespace com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure;
using namespace tomtom::navkit2;
using namespace orodoro::i18n;

text_generation::MessageType ConvertMessageType(
    protos::AnnouncementData::MessageType proto_message_type) {
  switch (proto_message_type) {
    case (protos::AnnouncementData::MessageType::AnnouncementData_MessageType_kFollow):
      return text_generation::MessageType::kFollow;
    case (protos::AnnouncementData::MessageType::AnnouncementData_MessageType_kFarAway):
      return text_generation::MessageType::kFarAway;
    case (protos::AnnouncementData::MessageType::AnnouncementData_MessageType_kWarning):
      return text_generation::MessageType::kWarning;
    case (protos::AnnouncementData::MessageType::AnnouncementData_MessageType_kMain):
      return text_generation::MessageType::kMain;
    case (protos::AnnouncementData::MessageType::AnnouncementData_MessageType_kConfirmation):
      return text_generation::MessageType::kConfirmation;
    case (
        protos::AnnouncementData::MessageType::AnnouncementData_MessageType_kExtendedConfirmation):
      return text_generation::MessageType::kExtendedConfirmation;
    default:
      break;
  }
  return text_generation::MessageType::kConfirmation;
}

text_generation::AudioInstructionType ConvertAudioInstructionType(
    protos::AudioInstructionType type) {
  switch (type) {
    case protos::kInstructionTypeArrival:
      return text_generation::AudioInstructionType::kArrival;
    case protos::kInstructionTypeWaypoint:
      return text_generation::AudioInstructionType::kWaypoint;
    case protos::kInstructionTypeDeparture:
      return text_generation::AudioInstructionType::kDeparture;
    case protos::kInstructionTypeExitRoundabout:
      return text_generation::AudioInstructionType::kExitRoundabout;
    case protos::kInstructionTypeRoundabout:
      return text_generation::AudioInstructionType::kRoundabout;
    case protos::kInstructionTypeTurn:
      return text_generation::AudioInstructionType::kTurn;
    case protos::kInstructionTypeObligatoryTurn:
      return text_generation::AudioInstructionType::kObligatoryTurn;
    case protos::kInstructionTypeExit:
      return text_generation::AudioInstructionType::kExit;
    case protos::kInstructionTypeFork:
      return text_generation::AudioInstructionType::kFork;
    case protos::kInstructionTypeSwitchHighway:
      return text_generation::AudioInstructionType::kSwitchHighway;
    case protos::kInstructionTypeMerge:
      return text_generation::AudioInstructionType::kMerge;
    case protos::kInstructionTypeTurnAroundWhenPossible:
      return text_generation::AudioInstructionType::kTurnAroundWhenPossible;
    case protos::kInstructionTypeBorderCrossing:
      return text_generation::AudioInstructionType::kBorderCrossing;
    case protos::kInstructionTypeEntryAutoTransport:
      return text_generation::AudioInstructionType::kEnterAutoTransport;
    case protos::kInstructionTypeExitAutoTransport:
      return text_generation::AudioInstructionType::kExitAutoTransport;
    case protos::kInstructionTypeTollgate:
      return text_generation::AudioInstructionType::kTollgate;
    case protos::kInstructionTypeEnterHov:
      return text_generation::AudioInstructionType::kEnterHov;
    case protos::kInstructionTypeExitHov:
      return text_generation::AudioInstructionType::kExitHov;
    case protos::kInstructionTypeContinueInterim:
      return text_generation::AudioInstructionType::kPreventive;
    default:
      return text_generation::AudioInstructionType::kTurn;
  }
  return text_generation::AudioInstructionType::kTurn;
}

text_generation::DrivingSide ConvertDrivingSide(protos::DrivingSide side) {
  switch (side) {
    case protos::DrivingSide::kDrivingSideLeft:
      return text_generation::DrivingSide::kLeft;
    case protos::DrivingSide::kDrivingSideRight:
      return text_generation::DrivingSide::kRight;
    default:
      return text_generation::DrivingSide::kRight;
  }
  return text_generation::DrivingSide::kRight;
}

text_generation::ItineraryPointSide ConvertItineraryPointSide(protos::ItineraryPointSide side) {
  switch (side) {
    case protos::ItineraryPointSide::kItineraryPointSideLeft:
      return text_generation::ItineraryPointSide::kLeft;
    case protos::ItineraryPointSide::kItineraryPointSideRight:
      return text_generation::ItineraryPointSide::kRight;
    case protos::ItineraryPointSide::kItineraryPointSideUnknown:
      return text_generation::ItineraryPointSide::kUnknown;
    default:
      return text_generation::ItineraryPointSide::kUnknown;
  }
  return text_generation::ItineraryPointSide::kUnknown;
}

CLanguage ConvertLanguage(const protos::Language& proto_language) {
  return {CISOLanguageCode(proto_language.iso_language_code()),
          CISOCountryCode(proto_language.iso_country_code()),
          CISOScriptCode(proto_language.iso_script_code())};
}

text_generation::PhoneticString ConvertPhoneticString(
    const protos::PhoneticString& proto_phonetic_string) {
  return {proto_phonetic_string.value(), proto_phonetic_string.alphabet(),
          ConvertLanguage(proto_phonetic_string.language())};
}

text_generation::PhoneticStringWithPreposition ConvertPhoneticStringWithPreposition(
    const protos::PhoneticStringWithPreposition& proto_phonetic_string_with_preposition) {
  return {ConvertPhoneticString(proto_phonetic_string_with_preposition.phonetic_string()),
          proto_phonetic_string_with_preposition.prefix()};
}

text_generation::TextWithPhonetic ConvertTextWithPhonetic(
    const protos::TextWithPhonetic& proto_phonetic) {
  text_generation::TextWithPhonetic text_with_phonetic;
  text_with_phonetic.text = proto_phonetic.text();
  text_with_phonetic.text_language = ConvertLanguage(proto_phonetic.text_language());
  text_with_phonetic.generic_use_case_phonetic_string =
      ConvertPhoneticString(proto_phonetic.generic_use_case_phonetic_string());
  if (proto_phonetic.has_into_use_case_phonetic_string()) {
    text_with_phonetic.into_use_case_phonetic_string =
        ConvertPhoneticStringWithPreposition(proto_phonetic.into_use_case_phonetic_string());
  }
  if (proto_phonetic.has_follow_use_case_phonetic_string()) {
    text_with_phonetic.follow_use_case_phonetic_string =
        ConvertPhoneticStringWithPreposition(proto_phonetic.follow_use_case_phonetic_string());
  }
  return text_with_phonetic;
}

text_generation::RoadIdentifierSource ConvertRoadIdentifierSource(
    protos::RoadIdentifierSource proto_source) {
  switch (proto_source) {
    case protos::RoadIdentifierSource::kSignpost:
      return text_generation::RoadIdentifierSource::kSignpost;
    case protos::RoadIdentifierSource::kRoad:
      return text_generation::RoadIdentifierSource::kRoad;
    default:
      return text_generation::RoadIdentifierSource::kRoad;
  }
  return text_generation::RoadIdentifierSource::kRoad;
}

text_generation::RoadAttribute ConvertRoadAttribute(protos::RoadAttribute proto_attribute) {
  switch (proto_attribute) {
    case protos::RoadAttribute::kNone:
      return text_generation::RoadAttribute::kNone;
    case protos::RoadAttribute::kNationalRoad:
      return text_generation::RoadAttribute::kNationalRoad;
    case protos::RoadAttribute::kPrefecturalRoad:
      return text_generation::RoadAttribute::kPrefecturalRoad;
    case protos::RoadAttribute::kExpressway:
      return text_generation::RoadAttribute::kExpressway;
    case protos::RoadAttribute::kCountyRoad:
      return text_generation::RoadAttribute::kCountyRoad;
    case protos::RoadAttribute::kNationalHighway:
      return text_generation::RoadAttribute::kNationalHighway;
    case protos::RoadAttribute::kProvincialHighway:
      return text_generation::RoadAttribute::kProvincialHighway;
    case protos::RoadAttribute::kProvincialRoad:
      return text_generation::RoadAttribute::kProvincialRoad;
    case protos::RoadAttribute::kTownshipRoad:
      return text_generation::RoadAttribute::kTownshipRoad;
    case protos::RoadAttribute::kTokyoPrefecturalRoad:
      return text_generation::RoadAttribute::kTokyoPrefecturalRoad;
    case protos::RoadAttribute::kHokkaidoPrefecturalRoad:
      return text_generation::RoadAttribute::kHokkaidoPrefecturalRoad;
    case protos::RoadAttribute::kOsakaAndKyotoPrefecturalRoad:
      return text_generation::RoadAttribute::kOsakaAndKyotoPrefecturalRoad;
    default:
      return text_generation::RoadAttribute::kNone;
  }
}

text_generation::RoadIdentifier ConvertRoadIdentifier(const protos::RoadIdentifier& proto_road_identifier) {
  return text_generation::RoadIdentifier{
    ConvertTextWithPhonetic(proto_road_identifier.identifier()),
    ConvertRoadIdentifierSource(proto_road_identifier.source()),
    ConvertRoadAttribute(proto_road_identifier.road_attribute())
  };
}



text_generation::RoadInformation ConvertRoadInformation(
    const protos::RoadInformation& proto_information) {

  auto convert_road_identifiers = [](const auto& proto_identifier_list) {
    std::vector<text_generation::RoadIdentifier> identifiers;
    std::transform(proto_identifier_list.begin(), proto_identifier_list.end(),
                   std::back_inserter(identifiers),
                   [](auto& proto_identifier) { return ConvertRoadIdentifier(proto_identifier); });
    return std::move(identifiers);
  };



  text_generation::RoadInformationBuilder builder;

  builder.SetControlledAccess(proto_information.is_controlled_access());
  builder.SetMotorway(proto_information.is_motorway());
  builder.SetUrbanArea(proto_information.is_urban_area());
  builder.SetRoadNumbers(std::move(convert_road_identifiers(proto_information.road_numbers())));
  builder.SetRoadNames(std::move(convert_road_identifiers(proto_information.road_names())));
  builder.SetCountryCode(proto_information.country_code());

  return builder.Build();
}

text_generation::Landmark ConvertLandmark(protos::Landmark proto_landmark) {
  switch (proto_landmark) {
    case protos::Landmark::kEndOfRoad:
      return text_generation::Landmark::kEndOfRoad;
    case protos::Landmark::kAtTrafficLight:
      return text_generation::Landmark::kAtTrafficLight;
    case protos::Landmark::kOnToBridge:
      return text_generation::Landmark::kOnToBridge;
    case protos::Landmark::kOnBridge:
      return text_generation::Landmark::kOnBridge;
    case protos::Landmark::kAfterBridge:
      return text_generation::Landmark::kAfterBridge;
    case protos::Landmark::kIntoTunnel:
      return text_generation::Landmark::kIntoTunnel;
    case protos::Landmark::kInsideTunnel:
      return text_generation::Landmark::kInsideTunnel;
    case protos::Landmark::kAfterTunnel:
      return text_generation::Landmark::kAfterTunnel;
    default:
      return text_generation::Landmark::kEndOfRoad;
  }
  return text_generation::Landmark::kEndOfRoad;
}

text_generation::Signpost ConvertSignpost(const protos::Signpost& proto_signpost) {
  return {ConvertTextWithPhonetic(proto_signpost.exit_number()),
          ConvertTextWithPhonetic(proto_signpost.exit_name()),
          ConvertTextWithPhonetic(proto_signpost.toward_name())};
}

text_generation::ExitDirection ConvertExitDirection(protos::ExitDirection proto_direction) {
  switch (proto_direction) {
    case protos::ExitDirection::kExitDirectionLeft:
      return text_generation::ExitDirection::kLeft;
    case protos::ExitDirection::kExitDirectionRight:
      return text_generation::ExitDirection::kRight;
    case protos::ExitDirection::kExitDirectionMiddle:
      return text_generation::ExitDirection::kMiddle;
    default:
      return text_generation::ExitDirection::kRight;
  }
  return text_generation::ExitDirection::kRight;
}

text_generation::ForkDirection ConvertForkDirection(protos::ForkDirection proto_direction) {
  switch (proto_direction) {
    case protos::ForkDirection::kForkDirectionLeft:
      return text_generation::ForkDirection::kLeft;
    case protos::ForkDirection::kForkDirectionRight:
      return text_generation::ForkDirection::kRight;
    case protos::ForkDirection::kForkDirectionMiddle:
      return text_generation::ForkDirection::kMiddle;
    default:
      return text_generation::ForkDirection::kRight;
  }
  return text_generation::ForkDirection::kRight;
}

text_generation::TurnDirection ConvertTurnDirection(protos::TurnDirection proto_direction) {
  switch (proto_direction) {
    case protos::TurnDirection::kTurnDirectionBearRight:
      return text_generation::TurnDirection::kSlightRight;
    case protos::TurnDirection::kTurnDirectionTurnRight:
      return text_generation::TurnDirection::kRight;
    case protos::TurnDirection::kTurnDirectionSharpRight:
      return text_generation::TurnDirection::kSharpRight;
    case protos::TurnDirection::kTurnDirectionBearLeft:
      return text_generation::TurnDirection::kSlightLeft;
    case protos::TurnDirection::kTurnDirectionTurnLeft:
      return text_generation::TurnDirection::kLeft;
    case protos::TurnDirection::kTurnDirectionSharpLeft:
      return text_generation::TurnDirection::kSharpLeft;
    case protos::TurnDirection::kTurnDirectionGoStraight:
      return text_generation::TurnDirection::kStraight;
    case protos::TurnDirection::kTurnDirectionTurnAround:
      return text_generation::TurnDirection::kBack;
    default:
      return text_generation::TurnDirection::kRight;
  }
  return text_generation::TurnDirection::kRight;
}

text_generation::SwitchHighwayDirection ConvertSwitchHighwayDirection(
    protos::SwitchHighwayDirection proto_direction) {
  switch (proto_direction) {
    case protos::SwitchHighwayDirection::kSwitchHighwayLeft:
      return text_generation::SwitchHighwayDirection::kLeft;
    case protos::SwitchHighwayDirection::kSwitchHighwayRight:
      return text_generation::SwitchHighwayDirection::kRight;
    case protos::SwitchHighwayDirection::kSwitchHighwayMiddle:
        return text_generation::SwitchHighwayDirection::kMiddle;
    default:
      return text_generation::SwitchHighwayDirection::kRight;
  }
  return text_generation::SwitchHighwayDirection::kRight;
}

text_generation::EnterHovDirection ConvertEnterHovDirection(
    protos::EnterHovDirection proto_direction) {
  switch (proto_direction) {
    case protos::EnterHovDirection::kEnterHovLeft:
      return text_generation::EnterHovDirection::kLeft;
    case protos::EnterHovDirection::kEnterHovRight:
      return text_generation::EnterHovDirection::kRight;
    default:
      return text_generation::EnterHovDirection::kRight;
  }
  return text_generation::EnterHovDirection::kRight;
}

text_generation::ExitHovDirection ConvertExitHovDirection(
    protos::ExitHovDirection proto_direction) {
  switch (proto_direction) {
    case protos::ExitHovDirection::kExitHovLeft:
      return text_generation::ExitHovDirection::kLeft;
    case protos::ExitHovDirection::kExitHovRight:
      return text_generation::ExitHovDirection::kRight;
    default:
      return text_generation::ExitHovDirection::kRight;
  }
  return text_generation::ExitHovDirection::kRight;
}

text_generation::MergeSide ConvertMergeSide(protos::MergeSide proto_direction) {
  switch (proto_direction) {
    case protos::MergeSide::kMergeToLeftLane:
      return text_generation::MergeSide::kToLeftLane;
    case protos::MergeSide::kMergeToRightLane:
      return text_generation::MergeSide::kToRightLane;
    default:
      return text_generation::MergeSide::kToRightLane;
  }
  return text_generation::MergeSide::kToRightLane;
}

text_generation::QuantizedAngle ConvertQuantizedAngle(protos::QuantizedAngle proto_angle) {
  switch (proto_angle) {
    case protos::QuantizedAngle::kStraight:
      return text_generation::QuantizedAngle::kStraight;
    case protos::QuantizedAngle::kSlightRight:
      return text_generation::QuantizedAngle::kSlightRight;
    case protos::QuantizedAngle::kRight:
      return text_generation::QuantizedAngle::kRight;
    case protos::QuantizedAngle::kSharpRight:
      return text_generation::QuantizedAngle::kSharpRight;
    case protos::QuantizedAngle::kSlightLeft:
      return text_generation::QuantizedAngle::kSlightLeft;
    case protos::QuantizedAngle::kLeft:
      return text_generation::QuantizedAngle::kLeft;
    case protos::QuantizedAngle::kSharpLeft:
      return text_generation::QuantizedAngle::kSharpLeft;
    case protos::QuantizedAngle::kBack:
      return text_generation::QuantizedAngle::kBack;
    default:
      return text_generation::QuantizedAngle::kStraight;
  }
  return text_generation::QuantizedAngle::kStraight;
}

text_generation::Roundabout::Direction ConvertRoundaboutDirection(
    protos::RoundaboutDirection proto_direction) {
  switch (proto_direction) {
    case protos::RoundaboutDirection::kRoundaboutDirectionExitCross:
      return text_generation::Roundabout::Direction::kCross;
    case protos::RoundaboutDirection::kRoundaboutDirectionExitBack:
      return text_generation::Roundabout::Direction::kBack;
    case protos::RoundaboutDirection::kRoundaboutDirectionExitLeft:
      return text_generation::Roundabout::Direction::kLeft;
    case protos::RoundaboutDirection::kRoundaboutDirectionExitRight:
      return text_generation::Roundabout::Direction::kRight;
    default:
      return text_generation::Roundabout::Direction::kCross;
  }
  return text_generation::Roundabout::Direction::kCross;
}

text_generation::RoundaboutType ConvertRoundaboutType(protos::RoundaboutType proto_roundaboutType) {
  switch (proto_roundaboutType) {
    case protos::RoundaboutType::kSmall:
      return text_generation::RoundaboutType::kSmall;
    default:
      return text_generation::RoundaboutType::kDefault;
  }
  return text_generation::RoundaboutType::kDefault;
}

text_generation::Roundabout ConvertRoundabout(const protos::Roundabout& proto_roundabout) {
  return {ConvertRoundaboutDirection(proto_roundabout.direction()),
          ConvertQuantizedAngle(proto_roundabout.turn_angle()), proto_roundabout.exit_number(),
          ConvertRoundaboutType(proto_roundabout.roundabout_type())};
}

text_generation::AutoTransportType ConvertAutoTransportType(
    const protos::AutoTransportType& proto_transport) {
  switch (proto_transport) {
    case protos::AutoTransportType::kCartrain:
      return text_generation::AutoTransportType::kCarTrain;
    case protos::AutoTransportType::kFerry:
      return text_generation::AutoTransportType::kFerry;
    default:
      return text_generation::AutoTransportType::kFerry;
  }
  return text_generation::AutoTransportType::kFerry;
}
text_generation::BorderCrossing ConvertBorderCrossing(
    const protos::BorderCrossing& proto_crossing) {
  text_generation::BorderCrossing border_crossing;

  border_crossing.from_country.country_name =
      ConvertTextWithPhonetic(proto_crossing.from_country().country_name());
  border_crossing.from_country.code =
      CISOCountryCode(proto_crossing.from_country().iso_country_code());

  border_crossing.to_country.country_name =
      ConvertTextWithPhonetic(proto_crossing.to_country().country_name());
  border_crossing.to_country.code = CISOCountryCode(proto_crossing.to_country().iso_country_code());

  return border_crossing;
}
}  // namespace

protos::AudioMessage ToProto(const text_generation::AudioMessage& audio_message) {
  protos::AudioMessage proto_audio_message;
  proto_audio_message.set_text(audio_message.text);
  proto_audio_message.set_locale(audio_message.locale);
  return proto_audio_message;
}

text_generation::AnnouncementData FromProto(
    const protos::AnnouncementData& proto_announcement_data) {
  boost::optional<text_generation::AudioInstruction> next_instruction;
  if (proto_announcement_data.has_next_instruction()) {
    next_instruction = FromProto(proto_announcement_data.next_instruction());
  }

  return {orodoro::quantities::TCentimeters{proto_announcement_data.distance_in_cm()},
          ConvertMessageType(proto_announcement_data.message_type()),
          FromProto(proto_announcement_data.instruction()), next_instruction};
}

text_generation::WarningData FromProto(
        const protos::DynamicRouteGuidanceData &dynamicRouteGuidanceData) {
    auto data = text_generation::DynamicRouteGuidanceData();
    data.reason = FromProto(dynamicRouteGuidanceData.reason());
    data.mode = FromProto(dynamicRouteGuidanceData.mode());
    data.travel_time_advantage = std::chrono::seconds{dynamicRouteGuidanceData.travel_time_advantage_seconds()};
    data.travel_delay = std::chrono::seconds{dynamicRouteGuidanceData.travel_delay_seconds()};
    data.estimated_travel_duration = std::chrono::seconds{dynamicRouteGuidanceData.estimated_travel_duration_seconds()};
    data.is_charging_plan_modified = dynamicRouteGuidanceData.is_charging_plan_modified();
    if (dynamicRouteGuidanceData.has_current_time_milliseconds()) {
        std::chrono::system_clock::time_point tp{
                std::chrono::milliseconds{dynamicRouteGuidanceData.current_time_milliseconds()}};
        data.current_time = tp;
    }
    if (dynamicRouteGuidanceData.has_warning_message_type()) {
        data.warning_message_type = FromProto(dynamicRouteGuidanceData.warning_message_type());
    }
    return text_generation::WarningData(data);
}

text_generation::WarningData FromProto(
        const protos::WarningData &proto_warning_data) {
    switch (proto_warning_data.WarningDataVariant_case()) {
        case protos::WarningData::kDynamicRouteGuidanceData: {
            auto &dynamicRouteGuidanceData = proto_warning_data.dynamicrouteguidancedata();
            return FromProto(dynamicRouteGuidanceData);
        }
        case protos::WarningData::kChargingStopData: {
            auto &chargingStopData = proto_warning_data.chargingstopdata();
            return FromProto(chargingStopData);
        }
        case protos::WarningData::kTrafficEventData: {
            auto &trafficEventData = proto_warning_data.trafficeventdata();
            return FromProto(trafficEventData);
        }
    }
}

text_generation::VerbosityLevel FromProto(const protos::VerbosityLevel& proto_verbosity_level) {
  switch (proto_verbosity_level.level()) {
    case protos::VerbosityLevel::Verbosity::VerbosityLevel_Verbosity_kCompact:
      return text_generation::VerbosityLevel::kCompact;
    case protos::VerbosityLevel::Verbosity::VerbosityLevel_Verbosity_kComprehensive:
      return text_generation::VerbosityLevel::kComprehensive;
    default:
      return text_generation::VerbosityLevel::kCompact;
  }
  return text_generation::VerbosityLevel::kCompact;
}

CLanguage FromProto(const protos::Language& proto_language) {
  return {CISOLanguageCode(proto_language.iso_language_code()),
          CISOCountryCode(proto_language.iso_country_code()),
          CISOScriptCode(proto_language.iso_script_code())};
}

distance_rounding::UnitSystem FromProto(const protos::UnitSystem& proto_unit_system) {
  switch (proto_unit_system.unit_system()) {
    case protos::UnitSystem::Unit::UnitSystem_Unit_kMetric:
      return distance_rounding::UnitSystem::kMetric;
    case protos::UnitSystem::Unit::UnitSystem_Unit_kImperialUK:
      return distance_rounding::UnitSystem::kImperialUK;
    case protos::UnitSystem::Unit::UnitSystem_Unit_kImperialNorthAmerica:
      return distance_rounding::UnitSystem::kImperialNorthAmerica;
    default:
      return distance_rounding::UnitSystem::kMetric;
  }
  return distance_rounding::UnitSystem::kMetric;
}

distance_rounding::RoundingSpecification FromProto(
    const protos::RoundingSpecification& proto_rounding_specification) {
  switch (proto_rounding_specification.rounding_specification()) {
    case protos::RoundingSpecification::Specification::RoundingSpecification_Specification_kDefault:
      return distance_rounding::RoundingSpecification::kDefault;
    case protos::RoundingSpecification::Specification::RoundingSpecification_Specification_kHcp3:
      return distance_rounding::RoundingSpecification::kHcp3;
    default:
      return distance_rounding::RoundingSpecification::kDefault;
  }
  return distance_rounding::RoundingSpecification::kDefault;
}

text_generation::LaneDirection
convertLaneDirectionType(const protos::AudioInstruction_Lane_LaneDirection &proto_laneDirection) {
    switch (proto_laneDirection) {
        case protos::AudioInstruction_Lane_LaneDirection::AudioInstruction_Lane_LaneDirection_kStraight :
            return text_generation::LaneDirection::kStraight;
        case protos::AudioInstruction_Lane_LaneDirection::AudioInstruction_Lane_LaneDirection_kSlightRight :
            return text_generation::LaneDirection::kSlightRight;
        case protos::AudioInstruction_Lane_LaneDirection::AudioInstruction_Lane_LaneDirection_kRight :
            return text_generation::LaneDirection::kRight;
        case protos::AudioInstruction_Lane_LaneDirection::AudioInstruction_Lane_LaneDirection_kSharpRight :
            return text_generation::LaneDirection::kSharpRight;
        case protos::AudioInstruction_Lane_LaneDirection::AudioInstruction_Lane_LaneDirection_kRightUTurn :
            return text_generation::LaneDirection::kRightUTurn;
        case protos::AudioInstruction_Lane_LaneDirection::AudioInstruction_Lane_LaneDirection_kSlightLeft :
            return text_generation::LaneDirection::kSlightLeft;
        case protos::AudioInstruction_Lane_LaneDirection::AudioInstruction_Lane_LaneDirection_kLeft :
            return text_generation::LaneDirection::kLeft;
        case protos::AudioInstruction_Lane_LaneDirection::AudioInstruction_Lane_LaneDirection_kSharpLeft :
            return text_generation::LaneDirection::kSharpLeft;
        case protos::AudioInstruction_Lane_LaneDirection::AudioInstruction_Lane_LaneDirection_kLeftUTurn :
            return text_generation::LaneDirection::kLeftUTurn;
    }
}

text_generation::Lane convertLaneType(const protos::AudioInstruction_Lane& proto_lane) {
    text_generation::Lane lane{};
    std::vector<text_generation::LaneDirection> directions;
    directions.reserve(proto_lane.directions().size());
    std::transform(
            proto_lane.directions().cbegin(),
            proto_lane.directions().cend(),
            std::back_inserter(directions),
            [](int value) {
                return convertLaneDirectionType(static_cast<protos::AudioInstruction_Lane_LaneDirection>(value));
            });
    lane.directions = directions;
    if (proto_lane.has_follow_direction()) {
        lane.follow_direction = convertLaneDirectionType(proto_lane.follow_direction());
    }
    return lane;
}

text_generation::ChargingStop convertChargingStop(const protos::ChargingStop &proto_charging_stop) {
    text_generation::ChargingStop chargingStop{};
    if (proto_charging_stop.has_operator_name()) {
        chargingStop.operator_name = ConvertTextWithPhonetic(proto_charging_stop.operator_name());
    }
    return chargingStop;
}

text_generation::AudioInstruction FromProto(
    const protos::AudioInstruction& proto_audio_instruction) {
  text_generation::AudioInstructionBuilder builder;
  builder.SetType(ConvertAudioInstructionType(proto_audio_instruction.type()))
      .SetDrivingSide(ConvertDrivingSide(proto_audio_instruction.driving_side()))
      .SetItineraryPointSide(
          ConvertItineraryPointSide(proto_audio_instruction.itinerary_point_side()))
      .SetOutgoingRoadInformation(
          ConvertRoadInformation(proto_audio_instruction.outgoing_road_information()));

  if (proto_audio_instruction.has_incoming_road_information()) {
    builder.SetIncomingRoadInformation(
        ConvertRoadInformation(proto_audio_instruction.incoming_road_information()));
  }

  if (proto_audio_instruction.has_landmark()) {
    builder.SetLandmark(ConvertLandmark(proto_audio_instruction.landmark()));
  }

  if (proto_audio_instruction.has_signpost()) {
    builder.SetSignpost(ConvertSignpost(proto_audio_instruction.signpost()));
  }

  if (proto_audio_instruction.has_traffic_light_offset_in_centimeters()) {
    builder.SetTrafficLightOffset(text_generation::Centimeters{
        proto_audio_instruction.traffic_light_offset_in_centimeters()});
  }

  if (proto_audio_instruction.has_exit_direction()) {
    builder.SetExitDirection(ConvertExitDirection(proto_audio_instruction.exit_direction()));
  }

  if (proto_audio_instruction.has_side_street_offset_in_centimeters()) {
    builder.SetSideStreetOffset(
        text_generation::Centimeters{proto_audio_instruction.side_street_offset_in_centimeters()});
  }

  if (proto_audio_instruction.has_fork_direction()) {
    builder.SetForkDirection(ConvertForkDirection(proto_audio_instruction.fork_direction()));
  }

  if (proto_audio_instruction.has_intersection_name_with_phonetic()) {
    builder.SetIntersectionName(
        ConvertTextWithPhonetic(proto_audio_instruction.intersection_name_with_phonetic()));
  }

  if (proto_audio_instruction.has_turn_direction()) {
    builder.SetTurnDirection(ConvertTurnDirection(proto_audio_instruction.turn_direction()));
  }

  if (proto_audio_instruction.has_switch_highway_direction()) {
    builder.SetSwitchHighwayDirection(
        ConvertSwitchHighwayDirection(proto_audio_instruction.switch_highway_direction()));
  }

  if (proto_audio_instruction.has_enter_hov_direction()) {
    builder.SetEnterHovDirection(
        ConvertEnterHovDirection(proto_audio_instruction.enter_hov_direction()));
  }

  if (proto_audio_instruction.has_exit_hov_direction()) {
    builder.SetExitHovDirection(
        ConvertExitHovDirection(proto_audio_instruction.exit_hov_direction()));
  }

  if (proto_audio_instruction.has_merge_side()) {
    builder.SetMergeSide(ConvertMergeSide(proto_audio_instruction.merge_side()));
  }

  if (proto_audio_instruction.has_roundabout()) {
    builder.SetRoundabout(ConvertRoundabout(proto_audio_instruction.roundabout()));
  }

  if (proto_audio_instruction.has_exit_roundabout()) {
    builder.SetExitRoundabout(
        ConvertRoundabout(proto_audio_instruction.exit_roundabout().roundabout()));
  }

  if (proto_audio_instruction.has_border_crossing()) {
    builder.SetBorderCrossing(ConvertBorderCrossing(proto_audio_instruction.border_crossing()));
  }

  if (proto_audio_instruction.has_auto_transport_type()) {
    builder.SetAutoTransportType(
        ConvertAutoTransportType(proto_audio_instruction.auto_transport_type()));
  }

  if (proto_audio_instruction.has_charging_stop()) {
    builder.SetChargingStop(convertChargingStop(proto_audio_instruction.charging_stop()));
  }

  std::vector<text_generation::Lane> laneGuidanceList;
  auto list_size = proto_audio_instruction.lane_guidance_size();
  laneGuidanceList.reserve(list_size);
  for (int i = 0; i < list_size; ++i) {
      laneGuidanceList.push_back(convertLaneType(proto_audio_instruction.lane_guidance(i)));
  }
  builder.SetLaneGuidance(text_generation::LaneGuidance{laneGuidanceList});
  return builder.Build();
}

text_generation::DynamicRouteGuidanceData::Reason FromProto(
        const protos::DynamicRouteGuidanceData_Reason& proto_dynamic_route_guidance_data_reason){
    switch (proto_dynamic_route_guidance_data_reason) {
        case protos::DynamicRouteGuidanceData_Reason::DynamicRouteGuidanceData_Reason_kRouteBlockage:
            return text_generation::DynamicRouteGuidanceData::Reason::kRouteBlockage;
        case protos::DynamicRouteGuidanceData_Reason::DynamicRouteGuidanceData_Reason_kRouteUnreachable :
            return text_generation::DynamicRouteGuidanceData::Reason::kRouteUnreachable;
        case protos::DynamicRouteGuidanceData_Reason::DynamicRouteGuidanceData_Reason_kRouteDelay:
            return text_generation::DynamicRouteGuidanceData::Reason::kRouteDelay;
        case protos::DynamicRouteGuidanceData_Reason::DynamicRouteGuidanceData_Reason_kAltRoute :
            return text_generation::DynamicRouteGuidanceData::Reason::kAltRoute;
        case protos::DynamicRouteGuidanceData_Reason::DynamicRouteGuidanceData_Reason_kAltRouteTTA :
            return text_generation::DynamicRouteGuidanceData::Reason::kAltRouteTTA;
        case protos::DynamicRouteGuidanceData_Reason::DynamicRouteGuidanceData_Reason_kAltRouteTTADueToDelay:
            return text_generation::DynamicRouteGuidanceData::Reason::kAltRouteTTADueToDelay;
        case protos::DynamicRouteGuidanceData_Reason::DynamicRouteGuidanceData_Reason_kAltRouteDueToBlockage:
            return text_generation::DynamicRouteGuidanceData::Reason::kAltRouteDueToBlockage;
        case protos::DynamicRouteGuidanceData_Reason::DynamicRouteGuidanceData_Reason_kAltRouteDueToUnreachable :
            return text_generation::DynamicRouteGuidanceData::Reason::kAltRouteDueToUnreachable;
    }
}

text_generation::DynamicRouteGuidanceData::Mode FromProto(
        const protos::DynamicRouteGuidanceData_Mode& proto_dynamic_route_guidance_data_mode){
    switch (proto_dynamic_route_guidance_data_mode) {
        case protos::DynamicRouteGuidanceData_Mode::DynamicRouteGuidanceData_Mode_kAutomatic:
            return text_generation::DynamicRouteGuidanceData::Mode::kAutomatic;
        case protos::DynamicRouteGuidanceData_Mode::DynamicRouteGuidanceData_Mode_kSemiDynamic:
            return text_generation::DynamicRouteGuidanceData::Mode::kSemiDynamic;
    }
}

text_generation::WarningMessageType FromProto(
        const protos::DynamicRouteGuidanceData_WarningMessageType &proto_dynamic_route_guidance_data_warning_msg_type) {
    switch (proto_dynamic_route_guidance_data_warning_msg_type) {
        case protos::DynamicRouteGuidanceData_WarningMessageType_kEarly:
            return text_generation::WarningMessageType::kEarly;
        case protos::DynamicRouteGuidanceData_WarningMessageType_kApproaching:
            return text_generation::WarningMessageType::kApproaching;
        case protos::DynamicRouteGuidanceData_WarningMessageType_kApproachingExtended:
            return text_generation::WarningMessageType::kApproachingExtended;
        case protos::DynamicRouteGuidanceData_WarningMessageType_kAccepting:
            return text_generation::WarningMessageType::kAccepting;
    }
}

text_generation::WarningData FromProto(const protos::ChargingStopData &chargingStopData) {
    auto data = text_generation::EVChargingStopChangeData();
    data.type = FromProto(chargingStopData.type());
    data.charger_type = FromProto(chargingStopData.charger_type());
    data.charger_operator_name = ConvertTextWithPhonetic(chargingStopData.charger_operator_name());
    data.location_name = ConvertTextWithPhonetic(chargingStopData.location_name());
    return text_generation::WarningData(data);
}

text_generation::EVChargingStopChangeData::Type FromProto(const protos::ChargingStopData_Type &type) {
    switch (type) {
        case protos::ChargingStopData_Type_NEXT_CHARGING_STOP_REPLACED:
            return text_generation::EVChargingStopChangeData::Type::kNextChargingStopReplaced;
        case protos::ChargingStopData_Type_ALL_CHARGING_STOPS_CHANGED:
            return text_generation::EVChargingStopChangeData::Type::kAllChargingStopsChanged;
        case protos::ChargingStopData_Type_NEXT_CHARGING_STOP_REMOVED:
            return text_generation::EVChargingStopChangeData::Type::kNextChargingStopRemoved;
        case protos::ChargingStopData_Type_NEXT_CHARGING_STOP_ADDED:
            return text_generation::EVChargingStopChangeData::Type::kNextChargingStopAdded;
        case protos::ChargingStopData_Type_RETURN_TO_LAST_CHARGING_STOP:
            return text_generation::EVChargingStopChangeData::Type::kReturnToLastChargingStop;
    }
}

text_generation::EVChargingStopChangeData::ChargerType FromProto(const protos::ChargingStopData_ChargerType &type) {
    switch (type) {
        case protos::ChargingStopData_ChargerType_DEFAULT:
            return text_generation::EVChargingStopChangeData::ChargerType::kDefault;
        case protos::ChargingStopData_ChargerType_FAST:
            return text_generation::EVChargingStopChangeData::ChargerType::kFast;
    }
}

text_generation::WarningData FromProto(const protos::TrafficEventData &trafficEventData) {
    auto data = text_generation::TrafficEventData();
    data.traffic_event_type = FromProto(trafficEventData.traffic_event_type());
    data.road_number = ConvertTextWithPhonetic(trafficEventData.road_number());
    data.road_name = ConvertTextWithPhonetic(trafficEventData.road_name());
    data.start_road_name = ConvertTextWithPhonetic(trafficEventData.start_road_name());
    data.end_road_name = ConvertTextWithPhonetic(trafficEventData.end_road_name());
    data.start_exit_number = ConvertTextWithPhonetic(trafficEventData.start_exit_number());
    data.end_exit_number = ConvertTextWithPhonetic(trafficEventData.end_exit_number());
    data.travel_delay = std::chrono::seconds{trafficEventData.travel_delay_seconds()};
    return text_generation::WarningData(data);
}

text_generation::TrafficEventType FromProto(
        const protos::TrafficEventData_TrafficEventType &type) {
    switch (type) {
        case protos::TrafficEventData_TrafficEventType_UNKNOWN:
            return text_generation::TrafficEventType::kUnknown;
        case protos::TrafficEventData_TrafficEventType_STATIONARY_TRAFFIC:
            return text_generation::TrafficEventType::kStationaryTraffic;
        case protos::TrafficEventData_TrafficEventType_QUEUING_TRAFFIC:
            return text_generation::TrafficEventType::kQueuingTraffic;
        case protos::TrafficEventData_TrafficEventType_SLOW_TRAFFIC:
            return text_generation::TrafficEventType::kSlowTraffic;
        case protos::TrafficEventData_TrafficEventType_TRAFFIC_JAM:
            return text_generation::TrafficEventType::kTrafficJam;
        case protos::TrafficEventData_TrafficEventType_ACCIDENT:
            return text_generation::TrafficEventType::kAccident;
        case protos::TrafficEventData_TrafficEventType_ROAD_CLOSED:
            return text_generation::TrafficEventType::kRoadClosed;
        case protos::TrafficEventData_TrafficEventType_EXIT_RESTRICTIONS:
            return text_generation::TrafficEventType::kExitRestrictions;
        case protos::TrafficEventData_TrafficEventType_ENTRY_RESTRICTIONS:
            return text_generation::TrafficEventType::kEntryRestrictions;
        case protos::TrafficEventData_TrafficEventType_ROADWORKS:
            return text_generation::TrafficEventType::kRoadworks;
        case protos::TrafficEventData_TrafficEventType_NARROW_LANES:
            return text_generation::TrafficEventType::kNarrowLanes;
        case protos::TrafficEventData_TrafficEventType_INCIDENTS:
            return text_generation::TrafficEventType::kIncidents;
        case protos::TrafficEventData_TrafficEventType_OBSTRUCTION_HAZARDS:
            return text_generation::TrafficEventType::kObstructionHazards;
        case protos::TrafficEventData_TrafficEventType_DANGEROUS_SITUATION:
            return text_generation::TrafficEventType::kDangerousSituation;
        case protos::TrafficEventData_TrafficEventType_VEHICLES_CARRYING_HAZARDOUS_MATERIALS:
            return text_generation::TrafficEventType::kVehiclesCarryingHazardousMaterials;
        case protos::TrafficEventData_TrafficEventType_SECURITY_INCIDENT:
            return text_generation::TrafficEventType::kSecurityIncident;
        case protos::TrafficEventData_TrafficEventType_EXCEPTIONAL_LOADS:
            return text_generation::TrafficEventType::kExceptionalLoads;
        case protos::TrafficEventData_TrafficEventType_SLIPPERY_ROAD:
            return text_generation::TrafficEventType::kSlipperyRoad;
        case protos::TrafficEventData_TrafficEventType_DANGER_OF_FLASH_FLOODS:
            return text_generation::TrafficEventType::kDangerOfFlashFloods;
        case protos::TrafficEventData_TrafficEventType_HAZARDOUS_DRIVING_CONDITIONS:
            return text_generation::TrafficEventType::kHazardousDrivingConditions;
        case protos::TrafficEventData_TrafficEventType_TRAFFIC_RESTRICTIONS:
            return text_generation::TrafficEventType::kTrafficRestrictions;
        case protos::TrafficEventData_TrafficEventType_STRONG_WINDS:
            return text_generation::TrafficEventType::kStrongWinds;
        case protos::TrafficEventData_TrafficEventType_SNOWFALL:
            return text_generation::TrafficEventType::kSnowfall;
        case protos::TrafficEventData_TrafficEventType_SMOG_ALERT:
            return text_generation::TrafficEventType::kSmogAlert;
        case protos::TrafficEventData_TrafficEventType_HEAVY_RAIN:
            return text_generation::TrafficEventType::kHeavyRain;
        case protos::TrafficEventData_TrafficEventType_REDUCED_VISIBILITY:
            return text_generation::TrafficEventType::kReducedVisibility;
        case protos::TrafficEventData_TrafficEventType_FOG:
            return text_generation::TrafficEventType::kFog;
        case protos::TrafficEventData_TrafficEventType_DANGEROUS_WEATHER_CONDITIONS:
            return text_generation::TrafficEventType::kDangerousWeatherConditions;
        case protos::TrafficEventData_TrafficEventType_DRIVER_ON_WRONG_CARRIAGEWAY:
            return text_generation::TrafficEventType::kDriverOnWrongCarriageway;
        case protos::TrafficEventData_TrafficEventType_DELAYS:
            return text_generation::TrafficEventType::kDelays;
        case protos::TrafficEventData_TrafficEventType_AIR_RAID_DANGER:
            return text_generation::TrafficEventType::kAirRaidDanger;
        case protos::TrafficEventData_TrafficEventType_GUNFIRE_ON_THE_ROAD_DANGER:
            return text_generation::TrafficEventType::kGunfireOnTheRoadDanger;
        case protos::TrafficEventData_TrafficEventType_EMERGENCY_VEHICLES:
            return text_generation::TrafficEventType::kEmergencyVehicles;
        case protos::TrafficEventData_TrafficEventType_POLICE_INTERVENTION_DANGER:
            return text_generation::TrafficEventType::kPoliceInterventionDanger;
        case protos::TrafficEventData_TrafficEventType_HIGH_SPEED_CHASE:
            return text_generation::TrafficEventType::kHighSpeedChase;
        case protos::TrafficEventData_TrafficEventType_BROKEN_DOWN_VEHICLE:
            return text_generation::TrafficEventType::kBrokenDownVehicle;
    }
}
