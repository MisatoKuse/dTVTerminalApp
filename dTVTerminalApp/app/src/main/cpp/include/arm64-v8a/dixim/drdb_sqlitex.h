/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#ifndef DRDB_SQLITEX_H
#define DRDB_SQLITEX_H

#include <du_type.h>

#include <sqlite3.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool drdb_sqlitex_open(const du_uchar* db_fn, sqlite3** db);

extern du_bool drdb_sqlitex_open2(const du_uchar* db_fn, du_bool read_only, sqlite3** db);

extern du_bool drdb_sqlitex_exec(sqlite3* db, const du_uchar* sql);

extern du_bool drdb_sqlitex_prepare(sqlite3* db, const du_uchar* sql, sqlite3_stmt** stmt);

extern du_bool drdb_sqlitex_step(sqlite3* db, sqlite3_stmt* stmt, du_bool* done);

extern du_bool drdb_sqlitex_integrity_check(sqlite3* db, du_bool quick_check, du_bool* ok);

extern du_bool drdb_sqlitex_reset(sqlite3* db, sqlite3_stmt* stmt);

#ifdef __cplusplus
}
#endif

#endif
