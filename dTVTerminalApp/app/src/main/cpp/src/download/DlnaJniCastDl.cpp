/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <jni.h>
#include <string>

#include "DlnaDownload.h"

/**
 * 機能： JNI cast
 */
namespace dtvt {

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
     * 機能：DLNAを開始
     */
    extern "C" jboolean JNICALL
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_nativeStartDlna(JNIEnv *env, jobject obj, jlong thiz) {
        unsigned char ret = 0;
        DlnaDownload *dlnaPtr = (DlnaDownload *) thiz;
        if (NULL == dlnaPtr) {
            return (jboolean) ret;
        }

        bool ret2 = dlnaPtr->start(env, obj);
        ret = (true == ret2 ? 1 : 0);

        return (jboolean) ret;
    }

    extern "C"  void JNICALL
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_download(JNIEnv *env,
                                                                        jobject instance, jlong thiz,
                                                                        jstring dirToSave_, jstring fileNameToSave_, jstring dtcp1host_, int dtcp1port, jstring url_, int cleartextSize, jstring xml_) {
        if (0==thiz) {
            return;
        }
        DlnaDownload *dlnaDownloadPtr = (DlnaDownload *) thiz;
        if (NULL == dirToSave_ || NULL==fileNameToSave_ || NULL==dtcp1host_ || NULL==url_ || NULL==xml_) {
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return;
        }

        const char *dirToSave = env->GetStringUTFChars(dirToSave_, 0);
        const char *fileNameToSave = env->GetStringUTFChars(fileNameToSave_, 0);
        const char *dtcp1host = env->GetStringUTFChars(dtcp1host_, 0);
        const char *url = env->GetStringUTFChars(url_, 0);
        const char *xml = env->GetStringUTFChars(xml_, 0);
        if(NULL==dirToSave){
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return;
        }
        if(NULL==fileNameToSave){
            env->ReleaseStringUTFChars(dirToSave_, (const char *) dirToSave);
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return;
        }
        if(NULL==dtcp1host){
            env->ReleaseStringUTFChars(dirToSave_, (const char *) dirToSave);
            env->ReleaseStringUTFChars(fileNameToSave_, (const char *) fileNameToSave);
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return;
        }
        if(NULL==url){
            env->ReleaseStringUTFChars(dirToSave_, (const char *) dirToSave);
            env->ReleaseStringUTFChars(fileNameToSave_, (const char *) fileNameToSave);
            env->ReleaseStringUTFChars(dtcp1host_, (const char *) dtcp1host);
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return;
        }
        if(NULL==xml){
            env->ReleaseStringUTFChars(dirToSave_, (const char *) dirToSave);
            env->ReleaseStringUTFChars(fileNameToSave_, (const char *) fileNameToSave);
            env->ReleaseStringUTFChars(dtcp1host_, (const char *) dtcp1host);
            env->ReleaseStringUTFChars(url_, (const char *) url);
            dlnaDownloadPtr->downloaderStatusHandler(DOWNLOADER_STATUS_UNKNOWN, NULL, dlnaDownloadPtr);
            return;
        }

        dlnaDownloadPtr->dtcpDownload(env, instance,
                              string((char *) dirToSave), string((char *) fileNameToSave), string((char *) dtcp1host),
                              dtcp1port, string((char *) url), cleartextSize, xml);

        env->ReleaseStringUTFChars(dirToSave_, (const char *) dirToSave);
        env->ReleaseStringUTFChars(fileNameToSave_, (const char *) fileNameToSave);
        env->ReleaseStringUTFChars(dtcp1host_, (const char *) dtcp1host);
        env->ReleaseStringUTFChars(url_, (const char *) url);
        env->ReleaseStringUTFChars(xml_, (const char *) xml);
    }

    extern "C"  void JNICALL
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterfaceDl_downloadCancel(JNIEnv *env, jobject instance, jlong thiz) {
        if (0==thiz) {
            return;
        }

        DlnaDownload *DlnaDownloadPtr = (DlnaDownload *) thiz;
        DlnaDownloadPtr->dtcpDownloadCancel();
    }

} //end of namespace dtvt
