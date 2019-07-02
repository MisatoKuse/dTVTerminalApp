/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id
 */ 

#ifndef CIPHER_FILE_TRUNCATE_H
#define CIPHER_FILE_TRUNCATE_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool cipher_file_truncate(const char* fn, uint64_t size);

#ifdef __cplusplus
}
#endif

#endif
