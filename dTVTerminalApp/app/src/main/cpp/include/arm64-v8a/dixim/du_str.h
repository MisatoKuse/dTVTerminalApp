/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *   The du_str interface provides methods for manipulating string ( such as
 *    copying, getting length, diff, converting to binary data ).
 */

#ifndef DU_STR_H
#define DU_STR_H

#include <du_type.h>
#include <du_str_array.h>

#include <du_conf.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Maximum size of a string representation of number which is formatted by du_str_fmt_* functions.
 */
#define DU_STR_FMT_SIZE 40 /* enough space to hold 2^128 - 1 in decimal, plus \0 */

/**
 *  Gets the CR(Carriage Return) + LF(Line Feed) string.
 */
extern const du_uchar* du_str_crlf(void);

/**
 *  Gets the TAB control + space string.
 */
extern const du_uchar* du_str_htsp(void);

/**
 *  Copies a string.
 *  It does not append a null character to the end of the destination string.
 *  @param[out] to  destination string.
 *  @param[in] from source string.
 *  @return  the number of characters copyed.
 */
extern du_uint32 du_str_copy(du_uchar* to, const du_uchar* from);

/**
 *  Copies a string and appends a null character to the destination string.
 *  @param[out] to  destination string.
 *  @param[in] from source string.
 *  @return  the number of characters copyed which inclueds a null character.
 */
extern du_uint32 du_str_copy0(du_uchar* to, const du_uchar* from);

/**
 *  Copies a string.
 *  It does not append a null character to the end of the destination string.
 *  This function is similar to du_str_copy(), except that not more than len length of from are copied.
 *  @param[out] to  destination string.
 *  @param[in] len the number of characters to copy.
 *  @param[in] from source string.
 *  @return  the number of characters copyed.
 */
extern du_uint32 du_str_copyn(du_uchar* to, du_uint32 len, const du_uchar* from);

/**
 *  Copies a len length of from to to and appends a null character to the destination string.
 *  @param[out] to  destination string.
 *  @param[in] len the number of characters to copy.
 *  @param[in] from source string.
 *  @return  the number of characters copyed which inclueds a null character.
 */
extern du_uint32 du_str_copyn0(du_uchar* to, du_uint32 len, const du_uchar* from);

/**
 *  Gets the length of the string.
 *  @param[in] s  du_uchar string data.
 *  @return  the number of characters in s.
 */
#ifdef DU_C99_SUPPORTED
#define du_str_len(s) ((du_uint32)(strlen((const char*)(s))))
#else
extern du_uint32 du_str_len(const du_uchar* s);
#endif

/**
 *  Finds a character in a string and returns its position.
 *  @param[in] s  du_uchar source string data.
 *  @param[in] c  character to be located.
 *  @return  the index position if the c character is found
 *           in the s string,
 *           or length of the s string if the c character is not found.
 */
extern du_uint32 du_str_chr(const du_uchar* s, du_uchar c);

/**
 *  Finds a character in the first n characters of s
 *  and rerurns its position.
 *  @param[in] s  du_uchar source string data.
 *  @param[in] n  number of characters to find the c in the s.
 *  @param[in] c  character to be located.
 *  @return  the index position if the c character is found
 *           in the s string,
 *           or length of the s string if the c character is not found.
 */
extern du_uint32 du_str_chrn(const du_uchar* s, du_uint32 n, du_uchar c);

/**
 *  Finds the last character in a string and return its position.
 *  @param[in] s  du_uchar source string data.
 *  @param[in] c  character to be located.
 *  @return  the index position of of the last occurrence of the c character
 *           in the s string,
 *           or length of the s string if the c character is not found.
 */
extern du_uint32 du_str_rchr(const du_uchar* s, du_uchar c);

/**
 *  Tests if the string contains the specified character.
 *  @param[in] s  du_uchar string data.
 *  @param[in] c a character to test.
 *  @return true if s string contains c character.
 *     false otherwise.
 */
extern du_bool du_str_contain(const du_uchar* s, du_uchar c);

/**
 *  Clones the same string.
 *  @param[in] s  du_uchar source string data.
 *  @param[out] clone the pointer of a created du_uchar string .
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_str_clone(const du_uchar* s, du_uchar** clone);

/**
 *  Compares specified two strings.
 *  @param[in] s a first string.
 *  @param[in] t a second string.
 *  @return the lexicographic relation of s to t.
 *  Less than, equal to, or greater than zero if s is found, respectively,
 *   to be less than, to match, or be greater than t.
 */
#ifdef DU_C99_SUPPORTED
#define du_str_diff(s, t) ((du_int32)(strcmp((const char*)(s), (const char*)(t))))
#else
extern du_int32 du_str_diff(const du_uchar* s, const du_uchar* t);
#endif

/**
 *  Compares the first len characters of the two strings.
 *  @param[in] s a first string.
 *  @param[in] len The number of characters to compare.
 *  @param[in] t a second string.
 *  @return Less than, equal to, or greater than zero if the first len characters of
 *          s is found, respectively, to be less than, to match,
 *          or be greater than t.
 */
#ifdef DU_C99_SUPPORTED
#define du_str_diffn(s, len, t) ((du_int32)(strncmp((const char*)(s), (const char*)(t), (size_t)(len))))
#else
extern du_int32 du_str_diffn(const du_uchar* s, du_uint32 len, const du_uchar* t);
#endif

/**
 *  Finds a substring.
 *  @param[in] s s string to search
 *  @param[in] t a string to search for.
 *  @return the index position of of the first occurrence of the t string
 *           in the s string, or length of the s string
 *           if the t string does not appear in s.
 *           If t points to a string of zero length, the function returns 0.
 */
extern du_uint32 du_str_find(const du_uchar* s, const du_uchar* t);

/**
 *  Returns the index of the first occurrence in s string of any character
 *  in a specified array of charset characters.
 *  @param[in] s s string to search
 *  @param[in]  charset a character array containing one or more characters to seek.
 *  @return The index position of the first occurrence in s where
 *  any character in charset was found; otherwise,
 *  the lenngth of s if no character in charset was found.
 */
extern du_uint32 du_str_skip_till(const du_uchar* s, const du_uchar* charset);

/**
 *  Returns the index of the next character which is not any character
 *  in a specified array of charset characters.
 *  @param[in] s s string to search
 *  @param[in]  charset a character array containing one or more characters to skip.
 *  @return  the index of the next character which is not any character
 *  in a specified array of charset characters.
 */
extern du_uint32 du_str_skip_while(const du_uchar* s, const du_uchar* charset);

/**
 *  Returns the index of the next character which is not whitespace or not TAB character.
 *  @param[in] s s string to search
 *  @return the index of the next character which is not whitespace or not TAB character.
 */
extern du_uint32 du_str_skip_space(const du_uchar* s);

/**
 *  Returns the index of the next character which is not whitespace or not TAB character.
 *  @param[in] s s string to search
 *  @return the index of the next character which is not whitespace or not TAB character.
 */
extern du_uint32 du_str_skip_word(const du_uchar* s);

/**
 *  Compares specified two strings.
 *  @param[in] s a first string.
 *  @param[in] t a second string.
 *  @return true if s is found to match t.
           false if s is not equal to t.
 */
#define du_str_equal(s, t) (!du_str_diff((s), (t)))

/**
 *  Compares the first len characters of the two strings.
 *  @param[in] s a first string.
 *  @param[in] len the number of characters to compare.
 *  @param[in] t a second string.
 *  @return true if the first len characters of s is found to match t.
           false the first len characters of s is not equal to t.
 */
#define du_str_equaln(s, len, t) (!du_str_diffn((s), (len), (t)))

/**
 *  Tests if s string starts with the specified t string.
 *  @param[in] s a string.
 *  @param[in] t a prefix string.
 *  @return true if the character sequence represented by t
 *     is a prefix of the character sequence represented by s.
 *     false otherwise.
 */
#define du_str_start(s, t) (!du_str_diffn((s), du_str_len(t), (t)))

/**
 *  Tests if s string ends with the specified t string.
 *  @param[in] s a string.
 *  @param[in] t a suffix string.
 *  @return true if the character sequence represented by t
 *     is a suffix of the character sequence represented by s.
 *     false otherwise.
 */
extern du_bool du_str_end(const du_uchar* s, const du_uchar* t);

/**
 *  Converts string to lowercase.
 *  @param[in,out] s a string to convert.
 */
extern void du_str_case_lower(du_uchar* s);

/**
 *  Converts the first len characters of a string to lowercase.
 *  @param[in,out] s a string to convert.
 *  @param[in] len the number of characters to convert.
 */
extern void du_str_case_lowern(du_uchar* s, du_uint32 len);

/**
 *  Converts string to uppercase.
 *  @param[in,out] s a string to convert.
 */
extern void du_str_case_upper(du_uchar* s);

/**
 *  Converts the first len characters of a string to uppercase.
 *  @param[in,out] s a string to convert.
 *  @param[in] len the number of characters to convert.
 */
extern void du_str_case_uppern(du_uchar* s, du_uint32 len);

/**
 *  Compares two strings ignoring case differences.
 *  @param[in] s a first string.
 *  @param[in] t a second string.
 *  @return the lexicographic relation of s to t.
 *  Less than, equal to, or greater than zero if s is found, respectively,
 *   to be less than, to match, or be greater than t.
 */
extern du_int32 du_str_case_diff(const du_uchar* s, const du_uchar* t);

/**
 *  Compares the first len characters of the two strings,
 *  ignoring case differences.
 *  @param[in] s a first string.
 *  @param[in] len the number of characters to compare.
 *  @param[in] t a second string.
 *  @return Less than, equal to, or greater than zero if the first len characters of
 *          s is found, respectively, to be less than, to match,
 *          or be greater than t.
 */
extern du_int32 du_str_case_diffn(const du_uchar* s, du_uint32 len, const du_uchar* t);

/**
 *  Finds a substring, ignoring case differences.
 *  @param[in] s a string to search
 *  @param[in] t a string to search for.
 *  @return the index position of the first occurrence of the t string
 *           in the s string, or length of the s string
 *           if the t string does not appear in s.
 *           If t points to a string of zero length, the function returns 0.
 */
extern du_uint32 du_str_case_find(const du_uchar* s, const du_uchar* t);

/**
 *  Compares specified two strings, ignoring case differences.
 *  @param[in] s a first string.
 *  @param[in] t a second string.
 *  @return true if s is found to match t.
           false if s is not equal to t.
 */
#define du_str_case_equal(s, t) (!du_str_case_diff((s), (t)))

/**
 *  Compares the first len characters of the two strings,
 *  ignoring case differences.
 *  @param[in] s a first string.
 *  @param[in] len the number of characters to compare.
 *  @param[in] t a second string.
 *  @return true if the first len characters of s is found to match t.
           false the first len characters of s is not equal to t.
 */
#define du_str_case_equaln(s, len, t) (!du_str_case_diffn((s), (len), (t)))

/**
 *  Tests if s string starts with the specified t string,
 *  ignoring case differences.
 *  @param[in] s a string.
 *  @param[in] t a prefix string.
 *  @return true if the character sequence represented by t
 *     is a prefix of the character sequence represented by s.
 *     false otherwise.
 */
#define du_str_case_start(s, t) (!du_str_case_diffn((s), du_str_len(t), (t)))

/**
 *  Tests if s string ends with the specified t string,
 *  ignoring case differences.
 *  @param[in] s a string.
 *  @param[in] t a suffix string.
 *  @return true if the character sequence represented by t
 *     is a suffix of the character sequence represented by s.
 *     false otherwise.
 */
extern du_bool du_str_case_end(const du_uchar* s, const du_uchar* t);

/**
 *  Converts a bool value to a string.
 *  @param[out] s string result.
 *  @param[in] i bool value to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_bool(du_uchar* s, du_bool i);

/**
 *  Converts an integer to a string.
 *  @param[out] s string result.
 *  @param[in] i number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_int(du_uchar* s, du_int i);

/**
 *  Converts a du_int8 number to a string.
 *  @param[out] s string result.
 *  @param[in] i du_int8 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_int8(du_uchar* s, du_int8 i);

/**
 *  Converts a du_int16 number to a string.
 *  @param[out] s string result.
 *  @param[in] i du_int16 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_int16(du_uchar* s, du_int16 i);

/**
 *  Converts a du_int32 number to a string.
 *  @param[out] s string result.
 *  @param[in] i du_int32 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_int32(du_uchar* s, du_int32 i);

/**
 *  Converts a du_int64 number to a string.
 *  @param[out] s string result.
 *  @param[in] i du_int64 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_int64(du_uchar* s, du_int64 i);

/**
 *  Converts an unsigned integer number to a string.
 *  @param[out] s string result.
 *  @param[in] u unsigned integer number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_uint(du_uchar* s, du_uint u);

/**
 *  Converts a du_uint8 number to a string.
 *  @param[out] s string result.
 *  @param[in] u du_uint8 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_uint8(du_uchar* s, du_uint8 u);

/**
 *  Converts a du_uint16 number to a string.
 *  @param[out] s string result.
 *  @param[in] u du_uint16 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_uint16(du_uchar* s, du_uint16 u);

/**
 *  Converts a du_uint32 number to a string.
 *  @param[out] s string result.
 *  @param[in] u du_uint32 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_uint32(du_uchar* s, du_uint32 u);

/**
 *  Converts a du_uint64 number to a string.
 *  @param[out] s string result.
 *  @param[in] u du_uint64 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_uint64(du_uchar* s, du_uint64 u);

/**
 *  Converts an unsigned integer number to a hexadecimal string.
 *  @param[out] s string result.
 *  @param[in] u unsigned integer number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_xint(du_uchar* s, du_uint u);

/**
 *  Converts a du_uint8 number to a hexadecimal string.
 *  @param[out] s string result.
 *  @param[in] u du_uint8 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_xint8(du_uchar* s, du_uint8 u);

/**
 *  Converts a du_uint16 number to a hexadecimal string.
 *  @param[out] s string result.
 *  @param[in] u du_uint16 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_xint16(du_uchar* s, du_uint16 u);

/**
 *  Converts a du_uint32 number to a hexadecimal string.
 *  @param[out] s string result.
 *  @param[in] u du_uint32 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_xint32(du_uchar* s, du_uint32 u);

/**
 *  Converts a du_uint64 number to a hexadecimal string.
 *  @param[out] s string result.
 *  @param[in] u du_uint64 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_xint64(du_uchar* s, du_uint64 u);

/**
 *  Converts an unsigned integer number to a string, padding with '0' characters on the left
 *   for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u unsigned integer number to be converted.
 *  @param[in] n the specified number of characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_0uint(du_uchar* s, du_uint u, du_uint32 n);

/**
 *  Converts a du_uint8 number to a string, padding with '0' characters on the left
 *   for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint8 number to be converted.
 *  @param[in] n the specified number of characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_0uint8(du_uchar* s, du_uint8 u, du_uint32 n);

/**
 *  Converts a du_uint16 number to a string, padding with '0' characters on the left
 *   for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint16 number to be converted.
 *  @param[in] n the specified number of characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_0uint16(du_uchar* s, du_uint16 u, du_uint32 n);

/**
 *  Converts a du_uint32 number to a string, padding with '0' characters on the left
 *   for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint32 number to be converted.
 *  @param[in] n the specified number of characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_0uint32(du_uchar* s, du_uint32 u, du_uint32 n);

/**
 *  Converts a du_uint64 number to a string, padding with '0' characters on the left
 *   for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint64 number to be converted.
 *  @param[in] n the specified number of characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_0uint64(du_uchar* s, du_uint64 u, du_uint32 n);

/**
 *  Converts an unsigned integer number to a hexadecimal string,
 *  padding with '0' characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u unsigned integer number to be converted.
 *  @param[in] n the specified number of characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_0xint(du_uchar* s, du_uint u, du_uint32 n);

/**
 *  Converts a du_uint8 number to a hexadecimal string, padding with '0'
 *  characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint8 number to be converted.
 *  @param[in] n the specified number of characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_0xint8(du_uchar* s, du_uint8 u, du_uint32 n);

/**
 *  Converts a du_uint16 number to a hexadecimal string, padding with '0'
 *  characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint16 number to be converted.
 *  @param[in] n the specified number of characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_0xint16(du_uchar* s, du_uint16 u, du_uint32 n);

/**
 *  Converts a du_uint32 number to a hexadecimal string, padding with '0'
 *  characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint32 number to be converted.
 *  @param[in] n the specified number of characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_0xint32(du_uchar* s, du_uint32 u, du_uint32 n);

/**
 *  Converts a du_uint64 number to a hexadecimal string, padding with '0'
 *  characters on the left for a specified total length n.
 *  @param[out] s string result.
 *  @param[in] u du_uint64 number to be converted.
 *  @param[in] n the specified number of characters in the resulting strings.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_0xint64(du_uchar* s, du_uint64 u, du_uint32 n);

#ifndef DIXIM_UTIL_DISABLE_FLOAT
/**
 *  Converts a du_float32 number to a string.
 *  @param[out] s string result.
 *  @param[in] f du_float32 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_float32(du_uchar* s, du_float32 f);

# ifndef DIXIM_UTIL_DISABLE_FLOAT64
/**
 *  Converts a du_float64 number to a string.
 *  @param[out] s string result.
 *  @param[in] f du_float64 number to be converted.
 *  @return the length of s string.
 */
extern du_uint32 du_str_fmt_float64(du_uchar* s, du_float64 f);
# endif
#endif

/**
 *  Converts a given string to a bool value.
 *  @param[in] s string to be converted.
 *  @param[out] u the bool value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_bool(const du_uchar* s, du_bool* u);

/**
 *  Converts a given string to an integer.
 *  @param[in] s string to be converted.
 *  @param[out] u the integer value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_int(const du_uchar* s, du_int* u);

/**
 *  Converts a given string to a du_int8 data.
 *  @param[in] s string to be converted.
 *  @param[out] u the du_int8 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_int8(const du_uchar* s, du_int8* u);

/**
 *  Converts a given string to a du_int16 data.
 *  @param[in] s string to be converted.
 *  @param[out] u the du_int16 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_int16(const du_uchar* s, du_int16* u);

/**
 *  Converts a given string to a du_int32 data.
 *  @param[in] s string to be converted.
 *  @param[out] u the du_int32 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_int32(const du_uchar* s, du_int32* u);

/**
 *  Converts a given string to a du_int64 data.
 *  @param[in] s string to be converted.
 *  @param[out] u the du_int64 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_int64(const du_uchar* s, du_int64* u);

/**
 *  Converts a given string to an unsigned int data.
 *  @param[in] s string to be converted.
 *  @param[out] u the unsigned int value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_uint(const du_uchar* s, du_uint* u);

/**
 *  Converts a given string to a du_uint8 data.
 *  @param[in] s string to be converted.
 *  @param[out] u the du_uint8 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_uint8(const du_uchar* s, du_uint8* u);

/**
 *  Converts a given string to a du_uint16 data.
 *  @param[in] s string to be converted.
 *  @param[out] u the du_uint16 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_uint16(const du_uchar* s, du_uint16* u);

/**
 *  Converts a given string to a du_uint32 data.
 *  @param[in] s string to be converted.
 *  @param[out] u the du_uint32 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_uint32(const du_uchar* s, du_uint32* u);

/**
 *  Converts a given string to a du_uint64 data.
 *  @param[in] s string to be converted.
 *  @param[out] u the du_uint64 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_uint64(const du_uchar* s, du_uint64* u);

/**
 *  Converts a hexadecimal string to an integer data.
 *  @param[in] s hexadecimal string to be converted.
 *  @param[out] u the integer value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_xint(const du_uchar* s, du_uint* u);

/**
 *  Converts a hexadecimal string to a du_uint16 data.
 *  @param[in] s hexadecimal string to be converted.
 *  @param[out] u the du_uint8 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_xint8(const du_uchar* s, du_uint8* u);

/**
 *  Converts a hexadecimal string to a du_uint16 data.
 *  @param[in] s hexadecimal string to be converted.
 *  @param[out] u the du_uint16 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_xint16(const du_uchar* s, du_uint16* u);

/**
 *  Converts a hexadecimal string to a du_uint32 data.
 *  @param[in] s hexadecimal string to be converted.
 *  @param[out] u the du_uint32 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_xint32(const du_uchar* s, du_uint32* u);

/**
 *  Converts a hexadecimal string to a du_uint64 data.
 *  @param[in] s hexadecimal string to be converted.
 *  @param[out] u the du_uint64 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_xint64(const du_uchar* s, du_uint64* u);

#ifndef DIXIM_UTIL_DISABLE_FLOAT
/**
 *  Converts a decimal string to a du_float32 data.
 *  @param[in] s decimal string to be converted.
 *  @param[out] f the du_float32 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_float32(const du_uchar* s, du_float32* f);

# ifndef DIXIM_UTIL_DISABLE_FLOAT64
/**
 *  Converts a decimal string to a du_float64 data.
 *  @param[in] s decimal string to be converted.
 *  @param[out] f the du_float64 value.
 *  @return length the characters converted.
 *  @remark this function stops reading the string s at the first
 *  character it cannot recognize as part of a number.
 */
extern du_uint32 du_str_scan_float64(const du_uchar* s, du_float64* f);
# endif
#endif

/**
 *  Clones the string list.
 *  @param[in] strlist  source string list.
 *  @param[out] clone the pointer of a created string list.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_str_list_clone(du_uchar** strlist, du_uchar*** clone);

/**
 *  Frees the resources of a string list.
 *  @param[in] strlist a string list.
 */
extern void du_str_list_free(du_uchar** strlist);

/**
 *  Finds a string in a string list.
 *  @param[in] strlist a string list to search.
 *  @param[in] str a string to search for.
 *  @return the index position of of the first occurrence of the str string
 *           in the strlist string list, or length of the strlist string list
 *           if the str string does not appear in strlist.
 */
extern du_uint32 du_str_list_find(const du_uchar** strlist, const du_uchar* str);

/**
 *  Splits a string into a string array by the delimiter character specified.
 *
 *  @param[in] src a string to be split.
 *  @param[in] delim a delimiter character.
 *  @param[out] dest a pointer to du_str_array where split strings are to be stored.
 *  @return true if the function succeeded, false otherwise.
 *  @remark <em>dest</em> must be initialized by du_str_array_init().
 */
extern du_bool du_str_split_by_char(const du_uchar* src, du_uchar delim, du_str_array* dest);

#ifdef __cplusplus
}
#endif

#endif
