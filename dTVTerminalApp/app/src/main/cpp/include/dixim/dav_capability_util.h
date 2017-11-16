/*
 * Copyright (c) 2008 DigiOn, Inc. All rights reserved.
 */

#ifndef DAV_CAPABILITY_UTIL_H
#define DAV_CAPABILITY_UTIL_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dav_capability_util_is_pn_param(const du_uchar* param);

extern du_bool dav_capability_util_is_pn_param2(const du_uchar* param, du_bool* has_dlna_pn);

#ifdef __cplusplus
}
#endif

#endif
