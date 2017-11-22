/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <jni.h>
#include <string>
#include <dupnp.h>

#include "Dlna.h"

/**
 * 機能： JNI cast
 */
namespace dtvt {

    /**
     * 機能：DLNA objectを作成
     */
    extern "C" jlong
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterface_nativeCreateDlnaObject(JNIEnv *env, jobject obj) {
        jlong result;
        result = (jlong) new Dlna();
        return result;
    }

    /**
     * 機能：DLNAを開始
     */
    extern "C" jboolean JNICALL
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterface_nativeStartDlna(JNIEnv *env, jobject obj, jlong thiz) {
        unsigned char ret = 0;
        Dlna *dlnaPtr = (Dlna *) thiz;
        if (NULL == dlnaPtr) {
            return (jboolean) ret;
        }

        bool ret2 = dlnaPtr->start(env, obj);
        ret = (true == ret2 ? 1 : 0);

        return (jboolean) ret;
    }

    /**
     * 機能：DLNAを停止
     */
    extern "C" void JNICALL
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterface_nativeStopDlna(JNIEnv *env, jobject obj, jlong thiz) {
        Dlna *dlnaPtr = (Dlna *) thiz;
        if (NULL == dlnaPtr) {
            return;
        }

        dlnaPtr->stop();
    }

    /**
     * 機能：コンテンツ一覧を取得
     */
    extern "C" jboolean JNICALL
    Java_com_nttdocomo_android_tvterminalapp_jni_DlnaInterface_browseRecVideoDms(JNIEnv *env, jobject instance,
                                                                    jlong thiz, jstring ctl_) {
        if (NULL == ctl_) {
            return JNI_FALSE;
        }
        const du_uchar *udn = (const du_uchar *) env->GetStringUTFChars(ctl_, 0);

        Dlna *dlnaPtr = (Dlna *) thiz;
        if (NULL == dlnaPtr || NULL == udn) {
            env->ReleaseStringUTFChars(ctl_, (const char *) udn);
            return JNI_FALSE;
        }

        bool ret = dlnaPtr->browseDms(udn);
        env->ReleaseStringUTFChars(ctl_, (const char *) udn);

        return ret;
    }

} //end of namespace dtvt
