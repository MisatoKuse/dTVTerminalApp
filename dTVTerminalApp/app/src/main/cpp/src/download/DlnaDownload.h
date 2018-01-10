/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

#ifndef DTVT_DlnaDownload_H
#define DTVT_DlnaDownload_H

#include <string>
#include <vector>
#include <jni.h>

#include "../Common.h"

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */

namespace dtvt {

    using namespace std;

    /**
     * 機能：Dlanについての機能を実現
     */
    class DlnaDownload {
    private:
        typedef struct Event {
            JavaVM *mJavaVM;
            jobject mJObject;

            Event() : mJavaVM(NULL), mJObject(NULL){}
        } Event;
    public:
        /**
         * 機能：DlnaDownloadコンストラクタ
         * @return
         */
        DlnaDownload();

        /**
         * 機能：DlnaDownloadをスタート
         * @param env
         * @param obj
         * @return true: 成功 false: 失敗
         */
        bool start(JNIEnv *env, jobject obj);

        /**
         * 機能：DlnaDownloadをストプ
         */
        void stop();

        /**
         * 機能：dtcp Download
         * @param env env
         * @param instance instance
         * @param info private home dir
         */
        void dtcpDownload(JNIEnv *env, jobject instance, std::string dirToSave, std::string fileNameToSave, std::string dtcp1host, int dtcp1port, std::string url, int cleartextSize, const char *xml);

        void dtcpDownloadCancel();

        /**
         * Callback of [typedef void (*downloader_status_handler)(DownloaderStatus status, const du_uchar* http_status, void* arg);]
         * @param status
         * @param http_status
         * @param arg
         */
        static void downloaderStatusHandler(DownloaderStatus status, const du_uchar* http_status, void* arg);

        /**
         * Callback of [typedef void (*downloader_progress_handler)(du_uint64 sent_size, du_uint64 total_size, void* arg);]
         * @param sent_size
         * @param total_size
         * @param arg
         */
        static void downloaderProgressHandler(du_uint64 sent_size, du_uint64 total_size, void* arg);

    private:
        bool initDevEnv();

        void notify(int msg, std::string content);

    private:
        Event mEvent;
    };

} //namespace dtvt

#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif /* DTVT_DlnaDownload_H */
