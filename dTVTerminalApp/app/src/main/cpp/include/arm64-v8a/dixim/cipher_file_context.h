/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: cipher_file_context.h 6759 2012-04-25 09:12:48Z gondo $ 
 */ 

#ifndef CIPHER_FILE_CONTEXT_H
#define CIPHER_FILE_CONTEXT_H

#include <du_type.h>
#include <secure_io.h>
#include <nmcc_db.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
    void* dummy;
} cipher_file_context_dummy, * cipher_file_context;

extern du_bool cipher_file_context_create(cipher_file_context* cfc);
extern void cipher_file_context_free(cipher_file_context cfc);
extern du_bool cipher_file_context_set_secure_io(cipher_file_context cfc, secure_io sio);
extern du_bool cipher_file_context_set_nmcc_db(cipher_file_context cfc, nmcc_db* db);
extern secure_io cipher_file_context_get_secure_io(cipher_file_context cfc);
extern nmcc_db* cipher_file_context_get_nmcc_db(cipher_file_context cfc);

#ifdef __cplusplus
}
#endif

#endif
