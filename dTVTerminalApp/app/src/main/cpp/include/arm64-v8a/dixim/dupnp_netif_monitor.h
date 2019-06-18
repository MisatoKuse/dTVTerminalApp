/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_NETIF_MONITOR_H
#define DUPNP_NETIF_MONITOR_H

#include <dupnp_impl.h>
#include <dupnp_netif.h>
#include <du_type.h>
#include <du_ptr_array.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_netif_monitor_init(dupnp_netif_monitor* x, dupnp_impl* upnp);

extern du_bool dupnp_netif_monitor_free(dupnp_netif_monitor* x);

extern du_bool dupnp_netif_monitor_start(dupnp_netif_monitor* x);

extern du_bool dupnp_netif_monitor_stop(dupnp_netif_monitor* x);

extern du_bool dupnp_netif_monitor_set_netif_change_handler(dupnp_netif_monitor* x, dupnp_netif_change_handler handler, void* arg);

extern du_bool dupnp_netif_monitor_remove_netif_change_handler(dupnp_netif_monitor* x, dupnp_netif_change_handler handler);

extern du_bool dupnp_netif_monitor_remove_netif_change_handler2(dupnp_netif_monitor* x, dupnp_netif_change_handler handler, void* arg);

extern du_bool dupnp_netif_monitor_enable_netif_monitor(dupnp_netif_monitor* x, du_bool flag);

extern du_bool dupnp_netif_monitor_get_netif_array(dupnp_netif_monitor* x, du_netif_array* array);

extern du_bool dupnp_netif_monitor_change_state_force(dupnp_netif_monitor* x);

#ifdef __cplusplus
}
#endif

#endif

