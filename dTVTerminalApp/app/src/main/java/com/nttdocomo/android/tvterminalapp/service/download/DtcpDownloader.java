/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDlListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDlStatus;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDownloadRet;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvDownload;

import java.io.File;

public class DtcpDownloader extends DownloaderBase implements DlnaDlListener {

    private DlnaProvDownload mDlnaProvDownload;
    private int mFinishedBytes;

    public DtcpDownloader(DownloadParam param, DownloadListener downloadListener)throws Exception{
        super(param, downloadListener);
        DtcpDownloadParam p = (DtcpDownloadParam) param;
        mDlnaProvDownload=new DlnaProvDownload(p.getSavePath());
    }

    @Override
    protected int calculateTotalBytes() {
        int ret=0;
        DtcpDownloadParam param;
        try{
            param= (DtcpDownloadParam)getDownloadParam();
            ret= param.getCleartextSize();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        return ret;
    }

    @Override
    protected void download() {
        DTVTLogger.debug("dtcp download begin, files");
        printDlPathFiles();
        mFinishedBytes=0;
        DtcpDownloadParam param= (DtcpDownloadParam) getDownloadParam();
        if(!param.isParamValid()){
            onFail(DownloadListener.DLError.DLError_ParamError);
            return;
        }

        Context context=param.getContext();
        if(null==context){
            onFail(DownloadListener.DLError.DLError_Other);
            return;
        }

        boolean ret=mDlnaProvDownload.startListen(this, context, param.getSavePath());
        if(!ret){
            onFail(DownloadListener.DLError.DLError_DmsError);
            return;
        }

        DlnaDownloadRet res= mDlnaProvDownload.download(param);
        switch (res){
            case DownloadRet_CopyKeyFileFailed:
                onFail(DownloadListener.DLError.DLError_CopyKeyFileFailed);
                break;
            case DownloadRet_ParamError:
                onFail(DownloadListener.DLError.DLError_ParamError);
                break;
            case DownloadRet_Unactivated:
                onFail(DownloadListener.DLError.DLError_Unactivated);
                break;
            case DownloadRet_OtherError:
                onFail(DownloadListener.DLError.DLError_Other);
                break;
            case DownloadRet_Succeed:
                break;
        }
    }

    @Override
    protected boolean isStorageSpaceLow() {
        //todo to finish
        return false;
    }

    @Override
    protected void cancelImpl() {
        if(null!=mDlnaProvDownload) {
            mDlnaProvDownload.cancel();
        }
    }

    @Override
    public void dlProgress(int sizeFinished) {
        if(isStorageSpaceLow()){
            setLowStorageSpace();
            return;
        }
        int diff= sizeFinished - mFinishedBytes;
        onProgress(diff);
        mFinishedBytes = sizeFinished;
    }

    @Override
    public void dlStatus(DlnaDlStatus status) {
        switch (status){
            case DOWNLOADER_STATUS_COMPLETED:
                onSuccess();
                onStopIt();
                break;
            case DOWNLOADER_STATUS_ERROR_OCCURED:
                onFail(DownloadListener.DLError.DLError_Download);
                onStopIt();
                break;
            case DOWNLOADER_STATUS_CANCELLED:
                onStopIt();
                break;
            case DOWNLOADER_STATUS_UNKNOWN:
                break;
            case DOWNLOADER_STATUS_MOVING:
                break;
        }
    }

    private void printDlPathFiles(){
        try {
            DtcpDownloadParam dp = (DtcpDownloadParam) getDownloadParam();
            if (null!=dp) {
                String path=dp.getSavePath();
                if(null!=path){
                    File f= new File(path);
                    File[] files= f.listFiles();
                    DTVTLogger.debug("------------------------");
                    for(File f2: files){
                        DTVTLogger.debug(f2.getAbsolutePath() + ", size=" + f2.length());
                    }
                    DTVTLogger.debug("------------------------");
                }
            }
        }catch (Exception e){

        }
    }

    private void onStopIt(){
        mFinishedBytes=0;
        DTVTLogger.debug("dtcp download end, files");
        printDlPathFiles();
    }

//    /**
//     * 機能：
//     *      １．Download Uiがなくなる場合、必ずこれをコールする
//     *      ２．Download Uiがない場合、Serviceは閉じる時、必ずこれをコールする
//     */
//    @Override
//    public void finishDl() {
//        if (null == mDlnaProvDownload) {
//            return;
//        }
//        mDlnaProvDownload.finishDl();
//    }
}
