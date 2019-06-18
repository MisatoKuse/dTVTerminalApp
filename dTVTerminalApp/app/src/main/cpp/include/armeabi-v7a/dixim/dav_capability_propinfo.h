/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DAV_CAPABILITY_PROPINFO_H
#define DAV_CAPABILITY_PROPINFO_H

#include <dav_capability.h>
#include <dav_didl_object.h>
#include <dav_protocol_info.h>
#include <dav_content_features.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dav_capability_propinfo {
    dav_didl_object_property prop;
    du_uint32 current_score;
    du_uint64 total_score;
    du_uint64 total_sub_score;
    du_uint32 priority;
    du_bool is_supported;
    dav_protocol_info pi;
    dav_content_features cf;
    dav_capability_format* fmt;
} dav_capability_propinfo;

extern void dav_capability_propinfo_init(dav_capability_propinfo* x);

extern void dav_capability_propinfo_free(dav_capability_propinfo* x);

extern du_bool dav_capability_propinfo_set(dav_capability_propinfo* x, dav_didl_object_property* prop, dav_capability* cap, dav_capability_class cls);

extern du_int64 dav_capability_propinfo_compare(dav_capability_propinfo* x, dav_capability_propinfo* y);

#ifdef __cplusplus
}
#endif

#endif
