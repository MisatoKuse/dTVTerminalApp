/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_SHARED_MEMORY_H
#define DUPNP_SHARED_MEMORY_H

#include <dupnp_impl.h>
#include <du_type.h>
#include <du_str_array.h>
#include <du_uchar_array.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DUPNP_SHARED_MEMORY_BUF_SIZE 1200

extern du_bool dupnp_shared_memory_init(dupnp_shared_memory* x, dupnp_impl* upnp);

extern void dupnp_shared_memory_free(dupnp_shared_memory* x);

extern du_uint8* dupnp_shared_memory_get_shared_memory(dupnp_shared_memory* x);

extern du_uint32 dupnp_shared_memory_get_shared_memory_size(dupnp_shared_memory* x);

extern du_str_array* dupnp_shared_memory_get_shared_memory_str_array(dupnp_shared_memory* x);

extern du_str_array* dupnp_shared_memory_get_shared_memory_str_array2(dupnp_shared_memory* x);

extern du_uchar_array* dupnp_shared_memory_get_shared_memory_uchar_array(dupnp_shared_memory* x);

extern du_uchar_array* dupnp_shared_memory_get_shared_memory_uchar_array2(dupnp_shared_memory* x);

extern du_uchar_array* dupnp_shared_memory_get_shared_memory_uchar_array3(dupnp_shared_memory* x);

extern du_uchar_array* dupnp_shared_memory_get_shared_memory_uchar_array4(dupnp_shared_memory* x);

extern du_uchar_array* dupnp_shared_memory_get_shared_memory_uchar_array5(dupnp_shared_memory* x);

extern du_uchar_array* dupnp_shared_memory_get_shared_memory_uchar_array6(dupnp_shared_memory* x);

#ifdef __cplusplus
}
#endif

#endif
