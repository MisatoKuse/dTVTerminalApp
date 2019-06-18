/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_http_output_buffer interface provides methods for buffering
 *   the HTTP output message to a socket stream.
 *   If you set chunked_encoding flag, you can send the data as chunked encoding data.
 *   Before using a du_http_output_buffer, use <b>du_http_output_buffer_init</b>
 *   to establish the pointer of buffer data, buffer size, du_socket referencing a socket,
 *   du_poll, write timeout value, and chunked_encoding flag stored in the
 *   du_http_output_buffer structure.
 */

#ifndef DU_HTTP_OUTPUT_BUFFER_H
#define DU_HTTP_OUTPUT_BUFFER_H

#include <du_socket.h>
#include <du_output_buffer.h>
#include <du_poll.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Structure of du_http_output_buffer.
 */
typedef struct du_http_output_buffer {
    du_output_buffer b;
    du_poll* p;
    du_socket s;
    du_int32 write_timeout_ms;
    du_bool is_chunked_encoding;
    du_bool is_eos;
} du_http_output_buffer;

/**
 *  Initializes a du_http_output_buffer.
 *  @param[out] hb  pointer to the du_http_output_buffer structure.
 *  @param[in] buf buffer area used to cache data.
 *  @param[in] len number of bytes of buf.
 *  @param[in] s du_socket data referencing a socket.
 *  @param[in] p pointer to du_poll data for waiting for some events.
 *  @param[in] write_timeout_ms the time-out for write, in milliseconds.
 *  A negative value means infinite timeout.
 *  @param[in] is_chunked_encoding flag of the chunked encoding whether the data in buffer
 *     encodes chunked encoding or not.
 *  @remark  If you set chunked_encoding flag as true, the data in buffer will be sent as
 *  chunked encoding data.
 */
extern void du_http_output_buffer_init(du_http_output_buffer* hb, du_uint8* buf, du_uint32 len, du_socket s, du_poll* p, du_int32 write_timeout_ms, du_bool is_chunked_encoding);

/**
 *  Gets a socket handle.
 *  and stores it in the location given by s.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @param[out] s pointer to a du_socket for receiving data.
 */
extern void du_http_output_buffer_get_socket(du_http_output_buffer* hb, du_socket* s);

/**
 *  Gets a poll information.
 *  and stores it in the location given by p.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @param[out] p pointer to the storage location for the pointer of du_poll.
 */
extern void du_http_output_buffer_get_poll(du_http_output_buffer* hb, du_poll** p);

/**
 *  Sets the time-out value, in milliseconds.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @param[in] msec  the time-out value for write, in milliseconds.
 *  A negative value means infinite timeout.
 */
extern void du_http_output_buffer_set_write_timeout(du_http_output_buffer* hb, du_int32 msec);

/**
 *  Gets the time-out value.
 *  stores it in the location given by msec.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @param[out] msec  pointer to the variable to receive the time-out interval value.
 *  A negative value means infinite timeout.
 */
extern void du_http_output_buffer_get_write_timeout(du_http_output_buffer* hb, du_int32* msec);

/**
 *  Sets the chunked_encoding flag value.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @param[in] is_chunked_encoding true if chunked_encoding is enabled, otherwise false.
 *  @remark  If you set chunked_encoding flag to true, the data in buffer will be  sent as
 *  chunked encoding data.
 */
extern void du_http_output_buffer_set_chunked_encoding(du_http_output_buffer* hb, du_bool is_chunked_encoding);

/**
 *  Gets the chunked_encoding flag value.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @param[out] is_chunked_encoding pointer to the variable to receive the chunked_encoding flag
 *  value.
 */
extern void du_http_output_buffer_get_chunked_encoding(du_http_output_buffer* hb, du_bool* is_chunked_encoding);

/**
 *  Resets hb buffer, emptying the current hb buffer.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 */
extern void du_http_output_buffer_reset(du_http_output_buffer* hb);

/**
 *  Gets the byte length of the used area of buffer of sb.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @return byte length of the used area of the buffer of hb.
 */
#define du_http_output_buffer_used(hb) du_output_buffer_used(&(hb)->b)

/**
 *  Gets the byte length of the buffer area of hb.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @return byte length of the buffer area of hb.
 */
#define du_http_output_buffer_length(hb) du_output_buffer_length(&(hb)->b)

/**
 *  Flushes this output buffer. This forces any buffered data to be written out to
 *  the socket.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_output_buffer_flush(du_http_output_buffer* hb);

/**
 *  Writes data to a HTTP output stream.
 *  This function might not buffer the data but write it out directly when the data
 *  size is larger than the buffer size.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @param[in] buf the byte array from which to copy len bytes to the hb.
 *  @param[in] len the number of bytes to be written to hb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_output_buffer_write(du_http_output_buffer* hb, const du_uint8* buf, du_uint32 len);

/**
 *  Writes data to a file output stream.
 *  This function always buffers the data first. If the data size is larger than
 *  the buffer size, it flushs the buffer and then it buffers the remaining data.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @param[in] buf the byte array from which to copy len bytes to the hb.
 *  @param[in] len the number of bytes to be written to hb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_output_buffer_write_align(du_http_output_buffer* hb, const du_uint8* buf, du_uint32 len);

/**
 *  Writes a null-terminated string.
 *  This function might not buffer the data but write it out directly when the data
 *  size is larger than the buffer size.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @param[in] str the string which to copy to the hb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_output_buffer_writes(du_http_output_buffer* hb, const du_uchar* str);

/**
 *  Writes a string to the fb.
 *  This function always buffers the data first. If the data size is larger than
 *  the buffer size, it flushs the buffer and then it buffers the remaining data.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @param[in] str the string which to copy to the hb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_output_buffer_writes_align(du_http_output_buffer* hb, const du_uchar* str);

/**
 *  Sends and end of stream data for chunked encoding.
 *  @param[in] hb  pointer to the du_http_output_buffer structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_http_output_buffer_send_eos(du_http_output_buffer* hb);

/**
 *  Copies data from the specified input buffer to the specified output buffer.
 *  @param[in] from  pointer to the du_http_output_buffer structure.
 *  @param[out] to  pointer to the du_http_ouput_buffer structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark to is a du_http_ouput_buffer initialized by
 *  the <b>du_http_output_buffer_init</b> function.
 */
#define du_http_output_buffer_copy(from, to) du_output_buffer_copy(&(from)->b, &(to)->b)

#ifdef __cplusplus
}
#endif

#endif
