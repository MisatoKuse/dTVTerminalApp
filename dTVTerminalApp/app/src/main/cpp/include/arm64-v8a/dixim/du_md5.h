/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 */

#ifndef DU_MD5_H
#define DU_MD5_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 */
#define DU_MD5_DIGEST_SIZE 16

/**
 *  Structure of du_md5.
 */
typedef struct {
    du_uint32 A;
    du_uint32 B;
    du_uint32 C;
    du_uint32 D;
    du_uint32 nblocks;
    du_uint8 buf[64];
    du_uint32 count;
} du_md5;

/**
 *  Initializes a du_md5 data area.
 *  @param[out] ctx du_md5 data area.
 */
extern void du_md5_init(du_md5* ctx);

/**
 *  Writes a successive block of data to be hashed.
 *  This function can be called repeatedly.
 *  @param[in,out] ctx  pointer to the du_md5 structure.
 *  @param[in] input the data to be digested.
 *  @param[in] len the length of input data.
 */
extern void du_md5_write(du_md5* ctx, const du_uint8* input, du_uint32 len);

/**
 *  Finishes the current hash computation and copies
 *  the digest value into a digest.
 *  @param[in] ctx  pointer to the du_md5 structure.
 *  @param[out] digest digest result.
 */
extern void du_md5_final(du_md5* ctx, du_uint8 digest[DU_MD5_DIGEST_SIZE]);

#ifdef __cplusplus
}
#endif

#endif
