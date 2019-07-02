/*
 * Copyright (c) 2017 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_ip interface provides various methods for getting/setting Internet Protocol (IP)
 *   address data (such as getting/setting IP address information to du_ip structure).
 *   du_ip interface supports IPv4 and IPv6. Before using a du_ip, use <b>du_ip_init</b>
 *   to initialize a du_ip data structure.
 */

#ifndef DU_IP_H
#define DU_IP_H

#include <du_ip_os.h>
#include <du_type.h>
#include <du_ip_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Maximum size of an IP address (v4 and v6).
 */
#define DU_IP_ADDR_SIZE 16

/**
 *  Maximum size of a string representation of the IP address.
 */
#define DU_IP_STR_SIZE 46

/**
 *  Enumeration of the IP address family.
 */
typedef enum {
    /**
     *   Unknown.
     */
    DU_IP_FAMILY_UNKNOWN,

    /**
     *   Version 4
     */
    DU_IP_FAMILY_V4,

    /**
     *   Version 6
     */
    DU_IP_FAMILY_V6,

} du_ip_family;

/**
 *  Gets an IPv4 address string indicating that the server should listen for client activity
 *  on all network interfaces.
 *  This Any IP address is "0.0.0.0".
 */
extern const du_uchar* du_ip_str_any4(void);

/**
 *  Gets an IPv4 loopback address string.
 *  The Loopback address is "127.0.0.1".
 */
extern const du_uchar* du_ip_str_loopback4(void);

/**
 *  Gets an IPv6 address string indicating that the server should listen for client activity
 *  on all network interfaces.
 *  This Any IP address is "::" in compact notation.
 */
extern const du_uchar* du_ip_str_any6(void);

/**
 *  Gets an IPv6 loopback address string.
 *  This Loopback address is "::1" in compact notation.
 */
extern const du_uchar* du_ip_str_loopback6(void);

/**
 *  Gets an IPv4 address indicating that the server should listen for client activity
 *  on all network interfaces.
 *  This value is equivalent to 0.0.0.0 in dotted-quad notation.
 */
extern const du_uint8* du_ip_addr_any4(void);

/**
 *  Gets an IPv4 loopback address.
 *  The Loopback address is equivalent to 127.0.0.1 in dotted-quad notation.
 */
extern const du_uint8* du_ip_addr_loopback4(void);

/**
 *  Gets an IPv6 address indicating that the server should listen for client activity
 *  on all network interfaces.
 *  This Any IP address is equivalent to 0:0:0:0:0:0:0:0 in colon-hexadecimal notation,
 */
extern const du_uint8* du_ip_addr_any6(void);

/**
 *  Gets an IPv6 loopback address.
 *  The Loopback address is equivalent to 0:0:0:0:0:0:0:1 in colon-hexadecimal notation.
 */
extern const du_uint8* du_ip_addr_loopback6(void);

/**
 *  Checks whether the specified ip is ANY address.
 *  @param[in] ip   pointer to the du_ip data structure.
 *  @return  true if the specified ip is ANY address.
 *           otherwise false.
 */
extern du_bool du_ip_is_any(const du_ip* ip);

/**
 *  Checks whether the specified ip is IP loopback address.
 *  @param[in] ip   pointer to the du_ip data structure.
 *  @return  true if the specified ip is IP loopback address.
 *           otherwise false.
 */
extern du_bool du_ip_is_loopback(const du_ip* ip);

/**
 *  Checks whether the specified ip is IPv6 Link local address.
 *  @param[in] ip   pointer to the du_ip data structure.
 *  @return true if the <em>ip</em> is link local address. false otherwise.
 */
extern du_bool du_ip_is_link_local6(du_ip* ip);

/**
 *  Initializes a du_ip data structure.
 *  @param[out] ip  pointer to the du_ip data structure.
 */
extern void du_ip_init(du_ip* ip);

/**
 *  Sets an IP address and a port number.
 *  @param[out] ip  pointer to the du_ip data structure.
 *  @param[in] addr_str specifies the IP address string. Accepts IPv6 scoped address(RFC4007, RFC6874).
 *  @param[in] port  specifies the port number.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 *  @see https://tools.ietf.org/html/rfc4007
 *  @see https://tools.ietf.org/html/rfc6874
 */
extern du_bool du_ip_set(du_ip* ip, const du_uchar* addr_str, du_uint16 port);

/**
 *  Sets an address family, an IP address and a port number.
 *  @param[out] ip  pointer to the du_ip data structure.
 *  @param[in] family the address family of the IP address.
 *  @param[in] addr specifies the IP address string.
 *  @param[in] port  specifies the port number.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 */
extern du_bool du_ip_set2(du_ip* ip, du_ip_family family, const du_uint8 addr[DU_IP_ADDR_SIZE], du_uint16 port);

/**
 *  Sets an address family, an IP address, a port number and scope ID.
 *  @param[out] ip  pointer to the du_ip data structure.
 *  @param[in] family the address family of the IP address.
 *  @param[in] addr specifies the IP address string.
 *  @param[in] port  specifies the port number.
 *  @param[in] scope_id  specifies the scope_id number. Always 0 for IPv4 address family.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 */
extern du_bool du_ip_set3(du_ip* ip, du_ip_family family, const du_uint8 addr[DU_IP_ADDR_SIZE], du_uint16 port, du_uint32 scope_id);

/**
 *  Sets Scope ID.
 *  @param[in] ip pointer to the du_ip data structure.
 *  @param[in] scope_id Scope ID.
 *  @return true if the function succeeded. false otherwise.
 *  @remarks If the <em>ip</em> isn't IPv6 address, this function does nothing.
 */
extern du_bool du_ip_set_scope_id(du_ip* ip, du_uint32 scope_id);

/**
 *  Gets Scope ID.
 *  @param[in] ip pointer to the du_ip data structure.
 *  @return Scope ID of the <em>ip</em>. 0 if it's not set.
 *  @remarks If <em>ip</em> isn't IPv6 address, this function returns 0.
 */
extern du_uint32 du_ip_get_scope_id(const du_ip* ip);

/**
 *  Gets an address family, an IP address string and a port number.
 *  @param[in] ip   pointer to the du_ip data structure.
 *  @param[out] family pointer to the variable to receive the address family value of the IP address.
 *  @param[out] addr_str du_uchar array variable to receive the IP address string.
 *  @param[out] port  pointer to the variable to receive the port number.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 *  @remarks
 *   <em>addr_str</em> won't include scope ID of IPv6 address literal.
 *   (i.e. '%<scope>' won't be included in there.)
 *   Please use du_ip_get3() to get scope ID of the address.
 */
extern du_bool du_ip_get(const du_ip* ip, du_ip_family* family, du_uchar addr_str[DU_IP_STR_SIZE], du_uint16* port);

/**
 *  Gets an address family, an IP address and a port number.
 *  @param[in] ip   pointer to the du_ip data structure.
 *  @param[out] family pointer to the variable to receive the address family of the IP address.
 *  @param[out] addr  du_uint8 array variable to receive the IP address.
 *  @param[out] addr_len  pointer to the variable to receive the byte length of of the addr.
 *  @param[out] port  pointer to the variable to receive the port number.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 */
extern du_bool du_ip_get2(const du_ip* ip, du_ip_family* family, du_uint8 addr[DU_IP_ADDR_SIZE], du_uint32* addr_len, du_uint16* port);

/**
 *  Gets an address family, an IP address, a port number and scope ID.
 *  @param[in] ip   pointer to the du_ip data structure.
 *  @param[out] family pointer to the variable to receive the address family of the IP address.
 *  @param[out] addr  du_uint8 array variable to receive the IP address.
 *  @param[out] addr_len  pointer to the variable to receive the byte length of of the addr.
 *  @param[out] port  pointer to the variable to receive the port number.
 *  @param[out] scope_id  pointer to the variable to receive the scope ID. Always 0 for IPv4.
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 *  @remarks The scope ID is always integer value even if the address specified to du_ip_set() had an I/F name as scope ID.
 */
extern du_bool du_ip_get3(const du_ip* ip, du_ip_family* family, du_uint8 addr[DU_IP_ADDR_SIZE], du_uint32* addr_len, du_uint16* port, du_uint32* scope_id);

/**
 *  Compares two IP addresses and port numbers.
 *  @param[in] a   pointer to an du_ip data structure to compare.
 *  @param[in] b   pointer to another du_ip data structure to compare.
 *  @return true if the IP address and port number of a is found respectively
 *  to match b.
 */
extern du_bool du_ip_equal(const du_ip* a, const du_ip* b);

/**
 *  Compares two address families.
 *  @param[in] a   pointer to an du_ip data structure to compare.
 *  @param[in] b   pointer to another du_ip data structure to compare.
 *  @return true if the address family of a is found to match the address family of b.
 *   false, otherwise.
 */
extern du_bool du_ip_family_equal(const du_ip* a, const du_ip* b);

/**
 *  Compares two IP addresses.
 *  @param[in] a   pointer to an du_ip data structure to compare.
 *  @param[in] b   pointer to another du_ip data structure to compare.
 *  @return true if the IP address of a is found to match the IP address of b.
 *   false, otherwise.
 */
extern du_bool du_ip_addr_equal(const du_ip* a, const du_ip* b);

/**
 *  Gets IP address by host name.
 *  @param[out] array  pointer to an array to receive one or more du_ip structures
 *  containing response IP address information about the host.
 *  @param[in] name a string containing a host (node) name or
 *   a numeric host address string. The numeric host address string is a dotted-decimal
 *   IPv4 address or an IPv6 hexadecimal address.
 *  @param[in] port port number
 *  @return  true if the function succeeds.
 *           false if the  function fails.
 *  @remark array is a pointer to a du_ip_array initialized by
 *  the <b>du_ip_array_init</b> function.
 */
extern du_bool du_ip_by_name(du_ip_array* array, const du_uchar* name, du_uint16 port);

/**
 *  Copies an ip infomation from one du_ip structure to another.
 *  @param[out] to the destination du_ip structure.
 *  @param[in] from the source du_ip structure.
 */
extern void du_ip_copy(du_ip* to, const du_ip* from);

#ifdef __cplusplus
}
#endif

#endif
