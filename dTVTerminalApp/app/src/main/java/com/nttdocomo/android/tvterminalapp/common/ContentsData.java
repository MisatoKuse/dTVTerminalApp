/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;

public class ContentsData {

    //ランキング順位
    private String rank = null;
    //サブタイトル
    private String time = null;
    //メインタイトル
    private String title = null;
    //評価ポイント
    private String ratStar = null;
    //サムネイルURL
    private String thumURL = null;
    // 録画予約ステータス
    private int recordingReservationStatus = RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_ALREADY_REFLECT;
    // チャンネル名
    private String channelName = null;
    // 録画番組用 チャンネル名
    private String recordedChannelName = null;
    // 録画番組用 コピー残り回数
    private int allowedUse = 0;
    // クリップ状態
    private String mSearchOk = "";

    //デバイス名
    private String deviceName = null;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRatStar() {
        return ratStar;
    }

    public void setRatStar(String ratStar) {
        this.ratStar = ratStar;
    }

    public String getThumURL() {
        return thumURL;
    }

    public void setThumURL(String thumURL) {
        this.thumURL = thumURL;
    }

    public void setRecordingReservationStatus(int status) {
        recordingReservationStatus = status;
    }

    public int getRecordingReservationStatus() {
        return recordingReservationStatus;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getRecordedChannelName() {
        return recordedChannelName;
    }

    public void setRecordedChannelName(String channelName) {
        this.recordedChannelName = channelName;
    }
    public int getAllowedUse() {
        return allowedUse;
    }

    public void setAllowedUse(int allowedUse) {
        this.allowedUse = allowedUse;
    }

    public String getSearchOk() {
        return mSearchOk;
    }

    public void setSearchOk(String mSearchOk) {
        this.mSearchOk = mSearchOk;
    }
}
