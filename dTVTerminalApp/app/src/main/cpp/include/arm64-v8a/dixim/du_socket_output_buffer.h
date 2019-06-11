/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_socket_output_buffer interface provides methods for buffering the output
 *   to a socket stream.
 *   Before using a du_socket_output_buffer, use <b>du_socket_output_buffer_init</b>
 *   to establish the pointer of buffer data, buffer size, du_socket referencing a socket,
 *   du_poll, and write timeout value stored in the du_socket_output_buffer
 *   structure.
 */

#ifndef DU_SOCKET_OUTPUT_BUFFER_H
#define DU_SOCKET_OUTPUT_BUFFER_H

#include <du_socket_os.h>
#include <du_poll_os.h>
#include <du_output_buffer.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Structure of du_socket_output_buffer.
 */
typedef struct du_socket_output_buffer {
    du_output_buffer b;
    du_poll* p;
    du_socket s;
    du_int32 write_timeout_ms;
    du_bool canceled;
    du_bool timed_out;
} du_socket_output_buffer;

/**
 *  Initializes a du_socket_output_buffer.
 *  @param[out] sb  pointer to the du_socket_output_buffer structure.
 *  @param[in] buf buffer area used to cache data.
 *  @param[in] len number of bytes of buf.
 *  @param[in] s du_socket data referencing a socket.
 *  @param[in] p pointer to du_poll data for waiting for some events.
 *  @param[in] write_timeout_ms the time-out for write, in milliseconds.
 *  A negative value means infinite timeout.
 */
extern void du_socket_output_buffer_init(du_socket_output_buffer* sb, du_uint8* buf, du_uint32 len, du_socket s, du_poll* p, du_int32 write_timeout_ms);

/**
 *  Gets a socket handle.
 *  and stores it in the location given by s.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 *  @param[out] s pointer to a du_socket for receiving data.
 */
extern void du_socket_output_buffer_get_socket(du_socket_output_buffer* sb, du_socket* s);

/**
 *  Gets a poll instance.
 *  and stores it in the location given by p.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 *  @param[out] p pointer to the storage location for the pointer of du_poll.
 */
extern void du_socket_output_buffer_get_poll(du_socket_output_buffer* sb, du_poll** p);

/**
 *  Sets the time-out value in milliseconds.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 *  @param[in] msec  the time-out value for write, in milliseconds.
 *  A negative value means infinite timeout.
 */
extern void du_socket_output_buffer_set_write_timeout(du_socket_output_buffer* sb, du_int32 msec);

/**
 *  Gets the time-out value in milliseconds.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 *  @param[out] msec  pointer to the variable to receive the time-out value for write.
 *  A negative value means infinite timeout.
 */
extern void du_socket_output_buffer_get_write_timeout(du_socket_output_buffer* sb, du_int32* msec);

/**
 *  Checks if waiting process has canceled.
 *  @param[in] sb pointer to the du_socket_output_buffer structure.
 *  @return  true if sb was canceled, otherwise false.
*/
extern du_bool du_socket_output_buffer_is_canceled(du_socket_output_buffer* sb);

/**
 *  Checks if waiting process timed out.
 *  @param[in] sb pointer to the du_socket_output_buffer structure.
 *  @return  true if sb was timed out, otherwise false.
*/
extern du_bool du_socket_output_buffer_is_timed_out(du_socket_output_buffer* sb);

/**
 *  Resets sb buffer with values which are specified by initializing function.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 */
#define du_socket_output_buffer_reset(sb) du_output_buffer_reset(&(sb)->b)

/**
 *  Gets the number of data bytes in the buffer.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 *  @return byte length of the used area of the buffer of sb.
 */
#define du_socket_output_buffer_used(sb) du_output_buffer_used(&(sb)->b)

/**
 *  Gets the length of buffer.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 *  @return byte length of the buffer area of sb.
 */
#define du_socket_output_buffer_length(sb) du_output_buffer_length(&(sb)->b)

/**
 *  Flushes the buffered data.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_output_buffer_flush(sb) du_output_buffer_flush(&(sb)->b)

/**
 *  Writes data to a file output stream.
 *  This function might not buffer the data but write it out directly when the data
 *  size is larger than the buffer size.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 *  @param[in] buf the byte array from which to copy len bytes to the sb.
 *  @param[in] len the number of bytes to be written to sb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_output_buffer_write(sb, buf, len) du_output_buffer_write(&(sb)->b, (buf), (len))

/**
 *  Writes data to the file output stream.
 *  This function always buffers the data first. If the data size is larger than
 *  the buffer size, it flushs the buffer and then it buffers the remaining data.
 *  <b>du_socket_output_buffer_write_align</b> write all data to sb buffer.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 *  @param[in] buf the byte array from which to copy len bytes to the sb.
 *  @param[in] len the number of bytes to be written to sb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_output_buffer_write_align(sb, buf, len) du_output_buffer_write_align(&(sb)->b, (buf), (len))

/**
 *  Writes a null-terminated string.
 *  This function might not buffer the data but write it out directly when the data
 *  size is larger than the buffer size.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 *  @param[in] str the string which to copy to the sb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_output_buffer_writes(sb, str) du_output_buffer_writes(&(sb)->b, (str))

/**
 *  Writes a string to the file output stream.
 *  This function always buffers the data first. If the data size is larger than
 *  the buffer size, it flushs the buffer and then it buffers the remaining data.
 *  @param[in] sb  pointer to the du_socket_output_buffer structure.
 *  @param[in] str the string which to copy to the sb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_output_buffer_writes_align(sb, str) du_output_buffer_writes_align(&(sb)->b, (str))

/**
 *  Copies data from the specified input buffer to the specified output buffer.
 *  @param[in] from  pointer to the du_socket_output_buffer structure.
 *  @param[out] to  pointer to the du_socket_ouput_buffer structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark to is a du_socket_ouput_buffer initialized by
 *  the <b>du_socket_output_buffer_init</b> function.
 */
#define du_socket_output_buffer_copy(from, to) du_output_buffer_copy(&(from)->b, &(to)->b)

extern du_bool du_socket_output_buffer_write_op(const du_uint8* buf, du_uint32 len, void* arg);

#ifdef __cplusplus
}
#endif

#endif
