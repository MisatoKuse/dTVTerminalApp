/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */

/**
 * @file
 *  The du_arp interface provides methods for getting IP-to-physical address
 *  mapping information.
 */

#ifndef DU_ARP_H
#define DU_ARP_H

#include <du_netif.h>
#include <du_arp_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  IP address and MAC address.
 */
typedef struct du_arp {
    /**
     *  IP address.
     */
    du_ip ip;

    /**
     *  MAC address.
     */
    du_uint8 mac[DU_NETIF_MAC_SIZE];
} du_arp;

/**
 *  Gets IP-to-physical address mapping information and stores them in array.
 *
 *  @param[out] array a pointer to an du_arp_array structure for receiving
 *              the IP-to-physical address mapping information.
 *  @retval 1 if the function succeeds.
 *  @retval 0 false if the function fails.
 *  @pre @p array must be initialized by du_arp_array_init().
 *  @see du_arp_array
 */
extern du_bool du_arp_get_list(du_arp_array* array);

#ifdef __cplusplus
}
#endif

#endif
