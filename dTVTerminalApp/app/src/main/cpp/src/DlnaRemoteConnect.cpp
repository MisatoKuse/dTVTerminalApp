//
//  DlnaRemoteConnect.cpp
//  dTVTerminal
//

#include "DlnaRemoteConnect.h"

#include <ddtcp_sink.h>
#include <ddtcp_plus_sink.h>
#include <ddtcp_util_http.h>
#include <ddtcp_private.h>
#include "drag_cp.h"

#include "DiRAG/dmp_conf.h"
#include "DiRAG/dvcdsc_device.h"
#include "LocalRegistration/local_registration.h"

#include "DlnaMacro.h"

#include <time.h>//TODO:tmp

#ifndef DDTCP_CRYPTO_SHA1_DIGEST_SIZE
    #define DDTCP_CRYPTO_SHA1_DIGEST_SIZE 20
#endif

std::function<void(eDiragConnectStatus status, du_uint32 errorCode)> DlnaRemoteConnect::DiragConnectStatusChangeCallback = nullptr;
std::function<void(bool result, eLocalRegistrationResultType resultType)> DlnaRemoteConnect::LocalRegistrationCallback = nullptr;
std::function<void(ddtcp_ret ddtcpSinkAkeEndRet)> DlnaRemoteConnect::DdtcpSinkAkeEndCallback = nullptr;

typedef struct regist_dms_visitor_context {
    const du_uchar* udn;
    du_uchar* control_url;
    du_uchar* dtcp1_host;
    du_uint16 dtcp1_port;
    du_bool is_v2;
    du_bool found;
    du_bool succeeded;
} regist_dms_visitor_context;

typedef struct ake_handler_info {
    du_sync sync;
    du_mutex mutex;
    du_bool error_occurred;
} ake_handler_info;

void dcp_connect_status_handler(drag_cp_connect_status status, void* arg) {
    eDiragConnectStatus retStatus;
    drag_error_code errorCode = 0;
    switch(status) {
        case DRAG_CP_CONNECT_STATUS_UNKNOWN:
            LOG_WITH("DRAG_CP_CONNECT_STATUS_UNKNOWN");
            retStatus = DiragConnectStatusUnknown;
            break;
        case DRAG_CP_CONNECT_STATUS_READY:
            LOG_WITH(">>> DRAG_CP_CONNECT_STATUS_READY <<<");
            retStatus = DiragConnectStatusReady;
            break;
        case DRAG_CP_CONNECT_STATUS_CONNECTED:
            LOG_WITH("=== DRAG_CP_CONNECT_STATUS_CONNECTED ===");
            retStatus = DiragConnectStatusConnected;
            break;
        case DRAG_CP_CONNECT_STATUS_DETECTED_DISCONNECTION:
            LOG_WITH("DRAG_CP_CONNECT_STATUS_DETECTED_DISCONNECTION");
            retStatus = DiragConnectStatusDetectedDisconnection;
            break;
        case DRAG_CP_CONNECT_STATUS_GAVEUP_RECONNECTION:
            LOG_WITH(">>> DRAG_CP_CONNECT_STATUS_GAVEUP_RECONNECTION <<<");
            retStatus = DiragConnectStatusGaveupReconnection;
            errorCode = drag_cp_get_last_error();
            break;
    }
    if (DlnaRemoteConnect::DiragConnectStatusChangeCallback != nullptr) {
        DlnaRemoteConnect::DiragConnectStatusChangeCallback(retStatus, errorCode);
    }
}

DlnaRemoteConnect::DlnaRemoteConnect() {
    DlnaRemoteConnect::DiragConnectStatusChangeCallback = nullptr;
    DlnaRemoteConnect::LocalRegistrationCallback = nullptr;
    DlnaRemoteConnect::DdtcpSinkAkeEndCallback = nullptr;
}

DlnaRemoteConnect::~DlnaRemoteConnect() {
    DlnaRemoteConnect::DiragConnectStatusChangeCallback = nullptr;
    DlnaRemoteConnect::LocalRegistrationCallback = nullptr;
    DlnaRemoteConnect::DdtcpSinkAkeEndCallback = nullptr;
}

bool DlnaRemoteConnect::restartDirag(DMP *d) {
    bool result = false;
    do {
        drag_cp_lrsys_stop();
        drag_cp_rasys_stop();
        BREAK_IF(!drag_cp_lrsys_start());
        BREAK_IF(!drag_cp_rasys_start(dcp_connect_status_handler, d));
        result = true;
    } while (false);
    LOG_WITH_BOOL_PARAM(result, "");
    return result;
}

bool DlnaRemoteConnect::startDirag(DMP *d) {
    bool result = false;
    do {
        BREAK_IF(!drag_cp_lrsys_start());
        BREAK_IF(!drag_cp_rasys_start(dcp_connect_status_handler, d));
        result = true;
    } while (false);
    LOG_WITH_BOOL_PARAM(result, "");
    return result;
}

bool DlnaRemoteConnect::stopDirag() {
    drag_cp_lrsys_stop(); // LR sub-system
    drag_cp_rasys_stop(); // remote access system
}

void DlnaRemoteConnect::finalizeDirag() {
    drag_cp_lrsys_stop(); // LR sub-system
    drag_cp_rasys_stop(); // remote access system
    drag_cp_finalize();
}

#pragma mark - RA AKE
du_bool ake_handler_info_create(ake_handler_info* info) {
    if (!du_sync_create(&info->sync)) {du_log_mark_w(0); return 0;}
    if (!du_mutex_create(&info->mutex)) {du_log_mark_w(0); goto error;}
    info->error_occurred = 0;
    return 1;
    
error:
    du_sync_free(&info->sync);
    {du_log_mark_w(0); return 0;}
}

void ake_handler_info_free(ake_handler_info* info) {
    du_sync_free(&info->sync);
    du_mutex_free(&info->mutex);
}

ddtcp_ret ake_end_handler(ddtcp_ret status, ddtcp_sink_ake ake, void* arg) {
    ake_handler_info* info = (ake_handler_info*)arg;
    
    if (DDTCP_FAILED(status)) {du_log_mark_w(0); goto error;}
    du_mutex_lock(&info->mutex);
    du_sync_notify(&info->sync);
    du_mutex_unlock(&info->mutex);
    if (DlnaRemoteConnect::DdtcpSinkAkeEndCallback != nullptr) {
        DlnaRemoteConnect::DdtcpSinkAkeEndCallback(status);
    }
    return status;
error:
    LOG_WITH("ake error status=0x%x\n", status);
    info->error_occurred = 1;
    du_mutex_lock(&info->mutex);
    du_sync_notify(&info->sync);
    du_mutex_unlock(&info->mutex);
    if (DlnaRemoteConnect::DdtcpSinkAkeEndCallback != nullptr) {
        DlnaRemoteConnect::DdtcpSinkAkeEndCallback(status);
    }
    return status;
}

du_bool sink_ra_register(DMP *dmp, const du_uchar* dtcp1_host, du_uint16 dtcp1_port) {
    LOG_WITH("dtcp1_host = %s, dtcp1_port = %d", dtcp1_host, dtcp1_port);
    ddtcp_sink_ake ake = 0;
    ake_handler_info hinfo;
    ddtcp_ret ret = DDTCP_RET_SUCCESS;
    
    if (!ake_handler_info_create(&hinfo)) {du_log_mark_w(0); goto error;}
    
    du_mutex_lock(&hinfo.mutex);
    if (DDTCP_FAILED(ret = ddtcp_sink_ra_register(dmp->dtcp, dtcp1_host, dtcp1_port, ake_end_handler, &hinfo, &ake))) {du_log_mark_w(0); goto error;}
    du_sync_wait(&hinfo.sync, &hinfo.mutex);
    du_mutex_unlock(&hinfo.mutex);
    if (hinfo.error_occurred) {du_log_mark_w(0); goto error;}
    
    if (DDTCP_FAILED(ret = ddtcp_sink_close_ake(&ake))) {du_log_mark_w(0); goto error;}
    ake_handler_info_free(&hinfo);
    
    return 1;
    
error:
    ddtcp_sink_close_ake(&ake);
    ake_handler_info_free(&hinfo);
    LOG_WITH("ra register error ret=0x%x\n", ret);
    {du_log_mark_w(0); return 0;}
}

void prepare_lr_register_response_handler(du_uint32 requeseted_id, local_registration_error_info* error_info) {
    if (error_info->type == LOCAL_REGISTRATION_ERROR_TYPE_NONE) {
        LOG_WITH("============================== Finsh PrepareRegistration ==============================");
    } else {
        LOG_WITH("============================== Error PrepareRegistration ==============================");
        LOG_WITH("http_status = %s, type = %d, soap_error_code = %s", error_info->http_status, error_info->type, error_info->soap_error_code);
        LOG_WITH("soap_error_description = %s", error_info->soap_error_description);
    }
}

void lr_register_response_handler(du_uint32 requeseted_id, local_registration_error_info* error_info) {
    bool result = false;
    eLocalRegistrationResultType resultType = LocalRegistrationResultTypeNone;
    if (error_info->type == LOCAL_REGISTRATION_ERROR_TYPE_NONE) {
        LOG_WITH("============================== Finsh Local Registration ==============================");
        result = true;
    } else {
        LOG_WITH("============================== Error Local Registration ==============================");
        LOG_WITH("http_status = %s, type = %d, soap_error_code = %s", error_info->http_status, error_info->type, error_info->soap_error_code);
        LOG_WITH("soap_error_description = %s", error_info->soap_error_description);

        if(du_str_equal(error_info->soap_error_code, DU_UCHAR_CONST("803"))) {
            resultType = LocalRegistrationResultTypeRegistrationOverError;
        } else {
            resultType = LocalRegistrationResultTypeUnknownError;
        }
    }
    if (DlnaRemoteConnect::LocalRegistrationCallback != nullptr) {
        LOG_WITH("before callback");
        DlnaRemoteConnect::LocalRegistrationCallback(result, resultType);
        LOG_WITH("after callback");
    }
}

bool regist_dms_visitor(dupnp_cp_dvcmgr_device* device, void* arg) {
    regist_dms_visitor_context* context = static_cast<regist_dms_visitor_context*>(arg);
    dvcdsc_device* dd = static_cast<dvcdsc_device*>(device->user_data);
    
    if (du_str_diff(context->udn, device->udn)) return 1;
    
    context->found = 1;
    if (!du_str_clone(dd->x_dps.control_url, &context->control_url)) return 0;
    if (!du_str_clone(dd->rs_regi_socket_host, &context->dtcp1_host)) goto error;
    context->dtcp1_port = dd->rs_regi_socket_port;
    context->is_v2 = dd->x_dps.dps_is_v2;
    context->succeeded = 1;
    return true;
    
error:
    du_alloc_free(&context->control_url);
    context->control_url = 0;
    return false;
}

du_bool getDeviceIdHash(DMP *d, du_uchar_array* hash_base64_ecoded) {
    ddtcp_ret ret = DDTCP_RET_SUCCESS;
    du_uint8 device_id_hash[DDTCP_CRYPTO_SHA1_DIGEST_SIZE];
    
    if (DDTCP_FAILED(ret = ddtcp_get_device_id_hash(d->dtcp, device_id_hash))) {du_log_mark_w(0); goto error;}
    if (!du_base64_encode(device_id_hash, DDTCP_CRYPTO_SHA1_DIGEST_SIZE, hash_base64_ecoded)) {du_log_mark_w(0); goto error;}
    
    return 1;
error:
    printf("get device id hash error ret=0x%x\n", ret);
    {du_log_mark_w(0); return 0;}
}

bool DlnaRemoteConnect::requestLocalRegistration(DMP *d, const du_uchar* udn, const du_uchar* registerName) {
    LOG_WITH_PARAM(">>>");
    bool result = false;
    regist_dms_visitor_context context;
    du_uchar_array deviceId;
    du_uchar_array hash;
    du_uint32 id;
    du_uint32 version;
    du_uint8 deviceIdHash[DDTCP_CRYPTO_SHA1_DIGEST_SIZE];
    
    // 登録名の作成はJava側で完了するように変更

    du_byte_zero((du_uint8*)&context, sizeof(context));
    du_byte_zero((du_uint8*)&deviceIdHash, DDTCP_CRYPTO_SHA1_DIGEST_SIZE);
    
    du_uchar_array_init(&deviceId);
    du_uchar_array_init(&hash);
    
    context.udn = udn;
    
    do {
        BREAK_IF(!dupnp_cp_dvcmgr_visit_device_type(&d->deviceManager, dmp_get_dms_type(), regist_dms_visitor, &context));
        BREAK_IF(!context.found);
        BREAK_IF(!context.succeeded);
        version = context.is_v2 ? 2 : 1; // set DLPA version
        //現在対応STB1号機では常に失敗しているがローカルレジストレーションには影響なさそう
        BREAK_IF(!local_registration_prepare_registration(&d->upnpInstance, dmp_get_user_agent(), context.control_url, prepare_lr_register_response_handler, &id, version));

        BREAK_IF(!sink_ra_register(d, context.dtcp1_host, context.dtcp1_port));
        BREAK_IF(!getDeviceIdHash(d, &hash));
        BREAK_IF(!du_uchar_array_cat0(&hash));

        BREAK_IF(!drag_cp_service_init(&deviceId, version)); // シミュレーターではここで失敗するためローカルレジストレーションできない
        BREAK_IF(!local_registration_register(&d->upnpInstance, dmp_get_user_agent(), context.control_url, du_uchar_array_get(&deviceId),
                                              DU_UCHAR_CONST(registerName), du_uchar_array_get(&hash), lr_register_response_handler, &id, version));
        result = true;
    } while (false);
    LOG_WITH_BOOL_PARAM(result, "");
    return result;
}

void DlnaRemoteConnect::connectRemote(const du_uchar* udn){
    bool result = drag_cp_connect_to_dms(DU_UCHAR(udn));
    LOG_WITH_BOOL_PARAM(result, "udn = %s", udn);
}

const char* DlnaRemoteConnect::getRemoteDeviceExpireDate(const du_uchar* udn) {
    LOG_WITH_PARAM(">>>");
    dms_info_array dia;
    dms_info* info;
    du_uint32 i;
    du_uint32 len;

    char date[64] = "";
    dms_info_array_init(&dia);
    if (!drag_cp_get_dms_list(&dia)) goto error;
    
    len = dms_info_array_length(&dia);
    info = dms_info_array_get(&dia);

    for (i = 0; i < len; ++i, ++info) {
        if (du_str_diff(udn, info->udn) == 0) {
            time_t t = info->expire_date;
            struct tm *tm = localtime(&t);
            strftime(date, sizeof(date), "%Y-%m-%d %H:%I:%S", tm);
            LOG_WITH_PARAM("%d. %s ExpireDate:[%s]", i, info->friendly_name, date);
            break;
        }
    }
    
    if (!len) LOG_WITH_PARAM("No Devices.");
    
    LOG_WITH_PARAM("index: %d-%d, total %d dmss\n", 0, len - 1, len);
    dms_info_array_free(&dia);
    
    LOG_WITH_PARAM("<<<");
    return date;
    
error:
    LOG_WITH_PARAM("<<< Error");
    dms_info_array_free(&dia);
    return nullptr;
}

#pragma mark - private method
const du_uchar* DlnaRemoteConnect::dmp_get_dms_type(void) {
    return dav_urn_msd(1);
}

const du_uchar* DlnaRemoteConnect::dmp_get_user_agent(void) {
    return DU_UCHAR_CONST("DLNADOC/1.50");
}
