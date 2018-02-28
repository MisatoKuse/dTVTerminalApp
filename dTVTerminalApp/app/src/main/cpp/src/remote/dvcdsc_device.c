/*
 * Copyright (c) 2007 DigiOn, Inc. All rights reserved.
 */

#include <dupnp_urn.h>
#include <du_byte.h>
#include <du_alloc.h>
#include <du_str.h>
#include "dvcdsc_device.h"

void dvcdsc_device_init(dvcdsc_device* device) {
    du_byte_zero((du_uint8*)device, sizeof(dvcdsc_device));
    du_uchar_array_init(&device->_tmp_device_type);
    du_uchar_array_init(&device->_tmp_friendly_name);
    du_uchar_array_init(&device->_tmp_udn);
    du_uchar_array_init(&device->_tmp_dlnadoc);
    du_uchar_array_init(&device->_tmp_diximcap);
    du_uchar_array_init(&device->_tmp_rs_regi_socket);
    dvcdsc_icon_init(&device->icon);
    dvcdsc_service_init(&device->cds);
    dvcdsc_service_init(&device->x_dps);
}

void dvcdsc_device_free(dvcdsc_device* device) {
    if (!device) return;
    du_uchar_array_free(&device->_tmp_device_type);
    du_uchar_array_free(&device->_tmp_friendly_name);
    du_uchar_array_free(&device->_tmp_udn);
    du_uchar_array_free(&device->_tmp_dlnadoc);
    du_uchar_array_free(&device->_tmp_diximcap);
    du_uchar_array_free(&device->_tmp_rs_regi_socket);
    du_alloc_free(device->_buffer);
    dvcdsc_icon_free(&device->icon);
    dvcdsc_service_free(&device->cds);
    dvcdsc_service_free(&device->x_dps);
    dvcdsc_device_init(device);
}

du_bool dvcdsc_device_set_device_type(dvcdsc_device* device, const du_uchar* device_type) {
    if (!du_uchar_array_copys0(&device->_tmp_device_type, device_type)) return 0;
    return 1;
}

du_bool dvcdsc_device_set_friendly_name(dvcdsc_device* device, const du_uchar* friendly_name) {
    if (!du_uchar_array_copys0(&device->_tmp_friendly_name, friendly_name)) return 0;
    return 1;
}

du_bool dvcdsc_device_set_udn(dvcdsc_device* device, const du_uchar* udn) {
    if (!du_uchar_array_copys0(&device->_tmp_udn, udn)) return 0;
    return 1;
}

du_bool dvcdsc_device_set_dlnadoc(dvcdsc_device* device, const du_uchar* dlnadoc) {
    if (!du_uchar_array_copys0(&device->_tmp_dlnadoc, dlnadoc)) return 0;
    return 1;
}

du_bool dvcdsc_device_set_diximcap(dvcdsc_device* device, const du_uchar* diximcap) {
    if (!du_uchar_array_copys0(&device->_tmp_diximcap, diximcap)) return 0;
    return 1;
}

du_bool dvcdsc_device_set_rs_regi_socket(dvcdsc_device* device, const du_uchar* rs_regi_socket) {
    if (!du_uchar_array_copys0(&device->_tmp_rs_regi_socket, rs_regi_socket)) return 0;
    return 1;
}

du_bool dvcdsc_device_pack(dvcdsc_device* device) {
    du_uint32 len1;
    du_uint32 len2;
    du_uint32 len3;
    du_uint32 len4;
    du_uint32 len5;
    du_uint32 len6;
    du_uint32 i = 0;

    du_alloc_free(device->_buffer);

    if (!dupnp_urn_is_device(du_uchar_array_get(&device->_tmp_device_type))) return 0;
    len1 = du_uchar_array_length(&device->_tmp_device_type);

    len2 = du_uchar_array_length(&device->_tmp_friendly_name);
    if (!len2) return 0;

    len3 = du_uchar_array_length(&device->_tmp_udn);
    if (!len3) return 0;

    len4 = du_uchar_array_length(&device->_tmp_dlnadoc);
    if (!len4) {
        if (!du_uchar_array_cat0(&device->_tmp_dlnadoc)) return 0;
        len4 = du_uchar_array_length(&device->_tmp_dlnadoc);
    }
    len5 = du_uchar_array_length(&device->_tmp_diximcap);
    if (!len5) {
        if (!du_uchar_array_cat0(&device->_tmp_diximcap)) return 0;
        len5 = du_uchar_array_length(&device->_tmp_diximcap);
    }
    len6 = du_uchar_array_length(&device->_tmp_rs_regi_socket);
    if (!len6) {
        if (!du_uchar_array_cat0(&device->_tmp_rs_regi_socket)) return 0;
        len6 = du_uchar_array_length(&device->_tmp_rs_regi_socket);
    } else {
       len6 =  du_str_chr(du_uchar_array_get(&device->_tmp_rs_regi_socket), ':');
        du_str_scan_uint16(du_uchar_array_get(&device->_tmp_rs_regi_socket) + len6 + 1, &device->rs_regi_socket_port);
        if (!du_uchar_array_insert0(&device->_tmp_rs_regi_socket, len6)) return 0;
        ++len6;
    }

    device->_buffer = (du_uchar*)du_alloc(len1 + len2 + len3 + len4 + len5 + len6);
    if (!device->_buffer) return 0;

    device->device_type = device->_buffer;
    i += du_byte_copy(device->_buffer, len1, du_uchar_array_get(&device->_tmp_device_type));

    device->friendly_name = device->_buffer + i;
    i += du_byte_copy(device->_buffer + i, len2, du_uchar_array_get(&device->_tmp_friendly_name));

    device->udn = device->_buffer + i;
    i += du_byte_copy(device->_buffer + i, len3, du_uchar_array_get(&device->_tmp_udn));

    device->dlnadoc = device->_buffer + i;
    i += du_byte_copy(device->_buffer + i, len4, du_uchar_array_get(&device->_tmp_dlnadoc));

    device->diximcap = device->_buffer + i;
    i += du_byte_copy(device->_buffer + i, len5, du_uchar_array_get(&device->_tmp_diximcap));

    device->rs_regi_socket_host = device->_buffer + i;
    i += du_byte_copy(device->_buffer + i, len6, du_uchar_array_get(&device->_tmp_rs_regi_socket));

    du_uchar_array_free(&device->_tmp_device_type);
    du_uchar_array_free(&device->_tmp_friendly_name);
    du_uchar_array_free(&device->_tmp_udn);
    du_uchar_array_free(&device->_tmp_dlnadoc);
    du_uchar_array_free(&device->_tmp_diximcap);
    du_uchar_array_free(&device->_tmp_rs_regi_socket);
    return 1;
}
