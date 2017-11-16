/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_ASYNCSVCMGR_INFO_ARRAY_H
#define DUPNP_ASYNCSVCMGR_INFO_ARRAY_H

#include <dupnp_asyncsvcmgr_info.h>
#include <du_type.h>
#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef du_array dupnp_asyncsvcmgr_info_array;

#define dupnp_asyncsvcmgr_info_array_init(x) du_array_init((x), sizeof(dupnp_asyncsvcmgr_info))

#define dupnp_asyncsvcmgr_info_array_allocate(x, len) du_array_allocate((x), (len))

#define dupnp_asyncsvcmgr_info_array_allocate_align(x, len) du_array_allocate_align((x), (len))

#define dupnp_asyncsvcmgr_info_array_get(x) ((dupnp_asyncsvcmgr_info*)du_array_get(x))

#define dupnp_asyncsvcmgr_info_array_get_pos(x, pos) du_array_get_pos((x), (pos))

extern du_uint32 dupnp_asyncsvcmgr_info_array_find_id(const dupnp_asyncsvcmgr_info_array* x, du_uint32 id);

#define dupnp_asyncsvcmgr_info_array_length(x) du_array_length(x)

#define dupnp_asyncsvcmgr_info_array_bytes(x) du_array_bytes(x)

#define dupnp_asyncsvcmgr_info_array_truncate_length(x, len) du_array_truncate_length((x), (len))

extern du_bool dupnp_asyncsvcmgr_info_array_truncate_length_object(dupnp_asyncsvcmgr_info_array* x, du_uint32 len);

#define dupnp_asyncsvcmgr_info_array_truncate(x) du_array_truncate(x)

extern du_bool dupnp_asyncsvcmgr_info_array_truncate_object(dupnp_asyncsvcmgr_info_array* x);

#define dupnp_asyncsvcmgr_info_array_free(x) du_array_free(x)

extern void dupnp_asyncsvcmgr_info_array_free_object(dupnp_asyncsvcmgr_info_array* x);

#define dupnp_asyncsvcmgr_info_array_failed(x) du_array_failed(x)

#define dupnp_asyncsvcmgr_info_array_equal(x, y) du_array_equal((x), (y))

#define dupnp_asyncsvcmgr_info_array_cat(to, from) du_array_cat((to), (from))

#define dupnp_asyncsvcmgr_info_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

#define dupnp_asyncsvcmgr_info_array_cato(to, object_ptr) du_array_cato((to), (du_uint8*)(object_ptr))

#define dupnp_asyncsvcmgr_info_array_remove(x, pos) du_array_remove((x), (pos))

extern du_bool dupnp_asyncsvcmgr_info_array_remove_object(dupnp_asyncsvcmgr_info_array* x, du_uint32 pos);

#ifdef __cplusplus
}
#endif

#endif

