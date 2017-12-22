/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * ダウンロードデータプロバイダー
 * Activityからこのクラスを利用する
 */
class DlDataProvider implements ServiceConnection, DownloadServiceListener {
    private DlDataProviderListener mDlDataProviderListener;
    private DownloadService.Binder mBinder;
    private Activity mActivity;

    public DlDataProvider(Activity activity, DlDataProviderListener dlDataProviderListener) throws Exception{
        if(null==activity){
            throw new Exception("DlDataProvider.DlDataProvider, null activity");
        }
        mActivity=activity;
        mDlDataProviderListener = dlDataProviderListener;
    }

    /**
     * DlDataProvider機能を有効
     */
    public void beginProvider(){
        if(null==mActivity){
            return;
        }
        Intent intent = new Intent(mActivity, DownloadService.class);
        mActivity.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    /**
     * DlDataProvider機能を無効
     */
    public void endProvider(){
        if(null==mActivity){
            return;
        }
        mActivity.unbindService(this);
    }

    public void setDlParam(DownloadParam param) throws Exception{
        DownloadService ds=getDownloadService();
        if(null!=ds){
            ds.setDlParam(param);
        }
    }

    private DownloadService getDownloadService(){
        if(null==mBinder){
            return null;
        }
        return mBinder.getDownloadService();
    }

    /**
     * ダウンロード開始
     */
    public void start(){
        DownloadService ds=getDownloadService();
        if(null!=ds){
            ds.start();
        }
    }

    /**
     * ダウンロード一時停止
     */
    public void pause(){
        DownloadService ds=getDownloadService();
        if(null!=ds){
            ds.pause();
        }
    }

    /**
     * ダウンロード再開
     */
    public void resume(){
        DownloadService ds=getDownloadService();
        if(null!=ds){
            ds.resume();
        }
    }

    /**
     * ダウンロード進捗通知
     */
    public int getProgressBytes(){
        DownloadService ds=getDownloadService();
        if(null!=ds){
            return ds.getProgressBytes();
        }
        return 0;
    }

    /**
     * ダウンロード進捗通知
     */
    public float getProgressPercent(){
        DownloadService ds=getDownloadService();
        if(null!=ds){
            return ds.getProgressPercent();
        }
        return 0.0f;
    }

    /**
     * ダウンロードエラー発生の時、コールされる
     */
    public DownloadListener.DLError isError(){
        DownloadService ds=getDownloadService();
        if(null!=ds){
            return ds.isError();
        }
        return DownloadListener.DLError.DLError_NoError;
    }

    /**
     * ダウンロードキャンセル
     */
    public void cancel(){
        DownloadService ds=getDownloadService();
        if(null!=ds){
            ds.cancel();
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mBinder = (DownloadService.Binder) iBinder;
        DownloadService ds=getDownloadService();
        if(null!=ds){
            ds.setDownloadServiceListener(this);
        }
        if(null!=mDlDataProviderListener){
            mDlDataProviderListener.dlDataProviderAvailable();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mBinder=null;
        if(null!=mDlDataProviderListener){
            mDlDataProviderListener.dlDataProviderUnavailable();
        }
    }

    @Override
    public void onStart(int totalFileByteSize) {
        if(null!=mDlDataProviderListener){
            mDlDataProviderListener.onStart(totalFileByteSize);
        }
    }

    @Override
    public void onPause() {
        if(null!=mDlDataProviderListener){
            mDlDataProviderListener.onPause();
        }
    }

    @Override
    public void onResume() {
        if(null!=mDlDataProviderListener){
            mDlDataProviderListener.onResume();
        }
    }

    @Override
    public void onProgress(int receivedBytes, int percent) {
        if(null!=mDlDataProviderListener){
            mDlDataProviderListener.onProgress(receivedBytes, percent);
        }
    }

    @Override
    public void onFail(DLError error) {
        if(null!=mDlDataProviderListener){
            mDlDataProviderListener.onFail(error);
        }
    }

    @Override
    public void onSuccess(String fullPath) {
        if(null!=mDlDataProviderListener){
            mDlDataProviderListener.onSuccess(fullPath);
        }
    }

    @Override
    public void onCancel() {
        if(null!=mDlDataProviderListener){
            mDlDataProviderListener.onCancel();
        }
    }

    @Override
    public void onLowStorageSpace() {
        if(null!=mDlDataProviderListener){
            mDlDataProviderListener.onLowStorageSpace();
        }
    }
}
