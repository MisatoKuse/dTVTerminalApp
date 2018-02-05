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
#include "DlnaDownload.h"
#include "dtcp.hpp"
#include "downloader.h"

namespace dtvt {

    //global var
    extern bool gIsGlobalDtcpInited;
    static bool sIsJustCanceled=false;

    //============================ local function b //============================//
    static pthread_mutex_t gMutexIsJustCanceled;

    static void setIsJustCanceled(bool yn){
        pthread_mutex_lock(&gMutexIsJustCanceled);
        sIsJustCanceled=yn;
        pthread_mutex_unlock(&gMutexIsJustCanceled);
    }

    string intToString(int n)  {
        stringstream ss;
        string str;
        ss<<n;
        ss>>str;
        return str;
    }

    string longToString(long n)  {
        stringstream ss;
        string str;
        ss<<n;
        ss>>str;
        return str;
    }
    //============================ local function e //============================//

    DlnaDownload::DlnaDownload(): mDtcp(NULL), mDirToSave(""), mPercentToNotify(1) {
        mEvent.mJavaVM=NULL;
        mEvent.mJObject=NULL;
    }

    DlnaDownload::~DlnaDownload(){
        stop();
    }

    bool DlnaDownload::start(JNIEnv *env, jobject obj, std::string dirToSave, int percentToNotify) {
        if(mDtcp){
           return true;
        }
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::start enter");
        jint ret = 0;
        bool ok = true;

        if (NULL == env || NULL == obj) {
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::start exit false");
            return false;
        }

        mPercentToNotify = percentToNotify;
        if(mPercentToNotify<0){
            mPercentToNotify= -mPercentToNotify;
        }
        if(mPercentToNotify>99){
            mPercentToNotify=1;
        }
        if(0==mPercentToNotify){
            mPercentToNotify=1;
        }

        //init mEvent
        ret = env->GetJavaVM(&mEvent.mJavaVM);
        if (0 != ret || NULL == mEvent.mJavaVM) {
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::start exit false");
            return false;
        }

        mEvent.mJObject = env->NewGlobalRef(obj);
        if(NULL==mEvent.mJObject){
            mEvent.mJavaVM = NULL;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::start exit false");
            return false;
        }

        if(!startDlEnv(obj, dirToSave)){
            env->DeleteGlobalRef(mEvent.mJObject);
            mEvent.mJObject=NULL;
            mEvent.mJavaVM = NULL;
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::start exit false");
            return false;
        }

        mDirToSave= dirToSave;

        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::start exit ok");
        return ok;
    }

    void DlnaDownload::stop() {
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::stop enter");
        if(mDtcp){
            dixim::dmsp::dtcp::dtcp* dtcp= (dixim::dmsp::dtcp::dtcp *) mDtcp;
            dtcp->stop();
            DelIfNotNull(dtcp);
            mDtcp = NULL;
        }
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::stop exit");
    }

    bool DlnaDownload::startDlEnv(jobject instance, std::string& dirToSave){
        if(false==gIsGlobalDtcpInited){
            return false;
        }
        if(mDtcp){
            return true;
        }

        const du_uchar* private_data_home_path = DU_UCHAR_CONST(dirToSave.c_str());
        //JavaVM *vm=NULL;
        dixim::dmsp::dtcp::dtcp* dtcp=NULL;

        mDtcp = new dixim::dmsp::dtcp::dtcp();
        if(!mDtcp){
            return false;
        }
        dtcp= (dixim::dmsp::dtcp::dtcp *) mDtcp;
        dtcp->private_data_home = (const char*)private_data_home_path;
        dtcp->ake_port = 53211;

        if (!dtcp->start(mEvent.mJavaVM, instance, this)) {
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::dtcpDownload, d.start failed");
            goto error3;
        }

        return true;

        error3:
            cipher_file_context_global_free();
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::startDlEnv, cipher_file_context_global_free when error");
            secure_io_global_free();
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::startDlEnv, secure_io_global_free when error");
            du_log_wv(0, DU_UCHAR_CONST("ERROR"));
            downloaderStatusHandler(DOWNLOADER_STATUS_ERROR_OCCURED, 0, this);
            if(mDtcp){
                dtcp= (dixim::dmsp::dtcp::dtcp *) mDtcp;
                DelIfNotNull(dtcp);
                mDtcp = NULL;
            }

        return false;
    }

    void DlnaDownload::downloaderStatusHandler(DownloaderStatus status, const du_uchar* http_status, void* arg){
        http_status = http_status;  //for no warnnnig
        if (NULL == arg) {
            return;
        }
        DlnaDownload *thiz = (DlnaDownload *) arg;
        std::string content= intToString(status);

        pthread_mutex_lock(&gMutexIsJustCanceled);
        if(DOWNLOADER_STATUS_ERROR_OCCURED==status){
            if(sIsJustCanceled){
                sIsJustCanceled=false;
            } else {
                thiz->notify(DLNA_MSG_ID_DL_STATUS, content);
            }
        } else {
            thiz->notify(DLNA_MSG_ID_DL_STATUS, content);
        }
        pthread_mutex_unlock(&gMutexIsJustCanceled);
    }

    void DlnaDownload::downloaderProgressHandler(du_uint64 sent_size, du_uint64 total_size, void* arg){
        if (NULL == arg) {
            return;
        }
        DlnaDownload *thiz = (DlnaDownload *) arg;

        static du_uint64 lastNotifiedBytes = 0;
        du_uint64 diff= sent_size - lastNotifiedBytes;
        int percent = thiz->getPercentToNotify();

        float res = ((float)(diff)) / ((float)total_size);
        float pp = percent * 0.01f;

        if(0 == lastNotifiedBytes){
            thiz->notify(DLNA_MSG_ID_DL_PROGRESS, "0");
        }

        if(sent_size >= total_size){
            std::string content= longToString(total_size);
            thiz->notify(DLNA_MSG_ID_DL_PROGRESS, content);
            lastNotifiedBytes=0;
            return;
        }
        if (res >= pp) {
            float f1 = ((float) sent_size);
            float f2 = ((float) total_size);
            int ff = (int) ((f1 / f2) * 100);
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp %d%%", ff);
            std::string content= longToString(diff);
            thiz->notify(DLNA_MSG_ID_DL_PROGRESS, content);
            lastNotifiedBytes = sent_size;
        }
    }


    void DlnaDownload::notify(int msg, std::string content) {
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

    bool DlnaDownload::isStarted(){
        return NULL!=mDtcp;
    }

    void DlnaDownload::dtcpDownload(std::string fileNameToSave, std::string dtcp1host_, int dtcp1port_, std::string url_, int cleartextSize, const char *xml){
        const du_uchar* dtcp1host = DU_UCHAR_CONST(dtcp1host_.c_str());
        du_uint16 dtcp1port = dtcp1port_;
        const du_uchar* url = DU_UCHAR_CONST(url_.c_str());
        //du_bool move = 0;
        du_bool move = 1;
        string fileToDownload= mDirToSave + "/" + fileNameToSave;
        const du_uchar* dixim_file = DU_UCHAR_CONST(fileToDownload.c_str());
        du_uint64 cleartext_size = (du_uint64)cleartextSize;
        du_str_array* request_header = 0;
        du_uchar* xmlStrDu = NULL;
        dixim::dmsp::dtcp::dtcp* dtcp= (dixim::dmsp::dtcp::dtcp *) mDtcp;

        if(!isStarted()){
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::dtcpDownload, !isStarted");
            downloaderStatusHandler(DOWNLOADER_STATUS_ERROR_OCCURED, 0, this);
            return;
        }

        xmlStrDu= (du_uchar *) xml;
        if(!xmlStrDu){
            goto error;
        }

        if (!downloader_download(
                dtcp->d,
                dtcp1host,
                dtcp1port,
                url,
                move,
                (downloader_status_handler) downloaderStatusHandler,
                (downloader_progress_handler)downloaderProgressHandler,
                this, //handler arg
                dixim_file,
                xmlStrDu,
                cleartext_size,
                request_header)) {
            goto error;
        }

        return;
        error:
            downloaderStatusHandler(DOWNLOADER_STATUS_ERROR_OCCURED, 0, this);
    }

    void DlnaDownload::dtcpDownloadCancel(){
        setIsJustCanceled(true);
        downloader_cancel();
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::dtcpDownloadCancel()");
    }

} //namespace dtvt
