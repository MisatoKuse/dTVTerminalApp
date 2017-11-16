/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_env interface provides a method for getting an environment variable.
 */

#ifndef DU_ENV_H
#define DU_ENV_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Searches the list of environment variables for s and returns a pointer to
 *  the value in the environment.
 *  @param[in] s   environment variable name.
 *  @return  a pointer to the value of s in the environment, or NULL if there is no match.
 */
extern const du_uchar* du_env_get(const du_uchar* s);

#ifdef __cplusplus
}
#endif

#endif
