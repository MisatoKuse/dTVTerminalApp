/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.jni.download.DlnaProvDownload;

import java.util.ArrayList;
import java.util.List;

//import android.support.v7.app.NotificationCompat;

public class DownloadService extends Service implements DownloadListener {
    private DownloadServiceListener mDownloadServiceListener;
    private DownloaderBase mDownloaderBase;
    private String mData;
    private static final int DOWNLOAD_SERVICE_ID = 1;
    private static List<DlData> sDlDataQue = new ArrayList<>();
    private boolean mIsUiRunning = false;

    //broadcast type
    public static final String DONWLOAD_OnProgress = "onProgress";
    public static final String DONWLOAD_OnSuccess = "onSuccess";
    public static final String DONWLOAD_OnFail = "onFail";
    public static final String DONWLOAD_OnLowStorageSpace = "onLowStorageSpace";
    public static final String DONWLOAD_OnCancelAll = "OnCancelAll";
    public static final String DONWLOAD_OnCancel = "onCancel";
    public static final String DONWLOAD_OnStart = "onStart";
    public static final String DONWLOAD_DlDataProviderAvailable = "onDlDataProviderAvailable";
    public static final String DONWLOAD_DlDataProviderUnavailable = "onDDataProviderUnavailable";

    //broadcast param type
    public static final String DONWLOAD_ParamInt = "paramInt";
    public static final String DONWLOAD_ParamString = "paramString";


    public synchronized static void setDlDataQue(final List<DlData> dlDataQue) {
        sDlDataQue = dlDataQue;
    }

    public synchronized static void setDlDataQueClear() {
        if (null != sDlDataQue) {
            sDlDataQue.clear();
        }
    }

    public synchronized static void setDlDataQueRemove0() {
        if (null != sDlDataQue) {
            sDlDataQue.remove(0);
        }
    }

    public synchronized static  List<DlData> getDlDataQue(){
        return sDlDataQue;
    }

    public static synchronized boolean isDownloadServiceRunning() {
        return (null != sDlDataQue) && 0 < sDlDataQue.size();
    }

    public void setDownloadServiceListener(final DownloadServiceListener dlServiceListener) {
        mDownloadServiceListener = dlServiceListener;
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
    public void start() {
        if (null != mDownloaderBase) {
            mDownloaderBase.start();
        }
    }

    /**
     * ダウンロード進捗通知.
     */
    public int getProgressBytes() {
        if (null != mDownloaderBase) {
            return mDownloaderBase.getProgressBytes();
        }
        return 0;
    }

    /**
     * ダウンロード進捗通知.
     */
    public float getProgressPercent() {
        if (null != mDownloaderBase) {
            return mDownloaderBase.getProgressPercent();
        }
        return 0.0f;
    }

    /**
     * ダウンロードエラー発生の時、コールされる.
     */
    public DLError isError() {
        if (null != mDownloaderBase) {
            return mDownloaderBase.isError();
        }
        return DLError.DLError_NoError;
    }

    /**
     * ダウンロードキャンセル.
     */
    public void cancel() {
        if (null != mDownloaderBase) {
            mDownloaderBase.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        startService();
        return super.onStartCommand(intent, flags, startId);
    }

    public void startService() {
        startForeground(DOWNLOAD_SERVICE_ID, getNotification(getResources().getString(R.string.record_download_notification), 0));
    }

    private Notification getNotification(final String title, final int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "");
        builder.setSmallIcon(R.mipmap.icd_app_tvterminal);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icd_app_tvterminal));
        builder.setContentTitle(title);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }

    public void stopService() {
        stopForeground(true);
        stopSelf();
        boolean isUiRunning = isUiRunning();
        if (!isUiRunning) {
            DlnaProvDownload.uninitGlobalDl();
            DlDataProvider.releaseInstance();
        }
    }

    public boolean isUiRunning() {
        return mIsUiRunning;
    }

    /**
     * DownloadParam設定.
     * @param param param
     */
    public void setDlParam(final DownloadParam param) {
        if (null == mDownloaderBase) {
            mDownloaderBase = new DtcpDownloader(param, this);
        } else {
            mDownloaderBase.reset(param);
        }
    }

    /**
     * onDestroy処理.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * コールバック.
     * @param totalFileByteSize totalFileByteSize
     */
    @Override
    public void onStart(final int totalFileByteSize) {
        if (null != mDownloadServiceListener) {
            mDownloadServiceListener.onStart(totalFileByteSize);
        }
    }

    /**
     * コールバック.
     * @param receivedBytes receivedBytes
     * @param percent 0-100
     */
    @Override
    public void onProgress(final int receivedBytes, final int percent) {
        if (null != mDownloadServiceListener) {
            mDownloadServiceListener.onProgress(receivedBytes, percent);
        }
    }

    /**
     * コールバック.
     * @param error　error
     */
    @Override
    public void onFail(final DLError error, final String savePath) {
        if (null != mDownloadServiceListener) {
            mDownloadServiceListener.onFail(error, savePath);
        }
    }

    /**
     * コールバック.
     * @param fullPath　fullPath
     */
    @Override
    public void onSuccess(final String fullPath) {
        if (null != mDownloadServiceListener) {
            mDownloadServiceListener.onSuccess(fullPath);
        }
    }

    /**
     * コールバック.
     */
    @Override
    public void onCancel(final String filePath) {
        if (null != mDownloadServiceListener) {
            mDownloadServiceListener.onCancel(filePath);
        }
    }

    /**
     * コールバック.
     */
    @Override
    public void onLowStorageSpace(final String fullPath) {
        if (null != mDownloadServiceListener) {
            mDownloadServiceListener.onLowStorageSpace(fullPath);
        }
    }

    /**
     * Binderクラス.
     */
    public class Binder extends android.os.Binder {
        public void setData(final String data) {
            DownloadService.this.mData = data;
        }
        public DownloadService getDownloadService() {
            return DownloadService.this;
        }
    }

    /**
     * 機能：.
     *      １．Download Uiがなくなる場合、且サービスにqueueはない場合、必ずこれをコールする
     *      ２．Download Uiがない場合、Serviceは閉じる時、必ずこれをコールする
     */
    public void stop() {
        if (null == mDownloaderBase) {
            return;
        }
        mDownloaderBase.stop();
    }

    /**
     * Ui runningを設定.
     */
    public void setUiRunning(final boolean yn) {
        mIsUiRunning = yn;
    }

    public synchronized boolean isDownloading() {
        if (null == mDownloaderBase) {
            return false;
        }
        return mDownloaderBase.isDownloading();
    }
}
