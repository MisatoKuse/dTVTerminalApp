/*
 * Copyright (c) 2018 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_NETIF_CHANGE_DETECTOR_INTERVAL_H
#define DUPNP_NETIF_CHANGE_DETECTOR_INTERVAL_H

#include <dupnp_netif_change_detector.h>
#include <dupnp_impl.h>

#ifdef __cplusplus
extern "C" {
#endif

extern dupnp_netif_change_detector* dupnp_netif_change_detector_interval_create(dupnp_impl* upnp, du_uint32 interval_sec);

#ifdef __cplusplus
}
#endif

#endif
