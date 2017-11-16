/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_DVC_EVTMGR_H
#define DUPNP_DVC_EVTMGR_H

#include <dupnp.h>
#include <dupnp_dvc_evtmgr_sub_info_array.h>
#include <dupnp_http_server.h>
#include <du_mutex.h>
#include <du_str_array.h>
#include <du_time.h>
#include <du_uint32_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef du_bool (*dupnp_dvc_evtmgr_message_handler)(du_uchar_array* property_set_xml, void* arg);

typedef enum dupnp_dvc_evtmgr_message_type {
    DUPNP_DVC_EVTMGR_MESSAGE_TYPE_UNKNOWN,
    DUPNP_DVC_EVTMGR_MESSAGE_TYPE_MANAGED,
    DUPNP_DVC_EVTMGR_MESSAGE_TYPE_CALLBACK,
} dupnp_dvc_evtmgr_message_type;

typedef struct dupnp_dvc_evtmgr {
    dupnp* upnp;
    du_uint32 subscription_timeout;
    du_uint32 maximum_rate;
    du_time last_evented_time;
    du_mutex mutex;
    du_id32 scheduled_task_id;

    dupnp_dvc_evtmgr_sub_info_array si_array;

    dupnp_dvc_evtmgr_message_type message_type;
    union {
        struct {
            du_str_array current_state_variable_parameters;
            du_str_array new_state_variable_parameters;
        } managed;

        struct {
            du_bool state_variable_changed;
            dupnp_dvc_evtmgr_message_handler initial_message_handler;
            void* initial_message_handler_arg;
            dupnp_dvc_evtmgr_message_handler message_handler;
            void* message_handler_arg;
        } callback;
    } message;
} dupnp_dvc_evtmgr;

extern du_bool dupnp_dvc_evtmgr_init(dupnp_dvc_evtmgr* evtmgr, dupnp* upnp, du_uint32 subscription_timeout, du_uint32 maximum_rate);

extern void dupnp_dvc_evtmgr_free(dupnp_dvc_evtmgr* evtmgr);

extern du_bool dupnp_dvc_evtmgr_start(dupnp_dvc_evtmgr* evtmgr);

extern void dupnp_dvc_evtmgr_stop(dupnp_dvc_evtmgr* evtmgr);

extern void dupnp_dvc_evtmgr_setup(dupnp_dvc_evtmgr* evtmgr, du_uint32 subscription_timeout, du_uint32 maximum_rate);

extern du_bool dupnp_dvc_evtmgr_set_state_variable(dupnp_dvc_evtmgr* evtmgr, const du_uchar* name, const du_uchar* value);

extern du_bool dupnp_dvc_evtmgr_subscribe(dupnp_dvc_evtmgr* evtmgr, const du_uchar* callback, dupnp_http_server* dhs, const du_uchar** error_status);

extern du_bool dupnp_dvc_evtmgr_renewal(dupnp_dvc_evtmgr* evtmgr, const du_uchar* sid, dupnp_http_server* dhs, const du_uchar** error_status);

extern du_bool dupnp_dvc_evtmgr_unsubscribe(dupnp_dvc_evtmgr* evtmgr, const du_uchar* sid, dupnp_http_server* dhs, const du_uchar** error_status);

extern void dupnp_dvc_evtmgr_netif_change_handler(dupnp_dvc_evtmgr* evtmgr);

extern du_bool dupnp_dvc_evtmgr_notify_state_variable_changed(dupnp_dvc_evtmgr* evtmgr);

extern du_bool dupnp_dvc_evtmgr_set_message_handler(dupnp_dvc_evtmgr* evtmgr, dupnp_dvc_evtmgr_message_handler initial_message_handler, void* initial_message_handler_arg, dupnp_dvc_evtmgr_message_handler message_handler, void* message_handler_arg);

#ifdef __cplusplus
}
#endif

#endif
