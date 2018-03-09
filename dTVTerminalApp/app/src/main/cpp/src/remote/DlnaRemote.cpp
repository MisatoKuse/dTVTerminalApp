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

#include <drag_cp.h>

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
        switch(status) {
            case DRAG_CP_CONNECT_STATUS_UNKNOWN:
                puts("DRAG_CP_CONNECT_STATUS_UNKNOWN");
                break;
            case DRAG_CP_CONNECT_STATUS_READY:
                puts("DRAG_CP_CONNECT_STATUS_READY");
                break;
            case DRAG_CP_CONNECT_STATUS_CONNECTED:
                puts("DRAG_CP_CONNECT_STATUS_CONNECTED");
                break;
            case DRAG_CP_CONNECT_STATUS_DETECTED_DISCONNECTION:
                puts("DRAG_CP_CONNECT_STATUS_DETECTED_DISCONNECTION");
                break;
            case DRAG_CP_CONNECT_STATUS_GAVEUP_RECONNECTION:
                puts("DRAG_CP_CONNECT_STATUS_GAVEUP_RECONNECTION");
                break;
        }
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

        duRet = dmp_start(mDmpRm);
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

    du_bool DlnaRemote::dmp_start(DmpRm* d) {
        if (!dupnp_start(&d->upnp)) {
            return 0;
        }
        if (!dupnp_cp_evtmgr_start(&d->em)) {
            goto error;
        }
        if (!dmp_cp_start(d)) {
            goto error2;
        }
        if (!player_start(&d->p)) {
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
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>> --------------------------------");
    }

    du_bool DlnaRemote::dmp_init(DmpRm* d, const du_uchar* conf_path){
        du_uchar_array ua;
        du_uchar_array ua2;
        dupnp_taskmgr* taskmgr;
        dupnp_schedtaskmgr* schedtaskmgr;
        const char* ddd = "/data/user/0/com.nttdocomo.android.tvterminalapp/files/drm/conf/dirag/rada/rada_relay";

        du_uchar_array_init(&ua);
        du_uchar_array_init(&ua2);

        du_byte_zero((du_uint8*)d, sizeof(DmpRm));

        if (!dupnp_init(&d->upnp, 0, 0)) {
            goto error;
        }
        printDir(ddd);

        if (!dmp_cp_init(d)) {
            goto error2;
        }
        printDir(ddd);
        dmp_event_adapter_init(&d->ea, &d->upnp, &d->dm, &d->em);
        printDir(ddd);

        if (!dmp_conf_get_capability_xml_path(conf_path, &ua)) {
            goto error3;
        }
        printDir(ddd);

        if (!du_uchar_array_cat0(&ua)) {
            goto error3;
        }

        if (!capability_init(d, du_uchar_array_get(&ua))) {
            goto error3;
        }
        printDir(ddd);

        if (!dmp_conf_get_download_dir_path(conf_path, &ua, &ua2)) {
            goto error4;
        }
        printDir(ddd);

        if (!du_uchar_array_cat0(&ua)) {
            goto error4;
        }
        if (!player_init(&d->p, 0, &d->cap, du_uchar_array_get(&ua))) {
            goto error4;
        }
        printDir(ddd);

#ifdef ENABLE_DTCP
        if (!dmp_conf_get_private_data_home_path(conf_path, &ua)) {
            goto error5;
        }
        printDir(ddd);

        if (!du_uchar_array_cat0(&ua)) {
            goto error5;
        }
        printDir(ddd);

        if (!player_set_private_data_home(&d->p, du_uchar_array_get(&ua))){
            goto error5;
        }
        printDir(ddd);
#endif

        if (!dupnp_cp_evtmgr_init(&d->em, &d->upnp)) {
            goto error5;
        }
        printDir(ddd);

        taskmgr = dupnp_get_taskmgr(&d->upnp);
        if (!taskmgr) {
            goto error6;
        }
        schedtaskmgr = dupnp_taskmgr_get_schedtaskmgr(taskmgr);
        printDir(ddd);
        if (!schedtaskmgr) {
            goto error6;
        }

        if (!dmp_ui_init(&d->ui, &d->upnp, &d->dm, &d->cb_browse, &d->cb_event,&d->p, &d->ea, schedtaskmgr, &d->cap, dmp_get_user_agent())) {
            goto error6;
        }
        printDir(ddd);

        dupnp_enable_netif_monitor(&d->upnp, 1);
        printDir(ddd);

        if (!dmp_conf_get_dirag_path(conf_path, &ua)) {
            goto error6;
        }
        printDir(ddd);

        if (!du_uchar_array_cat0(&ua)) {
            goto error6;
        }
        if (!drag_cp_initialize(du_uchar_array_get(&ua))) {
            goto error6;
        }
        printDir(ddd);

        if (!drag_cp_lrsys_start()) {
            goto error8;
        }
        printDir(ddd);

        //if (!drag_cp_rasys_start(dcp_connect_status_handler, d)) {
        //if (!drag_cp_rasys_start(dcp_connect_status_handler, (void*)&m_connect_status_arg)) {
        if (!drag_cp_rasys_start(dcp_connect_status_handler, this)) {
            drag_error_code err = drag_cp_get_last_error();
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp dmp_init func, drag_cp_get_last_error = %x", err);
            goto error9;
        }
        printDir(ddd);

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
