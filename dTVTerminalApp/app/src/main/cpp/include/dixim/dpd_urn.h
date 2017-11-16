/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dpd_urn.h
 *  @brief The dpd_urn interface provides methods for getting a
 *  specific URN for Printer Device.
 */

#ifndef DPD_URN_H
#define DPD_URN_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the Printer Device.
 * This value is "urn:schemas-upnp-org:device:Printer:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:device:Printer:v" string.
 */
extern const du_uchar* dpd_urn_p(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the PrintEnhanced.
 * This value is "urn:schemas-upnp-org:service:PrintEnhanced:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:service:PrintEnhanced:v" string.
 */
extern const du_uchar* dpd_urn_pe(du_uint32 v);

#ifdef __cplusplus
}
#endif

#endif
