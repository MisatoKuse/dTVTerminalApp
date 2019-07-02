/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#ifndef DRDB_RESULT_SET_SQLITE_H
#define DRDB_RESULT_SET_SQLITE_H

#include <drdb_statement_sqlite.h>
#include <sqlite3.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct drdb_result_set_sqlite {
    drdb_result_set api;
    drdb_statement_sqlite* stmt;
} drdb_result_set_sqlite;

extern drdb_result_set_sqlite* drdb_result_set_sqlite_create(drdb_statement_sqlite* stmt);

#ifdef __cplusplus
}
#endif

#endif
