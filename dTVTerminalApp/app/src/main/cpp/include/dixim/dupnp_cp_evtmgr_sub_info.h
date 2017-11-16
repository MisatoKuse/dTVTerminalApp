/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_CP_EVTMGR_SUB_INFO_H
#define DUPNP_CP_EVTMGR_SUB_INFO_H

#include <dupnp_cp.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dupnp_cp_evtmgr_sub_info {
    du_uchar* event_sub_url;
    du_uchar* sid;
    dupnp_cp_gena_event_handler event_handler;
    void* event_handler_arg;
} dupnp_cp_evtmgr_sub_info;

extern du_bool dupnp_cp_evtmgr_sub_info_init(dupnp_cp_evtmgr_sub_info* si, const du_uchar* event_sub_url, dupnp_cp_gena_event_handler handler, void* arg);

extern void dupnp_cp_evtmgr_sub_info_free(dupnp_cp_evtmgr_sub_info* si);

extern du_bool dupnp_cp_evtmgr_sub_info_set_sid(dupnp_cp_evtmgr_sub_info* si, const du_uchar* sid);

#ifdef __cplusplus
}
#endif

#endif
