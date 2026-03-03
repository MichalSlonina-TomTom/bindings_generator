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
struct JunctionViewInformationList;
struct JunctionViewInformation;
struct JunctionViewError;
struct JunctionViewResult;

namespace protobuf_helpers {

// Conversion functions for JunctionViewType
JunctionViewType ToNative(const JunctionViewType proto);
JunctionViewType ToProto(const JunctionViewType native);

// Conversion functions for JunctionViewDaylightType
JunctionViewDaylightType ToNative(const JunctionViewDaylightType proto);
JunctionViewDaylightType ToProto(const JunctionViewDaylightType native);

// Conversion functions for JunctionViewInformationList
JunctionViewInformationList ToNative(const JunctionViewInformationList proto);
JunctionViewInformationList ToProto(const JunctionViewInformationList& native);

// Conversion functions for JunctionViewInformation
JunctionViewInformation ToNative(const JunctionViewInformation proto);
JunctionViewInformation ToProto(const JunctionViewInformation& native);

// Conversion functions for JunctionViewError
JunctionViewError ToNative(const JunctionViewError proto);
JunctionViewError ToProto(const JunctionViewError& native);

// Conversion functions for ErrorType
ErrorType ToNative(const JunctionViewError_ErrorType proto);
JunctionViewError_ErrorType ToProto(const ErrorType native);

// Conversion functions for JunctionViewResult
JunctionViewResult ToNative(const JunctionViewResult proto);
JunctionViewResult ToProto(const JunctionViewResult& native);

}  // namespace protobuf_helpers

#endif // PROTOBUF_HELPERS_HPP
