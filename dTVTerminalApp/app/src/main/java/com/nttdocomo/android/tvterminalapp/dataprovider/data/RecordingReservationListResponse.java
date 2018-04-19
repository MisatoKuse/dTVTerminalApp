/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;


import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 録画予約一覧レスポンス.
 */
public class RecordingReservationListResponse {
    /**ステータス.*/
    private String mStatus = null;
    /**録画予約情報受信時刻.*/
    private String mReservation = null;
    /**ページャー.*/
    private String mPager = null;
    /**録画予約一覧リスト.*/
    private ArrayList<RecordingReservationMetaData> mRecordingReservationMetaData;
    /**録画予約情報受信時刻初期値.*/
    private static final String RECORDING_RESERVATION_FIXED = "";
    /**
     * ステータス取得.
     * @return ステータス
     */
    public String getStatus() {
        return mStatus;
    }
    /**
     * 録画予約情報受信時刻取得.
     * @return 録画予約情報受信時刻
     */
    public String getReservation() {
        return mReservation;
    }
    /**
     * ページャ取得.
     * @return ページャ
     */
    public String getPager() {
        return mPager;
    }

    /**
     * ステータス設定.
     * @param status 　ステータス
     */
    public void setStatus(final String status) {
        mStatus = status;
    }

    /**
     * 録画予約情報受信時刻設定 .
     * @param reservation 録画予約情報受信時刻
     */
    public void setReservation(final String reservation) {
        mReservation = reservation;
    }

    /**
     * ページャ設定.
     * @param pager  ページャ
     */
    public void setPager(final JSONObject pager) {
        mPager = pager.toString();
    }

    /**
     * 録画予約一覧リスト取得.
     * @return 録画予約一覧リスト
     */
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