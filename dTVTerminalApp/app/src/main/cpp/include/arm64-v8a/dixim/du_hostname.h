/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_hostname interface provides the method to get the host name of the
 *   local computer.
 */

#ifndef DU_HOSTNAME_H
#define DU_HOSTNAME_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Maximum size of a host name string.
 */
#define DU_HOSTNAME_SIZE 256

/**
 *  Gets the standard host name for the local computer.
 *  @param[out] hostname pointer to the string area to save the host name.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_hostname_get(du_uchar hostname[DU_HOSTNAME_SIZE]);

#ifdef __cplusplus
}
#endif

#endif
