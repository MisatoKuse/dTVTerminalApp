/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;


public class ContentsDetailGetResponse implements Serializable {

    private static final long serialVersionUID = 5073727609109258522L;

    /**
     * ステータス
     */
    private String mStatus;

    /**
     * VOD＆番組マージメタデータ（フル版）
     */
    private ArrayList<VodProgramMetaFullData> mVodProgramMetaData;

    public static final String GENRE_COUNT_GET_RESPONSE_STATUS = "status";
    public static final String GENRE_COUNT_GET_RESPONSE_LIST = "list";
    public static final String RECORDING_RESERVATION_FIXED = "";

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public ArrayList<VodProgramMetaFullData> getVodProgramMetaFullData() {
        return mVodProgramMetaData;
    }

    public void setVodProgramMetaFullData(ArrayList<VodProgramMetaFullData> vodMetaData) {
        this.mVodProgramMetaData = vodMetaData;
    }

    /**
     * コンストラクタ
     */
    public ContentsDetailGetResponse() {
        // 空文字で初期化
        mStatus = RECORDING_RESERVATION_FIXED;

        //VOD＆番組マージメタデータ（フル版）の初期化
        mVodProgramMetaData = new ArrayList<>();
    }
}
