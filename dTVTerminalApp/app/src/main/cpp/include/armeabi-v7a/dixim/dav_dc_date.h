/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dav_dc_date.h
 *  @brief The dav_dc_date interface provides a method for converting
 *  the DIDL-Lite <dc:date> element value.
 */

#ifndef DAV_DC_DATE_H
#define DAV_DC_DATE_H

#include <du_caltime.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DAV_DC_DATE_SIZE 32

/*
 * dc_date format:
 *   CCYY-MM-DD
 *   CCYY-MM-DDThh:mm:ss
 *   CCYY-MM-DDThh:mm:ssZ
 *   CCYY-MM-DDThh:mm:ss+hh:mm
 *   CCYY-MM-DDThh:mm:ss-hh:mm
 *   CCYY-MM-DDThh:mm:ss.sss
 *   CCYY-MM-DDThh:mm:ss.sssZ
 *   CCYY-MM-DDThh:mm:ss.sss+hh:mm
 *   CCYY-MM-DDThh:mm:ss.sss-hh:mm
 * normalize to:
 *   CCYY-MM-DDThh:mm:ss.sss
*/

/**
 * Converts a DIDL-Lite <dc:date> element value to a normalized format string
 * and stores it in <em>normalized</em>.
 * @param[in] dc_date the DIDL-Lite <dc:date> element value.
 * @param[in] normalized pointer to the storage location for the normalized format date data.
 * @remarks The DIDL-Lite <dc:date> element value <em>dc_date</em>
 * is the following format<br>
 *   CCYY-MM-DD[Thh:mm:ss[.sss][Z|[+|-]hh:mm]<br>
 *  Essentially, the following combinations are permitted<br>
 *   CCYY-MM-DD<br>
 *   CCYY-MM-DDThh:mm:ss<br>
 *   CCYY-MM-DDThh:mm:ssZ<br>
 *   CCYY-MM-DDThh:mm:ss+hh:mm<br>
 *   CCYY-MM-DDThh:mm:ss-hh:mm<br>
 *   CCYY-MM-DDThh:mm:ss.sss<br>
 *   CCYY-MM-DDThh:mm:ss.sssZ<br>
 *   CCYY-MM-DDThh:mm:ss.sss+hh:mm<br>
 *   CCYY-MM-DDThh:mm:ss.sss-hh:mm<br>
 * The format of the <em>normalized</em> is <br>
 *   CCYY-MM-DDThh:mm:ss.sss<br>
 */
extern du_bool dav_dc_date_normalize(const du_uchar* dc_date, du_uchar normalized[DAV_DC_DATE_SIZE]);

/**
 * Converts an <em>normalized</em> format date string to a <em>msec</em> du_timel data in millisecond.
 * @param[in] normalized string to be converted (normalized format date data).
 * @param[out] msec the time value in millisecond.
 * @return length the characters converted.
 * @remark this function stops reading the string <em>normalized</em> at the first
 * character it cannot recognize as part of a date string.
 */
extern du_uint32 dav_dc_date_scan_msec(const du_uchar* normalized, du_timel* msec);

/**
 * Converts an <em>normalized</em> format date string to a <em>sec</em> du_time data in second.
 * @param[in] normalized string to be converted (normalized format date data).
 * @param[out] sec the time in second.
 * @return length the characters converted.
 * @remark this function stops reading the string <em>normalized</em> at the first
 * character it cannot recognize as part of a date string.
 */
extern du_uint32 dav_dc_date_scan_sec(const du_uchar* normalized, du_time* sec);

/**
 * Converts a <em>msec</em> value in millisecond to a normalized dc:date format string.
 * @param[out] normalized string result.
 * @param[in] msec time in millisecond to be converted.
 * @return the length of <em>s</em> string.
 */
extern du_uint32 dav_dc_date_fmt_msec(du_uchar normalized[DAV_DC_DATE_SIZE], const du_timel* msec);

/**
 * Converts a <em>sec</em> value in second to a normalized dc:date format string.
 * @param[out] normalized string result.
 * @param[in] sec time in second to be converted.
 * @return the length of <em>s</em> string.
 */
extern du_uint32 dav_dc_date_fmt_sec(du_uchar normalized[DAV_DC_DATE_SIZE], const du_time* sec);

#ifdef __cplusplus
}
#endif

#endif
