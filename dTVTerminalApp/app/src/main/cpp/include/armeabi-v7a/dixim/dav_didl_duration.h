/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dav_didl_duration.h
 *  @brief The dav_didl_duration interface provides methods for handling
 *  res\@duration attribute value.
 */

#ifndef DAV_DIDL_DURATION_H
#define DAV_DIDL_DURATION_H

#include <du_type.h>
#include <dav_didl.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Maximum size of a string representation of duration.
 * The maximum value is "1193:02:47.295"<br>
 * The format is:<br>
 * duration = hours ":" minutes ":" seconds<br>
 * hours = 1*3 DIGIT; 0-999<br>
 * minutes = 2 DIGIT; 00-59<br>
 * seconds = 2 DIGIT ["." 3 DIGIT]; 00-59(.000-999)<br>
 */
#define DAV_DIDL_DURATION_STR_SIZE 16

/**
 * Converts an <em>s</em> duration data string to a <em>msec</em> du_uint32 data in millisecond.
 * @param[in] s string to be converted (duration data).
 * @param[out] msec the duration time value in millisecond.
 * @return length the characters converted.
 * @remark this function stops reading the string <em>s</em> at the first
 * character it cannot recognize as part of a duration data string.
 */
extern du_uint32 dav_didl_duration_scan(const du_uchar* s, du_uint32* msec);

/**
 * Converts a <em>msec</em> duration value in millisecond to a res\@duration format string.
 * @param[out] s string result.
 * @param[in] msec duration value in millisecond to be converted.
 * @return the length of <em>s</em> string.
 */
extern du_uint32 dav_didl_duration_fmt(du_uchar s[DAV_DIDL_DURATION_STR_SIZE], du_uint32 msec);

#ifdef __cplusplus
}
#endif

#endif
