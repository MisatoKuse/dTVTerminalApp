/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#ifndef DRDB_CONNECTION_SQLITE_H
#define DRDB_CONNECTION_SQLITE_H

#include <drdb_sqlite.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct drdb_connection_sqlite {
    drdb_connection api;
    drdb_sqlite* rdb;
    sqlite3* db;
    du_uint32 transaction_id;
    drdb_transaction_mode transaction_mode;
} drdb_connection_sqlite;

extern drdb_connection_sqlite* drdb_connection_sqlite_create(drdb_sqlite* rdb);

#ifdef __cplusplus
}
#endif

#endif
