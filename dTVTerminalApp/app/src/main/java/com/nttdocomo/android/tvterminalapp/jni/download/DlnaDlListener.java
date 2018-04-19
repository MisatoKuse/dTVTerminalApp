/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.download;

/**
 * dlnaダウンロードリスナーインタフェース.
 */
public interface DlnaDlListener {
    /**
     * 機能：ダウンロード進捗のコールバック.
     * @param sizeFinished sizeFinished
     */
    void dlProgress(final int sizeFinished);

    /**
     * 機能：ダウンロードステータスのコールバック.
     * @param status status
     */
    void dlStatus(final DlnaDlStatus status);
}
