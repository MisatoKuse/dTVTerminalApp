/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_locale interface provides a method for getting an locale information.
 */

#ifndef DU_LOCALE_H
#define DU_LOCALE_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  This structure contains some locale informations.
 *  If you want to know more information about structure members,
 *  see setlocale(3) man pages.
 */
typedef struct du_locale_info {
    /**
     *   language.
     *   NULL is set if locale is C or POSIX.
     */
    du_uchar* language;

    /**
     *   territory. (optional)
     */
    du_uchar* territory;

    /**
     *   code sets. (optional)
     */
    du_uchar* codeset;

    /**
     *   modifier. (optional)
     */
    du_uchar* modifier;

    du_uchar _buf[128];
} du_locale_info;

/**
 *  Gets locale information.
 *  @param[out] info   pointer to du_locale_info struct.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 */
extern du_bool du_locale_get(du_locale_info* info);

#ifdef __cplusplus
}
#endif

#endif
