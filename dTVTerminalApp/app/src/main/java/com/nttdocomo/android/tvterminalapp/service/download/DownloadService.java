/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class DownloadService extends Service implements DownloadListener {
    private DownloadServiceListener mDownloadServiceListener;
    private DownloaderBase mDownloaderBase;
    private String mData;

    public void setDownloadServiceListener(DownloadServiceListener dlServiceListener){
        mDownloadServiceListener=dlServiceListener;
    }

    /**
     * Create
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * ダウンロード開始
     */
    public void start(){
        if(null!=mDownloaderBase){
            mDownloaderBase.start();
        }
    }

    /**
     * ダウンロード一時停止
     */
    public void pause(){
        if(null!=mDownloaderBase){
            mDownloaderBase.pause();
        }
    }

    /**
     * ダウンロード再開
     */
    public void resume(){
        if(null!=mDownloaderBase){
            mDownloaderBase.resume();
        }
    }

    /**
     * ダウンロード進捗通知
     */
    public int getProgressBytes(){
        if(null!=mDownloaderBase){
            return mDownloaderBase.getProgressBytes();
        }
        return 0;
    }

    /**
     * ダウンロード進捗通知
     */
    public float getProgressPercent(){
        if(null!=mDownloaderBase){
            return mDownloaderBase.getProgressPercent();
        }
        return 0.0f;
    }

    /**
     * ダウンロードエラー発生の時、コールされる
     */
    public DownloadListener.DLError isError(){
        if(null!=mDownloaderBase){
            return mDownloaderBase.isError();
        }
        return DLError.DLError_NoError;
    }

    /**
     * ダウンロードキャンセル
     */
    public void cancel(){
        if(null!=mDownloaderBase){
            mDownloaderBase.cancel();
        }
    }

    /**
     * Service Bind
     * @param intent intent
     * @return IBinder
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    /**
     * onUnbind
     * @param intent intent
     * @return boolean
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * DownloadParam設定
     * @param param param
     * @throws Exception Exception
     */
    public void setDlParam(DownloadParam param) throws Exception{
        if(null==mDownloaderBase){
            mDownloaderBase=new KariDownloader(param, this);
        } else {
            mDownloaderBase.reset(param);
        }
    }

    /**
     * onDestroy処理
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * コールバック
     * @param totalFileByteSize totalFileByteSize
     */
    @Override
    public void onStart(int totalFileByteSize) {
        if(null!=mDownloadServiceListener){
            mDownloadServiceListener.onStart(totalFileByteSize);
        }
    }

    /**
     * コールバック
     */
    @Override
    public void onPause() {
        if(null!=mDownloadServiceListener){
            mDownloadServiceListener.onPause();
        }
    }

    /**
     * コールバック
     */
    @Override
    public void onResume() {
        if(null!=mDownloadServiceListener){
            mDownloadServiceListener.onResume();
        }
    }

    /**
     * コールバック
     * @param receivedBytes receivedBytes
     * @param percent 0-100
     */
    @Override
    public void onProgress(int receivedBytes, int percent) {
        if(null!=mDownloadServiceListener){
            mDownloadServiceListener.onProgress(receivedBytes, percent);
        }
    }

    /**
     * コールバック
     * @param error　error
     */
    @Override
    public void onFail(DLError error) {
        if(null!=mDownloadServiceListener){
            mDownloadServiceListener.onFail(error);
        }
    }

    /**
     * コールバック
     * @param fullPath　fullPath
     */
    @Override
    public void onSuccess(String fullPath) {
        if(null!=mDownloadServiceListener){
            mDownloadServiceListener.onSuccess(fullPath);
        }
    }

    /**
     * コールバック
     */
    @Override
    public void onCancel() {
        if(null!=mDownloadServiceListener){
            mDownloadServiceListener.onCancel();
        }
    }

    /**
     * コールバック
     */
    @Override
    public void onLowStorageSpace() {
        if(null!=mDownloadServiceListener){
            mDownloadServiceListener.onLowStorageSpace();
        }
    }

    /**
     * Binderクラス
     */
    public class Binder extends android.os.Binder{
        public void setData(String data){
            DownloadService.this.mData = data;
        }
        public DownloadService getDownloadService(){
            return DownloadService.this;
        }
    }

}
