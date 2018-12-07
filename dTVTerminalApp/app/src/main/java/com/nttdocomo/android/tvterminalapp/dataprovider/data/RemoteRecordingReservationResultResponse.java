/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

/**
 * リモート録画予約登録リザルト：レスポンスデータ.
 */
public class RemoteRecordingReservationResultResponse {
    /**ステータスNG.*/
    public static final String REMOTE_RECORDING_RESERVATION_RESULT_RESPONSE_STATUS_NG = "NG";
    /**エラー番号初期値.*/
    private static final String REMOTE_RECORDING_RESERVATION_FIXED_ERROR_NO = "";
    /**ステータス.*/
    private String mStatus; // Status(OK or NG)
    /**エラー番号.*/
    private String mErrorNo = REMOTE_RECORDING_RESERVATION_FIXED_ERROR_NO; //エラー番号
    /**登録件数超過.*/
    public static final String REMOTE_RECORDING_RESERVATION_OVER_REGISTRATION = "V005201";

    /**
     * ステータス取得.
     * @return ステータス
     */
    public String getStatus() {
        return mStatus;
    }

    /**
     * ステータス設定.
     * @param status ステータス
     */
    public void setStatus(final String status) {
        mStatus = status;
    }

    /**
     * エラー番号取得.
     * @return エラー番号
     */
    public String getErrorNo() {
        return mErrorNo;
    }

    /**
     * エラー番号設定.
     * @param errorNo エラー番号
     */
    public void setErrorNo(final String errorNo) {
        mErrorNo = errorNo;
    }

    /**
     * コンストラクタ.
     * @param result ステータス.
     * @param errorNo エラー番号
     */
    public RemoteRecordingReservationResultResponse(final String result, final String errorNo) {
        mStatus = result;
        mErrorNo = errorNo;
    }
}
