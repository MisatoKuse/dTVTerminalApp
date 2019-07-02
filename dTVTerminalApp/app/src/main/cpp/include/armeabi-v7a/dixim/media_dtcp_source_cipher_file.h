/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$
 */

#ifndef MEDIA_DTCP_SOURCE_CIPHER_FILE_H
#define MEDIA_DTCP_SOURCE_CIPHER_FILE_H

#include <dmedia_mpeg_ts_source.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct media_dtcp_source_cipher_file media_dtcp_source_cipher_file;

struct media_dtcp_source_cipher_file {
    dmedia_mpeg_ts_source b;
    du_bool (*get_status)(media_dtcp_source_cipher_file* x, du_file_status* fs);
};

extern media_dtcp_source_cipher_file* media_dtcp_source_cipher_file_create(const du_uchar* path);

extern media_dtcp_source_cipher_file* media_dtcp_source_cipher_file_create2(const du_uchar* path);

#ifdef __cplusplus
}
#endif

#endif
