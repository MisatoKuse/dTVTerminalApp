/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DTVT_DLNA_H
#define DTVT_DLNA_H

#include <string>
#include <vector>

#include "Common.h"
#include "DlnaDevXmlParser.h"
#include "DlnaRecVideoXmlParser.h"

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */

namespace dtvt {

    using namespace std;

    /**
     * 機能：Dlanについての機能を実現
     */
    class Dlna {
    private:
        typedef struct Event {
            jmethodID mJmethodID;
            JavaVM *mJavaVM;
            jclass mJClassDlna;
            jobject mJObject;
            jclass mJClassDmsItem;
            jclass mJClassRecVideoItem;

            Event() : mJmethodID(NULL), mJavaVM(NULL), mJClassDlna(NULL), mJObject(NULL) {}
        } Event;

    private:
        typedef enum {
            DLNA_STATE_STOP,
            DLNA_STATE_STARTED,
        } DLNA_STATE;

        typedef enum {
            //Browseコンテンツ
                    DLNA_MSG_ID_BROWSE_SOAP = 0,
            //デバイスjoin
                    DLNA_MSG_ID_DEV_DISP_JOIN = DLNA_MSG_ID_BROWSE_SOAP + 1,
            //デバイスleave
                    DLNA_MSG_ID_DEV_DISP_LEAVE = DLNA_MSG_ID_BROWSE_SOAP + 2,
        } DLNA_MSG_ID;

    private:
        dmp mDMP;
        DLNA_STATE mDLNA_STATE = DLNA_STATE_STOP;
        Event mEvent;
        DlnaXmlParserBase* mDlnaDevXmlParser;
        DlnaXmlParserBase* mDlnaRecVideoXmlParser;
        DlnaXmlParserBase* mRecursionXmlParser;

    public:
        Dlna();

        bool start(JNIEnv *env, jobject obj);

        void stop();

        bool browseDms(std::string controlUrl);

        static du_bool allowJoinHandler(dupnp_cp_dvcmgr *x, dupnp_cp_dvcmgr_device *device,
                                        dupnp_cp_dvcmgr_dvcdsc *dvcdsc, void *arg);

        static void joinHandler(dupnp_cp_dvcmgr *x, dupnp_cp_dvcmgr_device *device,
                                dupnp_cp_dvcmgr_dvcdsc *dvcdsc, void *arg);

        static du_bool leaveHandler(dupnp_cp_dvcmgr *x, dupnp_cp_dvcmgr_device *device, void *arg);

        static void browseDirectChildrenResponseHandler(dupnp_http_response *response, void *arg);

    private:
        bool init();

        void uninit();

        bool soapInit();

        void soapUninit();

        bool enableFunction();

        bool startDmgrAndEmgr();

        bool initDevEnv();

        bool sendSoap(std::string controlUrl, std::string objectId="0", const int startingIndex=0, const int requestCount=0, std::string browseFlag="BrowseDirectChildren");

        void notify(int msg, std::string content);
        void notifyObject(int msg, vector<StringVector> & vecVecContents);
    };

} //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif /* DTVT_DLNA_H */
