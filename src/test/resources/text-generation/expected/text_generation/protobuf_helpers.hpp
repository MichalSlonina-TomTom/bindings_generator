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
struct AudioMessage;
struct AnnouncementData;
struct VerbosityLevel;
struct UnitSystem;
struct RoundingSpecification;
struct WarningData;
struct DynamicRouteGuidanceData;
struct ChargingStopData;
struct TrafficEventData;

namespace protobuf_helpers {

// Conversion functions for AudioMessage
AudioMessage ToNative(const AudioMessage proto);
AudioMessage ToProto(const AudioMessage& native);

// Conversion functions for AnnouncementData
AnnouncementData ToNative(const AnnouncementData proto);
AnnouncementData ToProto(const AnnouncementData& native);

// Conversion functions for MessageType
MessageType ToNative(const AnnouncementData_MessageType proto);
AnnouncementData_MessageType ToProto(const MessageType native);

// Conversion functions for VerbosityLevel
VerbosityLevel ToNative(const VerbosityLevel proto);
VerbosityLevel ToProto(const VerbosityLevel& native);

// Conversion functions for Verbosity
Verbosity ToNative(const VerbosityLevel_Verbosity proto);
VerbosityLevel_Verbosity ToProto(const Verbosity native);

// Conversion functions for UnitSystem
UnitSystem ToNative(const UnitSystem proto);
UnitSystem ToProto(const UnitSystem& native);

// Conversion functions for Unit
Unit ToNative(const UnitSystem_Unit proto);
UnitSystem_Unit ToProto(const Unit native);

// Conversion functions for RoundingSpecification
RoundingSpecification ToNative(const RoundingSpecification proto);
RoundingSpecification ToProto(const RoundingSpecification& native);

// Conversion functions for Specification
Specification ToNative(const RoundingSpecification_Specification proto);
RoundingSpecification_Specification ToProto(const Specification native);

// Conversion functions for WarningData
WarningData ToNative(const WarningData proto);
WarningData ToProto(const WarningData& native);

// Conversion functions for DynamicRouteGuidanceData
DynamicRouteGuidanceData ToNative(const DynamicRouteGuidanceData proto);
DynamicRouteGuidanceData ToProto(const DynamicRouteGuidanceData& native);

// Conversion functions for Reason
Reason ToNative(const DynamicRouteGuidanceData_Reason proto);
DynamicRouteGuidanceData_Reason ToProto(const Reason native);

// Conversion functions for Mode
Mode ToNative(const DynamicRouteGuidanceData_Mode proto);
DynamicRouteGuidanceData_Mode ToProto(const Mode native);

// Conversion functions for WarningMessageType
WarningMessageType ToNative(const DynamicRouteGuidanceData_WarningMessageType proto);
DynamicRouteGuidanceData_WarningMessageType ToProto(const WarningMessageType native);

// Conversion functions for ChargingStopData
ChargingStopData ToNative(const ChargingStopData proto);
ChargingStopData ToProto(const ChargingStopData& native);

// Conversion functions for Type
Type ToNative(const ChargingStopData_Type proto);
ChargingStopData_Type ToProto(const Type native);

// Conversion functions for ChargerType
ChargerType ToNative(const ChargingStopData_ChargerType proto);
ChargingStopData_ChargerType ToProto(const ChargerType native);

// Conversion functions for TrafficEventData
TrafficEventData ToNative(const TrafficEventData proto);
TrafficEventData ToProto(const TrafficEventData& native);

// Conversion functions for TrafficEventType
TrafficEventType ToNative(const TrafficEventData_TrafficEventType proto);
TrafficEventData_TrafficEventType ToProto(const TrafficEventType native);

}  // namespace protobuf_helpers

#endif // PROTOBUF_HELPERS_HPP
