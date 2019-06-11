/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_cp_dvcmgr_device.h
 *  @brief The dupnp_cp_dvcmgr_device interface provides some methods to manipulate the
 * dupnp_cp_dvcmgr_device structure that stores the Device information.
 */

#ifndef DUPNP_CP_DVCMGR_DEVICE_H
#define DUPNP_CP_DVCMGR_DEVICE_H

#include <du_ip.h>
#include <du_time.h>
#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * dupnp_cp_dvcmgr_device structure contains the device information(IP address,
 * location, UDN, device type, etc.).
 */
typedef struct dupnp_cp_dvcmgr_device {
    du_ip ip;                /**< IP address of device. */
    du_uchar* location;      /**< URL for UPnP description. */
    du_uchar* udn;           /**< UDN(Unique Device Name) string. */
    du_uchar* device_type;   /**< UPnP device type */
    du_time expiration;      /**< expiration time of advertisement of the device given by the number of seconds since January 1, 1970, 00:00:00. */

    void* user_data;         /**< pointer to the user defined data area */
} dupnp_cp_dvcmgr_device;

/**
 * Initializes a dupnp_cp_dvcmgr_device data structure.
 * @param[out] x  pointer to the dupnp_cp_dvcmgr_device data structure.
 */
extern void dupnp_cp_dvcmgr_device_init(dupnp_cp_dvcmgr_device* x);

/**
 * Frees the region used by <em>x</em> dupnp_cp_dvcmgr_device structure.
 * @param[in] x pointer to dupnp_cp_dvcmgr_device structure.
 */
extern void dupnp_cp_dvcmgr_device_free(dupnp_cp_dvcmgr_device* x);

/**
 * Sets a location URL string in <em>x</em>.
 * @param[in] x pointer to the dupnp_cp_dvcmgr_device structure.
 * @param[in] location string of the location URL.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_cp_dvcmgr_device_set_location(dupnp_cp_dvcmgr_device* x, const du_uchar* location);

/**
 * Sets a UDN(Unique Device Name) string in <em>x</em>.
 * @param[in] x pointer to the dupnp_cp_dvcmgr_device structure.
 * @param[in] udn string of the UDN(Unique Device Name).
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_cp_dvcmgr_device_set_udn(dupnp_cp_dvcmgr_device* x, const du_uchar* udn);

/**
 * Sets a device type string in <em>x</em>.
 * @param[in] x pointer to the dupnp_cp_dvcmgr_device structure.
 * @param[in] device_type string of the device type.
 * @return  true if the function succeeds.
 *          false if the function fails.
 */
extern du_bool dupnp_cp_dvcmgr_device_set_device_type(dupnp_cp_dvcmgr_device* x, const du_uchar* device_type);

/**
 * Sets a expiration time of device advertisement in <em>x</em>.
 * @param[in] x pointer to the dupnp_cp_dvcmgr_device structure.
 * @param[in] expiration expiration time of device advertisement in second.
 * @remark expiration is the time given by the number of seconds since
 *  January 1, 1970, 00:00:00.
 */
extern void dupnp_cp_dvcmgr_device_set_expiration(dupnp_cp_dvcmgr_device* x, const du_time* expiration);

/**
 * Sets a IP address information in <em>x</em>.
 * @param[in] x pointer to the dupnp_cp_dvcmgr_device structure.
 * @param[in] ip pointer to the du_ip data.
 */
extern void dupnp_cp_dvcmgr_device_set_ip(dupnp_cp_dvcmgr_device* x, const du_ip* ip);

extern du_bool dupnp_cp_dvcmgr_device_clone(const dupnp_cp_dvcmgr_device* x, dupnp_cp_dvcmgr_device* xx);

#ifdef __cplusplus
}
#endif

#endif

