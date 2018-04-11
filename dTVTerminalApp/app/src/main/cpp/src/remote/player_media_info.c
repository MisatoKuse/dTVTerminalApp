/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include "player_media_info.h"
#include <dav_http.h>
#include <dav_protocol_info.h>
#include <dav_flags_param.h>
#include <dav_content_features.h>
#include <dav_op_param.h>
#include <du_mime_type.h>
#include <du_str.h>
#include <du_param.h>
#include <du_byte.h>
#include <du_alloc.h>

void player_media_info_init(player_media_info* mi) {
    du_byte_zero((du_uint8*)mi, sizeof(player_media_info));
}

void player_media_info_free(player_media_info* mi) {
    du_alloc_free(mi->title);
    du_alloc_free(mi->uri);
    du_alloc_free(mi->mime_type);
    du_alloc_free(mi->protocol_info);
    du_alloc_free(mi->original_uri);
    du_alloc_free(mi->original_mime_type);
}

void player_media_info_reset(player_media_info* mi) {
    player_media_info_free(mi);
    player_media_info_init(mi);
}

static void set_transfer_mode(dav_http_transfer_mode* transfer_mode, dav_content_features* cf, const du_uchar* upnp_class) {
    const du_str_array* param_array;
    const du_uchar* flags_param;

    *transfer_mode = DAV_HTTP_TRANSFER_MODE_BACKGROUND;

    param_array = dav_content_features_get_param_array(cf);
    if (!param_array) return;
    flags_param = du_param_get_value_by_name(param_array, dav_content_features_name_flags_param());

    if (!flags_param || !dav_flags_param_get_primary_flag(flags_param, DAV_FLAGS_PARAM_PF_DLNA_V15)) {
        if (dav_didl_derived_from(upnp_class, dav_didl_class_audio_item())) {
            *transfer_mode = DAV_HTTP_TRANSFER_MODE_STREAMING;
        } else if (dav_didl_derived_from(upnp_class, dav_didl_class_video_item())) {
            *transfer_mode = DAV_HTTP_TRANSFER_MODE_STREAMING;
        } else if (dav_didl_derived_from(upnp_class, dav_didl_class_image_item())) {
            *transfer_mode = DAV_HTTP_TRANSFER_MODE_INTERACTIVE;
        }
    } else {
        if (dav_flags_param_get_primary_flag(flags_param, DAV_FLAGS_PARAM_PF_TM_I)) *transfer_mode = DAV_HTTP_TRANSFER_MODE_INTERACTIVE;
        else if (dav_flags_param_get_primary_flag(flags_param, DAV_FLAGS_PARAM_PF_TM_S)) *transfer_mode = DAV_HTTP_TRANSFER_MODE_STREAMING;
        else if (dav_flags_param_get_primary_flag(flags_param, DAV_FLAGS_PARAM_PF_TM_B)) *transfer_mode = DAV_HTTP_TRANSFER_MODE_BACKGROUND;
    }
}

static void set_seek_supported(du_bool* byte_seek_supported, du_bool* time_seek_supported, dav_content_features* cf) {
    const du_str_array* param_array;
    const du_uchar* op_param;

    *byte_seek_supported = 0;
    *time_seek_supported = 0;
    param_array = dav_content_features_get_param_array(cf);
    if (!param_array) return;
    op_param = du_param_get_value_by_name(param_array, dav_content_features_name_op_param());
    if (!op_param) return;

    *byte_seek_supported = dav_op_param_is_supported(op_param, DAV_OP_PARAM_TYPE_BYTE_BASED_SEEK);
    *time_seek_supported = dav_op_param_is_supported(op_param, DAV_OP_PARAM_TYPE_TIME_BASED_SEEK);
}

static void set_connection_stalling_supported(du_bool* connection_stalling_supported, dav_content_features* cf) {
    const du_str_array* param_array;
    const du_uchar* flags_param;

    *connection_stalling_supported = 0;
    param_array = dav_content_features_get_param_array(cf);
    if (!param_array) return;
    flags_param = du_param_get_value_by_name(param_array, dav_content_features_name_flags_param());
    if (!flags_param) return;

    *connection_stalling_supported = dav_flags_param_get_primary_flag(flags_param, DAV_FLAGS_PARAM_PF_HTTP_STALLING);
}

static du_bool set_duration(du_uint32* duration, dav_didl_object_property* res) {
    const du_uchar* duration_str;

    duration_str = dav_didl_object_attribute_list_get_attribute_value(res->attr_list, dav_didl_attribute_duration());

    if (duration_str) {
        if (!dav_didl_duration_scan(duration_str, duration)) return 0;
    } else {
        *duration = 0;
    }

    return 1;
}

static du_bool set_protocol_info(du_uchar** protocol_info, dav_didl_object_property* res) {
    const du_uchar* pi;

    pi = dav_didl_object_attribute_list_get_attribute_value(res->attr_list, dav_didl_attribute_protocol_info());
    if (!protocol_info) return 1;

    if (!du_str_clone(pi, protocol_info)) return 0;
    return 1;
}

static du_bool set_playback_res_info(player_media_info* mi, dav_didl_object_property_list* prop_list, const du_uchar* uri) {
    dav_content_features cf;
    dav_didl_object_property* prop;
    dav_protocol_info pi;
    const du_uchar* s;
    du_uint32 i = 0;

    if (!dav_protocol_info_init(&pi)) return 0;
    dav_content_features_init(&cf);

    while ((prop = dav_didl_object_property_list_get_property(prop_list, dav_didl_element_res(), i++))) {
        if (!uri) break;
        if (du_str_equal(prop->value, uri)) break;
    }
    if (!prop) goto error;
    if (!du_str_clone(prop->value, &mi->uri)) goto error;

    if (!set_duration(&mi->duration_ms, prop)) goto error;
    if (!set_protocol_info(&mi->protocol_info, prop)) goto error;
    if (mi->protocol_info) {
        if (!dav_protocol_info_parse(&pi, mi->protocol_info)) goto error;

        s = dav_protocol_info_get_content_format(&pi);
        if (!du_str_clone(s, &mi->mime_type)) goto error;

        s = dav_protocol_info_get_additional_info(&pi);
        if (!s) goto error;
        if (!dav_content_features_parse(&cf, s)) goto error;

        set_seek_supported(&mi->byte_seek_supported, &mi->time_seek_supported, &cf);
        set_connection_stalling_supported(&mi->connection_stalling_supported, &cf);

        prop = dav_didl_object_property_list_get_property(prop_list, dav_didl_element_upnp_class(), 0);
        set_transfer_mode(&mi->transfer_mode, &cf, prop->value);
    }

    dav_content_features_free(&cf);
    dav_protocol_info_free(&pi);
    return 1;

error:
    dav_content_features_free(&cf);
    dav_protocol_info_free(&pi);
    return 0;
}

static void set_additional_mm_flags_param(player_media_info* mi, dav_content_features* cf) {    const du_str_array* param_array;
    const du_uchar* flags_param;

    mi->mm_flags = PMI_ADDITIONAL_MM_FLAGS_PARAM_NONE;

    param_array = dav_content_features_get_param_array(cf);
    if (!param_array) return;
    flags_param = du_param_get_value_by_name(param_array, dav_content_features_name_flags_param());
    if (!flags_param) return;

    if (dav_flags_param_get_primary_flag(flags_param, DAV_FLAGS_PARAM_PF_DIS_DTCP_COPY)) {
        mi->mm_flags = PMI_ADDITIONAL_MM_FLAGS_PARAM_DIS_DTCP_COPY;
    }
    if (dav_flags_param_get_primary_flag(flags_param, DAV_FLAGS_PARAM_PF_DIS_DTCP_MOVE)) {
        mi->mm_flags = PMI_ADDITIONAL_MM_FLAGS_PARAM_DIS_DTCP_MOVE;
    }
}

static du_bool set_original_res_info(player_media_info* mi, dav_didl_object_property_list* prop_list) {
    du_uint32 i;
    dav_didl_object_property* prop;
    const du_uchar* s;
    dav_protocol_info pi;
    dav_content_features cf;
    const du_str_array* param_array;
    du_bool found = 0;

    if (!dav_protocol_info_init(&pi)) return 0;
    dav_content_features_init(&cf);

    for (i = 0; i < prop_list->count; ++i) {
        prop = prop_list->list + i;
        if (prop->name != dav_didl_element_res()) continue;

        s = dav_didl_object_attribute_list_get_attribute_value(prop->attr_list, dav_didl_attribute_protocol_info());
        if (!s) continue;

        if (!dav_protocol_info_parse(&pi, s)) continue;

        s = dav_protocol_info_get_additional_info(&pi);
        if (!dav_content_features_parse(&cf, s)) continue;
        param_array = dav_content_features_get_param_array(&cf);

        s = du_param_get_value_by_name(param_array, dav_content_features_name_ci_param());
        if (s && du_str_equal(s, DU_UCHAR_CONST("1"))) continue;

        if (!du_str_clone(prop->value, &mi->original_uri)) goto error;
        s = dav_protocol_info_get_content_format(&pi);
        if (!du_str_clone(s, &mi->original_mime_type)) goto error;

        s = dav_protocol_info_get_content_format(&pi);
        if (du_mime_type_major_equal(s, DU_UCHAR_CONST("application")) && du_mime_type_sub_equal(s, DU_UCHAR_CONST("x-dtcp1"))) {
            du_uchar buf[256];

            mi->dtcp = 1;
            if (!du_mime_type_get_parameter_value(s, DU_UCHAR_CONST("dtcp1host"), mi->dtcp1host, sizeof mi->dtcp1host)) goto error;
            if (!du_mime_type_get_parameter_value(s, DU_UCHAR_CONST("dtcp1port"), buf, sizeof buf)) goto error;
            if (!du_str_scan_uint16(buf, &mi->dtcp1port)) goto error;

            if (du_mime_type_get_parameter_value(s, DU_UCHAR_CONST("dtcp1raport"), buf, sizeof buf)) {
                if (!du_str_scan_uint16(buf, &mi->dtcp1raport)) goto error;
            }

            set_additional_mm_flags_param(mi, &cf);
        }

        found = 1;
        break;
    }

    dav_protocol_info_free(&pi);
    dav_content_features_free(&cf);
    return found;

error:
    dav_protocol_info_free(&pi);
    dav_content_features_free(&cf);
    return 0;
}

du_bool player_media_info_set_property(player_media_info* mi, const du_uchar* uri, dav_didl_object* object) {
    dav_didl_object_property* prop;
    du_uint32 i = 0;

    player_media_info_reset(mi);

    if (object) {
        if (!(prop = dav_didl_object_property_list_get_property(object->prop_list, dav_didl_element_dc_title(), i++))) goto error;
        if (!du_str_clone(prop->value, &mi->title)) goto error;

        if (!set_playback_res_info(mi, object->prop_list, uri)) goto error;
        if (!set_original_res_info(mi, object->prop_list)) goto error;
    } else {
        if (!du_str_clone(uri, &mi->uri)) goto error;
    }
    return 1;

error:
    player_media_info_reset(mi);
    return 0;
}

const du_uchar* player_media_info_get_title(player_media_info* mi) {
    return mi->title;
}

du_uint32 player_media_info_get_duration(player_media_info* mi) {
    return mi->duration_ms;
}

du_bool player_media_info_get_byte_seek_supported(player_media_info* mi) {
    return mi->byte_seek_supported;
}

du_bool player_media_info_get_time_seek_supported(player_media_info* mi) {
    return mi->time_seek_supported;
}

du_bool player_media_info_is_connection_stalling_supported(player_media_info* mi) {
    return mi->connection_stalling_supported;
}

const du_uchar* player_media_info_get_uri(player_media_info* mi) {
    return mi->uri;
}

const du_uchar* player_media_info_get_mime_type(player_media_info* mi) {
    return mi->mime_type;
}

dav_http_transfer_mode player_media_info_get_transfer_mode(player_media_info* mi) {
    return mi->transfer_mode;
}

du_bool player_media_info_get_protocol_info(player_media_info* mi, du_uchar_array* protocol_info) {
    du_uchar_array_truncate(protocol_info);

    if (!mi->protocol_info) return 1;
    return du_uchar_array_cats(protocol_info, mi->protocol_info);
}

const du_uchar* player_media_info_get_original_uri(player_media_info* mi) {
    return mi->original_uri;
}

const du_uchar* player_media_info_get_original_mime_type(player_media_info* mi) {
    return mi->original_mime_type;
}

const du_bool player_media_info_is_dtcp(player_media_info* mi) {
    return mi->dtcp;
}

const du_uchar* player_media_info_get_dtcp1host(player_media_info* mi) {
    if (!mi->dtcp) return 0;
    return mi->dtcp1host;
}

du_uint16 player_media_info_get_dtcp1port(player_media_info* mi) {
    if (!mi->dtcp) return 0;
    return mi->dtcp1port;
}

du_uint16 player_media_info_get_dtcp1raport(player_media_info* mi) {
    if (!mi->dtcp) return 0;
    return mi->dtcp1raport;
}

pmi_additional_mm_flags_param player_media_info_get_additional_mm_flags_param(player_media_info* mi) {
    return mi->mm_flags;
}

