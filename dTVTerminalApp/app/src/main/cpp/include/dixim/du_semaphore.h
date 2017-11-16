/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_semaphore interface provides some operations on semaphores
 *  ( such as wait, trywait, post ).
 *   Before using a du_semaphore, use <b>du_semaphore_create</b> to initialize
 *   du_semaphore data
 *   Use <b>du_semaphore_free</b> to free the resources du_semaphore might hold.
 */

#ifndef DU_SEMAPHORE_H
#define DU_SEMAPHORE_H

#include <du_type.h>
#include <du_semaphore_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Creates the semaphore.
 *  @param[out] semaphore pointer to the du_semaphore data.
 *  @param[in] initial initial count for the semaphore.
 *    This value must be greater than or equal to zero and less than or equal to max.
 *  @param[in] max maximum count for the semaphore object. This value must be greater than zero.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_semaphore_create(du_semaphore* semaphore, du_uint32 initial, du_uint32 max);

/**
 *  Frees the resources semaphore might hold.
 *  @param[in] semaphore  pointer to the du_semaphore data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_semaphore_free(du_semaphore* semaphore);

/**
 *  Suspends the calling thread until semaphore has non-zero count
 *  or when the time-out interval elapses.
 *  Each time a waiting thread is released because of the semaphore's signaled state,
 *  the count of the semaphore is decreased by one.
 *  @param[in] semaphore  pointer to the du_semaphore data.
 *  @param[in] msec the time-out interval, in milliseconds.
 *    Zero value means infinite timeout.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_semaphore_wait(du_semaphore* semaphore, du_uint32 msec);

/**
 *  Tests semaphore state and returns immediately.
 *  If semaphore has non-zero count, the count is atomically decreased
 *  and <b>du_semaphore_trywait</b> immediately returns true.
 *  If the semaphore count is zero, <b>du_semaphore_trywait</b>
 *  immediately returns false.
 *  @param[in] semaphore  pointer to the du_semaphore data.
 *  @return  true if the semaphore has non-zero count.
 *           false if the semaphore has zero count.
 */
extern du_bool du_semaphore_trywait(du_semaphore* semaphore);

/**
 *  Increases the count of the semaphore.
 *  @param[in] semaphore  pointer to the du_semaphore data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_semaphore_post(du_semaphore* semaphore);

#ifdef __cplusplus
}
#endif

#endif
