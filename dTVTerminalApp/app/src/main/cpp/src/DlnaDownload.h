//
//  DlnaRemoteConnect.h
//  dTVTerminal
//

#ifndef DlnaDownload_h
#define DlnaDownload_h

#include <stdio.h>
#include <functional>
#include "DlnaDefine.h"
#include "download/downloader.h"
#include <string>
#include <jni.h>

class DlnaDownload {
public:
    DlnaDownload();
    ~DlnaDownload();

    // callback
public:
    static std::function<void(int progress)> DownloadProgressCallBack;
    static std::function<void(downloader_status status)> DownloadStatusCallBack;

    bool start(DMP *dmp, JavaVM *vm, jobject object, jmethodID mid);

    void startDownload(std::string fileNameToSave, std::string dtcp1host_, int dtcp1port_, std::string url_, jlong cleartextSize, const char *xml);

    void downloadCancel();

    void stop();

    static void downloaderStatusHandler(downloader_status status, const du_uchar* http_status, void* arg);

    static void downloaderProgressHandler(du_uint64 sent_size, du_uint64 total_size, void* arg);

    inline const int getPercentToNotify(){ return mPercentToNotify; }

private:
    bool isStarted();

private:
    int mPercentToNotify;
    void* mDtcp;
    std::string mDirToSave;
};

#endif /* DlnaRemoteConnect_h */
