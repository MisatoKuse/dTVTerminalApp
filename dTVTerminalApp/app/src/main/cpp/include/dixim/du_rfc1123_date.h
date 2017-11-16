/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_rfc1123_date interface provides methods for converting a string of the form
 *  defined in rfc1123 to/from a du_time data.
 *  The string form is @n
 *  "day-of-week, day month year hour:minute:second timezone".
 *   ex. "Wed, 30 Jun 2006 21:49:08 GMT" @n
 *  The abbreviations for day-of-week are `Sun', `Mon', `Tue', `Wed', `Thu',
 *  `Fri', and `Sat'. The abbreviations for the month are
 *  `Jan', `Feb', `Mar', `Apr', `May', `Jun', `Jul', `Aug', `Sep', `Oct', `Nov', and `Dec'.
 *
 *  Format:
 *  [day-of-week ","] day month year hour ":" minute [":" second] timezone
 *  @see RFC1123 section 5.2.14 and RFC822 section 5.
 */

#ifndef DU_RFC1123_DATE_H
#define DU_RFC1123_DATE_H

#include <du_time_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Maximum size of the string representation of RFC1123 date format.
 */
#define DU_RFC1123_DATE_SIZE 40

/**
 *  Converts du_time time data to a string format@n
 *  "day-of-week, day month year hour:minute:second GMT".
 *  @param[out] s string of the form defined in rfc1123.
 *  @param[in] t pointer to the time value.
 *  @remarks The du_time t is represented as seconds elapsed since midnight
 *  (00:00:00), January 1, 1970, coordinated universal time (UTC).
 */
extern du_uint32 du_rfc1123_date_fmt(du_uchar s[DU_RFC1123_DATE_SIZE], du_time* t);

/**
 *  Converts a string format defined in rfc1123 to a du_time time data.
 *  @param[in] s string of the form defined in rfc1123 to convert.
 *  @param[out] t storage pointer to the variable to receive time value.
 *  @remarks The du_time t is represented as seconds elapsed since midnight
 *  (00:00:00), January 1, 1970, coordinated universal time (UTC).
 */
extern du_uint32 du_rfc1123_date_scan(const du_uchar* s, du_time* t);

#ifdef __cplusplus
}
#endif

#endif
