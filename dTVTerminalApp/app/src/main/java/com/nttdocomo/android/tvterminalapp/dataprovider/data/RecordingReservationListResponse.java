/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;


import org.json.JSONObject;

import java.util.ArrayList;

public class RecordingReservationListResponse {
    private String mStatus = null;
    private String mReservation = null;
    private String mPager = null;
    private ArrayList<RecordingReservationMetaData> mRecordingReservationMetaData;  // 録画予約一覧リスト

    private static final String RECORDING_RESERVATION_FIXED = "";

    public String getStatus() {
        return mStatus;
    }

    public String getReservation() {
        return mReservation;
    }

    public String getPager() {
        return mPager;
    }

    public void setStatus(final String status) {
        mStatus = status;
    }

    public void setReservation(final String reservation) {
        mReservation = reservation;
    }

    public void setPager(final JSONObject pager) {
        mPager = pager.toString();
    }

    public ArrayList<RecordingReservationMetaData> getRecordingReservationMetaData() {
        return mRecordingReservationMetaData;
    }

    /**
     * コンストラクタ.
     *
     * @param recordingReservationMetaData 　録画予約メタデータ
     */
    public void setRecordingReservationMetaData(final ArrayList<RecordingReservationMetaData> recordingReservationMetaData) {
        mRecordingReservationMetaData = recordingReservationMetaData;
    }

    /**
     * コンストラクタ.
     */
    public RecordingReservationListResponse() {
        // OK 固定値
        mStatus = RECORDING_RESERVATION_FIXED;
        // 録画予約情報受信時刻
        mReservation = null;
        // ページャ
        mPager = RECORDING_RESERVATION_FIXED;
        // 録画予約一覧
        mRecordingReservationMetaData = new ArrayList<RecordingReservationMetaData>();
    }
}