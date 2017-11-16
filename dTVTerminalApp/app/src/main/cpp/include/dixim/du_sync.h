/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_sync interface provides various methods for synchronizing threads
 *  ( such as wait, timedwait, notify, notify_all ).
 *   Before using a du_sync, use <b>du_sync_create</b> to initialize
 *   du_sync data
 *   Use <b>du_sync_free</b> to free the resources du_sync might hold.
 */

#ifndef DU_SYNC_H
#define DU_SYNC_H

#include <du_type.h>
#include <du_sync_os.h>
#include <du_mutex_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Creates a synchronization object.
 *  sync is used for suspending the thread execution until some condition satisfied.
 *  @param[out] sync  pointer to the du_sync data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_sync_create(du_sync* sync);

/**
 *  Frees the resources sync might hold.
 *  @param[in] sync  pointer to the du_sync data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_sync_free(du_sync* sync);

/**
 *  Waits for sync be signaled.
 *  The thread execution is suspended until sync is signaled.
 *  @param[in] sync  pointer to the du_sync data.
 *  @param[in] mutex  pointer to the du_mutex data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_sync_wait(du_sync* sync, du_mutex* mutex);

/**
 *  Waits for sync be signaled.
 *  The thread execution is suspended until
 *  sync is signaled or msec time elapses.
 *  @param[in] sync  pointer to the du_sync data.
 *  @param[in] mutex  pointer to the du_mutex data.
 *  @param[in] msec  the number of milliseconds to wait before this function returns.
 *  @return  true if the function succeeds.
 *           false if the function fails or timed out.
 */
extern du_bool du_sync_timedwait(du_sync* sync, du_mutex* mutex, du_uint32 msec);

/**
 *  Restarts one of the threads that are waiting on sync.
 *  If no threads are waiting on sync, nothing happens.
 *  If several threads are waiting on sync, exactly one is restarted,
 *  but it is not specified which.
 *  @param[in] sync  pointer to the du_sync data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_sync_notify(du_sync* sync);

/**
 *  Restarts all the threads that are waiting on sync.
 *  Nothing happens if no threads are waiting on sync.
 *  @param[in] sync  pointer to the du_sync data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_sync_notify_all(du_sync* sync);

#ifdef __cplusplus
}
#endif

#endif
