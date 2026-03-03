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

AudioMessage ToNative(const AudioMessage proto) {
    AudioMessage result;
    result.text = proto.text();
    result.locale = proto.locale();
    return result;
}

AudioMessage ToProto(const AudioMessage& native) {
    AudioMessage result;
    result.set_text(native.text);
    result.set_locale(native.locale);
    return result;
}

AnnouncementData ToNative(const AnnouncementData proto) {
    AnnouncementData result;
    result.distanceInCm = proto.distance_in_cm();
    result.messageType = ToNative(proto.message_type());
    result.instruction = ToNative(proto.instruction());
    result.nextInstruction = ToNative(proto.next_instruction());
    return result;
}

AnnouncementData ToProto(const AnnouncementData& native) {
    AnnouncementData result;
    result.set_distance_in_cm(native.distanceInCm);
    result.set_message_type(ToProto(native.messageType));
    result.mutable_instruction()->CopyFrom(ToProto(native.instruction));
    result.mutable_next_instruction()->CopyFrom(ToProto(native.nextInstruction));
    return result;
}

MessageType ToNative(const AnnouncementData_MessageType proto) {
    switch (proto) {
        case AnnouncementData_MessageType::kFollow: return MessageType::KFOLLOW;
        case AnnouncementData_MessageType::kFarAway: return MessageType::KFARAWAY;
        case AnnouncementData_MessageType::kWarning: return MessageType::KWARNING;
        case AnnouncementData_MessageType::kMain: return MessageType::KMAIN;
        case AnnouncementData_MessageType::kConfirmation: return MessageType::KCONFIRMATION;
        case AnnouncementData_MessageType::kExtendedConfirmation: return MessageType::KEXTENDEDCONFIRMATION;
        default: return MessageType::KFOLLOW;
    }
}

AnnouncementData_MessageType ToProto(const MessageType native) {
    switch (native) {
        case MessageType::KFOLLOW: return AnnouncementData_MessageType::kFollow;
        case MessageType::KFARAWAY: return AnnouncementData_MessageType::kFarAway;
        case MessageType::KWARNING: return AnnouncementData_MessageType::kWarning;
        case MessageType::KMAIN: return AnnouncementData_MessageType::kMain;
        case MessageType::KCONFIRMATION: return AnnouncementData_MessageType::kConfirmation;
        case MessageType::KEXTENDEDCONFIRMATION: return AnnouncementData_MessageType::kExtendedConfirmation;
        default: return AnnouncementData_MessageType::kFollow;
    }
}

VerbosityLevel ToNative(const VerbosityLevel proto) {
    VerbosityLevel result;
    result.level = ToNative(proto.level());
    return result;
}

VerbosityLevel ToProto(const VerbosityLevel& native) {
    VerbosityLevel result;
    result.set_level(ToProto(native.level));
    return result;
}

Verbosity ToNative(const VerbosityLevel_Verbosity proto) {
    switch (proto) {
        case VerbosityLevel_Verbosity::kComprehensive: return Verbosity::KCOMPREHENSIVE;
        case VerbosityLevel_Verbosity::kCompact: return Verbosity::KCOMPACT;
        default: return Verbosity::KCOMPREHENSIVE;
    }
}

VerbosityLevel_Verbosity ToProto(const Verbosity native) {
    switch (native) {
        case Verbosity::KCOMPREHENSIVE: return VerbosityLevel_Verbosity::kComprehensive;
        case Verbosity::KCOMPACT: return VerbosityLevel_Verbosity::kCompact;
        default: return VerbosityLevel_Verbosity::kComprehensive;
    }
}

UnitSystem ToNative(const UnitSystem proto) {
    UnitSystem result;
    result.unitSystem = ToNative(proto.unit_system());
    return result;
}

UnitSystem ToProto(const UnitSystem& native) {
    UnitSystem result;
    result.set_unit_system(ToProto(native.unitSystem));
    return result;
}

Unit ToNative(const UnitSystem_Unit proto) {
    switch (proto) {
        case UnitSystem_Unit::kMetric: return Unit::KMETRIC;
        case UnitSystem_Unit::kImperialUK: return Unit::KIMPERIALUK;
        case UnitSystem_Unit::kImperialNorthAmerica: return Unit::KIMPERIALNORTHAMERICA;
        default: return Unit::KMETRIC;
    }
}

UnitSystem_Unit ToProto(const Unit native) {
    switch (native) {
        case Unit::KMETRIC: return UnitSystem_Unit::kMetric;
        case Unit::KIMPERIALUK: return UnitSystem_Unit::kImperialUK;
        case Unit::KIMPERIALNORTHAMERICA: return UnitSystem_Unit::kImperialNorthAmerica;
        default: return UnitSystem_Unit::kMetric;
    }
}

RoundingSpecification ToNative(const RoundingSpecification proto) {
    RoundingSpecification result;
    result.roundingSpecification = ToNative(proto.rounding_specification());
    return result;
}

RoundingSpecification ToProto(const RoundingSpecification& native) {
    RoundingSpecification result;
    result.set_rounding_specification(ToProto(native.roundingSpecification));
    return result;
}

Specification ToNative(const RoundingSpecification_Specification proto) {
    switch (proto) {
        case RoundingSpecification_Specification::kDefault: return Specification::KDEFAULT;
        case RoundingSpecification_Specification::kHcp3: return Specification::KHCP3;
        default: return Specification::KDEFAULT;
    }
}

RoundingSpecification_Specification ToProto(const Specification native) {
    switch (native) {
        case Specification::KDEFAULT: return RoundingSpecification_Specification::kDefault;
        case Specification::KHCP3: return RoundingSpecification_Specification::kHcp3;
        default: return RoundingSpecification_Specification::kDefault;
    }
}

WarningData ToNative(const WarningData proto) {
    WarningData result;
    switch (proto.WarningDataVariant_case()) {
        case WarningData::kDynamicRouteGuidanceData: result.dynamicRouteGuidanceData = proto.dynamicRouteGuidanceData(); break;
        case WarningData::kChargingStopData: result.ChargingStopData = proto.ChargingStopData(); break;
        case WarningData::kTrafficEventData: result.trafficEventData = proto.trafficEventData(); break;
        default: break;
    }
    return result;
}

WarningData ToProto(const WarningData& native) {
    WarningData result;
    if (native.WarningDataVariant_case == WarningData::kDynamicRouteGuidanceData) {
        result.set_dynamicRouteGuidanceData(native.dynamicRouteGuidanceData);
    }
    if (native.WarningDataVariant_case == WarningData::kChargingStopData) {
        result.set_ChargingStopData(native.ChargingStopData);
    }
    if (native.WarningDataVariant_case == WarningData::kTrafficEventData) {
        result.set_trafficEventData(native.trafficEventData);
    }
    return result;
}

DynamicRouteGuidanceData ToNative(const DynamicRouteGuidanceData proto) {
    DynamicRouteGuidanceData result;
    result.reason = ToNative(proto.reason());
    result.mode = ToNative(proto.mode());
    result.travelTimeAdvantageSeconds = proto.travel_time_advantage_seconds();
    result.travelDelaySeconds = proto.travel_delay_seconds();
    result.estimatedTravelDurationSeconds = proto.estimated_travel_duration_seconds();
    result.isChargingPlanModified = proto.is_charging_plan_modified();
    if (proto.has_current_time_milliseconds()) { result.currentTimeMilliseconds = proto.current_time_milliseconds(); }
    result.warningMessageType = ToNative(proto.warning_message_type());
    return result;
}

DynamicRouteGuidanceData ToProto(const DynamicRouteGuidanceData& native) {
    DynamicRouteGuidanceData result;
    result.set_reason(ToProto(native.reason));
    result.set_mode(ToProto(native.mode));
    result.set_travel_time_advantage_seconds(native.travelTimeAdvantageSeconds);
    result.set_travel_delay_seconds(native.travelDelaySeconds);
    result.set_estimated_travel_duration_seconds(native.estimatedTravelDurationSeconds);
    result.set_is_charging_plan_modified(native.isChargingPlanModified);
    if (native.currentTimeMilliseconds.has_value()) { result.set_current_time_milliseconds(native.currentTimeMilliseconds.value()); }
    result.set_warning_message_type(ToProto(native.warningMessageType));
    return result;
}

Reason ToNative(const DynamicRouteGuidanceData_Reason proto) {
    switch (proto) {
        case DynamicRouteGuidanceData_Reason::kRouteBlockage: return Reason::KROUTEBLOCKAGE;
        case DynamicRouteGuidanceData_Reason::kRouteUnreachable: return Reason::KROUTEUNREACHABLE;
        case DynamicRouteGuidanceData_Reason::kRouteDelay: return Reason::KROUTEDELAY;
        case DynamicRouteGuidanceData_Reason::kAltRoute: return Reason::KALTROUTE;
        case DynamicRouteGuidanceData_Reason::kAltRouteTTA: return Reason::KALTROUTETTA;
        case DynamicRouteGuidanceData_Reason::kAltRouteTTADueToDelay: return Reason::KALTROUTETTADUETODELAY;
        case DynamicRouteGuidanceData_Reason::kAltRouteDueToBlockage: return Reason::KALTROUTEDUETOBLOCKAGE;
        case DynamicRouteGuidanceData_Reason::kAltRouteDueToUnreachable: return Reason::KALTROUTEDUETOUNREACHABLE;
        default: return Reason::KROUTEBLOCKAGE;
    }
}

DynamicRouteGuidanceData_Reason ToProto(const Reason native) {
    switch (native) {
        case Reason::KROUTEBLOCKAGE: return DynamicRouteGuidanceData_Reason::kRouteBlockage;
        case Reason::KROUTEUNREACHABLE: return DynamicRouteGuidanceData_Reason::kRouteUnreachable;
        case Reason::KROUTEDELAY: return DynamicRouteGuidanceData_Reason::kRouteDelay;
        case Reason::KALTROUTE: return DynamicRouteGuidanceData_Reason::kAltRoute;
        case Reason::KALTROUTETTA: return DynamicRouteGuidanceData_Reason::kAltRouteTTA;
        case Reason::KALTROUTETTADUETODELAY: return DynamicRouteGuidanceData_Reason::kAltRouteTTADueToDelay;
        case Reason::KALTROUTEDUETOBLOCKAGE: return DynamicRouteGuidanceData_Reason::kAltRouteDueToBlockage;
        case Reason::KALTROUTEDUETOUNREACHABLE: return DynamicRouteGuidanceData_Reason::kAltRouteDueToUnreachable;
        default: return DynamicRouteGuidanceData_Reason::kRouteBlockage;
    }
}

Mode ToNative(const DynamicRouteGuidanceData_Mode proto) {
    switch (proto) {
        case DynamicRouteGuidanceData_Mode::kAutomatic: return Mode::KAUTOMATIC;
        case DynamicRouteGuidanceData_Mode::kSemiDynamic: return Mode::KSEMIDYNAMIC;
        default: return Mode::KAUTOMATIC;
    }
}

DynamicRouteGuidanceData_Mode ToProto(const Mode native) {
    switch (native) {
        case Mode::KAUTOMATIC: return DynamicRouteGuidanceData_Mode::kAutomatic;
        case Mode::KSEMIDYNAMIC: return DynamicRouteGuidanceData_Mode::kSemiDynamic;
        default: return DynamicRouteGuidanceData_Mode::kAutomatic;
    }
}

WarningMessageType ToNative(const DynamicRouteGuidanceData_WarningMessageType proto) {
    switch (proto) {
        case DynamicRouteGuidanceData_WarningMessageType::kEarly: return WarningMessageType::KEARLY;
        case DynamicRouteGuidanceData_WarningMessageType::kApproaching: return WarningMessageType::KAPPROACHING;
        case DynamicRouteGuidanceData_WarningMessageType::kApproachingExtended: return WarningMessageType::KAPPROACHINGEXTENDED;
        case DynamicRouteGuidanceData_WarningMessageType::kAccepting: return WarningMessageType::KACCEPTING;
        default: return WarningMessageType::KEARLY;
    }
}

DynamicRouteGuidanceData_WarningMessageType ToProto(const WarningMessageType native) {
    switch (native) {
        case WarningMessageType::KEARLY: return DynamicRouteGuidanceData_WarningMessageType::kEarly;
        case WarningMessageType::KAPPROACHING: return DynamicRouteGuidanceData_WarningMessageType::kApproaching;
        case WarningMessageType::KAPPROACHINGEXTENDED: return DynamicRouteGuidanceData_WarningMessageType::kApproachingExtended;
        case WarningMessageType::KACCEPTING: return DynamicRouteGuidanceData_WarningMessageType::kAccepting;
        default: return DynamicRouteGuidanceData_WarningMessageType::kEarly;
    }
}

ChargingStopData ToNative(const ChargingStopData proto) {
    ChargingStopData result;
    result.type = ToNative(proto.type());
    result.chargerType = ToNative(proto.charger_type());
    result.chargerOperatorName = ToNative(proto.charger_operator_name());
    result.locationName = ToNative(proto.location_name());
    return result;
}

ChargingStopData ToProto(const ChargingStopData& native) {
    ChargingStopData result;
    result.set_type(ToProto(native.type));
    result.set_charger_type(ToProto(native.chargerType));
    result.mutable_charger_operator_name()->CopyFrom(ToProto(native.chargerOperatorName));
    result.mutable_location_name()->CopyFrom(ToProto(native.locationName));
    return result;
}

Type ToNative(const ChargingStopData_Type proto) {
    switch (proto) {
        case ChargingStopData_Type::NEXT_CHARGING_STOP_REPLACED: return Type::NEXT_CHARGING_STOP_REPLACED;
        case ChargingStopData_Type::ALL_CHARGING_STOPS_CHANGED: return Type::ALL_CHARGING_STOPS_CHANGED;
        case ChargingStopData_Type::NEXT_CHARGING_STOP_REMOVED: return Type::NEXT_CHARGING_STOP_REMOVED;
        case ChargingStopData_Type::NEXT_CHARGING_STOP_ADDED: return Type::NEXT_CHARGING_STOP_ADDED;
        case ChargingStopData_Type::RETURN_TO_LAST_CHARGING_STOP: return Type::RETURN_TO_LAST_CHARGING_STOP;
        default: return Type::NEXT_CHARGING_STOP_REPLACED;
    }
}

ChargingStopData_Type ToProto(const Type native) {
    switch (native) {
        case Type::NEXT_CHARGING_STOP_REPLACED: return ChargingStopData_Type::NEXT_CHARGING_STOP_REPLACED;
        case Type::ALL_CHARGING_STOPS_CHANGED: return ChargingStopData_Type::ALL_CHARGING_STOPS_CHANGED;
        case Type::NEXT_CHARGING_STOP_REMOVED: return ChargingStopData_Type::NEXT_CHARGING_STOP_REMOVED;
        case Type::NEXT_CHARGING_STOP_ADDED: return ChargingStopData_Type::NEXT_CHARGING_STOP_ADDED;
        case Type::RETURN_TO_LAST_CHARGING_STOP: return ChargingStopData_Type::RETURN_TO_LAST_CHARGING_STOP;
        default: return ChargingStopData_Type::NEXT_CHARGING_STOP_REPLACED;
    }
}

ChargerType ToNative(const ChargingStopData_ChargerType proto) {
    switch (proto) {
        case ChargingStopData_ChargerType::DEFAULT: return ChargerType::DEFAULT;
        case ChargingStopData_ChargerType::FAST: return ChargerType::FAST;
        default: return ChargerType::DEFAULT;
    }
}

ChargingStopData_ChargerType ToProto(const ChargerType native) {
    switch (native) {
        case ChargerType::DEFAULT: return ChargingStopData_ChargerType::DEFAULT;
        case ChargerType::FAST: return ChargingStopData_ChargerType::FAST;
        default: return ChargingStopData_ChargerType::DEFAULT;
    }
}

TrafficEventData ToNative(const TrafficEventData proto) {
    TrafficEventData result;
    result.trafficEventType = ToNative(proto.traffic_event_type());
    result.roadNumber = ToNative(proto.road_number());
    result.roadName = ToNative(proto.road_name());
    result.startRoadName = ToNative(proto.start_road_name());
    result.endRoadName = ToNative(proto.end_road_name());
    result.startExitNumber = ToNative(proto.start_exit_number());
    result.endExitNumber = ToNative(proto.end_exit_number());
    if (proto.has_travel_delay_seconds()) { result.travelDelaySeconds = proto.travel_delay_seconds(); }
    return result;
}

TrafficEventData ToProto(const TrafficEventData& native) {
    TrafficEventData result;
    result.set_traffic_event_type(ToProto(native.trafficEventType));
    result.mutable_road_number()->CopyFrom(ToProto(native.roadNumber));
    result.mutable_road_name()->CopyFrom(ToProto(native.roadName));
    result.mutable_start_road_name()->CopyFrom(ToProto(native.startRoadName));
    result.mutable_end_road_name()->CopyFrom(ToProto(native.endRoadName));
    result.mutable_start_exit_number()->CopyFrom(ToProto(native.startExitNumber));
    result.mutable_end_exit_number()->CopyFrom(ToProto(native.endExitNumber));
    if (native.travelDelaySeconds.has_value()) { result.set_travel_delay_seconds(native.travelDelaySeconds.value()); }
    return result;
}

TrafficEventType ToNative(const TrafficEventData_TrafficEventType proto) {
    switch (proto) {
        case TrafficEventData_TrafficEventType::UNKNOWN: return TrafficEventType::UNKNOWN;
        case TrafficEventData_TrafficEventType::STATIONARY_TRAFFIC: return TrafficEventType::STATIONARY_TRAFFIC;
        case TrafficEventData_TrafficEventType::QUEUING_TRAFFIC: return TrafficEventType::QUEUING_TRAFFIC;
        case TrafficEventData_TrafficEventType::SLOW_TRAFFIC: return TrafficEventType::SLOW_TRAFFIC;
        case TrafficEventData_TrafficEventType::TRAFFIC_JAM: return TrafficEventType::TRAFFIC_JAM;
        case TrafficEventData_TrafficEventType::ACCIDENT: return TrafficEventType::ACCIDENT;
        case TrafficEventData_TrafficEventType::ROAD_CLOSED: return TrafficEventType::ROAD_CLOSED;
        case TrafficEventData_TrafficEventType::EXIT_RESTRICTIONS: return TrafficEventType::EXIT_RESTRICTIONS;
        case TrafficEventData_TrafficEventType::ENTRY_RESTRICTIONS: return TrafficEventType::ENTRY_RESTRICTIONS;
        case TrafficEventData_TrafficEventType::ROADWORKS: return TrafficEventType::ROADWORKS;
        case TrafficEventData_TrafficEventType::NARROW_LANES: return TrafficEventType::NARROW_LANES;
        case TrafficEventData_TrafficEventType::INCIDENTS: return TrafficEventType::INCIDENTS;
        case TrafficEventData_TrafficEventType::OBSTRUCTION_HAZARDS: return TrafficEventType::OBSTRUCTION_HAZARDS;
        case TrafficEventData_TrafficEventType::DANGEROUS_SITUATION: return TrafficEventType::DANGEROUS_SITUATION;
        case TrafficEventData_TrafficEventType::VEHICLES_CARRYING_HAZARDOUS_MATERIALS: return TrafficEventType::VEHICLES_CARRYING_HAZARDOUS_MATERIALS;
        case TrafficEventData_TrafficEventType::SECURITY_INCIDENT: return TrafficEventType::SECURITY_INCIDENT;
        case TrafficEventData_TrafficEventType::EXCEPTIONAL_LOADS: return TrafficEventType::EXCEPTIONAL_LOADS;
        case TrafficEventData_TrafficEventType::SLIPPERY_ROAD: return TrafficEventType::SLIPPERY_ROAD;
        case TrafficEventData_TrafficEventType::DANGER_OF_FLASH_FLOODS: return TrafficEventType::DANGER_OF_FLASH_FLOODS;
        case TrafficEventData_TrafficEventType::HAZARDOUS_DRIVING_CONDITIONS: return TrafficEventType::HAZARDOUS_DRIVING_CONDITIONS;
        case TrafficEventData_TrafficEventType::TRAFFIC_RESTRICTIONS: return TrafficEventType::TRAFFIC_RESTRICTIONS;
        case TrafficEventData_TrafficEventType::STRONG_WINDS: return TrafficEventType::STRONG_WINDS;
        case TrafficEventData_TrafficEventType::SNOWFALL: return TrafficEventType::SNOWFALL;
        case TrafficEventData_TrafficEventType::SMOG_ALERT: return TrafficEventType::SMOG_ALERT;
        case TrafficEventData_TrafficEventType::HEAVY_RAIN: return TrafficEventType::HEAVY_RAIN;
        case TrafficEventData_TrafficEventType::REDUCED_VISIBILITY: return TrafficEventType::REDUCED_VISIBILITY;
        case TrafficEventData_TrafficEventType::FOG: return TrafficEventType::FOG;
        case TrafficEventData_TrafficEventType::DANGEROUS_WEATHER_CONDITIONS: return TrafficEventType::DANGEROUS_WEATHER_CONDITIONS;
        case TrafficEventData_TrafficEventType::DRIVER_ON_WRONG_CARRIAGEWAY: return TrafficEventType::DRIVER_ON_WRONG_CARRIAGEWAY;
        case TrafficEventData_TrafficEventType::DELAYS: return TrafficEventType::DELAYS;
        case TrafficEventData_TrafficEventType::AIR_RAID_DANGER: return TrafficEventType::AIR_RAID_DANGER;
        case TrafficEventData_TrafficEventType::GUNFIRE_ON_THE_ROAD_DANGER: return TrafficEventType::GUNFIRE_ON_THE_ROAD_DANGER;
        case TrafficEventData_TrafficEventType::EMERGENCY_VEHICLES: return TrafficEventType::EMERGENCY_VEHICLES;
        case TrafficEventData_TrafficEventType::POLICE_INTERVENTION_DANGER: return TrafficEventType::POLICE_INTERVENTION_DANGER;
        case TrafficEventData_TrafficEventType::HIGH_SPEED_CHASE: return TrafficEventType::HIGH_SPEED_CHASE;
        case TrafficEventData_TrafficEventType::BROKEN_DOWN_VEHICLE: return TrafficEventType::BROKEN_DOWN_VEHICLE;
        default: return TrafficEventType::UNKNOWN;
    }
}

TrafficEventData_TrafficEventType ToProto(const TrafficEventType native) {
    switch (native) {
        case TrafficEventType::UNKNOWN: return TrafficEventData_TrafficEventType::UNKNOWN;
        case TrafficEventType::STATIONARY_TRAFFIC: return TrafficEventData_TrafficEventType::STATIONARY_TRAFFIC;
        case TrafficEventType::QUEUING_TRAFFIC: return TrafficEventData_TrafficEventType::QUEUING_TRAFFIC;
        case TrafficEventType::SLOW_TRAFFIC: return TrafficEventData_TrafficEventType::SLOW_TRAFFIC;
        case TrafficEventType::TRAFFIC_JAM: return TrafficEventData_TrafficEventType::TRAFFIC_JAM;
        case TrafficEventType::ACCIDENT: return TrafficEventData_TrafficEventType::ACCIDENT;
        case TrafficEventType::ROAD_CLOSED: return TrafficEventData_TrafficEventType::ROAD_CLOSED;
        case TrafficEventType::EXIT_RESTRICTIONS: return TrafficEventData_TrafficEventType::EXIT_RESTRICTIONS;
        case TrafficEventType::ENTRY_RESTRICTIONS: return TrafficEventData_TrafficEventType::ENTRY_RESTRICTIONS;
        case TrafficEventType::ROADWORKS: return TrafficEventData_TrafficEventType::ROADWORKS;
        case TrafficEventType::NARROW_LANES: return TrafficEventData_TrafficEventType::NARROW_LANES;
        case TrafficEventType::INCIDENTS: return TrafficEventData_TrafficEventType::INCIDENTS;
        case TrafficEventType::OBSTRUCTION_HAZARDS: return TrafficEventData_TrafficEventType::OBSTRUCTION_HAZARDS;
        case TrafficEventType::DANGEROUS_SITUATION: return TrafficEventData_TrafficEventType::DANGEROUS_SITUATION;
        case TrafficEventType::VEHICLES_CARRYING_HAZARDOUS_MATERIALS: return TrafficEventData_TrafficEventType::VEHICLES_CARRYING_HAZARDOUS_MATERIALS;
        case TrafficEventType::SECURITY_INCIDENT: return TrafficEventData_TrafficEventType::SECURITY_INCIDENT;
        case TrafficEventType::EXCEPTIONAL_LOADS: return TrafficEventData_TrafficEventType::EXCEPTIONAL_LOADS;
        case TrafficEventType::SLIPPERY_ROAD: return TrafficEventData_TrafficEventType::SLIPPERY_ROAD;
        case TrafficEventType::DANGER_OF_FLASH_FLOODS: return TrafficEventData_TrafficEventType::DANGER_OF_FLASH_FLOODS;
        case TrafficEventType::HAZARDOUS_DRIVING_CONDITIONS: return TrafficEventData_TrafficEventType::HAZARDOUS_DRIVING_CONDITIONS;
        case TrafficEventType::TRAFFIC_RESTRICTIONS: return TrafficEventData_TrafficEventType::TRAFFIC_RESTRICTIONS;
        case TrafficEventType::STRONG_WINDS: return TrafficEventData_TrafficEventType::STRONG_WINDS;
        case TrafficEventType::SNOWFALL: return TrafficEventData_TrafficEventType::SNOWFALL;
        case TrafficEventType::SMOG_ALERT: return TrafficEventData_TrafficEventType::SMOG_ALERT;
        case TrafficEventType::HEAVY_RAIN: return TrafficEventData_TrafficEventType::HEAVY_RAIN;
        case TrafficEventType::REDUCED_VISIBILITY: return TrafficEventData_TrafficEventType::REDUCED_VISIBILITY;
        case TrafficEventType::FOG: return TrafficEventData_TrafficEventType::FOG;
        case TrafficEventType::DANGEROUS_WEATHER_CONDITIONS: return TrafficEventData_TrafficEventType::DANGEROUS_WEATHER_CONDITIONS;
        case TrafficEventType::DRIVER_ON_WRONG_CARRIAGEWAY: return TrafficEventData_TrafficEventType::DRIVER_ON_WRONG_CARRIAGEWAY;
        case TrafficEventType::DELAYS: return TrafficEventData_TrafficEventType::DELAYS;
        case TrafficEventType::AIR_RAID_DANGER: return TrafficEventData_TrafficEventType::AIR_RAID_DANGER;
        case TrafficEventType::GUNFIRE_ON_THE_ROAD_DANGER: return TrafficEventData_TrafficEventType::GUNFIRE_ON_THE_ROAD_DANGER;
        case TrafficEventType::EMERGENCY_VEHICLES: return TrafficEventData_TrafficEventType::EMERGENCY_VEHICLES;
        case TrafficEventType::POLICE_INTERVENTION_DANGER: return TrafficEventData_TrafficEventType::POLICE_INTERVENTION_DANGER;
        case TrafficEventType::HIGH_SPEED_CHASE: return TrafficEventData_TrafficEventType::HIGH_SPEED_CHASE;
        case TrafficEventType::BROKEN_DOWN_VEHICLE: return TrafficEventData_TrafficEventType::BROKEN_DOWN_VEHICLE;
        default: return TrafficEventData_TrafficEventType::UNKNOWN;
    }
}

}  // namespace protobuf_helpers
