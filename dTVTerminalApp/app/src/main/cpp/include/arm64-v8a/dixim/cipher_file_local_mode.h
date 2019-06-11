/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

#ifndef CIPHER_FILE_LOCAL_MODE_H
#define CIPHER_FILE_LOCAL_MODE_H

#include <du_type.h>
#include <cipher_file.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void* cipher_file_local_mode;

extern du_bool cipher_file_local_mode_initialize(const du_uint8 initial_data[CIPHER_FILE_LOCAL_MODE_INITIAL_DATA_SIZE], cipher_file_local_mode* lm);
extern void cipher_file_local_mode_finalize(cipher_file_local_mode lm);
extern du_bool cipher_file_local_mode_decrypt(cipher_file_local_mode lm, const du_uint8* cipher, du_uint32 size, du_uint8* clear);

#ifdef __cplusplus
}
#endif

#endif
