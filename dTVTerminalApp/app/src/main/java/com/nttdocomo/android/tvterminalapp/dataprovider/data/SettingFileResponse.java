/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

/**
 * セッティングファイルの読み込み情報.
 */
public class SettingFileResponse {

    /**設定ファイル.*/
    private SettingFileMetaData mSettingFile = new SettingFileMetaData();

    /**
     * 設定ファイル取得.
     * @return 設定ファイル
     */
    public SettingFileMetaData getSettingFile() {
        return mSettingFile;
    }

    /**
     * 設定ファイル設定.
     * @param settingFile 設定ファイル
     */
    public void setSettingFile(final SettingFileMetaData settingFile) {
        this.mSettingFile = settingFile;
    }
}