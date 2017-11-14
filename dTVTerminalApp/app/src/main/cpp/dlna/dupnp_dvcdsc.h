/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_dvcdsc.h
 *  @brief The dupnp_dvcdsc.h defines dupnp_dvcdsc data structure.
 *  \anchor dupnp_dvcdsc_desc1
 *  dupnp_dvcdsc structure contains the information for a root device.
 *  Each device information is stored in dupnp_dvcdsc_device
 *  structure and each service information is stored in
 *  dupnp_dvcdsc_service structure.
 *  The following figure shows a relation of dupnp_dvcdsc, dupnp_dvcdsc_device,
 *  and dupnp_dvcdsc_service of devices.<br><br>
 *  \image html dupnp_dvcdsc1.jpg "Relation of dupnp_dvcdsc, dupnp_dvcdsc_device, and dupnp_dvcdsc_service"
 *
 * @see dupnp_dvc.h
 */

#ifndef DUPNP_DVCDSC_H
#define DUPNP_DVCDSC_H

#include <dupnp_dvcdsc_device_array.h>
#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * dupnp_dvcdsc structure contains the information of device description.
 * @see dupnp_dvc.h
 */
typedef struct dupnp_dvcdsc {
    du_uchar* location; //!< location URL path.
    du_uchar* path; //!< The path of device description XML.
    du_uchar* url_base_path;   //!< the base URL path string for device description.
    dupnp_dvcdsc_device_array device_array; //!< Array of dupnp_dvcdsc_device structure(dupnp_dvcdsc_device structure contains the device UDN(Unique Device Name), device type, and ServiceList information).
} dupnp_dvcdsc;

extern void dupnp_dvcdsc_init(dupnp_dvcdsc* dvcdsc);

extern void dupnp_dvcdsc_free(dupnp_dvcdsc* dvcdsc);

extern du_bool dupnp_dvcdsc_set_url_base_path(dupnp_dvcdsc* dvcdsc, const du_uchar* url_base_path);

extern du_bool dupnp_dvcdsc_set_location(dupnp_dvcdsc* dvcdsc, const du_uchar* location);

extern du_bool dupnp_dvcdsc_set_path(dupnp_dvcdsc* dvcdsc, const du_uchar* path);

extern du_bool dupnp_dvcdsc_parse(dupnp_dvcdsc* dvcdsc, const du_uchar* location, const du_uchar* path, du_str_array* translate_param_array);

extern du_bool dupnp_dvcdsc_set_translate_param(const du_uchar* ip_str, du_str_array* translate_param_array);

extern const du_uchar* dupnp_dvcdsc_get_hostname(void);

#ifdef __cplusplus
}
#endif

#endif
