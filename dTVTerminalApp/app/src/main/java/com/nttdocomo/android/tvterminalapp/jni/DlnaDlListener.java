/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


public interface DlnaDlListener {
    /**
     * 機能：団ロード進捗のコールバック
     * @param sizeFinished
     */
    void dlProgress(int sizeFinished);

    /**
     * 機能：団ロードステータスのコールバック
     * @param status status
     */
    void dlStatus(DlnaDlStatus status);
}
