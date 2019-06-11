/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_dvcdsc_info.h
 *  @brief The dupnp_dvcdsc_info.h defines dupnp_dvcdsc_info data structure.
 *  @see dupnp_dvc.h
 */

#ifndef DUPNP_DVCDSC_INFO_H
#define DUPNP_DVCDSC_INFO_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
 * dupnp_dvcdsc_info structure contains the path and location information.
 *  @see dupnp_dvc.h
 */
typedef struct dupnp_dvcdsc_info {
    du_uchar* path;     //!< path of device description XML.
    du_uchar* location; //!< location URL path.
} dupnp_dvcdsc_info;

extern void dupnp_dvcdsc_info_init(dupnp_dvcdsc_info* x);

extern void dupnp_dvcdsc_info_free(dupnp_dvcdsc_info* x);

extern du_bool dupnp_dvcdsc_info_set_path(dupnp_dvcdsc_info* x, const du_uchar* path);

extern du_bool dupnp_dvcdsc_info_set_location(dupnp_dvcdsc_info* x, const du_uchar* location);

#ifdef __cplusplus
}
#endif

#endif
