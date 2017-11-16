/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


#ifndef DCU_REGEX_H
#define DCU_REGEX_H

/**
 * @file
 *    The dcu_regex interface provides various methods for regular expression.
 */

#include <du_uchar_array.h>
#include <du_str_array.h>
#include <dcu_regex_match_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  This structure contains the information of regular expression.
 */
typedef struct dcu_regex {
    void* impl;
} dcu_regex;

/**
 *  Initializes dcu_regex.
 *  @param[out] x  pointer to the dcu_regex data structure.
 *  @param[in] regex regular expression character string.
 *  @param[in] cflags flags used to determine the type of compilation.
 *                    see also regcomp(3) linux man page.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_regex_init(dcu_regex* x, const du_uchar* regex, du_int cflags);

/**
 *  Checks whether str string matches regular expression.
 *  @param[in] x  pointer to the dcu_regex data structure.
 *  @param[in] str target character string.
 *  @return  true if str string matches regular expression, false otherwise.
 */
extern du_bool dcu_regex_is_match(dcu_regex* x, const du_uchar* str);

/**
 *  Count number of matches with regular expression.
 *  @param[in] x  pointer to the dcu_regex data structure.
 *  @param[in] str target character string.
 *  @param[in] maximum_count the maximum number of matches.
 *             Specify 0 if you don't want to limit.
 *  @param[out] count number of matches.
 *  @param[out] match_array match points information.
 *              Specify NULL if you don't want this information.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_regex_count_match(dcu_regex* x, const du_uchar* str, du_uint32 maximum_count, du_uint32* count, dcu_regex_match_array* match_array);

/**
 *  Get matched strings with regular expression.
 *  @param[in] x  pointer to the dcu_regex data structure.
 *  @param[in] str target character string.
 *  @param[in] maximum_count the maximum number of matches.
 *             Specify 0 if you don't want to limit.
 *  @param[out] match_str_array matched strings.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_regex_get_match(dcu_regex* x, const du_uchar* str, du_uint32 maximum_count, du_str_array* match_str_array);

/**
 *  Substitute strings with regular expression.
 *  @param[in] x  pointer to the dcu_regex data structure.
 *  @param[in] str target character string.
 *  @param[in] substitute_str string after it substitutes.
 *  @param[in] maximum_count the maximum number of matches.
 *             Specify 0 if you don't want to limit.
 *  @param[out] count number of matches.
 *  @param[out] substituted_str substituted string.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_regex_substitute(dcu_regex* x, const du_uchar* str, const du_uchar* substitute_str, du_uint32 maximum_count, du_uint32* count, du_uchar_array* substituted_str);

/**
 *  Split the string str into a list of strings splitted_str.
 *  @param[in] x  pointer to the dcu_regex data structure.
 *  @param[in] str target character string.
 *  @param[in] limit the maximum number of fields the str will be split into.
 *  @param[out] splitted_str splitted list of strings.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_regex_split(dcu_regex* x, const du_uchar* str, du_uint32 limit, du_str_array* splitted_str);

/**
 *  Frees the region used by dcu_regex.
 *  @param[in,out] x pointer to the dcu_regex structure.
 */
extern void dcu_regex_free(dcu_regex* x);

#ifdef __cplusplus
}
#endif

#endif
