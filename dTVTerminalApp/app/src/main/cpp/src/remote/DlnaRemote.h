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
         * 機能：DlnaRemoteをストプ
         */
        void stop();


    private:
        bool startDlEnv(JNIEnv *env, jobject obj, std::string& confDir);

        void notify(int msg, std::string content);
        bool isStarted();

    private:
        Event mEvent;
        void* mDtcp;
        std::string mConfDir;
    };

} //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif /* DTVT_DlnaRemote_H */
