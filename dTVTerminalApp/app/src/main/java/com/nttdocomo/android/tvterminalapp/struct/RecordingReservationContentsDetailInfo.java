/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

/**
 * 録画予約コンテンツ詳細クラス.
 * 　　機能： コンテンツ詳細を管理するクラスである
 */
public class RecordingReservationContentsDetailInfo {
    /**
     * 放送種別.
     */
    private int mPlatformType;
    /**
     * サービスID.
     */
    private final String mServiceId;
    /**
     * イベントID(番組指定予約の場合必須).
     */
    private String mEventId = null;
    /**
     * 番組タイトル（STB予約リストに表示するタイトル）.
     */
    private final String mTitle;
    /**
     * 録画予約開始時間.
     */
    private final long mStartTime;
    /**
     * 予約時間の長さ.
     */
    private final long mDuration;
    /**
     * 定期予約指定値:0～10（イベントIDありの場合0固定）.
     */
    private static final int sLoopTypeNum = 0;
    /**
     * 予約種類（地デジ、BSの場合必須）.
     */
    private static final int sResvType = 1;
    /**
     * パレンタル設定値.
     */
    private final String mRValue;

    /**
     * コンストラク.
     * @param serviceId サービスID
     * @param title タイトル
     * @param startTime 録画予約開始時間
     * @param duration 予約時間の長さ
     * @param rValue パレンタル設定値
     */
    public RecordingReservationContentsDetailInfo(
            final String serviceId,
            final String title,
            final long startTime,
            final int duration,
            final String rValue) {
        mServiceId = serviceId;
        mTitle = title;
        mStartTime = startTime;
        mDuration = duration;
        mRValue = rValue;

    }

    /**
     * イベントIdを設定.
     * @param eventId イベントId
     */
    public void setEventId(final String eventId) {
        mEventId = eventId;
    }

    /**
     * 放送種別取得.
     * @return 放送種別
     */
    public int getPlatformType() {
        return mPlatformType;
    }

    /**
     * 放送種別設定.
     * @param mPlatformType 放送種別
     */
    public void setPlatformType(final int mPlatformType) {
        this.mPlatformType = mPlatformType;
    }

    /**
     * サービスID取得.
     * @return サービスID
     */
    public String getServiceId() {
        return mServiceId;
    }

    /**
     * イベントID(番組指定予約の場合必須)取得.
     * @return イベントID(番組指定予約の場合必須)
     */
    public String getEventId() {
        return mEventId;
    }

    /**
     * 番組タイトル（STB予約リストに表示するタイトル）取得.
     * @return 番組タイトル
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * 録画予約開始時間取得.
     * @return 録画予約開始時間
     */
    public long getStartTime() {
        return mStartTime;
    }

    /**
     * 予約時間の長さ取得.
     * @return 予約時間の長さ
     */
    public long getDuration() {
        return mDuration;
    }

    /**
     * 定期予約指定値:0～10（イベントIDありの場合0固定）取得.
     * @return 定期予約指定値:0～10（イベントIDありの場合0固定）
     */
    public int getLoopTypeNum() {
        return sLoopTypeNum;
    }

    /**
     * 予約種類.
     * @return 予約種類（地デジ、BSの場合必須）
     */
    public int getResvType() {
        return sResvType;
    }

    /**
     * パレンタル設定値取得.
     * @return パレンタル設定値
     */
    public String getRValue() {
        return mRValue;
    }

    /**
     * toString.
     * @return String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("plat:" + getPlatformType())
                .append(" sId:" + getServiceId())
                .append(" eId:" + getEventId())
                .append(" title:" + getTitle())
                .append(" start:" + getStartTime())
                .append(" dur:" + getDuration())
                .append(" loop:" + getLoopTypeNum())
                .append(" rval:" + getRValue());
        return builder.toString();
    }

}
