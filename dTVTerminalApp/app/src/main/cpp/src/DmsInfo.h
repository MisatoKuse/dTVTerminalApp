/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DMS_INFO_H
#define DMS_INFO_H

#include "du_uchar.h"
// デバイスディスクリプションの要素
#define DMS_DEVICE_DESCRIPTION ("device_description")
#define DMS_ROOT_ELEMENT ("root")
#define DMS_URLBASE_ELEMENT ("URLBase")
#define DMS_DEVICE_ELEMENT ("device")
#define DMS_DEVICE_LIST_ELEMENT ("deviceList")
#define DMS_UUID_ELEMENT ("UDN")
#define DMS_DEVICE_TYPE ("deviceType")
#define DMS_SERVICE_ELEMENT ("service")
#define DMS_SERVICE_TYPE ("serviceType")
#define DMS_SERVICE_LIST ("serviceList")
#define DMS_CONTROL_URL ("controlURL")
#define DMS_EVENTSUB_URL ("eventSubURL")
#define DMS_FRIENDLY_NAME_ELEMENT ("friendlyName")

//STB2号機限定情報項目の追加
#define DMS_MODEL_NAME ("modelName")
#define DMS_MANUFACTURER ("manufacturer")

//STB2号機のモデル名
#define DMS_MODE_NAME_STB2ND ("TT01")
//STB2号機の製造元名
#define DMS_MANUFACTURER_STB2ND ("HUAWEI TECHNOLOGIES CO.,LTD")


#ifdef __cplusplus
extern "C" {
#endif

/**
 * CDS情報： dms_info の内部情報
 */
typedef struct cds_info {
    du_uchar* control_url;
    du_uchar* event_sub_url;
} cds_info;

/**
 * デバイスマネージャーが管理する DMS情報
 */
typedef struct dms_info {
    du_uchar* udn;              // unique device name.
    du_uchar* friendly_name;    // friendly name of device.
    cds_info cds;               // information of content directory service.

    du_uchar* modelName;        //モデル名
    du_uchar* manufacture;      //製造元名
} dms_info;

extern dms_info* createDmsInfoXmlDoc(const du_uchar* xml, du_uint32 xml_len, const du_uchar* udn, const du_uchar* device_type, const du_uchar* location);
extern void freeDmsInfoXmlDoc(dms_info* info);
//extern dms_info* dms_info_clone(dms_info* info);

#ifdef __cplusplus
}
#endif

#endif
