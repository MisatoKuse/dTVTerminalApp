/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DAV_CAPABILITY_PARSER_LIMIT_H
#define DAV_CAPABILITY_PARSER_LIMIT_H

#include <dav_capability_format.h>
#include <du_uint32_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef enum dav_capability_parser_limit_type {
    DAV_CAPABILITY_PARSER_LIMIT_TYPE_UNKNOWN,
    DAV_CAPABILITY_PARSER_LIMIT_TYPE_VALUE,
    DAV_CAPABILITY_PARSER_LIMIT_TYPE_RANGE,
} dav_capability_parser_limit_type;

typedef struct dav_capability_parser_limit {
    dav_capability_id id;

    dav_capability_parser_limit_type type;
    union {
        struct {
            du_uint32_array value_array;
        } value;
        struct {
            du_uint32 max;
            du_uint32 min;
        } range;
    } limit;
} dav_capability_parser_limit;

extern void dav_capability_parser_limit_init(dav_capability_parser_limit* x);

extern void dav_capability_parser_limit_free(dav_capability_parser_limit* x);

extern void dav_capability_parser_limit_reset(dav_capability_parser_limit* x);

extern du_bool dav_capability_parser_limit_set_type(dav_capability_parser_limit* x, dav_capability_parser_limit_type type);

#ifdef __cplusplus
}
#endif

#endif
