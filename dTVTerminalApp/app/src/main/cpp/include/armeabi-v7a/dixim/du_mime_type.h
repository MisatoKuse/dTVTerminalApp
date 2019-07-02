/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_mime_type interface provides some methods for maminupating mime type string
 *  (such as checking major type and sub type, getting a specified parameter value).
 */

#ifndef DU_MIME_TYPE_H
#define DU_MIME_TYPE_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Tests if major type of mime_type is major_type,
 *  ignoring case differences.
 *  @param[in] mime_type a mime type string.
 *  @param[in] major_type a string to be compared.
 *  @return true if the major type represented by mime_type
 *     is major_type.
 *     false otherwise.
 */
extern du_bool du_mime_type_major_equal(const du_uchar* mime_type, const du_uchar* major_type);

/**
 *  Tests if sub type of mime_type is sub_type,
 *  ignoring case differences.
 *  @param[in] mime_type a mime type string.
 *  @param[in] sub_type a string to be compared.
 *  @return true if the sub type represented by mime_type
 *     is sub_type.
 *     false otherwise.
 */
extern du_bool du_mime_type_sub_equal(const du_uchar* mime_type, const du_uchar* sub_type);

/**
 *  Gets a parameter value of a MIME-TYPE.
 *  @param[in] mime_type a mime type string.
 *  @param[in] name a parameter name string.
 *  @param[out] buf storage location for the value string.
 *  @param[in] len  the byte length of buf.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_mime_type_get_parameter_value(const du_uchar* mime_type, const du_uchar* name, du_uchar* buf, du_uint32 len);

/**
 *  Gets a major type of a MIME-TYPE.
 *  @param[in] mime_type a mime type string.
 *  @param[out] buf storage location for the major type string.
 *  @param[in] len  the byte length of buf.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_mime_type_get_major(const du_uchar* mime_type, du_uchar* buf, du_uint32 len);

/**
 *  Gets a minor type of a MIME-TYPE.
 *  @param[in] mime_type a mime type string.
 *  @param[out] buf storage location for the minor type string.
 *  @param[in] len  the byte length of buf.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_mime_type_get_minor(const du_uchar* mime_type, du_uchar* buf, du_uint32 len);

#ifdef __cplusplus
}
#endif

#endif
