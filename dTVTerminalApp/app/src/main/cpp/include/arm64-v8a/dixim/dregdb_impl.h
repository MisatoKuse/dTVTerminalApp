/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dregdb.h
 *  @brief The dregdb interface provides various basic methods for accessing CDS DB
 * (such as opening DB, closing DB, searching, updating, deleting data).
 */

#ifndef DREGDB_IMPL_H
#define DREGDB_IMPL_H

#include <dregdb.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dregdb_impl {
    dregdb api;
    du_uchar* table_name;
    du_uchar_array ua;
} dregdb_impl;

extern dregdb_impl* dregdb_impl_create(const du_uchar* table_name);

#ifdef __cplusplus
}
#endif

#endif
