/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 *
 * $Id$
 */

#ifndef CHECK_WLAN_WEP_H
#define CHECK_WLAN_WEP_H

#include <ddtcp.h>
#include <du_ip.h>

#ifdef __cplusplus
extern "C" {
#endif

extern ddtcp_ret check_wlan_wep_init();

extern ddtcp_ret check_wlan_wep(ddtcp_wlan_wep_check_point check_point, const du_uchar local_addr_str[DU_IP_STR_SIZE], void* arg, du_bool* wlan_wep_off);

extern void check_wlan_wep_free();

#ifdef __cplusplus
}
#endif

#endif
