

#include <string.h>
#include <inttypes.h>
#include <pthread.h>
#include <jni.h>
#include <android/log.h>
#include <assert.h>
#include <secure_io_global.h>
#include <cipher_file_context_global.h>
#include "DlnaMacro.h"
#include "DlnaBase.h"
#include "DlnaDmsBrowse.h"
#include "DlnaRemoteConnect.h"

#define PackageName "com/nttdocomo/android/tvterminalapp/jni/"
#define DlnaManagerClassName "DlnaManager"

// processing callback to handler class
typedef struct MyContext {
    JavaVM *javaVM;
    jclass jniHelperClz;
    jobject jniHelperObj;
} MyContext;

//for java
MyContext g_ctx;

//for c/cpp
DlnaBase *dlnaBase;
DlnaDmsBrowse *dlnaDmsBrowse;
DlnaRemoteConnect *dlnaRemoteConnect;
DMP *dmp;

extern "C" {

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOG_WITH("");
    JNIEnv *env;
    memset(&g_ctx, 0, sizeof(g_ctx));

    g_ctx.javaVM = vm;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR; // JNI version not supported.
    }

    jclass clz = env->FindClass(PackageName DlnaManagerClassName);
    g_ctx.jniHelperClz = (jclass) env->NewGlobalRef(clz);

    jmethodID jniHelperCtor = env->GetMethodID(g_ctx.jniHelperClz, "<init>", "()V");
    jobject handler = env->NewObject(g_ctx.jniHelperClz, jniHelperCtor);
    g_ctx.jniHelperObj = env->NewGlobalRef(handler);

    return JNI_VERSION_1_6;
}

// region call from java
JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_secureIoGlobalCreate(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    secure_io_global_create();
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_cipherFileContextGlobalCreate(JNIEnv *env, jobject thiz, jstring path) {
    LOG_WITH("");
    const char *pathString = env->GetStringUTFChars(path, 0);
    cipher_file_context_global_create(secure_io_global_get_instance(), DU_UCHAR(pathString));
}

//private native void cipherFileContextGlobalCreate(String privateDataPath);
JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_initDmp(JNIEnv *env, jobject thiz, jstring path) {
    LOG_WITH("");

    dmp = new DMP();
    dlnaBase = new DlnaBase();
    dlnaDmsBrowse = new DlnaDmsBrowse();
    dlnaRemoteConnect = new DlnaRemoteConnect();
    dlnaBase->DmsFoundCallback = [](const char* friendlyName, const char* udn, const char* location, const char* controlUrl, const char* eventSubscriptionUrl) {
        LOG_WITH("friendlyName = %s", friendlyName);
        JNIEnv *_env = NULL;
        int status = g_ctx.javaVM->GetEnv((void **) &_env, JNI_VERSION_1_6);
        bool isAttached = false;
        if (status < 0) {
            status = g_ctx.javaVM->AttachCurrentThread(&_env, NULL);
            if (status < 0 || NULL == _env) {
                return;
            }
            isAttached = true;
        }
        LOG_WITH_BOOL(isAttached, "isAttached");

        jstring nameString = _env->NewStringUTF(friendlyName);
        jstring udnString = _env->NewStringUTF(udn);
        jstring locationString = _env->NewStringUTF(location);
        jstring controlUrlString = _env->NewStringUTF(controlUrl);
        jstring eventSubscriptionString = _env->NewStringUTF(eventSubscriptionUrl);
//        jstring modelNameString = _env->NewStringUTF(friendlyName);
//        jstring manufacturerString = _env->NewStringUTF(friendlyName);

        jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "DmsFoundCallback", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");

        _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, nameString, udnString, locationString, controlUrlString, eventSubscriptionString);
        _env->DeleteLocalRef(nameString);
        _env->DeleteLocalRef(udnString);
        _env->DeleteLocalRef(locationString);
        _env->DeleteLocalRef(controlUrlString);
        _env->DeleteLocalRef(eventSubscriptionString);

        if (isAttached) {
            g_ctx.javaVM->DetachCurrentThread();
        }
    };
    dlnaBase->DmsLeaveCallback = [](const char* udn) {
        JNIEnv *_env = NULL;
        int status = g_ctx.javaVM->GetEnv((void **) &_env, JNI_VERSION_1_6);
        bool isAttached = false;
        if (status < 0) {
            status = g_ctx.javaVM->AttachCurrentThread(&_env, NULL);
            if (status < 0 || NULL == _env) {
                return;
            }
            isAttached = true;
        }

        jstring udnString = _env->NewStringUTF(udn);
        jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "DmsLeaveCallback", "(Ljava/lang/String;)V");
        _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, udnString);
        _env->DeleteLocalRef(udnString);

        if (isAttached) {
            g_ctx.javaVM->DetachCurrentThread();
        }
    };

    dlnaRemoteConnect->LocalRegistrationCallback = [](bool result, eLocalRegistrationResultType resultType) {
        JNIEnv *_env = NULL;
        int status = g_ctx.javaVM->GetEnv((void **) &_env, JNI_VERSION_1_6);
        bool isAttached = false;
        if (status < 0) {
            status = g_ctx.javaVM->AttachCurrentThread(&_env, NULL);
            if (status < 0 || NULL == _env) {
                return;
            }
            isAttached = true;
        }

        jint resultTypeNumber = 0;
        if (resultType == LocalRegistrationResultTypeRegistrationOverError) {
            resultTypeNumber = 1;
        }
        jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "RegistResultCallBack", "(ZI)V");
        _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, result, resultTypeNumber);

        if (isAttached) {
            g_ctx.javaVM->DetachCurrentThread();
        }
    };
    dlnaRemoteConnect->DdtcpSinkAkeEndCallback = [](ddtcp_ret ddtcpSinkAkeEndRet) {
        JNIEnv *_env = NULL;
        int status = g_ctx.javaVM->GetEnv((void **) &_env, JNI_VERSION_1_6);
        bool isAttached = false;
        if (status < 0) {
            status = g_ctx.javaVM->AttachCurrentThread(&_env, NULL);
            if (status < 0 || NULL == _env) {
                return;
            }
            isAttached = true;
        }

        switch(ddtcpSinkAkeEndRet) {
            case DDTCP_RET_SUCCESS:
                break;
            default:
                jint resultTypeNumber = 2;
                jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "RegistResultCallBack", "(ZI)V");
                _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, false, resultTypeNumber);
                break;
        }

        if (isAttached) {
            g_ctx.javaVM->DetachCurrentThread();
        }
    };
    dlnaRemoteConnect->DiragConnectStatusChangeCallback = [](eDiragConnectStatus connectStatus) {
        JNIEnv *_env = NULL;
        int status = g_ctx.javaVM->GetEnv((void **) &_env, JNI_VERSION_1_6);
        bool isAttached = false;
        if (status < 0) {
            status = g_ctx.javaVM->AttachCurrentThread(&_env, NULL);
            if (status < 0 || NULL == _env) {
                return;
            }
            isAttached = true;
        }

        jint connectStatusType = 0;
        switch(connectStatus) {
            case DiragConnectStatusUnknown:
                connectStatusType = 2;
                break;
            case DiragConnectStatusReady:
                connectStatusType = 3;
                break;
            case DiragConnectStatusConnected:
                connectStatusType = 4;
                break;
            case DiragConnectStatusDetectedDisconnection:
                connectStatusType = 5;
                break;
            case DiragConnectStatusGaveupReconnection:
                connectStatusType = 6;
                break;
            default:
                break;
        }
        jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "RemoteConnectStatusCallBack", "(I)V");
        _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, connectStatusType);

        if (isAttached) {
            g_ctx.javaVM->DetachCurrentThread();
        }
    };
    dlnaDmsBrowse->ContentBrowseCallback = [](std::vector<ContentInfo> contentList, const char* containerId) {
        for (auto item : contentList) {
            LOG_WITH("item.name = %s ", item.name);
        }
//            jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "ContentBrowseCallback", "(contentList;Ljava/lang/String;)V");
//            _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, nameString, udnString, locationString, controlUrlString, eventSubscriptionString);
//            _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, contentList, containerId);
    };

    bool resultDmp = dlnaBase->initDmp(dmp);
    LOG_WITH_BOOL(resultDmp, "resultDmp");

    const char *pathString = env->GetStringUTFChars(path, 0);
    char buffer[256];
    snprintf(buffer, sizeof(buffer), "%s/%s", pathString, "capability.xml");
    bool resultDavCapability = dlnaBase->initDavCapability(dmp, buffer);
    LOG_WITH_BOOL(resultDavCapability, "resultDavCapability");
    LOG_WITH("pathString %s ", pathString);
    bool resultDtcp = dlnaBase->initDtcp(dmp, pathString);
    LOG_WITH_BOOL(resultDtcp, "resultDtcp");

}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_initDirag(JNIEnv *env, jobject thiz, jstring path) {
    const char *diragPath = env->GetStringUTFChars(path, 0);
    LOG_WITH("diragPath = %s", diragPath);
    dlnaBase->initDirag(diragPath);
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_startDmp(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    auto result = dlnaBase->startDmp(dmp);
    LOG_WITH_BOOL(result, "startDmp result");
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_stopDmp(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    dlnaBase->stopDmp(dmp);
}
//connectDmsWithUdn
JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_connectDmsWithUdn(JNIEnv *env, jobject thiz, jstring udn) {
    LOG_WITH("");
    const char *udnString = env->GetStringUTFChars(udn, 0);
    dlnaDmsBrowse->connectDmsWithUdn(dmp, DU_UCHAR(udnString));
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_browseContentWithContainerId(JNIEnv *env, jobject thiz, jint offset, jint limit, jstring containerId) {
    LOG_WITH("");
    const char *containerIdString = env->GetStringUTFChars(containerId, 0);
    dlnaDmsBrowse->selectContainerWithContainerId(dmp, offset, limit, DU_UCHAR(containerIdString));
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_startDtcp(JNIEnv *env, jobject thiz) {
    LOG_WITH("");

    jclass clazz = env->GetObjectClass(thiz);
    jmethodID mid = env->GetMethodID(clazz, "getUniqueId", "()Ljava/lang/String;");
    dlnaBase->startDtcp(dmp, g_ctx.javaVM, thiz, mid);

}
JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_restartDirag(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    dlnaRemoteConnect->restartDirag(dmp);
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_stopDirag(JNIEnv *env, jobject thiz) {
LOG_WITH("");
    dlnaRemoteConnect->stopDirag();
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_requestLocalRegistration(JNIEnv *env, jobject thiz, jstring udn, jstring deviceName) {
    const char *udnString = env->GetStringUTFChars(udn, 0);
    const char *deviceNameString = env->GetStringUTFChars(deviceName, 0);
    LOG_WITH("udnString = %s", udnString);
    dlnaRemoteConnect->requestLocalRegistration(dmp, DU_UCHAR(udnString), DU_UCHAR(deviceNameString));
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_requestRemoteConnect(JNIEnv *env, jobject thiz, jstring udn) {
    const char *udnString = env->GetStringUTFChars(udn, 0);
    LOG_WITH("udnString = %s", udnString);
    dlnaRemoteConnect->connectRemote(DU_UCHAR(udnString));
}

JNIEXPORT jstring JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_getRemoteDeviceExpireDate(JNIEnv *env, jobject thiz, jstring udn) {
    const char *udnString = env->GetStringUTFChars(udn, 0);
    LOG_WITH("udnString = %s", udnString);
    auto expireDate = dlnaRemoteConnect->getRemoteDeviceExpireDate(DU_UCHAR(udnString));
    LOG_WITH("expireDate = %s", expireDate);
    char buf[64];
    strncpy(buf, expireDate, sizeof(buf));
    jstring jstrBuf = env->NewStringUTF(buf);
    return jstrBuf;
}

// endregion call from java

}
