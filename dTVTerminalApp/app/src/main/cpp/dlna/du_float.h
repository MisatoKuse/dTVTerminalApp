/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *   The du_float interface provides methods for float data manipulation
 *   (such as pack and unpack).
 */

#ifndef DU_FLOAT_H
#define DU_FLOAT_H

#include "du_type.h"

#ifdef __cplusplus
extern "C" {
#endif

#ifndef DIXIM_UTIL_DISABLE_FLOAT

/**
 *  Converts 32 bits data to four 8bits data array in a little endian manner.
 *
 *  @param[out] s the destination byte data array.
 *  @param[in] f the source 32 bits data.
 */
extern void du_float32_pack(du_uint8 s[4], du_float32 f);

/**
 *  Converts 32 bits data to four 8bits data array in a big endian manner.
 *
 *  @param[out] s the destination byte data array.
 *  @param[in] f the source 32 bits data.
 */
extern void du_float32_pack_big(du_uint8 s[4], du_float32 f);

/**
 *  Converts four 8bits data array to 32 bits data in a little endian manner.
 *
 *  @param[in] s the source byte data array.
 *  @param[out] f pointer to the destination 32 bits data.
 */
extern void du_float32_unpack(const du_uint8 s[4], du_float32* f);

/**
 *  Converts four 8bits data array to 32 bits data in a big endian manner.
 *  @param[in] s the source byte data array.
 *  @param[out] f pointer to the destination 32 bits data.
 */
extern void du_float32_unpack_big(const du_uint8 s[4], du_float32* f);

# ifndef DIXIM_UTIL_DISABLE_FLOAT64

/**
 *  Converts 64 bits data to eight 8bits data array in a little endian manner.
 *
 *  @param[out] s the destination byte data array.
 *  @param[in] f the source 64 bits data.
 */
extern void du_float64_pack(du_uint8 s[8], du_float64 f);

/**
 *  Converts 64 bits data to eight 8bits data array in a big endian manner.
 *
 *  @param[out] s the destination byte data array.
 *  @param[in] f the source 64 bits data.
 */
extern void du_float64_pack_big(du_uint8 s[8], du_float64 f);

/**
 *  Converts eight 8bits data array to 64 bits data in a little endian manner.
 *
 *  @param[in] s the source byte data array.
 *  @param[out] f pointer to the destination 64 bits data.
 */
extern void du_float64_unpack(const du_uint8 s[8], du_float64* f);

/**
 *  Converts eight 8bits data array to 64 bits data in a big endian manner.
 *
 *  @param[in] s the source byte data array.
 *  @param[out] f pointer to the destination 64 bits data.
 */
extern void du_float64_unpack_big(const du_uint8 s[8], du_float64* f);

# endif
#endif

#ifdef __cplusplus
}
#endif

#endif
