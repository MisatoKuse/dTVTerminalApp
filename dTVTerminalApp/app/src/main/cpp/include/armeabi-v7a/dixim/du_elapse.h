/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *  The du_elapse interface provides methods for getting time difference value.
 *
 *  Before using a du_elapse, use du_elapse_start() to initialize du_elapse
 *  data.
 *  Use du_elapse_delta() to get time difference in millisecond
 *  between the previous time measurement and the current system time.
 *  du_elapse_total() gives time difference in millisecond between
 *  the time du_elapse_start() called and the current system time.
 */

#ifndef DU_ELAPSE_H
#define DU_ELAPSE_H

#include <du_time.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Structure which is used by du_elapse API.
 */
typedef struct {
    du_timel start;
    du_timel delta;
} du_elapse;

/**
 *  Initializes du_elapse s.
 *
 *  Sets the current system time in s.
 *
 *  @param[out] s pointer to the du_elapse data.
 */
extern void du_elapse_start(du_elapse* s);

/**
 *  Gets time difference in millisecond between the previous time measurement
 *  and the current system time.
 *
 *  @param[in,out] s pointer to the du_elapse data.
 *  @return time difference in millisecond between the previous time measurement
 *  and the current system time.
 *  @pre @p s must be started by calling du_elapse_start().
 */
extern du_uint32 du_elapse_delta(du_elapse* s);

/**
 *  Gets time difference in millisecond between the time du_elapse_start called
 *  and the current system time.
 *
 *  @param[in] s pointer to the du_elapse data.
 *  @return time difference in millisecond between the time du_elapse_start
 *  called and the current system time.
 *  @pre @p s must be started by calling du_elapse_start().
 */
extern du_uint32 du_elapse_total(du_elapse* s);

/**
 *  Initializes du_elapse s.
 *
 *  Sets the current system time in s.
 *
 *  @param[out] s pointer to the du_elapse data.
 */
extern void du_elapse_start2(du_elapse* s);

/**
 *  Gets time difference in microsecond between the previous time measurement
 *  and the current system time.
 *
 *  @param[in,out] s pointer to the du_elapse data.
 *  @return time difference in microsecond between the previous time measurement
 *  and the current system time.
 *  @pre @p s must be started by calling du_elapse_start2().
 */
extern du_uint32 du_elapse_delta2(du_elapse* s);

/**
 *  Gets time difference in microsecond between the time du_elapse_start2 called
 *  and the current system time.
 *
 *  @param[in] s pointer to the du_elapse data.
 *  @return time difference in microsecond between the time du_elapse_start2
 *  called and the current system time.
 *  @pre @p s must be started by calling du_elapse_start2().
 */
extern du_uint32 du_elapse_total2(du_elapse* s);

#ifdef __cplusplus
}
#endif

#endif
