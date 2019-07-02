/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DU_MUTEX_OS_H
#define DU_MUTEX_OS_H

#include <pthread.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Structure of du_mutex.
 */
typedef pthread_mutex_t du_mutex;

#ifdef __cplusplus
}
#endif

#endif
