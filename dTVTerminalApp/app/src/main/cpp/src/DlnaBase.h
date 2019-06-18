/**
 * Copyright ©︎ 2018 NTT DOCOMO,INC.All Rights Reserved.
 */

#ifndef DlnaBase_h
#define DlnaBase_h

#include <functional>
#include "DlnaDefine.h"

class DlnaBase {
public:
    DlnaBase();
    ~DlnaBase();

    // callback ==============================
    //検索されたDMSがあればその情報を返す
    static std::function<void(const char* friendlyName, const char* udn, const char* location, const char* controlUrl, const char* eventSubscriptionUrl)> DmsFoundCallback;
    static std::function<void(const char* udn)> DmsLeaveCallback;

    // API ==============================
public:

    /**
     DMPの初期化
     upnp,soap,browseなどの初期化

     @param dmp target
     @return result
     */
    bool initDmp(DMP *dmp);

    /**
     DavCapabilityの初期化

     @param dmp target
     @param path xmlファイルパス
     @return result
     */
    bool initDavCapability(DMP *dmp, const char* path);

    bool initDtcp(DMP *dmp, const char* path);

    /**
     DiRAGの初期化

     @param path dirag_confパス
     @return result
     */
    bool initDirag(const char* path);

    /**
     M-SEARCHリクエスト（ホームネットワーク検索）

     @param dmp target
     @return result
     */
    bool startDmp(DMP *dmp);

    ddtcp_ret startDtcp(DMP *dmp, JavaVM *vm, jobject object, jmethodID mid);
    bool stopDtcp(DMP *dmp);

    /**
     dmpを中止する
     @param dmp target
     */
    void stopDmp(DMP *dmp);

    /**
     DMPのメモリ割り当てを解放する
     @param dmp target
     */
    void freeDmp(DMP *dmp);
};

#endif /* DlnaBase_h */
