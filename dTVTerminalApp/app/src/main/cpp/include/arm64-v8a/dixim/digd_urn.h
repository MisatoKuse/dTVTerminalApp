/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file digd_urn.h
 *  @brief The digd_urn interface provides methods for getting a
 *  specific URN for UPnP IGD.
 */

#ifndef DIGD_URN_H
#define DIGD_URN_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the InternetGatewayDevice.
 * This value is "urn:schemas-upnp-org:device:InternetGatewayDevice:v".
 * @param[in] v version number of the device. It must be between 1 and 2.
 * @return  "urn:schemas-upnp-org:device:InternetGatewayDevice:v" string.
 */
extern const du_uchar* digd_urn_igd(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the LANDevice.
 * This value is "urn:schemas-upnp-org:device:LANDevice:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:device:LANDevice:v" string.
 */
extern const du_uchar* digd_urn_land(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the WANDevice.
 * This value is "urn:schemas-upnp-org:device:WANDevice:v".
 * @param[in] v version number of the device. It must be between 1 and 2.
 * @return  "urn:schemas-upnp-org:device:WANDevice:v" string.
 */
extern const du_uchar* digd_urn_wand(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the LANDevice.
 * This value is "urn:schemas-upnp-org:device:WANConnectionDevice:v".
 * @param[in] v version number of the device. It must be between 1 and 2.
 * @return  "urn:schemas-upnp-org:device:WANConnectionDevice:v" string.
 */
extern const du_uchar* digd_urn_wancd(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the Layer3Forwarding.
 * This value is "urn:schemas-upnp-org:service:Layer3Forwarding:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:service:Layer3Forwarding:v" string.
 */
extern const du_uchar* digd_urn_l3f(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the WANIPConnection.
 * This value is "urn:schemas-upnp-org:service:WANIPConnection:v".
 * @param[in] v version number of the device. It must be between 1 and 2.
 * @return  "urn:schemas-upnp-org:service:WANIPConnection:v" string.
 */
extern const du_uchar* digd_urn_wanipc(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the LANHostConfigManagement.
 * This value is "urn:schemas-upnp-org:service:LANHostConfigManagement:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:service:LANHostConfigManagement:v" string.
 */
extern const du_uchar* digd_urn_lanhcm(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the WANCableLinkConfig.
 * This value is "urn:schemas-upnp-org:service:WANCableLinkConfig:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:service:WANCableLinkConfig:v" string.
 */
extern const du_uchar* digd_urn_wanclc(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the WANDSLLinkConfig.
 * This value is "urn:schemas-upnp-org:service: WANDSLLinkConfig:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:service: WANDSLLinkConfig:v" string.
 */
extern const du_uchar* digd_urn_wandsllc(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the WANEthernetLinkConfig.
 * This value is "urn:schemas-upnp-org:service:WANEthernetLinkConfig:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:service:WANEthernetLinkConfig:v" string.
 */
extern const du_uchar* digd_urn_wanelc(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the WANPOTSLinkConfig.
 * This value is "urn:schemas-upnp-org:service:WANPOTSLinkConfig:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:service:WANPOTSLinkConfig:v" string.
 */
extern const du_uchar* digd_urn_wanpotslc(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the WANPPPConnection.
 * This value is "urn:schemas-upnp-org:service:WANPPPConnection:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:service:WANPPPConnection:v" string.
 */
extern const du_uchar* digd_urn_wanpppc(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the WANIPv6FirewallControl.
 * This value is "urn:schemas-upnp-org:service:WANIPv6FirewallControl:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:service:WANIPv6FirewallControl:v" string.
 */
extern const du_uchar* digd_urn_wanipv6fc(du_uint32 v);

#ifdef __cplusplus
}
#endif

#endif
