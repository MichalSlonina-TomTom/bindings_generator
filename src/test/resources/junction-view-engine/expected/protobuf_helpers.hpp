/*
 * © 2024 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

#pragma once

#include <tomtom/sdk/junctionview_engine/junctionview_engine.hpp>

#include "junction_view_information.pb.h"
#include "junction_view_request.pb.h"

namespace tomtom {
namespace sdk {
namespace bindings {
namespace junction_view_engine {
namespace internal {
struct NativeJunctionViewRequestParams {
  std::vector<tomtom::sdk::junctionviewnda::ArcIntKeyWithOffset> arc_keys_with_offsets{};
  tomtom::sdk::junctionviewnda::JunctionViewGenerationParameters junction_view_generation_params{};
};

struct NativeJunctionViewResult {
  tomtom::sdk::junctionviewnda::Image image;
  tomtom::sdk::junctionviewnda::JunctionViewType type;
  tomtom::sdk::junctionviewnda::JunctionViewDaylightType daylight_type;
  orodoro::quantities::TCentimeters start_offset;
  orodoro::quantities::TCentimeters end_offset;
};

NativeJunctionViewRequestParams FromProto(
    const com::tomtom::sdk::navigation::junctionview::protos::JunctionViewRequest& request);

com::tomtom::sdk::navigation::junctionview::protos::JunctionViewResult ToProto(
    const std::vector<tomtom::sdk::junctionviewnda::JunctionViewInformation>& junction_views);
com::tomtom::sdk::navigation::junctionview::protos::JunctionViewType ToProto(
    tomtom::sdk::junctionviewnda::JunctionViewType native_type);
com::tomtom::sdk::navigation::junctionview::protos::JunctionViewDaylightType ToProto(
    tomtom::sdk::junctionviewnda::JunctionViewDaylightType native_daylight_type);
com::tomtom::sdk::navigation::junctionview::protos::JunctionViewResult ToProto(
    const tomtom::sdk::junctionviewnda::JunctionViewError& junction_view_error);

}  // namespace internal
}  // namespace junction_view_engine
}  // namespace bindings
}  // namespace sdk
}  // namespace tomtom
