/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The limits for integer types are defined in this header file.
 */


#ifndef DU_LIMITS_H
#define DU_LIMITS_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *   Maximum value of type du_int16.
 */
#define DU_INT16_MAX DU_INT16_CONST(0x7fff)

/**
 *   Minimum value of type du_int16.
 */
#define DU_INT16_MIN (-DU_INT16_MAX - DU_INT16_CONST(1))

/**
 *   Maximum value of type du_uint16.
 */
#define DU_UINT16_MAX DU_UINT16_CONST(0xffff)

/**
 *   Maximum value of type du_int32.
 */
#define DU_INT32_MAX DU_INT32_CONST(0x7fffffff)

/**
 *   Minimum value of type du_int32.
 */
#define DU_INT32_MIN (-DU_INT32_MAX - DU_INT32_CONST(1))

/**
 *   Maximum value of type du_uint32.
 */
#define DU_UINT32_MAX DU_UINT32_CONST(0xffffffff)

/**
 *   Maximum value of type du_int64.
 */
#define DU_INT64_MAX DU_INT64_CONST(0x7fffffffffffffff)

/**
 *   Minimum value of type du_int64.
 */
#define DU_INT64_MIN (-DU_INT64_MAX - DU_INT64_CONST(1))

/**
 *   Maximum value of type du_uint64.
 */
#define DU_UINT64_MAX DU_UINT64_CONST(0xffffffffffffffff)

#ifdef __cplusplus
}
#endif

#endif
