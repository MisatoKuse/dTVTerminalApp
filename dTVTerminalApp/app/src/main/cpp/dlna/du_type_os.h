/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

/** @file du_type_os.h
 * @brief The du_type_os interface provides a type cast for explicit conversion
 *  of the type of an object in a specific situation.
 *  The data types supported by dixim_util are defined in this file.
 */

#ifndef DU_TYPE_OS_H
#define DU_TYPE_OS_H

#include <stdint.h>
#include <wchar.h>

#ifdef __cplusplus
extern "C" {
#endif

#define DU_INT16_CONST(x) (x) /**< Defines <em>x</em> as a constant 16-bit signed interger. */

#define DU_UINT16_CONST(x) (x) /**< Defines <em>x</em> as a constant 16-bit unsigned interger. */

#define DU_INT32_CONST(x) (x)  /**< Defines <em>x</em> as a constant 32-bit signed interger. */

#define DU_UINT32_CONST(x) (x ## U) /**< Defines <em>x</em> as a constant 32-bit unsigned interger. */

#define DU_INT64_CONST(x) (x ## LL) /**< Defines <em>x</em> as a constant 64-bit signed interger. */

#define DU_UINT64_CONST(x) (x ## ULL) /**< Defines <em>x</em> as a constant 64-bit unsigned interger. */

#ifndef DIXIM_UTIL_DISABLE_FLOAT
#define DU_FLOAT32_CONST(x) (x ## F) /**< Defines <em>x</em> as a constant 32-bit float. */
# ifndef DIXIM_UTIL_DISABLE_FLOAT64
#define DU_FLOAT64_CONST(x) (x) /**< Defines <em>x</em> as a constant 64-bit float. */
# endif
#endif

#ifdef __cplusplus
typedef bool du_bool; /**< Boolean variable. */
#else
typedef _Bool du_bool; /**< Boolean variable. */
#endif
typedef unsigned char du_uchar;  /**< 8bit unsigned character. */
typedef int du_int;  /**< signed integer. */
typedef unsigned int du_uint; /**< unsigned integer. */
typedef int8_t du_int8; /**< 8-bit signed integer. */
typedef uint8_t du_uint8; /**< 8-bit unsigned integer. */
typedef int16_t du_int16; /**< 16-bit unsigned integer. */
typedef uint16_t du_uint16; /**< 16-bit unsigned integer. */
typedef int32_t du_int32;   /**< 32-bit signed integer. */
typedef uint32_t du_uint32; /**< 32-bit unsigned integer. */
typedef int64_t du_int64;   /**< 64-bit signed integer. */
typedef uint64_t du_uint64; /**< 64-bit unsigned integer. */
#ifndef DIXIM_UTIL_DISABLE_FLOAT
typedef float du_float32;   /**< 32-bit floating point numbers. */
# ifndef DIXIM_UTIL_DISABLE_FLOAT64
typedef double du_float64;  /**< 64-bit floating point numbers. */
# endif
#endif

typedef intptr_t du_intptr;  /**< Signed integral type for pointer precision. Use when casting a pointer to an integer to perform pointer arithmetic. */
typedef uintptr_t du_uintptr; /**< Unsigned integral type for pointer precision. Use when casting a pointer to a unsigned integer to perform pointer arithmetic. */

/**
 * A type cast to convert type of <em>x</em> to du_uchar * type.
 * @remark Use this macro for using char * type data like string literals as du_uchar * type data.
 */
#define DU_UCHAR(x) ((du_uchar*)x)

/**
 * A type cast to convert type of <em>x</em> to const du_uchar * type.
 * @remark Use this macro for using const char * type data like string literals as const du_uchar * type data.
 */
#define DU_UCHAR_CONST(x) ((const du_uchar*)x)

#define du_vsnprintf vsnprintf

#ifdef __cplusplus
}
#endif

#endif
