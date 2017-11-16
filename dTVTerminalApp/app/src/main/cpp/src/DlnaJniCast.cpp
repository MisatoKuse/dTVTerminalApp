/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <jni.h>
#include <string>
#include <dupnp.h>

//to do: 2017/11/17更新
//コンパイルできるように、一時コメントアウトした。
//#include "Dlna.h"

/**
 * 機能： JNI cast
 */
namespace dtvt {

    /**
     * DLNA objectを作成
     */
    extern "C" jlong
    Java_test_jni_jni_DlnaInterface_nativeCreateDlnaObject(JNIEnv *env, jobject obj) {

        jlong result;
        //to do: 2017/11/17更新
        //コンパイルできるように、一時コメントアウトした。
        //result = (jlong) new Dlna();
        return result;
    }

    /**
     * DLNAを開始
     */
    extern "C" jboolean JNICALL
    Java_test_jni_jni_DlnaInterface_nativeStartDlna(JNIEnv *env, jobject obj, jlong thiz) {
        unsigned char ret = 0;
        //to do: 2017/11/17更新
        //コンパイルできるように、一時コメントアウトした。
//        Dlna *dlnaPtr = (Dlna *) thiz;
//        if (NULL == dlnaPtr) {
//            return (jboolean) ret;
//        }
//
//        bool ret2 = dlnaPtr->start(env, obj);
//        ret = (true == ret2 ? 1 : 0);

        return (jboolean) ret;
    }

    /**
     * DLNAを停止
     */
    extern "C" void JNICALL
    Java_test_jni_jni_DlnaInterface_nativeStopDlna(JNIEnv *env, jobject obj, jlong thiz) {
        //to do: 2017/11/17更新
        //コンパイルできるように、一時コメントアウトした。
//        Dlna *dlnaPtr = (Dlna *) thiz;
//        if (NULL == dlnaPtr) {
//            return;
//        }
//
//        dlnaPtr->stop();
    }

    /**
     * コンテンツ一覧を取得
     */
    extern "C" jboolean JNICALL
    Java_test_jni_jni_DlnaInterface_browseDms__JLjava_lang_String_2(JNIEnv *env, jobject instance,
                                                                    jlong thiz, jstring ctl_) {
        //to do: 2017/11/17更新
        //コンパイルできるように、一時コメントアウトした。
//        if (NULL == ctl_) {
//            return NULL;
//        }
//        const du_uchar *udn = (const du_uchar *) env->GetStringUTFChars(ctl_, 0);
//
//        Dlna *dlnaPtr = (Dlna *) thiz;
//        if (NULL == dlnaPtr || NULL == udn) {
//            env->ReleaseStringUTFChars(ctl_, (const char *) udn);
//            return NULL;
//        }
//
//        bool ret = dlnaPtr->browseDms(udn);
//        env->ReleaseStringUTFChars(ctl_, (const char *) udn);
//
//        return ret;
        return true;
    }

} //end of namespace dtvt
