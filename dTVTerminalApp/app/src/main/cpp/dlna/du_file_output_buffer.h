/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_file_output_buffer interface provides methods for buffering the output
 *   to a file stream.
 *   Before using a du_file_output_buffer, use <b>du_file_output_buffer_init</b>
 *   to establish the pointer of buffer data, buffer size, and function using to write buffered data
 *   stored in the du_file_output_buffer structure.
 */

#ifndef DU_FILE_OUTPUT_BUFFER_H
#define DU_FILE_OUTPUT_BUFFER_H

#include "du_output_buffer.h"
#include "du_file_os.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Structure of du_file_output_buffer.
 */
typedef struct du_file_output_buffer {
    du_output_buffer b;
    du_file f;
} du_file_output_buffer;

/**
 *  Initializes a du_file_output_buffer.
 *  @param[out] fb  pointer to the du_file_output_buffer structure.
 *  @param[in] buf buffer area used to cache data.
 *  @param[in] len number of bytes of buf.
 *  @param[in] f a handle to the file.
 */
extern void du_file_output_buffer_init(du_file_output_buffer* fb, du_uint8* buf, du_uint32 len, du_file f);

/**
 *  Gets a file handle.
 *  @param[in] fb  pointer to the du_file_output_buffer structure.
 *  @param[out] f pointer to the variable to receive the handle stored in fb.
 */
extern void du_file_output_buffer_get_file(du_file_output_buffer* fb, du_file* f);

/**
 *  Resets fb buffer with values which are specified by initializing function.
 *  @param[in] fb  pointer to the du_file_output_buffer structure.
 */
#define du_file_output_buffer_reset(fb) du_output_buffer_reset(&(fb)->b)

/**
 *  Gets the number of data bytes in the buffer.
 *  @param[in] fb  pointer to the du_file_output_buffer structure.
 *  @return  the number of data bytes in the buffer.
 */
#define du_file_output_buffer_used(fb) du_output_buffer_used(&(fb)->b)

/**
 *  Gets the length of buffer.
 *  @param[in] fb  pointer to the du_file_output_buffer structure.
 *  @return  the length of buffer of fb.
 */
#define du_file_output_buffer_length(fb) du_output_buffer_length(&(fb)->b)

/**
 *  Flushes the buffered data.
 *  @param[in] fb pointer to the du_file_output_buffer structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_file_output_buffer_flush(fb) du_output_buffer_flush(&(fb)->b)

/**
 *  Writes data to a file output stream.
 *  This function might not buffer the data but write it out directly when the data
 *  size is larger than the buffer size.
 *  @param[in] fb  pointer to the du_file_output_buffer structure.
 *  @param[in] buf the byte array from which to copy len bytes to the fb.
 *  @param[in] len the number of bytes to be written to fb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_file_output_buffer_write(fb, buf, len) du_output_buffer_write(&(fb)->b, (buf), (len))

/**
 *  Writes data to the file output stream.
 *  This function always buffers the data first. If the data size is larger than
 *  the buffer size, it flushs the buffer and then it buffers the remaining data.
 *  @param[in] fb  pointer to the du_file_output_buffer structure.
 *  @param[in] buf the byte array from which to copy len bytes to the fb.
 *  @param[in] len the number of bytes to be written to fb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_file_output_buffer_write_align(fb, buf, len) du_output_buffer_write_align(&(fb)->b, (buf), (len))

/**
 *  Writes a null-terminated string.
 *  This function might not buffer the data but write it out directly when the data
 *  size is larger than the buffer size.
 *  @param[in] fb  pointer to the du_file_output_buffer structure.
 *  @param[in] str the string which to copy to the fb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_file_output_buffer_writes(fb, str) du_output_buffer_writes(&(fb)->b, (str))

/**
 *  Writes a string to the file output stream.
 *  This function always buffers the data first. If the data size is larger than
 *  the buffer size, it flushs the buffer and then it buffers the remaining data.
 *  @param[in] fb  pointer to the du_file_output_buffer structure.
 *  @param[in] str the string which to copy to the fb.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
#define du_file_output_buffer_writes_align(fb, str) du_output_buffer_writes_align(&(fb)->b, (str))

/**
 *  Copies data from the specified input buffer to the specified output buffer.
 *  @param[in] from  du_file_input_buffer structure.
 *  @param[out] to  pointer to the du_file_ouput_buffer structure.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark to is a du_file_ouput_buffer initialized by
 *  the <b>du_file_output_buffer_init</b> function.
 */
#define du_file_output_buffer_copy(from, to) du_output_buffer_copy(&(from)->b, &(to)->b)

#ifdef __cplusplus
}
#endif

#endif
