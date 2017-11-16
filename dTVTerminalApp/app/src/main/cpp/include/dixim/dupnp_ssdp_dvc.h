/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_SSDP_DVC_H
#define DUPNP_SSDP_DVC_H

#include <dupnp_ssdp.h>
#include <dupnp_dvc.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_ssdp_dvc_start(dupnp_ssdp* ssdp);

extern void dupnp_ssdp_dvc_stop(dupnp_ssdp* ssdp);

extern du_bool dupnp_ssdp_dvc_enable_listener(dupnp_ssdp* ssdp, du_bool flag);

extern du_bool dupnp_ssdp_dvc_enable_advertisement(dupnp_ssdp* ssdp, du_bool flag);

extern du_bool dupnp_ssdp_dvc_send_alive(dupnp_ssdp* ssdp, const du_str_array* additional_header, du_bool enable_hook);

extern du_bool dupnp_ssdp_dvc_send_alive_no_shared_memory(dupnp_ssdp* ssdp, const du_str_array* additional_header, du_bool enable_hook);

extern du_bool dupnp_ssdp_dvc_send_byebye(dupnp_ssdp* ssdp, const du_str_array* additional_header, du_bool enable_hook);

extern void dupnp_ssdp_dvc_set_alive_hook(dupnp_ssdp* ssdp, dupnp_dvc_ssdp_adv_hook hook, void* arg);

extern void dupnp_ssdp_dvc_set_byebye_hook(dupnp_ssdp* ssdp, dupnp_dvc_ssdp_adv_hook hook, void* arg);

extern du_bool dupnp_ssdp_dvc_send_search_response(dupnp_ssdp* ssdp, du_str_array* additional_header, du_socket s, const du_ip* local_ip, const du_ip* remote_ip, du_uint32 mx, const du_uchar* st);

extern void dupnp_ssdp_dvc_set_search_response_hook(dupnp_ssdp* ssdp, dupnp_dvc_ssdp_search_response_hook hook, void* arg);

extern void dupnp_ssdp_dvc_set_pre_adv_hook(dupnp_ssdp* ssdp, dupnp_dvc_ssdp_pre_adv_hook hook, void* arg);

extern void dupnp_ssdp_dvc_search_handler(dupnp_ssdp* ssdp, du_socket s, dupnp_ssdp_info* info);

extern void dupnp_ssdp_dvc_set_max_age(dupnp_ssdp* ssdp, du_uint32 max_age);

#ifdef __cplusplus
}
#endif

#endif
