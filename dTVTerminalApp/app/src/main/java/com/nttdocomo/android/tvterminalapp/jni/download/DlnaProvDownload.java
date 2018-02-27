/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.download;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.service.download.DtcpDownloadParam;

/**
 * 機能：DlnaからActivityにDownload I/Fを提供するクラス.
 */
public class DlnaProvDownload {
    private DlnaInterfaceDl mDlnaInterfaceDl = null;

    /**
     * 機能：DlnaProvRecVideoを構造.
     *
     * @param savePath 保存パス
     */
    public DlnaProvDownload(final String savePath) {
        DTVTLogger.start();
        mDlnaInterfaceDl = new DlnaInterfaceDl(savePath);
        DTVTLogger.end();
    }

    /**
     * 機能：Listenを始める.
     * @param lis listener
     * @return 成功true
     */
    public DlnaDownloadRet startListen(final DlnaDlListener lis, final Context context, final String savePath, final int percentToNotify) {
        DTVTLogger.start();
        if (null == mDlnaInterfaceDl) {
            DTVTLogger.end();
            return DlnaDownloadRet.DownloadRet_ParamError;
        }
        mDlnaInterfaceDl.setDlnaDlListener(lis, context);
        DTVTLogger.end();
        return mDlnaInterfaceDl.startDtcpDl(savePath, percentToNotify);
    }

    /**
     * 機能：ダウンロード開始.
     * @return 成功true
     */
    public DlnaDownloadRet download(final DtcpDownloadParam param) {
        DTVTLogger.start();
        if (null == mDlnaInterfaceDl) {
            DTVTLogger.end();
            return DlnaDownloadRet.DownloadRet_OtherError;
        }
        DTVTLogger.end();
        return mDlnaInterfaceDl.download(param);
    }

//    /**
//     * 機能：.
//     *      １．Download Uiがなくなる場合、必ずこれをコールする
//     *      ２．Download Uiがない場合、Serviceは閉じる時、必ずこれをコールする
//     */
//    public void finishDl(){
//        if(null==mDlnaInterfaceDl){
//            return;
//        }
//        mDlnaInterfaceDl.stopDtcpDl();
//    }

    /**
     * 機能：.
     *      １．Download Uiがなくなる場合、且サービスにqueueはない場合、必ずこれをコールする
     *      ２．Download Uiがない場合、Serviceは閉じる時、必ずこれをコールする
     */
    public void stopListen() {
        if (null == mDlnaInterfaceDl) {
            return;
        }
        mDlnaInterfaceDl.stopDtcpDl();
    }

    /**
     *
     */
    public void cancel() {
       if (null != mDlnaInterfaceDl) {
           mDlnaInterfaceDl.downloadCancel();
       }
    }

    public static boolean initGlobalDl(final String saveDir) {
        return DlnaInterfaceDl.initGlobalDl(saveDir);
    }

    public static void uninitGlobalDl(){
        DlnaInterfaceDl.uninitGlobalDl();
    }
}
