/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import android.content.Context;
import android.os.Handler;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.activation.NewEnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.service.download.DtcpDownloadParam;

import java.io.File;

/**
 * 機能：For Dlna download
 */
public class DlnaInterfaceDl {
    private Handler mHandler= new Handler();

    //Download
    private DlnaDlListener mDlnaDlListener;
    private Context mContext;
    private long mNativeDlna = 0;

    /**
     * 機能：デフォールト構造
     */
    public DlnaInterfaceDl() throws Exception{
        if(!startDlna()){
            throw new Exception("DlnaInterfaceDl.DlnaInterfaceDl() constructor failed");
        }
    }

    /**
     * 機能：Dlna機能を開始
     *
     * @return boolean
     */
    private boolean startDlna(){
        if (0 == mNativeDlna) {
            mNativeDlna = nativeCreateDlnaDownloadObject();
        }
        if(0==mNativeDlna){
            return false;
        }
        return nativeStartDlna(mNativeDlna);
    }

    /**
     * 機能：jni関数
     * @return c++ dlna object
     */
    private native long nativeCreateDlnaDownloadObject();

    /**
     * 機能：jni c/c++からの通知を処理
     * @param msg msg
     * @param content content
     */
    public void notifyFromNative(int msg, String content) {
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
     * 機能：libをロードする
     */
    static {
        System.loadLibrary("dtvtlib");
    }

    /**
     * todo: WiFiは切ると、対応する
     */
    public void toDoWithWiFiLost(){

    }

    /**
     * 機能：端末idを取得
     * @return String unique id
     */
    public String getUniqueId() {
        if(null==mContext){
            return "";
        }
        return EnvironmentUtil.getCalculatedUniqueId(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
    }

    /**
     * 機能：Dlna download listenerを設定
     * @param lis lis
     */
    public void setDlnaDlListener(DlnaDlListener lis, Context context){
        mDlnaDlListener=lis;
        mContext= context;
    }

    /**
     * 機能：団ロード進捗のコールバック
     * @param content size downloaded
     */
    private void dlProgress(String content) {
        DTVTLogger.start();
        if(null==mDlnaDlListener){
            DTVTLogger.end();
            return;
        }
        String size=content;
        int sizeI=0;
        try{
            sizeI=Integer.parseInt(size);
        }catch (Exception e){
            DTVTLogger.debug(e.getMessage());
            DTVTLogger.end();
            return;
        }
        mDlnaDlListener.dlProgress(sizeI);
        DTVTLogger.end();
    }

    /**
     * 機能：団ロードステータスのコールバック
     * @param content
     */
    private void dlStatus(String content) {
        DTVTLogger.start();
        if(null==mDlnaDlListener){
            return;
        }
        String status=content;
        DlnaDlStatus statusEnum;
        try{
            int i=Integer.parseInt(status);
            statusEnum = DlnaDlStatus.values()[i];;
        }catch (Exception e){
            DTVTLogger.debug(e.getMessage());
            DTVTLogger.end();
            return;
        }
        mDlnaDlListener.dlStatus(statusEnum);
        DTVTLogger.end();
    }

    /**
     * 機能：download
     *
     * @return DownloadRet_Succeed: 成功　
     *          DownloadRet_Unactivated：unactivated
     *          DownloadRet_CopyKeyFileFailed: copy key file error
     *          DownloadRet_OtherError: other error
     */
    public DlnaDownloadRet download(final DtcpDownloadParam param, final String xml) {
        DTVTLogger.start();

        if(null==mContext){
            DTVTLogger.end();
            return DlnaDownloadRet.DownloadRet_OtherError;
        }

        if(null==param || !param.isParamValid()){
            return DlnaDownloadRet.DownloadRet_ParamError;
        }

        String homeDtcpPath = param.getSavePath();  //e.g. EnvironmentUtil.getPrivateDataHome(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        String homePlayerPath = EnvironmentUtil.getPrivateDataHome(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER);
        String db_post = homePlayerPath + "/" + "db_post";

        File f=new File(db_post);
        boolean has=f.exists();
        if(!has){
            DTVTLogger.end();
            return DlnaDownloadRet.DownloadRet_Unactivated;   //unactivated
        }

        File homePlayerPathDir=new File(homePlayerPath);
        File[] allFile=homePlayerPathDir.listFiles();
        for(File file:allFile){
            DTVTLogger.debug("player dir dtcp before copy db_post, ---------------->" + file.getName());
        }

        File homeDtcpPathDir=new File(homeDtcpPath);
        allFile=homeDtcpPathDir.listFiles();
        for(File file:allFile){
            DTVTLogger.debug("dtcp before copy db_post, ---------------->"+file.getName());
        }

        String homeParent= getParentDir(homeDtcpPath);
        int ret= NewEnvironmentUtil.copyDeviceKeyFromOtherCMWork(mContext, homeParent, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        if(1!=ret && 3!=ret){
            DTVTLogger.end();
            return DlnaDownloadRet.DownloadRet_CopyKeyFileFailed;
        }

        allFile=homeDtcpPathDir.listFiles();
        for(File file:allFile){
            DTVTLogger.debug("dtcp after copy db_post, ---------------->"+ file.getName());
        }

        long id=Thread.currentThread().getId();
        DTVTLogger.debug("HandlerThread:"+id);

//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                long id=Thread.currentThread().getId();
//                DTVTLogger.debug("HandlerThread:"+id);
//                download(mNativeDlna, param.getSavePath(), param.getSaveFileName(), param.getDtcp1host(), param.getDtcp1port(), param.getUrl(), param.getCleartextSize(), xml);
//            }
//        });
        download(mNativeDlna, param.getSavePath(), param.getSaveFileName(), param.getDtcp1host(), param.getDtcp1port(), param.getUrl(), param.getCleartextSize(), xml);
        DTVTLogger.end();
        return DlnaDownloadRet.DownloadRet_Succeed;
    }

    private String getParentDir(String dir){
        File f=new File(dir);
        if(!f.exists()){
            return "";
        }
        return f.getParent();
    }

    public void downloadCancel(){
        downloadCancel(mNativeDlna);
    }

    /**
     * 機能：jni関数
     * @param prt prt
     * @param dirToSave dirToSave
     * @param fileNameToSave fileNameToSave
     * @param dtcp1host dtcp1host
     * @param dtcp1port dtcp1port
     * @param url url
     * @param cleartextSize cleartextSize
     */
    private native void download(long prt, String dirToSave, String fileNameToSave, String dtcp1host, int dtcp1port, String url, int cleartextSize, String itemId);

    /**
     * 機能：jni関数
     * @return 操作結果
     */
    private native boolean nativeStartDlna(long prt);

    /**
     * Download Cancel
     * @param prt prt
     */
    private native void downloadCancel(long prt);
}