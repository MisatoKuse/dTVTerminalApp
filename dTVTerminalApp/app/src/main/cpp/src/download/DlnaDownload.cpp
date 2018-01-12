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
#include "../DTVTLogger.h"

namespace dtvt {

    //local function b
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
    //local function e

    DlnaDownload::DlnaDownload(): mDtcp(NULL), mDirToSave("") {
        mEvent.mJavaVM=NULL;
        mEvent.mJObject=NULL;
    }

    DlnaDownload::~DlnaDownload(){
        stop();
    }

    bool DlnaDownload::start(JNIEnv *env, jobject obj, std::string dirToSave) {
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

        if(!startDlEnv(env, obj, dirToSave)){
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
            cipher_file_context_global_free();
            secure_io_global_free();
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::stop, cipher_file_context_global_free");
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::stop, secure_io_global_free");
        }
//        if(mEvent.mJavaVM){
//            JNIEnv *env= NULL;
//            mEvent.mJavaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
//            if(env){
//                env->DeleteGlobalRef(mEvent.mJObject);
//            }
//        }
        DelIfNotNull(mDtcp);
//        mEvent.mJObject=NULL;
//        mEvent.mJavaVM = NULL;
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::stop exit");
    }

    bool DlnaDownload::startDlEnv(JNIEnv *env, jobject instance, std::string& dirToSave){
        const du_uchar* private_data_home_path = DU_UCHAR_CONST(dirToSave.c_str());
        JavaVM *vm=NULL;
        dixim::dmsp::dtcp::dtcp* dtcp=NULL;

        if (!cipher_file_context_global_create(secure_io_global_get_instance(), private_data_home_path)) {
            goto error2;
        }
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::startDlEnv, secure_io_global_get_instance");
        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::startDlEnv, cipher_file_context_global_create");

        DelIfNotNull(mDtcp);
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
        error2:
            secure_io_global_free();
            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::startDlEnv, secure_io_global_free when error");
            du_log_wv(0, DU_UCHAR_CONST("ERROR"));
            downloaderStatusHandler(DOWNLOADER_STATUS_ERROR_OCCURED, 0, this);
            DelIfNotNull(mDtcp);
        return false;
    }

    void DlnaDownload::downloaderStatusHandler(DownloaderStatus status, const du_uchar* http_status, void* arg){
        if (NULL == arg) {
            return;
        }
        DlnaDownload *thiz = (DlnaDownload *) arg;
        std::string content= intToString(status);
        thiz->notify(DLNA_MSG_ID_DL_STATUS, content);
    }

    void DlnaDownload::downloaderProgressHandler(du_uint64 sent_size, du_uint64 total_size, void* arg){
        if (NULL == arg) {
            return;
        }
        DlnaDownload *thiz = (DlnaDownload *) arg;
        std::string content= longToString(sent_size);
        thiz->notify(DLNA_MSG_ID_DL_PROGRESS, content);
    }


    void DlnaDownload::notify(int msg, std::string content) {
        JNIEnv *env = NULL;
        int status = mEvent.mJavaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
        if (status < 0) {
            status = mEvent.mJavaVM->AttachCurrentThread(&env, NULL);
            if (status < 0 || NULL == env) {
                return;
            }
        }

        jclass listActivityClazz = env->GetObjectClass(mEvent.mJObject);
        jmethodID method = env->GetMethodID(listActivityClazz, "notifyFromNative",
                                            "(ILjava/lang/String;)V");
        if (NULL == method) {
            return;
        }

        jstring jstr = env->NewStringUTF(content.c_str());
        env->CallVoidMethod(mEvent.mJObject, method, msg, jstr);
        env->DeleteLocalRef(jstr);
        env->DeleteLocalRef(listActivityClazz);
    }

    bool DlnaDownload::isStarted(){
        return NULL!=mDtcp;
    }

    void DlnaDownload::dtcpDownload(std::string fileNameToSave, std::string dtcp1host_, int dtcp1port_, std::string url_, int cleartextSize, const char *xml){
        const du_uchar* dtcp1host = DU_UCHAR_CONST(dtcp1host_.c_str());
        du_uint16 dtcp1port = dtcp1port_;
        const du_uchar* url = DU_UCHAR_CONST(url_.c_str());
        du_bool move = 1;
        string fileToDownload= mDirToSave + "/" + fileNameToSave;
        const du_uchar* dixim_file = DU_UCHAR_CONST(fileToDownload.c_str());
        du_uint64 cleartext_size = cleartextSize;
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

//下記のソースはAppはForegroundの場合は実行正常であるが、Appは閉じると、Serviceで二番めのdlは失敗なので、一時コメントアウトし、別の方法を検討することになる。まず、下記保留
//    void DlnaDownload::dtcpDownload(JNIEnv *env, jobject instance, std::string dirToSave, std::string fileNameToSave, std::string dtcp1host_, int dtcp1port_, std::string url_, int cleartextSize, const char *xml){
//        const du_uchar* dtcp1host = DU_UCHAR_CONST(dtcp1host_.c_str());
//        du_uint16 dtcp1port = dtcp1port_;
//        const du_uchar* url = DU_UCHAR_CONST(url_.c_str());
//        du_bool move = 1;
//        bool retTmp=true;
//        string fileToDownload= dirToSave + "/" + fileNameToSave;
//        const du_uchar* dixim_file = DU_UCHAR_CONST(fileToDownload.c_str());
//        du_uint64 cleartext_size = cleartextSize;
//        du_str_array* request_header = 0;
//        const du_uchar* private_data_home_path = DU_UCHAR_CONST(dirToSave.c_str());
//        du_uchar* xmlStrDu=NULL;
//
//        if (!cipher_file_context_global_create(secure_io_global_get_instance(), private_data_home_path)) {
//            goto error2;
//        }
//        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::dtcpDownload, secure_io_global_get_instance");
//        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::dtcpDownload, cipher_file_context_global_create");
//
//        xmlStrDu= (du_uchar *) xml;
//        if(!xmlStrDu){
//            goto error2;
//        }
//
//        {
//            dixim::dmsp::dtcp::dtcp d;
//            d.private_data_home = (const char*)private_data_home_path;
//            d.ake_port = 53211;
//            JavaVM *vm=NULL;
//
//            env->GetJavaVM(&vm);
//            if(NULL==vm){
//                goto error2;
//            }
//            //if (!d.start(mEvent.mJavaVM, instance, this)) {
//            if (!d.start(vm, instance, this)) {
//                DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::dtcpDownload, d.start failed");
//                goto error3;
//            }
//            if (!downloader_download(
//                    d.d,
//                    dtcp1host,
//                    dtcp1port,
//                    url,
//                    move,
//                    (downloader_status_handler) downloaderStatusHandler,
//                    (downloader_progress_handler)downloaderProgressHandler,
//                    this, //handler arg
//                    dixim_file,
//                    xmlStrDu,
//                    cleartext_size,
//                    request_header)) {
//                goto error3;
//            }
//            d.stop(); //original
//        }
//
//
//
//        cipher_file_context_global_free();
//        secure_io_global_free();
//        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::dtcpDownload, cipher_file_context_global_free when ok");
//        DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::dtcpDownload, secure_io_global_free when ok");
//        du_log_dv(0, DU_UCHAR_CONST("OK"));
//
//        return;
//
//        error3:
//            cipher_file_context_global_free();
//            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::dtcpDownload, cipher_file_context_global_free when error");
//        error2:
//            secure_io_global_free();
//            DTVT_LOG_DBG("C>>>>>>>>>>>>>>>>DlnaDownload.cpp DlnaDownload::dtcpDownload, secure_io_global_free when error");
//            du_log_wv(0, DU_UCHAR_CONST("ERROR"));
//            downloaderStatusHandler(DOWNLOADER_STATUS_ERROR_OCCURED, 0, this);
//    }

    void DlnaDownload::dtcpDownloadCancel(){
        downloader_cancel();
    }

} //namespace dtvt
