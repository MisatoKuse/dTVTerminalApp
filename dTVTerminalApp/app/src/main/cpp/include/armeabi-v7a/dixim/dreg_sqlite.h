/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dreg_sqlite.h
 *  @brief The dreg interface provides accessing SQLite DB.
 * (such as create instance of dreg).
 */

#ifndef DREG_SQLITE_H
#define DREG_SQLITE_H

#include <dreg.h>

#include <drdb.h>

#include <du_mutex.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dreg_sqlite dreg_sqlite;

struct dreg_sqlite {
    dreg reg;
    drdb* rdb;
    drdb_connection* conn;
    du_mutex mutex;
};

/**
 * Creates a dreg structure for SQLite DB.
 * @param[in] fn path of SQLite DB.
 * @return pointer to the dreg structure for SQLite DB.
 */
extern dreg* dreg_sqlite_create(const du_uchar* fn);

extern dreg* dreg_sqlite_create2(const du_uchar* fn, const du_uchar* backup_fn);

#ifdef __cplusplus
}
#endif

#endif
