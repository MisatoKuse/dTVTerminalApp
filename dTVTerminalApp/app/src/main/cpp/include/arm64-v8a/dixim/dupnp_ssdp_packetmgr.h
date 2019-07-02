/*
 * Copyright (c) 2009 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_SSDP_PACKETMGR_H
#define DUPNP_SSDP_PACKETMGR_H

#include <du_socket.h>
#include <du_ip.h>
#include <du_str_array.h>
#include <du_socket_array.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dupnp_ssdp_packetmgr {
    du_socket_array _s;
    du_ip_array _ip;
    du_str_array _sa;
    du_uint32 _interval_ms;
} dupnp_ssdp_packetmgr;

extern du_bool dupnp_ssdp_packetmgr_init(dupnp_ssdp_packetmgr* x, du_uint32 interval_ms);

extern void dupnp_ssdp_packetmgr_free(dupnp_ssdp_packetmgr* x);

extern du_bool dupnp_ssdp_packetmgr_add(dupnp_ssdp_packetmgr* x, const du_ip* ip, du_socket s, const du_uchar* data);

extern du_bool dupnp_ssdp_packetmgr_send(dupnp_ssdp_packetmgr* x);

#ifdef __cplusplus
}
#endif

#endif

