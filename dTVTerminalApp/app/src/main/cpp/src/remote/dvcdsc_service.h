/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#ifndef DVCDSC_SERVICE_H
#define DVCDSC_SERVICE_H

#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dvcdsc_service {
    const du_uchar* service_type;
    const du_uchar* scpd_url;
    const du_uchar* control_url;
    const du_uchar* event_sub_url;
    du_bool dps_is_v2;
    du_uchar* _buffer;
} dvcdsc_service;

extern void dvcdsc_service_init(dvcdsc_service* service);

extern du_bool dvcdsc_service_set(dvcdsc_service* service, const du_uchar* service_type, const du_uchar* scpd_url, const du_uchar* control_url, const du_uchar* event_sub_url);

extern du_bool dvcdsc_service_set_dps_v2(dvcdsc_service* service, du_bool v);

extern du_bool dvcdsc_service_is_empty(dvcdsc_service* service);

extern void dvcdsc_service_free(dvcdsc_service* service);

#ifdef __cplusplus
}
#endif

#endif
