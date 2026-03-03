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

Language ToNative(const Language proto) {
    Language result;
    result.isoLanguageCode = proto.iso_language_code();
    result.isoCountryCode = proto.iso_country_code();
    result.isoScriptCode = proto.iso_script_code();
    return result;
}

Language ToProto(const Language& native) {
    Language result;
    result.set_iso_language_code(native.isoLanguageCode);
    result.set_iso_country_code(native.isoCountryCode);
    result.set_iso_script_code(native.isoScriptCode);
    return result;
}

}  // namespace protobuf_helpers
