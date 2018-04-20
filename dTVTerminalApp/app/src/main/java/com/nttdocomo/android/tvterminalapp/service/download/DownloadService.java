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

/**
 * ダウンロードサービスクラス.
 */
public class DownloadService extends Service implements DownloadListener {
    /**ダウンロードサービスクラスリスナー.*/
    private DownloadServiceListener mDownloadServiceListener;
    /**DownloaderBase.*/
    private DownloaderBase mDownloaderBase;
    /**data.*/
    private String mData;
    /**ダウンロードサービスID.*/
    private static final int DOWNLOAD_SERVICE_ID = 1;
    /**ダウンロードデータキュー.*/
    private static List<DlData> sDlDataQue = new ArrayList<>();
    /**UI更新あるか.*/
    private boolean mIsUiRunning = false;

    //broadcast type
    /**プログレース.*/
    public static final String DONWLOAD_OnProgress = "onProgress";
    /**ダウンロード成功.*/
    public static final String DONWLOAD_OnSuccess = "onSuccess";
    /**ダウンロード失敗.*/
    public static final String DONWLOAD_OnFail = "onFail";
    /**メモリ不足.*/
    public static final String DONWLOAD_OnLowStorageSpace = "onLowStorageSpace";
    /**ダウンロードタスク全キャンセル.*/
    public static final String DONWLOAD_OnCancelAll = "OnCancelAll";
    /**ダウンロードタスクキャンセル.*/
    public static final String DONWLOAD_OnCancel = "onCancel";
    /**ダウンロード開始.*/
    public static final String DONWLOAD_OnStart = "onStart";
    /**ダウンロードデータプロバイダー使用可.*/
    public static final String DONWLOAD_DlDataProviderAvailable = "onDlDataProviderAvailable";
    /**ダウンロードデータプロバイダー使用不可.*/
    public static final String DONWLOAD_DlDataProviderUnavailable = "onDDataProviderUnavailable";

    //broadcast param type
    /**broadcast param type　int.*/
    public static final String DONWLOAD_ParamInt = "paramInt";
    /**broadcast param type  string.*/
    public static final String DONWLOAD_ParamString = "paramString";

    /**
     * ダウンロードデータキュー設定.
     * @param dlDataQue ダウンロードデータキュー
     */
    public synchronized static void setDlDataQue(final List<DlData> dlDataQue) {
        sDlDataQue = dlDataQue;
    }
    /**ダウンロードデータキュークリア.*/
    public synchronized static void setDlDataQueClear() {
        if (null != sDlDataQue) {
            sDlDataQue.clear();
        }
    }
    /**ダウンロードデータキューから先頭アイテム取り除く.*/
    public synchronized static void setDlDataQueRemove0() {
        if (null != sDlDataQue) {
            sDlDataQue.remove(0);
        }
    }

    /**
     * ダウンロードデータキュー.
     * @return ダウンロードデータキュー
     */
    public synchronized static  List<DlData> getDlDataQue() {
        return sDlDataQue;
    }

    /**
     * ダウンロードサービス稼働中か.
     * @return true or false
     */
    public static synchronized boolean isDownloadServiceRunning() {
        return (null != sDlDataQue) && 0 < sDlDataQue.size();
    }

    /**
     * ダウンロードリスナー設定.
     * @param dlServiceListener ダウンロードリスナー
     */
    public void setDownloadServiceListener(final DownloadServiceListener dlServiceListener) {
        mDownloadServiceListener = dlServiceListener;
    }

    /**
     * Create.
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * ダウンロード開始.
     */
    public void start() {
        if (null != mDownloaderBase) {
            mDownloaderBase.start();
        }
    }

    /**
     * ダウンロード進捗通知.
     * @return  ダウンロード進捗
     */
    public int getProgressBytes() {
        if (null != mDownloaderBase) {
            return mDownloaderBase.getProgressBytes();
        }
        return 0;
    }

    /**
     * ダウンロード進捗通知.
     * @return ダウンロード進捗
     */
    public float getProgressPercent() {
        if (null != mDownloaderBase) {
            return mDownloaderBase.getProgressPercent();
        }
        return 0.0f;
    }

    /**
     * ダウンロードエラー発生の時、コールされる.
     * @return ダウンロードエラータイプ
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
    public IBinder onBind(final Intent intent) {
        return new Binder();
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return true;
    }

    @Override
    public void onRebind(final Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        startService();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * ダウンロード開始開始.
     */
    public void startService() {
        startForeground(DOWNLOAD_SERVICE_ID, getNotification(getResources().getString(R.string.record_download_notification), 0));
    }

    /**
     * 通知表示.
     * @param title ダウンロード中番組タイトル.
     * @param progress  ダウンロード進捗.
     * @return 通知
     */
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

    /**
     * ダウンロード停止.
     */
    public void stopService() {
        stopForeground(true);
        stopSelf();
        boolean isUiRunning = isUiRunning();
        if (!isUiRunning) {
            DlnaProvDownload.uninitGlobalDl();
            DlDataProvider.releaseInstance();
        }
    }

    /**
     * UI更新あるか.
     * @return true or false
     */
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
        /**
         * setData.
         * @param data data
         */
        public void setData(final String data) {
            DownloadService.this.mData = data;
        }

        /**
         * DownloadService取得.
         * @return  DownloadService
         */
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
     * @param yn true or false
     */
    public void setUiRunning(final boolean yn) {
        mIsUiRunning = yn;
    }

    /**
     * ダウンロード中か.
     * @return true or false
     */
    public synchronized boolean isDownloading() {
        if (null == mDownloaderBase) {
            return false;
        }
        return mDownloaderBase.isDownloading();
    }
}
