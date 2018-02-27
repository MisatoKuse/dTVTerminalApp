/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;


public class ContentsDetailGetResponse implements Serializable {

    private static final long serialVersionUID = 5073727609109258522L;

    /**
     * ステータス.
     */
    private String mStatus;

    /**
     * VOD＆番組マージメタデータ（フル版）.
     */
    private ArrayList<VodMetaFullData> mVodProgramMetaData;

    public static final String GENRE_COUNT_GET_RESPONSE_STATUS = "status";
    public static final String GENRE_COUNT_GET_RESPONSE_LIST = "list";
    private static final String RECORDING_RESERVATION_FIXED = "";

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(final String status) {
        mStatus = status;
    }

    public ArrayList<VodMetaFullData> getVodMetaFullData() {
        return mVodProgramMetaData;
    }

    public void setVodMetaFullData(final ArrayList<VodMetaFullData> vodMetaData) {
        this.mVodProgramMetaData = vodMetaData;
    }

    /**
     * コンストラクタ.
     */
    public ContentsDetailGetResponse() {
        // 空文字で初期化
        mStatus = RECORDING_RESERVATION_FIXED;

        //VOD＆番組マージメタデータ（フル版）の初期化
        mVodProgramMetaData = new ArrayList<>();
    }
}
