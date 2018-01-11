/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ring_buffer.h 2301 2008-06-25 13:06:27Z gondo $
 */

#ifndef RING_BUFFER_H
#define RING_BUFFER_H

#include <du_type_os.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    du_uint8* buf;
    du_uint32 item_size;
    du_uint32 item_count;
    du_uint32 wp;
    du_uint32 rp;
} ring_buffer;

extern void ring_buffer_init(ring_buffer* rb, du_uint8* buf, du_uint32 item_size, du_uint32 item_count);
extern du_bool ring_buffer_push(ring_buffer* rb, du_uint8* item);
extern du_bool ring_buffer_pop(ring_buffer* rb, du_uint8* item);

#ifdef __cplusplus
}
#endif

#endif

