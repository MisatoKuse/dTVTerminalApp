/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: cipher_file_context_global.h 6759 2012-04-25 09:12:48Z gondo $ 
 */ 

#ifndef CIPHER_FILE_CONTEXT_GLOBAL_H
#define CIPHER_FILE_CONTEXT_GLOBAL_H

#include "cipher_file_context.h"

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool cipher_file_context_global_create(secure_io sio, const du_uchar* path);
extern du_bool cipher_file_context_global_create_2(secure_io sio, const du_uchar* main_path, const du_uchar* sub_path);
extern void cipher_file_context_global_free();
extern cipher_file_context cipher_file_context_global_get_instance();

#ifdef __cplusplus
}
#endif

#endif
