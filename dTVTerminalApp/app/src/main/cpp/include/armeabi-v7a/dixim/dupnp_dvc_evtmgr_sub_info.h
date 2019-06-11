/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_dvc_evtmgr_sub_info.h
 */

#ifndef DUPNP_DVC_EVTMGR_SUB_INFO_H
#define DUPNP_DVC_EVTMGR_SUB_INFO_H

#include <du_ip.h>
#include <du_str_array.h>
#include <du_time.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * This structure contains information of a subscription.
 */
typedef struct dupnp_dvc_evtmgr_sub_info {
    du_str_array callback;      //!< Callback URL of subscriber.
    du_uchar_array sid;         //!< Subscription identifier.
    du_uint32 seq;              //!< Eventy key.
    du_time expiration;         //!< Expiration date.
    du_ip remote_ip;            //!< IP address of subscriber.
} dupnp_dvc_evtmgr_sub_info;

extern du_bool dupnp_dvc_evtmgr_sub_info_init(dupnp_dvc_evtmgr_sub_info* si, const du_uchar* callback, du_uint32 subscription_timeout, const du_ip* remote_ip);

extern void dupnp_dvc_evtmgr_sub_info_free(dupnp_dvc_evtmgr_sub_info* si);

#ifdef __cplusplus
}
#endif

#endif
