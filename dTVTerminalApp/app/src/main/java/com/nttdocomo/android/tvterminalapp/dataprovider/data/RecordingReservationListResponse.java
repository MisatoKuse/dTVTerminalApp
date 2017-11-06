/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;


import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import org.json.JSONArray;

import java.util.ArrayList;

public class RecordingReservationListResponse {
    private String mStatus;// status
    private String[] mPager;//pager
    private ArrayList<RecordingReservationMetaData> mRecordingReservationMetaData;  // 録画予約一覧リスト

    public static final String RECORDING_RESERVATION_META_RESPONSE_STATUS = "status";
    public static final String RECORDING_RESERVATION_META_RESPONSE_PAGER = "pager";
    public static final String RECORDING_RESERVATION_META_RESPONSE_RESERVATION_LIST = "reservation_list";

    public String getStatus() {
        return mStatus;
    }

    public String[] getPager() {
        return mPager;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public void setPager(JSONArray pager) {
        mPager = DBUtils.toStringArray(pager);
    }

    public ArrayList<RecordingReservationMetaData> getRecordingReservationMetaData() {
        return mRecordingReservationMetaData;
    }

    public void setRecordingReservationMetaData(ArrayList<RecordingReservationMetaData> recordingReservationMetaData) {
        mRecordingReservationMetaData = recordingReservationMetaData;
    }

    public RecordingReservationListResponse() {
        mStatus = "";     // OK 固定値
        mPager = new String[0];
        mRecordingReservationMetaData = new ArrayList<RecordingReservationMetaData>();  // 録画予約一覧
    }
}
