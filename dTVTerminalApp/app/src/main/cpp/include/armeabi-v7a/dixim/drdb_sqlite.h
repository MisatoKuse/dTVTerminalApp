/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#ifndef DRDB_SQLITE_H
#define DRDB_SQLITE_H

#include <drdb.h>

#include <du_mutex.h>
#include <du_sync.h>

#include <sqlite3.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct drdb_sqlite {
    drdb api;
    du_uchar* db_fn;
    du_uchar* backup_fn;
    du_uchar* recovery_fn;
    du_mutex mutex;

    du_uint32 transaction_id_seq;
    du_uint32 number_transactions;
    du_uint32 number_syncs;
    du_mutex ro_mutex;
    du_mutex rw_mutex;
    du_mutex transaction_id_mutex;
    du_mutex sync_mutex;
    du_sync sync;
} drdb_sqlite;

extern drdb* drdb_sqlite_create(const du_uchar* db_fn);

extern du_bool drdb_sqlite_recover(drdb* rdb);

extern du_bool drdb_sqlite_set_backup_filename(drdb* rdb, const du_uchar* backup_fn);

extern int drdb_sqlite_get_error_code(drdb_connection* conn);

#ifdef __cplusplus
}
#endif

#endif
