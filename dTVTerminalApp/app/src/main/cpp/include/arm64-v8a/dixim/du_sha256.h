/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 */

#ifndef DU_SHA256_H
#define DU_SHA256_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 */
#define DU_SHA256_DIGEST_SIZE 32

/**
 *  Structure of du_sha256.
 */
typedef struct {
    du_uint8 data[64];
    du_uint32 datalen;
    du_uint32 bitlen[2];
    du_uint32 state[8];
} du_sha256;

/**
 *  Initializes a du_sha256 data area.
 *  @param[out] ctx du_sha256 data area.
 */
extern void du_sha256_init(du_sha256* ctx);

/**
 *  Writes a successive block of data to be hashed.
 *  This function can be called repeatedly.
 *  @param[in,out] ctx  pointer to the du_sha256 structure.
 *  @param[in] input the data to be digested.
 *  @param[in] len the length of input data.
 */
extern void du_sha256_write(du_sha256* ctx, const du_uint8* input, du_uint32 len);

/**
 *  Finishes the current hash computation and copies
 *  the digest value into a digest.
 *  @param[in] ctx  pointer to the du_sha256 structure.
 *  @param[out] digest digest result.
 */
extern void du_sha256_final(du_sha256* ctx, du_uint8 digest[DU_SHA256_DIGEST_SIZE]);

#ifdef __cplusplus
}
#endif

#endif
