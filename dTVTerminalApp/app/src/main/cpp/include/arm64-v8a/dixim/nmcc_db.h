/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: nmcc_db.h 4920 2010-07-07 09:50:46Z gondo $ 
 */ 

#ifndef NMCC_DB_H
#define NMCC_DB_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct nmcc_db nmcc_db;

typedef void (*nmcc_db_free)(nmcc_db* db);

#define NMCC_DB_CONTENT_ID_RECIVED_TIME_SEC_SIZE 4
#define NMCC_DB_CONTENT_ID_RANDOM_SIZE 3
#define NMCC_DB_CONTENT_ID_SIZE (NMCC_DB_CONTENT_ID_RECIVED_TIME_SEC_SIZE + NMCC_DB_CONTENT_ID_RANDOM_SIZE)

typedef du_bool (*nmcc_db_add_entry)(nmcc_db* db, const du_uint8 content_id[NMCC_DB_CONTENT_ID_SIZE], du_uint8 initial_count);

typedef du_bool (*nmcc_db_decrease_count)(nmcc_db* db, const du_uint8 content_id[NMCC_DB_CONTENT_ID_SIZE], du_uint8 count_decreased, du_uint8* present_count);

typedef du_bool (*nmcc_db_get_count)(nmcc_db* db, const du_uint8 content_id[NMCC_DB_CONTENT_ID_SIZE], du_uint8* count);

typedef du_bool (*nmcc_db_backup)(nmcc_db* db);

struct nmcc_db {
    nmcc_db_free free;
    nmcc_db_add_entry add_entry;
    nmcc_db_decrease_count decrease_count;
    nmcc_db_get_count get_count;
    nmcc_db_backup backup;
};

extern nmcc_db* g_nmcc_db;

#ifdef __cplusplus
}
#endif

#endif

