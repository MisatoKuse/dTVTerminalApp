/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <jni.h>
#include <string>

#include <cipher_file_context_global.h>
#include <secure_io_global.h>
#include "DlnaDownload.h"
#include "../DTVTLogger.h"
#include "../android_log_handler.h"

/**
 * 機能： JNI cast
 */
namespace dtvt {

    //global var
    bool gIsGlobalDtcpInited=false;

    /**
     * 機能：DlnaDownload objectを作成
     */
    extern "C" jlong
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_nativeCreateDlnaDownloadObject(JNIEnv *env, jobject obj) {
        jlong result;
        result = (jlong) new DlnaDownload();
        return result;
    }

    /**
     * 機能：dlを開始
     */
    extern "C" jboolean JNICALL
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_nativeStartDlna(JNIEnv *env, jobject obj, jlong thiz, jstring dirToSave_) {
        unsigned char ret = 0;
        DlnaDownload *dlnaDownloadPtr = (DlnaDownload *) thiz;
        if (NULL == dlnaDownloadPtr) {
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_nativeStartDlna exit, 0==thiz");
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return (jboolean) ret;
        }

        if (NULL == dirToSave_) {
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_nativeStartDlna exit, param error dirToSave_");
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return (jboolean)ret;
        }

        const char *dirToSave = env->GetStringUTFChars(dirToSave_, 0);
        if(NULL==dirToSave){
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_nativeStartDlna exit, param error dirToSave_");
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return (jboolean)ret;
        }

        bool ret2 = dlnaDownloadPtr->start(env, obj, dirToSave);
        ret = (true == ret2 ? 1 : 0);

        env->ReleaseStringUTFChars(dirToSave_, (const char *) dirToSave);

        return (jboolean) ret;
    }

//    extern "C" void JNICALL
//    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_nativeStop(JNIEnv *env, jobject obj, jlong thiz) {
//        DlnaDownload *dlnaDownloadPtr = (DlnaDownload *) thiz;
//        if (NULL == dlnaDownloadPtr) {
//            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_nativeStop exit, 0==thiz");
//            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
//            return;
//        }
//        dlnaDownloadPtr->stop();
//    }

    extern "C"  void JNICALL
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download(JNIEnv *env,
                                                                        jobject instance, jlong thiz,
                                                                        /*jstring dirToSave_, */jstring fileNameToSave_, jstring dtcp1host_, int dtcp1port, jstring url_, int cleartextSize, jstring xml_) {
        DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download enter");
        if (0==thiz) {
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download exit, 0==thiz");
            return;
        }
        DlnaDownload *dlnaDownloadPtr = (DlnaDownload *) thiz;
        if (NULL==fileNameToSave_ || NULL==dtcp1host_ || NULL==url_ || NULL==xml_) {
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download exit, param error 1");
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return;
        }

        const char *fileNameToSave = env->GetStringUTFChars(fileNameToSave_, 0);
        const char *dtcp1host = env->GetStringUTFChars(dtcp1host_, 0);
        const char *url = env->GetStringUTFChars(url_, 0);
        const char *xml = env->GetStringUTFChars(xml_, 0);
        if(NULL==fileNameToSave){
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download exit, param error 3");
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return;
        }
        if(NULL==dtcp1host){
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download exit, param error 4");
            env->ReleaseStringUTFChars(fileNameToSave_, (const char *) fileNameToSave);
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return;
        }
        if(NULL==url){
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download exit, param error 5");
            env->ReleaseStringUTFChars(fileNameToSave_, (const char *) fileNameToSave);
            env->ReleaseStringUTFChars(dtcp1host_, (const char *) dtcp1host);
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return;
        }
        if(NULL==xml){
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download exit, param error 6");
            env->ReleaseStringUTFChars(fileNameToSave_, (const char *) fileNameToSave);
            env->ReleaseStringUTFChars(dtcp1host_, (const char *) dtcp1host);
            env->ReleaseStringUTFChars(url_, (const char *) url);
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return;
        }

        dlnaDownloadPtr->dtcpDownload(string((char *) fileNameToSave), string((char *) dtcp1host),
                              dtcp1port, string((char *) url), cleartextSize, xml);

        env->ReleaseStringUTFChars(fileNameToSave_, (const char *) fileNameToSave);
        env->ReleaseStringUTFChars(dtcp1host_, (const char *) dtcp1host);
        env->ReleaseStringUTFChars(url_, (const char *) url);
        env->ReleaseStringUTFChars(xml_, (const char *) xml);
        DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download exit");
    }

    extern "C"  void JNICALL
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_downloadCancel(JNIEnv *env, jobject instance, jlong thiz) {
        if (0==thiz) {
            return;
        }

        DlnaDownload *DlnaDownloadPtr = (DlnaDownload *) thiz;
        DlnaDownloadPtr->dtcpDownloadCancel();
    }

    extern "C"  jboolean JNICALL
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_initGlobalDl(JNIEnv *env, jobject instance, jstring privateHome_) {
        gIsGlobalDtcpInited=false;
        unsigned char ret = 0;
        if(!secure_io_global_create()){
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download exit, secure_io_global_create error");
            ret = 0;
        }
        DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download exit, secure_io_global_create ok");

        const char *privateHome = env->GetStringUTFChars(privateHome_, 0);
        if(NULL==privateHome){
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_initGlobalDl exit, param error");
            return (jboolean)0;
        }
        if (!cipher_file_context_global_create(secure_io_global_get_instance(), (const du_uchar *) privateHome)) {
            DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_initGlobalDl exit, cipher_file_context_global_create error");
            secure_io_global_free();
            return (jboolean)0;
        }
        DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download exit, cipher_file_context_global_create ok");
        env->ReleaseStringUTFChars(privateHome_, (const char *) privateHome);
        ret=1;
        gIsGlobalDtcpInited=true;

        set_android_log_handler();

        return (jboolean)ret;
    }

    extern "C"  void JNICALL
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_uninitGlobalDl(JNIEnv *env, jobject instance) {
        cipher_file_context_global_free();
        DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_uninitGlobalDl exit, cipher_file_context_global_free ok");
        secure_io_global_free();
        DTVT_LOG_DBG("Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_uninitGlobalDl exit, secure_io_global_free ok");
    }



} //end of namespace dtvt
