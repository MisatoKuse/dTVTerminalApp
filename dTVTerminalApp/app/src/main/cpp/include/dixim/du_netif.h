/*
 * Copyright (c) 2016 DigiOn, Inc. All rights reserved.
 */


/**
 * @file
 *   The du_netif interface provides a method for getting
 *  the addresses associated with the adapters on the local computer.
 */

#ifndef DU_NETIF_H
#define DU_NETIF_H

typedef struct du_netif du_netif;

#include <du_ip_os.h>
#include <du_netif_os.h>
#include <du_type.h>
#include <du_netif_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 *  Maximum size of a MAC address.
 */
#define DU_NETIF_MAC_SIZE 6

/**
 *  Maximum size of a string representation of a MAC address.
 */
#define DU_NETIF_MAC_STR_SIZE 20

/**
 *  Structure of network interface.
 */
struct du_netif {
    /**
     *   Interface name.
     */
    du_uchar name[DU_NETIF_MAX_NAME_SIZE];

    /**
     *   Mac address.
     */
    du_uint8 mac[DU_NETIF_MAC_SIZE];

    /**
     *   IP address.
     */
    du_ip ip;

    /**
     *   Net mask address.
     */
    du_ip mask;

    /**
     *   Whether or not this interface is up.
     */
    du_bool up;

    /**
     *   index.
     */
    du_uint32 index;

};

/**
 *  An interface of callback function which selects local network interfaces to be used.
 *
 *  @param[in] nif a pointer to du_netif represents a local network interface found.
 *  @param[out] is_ignored set true if the nif must not be included in a list du_netif_get_list() returns. Default is false.
 *  @param[in] arg a pointer to user data.
 *  @return true if the function succeeded, false otherwise.
 */
typedef du_bool (*du_netif_list_filter)(du_netif* nif, du_bool* is_ignored, void* arg);

/**
 *  Retrieves all network interfaces of IPv4.
 *  @param[out] array pointer to du_netif_array to receive the information of the addresses.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark array is a pointer to a du_netif_array initialized by
 *  the <b>du_netif_array_init</b> function.
 */
extern du_bool du_netif_get_list(du_netif_array* array);

/**
 *  Retrieves user-specified network interfaces of IPv4.
 *  Elements of the array are able to filtered with the filter callback function.
 *  In this API, callback function which is set by du_netif_set_netif_list_filter() isn't called.
 *
 *  @param[in] filter a callback function implements <em>du_netif_list_filter</em> interface.
 *  @param[in] arg a pointer to user data which is passed to the <em>filter</em>.
 *  @param[out] array pointer to du_netif_array to receive the information of the addresses.
 *  @return  true if the function succeeds.
 *           false if the function fails.
 *  @remark array is a pointer to a du_netif_array initialized by
 *  the <b>du_netif_array_init</b> function.
 */
extern du_bool du_netif_get_list2(du_netif_list_filter filter, void* arg, du_netif_array* array);

/**
 *  Sets a callback function which selects local network interface to be used.
 *  User can select items of a list which du_netif_get_list() returns.
 *  This callback is called by each interface found.
 *
 *  @param[in] filter a callback function implements <em>du_netif_list_filter</em> interface.
 *  @param[in] arg a pointer to user data which is passed to the <em>filter</em>.
 *  @remark This callback affects to results of all du_netif_get_list() calls in an application process.
 */
extern void du_netif_set_netif_list_filter(du_netif_list_filter filter, void* arg);

/**
 *  Removes the callback function which is set by du_netif_set_netif_list_filter().
 */
extern void du_netif_remove_netif_list_filter(void);

/**
 *  Converts binary MAC address to a string format.
 *  @param[out] s string result.
 *  @param[in] mac MAC address to be converted.
 *  @return the length of s string.
 *  @remark s is a hexadecimal string like the following format: 12:34:56:78:9a:bc
 */
extern du_uint32 du_netif_mac_fmt(du_uchar s[DU_NETIF_MAC_STR_SIZE], const du_uint8 mac[DU_NETIF_MAC_SIZE]);

/**
 *  Converts a string format of MAC address to an binary format.
 *  @param[in] s hexadecimal string of MAC address to be converted.
 *  @param[out] mac the string's MAC value.
 *  @return the index position of the character that stops scan.
 *   If s cannot recognize as MAC address, returns zero.
 *  @remark This function stops reading the string s at the first
 *    character it cannot recognize as part of a number.
 *   s is a hexadecimal string like the following format: 12:34:56:78:9a:bc
 */
extern du_uint32 du_netif_mac_scan(const du_uchar s[DU_NETIF_MAC_STR_SIZE], du_uint8 mac[DU_NETIF_MAC_SIZE]);

/**
 *  Checks if the specified remote_ip is in the same network of the netif.
 *  @param[in] netif pointer to du_netif structure to compare.
 *  @param[in] remote_ip pointer to du_ip structure to compare.
 *  @return true if the IP address of netif is found to
 *    match the IP address of remote_ip.
 *   false, otherwise.
 */
extern du_bool du_netif_network_addr_include(const du_netif* netif, const du_ip* remote_ip);

/**
 *  Checks if the specified remote_ip is in the same network of the netif.
 *  @param[in] netif pointer to du_netif structure to compare.
 *  @param[in] remote_ip pointer to du_ip structure to compare.
 *  @return true if the IP address of netif is found to
 *    match the IP address of remote_ip.
 *   false, otherwise.
 */
#define du_netif_network_addr_equal(netif, remote_ip) du_netif_network_addr_include(netif, remote_ip)

// The following APIs are internal use only.
extern du_netif_list_filter du_netif_get_netif_list_filter(void);

extern void* du_netif_get_netif_list_filter_arg(void);

#ifdef __cplusplus
}
#endif

#endif
