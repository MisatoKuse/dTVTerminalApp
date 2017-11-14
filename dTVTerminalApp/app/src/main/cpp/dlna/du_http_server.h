/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_http_server interface provides methods for sending/receiving
 *   HTTP message ( such as reading request headers, sending response message,
 *    getting a corresponding reason phrase of a status code ).
 *   Before using a du_http_server, use <b>du_http_server_init</b> to
 *   initialize a du_http_server structure data.
 *    Use <b>du_http_server_free</b> to free the region used by du_http_server.
 */

#ifndef DU_HTTP_SERVER_H
#define DU_HTTP_SERVER_H

#include "du_ip.h"
#include "du_socket.h"
#include "du_socket_input_buffer.h"
#include "du_socket_output_buffer.h"
#include "du_uri.h"
#include "du_http.h"
#include "du_time.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Definition of du_http_server.
 */
typedef struct du_http_server {
    du_ip local_ip;
    du_ip remote_ip;
    du_socket socket;
    du_poll poll;
    du_uchar in_buf[2048];
    du_uchar out_buf[256];
    du_socket_input_buffer in_sb;
    du_socket_output_buffer out_sb;
    du_uchar method[32];
    du_uri uri;
    du_uchar version[12];
    du_str_array header;
    du_uchar_array context;
    du_bool persistent_connection;
    du_uint64 remains;
    du_http_chunk chunk;
    du_int state;
    du_uint32 socket_buf_size;
} du_http_server;

/**
 *  Initializes a du_http_server hs.
 *  @param[out] hs  pointer to the du_http_server structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_init(du_http_server* hs);

/**
 *  Resets hs to reuse.
 *  @param[in] hs pointer to the du_http_server structure.
 */
extern void du_http_server_reset(du_http_server* hs);

/**
 *  Closes an existing socket if it's opened and frees all resources.
 *  @param[in] hs pointer to the du_http_server structure.
 */
extern void du_http_server_free(du_http_server* hs);

/**
 *  Sets a socket descriptor.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[in] socket socket.
 */
extern void du_http_server_set_socket(du_http_server* hs, du_socket socket);

/**
 *  Sets a socket descriptor with read/write timeout.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[in] socket socket.
 *  @param[in] read_timeout_ms read timeout in msec.
 *  @param[in] write_timeout_ms write timeout in msec.
 */
extern void du_http_server_set_socket2(du_http_server* hs, du_socket socket, du_int32 read_timeout_ms, du_int32 write_timeout_ms);

/**
 *  Gets a socket descriptor.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[out] socket socket.
 */
extern void du_http_server_get_socket(du_http_server* hs, du_socket* socket);

/**
 *  Gets a poll.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return the pointer of du_poll.
 */
extern du_poll* du_http_server_get_poll(du_http_server* hs);

/**
 *  Gets the local IP.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[out] local_ip local IP.
 */
extern void du_http_server_get_local_ip(du_http_server* hs, du_ip* local_ip);

/**
 *  Sets the remote IP.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[in] remote_ip remote IP.
 */
extern void du_http_server_set_remote_ip(du_http_server* hs, const du_ip* remote_ip);

/**
 *  Gets the remote IP.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[out] remote_ip remote IP.
 */
extern void du_http_server_get_remote_ip(du_http_server* hs, du_ip* remote_ip);

/**
 *  Gets the socket input buffer.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return pointer of du_socket_input_buffer.
 */
extern du_socket_input_buffer* du_http_server_get_socket_input_buffer(du_http_server* hs);

/**
 *  Gets the socket output buffer.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return pointer of du_socket_output_buffer.
 */
extern du_socket_output_buffer* du_http_server_get_socket_output_buffer(du_http_server* hs);

/**
 *  Gets the request method.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return a request method.
 */
extern const du_uchar* du_http_server_get_method(du_http_server* hs);

/**
 *  Gets the request URI.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return a request URI.
 */
extern const du_uri* du_http_server_get_uri(du_http_server* hs);

/**
 *  Gets the request HTTP version.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return a request HTTP version.
 */
extern const du_uchar* du_http_server_get_version(du_http_server* hs);

/**
 *  Gets the request header.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return a request header.
 */
extern const du_str_array* du_http_server_get_header(du_http_server* hs);

/**
 *  Sets socket buffer size. Socket buffer size is 65535 by default.
 *  @param[in] hs pointer to the du_http_server structure.
 *  @param[in] size socket buffer size to set. Secifies 0 if you don't want to set socket buffer size expressly.
 */
extern void du_http_server_set_socket_buffer_size(du_http_server* hs, du_uint32 size);

/**
 *  Checks whether HTTP version of request is 1.0.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return  true if HTTP version of request is 1.0.
 *           false if HTTP version of request is not 1.0.
 */
extern du_bool du_http_server_is_request_version_1_0(du_http_server* hs);

/**
 *  Reads a start line ( the first line of a request message ) with nonblocking manner.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[out] tmp_buf the temporary buffer.
 *  @param[in] tmp_buf_len temporary buffer size.
 *  @param[out] completed true if reading the start line data of the request
 *    message has been completed. Otherwise false.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_read_start_line_nonblock(du_http_server* hs, du_uchar* tmp_buf, du_uint32 tmp_buf_len, du_bool* completed);

/**
 *  Reads a HTTP request header from the socket input stream.
 *    in nonblocking manner.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[out] completed true if all of the header data of the request message
 *    has been read, otherwise false.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_read_header_nonblock(du_http_server* hs, du_bool* completed);

/**
 *  Reads a start line and a request header.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_read_header(du_http_server* hs);

/**
 *  Reads HTTP body of a request message from the socket input stream.
 *   in nonblocking manner.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[out] buf the buffer to which the body data are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[out] nbytes pointer to the variable to receive the total number of bytes read
 *          into buf.
 *  @param[out] completed true if all of the body data of the request message
 *    has been read, otherwise false.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_read_body_nonblock(du_http_server* hs, du_uint8* buf, du_uint32 len, du_uint32* nbytes, du_bool* completed);

/**
 *  Tests if HTTP input stream is the end of stream.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return  true if the request body is end of stream.
 *           false otherwise.
 */
extern du_bool du_http_server_is_request_body_eos(du_http_server* hs);

/**
 *  Returns a corresponding reason phrase of status code.
 *  @param[in] status  pointer to the variable of the status code.
 *  @param[in] default_reason a reason phrase to return or NULL.
 *  @return a corresponding reason phrase of status code
 *    if default_reason is NULL. default_reason
 *    if default_reason is not NULL.
 *    "Error" if there is not the corresponding reason phrase of status code and
 *    default_reason is NULL.
 */
extern const du_uchar* du_http_server_get_reason(const du_uchar status[4], const du_uchar* default_reason);

/**
 *  Writes a status line of response message to a socket output stream.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[in] status  pointer to the variable of the status code.
 *  @param[in] reason a reason phrase string.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_write_status_line(du_http_server* hs, const du_uchar status[4], const du_uchar* reason);

/**
 *  Writes a HTTP response header to a socket output stream.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[in] header  pointer to the HTTP header lines data.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_write_header(du_http_server* hs, const du_str_array* header);

/**
 *  Writes data to a socket output stream.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[in] data pointer to the data.
 *  @param[in] size the number of bytes to be written to the output buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_write(du_http_server* hs, const du_uint8* data, du_uint32 size);

/**
 *  Writes data to a socket output stream as a chunked encoding.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[in] data pointer to the data.
 *  @param[in] size the number of bytes to be written to the output buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_write_chunk(du_http_server* hs, const du_uint8* data, du_uint32 size);

/**
 *  Writes an end of stream for chunked encoding to a socket output stream.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_write_chunk_eos(du_http_server* hs);

/**
 *  Flushes the output buffer.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_flush_output(du_http_server* hs);

/**
 *  Writes file data to the socket output stream in nonblocking manner.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[in] f the file handle to the open file that this function sends.
 *  @param[in] offset du_int64 data holding the input file pointer position from which
 *      this function will start reading data.
 *  @param[in] size total number of bytes to send.
 *  @param[out] nbytes the total number of bytes sent.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_send_file_nonblock(du_http_server* hs, du_file f, du_uint64 offset, du_uint64 size, du_uint64* nbytes);

/**
 *  Writes file data to the socket output stream.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[in] f the file handle to the open file that this function sends.
 *  @param[in] offset du_int64 data holding the input file pointer position from which
 *      this function will start reading data.
 *  @param[in] size total number of bytes to send.
 *  @param[out] nbytes the total number of bytes sent.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_send_file(du_http_server* hs, du_file f, du_uint64 offset, du_uint64 size, du_uint64* nbytes);

/**
 *  Writes a response message which containes the error reason phrase.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[in] status  pointer to the variable of the status code.
 *  @param[in] reason a reason phrase string.
 *  @param[in] header pointer to the HTTP header lines.
 *  @param[in] server_name server name string to set in server header.
 *  @param[in] is_persistent_connection set false if Connection: close header is needed.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_write_error_response(du_http_server* hs, const du_uchar status[4], const du_uchar* reason, du_str_array* header, const du_uchar* server_name, du_bool is_persistent_connection);

/**
 *  Writes a response message which containes the error reason phrase.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @param[in] status  pointer to the variable of the status code.
 *  @param[in] reason a reason phrase string.
 *  @param[in] server_name server name string to set in server header.
 *  @param[in] is_persistent_connection set false if Connection: close header is needed.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_server_write_error_response2(du_http_server* hs, const du_uchar status[4], const du_uchar* reason, const du_uchar* server_name, du_bool is_persistent_connection);

/**
 *  Checks whether a request message requires a persistent connection.
 *  @param[in] hs  pointer to the du_http_server structure.
 *  @return  true if the request message is available, false otherwise.
 *  @remark Returns false if the request message contains a Connection HTTP header
 *  with the value close.
 */
extern du_bool du_http_server_is_persistent_connection_request(du_http_server* hs);

#ifdef __cplusplus
}
#endif

#endif
