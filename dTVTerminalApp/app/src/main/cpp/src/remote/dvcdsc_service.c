/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include <du_str.h>
#include <du_byte.h>
#include <du_alloc.h>
#include "dvcdsc_service.h"

void dvcdsc_service_init(dvcdsc_service* service) {
    du_byte_zero((du_uint8*)service, sizeof(dvcdsc_service));
}

void dvcdsc_service_free(dvcdsc_service* service) {
    if (!service) return;
    du_alloc_free(service->_buffer);
    dvcdsc_service_init(service);
}

du_bool dvcdsc_service_set(dvcdsc_service* service, const du_uchar* service_type, const du_uchar* scpd_url, const du_uchar* control_url, const du_uchar* event_sub_url) {
    du_uint32 len1;
    du_uint32 len2;
    du_uint32 len3;
    du_uint32 len4;
    du_uint32 i = 0;

    du_alloc_free(service->_buffer);
    len1 = du_str_len(service_type);
    if (!len1) return 0;
    len2 = du_str_len(scpd_url);
    if (!len2) return 0;
    len3 = du_str_len(control_url);
    if (!len3) return 0;
    len4 = du_str_len(event_sub_url);
    if (!len4) return 0;

    service->_buffer = (du_uchar*)du_alloc(len1 + len2 + len3 + len4 + 4);
    if (!service->_buffer) return 0;

    service->service_type = service->_buffer;
    i = du_byte_copy(service->_buffer, len1, service_type);
    service->_buffer[i++] = 0;

    service->scpd_url = service->_buffer + i;
    i += du_byte_copy(service->_buffer + i, len2, scpd_url);
    service->_buffer[i++] = 0;

    service->control_url = service->_buffer + i;
    i += du_byte_copy(service->_buffer + i, len3, control_url);
    service->_buffer[i++] = 0;

    service->event_sub_url = service->_buffer + i;
    i += du_byte_copy(service->_buffer + i, len4, event_sub_url);
    service->_buffer[i++] = 0;
    return 1;
}

du_bool dvcdsc_service_set_dps_v2(dvcdsc_service* service, du_bool v) {
    service->dps_is_v2 = v;
    return 1;
}

du_bool dvcdsc_service_is_empty(dvcdsc_service* service) {
    return !service->_buffer;
}
