/*
 * Copyright (c) 2012 DigiOn, Inc. All rights reserved.
 */

/** @file
 *  @brief The dacs_urn interface provides methods for getting a
 *  specific URN for Dtcp Plus Service.
 */

#ifndef DDPS_URN_H
#define DDPS_URN_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Returns a URN string for the serviceType element value required for a
 * XML device description of the DtcpPlus.
 * This value is "urn:schemas-digion-com:service:X_DtcpPlus:v".
 * @param[in] v version number of the device.
 * @return  "urn:schemas-digion-com:service:X_DtcpPlus:v" string.
 */
#define ddps_urn_dps(v) (const du_uchar*)((v == 1) ? ("urn:schemas-digion-com:service:X_DtcpPlus:1") : ("urn:schemas-dlpa-jp:service:X_DtcpPlus:1"))

#ifdef __cplusplus
}
#endif

#endif
