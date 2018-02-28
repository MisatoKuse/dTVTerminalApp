/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DMP_EVENT_ADAPTER_H
#define DMP_EVENT_ADAPTER_H

#include <dupnp_cp_dvcmgr.h>
#include <dupnp_cp_evtmgr.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void (*dmp_event_adapter_allow_join_handler)(dupnp_cp_dvcmgr_device* device, void* handler_arg);

typedef void (*dmp_event_adapter_join_handler)(dupnp_cp_dvcmgr_device* device, void* handler_arg);

typedef void (*dmp_event_adapter_leave_handler)(dupnp_cp_dvcmgr_device* device, void* handler_arg);

typedef void (*dmp_event_adapter_netif_change_handler)(void* handler_arg);

typedef struct dmp_event_adapter {
    dupnp* _upnp;
    dupnp_cp_dvcmgr* _dm;
    dupnp_cp_evtmgr* _em;

    du_ptr_array _join_handler_array;
    du_ptr_array _leave_handler_array;
    du_ptr_array _netif_change_handler_array;
} dmp_event_adapter;

extern void dmp_event_adapter_init(dmp_event_adapter* ea, dupnp* upnp, dupnp_cp_dvcmgr* dm, dupnp_cp_evtmgr* em);

extern void dmp_event_adapter_free(dmp_event_adapter* ea);

extern du_bool dmp_event_adapter_set_join_handler(dmp_event_adapter* ea, dmp_event_adapter_join_handler handler, void* handler_arg);

extern du_bool dmp_event_adapter_set_leave_handler(dmp_event_adapter* ea, dmp_event_adapter_leave_handler handler, void* handler_arg);

extern du_bool dmp_event_adapter_set_netif_change_handler(dmp_event_adapter* ea, dmp_event_adapter_netif_change_handler handler, void* handler_arg);

#ifdef __cplusplus
}
#endif

#endif
