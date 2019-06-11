/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *  The du_int interface provides methods for 2, 4, 8 bytes data manipulation ( such as
 *    pack, unpack, multiply ).
 */

#ifndef DU_INT_H
#define DU_INT_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Converts 16 bits data to two 8bits data array in a little endian manner.
 *  For example a 16 bits data 0x1234 (4660 decimal) would be
 *     converted to 2 byte data array s as
 *       s[0]=0x34 s[1]=0x12 (little-endian order).
 *  @param[out] s  the destination byte data array.
 *  @param[in] u  the source 16 bits data.
 */
extern void du_uint16_pack(du_uint8 s[2], du_uint16 u);

/**
 *  Converts 16 bits data to two 8bits data array in a big endian manner.
 *  For example a 16 bits data 0x1234 (4660 decimal) would be
 *     converted to 2 byte data array s as
 *       s[0]=0x12 s[1]=0x34 (big-endian order).
 *  @param[out] s  the destination byte data array.
 *  @param[in] u  the source 16 bits data.
 */
extern void du_uint16_pack_big(du_uint8 s[2], du_uint16 u);

/**
 *  Converts two 8bits data array to 16 bits data in a little endian manner.
 *  For example 2 byte data array s[0]=0x34 s[1]=0x12 would be
 *     converted to a 16 bits data as 0x1234 (4660 decimal).
 *  @param[in] s  the source byte data array.
 *  @param[out] u  pointer to the destination 16 bits data.
 */
extern void du_uint16_unpack(const du_uint8 s[2], du_uint16* u);

/**
 *  Converts two 8bits data array to 16 bits data in a big endian manipulation.
 *  For example 2 byte data array s[0]=0x12 s[1]=0x34 would be
 *     converted to a 16 bits data as 0x1234 (4660 decimal).
 *  @param[in] s  the source byte data array.
 *  @param[out] u  pointer to the destination 16 bits data.
 */
extern void du_uint16_unpack_big(const du_uint8 s[2], du_uint16* u);

/**
 *  Converts 32 bits data to four 8bits data array in a little endian manner.
 *  For example a 32 bits data 0x12345678 (305419896 decimal) would be
 *     converted to 4 byte data array s as
 *       s[0]=0x78 s[1]=0x56 s[2]=0x34 s[3]=0x12 (little-endian order).
 *  @param[out] s  the destination byte data array.
 *  @param[in] u  the source 32 bits data.
 */
extern void du_uint32_pack(du_uint8 s[4], du_uint32 u);

/**
 *  Converts 32 bits data to four 8bits data array in a big endian manipulation.
 *  For example a 32 bits data 0x12345678 (305419896 decimal) would be
 *     converted to 4 byte data array s as
 *       s[0]=0x12 s[1]=0x34 s[2]=0x56 s[3]=0x78 (big-endian order).
 *  @param[out] s  the destination byte data array.
 *  @param[in] u  the source 32 bits data.
 */
extern void du_uint32_pack_big(du_uint8 s[4], du_uint32 u);

/**
 *  Converts four 8bits data array to 32 bits data in a little endian manner.
 *  For example 4 byte data array s[0]=0x78 s[1]=0x56 s[2]=0x34 s[3]=0x12  would be
 *     converted to a 32 bits data as 0x12345678 (305419896 decimal).
 *  @param[in] s  the source byte data array.
 *  @param[out] u  pointer to the destination 32 bits data.
 */
extern void du_uint32_unpack(const du_uint8 s[4], du_uint32* u);

/**
 *  Converts four 8bits data array to 32 bits data in a big endian manipulation.
 *  For example 4 byte data array s[0]=0x12 s[1]=0x34 s[2]=0x56 s[3]=0x78  would be
 *     converted to a 32 bits data as 0x12345678 (305419896 decimal).
 *  @param[in] s  the source byte data array.
 *  @param[out] u  pointer to the destination 32 bits data.
 */
extern void du_uint32_unpack_big(const du_uint8 s[4], du_uint32* u);

/**
 *  Converts 64 bits data to eight 8bits data array in a little endian manner.
 *  For example a 64 bits data 0x123456789abcdef0 (1311768467463790320 decimal) would be
 *  converted to 8 byte data array s as
 *  s[0]=0xf0 s[1]=0xde s[2]=0xbc s[3]=0x9a s[4]=0x78 s[5]=0x56 s[6]=0x34 s[7]=0x12
 *  (little-endian order).
 *  @param[out] s  the destination byte data array.
 *  @param[in] u  the source 64 bits data.
 */
extern void du_uint64_pack(du_uint8 s[8], du_uint64 u);

/**
 *  Converts 64 bits data to eight 8bits data array in a big endian manipulation.
 *  For example a 64 bits data 0x123456789abcdef0 (1311768467463790320 decimal) would be
 *  converted to 8 byte data array s as
 *  s[0]=0x12 s[1]=0x34 s[2]=0x56 s[3]=0x78 s[4]=0x9a s[5]=0xbc s[6]=0xde s[7]=0f0
 *  (big-endian order).
 *  @param[out] s  the destination byte data array.
 *  @param[in] u  the source 64 bits data.
 */
extern void du_uint64_pack_big(du_uint8 s[8], du_uint64 u);

/**
 *  Converts eight 8bits data array to 64 bits data in a little endian manner.
 *  For example 8 byte data array
 *  s[0]=0xf0 s[1]=0xde s[2]=0xbc s[3]=0x9a s[4]=0x78 s[5]=0x56 s[6]=0x34 s[7]=0x12
 *     would be converted to a 64 bits data as 0x123456789abcdef0 (1311768467463790320 decimal).
 *  @param[in] s  the source byte data array.
 *  @param[out] u  pointer to the destination 64 bits data.
 */
extern void du_uint64_unpack(const du_uint8 s[8], du_uint64* u);

/**
 *  Converts eight 8bits data array to 64 bits data in a big endian manner.
 *  For example 8 byte data array
 *  s[0]=0x12 s[1]=0x34 s[2]=0x56 s[3]=0x78 s[4]=0x9a s[5]=0xbc s[6]=0xde s[7]=0f0
 *     would be converted to a 64 bits data as 0x123456789abcdef0 (1311768467463790320 decimal).
 *  @param[in] s  the source byte data array.
 *  @param[out] u  pointer to the destination 64 bits data.
 */
extern void du_uint64_unpack_big(const du_uint8 s[8], du_uint64* u);

/**
 *  Multiplies two specified 16bits integer values.
 *  @param[in] a  a 16bits integer (the multiplicand).
 *  @param[in] b  a 16bits integer (the multiplier).
 *  @param[out] c  pointer to the 16bits integer that is the result of multiplying a and b.
 *  @return  true if the function succeeds.
 *           false if the function fails ( such as overflow ).
 */
extern du_bool du_int16_mult(du_int16 a, du_int16 b, du_int16* c);

/**
 *  Multiplies two specified 32bits integer values.
 *  @param[in] a  a 32bits integer (the multiplicand).
 *  @param[in] b  a 32bits integer (the multiplier).
 *  @param[out] c  pointer to a 32bits integer that is the result of multiplying a and b.
 *  @return  true if the function succeeds.
 *           false if the function fails ( such as overflow ).
 */
extern du_bool du_int32_mult(du_int32 a, du_int32 b, du_int32* c);

/**
 *  Multiplies two specified 64bits integer values.
 *  @param[in] a  a 64bits integer (the multiplicand).
 *  @param[in] b  a 64bits integer (the multiplier).
 *  @param[out] c  pointer to a 64bits integer that is the result of multiplying a and b.
 *  @return  true if the function succeeds.
 *           false if the function fails ( such as overflow ).
 */
extern du_bool du_int64_mult(du_int64 a, du_int64 b, du_int64* c);

/**
 *  Multiplies two specified 16bits unsigned integer values.
 *  @param[in] a  a 16bits unsigned integer (the multiplicand).
 *  @param[in] b  a 16bits unsigned integer (the multiplier).
 *  @param[out] c  pointer to a 16bits unsigned integer that is the result of multiplying a and b.
 *  @return  true if the function succeeds.
 *           false if the function fails ( such as overflow ).
 */
extern du_bool du_uint16_mult(du_uint16 a, du_uint16 b, du_uint16* c);

/**
 *  Multiplies two specified 32bits unsigned integer values.
 *  @param[in] a  a 32bits unsigned integer (the multiplicand).
 *  @param[in] b  a 32bits unsigned integer (the multiplier).
 *  @param[out] c  pointer to a 32bits unsigned integer that is the result of multiplying a and b.
 *  @return  true if the function succeeds.
 *           false if the function fails ( such as overflow ).
 */
extern du_bool du_uint32_mult(du_uint32 a, du_uint32 b, du_uint32* c);

/**
 *  Multiplies two specified 64bits unsigned integer values.
 *  @param[in] a  a 64bits unsigned integer (the multiplicand).
 *  @param[in] b  a 64bits unsigned integer (the multiplier).
 *  @param[out] c  pointer to a 64bits unsigned integer that is the result of multiplying a and b.
 *  @return  true if the function succeeds.
 *           false if the function fails ( such as overflow ).
 */
extern du_bool du_uint64_mult(du_uint64 a, du_uint64 b, du_uint64* c);

#ifdef __cplusplus
}
#endif

#endif
