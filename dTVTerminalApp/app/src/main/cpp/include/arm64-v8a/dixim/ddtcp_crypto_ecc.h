/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

/** @file ddtcp_crypto_ecc.h
 *  @brief The ddtcp_crypto_ecc interface provides some methods for Elliptic Curve Cryptography(ECC).<br>
 *         1, <b>ddtcp_crypto_ecc_initialize</b> initializes the ECC context.<br>
 *         2, To generate the EC-DSA signature, calls <b>ddtcp_crypto_ecc_sign_data</b>.<br>
 *         3, To verify the EC-DSA signature, calls <b>ddtcp_crypto_ecc_verify_data</b>.<br>
 *         4, To generate the EC-DH shared secret, calls <b>ddtcp_crypto_ecc_get_first_phase_value</b> and <b>ddtcp_crypto_ecc_get_shared_secret</b>.<br>
 *         5, <b>ddtcp_crypto_ecc_finalize</b> finalizes the ECC context.<br>
 */

#ifndef DDTCP_CRYPTO_ECC_H
#define DDTCP_CRYPTO_ECC_H

#include <du_type_os.h>
#include <ddtcp_crypto_rng.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DDTCP_CRYPTO_ECC_EC_DSA_SIGNATURE_SIZE 40 /**< size of EC-DSA signature */
#define DDTCP_CRYPTO_ECC_EC_DSA_PUBLIC_KEY_SIZE 40 /**< size of EC-DSA public key */
#define DDTCP_CRYPTO_ECC_EC_DSA_PRIVATE_KEY_SIZE 20 /**< size of EC-DSA private key */
#define DDTCP_CRYPTO_ECC_EC_DH_FIRST_PHASE_VALUE_SIZE 40 /**< size of EC-DH first phase value */
#define DDTCP_CRYPTO_ECC_EC_DH_FIRST_PHASE_SECRET_SIZE 20 /**< size of EC-DH first phase secret */
#define DDTCP_CRYPTO_ECC_EC_DH_SHARED_SECRET 12 /**< size of EC-DH first shared secret */

/**
 * Type of ECC context
 */
typedef void* ddtcp_crypto_ecc;

/**
 * Enumerated type of key size
 */
typedef enum {
    DDTCP_CRYPT_ECC_KEY_SIZE_160 = 20, /**< 160bit */
} ddtcp_crypto_ecc_key_size;

/**
 * Initializes ECC context.
 * @param[in] key_size size of key
 * @param[in] rng_f initialized ddtcp_crypto_rng_f handle
 * @param[out] ecc ECC context
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_ecc_initialize(ddtcp_crypto_ecc_key_size key_size, ddtcp_crypto_rng_f rng_f, ddtcp_crypto_ecc* ecc);

/**
 * Finalizes ECC context.
 * @param[in] ecc ECC context
 * @return true success
 *         false failure
 */
extern ddtcp_ret ddtcp_crypto_ecc_finalize(ddtcp_crypto_ecc ecc);

/**
 * Generates a EC-DSA signature.
 * @param[in] ecc ECC context
 * @param[in] sign_key sign key, 160bit
 * @param[in] buf buffer of data to sign
 * @param[in] size number of bytes of buffer
 * @param[out] sign signature, 320bit
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_ecc_sign_data(ddtcp_crypto_ecc ecc, const du_uint8* sign_key, const du_uint8* buf, du_uint32 size, du_uint8* sign);

/**
 * Verifies the EC-DSA signature.
 * @param[in] ecc ECC context
 * @param[in] verify_key verify key, 320bit
 * @param[in] buf buffer of data to sign
 * @param[in] size number of bytes of buffer
 * @param[out] sign signature, 320bit
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_ecc_verify_data(ddtcp_crypto_ecc ecc, const du_uint8* verify_key, const du_uint8* buf, du_uint32 size, const du_uint8* sign);

/**
 * Gets a EC-DH first phase value.
 * @param[in] ecc ECC context
 * @param[out] x_v Xv, Diffie first phase value, 320bit
 * @param[out] x_k Xk, secret information, 160bit
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_ecc_get_first_phase_value(ddtcp_crypto_ecc ecc, du_uint8* x_v, du_uint8* x_k);

/**
 * Gets a EC-DH shared secret.
 * @param[in] ecc ECC context
 * @param[in] y_v Yv, Diffie first phase value generated, 320bit
 * @param[in] x_k Xk, secret information, 160bit
 * @param[out] secret shared secret, 96bit
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_ecc_get_shared_secret(ddtcp_crypto_ecc ecc, const du_uint8* y_v, const du_uint8* x_k, du_uint8* secret);

#ifdef __cplusplus
}
#endif

#endif



 
 
 
 
 
 
