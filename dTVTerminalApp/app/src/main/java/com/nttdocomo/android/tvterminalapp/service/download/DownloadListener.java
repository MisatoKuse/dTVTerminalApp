/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;


public interface DownloadListener {

    /**
     * ダウンロードエラー定義
     */
    public enum DLError{
        DLError_NetLost,
        DLError_DmsLost,
        DLError_DmsError,
        DLError_StorageNoSpace,
        DLError_Other,
    }

    /**
     * ダウンロード開始の時、コールされる
     * @param fileByteSize fileByteSize
     */
    void onStart(int fileByteSize);

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
}
