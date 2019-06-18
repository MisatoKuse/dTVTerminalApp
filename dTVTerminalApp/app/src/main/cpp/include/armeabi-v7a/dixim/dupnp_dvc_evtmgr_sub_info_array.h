/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVC_EVTMGR_SUB_INFO_ARRAY_H
#define DUPNP_DVC_EVTMGR_SUB_INFO_ARRAY_H

#include <dupnp_dvc_evtmgr_sub_info.h>
#include <du_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef du_array dupnp_dvc_evtmgr_sub_info_array;

#define dupnp_dvc_evtmgr_sub_info_array_init(x) du_array_init((x), sizeof(dupnp_dvc_evtmgr_sub_info))

#define dupnp_dvc_evtmgr_sub_info_array_allocate(x, len) du_array_allocate((x), (len))

#define dupnp_dvc_evtmgr_sub_info_array_allocate_align(x, len) du_array_allocate_align((x), (len))

#define dupnp_dvc_evtmgr_sub_info_array_get(x) ((dupnp_dvc_evtmgr_sub_info*)du_array_get(x))

#define dupnp_dvc_evtmgr_sub_info_array_get_pos(x, pos) du_array_get_pos((x), (pos))

extern du_uint32 dupnp_dvc_evtmgr_sub_info_array_find_sid(const dupnp_dvc_evtmgr_sub_info_array* x, const du_uchar* sid);

extern du_uint32 dupnp_dvc_evtmgr_sub_info_array_find_callback(const dupnp_dvc_evtmgr_sub_info_array* x, const du_uchar* callback);

#define dupnp_dvc_evtmgr_sub_info_array_length(x) du_array_length(x)

#define dupnp_dvc_evtmgr_sub_info_array_bytes(x) du_array_bytes(x)

#define dupnp_dvc_evtmgr_sub_info_array_truncate_length(x, len) du_array_truncate_length((x), (len))

extern du_bool dupnp_dvc_evtmgr_sub_info_array_truncate_length_object(dupnp_dvc_evtmgr_sub_info_array* x, du_uint32 len);

#define dupnp_dvc_evtmgr_sub_info_array_truncate(x) du_array_truncate(x)

extern du_bool dupnp_dvc_evtmgr_sub_info_array_truncate_object(dupnp_dvc_evtmgr_sub_info_array* x);

#define dupnp_dvc_evtmgr_sub_info_array_free(x) du_array_free(x)

extern void dupnp_dvc_evtmgr_sub_info_array_free_object(dupnp_dvc_evtmgr_sub_info_array* x);

#define dupnp_dvc_evtmgr_sub_info_array_failed(x) du_array_failed(x)

#define dupnp_dvc_evtmgr_sub_info_array_equal(x, y) du_array_equal((x), (y))

#define dupnp_dvc_evtmgr_sub_info_array_cato(to, info_ptr) du_array_cato((to), (du_uint8*)(info_ptr))

#define dupnp_dvc_evtmgr_sub_info_array_remove(x, pos) du_array_remove((x), (pos))

extern du_bool dupnp_dvc_evtmgr_sub_info_array_remove_object(dupnp_dvc_evtmgr_sub_info_array* x, du_uint32 pos);

#ifdef __cplusplus
}
#endif

#endif
