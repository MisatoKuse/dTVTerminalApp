/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_SSDP_CP_H
#define DUPNP_SSDP_CP_H

#include <dupnp_cp.h>
#include <dupnp_ssdp.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_ssdp_cp_set_alive_handler(dupnp_ssdp* ssdp, dupnp_cp_ssdp_alive_handler handler, void* arg);

extern du_bool dupnp_ssdp_cp_set_byebye_handler(dupnp_ssdp* ssdp, dupnp_cp_ssdp_byebye_handler handler, void* arg);

extern du_bool dupnp_ssdp_cp_set_response_handler(dupnp_ssdp* ssdp, dupnp_cp_ssdp_response_handler handler, void* arg);

extern du_bool dupnp_ssdp_cp_remove_alive_handler(dupnp_ssdp* ssdp, dupnp_cp_ssdp_alive_handler handler);

extern du_bool dupnp_ssdp_cp_remove_alive_handler2(dupnp_ssdp* ssdp, dupnp_cp_ssdp_alive_handler handler, void* arg);

extern du_bool dupnp_ssdp_cp_remove_byebye_handler(dupnp_ssdp* ssdp, dupnp_cp_ssdp_byebye_handler handler);

extern du_bool dupnp_ssdp_cp_remove_byebye_handler2(dupnp_ssdp* ssdp, dupnp_cp_ssdp_byebye_handler handler, void* arg);

extern du_bool dupnp_ssdp_cp_remove_response_handler(dupnp_ssdp* ssdp, dupnp_cp_ssdp_response_handler handler);

extern du_bool dupnp_ssdp_cp_remove_response_handler2(dupnp_ssdp* ssdp, dupnp_cp_ssdp_response_handler handler, void* arg);

extern void dupnp_ssdp_cp_set_pre_search_hook(dupnp_ssdp* ssdp, dupnp_cp_ssdp_pre_search_hook hook, void* arg);

extern du_bool dupnp_ssdp_cp_enable_listener(dupnp_ssdp* ssdp, du_bool flag);

extern du_bool dupnp_ssdp_cp_enable_search(dupnp_ssdp* ssdp, du_bool flag);

extern du_bool dupnp_ssdp_cp_start(dupnp_ssdp* ssdp);

extern void dupnp_ssdp_cp_stop(dupnp_ssdp* ssdp);

extern void dupnp_ssdp_cp_notify_handler(dupnp_ssdp* ssdp, dupnp_ssdp_info* info);

extern du_bool dupnp_ssdp_cp_search(dupnp_ssdp* ssdp, const du_str_array* header, const du_uchar* st, du_uint32 mx);

#ifdef __cplusplus
}
#endif

#endif
