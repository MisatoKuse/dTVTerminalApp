/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 *
 * $Id: $
 */

#ifndef CIPHER_FILE_INTER_SOURCE_H
#define CIPHER_FILE_INTER_SOURCE_H

#ifdef __cplusplus
extern "C" {
#endif

typedef struct cipher_file_inter_source cipher_file_inter_source;

struct cipher_file_inter_source {
    void (*free)(cipher_file_inter_source* x);
    du_bool (*get_status)(cipher_file_inter_source* x, const du_uchar* fn, du_file_status* buf);
    du_bool (*get_fstatus)(cipher_file_inter_source* x, du_file_status* buf);
    du_bool (*open_read)(cipher_file_inter_source* x, const du_uchar* fn);
    du_bool (*open_truncate)(cipher_file_inter_source* x, const du_uchar* fn);
    du_bool (*open_readwrite)(cipher_file_inter_source* x, const du_uchar* fn);
    du_bool (*close)(cipher_file_inter_source* x);
    du_bool (*read)(cipher_file_inter_source* x, du_uint8* buf, du_uint32 len, du_uint32* nbytes);
    du_bool (*write_all)(cipher_file_inter_source* x, const du_uint8* buf, du_uint32 len);
    du_bool (*flush)(cipher_file_inter_source* x);
    du_bool (*seek_set)(cipher_file_inter_source* x, du_uint64 pos, du_uint64* new_pos);
    du_bool (*seek_pos)(cipher_file_inter_source* x, du_uint64* pos);

    // XXX: for AES
    du_bool (*set_key)(cipher_file_inter_source* x, du_uint8 key[DDTCP_CRYPTO_AES_BLOCK_SIZE_128]);
    du_bool (*set_iv)(cipher_file_inter_source* x, du_uint8 iv[DDTCP_CRYPTO_AES_BLOCK_SIZE_128]);
    du_bool (*cipher_read_block)(cipher_file_inter_source* x, du_uint8* buf, du_uint32 len, du_uint32* nbytes);
    du_bool (*cipher_write_block_all)(cipher_file_inter_source* x, const du_uint8* buf, du_uint32 len);
    du_bool (*cipher_read)(cipher_file_inter_source* x, du_uint8* buf, du_uint32 len, du_uint32* nbytes);
    du_bool (*cipher_write_all)(cipher_file_inter_source* x, const du_uint8* buf, du_uint32 len);
    du_bool (*cipher_write_end)(cipher_file_inter_source* x, du_uint16* padding_size);
};

extern cipher_file_inter_source* cipher_file_inter_source_create(du_uint64 chunk_size);

#ifdef __cplusplus
}
#endif

#endif
