/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.detail;

import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

/*
 * 録画予約コンテンツ詳細クラス
 * 　　機能： コンテンツ詳細を管理するクラスである
 */
public class RecordingReservationContentsDetailInfo {
    /**
     * 定期予約指定値
     */
    // 単発予約
    public static int REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_0 = 0;
    // 毎月曜日
    public static int REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_1 = 1;
    // 毎火曜日
    public static int REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_2 = 2;
    // 毎水曜日
    public static int REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_3 = 3;
    // 毎木曜日
    public static int REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_4 = 4;
    // 毎金曜日
    public static int REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_5 = 5;
    // 毎土曜日
    public static int REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_6 = 6;
    // 毎日曜日
    public static int REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_7 = 7;
    // 毎月～金曜日
    public static int REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_8 = 8;
    // 毎月～土曜日
    public static int REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_9 = 9;
    // 毎日
    public static int REMOTE_RECORDING_RESERVATION_LOOP_TYPE_NUM_10 = 10;

    /**
     * 放送種別 1:多チャンネル放送
     */
    private int mPlatformType = 1;
    /**
     * サービスID
     */
    private String mServiceId;
    /**
     * イベントID(番組指定予約の場合必須)
     */
    private String mEventId = null;
    /**
     * 番組タイトル（STB予約リストに表示するタイトル）
     */
    private String mTitle;
    /**
     * 録画予約開始時間
     */
    private long mStartTime;
    /**
     * 予約時間の長さ
     */
    private long mDuration;
    /**
     * 定期予約指定値:0～10（イベントIDありの場合0固定）
     */
    private int mLoopTypeNum = 0;
    /**
     * パレンタル設定値
     */
    private String mRValue;

    public RecordingReservationContentsDetailInfo(
            String serviceId,
            String title,
            long startTime,
            int duration,
            String rValue) {
        mServiceId = serviceId;
        mTitle = title;
        mStartTime = startTime;
        mDuration = duration;
        mRValue = rValue;

    }

    public void setEventId(String eventId) {
        mEventId = eventId;
    }

    public void setLoopTypeNum(int loopTypeNum) {
        // 0以外の場合、開始時間は0時00分00秒からの時間となる
        if (loopTypeNum != 0) {
            DateUtils.getCalculationRecordingReservationStartTime(mStartTime);
        }
        mLoopTypeNum = loopTypeNum;
    }

    public int getPlatformType() {
        return mPlatformType;
    }

    public String getServiceId() {
        return mServiceId;
    }

    public String getEventId() {
        return mEventId;
    }

    public String getTitle() {
        return mTitle;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getDuration() {
        return mDuration;
    }

    public int getLoopTypeNum() {
        return mLoopTypeNum;
    }

    public String getRValue() {
        return mRValue;
    }

}
