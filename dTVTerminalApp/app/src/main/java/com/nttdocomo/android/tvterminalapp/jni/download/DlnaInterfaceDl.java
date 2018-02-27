/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.download;

import android.content.Context;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaInterface;
import com.nttdocomo.android.tvterminalapp.jni.activation.NewEnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.service.download.DtcpDownloadParam;

import java.io.File;

/**
 * 機能：For Dlna download.
 */
public class DlnaInterfaceDl {
    //private Handler mHandler= new Handler();

    //Download
    private DlnaDlListener mDlnaDlListener;
    private Context mContext;
    private long mNativeDlna = 0;

    /**
     * 機能：デフォールト構造.
     */
    public DlnaInterfaceDl(final String savePath)/* throws Exception*/ {
//        if(!startDtcpDl(savePath)){
//            throw new Exception("DlnaInterfaceDl.DlnaInterfaceDl() constructor failed");
//        }
    }

    /**
     * 機能：Dlna機能を開始。「stopDtcpDl」とPairで使用しなければならない.
     *
     * @return boolean
     */
    public DlnaDownloadRet startDtcpDl(final String pathToSave, final int percentToNotify) {
        if (0 == mNativeDlna) {
            mNativeDlna = nativeCreateDlnaDownloadObject();
        }
        if (0 == mNativeDlna) {
            return DlnaDownloadRet.DownloadRet_ParamError;
        }

        String homeDtcpPath = pathToSave;  //e.g. EnvironmentUtil.getPrivateDataHome(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        String homePlayerPath = NewEnvironmentUtil.getPrivateDataHome(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER);
        String db_post = homePlayerPath + "/" + "db_post";

        File f = new File(db_post);
        boolean has = f.exists();
        if (!has) {
            DTVTLogger.end();
            return DlnaDownloadRet.DownloadRet_Unactivated;   //unactivated
        }

//        File homePlayerPathDir=new File(homePlayerPath);
//        File[] allFile=homePlayerPathDir.listFiles();
//        for(File file:allFile){
//            DTVTLogger.debug("player dir dtcp before copy db_post, ---------------->" + file.getName());
//        }
//
        File homeDtcpPathDir = new File(homeDtcpPath);
//        allFile=homeDtcpPathDir.listFiles();
//        for(File file:allFile){
//            DTVTLogger.debug("dtcp before copy db_post, ---------------->"+file.getName());
//        }

        if (!homeDtcpPathDir.exists()) {
            if (!homeDtcpPathDir.mkdirs()) {
                return DlnaDownloadRet.DownloadRet_CopyKeyFileFailed;
            }
        }

        String homeParent = getParentDir(homeDtcpPath);
        int ret = NewEnvironmentUtil.copyDeviceKeyFromOtherCMWork(mContext, homeParent, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        if (1 != ret && 3 != ret) {
            DTVTLogger.end();
            return DlnaDownloadRet.DownloadRet_CopyKeyFileFailed;
        }

        File[] allFile = homeDtcpPathDir.listFiles();
        if (null != allFile) {
            for (File file : allFile) {
                DTVTLogger.debug("dtcp after copy db_post, ---------------->" + file.getName());
            }
        }

        long id = Thread.currentThread().getId();
        DTVTLogger.debug("HandlerThread:" + id);

        boolean r = nativeStartDlna(mNativeDlna, pathToSave, percentToNotify);
        if (!r) {
            return DlnaDownloadRet.DownloadRet_OtherError;
        }

        return DlnaDownloadRet.DownloadRet_Succeed;
    }

    /**
     * 機能：Dlna機能を停止。「startDtcpDl」とPairで使用しなければならない.
     */
    public void stopDtcpDl() {
        nativeStopDlna(mNativeDlna);
    }

    /**
     * 機能：jni関数.
     * @return c++ dlna object
     */
    private native long nativeCreateDlnaDownloadObject();

    /**
     * 機能：jni c/c++からの通知を処理.
     * @param msg msg
     * @param content content
     */
    public void notifyFromNative(final int msg, final String content) {
        //DTVTLogger.debug("msg=" + msg + ", content=" + content);
        switch (msg) {
            case DlnaInterface.DLNA_MSG_ID_DL_PROGRESS:
                dlProgress(content);
                break;
            case DlnaInterface.DLNA_MSG_ID_DL_STATUS:
                dlStatus(content);
                break;
            default:
                break;
        }
    }

    /*
     * 機能：libをロードする.
     */
    static {
        System.loadLibrary("dtvtlib");
    }

    /**
     * todo: WiFiは切ると、対応する.
     */
    public void toDoWithWiFiLost() {

    }

    /**
     * 機能：端末idを取得.
     * @return String unique id
     */
    public String getUniqueId() {
        if (null == mContext) {
            return "";
        }
        return EnvironmentUtil.getCalculatedUniqueId(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
    }

    /**
     * 機能：Dlna download listenerを設定.
     * @param lis lis
     */
    public void setDlnaDlListener(final DlnaDlListener lis, final Context context) {
        mDlnaDlListener = lis;
        mContext = context;
    }

    /**
     * 機能：ダウンロード進捗のコールバック.
     * @param content size downloaded
     */
    private void dlProgress(final String content) {
        //DTVTLogger.start();
        if (null == mDlnaDlListener) {
            //DTVTLogger.end();
            return;
        }
        String size = content;
        int sizeI = 0;
        try {
            sizeI = Integer.parseInt(size);
        } catch (Exception e) {
            DTVTLogger.debug(e.getMessage());
            //DTVTLogger.end();
            return;
        }
        mDlnaDlListener.dlProgress(sizeI);
        //DTVTLogger.end();
    }

    /**
     * 機能：ダウンロードステータスのコールバック.
     * @param content
     */
    private void dlStatus(final String content) {
        DTVTLogger.start();
        if (null == mDlnaDlListener) {
            return;
        }
        String status = content;
        DlnaDlStatus statusEnum;
        try {
            int i = Integer.parseInt(status);
            statusEnum = DlnaDlStatus.values()[i];;
        } catch (Exception e) {
            DTVTLogger.debug(e.getMessage());
            DTVTLogger.end();
            return;
        }
        mDlnaDlListener.dlStatus(statusEnum);
        DTVTLogger.end();
    }

    /**
     * 機能：download.
     *
     * @return DownloadRet_Succeed: 成功　
     *          DownloadRet_Unactivated：unactivated
     *          DownloadRet_CopyKeyFileFailed: copy key file error
     *          DownloadRet_OtherError: other error
     */
    public DlnaDownloadRet download(final DtcpDownloadParam param/*, final String xml*/) {
        DTVTLogger.start();

        if (null == mContext) {
            DTVTLogger.end();
            return DlnaDownloadRet.DownloadRet_OtherError;
        }

        if (null == param || !param.isParamValid()) {
            return DlnaDownloadRet.DownloadRet_ParamError;
        }
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                long id=Thread.currentThread().getId();
//                DTVTLogger.debug("HandlerThread:"+id);
//                download(mNativeDlna, param.getSavePath(), param.getSaveFileName(), param.getDtcp1host(), param.getDtcp1port(), param.getUrl(), param.getCleartextSize(), xml);
//            }
//        });
        download(mNativeDlna, param.getSaveFileName(), param.getDtcp1host(), param.getDtcp1port(), param.getUrl(), param.getCleartextSize(), param.getXmlToDl());
        DTVTLogger.end();
        return DlnaDownloadRet.DownloadRet_Succeed;
    }

    private String getParentDir(final String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            boolean r = f.mkdirs();
            if (!r) {
                return "";
            }
        }
        return f.getParent();
    }

    public void downloadCancel() {
        downloadCancel(mNativeDlna);
    }

    /**
     * 機能：jni関数.
     * @param prt prt
     * @param fileNameToSave fileNameToSave
     * @param dtcp1host dtcp1host
     * @param dtcp1port dtcp1port
     * @param url url
     * @param cleartextSize cleartextSize
     */
    private native void download(long prt, String fileNameToSave, String dtcp1host, int dtcp1port, String url, int cleartextSize, String itemId);

    /**
     * 機能：jni関数.
     * @return 操作結果
     */
    private native boolean nativeStartDlna(long prt, String pathToSave, int percentToNotify);

    /**
     * 機能：jni関数.
     */
    private native void nativeStopDlna(long prt);

//    /**
//     * 機能：jni関数.
//     * @return 操作結果
//     */
//    private native boolean nativeStop(long prt);

    /**
     * Download Cancel.
     * @param prt prt
     */
    private native void downloadCancel(long prt);

    public static native boolean initGlobalDl(String saveDir);

    public static native void uninitGlobalDl();
}