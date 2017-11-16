/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DAV_CAPABILITY_PRIORITY_H
#define DAV_CAPABILITY_PRIORITY_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef enum dav_capability_priority_algrorithm {
    DAV_CAPABILITY_PRIORITY_ALGRORITHM_UNKNOWN,
    DAV_CAPABILITY_PRIORITY_ALGRORITHM_LARGEST,
    DAV_CAPABILITY_PRIORITY_ALGRORITHM_LARGER_NEAREST,
    DAV_CAPABILITY_PRIORITY_ALGRORITHM_NEAREST,
} dav_capability_priority_algrorithm;

typedef enum dav_capability_priority_scan_method {
    DAV_CAPABILITY_PRIORITY_SCAN_METHOD_UNKNOWN,
    DAV_CAPABILITY_PRIORITY_SCAN_METHOD_PROGRESSIVE,
    DAV_CAPABILITY_PRIORITY_SCAN_METHOD_INTERLACE,
} dav_capability_priority_scan_method;

typedef enum dav_capability_priority_item {
    DAV_CAPABILITY_PRIORITY_ITEM_UNKNOWN,
    DAV_CAPABILITY_PRIORITY_ITEM_OPERABILITY,
    DAV_CAPABILITY_PRIORITY_ITEM_ORIGINALITY,
    DAV_CAPABILITY_PRIORITY_ITEM_RESOLUTION,
    DAV_CAPABILITY_PRIORITY_ITEM_BITS_PER_SAMPLE,
    DAV_CAPABILITY_PRIORITY_ITEM_SAMPLE_FREQUENCY,
    DAV_CAPABILITY_PRIORITY_ITEM_NR_AUDIO_CHANNELS,
    DAV_CAPABILITY_PRIORITY_ITEM_CONSISTENCY,
    DAV_CAPABILITY_PRIORITY_ITEM_BITRATE,
    DAV_CAPABILITY_PRIORITY_ITEM_SCAN_METHOD,
} dav_capability_priority_item;

typedef struct dav_capability_priority {
    du_uint64 _priority;
    dav_capability_priority_algrorithm algorithm;
    dav_capability_priority_scan_method scan_method;
} dav_capability_priority;

extern du_bool dav_capability_priority_init(dav_capability_priority* x);

extern du_bool dav_capability_priority_add(dav_capability_priority* x, dav_capability_priority_item item, du_uint32 priority);

extern dav_capability_priority_item dav_capability_priority_get(dav_capability_priority* x, du_uint32 priority);

#ifdef __cplusplus
}
#endif

#endif

