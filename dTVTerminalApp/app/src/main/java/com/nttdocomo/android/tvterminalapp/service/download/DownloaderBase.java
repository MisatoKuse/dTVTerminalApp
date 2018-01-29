/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;


import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.activation.NewEnvironmentUtil;

import java.io.File;

public abstract class DownloaderBase {

    private DownloadParam mDownloadParam;
    private int mDownloadedBytes;
    private int mTotalBytes;
    private DownloadListener.DLError mError;
//    private boolean mIsPause;
//    private boolean mIsCanceled;
    private DownloadListener mDownloadListener;
//    private int mNotifiedBytes;
    public static final String sDlPrefix = "d_";

    protected void setDownloadedBytes(int bytesDone){
        mDownloadedBytes=bytesDone;
    }

    public DownloadParam getDownloadParam() {
        return mDownloadParam;
    }

    /**
     * Constructor
     * @param param param
     * @throws Exception Exception
     */
    DownloaderBase(DownloadParam param, DownloadListener downloadListener) throws Exception{
        if(null==param){
            throw new Exception("DownloaderBase.DownloaderBase, param is null");
        }
        mDownloadParam = param;
        mDownloadListener = downloadListener;
        reset();
    }

    /**
     * 新しいダウンロードする場合、使用する
     * @param param param
     * @throws Exception Exception
     */
    public void reset(DownloadParam param)throws Exception{
        if(null==param){
            throw new Exception("DownloaderBase.reset, param is null");
        }
        mDownloadParam = param;
        reset();
    }

    /**
     * ダウンロードステータスをリセット
     */
    protected void reset(){
        mDownloadedBytes=0;
        mTotalBytes=0;
        mError= DownloadListener.DLError.DLError_NoError;
//        mIsPause=false;
//        mIsCanceled=false;
    }

    /**
     * Totalダウンロードサイズを戻す
     * @return int
     */
    protected abstract int calculateTotalBytes();

    /**
     * ダウンロード開始
     */
    public void start(){
//        synchronized (this) {
//            mIsPause=false;
//            mIsCanceled=false;
//        }
        mTotalBytes = calculateTotalBytes();
        if(isStorageSpaceLow()){
            setLowStorageSpace();
            return;
        }
        if(null!=mDownloadListener){
            mDownloadListener.onStart(mTotalBytes);
        }
        newDl();
    }

    private void newDl(){
        try {
            DownLoadThread dt = new DownLoadThread();
            dt.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * 実際のダウンロードを実現
     */
    protected abstract void download();

    /* Build.VERSION.SDK_INT >= 26の時、この関数を実現
     * ref: https://developer.android.com/reference/android/content/Context.html#getCacheDir()
     *      getCacheQuotaBytes(java.util.UUID)
     */
    protected abstract boolean isStorageSpaceLow();

    /**
     * Sub Classでダウンロード成功したとき、この関数をコール
     */
    protected void onSuccess() {
        if(null!=mDownloadListener){
            String path=getFullFilePath();
            mDownloadListener.onSuccess(path);
        }
    }

    protected void onProgress(int everyTimeBytes){
        mDownloadedBytes+=everyTimeBytes;
        if(null!=mDownloadListener && null != mDownloadParam){
            int total = mDownloadParam.getTotalSizeToDl();
            if(0==total){
                return;
            }
            int ff = (int)( ( (float)mDownloadedBytes)/((float) total) * 100 );
            if(100<ff){
                ff = 100;
            }
            if(ff<0){
                ff=0;
            }
            mDownloadListener.onProgress(mDownloadedBytes, ff);
        }
    }

    /**
     * ダウンロード一時停止
     */
    void pause(){
//        synchronized (this) {
//            mIsPause=true;
//            mIsCanceled=false;
//        }
        if(null==mDownloadListener){
            return;
        }
        mDownloadListener.onPause();
    }

    class DownLoadThread extends Thread {
        @Override
        public void run() {
//            if (mIsCanceled || mIsPause) {
//                cancelImpl();
//                return;
//            }
            //DTVTLogger.start();
            download();
            //DTVTLogger.end();
        }
    }

    /**
     * ダウンロード再開
     */
    void resume(){
//        synchronized (this) {
//            mIsPause=false;
//            mIsCanceled=false;
//        }
        if(null!=mDownloadListener){
            mDownloadListener.onResume();
        }
        newDl();
    }

    /**
     * ダウンロード進捗通知
     */
    int getProgressBytes(){
        return mDownloadedBytes;
    }

    /**
     * ダウンロード進捗通知
     */
    float getProgressPercent(){
        if(0==mTotalBytes){
            return 0.0f;
        }
        return ((float)mDownloadedBytes)/((float)mTotalBytes);
    }

    /**
     * ダウンロードエラー発生の時、コールされる
     */
    DownloadListener.DLError isError(){
        return mError;
    }

    /**
     * ダウンロードキャンセル
     */
    void cancel(){
        synchronized (this) {
//            mIsPause=false;
//            mIsCanceled=true;
            cancelImpl();
        }
    }

    private String getFullFilePath() {
        if (null != mDownloadParam) {
            StringBuilder sb=new StringBuilder();
            String path=mDownloadParam.getSavePath();
            String name=mDownloadParam.getSaveFileName();
            if(null==path || null==name){
                return null;
            }
            sb.append(path);
            sb.append(File.separator);
            sb.append(name);
            return sb.toString();
        }
        return null;
    }


    /**
     * Sub Classでダウンロード成功したとき、この関数をコール
     */
    protected abstract void cancelImpl();

    /**
     * ダウンロード容量不足
     */
    void setLowStorageSpace(){
//        synchronized (this) {
//            mIsPause=true;
//        }
        if(null!=mDownloadListener){
            if(null!=mDownloadParam){
                String path=getFullFilePath();
                if(null!=path){
                    mDownloadListener.onLowStorageSpace(path);
                }
            }
        }
    }

    /**
     * todo
     * @return MB
     */
    long getInnerStorageSafeSpace(){
        return 200;
    }

    /**
     * Sub Classでダウンロード成功したとき、この関数をコール
     */
    protected void onFail(DownloadListener.DLError error) {
        if(null!=mDownloadListener && null!=mDownloadParam){
            final String savePath= mDownloadParam.getSavePath() + File.separator + mDownloadParam.getSaveFileName();
            mDownloadListener.onFail(error, savePath);
        }
    }

    /**
     *
     */
    protected void onCancel(){
        if(null!=mDownloadListener && null!=mDownloadParam){
            final String savePath= mDownloadParam.getSavePath() + File.separator + mDownloadParam.getSaveFileName();
            mDownloadListener.onCancel(savePath);
        }
    }

    /**
     * 機能：xml item idからストレージ名を戻す
     * @param id xml item id
     * @return ret ret
     */
    public static String getFileNameById(String id) {
        String ret=id;
        ret=ret.replaceAll("[^a-z^A-Z^0-9]", "_");
        return sDlPrefix + (new StringBuilder(ret)).toString();
    }

    /**
     * 機能：
     *      １．Download Uiがなくなる場合、且サービスにqueueはない場合、必ずこれをコールする
     *      ２．Download Uiがない場合、Serviceは閉じる時、必ずこれをコールする
     */
    public abstract void stop();

    /**
     * Get save path
     * @return
     */
    public static String getDownloadPath(Context context){
        String downLoadPath = null;
        if(null == context){
            return null;
        }

        String dmp=getDmpFolderName(context);
        if(null==dmp || dmp.isEmpty()){
            downLoadPath = NewEnvironmentUtil.getPrivateDataHome(context, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        } else {
            File[] files = ContextCompat.getExternalFilesDirs(context, null);
            if (files != null) {
                if (files.length > 0) {
                    for (int i = files.length - 1; i >= 0; i--) {
                        File file = files[i];
                        if (file != null) {
                            downLoadPath = file.getAbsolutePath() + File.separator + dmp;
                            break;
                        }
                    }
                }
            }
        }
        return downLoadPath;
    }

    public static String getDmpFolderName(final Context context){
        if(null==context){
            return null;
        }
        String ret= EnvironmentUtil.getPrivateDataHome(context, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        String sp= File.separator;
        int i= ret.lastIndexOf(sp);
        int l=ret.length();
        if(0>i || i>=l){
            return "";
        }
        return ret.substring(i+1, l);
    }
}
