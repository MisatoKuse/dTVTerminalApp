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
            jclass mJClassBsChListItem;
            jclass mJClassTerChListItem;
            jclass mJClassHikariChListItem;

            Event() : mJmethodID(NULL), mJavaVM(NULL), mJClassDlna(NULL), mJObject(NULL), mJClassDmsItem(NULL), mJClassRecVideoItem(NULL), mJClassBsChListItem(NULL),
                      mJClassTerChListItem(NULL), mJClassHikariChListItem(NULL){}
        } Event;

    private:
        typedef enum {
            DLNA_STATE_STOP,
            DLNA_STATE_STARTED,
        } DLNA_STATE;

//        typedef enum {
//            //Browseコンテンツ
//            DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST = 0,
//            //デバイスjoin
//            DLNA_MSG_ID_DEV_DISP_JOIN = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 1,
//            //デバイスleave
//            DLNA_MSG_ID_DEV_DISP_LEAVE = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 2,
//            //BSデジタルに関して、チャンネルリストを発見
//            DLNA_MSG_ID_BS_CHANNEL_LIST = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 3,
//        } DLNA_MSG_ID;

    private:
        dmp mDMP;
        DLNA_STATE mDLNA_STATE;
        Event mEvent;
        DlnaXmlParserBase* mDlnaDevXmlParser;
        DlnaXmlParserBase* mRecursionXmlParser;
        DlnaXmlParserBase* mDlnaRecVideoXmlParser;
        DlnaXmlParserBase* mBsDigitalXmlParser;
        DlnaXmlParserBase* mTerChXmlParser;
        DlnaXmlParserBase* mHikariChXmlParser;

    public:
        /**
         * 機能：Dlnaコンストラクタ
         * @return
         */
        Dlna();

        /**
         * 機能：dlnaをスタート
         * @param env
         * @param obj
         * @return true: 成功 false: 失敗
         */
        bool start(JNIEnv *env, jobject obj);

        /**
         * 機能：dlnaをストプ
         */
        void stop();

        /**
         * 機能：録画ヴィデオ一覧を発見
         * @param controlUrl
         * @return true: 成功 false: 失敗
         */
        bool browseRecVideoListDms(std::string controlUrl);

        /**
         * 機能：BSデジタルに関して、チャンネルリストを発見
         * @param controlUrl
         * @return true: 成功  false:失敗
         */
        bool browseBsChListDms(std::string controlUrl);

        bool browseTerChListDms(std::string controlUrl);

        bool browseHikariChListDms(std::string controlUrl);

        /**
         * 機能：Dlnaコールバック
         * @param x
         * @param device
         * @param dvcdsc
         * @param arg
         * @return
         */
        static du_bool allowJoinHandler(dupnp_cp_dvcmgr *x, dupnp_cp_dvcmgr_device *device,
                                        dupnp_cp_dvcmgr_dvcdsc *dvcdsc, void *arg);

        /**
         * 機能：Dlnaコールバック
         * @param x
         * @param device
         * @param dvcdsc
         * @param arg
         */
        static void joinHandler(dupnp_cp_dvcmgr *x, dupnp_cp_dvcmgr_device *device,
                                dupnp_cp_dvcmgr_dvcdsc *dvcdsc, void *arg);

        /**
         * 機能：Dlnaコールバック
         * @param x
         * @param device
         * @param arg
         * @return
         */
        static du_bool leaveHandler(dupnp_cp_dvcmgr *x, dupnp_cp_dvcmgr_device *device, void *arg);

        /**
         * 機能：Dlnaコールバック
         * @param response
         * @param arg
         */
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
        void notifyObject(DLNA_MSG_ID msg, vector<StringVector> & vecContents);
    };

} //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif /* DTVT_DLNA_H */
