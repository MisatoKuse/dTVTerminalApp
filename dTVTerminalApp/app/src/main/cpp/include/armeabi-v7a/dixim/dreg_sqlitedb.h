/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DREG_SQLITEDB_H
#define DREG_SQLITEDB_H

#include <drdb.h>

#include <du_str_array.h>
#include <du_type.h>
#include <du_uchar_array.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dreg_sqlitedb_get(drdb_connection* conn, const du_uchar* key, du_uint8* buf, du_uint32 len, du_uint32* nbytes);

extern du_bool dreg_sqlitedb_get2(drdb_connection* conn, const du_uchar* key, drdb_result_set** rs);

extern du_bool dreg_sqlitedb_put(drdb_connection* conn, const du_uchar* key, const du_uint8* buf, du_uint32 len);

extern du_bool dreg_sqlitedb_remove(drdb_connection* conn, const du_uchar* key);

extern du_bool dreg_sqlitedb_get_keys(drdb_connection* conn, du_str_array* keys);

extern du_bool dreg_sqlitedb_get_keys_prefix(drdb_connection* conn, const du_uchar* key_prefix, du_str_array* keys);

extern du_bool dreg_sqlitedb_clear(drdb_connection* conn);

#ifdef __cplusplus
}
#endif

#endif
