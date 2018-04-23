/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.download.DlnaDlListener;
import com.nttdocomo.android.tvterminalapp.jni.download.DlnaDlStatus;
import com.nttdocomo.android.tvterminalapp.jni.download.DlnaDownloadRet;
import com.nttdocomo.android.tvterminalapp.jni.download.DlnaProvDownload;

import java.io.File;

/**
 * tcpダウンローダークラス.
 */
public class DtcpDownloader extends DownloaderBase implements DlnaDlListener {
    /**DlnaProvDownloadインスタンス.*/
    private DlnaProvDownload mDlnaProvDownload;

    /**
     * Dtcpダウンローダー.
     *
     * @param param ダウンロードパラメータ
     * @param downloadListener リスナー
     */
    DtcpDownloader(final DownloadParam param, final DownloadListener downloadListener) {
        super(param, downloadListener);
        DtcpDownloadParam p = (DtcpDownloadParam) param;
        mDlnaProvDownload = new DlnaProvDownload(p.getSavePath());
    }

    @Override
    protected int calculateTotalBytes() {
        int ret = 0;
        DtcpDownloadParam param;
        try {
            param = (DtcpDownloadParam) getDownloadParam();
            ret = param.getCleartextSize();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        return ret;
    }

    @Override
    protected void download() {
        DTVTLogger.debug("dtcp download begin, files");
        printDlPathFiles();
        //mFinishedBytes=0;
        DtcpDownloadParam param = (DtcpDownloadParam) getDownloadParam();
        if (!param.isParamValid()) {
            onFail(DownloadListener.DLError.DLError_ParamError);
            return;
        }

        Context context = param.getContext();
        if (null == context) {
            onFail(DownloadListener.DLError.DLError_Other);
            return;
        }

        DlnaDownloadRet res = mDlnaProvDownload.startListen(this, context, param.getSavePath(), param.getPercentToNotity());
        if (DlnaDownloadRet.DownloadRet_Succeed != res) {
            errors(res);
            return;
        }

        res = mDlnaProvDownload.download(param);
        errors(res);
    }

    /**
     * エラー.
     * @param res ダウンロード結果
     */
    private void errors(final DlnaDownloadRet res) {
        switch (res) {
            case DownloadRet_CopyKeyFileFailed:
                onFail(DownloadListener.DLError.DLError_CopyKeyFileFailed);
                break;
            case DownloadRet_ParamError:
                onFail(DownloadListener.DLError.DLError_ParamError);
                break;
            case DownloadRet_Unactivated:
                onFail(DownloadListener.DLError.DLError_Unactivated);
                break;
            case DownloadRet_OtherError:
                onFail(DownloadListener.DLError.DLError_Other);
                break;
            case DownloadRet_Succeed:
                break;
        }
    }

    @Override
    protected boolean isStorageSpaceLow() {
        DownloadParam param =  getDownloadParam();
        if (null == param) {
            return true;
        }
        try {
            DtcpDownloadParam dp = (DtcpDownloadParam) param;
            String path = dp.getSavePath();
            if (null == path || path.isEmpty()) {
                return true;
            }
            File f = new File(path);
            if (!f.exists()) {
                if (!f.mkdirs()) {
                    return true;
                }
            }
            long usableSpace = (f.getUsableSpace() / 1024) / 1024;  //-->MB
            long safeSpace = getInnerStorageSafeSpaceMB();
            int dlSize = (dp.getCleartextSize() / 1024) / 1024; //-->MB
            return (usableSpace - dlSize) < safeSpace;
        } catch (Exception e) {
            //TODO :汎用例外catchは削除する事.必要な例外をキャッチして必要な処理を記載する事.
            DTVTLogger.debug(e);
        }
        return true;
    }

    @Override
    protected void cancelImpl() {
        if (null != mDlnaProvDownload) {
            mDlnaProvDownload.cancel();
        }
    }

    @Override
    public void dlProgress(final int sizeFinished) {
        onProgress(sizeFinished);
    }

    @Override
    public void dlStatus(final DlnaDlStatus status) {
        switch (status) {
            case DOWNLOADER_STATUS_COMPLETED:
                onSuccess();
                onStopIt();
                break;
            case DOWNLOADER_STATUS_ERROR_OCCURED:
                onFail(DownloadListener.DLError.DLError_Download);
                onStopIt();
                break;
            case DOWNLOADER_STATUS_CANCELLED:
                onCancel();
                onStopIt();
                break;
            case DOWNLOADER_STATUS_UNKNOWN:
                break;
            case DOWNLOADER_STATUS_MOVING:
                break;
            //TODO :必ずdefault文を記載する事.
        }
    }

    /**
     *ダウンロードパスーを出力する.
     */
    private void printDlPathFiles() {
        try {
            DtcpDownloadParam dp = (DtcpDownloadParam) getDownloadParam();
            if (null != dp) {
                String path = dp.getSavePath();
                if (null != path) {
                    File f = new File(path);
                    File[] files = f.listFiles();
                    DTVTLogger.debug("------------------------");
                    if (null != files) {
                        for (File f2 : files) {
                            if (null != f2) {
                                DTVTLogger.debug(f2.getAbsolutePath() + ", size=" + f2.length());
                            }
                        }
                    }
                    DTVTLogger.debug("------------------------");
                }
            }
        } catch (Exception e) {
            //TODO :汎用例外catchは削除する事.必要な例外をキャッチして必要な処理を記載する事.
            DTVTLogger.debug(e);
        }
    }

    /**
     * ダウンロード停止.
     */
    private void onStopIt() {
        DTVTLogger.debug("dtcp download end, files");
        printDlPathFiles();
    }

    /**
     * 機能：.
     *      １．Download Uiがなくなる場合、且サービスにqueueはない場合、必ずこれをコールする
     *      ２．Download Uiがない場合、Serviceは閉じる時、必ずこれをコールする
     */
    @Override
    public void stop() {
        if (null == mDlnaProvDownload) {
            return;
        }
        mDlnaProvDownload.stopListen();
    }
}
