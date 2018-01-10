/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

interface DownloadListener {

    /**
     * ダウンロードエラー定義
     */
    enum DLError{
        DLError_NetLost,
        DLError_DmsLost,
        DLError_DmsError,
        DLError_StorageNoSpace,
        DLError_Download,
        DLError_ParamError,
        DLError_Unactivated,
        DLError_CopyKeyFileFailed,
        DLError_Other,
        DLError_NoError,
    }

    /**
     * ダウンロード開始の時、コールされる
     * @param totalFileByteSize totalFileByteSize
     */
    void onStart(int totalFileByteSize);

    /**
     * ダウンロード一時停止の時、コールされる
     */
    void onPause();

    /**
     * ダウンロード再開の時、コールされる
     */
    void onResume();

    /**
     * ダウンロード進捗通知の時、コールされる
     * @param receivedBytes receivedBytes
     *  @param percent 0-100
     */
    void onProgress(int receivedBytes, int percent);

    /**
     * ダウンロードエラー発生の時、コールされる
     * @param error　error
     */
    void onFail(DLError error);

    /**
     * ダウンロード完了の時、コールされる
     * @param fullPath　fullPath
     */
    void onSuccess(String fullPath);

    /**
     * ダウンロードキャンセルの時、コールされる
     */
    void onCancel();

    /**
     * ダウンロード容量不足の場合、コールされる
     */
    void onLowStorageSpace();
}
