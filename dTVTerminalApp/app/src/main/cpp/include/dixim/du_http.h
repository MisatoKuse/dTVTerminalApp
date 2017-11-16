/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_http interface provides various methods for manipulating http header
 *   string ( such as reading HTTP header data, searching header,
 *   getting/setting value of some fields, and reading chunked transfer coding data ).
 */

#ifndef DU_HTTP_H
#define DU_HTTP_H

#include <du_socket_input_buffer.h>
#include <du_str_array.h>
#include <du_str_input_buffer.h>
#include <du_time.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef enum {
    DU_HTTP_CHUNK_STATE_ERROR,
    DU_HTTP_CHUNK_STATE_INIT,
    DU_HTTP_CHUNK_STATE_SIZE,
    DU_HTTP_CHUNK_STATE_START_LF,
    DU_HTTP_CHUNK_STATE_DATA,
    DU_HTTP_CHUNK_STATE_END_CR,
    DU_HTTP_CHUNK_STATE_END_LF,
    DU_HTTP_CHUNK_STATE_EXTENSION,
    DU_HTTP_CHUNK_STATE_TRAILER0,
    DU_HTTP_CHUNK_STATE_TRAILER,
    DU_HTTP_CHUNK_STATE_TRAILER_LF,
    DU_HTTP_CHUNK_STATE_BODY_END_LF,
    DU_HTTP_CHUNK_STATE_EOS,
} du_http_chunk_state;

typedef struct du_http_chunk {
    du_http_chunk_state state;
    du_uint64 remains;
} du_http_chunk;

/**
 *  This structure contains http-range information.
 */
typedef struct du_http_content_range {
    /**
     *   absolute byte positions for the first byte of the range
     */
    du_uint64 offset;

    /**
     *   byte length of the range.
     */
    du_uint64 content_length;

    /**
     *   total byte length of the full entity-body
     */
    du_uint64 total;

} du_http_content_range;

/**
 *  Finds a text line and returns the text length and the index to the first occurrence of
 *  CR(Carriage Return)+LF(Line Feed).
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[out] total pointer to the variable to receive the number of bytes
 *    stored in the sb buffer.
 *  @param[out] index pointer to the variable to receive the index to the
 *    first occurrence of CR(Carriage Return)+LF(Line Feed) in the sb buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_find_line_socket(du_socket_input_buffer* sb, du_uint32* total, du_uint32* index);

/**
 *  Finds a text line and returns the text length and the index to the first occurrence of
 *  CR(Carriage Return)+LF(Line Feed).
 *  @param[in] sb  pointer to the du_str_input_buffer structure.
 *  @param[out] total pointer to the variable to receive the number of bytes
 *    stored in the sb buffer.
 *  @param[out] index pointer to the variable to receive the index to the
 *    first occurrence of CR(Carriage Return)+LF(Line Feed) in the sb buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_find_line_str(du_str_input_buffer* sb, du_uint32* total, du_uint32* index);

/**
 *  Tests if the name of given HTTP header field is name.
 *  @param[in] field a http header field.
 *  @param[in] name a string to search for.
 *  @return  true if, and only if, field has the name as a name of http header.
 */
extern du_bool du_http_header_name_equal(const du_uchar* field, const du_uchar* name);

/**
 *  Attempts to find the specified name from the given header and returns the position.
 *  @param[in] header pointer to the du_str_array structure.
 *  @param[in] name the specified name string to search for.
 *  @return
 *    position of the specified name string if, and only if,
 *    the specified name string finds in the array.
 *    length of header if the specified name string does not find in
 *    header or header has failed.
 */
extern du_uint32 du_http_header_find(const du_str_array* header, const du_uchar* name);

/**
 *  Returns the value string of the given http header field.
 *  @param[in] field a http header string.
 *  @return a pointer to the value of field, or NULL if field does not have header name.
 */
extern const du_uchar* du_http_header_get_value(const du_uchar* field);

/**
 *  Finds a HTTP header field by name and returns the value string.
 *  and returns a pointer to the value substring.
 *  @param[in] header pointer to the du_str_array structure.
 *  @param[in] name the specified name string to search for.
 *  @return
 *    a pointer to the value substring if, and only if,
 *    the specified name string finds in the array, or NULL if name does not appear in
 *  header array.
 */
extern const du_uchar* du_http_header_get_value2(const du_str_array* header, const du_uchar* name);

/**
 *  Gets a "max-age" value.
 *  @param[in] value a string.
 *  @param[out] max_age the value of "max-age" in value.
 *  @return  true if the function succeeds.
 *           false if the "max-age" is not found in value or
 *           input "max-age" value cannot be converted to a integer.
 */
extern du_bool du_http_header_get_max_age(const du_uchar* value, du_uint32* max_age);

/**
 *  Gets a timeout value.
 *  @param[in] value a string specifies the timeout in the following format.<br>
 *      second-<i>requested subscription duration</i> <br>
 *      <i>requested subscription duration</i> is either number of seconds or "infinite".
 *  @param[out] timeout pointer to the variable to receive the timeout value
 *     given by value. If the <i>requested subscription duration</i> is "infinite",
 *     then timeout value is -1.
 *  @return  true if the function succeeds.
 *           false if the timeout value substring does not appear in value string.
 *  @remark timeout is a HTTP header sent with a request/response message of
 *  UPnP SUBSCRIBE method.
 */
extern du_bool du_http_header_get_timeout(const du_uchar* value, du_int32* timeout);

/**
 *  Parses a HTTP range header field.
 *  @param[in] value HTTP range header field.
 *  @param[in] total total length of the full entity-body.
 *  @param[out] content_range pointer to du_http_content_range structure.
 *  @param[out] error_status pointer to the variable to receive the HTTP status code.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark An example of a content-range HTTP header is <br>
 *       "Range: bytes=21010-47021"
 *  @remark total must be set to non-zero value.
 */
extern du_bool du_http_header_parse_range(const du_uchar* value, du_uint64 total, du_http_content_range* content_range, const du_uchar** error_status);

/**
 *  Removes a HTTP header field from the given header by name.
 *  @param[in,out] header pointer to the du_str_array structure.
 *  @param[in] name the specified name string of the element to remove.
 */
extern void du_http_header_remove(du_str_array* header, const du_uchar* name);

/**
 *  Sets a HTTP header field to the given header.
 *  @param[in,out] header pointer to the du_str_array structure.
 *  @param[in] name header name string.
 *  @param[in] value header value(string data).
 *  @param[in] replace set true if you replace the old name header line
 *     previously contained in the header to new value.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark "name: valueCR+LF" line is added or replaced in header string array.
 *           header is a pointer to a du_str_array initialized by
 *           the <b>du_str_array_init</b> function.
 */
extern du_bool du_http_header_set_str(du_str_array* header, const du_uchar* name, const du_uchar* value, du_bool replace);

/**
 *  Sets a HTTP header field to the given header.
 *  @param[in,out] header pointer to the du_str_array structure.
 *  @param[in] name header name string.
 *  @param[in] value header value(du_uint32 data).
 *  @param[in] replace set true if you replace the old name header line
 *     previously contained in the header to new value.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark "name: valueCR+LF" line is added or replaced in header string array.
 *           header is a pointer to a du_str_array initialized by
 *           the <b>du_str_array_init</b> function.
 */
extern du_bool du_http_header_set_uint32(du_str_array* header, const du_uchar* name, du_uint32 value, du_bool replace);

/**
 *  Sets a HTTP header field to the given header.
 *  @param[in,out] header pointer to the du_str_array structure.
 *  @param[in] name header name string.
 *  @param[in] value header value(du_uint64 data).
 *  @param[in] replace set true if you replace the old name header line
 *     previously contained in the header to new value.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark "name: valueCR+LF" line is added or replaced in header string array.
 *           header is a pointer to a du_str_array initialized by
 *           the <b>du_str_array_init</b> function.
 */
extern du_bool du_http_header_set_uint64(du_str_array* header, const du_uchar* name, du_uint64 value, du_bool replace);

/**
 *  Sets an RFC1123 Date formatted string to the given header.
 *  @param[in,out] header pointer to the du_str_array structure.
 *  @param[in] name header name string.
 *  @param[in] value header value(du_time data).
 *  @param[in] replace set true if you replace the old name header line
 *     previously contained in the header to new value.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark "name: valueCR+LF" line is added or replaced in header string array.
 *          Here value is a string of the form defined in rfc1123 converted from a
 *          value data .
 *          header is a pointer to a du_str_array initialized by
 *          the <b>du_str_array_init</b> function.
 *  @see du_rfc1123_date.h
 */
extern du_bool du_http_header_set_rfc1123_date(du_str_array* header, const du_uchar* name, du_time value, du_bool replace);

/**
 *  Sets a SOAP man header field to the given header.
 *  @param[in,out] header pointer to the du_str_array structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark header is a pointer to a du_str_array initialized by
 *       the <b>du_str_array_init</b> function.
 */
extern du_bool du_http_header_set_soap_man(du_str_array* header);

/**
 *  Sets a content type header field 'content-type: text/xml; charset=\"utf-8\"' for XML data.
 *  @param[in,out] header pointer to the du_str_array structure.
 *  @param[in] replace set true if you replace the old content-type header line
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark "content-type: text/xml; charset=\"utf-8\"CR+LF" line is added or replaced in
 *        header string array.
 *       header is a pointer to a du_str_array initialized by
 *       the <b>du_str_array_init</b> function.
 */
extern du_bool du_http_header_set_content_type_xml(du_str_array* header, du_bool replace);

/**
 *  Sets a content type header field 'content-type: text/htm' for HTML data.
 *  @param[in,out] header pointer to the du_str_array structure.
 *  @param[in] replace set true if you replace the old content-type header line
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark "content-type: text/htmlCR+LF" line is added or replaced in
 *        header string array.
 *       header is a pointer to a du_str_array initialized by
 *       the <b>du_str_array_init</b> function.
 */
extern du_bool du_http_header_set_content_type_html(du_str_array* header, du_bool replace);

/**
 *  Sets a timeout header field.
 *  @param[in,out] header pointer to the du_str_array structure.
 *  @param[in] timeout timetout value in second. If timeout value is less than zero,
 *   it means timeout is infinite.
 *  @param[in] replace set true if you replace the old TIMEOUT header line
 *  @remark "TIMEOUT: Second-timeout" or "TIMEOUT: Second-infinite" line is added or replaced in
 *        header string array.
 */
extern du_bool du_http_header_set_timeout(du_str_array* header, du_int32 timeout, du_bool replace);

/**
 *  Sets common response header fields (date and server).
 *  @param[in,out] header pointer to the du_str_array structure.
 *  @param[in] server_name server name string to set in server header.
 *  @param[in] is_persistent_connection set false if you want to add the connection header
 *              with close value in header, otherwise set true.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark Adds the following lines in header.<br>
 *   "server: server_nameCR+LF" <br>
 *   "date: current date valueCR+LF" <br>
 *  and if is_persistent_connection is set false, adds the following line
 *   in header.<br>
 *   "connection: closeCR+LF" <br>
 *  header is a pointer to a du_str_array initialized by
 *  the <b>du_str_array_init</b> function.
 */
extern du_bool du_http_header_set_common_response_header(du_str_array* header, const du_uchar* server_name, du_bool is_persistent_connection);

/**
 *  Sets common SOAP response header fields (server, date, ext, and content type).
 *  @param[in,out] header pointer to the du_str_array structure.
 *  @param[in] server_name server name string to set in server header.
 *  @param[in] is_persistent_connection set false if you want to add the connection header
 *              with close value in header, otherwise set true.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark Adds the following lines in header.<br>
 *   "server: server_nameCR+LF" <br>
 *   "date: current date valueCR+LF" <br>
 *   "ext: CR+LF" <br>
 *   "content-type: text/xml;charset=\"utf-8\"CR+LF" <br>
 *  and if is_persistent_connection is set false, adds the following line
 *   in header.<br>
 *   "connection: closeCR+LF" <br>
 *  header is a pointer to a du_str_array initialized by
 *  the <b>du_str_array_init</b> function.
 */
extern du_bool du_http_header_set_common_soap_response_header(du_str_array* header, const du_uchar* server_name, du_bool is_persistent_connection);

/**
 *  Initializes the HTTP range header field.
 *  @param[out] field pointer to the du_uchar_array structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark field is a pointer to a du_uchar_array initialized by
 *  the <b>du_uchar_array_init</b> function.
 */
extern du_bool du_http_header_range_init(du_uchar_array* field);

/**
 *  Initializes the HTTP content range header field.
 *  @param[out] field pointer to the du_uchar_array structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark field is a pointer to a du_uchar_array initialized by
 *  the <b>du_uchar_array_init</b> function.
 */
extern du_bool du_http_header_content_range_init(du_uchar_array* field);

/**
 *  Sets a start position for the range header field.
 *  @param[out] field pointer to the du_uchar_array structure.
 *  @param[in] pos value of the byte-offset of the first byte in a range.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark field is a pointer to a du_uchar_array initialized by
 *  the <b>du_uchar_array_init</b> and <b>du_http_header_range_init</b>.
 */
extern du_bool du_http_header_range_start_pos(du_uchar_array* field, du_uint64 pos);

/**
 *  Sets an end position for the range header field.
 *  @param[out] field pointer to the du_uchar_array structure.
 *  @param[in] pos value of the byte-offset of the last byte in a range.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark field is a pointer to a du_uchar_array initialized by
 *  the <b>du_uchar_array_init</b> and <b>du_http_header_range_init</b>.
 */
extern du_bool du_http_header_range_end_pos(du_uchar_array* field, du_uint64 pos);

/**
 *  Sets a length value for the range header field.
 *  @param[out] field pointer to the du_uchar_array structure.
 *  @param[in] length value.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark field is a pointer to a du_uchar_array initialized by
 *  the <b>du_uchar_array_init</b> and <b>du_http_header_range_init</b>.
 */
extern du_bool du_http_header_range_length(du_uchar_array* field, du_uint64 length);

/**
 *  Sets an unknown_length value ("*") for the range header field.
 *  @param[out] field pointer to the du_uchar_array structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark field is a pointer to a du_uchar_array initialized by
 *  the <b>du_uchar_array_init</b> and <b>du_http_header_range_init</b>.
 */
extern du_bool du_http_header_range_unknown_length(du_uchar_array* field);

/**
 *  Reads HTTP header from a socket input stream.
 *  @param[out] header pointer to the du_str_array structure to store HTTP header data.
 *  @param[in] sb pointer to the du_socket_input_buffer structure for reading data .
 *  @param[in,out] context pointer to the du_uchar_array structure for temporary buffer.
 *  @param[out] completed true if reading of the HTTP header field completes, otherwise false.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark header is a pointer to a du_str_array initialized by
 *  the <b>du_str_array_init</b> function.
 *  context is a pointer to a du_uchar_array initialized by
 *  the <b>du_uchar_array_init</b> function.
 */
extern du_bool du_http_header_read_socket(du_str_array* header, du_socket_input_buffer* sb, du_uchar_array* context, du_bool* completed);


/**
 *  Reads HTTP header from a string data.
 *  @param[out] header pointer to the du_str_array structure to store HTTP header data.
 *  @param[in] sb pointer to the du_str_input_buffer structure for reading data .
 *  @param[in,out] context pointer to the du_uchar_array structure for temporary buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark header is a pointer to a du_str_array initialized by
 *  the <b>du_str_array_init</b> function.
 *  context is a pointer to a du_uchar_array initialized by
 *  the <b>du_uchar_array_init</b> function.
 */
extern du_bool du_http_header_read_str(du_str_array* header, du_str_input_buffer* sb, du_uchar_array* context);

/**
 *  Initializes a du_http_chunk structure.
 *  @param[out] chunk  pointer to the du_http_chunk structure.
 */
extern void du_http_chunk_init(du_http_chunk* chunk);

/**
 *  Reads chunked transfer encoding data.
 *  @param[in] chunk pointer to the du_http_chunk structure using for reading data.
 *  @param[in] sb pointer to the du_str_input_buffer structure for reading data .
 *  @param[out] buf the buffer to which the chunked transfer coding data are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[out] nbytes pointer to the variable to receive the total number of bytes read
 *          into buf.
 *  @param[out] completed true if reading of the chunk data completes, otherwise false.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_chunk_read(du_http_chunk* chunk, du_socket_input_buffer* sb, du_uint8* buf, du_uint32 len, du_uint32* nbytes, du_bool* completed);

/**
 *  Returns a HTTP DELETE method string.
 *  This DELETE method string is "DELETE".
 *  @return  a HTTP DELETE method string.
 */
extern const du_uchar* du_http_method_delete(void);

/**
 *  Returns a HTTP GET method string.
 *  This GET method string is "GET".
 *  @return  a HTTP GET method string.
 */
extern const du_uchar* du_http_method_get(void);

/**
 *  Returns a HTTP HEAD method string.
 *  This HEAD method string is "HEAD".
 *  @return  a HTTP HEAD method string.
 */
extern const du_uchar* du_http_method_head(void);

/**
 *  Returns a M-POST method string.
 *  This M-POST method string is "M-POST".
 *  @return  a M-POST method string.
 */
extern const du_uchar* du_http_method_m_post(void);

/**
 *  Returns a M-SEARCH method string.
 *  This M-SEARCH method string is "M-SEARCH".
 *  @return  a M-SEARCH method string.
 */
extern const du_uchar* du_http_method_m_search(void);

/**
 *  Returns a HTTP NOTIFY method string.
 *  This NOTIFY method string is "NOTIFY".
 *  @return  a HTTP NOTIFY method string.
 */
extern const du_uchar* du_http_method_notify(void);

/**
 *  Returns a HTTP OPTIONS method string.
 *  This OPTIONS method string is "OPTIONS".
 *  @return  a HTTP OPTIONS method string.
 */
extern const du_uchar* du_http_method_options(void);

/**
 *  Returns a HTTP POST method string.
 *  This POST method string is "POST".
 *  @return  a HTTP POST method string.
 */
extern const du_uchar* du_http_method_post(void);

/**
 *  Returns a HTTP PUT method string.
 *  This PUT method string is "PUT".
 *  @return  a HTTP PUT method string.
 */
extern const du_uchar* du_http_method_put(void);

/**
 *  Returns a SUBSCRIBE method string.
 *  This SUBSCRIBE method string is "SUBSCRIBE".
 *  @return  a SUBSCRIBE method string.
 */
extern const du_uchar* du_http_method_subscribe(void);

/**
 *  Returns a TRACE method string.
 *  This TRACE method string is "TRACE".
 *  @return  a TRACE method string.
 */
extern const du_uchar* du_http_method_trace(void);

/**
 *  Returns a UNSUBSCRIBE method string.
 *  This UNSUBSCRIBE method string is "UNSUBSCRIBE".
 *  @return  a UNSUBSCRIBE method string.
 */
extern const du_uchar* du_http_method_unsubscribe(void);

/**
 *  Returns a Accept-Language HTTP header field string.
 *  This Accept-Language header field string is "Accept-Language".
 *  @return  a Accept-Language HTTP header field string.
 */
extern const du_uchar* du_http_header_accept_language(void);

/**
 *  Returns a Cache-Control HTTP header field string.
 *  This Cache-Control header field string is "Cache-Control".
 *  @return  a Cache-Control HTTP header field string.
 */
extern const du_uchar* du_http_header_cache_control(void);

/**
 *  Returns a Connection HTTP header field string.
 *  This Connection header field string is "Connection".
 *  @return  a Connection HTTP header field string.
 */
extern const du_uchar* du_http_header_connection(void);

/**
 *  Returns a Content-Length HTTP header field string.
 *  This Content-Length header field string is "Content-Length".
 *  @return  a Content-Length HTTP header field string.
 */
extern const du_uchar* du_http_header_content_length(void);

/**
 *  Returns a Content-Range HTTP header field string.
 *  This Content-Range header field string is "Content-Range".
 *  @return  a Content-Range HTTP header field string.
 */
extern const du_uchar* du_http_header_content_range(void);

/**
 *  Returns a Content-Type HTTP header field string.
 *  This Content-Type header field string is "Content-Type".
 *  @return  a Content-Type HTTP header field string.
 */
extern const du_uchar* du_http_header_content_type(void);

/**
 *  Returns a Date HTTP header field string.
 *  This Date header field string is "Date".
 *  @return  a Date HTTP header field string.
 */
extern const du_uchar* du_http_header_date(void);

/**
 *  Returns a Etag HTTP header field string.
 *  This Etag header field string is "Etag".
 *  @return  a Etag HTTP header field string.
 */
extern const du_uchar* du_http_header_etag(void);

/**
 *  Returns a Expect HTTP header field string.
 *  This Expect header field string is "Expect".
 *  @return  a Expect HTTP header field string.
 */
extern const du_uchar* du_http_header_expect(void);

/**
 *  Returns a EXT HTTP header field string.
 *  This EXT header field string is "EXT".
 *  @return  a EXT HTTP header field string.
 */
extern const du_uchar* du_http_header_ext(void);

/**
 *  Returns a Host HTTP header field string.
 *  This Host header field string is "Host".
 *  @return  a Host HTTP header field string.
 */
extern const du_uchar* du_http_header_host(void);

/**
 *  Returns a If-Match HTTP header field string.
 *  This If-Match header field string is "If-Match".
 *  @return  a If-Match HTTP header field string.
 */
extern const du_uchar* du_http_header_if_match(void);

/**
 *  Returns a If-None-Match HTTP header field string.
 *  This If-None-Match header field string is "If-None-Match".
 *  @return  a If-None-Match HTTP header field string.
 */
extern const du_uchar* du_http_header_if_none_match(void);

/**
 *  Returns a If-Range HTTP header field string.
 *  This If-Range header field string is "If-Range".
 *  @return  a If-Range HTTP header field string.
 */
extern const du_uchar* du_http_header_if_range(void);

/**
 *  Returns a If-Unmodified-Since HTTP header field string.
 *  This If-Unmodified-Since header field string is "If-Unmodified-Since".
 *  @return  a If-Unmodified-Since HTTP header field string.
 */
extern const du_uchar* du_http_header_if_unmodified_since(void);

/**
 *  Returns a If-Modified-Since HTTP header field string.
 *  This If-Modified-Since header field string is "If-Modified-Since".
 *  @return  a If-Modified-Since HTTP header field string.
 */
extern const du_uchar* du_http_header_if_modified_since(void);

/**
 *  Returns a Last-Modified HTTP header field string.
 *  This Last-Modified header field string is "Last-Modified".
 *  @return  a Last-Modified HTTP header field string.
 */
extern const du_uchar* du_http_header_last_modified(void);

/**
 *  Returns a Location HTTP header field string.
 *  This Location header field string is "Location".
 *  @return  a Location HTTP header field string.
 */
extern const du_uchar* du_http_header_location(void);

/**
 *  Returns a MX HTTP header field string.
 *  This MX header field string is "MX".
 *  @return  a MX HTTP header field string.
 */
extern const du_uchar* du_http_header_mx(void);

/**
 *  Returns a MAN ( mandatory) HTTP header field string.
 *  This MAN header field string is "MAN".
 *  @return  a MAN ( mandatory) HTTP header field string.
 */
extern const du_uchar* du_http_header_man(void);

/**
 *  Returns a NT header field string.
 *  This NT header field string is "NT".
 *  @return  a NT header field string.
 */
extern const du_uchar* du_http_header_nt(void);

/**
 *  Returns a NTS header field string.
 *  This NTS header field string is "NTS".
 *  @return  a NTS header field string.
 */
extern const du_uchar* du_http_header_nts(void);

/**
 *  Returns a Pragma header field string.
 *  This Pragma header field string is "Pragma".
 *  @return  a Pragma header field string.
 */
extern const du_uchar* du_http_header_pragma(void);

/**
 *  Returns a range HEADER field string.
 *  This SERVER header field string is "Range".
 *  @return  a Range header field string.
 */
extern const du_uchar* du_http_header_range(void);

/**
 *  Returns a Server header field string.
 *  This Server header field string is "Server".
 *  @return  a Server header field string.
 */
extern const du_uchar* du_http_header_server(void);

/**
 *  Returns a SOAPACTION header field string.
 *  This SOAPACTION header field string is "SOAPACTION".
 *  @return  a SOAPACTION header field string.
 */
extern const du_uchar* du_http_header_soapaction(void);

/**
 *  Returns a ST header field string.
 *  This ST header field string is "ST".
 *  @return  a ST header field string.
 */
extern const du_uchar* du_http_header_st(void);

/**
 *  Returns a Transfer-Encoding header field string.
 *  This Transfer-Encoding header field string is "Transfer-Encoding".
 *  @return  a Transfer-Encoding header field string.
 */
extern const du_uchar* du_http_header_transfer_encoding(void);

/**
 *  Returns a USN header field string.
 *  This USN header field string is "USN".
 *  @return  a USN header field string.
 */
extern const du_uchar* du_http_header_usn(void);

/**
 *  Returns a Vary header field string.
 *  This Vary header field string is "varY".
 *  @return  a Vary header field string.
 */
extern const du_uchar* du_http_header_vary(void);

/**
 *  Returns a CALLBACK header field string.
 *  This CALLBACK header field string is "CALLBACK".
 *  @return  a CALLBACK header field string.
 */
extern const du_uchar* du_http_header_callback(void);

/**
 *  Returns SID( subscription identifier ) header field string.
 *  This SID header field string is "SID".
 *  @return  sid HEADER field string.
 */
extern const du_uchar* du_http_header_sid(void);

/**
 *  Returns a TIMEOUT header field string.
 *  This TIMEOUT header field string is "TIMEOUT".
 *  @return  timeout HEADER field string.
 */
extern const du_uchar* du_http_header_timeout(void);

/**
 *  Returns a SEQ header field string.
 *  This SEQ header field string is "SEQ".
 *  @return  seq HEADER field string.
 */
extern const du_uchar* du_http_header_seq(void);

/**
 *  Returns a User-Agent header field string.
 *  This User-Agent header field string is "User-Agent".
 *  @return  User-Agent header field string.
 */
extern const du_uchar* du_http_header_user_agent(void);

/**
 *  Returns a Content-MD5 header field string.
 *  This Content-MD5 header field string is "Content-MD5".
 *  @return  Content-MD5 header field string.
 */
extern const du_uchar* du_http_header_content_md5(void);

/**
 *  Returns a Content-Language header field string.
 *  This Content-Language header field string is "Content-Language".
 *  @return  a Content-Language header field string.
 */
extern const du_uchar* du_http_header_content_language(void);

/**
 *  Returns a Content-Location header field string.
 *  This Content-Location header field string is "Content-Location".
 *  @return  a Content-Location header field string.
 */
extern const du_uchar* du_http_header_content_location(void);

/**
 *  Returns a Content-Encoding header field string.
 *  This Content-Encoding header field string is "Content-Encoding".
 *  @return a Content-Encoding header field string.
 */
extern const du_uchar* du_http_header_content_encoding(void);

/**
 *  Returns a ": " string.
 *  @return  ": " string.
 */
extern const du_uchar* du_http_header_colon_sp(void);

/**
 *  Returns a HTTP status code which meaning is "Continue".
 *  @return  "100" string.
 */
extern const du_uchar* du_http_status_continue(void);

/**
 *  Returns a HTTP status code which meaning is "Switching Protocols".
 *  @return  "101" string.
 */
extern const du_uchar* du_http_status_switching_protocols(void);

/**
 *  Returns a HTTP status code which meaning is "OK".
 *  @return  "200" string.
 */
extern const du_uchar* du_http_status_ok(void);

/**
 *  Returns a HTTP status code which meaning is "Created".
 *  @return  "201" string.
 */
extern const du_uchar* du_http_status_created(void);

/**
 *  Returns a HTTP status code which meaning is "Accepted".
 *  @return  "202" string.
 */
extern const du_uchar* du_http_status_accepted(void);

/**
 *  Returns a HTTP status code which meaning is "Non-Authoritative Information".
 *  @return  "203" string.
 */
extern const du_uchar* du_http_status_non_authoritative_information(void);

/**
 *  Returns a HTTP status code which meaning is "No Content".
 *  @return  "204" string.
 */
extern const du_uchar* du_http_status_no_content(void);

/**
 *  Returns a HTTP status code which meaning is "Reset Content".
 *  @return  "205" string.
 */
extern const du_uchar* du_http_status_reset_content(void);

/**
 *  Returns a HTTP status code which meaning is "Partial Content".
 *  @return  "206" string.
 */
extern const du_uchar* du_http_status_partial_content(void);

/**
 *  Returns a HTTP status code which meaning is "Multiple Choices".
 *  @return  "300" string.
 */
extern const du_uchar* du_http_status_multiple_choices(void);

/**
 *  Returns a HTTP status code which meaning is "Moved Permanently".
 *  @return  "301" string.
 */
extern const du_uchar* du_http_status_moved_permanently(void);

/**
 *  Returns a HTTP status code which meaning is "Found".
 *  @return  "302" string.
 */
extern const du_uchar* du_http_status_found(void);

/**
 *  Returns a HTTP status code which meaning is "See Other".
 *  @return  "303" string.
 */
extern const du_uchar* du_http_status_see_other(void);

/**
 *  Returns a HTTP status code which meaning is "Not Modified".
 *  @return  "304" string.
 */
extern const du_uchar* du_http_status_not_modified(void);

/**
 *  Returns a HTTP status code which meaning is "Use Proxy".
 *  @return  "305" string.
 */
extern const du_uchar* du_http_status_use_proxy(void);

/**
 *  Returns a HTTP status code which meaning is "Temporary Redirect".
 *  @return  "307" string.
 */
extern const du_uchar* du_http_status_temporary_redirect(void);

/**
 *  Returns a HTTP status code which meaning is "Bad Request".
 *  @return  "400" string.
 */
extern const du_uchar* du_http_status_bad_request(void);

/**
 *  Returns a HTTP status code which meaning is "Unauthorized".
 *  @return  "401" string.
 */
extern const du_uchar* du_http_status_unauthorized(void);

/**
 *  Returns a HTTP status code which meaning is "Payment Required".
 *  @return  "402" string.
 */
extern const du_uchar* du_http_status_payment_required(void);

/**
 *  Returns a HTTP status code which meaning is "Forbidden".
 *  @return  "403" string.
 */
extern const du_uchar* du_http_status_forbidden(void);

/**
 *  Returns a HTTP status code which meaning is "Not Found".
 *  @return  "404" string.
 */
extern const du_uchar* du_http_status_not_found(void);

/**
 *  Returns a HTTP status code which meaning is "Method Not Allowed".
 *  @return  "405" string.
 */
extern const du_uchar* du_http_status_method_not_allowed(void);

/**
 *  Returns a HTTP status code which meaning is "Not Acceptable".
 *  @return  "406" string.
 */
extern const du_uchar* du_http_status_not_acceptable(void);

/**
 *  Returns a HTTP status code which meaning is "Proxy Authentication Required".
 *  @return  "407" string.
 */
extern const du_uchar* du_http_status_proxy_authentication_required(void);

/**
 *  Returns a HTTP status code which meaning is "Request Timeout".
 *  @return  "408" string.
 */
extern const du_uchar* du_http_status_request_timeout(void);

/**
 *  Returns a HTTP status code which meaning is "Conflict".
 *  @return  "409" string.
 */
extern const du_uchar* du_http_status_conflict(void);

/**
 *  Returns a HTTP status code which meaning is "Gone".
 *  @return  "410" string.
 */
extern const du_uchar* du_http_status_gone(void);

/**
 *  Returns a HTTP status code which meaning is "Length Required".
 *  @return  "411" string.
 */
extern const du_uchar* du_http_status_length_required(void);

/**
 *  Returns a HTTP status code which meaning is "Precondition Failed".
 *  @return  "412" string.
 */
extern const du_uchar* du_http_status_precondition_failed(void);

/**
 *  Returns a HTTP status code which meaning is "Request Entity Too Large".
 *  @return  "413" string.
 */
extern const du_uchar* du_http_status_request_entity_too_large(void);

/**
 *  Returns a HTTP status code which meaning is "Request-URI Too Long".
 *  @return  "414" string.
 */
extern const du_uchar* du_http_status_request_uri_too_long(void);

/**
 *  Returns a HTTP status code which meaning is "Unsupported Media Type".
 *  @return  "415" string.
 */
extern const du_uchar* du_http_status_unsupported_media_type(void);

/**
 *  Returns a HTTP status code which meaning is "Requested Range Not Suitable".
 *  @return  "416" string.
 */
extern const du_uchar* du_http_status_requested_range_not_satisfiable(void);

/**
 *  Returns a HTTP status code which meaning is "Expectation Failed".
 *  @return  "417" string.
 */
extern const du_uchar* du_http_status_expectation_failed(void);

/**
 *  Returns a HTTP status code which meaning is "Internal Server Error".
 *  @return  "500" string.
 */
extern const du_uchar* du_http_status_internal_server_error(void);

/**
 *  Returns a HTTP status code which meaning is "Not Implemented".
 *  @return  "501" string.
 */
extern const du_uchar* du_http_status_not_implemented(void);

/**
 *  Returns a HTTP status code which meaning is "Bad Gateway".
 *  @return  "502" string.
 */
extern const du_uchar* du_http_status_bad_gateway(void);

/**
 *  Returns a HTTP status code which meaning is "Service Unavailable".
 *  @return  "503" string.
 */
extern const du_uchar* du_http_status_service_unavailable(void);

/**
 *  Returns a HTTP status code which meaning is "Gateway Timeout".
 *  @return  "504" string.
 */
extern const du_uchar* du_http_status_gateway_timeout(void);

/**
 *  Returns a HTTP status code which meaning is "HTTP Version Not Supported".
 *  @return  "505" string.
 */
extern const du_uchar* du_http_status_http_version_not_supported(void);

/**
 *  Checks whether status string starts with '1' character.
 *  @param[in] status  an HTTP status code string.
 *  @return  true if status string starts with '1', false otherwise.
 */
extern du_bool du_http_status_is_informational(const du_uchar* status);

/**
 *  Checks whether status string starts with '2' character.
 *  @param[in] status  an HTTP status code string.
 *  @return  true if status string starts with '2', false otherwise.
 */
extern du_bool du_http_status_is_successful(const du_uchar* status);

/**
 *  Checks whether status string starts with '3' character.
 *  @param[in] status  an HTTP status code string.
 *  @return  true if status string starts with '3', false otherwise.
 */
extern du_bool du_http_status_is_redirection(const du_uchar* status);

/**
 *  Checks whether status string starts with '4' character.
 *  @param[in] status  an HTTP status code string.
 *  @return  true if status string starts with '4', false otherwise.
 */
extern du_bool du_http_status_is_client_error(const du_uchar* status);

/**
 *  Checks whether status string starts with '5' character.
 *  @param[in] status  an HTTP status code string.
 *  @return  true if status string starts with '5', false otherwise.
 */
extern du_bool du_http_status_is_server_error(const du_uchar* status);

#ifdef __cplusplus
}
#endif

#endif
