/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

#ifndef DCU_REGEX_MULTI_SUBSTITUTER_H
#define DCU_REGEX_MULTI_SUBSTITUTER_H

/**
 * @file
 *  The dcu_regex_multi_substituter interface provides substitute methods
 *  for multi dcu_regex objects.
 */

#include <dcu_regex_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  This structure contains the information of multi dcu_regex objects.
 */
typedef struct dcu_regex_multi_substituter {
    dcu_regex_array ra;
} dcu_regex_multi_substituter;

/**
 *  Initializes a dcu_regex_multi_substituter data area.
 *
 *  @param[out] x  pointer to the dcu_regex_multi_substituter data structure.
 *  @param[in] keys  regular expression character strings array.
 *  @param[in] cflags flags used to determine the type of compilation.
 *                    see also regcomp(3) linux man page.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark  dcu_regex objects initialized by all @p keys strings are registered in @p x .
 */
extern du_bool dcu_regex_multi_substituter_init(dcu_regex_multi_substituter* x, const du_str_array* keys, du_int cflags);

/**
 *  Frees the region of all elements in x,
 *
 *  @param[in,out] x pointer to the dcu_regex_multi_substituter structure.
 */
extern void dcu_regex_multi_substituter_free(dcu_regex_multi_substituter* x);

/**
 *  Substitute strings with multi pattern regular expressions at a time.
 *
 *  @param[in] x  pointer to the dcu_regex_multi_substituter data structure.
 *  @param[in] str  target character string.
 *  @param[in] substitute_str_param_array  strings after it substitutes.
 *  @param[in] max_count the maximum number of matches.
 *             Specify 0 if you don't want to limit.
 *  @param[out] substituted_str substituted string.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_regex_multi_substituter_substitute(const dcu_regex_multi_substituter* x, const du_uchar* str, const du_str_array* substitute_str_param_array, du_uint32 max_count, du_uchar_array* substituted_str);

#ifdef __cplusplus
}
#endif

#endif
