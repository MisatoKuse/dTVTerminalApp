/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DU_TIME_OS_H
#define DU_TIME_OS_H

#include <du_type_os.h>
#include <time.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Time since the Epoch (00:00:00 UTC, January 1, 1970), measured in seconds.
 */
typedef time_t du_time;

/**
 * Time since the Epoch (00:00:00 UTC, January 1, 1970), measured in milli-seconds.
 */
typedef du_int64 du_timel;

#ifdef __cplusplus
}
#endif

#endif
