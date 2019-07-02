/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
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
 *  Gets the multicast IP v4 address reserved for SSDP by IANA.
 *  The address is equivalent to 239.255.255.255 in dotted-quad notation.
 */
extern const du_uint8* du_ssdp_ip_addr4(void);

/**
 *  Gets the multicast IP v6 address reserved for SSDP by IANA.
 *  The address is equivalent to FF02::C.
 */
extern const du_uint8* du_ssdp_ip_addr6(void);

/**
 *  Gets the multicast IP v4 address string reserved for SSDP by IANA.
 *  This address is "239.255.255.255".
 */
extern const du_uchar* du_ssdp_ip_str4(void);

/**
 *  Gets the multicast IP v6 address string reserved for SSDP by IANA.
 *  This address is "[FF02::C]".
 */
extern const du_uchar* du_ssdp_ip_str6(void);

/**
 *  Gets the multicast IP v4 address and port string reserved for SSDP by IANA.
 *  This address is "239.255.255.255:1900".
 */
extern const du_uchar* du_ssdp_ip_port_str4(void);

/**
 *  Gets the multicast IP v6 address and port string reserved for SSDP by IANA.
 *  This address is "[FF02::C]:1900".
 */
extern const du_uchar* du_ssdp_ip_port_str6(void);

/**
 *  Gets the port reserved for SSDP by IANA.
 *  The port is 1900.
 */
extern du_uint16 du_ssdp_port(void);

/**
 *  Gets the port string reserved for SSDP by IANA.
 *  The port is "1900".
 */
extern const du_uchar* du_ssdp_port_str(void);

/**
 *  Sets the port number for SSDP.
 *  The default port number is 1900.
 */
extern void du_ssdp_set_port(du_uint16 port);

#ifdef __cplusplus
}
#endif

#endif
