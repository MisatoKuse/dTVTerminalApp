/*
 * Copyright (c) 2006 DigiOn, Inc. All rights reserved.
 */

#ifndef DUPNP_SSDP_H
#define DUPNP_SSDP_H

#include <dupnp_impl.h>
#include <du_socket.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dupnp_ssdp_receive_packet(du_socket socket, du_uchar* buf, du_uint32 len, du_ip* remote_ip);

extern du_bool dupnp_ssdp_init(dupnp_ssdp* ssdp, dupnp_impl* upnp, du_uint16 port);

extern void dupnp_ssdp_free(dupnp_ssdp* ssdp);

extern du_bool dupnp_ssdp_enable_listener(dupnp_ssdp* ssdp, du_bool flag);

extern du_bool dupnp_ssdp_start(dupnp_ssdp* ssdp);

extern void dupnp_ssdp_stop(dupnp_ssdp* ssdp);

extern du_bool dupnp_ssdp_get_if(dupnp_ssdp* ssdp, du_socket socket, du_ip* ip);

extern du_bool dupnp_ssdp_get_socket(dupnp_ssdp* ssdp, du_ip* ip, du_socket* socket);

#ifdef __cplusplus
}
#endif

#endif
