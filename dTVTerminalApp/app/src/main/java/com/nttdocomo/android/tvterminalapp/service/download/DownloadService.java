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

import java.util.ArrayList;
import java.util.List;

/**
 * ダウンロードサービスクラス.
 */
public class DownloadService extends Service implements DownloadListener {
    /**ダウンロードサービスクラスリスナー.*/
    private DownloadServiceListener mDownloadServiceListener;
    /**DownloaderBase.*/
    private DownloaderBase mDownloaderBase;
    /**ダウンロードサービスID.*/
    private static final int DOWNLOAD_SERVICE_ID = 1;
    /**ダウンロードデータキュー.*/
    private static List<DownloadData> sDownloadDataQue = new ArrayList<>();
    /**UI更新あるか.*/
    private boolean mIsUiRunning = false;

    //broadcast type
    /**プログレース.*/
    public static final String DOWNLOAD_ON_PROGRESS = "onProgress";
    /**ダウンロード成功.*/
    public static final String DOWNLOAD_ON_SUCCESS = "onSuccess";
    /**ダウンロード失敗.*/
    public static final String DOWNLOAD_ON_FAIL = "onFail";
    /**メモリ不足.*/
    public static final String DOWNLOAD_ON_LOW_STORAGE_SPACE = "onLowStorageSpace";
    /**ダウンロードタスク全キャンセル.*/
    public static final String DOWNLOAD_ON_CANCEL_ALL = "OnCancelAll";
    /**ダウンロードタスクキャンセル.*/
    public static final String DOWNLOAD_ON_CANCEL = "onCancel";
    /**ダウンロード開始.*/
    public static final String DOWNLOAD_ON_START = "onStart";
    /**ダウンロードデータプロバイダー使用可.*/
    public static final String DOWNLOAD_DL_DATA_PROVIDER_AVAILABLE = "onDlDataProviderAvailable";
    /**ダウンロードデータプロバイダー使用不可.*/
    public static final String DOWNLOAD_DL_DATA_PROVIDER_UNAVAILABLE = "onDDataProviderUnavailable";

    //broadcast param type
    /**broadcast param type　int.*/
    public static final String DOWNLOAD_PARAM_INT = "paramInt";
    /**broadcast param type  string.*/
    public static final String DOWNLOAD_PARAM_STRING = "paramString";

    /**
     * ダウンロードデータキュー設定.
     * @param downloadDataQue ダウンロードデータキュー
     */
    public synchronized static void setDlDataQue(final List<DownloadData> downloadDataQue) {
        sDownloadDataQue = downloadDataQue;
    }
    /**ダウンロードデータキュークリア.*/
    public synchronized static void setDlDataQueClear() {
        if (null != sDownloadDataQue) {
            sDownloadDataQue.clear();
        }
    }
    /**ダウンロードデータキューから先頭アイテム取り除く.*/
    public synchronized static void setDlDataQueRemove0() {
        if (null != sDownloadDataQue) {
            sDownloadDataQue.remove(0);
        }
    }

    /**
     * ダウンロードデータキュー.
     * @return ダウンロードデータキュー
     */
    public synchronized static  List<DownloadData> getDlDataQue() {
        return sDownloadDataQue;
    }

    /**
     * ダウンロードサービス稼働中か.
     * @return true or false
     */
    public static synchronized boolean isDownloadServiceRunning() {
        return (null != sDownloadDataQue) && 0 < sDownloadDataQue.size();
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
     * ダウンロードエラー発生の時、コールされる.
     * @return ダウンロードエラータイプ
     */
    public DownLoadError isError() {
        if (null != mDownloaderBase) {
            return mDownloaderBase.isError();
        }
        return DownLoadError.DLError_NoError;
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
     * ダウンロード開始.
     */
    private void startService() {
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
            DownloadDataProvider.releaseInstance();
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

    @Override
    public void onStart(final int totalFileByteSize) {
        if (null != mDownloadServiceListener) {
            mDownloadServiceListener.onStart(totalFileByteSize);
        }
    }

    @Override
    public void onProgress(final int percent) {
        if (null != mDownloadServiceListener) {
            mDownloadServiceListener.onProgress(percent);
        }
    }

    @Override
    public void onFail(final DownLoadError error, final String savePath) {
        if (null != mDownloadServiceListener) {
            mDownloadServiceListener.onFail(error, savePath);
        }
    }

    @Override
    public void onSuccess(final String fullPath) {
        if (null != mDownloadServiceListener) {
            mDownloadServiceListener.onSuccess(fullPath);
        }
    }

    @Override
    public void onCancel(final String filePath) {
        if (null != mDownloadServiceListener) {
            mDownloadServiceListener.onCancel(filePath);
        }
    }

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
        return null != mDownloaderBase && mDownloaderBase.isDownloading();
    }
}
