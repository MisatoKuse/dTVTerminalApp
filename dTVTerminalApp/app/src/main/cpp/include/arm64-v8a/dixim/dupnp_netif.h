/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_NETIF_H
#define DUPNP_NETIF_H

#include <du_netif_array.h>
#include <du_mutex.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef void (*dupnp_netif_change_handler)(void* arg);

typedef struct dupnp_netif {
    du_mutex mutex;
    du_netif_array netif_array;
    du_netif_array tmp_netif_array;
    du_bool changed;
    du_netif_list_filter filter;
    void* filter_arg;
} dupnp_netif;

extern du_bool dupnp_netif_init(dupnp_netif* netif);

extern void dupnp_netif_free(dupnp_netif* netif);

extern du_bool dupnp_netif_check(dupnp_netif* netif);

extern du_bool dupnp_netif_get_array(dupnp_netif* netif, du_netif_array* array);

extern du_bool dupnp_netif_is_changed(dupnp_netif* netif);

extern du_bool dupnp_netif_set_list_filter(dupnp_netif* netif, du_netif_list_filter filter, void* arg);

extern void dupnp_netif_remove_list_filter(dupnp_netif* netif);

#ifdef __cplusplus
}
#endif

#endif
