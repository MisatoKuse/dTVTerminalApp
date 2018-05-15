/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#ifndef DLNA_DMS_INFO_H
#define DLNA_DMS_INFO_H

#include <du_uchar.h>
#include <du_type.h>

#ifdef __cplusplus
extern "C" {
#endif
typedef struct DmsInfo DmsInfo;

extern DmsInfo* dms_info_create(const du_uchar* xml, du_uint32 xml_len, const du_uchar* udn, const du_uchar* device_type, const du_uchar* location);
    
extern void dms_info_free(DmsInfo* info);
extern DmsInfo* dms_info_clone(DmsInfo* info);

#ifdef __cplusplus
}
#endif

#endif
