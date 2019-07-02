/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

#ifndef DDTCP_UTIL_SCRAMBLER_H
#define DDTCP_UTIL_SCRAMBLER_H

#include <ddtcp.h>
#include <ddtcp_util_scrambler.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void* ddtcp_util_scrambler;

#define DDTCP_UTIL_SCRAMBLER_SEED_SIZE 20

typedef ddtcp_ret (*ddtcp_util_scrambler_rng_generate_handler)(du_uint8* random);

extern ddtcp_ret ddtcp_util_scrambler_create(ddtcp_util_scrambler* scrambler, ddtcp_util_scrambler_rng_generate_handler rng_generater_handler);
extern ddtcp_ret ddtcp_util_scrambler_scramble(ddtcp_util_scrambler scrambler, du_uint8* buf, du_uint32 size); 
extern ddtcp_ret ddtcp_util_scrambler_descramble(ddtcp_util_scrambler scrambler, du_uint8* buf, du_uint32* nbytes);
extern ddtcp_ret ddtcp_util_scrambler_reset(ddtcp_util_scrambler scrambler);
extern ddtcp_ret ddtcp_util_scrambler_free(ddtcp_util_scrambler* scrambler);

#ifdef __cplusplus
}
#endif

#endif

