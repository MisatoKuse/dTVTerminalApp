/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DTVT_DlnaRemote_H
#define DTVT_DlnaRemote_H

#include <string>
#include <vector>
#include <jni.h>

#include "../Common.h"
#include "CommonRm.h"
#include "dmp.h"

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */

namespace dtvt {

    using namespace std;

    /**
     * 機能：Dlanについての機能を実現
     */
    class DlnaRemote {
    private:
        typedef struct Event {
            JavaVM *mJavaVM;
            jobject mJObject;

            Event() : mJavaVM(NULL), mJObject(NULL){}
        } Event;
    public:
        /**
         * 機能：DlnaRemoteコンストラクタ
         * @return
         */
        DlnaRemote();

        virtual ~DlnaRemote();

        /**
         * 機能：DlnaRemoteをスタート
         * @param env
         * @param obj
         * @return true: 成功 false: 失敗
         */
        bool start(JNIEnv *env, jobject obj, std::string dirToSave);

        /**
         * 機能：DlnaRemoteをスタート
         * @param udn
         * @return true: 成功 false: 失敗
         */
        bool regist(std::string udn);

        /**
         * 機能：DlnaRemoteを接続
         * @param udn
         * @return true: 成功 false: 失敗
         */
        bool connect(std::string udn);
        /**
         * 機能：DlnaRemoteをストプ
         */
        void stop();

        void notify(int msg, std::string content);

    private:
        //bool startDlEnv(JNIEnv *env, jobject obj, std::string& confDir);


        bool isStarted();

        du_bool dmp_init(DmpRm* d, const du_uchar* conf_path);
        du_bool dmp_cp_init(DmpRm* d);
        du_bool capability_init(DmpRm* p, const du_uchar* cap_path);
        du_bool dmp_start(DmpRm* d, jobject instance, std::string& dirToSave);
        void dmp_free(DmpRm* d);

    private:
        Event mEvent;
        DmpRm* mDmpRm;
        std::string mConfDir;
        connect_status_arg_s m_connect_status_arg;
    };

} //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif /* DTVT_DlnaRemote_H */
