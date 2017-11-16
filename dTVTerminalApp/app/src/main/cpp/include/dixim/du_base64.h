/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *  The du_base64 interface provides data encoding and decoding as specified
 *  in RFC 3548. Base64 encoding is the scheme used to transmit binary data.
 *  Base64 processes data as 24-bit groups, mapping this data to four encoded
 *  characters.
 *  Each 6 bits of the 24-bit group is used as an index into a mapping table
 *  (the base64 alphabet) to obtain a character for the encoded data.
 *  The encoded data has line lengths that are limited to 76 characters.
 */

#ifndef DU_BASE64_H
#define DU_BASE64_H

#include <du_uchar_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Encodes @p input as Base64 and stores the encoded string in @p output.
 *
 *  @param[in] input pointer to the data to encode as Base64.
 *  @param[in] size the length to encode.
 *  @param[out] output pointer to the storage location for
 *              the Base64-encoded string.
 *  @retval 1 if, and only if, @p input data is encoded successfully.
 *  @retval 0 the encoding failed.
 *  @pre @p output must be initialized by du_uchar_array_init().
 */
extern du_bool du_base64_encode(const du_uint8* input, du_uint32 size, du_uchar_array* output);

/**
 *  Decodes a string of data that has been encoded in base64 format,
 *  and stores the decoded data in @p output.
 *
 *  @param[in] input pointer to the base64-encoded string to decode.
 *  @param[in] size  the length of @p input to decode.
 *  @param[out] output pointer to the storage location for the decoded data.
 *  @retval 1 if, and only if, @p input data is decoded successfully.
 *  @retval 0 the decoding failed.
 *  @pre @p output must be initialized by du_uchar_array_init().
 */
extern du_bool du_base64_decode(const du_uchar* input, du_uint32 size, du_uchar_array* output);

#ifdef __cplusplus
}
#endif

#endif
