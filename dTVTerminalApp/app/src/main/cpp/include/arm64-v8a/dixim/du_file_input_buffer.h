/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_file_input_buffer interface provides methods for buffering the input
 *   from a file stream.
 *   Before using a du_file_input_buffer, use <b>du_file_input_buffer_init</b>
 *   to establish the pointer of buffer data, buffer size, and function using to read buffered data
 *   stored in the du_file_input_buffer structure.
 */

#ifndef DU_FILE_INPUT_BUFFER_H
#define DU_FILE_INPUT_BUFFER_H

#include <du_input_buffer.h>
#include <du_file_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Structure of du_file_input_buffer.
 */
typedef struct du_file_input_buffer {
    du_input_buffer b;
    du_file f;
} du_file_input_buffer;

/**
 *  Initializes a du_file_input_buffer.
 *  @param[out] fb  du_file_input_buffer structure.
 *  @param[in] buf buffer area used to cache data.
 *  @param[in] len number of bytes of buf.
 *  @param[in] f a handle to the file.
 */
extern void du_file_input_buffer_init(du_file_input_buffer* fb, du_uint8* buf, du_uint32 len, du_file f);

/**
 *  Gets the file handle.
 *  @param[in] fb  pointer to the du_file_input_buffer structure.
 *  @param[out] f pointer to the variable to receive the handle stored in fb.
 */
extern void du_file_input_buffer_get_file(du_file_input_buffer* fb, du_file* f);

/**
 *  Gets the file position of the available data to be read.
 *  @param[in] fb  pointer to the du_file_input_buffer structure.
 *  @param[out] pos pointer to the variable to receive the file pointer
 *   as measured in bytes from the beginning of the file.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_input_buffer_get_pos(du_file_input_buffer* fb, du_uint64* pos);

/**
 *  Resets fb buffer with values which are specified by initializing function.
 *  @param[in,out] fb  pointer to the du_file_input_buffer structure.
 */
#define du_file_input_buffer_reset(fb) du_input_buffer_reset(&(fb)->b)

/**
 *  Gets the number of data bytes which can be read immediately.
 *  @param[in] fb  pointer to the du_file_input_buffer structure.
 *  @return  the number of data bytes which can be read immediately.
 */
#define du_file_input_buffer_available(fb) du_input_buffer_available(&(fb)->b)

/**
 *  Returns the length of buffer.
 *  @param[in] fb  pointer to the du_file_input_buffer structure.
 *  @return  the length of buffer of fb.
 */
#define du_file_input_buffer_length(fb) du_input_buffer_length(&(fb)->b)

/**
 *  Reads bytes from the stream.
 *  @param[in] fb  pointer to the du_file_input_buffer structure.
 *  @param[out] buf the buffer to which data are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[out] nbytes pointer to the variable to receive the total number of bytes read
 *          into buf.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_file_input_buffer_read(fb, buf, len, nbytes) du_input_buffer_read(&(fb)->b, (buf), (len), (nbytes))

/**
 *  Reads the next token.
 *  @param[in] fb  pointer to the du_file_input_buffer structure.
 *  @param[out] buf the buffer to which token are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[in] charset the delimiter of the token to be found in fb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_file_input_buffer_read_token(fb, buf, len, charset) du_input_buffer_read_token(&(fb)->b, (buf), (len), (charset))

/**
 *  Reads a line of text.
 *  A line is considered to be terminated by a line feed ('\\n').
 *  if a carriage return ('\\r') is followed immediately by a linefeed,
 *  it is also deleted.
 *  @param[in] fb  pointer to the du_file_input_buffer structure.
 *  @param[out] buf the buffer to which the line of text are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_file_input_buffer_read_line(fb, buf, len) du_input_buffer_read_line(&(fb)->b, (buf), (len))

/**
 *  Reads len bytes data.
 *  @param[in] fb  pointer to the du_file_input_buffer structure.
 *  @param[out] buf the buffer to which data are to be copied.
 *  @param[in] len the number of byte to read.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_file_input_buffer_fill(fb, buf, len) du_input_buffer_fill(&(fb)->b, (buf), (len))

/**
 *  Attempts to skip over a specified number of bytes.
 *  @param[in] fb  pointer to the du_file_input_buffer structure.
 *  @param[in] size the number of bytes to skip.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_file_input_buffer_skip(du_file_input_buffer* fb, du_uint64 size);

/**
 *  Attempts to fill up the buffer with new data.
 *  @param[in] fb  pointer to the du_file_input_buffer structure.
 *  @param[out] nbytes pointer to the variable to receive the number of bytes stored
 *  in the fb buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_file_input_buffer_pool(fb, nbytes) du_input_buffer_pool(&(fb)->b, (nbytes))

/**
 *  Feeds data.
 *  @param[in] fb pointer to the du_file_input_buffer structure.
 *  @param[out] nbytes pointer to the variable to receive the number of bytes
 *  stored in the fb buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_file_input_buffer_feed(fb, nbytes) du_input_buffer_feed(&(fb)->b, (nbytes))

/**
 *  Returns the pointer of available data.
 *  @param[in] fb pointer to the du_file_input_buffer structure.
 *  @return  the pointer of available data to be read.
 */
#define du_file_input_buffer_peek(fb) du_input_buffer_peek(&(fb)->b)

/**
 *  Sets the position within the current buffer.
 *  @param[in] fb  pointer to the du_file_input_buffer structure.
 *  @param[in] len a byte offset relative to current position.
 */
#define du_file_input_buffer_seek(fb, len) du_input_buffer_seek(&(fb)->b, (len))

#ifdef __cplusplus
}
#endif

#endif
