/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVC_EVTMGR_SUB_INFO_H
#define DUPNP_DVC_EVTMGR_SUB_INFO_H

#include <du_str_array.h>
#include <du_time.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dupnp_dvc_evtmgr_sub_info {
    du_str_array callback;
    du_uchar_array sid;
    du_uint32 seq;
    du_time expiration;
} dupnp_dvc_evtmgr_sub_info;

extern du_bool dupnp_dvc_evtmgr_sub_info_init(dupnp_dvc_evtmgr_sub_info* si, const du_uchar* callback, du_uint32 subscription_timeout);

extern void dupnp_dvc_evtmgr_sub_info_free(dupnp_dvc_evtmgr_sub_info* si);

#ifdef __cplusplus
}
#endif

#endif
