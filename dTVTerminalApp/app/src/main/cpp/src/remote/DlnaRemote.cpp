/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <sstream>
#include <du_http_client.h>
#include <cstring>
#include "DlnaRemote.h"
#include "../DTVTLogger.h"
#include "dmp.h"

#include <dupnp_cp.h>
#include <dupnp_dvc_service.h>
#include <dupnp_dvc_service_adapter.h>
#include <dupnp_dvcdsc_device.h>
#include <dupnp_dvcdsc_device_array.h>
#include <dmrd_avt_stub.h>
#include <dmrd_cms_stub.h>
#include <dmrd_rcs_stub.h>
#include <dupnp_taskmgr.h>
#include <dav_urn.h>
#include <dupnp_urn.h>
#include <du_byte.h>
#include <du_str.h>
#include <du_signal.h>
#include <du_log.h>
#include <stdio.h>
#include "dmp_conf.h"
#include "dmp_event_adapter.h"
#include "CommonRm.h"
#include "../android_log_handler.h"
#include "dirent.h"
#include <du_file_input_buffer.h>
#include <du_file.h>

#include <drag_cp.h>

#include "dvcdsc_device.h"
#include "local_registration.h"
#include <du_alloc.h>
#include "dmp_ui_dms_view.h"

#define CURRENT_BITRATE 8 * 1024 * 1024 * 50
#define MONITOR_WIDTH 1280
#define MONITOR_HEIGHT 720

namespace dtvt {

    //===================================== c function begin ==========================================//
    static void capability_free(DmpRm* p) {
        dav_capability_free(&p->cap);
    }

    static void dmp_cp_free(DmpRm* d) {
        dav_capability_free(&d->cap);
        dupnp_cp_dvcmgr_free(&d->dm);
        cds_browser_free(&d->cb_browse);
        cds_browser_free(&d->cb_event);
    }

    static du_bool dmp_cp_start(DmpRm* d) {
        if (!dupnp_cp_dvcmgr_start(&d->dm)) return 0;

        return 1;
    }

    static void dmp_cp_stop(DmpRm* d) {
        dupnp_cp_dvcmgr_stop(&d->dm);
    }

    static void dcp_connect_status_handler(drag_cp_connect_status status, void* arg) {
        DlnaRemote *thiz = (DlnaRemote *) arg;
        std::string content;
        switch(status) {
            //不明
            case DRAG_CP_CONNECT_STATUS_UNKNOWN:
                puts("DRAG_CP_CONNECT_STATUS_UNKNOWN");
                content = "UNKNOWN";
                break;
            //接続準備環境
            case DRAG_CP_CONNECT_STATUS_READY:
                puts("DRAG_CP_CONNECT_STATUS_READY");
                content = "READY";
                break;
            //接続中
            case DRAG_CP_CONNECT_STATUS_CONNECTED:
                puts("DRAG_CP_CONNECT_STATUS_CONNECTED");
                content = "CONNECTED";
                break;
            //切断
            case DRAG_CP_CONNECT_STATUS_DETECTED_DISCONNECTION:
                puts("DRAG_CP_CONNECT_STATUS_DETECTED_DISCONNECTION");
                content = "DISCONNECTION";
                break;
            //再接続失敗
            case DRAG_CP_CONNECT_STATUS_GAVEUP_RECONNECTION:
                puts("DRAG_CP_CONNECT_STATUS_GAVEUP_RECONNECTION");
                content = "RECONNECTION";
                break;
        }
//        thiz->notify(DLNA_MSG_ID_RM_STATUS, content);
    }

    //====================================== c function end ===========================================//

    //global var
    extern bool gIsGlobalDtcpInited;

    DlnaRemote::DlnaRemote(): mDmpRm(NULL), mConfDir("") {
        mEvent.mJavaVM=NULL;
        mEvent.mJObject=NULL;
        m_connect_status_arg.parameter = 0;
    }

    DlnaRemote::~DlnaRemote(){
        stop();
    }

    bool DlnaRemote::start(JNIEnv *env, jobject obj, std::string confDir) {
        set_android_log_handler();
        if(mDmpRm){
           return true;
        }
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::start enter");
        jint ret = 0;
        bool ok = true;

        if (NULL == env || NULL == obj) {
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::start exit false");
            return false;
        }

        //init mEvent
        ret = env->GetJavaVM(&mEvent.mJavaVM);
        if (0 != ret || NULL == mEvent.mJavaVM || 0 == confDir.length()) {
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::start exit false");
            return false;
        }

        mEvent.mJObject = env->NewGlobalRef(obj);
        if(NULL==mEvent.mJObject){
            mEvent.mJavaVM = NULL;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::start exit false");
            return false;
        }

        mDmpRm = new DmpRm();
        IfNullReturnFalse(mDmpRm);
        du_bool duRet = dmp_init(mDmpRm, (const du_uchar *) confDir.c_str());
        if(false == duRet){
            env->DeleteGlobalRef(mEvent.mJObject);
            mEvent.mJObject=NULL;
            mEvent.mJavaVM = NULL;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::start exit false");
            return false;
        }

        duRet = dmp_start(mDmpRm, obj, confDir);
        if(false == duRet){
            stop();
            env->DeleteGlobalRef(mEvent.mJObject);
            mEvent.mJObject=NULL;
            mEvent.mJavaVM = NULL;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::start exit false");
            return false;
        }

        mConfDir= confDir;

        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::start exit ok");
        return ok;
    }

    du_bool dmp_ui_regist(dmp_ui* ui, player* p, std::string udn) {
        du_file_input_buffer fb;
        du_uchar fb_buf[256];
        du_uint argc = 2;
        const du_uchar* argv[5];

        du_file_input_buffer_init(&fb, fb_buf, sizeof fb_buf, du_file_stdin());

        argv[0] = DU_UCHAR_CONST("lr");
        argv[1] = DU_UCHAR_CONST("1");

        ui->_current_view->_user_data;
        ui->_current_view->_command_lr(ui->_current_view->_user_data, p, argc, argv, (du_uchar*)udn.c_str());
        return 1;
    }

    static void lr_register_response_handler(du_uint32 requeseted_id, local_registration_error_info* error_info) {
        if (error_info->type == LOCAL_REGISTRATION_ERROR_TYPE_NONE) {
            puts("Finsh PrepareRegistration/Local (Un)Registration");
        } else {
            puts("Error PrepareRegistration/Local (Un)Registration");
            printf("%d\n", error_info->type);
            printf("%s\n", error_info->http_status);
            printf("%s\n", error_info->soap_error_code);
            printf("%s\n", error_info->soap_error_description);
        }
    }

    typedef struct regist_dms_visitor_context {
        dmp_ui_dms_view* mv;
        const du_uchar* udn;
        du_uchar* control_url;
        du_uchar* dtcp1_host;
        du_uint16 dtcp1_port;
        du_bool is_v2;
        du_bool found;
        du_bool succeeded;
    } regist_dms_visitor_context;

    static du_bool regist_dms_visitor(dupnp_cp_dvcmgr_device* device, void* arg) {
        regist_dms_visitor_context* context = (regist_dms_visitor_context*)arg;
        dvcdsc_device* dd = (dvcdsc_device*)device->user_data;

        if (du_str_diff(context->udn, device->udn)) return 1;

        context->found = 1;
        if (!du_str_clone(dd->x_dps.control_url, &context->control_url)) return 0;
        if (!du_str_clone(dd->rs_regi_socket_host, &context->dtcp1_host)) goto error;
        context->dtcp1_port = dd->rs_regi_socket_port;
        context->is_v2 = dd->x_dps.dps_is_v2;
        context->succeeded = 1;
        return 0;

        error:
        du_alloc_free(&context->control_url);
        context->control_url = 0;
        return 0;
    }

//    du_bool dmp_ui_dms_view_init(dmp_ui_view* view, dmp_ui* ui, dupnp* upnp, dupnp_cp_dvcmgr* dm, player* p, const du_uchar* user_agent) {
//        dmp_ui_dms_view* mv = 0;
//
//        mv = du_alloc_zero(sizeof(dmp_ui_dms_view));
//        if (!mv) return 0;
//        mv->_ui = ui;
//        mv->_upnp = upnp;
//        mv->user_agent = user_agent;
//        mv->_dm = dm;
//        mv->_player = p;
//
//        du_str_array_init(&mv->_udn_array);
//
//        view->_user_data = mv;
//        view->_command = local_registration;
//        view->_update_screen = update_screen;
//        return 1;
//    }

    bool DlnaRemote::regist(std::string udn) {
        set_android_log_handler();
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::regist enter");

        mDmpRm->ea._join_handler_array;
        if (!dmp_ui_regist(&mDmpRm->ui, &mDmpRm->p, udn)) {
            goto error;
        }
        return 1;
        error:
        player_stop(&mDmpRm->p);
        return 0;
    }

    du_bool dmp_ui_connect(dmp_ui* ui, player* p, std::string udn) {
        du_file_input_buffer fb;
        du_uchar fb_buf[256];
        du_uint argc = 2;
        const du_uchar* argv[5];

        du_file_input_buffer_init(&fb, fb_buf, sizeof fb_buf, du_file_stdin());

        argv[0] = DU_UCHAR_CONST("conn");
        argv[1] = DU_UCHAR_CONST("1");
        ui->_current_view->_user_data;
//        &mDmpRm->p
//        mv->_player = p;
//        ui->_dms_view._command(mDmpRm->ea._join_handler_array, argc, argv);
        ui->_current_view->_command_lr(ui->_current_view->_user_data, p, argc, argv, (du_uchar*) udn.c_str());
//        ui->_dms_view dmp_ui_dms_view* mv
        return 1;
    }

    bool DlnaRemote::connect(std::string udn) {
        set_android_log_handler();
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::regist enter");

        mDmpRm->ea._join_handler_array;
        if (!dmp_ui_connect(&mDmpRm->ui, &mDmpRm->p, udn)) {
            goto error;
        }
        return 1;
        error:
        player_stop(&mDmpRm->p);
        return 0;
    }

    void clearAttachStatus(JNIEnv *env, JavaVM *vm, bool isAttached){
        if(env && vm && isAttached){
            vm->DetachCurrentThread();
        }
    }

    du_bool DlnaRemote::dmp_start(DmpRm* d, jobject instance, std::string& dirToSave) {

        JNIEnv *env = NULL;
        bool isAttached=false;
        int status = mEvent.mJavaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
        if (status < 0) {
            status = mEvent.mJavaVM->AttachCurrentThread(&env, NULL);
            if (status < 0 || NULL == env) {
                DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start failed, status < 0 || NULL == env");
                return false;
            }
            isAttached=true;
        }

        jobject objTmp = env->NewGlobalRef(instance);
        jclass clazz = env->GetObjectClass(objTmp);
        jmethodID mid = env->GetMethodID(clazz, "getUniqueId",
                                         "()Ljava/lang/String;");
        if (env->ExceptionCheck()) {
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start failed, env->ExceptionCheck");
            env->DeleteGlobalRef(instance);
            env->DeleteLocalRef(clazz);

            clearAttachStatus(env, mEvent.mJavaVM, isAttached);
            return false;
        }

        jstring strObj = (jstring) env->CallObjectMethod(objTmp, mid);
        if (env->ExceptionCheck()) {
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>dtcp.hpp dtcp.start failed, jstring strObj");
            env->DeleteGlobalRef(instance);
            env->DeleteLocalRef(clazz);
            env->DeleteLocalRef(strObj);
            clearAttachStatus(env, mEvent.mJavaVM, isAttached);
            return false;
        }

        if (!dupnp_start(&d->upnp)) {
            return 0;
        }
        if (!dupnp_cp_evtmgr_start(&d->em)) {
            goto error;
        }
        if (!dmp_cp_start(d)) {
            goto error2;
        }
        if (!player_start(&d->p, mEvent.mJavaVM, objTmp, mid)) {
            goto error3;
        }
        if (!dmp_ui_start(&d->ui)) {
            goto error4;
        }
        return 1;

        error4:
            player_stop(&d->p);
        error3:
            dmp_cp_stop(d);
        error2:
            dupnp_cp_evtmgr_stop(&d->em);
        error:
            dupnp_stop(&d->upnp);
            return 0;
    }

    void DlnaRemote::stop() {
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::stop enter");
        dmp_free(mDmpRm);
        drag_cp_finalize();
//        if(mDmpRm){
//            dixim::dmsp::dtcp::dtcp* dtcp= (dixim::dmsp::dtcp::dtcp *) mDmpRm;
//            dtcp->stop();
//        }
        DelIfNotNull(mDmpRm);
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::stop exit");
    }

    void printDir(string dir){
        const char* rootPath = dir.c_str();  //"/sdcard/0";
        DIR* dirTmp = opendir(rootPath);

        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>> list of %s", rootPath);
        if (dirTmp != NULL) {
            dirent* currentDir;
            while ((currentDir = readdir(dirTmp)) != NULL) {
                DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>     %s", currentDir->d_name); //int currentDir->d_type
            }
            closedir(dirTmp);
        }
    }

    void DlnaRemote::notify(int msg, std::string content) {
        JNIEnv *env = NULL;
        bool isAttached=false;
        int status = mEvent.mJavaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
        if (status < 0) {
            status = mEvent.mJavaVM->AttachCurrentThread(&env, NULL);
            if (status < 0 || NULL == env) {
                return;
            }
            isAttached=true;
        }

        jclass listActivityClazz = env->GetObjectClass(mEvent.mJObject);
        if(NULL==listActivityClazz){
            if(isAttached){
                mEvent.mJavaVM->DetachCurrentThread();
            }
            return;
        }
        jmethodID method = env->GetMethodID(listActivityClazz, "notifyFromNative",
                                            "(ILjava/lang/String;)V");
        if (NULL == method) {
            env->DeleteLocalRef(listActivityClazz);
            if(isAttached){
                mEvent.mJavaVM->DetachCurrentThread();
            }
            return;
        }

        jstring jstr = env->NewStringUTF(content.c_str());
        env->CallVoidMethod(mEvent.mJObject, method, msg, jstr);
        env->DeleteLocalRef(jstr);
        env->DeleteLocalRef(listActivityClazz);
        if(isAttached){
            mEvent.mJavaVM->DetachCurrentThread();
        }
    }

    du_bool DlnaRemote::dmp_init(DmpRm* d, const du_uchar* conf_path){
        du_uchar_array ua;
        du_uchar_array ua2;
        dupnp_taskmgr* taskmgr;
        dupnp_schedtaskmgr* schedtaskmgr;

        du_uchar_array_init(&ua);
        du_uchar_array_init(&ua2);

        du_byte_zero((du_uint8*)d, sizeof(DmpRm));

        if (!dupnp_init(&d->upnp, 0, 0)) {
            goto error;
        }

        if (!dmp_cp_init(d)) {
            goto error2;
        }

        dmp_event_adapter_init(&d->ea, &d->upnp, &d->dm, &d->em);

        if (!dmp_conf_get_capability_xml_path(conf_path, &ua)) {
            goto error3;
        }

        if (!du_uchar_array_cat0(&ua)) {
            goto error3;
        }

        if (!capability_init(d, du_uchar_array_get(&ua))) {
            goto error3;
        }

        if (!dmp_conf_get_download_dir_path(conf_path, &ua, &ua2)) {
            goto error4;
        }

        if (!du_uchar_array_cat0(&ua)) {
            goto error4;
        }
        if (!player_init(&d->p, 0, &d->cap, du_uchar_array_get(&ua))) {
            goto error4;
        }

        if (!dmp_conf_get_private_data_home_path(conf_path, &ua)) {
            goto error5;
        }

        if (!du_uchar_array_cat0(&ua)) {
            goto error5;
        }

        if (!player_set_private_data_home(&d->p, du_uchar_array_get(&ua))){
            goto error5;
        }

        if (!dupnp_cp_evtmgr_init(&d->em, &d->upnp)) {
            goto error5;
        }

        taskmgr = dupnp_get_taskmgr(&d->upnp);
        if (!taskmgr) {
            goto error6;
        }
        schedtaskmgr = dupnp_taskmgr_get_schedtaskmgr(taskmgr);
        if (!schedtaskmgr) {
            goto error6;
        }

        if (!dmp_ui_init(&d->ui, &d->upnp, &d->dm, &d->cb_browse, &d->cb_event,&d->p, &d->ea, schedtaskmgr, &d->cap, dmp_get_user_agent())) {
            goto error6;
        }

        dupnp_enable_netif_monitor(&d->upnp, 1);

        if (!dmp_conf_get_dirag_path(conf_path, &ua)) {
            goto error6;
        }

        if (!du_uchar_array_cat0(&ua)) {
            goto error6;
        }
        //Dirag sdk を起動する
        if (!drag_cp_initialize(du_uchar_array_get(&ua))) {
            goto error6;
        }

        //ローカルレジストレーション をスタート
        if (!drag_cp_lrsys_start()) {
            goto error8;
        }

        if (!drag_cp_rasys_start(dcp_connect_status_handler, d)) {
            drag_error_code err = drag_cp_get_last_error();
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp dmp_init func, drag_cp_get_last_error = %x", err);
            goto error9;
        }

        du_uchar_array_free(&ua);
        du_uchar_array_free(&ua2);

        return 1;

        error9:
            drag_cp_lrsys_stop();
        error8:
            drag_cp_finalize();
        error6:
            dmp_event_adapter_free(&d->ea);
            dupnp_cp_evtmgr_free(&d->em);

        error5:
            player_free(&d->p);
        error4:
            capability_free(d);
        error3:
            dmp_cp_free(d);
        error2:
            dupnp_free(&d->upnp);
        error:
            du_uchar_array_free(&ua);
            du_uchar_array_free(&ua2);
            return 0;
    }

    void DlnaRemote::dmp_free(DmpRm* d) {
        player_free(&d->p);
        dmp_event_adapter_free(&d->ea);
        dupnp_cp_evtmgr_free(&d->em);
        capability_free(d);
        dmp_cp_free(d);
        dupnp_free(&d->upnp);
        dmp_ui_free(&d->ui);
        drag_cp_stop();
    }

    du_bool DlnaRemote::dmp_cp_init(DmpRm* d) {
        if (!dupnp_cp_dvcmgr_init(&d->dm, &d->upnp)) {
            return 0;
        }
        if (!cds_browser_init(&d->cb_browse, &d->upnp, &d->em, dmp_get_user_agent())) {
            goto error;
        }
        if (!cds_browser_init(&d->cb_event, &d->upnp, &d->em, dmp_get_user_agent())) {
            goto error2;
        }

        if (!dupnp_cp_dvcmgr_add_device_type(&d->dm, dmp_get_dms_type())) {
            goto error3;
        }

        if (!dupnp_enable_netif_monitor(&d->upnp, 1)) {
            goto error3;
        }
        if (!dupnp_cp_enable_ssdp_listener(&d->upnp, 1)) {
            goto error3;
        }
        if (!dupnp_cp_enable_ssdp_search(&d->upnp, 1)) {
            goto error3;
        }
        if (!dupnp_cp_enable_http_server(&d->upnp, 1)) {
            goto error3;
        }

        return 1;

        error3:
        cds_browser_free(&d->cb_event);
        error2:
        cds_browser_free(&d->cb_browse);
        error:
        dupnp_cp_dvcmgr_free(&d->dm);
        return 0;
    }

    du_bool DlnaRemote::capability_init(DmpRm* p, const du_uchar* cap_path) {
        if (!dav_capability_init(&p->cap)) {
            return 0;
        }

        if (!dav_capability_set_capability_file(&p->cap, cap_path)) {
            goto error;
        }

        dav_capability_set_max_bitrate(&p->cap, CURRENT_BITRATE);
        dav_capability_set_resolution(&p->cap, MONITOR_WIDTH, MONITOR_HEIGHT);

        return 1;

        error:
        dav_capability_free(&p->cap);
        return 0;
    }

} //namespace dtvt
