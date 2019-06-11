/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_GENA_CP_SUB_INFO_ARRAY_H
#define DUPNP_GENA_CP_SUB_INFO_ARRAY_H

#include <dupnp_gena_cp_sub_info.h>
#include <du_array.h>

typedef du_array dupnp_gena_cp_sub_info_array;

#ifdef __cplusplus
extern "C" {
#endif

#define dupnp_gena_cp_sub_info_array_init(x) du_array_init((x), sizeof(dupnp_gena_cp_sub_info))

#define dupnp_gena_cp_sub_info_array_allocate(x, len) du_array_allocate((x), (len))

#define dupnp_gena_cp_sub_info_array_allocate_align(x, len) du_array_allocate_align((x), (len))

#define dupnp_gena_cp_sub_info_array_get(x) ((dupnp_gena_cp_sub_info*)du_array_get(x))

#define dupnp_gena_cp_sub_info_array_get_pos(x, pos) du_array_get_pos((x), (pos))

extern du_uint32 dupnp_gena_cp_sub_info_array_find(const dupnp_gena_cp_sub_info_array* x, const du_uchar* event_sub_url, const du_uchar* callback_url_path);

extern du_uint32 dupnp_gena_cp_sub_info_array_find_url(const dupnp_gena_cp_sub_info_array* x, const du_uchar* url);

extern du_uint32 dupnp_gena_cp_sub_info_array_find_sid(const dupnp_gena_cp_sub_info_array* x, const du_uchar* sid);

extern du_uint32 dupnp_gena_cp_sub_info_array_find_id(const dupnp_gena_cp_sub_info_array* x, const du_uint32 id);

#define dupnp_gena_cp_sub_info_array_length(x) du_array_length(x)

#define dupnp_gena_cp_sub_info_array_bytes(x) du_array_bytes(x)

#define dupnp_gena_cp_sub_info_array_truncate_length(x, len) du_array_truncate_length((x), (len))

extern du_bool dupnp_gena_cp_sub_info_array_truncate_length_object(dupnp_gena_cp_sub_info_array* x, du_uint32 len);

#define dupnp_gena_cp_sub_info_array_truncate(x) du_array_truncate(x)

extern du_bool dupnp_gena_cp_sub_info_array_truncate_object(dupnp_gena_cp_sub_info_array* x);

#define dupnp_gena_cp_sub_info_array_free(x) du_array_free(x)

extern void dupnp_gena_cp_sub_info_array_free_object(dupnp_gena_cp_sub_info_array* x);

#define dupnp_gena_cp_sub_info_array_failed(x) du_array_failed(x)

#define dupnp_gena_cp_sub_info_array_equal(x, y) du_array_equal((x), (y))

#define dupnp_gena_cp_sub_info_array_cato(to, info_ptr) du_array_cato((to), (du_uint8*)(info_ptr))

#define dupnp_gena_cp_sub_info_array_remove(x, pos) du_array_remove((x), (pos))

extern du_bool dupnp_gena_cp_sub_info_array_remove_object(dupnp_gena_cp_sub_info_array* x, du_uint32 pos);

#ifdef __cplusplus
}
#endif

#endif
