/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: nmcc_db_sqlite_2.h 2952 2008-10-30 03:11:46Z gondo $ 
 */ 

#ifndef NMCC_DB_SQLITE_2_H
#define NMCC_DB_SQLITE_2_H

#include <dregdb.h>
#include <du_mutex.h>
#include "nmcc_db.h"
#include "nmcc_db_sqlite.h"
#include <secure_io.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct nmcc_db_sqlite_2 {
    nmcc_db api;
    nmcc_db* main;
    nmcc_db* sub;
    du_mutex mutex;
} nmcc_db_sqlite_2;

extern nmcc_db* nmcc_db_sqlite_2_create(const du_uchar* main_db_dir_path, const du_uchar* sub_db_dir_path, secure_io sio);

#ifdef __cplusplus
}
#endif

#endif
