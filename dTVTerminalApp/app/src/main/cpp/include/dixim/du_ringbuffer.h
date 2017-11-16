/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */

/*
 * @file
 *  du_ringbuffer interface provides a method for ring buffer.
 */

#ifndef DU_RINGBUFFER_H
#define DU_RINGBUFFER_H

#include <du_mutex.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  du_ringbuffer structure contains data making up ring buffer.
 */
typedef struct du_ringbuffer {
    du_uint8* b;
    du_uint32 s;

    du_uint32 i;
    du_uint32 f;

    du_bool eos;
    du_bool running;

    du_mutex m;
    du_mutex m2;
} du_ringbuffer;

/**
 *  Initializes ring buffer.
 *
 *  @param[in,out] x pointer to du_ringbuffer to initialize.
 *  @param[in] buf_size buffer size used by @p x (unit: byte).
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @post @p x must be freed with du_ringbuffer_free().
 */
extern du_bool du_ringbuffer_init(du_ringbuffer* x, du_uint32 buf_size);

/**
 *  Starts du_ringbuffer.
 *
 *  @param[in,out] x pointer to du_ringbuffer to start.
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @post @p x must be stopped with du_ringbuffer_stop().
 */
extern du_bool du_ringbuffer_start(du_ringbuffer* x);

/**
 *  Stops ring buffer.
 *
 *  @param[in,out] x pointer to du_ringbuffer to stop.
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p x must be stopped with du_ringbuffer_stop().
 */
extern void du_ringbuffer_stop(du_ringbuffer* x);

/**
 *  Frees ring buffer.
 *
 *  @param[in,out] x pointer to du_ringbuffer to free.
 *  @pre @p x must be initialized with du_ringbuffer_init().
 */
extern void du_ringbuffer_free(du_ringbuffer* x);

/**
 *  Checks whether ring buffer is running.
 *
 *  @param[in] x pointer to du_ringbuffer to check.
 *  @param[out] running returns 1 if ring buffer is running.
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p x must be initialized with du_ringbuffer_init().
 */
extern du_bool du_ringbuffer_is_running(du_ringbuffer* x, du_bool* running);

/**
 *  Checks whether state of ring buffer is EOS (End Of Stream).
 *
 *  EOS means that ring buffer has no input and its buffer size is 0.
 *
 *  @param[in] x pointer to du_ringbuffer to check.
 *  @param[out] running returns 1 if ring buffer is EOS.
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p x must be initialized with du_ringbuffer_init().
 */
extern du_bool du_ringbuffer_is_eos(du_ringbuffer* x, du_bool* eos);

/**
 *  Sets buffer size of ring buffer.
 *
 *  @param[in,out] x pointer to du_ringbuffer to set.
 *  @param[in] size buffer size of ring buffer (unit: byte).
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p x must be initialized with du_ringbuffer_init().
 *  @remark This API mustn't be called when ring buffer is running.
 */
extern du_bool du_ringbuffer_set_buffer_size(du_ringbuffer* x, du_uint32 size);

/**
 *  Gets buffer size of ring buffer.
 *
 *  @param[in] x pointer to du_ringbuffer to get.
 *  @param[out] size buffer size of ring buffer (unit: byte).
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p x must be initialized with du_ringbuffer_init().
 */
extern du_bool du_ringbuffer_get_buffer_size(du_ringbuffer* x, du_uint32* size);

/**
 *  Gets readable buffer size of ring buffer.
 *
 *  @param[in] x pointer to du_ringbuffer to get.
 *  @param[out] size readable buffer size of ring buffer (unit: byte).
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p x must be initialized with du_ringbuffer_init().
 */
extern du_bool du_ringbuffer_get_active_buffer_size(du_ringbuffer* x, du_uint32* size);

/**
 *  Gets writable buffer size of ring buffer.
 *
 *  @param[in] x pointer to du_ringbuffer to get.
 *  @param[out] size writable buffer size of ring buffer (unit: byte).
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p x must be initialized with du_ringbuffer_init().
 */
extern du_bool du_ringbuffer_get_inactive_buffer_size(du_ringbuffer* x, du_uint32* size);

/**
 *  Peeks data in ring buffer.
 *
 *  Peeked data in ring buffer using this API isn't removed from the ring
 *  buffer.
 *
 *  @param[in,out] x pointer to du_ringbuffer to write.
 *  @param[out] b Peeked data.
 *              @p b must be allocated 1 byte.
 *  @param[in] pos position to peek (unit: byte).
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p x must be started with du_ringbuffer_start().
 */
extern du_bool du_ringbuffer_peek(du_ringbuffer* x, du_uint8* b, du_uint32 pos);

/**
 *  Writes data in ring buffer.
 *
 *  Read data in ring buffer using this API is removed from the ring buffer.
 *
 *  @param[in,out] x pointer to du_ringbuffer to write.
 *  @param[in] size buffer size to read (unit: byte).
 *  @param[out] read_size read size (unit: byte).
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p x must be started with du_ringbuffer_start().
 */
extern du_bool du_ringbuffer_read(du_ringbuffer* x, du_uint8* b, du_uint32 size, du_uint32* read_size);

/**
 *  Writes data in ring buffer.
 *
 *  To change state of ring buffer to EOS, call this API with @p size set to 0.
 *
 *  @param[in,out] x pointer to du_ringbuffer to write.
 *  @param[in] size buffer size to write (unit: byte).
 *  @param[out] skipped_size wrote buffer size (unit: byte).
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p x must be started with du_ringbuffer_start().
 *  @remark This API is run synchronously.
 *  @remark Don't call this API with @p x which state is EOS.
 *  @see du_ringbuffer_is_eos()
 */
extern du_bool du_ringbuffer_write(du_ringbuffer* x, du_uint8* b, du_uint32 size, du_uint32* written_size);

/**
 *  Skips buffer in ring buffer.
 *
 *  Skipped data in ring buffer using this API is removed from the ring buffer.
 *
 *  @param[in,out] x pointer to du_ringbuffer to skip.
 *  @param[in] size buffer size to skip (unit: byte).
 *  @param[out] skipped_size skipped buffer size (unit: byte).
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 *  @pre @p x must be started with du_ringbuffer_start().
 */
extern du_bool du_ringbuffer_skip(du_ringbuffer* x, du_uint32 size, du_uint32* skiped_size);

#ifdef __cplusplus
}
#endif

#endif
