/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


#ifndef DCU_REGEX_MATCH_H
#define DCU_REGEX_MATCH_H

/**
 * @file
 *    The dcu_regex_match interface provides a structure for regular expression.
 */

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  This structure contains the information of match point with regular expression.
 */
typedef struct dcu_regex_match {
    /**
     *   start offset of the substring match within the string.
     */
    du_uint32 start;

    /**
     *   end offset of the substring match within the string.
     */
    du_uint32 end;

} dcu_regex_match;

#ifdef __cplusplus
}
#endif

#endif
