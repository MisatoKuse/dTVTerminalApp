/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DAV_CAPABILITY_FORMAT_H
#define DAV_CAPABILITY_FORMAT_H

#include <du_str_array.h>
#include <du_int_array.h>
#include <du_limits.h>
#include <dav_capability_cache.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DAV_CAPABILITY_RANGE_LIMIT_UNDEFINED DU_UINT32_MAX

typedef enum {
    DAV_CAPABILITY_ID_INVALID,
    DAV_CAPABILITY_ID_BITS_PER_SAMPLE,
    DAV_CAPABILITY_ID_BITS_PER_SECOND,
    DAV_CAPABILITY_ID_COLOR_DEPTH,
    DAV_CAPABILITY_ID_DURATION,
    DAV_CAPABILITY_ID_HEIGHT,
    DAV_CAPABILITY_ID_NR_AUDIO_CHANNELS,
    DAV_CAPABILITY_ID_RESOLUTION,
    DAV_CAPABILITY_ID_SAMPLE_FREQUENCY,
    DAV_CAPABILITY_ID_SIZE,
    DAV_CAPABILITY_ID_WIDTH,
    DAV_CAPABILITY_ID_SCAN_METHOD,
} dav_capability_id;

typedef struct dav_capability_value_limit {
    dav_capability_id id;
    du_uint32 value_array_len;
    du_uint32* value_array;
} dav_capability_value_limit;

typedef struct dav_capability_range_limit {
    dav_capability_id id;
    du_uint32 min;
    du_uint32 max;
} dav_capability_range_limit;

typedef enum dav_capability_format_class {
    DAV_CAPABILITY_FORMAT_CLASS_UNKNOWN,
    DAV_CAPABILITY_FORMAT_CLASS_VIDEO,
    DAV_CAPABILITY_FORMAT_CLASS_IMAGE,
    DAV_CAPABILITY_FORMAT_CLASS_THUMBMAIL,
    DAV_CAPABILITY_FORMAT_CLASS_AUDIO,
    DAV_CAPABILITY_FORMAT_CLASS_TEXT,
} dav_capability_format_class;

typedef struct dav_capability_format_param {
    du_uint32 pn_type_array_len;
    du_uchar*** pn_type_array;
    du_uint32* pn_array_len;

    const du_uchar* op;

    du_int32* ps_array;
    du_uint32 ps_array_len;

    const du_uchar* flags;
} dav_capability_format_param;

typedef struct dav_capability_format {
    dav_capability_format_class cls;

    du_uint32 priority;
    du_bool non_pn_contents_support;
    du_bool pn_contents_support;

    du_uchar** protocol_array;
    du_uint32 protocol_array_len;

    dav_capability_format_param supported_param;
    dav_capability_format_param required_param;

    du_uchar** mime_type_array;
    du_uint32 mime_type_array_len;

    dav_capability_value_limit** value_limit_array;
    du_uint32 value_limit_array_len;

    dav_capability_range_limit** range_limit_array;
    du_uint32 range_limit_array_len;

    du_uchar* _uc;
    du_int32* _i;
} dav_capability_format;

extern du_bool dav_capability_format_init(dav_capability_format* format);

extern void dav_capability_format_free(dav_capability_format* format);

extern dav_capability_id dav_capability_format_get_id_by_name(const du_uchar* name);

extern du_bool dav_capability_format_set_by_protocol_info(dav_capability_format* format, dav_capability_cache* cache, const du_uchar* protocol_info);

extern du_bool dav_capability_format_fix(dav_capability_format* format, dav_capability_cache* cache);

#ifdef __cplusplus
}
#endif

#endif
