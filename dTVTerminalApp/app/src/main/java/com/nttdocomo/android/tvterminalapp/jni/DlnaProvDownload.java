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

    /**
     * 機能：DlnaProvRecVideoを構造
     */
    public DlnaProvDownload() {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * 機能：Listenを停止
     */
    public void stopListen(){
        DTVTLogger.start();
        DlnaInterface di= DlnaInterface.getInstance();
        if(null==di){
            DTVTLogger.end();
            return;
        }
        di.setDlnaDlListener(null, null);
        DTVTLogger.end();
    }

    /**
     * 機能：Listenを始める
     * @param lis listener
     * @return 成功true
     */
    public boolean startListen(DlnaDmsItem item, DlnaDlListener lis, Context context){
        DTVTLogger.start();
        DlnaInterface di= DlnaInterface.getInstance();
        if(null==di){
            DTVTLogger.end();
            return false;
        }
        if(!di.startDlna()){
            DTVTLogger.end();
            return false;
        }
        boolean ret = di.registerCurrentDms(item);
        if(!ret){
            DTVTLogger.end();
            return false;
        }
        di.setDlnaDlListener(lis, context);
        DTVTLogger.end();
        return true;
    }

    /**
     * 機能：ダウンロード開始
     * @return 成功true
     */
    public DlnaDownloadRet download(DtcpDownloadParam param){
        DTVTLogger.start();
        DlnaInterface di= DlnaInterface.getInstance();
        if(null==di){
            DTVTLogger.end();
            return DlnaDownloadRet.DownloadRet_OtherError;
        }
        DTVTLogger.end();
        return di.download(param);
    }
}
