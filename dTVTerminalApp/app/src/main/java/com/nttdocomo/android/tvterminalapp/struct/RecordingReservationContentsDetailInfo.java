/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

/*
 * 録画予約コンテンツ詳細クラス.
 * 　　機能： コンテンツ詳細を管理するクラスである
 */
public class RecordingReservationContentsDetailInfo {
    /**
     * 放送種別 1:多チャンネル放送.
     */
    private int mPlatformType = 1;
    /**
     * サービスID.
     */
    private String mServiceId;
    /**
     * イベントID(番組指定予約の場合必須).
     */
    private String mEventId = null;
    /**
     * 番組タイトル（STB予約リストに表示するタイトル）.
     */
    private String mTitle;
    /**
     * 録画予約開始時間.
     */
    private long mStartTime;
    /**
     * 予約時間の長さ.
     */
    private long mDuration;
    /**
     * 定期予約指定値:0～10（イベントIDありの場合0固定）.
     */
    private int mLoopTypeNum = 0;
    /**
     * パレンタル設定値.
     */
    private String mRValue;

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

    public void setEventId(final String eventId) {
        mEventId = eventId;
    }

    public void setLoopTypeNum(final int loopTypeNum) {
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
