/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#ifndef DLNA_DMP_CONF_H
#define DLNA_DMP_CONF_H

#include <du_uchar_array.h>

#ifdef __cplusplus
extern "C" {
#endif

extern du_bool dmp_conf_get_device_conf_path(const du_uchar* conf_path, du_uchar_array* device_conf_path);

extern du_bool dmp_conf_get_upnp_port(const du_uchar* conf_path, du_uint16* upnp_port, du_uchar_array* tmp_ua);

extern du_bool dmp_conf_get_capability_xml_path(const du_uchar* conf_path, du_uchar_array* capability_xml_path);

extern du_bool dmp_conf_get_download_dir_path(const du_uchar* conf_path, du_uchar_array* download_dir_path, du_uchar_array* tmp_ua);

extern du_bool dmp_conf_get_mime_type_list_path(const du_uchar* conf_path, du_uchar_array* mime_type_list_path);

extern du_bool dmp_conf_get_private_data_home_path(const du_uchar* conf_path, du_uchar_array* private_data_home_path);

extern du_bool dmp_conf_get_dirag_path(const du_uchar* conf_path, du_uchar_array* dirag_path);

#ifdef __cplusplus
}
#endif

#endif

