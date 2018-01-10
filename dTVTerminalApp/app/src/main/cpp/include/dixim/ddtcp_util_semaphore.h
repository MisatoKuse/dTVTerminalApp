/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_util_semaphore.h 994 2008-02-27 16:40:28Z gondo $ 
 */ 

#ifndef DDTCP_UTIL_SEMAPHORE_H
#define DDTCP_UTIL_SEMAPHORE_H

#include <ddtcp.h>
#include <du_semaphore.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    du_bool is_created;
    du_semaphore semaphore;
    du_bool is_failed;
} ddtcp_util_semaphore;

extern du_bool ddtcp_util_semaphore_create(ddtcp_util_semaphore* semaphore, du_uint32 max);
extern du_bool ddtcp_util_semaphore_free(ddtcp_util_semaphore* semaphore);
extern du_bool ddtcp_util_semaphore_wait(ddtcp_util_semaphore* semaphore);
extern du_bool ddtcp_util_semaphore_post(ddtcp_util_semaphore* semaphore);
extern du_bool ddtcp_util_semaphore_wait_2(ddtcp_util_semaphore* semaphore, du_bool* is_acquired);
extern du_bool ddtcp_util_semaphore_post_2(ddtcp_util_semaphore* semaphore, du_bool* is_acquired);

#ifdef __cplusplus
}
#endif

#endif
