/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *    The dcu_multi_regex interface provides various methods for multiple regular expression.
 */

#ifndef DCU_MULTI_REGEX_H
#define DCU_MULTI_REGEX_H

#include <du_type.h>
#include <dcu_regex_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  This structure contains the information of multiple regular expression.
 */
typedef struct dcu_multi_regex {
    dcu_regex_array _regex_array;
} dcu_multi_regex;

/**
 *  Initializes dcu_multi_regex.
 *  @param[out] x  pointer to the dcu_multi_regex data structure.
 */
extern void dcu_multi_regex_init(dcu_multi_regex* x);

/**
 *  Frees the region used by dcu_regex.
 *  @param[in,out] x pointer to the dcu_multi_regex structure.
 */
extern void dcu_multi_regex_free(dcu_multi_regex* x);

/**
 *  Adds regular expression to the dcu_multi_regex.
 *  @param[out] x  pointer to the dcu_multi_regex data structure.
 *  @param[in] regex regular expression character string.
 *  @param[in] cflags flags used to determine the type of compilation.
 *                    see also regcomp(3) linux man page.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool dcu_multi_regex_add(dcu_multi_regex* x, const du_uchar* regex, du_int cflags);

/**
 *  Checks whether str string matches regular expression.
 *  @param[in] x  pointer to the dcu_multi_regex data structure.
 *  @param[in] str target character string.
 *  @param[in] logical_and Specify 1 if all of the regex need to match.
 *             Specify 0 if one of the regex need to match.
 *  @return  true if str string matches regular expression, false otherwise.
 */
extern du_bool dcu_multi_regex_is_match(dcu_multi_regex* x, const du_uchar* str, du_bool logical_and);

#ifdef __cplusplus
}
#endif

#endif
