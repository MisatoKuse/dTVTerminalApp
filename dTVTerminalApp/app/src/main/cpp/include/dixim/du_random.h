/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_random interface provides method for generating a random number.
 */

#ifndef DU_RANDOM_H
#define DU_RANDOM_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Returns a random number.
 *  @return  a random number
 */
extern du_uint32 du_random_get(void);

#ifdef __cplusplus
}
#endif

#endif
