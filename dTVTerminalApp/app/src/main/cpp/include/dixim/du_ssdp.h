/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_ssdp interface provides methods for getting
 *  IP address and port information reserved for SSDP (Simple Service Discovery Protocol)
 *  by IANA ( Internet Assigned Numbers Authority ).
 */

#ifndef DU_SSDP_H
#define DU_SSDP_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Gets the multicast IP address reserved for SSDP by IANA.
 *  The address is equivalent to 239.255.255.255 in dotted-quad notation.
 */
extern const du_uint8* du_ssdp_ip_addr4(void);

/**
 *  Gets the multicast IP address string reserved for SSDP by IANA.
 *  This address is "239.255.255.255".
 */
extern const du_uchar* du_ssdp_ip_str4(void);

/**
 *  Gets the multicast IP address and port string reserved for SSDP by IANA.
 *  This address is "239.255.255.255:1900".
 */
extern const du_uchar* du_ssdp_ip_port_str4(void);

/**
 *  Gets the port reserved for SSDP by IANA.
 *  The port is 1900.
 */
extern du_uint16 du_ssdp_port(void);

#ifdef __cplusplus
}
#endif

#endif
