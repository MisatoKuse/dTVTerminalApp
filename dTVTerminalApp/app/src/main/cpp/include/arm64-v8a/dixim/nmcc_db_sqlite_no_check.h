/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: nmcc_db_sqlite_no_check.h 4920 2010-07-07 09:50:46Z gondo $ 
 */ 

#ifndef NMCC_DB_SQLITE_NO_CHECK_H
#define NMCC_DB_SQLITE_NO_CHECK_H

#include <nmcc_db_sqlite.h>

#ifdef __cplusplus
extern "C" {
#endif

extern nmcc_db* nmcc_db_sqlite_create_no_check(const du_uchar* db_dir_path, secure_io sio);

extern du_bool nmcc_db_sqlite_update_db_hash_no_check(const du_uchar* db_dir_path, secure_io sio);

#ifdef __cplusplus
}
#endif

#endif
