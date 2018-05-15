/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#ifndef DLNA_DVCDSC_DEVICE_H
#define DLNA_DVCDSC_DEVICE_H

#include <du_uchar_array.h>
#include "dvcdsc_icon.h"
#include "dvcdsc_service.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct dvcdsc_device {
    const du_uchar* device_type;
    const du_uchar* friendly_name;
    const du_uchar* udn;
    const du_uchar* dlnadoc;
    const du_uchar* diximcap;
    const du_uchar* rs_regi_socket_host;
    du_uint16 rs_regi_socket_port;
    dvcdsc_icon icon;
    dvcdsc_service cds;
    dvcdsc_service x_dps;

    du_uchar_array _tmp_device_type;
    du_uchar_array _tmp_friendly_name;
    du_uchar_array _tmp_udn;
    du_uchar_array _tmp_dlnadoc;
    du_uchar_array _tmp_diximcap;
    du_uchar_array _tmp_rs_regi_socket;
    du_uchar* _buffer;
} dvcdsc_device;

extern void dvcdsc_device_init(dvcdsc_device* device);

extern du_bool dvcdsc_device_set_device_type(dvcdsc_device* device, const du_uchar* device_type);

extern du_bool dvcdsc_device_set_friendly_name(dvcdsc_device* device, const du_uchar* friendly_name);

extern du_bool dvcdsc_device_set_udn(dvcdsc_device* device, const du_uchar* udn);

extern du_bool dvcdsc_device_set_dlnadoc(dvcdsc_device* device, const du_uchar* dlnadoc);

extern du_bool dvcdsc_device_set_diximcap(dvcdsc_device* device, const du_uchar* diximcap);

extern du_bool dvcdsc_device_set_rs_regi_socket(dvcdsc_device* device, const du_uchar* rs_regi_socket);

extern du_bool dvcdsc_device_pack(dvcdsc_device* device);

extern void dvcdsc_device_free(dvcdsc_device* device);

#ifdef __cplusplus
}
#endif

#endif
