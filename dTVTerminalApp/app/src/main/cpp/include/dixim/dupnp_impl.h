/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_IMPL_H
#define DUPNP_IMPL_H

#include <dupnp_cp.h>
#include <dupnp_dvc.h>
#include <dupnp_gena_cp_sub_info_array.h>
#include <dupnp_dvcdsc_array.h>
#include <dupnp_dvc_service_array.h>
#include <dupnp_asyncsvcmgr_info_array.h>
#include <du_net.h>
#include <du_scheduler.h>
#include <du_type.h>
#include <du_mutex.h>
#include <du_thread.h>
#include <du_socket_array.h>
#include <du_net_task_array.h>
#include <du_ptr_array.h>
#include <du_str_array.h>
#include <du_ip_array.h>
#include <du_arp_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dupnp_impl dupnp_impl;
typedef struct dupnp_svcmgr dupnp_svcmgr;
typedef struct dupnp_taskmgr dupnp_taskmgr;

typedef struct dupnp_gena_cp {
    du_bool enable_gena;
    dupnp_gena_cp_sub_info_array info_list;
    dupnp_gena_cp_sub_info_array tmp_list;
    du_mutex mutex;
    du_id32 renewal_scheduled_task_id;
} dupnp_gena_cp;

/**
 * Structure of dupnp_nettaskmgr.
 */
typedef struct dupnp_nettaskmgr {
    dupnp_taskmgr* taskmgr;
    du_net net;
} dupnp_nettaskmgr;

/**
 * Structure of dupnp_schedtaskmgr.
 */
typedef struct dupnp_schedtaskmgr {
    dupnp_taskmgr* taskmgr;
    du_scheduler scheduler;
} dupnp_schedtaskmgr;

struct dupnp_taskmgr {
    dupnp_impl* upnp;
    du_uint32 stack_size;
    du_thread thread;
    volatile du_bool stop;
    du_bool create_thread;

    dupnp_nettaskmgr nettaskmgr;
    dupnp_schedtaskmgr schedtaskmgr;
};

typedef struct dupnp_ssdp {
    dupnp_impl* upnp;
    du_uint16 search_port;
    du_socket search_socket;
    du_id32 search_socket_task_id;
    du_bool enable_cp_search;
    du_bool enable_dvc_advertisement;
    du_bool enable_cp_listener;
    du_bool enable_dvc_listener;
    du_socket_array ssdp_socket_array;
    du_uint32_array ssdp_socket_task_id_array;
    du_ip_array ssdp_socket_ip_array;
    du_ptr_array alive_handler_array;
    du_ptr_array byebye_handler_array;
    du_ptr_array response_handler_array;
    dupnp_dvc_ssdp_adv_hook alive_hook;
    void* alive_hook_arg;
    dupnp_dvc_ssdp_adv_hook byebye_hook;
    void* byebye_hook_arg;
    dupnp_dvc_ssdp_search_response_hook search_response_hook;
    void* search_response_hook_arg;
    dupnp_dvc_ssdp_pre_adv_hook pre_adv_hook;
    void* pre_adv_hook_arg;
    dupnp_cp_ssdp_pre_search_hook pre_search_hook;
    void* pre_search_hook_arg;
    du_uint32 max_age;
    du_id32 advertize_scheduled_task_id;
    du_id32 rebind_task_id;

    du_bool enable_listener_sockets;
    du_mutex mutex;
} dupnp_ssdp;

typedef struct dupnp_http {
    dupnp_impl* upnp;
    du_bool enable_cp_http;
    du_bool enable_dvc_http;
    du_uint16 port;
    du_socket socket;
    du_id32 socket_task_id;
    du_uchar* server_name;
    du_ptr_array access_control_handler_array;
    du_ptr_array server_request_event_listener_array;
    du_arp_array arp_array;
    du_netif_array netif_array;

    du_mutex mutex;
} dupnp_http;

typedef struct dupnp_gena {
    dupnp_impl* upnp;
    dupnp_gena_cp gena_cp;
} dupnp_gena;

typedef struct dupnp_dvcdscmgr {
    dupnp_impl* upnp;
    dupnp_dvcdsc_array dvcdsc_array;
} dupnp_dvcdscmgr;

typedef struct dupnp_asyncsvcmgr {
    dupnp_svcmgr* svcmgr;
    dupnp_asyncsvcmgr_info_array info_array;
    du_uint32 latest_id;
    du_mutex mutex;
} dupnp_asyncsvcmgr;

struct dupnp_svcmgr {
    dupnp_impl* upnp;
    dupnp_dvc_service_array service_array;
    du_ptr_array _dlib_array;
    dupnp_asyncsvcmgr asyncsvcmgr;
    du_ptr_array _pre_upnp_action_handler_array;
};

typedef struct dupnp_netif_monitor {
    dupnp_impl* upnp;
    du_bool enable_netif_monitor;
    du_id32 netif_scheduled_task_id;
    du_ptr_array netif_change_handler_array;
    dupnp_netif netif;
    du_bool changed_force;
    du_mutex mutex;
} dupnp_netif_monitor;

typedef struct dupnp_shared_memory {
    dupnp_impl* upnp;
    du_uint8* buf;
    du_str_array buf_sa;
    du_str_array buf_sa2;
    du_uchar_array buf_ua;
    du_uchar_array buf_ua2;
    du_uchar_array buf_ua3;
    du_uchar_array buf_ua4;
    du_uchar_array buf_ua5;
    du_uchar_array buf_ua6;
} dupnp_shared_memory;

struct dupnp_impl {
    dupnp* upnp;
    volatile du_bool running;
    du_uint32 max_body_size;
    dupnp_taskmgr taskmgr;
    dupnp_ssdp ssdp;
    dupnp_http http;
    dupnp_gena gena;
    dupnp_dvcdscmgr dvcdscmgr;
    dupnp_svcmgr svcmgr;
    dupnp_netif_monitor netif_monitor;
    dupnp_shared_memory shared_memory;
    du_str_array url_alias_param_array;
    du_str_array sendable_name_array;
    du_str_array placeholder_param_array;
    volatile du_bool _dvc_setup;
    du_uchar* software_code;
};

#ifdef __cplusplus
}
#endif

#endif
