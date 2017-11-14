/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_mcast interface provides some methods for multicast communication
 *  (such as open, close, join, leave).
 */

#ifndef DU_MCAST_H
#define DU_MCAST_H

#include "du_ip.h"
#include "du_socket_os.h"
#include "du_socket_array.h"
#include "du_netif.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Creates a new socket, joins the socket to multicast group
 *  on the specified interface.
 *  @param[in] socket  pointer to the du_socket data referencing a new socket.
 *  @param[in] mcast_ip multicast group.
 *  @param[in] netif pointer to du_netif structure stored information of
 *   the interface for multicast traffic.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_mcast_open(du_socket* socket, const du_ip* mcast_ip, const du_netif* netif);

/**
 *  Creates multiple new sockets, joins the sockets to multicast group
 *  on the specified interfaces.
 *  @param[in] socket_array  pointer to the du_socket_array data referencing new sockets.
 *  @param[in] mcast_ip multicast group.
 *  @param[in] netif_array pointer to du_netif_array structure stored information of
 *   the interfaces for multicast traffic.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_mcast_open_multiple(du_socket_array* socket_array, const du_ip* mcast_ip, du_netif_array* netif_array);

/**
 *  Closes the socket.
 *  @param[in] socket  pointer to the du_socket data referencing the socket.
 */
extern void du_mcast_close(du_socket socket);

/**
 *  Closes the multiple sockets.
 *  @param[in] socket_array  pointer to the du_socket_array data referencing the sockets.
 */
extern void du_mcast_close_multiple(du_socket_array* socket_array);

/**
 *  Sets the outgoing interface for multicast traffic.
 *  @param[in] s  the du_socket data referencing the socket.
 *  @param[in] ip IP address of the outgoing interface for multicast traffic.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_mcast_set_if(du_socket s, const du_ip* ip);

/**
 *  Gets the outgoing interface for multicast traffic.
 *  @param[in] s  du_socket data referencing the socket.
 *  @param[out] ip IP address of the outgoing interface for multicast traffic.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_mcast_get_if(du_socket s, du_ip* ip);

/**
 *  Joins the socket to multicast group on the specified interface.
 *  @param[in] s  du_socket data referencing the socket.
 *  @param[in] mcast_ip IP address of the multicast group.
 *  @param[in] netif pointer to du_netif structure stored information of
 *   the interface for multicast traffic.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_mcast_join(du_socket s, const du_ip* mcast_ip, const du_netif* netif);

/**
 *  Leaves the specified multicast group from the specified interface.
 *  @param[in] s  du_socket data referencing the socket.
 *  @param[in] mcast_ip IP address of the multicast group.
 *  @param[in] netif pointer to du_netif structure stored information of
 *   the interface for multicast traffic.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_mcast_leave(du_socket s, const du_ip* mcast_ip, const du_netif* netif);

/**
 *  Sets the time-to-live (TTL) value associated with IP multicast traffic on the socket.
 *  @param[in] s  du_socket data referencing the socket.
 *  @param[in] family IP address family ( DU_IP_FAMILY_V4 or DU_IP_FAMILY_V6 ).
 *  @param[in] ttl  ttl value to set.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 */
extern du_bool du_mcast_set_ttl(du_socket s, du_ip_family family, du_uint8 ttl);

#ifdef __cplusplus
}
#endif

#endif
