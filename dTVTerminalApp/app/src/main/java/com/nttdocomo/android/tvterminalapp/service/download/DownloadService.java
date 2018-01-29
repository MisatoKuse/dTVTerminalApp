/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.jni.download.DlnaProvDownload;

import java.util.ArrayList;
import java.util.List;

public class DownloadService extends Service implements DownloadListener {
    private DownloadServiceListener mDownloadServiceListener;
    private DownloaderBase mDownloaderBase;
    private String mData;
    private static final int DOWNLOAD_SERVICE_ID = 1;
    private static List<DlData> sDlDataQue = new ArrayList<>();
    private static int BINDSTATUS = 1;
    public static final int UNBINED = 1;
    public static final int BINDED = 2;
    public static final int BACKGROUD = 3;

    public static final String DONWLOAD_UPDATE = "update";
    public static final String DONWLOAD_SUCCESS = "success";
    public static final String DONWLOAD_FAIL = "fail";
    public static final String DONWLOAD_PATH = "path";
    public static final String DONWLOAD_LowStorageSpace = "lowStorageSpace";


    public static void setDlDataQue(List<DlData> dlDataQue){
        sDlDataQue = dlDataQue;
    }

    public static void setDlDataQueClear(){
        if(null != sDlDataQue){
            sDlDataQue.clear();
        }
    }

    public static void setDlDataQueRemove0(){
        if(null != sDlDataQue){
            sDlDataQue.remove(0);
        }
    }

    public static  List<DlData> getDlDataQue(){
        return sDlDataQue;
    }
    
    public void setDownloadServiceListener(DownloadServiceListener dlServiceListener){
        mDownloadServiceListener=dlServiceListener;
    }

    public static void setBindStatus(int status){
        BINDSTATUS = status;
    }

    public static int getBindStatus(){
        return BINDSTATUS;
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

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*String msg = intent.getExtras().getString("downloadStatus");
        String url = intent.getExtras().getString("url");
        String path = intent.getExtras().getString("path");
        String fileName =intent.getExtras().getString("fileName");
        if (msg.equals("start")) {
            startDownload();
        } else if (msg.equals("pause")) {
            downloader.pause();
        } else if (msg.equals("resume")) {
            downloader.resume();
        } else if (msg.equals("stop")) {
            downloader.cancel();
            stopForeground(true);
            stopSelf();
        }*/
        startService();
        return super.onStartCommand(intent, flags, startId);
    }

    public void startService(){
        startForeground(DOWNLOAD_SERVICE_ID ,getNotification(getResources().getString(R.string.record_download_notification), 0));
    }

//    private void notifyProgress(String message, int progress){
//        getNotificationManager().notify(1, getNotification(message, progress));
//    }

    private Notification getNotification(String title, int progress) {
//        Intent intent = new Intent(this, RecordedListActivity.class);
//        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.icd_app_tvterminal);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icd_app_tvterminal));
//        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }

//    private NotificationManager getNotificationManager() {
//        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//    }

    public void stopService(){
        stopForeground(true);
        stopSelf();
        boolean isUiRunning=isUiRunning();
        if(!isUiRunning) {
            DlnaProvDownload.uninitGlobalDl();
        }
    }

    private boolean isUiRunning(){
        switch (BINDSTATUS) {
            case BACKGROUD:
                return true;
            case UNBINED:
                return false;
            case BINDED:
                return true;
        }
        return true;
    }

    /**
     * DownloadParam設定
     * @param param param
     * @throws Exception Exception
     */
    public void setDlParam(DownloadParam param) throws Exception{
        if(null==mDownloaderBase){
            mDownloaderBase = new DtcpDownloader(param, this);
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
    public void onFail(DLError error, final String savePath) {
        if(null!=mDownloadServiceListener){
            mDownloadServiceListener.onFail(error, savePath);
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
    public void onCancel(final String filePath) {
        if(null!=mDownloadServiceListener){
            mDownloadServiceListener.onCancel(filePath);
        }
    }

    /**
     * コールバック
     */
    @Override
    public void onLowStorageSpace(final String fullPath) {
        if(null!=mDownloadServiceListener){
            mDownloadServiceListener.onLowStorageSpace(fullPath);
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

    /**
     * 機能：
     *      １．Download Uiがなくなる場合、且サービスにqueueはない場合、必ずこれをコールする
     *      ２．Download Uiがない場合、Serviceは閉じる時、必ずこれをコールする
     */
    public void stop(){
        if (null == mDownloaderBase) {
            return;
        }
        mDownloaderBase.stop();
    }
}
