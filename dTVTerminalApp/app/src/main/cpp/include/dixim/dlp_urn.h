/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dlp_urn.h
 *  @brief The dlp_urn interface provides methods for getting a
 *  specific URN for Low Power Device.
 */

#ifndef DPD_URN_H
#define DPD_URN_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the LowPowerDevice.
 * This value is "urn:schemas-upnp-org:service:LowPowerDevice:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:service:LowPowerDevice:v" string.
 */
extern const du_uchar* dlp_urn_lpd(du_uint32 v);

/**
 * Returns a URN string for the deviceType element value required for a
 * XML device description of the LowPowerProxy.
 * This value is "urn:schemas-upnp-org:service:LowPowerProxy:v".
 * @param[in] v version number of the device. It must be 1.
 * @return  "urn:schemas-upnp-org:service:LowPowerProxy:v" string.
 */
extern const du_uchar* dlp_urn_lpp(du_uint32 v);

#ifdef __cplusplus
}
#endif

#endif
