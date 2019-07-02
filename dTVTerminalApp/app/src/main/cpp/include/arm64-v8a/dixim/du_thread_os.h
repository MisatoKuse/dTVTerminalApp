/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file du_thread_os.h
 * @brief The du_thread interface provides methods for thread management for Linux.
 *  ( sets/gets thread priority ).
 */

#ifndef DU_THREAD_OS_H
#define DU_THREAD_OS_H

#include <pthread.h>
#include <sys/types.h>
#include <unistd.h>
#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct du_thread {
    pthread_t _tid;
    pid_t _pid;
} du_thread;

/**
 * Sets the thread scheduling policy.
 * @param[in] thread pointer to a du_thread data.
 * @param[in] policy thread scheduling policy value.
 *  This parameter can be one of the following values.
 *  @li @c SCHED_FIFO first-in first-out scheduling policy
 *  @li @c SCHED_RR   round-robin scheduling policy
 *  @li @c SCHED_OTHER default linux time-sharing scheduling
 *  @li @c SCHED_BATCH scheduling batch process
 *  @li @c SCHED_IDLE for running very low priority background jobs
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool du_thread_set_policy(du_thread* thread, du_int policy);

/**
 * Gets the thread scheduling policy value.
 * @param[in] thread pointer to a du_thread data.
 * @param[out] policy thread scheduling policy value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool du_thread_get_policy(du_thread* thread, du_int* policy);

#ifdef __cplusplus
}
#endif

#endif
