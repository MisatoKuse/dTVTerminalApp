/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: nmcc_db_sqlite.h 4920 2010-07-07 09:50:46Z gondo $ 
 */ 

#ifndef NMCC_DB_SQLITE_H
#define NMCC_DB_SQLITE_H

#include <dregdb.h>
#include <du_mutex.h>
#include "nmcc_db.h"
#include <secure_io.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct nmcc_db_sqlite {
    nmcc_db api;
    dregdb* reg;
    drdb* db;
    du_uchar* db_fn;
    du_uchar* db_bak_fn;
    du_uchar* last_query_fn;
    du_mutex mutex;
    du_bool sub;
    secure_io sio;
} nmcc_db_sqlite;

extern nmcc_db* nmcc_db_sqlite_create(const du_uchar* db_dir_path, secure_io sio);
//extern du_bool nmcc_db_sqlite_create_initial_db(const du_uchar* db_dir_path);
 
extern nmcc_db* nmcc_db_sqlite_create_impl(const du_uchar* db_fn, const du_uchar* db_bak_fn, const du_uchar* last_query_fn, du_bool sub, secure_io sio);
extern du_bool nmcc_db_sqlite_check_db_hash(nmcc_db* db, du_bool* hash_valid, du_bool* dummy_hash);
extern du_bool nmcc_db_sqlite_update_db_hash(nmcc_db* db);
extern du_bool nmcc_db_sqlite_copy_file(const du_uchar* oldfn, const du_uchar* newfn);
extern du_bool nmcc_db_sqlite_recover_db(nmcc_db* db, const du_uchar* db_fn, const du_uchar* db_bak_fn);

extern du_bool test_nmcc_db_sqlite_clear_db_hash(const du_uchar* db_dir_path, secure_io sio);

#ifdef __cplusplus
}
#endif

#endif
