/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#include <du_http_client.h>
#include <dupnp_cp.h>
#include <dav_urn.h>
#include <du_str.h>
#include <du_byte.h>
#include <dav_cds.h>
#include <dupnp_soap.h>
#include <cstring>
#include "Dlna.h"
#include "DmsInfo.h"
#include "DlnaBSDigitalXmlParser.h"
#include "DlnaTerChXmlParser.h"
#include "DlnaHikariChXmlParser.h"

namespace dtvt {

    Dlna::Dlna(): mDLNA_STATE(DLNA_STATE_STOP), mDlnaDevXmlParser(NULL), mRecursionXmlParser(NULL), mDlnaRecVideoXmlParser(NULL), mBsDigitalXmlParser(NULL), mTerChXmlParser(NULL), mHikariChXmlParser(NULL){
        mDMP.upnp._impl = NULL;
    }

    bool Dlna::init() {
        du_byte_zero((du_uint8 *) &mDMP, sizeof(dmp));
        du_bool isInitOk = dupnp_init(&mDMP.upnp, 0, 0);
        bool ok = (true == isInitOk);
        mDlnaDevXmlParser = (DlnaXmlParserBase*)new DlnaDevXmlParser();
        IfNullGoTo(mDlnaDevXmlParser, error_1);
        mDlnaRecVideoXmlParser = (DlnaXmlParserBase*)new DlnaRecVideoXmlParser();
        IfNullGoTo(mDlnaRecVideoXmlParser, error_2);
        return ok;

        error_2:
        delete mDlnaDevXmlParser;
        mDlnaDevXmlParser = NULL;
        error_1:
        return false;
    }

    void Dlna::uninit() {
        soapUninit();

        if (NULL != mDMP.upnp._impl) {
            dupnp_free(&mDMP.upnp);
        }
        mDMP.upnp._impl = NULL;
        mRecursionXmlParser=NULL;

        DelIfNotNull(mDlnaDevXmlParser);
        DelIfNotNull(mDlnaRecVideoXmlParser);
        DelIfNotNull(mBsDigitalXmlParser);
        DelIfNotNull(mTerChXmlParser);
        DelIfNotNull(mHikariChXmlParser);
    }

    bool Dlna::start(JNIEnv *env, jobject obj) {
        du_bool isStartOk = false;

        jclass tmpDMSItem=NULL;
        jclass tmpDlnaRecVideoItem=NULL;
        jclass tmpDlnaBsChListItem=NULL;
        jclass tmpDlnaTerChListItem=NULL;
        jclass tmpDlnaHikariChListItem=NULL;

        jint ret = 0;
        bool ok = false;

        if (NULL == env || NULL == obj) {
            return false;
        }
        if (DLNA_STATE_STARTED == mDLNA_STATE) {
            return true;
        }

        //0. init mEvent
        ret = env->GetJavaVM(&mEvent.mJavaVM);
        if (0 != ret || NULL == mEvent.mJavaVM) {
            goto error_1;
        }
        mEvent.mJClassDlna = env->FindClass("com/nttdocomo/android/tvterminalapp/jni/DlnaInterface");
        if (NULL == mEvent.mJClassDlna) {
            goto error_2;
        }

        tmpDMSItem = env->FindClass("com/nttdocomo/android/tvterminalapp/jni/DlnaDmsItem");
        mEvent.mJClassDmsItem = (jclass)env->NewGlobalRef(tmpDMSItem);
        if (NULL == mEvent.mJClassDmsItem) {
            goto error_2;
        }

        tmpDlnaRecVideoItem = env->FindClass("com/nttdocomo/android/tvterminalapp/jni/DlnaRecVideoItem");
        mEvent.mJClassRecVideoItem = (jclass)env->NewGlobalRef(tmpDlnaRecVideoItem);
        if (NULL == mEvent.mJClassRecVideoItem) {
            goto error_2;
        }

        tmpDlnaBsChListItem = env->FindClass("com/nttdocomo/android/tvterminalapp/jni/DlnaBsChListItem");
        mEvent.mJClassBsChListItem = (jclass)env->NewGlobalRef(tmpDlnaBsChListItem);
        if (NULL == mEvent.mJClassBsChListItem) {
            goto error_2;
        }

        tmpDlnaTerChListItem = env->FindClass("com/nttdocomo/android/tvterminalapp/jni/DlnaTerChListItem");
        mEvent.mJClassTerChListItem = (jclass)env->NewGlobalRef(tmpDlnaTerChListItem);
        if (NULL == mEvent.mJClassTerChListItem) {
            goto error_2;
        }

        tmpDlnaHikariChListItem = env->FindClass("com/nttdocomo/android/tvterminalapp/jni/DlnaHikariChListItem");
        mEvent.mJClassHikariChListItem = (jclass)env->NewGlobalRef(tmpDlnaHikariChListItem);
        if (NULL == mEvent.mJClassHikariChListItem) {
            goto error_2;
        }

        mEvent.mJmethodID = env->GetMethodID(mEvent.mJClassDlna, "notifyFromNative",
                                             "(ILjava/lang/String;)V");
        if (NULL == mEvent.mJmethodID) {
            goto error_2;
        }
        mEvent.mJObject = env->NewGlobalRef(obj);

        //1. init
        ok = init();
        if (!ok) {
            goto error_2;
        }

        //2. enable functions
        ok = enableFunction();
        if (!ok) {
            goto error_3;
        }

        //3 start
        isStartOk = dupnp_start(&mDMP.upnp);
        ok = (true == isStartOk);
        if (ok) {
            mDLNA_STATE = DLNA_STATE_STARTED;
        } else {
            goto error_3;
        }

        ok = initDevEnv();
        if (!ok) {
            goto error_3;
        }

        return ok;

        error_3:
        mEvent.mJmethodID = NULL;
        uninit();
        error_2:
        if (env) {
            if (mEvent.mJClassDlna) {
                env->DeleteLocalRef(mEvent.mJClassDlna);
                mEvent.mJClassDlna=NULL;
            }
            if (mEvent.mJObject) {
                env->DeleteGlobalRef(mEvent.mJObject);
                mEvent.mJObject=NULL;
            }
            if(mEvent.mJClassDmsItem){
                env->DeleteGlobalRef(mEvent.mJClassDmsItem);
                mEvent.mJClassDmsItem=NULL;
            }
            if(mEvent.mJClassRecVideoItem){
                env->DeleteGlobalRef(mEvent.mJClassRecVideoItem);
                mEvent.mJClassRecVideoItem=NULL;
            }
            if(mEvent.mJClassBsChListItem){
                env->DeleteGlobalRef(mEvent.mJClassBsChListItem);
                mEvent.mJClassBsChListItem=NULL;
            }
            if(mEvent.mJClassTerChListItem){
                env->DeleteGlobalRef(mEvent.mJClassTerChListItem);
                mEvent.mJClassTerChListItem=NULL;
            }
            if(mEvent.mJClassHikariChListItem){
                env->DeleteGlobalRef(mEvent.mJClassHikariChListItem);
                mEvent.mJClassHikariChListItem=NULL;
            }
        }
        mEvent.mJavaVM = NULL;
        mEvent.mJClassDlna = NULL;
        mEvent.mJObject = NULL;
        error_1:
        return false;
    }

    void Dlna::stop() {
        if (DLNA_STATE_STARTED != mDLNA_STATE) {
            return;
        }
        dupnp_stop(&mDMP.upnp);
        uninit();
        if (mEvent.mJavaVM) {
            JNIEnv *env = NULL;
            int status = mEvent.mJavaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
            if (status < 0) {
                status = mEvent.mJavaVM->AttachCurrentThread(&env, NULL);
                if (status < 0) {
                    env = NULL;
                }
            }

            if (env) {
                if (mEvent.mJObject) {
                    env->DeleteGlobalRef(mEvent.mJObject);
                }
            }
        }

        mEvent.mJObject = NULL;
        mEvent.mJavaVM = NULL;
        mEvent.mJmethodID = NULL;
        mEvent.mJClassDlna = NULL;

        mDLNA_STATE = DLNA_STATE_STOP;
    }

    bool Dlna::enableFunction() {
        du_bool ret = dupnp_enable_netif_monitor(&mDMP.upnp, 1);
        if (!ret) {
            return false;
        }

        ret = dupnp_cp_enable_ssdp_listener(&mDMP.upnp, 1);
        if (!ret) {
            return false;
        }

        ret = dupnp_cp_enable_ssdp_search(&mDMP.upnp, 1);
        if (!ret) {
            return false;
        }

        ret = dupnp_cp_enable_http_server(&mDMP.upnp, 1);
        if (!ret) {
            return false;
        }

        return true;
    }

    bool Dlna::initDevEnv() {
        bool ret = false;
        if (mDLNA_STATE != DLNA_STATE_STARTED) {
            return false;
        }

        // initialize device manager.
        if (!dupnp_cp_dvcmgr_init(&mDMP.deviceManager, &mDMP.upnp)) {
            goto error0;
        }

        // initialize event manager.
        if (!dupnp_cp_evtmgr_init(&mDMP.eventManager, &mDMP.upnp)) {
            goto error1;
        }

        // discover Media Server Devices which version is 1 or later.
        if (!dupnp_cp_dvcmgr_add_device_type(&mDMP.deviceManager, dav_urn_msd(1))) {
            goto error2;
        }
        if (!dupnp_cp_dvcmgr_add_device_type(&mDMP.deviceManager, dav_urn_msd(2))) {
            goto error2;
        }
        if (!dupnp_cp_dvcmgr_add_device_type(&mDMP.deviceManager, dav_urn_msd(3))) {
            goto error2;
        }

        // set a callback function which is called before the device information are stored in the device manager when new devices join to the network.
        dupnp_cp_dvcmgr_set_allow_join_handler(&mDMP.deviceManager, Dlna::allowJoinHandler, this);

        // set a callback function which is called after the device information are stored in the device manager when new devices join to the network.
        dupnp_cp_dvcmgr_set_join_handler(&mDMP.deviceManager, Dlna::joinHandler, this);

        // set a callback function which is called when devices leave from the network.
        dupnp_cp_dvcmgr_set_leave_handler(&mDMP.deviceManager, Dlna::leaveHandler, this);

        dupnp_cp_dvcmgr_set_user_agent(&mDMP.deviceManager,
                                       DU_UCHAR_CONST("UPnP/1.0 DLNADOC/1.50 DiXiM-SimpleDMP/1.0"));

        if (!soapInit()) {
            goto error2;
        }

        ret = startDmgrAndEmgr();
        if (!ret) {
            goto error2;
        }

        return true;

        error2:
        dupnp_cp_evtmgr_free(&mDMP.eventManager);
        error1:
        dupnp_cp_dvcmgr_free(&mDMP.deviceManager);
        error0:
        return false;
    }

    bool Dlna::soapInit() {

        du_byte_zero((du_uint8 *) &mDMP.soap, sizeof(soap));

        du_str_array_init(&mDMP.soap.request_header);
        if (!dupnp_soap_header_set_content_type(&mDMP.soap.request_header)) {
            goto error;
        }
        if (!dupnp_soap_header_set_user_agent(&mDMP.soap.request_header, DEFAULT_USER_AGENT)) {
            goto error;
        }

        if (!du_mutex_create(&mDMP.soap.mutex)) {
            goto error;
        }
        if (!du_sync_create(&mDMP.soap.sync)) {
            goto error2;
        }
        mDMP.soap.id = DUPNP_INVALID_ID;

        return true;

        error2:
        du_mutex_free(&mDMP.soap.mutex);
        error:
        du_str_array_free(&mDMP.soap.request_header);
        return false;
    }

    void Dlna::soapUninit() {
        du_str_array_free(&mDMP.soap.request_header);
        du_mutex_free(&mDMP.soap.mutex);
        du_sync_free(&mDMP.soap.sync);
    }

    bool Dlna::startDmgrAndEmgr() {
        if (!dupnp_cp_evtmgr_start(&mDMP.eventManager)) {
            goto error;
        }
        if (!dupnp_cp_dvcmgr_start(&mDMP.deviceManager)) {
            goto error2;
        }

        return true;

        error2:
        dupnp_cp_evtmgr_stop(&mDMP.eventManager);
        error:
        return false;
    }

    static du_bool checkSoapResponseError(dupnp_http_response *response) {
        du_str_array param_array;

        du_str_array_init(&param_array);

        // check cancel.
        if (response->error == DU_SOCKET_ERROR_CANCELED) {
            goto error;
        }

        // check general error.
        if (response->error != DU_SOCKET_ERROR_NONE) {
            goto error;
        }

        // check SOAP error.
        if (du_str_equal(response->status, du_http_status_internal_server_error())) {
            const du_uchar *soap_error_code;
            const du_uchar *soap_error_description;

            if (!dupnp_soap_parse_error_response(response->body, response->body_size, &param_array,
                                                 &soap_error_code, &soap_error_description)) {
                goto error;
            }
            goto error;
        }

        // check HTTP error.
        if (!du_http_status_is_successful(response->status)) {
            goto error;
        }

        du_str_array_free(&param_array);
        return true;

        error:
        du_str_array_free(&param_array);
        return false;
    }

    /*static*/ void  Dlna::browseDirectChildrenResponseHandler(dupnp_http_response *response, void *arg) {
        if (NULL == arg) {
            return;
        }

        DlnaXmlParserBase* parser=NULL;
        Dlna *thiz = (Dlna *) arg;
        IfNullReturn(thiz);
        IfNullReturn(thiz->mRecursionXmlParser);

        std::vector<std::vector<std::string> > vv;
        dmp *d = &thiz->mDMP;

        du_mutex_lock(&d->soap.mutex);
        d->soap.id = DUPNP_INVALID_ID;
        std::string containerId;
        std::string isContainerId = "0";

        if (!checkSoapResponseError(response)) {
            goto error;
        }

        parser = thiz->mRecursionXmlParser;
        parser->parse((void *) response, vv, containerId, isContainerId);
        #if defined(DLNA_KARI_DMS_UNIVERSAL)
            if(containerId.length() != 0){
                thiz->sendSoap((char*)response->url, containerId);
            } else {
                if(0==vv.size()){
                    du_sync_notify(&d->soap.sync);
                    du_mutex_unlock(&d->soap.mutex);
                    return;
                }

                thiz->notifyObject(parser->getMsgId(), vv);

                //du_sync_notify(&d->soap.sync);
            }
        #elif defined(DLNA_KARI_DMS_NAS)
            if(0==vv.size()){
                du_sync_notify(&d->soap.sync);
                du_mutex_unlock(&d->soap.mutex);
                return;
            }

            thiz->notifyObject(parser->getMsgId(), vv);
        #endif

        du_sync_notify(&d->soap.sync);
        du_mutex_unlock(&d->soap.mutex);
        return;

        error:
        du_sync_notify(&d->soap.sync);
        du_mutex_unlock(&d->soap.mutex);
    }

    //bool Dlna::sendSoap(std::string controlUrl, std::string objectId="0", const int startingIndex=0, const int requestCount=0, std::string browseFlag="BrowseDirectChildren"){
    bool Dlna::sendSoap(std::string controlUrl, std::string objectId, const int startingIndex, const int requestCount, std::string browseFlag){
        if (0 == controlUrl.length() || 0==browseFlag.length() || 0==objectId.length()) {
            return false;
        }
        int index=max(startingIndex, 0);
        int count=max(requestCount, 0);
        du_uchar_array request_body;
        du_uchar_array_init(&request_body);
        du_mutex_lock(&mDMP.soap.mutex);

        if (!dav_cds_make_browse(&request_body,
                                 1,
                                 (const du_uchar *) objectId.c_str(),
                                 (const du_uchar *) browseFlag.c_str(), //DU_UCHAR_CONST("BrowseDirectChildren") or DU_UCHAR_CONST("BrowseMetadata"),
                                 BDC_FILTER,
                                 index,
                                 count,
                                 DU_UCHAR_CONST(""))) {
            goto error_1;
        }

        if (!dupnp_soap_header_set_soapaction(&mDMP.soap.request_header, dav_urn_cds(1),
                                              DU_UCHAR_CONST("Browse"))) {
            goto error_1;
        }

        if (!dupnp_http_soap(&mDMP.upnp,
                             (const du_uchar *) controlUrl.c_str(),
                             &mDMP.soap.request_header,
                             du_uchar_array_get(&request_body),
                             du_uchar_array_length(&request_body),
                             READ_TIMEOUT_MS,
                             Dlna::browseDirectChildrenResponseHandler,
                             this,
                             &mDMP.soap.id)) {
            du_mutex_unlock(&mDMP.soap.mutex);
            return false;
        }

        du_mutex_unlock(&mDMP.soap.mutex);

        return true;

        error_1:
        du_mutex_unlock(&mDMP.soap.mutex);
        return false;
    }

    void Dlna::notify(int msg, std::string content) {
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
        mEvent.mJavaVM->DetachCurrentThread();
    }

    bool setJavaObjectField(JNIEnv *env, jclass cls, const char* const  fieldName, const char* const classPath, string& value, jobject obj){
        if(NULL==env || NULL == cls || NULL==fieldName
           || NULL==classPath || NULL==obj || NULL ==value.c_str()){
            return false;
        }

        jfieldID field = env->GetFieldID(cls, fieldName, classPath);
        if(NULL==field){
            return false;
        }

        jstring str = env->NewStringUTF(value.c_str());
        env->SetObjectField(obj, field, str);
        env->DeleteLocalRef(str);

        return true;
    }

    bool addDmsInfo(JNIEnv *env, jclass cl, jmethodID cons, StringVector& datas, jobject objOut) {
        StringVector::iterator i=datas.begin();

        //mUdn
        bool ret= setJavaObjectField(env, cl, DmsItem_Field_mUdn,Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //mControlUr
        ret= setJavaObjectField(env, cl, DmsItem_Field_mControlUrl, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //mHttp
        ret= setJavaObjectField(env, cl, DmsItem_Field_mHttp, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //mFriendlyName
        ret= setJavaObjectField(env, cl, DmsItem_Field_mFriendlyName, Dlna_Java_String_Path,  *i, objOut);
        if(!ret){
            return false;
        }

        return true;
    }

    bool addRecVideoItem(JNIEnv *env, jclass cl, jmethodID cons, StringVector& datas, jobject objOut, jobject listObj, jmethodID listAddId) {
        StringVector::iterator i=datas.begin();

        //mItemId
        bool ret= setJavaObjectField(env, cl, RecVideoItem_Field_mItemId, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //mTitle
        ret= setJavaObjectField(env, cl, RecVideoItem_Field_mTitle, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //mSize
        ret= setJavaObjectField(env, cl, RecVideoItem_Field_mSize, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //mDuration
        ret= setJavaObjectField(env, cl, RecVideoItem_Field_mDuration, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //mResolution
        ret= setJavaObjectField(env, cl, RecVideoItem_Field_mResolution, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //mBitrate
        ret= setJavaObjectField(env, cl, RecVideoItem_Field_mBitrate, Dlna_Java_String_Path, *i++, objOut);
        if(!ret){
            return false;
        }

        //mUpnpIcon
        ret= setJavaObjectField(env, cl, RecVideoItem_Field_mResUrl, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //mUpnpIcon
        ret= setJavaObjectField(env, cl, RecVideoItem_Field_mUpnpIcon, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //mDate
        ret= setJavaObjectField(env, cl, RecVideoItem_Field_mDate, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //mAllowedUse?

        //Xml_Item_VideoType
        ret= setJavaObjectField(env, cl, RecVideoItem_Field_mVideoType, Dlna_Java_String_Path,  *i, objOut);
        if(!ret){
            return false;
        }

        ret=env->CallBooleanMethod(listObj , listAddId , objOut);

        return ret;
    }

    bool addBsChListItem(JNIEnv *env, jclass cl, jmethodID cons, StringVector& datas, jobject objOut, jobject listObj, jmethodID listAddId) {
        StringVector::iterator i=datas.begin();

        //DlnaBsChListItem_Field_mChannelNo
        bool ret= setJavaObjectField(env, cl, DlnaBsChListItem_Field_mChannelNo, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaBsChListItem_Field_mTitle
        ret= setJavaObjectField(env, cl, DlnaBsChListItem_Field_mTitle, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaBsChListItem_Field_mSize
        ret= setJavaObjectField(env, cl, DlnaBsChListItem_Field_mSize, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaBsChListItem_Field_mDuration
        ret= setJavaObjectField(env, cl, DlnaBsChListItem_Field_mDuration, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaBsChListItem_Field_mResolution
        ret= setJavaObjectField(env, cl, DlnaBsChListItem_Field_mResolution, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaBsChListItem_Field_mBitrate
        ret= setJavaObjectField(env, cl, DlnaBsChListItem_Field_mBitrate, Dlna_Java_String_Path, *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaBsChListItem_Field_mResUrl
        ret= setJavaObjectField(env, cl, DlnaBsChListItem_Field_mResUrl, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaBsChListItem_Field_mThumbnail
        ret= setJavaObjectField(env, cl, DlnaBsChListItem_Field_mThumbnail, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaBsChListItem_Field_mDate
        ret= setJavaObjectField(env, cl, DlnaBsChListItem_Field_mDate, Dlna_Java_String_Path,  *i, objOut);
        if(!ret){
            return false;
        }

        ret=env->CallBooleanMethod(listObj , listAddId , objOut);

        return ret;
    }

    bool addTerChListItem(JNIEnv *env, jclass cl, jmethodID cons, StringVector& datas, jobject objOut, jobject listObj, jmethodID listAddId) {
        StringVector::iterator i=datas.begin();

        //DlnaTerChListItem_Field_mChannelNo
        bool ret= setJavaObjectField(env, cl, DlnaTerChListItem_Field_mChannelNo, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaTerChListItem_Field_mTitle
        ret= setJavaObjectField(env, cl, DlnaTerChListItem_Field_mTitle, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaTerChListItem_Field_mSize
        ret= setJavaObjectField(env, cl, DlnaTerChListItem_Field_mSize, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaTerChListItem_Field_mDuration
        ret= setJavaObjectField(env, cl, DlnaTerChListItem_Field_mDuration, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaTerChListItem_Field_mResolution
        ret= setJavaObjectField(env, cl, DlnaTerChListItem_Field_mResolution, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaTerChListItem_Field_mBitrate
        ret= setJavaObjectField(env, cl, DlnaTerChListItem_Field_mBitrate, Dlna_Java_String_Path, *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaTerChListItem_Field_mResUrl
        ret= setJavaObjectField(env, cl, DlnaTerChListItem_Field_mResUrl, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaTerChListItem_Field_mThumbnail
        ret= setJavaObjectField(env, cl, DlnaTerChListItem_Field_mThumbnail, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaTerChListItem_Field_mDate
        ret= setJavaObjectField(env, cl, DlnaTerChListItem_Field_mDate, Dlna_Java_String_Path,  *i, objOut);
        if(!ret){
            return false;
        }

        ret=env->CallBooleanMethod(listObj , listAddId , objOut);

        return ret;
    }

    bool addHikariChListItem(JNIEnv *env, jclass cl, jmethodID cons, StringVector& datas, jobject objOut, jobject listObj, jmethodID listAddId) {
        StringVector::iterator i=datas.begin();

        //DlnaHikariChListItem_Field_mChannelNo
        bool ret= setJavaObjectField(env, cl, DlnaHikariChListItem_Field_mChannelNo, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaHikariChListItem_Field_mTitle
        ret= setJavaObjectField(env, cl, DlnaHikariChListItem_Field_mTitle, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaHikariChListItem_Field_mSize
        ret= setJavaObjectField(env, cl, DlnaHikariChListItem_Field_mSize, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaHikariChListItem_Field_mDuration
        ret= setJavaObjectField(env, cl, DlnaHikariChListItem_Field_mDuration, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaHikariChListItem_Field_mResolution
        ret= setJavaObjectField(env, cl, DlnaHikariChListItem_Field_mResolution, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaHikariChListItem_Field_mBitrate
        ret= setJavaObjectField(env, cl, DlnaHikariChListItem_Field_mBitrate, Dlna_Java_String_Path, *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaHikariChListItem_Field_mResUrl
        ret= setJavaObjectField(env, cl, DlnaHikariChListItem_Field_mResUrl, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaHikariChListItem_Field_mThumbnail
        ret= setJavaObjectField(env, cl, DlnaHikariChListItem_Field_mThumbnail, Dlna_Java_String_Path,  *i++, objOut);
        if(!ret){
            return false;
        }

        //DlnaHikariChListItem_Field_mDate
        ret= setJavaObjectField(env, cl, DlnaHikariChListItem_Field_mDate, Dlna_Java_String_Path,  *i, objOut);
        if(!ret){
            return false;
        }

        ret=env->CallBooleanMethod(listObj , listAddId , objOut);

        return ret;
    }

    bool addListItems(DLNA_MSG_ID msg, JNIEnv *env, jclass cl, jmethodID cons,
                      vector<StringVector> &datas, jobject listObj, jmethodID listAddId,
                      jclass recVideoItemClass, jmethodID recVideoItemConstructor) {
        bool ret=true;

        for(vector<StringVector>::iterator i=datas.begin(); i!=datas.end(); ++i){
            jobject objOut= env->NewObject(recVideoItemClass , recVideoItemConstructor);
            if(NULL==objOut){
                return false;
            }
            switch (msg) {
                case DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST:
                    if (!addRecVideoItem(env, cl, cons, *i, objOut, listObj, listAddId)) {
                        ret = false;
                        env->DeleteLocalRef(objOut);
                        break;
                    }
                    break;
                case DLNA_MSG_ID_BS_CHANNEL_LIST:
                    if (!addBsChListItem(env, cl, cons, *i, objOut, listObj, listAddId)) {
                        ret = false;
                        env->DeleteLocalRef(objOut);
                        break;
                    }
                    break;
                case DLNA_MSG_ID_TER_CHANNEL_LIST:
                    if (!addTerChListItem(env, cl, cons, *i, objOut, listObj, listAddId)) {
                        ret = false;
                        env->DeleteLocalRef(objOut);
                        break;
                    }
                    break;
                case DLNA_MSG_ID_HIKARI_CHANNEL_LIST:
                    if (!addHikariChListItem(env, cl, cons, *i, objOut, listObj, listAddId)) {
                        ret = false;
                        env->DeleteLocalRef(objOut);
                        break;
                    }
                    break;
                case DLNA_MSG_ID_DEV_DISP_JOIN:
                case DLNA_MSG_ID_DEV_DISP_LEAVE:
                case DLNA_MSG_ID_INVALID:
                    break;
            }
            env->DeleteLocalRef(objOut);
        }

        return ret;
    }


    void Dlna::notifyObject(DLNA_MSG_ID msg, vector<StringVector> & vecContents) {
        JNIEnv *env = NULL;
        jobject itemObj = NULL;
        StringVector datas;
        jclass listActivityClazz = NULL;
        jmethodID method = NULL;
        jmethodID itemCostruct = NULL;
        jmethodID listAddId  = NULL;
        jmethodID listCostruct = NULL;
        jobject listObj = NULL;
        jclass listCls = NULL;

        int status = mEvent.mJavaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
        if (status < 0) {
            status = mEvent.mJavaVM->AttachCurrentThread(&env, NULL);
            if (status < 0 || NULL == env) {
                return;
            }
        }

        listCls = env->FindClass("java/util/ArrayList");
        IfNullGoTo(listCls, error_or_return);

        listCostruct = env->GetMethodID(listCls , "<init>","()V");
        IfNullGoTo(listCostruct, error_or_return);

        listObj = env->NewObject(listCls , listCostruct);
        IfNullGoTo(listObj, error_or_return);

        listAddId  = env->GetMethodID(listCls,"add","(Ljava/lang/Object;)Z");
        IfNullGoTo(listAddId, error_or_return);

        switch (msg){
            case DLNA_MSG_ID_DEV_DISP_JOIN:
                itemCostruct = env->GetMethodID(mEvent.mJClassDmsItem, "<init>", "()V");
                IfNullGoTo(itemCostruct, error_or_return);
                itemObj = env->NewObject(mEvent.mJClassDmsItem , itemCostruct);
                IfNullGoTo(itemObj, error_or_return);
                if(0==vecContents.size() || NULL==itemObj){
                    goto error_or_return;
                }
                datas= *vecContents.begin();
                if(!addDmsInfo(env, mEvent.mJClassDmsItem, itemCostruct, datas, itemObj)){
                    goto error_or_return;
                }
                env->CallBooleanMethod(listObj , listAddId , itemObj);
                break;
            case DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST:
                itemCostruct = env->GetMethodID(mEvent.mJClassRecVideoItem, "<init>", "()V");
                IfNullGoTo(itemCostruct, error_or_return);
                if(0==vecContents.size() ){
                    goto error_or_return;
                }
                if(!addListItems(DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST, env, mEvent.mJClassRecVideoItem,
                                 itemCostruct, vecContents, listObj, listAddId,
                                 mEvent.mJClassRecVideoItem, itemCostruct) ){
                    goto error_or_return;
                }
                break;
            case DLNA_MSG_ID_BS_CHANNEL_LIST:
                itemCostruct = env->GetMethodID(mEvent.mJClassBsChListItem, "<init>", "()V");
                IfNullGoTo(itemCostruct, error_or_return);
                if(0==vecContents.size() ){
                    goto error_or_return;
                }
                if(!addListItems(DLNA_MSG_ID_BS_CHANNEL_LIST, env, mEvent.mJClassBsChListItem,
                                 itemCostruct, vecContents, listObj, listAddId,
                                 mEvent.mJClassBsChListItem, itemCostruct) ){
                    goto error_or_return;
                }
                break;
            case DLNA_MSG_ID_TER_CHANNEL_LIST:
                itemCostruct = env->GetMethodID(mEvent.mJClassTerChListItem, "<init>", "()V");
                IfNullGoTo(itemCostruct, error_or_return);
                if(0==vecContents.size() ){
                    goto error_or_return;
                }
                if(!addListItems(DLNA_MSG_ID_TER_CHANNEL_LIST, env, mEvent.mJClassTerChListItem,
                                 itemCostruct, vecContents, listObj, listAddId,
                                 mEvent.mJClassTerChListItem, itemCostruct) ){
                    goto error_or_return;
                }
                break;
            case DLNA_MSG_ID_HIKARI_CHANNEL_LIST:
                itemCostruct = env->GetMethodID(mEvent.mJClassHikariChListItem, "<init>", "()V");
                IfNullGoTo(itemCostruct, error_or_return);
                if(0==vecContents.size() ){
                    goto error_or_return;
                }
                if(!addListItems(DLNA_MSG_ID_HIKARI_CHANNEL_LIST, env, mEvent.mJClassHikariChListItem,
                                 itemCostruct, vecContents, listObj, listAddId,
                                 mEvent.mJClassHikariChListItem, itemCostruct) ){
                    goto error_or_return;
                }
                break;
            case DLNA_MSG_ID_DEV_DISP_LEAVE:
            case DLNA_MSG_ID_INVALID:
                break;
        }

        listActivityClazz = env->GetObjectClass(mEvent.mJObject);
        method = env->GetMethodID(listActivityClazz, "notifyObjFromNative", "(ILjava/util/ArrayList;)V");
        IfNullGoTo(method, error_or_return);

        env->CallVoidMethod(mEvent.mJObject, method, msg, listObj);

        error_or_return:
        if(!env){
            return;
        }
        if(itemObj){
            env->DeleteLocalRef(itemObj);
            itemObj=NULL;
        }
        if(listObj){
            env->DeleteLocalRef(listObj);
            listObj=NULL;
        }
        if(listActivityClazz){
            env->DeleteLocalRef(listActivityClazz);
            listActivityClazz=NULL;
        }
        if(listCls){
            env->DeleteLocalRef(listCls);
            listCls=NULL;
        }
    }

    bool Dlna::browseRecVideoListDms(std::string controlUrl) {
        if(NULL==mDlnaRecVideoXmlParser){
            mDlnaRecVideoXmlParser=(DlnaXmlParserBase*)new DlnaRecVideoXmlParser();
            IfNullReturnFalse(mBsDigitalXmlParser);
        }
        mRecursionXmlParser=mDlnaRecVideoXmlParser;
        return sendSoap(controlUrl, DLNA_DMS_ROOT);
    }

    bool Dlna::browseBsChListDms(std::string controlUrl) {
        if(NULL==mBsDigitalXmlParser){
            mBsDigitalXmlParser=(DlnaXmlParserBase*)new DlnaBSDigitalXmlParser();
            IfNullReturnFalse(mBsDigitalXmlParser);
        }
        mRecursionXmlParser=mBsDigitalXmlParser;
        return sendSoap(controlUrl, "0");
    }

    bool Dlna::browseTerChListDms(std::string controlUrl) {
        if(NULL==mTerChXmlParser){
            mTerChXmlParser=(DlnaXmlParserBase*)new DlnaTerChXmlParser();
            IfNullReturnFalse(mTerChXmlParser);
        }
        mRecursionXmlParser=mTerChXmlParser;
        return sendSoap(controlUrl, "0");
    }

    bool Dlna::browseHikariChListDms(std::string controlUrl) {
        if(NULL==mHikariChXmlParser){
            mHikariChXmlParser=(DlnaXmlParserBase*)new DlnaHikariChXmlParser();
            IfNullReturnFalse(mHikariChXmlParser);
        }
        mRecursionXmlParser=mHikariChXmlParser;
        return sendSoap(controlUrl, "0");
    }


    /**
     * デバイスディスクリプションを解析してデバイス情報を設定する
     *
     * @param x
     * @param device
     * @param dvcdsc
     * @param arg
     * @return このデバイスをデバイスマネージャーが管理するリストに追加する場合 1 追加しない場合 0
     */
    /*static*/ du_bool Dlna::allowJoinHandler(dupnp_cp_dvcmgr *x, dupnp_cp_dvcmgr_device *device,
                                              dupnp_cp_dvcmgr_dvcdsc *dvcdsc, void *arg) {
        dms_info* info;

        // イスディスクリプションを解析
        info = createDmsInfoXmlDoc(dvcdsc->xml, dvcdsc->xml_len, device->udn, device->device_type, device->location);
        if (!info){
            return 0; // このデバイスをデバイスマネージャーの管理リストに追加しません
        }
        // 特定のDMS に限定する場合は friendly_name を指定する
        if (NULL == strstr((char*)info->friendly_name, "特定のDMSのfriendly_name")){
//        return 0; // このデバイスをデバイスマネージャーの管理リストに追加しません
        }
        device->user_data = (void*)info; // デバイスディスクリプションの解析情報 ※leaveHandlerで解放すること

        return 1;
    }

    /**
     * デバイス検出
     *
     * @param x
     * @param device
     * @param dvcdsc
     * @param arg
     */
    /*static*/ void Dlna::joinHandler(dupnp_cp_dvcmgr *x, dupnp_cp_dvcmgr_device *device,
                                      dupnp_cp_dvcmgr_dvcdsc *dvcdsc, void *arg) {
        if (NULL == arg || NULL == dvcdsc || NULL == dvcdsc->xml) {
            return;
        }
        Dlna *thiz = (Dlna *) arg;
        IfNullReturn(thiz);
        IfNullReturn(thiz->mDlnaDevXmlParser);

        std::vector<std::vector<std::string> > vv;
        DlnaDevXmlParser* parser= (DlnaDevXmlParser*)thiz->mDlnaDevXmlParser;
        std::string containerId;
        parser->parse(device, vv);
        if(0==vv.size()){
            return;
        }

        thiz->notifyObject(DLNA_MSG_ID_DEV_DISP_JOIN, vv);
    }

    /**
     * デバイスの停止：デバイス情報の解放
     *
     * @param x
     * @param device
     * @param arg
     * @return
     */
    /*static*/ du_bool
    Dlna::leaveHandler(dupnp_cp_dvcmgr *x, dupnp_cp_dvcmgr_device *device, void *arg) {
        if (NULL == arg || NULL == device || NULL == device->udn) {
            return 1;
        }
        Dlna *thiz = (Dlna *) arg;
        dms_info *info = (dms_info*)device->user_data;

        std::string content((char *) device->udn);
        freeDmsInfoXmlDoc(info);    // allowJoinHandler#createDmsInfoXmlDoc で取得したデバイス情報の解放

        thiz->notify(DLNA_MSG_ID_DEV_DISP_LEAVE, content);

        return 1;
    }

} //namespace dtvt
