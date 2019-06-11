/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

#ifndef DDTCP_UTIL_EVENT_H
#define DDTCP_UTIL_EVENT_H

#include <ddtcp.h>
#include <du_semaphore.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct ddtcp_util_event {
    du_bool is_created;
    du_semaphore semaphore;
    du_bool is_failed;
} ddtcp_util_event;

extern du_bool ddtcp_util_event_create(ddtcp_util_event* event);
extern du_bool ddtcp_util_event_free(ddtcp_util_event* event);
extern du_bool ddtcp_util_event_notify(ddtcp_util_event* event);
extern du_bool ddtcp_util_event_wait(ddtcp_util_event* event, du_uint32 msec);

#ifdef __cplusplus
}
#endif

#endif
