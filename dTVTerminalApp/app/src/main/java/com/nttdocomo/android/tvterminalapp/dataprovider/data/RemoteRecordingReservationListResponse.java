/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;


public class RemoteRecordingReservationListResponse implements Serializable {
    private static final long serialVersionUID = -171253737402592759L;

    private String mStatus;// status
    private String mCount;//count
    private ArrayList<RemoteRecordingReservationMetaData> mRemoteRecordingReservationMetaData;  // リモート録画予約一覧リスト

    public static final String REMOTE_RECORDING_RESERVATION_META_RESPONSE_STATUS = "status";
    public static final String REMOTE_RECORDING_RESERVATION_META_RESPONSE_COUNT = "count";
    public static final String REMOTE_RECORDING_RESERVATION_META_RESPONSE_LIST = "list";

    public String getStatus() {
        return mStatus;
    }

    public String getCount() {
        return mCount;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public void setCount(String count) {
        mCount = count;
    }

    public ArrayList<RemoteRecordingReservationMetaData> getRemoteRecordingReservationMetaData() {
        return mRemoteRecordingReservationMetaData;
    }

    public void setRemoteRecordingReservationMetaData(ArrayList<RemoteRecordingReservationMetaData> remoteRecordingReservationMetaData) {
        mRemoteRecordingReservationMetaData = remoteRecordingReservationMetaData;
    }

    public RemoteRecordingReservationListResponse() {
        mStatus = "";     // OK 固定値
        mCount = "";      //取得した予約情報の件数
        mRemoteRecordingReservationMetaData = new ArrayList<RemoteRecordingReservationMetaData>();  // リモート録画予約一覧
    }
}
