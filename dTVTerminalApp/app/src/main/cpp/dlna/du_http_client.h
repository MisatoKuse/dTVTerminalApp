/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_http_client interface provides methods for sending/receiving
 *   HTTP message ( such as sending request message, receiving response message,
 *    getting status code ).
 *   Before using a du_http_client, use du_http_client_init() to
 *   initialize a du_http_client structure data.
 *    Use du_http_client_free() to free the region used by du_http_client.<br>
 *  The following is the typical sequence for using du_http_client.<br>
 *  1. du_http_client_init()<br>
 *  2. du_http_client_set_uri()<br>
 *  3. du_http_client_request()<br>
 *  4. du_http_client_read_body()<br>
 *  5. repeats 4. until all body data has been read.<br>
 *  6. du_http_client_disconnect()<br>
 *  7. repeats 2.-6., if necessary.<br>
 *  8. du_http_client_free()<br>
 *  The following sequence is the typical sequence for using pipelined du_http_client.<br>
 *  1. du_http_client_init()<br>
 *  2. du_http_client_set_uri()<br>
 *  3. du_http_client_connect()<br>
 *  4. du_http_client_write_header()<br>
 *  5. du_http_client_write_body(), if necessary.<br>
 *  6. repeats 4.-5.<br>
 *  7. du_http_client_read_header()<br>
 *  8. du_http_client_read_body()<br>
 *  9. repeats 8. until all body data has been read.<br>
 *  10. repeats 7.-9.<br>
 *  11. du_http_client_disconnect()<br>
 *  12. du_http_client_free()<br>
 *  If you use low level APIs instead of du_http_client_request(), you should handle the
 *  request/response sequences (such as redirecting the request) yourself.
 */

#ifndef DU_HTTP_CLIENT_H
#define DU_HTTP_CLIENT_H

#include "du_socket.h"
#include "du_http.h"
#include "du_uri.h"
#include "du_poll_os.h"
#include "du_ip_array.h"
#include "du_socket_input_buffer.h"
#include "du_socket_output_buffer.h"
#include "du_str_array.h"
#include "du_limits.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Enumeration of HTTP version.
 */
typedef enum {
    /**
     *   HTTP/1.0
     */
    DU_HTTP_CLIENT_HTTP_VERSION_1_0,

    /**
     *   HTTP/1.1
     */
    DU_HTTP_CLIENT_HTTP_VERSION_1_1,

} du_http_client_http_version;

/**
 *  This is used for a output_buffer_size parameter of du_http_client_init()
 *  for the purpose of disabling bufffering.
 */
#define DU_HTTP_CLIENT_OUTPUT_BUFFER_SIZE_ZERO (DU_UINT32_MAX)

/**
 *  Definition of du_http_client.
 */
typedef struct du_http_client {
    du_uri uri;
    du_http_client_http_version version;
    const du_uchar* proxy_host;
    du_uint16 proxy_port;
    du_ip_array ip_array;
    du_uint32 ip_index;
    du_socket socket;
    du_poll poll;
    du_uint8* in_buf;
    du_uint32 in_buf_size;
    du_socket_input_buffer in_sb;
    du_uint8* out_buf;
    du_uint32 out_buf_size;
    du_socket_output_buffer out_sb;
    du_uchar_array context;
    du_uint64 remains;
    du_http_chunk chunk;
    du_int state;
    du_uint32 max_following_redirects;
    du_uint32 socket_buf_size;
    du_bool disable_output_buffer;
    du_socket_error error;
    du_bool cancel_requested;
} du_http_client;

/**
 *  Initializes a du_http_client hc.
 *  @param[in] hc  pointer to the du_http_client structure.
 *  @param[in] input_buffer_size number of bytes of input buffer.
 *    If input_buffer_size is 0, default buffer size will be used.
 *    The default buffer size is 8192.
 *  @param[in] output_buffer_size number of bytes of output buffer.
 *    If output_buffer_size is 0, default buffer size will be used.
 *    The default buffer size is 1024.
 *    If output_buffer_size is DU_HTTP_CLIENT_OUTPUT_BUFFER_SIZE_ZERO,
 *    bufferling to output is disabled; it may increase transfer speed for POST method
 *    than using buffering where a large size content is posted to a HTTP server.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_init(du_http_client* hc, du_uint32 input_buffer_size, du_uint32 output_buffer_size);

/**
 *  Closes an existing socket if it's opened and frees all resources.
 *  @param[in] hc pointer to the du_http_client structure.
 */
extern void du_http_client_free(du_http_client* hc);

/**
 *  Sets a URI.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] uri URI string.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark returns false if the protocol of uri is not "http".
 *  @remark hc is a pointer to a du_http_client structure
 *  initialized by the <b>du_http_client_init</b> function.
 */
extern du_bool du_http_client_set_uri(du_http_client* hc, const du_uchar* uri);

/**
 *  Gets a URI.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @return pointer to a du_uri structure data of the URI information stored in hc.
 */
extern du_uri* du_http_client_get_uri(du_http_client* hc);

/**
 *  Sets HTTP version which is used when http client sends requests to the server.
 *  The default value is DU_HTTP_CLIENT_HTTP_VERSION_1_1.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] version HTTP version.
 */
extern void du_http_client_set_http_version(du_http_client* hc, du_http_client_http_version version);

extern du_http_client_http_version du_http_client_get_http_version(du_http_client* hc);

/**
 *  Sets the maximum number of following redirests.
 *  When servers respond 3xx, du_http_client usually follows the redirection.
 *  The default value is 10.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] max_count the maximum number of following redirests.
 */
extern void du_http_client_set_max_following_redirects(du_http_client* hc, du_uint32 max_count);

/**
 *  Sets a proxy information.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] proxy_host host name (IP address) of proxy.
 *  @param[in] proxy_port port number of proxy.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark hc is a pointer to a du_http_client structure
 *  initialized by the <b>du_http_client_init</b> function.
 */
extern du_bool du_http_client_set_proxy(du_http_client* hc, const du_uchar* proxy_host, du_uint16 proxy_port);

/**
 *  Gets a proxy host.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @return host name of the proxy stored in hc.
 */
extern const du_uchar* du_http_client_get_proxy_host(du_http_client* hc);

/**
 *  Gets a proxy port number.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @return port number of the proxy stored in hc.
 */
extern du_uint16 du_http_client_get_proxy_port(du_http_client* hc);

/**
 *  Sets socket handle. The socket must be already connected to the peer.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] socket socket handle to set.
 */
extern void du_http_client_set_socket(du_http_client* hc, du_socket socket);

/**
 *  Gets socket handle.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @return socket handle.
 */
extern du_socket du_http_client_get_socket(du_http_client* hc);

/**
 *  Gets poll handle.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @return poll handle.
 */
extern du_poll* du_http_client_get_poll(du_http_client* hc);

/**
 *  Sets socket buffer size. Socket buffer size is 65535 by default.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] size socket buffer size to set. Secifies 0 if you don't want to set socket buffer size expressly.
 */
extern void du_http_client_set_socket_buffer_size(du_http_client* hc, du_uint32 size);

/**
 *  Cancels the I/O processing on the socket.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_cancel(du_http_client* hc);

/**
 *  Creates a TCP socket with no-delay option and connects to a server or a proxy.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_connect_nonblock(du_http_client* hc);

/**
 *  Changes the state to a connection completed state.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @remark This function should be called after the completion of the connection
 *     requested by <b>du_http_client_connect_nonblock</b>.
 */
extern void du_http_client_connect_nonblock_completed(du_http_client* hc);

/**
 *  Creates a TCP socket and connects to a server or a proxy.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] timeout_ms the connection timeout.
 *  A negative value means infinite timeout.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_connect(du_http_client* hc, du_int32 timeout_ms);

/**
 *  Closes a socket it it's opened.
 *  @param[in] hc pointer to the du_http_client structure.
 */
extern void du_http_client_disconnect(du_http_client* hc);

/**
 *  Writes a request line and a header of a HTTP request message to the socket stream.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] method a method string of the request line ( like "GET", "POST" and so on ).
 *  @param[in] content_length value of the content-length HTTP header.
 *  @param[in] request_header pointer to a du_str_array stored HTTP header string array.
 *  @param[in] timeout_ms the length of time in milliseconds which will be waited before returning.
 *  A negative value means infinite timeout.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_write_header(du_http_client* hc, const du_uchar* method, du_uint32 content_length, const du_str_array* request_header, du_int32 timeout_ms);

/**
 *  Writes a request line and a header of a HTTP request message to the socket stream.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] method a method string of the request line ( like "GET", "POST" and so on ).
 *  @param[in] content_length value of the content-length HTTP header.
 *  @param[in] connection_close if true, "Connection: close" header will be added to request header.
 *  @param[in] request_header pointer to a du_str_array stored HTTP header string array.
 *  @param[in] timeout_ms the length of time in milliseconds which will be waited before returning.
 *  A negative value means infinite timeout.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_write_header2(du_http_client* hc, const du_uchar* method, du_uint32 content_length, du_bool connection_close, const du_str_array* request_header, du_int32 timeout_ms);

/**
 *  Reads a status line ( the first line of a response message ) from the socket stream.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[out] status pointer to the variable to receive the status code of the status line.
 *  @param[out] completed true if the status line has been read, otherwise false.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_read_status_line_nonblock(du_http_client* hc, du_uchar status[4], du_bool* completed);

/**
 *  Reads a HTTP header from the socket stream.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[out] response_header pointer to du_str_array structure to receive the HTTP headers of
 *    a response message.
 *  @param[in,out] context pointer to the du_uchar_array structure for temporary buffer.
 *  @param[out] completed true if all of the headers has been read, otherwise false.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark response_header is a pointer to a du_str_array structure
 *  initialized by the <b>du_str_array_init</b> function.
 */
extern du_bool du_http_client_read_header_nonblock(du_http_client* hc, du_str_array* response_header, du_uchar_array* context, du_bool* completed);

/**
 *  Reads a status code and a response header from the socket stream.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[out] status pointer to the variable to receive the status code of the status line.
 *  @param[out] response_header pointer to du_str_array structure to receive the HTTP headers of
 *    a response message.
 *  @param[in] timeout_ms the length of time in milliseconds which will be waited for
 *    each data reading from the input buffer.
 *  A negative value means infinite timeout.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark response_header is a pointer to a du_str_array structure
 *  initialized by the <b>du_str_array_init</b> function.
 */
extern du_bool du_http_client_read_header(du_http_client* hc, du_uchar status[4], du_str_array* response_header, du_int32 timeout_ms);

/**
 *  Reads response data from the socket stream.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[out] buf the buffer to which the body data are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[out] nbytes pointer to the variable to receive the total number of bytes read
 *          into buf.
 *  @param[out] completed true if all of the body data of the response message
 *    has been read, otherwise false.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_read_body_nonblock(du_http_client* hc, du_uint8* buf, du_uint32 len, du_uint32* nbytes, du_bool* completed);

/**
 *  Tests if the HTTP stream is the end of stream.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @return  true if the response body is end of stream.
 *           false otherwise.
 */
extern du_bool du_http_client_is_response_body_eos(du_http_client* hc);

/**
 *  Reads response data from the socket stream.
 *  It supports following formats: Content-Length based fixed length data,
 *  chunked transfer encoding basd variable length data, no Content-Length
 *  (and close connection) based variable length data.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[out] buf the buffer to which the body data are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[in] timeout_ms the length of time in milliseconds which will be waited for
 *    each data reading from the input buffer.
 *  A negative value means infinite timeout.
 *  @param[out] nbytes pointer to the variable to receive the total number of bytes read
 *          into buf.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_read_body(du_http_client* hc, du_uint8* buf, du_uint32 len, du_int32 timeout_ms, du_uint32* nbytes);

/**
 *  Writes request message body data to the socket stream.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] buf pointer to the body data to be written.
 *  @param[in] len number of byte to write.
 *  @param[in] timeout_ms the length of time in milliseconds which will be waited for
 *    each data writing to the output buffer.
 *  A negative value means infinite timeout.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_write_body(du_http_client* hc, const du_uint8* buf, du_uint32 len, du_int32 timeout_ms);

/**
 *  Writes chunked transfer encoding data to the socket stream.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] buf pointer to the chunked transfer coding data to be written.
 *  @param[in] len number of byte to write.
 *  @param[in] timeout_ms the length of time in milliseconds which will be waited for
 *    each data writing to the output buffer.
 *  A negative value means infinite timeout.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_write_chunk_body(du_http_client* hc, du_uint8* buf, du_uint32 len, du_int32 timeout_ms);

/**
 *  Sends a request message and receives a response message.
 *  @param[in] hc pointer to the du_http_client structure.
 *  @param[in] method a method string of the HTTP request line ( like "GET", "POST" and so on ).
 *  @param[in] request_header pointer to a du_str_array stored HTTP header string array.
 *    NULL if you don't want to use specific header fields.
 *  @param[in] request_body pointer to the request_body data to be send.
 *  @param[out] status pointer to the variable to receive the status code of the response message.
 *  @param[out] response_header pointer to du_str_array structure to receive the HTTP headers of
 *    a response message.
 *  @param[in] connect_timeout_ms the length of time in milliseconds which will be waited for
 *    connecting to a server or a proxy specified in hc.
 *  A negative value means infinite timeout.
 *  @param[in] write_timeout_ms the length of time in milliseconds which will be waited for
 *    each data writing to the output buffer.
 *  A negative value means infinite timeout.
 *  @param[in] read_timeout_ms the length of time in milliseconds which will be waited for
 *    each data reading from the input buffer.
 *  A negative value means infinite timeout.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_client_request(du_http_client* hc, const du_uchar* method, du_str_array* request_header, const du_uchar* request_body, du_uchar status[4], du_str_array* response_header, du_int32 connect_timeout_ms, du_int32 write_timeout_ms, du_int32 read_timeout_ms);

/**
 *  Gets the error status of the last operation.
 *  @return  the error status for the last operation that failed.
*/
extern du_socket_error du_http_client_get_last_error(du_http_client* hc);

#ifdef __cplusplus
}
#endif

#endif
