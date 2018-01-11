/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_util_thread.h 7556 2012-12-07 11:55:26Z gondo $ 
 */ 

#ifndef DDTCP_UTIL_THREAD_H
#define DDTCP_UTIL_THREAD_H

#include <ddtcp.h>
#include <du_thread.h>
#include <ddtcp_util_event.h>
#include <ddtcp_util_log.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    du_thread thread;
    du_bool is_created;
    ddtcp_util_event stop_event;
    du_bool is_stopped;
    du_bool is_failed;
} ddtcp_util_thread;

extern du_bool ddtcp_util_thread_create(ddtcp_util_thread* thread, void (*start_routine)(void*), void* arg);
extern du_bool ddtcp_util_thread_wait(ddtcp_util_thread* thread, du_uint32 msec);
extern du_bool ddtcp_util_thread_is_stopped(ddtcp_util_thread* thread);
extern du_bool ddtcp_util_thread_join(ddtcp_util_thread* thread);
extern du_bool ddtcp_util_thread_set_priority_self_high();
extern du_bool ddtcp_util_thread_set_priority_self(du_thread_priority priority);

//#ifdef DEBUG_LOG
#if 0
#define ddtcp_util_thread_yield() { \
    du_thread_yield(); \
    du_log_dv(ddtcp_util_log_category, DU_UCHAR("thread yield")); \
}
#else
#define ddtcp_util_thread_yield() du_thread_yield()
#endif


#ifdef __cplusplus
}
#endif

#endif
