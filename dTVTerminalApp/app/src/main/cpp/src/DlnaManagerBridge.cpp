

#include <string.h>
#include <inttypes.h>
#include <pthread.h>
#include <jni.h>
#include <android/log.h>
#include <assert.h>
#include <secure_io_global.h>
#include <du_signal.h>
#include <cipher_file_context_global.h>
#include "DlnaMacro.h"
#include "DlnaBase.h"
#include "DlnaDmsBrowse.h"
#include "DlnaRemoteConnect.h"
#include "DlnaDownload.h"
#include "download/downloader.h"
#include "LocalRegistration/local_registration.h"

#define PackageName "com/nttdocomo/android/tvterminalapp/jni/"
#define DlnaManagerClassName "DlnaManager"
#define DlnaObjectClassName "DlnaObject"

// processing callback to handler class
typedef struct MyContext {
    JavaVM *javaVM;
    jclass jniHelperClz;
    jobject jniHelperObj;

} MyContext;
/**
 * 共通モデルクラスのidとfieldIdを格納
 */
struct JniStruct{
    jclass cls;
    jmethodID constructorId;

    jfieldID objectId;
    jfieldID xml;
    jfieldID date;
    jfieldID cleartextSize;
    jfieldID title;
    jfieldID bitrate;
    jfieldID channelName;
    jfieldID channelNr;
    jfieldID rating;
    jfieldID duration;
    jfieldID resUrl;
    jfieldID size;
    jfieldID videoType;
};


//for java
MyContext g_ctx;
JniStruct jniModelStruct;

//for c/cpp
DlnaBase *dlnaBase;
DlnaDmsBrowse *dlnaDmsBrowse;
DlnaRemoteConnect *dlnaRemoteConnect;
DlnaDownload *dlnaDownload;
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
    //TODO:sharedでもらわないとアクセスが1回増える
    jmethodID jniHelperCtor = env->GetMethodID(g_ctx.jniHelperClz, "<init>", "()V");
    jobject handler = env->NewObject(g_ctx.jniHelperClz, jniHelperCtor);
    g_ctx.jniHelperObj = env->NewGlobalRef(handler);

    jclass modelClz = env->FindClass(PackageName DlnaObjectClassName);
    jniModelStruct.cls = (jclass) env->NewGlobalRef(modelClz);
    jniModelStruct.constructorId = env->GetMethodID(jniModelStruct.cls, "<init>", "()V");

    jniModelStruct.objectId = env->GetFieldID(jniModelStruct.cls, "mObjectId", "Ljava/lang/String;");
    jniModelStruct.xml = env->GetFieldID(jniModelStruct.cls, "mXml", "Ljava/lang/String;");
    jniModelStruct.date = env->GetFieldID(jniModelStruct.cls, "mDate", "Ljava/lang/String;");
    jniModelStruct.cleartextSize = env->GetFieldID(jniModelStruct.cls, "mCleartextSize", "Ljava/lang/String;");
    jniModelStruct.title = env->GetFieldID(jniModelStruct.cls, "mTitle", "Ljava/lang/String;");
    jniModelStruct.bitrate = env->GetFieldID(jniModelStruct.cls, "mBitrate", "Ljava/lang/String;");
    jniModelStruct.channelName = env->GetFieldID(jniModelStruct.cls, "mChannelName", "Ljava/lang/String;");
    jniModelStruct.channelNr = env->GetFieldID(jniModelStruct.cls, "mChannelNr", "Ljava/lang/String;");
    jniModelStruct.rating = env->GetFieldID(jniModelStruct.cls, "mRating", "Ljava/lang/String;");
    jniModelStruct.duration = env->GetFieldID(jniModelStruct.cls, "mDuration", "Ljava/lang/String;");
    jniModelStruct.resUrl = env->GetFieldID(jniModelStruct.cls, "mResUrl", "Ljava/lang/String;");
    jniModelStruct.size = env->GetFieldID(jniModelStruct.cls, "mSize", "Ljava/lang/String;");
    jniModelStruct.videoType = env->GetFieldID(jniModelStruct.cls, "mVideoType", "Ljava/lang/String;");

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
    if (NULL==path) {
        LOG_WITH("cipherFileContextGlobalCreate path is NULL");
        return;
    }
    const char *pathString = env->GetStringUTFChars(path, 0);
    cipher_file_context_global_create(secure_io_global_get_instance(), DU_UCHAR(pathString));
}

void fillContentInfoIntoJni(JNIEnv *env, const ContentInfo *src, jobject &dst) {
    jstring objectIdString = env->NewStringUTF(src->objectId);
    env->SetObjectField(dst, jniModelStruct.objectId, objectIdString);
    env->DeleteLocalRef(objectIdString);

    jstring cleartextSizeString = env->NewStringUTF(src->cleartextSize);
    env->SetObjectField(dst, jniModelStruct.cleartextSize, cleartextSizeString);
    env->DeleteLocalRef(cleartextSizeString);

    jstring dateString = env->NewStringUTF(src->date);
    env->SetObjectField(dst, jniModelStruct.date, dateString);
    env->DeleteLocalRef(dateString);

    jstring xmlString = env->NewStringUTF(src->xml);
    env->SetObjectField(dst, jniModelStruct.xml, xmlString);
    env->DeleteLocalRef(xmlString);

    jstring nameString = env->NewStringUTF(src->name);
    env->SetObjectField(dst, jniModelStruct.title, nameString);
    env->DeleteLocalRef(nameString);

    jstring channelNameString = env->NewStringUTF(src->channelName);
    env->SetObjectField(dst, jniModelStruct.channelName, channelNameString);
    env->DeleteLocalRef(channelNameString);

    jstring channelNrString = env->NewStringUTF(src->channelNr);
    env->SetObjectField(dst, jniModelStruct.channelNr, channelNrString);
    env->DeleteLocalRef(channelNrString);

    jstring ratingString = env->NewStringUTF(src->rating);
    env->SetObjectField(dst, jniModelStruct.rating, ratingString);
    env->DeleteLocalRef(ratingString);

    jstring durationString = env->NewStringUTF(src->duration);
    env->SetObjectField(dst, jniModelStruct.duration, durationString);
    env->DeleteLocalRef(durationString);

    jstring resUrlString = env->NewStringUTF(src->contentPath);
    env->SetObjectField(dst, jniModelStruct.resUrl, resUrlString);
    env->DeleteLocalRef(resUrlString);

    jstring sizeString = env->NewStringUTF(src->size);
    env->SetObjectField(dst, jniModelStruct.size, sizeString);
    env->DeleteLocalRef(sizeString);

    jstring videoTypeString = env->NewStringUTF(src->protocolInfo);
    env->SetObjectField(dst, jniModelStruct.videoType, videoTypeString);
    env->DeleteLocalRef(videoTypeString);

    jstring bitrateString = env->NewStringUTF(src->bitrate);
    env->SetObjectField(dst, jniModelStruct.bitrate, bitrateString);
    env->DeleteLocalRef(bitrateString);
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_initDmp(JNIEnv *env, jobject thiz, jstring path) {
    LOG_WITH("");
    if (NULL==path) {
        LOG_WITH("initDmp path is NULL");
        return;
    }
    dmp = new DMP();
    dlnaBase = new DlnaBase();
    dlnaDmsBrowse = new DlnaDmsBrowse();
    dlnaRemoteConnect = new DlnaRemoteConnect();
    dlnaDownload = new DlnaDownload();

    //region dlnaBase callback
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
    //endregion dlnaBase callback

    //region dlnaDmsBrowse callback
    dlnaDmsBrowse->ContentBrowseCallback = [](std::vector<ContentInfo> contentList, const char* containerId, bool complete) {
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

        jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "ContentBrowseCallback", "(Ljava/lang/String;[Lcom/nttdocomo/android/tvterminalapp/jni/DlnaObject;Z)V");
        jobjectArray returnArray = _env->NewObjectArray((jsize)contentList.size(), jniModelStruct.cls, nullptr);
        for (size_t i = 0 ; i < contentList.size() ; ++i) {
            auto *item = &contentList.at(i);
            jobject jobj = _env->NewObject(jniModelStruct.cls, jniModelStruct.constructorId);
            fillContentInfoIntoJni(_env, item, jobj);
            _env->SetObjectArrayElement(returnArray, i, jobj);
            _env->DeleteLocalRef(jobj);
        }
        jstring containerIdString = _env->NewStringUTF(containerId);
        _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, containerIdString, returnArray, complete);
        _env->DeleteLocalRef(returnArray);
        _env->DeleteLocalRef(containerIdString);

        if (isAttached) {
            g_ctx.javaVM->DetachCurrentThread();
        }
    };
    //endregion dlnaDmsBrowse callback

    //region dlnaDmsBrowse callback
    dlnaDmsBrowse->ContentBrowseErrorCallback = [](const char* containerId, eDlnaErrorType error) {
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
        LOG_WITH("ContentBrowseErrorCallback containerId = %s", containerId);
        LOG_WITH("before GetMethodID");
        jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "ContentBrowseErrorCallback", "(Ljava/lang/String;)V");
        jstring containerIdString = _env->NewStringUTF(containerId);
        _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, containerIdString);
        LOG_WITH("after CallVoidMethod");

        if (isAttached) {
            g_ctx.javaVM->DetachCurrentThread();
        }
    };
    //endregion dlnaDmsBrowse callback

    //region dlnaRemoteConnect callback
    dlnaRemoteConnect->LocalRegistrationCallback = [](bool result, int resultType, const du_uchar* errorCode) {
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

        jint resultTypeNumber = resultType;
        jstring errorCodeString = _env->NewStringUTF((const char*)errorCode);

        LOG_WITH("before GetMethodID");
        jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "RegistResultCallBack", "(ZILjava/lang/String;)V");
        _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, result, resultTypeNumber, errorCodeString);
        LOG_WITH("after CallVoidMethod");

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

        jint resultTypeNumber = LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_NONE;
        switch (ddtcpSinkAkeEndRet) {
            case DDTCP_RET_SUCCESS:  // 成功
                break;
            case DDTCP_RET_FAILURE_RA_SINK_NOT_REGISTERED:  // 超過台数エラー
                resultTypeNumber = LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_DEVICE_OVER;
                break;
            default:
                resultTypeNumber = LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_OTHER;
                break;
        }

        if (resultTypeNumber != LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_NONE) {
            jstring errorCodeString = _env->NewStringUTF((const char*)ddtcpSinkAkeEndRet);
            jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "RegistResultCallBack", "(ZILjava/lang/String;)V");
            _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, false, resultTypeNumber, errorCodeString);
        }
        if (isAttached) {
            g_ctx.javaVM->DetachCurrentThread();
        }
    };
    dlnaRemoteConnect->DiragConnectStatusChangeCallback = [](eDiragConnectStatus connectStatus, du_uint32 errorCode) {
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
        jint errorResult = errorCode;
        jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "RemoteConnectStatusCallBack", "(II)V");
        _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, connectStatusType, errorResult);

        if (isAttached) {
            g_ctx.javaVM->DetachCurrentThread();
        }
    };
    //endregion dlnaRemoteConnect callback
    dlnaDownload->DownloadProgressCallBack = [](int progress) {
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
        jint progressResult = progress;
        jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "DownloadProgressCallBack", "(I)V");
        _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, progressResult);

        if (isAttached) {
            g_ctx.javaVM->DetachCurrentThread();
        }
    };
    dlnaDownload->DownloadStatusCallBack = [](downloader_status downloadStatus) {
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
        jint downloadStatusType = 10;
        switch(downloadStatus) {
            case DOWNLOADER_STATUS_UNKNOWN:
                downloadStatusType = 11;
                break;
            case DOWNLOADER_STATUS_MOVING:
                downloadStatusType = 12;
                break;
            case DOWNLOADER_STATUS_COMPLETED:
                downloadStatusType = 13;
                break;
            case DOWNLOADER_STATUS_CANCELLED:
                downloadStatusType = 14;
                break;
            case DOWNLOADER_STATUS_ERROR_OCCURED:
                downloadStatusType = 15;
                break;
            default:
                break;
        }
        jmethodID methodID = _env->GetMethodID(g_ctx.jniHelperClz, "DownloadStatusCallBack", "(I)V");
        _env->CallVoidMethod(g_ctx.jniHelperObj, methodID, downloadStatusType);

        if (isAttached) {
            g_ctx.javaVM->DetachCurrentThread();
        }
    };
    //region dlnadownload callback

    //endregion dlnadownload callback

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
    if (NULL==path) {
        LOG_WITH("initDirag path is NULL");
        return;
    }
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

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_freeDmp(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    dlnaBase->freeDmp(dmp);
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_resetdmp(JNIEnv *env, jobject thiz, jstring path) {
    if (NULL==path) {
        LOG_WITH("resetdmp path is NULL");
        return;
    }
    if (dmp == nullptr) {
        dmp = new DMP();
    }
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
//connectDmsWithUdn
JNIEXPORT jboolean JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_connectDmsWithUdn(JNIEnv *env, jobject thiz, jstring udn) {
    LOG_WITH("");
    if (NULL==udn) {
        LOG_WITH("connectDmsWithUdn udn is NULL");
        return 0;
    }
    const char *udnString = env->GetStringUTFChars(udn, 0);
    return (jboolean) dlnaDmsBrowse->connectDmsWithUdn(dmp, DU_UCHAR(udnString));
}

JNIEXPORT jboolean JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_browseContentWithContainerId(JNIEnv *env, jobject thiz, jint offset, jint limit, jstring containerId, jstring controlUrl) {
    LOG_WITH("");
    const char *containerIdString = env->GetStringUTFChars(containerId, 0);
    if (NULL == controlUrl) {
        return (jboolean) dlnaDmsBrowse->selectContainerWithContainerId(dmp, (du_uint32)offset, (du_uint32)limit, DU_UCHAR(containerIdString), nullptr);
    } else {
        const char *controlUrlString = env->GetStringUTFChars(controlUrl, 0);
        return (jboolean) dlnaDmsBrowse->selectContainerWithContainerId(dmp, (du_uint32)offset, (du_uint32)limit, DU_UCHAR(containerIdString), DU_UCHAR(controlUrlString));
    }
}

JNIEXPORT ddtcp_ret JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_startDtcp(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    jclass clazz = env->GetObjectClass(thiz);
    jmethodID mid = env->GetMethodID(clazz, "getUniqueId", "()Ljava/lang/String;");
    ddtcp_ret result = dlnaBase->startDtcp(dmp, g_ctx.javaVM, thiz, mid);
    return result;
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_stopDtcp(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    dlnaBase->stopDtcp(dmp);
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_startDirag(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    dlnaRemoteConnect->startDirag(dmp);
}

JNIEXPORT jboolean JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_restartDirag(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    bool result = dlnaRemoteConnect->restartDirag(dmp);
    return (jboolean) result;
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_stopDirag(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    dlnaRemoteConnect->stopDirag();
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_requestLocalRegistration(JNIEnv *env, jobject thiz, jstring udn, jstring deviceName) {
    if (NULL==udn) {
        LOG_WITH("requestLocalRegistration udn is NULL");
        if (DlnaRemoteConnect::LocalRegistrationCallback != nullptr) {
            DlnaRemoteConnect::LocalRegistrationCallback(false, LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_REQUEST, DU_UCHAR_CONST(LOCAL_REGISTRATION_REQUEST_ERROR_UDN_NULL));
        }
        return;
    }
    if (NULL==deviceName) {
        LOG_WITH("requestLocalRegistration deviceName is NULL");
        if (DlnaRemoteConnect::LocalRegistrationCallback != nullptr) {
            DlnaRemoteConnect::LocalRegistrationCallback(false, LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_REQUEST, DU_UCHAR_CONST(LOCAL_REGISTRATION_REQUEST_ERROR_DEVICE_NAME_NULL));
        }
        return;
    }
    const char *udnString = env->GetStringUTFChars(udn, 0);
    const char *deviceNameString = env->GetStringUTFChars(deviceName, 0);
    LOG_WITH("udnString = %s", udnString);
    bool result = dlnaRemoteConnect->requestLocalRegistration(dmp, DU_UCHAR(udnString), DU_UCHAR(deviceNameString));
    if (!result) {
        if (DlnaRemoteConnect::LocalRegistrationCallback != nullptr) {
            DlnaRemoteConnect::LocalRegistrationCallback(false, LOCAL_REGISTRATION_CALLBACK_ERROR_TYPE_REQUEST, DU_UCHAR_CONST(LOCAL_REGISTRATION_ERROR_OTHER));
        }
    }
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_requestRemoteConnect(JNIEnv *env, jobject thiz, jstring udn) {
    if (NULL==udn) {
        LOG_WITH("requestRemoteConnect udn is NULL");
        return;
    }
    const char *udnString = env->GetStringUTFChars(udn, 0);
    LOG_WITH("udnString = %s", udnString);
    dlnaRemoteConnect->connectRemote(DU_UCHAR(udnString));
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_requestRemoteDisconnect(JNIEnv *env, jobject thiz, jstring udn) {
    if (NULL==udn) {
        LOG_WITH("requestRemoteDisconnect udn is NULL");
        return;
    }
    const char *udnString = env->GetStringUTFChars(udn, 0);
    LOG_WITH("udnString = %s", udnString);
    dlnaRemoteConnect->disconnectRemote(DU_UCHAR(udnString));
}

JNIEXPORT jstring JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_getRemoteDeviceExpireDate(JNIEnv *env, jobject thiz, jstring udn) {
    if (NULL==udn) {
        LOG_WITH("getRemoteDeviceExpireDate udn is NULL");
        return NULL;
    }
    const char *udnString = env->GetStringUTFChars(udn, 0);
    LOG_WITH("udnString = %s", udnString);
    char *expireDate = new char[64];
    dlnaRemoteConnect->getRemoteDeviceExpireDate(DU_UCHAR(udnString), expireDate, 64);
    LOG_WITH("expireDate = %s", expireDate);
    jstring jstrBuf = nullptr;
    char buf[64];
    strncpy(buf, expireDate, sizeof(buf));
    jstrBuf = env->NewStringUTF(buf);
    delete[](expireDate);
    return jstrBuf;
}

JNIEXPORT jboolean JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_downLoadStartDtcp(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    jclass clazz = env->GetObjectClass(thiz);
    jmethodID mid = env->GetMethodID(clazz, "getUniqueId", "()Ljava/lang/String;");
    bool result = dlnaDownload->start(dmp, g_ctx.javaVM, thiz, mid);
    return (jboolean) result;
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_download(JNIEnv *env, jobject thiz, jstring fileNameToSave_, jstring dtcp1host_, int dtcp1port, jstring url_, jlong cleartextSize, jstring xml_) {
    if (NULL==fileNameToSave_) {
        LOG_WITH("download fileNameToSave_ is NULL");
        dlnaDownload->startDownload("", "", dtcp1port, "", cleartextSize, nullptr);
        return;
    }
    if (NULL==dtcp1host_) {
        LOG_WITH("download dtcp1host_ is NULL");
        dlnaDownload->startDownload("", "", dtcp1port, "", cleartextSize, nullptr);
        return;
    }
    if (NULL==url_) {
        LOG_WITH("download url_ is NULL");
        dlnaDownload->startDownload("", "", dtcp1port, "", cleartextSize, nullptr);
        return;
    }
    if (NULL==xml_) {
        LOG_WITH("download xml_ is NULL");
        dlnaDownload->startDownload("", "", dtcp1port, "", cleartextSize, nullptr);
        return;
    }
    const char *fileNameToSave = env->GetStringUTFChars(fileNameToSave_, 0);
    const char *dtcp1host = env->GetStringUTFChars(dtcp1host_, 0);
    const char *url = env->GetStringUTFChars(url_, 0);
    const char *xml = env->GetStringUTFChars(xml_, 0);
    dlnaDownload->startDownload(fileNameToSave, dtcp1host, dtcp1port, url, cleartextSize, xml);
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_downloadCancel(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    dlnaDownload->downloadCancel();
}

JNIEXPORT void JNICALL
Java_com_nttdocomo_android_tvterminalapp_jni_DlnaManager_downloadStop(JNIEnv *env, jobject thiz) {
    LOG_WITH("");
    dlnaDownload->stop();
}
// endregion call from java

}
