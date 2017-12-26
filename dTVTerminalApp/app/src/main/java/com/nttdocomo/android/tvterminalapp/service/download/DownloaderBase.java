/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;


import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.File;

abstract class DownloaderBase {

    private DownloadParam mDownloadParam;
    private int mDownloadedBytes;
    private int mTotalBytes;
    private DownloadListener.DLError mError;
    private boolean mIsPause;
    private boolean mIsCanceled;
    private DownloadListener mDownloadListener;
    private DownLoadThread mDownLoadThread;

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
        mError=DownloadListener.DLError.DLError_NoError;
        mIsPause=false;
        mIsCanceled=false;
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
        synchronized (this) {
            mIsPause=false;
            mIsCanceled=false;
        }
        mTotalBytes = calculateTotalBytes();
        if(null!=mDownloadListener){
            mDownloadListener.onStart(mTotalBytes);
        }
        mDownLoadThread = new DownLoadThread();
        mDownLoadThread.start();
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
            StringBuilder path=new StringBuilder();
            path.append(mDownloadParam.getSavePath());
            path.append(File.separator);
            path.append(mDownloadParam.getSaveFileName());
            mDownloadListener.onSuccess(path.toString());
        }
    }

    protected void onProgress(int everyTimeBytes){
        if(mTotalBytes > 0){
            float res = everyTimeBytes / mTotalBytes;
            int p = mDownloadParam.getPercentToNotity();
            float pp = p * 0.01f;
            if (res >= pp) {
                float f1 = ((float) mDownloadedBytes);
                float f2 = ((float) mTotalBytes);
                int ff = (int) ((f1 / f2) * 100);
                mDownloadListener.onProgress(mDownloadedBytes, ff);
            }
        }
    }

    /**
     * ダウンロード一時停止
     */
    void pause(){
        synchronized (this) {
            mIsPause=true;
            mIsCanceled=false;
        }
        if(null==mDownloadListener){
            return;
        }
        mDownloadListener.onPause();
    }

    class DownLoadThread extends Thread {
        @Override
        public void run() {
            if (mIsCanceled || mIsPause) {
                return;
            }
            download();
        }
    }

    /**
     * ダウンロード再開
     */
    void resume(){
        synchronized (this) {
            mIsPause=false;
            mIsCanceled=false;
        }
        if(null!=mDownloadListener){
            mDownloadListener.onResume();
        }
        mDownLoadThread.start();
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
        return mDownloadedBytes/mTotalBytes;
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
            mIsPause=false;
            mIsCanceled=true;
        }
        try{
            mDownLoadThread.join();
        }catch (Exception e){
            DTVTLogger.debug("DownloaderBase.cancel, cause=" + e.getCause());
        }
        if(null!=mDownloadListener){
            mDownloadListener.onCancel();
        }
    }

    /**
     * ダウンロード容量不足
     */
    void setLowStorageSpace(){
        synchronized (this) {
            mIsPause=true;
        }
        if(null!=mDownloadListener){
            mDownloadListener.onLowStorageSpace();
        }
    }

    /**
     * todo
     * @return MB
     */
    long getInnerStorageSafeSpace(){
        return 200;
    }
}
