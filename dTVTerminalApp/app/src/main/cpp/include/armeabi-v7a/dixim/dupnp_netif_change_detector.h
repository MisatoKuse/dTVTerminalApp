/*
 * Copyright (c) 2018 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_NETIF_CHANGE_DETECTOR_H
#define DUPNP_NETIF_CHANGE_DETECTOR_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dupnp_netif_change_detector dupnp_netif_change_detector;

typedef void (*dupnp_netif_change_detector_netif_changed)(void* arg);

struct dupnp_netif_change_detector {
    void (*free)(dupnp_netif_change_detector* x);
    void (*set_netif_changed_handler)(dupnp_netif_change_detector* x, dupnp_netif_change_detector_netif_changed handler, void* handler_arg);
    du_bool (*start)(dupnp_netif_change_detector* x);
    void (*stop)(dupnp_netif_change_detector* x);
};

#ifdef __cplusplus
}
#endif

#endif
