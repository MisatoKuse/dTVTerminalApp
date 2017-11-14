/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DU_POLL_OS_H
#define DU_POLL_OS_H

#include "du_pollinfo_array.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct du_poll {
    du_pollinfo_array array;
    du_int pipe[2];
    volatile du_bool timed_out;
} du_poll;

#ifdef __cplusplus
}
#endif

#endif
