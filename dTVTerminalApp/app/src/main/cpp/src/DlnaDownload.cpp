//
//  DlnaDownload.cpp
//  dTVTerminal
//

#include "DlnaDownload.h"

#include <ddtcp_private.h>

#include "DlnaMacro.h"
#include "download/dtcp.hpp"
#include <cstring>
#include <sstream>

#ifndef DDTCP_CRYPTO_SHA1_DIGEST_SIZE
#define DDTCP_CRYPTO_SHA1_DIGEST_SIZE 20
#endif
using namespace std;
using namespace dixim::dmsp::dtcp;
static bool sIsJustCanceled=false;
static pthread_mutex_t gMutexIsJustCanceled;
std::function<void(int progress)> DlnaDownload::DownloadProgressCallBack = nullptr;
std::function<void(downloader_status status)> DlnaDownload::DownloadStatusCallBack = nullptr;

DlnaDownload::DlnaDownload(): mDtcp(NULL), mDirToSave(""), mPercentToNotify(1) {
    DlnaDownload::DownloadProgressCallBack = nullptr;
    DlnaDownload::DownloadStatusCallBack = nullptr;
}

DlnaDownload::~DlnaDownload() {
    stop();
    DlnaDownload::DownloadProgressCallBack = nullptr;
    DlnaDownload::DownloadStatusCallBack = nullptr;
}

static void setIsJustCanceled(bool yn){
    pthread_mutex_lock(&gMutexIsJustCanceled);
    sIsJustCanceled=yn;
    pthread_mutex_unlock(&gMutexIsJustCanceled);
}

bool DlnaDownload::isStarted(){
    return NULL!=mDtcp;
}

void DlnaDownload::downloaderStatusHandler(downloader_status status, const du_uchar* http_status, void* arg){
    if (NULL == arg) {
        return;
    }
    pthread_mutex_lock(&gMutexIsJustCanceled);
    if(DOWNLOADER_STATUS_ERROR_OCCURED == status){
        if(sIsJustCanceled){
            sIsJustCanceled=false;
            if (DlnaDownload::DownloadStatusCallBack != nullptr) {
                DlnaDownload::DownloadStatusCallBack(status);
            }
            pthread_mutex_unlock(&gMutexIsJustCanceled);
            return;
        }
    }
    if (DlnaDownload::DownloadStatusCallBack != nullptr) {
        DlnaDownload::DownloadStatusCallBack(status);
    }
    pthread_mutex_unlock(&gMutexIsJustCanceled);
}

void DlnaDownload::downloaderProgressHandler(du_uint64 sent_size, du_uint64 total_size, void* arg){
    if (NULL == arg) {
        return;
    }
    DlnaDownload *thiz = (DlnaDownload *) arg;

    static du_uint64 lastNotifiedBytes = 0;
    du_uint64 diff= sent_size - lastNotifiedBytes;
    int percent = thiz->getPercentToNotify();

    float res = ((float)(diff)) / ((float)total_size);
    float pp = percent * 0.01f;

    if (sent_size >= total_size) {
        lastNotifiedBytes=0;
        return;
    }
    if (res >= pp) {
        float f1 = ((float) sent_size);
        float f2 = ((float) total_size);
        int ff = (int) ((f1 / f2) * 100);
        if (ff > 99) {
            LOG_WITH("download percent >= 99 %d%%", ff);
            ff = 99;
        }
        LOG_WITH("C>>>>>>>>>>>>>>>>DlnaDownload.cpp %d%%", ff);
        if (DlnaDownload::DownloadProgressCallBack != nullptr) {
            DlnaDownload::DownloadProgressCallBack(ff);
        }
        lastNotifiedBytes = sent_size;
    }
}

bool DlnaDownload::start(DMP *dmp, JavaVM *vm, jobject object, jmethodID mid) {
    if(mDtcp){
        return true;
    }
    mDtcp = new dtcp();
    dixim::dmsp::dtcp::dtcp* d=NULL;
    d = (dixim::dmsp::dtcp::dtcp *) mDtcp;
    d->private_data_home = (const char*)dmp->private_data_home;
    d->ake_port = 53211;
    return d->start(vm, object);
}

void DlnaDownload::startDownload(std::string fileNameToSave, std::string dtcp1host_, int dtcp1port_, std::string url_, jlong cleartextSize, const char *xml) {
    if(fileNameToSave.empty() || dtcp1host_.empty() || url_.empty() || xml == nullptr){
        downloaderStatusHandler(DOWNLOADER_STATUS_ERROR_OCCURED, 0, this);
        return;
    }
    const du_uchar* dtcp1host = DU_UCHAR_CONST(dtcp1host_.c_str());
    du_uint16 dtcp1port = (du_uint16)dtcp1port_;
    const du_uchar* url = DU_UCHAR_CONST(url_.c_str());
    //du_bool move = 0;
    du_bool move = 1;
    string fileToDownload = mDirToSave + "/" + fileNameToSave;
    const du_uchar* dixim_file = DU_UCHAR_CONST(fileNameToSave.c_str());
    du_uint64 cleartext_size = (du_uint64)cleartextSize;
    du_str_array* request_header = 0;
    du_uchar* xmlStrDu = NULL;
    dixim::dmsp::dtcp::dtcp* dtcp= (dixim::dmsp::dtcp::dtcp *) mDtcp;
    if(!isStarted()){
        downloaderStatusHandler(DOWNLOADER_STATUS_ERROR_OCCURED, 0, this);
        return;
    }
    xmlStrDu= (du_uchar *) xml;
    if(!xmlStrDu){
        goto error;
    }
    downloader_download(
            dtcp->d,
            dtcp1host,
            dtcp1port,
            url,
            move,
            (downloader_status_handler) downloaderStatusHandler,
            (downloader_progress_handler) downloaderProgressHandler,
            this, //handler arg
            dixim_file,
            xmlStrDu,
            cleartext_size,
            request_header);
    return;
    error:
    downloaderStatusHandler(DOWNLOADER_STATUS_ERROR_OCCURED, 0, this);
}

void DlnaDownload::stop() {
    LOG_WITH("dtcp stop");
    if(mDtcp){
        dixim::dmsp::dtcp::dtcp* dtcp= (dixim::dmsp::dtcp::dtcp *) mDtcp;
        dtcp->stop();
        mDtcp = NULL;
    }
}

void DlnaDownload::downloadCancel(){
    setIsJustCanceled(true);
    downloader_cancel();
    LOG_WITH("c++ download cancel");
}

