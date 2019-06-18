/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

#ifndef LIBTOM_AES_H
#define LIBTOM_AES_H

#include <ddtcp.h>
#include <du_type_os.h>
#include <ddtcp_crypto_aes.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void* libtom_aes;

extern ddtcp_ret libtom_aes_initialize(ddtcp_crypto_aes_mode mode, ddtcp_crypto_aes_padding padding, const du_uint8* key, ddtcp_crypto_aes_key_size key_size, libtom_aes* aes);
extern ddtcp_ret libtom_aes_finalize(libtom_aes aes);
extern ddtcp_ret libtom_aes_encrypt(libtom_aes aes, const du_uint8* iv, const du_uint8* clear, du_uint32 clear_size, du_uint8* cipher);
extern ddtcp_ret libtom_aes_decrypt(libtom_aes aes, const du_uint8* iv, const du_uint8* cipher, du_uint32 cipher_size, du_uint8* clear);
    
#ifdef __cplusplus
}
#endif

#endif

