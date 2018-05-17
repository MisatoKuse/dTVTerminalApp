/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#include <secure_io_global.h>
#include <hwif_aux.h>
#include <jni.h>
#include "DlnaBase.h"
#include "DlnaMacro.h"
#include "DlnaDefine.h"

#include "drag_cp.h"
#include "Browse/soap.h"
#include "Browse/browse.h"
#include "Browse/dms_info.h"
#include "DiRAG/dvcdsc_device.h"
#include "DiRAG/dvcdsc_device_array.h"
#include "DiRAG/dvcdsc_parser.h"

#define NETWORK_BITRATE 50 * 1024 * 1024 * 8
#define DISPLAY_WIDTH 1920
#define DISPLAY_HEIGHT 1080
#define THUMBNAIL_WIDTH 160
#define THUMBNAIL_HEIGHT 120

//STB2号機のモデル名
#define DMS_MODE_NAME_STB2ND ("TT01")
//STB2号機の製造元名
#define DMS_MANUFACTURER_STB2ND ("HUAWEI TECHNOLOGIES CO.,LTD")

std::function<void(const char* friendlyName, const char* udn, const char* location, const char* controlUrl, const char* eventSubscriptionUrl)> DlnaBase::DmsFoundCallback = nullptr;
std::function<void(const char* udn)> DlnaBase::DmsLeaveCallback = nullptr;

//#pragma mark - not instance method

du_bool get_ra_dvcdsc_device(dupnp_cp_dvcmgr_dvcdsc* dvcdsc, dupnp_cp_dvcmgr_device* device, dvcdsc_device** dd);

du_bool get_ra_dvcdsc_device(dupnp_cp_dvcmgr_dvcdsc *dvcdsc, dupnp_cp_dvcmgr_device *device,
                             dvcdsc_device **dd) {
    dvcdsc_device_array dd_array;

    dvcdsc_device* dd_;
    du_uint32 len;
    du_uint32 pos;

    dvcdsc_device_array_init(&dd_array);

    if (!dvcdsc_parser_parse(&dd_array, dvcdsc->xml, dvcdsc->xml_len, dvcdsc->location)) goto error;

    len = dvcdsc_device_array_length(&dd_array);
    pos = dvcdsc_device_array_find_by_udn_and_device_type(&dd_array, device->udn, device->device_type);
    if (pos == len) goto error;
    dd_ = (dvcdsc_device*)dvcdsc_device_array_get_pos(&dd_array, pos);
    if (du_str_len(dd_->diximcap) == du_str_find(dd_->diximcap, DU_UCHAR_CONST("dtcp-plus:"))) goto error;
    if (!(*dd = (dvcdsc_device*)du_alloc(sizeof(dvcdsc_device)))) goto error;
    du_byte_copy((du_uint8*)*dd, sizeof(dvcdsc_device), (du_uint8*)dd_);

    dvcdsc_device_array_remove(&dd_array, pos);
    dvcdsc_device_array_free_object(&dd_array);
    return 1;

    error:
    dvcdsc_device_array_free_object(&dd_array);
    return 0;
}

du_bool allowjoinHandler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_device* device, dupnp_cp_dvcmgr_dvcdsc* dvcdsc, void* arg) {
    dvcdsc_device* dd = 0;
    // DMSがjoinした時にコールされてパースをする
    if (!get_ra_dvcdsc_device(dvcdsc, device, &dd)) return 0;
    device->user_data = (void*)dd;
    return 1;
}
    /**
     * 指定されたDMS情報が、STB2号機であることの判定.
     *
     * @param dmsInfo DMS情報
     * @return STB2号機だった場合はtrue
     */
bool isSTB2nd(dvcdsc_device *info) {
    //モデル名が"TT01"かを見る
    if(0 != strcmp((char*)info->model_name, DMS_MODE_NAME_STB2ND)) {
        //TT01ではなかったので、falseで帰る
        return false;
    }

    //製造元が"HUAWEI TECHNOLOGIES CO.,LTD"かを見る（Analyzeがifから3項演算子への変更を推奨するが、メンテナンス性が低下するので行わない）
    if(0 != strcmp((char*)info->manufacturer, DMS_MANUFACTURER_STB2ND)) {
        //HUAWEI・・・ではなかったので、falseで帰る
        return false;
    }

    //STB2号機である事の確認ができたので、trueで帰る
    return true;
}
void joinHandler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_device* device, dupnp_cp_dvcmgr_dvcdsc* dvcdsc, void* arg) {
    dvcdsc_device* info = static_cast<dvcdsc_device*>(device->user_data);
    LOG_WITH("info friendly_name = %s, udn = %s, location = %s", info->friendly_name, info->udn, device->location);
    LOG_WITH("controlUrl = %s, eventUrl= %s", info->cds.control_url, info->cds.event_sub_url);
    if (!isSTB2nd(info)) {
        return;
    }
    if (DlnaBase::DmsFoundCallback != nullptr) {
        DlnaBase::DmsFoundCallback((char*)info->friendly_name, (char*)info->udn, (char*)device->location, (char*)info->cds.control_url, (char*)info->cds.event_sub_url);
    }
}

bool leaveHandler(dupnp_cp_dvcmgr* x, dupnp_cp_dvcmgr_device* device, void* arg) {
    dvcdsc_device* info = static_cast<dvcdsc_device*>(device->user_data);
    LOG_WITH("info->friendly_name = %s, device->udn = %s", info->friendly_name, device->udn);
    if (DlnaBase::DmsLeaveCallback != nullptr) {
        DlnaBase::DmsLeaveCallback((char*)info->udn);
    }
    dvcdsc_device_free(info);
    return true;
}

//#pragma mark - instance method
DlnaBase::DlnaBase() {
    DlnaBase::DmsFoundCallback = nullptr;
    DlnaBase::DmsLeaveCallback = nullptr;
}

DlnaBase::~DlnaBase() {
    DlnaBase::DmsFoundCallback = nullptr;
    DlnaBase::DmsLeaveCallback = nullptr;
}

//#pragma mark - init method
bool DlnaBase::initDmp(DMP *dmp) {
    secure_io_global_create();

    LOG_WITH_PARAM(">>>");
    bool result = true;
    do {
        result = false; // inProgress
        BREAK_IF(!dupnp_init(&dmp->upnpInstance, 0, 0));
        //cp init start ==================================================
        BREAK_IF(!dupnp_enable_netif_monitor(&dmp->upnpInstance, 1));
        BREAK_IF(!dupnp_cp_enable_ssdp_listener(&dmp->upnpInstance, 1));
        BREAK_IF(!dupnp_cp_enable_ssdp_search(&dmp->upnpInstance, 1));
        BREAK_IF(!dupnp_cp_enable_http_server(&dmp->upnpInstance, 1));

        BREAK_IF(!dupnp_cp_dvcmgr_init(&dmp->deviceManager, &dmp->upnpInstance));
        BREAK_IF(!dupnp_cp_evtmgr_init(&dmp->eventManager, &dmp->upnpInstance));

        BREAK_IF(!dupnp_cp_dvcmgr_add_device_type(&dmp->deviceManager, dav_urn_msd(1)));

        // 新しいデバイスがネットワークにジョインする直前のコールバック
        // set a callback function which is called before the device information are stored in the device manager when new devices join to the network.
        dupnp_cp_dvcmgr_set_allow_join_handler(&dmp->deviceManager, allowjoinHandler, &dmp);

        // 新しいデバイスがネットワークにジョインする直後のコールバック
        dupnp_cp_dvcmgr_set_join_handler(&dmp->deviceManager, joinHandler, &dmp);

        // デバイスがネットワークから抜けた時のコールバック
        dupnp_cp_dvcmgr_set_leave_handler(&dmp->deviceManager, leaveHandler, 0);

        dupnp_cp_dvcmgr_set_user_agent(&dmp->deviceManager, DU_UCHAR_CONST("UPnP/1.0 DLNADOC/1.50 DiXiM-SimpleDMP/1.0"));

        BREAK_IF(!soap_init(&dmp->soapInfo, DU_UCHAR_CONST("DLNADOC/1.50")));
        browse_init(&dmp->browseInfo, 10);

        ddtcp_ret ret = ddtcp_create_dtcp(&dmp->dtcp);
        BREAK_IF(DDTCP_FAILED(ret));

        result = true;
    } while (false);
    LOG_WITH_BOOL_PARAM(result, " <<<");
    return result;
}

bool DlnaBase::initDavCapability(DMP *dmp, const char* path) {
    bool result = false;
    do {
        BREAK_IF(!dav_capability_init(&dmp->davCapability));
        BREAK_IF(!dav_capability_set_capability_file(&dmp->davCapability, DU_UCHAR_CONST(path)));
        dav_capability_set_max_bitrate(&dmp->davCapability, NETWORK_BITRATE);
        dav_capability_set_resolution(&dmp->davCapability, DISPLAY_WIDTH, DISPLAY_HEIGHT);
        dav_capability_set_resolution_for_thumbnail(&dmp->davCapability, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
        result = true;
    } while (false);
    LOG_WITH_BOOL_PARAM(result, "");
    return result;
}

bool DlnaBase::initDtcp(DMP *dmp, const char* path) {
    du_uchar_array ua;
    du_uchar_array_init(&ua);
    bool result = false;
    do {
        if (DDTCP_FAILED(ddtcp_create_dtcp(&dmp->dtcp))){
            break;
        }
        BREAK_IF(!du_uchar_array_copys(&ua, DU_UCHAR(path)));
        BREAK_IF(!du_uchar_array_cat0(&ua));
        BREAK_IF(!du_uchar_array_cat0(&ua));
        if (!du_str_clone(du_uchar_array_get(&ua), &dmp->private_data_home)) {
            du_log_mark_w(0);
            return 0;
        }
        LOG_WITH("dmp->private_data_home = %s", dmp->private_data_home);
        result = true;
    } while(false);
    LOG_WITH_BOOL(result, "");
    return result;
}

bool DlnaBase::initDirag(const char* path) {
    bool result = false;
    du_uchar_array ua;
    du_uchar_array_init(&ua);
    do {
        BREAK_IF(!du_uchar_array_copys(&ua, DU_UCHAR(path)));
        BREAK_IF(!du_uchar_array_cat0(&ua));
        BREAK_IF(!drag_cp_initialize(du_uchar_array_get(&ua)));
        result = true;
    } while (false);
    LOG_WITH_BOOL_PARAM(result, "");
    du_uchar_array_free(&ua);

    return result;
}

#pragma mark - run&stop method
bool DlnaBase::startDmp(DMP *dmp) {
    LOG_WITH_PARAM(">>>");
    bool result = false;
    //DMP start ==================================================
    //2.サーチ開始
    do {
        result = false;
        LOG_WITH_PARAM("before dupnp_start");
        BREAK_IF(!dupnp_start(&dmp->upnpInstance));
        LOG_WITH_PARAM("before dupnp_cp_evtmgr_start");
        BREAK_IF(!dupnp_cp_evtmgr_start(&dmp->eventManager));
        LOG_WITH_PARAM("before dupnp_cp_dvcmgr_start");
        BREAK_IF(!dupnp_cp_dvcmgr_start(&dmp->deviceManager));
        result = true;
    } while (false);
    LOG_WITH_BOOL_PARAM(result, " <<<");
    //dmp start ==================================================
    return result;
}

bool DlnaBase::startDtcp(DMP *dmp, JavaVM *vm, jobject object, jmethodID mid) {
    LOG_WITH_PARAM(">>>");
    bool result = false;
    do {
        void *private_data;
        dixim_hwif_private_data_io dstPrivateDataIo;
        dstPrivateDataIo.private_data_home = dmp->private_data_home;
        dstPrivateDataIo.vm = vm;
        dstPrivateDataIo.obj = object;
        dstPrivateDataIo.mac_address_method_id = mid;
        private_data = &dstPrivateDataIo;
        result = false;
        LOG_WITH(" before ddtcp_set_additional_param");
        if (DDTCP_FAILED(ddtcp_set_additional_param(dmp->dtcp, DDTCP_ADDITINAL_PARAM_TYPE_PRIVATE_DATA_IO, private_data))) {
            du_log_mark_w(0);
            break;
        }
        LOG_WITH(" after ddtcp_set_additional_param");
        ddtcp_ret ret = ddtcp_startup(dmp->dtcp);

        if (DDTCP_FAILED(ret)) {
            du_log_mark_w(0);
            break;
        }
        result = true;
    } while (false);

    LOG_WITH_BOOL_PARAM(result, " <<<");
    return result;
}

void DlnaBase::stopDmp(DMP *dmp) {
    dupnp_cp_evtmgr_stop(&dmp->eventManager);
    dupnp_stop(&dmp->upnpInstance);
    dupnp_cp_dvcmgr_stop(&dmp->deviceManager);
}

//DiRAGのfreeも持ってくる
void DlnaBase::freeDmp(DMP *dmp) {
    dav_capability_free(&dmp->davCapability);//未設定の場合クラッシュ
    dupnp_cp_evtmgr_free(&dmp->eventManager);
    dupnp_cp_dvcmgr_free(&dmp->deviceManager);
    browse_free(&dmp->browseInfo);
    soap_free(&dmp->soapInfo);
    dupnp_free(&dmp->upnpInstance);
}