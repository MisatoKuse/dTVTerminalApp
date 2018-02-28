/*
 * Copyright (c) 2011 DigiOn, Inc. All rights reserved.
 */

#include "dmp_conf.h"
#include <du_file.h>
#include <du_str.h>

static du_bool get_conf_path(const du_uchar* conf_path, const du_uchar* name, du_uchar_array* path) {
    du_uchar_array_copys(path, conf_path);
    du_uchar_array_cato(path, du_file_separator());
    du_uchar_array_cats(path, name);
    if (du_uchar_array_failed(path)) return 0;
    return 1;
}

static du_bool get_port_value(const du_uchar* path, du_uint16* port) {
    du_uchar strnum[DU_STR_FMT_SIZE];

    if (!du_file_get_first_line(path, strnum, sizeof strnum)) return 0;
    if (!du_str_scan_uint16(strnum, port)) return 0;
    return 1;
}

du_bool dmp_conf_get_mime_type_list_path(const du_uchar* conf_path, du_uchar_array* mime_type_list_path) {
    if (!get_conf_path(conf_path, DU_UCHAR_CONST("mime_type_list"), mime_type_list_path)) return 0;
    return 1;
}

du_bool dmp_conf_get_device_conf_path(const du_uchar* conf_path, du_uchar_array* device_conf_path) {
    if (!get_conf_path(conf_path, DU_UCHAR_CONST("device_conf.xml"), device_conf_path)) return 0;
    return 1;
}

du_bool dmp_conf_get_upnp_port(const du_uchar* conf_path, du_uint16* upnp_port, du_uchar_array* tmp_ua) {
    if (!get_conf_path(conf_path, DU_UCHAR_CONST("upnp_port"), tmp_ua)) return 0;
    if (!du_uchar_array_cat0(tmp_ua)) return 0;
    if (!get_port_value(du_uchar_array_get(tmp_ua), upnp_port)) return 0;
    return 1;
}

du_bool dmp_conf_get_download_dir_path(const du_uchar* conf_path, du_uchar_array* download_dir_path, du_uchar_array* tmp_ua) {
    du_uchar str[1024];

    du_uchar_array_truncate(download_dir_path);
    if (!get_conf_path(conf_path, DU_UCHAR_CONST("download_dir"), tmp_ua)) {
        return 0;
    }
    if (!du_uchar_array_cat0(tmp_ua)) {
        return 0;
    }
    if (!du_file_get_first_line(du_uchar_array_get(tmp_ua), str, sizeof str)) {
        return 0;
    }
    if (!du_uchar_array_cats(download_dir_path, str)) {
        return 0;
    }
    return 1;
}

du_bool dmp_conf_get_capability_xml_path(const du_uchar* conf_path, du_uchar_array* capability_xml_path) {
    if (!get_conf_path(conf_path, DU_UCHAR_CONST("capability.xml"), capability_xml_path)) return 0;
    return 1;
}

#ifdef ENABLE_DTCP
du_bool dmp_conf_get_private_data_home_path(const du_uchar* conf_path, du_uchar_array* private_data_home_path) {
    if (!get_conf_path(conf_path, DU_UCHAR_CONST("private_data_home"), private_data_home_path)) return 0;
    return 1;
}
#endif

du_bool dmp_conf_get_dirag_path(const du_uchar* conf_path, du_uchar_array* dirag_path) {
    if (!get_conf_path(conf_path, DU_UCHAR_CONST("dirag/drag_configuration.xml"), dirag_path)) return 0;
    return 1;
}

