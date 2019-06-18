/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DMEDIA_DETECTOR_TARGET_ARRAY_OS_H
#define DMEDIA_DETECTOR_TARGET_ARRAY_OS_H

#include <du_type.h>
#include <du_array.h>

#include <dmedia_detector_target_os.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef du_array dmedia_detector_target_array;

#define dmedia_detector_target_array_init(x) du_array_init((x), sizeof(dmedia_detector_target))

#define dmedia_detector_target_array_allocate(x, len) du_array_allocate((x), (len))

#define dmedia_detector_target_array_allocate_align(x, len) du_array_allocate_align((x), (len))

#define dmedia_detector_target_array_get(x) ((dmedia_detector_target*)du_array_get(x))

#define dmedia_detector_target_array_get_pos(x, pos) du_array_get_pos((x), (pos))

extern du_uint32 dmedia_detector_target_array_find_path(const dmedia_detector_target_array* x, const du_uchar* path);

extern du_uint32 dmedia_detector_target_array_find_watch_desc(const dmedia_detector_target_array* x, du_int watch_desc);

#define dmedia_detector_target_array_length(x) du_array_length(x)

#define dmedia_detector_target_array_bytes(x) du_array_bytes(x)

#define dmedia_detector_target_array_truncate_length(x, len) du_array_truncate_length((x), (len))

extern du_bool dmedia_detector_target_array_truncate_length_object(dmedia_detector_target_array* x, du_uint32 len);

#define dmedia_detector_target_array_truncate(x) du_array_truncate(x)

extern du_bool dmedia_detector_target_array_truncate_object(dmedia_detector_target_array* x);

#define dmedia_detector_target_array_free(x) du_array_free(x)

extern void dmedia_detector_target_array_free_object(dmedia_detector_target_array* x);

#define dmedia_detector_target_array_failed(x) du_array_failed(x)

#define dmedia_detector_target_array_equal(x, y) du_array_equal((x), (y))

#define dmedia_detector_target_array_cat(to, from) du_array_cat((to), (from))

#define dmedia_detector_target_array_catn(to, from, len) du_array_catn((to), (du_uint8*)(from), (len))

#define dmedia_detector_target_array_cato(to, dmedia_detector_target_ptr) du_array_cato((to), (du_uint8*)(dmedia_detector_target_ptr))

#define dmedia_detector_target_array_remove(x, pos) du_array_remove((x), (pos))

extern du_bool dmedia_detector_target_array_remove_object(dmedia_detector_target_array* x, du_uint32 pos);

#ifdef __cplusplus
}
#endif

#endif

