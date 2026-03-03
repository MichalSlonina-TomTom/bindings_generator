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

#include "protobuf_helpers.hpp"

using TCentimeters = orodoro::quantities::TCentimeters;
using TMeters = orodoro::quantities::TMeters;

namespace tomtom {
namespace sdk {
namespace bindings {
namespace junction_view_engine {
namespace internal {

NativeJunctionViewRequestParams FromProto(
    const com::tomtom::sdk::navigation::junctionview::protos::JunctionViewRequest& request) {
  NativeJunctionViewRequestParams native_junction_view_request_params;
  native_junction_view_request_params.junction_view_generation_params.route_id = request.route_id();
  if (request.has_route_window()) {
    native_junction_view_request_params.junction_view_generation_params.increment_start_offset =
        boost::none;
    native_junction_view_request_params.junction_view_generation_params.increment_end_offset =
        boost::none;

    native_junction_view_request_params.arc_keys_with_offsets.reserve(
        request.route_window().route_arcs_size());
    const auto proto_arc_int_keys = request.route_window().route_arcs();
    for (const auto& proto_arc_int_key : proto_arc_int_keys) {
      native_junction_view_request_params.arc_keys_with_offsets.push_back(
          {proto_arc_int_key.arc_key(),
           orodoro::quantities::TCentimeters(
               proto_arc_int_key.tail_offset_on_route_in_centimeters()),
           proto_arc_int_key.has_arrival_offset_on_arc_in_centimeters()
               ? boost::optional<orodoro::quantities::TCentimeters>(
                     orodoro::quantities::TCentimeters(
                         proto_arc_int_key.arrival_offset_on_arc_in_centimeters()))
               : boost::none,
           static_cast<std::uint64_t>(proto_arc_int_key.feature_version())});
    }
  }

  // JunctionView engine API filters junction views based on route ranges.
  // For now the NativeJunctionViewClient provides us only with the maneuver offset.
  // We convert this maneuver offset to a range defined around this maneuver offset <x,x>.
  // This NativeJunctionViewClient API should be extended in the future to provide the ability to
  // additionally pass instruction length.
  const auto instruction_offset = request.instruction_offset_in_meters();
  native_junction_view_request_params.junction_view_generation_params.route_ranges.emplace_back(
      std::pair<TMeters, TMeters>(instruction_offset, instruction_offset));

  return native_junction_view_request_params;
}

com::tomtom::sdk::navigation::junctionview::protos::JunctionViewResult ToProto(
    const std::vector<tomtom::sdk::junctionviewnda::JunctionViewInformation>& junction_views) {
  com::tomtom::sdk::navigation::junctionview::protos::JunctionViewResult proto_result;
  auto* proto_junction_view_list = proto_result.mutable_junction_views();
  for (const auto& junction_view : junction_views) {
    auto protoList = proto_junction_view_list->mutable_junction_view_information_list();
    auto* tmp = protoList->Add();
    tmp->mutable_data_png()->assign(junction_view.image.data_png.begin(),
                                    junction_view.image.data_png.end());
    tmp->set_type(ToProto(junction_view.type));
    tmp->set_daylight_type(ToProto(junction_view.daylight_type));
    tmp->set_start_route_offset_in_centimeters(junction_view.start_route_offset.Count());
    tmp->set_end_route_offset_in_centimeters(junction_view.end_route_offset.Count());
  }
  return proto_result;
}

com::tomtom::sdk::navigation::junctionview::protos::JunctionViewType ToProto(
    tomtom::sdk::junctionviewnda::JunctionViewType native_type) {
  switch (native_type) {
    case junctionviewnda::JunctionViewType::JUNCTION:
      return com::tomtom::sdk::navigation::junctionview::protos::JunctionViewType::kJunction;
    case junctionviewnda::JunctionViewType::SIGNBOARD:
      return com::tomtom::sdk::navigation::junctionview::protos::JunctionViewType::kSignboard;
    case junctionviewnda::JunctionViewType::ETC:
      return com::tomtom::sdk::navigation::junctionview::protos::JunctionViewType::kEtc;
  }
}

com::tomtom::sdk::navigation::junctionview::protos::JunctionViewDaylightType ToProto(
    tomtom::sdk::junctionviewnda::JunctionViewDaylightType native_daylight_type) {
  switch (native_daylight_type) {
    case junctionviewnda::JunctionViewDaylightType::DAY:
      return com::tomtom::sdk::navigation::junctionview::protos::JunctionViewDaylightType::kDay;
    case junctionviewnda::JunctionViewDaylightType::NIGHT:
      return com::tomtom::sdk::navigation::junctionview::protos::JunctionViewDaylightType::kNight;
    case junctionviewnda::JunctionViewDaylightType::ALWAYS:
      return com::tomtom::sdk::navigation::junctionview::protos::JunctionViewDaylightType::kAlways;
  }
}

com::tomtom::sdk::navigation::junctionview::protos::JunctionViewResult ToProto(
    const tomtom::sdk::junctionviewnda::JunctionViewError& junction_view_error) {
  com::tomtom::sdk::navigation::junctionview::protos::JunctionViewResult proto_result;
  proto_result.mutable_error()->set_message(junction_view_error.message);
  switch (junction_view_error.type) {
    case tomtom::sdk::junctionviewnda::JunctionViewError::Type::ArcVersionMismatch:
      proto_result.mutable_error()->set_errortype(
          com::tomtom::sdk::navigation::junctionview::protos::JunctionViewError_ErrorType::
              JunctionViewError_ErrorType_kArcVersionMismatch);
      break;
    case junctionviewnda::JunctionViewError::Type::MoreArcsRequired:
      proto_result.mutable_error()->set_errortype(
          com::tomtom::sdk::navigation::junctionview::protos::JunctionViewError_ErrorType::
              JunctionViewError_ErrorType_kMoreArcsRequired);
      break;
  }
  return proto_result;
}

}  // namespace internal
}  // namespace junction_view_engine
}  // namespace bindings
}  // namespace sdk
}  // namespace tomtom
