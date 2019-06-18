/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

#ifndef DDTCP_CRYPTO_H
#define DDTCP_CRYPTO_H

#include <ddtcp_crypto_aes.h>
#include <ddtcp_crypto_ecc.h>
#include <ddtcp_crypto_rng.h>
#include <ddtcp_crypto_sha.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void* ddtcp_crypto_rng_f_update_arg;

extern ddtcp_ret ddtcp_crypto_rng_f_generate_exclusive_max(ddtcp_crypto_rng_f rng_f, du_uint8* random, const du_uint8* max);
extern du_int32 ddtcp_crypto_compare(const du_uint8* a, const du_uint8* b, du_uint8 size);
extern ddtcp_ret ddtcp_crypto_rng_f_read_seed(void* arg, du_uint8* seed);
extern ddtcp_ret ddtcp_crypto_rng_f_write_seed(void* arg, const du_uint8* seed);
extern ddtcp_ret ddtcp_crypto_rng_f_2(ddtcp_crypto_rng_f rng_f, du_uint8 *buf, du_uint32 size);

/**
 * Mod Add
 * @param[in] x 160bit
 * @param[in] y 160bit
 * @param[in] m 160bit mod --- XXX: dummy!!! 160bit mod only
 * @param[out] r 160bit result
 */
extern void ddtcp_crypto_mod_add(const du_uint8* x, const du_uint8* y, const du_uint8* m, du_uint8* r);

extern const du_uint8 ddtcp_crypto_bn_0[];
extern const du_uint8 ddtcp_crypto_bn_1[];
extern const du_uint8 ddtcp_crypto_bn_64[];
extern const du_uint8 ddtcp_crypto_bn_160[];

extern ddtcp_ret ddtcp_crypto_simple_rng_f_initialize(du_uint8* seed);
extern ddtcp_ret ddtcp_crypto_simple_rng_f_generate(du_uint8* random);
extern ddtcp_ret ddtcp_crypto_simple_rng_f_initialize_temporary(void);

#ifdef __cplusplus
}
#endif

#endif
