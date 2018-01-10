/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 *
 * $Id: cipher_file_source.h 7714 2013-02-26 11:32:36Z umezaki $
 */

#ifndef CIPHER_FILE_SOURCE_H
#define CIPHER_FILE_SOURCE_H

#include <du_type.h>
#include <du_file.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct cipher_file_source cipher_file_source;

struct cipher_file_source {
    void (*free)(cipher_file_source* x);
    du_bool (*get_status)(cipher_file_source* x, const du_uchar* fn, du_file_status* buf);
    du_bool (*get_fstatus)(cipher_file_source* x, du_file_status* buf);
    du_bool (*open_read)(cipher_file_source* x, const du_uchar* fn);
    du_bool (*open_truncate)(cipher_file_source* x, const du_uchar* fn);
    du_bool (*open_readwrite)(cipher_file_source* x, const du_uchar* fn);
    du_bool (*close)(cipher_file_source* x);
    du_bool (*read)(cipher_file_source* x, du_uint8* buf, du_uint32 len, du_uint32* nbytes);
    du_bool (*write_all)(cipher_file_source* x, const du_uint8* buf, du_uint32 len);
    du_bool (*flush)(cipher_file_source* x);
    du_bool (*seek_set)(cipher_file_source* x, du_uint64 pos, du_uint64* new_pos);
    du_bool (*seek_pos)(cipher_file_source* x, du_uint64* pos);
};

#ifdef __cplusplus
}
#endif

#endif
