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

JunctionViewType ToNative(const JunctionViewType proto) {
    switch (proto) {
        case JunctionViewType::kJunction: return JunctionViewType::KJUNCTION;
        case JunctionViewType::kSignboard: return JunctionViewType::KSIGNBOARD;
        case JunctionViewType::kEtc: return JunctionViewType::KETC;
        default: return JunctionViewType::KJUNCTION;
    }
}

JunctionViewType ToProto(const JunctionViewType native) {
    switch (native) {
        case JunctionViewType::KJUNCTION: return JunctionViewType::kJunction;
        case JunctionViewType::KSIGNBOARD: return JunctionViewType::kSignboard;
        case JunctionViewType::KETC: return JunctionViewType::kEtc;
        default: return JunctionViewType::kJunction;
    }
}

JunctionViewDaylightType ToNative(const JunctionViewDaylightType proto) {
    switch (proto) {
        case JunctionViewDaylightType::kDay: return JunctionViewDaylightType::KDAY;
        case JunctionViewDaylightType::kNight: return JunctionViewDaylightType::KNIGHT;
        case JunctionViewDaylightType::kAlways: return JunctionViewDaylightType::KALWAYS;
        default: return JunctionViewDaylightType::KDAY;
    }
}

JunctionViewDaylightType ToProto(const JunctionViewDaylightType native) {
    switch (native) {
        case JunctionViewDaylightType::KDAY: return JunctionViewDaylightType::kDay;
        case JunctionViewDaylightType::KNIGHT: return JunctionViewDaylightType::kNight;
        case JunctionViewDaylightType::KALWAYS: return JunctionViewDaylightType::kAlways;
        default: return JunctionViewDaylightType::kDay;
    }
}

JunctionViewInformationList ToNative(const JunctionViewInformationList proto) {
    JunctionViewInformationList result;
    for (const auto& item : proto.junction_view_information_list()) { result.junctionViewInformationList.push_back(ToNative(item)); }
    return result;
}

JunctionViewInformationList ToProto(const JunctionViewInformationList& native) {
    JunctionViewInformationList result;
    for (const auto& item : native.junctionViewInformationList) {
        result.add_junction_view_information_list(item);
    }
    return result;
}

JunctionViewInformation ToNative(const JunctionViewInformation proto) {
    JunctionViewInformation result;
    result.dataPng = proto.data_png();
    result.type = ToNative(proto.type());
    result.daylightType = ToNative(proto.daylight_type());
    result.startRouteOffsetInCentimeters = proto.start_route_offset_in_centimeters();
    result.endRouteOffsetInCentimeters = proto.end_route_offset_in_centimeters();
    return result;
}

JunctionViewInformation ToProto(const JunctionViewInformation& native) {
    JunctionViewInformation result;
    result.set_data_png(native.dataPng);
    result.set_type(ToProto(native.type));
    result.set_daylight_type(ToProto(native.daylightType));
    result.set_start_route_offset_in_centimeters(native.startRouteOffsetInCentimeters);
    result.set_end_route_offset_in_centimeters(native.endRouteOffsetInCentimeters);
    return result;
}

JunctionViewError ToNative(const JunctionViewError proto) {
    JunctionViewError result;
    result.errorType = ToNative(proto.errorType());
    result.message = proto.message();
    return result;
}

JunctionViewError ToProto(const JunctionViewError& native) {
    JunctionViewError result;
    result.set_errorType(ToProto(native.errorType));
    result.set_message(native.message);
    return result;
}

ErrorType ToNative(const JunctionViewError_ErrorType proto) {
    switch (proto) {
        case JunctionViewError_ErrorType::kArcVersionMismatch: return ErrorType::KARCVERSIONMISMATCH;
        case JunctionViewError_ErrorType::kMoreArcsRequired: return ErrorType::KMOREARCSREQUIRED;
        default: return ErrorType::KARCVERSIONMISMATCH;
    }
}

JunctionViewError_ErrorType ToProto(const ErrorType native) {
    switch (native) {
        case ErrorType::KARCVERSIONMISMATCH: return JunctionViewError_ErrorType::kArcVersionMismatch;
        case ErrorType::KMOREARCSREQUIRED: return JunctionViewError_ErrorType::kMoreArcsRequired;
        default: return JunctionViewError_ErrorType::kArcVersionMismatch;
    }
}

JunctionViewResult ToNative(const JunctionViewResult proto) {
    JunctionViewResult result;
    switch (proto.Result_case()) {
        case JunctionViewResult::kJunctionViews: result.junctionViews = proto.junction_views(); break;
        case JunctionViewResult::kError: result.error = proto.error(); break;
        default: break;
    }
    return result;
}

JunctionViewResult ToProto(const JunctionViewResult& native) {
    JunctionViewResult result;
    if (native.Result_case == JunctionViewResult::kJunctionViews) {
        result.set_junction_views(native.junctionViews);
    }
    if (native.Result_case == JunctionViewResult::kError) {
        result.set_error(native.error);
    }
    return result;
}

}  // namespace protobuf_helpers
