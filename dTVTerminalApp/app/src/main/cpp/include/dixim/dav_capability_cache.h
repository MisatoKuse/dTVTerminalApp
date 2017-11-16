/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DAV_CAPABILITY_CACHE_H
#define DAV_CAPABILITY_CACHE_H

#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dav_capability_cache {
    du_str_array _flag_array;
} dav_capability_cache;

extern void dav_capability_cache_init(dav_capability_cache* x);

extern void dav_capability_cache_free(dav_capability_cache* x);

extern void dav_capability_cache_reset(dav_capability_cache* x);

extern du_bool dav_capability_cache_flag_add(dav_capability_cache* x, const du_uchar* flag);

extern du_bool dav_capability_cache_flag_get_idx(dav_capability_cache* x, const du_uchar* flag, du_uint32* idx);

extern void dav_capability_cache_flag_get_ptr(dav_capability_cache* x, du_uint32 idx, const du_uchar** ptr);

#ifdef __cplusplus
}
#endif

#endif
