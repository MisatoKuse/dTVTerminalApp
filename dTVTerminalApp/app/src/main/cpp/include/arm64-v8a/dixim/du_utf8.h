/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *   The du_utf8 interface provides methods converting a wide characters
 *  to/from a UCS Transformation Format, 8-bit form(UTF-8) characters.
 */

#ifndef DU_UTF8_H
#define DU_UTF8_H

#include <du_type.h>
#include <du_uchar_array.h>
#include <du_wchar_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Converts UTF-8 characters u to wide characters w.
 *  @param[in] u pointer to a du_uchar data that contains UTF-8 characters to be converted.
 *  @param[in,out] usize_left in: the byte length of u to be converted.<br>
 *                            out: the byte length of UTF-8 characters in u
 *                                 that are not yet converted.
 *  @param[out] w pointer to a wchar_t data to store converted wide characters.
 *  @param[in]  wsize maximum number of wide characters to store in w.
 *  @param[out] len pointer to the variable to receive the number of wide characters
 *  stored in the w.
 *  @return  true if the function succeeds. false if the function fails.
 */
extern du_bool du_utf8_to_wcs(const du_uchar* u, du_uint32* usize_left, wchar_t* w, du_uint32 wsize, du_uint32* len);

/**
 *  Appends wide characters converted from UTF-8 characters u to the destination array wa.
 *  @param[in] u pointer to a du_uchar data that contains UTF-8 characters to be converted.
 *  @param[in] usize the byte length of u to be converted.<br>
 *  @param[out] wa pointer to the destination du_wchar_array structure to store converted wide characters.
 *  @return  true if the function succeeds. false if the function fails.
 */
extern du_bool du_utf8_cat_to_wcs(const du_uchar* u, du_uint32 usize, du_wchar_array* wa);

/**
 *  Converts wide characters w to UTF-8 characters u.
 *  @param[in] w pointer to a wchar_t data that contains wide characters to be converted.
 *  @param[in,out] wsize_left in: number of wide characters to be converted.<br>
 *                            out: number of wide characters in w
 *                                 that are not yet converted.
 *  @param[out] u pointer to a du_uchar data to store converted UTF-8 characters.
 *  @param[in] usize maximum number of byte to store in u.
 *  @param[out] len pointer to the variable to receive the number of byte
 *   stored in the u.
 *  @return  true if the function succeeds. false if the function fails.
 */
extern du_bool du_utf8_from_wcs(const wchar_t* w, du_uint32* wsize_left, du_uchar* u, du_uint32 usize, du_uint32* len);

/**
 *  Appends UTF-8 characters converted from wide characters w to the destination array ua.
 *  @param[in] w pointer to a wchar_t data that contains wide characters to be converted.
 *  @param[in] wsize number of wide characters to be converted.<br>
 *  @param[out] ua pointer to the destination du_uchar_array structure to store converted UTF-8 characters.
 *  @return  true if the function succeeds. false if the function fails.
 */
extern du_bool du_utf8_cat_from_wcs(const wchar_t* w, du_uint32 wsize, du_uchar_array* ua);

/**
 *  Gets the byte length of the first UTF-8 character of string.
 *  @param[in] u pointer to a du_uchar data of UTF-8 characters.
 *  @param[in] len byte length of u UTF-8 characters.
 *  @param[out] nbytes pointer to the variable to receive the byte length of
 *   first character of u.
 *  @return  true if the function succeeds. false if the function fails.
 */
extern du_bool du_utf8_get_char_size(const du_uchar* u, du_uint32 len, du_uint32* nbytes);

/**
 *  Gets the number of characters of UTF-8 string.
 *  @param[in] u pointer to a du_uchar data of UTF-8 characters.
 *  @param[in] len length of u UTF-8 characters.
 *  @param[out] str_len pointer to the variable to receive the character numbers of
 *   u.
 *  @return  true if the function succeeds. false if the function fails.
 */
extern du_bool du_utf8_get_str_len(const du_uchar* u, du_uint32 len, du_uint32* str_len);

#ifdef __cplusplus
}
#endif

#endif
