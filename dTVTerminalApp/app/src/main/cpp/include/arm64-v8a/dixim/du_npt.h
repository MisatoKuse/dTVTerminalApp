/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *   The du_npt interface provides methods converting a npt-time format string
 *  to/from a millisecond data.
 *  The npt(normal play time) described in rfc2326 consists
 *  of a decimal fraction. The part left of the decimal may be expressed
 *  in either seconds or "hours:minutes:seconds".
 *  The part right of the decimal point measures fractions of a second.
 *  ex. sec format is 123.45, hhmmss format is 12:05:35.3
 */

#ifndef DU_NPT_H
#define DU_NPT_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Maximum size of a NPT string.
 */
#define DU_NPT_STR_SIZE 40

/**
 *  Converts a npt string format defined in rfc1123 to milliseconds.
 *  @param[in] s string of the form "sec"+"."+"fractions of a second".
 *  @param[out] msec storage pointer to the variable to receive time in millisecond.
 */
extern du_uint32 du_npt_scan_sec(const du_uchar* s, du_uint32* msec);

/**
 *  Converts a npt string format defined in rfc1123 to milliseconds.
 *  @param[in] s string of the form "hh:mm:ss"+"."+"fractions of a second".
 *  @param[out] msec storage pointer to the variable to receive time in millisecond.
 */
extern du_uint32 du_npt_scan_hhmmss(const du_uchar* s, du_uint32* msec);

/**
 *  Converts a time in milliseconds to a npt string format defined in rfc1123.
 *  @param[out] s string of the form "sec"+"."+"fractions of a second".
 *  @param[in] msec time in millisecond to convert.
 */
extern du_uint32 du_npt_fmt_sec(du_uchar s[DU_NPT_STR_SIZE], du_uint32 msec);

#ifdef __cplusplus
}
#endif

#endif
