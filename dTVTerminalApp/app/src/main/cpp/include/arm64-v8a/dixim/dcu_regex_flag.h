/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *    The dcu_regex_flag defines flags  for regular expression.
 */

#ifndef DCU_REGEX_FLAG_H
#define DCU_REGEX_FLAG_H

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  The enumeration of flags for regular expression.
 */
typedef enum dcu_regex_flag {
    /**
     *   Ignore case in match.
     */
    DCU_REGEX_FLAG_ICASE    = (1<<0),

    /**
     *   Change the handling of <newline>s, as described in the text.
     */
    DCU_REGEX_FLAG_NEWLINE  = (1<<1),

    /**
     *   The first character of the string pointed to by string is not the beginning of the line.
     *   Therefore, the circumflex character ( '^' ), when taken as a special character,
     *   shall not match the beginning of string.
     */
    DCU_REGEX_FLAG_NOTBOL   = (1<<2),

    /**
     *   The last character of the string pointed to by string is not the end of the line.
     *   Therefore, the dollar sign ( '$' ), when taken as a special character,
     *   shall not match the end of string.
     */
    DCU_REGEX_FLAG_NOTEOL   = (1<<3),

    /**
     *   Use Extended Regular Expressions.
     */
    DCU_REGEX_FLAG_EXTENDED = (1<<4),

    /**
     *   Report only success/fail.
     */
    DCU_REGEX_FLAG_NOSUB    = (1<<5),
} dcu_regex_flag;

#ifdef __cplusplus
}
#endif

#endif
