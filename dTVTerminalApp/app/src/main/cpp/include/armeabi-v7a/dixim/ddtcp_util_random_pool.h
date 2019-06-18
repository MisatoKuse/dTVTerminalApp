/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

#ifndef DDTCP_UTIL_RANDOM_POOL_H
#define DDTCP_UTIL_RANDOM_POOL_H

#include <ddtcp.h>
#include <ddtcp_util_log.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DDTCP_UTIL_RANDOM_POOL_COPY_IMPLIMENT_MACRO

#define DDTCP_UTIL_RANDOM_POOL_SEED_SIZE 20
#define DDTCP_UTIL_RANDOM_POOL_SIZE (DDTCP_UTIL_RANDOM_POOL_SEED_SIZE * 100)

typedef ddtcp_ret (*ddtcp_util_random_pool_rng_generate_handler)(du_uint8* random);

extern ddtcp_ret ddtcp_util_random_pool_initialize(ddtcp_util_random_pool_rng_generate_handler rng_generate_handler);
extern ddtcp_ret ddtcp_util_random_pool_reset(void);

extern du_uint8 g_ddtcp_util_random_pool[DDTCP_UTIL_RANDOM_POOL_SIZE];
extern du_uint8 g_ddtcp_util_random_pool_counter;
extern du_bool g_ddtcp_util_random_pool_initialized;

#ifdef DDTCP_UTIL_RANDOM_POOL_COPY_IMPLIMENT_MACRO
#define ddtcp_util_random_pool_copy(s, n) { \
    volatile du_uint8* _random_pool_copy_pos = ((du_uint8*)(s)); \
    du_uint32 _random_pool_copy_size = ((du_uint32)(n)); \
 \
    if (!g_ddtcp_util_random_pool_initialized) { \
        /* ddtcp_util_log_printf((ddtcp_util_log_category, DU_UCHAR("XXXXX ddtcp_util_random_pool_copy(), random pool is not initialized"))); */ \
    } \
 \
    while (_random_pool_copy_size--) { \
        *(_random_pool_copy_pos++) = g_ddtcp_util_random_pool[g_ddtcp_util_random_pool_counter++ % sizeof(g_ddtcp_util_random_pool)]; \
    } \
}
#endif

#ifdef __cplusplus
}
#endif

#endif
