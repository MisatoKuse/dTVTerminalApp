/*
 * Copyright (c) 2019 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *  The du_os interface provides a method for obtaining
 *  information about the version of the Operating System (OS) that is currently
 *  running.
 */

#ifndef DU_OS_H
#define DU_OS_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  du_os_version structure contains OS version information.
 */
typedef struct du_os_version {
    /**
     *  OS name
     */
    du_uchar name[64];

    /**
     *  OS version
     */
    du_uchar version[64];
} du_os_version;

/**
 *  Obtains information about OS that is currently running.
 *
 *  @param[out] version pointer to du_os_version structure to receive version
 *              information.
 *  @retval 1 function succeeds.
 *  @retval 0 function fails.
 */
extern du_bool du_os_get_version(du_os_version* version);

#ifdef __cplusplus
}
#endif

#endif
