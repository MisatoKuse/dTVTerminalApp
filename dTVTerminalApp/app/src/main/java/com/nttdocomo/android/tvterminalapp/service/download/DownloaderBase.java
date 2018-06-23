/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;


import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.activation.NewEnvironmentUtil;

import java.io.File;

/**
 * ダウンロード基底クラス.
 */
public abstract class DownloaderBase {
    /**ダウンロードパラメータ.*/
    private DownloadParam mDownloadParam;
    /**トータルBytesサイズ.*/
    private int mTotalBytes;
    /**エラー.*/
    private DownloadListener.DownLoadError mError;
    /**ダウンロードリスナー.*/
    private final DownloadListener mDownloadListener;
    /**d_.*/
    public static final String sDlPrefix = "d_";
    /**ダウンロード中なのか.*/
    private boolean mIsDownloading = false;

    /**
     * ダウンロードパラメータ取得.
     * @return ダウンロードパラメータ
     */
    DownloadParam getDownloadParam() {
        return mDownloadParam;
    }

    /**
     * Constructor.
     * @param param param
     * @param downloadListener リスナー
     */
    DownloaderBase(final DownloadParam param, final DownloadListener downloadListener) {
        mDownloadParam = param;
        mDownloadListener = downloadListener;
        reset();
    }

    /**
     * 新しいダウンロードする場合、使用する.
     * @param param param
     */
    public void reset(final DownloadParam param) {
        if (null == param) {
            DTVTLogger.error("DownloaderBase.reset, param is null");
            return;
        }
        mDownloadParam = param;
        reset();
    }

    /**
     * ダウンロードステータスをリセット.
     */
    private void reset() {
        mTotalBytes = 0;
        mError = DownloadListener.DownLoadError.DLError_NoError;
        setDownloading(false);
    }

    /**
     * Totalダウンロードサイズを戻す.
     * @return int
     */
    protected abstract int calculateTotalBytes();

    /**
     * ダウンロード開始.
     */
    public void start() {
        mTotalBytes = calculateTotalBytes();
        if (isStorageSpaceLow()) {
            setLowStorageSpace();
            return;
        }
        if (!isDownloading()) {
            if (null != mDownloadListener) {
                mDownloadListener.onStart(mTotalBytes);
            }
            newDl();
        }
    }

    /**
     * 新しいダウンロードタスク.
     */
    private void newDl() {
        try {
            DownLoadThread dt = new DownLoadThread();
            dt.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * 実際のダウンロードを実現.
     */
    protected abstract void download();

    /* Build.VERSION.SDK_INT >= 26の時、この関数を実現.
     * ref: https://developer.android.com/reference/android/content/Context.html#getCacheDir()
     *      getCacheQuotaBytes(java.util.UUID)
     */

    /**
     * ストレージ不足なのか取得.
     * @return true or false
     */
    protected abstract boolean isStorageSpaceLow();

    /**
     * Sub Classでダウンロード成功したとき、この関数をコール.
     */
    void onSuccess() {
        if (null != mDownloadListener) {
            String path = getFullFilePath();
            mDownloadListener.onSuccess(path);
        }
    }

    /**
     * ダウンロード進捗.
     * @param everyTimeBytes everyTimeBytes
     */
    void onProgress(final int everyTimeBytes) {
        if (null != mDownloadListener && null != mDownloadParam) {
            mDownloadListener.onProgress(everyTimeBytes);
        }
    }

    /**
     * DownLoadThread.
     */
    private class DownLoadThread extends Thread {
        @Override
        public void run() {
            setDownloading(true);
            download();
            setDownloading(false);
        }
    }

    /**
     * ダウンロードエラー発生の時、コールされる.
     * @return ダウンロードエラー取得.
     */
    DownloadListener.DownLoadError isError() {
        return mError;
    }

    /**
     * ダウンロードキャンセル.
     */
    void cancel() {
        synchronized (this) {
            DlnaManager.shared().DownloadCancel();
        }
    }

    /**
     * ダウンロードしたファイルのフルパスを取得.
     * @return ダウンロードしたファイルのフルパス
     */
    private String getFullFilePath() {
        if (null != mDownloadParam) {
            StringBuilder sb = new StringBuilder();
            String path = mDownloadParam.getSavePath();
            String name = mDownloadParam.getSaveFileName();
            if (null == path || null == name) {
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
     * ダウンロード容量不足.
     */
    private void setLowStorageSpace() {
        if (null != mDownloadListener) {
            if (null != mDownloadParam) {
                String path = getFullFilePath();
                if (null != path) {
                    mDownloadListener.onLowStorageSpace(path);
                }
            }
        }
    }

    /**
     * @return MB.
     */
    long getInnerStorageSafeSpaceMB() {
        return 500;
    }

    /**
     * Sub Classでダウンロード成功したとき、この関数をコール.
     * @param error error
     */
    void onFail(final DownloadListener.DownLoadError error) {
        setDownloading(false);
        if (null != mDownloadListener && null != mDownloadParam) {
            final String savePath = mDownloadParam.getSavePath() + File.separator + mDownloadParam.getSaveFileName();
            mDownloadListener.onFail(error, savePath);
        }
    }

    /**
     *ダウンロードをキャンセルする.
     */
    void onCancel() {
        setDownloading(false);
        if (null != mDownloadListener && null != mDownloadParam) {
            final String savePath = mDownloadParam.getSavePath() + File.separator + mDownloadParam.getSaveFileName();
            mDownloadListener.onCancel(savePath);
        }
    }

    /**
     * 機能：xml item idからストレージ名を戻す.
     * @param id xml item id
     * @return ret ret
     */
    public static String getFileNameById(final String id) {
        String ret = id;
        ret = ret.replaceAll("[^a-z^A-Z^0-9]", "_");
        return sDlPrefix + (new StringBuilder(ret)).toString();
    }

    /**
     * 機能：.
     *      １．Download Uiがなくなる場合、且サービスにqueueはない場合、必ずこれをコールする
     *      ２．Download Uiがない場合、Serviceは閉じる時、必ずこれをコールする
     */
    public abstract void stop();

    /**
     * Get save path.
     * @param context context
     * @return save path
     */
    public static String getDownloadPath(final Context context) {
        String downLoadPath = null;
        if (null == context) {
            return null;
        }

        String dmp = getDmpFolderName(context);
        if (null == dmp || dmp.isEmpty()) {
            downLoadPath = NewEnvironmentUtil.getPrivateDataHome(context, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        } else {
            File[] files = ContextCompat.getExternalFilesDirs(context, null);
            if (files != null) {
                if (files.length > 0) {
                    for (int i = files.length - 1; i >= 0; i--) {
                        File file = files[i];
                        if (file != null) {
                            downLoadPath = file.getAbsolutePath() + File.separator + dmp;
                            File dmpFile = new File(downLoadPath);
                            if (!dmpFile.exists()) {
                                boolean r = dmpFile.mkdirs();
                                if (r) {
                                    break;
                                } else {
                                    downLoadPath = null;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return downLoadPath;
    }

    /**
     * getDmpFolderName.
     * @param context コンテキスト
     * @return DmpFolderName
     */
    private static String getDmpFolderName(final Context context) {
        if (null == context) {
            return null;
        }
        String ret = EnvironmentUtil.getPrivateDataHome(context, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        String sp = File.separator;
        int i = ret.lastIndexOf(sp);
        int l = ret.length();
        if (0 > i || i >= l) {
            return "";
        }
        return ret.substring(i + 1, l);
    }

    /**
     * ダウンロード中に設定.
     * @param yn true or false
     */
    private synchronized void setDownloading(final boolean yn) {
        mIsDownloading = yn;
    }

    /**
     * ダウンロード中なのか取得.
     * @return true or false
     */
    public synchronized boolean isDownloading() {
        return mIsDownloading;
    }
}
