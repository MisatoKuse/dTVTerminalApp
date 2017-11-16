/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_wstr interface provides methods for manipulating wide-character
 *  string ( such as copying, getting length, diff, converting to bibary data).
 */

#ifndef DU_WSTR_H
#define DU_WSTR_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Maximum size of a string representation of a number which is formatted by du_wstr_fmt_* functions.
 */
#define DU_WSTR_FMT_SIZE 40 // enough space to hold 2^128 - 1 in decimal, plus \0

/**
 *  Copies a wide-character string.
 *  @param[out] to  destination wide-character string.
 *  @param[in] from source wide-character string.
 *  @return  the number of characters copied.
 */
extern du_uint32 du_wstr_copy(wchar_t* to, const wchar_t* from);

/**
 *  Copies a wide-character string and appends a null character to the destination string.
 *  @param[out] to  destination wide-character string.
 *  @param[in] from source wide-character string.
 *  @return  the number of characters copyed which inclueds a null character.
 */
extern du_uint32 du_wstr_copy0(wchar_t* to, const wchar_t* from);

/**
 *  Copies a wide-character string.
 *  It does not append a null character to the end of the destination wide-character string.
 *  This function is similar to du_wstr_copy(), except that not more than len length of from are copied.
 *  @param[out] to  destination wide-character string.
 *  @param[in] len the number of characters to copy.
 *  @param[in] from source wide-character string.
 *  @return  the number of characters copyed.
 */
extern du_uint32 du_wstr_copyn(wchar_t* to, du_uint32 len, const wchar_t* from);

/**
 *  Copies a len length of from to to and appends a null character to the destination wide-character string.
 *  @param[out] to  destination wide-character string.
 *  @param[in] len the number of characters to copy.
 *  @param[in] from source wide-character string.
 *  @return  the number of characters copyed which inclueds a null character.
 */
extern du_uint32 du_wstr_copyn0(wchar_t* to, du_uint32 len, const wchar_t* from);

/**
 *  Gets the number of wide-characters in a s string.
 *  @param[in] s a wide-character string.
 *  @return  the number of wide-characters in s.
 */
extern du_uint32 du_wstr_len(const wchar_t* s);

/**
 *  Finds a wide-character c in a s string and returns its index position.
 *  @param[in] s a wide-character source string data.
 *  @param[in] c a wide-character to be located.
 *  @return  the index position if the c character is found
 *           in the s string,
 *           or length of the s string if the c character is not found.
 */
extern du_uint32 du_wstr_chr(const wchar_t* s, wchar_t c);

/**
 *  Finds a wide-character c in the first n characters of s string
 *  and returns its index position.
 *  @param[in] s  a wide-character source string data.
 *  @param[in] n  number of wide-characters to find the c in the s.
 *  @param[in] c  a wide-character to be located.
 *  @return  the index position if the c character is found
 *           in the s string,
 *           or length of the s string if the c character is not found.
 */
extern du_uint32 du_wstr_chrn(const wchar_t* s, du_uint32 n, wchar_t c);

/**
 *  Gets the index position of the last occurrence of a specified c wide-character
 *  within s wide-character string.
 *  @param[in] s  a wide-character source string data.
 *  @param[in] c  a wide-character to be located.
 *  @return  the index position of the last occurrence of the c character
 *           in the s string,
 *           or length of the s string if the c character is not found.
 */
extern du_uint32 du_wstr_rchr(const wchar_t* s, wchar_t c);

/**
 *  Creates a new wide-character string with the same value.
 *  @param[in] s a wide-character source string data.
 *  @param[out] clone the pointer of a created wide-character string .
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_wstr_clone(const wchar_t* s, wchar_t** clone);

/**
 *  Compares specified two wide-character strings.
 *  @param[in] s a first wide-character string.
 *  @param[in] t a second wide-character string.
 *  @return the lexicographic relation of s to t.
 *  Less than, equal to, or greater than zero if s is found, respectively,
 *   to be less than, to match, or be greater than t.
 */
extern du_int32 du_wstr_diff(const wchar_t* s, const wchar_t* t);

/**
 *  Compares the first len wide-characters of the two wide-character strings.
 *  @param[in] s a first wide-character string.
 *  @param[in] len the number of characters to compare.
 *  @param[in] t a second wide-character string.
 *  @return Less than, equal to, or greater than zero if the first len characters of
 *          s is found, respectively, to be less than, to match,
 *          or be greater than t.
 */
extern du_int32 du_wstr_diffn(const wchar_t* s, du_uint32 len, const wchar_t* t);

/**
 *  Finds a substring.
 *  @param[in] s s wide-character string to search
 *  @param[in] t a wide-character string to search for.
 *  @return the index position of the first occurrence of the t string
 *           in the s string, or length of the s string
 *           if the t string does not appear in s.
 *           If t points to a string of zero length, the function returns 0.
 */
extern du_uint32 du_wstr_find(const wchar_t* s, const wchar_t* t);

/**
 *  Compares specified two wide-character strings.
 *  @param[in] s a first wide-character string.
 *  @param[in] t a second wide-character string.
 *  @return true if s is found to match t.
           false if s is not equal to t.
 */
#define du_wstr_equal(s, t) (!du_wstr_diff((s), (t)))

/**
 *  Compares the first len wide-characters of the two wide-character strings.
 *  @param[in] s a first wide-character string.
 *  @param[in] len the number of characters to compare.
 *  @param[in] t a second wide-character string.
 *  @return true if the first len characters of s is found to match t.
           false the first len characters of s is not equal to t.
 */
#define du_wstr_equaln(s, len, t) (!du_wstr_diffn((s), (len), (t)))

/**
 *  Tests if s wide-character string ends with the specified t
 *  wide-character string.
 *  @param[in] s a wide-characterstring.
 *  @param[in] t a wide-character suffix string.
 *  @return true if the wide-character sequence represented by t
 *     is a suffix of the wide-character sequence represented by s.
 *     false otherwise.
 */
du_bool du_wstr_end(const wchar_t* s, const wchar_t* t);

/**
 *  Converts wide-character string to lowercase.
 *  @param[in,out] s a wide-character string to convert.
 */
extern void du_wstr_case_lower(wchar_t* s);

/**
 *  Converts the first len wide-characters of a wide-character string to lowercase.
 *  @param[in,out] s a wide-character string to convert.
 *  @param[in] len the number of characters to convert.
 */
extern void du_wstr_case_lowern(wchar_t* s, du_uint32 len);

/**
 *  Converts a wide-character string to uppercase.
 *  @param[in,out] s a wide-character string to convert.
 */
extern void du_wstr_case_upper(wchar_t* s);

/**
 *  Converts the first len wide-characters of a wide-character string to uppercase.
 *  @param[in,out] s a wide-character string to convert.
 *  @param[in] len the number of characters to convert.
 */
extern void du_wstr_case_uppern(wchar_t* s, du_uint32 len);

/**
 *  Compares two wide-character strings, ignoring case differences by first
 *  converting them to their lowercase forms.
 *  @param[in] s a first wide-character string.
 *  @param[in] t a second wide-character string.
 *  @return the lexicographic relation of s to t.
 *  Less than, equal to, or greater than zero if s is found, respectively,
 *   to be less than, to match, or be greater than t.
 */
extern du_int32 du_wstr_case_diff(const wchar_t* s, const wchar_t* t);

/**
 *  Compares the first len characters of the two wide-character strings,
 *  ignoring case differences by first
 *  converting them to their lowercase forms.
 *  @param[in] s a first wide-character string.
 *  @param[in] len the number of characters to compare.
 *  @param[in] t a second wide-character string.
 *  @return Less than, equal to, or greater than zero if the first len characters of
 *          s is found, respectively, to be less than, to match,
 *          or be greater than t.
 */
extern du_int32 du_wstr_case_diffn(const wchar_t* s, du_uint32 len, const wchar_t* t);

/**
 *  Finds a substring, ignoring case differences by first
 *  converting wide-character strings to their lowercase forms.
 *  @param[in] s a wide-character string to search
 *  @param[in] t a wide-character string to search for.
 *  @return the index position of the first occurrence of the t string
 *           in the s string, or length of the s string
 *           if the t string does not appear in s.
 *           If t points to a string of zero length, the function returns 0.
 */
extern du_uint32 du_wstr_case_find(const wchar_t* s, const wchar_t* t);

/**
 *  Compares specified two wide-character strings, ignoring case differences by first
 *  converting them to their lowercase forms.
 *  @param[in] s a first wide-character string.
 *  @param[in] t a second wide-character string.
 *  @return true if s is found to match t.
           false if s is not equal to t.
 */
#define du_wstr_case_equal(s, t) (!du_wstr_case_diff((s), (t)))

/**
 *  Compares the first len wide-characters of the two wide-character strings,
 *  ignoring case differences by first converting them to their lowercase forms.
 *  @param[in] s a first wide-character string.
 *  @param[in] len the number of characters to compare.
 *  @param[in] t a second wide-character string.
 *  @return true if the first len characters of s is found to match t.
           false the first len characters of s is not equal to t.
 */
#define du_wstr_case_equaln(s, len, t) (!du_wstr_case_diffn((s), (len), (t)))

/**
 *  Tests if s wide-character string ends with the specified t
 *  wide-character string, ignoring case differences by first converting them
 *  to their lowercase forms.
 *  @param[in] s a wide-characterstring.
 *  @param[in] t a wide-character suffix string.
 *  @return true if the wide-character sequence represented by t
 *     is a suffix of the wide-character sequence represented by s.
 *     false otherwise.
 */
du_bool du_wstr_case_end(const wchar_t* s, const wchar_t* t);

/**
 *  Converts an integer to a wide-character string.
 *  @param[out] s wide-character string result.
 *  @param[in] i number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_int(wchar_t* s, du_int i);

/**
 *  Converts a du_int16 number to a wide-character string.
 *  @param[out] s string result.
 *  @param[in] i du_int16 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_int16(wchar_t* s, du_int16 i);

/**
 *  Converts a du_int32 number to a wide-character string.
 *  @param[out] s string result.
 *  @param[in] i du_int32 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_int32(wchar_t* s, du_int32 i);

/**
 *  Converts a du_int64 number to a wide-character string.
 *  @param[out] s string result.
 *  @param[in] i du_int64 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_int64(wchar_t* s, du_int64 i);

/**
 *  Converts an unsigned integer number to a wide-character string.
 *  @param[out] s string result.
 *  @param[in] u unsigned integer number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_uint(wchar_t* s, du_uint u);

/**
 *  Converts a du_uint16 number to a wide-character string.
 *  @param[out] s string result.
 *  @param[in] u du_uint16 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_uint16(wchar_t* s, du_uint16 u);

/**
 *  Converts a du_uint32 number to a wide-character string.
 *  @param[out] s string result.
 *  @param[in] u du_uint32 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_uint32(wchar_t* s, du_uint32 u);

/**
 *  Converts a du_uint64 number to a wide-character string.
 *  @param[out] s string result.
 *  @param[in] u du_uint64 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_uint64(wchar_t* s, du_uint64 u);

/**
 *  Converts an unsigned integer number to a hexadecimal wide-character string.
 *  @param[out] s string result.
 *  @param[in] u unsigned integer number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_xint(wchar_t* s, du_uint u);

/**
 *  Converts a du_uint16 number to a hexadecimal wide-character string.
 *  @param[out] s string result.
 *  @param[in] u du_uint16 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_xint16(wchar_t* s, du_uint16 u);

/**
 *  Converts a du_uint32 number to a hexadecimal wide-character string.
 *  @param[out] s string result.
 *  @param[in] u du_uint32 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_xint32(wchar_t* s, du_uint32 u);

/**
 *  Converts a du_uint64 number to a hexadecimal wide-character string.
 *  @param[out] s string result.
 *  @param[in] u du_uint64 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_xint64(wchar_t* s, du_uint64 u);

/**
 *  Converts an unsigned integer number to a wide-character string,
 *  padding with L'0' wide-characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u unsigned integer number to be converted.
 *  @param[in] n the specified number of wide-characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_0uint(wchar_t* s, du_uint u, du_uint32 n);

/**
 *  Converts a du_uint16 number to a wide-character string,
 *  padding with L'0' wide-characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint16 number to be converted.
 *  @param[in] n the specified number of wide-characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_0uint16(wchar_t* s, du_uint16 u, du_uint32 n);

/**
 *  Converts a du_uint32 number to a wide-character string,
 *  padding with L'0' wide-characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint32 number to be converted.
 *  @param[in] n the specified number of wide-characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_0uint32(wchar_t* s, du_uint32 u, du_uint32 n);

/**
 *  Converts a du_uint64 number to a wide-character string,
 *  padding with L'0' wide-characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint64 number to be converted.
 *  @param[in] n the specified number of wide-characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_0uint64(wchar_t* s, du_uint64 u, du_uint32 n);

/**
 *  Converts an unsigned integer number to a hexadecimal wide-character string,
 *  padding with L'0' wide-characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u unsigned integer number to be converted.
 *  @param[in] n the specified number of wide-characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_0xint(wchar_t* s, du_uint u, du_uint32 n);

/**
 *  Converts a du_uint16 number to a hexadecimal wide-character string, padding with L'0'
 *  wide-characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint16 number to be converted.
 *  @param[in] n the specified number of wide-characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_0xint16(wchar_t* s, du_uint16 u, du_uint32 n);

/**
 *  Converts a du_uint32 number to a hexadecimal wide-character string, padding with L'0'
 *  wide-characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint32 number to be converted.
 *  @param[in] n the specified number of wide-characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_0xint32(wchar_t* s, du_uint32 u, du_uint32 n);

/**
 *  Converts a du_uint64 number to a hexadecimal wide-character string, padding with L'0'
 *  wide-characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint64 number to be converted.
 *  @param[in] n the specified number of wide-characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_wstr_fmt_0xint64(wchar_t* s, du_uint64 u, du_uint32 n);

/**
 *  Converts a given wide-character string to an integer.
 *  @param[in] s a wide-character string to be converted.
 *  @param[out] u the string's integer value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
*/
extern du_uint32 du_wstr_scan_int(const wchar_t* s, du_int* u);

/**
 *  Converts a given wide-character string to a du_int16 data.
 *  @param[in] s wide-character string to be converted.
 *  @param[out] u the string's du_int16 value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
 */
extern du_uint32 du_wstr_scan_int16(const wchar_t* s, du_int16* u);

/**
 *  Converts a given wide-character string to a du_int32 data.
 *  @param[in] s wide-character string to be converted.
 *  @param[out] u the string's du_int32 value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
 */
extern du_uint32 du_wstr_scan_int32(const wchar_t* s, du_int32* u);

/**
 *  Converts a given wide-character string to a du_int64 data.
 *  @param[in] s wide-character string to be converted.
 *  @param[out] u the string's du_int64 value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
 */
extern du_uint32 du_wstr_scan_int64(const wchar_t* s, du_int64* u);

/**
 *  Converts a given wide-character string to an unsigned int data.
 *  @param[in] s wide-character string to be converted.
 *  @param[out] u the string's unsigned int value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
 */
extern du_uint32 du_wstr_scan_uint(const wchar_t* s, du_uint* u);

/**
 *  Converts a given wide-character string to a du_uint16 data.
 *  @param[in] s wide-character string to be converted.
 *  @param[out] u the string's du_uint16 value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
 */
extern du_uint32 du_wstr_scan_uint16(const wchar_t* s, du_uint16* u);

/**
 *  Converts a given wide-character string to a du_uint32 data.
 *  @param[in] s wide-character string to be converted.
 *  @param[out] u the string's du_uint32 value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
 */
extern du_uint32 du_wstr_scan_uint32(const wchar_t* s, du_uint32* u);

/**
 *  Converts a given wide-character string to a du_uint64 data.
 *  @param[in] s wide-character string to be converted.
 *  @param[out] u the string's du_uint64 value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
 */
extern du_uint32 du_wstr_scan_uint64(const wchar_t* s, du_uint64* u);

/**
 *  Converts a hexadecimal wide-character string to an integer data.
 *  @param[in] s hexadecimal wide-character string to be converted.
 *  @param[out] u the string's integer value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
 */
extern du_uint32 du_wstr_scan_xint(const wchar_t* s, du_uint* u);

/**
 *  Converts a hexadecimal wide-character string to a du_uint16 data.
 *  @param[in] s hexadecimal wide-character string to be converted.
 *  @param[out] u the string's du_uint16 value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
 */
extern du_uint32 du_wstr_scan_xint16(const wchar_t* s, du_uint16* u);

/**
 *  Converts a hexadecimal wide-character string to a du_uint32 data.
 *  @param[in] s hexadecimal wide-character string to be converted.
 *  @param[out] u the string's du_uint32 value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
 */
extern du_uint32 du_wstr_scan_xint32(const wchar_t* s, du_uint32* u);

/**
 *  Converts a hexadecimal wide-character string to a du_uint64 data.
 *  @param[in] s hexadecimal wide-character string to be converted.
 *  @param[out] u the string's du_uint64 value.
 *  @return the index position of the wide-character that stops scan.
 *  @remark this function stops reading the string s at the first
 *  wide-character it cannot recognize as part of a number.
 */
extern du_uint32 du_wstr_scan_xint64(const wchar_t* s, du_uint64* u);

/**
 *  Creates a new wide-character string list with the same value.
 *  @param[in] strlist  source wide-character string list.
 *  @param[out] clone the pointer of a created wide-character string list.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_wstr_list_clone(wchar_t** strlist, wchar_t*** clone);

/**
 *  Frees the region of a strlist wide-character string list.
 *  @param[in] strlist a wide-character string list.
 */
extern void du_wstr_list_free(wchar_t** strlist);

/**
 *  Finds a wide-character string in a wide-character string list.
 *  @param[in] strlist a wide-character string list to search.
 *  @param[in] str a wide-character string to search for.
 *  @return the index position of the first occurrence of the str string
 *           in the strlist string list, or length of the strlist string list
 *           if the str string does not appear in strlist.
 */
extern du_uint32 du_wstr_list_find(wchar_t** strlist, const wchar_t* str);

#ifdef __cplusplus
}
#endif

#endif
