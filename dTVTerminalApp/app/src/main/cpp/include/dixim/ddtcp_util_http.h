/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_util_http.h 7082 2012-07-02 09:39:39Z gondo $ 
 */ 

/** @file ddtcp_util_http.h
 *  @brief The ddtcp_util_http interface provides various methods for manipulating http header 
 *   string ( such as getting/setting value of some headers ).
 */

#ifndef DDTCP_UTIL_HTTP_H
#define DDTCP_UTIL_HTTP_H

#include <du_type.h>
#include <du_uchar_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Returns a content-range.dtcp.com HTTP header field string.
 * This content-range.dtcp.com header field string is "content-range.dtcp.com".
 * @return  a content-range.dtcp.com HTTP header field string.
 */
extern const du_uchar* ddtcp_util_http_header_content_range_dtcp_com();

/**
 * Returns a range.dtcp.com header field string.
 * This server header field string is "range.dtcp.com".
 * @return  a range.dtcp.com header field string.
 */
extern const du_uchar* ddtcp_util_http_header_range_dtcp_com();

/**
 * Clears <em>field</em> and sets "content-range.dtcp.com: bytes=" string to <em>field</em>.
 * @param[out] field pointer to the du_uchar_array structure.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark <em>field</em> is a pointer to a <em>du_uchar_array</em> initialized by 
 * the <b>du_uchar_array_init</b> function. 
 */
extern du_bool ddtcp_util_http_header_content_range_dtcp_com_init(du_uchar_array* field);

/**
 * Returns a blkmove.dtcp.com HTTP header field string.
 * This blkmove.dtcp.com header field string is "blkmove.dtcp.com".
 * @return  a blkmove.dtcp.com HTTP header field string.
 */
extern const du_uchar* ddtcp_util_http_header_blkmove_dtcp_com();

/**
 * Returns a Alt-ExchangeKey.dtcp.com HTTP header field string.
 * This Alt-ExchangeKey.dtcp.com header field string is "Alt-ExchangeKey.dtcp.com".
 * @return  a Alt-ExchangeKey.dtcp.com HTTP header field string.
 */
extern const du_uchar* ddtcp_util_http_header_alt_exchangekey_dtcp_com();

/**
 * Returns a CMI.dtcp.com HTTP header field string.
 * This CMI.dtcp.com header field string is "CMI.dtcp.com".
 * @return  a CMI.dtcp.com HTTP header field string.
 */
extern const du_uchar* ddtcp_util_http_header_cmi_dtcp_com();

/**
 * Returns a RemoteAccess.dtcp.com HTTP header field string.
 * This RemoteAccess.dtcp.com header field string is "RemoteAccess.dtcp.com".
 * @return  a RemoteAccess.dtcp.com HTTP header field string.
 */
extern const du_uchar* ddtcp_util_http_header_remoteaccess_dtcp_com();

#ifdef __cplusplus
}
#endif

#endif

