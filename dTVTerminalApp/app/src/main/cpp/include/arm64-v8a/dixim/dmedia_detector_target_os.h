/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DMEDIA_DETECTOR_TARGET_OS_H
#define DMEDIA_DETECTOR_TARGET_OS_H

#include <du_type.h>
#include <du_timel_array.h>
#include <du_int_array.h>
#include <du_poll.h>
#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

struct dmedia_detector;

typedef struct dmedia_detector_target {
    struct dmedia_detector* _parent;
    du_uchar_array _path;
    du_timel _first_recieved_time;
    du_int_array _watch_desc_array;
    du_str_array _path_array;

    du_str_array _evented_sub_root_path_array;

    du_uint32 _max_evented_path_array_len;
    du_str_array _evented_path_array;
    du_uint32 _cur_evented_path_array_len;

    uint32_t _detected_mask;
} dmedia_detector_target;

extern du_bool dmedia_detector_target_init(dmedia_detector_target* x, struct dmedia_detector* md);

extern void dmedia_detector_target_free(dmedia_detector_target* x);

extern du_bool dmedia_detector_target_set_path(dmedia_detector_target* x, const du_uchar* path);

extern du_bool dmedia_detector_target_add_watch_directory(dmedia_detector_target* x, du_uchar_array* dir_path);

extern du_bool dmedia_detector_target_remove_watch_directory(dmedia_detector_target* x, du_uchar_array* dir_path);

extern du_uint32 dmedia_detector_target_count_watch_desc(dmedia_detector_target* x, du_int watch_desc);

extern const du_uchar* dmedia_detector_target_get_path(dmedia_detector_target* x, du_int watch_desc);

extern du_bool dmedia_detector_target_clear_evented_info(dmedia_detector_target* x);

#ifdef __cplusplus
}
#endif

#endif
