/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

/** @file ddtcp_crypto_sha.h
 *  @brief The ddtcp_crypto_sha interface provides some methods to calculate SHA message digests.<br>
 */

#ifndef DDTCP_CRYPTO_SHA_H
#define DDTCP_CRYPTO_SHA_H

#include <ddtcp.h>
#include <du_type_os.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DDTCP_CRYPTO_SHA1_DIGEST_SIZE 20 /**< size of a SHA1 digest */

/**
 * Calculates a SHA1 digest.
 * @param[in] buf buffer of data to hash
 * @param[in] size number of bytes of buffer
 * @param[out] digest SHA1 digest, 160bit
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_sha1(const du_uint8* buf, du_uint32 size, du_uint8 digest[DDTCP_CRYPTO_SHA1_DIGEST_SIZE]);

#ifdef __cplusplus
}
#endif

#endif
