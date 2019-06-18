/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

/** @file ddtcp_crypto_aes.h
 *  @brief The ddtcp_crypto_aes interface provides some methods for AES.<br>
 *         1, <b>ddtcp_crypto_aes_initialize</b> initializes the AES context.<br>
 *         2, To encrypt data, calls <b>ddtcp_crypto_aes_encrypt</b>.<br>
 *         3, To decrypt data, calls <b>ddtcp_crypto_aes_decrypt</b>.<br>
 *         4, <b>ddtcp_crypto_aes_finalize</b> finalizes the AES context.<br>
 */

#ifndef DDTCP_CRYPTO_AES_H
#define DDTCP_CRYPTO_AES_H

#include <ddtcp.h>
#include <du_type_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Type of AES context
 */
typedef void* ddtcp_crypto_aes; 

/**
 * Enumerated type of mode
 */
typedef enum {
    DDTCP_CRYPTO_AES_MODE_CBC = 1, /**< CBC mode */
} ddtcp_crypto_aes_mode;

/**
 * Enumerated type of AES block size
 */
typedef enum {
    DDTCP_CRYPTO_AES_BLOCK_SIZE_128 = 16, /**< 128bit */
} ddtcp_crypto_aes_block_size;

/**
 * Enumerated type of key size
 */
typedef enum {
    DDTCP_CRYPTO_AES_KEY_SIZE_128 = 16, /**< 128bit */
} ddtcp_crypto_aes_key_size;

/**
 * Enumerated type of how to pad
 */
typedef enum {
    DDTCP_CRYPTO_AES_PADDING_ZERO, /**< zero padding */
} ddtcp_crypto_aes_padding;

/**
 * Initializes AES context.
 * @param[in] mode AES mode
 * @param[in] padding how to pad
 * @param[in] key AES key, 128bit
 * @param[in] key_size size of key
 * @param[out] aes AES context
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_aes_initialize(ddtcp_crypto_aes_mode mode, ddtcp_crypto_aes_padding padding, const du_uint8* key, ddtcp_crypto_aes_key_size key_size, ddtcp_crypto_aes* aes);

/**
 * Finalizes AES context.
 * @param[in] aes AES context
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_aes_finalize(ddtcp_crypto_aes aes);

/**
 * Encrypts data
 * @param[in] aes AES context
 * @param[in] iv IV, 128bit
 * @param[in] clear buffer of clear data
 * @param[in] clear_size size of clear data buffer
 * @param[out] cipher buffer to write cipher data
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_aes_encrypt(ddtcp_crypto_aes aes, const du_uint8* iv, const du_uint8* clear, du_uint32 clear_size, du_uint8* cipher);

/**
 * Decrypts data
 * @param[in] aes AES context
 * @param[in] iv IV, 128bit
 * @param[in] cipher buffer of cipher data
 * @param[in] cipher_size size of cipher data buffer
 * @param[out] clear buffer to write clear data
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_aes_decrypt(ddtcp_crypto_aes aes, const du_uint8* iv, const du_uint8* cipher, du_uint32 cipher_size, du_uint8* clear);
    
#ifdef __cplusplus
}
#endif

#endif

