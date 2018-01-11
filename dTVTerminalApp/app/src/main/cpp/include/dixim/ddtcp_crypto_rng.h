/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id: ddtcp_crypto_rng.h 5823 2011-05-31 04:08:24Z gondo $ 
 */ 

/** @file ddtcp_crypto_rng.h
 *  @brief The ddtcp_crypto_rng interface provides some methods to generate random values.<br>
 *         1, <b>ddtcp_crypto_rng_f_initialize</b> initializes Random Number Generateor for Full Authentication(RNGf).<br>
 *         2, <b>ddtcp_crypto_rng_f</b> generates random value. Output <em>seed</em> is passed when this function is called next time<br>
 *         3, <b>ddtcp_crypto_rng_f_finalize</b> finalizes RNGf.<br>
 */

#ifndef DDTCP_CRYPTO_RNG_H
#define DDTCP_CRYPTO_RNG_H

#include <du_type_os.h>
#include <ddtcp.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DDTCP_CRYPTO_RNG_F_SEED_SIZE 20 /**< size of a seed of RNGf */

/*
 * Type of RNGf context
 */
typedef void* ddtcp_crypto_rng_f;

/**
 * Callback function interface to read the seed of RNGf.
 * @param[out] buf buffer to read seed, 160bit
 * @param[in] arg optional argument
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
typedef ddtcp_ret (*ddtcp_crypto_rng_f_read_seed_handler)(void* arg, du_uint8* seed);

/**
 * Callback function interface to write the seed of RNGf.
 * @param[in] buf buffer containing the data to be written, 160bit
 * @param[in] arg optional argument
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br> 
 */
typedef ddtcp_ret (*ddtcp_crypto_rng_f_write_seed_handler)(void* arg, const du_uint8* seed);

/**
 * Structure of information to update random seed of RNGf
 */
typedef struct {
    ddtcp_crypto_rng_f_read_seed_handler read; /**< callback function interface to read the seed */
    void* read_arg; /**< optional argument */
    ddtcp_crypto_rng_f_write_seed_handler write; /**< callback function interface to write the seed */
    void* write_arg; /**< optional argument */
} ddtcp_crypto_rng_f_update_seed_info;

/**
 * Initializes RNGf.
 * @param[in] update_seed_info information to update random seed
 * @param[out] rng_f RNGf context
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_rng_f_initialize(ddtcp_crypto_rng_f_update_seed_info* update_seed_info, ddtcp_crypto_rng_f* rng_f);

/**
 * Finalizes RNGf.
 * @param[in] rng_f RNGf context
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_rng_f_finalize(ddtcp_crypto_rng_f rng_f);

/**
 * Generates a random value for Full Authentication.
 * @param[in] rng_f RNGf context
 * @param[out] random random value, 160bit
 * @return DDTCP_RET_SUCCESS success<br>
 *         DDTCP_RET_FAILURE failure<br>
 */
extern ddtcp_ret ddtcp_crypto_rng_f_generate(ddtcp_crypto_rng_f rng_f, du_uint8* random);

#ifdef __cplusplus
}
#endif

#endif
