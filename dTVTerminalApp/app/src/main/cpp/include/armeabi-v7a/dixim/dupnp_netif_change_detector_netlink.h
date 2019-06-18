/*
 * Copyright (c) 2018 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_NETIF_CHANGE_DETECTOR_NETLINK_H
#define DUPNP_NETIF_CHANGE_DETECTOR_NETLINK_H

#include <dupnp_impl.h>
#include <dupnp_netif_change_detector.h>

#ifdef __cplusplus
extern "C" {
#endif

extern dupnp_netif_change_detector* dupnp_netif_change_detector_netlink_create(dupnp_impl* upnp);

#ifdef __cplusplus
}
#endif

#endif
