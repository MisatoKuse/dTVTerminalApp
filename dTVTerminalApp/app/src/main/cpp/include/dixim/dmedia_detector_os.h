/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DMEDIA_DETECTOR_OS_H
#define DMEDIA_DETECTOR_OS_H

#include <dmedia_detector.h>
#include <dmedia_detector_target_array_os.h>
#include <du_type.h>
#include <du_mutex.h>
#include <du_poll.h>
#include <du_thread.h>

#include <sys/inotify.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef du_bool (*dmedia_detector_allow_event_handler)(dmedia_detector* x, struct inotify_event* ev, void* arg);

typedef du_bool (*dmedia_detector_allow_addwatch_handler)(dmedia_detector* x, const du_uchar* path, void* arg);

struct dmedia_detector {
    du_int _inotify_desc;
    du_poll _poll;
    du_mutex _mutex;
    du_uint32 _maximum_rate_ms;

    volatile du_bool _canceled;
    dmedia_detector_target_array _target_array;

    dmedia_detector_event_handler _handler;
    void* _arg;

    dmedia_detector_allow_event_handler _allow_event_handler;
    void* _allow_event_handler_arg;

    dmedia_detector_allow_addwatch_handler _allow_addwatch_handler;
    void* _allow_addwatch_handler_arg;

    du_thread _th;
    du_bool _run;
};

extern du_uint32 dmedia_detector_count_watch_desc(dmedia_detector* x, du_int watch_desc);

extern du_bool dmedia_detector_set_allow_event_handler(dmedia_detector* x, dmedia_detector_allow_event_handler handler, void* arg);

extern du_bool dmedia_detector_set_allow_addwatch_handler(dmedia_detector* x, dmedia_detector_allow_addwatch_handler handler, void* arg);

#ifdef __cplusplus
}
#endif

#endif
