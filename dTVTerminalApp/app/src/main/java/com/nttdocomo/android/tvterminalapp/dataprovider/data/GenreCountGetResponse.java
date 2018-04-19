/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ジャンル毎コンテンツ数取得レスポンス.
 */
public class GenreCountGetResponse implements Serializable {

    private static final long serialVersionUID = 9180129484857627225L;
    /**
     * status.
     */
    private String mStatus;
    /**
     * ジャンル毎コンテンツ数取得メタデータ.
     */
    private ArrayList<GenreCountGetMetaData> mGenreCountGetMetaData;
    /**
     * status初期値.
     */
    private static final String RECORDING_RESERVATION_FIXED = "";

    /**
     * ステータス取得.
     * @return ステータス
     */
    public String getStatus() {
        return mStatus;
    }

    /**
     * ステータス設定する.
     * @param status ステータス
     */
    public void setStatus(final String status) {
        mStatus = status;
    }

    /**
     * ジャンル毎コンテンツ数メタデータ取得.
     * @return ジャンルメタデータ
     */
    public ArrayList<GenreCountGetMetaData> getGenreCountGetMetaData() {
        return mGenreCountGetMetaData;
    }

    /**
     * ジャンル毎コンテンツ数メタデータ設定.
     * @param mGenreCountGetMetaData ジャンル毎コンテンツ数メタデータ
     */
    public void setGenreCountGetMetaData(final ArrayList<GenreCountGetMetaData> mGenreCountGetMetaData) {
        this.mGenreCountGetMetaData = mGenreCountGetMetaData;
    }

    /**
     * ジャンル毎コンテンツ数取得レスポンス構造.
     */
    public GenreCountGetResponse() {
        mStatus = RECORDING_RESERVATION_FIXED;     // OK 固定値
        mGenreCountGetMetaData = new ArrayList<GenreCountGetMetaData>();
    }
}
