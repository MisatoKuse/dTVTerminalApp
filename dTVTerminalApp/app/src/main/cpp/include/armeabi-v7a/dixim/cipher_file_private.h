/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: $ 
 */ 

#ifndef CIPHER_FILE_PRIVATE_H
#define CIPHER_FILE_PRIVATE_H

#include <cipher_file.h>

#ifdef __cplusplus
extern "C" {
#endif

#define CIPHER_FILE_CONTAINER_FORMAT_TTS_BC 192 // XXX: for backward compatibility.

extern du_bool cipher_file_set_container_format_tts_bc(cipher_file* cf);

#ifdef __cplusplus
}
#endif

#endif
