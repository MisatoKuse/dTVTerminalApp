/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_str_input_buffer interface provides methods for reading data
 *   from buffer of string ( such as initialize, read, seek ).
 *   A buffer is a block of bytes in memory used to cache data, thereby reducing the number
 *   of calls to the operating system and improves read performance.
 *   Before using a du_str_input_buffer, use <b>du_str_input_buffer_init</b>
 *   to establish the pointer of buffer data, buffer size
 *   stored in the du_str_input_buffer structure.
 */

#ifndef DU_STR_INPUT_BUFFER_H
#define DU_STR_INPUT_BUFFER_H

#include "du_input_buffer.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Structure of du_str_input_buffer.
 */
typedef du_input_buffer du_str_input_buffer;

/**
 *  Initializes a du_str_input_buffer.
 *  @param[out] sb  pointer to the du_str_input_buffer structure.
 *  @param[in] str pointer of buffer area.
 */
extern void du_str_input_buffer_init(du_str_input_buffer* sb, const du_uchar* str);

/**
 *  Initializes a du_str_input_buffer.
 *  @param[out] sb  pointer to the du_str_input_buffer structure.
 *  @param[in] str pointer of buffer area.
 *  @param[in] len number of bytes of str.
 */
extern void du_str_input_buffer_init2(du_str_input_buffer* sb, const du_uchar* str, du_uint32 len);

/**
 *  Resets sb buffer with values which are specified by initializing function.
 *  @param[in] sb  pointer to the du_str_input_buffer structure.
 */
#define du_str_input_buffer_reset(sb) du_input_buffer_reset(sb)

/**
 *  Gets the number of data bytes which can be read.
 *  @param[in] sb  pointer to the du_str_input_buffer structure.
 *  @return  the number of bytes stored in the sb buffer.
 */
#define du_str_input_buffer_available(sb) du_input_buffer_available(sb)

/**
 *  Gets the length of buffer.
 *  @param[in] sb  pointer to the du_file_input_buffer structure.
 *  @return  the length of buffer of sb.
 */
#define du_str_input_buffer_length(sb) du_input_buffer_length(sb)

/**
 *  Reads bytes from the stream.
 *  @param[in] sb pointer to the du_str_input_buffer structure.
 *  @param[out] buf the buffer to which data are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[out] nbytes pointer to the variable to receive the total number of bytes
 *  read into buf.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_str_input_buffer_read(sb, buf, len, nbytes) du_input_buffer_read((sb), (buf), (len), (nbytes))

/**
 *  Reads the next token.
 *  @param[in] sb pointer to the du_str_input_buffer structure.
 *  @param[out] buf the buffer to which token are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[in] charset the delimiter of the token to be found in sb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_str_input_buffer_read_token(sb, buf, len, charset) du_input_buffer_read_token((sb), (buf), (len), (charset))

/**
 *  Reads a line of text.
 *  A line is considered to be terminated by a line feed ('\\n').
 *  if a carriage return ('\\r') followed immediately by a linefeed,
 *  deletes the carriage return.
 *  @param[in] sb pointer to the du_str_input_buffer structure.
 *  @param[out] buf the buffer to which the line of text are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_str_input_buffer_read_line(sb, buf, len) du_input_buffer_read_line((sb), (buf), (len))

/**
 *  Reads len bytes data.
 *  @param[in] sb  pointer to the du_str_input_buffer structure.
 *  @param[out] buf the buffer to which data are to be copied.
 *  @param[in] len the number of byte to read.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_str_input_buffer_fill(sb, buf, len) du_input_buffer_fill((sb), (buf), (len))

/**
 *  Attempts to skip over a specified number of bytes.
 *  @param[in] sb pointer to the du_str_input_buffer structure.
 *  @param[in] size the number of bytes to skip.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_str_input_buffer_skip(sb, size) du_input_buffer_skip((sb), (size))

/**
 *  Attempts to fill up the buffer with new data.
 *  @param[in] sb pointer to the du_str_input_buffer structure.
 *  @param[out] nbytes pointer to the variable to receive the number of bytes stored
 *  in the sb buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_str_input_buffer_pool(sb, nbytes) du_input_buffer_pool((sb), (nbytes))

/**
 *  Feeds data.
 *  @param[in] sb pointer to the du_str_input_buffer structure.
 *  @param[out] nbytes pointer to the variable to receive the number of bytes
 *  stored in the sb buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_str_input_buffer_feed(sb, nbytes) du_input_buffer_feed((sb), (nbytes))

/**
 *  Returns the pointer of available data.
 *  @param[in] sb pointer to the du_str_input_buffer structure.
 *  @return  the pointer of next available data to be read.
 */
#define du_str_input_buffer_peek(sb) du_input_buffer_peek(sb)

/**
 *  Sets the position within the current buffer.
 *  @param[in] sb  pointer to the du_str_input_buffer structure.
 *  @param[in] len a byte offset relative to current position.
 */
#define du_str_input_buffer_seek(sb, len) du_input_buffer_seek((sb), (len))

/**
 *  Returns the pointer of next available data to be read in sb buffer.
 *  @param[in] sb pointer to the du_str_input_buffer structure.
 *  @return  the pointer of next available data to be read.
 */
#define DU_STR_INPUT_BUFFER_PEEK(sb) DU_INPUT_BUFFER_PEEK(sb)

/**
 *  Sets the position within the current sb buffer.
 *  @param[in] sb  pointer to the du_str_input_buffer structure.
 *  @param[in] len a byte offset relative to current position.
 */
#define DU_STR_INPUT_BUFFER_SEEK(sb, len) DU_INPUT_BUFFER_SEEK((sb), (len))

#ifdef __cplusplus
}
#endif

#endif
