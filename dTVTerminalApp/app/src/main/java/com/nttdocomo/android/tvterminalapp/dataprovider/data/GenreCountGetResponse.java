/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;


public class GenreCountGetResponse implements Serializable {

    private static final long serialVersionUID = 9180129484857627225L;
    private String mStatus; //status
    private ArrayList<GenreCountGetMetaData> mGenreCountGetMetaData; //ジャンル毎コンテンツ数取得メタデータ
    public static final String GENRE_COUNT_GET_RESPONSE_STATUS = "status";
    public static final String GENRE_COUNT_GET_RESPONSE_LIST = "list";
    private static final String RECORDING_RESERVATION_FIXED = "";

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(final String status) {
        mStatus = status;
    }

    public ArrayList<GenreCountGetMetaData> getGenreCountGetMetaData() {
        return mGenreCountGetMetaData;
    }

    public void setGenreCountGetMetaData(final ArrayList<GenreCountGetMetaData> mGenreCountGetMetaData) {
        this.mGenreCountGetMetaData = mGenreCountGetMetaData;
    }

    public GenreCountGetResponse() {
        mStatus = RECORDING_RESERVATION_FIXED;     // OK 固定値
        mGenreCountGetMetaData = new ArrayList<GenreCountGetMetaData>();
    }
}
