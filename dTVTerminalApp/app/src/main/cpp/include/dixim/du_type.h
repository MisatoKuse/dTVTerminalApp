/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_type interface provides various data types.
 */

#ifndef DU_TYPE_H
#define DU_TYPE_H

#include <du_type_os.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Type of 32-bit ID.
 */
typedef du_uint32 du_id32;

/**
 *  An invalid value for du_id32.
 */
#define DU_ID32_INVALID 0

#ifdef __cplusplus
}
#endif

#endif
