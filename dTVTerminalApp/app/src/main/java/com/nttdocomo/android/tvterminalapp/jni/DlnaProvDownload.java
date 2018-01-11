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
    public DlnaProvDownload() throws Exception{
        DTVTLogger.start();
        mDlnaInterfaceDl=new DlnaInterfaceDl();
        DTVTLogger.end();
    }

    /**
     * 機能：Listenを始める
     * @param lis listener
     * @return 成功true
     */
    public boolean startListen(DlnaDlListener lis, Context context){
        DTVTLogger.start();
        if(null==mDlnaInterfaceDl) {
            DTVTLogger.end();
            return false;
        }
        mDlnaInterfaceDl.setDlnaDlListener(lis, context);
        DTVTLogger.end();
        return true;
    }

    /**
     * 機能：ダウンロード開始
     * @return 成功true
     */
    public DlnaDownloadRet download(DtcpDownloadParam param){
        DTVTLogger.start();

        DlnaInterface di = DlnaInterface.getInstance();
        if(null==di) {
            return DlnaDownloadRet.DownloadRet_ParamError;
        }

        String xml= di.getDlParam(param);
        if(null==xml || 0==xml.length()){
            return DlnaDownloadRet.DownloadRet_ParamError;
        }

        if(null==mDlnaInterfaceDl){
            DTVTLogger.end();
            return DlnaDownloadRet.DownloadRet_OtherError;
        }
        DTVTLogger.end();
        return mDlnaInterfaceDl.download(param, xml);
    }
}
