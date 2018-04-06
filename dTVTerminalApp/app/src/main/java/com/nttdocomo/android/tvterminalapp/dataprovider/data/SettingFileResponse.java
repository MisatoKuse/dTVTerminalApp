/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

/**
 * セッティングファイルの読み込み情報
 */
public class SettingFileResponse {

    //設定ファイル
    private SettingFileMetaData mSettingFile = new SettingFileMetaData();

    public SettingFileMetaData getSettingFile() {
        return mSettingFile;
    }

    public void setSettingFile(SettingFileMetaData settingFile) {
        this.mSettingFile = settingFile;
    }
}