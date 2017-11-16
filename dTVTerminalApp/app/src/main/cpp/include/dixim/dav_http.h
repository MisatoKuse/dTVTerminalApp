/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dav_http.h
 *  @brief The dav_http interface provides some methods for creating a
 *  HTTP headers for UPnP AV ( such as creating a PlaySpeed.dlna.org HTTP header and
 *  a TimeSeekRange.dlna.org HTTP header).
 */

#ifndef DAV_HTTP_H
#define DAV_HTTP_H

#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Type of HTTP transfer mode.
  * @li @c DAV_HTTP_TRANSFER_MODE_UNKNOWN : Type is unknown.
  * @li @c DAV_HTTP_TRANSFER_MODE_STREAMING : Streaming mode.
  * @li @c DAV_HTTP_TRANSFER_MODE_INTERACTIVE : Tnteractive mode.
  * @li @c DAV_HTTP_TRANSFER_MODE_BACKGROUND : Background mode.
  */
typedef enum {
    DAV_HTTP_TRANSFER_MODE_UNKNOWN,
    DAV_HTTP_TRANSFER_MODE_STREAMING,
    DAV_HTTP_TRANSFER_MODE_INTERACTIVE,
    DAV_HTTP_TRANSFER_MODE_BACKGROUND,
} dav_http_transfer_mode;

/**
 * This structure contains the values given by TimeSeekRange.dlna.org HTTP header.
 */
typedef struct dav_http_content_time_seek_range {
    du_uint32 start_time_msec; /**< Specifies time for the start of content binary  */
    du_uint32 end_time_msec; /**< Specifies time for the end of content binary  */
    du_uint32 duration_msec; /**< Specifies the duration of an entire content binary  */
} dav_http_content_time_seek_range;

/**
 * Initializes a <em>field</em> and appends the string of the PlaySpeed.dlna.org HTTP header
 * to <em>field</em>.
 * @param[out] field pointer to the du_uchar_array structure data.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remarks The PlaySpeed.dlna.org HTTP header field is used for the request of
 * changing the playback speed. This function appends "PlaySpeed.dlna.org: speed="
 * string to <em>field</em>.
 */
extern du_bool dav_http_header_play_speed_init(du_uchar_array* field);

/**
 * Appends play-speed value specified by <em>integer</em> to <em>field</em>.
 * @param[in,out] field pointer to the destination du_uchar_array structure data.
 * @param[in] integer the play-speed value
 * @return  true if, and only if, <em>integer</em> value is appended successfully.
 * @remarks  <em>field</em> is a pointer to a <em>du_uchar_array</em> initialized by
 * the <b>dav_http_header_play_speed_init</b> function.
 * The PlaySpeed.dlna.org HTTP header string is stored in <em>field</em>
 *  using <b>dav_http_header_play_speed_integer</b> function after
 * <b>dav_http_header_play_speed_init</b> in the following format .<br>
 *  "PlaySpeed.dlna.org: speed=integer"
 */
extern du_bool dav_http_header_play_speed_integer(du_uchar_array* field, du_int32 integer);

/**
 * Appends the play-speed value specified by <em>numerator</em> and <em>denominator</em>
 * to <em>field</em>.
 * @param[in,out] field pointer to the destination du_uchar_array structure data.
 * @param[in] numerator the numerator value of the play-speed.
 * @param[in] denominator the denominator value of the play-speed.
 * @return  true if, and only if, <em>numerator</em> and <em>denominator</em> are
 *  appended successfully.
 * @remarks  <em>field</em> is a pointer to a <em>du_uchar_array</em> initialized by
 * the <b>dav_http_header_play_speed_init</b> function.
 * The PlaySpeed.dlna.org HTTP header string is stored in <em>field</em>
 *  using <b>dav_http_header_play_speed_fraction</b> function after
 * <b>dav_http_header_play_speed_init</b> in the following format .<br>
 *  "PlaySpeed.dlna.org: speed=numerator/denominator"
 */
extern du_bool dav_http_header_play_speed_fraction(du_uchar_array* field, du_int32 numerator, du_uint32 denominator);

/**
 * Initializes a <em>field</em> and appends the string of the TimeSeekRange.dlna.org HTTP header
 * to <em>field</em>.
 * @param[out] field pointer to the du_uchar_array structure data.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remarks The TimeSeekRange.dlna.org HTTP header field is used for
 * performing a seek operation on a stream.
 *  This function appends "TimeSeekRange.dlna.org: npt="
 * string to <em>field</em>.
 */
extern du_bool dav_http_header_time_seek_range_init(du_uchar_array* field);

/**
 * Appends a startpoint time value specified by <em>msec</em> to <em>field</em>.
 * @param[in,out] field pointer to the destination du_uchar_array structure data.
 * @param[in] msec the startpoint time value of the time-range of
 *  the TimeSeekRange.dlna.org header field in millisecond.
 * @return  true if, and only if, <em>msec</em> value is appended successfully.
 * @remarks  <em>field</em> is a pointer to a <em>du_uchar_array</em> initialized by
 * the <b>dav_http_header_time_seek_range_init</b> function.
 */
extern du_bool dav_http_header_time_seek_range_start_pos(du_uchar_array* field, du_uint32 msec);

/**
 * Appends a endpoint time value specified by <em>msec</em> to <em>field</em>.
 * @param[in,out] field pointer to the destination du_uchar_array structure data.
 * @param[in] msec the endpoint time value of the time-range of
 *  the TimeSeekRange.dlna.org header field in millisecond.
 * @return  true if, and only if, <em>msec</em> value is appended successfully.
 * @remarks  <em>field</em> is a pointer to a <em>du_uchar_array</em> initialized by
 * the <b>dav_http_header_time_seek_range_init</b> function.
 * The PlaySpeed.dlna.org HTTP header string is stored in <em>field</em>
 *  using <b>dav_http_header_time_seek_range_end_pos</b> function after
 * <b>dav_http_header_play_speed_init</b>,
 * <b>dav_http_header_time_seek_range_start_pos</b> in the following format .<br>
 *  "TimeSeekRange.dlna.org: npt=startpont_time/endpoint_time"<br>
 *  Here, startpont_time and endpoint_time are time value to specify the range of
 *  the TimeSeekRange.dlna.org header.
 */
extern du_bool dav_http_header_time_seek_range_end_pos(du_uchar_array* field, du_uint32 msec);

/**
 * Appends a duration time value specified by <em>msec</em> to <em>field</em>.
 * @param[in,out] field pointer to the destination du_uchar_array structure data.
 * @param[in] msec the duration time value in millisecond.
 * @return  true if, and only if, <em>msec</em> value is appended successfully.
 * @remarks  <em>field</em> is a pointer to a <em>du_uchar_array</em> initialized by
 * the <b>dav_http_header_time_seek_range_init</b> function.
 */
extern du_bool dav_http_header_time_seek_range_duration(du_uchar_array* field, du_uint32 msec);

/**
 * Appends a unknown duration time ("*") to <em>field</em>.
 * @param[in,out] field pointer to the destination du_uchar_array structure data.
 * @return  true if, and only if, <em>msec</em> value is appended successfully.
 * @remarks  <em>field</em> is a pointer to a <em>du_uchar_array</em> initialized by
 * the <b>dav_http_header_time_seek_range_init</b> function.
 */
extern du_bool dav_http_header_time_seek_range_unknown_duration(du_uchar_array* field);

/**
 * Parses TimeSeekRange.dlna.org HTTP header.
 * @param[in] value TimeSeekRange.dlna.org HTTP header value(string data).
 * @param[out] start_msec specifies time for the start of content binary
 * @param[out] end_msec specifies time for the end of content binary
 * @param[out] has_end_msec true if TimeSeekRange.dlna.org HTTP header has end time, otherwise false.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark An example of a TimeSeekRange.dlna.org HTTP header is <br>
 *      "TimeSeekRange.dlna.org: npt=10.123-24.567"
 *      "TimeSeekRange.dlna.org: npt=10.123-"
 */
extern du_bool dav_http_header_parse_time_seek_range(const du_uchar* value, du_uint32* start_msec, du_uint32* end_msec, du_bool* has_end_msec);

/**
 * Gets the dav_http_transfer_mode value from <em>value</em>.
 * @param[in] value string that specifies the transfer mode (such as "Streaming", "Interactive",
 *    "Background").
 * @param[out] transfer_mode type of HTTP transfer mode.
 */
extern void dav_http_header_get_transfer_mode(const du_uchar* value, dav_http_transfer_mode* transfer_mode);

/**
 * Sets a transfermode.dlna.org header line in <em>header</em>.
 * @param[in,out] header pointer to the du_str_array structure.
 * @param[in] transfer_mode type of HTTP transfer mode.
 * @param[in] replace set true if you replace the old transfermode.dlna.org header line
 *    previously contained in the <em>header</em> to new <em>transfer_mode</em> value.
 * @return  true if the function succeeds.
 *          false if the function fails.
 * @remark "transfermode.dlna.org: valueCR+LF" line is added or replaced in
 *    <em>header</em> string array. Here, value is one of the following string,
 *    "Streaming", "Interactive", "Background". <br>
 *          <em>header</em> is a pointer to a <em>du_str_array</em> initialized by
 *          the <b>du_str_array_init</b> function.
 */
extern du_bool dav_http_header_set_transfer_mode(du_str_array* header, dav_http_transfer_mode transfer_mode, du_bool replace);

/**
 * Returns getcontentFeatures.dlna.org HTTP header string
 * This string is "getcontentfeatures.dlna.org".
 * @return  "getcontentfeatures.dlna.org" string.
 */
extern const du_uchar* dav_http_header_get_content_features_dlna_org(void);

/**
 * Returns contentFeatures.dlna.org HTTP header string
 * This string is "contentfeatures.dlna.org".
 * @return  "contentfeatures.dlna.org" string.
 */
extern const du_uchar* dav_http_header_content_features_dlna_org(void);

/**
 * Returns TimeSeekRange.dlna.org HTTP header string
 * This string is "timeseekrange.dlna.org".
 * @return  "timeseekrange.dlna.org" string.
 */
extern const du_uchar* dav_http_header_time_seek_range_dlna_org(void);

/**
 * Returns PlaySpeed.dlna.org HTTP header string
 * This string is "playspeed.dlna.org".
 * @return  "playspeed.dlna.org" string.
 */
extern const du_uchar* dav_http_header_play_speed_dlna_org(void);

/**
 * Returns realTimeInfo.dlna.org HTTP header string
 * This string is "realtimeinfo.dlna.org".
 * @return  "realtimeinfo.dlna.org" string.
 */
extern const du_uchar* dav_http_header_real_time_info_dlna_org(void);

/**
 * Returns transferMode.dlna.org HTTP header string
 * This string is "transfermode.dlna.org".
 * @return  "transfermode.dlna.org" string.
 */
extern const du_uchar* dav_http_header_transfer_mode_dlna_org(void);

#ifdef __cplusplus
}
#endif

#endif
