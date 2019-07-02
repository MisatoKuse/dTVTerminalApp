/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 *
 * $Id$ 
 */ 

#ifndef DDTCP_UTIL_TIME_H
#define DDTCP_UTIL_TIME_H

#include <tr_util.h>
#include <tr_util_rename.h>

#ifdef __cplusplus
extern "C" {
#endif

extern void ddtcp_util_time_msec(tru_timel* t);
extern void ddtcp_util_time_usec(tru_timel* t);
extern tru_timel ddtcp_util_time_elapsed_msec_from(tru_timel start_msec);
extern du_float32 ddtcp_util_time_elapsed_sec_from(tru_timel start_msec);
extern du_float32 ddtcp_util_time_elapsed_sec(tru_timel start_msec, tru_timel end_msec);

#ifdef __cplusplus
}
#endif

#endif
