/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDlListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDlStatus;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDownloadRet;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvDownload;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * todo 作成中
 */
public class DtcpDownloader extends DownloaderBase implements DlnaDlListener {

    private DlnaProvDownload mDlnaProvDownload;
    private int mFinishedBytes;

    public DtcpDownloader(DownloadParam param, DownloadListener downloadListener)throws Exception{
        super(param, downloadListener);
        mDlnaProvDownload=new DlnaProvDownload();
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

        boolean ret=mDlnaProvDownload.startListen(this, context);
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

    private void onStopIt(){
        mFinishedBytes=0;
    }
}
