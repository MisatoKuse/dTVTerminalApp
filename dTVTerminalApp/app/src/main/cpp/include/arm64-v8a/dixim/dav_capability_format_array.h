/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DAV_CAPABILITY_FORMAT_ARRAY_H
#define DAV_CAPABILITY_FORMAT_ARRAY_H

#include <dav_capability_format.h>
#include <du_type.h>
#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef du_array dav_capability_format_array;

#define dav_capability_format_array_init(x) du_array_init((x), sizeof(dav_capability_format))

#define dav_capability_format_array_allocate(x, len) du_array_allocate((x), (len))

#define dav_capability_format_array_allocate_align(x, len) du_array_allocate_align((x), (len))

#define dav_capability_format_array_get(x) ((dav_capability_format*)du_array_get(x))

#define dav_capability_format_array_get_pos(x, pos) du_array_get_pos((x), (pos))

extern du_uint32 dav_capability_format_array_find_profile(dav_capability_format_array* x, const du_uchar* name, const du_uchar* value, du_uint32 idx);

extern du_uint32 dav_capability_format_array_find_mimetype(dav_capability_format_array* x, const du_uchar* mimetype, du_uint32 idx);

#define dav_capability_format_array_length(x) du_array_length(x)

#define dav_capability_format_array_bytes(x) du_array_bytes(x)

#define dav_capability_format_array_truncate_length(x, len) du_array_truncate_length((x), (len))

extern du_bool dav_capability_format_array_truncate_length_object(dav_capability_format_array* x, du_uint32 len);

#define dav_capability_format_array_truncate(x) du_array_truncate(x)

extern du_bool dav_capability_format_array_truncate_object(dav_capability_format_array* x);

#define dav_capability_format_array_free(x) du_array_free(x)

extern void dav_capability_format_array_free_object(dav_capability_format_array* x);

#define dav_capability_format_array_failed(x) du_array_failed(x)

#define dav_capability_format_array_equal(x, y) du_array_equal((x), (y))

#define dav_capability_format_array_cat(to, from) du_array_cat((to), (from))

#define dav_capability_format_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

#define dav_capability_format_array_cato(to, object_ptr) du_array_cato((to), (du_uint8*)(object_ptr))

#define dav_capability_format_array_remove(x, pos) du_array_remove((x), (pos))

extern du_bool dav_capability_format_array_remove_object(dav_capability_format_array* x, du_uint32 pos);

#ifdef __cplusplus
}
#endif

#endif
