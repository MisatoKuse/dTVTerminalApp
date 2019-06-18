/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file dav_didl_object_packed.h
 *  @brief The dav_didl_object_packed interface provides methods for manipulating
 *  the dav_didl_object_packed data structure.
 */


#ifndef DAV_DIDL_OBJECT_PACKED_H
#define DAV_DIDL_OBJECT_PACKED_H

#include <du_type.h>
#include <dav_didl_object.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dav_didl_object_packed_buf_header {
    du_uint32 n_byte_header;
    du_uint32 n_byte_total;
    du_uint32 n_byte_text;
    du_uint32 n_byte_attr_array;
    du_uint32 n_byte_attr_list_array;
    du_uint32 n_byte_prop_array;
    du_uint32 n_byte_prop_list_array;
} dav_didl_object_packed_buf_header;

typedef struct dav_didl_object_packed {
    const dav_didl_name* name;
    dav_didl_object_attribute_list* attr_list;
    dav_didl_object_property_list* prop_list;
    void (*_free)(struct dav_didl_object*);
    du_bool (*_clone)(const struct dav_didl_object*, struct dav_didl_object*);
    du_uchar* _buf;
} dav_didl_object_packed;

extern void dav_didl_object_init_packed(dav_didl_object_packed* x);

extern void dav_didl_object_free_packed(dav_didl_object* x);

#ifdef __cplusplus
}
#endif

#endif
