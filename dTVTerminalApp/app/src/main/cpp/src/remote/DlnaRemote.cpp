/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <sstream>
#include <du_http_client.h>
#include <cstring>
#include <ddtcp.h>
#include <cipher_file_context_global.h>
#include <secure_io_global.h>
#include <du_log.h>
#include "DlnaRemote.h"
#include "../DTVTLogger.h"

namespace dtvt {

    //global var
    extern bool gIsGlobalDtcpInited;

    DlnaRemote::DlnaRemote(): mDtcp(NULL), mDirToSave("") {
        mEvent.mJavaVM=NULL;
        mEvent.mJObject=NULL;
    }

    DlnaRemote::~DlnaRemote(){
        stop();
    }

    bool DlnaRemote::start(JNIEnv *env, jobject obj, std::string confDir) {
        if(mDtcp){
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
        if (0 != ret || NULL == mEvent.mJavaVM) {
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::start exit false");
            return false;
        }

        mEvent.mJObject = env->NewGlobalRef(obj);
        if(NULL==mEvent.mJObject){
            mEvent.mJavaVM = NULL;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::start exit false");
            return false;
        }

        if(!startDlEnv(env, obj, confDir)){
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

    void DlnaRemote::stop() {
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::stop enter");
//        if(mDtcp){
//            dixim::dmsp::dtcp::dtcp* dtcp= (dixim::dmsp::dtcp::dtcp *) mDtcp;
//            dtcp->stop();
//        }
        DelIfNotNull(mDtcp);
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::stop exit");
    }

    bool DlnaRemote::startDlEnv(JNIEnv *env, jobject instance, std::string& dirToSave){
//        if(false==gIsGlobalDtcpInited){
//            return false;
//        }
//        if(mDtcp){
//            return true;
//        }
//
//        const du_uchar* private_data_home_path = DU_UCHAR_CONST(dirToSave.c_str());
//        JavaVM *vm=NULL;
//        dixim::dmsp::dtcp::dtcp* dtcp=NULL;
//
//        //DelIfNotNull(mDtcp);
//        mDtcp = new dixim::dmsp::dtcp::dtcp();
//        if(!mDtcp){
//            return false;
//        }
//        dtcp= (dixim::dmsp::dtcp::dtcp *) mDtcp;
//        dtcp->private_data_home = (const char*)private_data_home_path;
//        dtcp->ake_port = 53211;
//
//        if (!dtcp->start(mEvent.mJavaVM, instance, this)) {
//            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::dtcpDownload, d.start failed");
//            goto error3;
//        }
//
//        return true;
//
//        error3:
//            cipher_file_context_global_free();
//            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::startDlEnv, cipher_file_context_global_free when error");
//        error2:
//            secure_io_global_free();
//            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaRemote.cpp DlnaRemote::startDlEnv, secure_io_global_free when error");
//            du_log_wv(0, DU_UCHAR_CONST("ERROR"));
//            downloaderStatusHandler(DOWNLOADER_STATUS_ERROR_OCCURED, 0, this);
//            DelIfNotNull(mDtcp);
        return false;
    }



} //namespace dtvt
