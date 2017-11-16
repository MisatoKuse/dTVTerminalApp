/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dav_didl_resolution.h
 *  @brief The dav_didl_resolution interface provides methods for handling
 *  res\@resolution attribute value.
 */

#ifndef DAV_DIDL_RESOLUTION_H
#define DAV_DIDL_RESOLUTION_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DAV_DIDL_RESOLUTION_STR_SIZE 10 * 2 + 1 + 1

/**
 * Converts an <em>s</em> resolution data string to a <em>width</em> and <em>height</em> du_uint32 data.
 * @param[in] s string to be converted (resolution data).
 * @param[out] width the resolution width value.
 * @param[out] height the resolution height value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark this function stops reading the string <em>s</em> at the first
 * character it cannot recognize as part of a resolution data string.
 */
extern du_bool dav_didl_resolution_scan(const du_uchar* s, du_uint32* width, du_uint32* height);

/**
 * Converts a <em>width</em> and <em>heigh</em> resolution value to a res\@resolution format string.
 * @param[out] s string result.
 * @param[in] width resolution width value.
 * @param[in] height resolution height value.
 * @return the length of <em>s</em> string.
 */
extern du_uint32 dav_didl_resolution_fmt(du_uchar s[DAV_DIDL_RESOLUTION_STR_SIZE], du_uint32 width, du_uint32 height);

#ifdef __cplusplus
}
#endif

#endif
