/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_uchar interface provides methods for maminupating uchar data ( such as
 *    converting to bibary data, checking character encoding type).
 */

#ifndef DU_UCHAR_H
#define DU_UCHAR_H

#include "du_type.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Checks whether ch is a hexadecimal character.
 *  @param[in] ch the character to check.
 *  @return  true if ch is a hexadecimal character, false otherwise.
 */
extern du_bool du_uchar_is_hex(du_uint8 ch);

/**
 *  Converts a hexadecimal character to du_uint8 data.
 *  @param[in] ch du_uchar data to be converted.
 *  @return du_uint8 value produced by interpreting the input ch
 *  character as a hexadecimal number. The return value is 0 if the input ch
 *  cannot be converted.
 */
extern du_uint8 du_uchar_hex_to_uint8(du_uchar ch);

/**
 *  Converts a du_uint8 number to a hexadecimal character.
 *  @param[in] u du_uint8 data to be converted.
 *  @return du_uchar character produced by interpreting the input u
 *  as a hexadecimal number. The return value is '0' if the input u
 *  cannot be converted.
 */
extern du_uchar du_uchar_uint8_to_hex(du_uint8 u);

#ifdef __cplusplus
}
#endif

#endif
