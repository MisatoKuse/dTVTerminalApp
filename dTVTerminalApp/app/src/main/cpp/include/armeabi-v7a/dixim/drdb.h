/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#ifndef DRDB_H
#define DRDB_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct drdb drdb;

typedef struct drdb_connection drdb_connection;

typedef struct drdb_statement drdb_statement;

typedef struct drdb_result_set drdb_result_set;

/**
 * Transaction mode.
 */
typedef enum {
    DRDB_TRANSACTION_MODE_UNKNOWN,
    DRDB_TRANSACTION_MODE_READ_ONLY,  /**< Transaction only of reading. */
    DRDB_TRANSACTION_MODE_READ_WRITE,  /**< Transaction of reading and writing. */
} drdb_transaction_mode;

typedef void (*drdb_free)(drdb* rdb);

typedef du_bool (*drdb_open)(drdb* rdb);

typedef du_bool (*drdb_close)(drdb* rdb);

typedef du_bool (*drdb_create_connection)(drdb* rdb, drdb_connection** conn);

struct drdb {
    drdb_free free;
    drdb_open open;
    drdb_close close;
    drdb_create_connection create_connection;
};

typedef du_bool (*drdb_connection_close)(drdb_connection* conn);

typedef du_bool (*drdb_connection_begin)(drdb_connection* conn);

typedef du_bool (*drdb_connection_begin2)(drdb_connection* conn, drdb_transaction_mode transaction_mode);

typedef du_bool (*drdb_connection_commit)(drdb_connection* conn);

typedef du_bool (*drdb_connection_rollback)(drdb_connection* conn);

typedef du_bool (*drdb_connection_prepare_statement)(drdb_connection* conn, const du_uchar* sql, drdb_statement** stmt);

struct drdb_connection {
    drdb_connection_close close;
    drdb_connection_begin begin;
    drdb_connection_begin2 begin2;
    drdb_connection_commit commit;
    drdb_connection_rollback rollback;
    drdb_connection_prepare_statement prepare_statement;
};

typedef du_bool (*drdb_statement_close)(drdb_statement* stmt);

typedef du_bool (*drdb_statement_set_boolean)(drdb_statement* stmt, du_uint32 index, du_bool value);

typedef du_bool (*drdb_statement_set_int)(drdb_statement* stmt, du_uint32 index, du_int32 value);

typedef du_bool (*drdb_statement_set_uint)(drdb_statement* stmt, du_uint32 index, du_uint32 value);

typedef du_bool (*drdb_statement_set_int64)(drdb_statement* stmt, du_uint32 index, du_int64 value);

typedef du_bool (*drdb_statement_set_uint64)(drdb_statement* stmt, du_uint32 index, du_uint64 value);

typedef du_bool (*drdb_statement_set_string)(drdb_statement* stmt, du_uint32 index, const du_uchar* value, du_uint32 length);

typedef du_bool (*drdb_statement_set_bytes)(drdb_statement* stmt, du_uint32 index, const du_uint8* value, du_uint32 length);

typedef du_bool (*drdb_statement_set_null)(drdb_statement* stmt, du_uint32 index);

typedef du_bool (*drdb_statement_execute_update)(drdb_statement* stmt);

typedef du_bool (*drdb_statement_execute_query)(drdb_statement* stmt, drdb_result_set** rs);

typedef du_bool (*drdb_statement_reset)(drdb_statement* stmt);

struct drdb_statement {
    drdb_statement_close close;
    drdb_statement_set_boolean set_boolean;
    drdb_statement_set_int set_int;
    drdb_statement_set_uint set_uint;
    drdb_statement_set_int64 set_int64;
    drdb_statement_set_uint64 set_uint64;
    drdb_statement_set_string set_string;
    drdb_statement_set_bytes set_bytes;
    drdb_statement_set_null set_null;
    drdb_statement_execute_update execute_update;
    drdb_statement_execute_query execute_query;
    drdb_statement_reset reset;
};

typedef drdb_statement* (*drdb_result_set_get_statement)(drdb_result_set* rs);

typedef du_bool (*drdb_result_set_close)(drdb_result_set* rs);

typedef du_bool (*drdb_result_set_next)(drdb_result_set* rs, du_bool* next);

typedef du_bool (*drdb_result_set_get_boolean)(drdb_result_set* rs, du_uint32 index, du_bool* value);

typedef du_bool (*drdb_result_set_get_int)(drdb_result_set* rs, du_uint32 index, du_int32* value);

typedef du_bool (*drdb_result_set_get_uint)(drdb_result_set* rs, du_uint32 index, du_uint32* value);

typedef du_bool (*drdb_result_set_get_int64)(drdb_result_set* rs, du_uint32 index, du_int64* value);

typedef du_bool (*drdb_result_set_get_uint64)(drdb_result_set* rs, du_uint32 index, du_uint64* value);

typedef du_bool (*drdb_result_set_get_string)(drdb_result_set* rs, du_uint32 index, const du_uchar** value);

typedef du_bool (*drdb_result_set_get_bytes)(drdb_result_set* rs, du_uint32 index, const du_uint8** value, du_uint32* length);

typedef du_bool (*drdb_result_set_is_null)(drdb_result_set* rs, du_uint32 index, du_bool* flag);

struct drdb_result_set {
    drdb_result_set_get_statement get_statement;
    drdb_result_set_close close;
    drdb_result_set_next next;
    drdb_result_set_get_boolean get_boolean;
    drdb_result_set_get_int get_int;
    drdb_result_set_get_uint get_uint;
    drdb_result_set_get_int64 get_int64;
    drdb_result_set_get_uint64 get_uint64;
    drdb_result_set_get_string get_string;
    drdb_result_set_get_bytes get_bytes;
    drdb_result_set_is_null is_null;
};

#ifdef __cplusplus
}
#endif

#endif
