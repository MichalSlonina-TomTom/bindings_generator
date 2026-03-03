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

#ifndef PROTOBUF_HELPERS_HPP
#define PROTOBUF_HELPERS_HPP

#include <orodoro/i18n/language.hpp>

#include <tomtom/navkit2/navigation/distance_rounding/common_types.hpp>
#include <tomtom/navkit2/text_generation/audio_message_data.hpp>
#include <tomtom/navkit2/text_generation/warning_data.hpp>
#include <tomtom/navkit2/text_generation/common_types.hpp>

#include "audio_instruction.pb.h"
#include "text_generation.pb.h"

com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::AudioMessage ToProto(
    const tomtom::navkit2::text_generation::AudioMessage& audio_message);

tomtom::navkit2::text_generation::AnnouncementData FromProto(
    const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        AnnouncementData& proto_audio_message_data);

tomtom::navkit2::text_generation::WarningData FromProto(
        const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        WarningData& proto_warning_data);

tomtom::navkit2::text_generation::VerbosityLevel FromProto(
    const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        VerbosityLevel& proto_verbosity_level);

orodoro::i18n::CLanguage FromProto(
    const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::Language&
        proto_language);

tomtom::navkit2::distance_rounding::UnitSystem FromProto(
    const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::UnitSystem&
        proto_unit_system);

tomtom::navkit2::distance_rounding::RoundingSpecification FromProto(
    const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        RoundingSpecification& proto_rounding_specification);

tomtom::navkit2::text_generation::AudioInstruction FromProto(
    const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        AudioInstruction& proto_audio_instruction);

tomtom::navkit2::text_generation::DynamicRouteGuidanceData::Reason FromProto(
        const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        DynamicRouteGuidanceData_Reason& proto_dynamic_route_guidance_data_reason);

tomtom::navkit2::text_generation::DynamicRouteGuidanceData::Mode FromProto(
        const  com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        DynamicRouteGuidanceData_Mode& proto_dynamic_route_guidance_data_mode);

tomtom::navkit2::text_generation::WarningMessageType FromProto(
        const  com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        DynamicRouteGuidanceData_WarningMessageType& proto_dynamic_route_guidance_data_warning_msg_type);

tomtom::navkit2::text_generation::WarningData FromProto(
        const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        DynamicRouteGuidanceData &dynamicRouteGuidanceData);

tomtom::navkit2::text_generation::WarningData FromProto(
        const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        ChargingStopData &chargingStopData);

tomtom::navkit2::text_generation::EVChargingStopChangeData::Type FromProto(
        const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        ChargingStopData_Type &type);

tomtom::navkit2::text_generation::EVChargingStopChangeData::ChargerType FromProto(
        const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        ChargingStopData_ChargerType &type);

tomtom::navkit2::text_generation::WarningData FromProto(
        const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        TrafficEventData &trafficEventData);

tomtom::navkit2::text_generation::TrafficEventType FromProto(
        const com::tomtom::sdk::navigation::verbalmessagegeneration::infrastructure::protos::
        TrafficEventData_TrafficEventType &type);

#endif  // PROTOBUF_HELPERS_HPP
