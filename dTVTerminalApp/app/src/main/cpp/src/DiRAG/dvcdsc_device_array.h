/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#ifndef DLNA_DVCDSC_DEVICE_ARRAY_H
#define DLNA_DVCDSC_DEVICE_ARRAY_H

#include <du_array.h>
#include "dvcdsc_device.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef du_array dvcdsc_device_array;

#define dvcdsc_device_array_init(x) du_array_init((x), sizeof(dvcdsc_device))

#define dvcdsc_device_array_allocate(x, len) du_array_allocate((x), (len))

#define dvcdsc_device_array_allocate_align(x, len) du_array_allocate_align((x), (len))

#define dvcdsc_device_array_get(x) ((dvcdsc_device*)du_array_get(x))

#define dvcdsc_device_array_get_pos(x, pos) du_array_get_pos((x), (pos))

extern du_uint32 dvcdsc_device_array_find_by_udn_and_device_type(const dvcdsc_device_array* x, const du_uchar* udn, const du_uchar* device_type);

#define dvcdsc_device_array_length(x) du_array_length(x)

#define dvcdsc_device_array_bytes(x) du_array_bytes(x)

#define dvcdsc_device_array_truncate_length(x, len) du_array_truncate_length((x), (len))

extern du_bool dvcdsc_device_array_truncate_length_object(dvcdsc_device_array* x, du_uint32 len);

#define dvcdsc_device_array_truncate(x) du_array_truncate(x)

extern du_bool dvcdsc_device_array_truncate_object(dvcdsc_device_array* x);

#define dvcdsc_device_array_free(x) du_array_free(x)

extern void dvcdsc_device_array_free_object(dvcdsc_device_array* x);

#define dvcdsc_device_array_failed(x) du_array_failed(x)

#define dvcdsc_device_array_equal(x, y) du_array_equal((x), (y))

#define dvcdsc_device_array_cat(to, from) du_array_cat((to), (from))

#define dvcdsc_device_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

#define dvcdsc_device_array_cato(to, object_ptr) du_array_cato((to), (du_uint8*)(object_ptr))

#define dvcdsc_device_array_remove(x, pos) du_array_remove((x), (pos))

extern du_bool dvcdsc_device_array_remove_object(dvcdsc_device_array* x, du_uint32 pos);

#ifdef __cplusplus
}
#endif

#endif
