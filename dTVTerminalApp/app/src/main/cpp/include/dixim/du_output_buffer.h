/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_output_buffer interface provides methods for buffering the output
 *  to a stream.
 *  ( such as initialize, write, flush ).
 *  Before using a du_output_buffer, use <b>du_output_buffer_init</b> to establish
 *  the pointer of buffer data, buffer size, and function using to write buffered data
 *  stored in the du_ouput_buffer structure.
 */

#ifndef DU_OUTPUT_BUFFER_H
#define DU_OUTPUT_BUFFER_H

#include <du_input_buffer.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  The interface definition to write data.
 *  @param[in] buf  the byte array from which to copy len bytes
 *     to the current buffered stream.
 *  @param[in] len the number of bytes to be written to the current buffered stream.
 *  @param[in] arg a parameter for the write function.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
typedef du_bool (*du_output_buffer_write_op)(const du_uint8* buf, du_uint32 len, void* arg);

/**
 *  Structure of du_output_buffer.
 */
typedef struct du_output_buffer {
    du_uint8* x;
    du_uint32 len;
    du_uint32 p;
    du_uint32 n;
    du_output_buffer_write_op write_op;
    void* arg;
} du_output_buffer;

/**
 *  Initializes a du_output_buffer.
 *  @param[out] b  pointer to the du_output_buffer structure.
 *  @param[in] buf buffer area used to cache data.
 *  @param[in] len number of bytes of b.
 *  @param[in] write_op the function using to write the data to buf.
 *  @param[in] arg a parameter for the write_op function.
 */
extern void du_output_buffer_init(du_output_buffer* b, du_uint8* buf, du_uint32 len, du_output_buffer_write_op write_op, void* arg);

/**
 *  Resets b buffer with values which are specified by initializing function.
 *  @param[in] b  pointer to the du_output_buffer structure.
 */
extern void du_output_buffer_reset(du_output_buffer* b);

/**
 *  Gets the number of data bytes in the buffer.
 *  @param[in] b  pointer to the du_output_buffer structure.
 *  @return byte length of the used area of the buffer of b.
 */
extern du_uint32 du_output_buffer_used(du_output_buffer* b);

/**
 *  Gets the length of buffer.
 *  @param[in] b  pointer to the du_output_buffer structure.
 *  @return byte length of the buffer area of b.
 */
extern du_uint32 du_output_buffer_length(du_output_buffer* b);

/**
 *  Flushes the buffered data.
 *  @param[in] b  pointer to the du_output_buffer structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_output_buffer_flush(du_output_buffer* b);

/**
 *  Writes data to a output stream.
 *  This function might not buffer the data but write it out directly when the data
 *  size is larger than the buffer size.
 *  @param[in] b  pointer to the du_ouput_buffer structure.
 *  @param[in] buf the byte array from which to copy len bytes to the b.
 *  @param[in] len the number of bytes to be written to b.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_output_buffer_write(du_output_buffer* b, const du_uint8* buf, du_uint32 len);

/**
 *  Writes data to the output stream.
 *  This function always buffers the data first. If the data size is larger than
 *  the buffer size, it flushs the buffer and then it buffers the remaining data.
 *  <b>du_output_buffer_write_align</b> write all data to b buffer.
 *  @param[in] b  pointer to the du_ouput_buffer structure.
 *  @param[in] buf the byte array from which to copy len bytes to the b.
 *  @param[in] len the number of bytes to be written to b.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_output_buffer_write_align(du_output_buffer* b, const du_uint8* buf, du_uint32 len);

/**
 *  Writes a null-terminated string.
 *  This function might not buffer the data but write it out directly when the data
 *  size is larger than the buffer size.
 *  @param[in] b  pointer to the du_ouput_buffer structure.
 *  @param[in] str the string which to copy to the b.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_output_buffer_writes(du_output_buffer* b, const du_uchar* str);

/**
 *  Writes a string to the output stream.
 *  This function always buffers the data first. If the data size is larger than
 *  the buffer size, it flushs the buffer and then it buffers the remaining data.
 *  @param[in] b pointer to the du_ouput_buffer structure.
 *  @param[in] str the string which to copy to the b.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_output_buffer_writes_align(du_output_buffer* b, const du_uchar* str);

/**
 *  Copies data from the specified input buffer to the specified output buffer.
 *  @param[in] from pointer to the du_input_buffer structure.
 *  @param[in] to pointer to the du_ouput_buffer structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_output_buffer_copy(du_input_buffer* from, du_output_buffer* to);

#ifdef __cplusplus
}
#endif

#endif
