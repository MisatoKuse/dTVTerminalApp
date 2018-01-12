/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.service.download.DtcpDownloadParam;

/**
 * 機能：DlnaからActivityにDownload I/Fを提供するクラス
 */
public class DlnaProvDownload {
    DlnaInterfaceDl mDlnaInterfaceDl;

    /**
     * 機能：DlnaProvRecVideoを構造
     */
    public DlnaProvDownload(String savePath) throws Exception{
        DTVTLogger.start();
        mDlnaInterfaceDl=new DlnaInterfaceDl(savePath);
        DTVTLogger.end();
    }

    /**
     * 機能：Listenを始める
     * @param lis listener
     * @return 成功true
     */
    public boolean startListen(DlnaDlListener lis, Context context, String savePath){
        DTVTLogger.start();
        if(null==mDlnaInterfaceDl) {
            DTVTLogger.end();
            return false;
        }
        mDlnaInterfaceDl.setDlnaDlListener(lis, context);
        if(!mDlnaInterfaceDl.startDtcpDl(savePath)){
            DTVTLogger.end();
            return false;
        }
        DTVTLogger.end();
        return true;
    }

    /**
     * 機能：ダウンロード開始
     * @return 成功true
     */
    public DlnaDownloadRet download(DtcpDownloadParam param){
        DTVTLogger.start();
        if(null==mDlnaInterfaceDl){
            DTVTLogger.end();
            return DlnaDownloadRet.DownloadRet_OtherError;
        }
        DTVTLogger.end();
        return mDlnaInterfaceDl.download(param);
    }

    /**
     * 機能：
     *      １．Download Uiがなくなる場合、必ずこれをコールする
     *      ２．Download Uiがない場合、Serviceは閉じる時、必ずこれをコールする
     */
    public void finishDl(){
        if(null==mDlnaInterfaceDl){
            return;
        }
        mDlnaInterfaceDl.stopDtcpDl();
    }

    /**
     *
     */
    public void cancel(){
       if(null!=mDlnaInterfaceDl){
           mDlnaInterfaceDl.downloadCancel();
       }
    }
}
