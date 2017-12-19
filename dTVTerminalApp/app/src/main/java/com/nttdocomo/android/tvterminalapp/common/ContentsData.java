/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;

public class ContentsData {

    //ランキング順位
    private String mRank = null;
    //サブタイトル
    private String mTime = null;
    //メインタイトル
    private String mTitle = null;
    //評価ポイント
    private String mRatStar = null;
    //サムネイルURL
    private String mThumURL = null;
    // 録画予約ステータス
    private int mRecordingReservationStatus = RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_ALREADY_REFLECT;
    // チャンネル名
    private String mChannelName = null;
    // 録画番組用 コピー残り回数
    private int mAllowedUse = 0;

    //デバイス名
    private String deviceName = null;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getRank() {
        return mRank;
    }

    public void setRank(String rank) {
        this.mRank = rank;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getRatStar() {
        return mRatStar;
    }

    public void setRatStar(String ratStar) {
        this.mRatStar = ratStar;
    }

    public String getThumURL() {
        return mThumURL;
    }

    public void setThumURL(String thumURL) {
        this.mThumURL = thumURL;
    }

    public void setRecordingReservationStatus(int status) {
        mRecordingReservationStatus = status;
    }

    public int getRecordingReservationStatus() {
        return mRecordingReservationStatus;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public void setChannelName(String channelName) {
        this.mChannelName = channelName;
    }
    public int getAllowedUse() {
        return mAllowedUse;
    }

    public void setAllowedUse(int allowedUse) {
        this.mAllowedUse = allowedUse;
    }
}
