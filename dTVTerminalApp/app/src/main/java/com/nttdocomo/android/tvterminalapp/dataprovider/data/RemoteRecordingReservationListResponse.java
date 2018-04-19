/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * リモート録画予約一覧取得：正常時レスポンスデータ.
 */
public class RemoteRecordingReservationListResponse implements Serializable {
    private static final long serialVersionUID = -171253737402592759L;
    /**status.*/
    private String mStatus; // status
    /**予約情報の件数.*/
    private int mCount; //count
    /**リモート録画予約一覧リスト.*/
    private ArrayList<RemoteRecordingReservationMetaData> mRemoteRecordingReservationMetaData;  // リモート録画予約一覧リスト
    /**取得した予約情報の件数初期値.*/
    private static final int REMOTE_RECORDING_RESERVATION_INFO_INIT_COUNT = 0;
    /**status初期値.*/
    private static final String REMOTE_RECORDING_RESERVATION_FIXED_STATUS = "";

    /**
     * status取得.
     * @return status
     */
    public String getStatus() {
        return mStatus;
    }

    /**
     * 予約情報の件数取得.
     * @return 予約情報の件数
     */
    public int getCount() {
        return mCount;
    }

    /**
     * status設定.
     * @param status status
     */
    public void setStatus(final String status) {
        mStatus = status;
    }

    /**
     * 予約情報の件数設定.
     * @param count 予約情報の件数
     */
    public void setCount(final int count) {
        mCount = count;
    }

    /**
     * リモート録画予約一覧リスト取得.
     * @return リモート録画予約一覧リスト
     */
    public ArrayList<RemoteRecordingReservationMetaData> getRemoteRecordingReservationMetaData() {
        return mRemoteRecordingReservationMetaData;
    }

    /**
     * コンストラクタ設定.
     * @param remoteRecordingReservationMetaData リモート録画予約一覧メタデータ
     */
    public void setRemoteRecordingReservationMetaData(final ArrayList<RemoteRecordingReservationMetaData> remoteRecordingReservationMetaData) {
        mRemoteRecordingReservationMetaData = remoteRecordingReservationMetaData;
    }

    /**
     * コンストラクタ.
     */
    public RemoteRecordingReservationListResponse() {
        mStatus = REMOTE_RECORDING_RESERVATION_FIXED_STATUS;     // OK 固定値
        mCount = REMOTE_RECORDING_RESERVATION_INFO_INIT_COUNT;      //取得した予約情報の件数
        mRemoteRecordingReservationMetaData = new ArrayList<RemoteRecordingReservationMetaData>();  // リモート録画予約一覧
    }
}
