/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

/** @file dupnp_dvcdsc_service.h
 *  @brief The dupnp_dvcdsc_service.h defines dupnp_dvcdsc_service data structure.
 *  @see dupnp_dvcdsc.h, dupnp_dvc.h
 */

#ifndef DUPNP_DVCDSC_SERVICE_H
#define DUPNP_DVCDSC_SERVICE_H

#include <du_type.h>
#include <du_uchar_array.h>
#include <du_str_array.h>

#ifdef __cplusplus
extern "C" {
#endif

/**
  * Enumeration of service types.
  */
typedef enum dupnp_dvcdsc_service_type {
   DUPNP_DVCDSC_SERVICE_TYPE_UNKNOWN, //!< Never used.
   DUPNP_DVCDSC_SERVICE_TYPE_HTTP, //!< HTTP service.
   DUPNP_DVCDSC_SERVICE_TYPE_UPNP //!< UPnP service.
} dupnp_dvcdsc_service_type;

/**
* This structure defines the URL for service when the type of dupnp_dvcdsc_service_type is
*  DUPNP_DVCDSC_SERVICE_TYPE_HTTP.
*/
typedef struct dupnp_dvcdsc_service_http {
    du_uchar* url_path;  //!< URL path part.
} dupnp_dvcdsc_service_http;

/**
* This structure defines the URLs for service when the type of dupnp_dvcdsc_service_type is
*  DUPNP_DVCDSC_SERVICE_TYPE_UPNP.
*/
typedef struct dupnp_dvcdsc_service_upnp {
    du_uchar* desc_path;         //!< File path for service description.
    du_uchar* control_url_path;  //!< URL for control.
    du_uchar* event_sub_url_path; //!< URL for eventing.
    du_uchar* scpd_url_path;     //!< URL for service description.
} dupnp_dvcdsc_service_upnp;

/**
 * dupnp_dvcdsc_service structure contains the service type, serviceId etc.
 * given by ServiceList.
 *  @see dupnp_dvcdsc.h, dupnp_dvc.h
 */
typedef struct dupnp_dvcdsc_service {
    du_uchar* service_type;  //!< UPnP service type.
    du_uchar* service_id;    //!< Service Identifier(must be unique within device description).

    du_str_array param_array;  //!< parameter name and its value information.

    dupnp_dvcdsc_service_type type; //!< Service type.

    /**
     * This structure defines the URL(s) for service.
     * If the type of dupnp_dvcdsc_service_type is
     *  DUPNP_DVCDSC_SERVICE_TYPE_HTTP the URL data is given by http structure.
     * Otherwise (services that satisfies the UPnP Device Architecture specifications)
     *  available URL data is given by upnp structure.
     */
    union {
        dupnp_dvcdsc_service_http http;
        dupnp_dvcdsc_service_upnp upnp;
    };

    du_uchar_array _buf;
    du_uchar* _buf2;
} dupnp_dvcdsc_service;

extern du_bool dupnp_dvcdsc_service_init(dupnp_dvcdsc_service* x);

extern void dupnp_dvcdsc_service_free(dupnp_dvcdsc_service* x);

extern du_bool dupnp_dvcdsc_service_set_service_type(dupnp_dvcdsc_service* x, const du_uchar* service_type);

extern du_bool dupnp_dvcdsc_service_set_service_id(dupnp_dvcdsc_service* x, const du_uchar* service_id);

extern du_bool dupnp_dvcdsc_service_set_type(dupnp_dvcdsc_service* x, dupnp_dvcdsc_service_type type);

extern du_bool dupnp_dvcdsc_service_set_http_url_path(dupnp_dvcdsc_service* x, const du_uchar* url_path);

extern du_bool dupnp_dvcdsc_service_set_upnp_control_url_path(dupnp_dvcdsc_service* x, const du_uchar* control_url_path);

extern du_bool dupnp_dvcdsc_service_set_upnp_event_sub_url_path(dupnp_dvcdsc_service* x, const du_uchar* event_sub_url_path);

extern du_bool dupnp_dvcdsc_service_set_upnp_scpd_url_path(dupnp_dvcdsc_service* x, const du_uchar* scpd_url_path);

extern du_bool dupnp_dvcdsc_service_set_desc_path(dupnp_dvcdsc_service* x, const du_uchar* path);

extern du_bool dupnp_dvcdsc_service_pack(dupnp_dvcdsc_service* x);

#ifdef __cplusplus
}
#endif

#endif
