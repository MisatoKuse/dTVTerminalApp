/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

/**
 * リモート録画予約一覧取得：レスポンスデータ.
 */
public class RemoteRecordingReservationResultResponse {

    public static final String REMOTE_RECORDING_RESERVATION_RESULT_RESPONSE_STATUS = "status";
    public static final String REMOTE_RECORDING_RESERVATION_RESULT_RESPONSE_ERROR_NO = "errorno";
    public static final String REMOTE_RECORDING_RESERVATION_RESULT_RESPONSE_STATUS_NG = "NG";
    private static final String REMOTE_RECORDING_RESERVATION_FIXED_ERROR_NO = "";

    private String mStatus; // Status(OK or NG)
    private String mErrorNo = REMOTE_RECORDING_RESERVATION_FIXED_ERROR_NO; //エラー番号

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(final String status) {
        mStatus = status;
    }

    public String getErrorNo() {
        return mErrorNo;
    }

    public void setErrorNo(final String errorNo) {
        mErrorNo = errorNo;
    }

    /**
     * コンストラクタ.
     */
    public RemoteRecordingReservationResultResponse(final String result, final String errorNo) {
        mStatus = result;
        mErrorNo = errorNo;
    }
}
