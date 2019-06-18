/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$
 */

#ifndef CIPHER_FILE_SOURCE_CHUNKED_FILE_H
#define CIPHER_FILE_SOURCE_CHUNKED_FILE_H

#include <cipher_file_source.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct cipher_file_source cipher_file_source_chunked_file;

extern cipher_file_source* cipher_file_source_chunked_file_create(void);

extern du_bool cipher_file_source_chunked_file_set_chunk_size(cipher_file_source_chunked_file* x, du_uint64 chunk_size);

#ifdef __cplusplus
}
#endif

#endif
