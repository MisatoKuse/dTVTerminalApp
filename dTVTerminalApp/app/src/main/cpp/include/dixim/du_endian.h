/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *   The du_endian interface provides a method for getting host byte order.
 */

#ifndef DU_ENDIAN_H
#define DU_ENDIAN_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  This structure contains host byte order types.
 */
typedef enum du_endian_type {
    DU_ENDIAN_TYPE_INVALID,

    /**
     *  Little endian.
     */
    DU_ENDIAN_TYPE_LITTLE,

    /**
     *  Big endian.
     */
    DU_ENDIAN_TYPE_BIG,
} du_endian_type;

/**
 *  Gets host byte order.
 *
 *  @return host byte order.
 */
extern du_endian_type du_endian_get(void);

/**
 *  Checks whether host byte order is little endian.
 *
 *  @retval 1 host byte order is little endian.
 *  @retval 0 otherwise.
 */
extern du_bool du_endian_is_little(void);

/**
 *  Checks whether host byte order is big endian.
 *
 *  @retval 1 host byte order is big endian.
 *  @retval 0 otherwise.
 */
extern du_bool du_endian_is_big(void);

#ifdef __cplusplus
}
#endif

#endif
