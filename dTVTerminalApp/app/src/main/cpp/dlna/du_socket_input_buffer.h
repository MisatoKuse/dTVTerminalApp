/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_socket_input_buffer interface provides methods for buffering the input
 *  from a socket stream.
 *  Before using a du_socket_input_buffer, use <b>du_socket_input_buffer_init</b>
 *  to establish the pointer of buffer data, buffer size, du_socket referencing a socket,
 *  du_poll, and read timeout value
 *  stored in the du_socket_input_buffer structure.
 */

#ifndef DU_SOCKET_INPUT_BUFFER_H
#define DU_SOCKET_INPUT_BUFFER_H

#include "du_socket_os.h"
#include "du_poll_os.h"
#include "du_input_buffer.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Structure of du_socket_input_buffer.
 */
typedef struct du_socket_input_buffer {
    du_input_buffer b;
    du_poll* p;
    du_socket s;
    du_int32 read_timeout_ms;
    du_bool canceled;
    du_bool timed_out;
} du_socket_input_buffer;

/**
 *  Initializes a du_socket_input_buffer.
 *  @param[out] sb  pointer to the du_socket_input_buffer structure.
 *  @param[in] buf buffer area used to cache data.
 *  @param[in] len number of bytes of buf.
 *  @param[in] s du_socket data referencing a socket.
 *  @param[in] p pointer to du_poll data for waiting for some events.
 *  @param[in] read_timeout_ms the time-out for read, in milliseconds.
 *  A negative value means infinite timeout.
 */
extern void du_socket_input_buffer_init(du_socket_input_buffer* sb, du_uint8* buf, du_uint32 len, du_socket s, du_poll* p, du_int32 read_timeout_ms);

/**
 *  Gets the socket handle.
 *  and stores it in the location given by s.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[out] s pointer to a du_socket for receiving data.
 */
extern void du_socket_input_buffer_get_socket(du_socket_input_buffer* sb, du_socket* s);

/**
 *  Gets a poll instance.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[out] p pointer to the storage location for the pointer of du_poll.
 */
extern void du_socket_input_buffer_get_poll(du_socket_input_buffer* sb, du_poll** p);

/**
 *  Sets the time-out value in milliseconds.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[in] msec  the time-out value for read, in milliseconds.
 *  A negative value means infinite timeout.
 */
extern void du_socket_input_buffer_set_read_timeout(du_socket_input_buffer* sb, du_int32 msec);

/**
 *  Gets the time-out value in milliseconds.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[out] msec  pointer to the variable to receive the time-out value for read.
 *  A negative value means infinite timeout.
 */
extern void du_socket_input_buffer_get_read_timeout(du_socket_input_buffer* sb, du_int32* msec);

/**
 *  Checks if waiting process has canceled.
 *  @param[in] sb pointer to the du_socket_input_buffer structure.
 *  @return  true if sb was canceled, otherwise false.
*/
extern du_bool du_socket_input_buffer_is_canceled(du_socket_input_buffer* sb);

/**
 *  Checks if waiting process timed out.
 *  @param[in] sb pointer to the du_socket_input_buffer structure.
 *  @return  true if sb was timed out, otherwise false.
*/
extern du_bool du_socket_input_buffer_is_timed_out(du_socket_input_buffer* sb);

/**
 *  Resets sb buffer with values which are specified by initializing function.
 *  @param[in,out] sb  du_socket_input_buffer structure.
 */
#define du_socket_input_buffer_reset(sb) du_input_buffer_reset(&(sb)->b)

/**
 *  Returns the number of data bytes which can be read immediately.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @return  the number of bytes stored in the sb buffer.
 */
#define du_socket_input_buffer_available(sb) du_input_buffer_available(&(sb)->b)

/**
 *  Returns the length of buffer of fb.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @return  the byte length of the the buffer in the sb.
 */
#define du_socket_input_buffer_length(sb) du_input_buffer_length(&(sb)->b)

/**
 *  Reads bytes from the socket stream.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[out] buf the buffer to which data are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[out] nbytes pointer to the variable to receive the total number of bytes read
 *          into buf.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_input_buffer_read(sb, buf, len, nbytes) du_input_buffer_read(&(sb)->b, (buf), (len), (nbytes))

/**
 *  Reads the next token.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[out] buf the buffer to which token are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[in] charset the delimiter of the token to be found in sb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_input_buffer_read_token(sb, buf, len, charset) du_input_buffer_read_token(&(sb)->b, (buf), (len), (charset))

/**
 *  Reads a line of text.
 *  A line is considered to be terminated by a line feed ('\\n').
 *  if a carriage return ('\\r') followed immediately by a linefeed,
 *  deletes the carriage return.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[out] buf the buffer to which the line of text are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_input_buffer_read_line(sb, buf, len) du_input_buffer_read_line(&(sb)->b, (buf), (len))

/**
 *  Reads len bytes data.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[out] buf the buffer to which data are to be copied.
 *  @param[in] len the number of byte to read.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_input_buffer_fill(sb, buf, len) du_input_buffer_fill(&(sb)->b, (buf), (len))

/**
 *  Attempts to skip over a specified number of bytes.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[in] size the number of bytes to skip.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_input_buffer_skip(sb, size) du_input_buffer_skip(&(sb)->b, (size))

/**
 *  Attempts to fill up the buffer with new data.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[out] nbytes pointer to the variable to receive the number of bytes stored
 *  in the sb buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_input_buffer_pool(sb, nbytes) du_input_buffer_pool(&(sb)->b, (nbytes))

/**
 *  Feeds data.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[out] nbytes pointer to the variable to receive the number of bytes
 *  stored in the sb buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_socket_input_buffer_feed(sb, nbytes) du_input_buffer_feed(&(sb)->b, (nbytes))

/**
 *  Returns the pointer of available data.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @return  the pointer of next available data to be read.
 */
#define du_socket_input_buffer_peek(sb) du_input_buffer_peek(&(sb)->b)

/**
 *  Sets the position within the current buffer.
 *  @param[in] sb  pointer to the du_socket_input_buffer structure.
 *  @param[in] len a byte offset relative to current position.
 */
#define du_socket_input_buffer_seek(sb, len) du_input_buffer_seek(&(sb)->b, (len))

#ifdef __cplusplus
}
#endif

#endif
