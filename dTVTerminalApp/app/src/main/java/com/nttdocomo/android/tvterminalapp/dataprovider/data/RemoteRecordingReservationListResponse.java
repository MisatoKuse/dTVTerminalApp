/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * リモート録画予約一覧取得：正常時レスポンスデータ
 */
public class RemoteRecordingReservationListResponse implements Serializable {
    private static final long serialVersionUID = -171253737402592759L;

    private String mStatus;// status
    private int mCount;//count
    private ArrayList<RemoteRecordingReservationMetaData> mRemoteRecordingReservationMetaData;  // リモート録画予約一覧リスト

    public static final String REMOTE_RECORDING_RESERVATION_META_RESPONSE_STATUS = "status";
    public static final String REMOTE_RECORDING_RESERVATION_META_RESPONSE_COUNT = "count";
    public static final String REMOTE_RECORDING_RESERVATION_META_RESPONSE_LIST = "list";
    private static final int REMOTE_RECORDING_RESERVATION_INFO_INIT_COUNT = 0;
    private static final String REMOTE_RECORDING_RESERVATION_FIXED_STATUS = "";

    public String getStatus() {
        return mStatus;
    }

    public int getCount() {
        return mCount;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public ArrayList<RemoteRecordingReservationMetaData> getRemoteRecordingReservationMetaData() {
        return mRemoteRecordingReservationMetaData;
    }

    /**
     * コンストラクタ
     * @param remoteRecordingReservationMetaData リモート録画予約一覧メタデータ
     */
    public void setRemoteRecordingReservationMetaData(ArrayList<RemoteRecordingReservationMetaData> remoteRecordingReservationMetaData) {
        mRemoteRecordingReservationMetaData = remoteRecordingReservationMetaData;
    }

    /**
     * コンストラクタ
     */
    public RemoteRecordingReservationListResponse() {
        mStatus = REMOTE_RECORDING_RESERVATION_FIXED_STATUS;     // OK 固定値
        mCount = REMOTE_RECORDING_RESERVATION_INFO_INIT_COUNT;      //取得した予約情報の件数
        mRemoteRecordingReservationMetaData = new ArrayList<RemoteRecordingReservationMetaData>();  // リモート録画予約一覧
    }
}
