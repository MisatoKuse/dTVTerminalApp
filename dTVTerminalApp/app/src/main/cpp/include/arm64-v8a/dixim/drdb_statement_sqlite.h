/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#ifndef DRDB_STATEMENT_SQLITE_H
#define DRDB_STATEMENT_SQLITE_H

#include <drdb_connection_sqlite.h>
#include <sqlite3.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct drdb_statement_sqlite {
    drdb_statement api;
    drdb_connection_sqlite* conn;
    sqlite3_stmt* stmt;
} drdb_statement_sqlite;

extern drdb_statement_sqlite* drdb_statement_sqlite_create(drdb_connection_sqlite* conn, const du_uchar* sql);

#ifdef __cplusplus
}
#endif

#endif
