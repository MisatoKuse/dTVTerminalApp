/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_input_buffer interface provides methods for buffering the input
 *   from a stream.
 *   Before using a du_input_buffer, use <b>du_input_buffer_init</b> to establish
 *   the pointer of buffer data, buffer size, and function using to read buffered data
 *   stored in the du_input_buffer structure.
 */

#ifndef DU_INPUT_BUFFER_H
#define DU_INPUT_BUFFER_H

#include <du_int.h>

#ifdef __cplusplus
extern "C" {
#endif


/**
 *  Interface definition to read data.
 *  @param[out] buf   the buffer to which data are to be copied.
 *  @param[in] len    maximum number of byte to read.
 *  @param[out] nbytes the total number of bytes read into buf.
 *  @param[in] arg a parameter for the read function.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
typedef du_bool (*du_input_buffer_read_op)(du_uint8* buf, du_uint32 len, du_uint32* nbytes, void* arg);

/**
 *  Interface definition to read data for read-only buffer.
 *  @param[out] buf   the buffer to which data are to be copied.
 *  @param[in] len    maximum number of byte to read.
 *  @param[out] nbytes the total number of bytes read into buf.
 *  @param[in] arg a parameter for the read function.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
typedef du_bool (*du_input_buffer_read_op_ro)(const du_uint8* buf, du_uint32 len, du_uint32* nbytes, void* arg);

/**
 * Status of input buffer.
 */
typedef enum {
    DU_INPUT_BUFFER_READ_WRITE,
    DU_INPUT_BUFFER_READ_ONLY,
} du_input_buffer_status;

/**
 *  Structure of du_input_buffer.
 */
typedef struct du_input_buffer {
    du_uint8* x;
    const du_uint8* x_ro;
    du_uint32 len;
    du_uint32 p;
    du_uint32 n;
    du_input_buffer_read_op read_op;
    du_input_buffer_read_op_ro read_op_ro;
    void* arg;
    du_input_buffer_status s;
} du_input_buffer;

/**
 *  Initializes a du_input_buffer.
 *  @param[out] b pointer to the du_input_buffer structure.
 *  @param[in] buf buffer area used to cache data.
 *  @param[in] len number of bytes of b.
 *  @param[in] read_op the function using to read the data stored in buf.
 *  @param[in] arg a parameter for the read_op function.
 */
extern void du_input_buffer_init(du_input_buffer* b, du_uint8* buf, du_uint32 len, du_input_buffer_read_op read_op, void* arg);

/**
 *  Initializes a du_input_buffer for read-only buffer.
 *  @param[out] b pointer to the du_input_buffer structure.
 *  @param[in] buf_ro buffer area used to cache data.
 *  @param[in] len number of bytes of b.
 *  @param[in] read_op the function using to read the data stored in buf.
 *  @param[in] arg a parameter for the read_op function.
 */
extern void du_input_buffer_init2(du_input_buffer* b, const du_uint8* buf_ro, du_uint32 len, du_input_buffer_read_op_ro read_op_ro, void* arg);

/**
 *  Resets b buffer with values which are specified by initializing function.
 *  @param[in,out] b  pointer to the du_input_buffer structure.
 */
extern void du_input_buffer_reset(du_input_buffer* b);

/**
 *  Returns the number of data bytes which can be read immediately.
 *  @param[in] b  pointer to the du_input_buffer structure.
 *  @return  the number of data bytes which can be read immediately.
 */
extern du_uint32 du_input_buffer_available(du_input_buffer* b);

/**
 *  Returns the length of buffer of b.
 *  @param[in] b  pointer to the du_input_buffer structure.
 *  @return  the length of buffer of b.
 */
extern du_uint32 du_input_buffer_length(du_input_buffer* b);

/**
 *  Reads bytes from the stream.
 *  @param[in] b pointer to the du_input_buffer structure.
 *  @param[out] buf the buffer to which data are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[out] nbytes pointer to the variable to receive the total number of bytes
 *  read into buf.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_input_buffer_read(du_input_buffer* b, du_uint8* buf, du_uint32 len, du_uint32* nbytes);

/**
 *  Reads the next token.
 *  @param[in] b pointer to the du_input_buffer structure.
 *  @param[out] buf the buffer to which token are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @param[in] charset the delimiter of the token to be found in b.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_input_buffer_read_token(du_input_buffer* b, du_uint8* buf, du_uint32 len, const du_uchar* charset);

/**
 *  Reads a line of text.
 *  A line is considered to be terminated by a line feed ('\\n').
 *  if a carriage return ('\\r') followed immediately by a linefeed,
 *  deletes the carriage return.
 *  @param[in] b pointer to the du_input_buffer structure.
 *  @param[out] buf the buffer to which the line of text are to be copied.
 *  @param[in] len maximum number of byte to read.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_input_buffer_read_line(du_input_buffer* b, du_uint8* buf, du_uint32 len);

/**
 *  Reads len bytes data.
 *  @param[in] b  pointer to the du_input_buffer structure.
 *  @param[out] buf the buffer to which data are to be copied.
 *  @param[in] len the number of byte to read.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_input_buffer_fill(du_input_buffer* b, du_uint8* buf, du_uint32 len);

/**
 *  Attempts to skip over a specified number of bytes.
 *  @param[in] b pointer to the du_input_buffer structure.
 *  @param[in] size the number of bytes to skip.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_input_buffer_skip(du_input_buffer* b, du_uint64 size);

/**
 *  Attempts to fill up the buffer with new data.
 *  @param[in] b pointer to the du_input_buffer structure.
 *  @param[out] nbytes pointer to the variable to receive the number of bytes stored
 *  in the b buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_input_buffer_pool(du_input_buffer* b, du_uint32* nbytes);

/**
 *  Feeds data.
 *  @param[in] b pointer to the du_input_buffer structure.
 *  @param[out] nbytes pointer to the variable to receive the number of bytes
 *  stored in the b buffer.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_input_buffer_feed(du_input_buffer* b, du_uint32* nbytes);

/**
 *  Returns the pointer of available data.
 *  @param[in] b pointer to the du_input_buffer structure.
 *  @return  the pointer of next available data to be read.
 */
#define du_input_buffer_peek(b) (( (b)->x ? (b)->x : (b)->x_ro ) + (b)->n)

/**
 *  Sets the position within the current buffer.
 *  @param[in] b  pointer to the du_input_buffer structure.
 *  @param[in] len a byte offset relative to current position.
 */
#define du_input_buffer_seek(b, len) (((b)->p -= (len)), ((b)->n += (len)))

#ifdef __cplusplus
}
#endif

#endif
