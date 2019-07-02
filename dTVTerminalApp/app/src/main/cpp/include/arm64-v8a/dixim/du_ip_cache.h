/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *
 */

#ifndef DU_IP_CACHE_H
#define DU_IP_CACHE_H

#include <du_ip.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool du_ip_cache_add(const du_ip* ip);

extern du_bool du_ip_cache_get(du_ip* ip, const du_ip* ip_target);

#ifdef __cplusplus
}
#endif

#endif
