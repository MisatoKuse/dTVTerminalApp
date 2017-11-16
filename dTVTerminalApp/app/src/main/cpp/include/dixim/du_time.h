/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_time interface provides methods for getting current time.
 */

#ifndef DU_TIME_H
#define DU_TIME_H

#ifdef __cplusplus
extern "C" {
#endif

#include <du_time_os.h>

/**
 *  Gets the number of seconds since January 1, 1970, 00:00:00
 *  @param[out] t   pointer to the variable to receive time in seconds.

 */
extern void du_time_sec(du_time* t);

/**
 *  Gets the number of milliseconds since January 1, 1970, 00:00:00
 *  @param[out] t   pointer to the variable to receive time in milliseconds.
 */
extern void du_time_msec(du_timel* t);

/**
 *  Gets the number of microseconds since January 1, 1970, 00:00:00
 *  @param[out] t   pointer to the variable to receive time in microseconds.
 */
extern void du_time_usec(du_timel* t);

#ifdef __cplusplus
}
#endif

#endif
