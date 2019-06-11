/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_dvcdsc_device.h
 *  @brief The dupnp_dvcdsc_device.h defines dupnp_dvcdsc_device data structure.
 *  @see dupnp_dvcdsc.h, dupnp_dvc.h
 */

#ifndef DUPNP_DVCDSC_DEVICE_H
#define DUPNP_DVCDSC_DEVICE_H

#include <dupnp_dvcdsc_service_array.h>
#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * dupnp_dvcdsc_device structure contains the device UDN(Unique Device Name), device type,
 * and ServiceList information.
 *  @see dupnp_dvcdsc.h, dupnp_dvc.h
 */
typedef struct dupnp_dvcdsc_device {
    du_uchar* udn;           //!< UDN(Unique Device Name) string.
    du_uchar* device_type;   //!< UPnP Device type.
    du_bool _ipv6_enabled;
    dupnp_dvcdsc_service_array service_array; //!< ServiceList information of the device(array of dupnp_dvcdsc_service structure).
    du_str_array param_array;  //!< parameter name and its value information.
} dupnp_dvcdsc_device;

extern void dupnp_dvcdsc_device_init(dupnp_dvcdsc_device* x);

extern void dupnp_dvcdsc_device_free(dupnp_dvcdsc_device* x);

extern du_bool dupnp_dvcdsc_device_set_udn(dupnp_dvcdsc_device* x, const du_uchar* udn);

extern du_bool dupnp_dvcdsc_device_set_device_type(dupnp_dvcdsc_device* x, const du_uchar* device_type);

#ifdef __cplusplus
}
#endif

#endif
