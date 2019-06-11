/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dregdb.h
 *  @brief The dregdb interface provides various basic methods for accessing CDS DB
 * (such as opening DB, closing DB, searching, updating, deleting data).
 */

#ifndef DREGDB_H
#define DREGDB_H

#include <drdb.h>

#include <du_str_array.h>
#include <du_uchar_array.h>
#include <du_uint8_array.h>
#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dregdb dregdb;

typedef void (*dregdb_free)(dregdb* reg);

typedef du_bool (*dregdb_clear)(dregdb* reg, drdb_connection* conn);

typedef du_bool (*dregdb_get_count)(dregdb* reg, drdb_connection* conn, du_uint32 id, du_uint32* count);

typedef du_bool (*dregdb_get_key_list)(dregdb* reg, drdb_connection* conn, du_uint32 id, du_str_array* key_list);

typedef du_bool (*dregdb_exists)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_bool* exists);

typedef du_bool (*dregdb_remove)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key);

typedef du_bool (*dregdb_remove_all)(dregdb* reg, drdb_connection* conn, du_uint32 id);

typedef du_bool (*dregdb_get_bool)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_bool* value);

typedef du_bool (*dregdb_put_bool)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_bool value);

typedef du_bool (*dregdb_get_int8)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_int8* value);

typedef du_bool (*dregdb_put_int8)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_int8 value);

typedef du_bool (*dregdb_get_uint8)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_uint8* value);

typedef du_bool (*dregdb_put_uint8)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_uint8 value);

typedef du_bool (*dregdb_get_int16)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_int16* value);

typedef du_bool (*dregdb_put_int16)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_int16 value);

typedef du_bool (*dregdb_get_uint16)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_uint16* value);

typedef du_bool (*dregdb_put_uint16)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_uint16 value);

typedef du_bool (*dregdb_get_int32)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_int32* value);

typedef du_bool (*dregdb_put_int32)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_int32 value);

typedef du_bool (*dregdb_get_uint32)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_uint32* value);

typedef du_bool (*dregdb_put_uint32)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_uint32 value);

typedef du_bool (*dregdb_get_int64)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_int64* value);

typedef du_bool (*dregdb_put_int64)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_int64 value);

typedef du_bool (*dregdb_get_uint64)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_uint64* value);

typedef du_bool (*dregdb_put_uint64)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_uint64 value);

typedef du_bool (*dregdb_get_float32)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_float32* value);

typedef du_bool (*dregdb_put_float32)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_float32 value);

typedef du_bool (*dregdb_get_float64)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_float64* value);

typedef du_bool (*dregdb_put_float64)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_float64 value);

typedef du_bool (*dregdb_get_string)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_uchar** value);

typedef du_bool (*dregdb_get_string2)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_uchar_array* ua);

typedef du_bool (*dregdb_put_string)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, const du_uchar* value);

typedef du_bool (*dregdb_get_binary)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, du_uint8* buf, du_uint32* length);

typedef du_bool (*dregdb_put_binary)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key, const du_uint8* buf, du_uint32 length);

typedef du_bool (*dregdb_get_uint8_list)(dregdb* reg, drdb_connection* conn, du_uint32 id, du_str_array* key_list, du_uint8_array* value_list);

typedef du_bool (*dregdb_put_null)(dregdb* reg, drdb_connection* conn, du_uint32 id, const du_uchar* key);

struct dregdb {
    dregdb_free free;
    dregdb_clear clear;
    dregdb_get_count get_count;
    dregdb_get_key_list get_key_list;
    dregdb_remove remove;
    dregdb_remove_all remove_all;
    dregdb_exists exists;
    dregdb_get_bool get_bool;
    dregdb_put_bool put_bool;
    dregdb_get_int8 get_int8;
    dregdb_put_int8 put_int8;
    dregdb_get_uint8 get_uint8;
    dregdb_put_uint8 put_uint8;
    dregdb_get_int16 get_int16;
    dregdb_put_int16 put_int16;
    dregdb_get_uint16 get_uint16;
    dregdb_put_uint16 put_uint16;
    dregdb_get_int32 get_int32;
    dregdb_put_int32 put_int32;
    dregdb_get_uint32 get_uint32;
    dregdb_put_uint32 put_uint32;
    dregdb_get_int64 get_int64;
    dregdb_put_int64 put_int64;
    dregdb_get_uint64 get_uint64;
    dregdb_put_uint64 put_uint64;
    dregdb_get_float32 get_float32;
    dregdb_put_float32 put_float32;
    dregdb_get_float64 get_float64;
    dregdb_put_float64 put_float64;
    dregdb_get_string get_string;
    dregdb_get_string2 get_string2;
    dregdb_put_string put_string;
    dregdb_get_binary get_binary;
    dregdb_put_binary put_binary;
    dregdb_get_uint8_list get_uint8_list;
    dregdb_put_null put_null;
};

#ifdef __cplusplus
}
#endif

#endif
