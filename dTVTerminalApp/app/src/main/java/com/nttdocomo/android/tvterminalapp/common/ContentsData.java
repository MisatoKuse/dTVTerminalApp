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
    private String mSearchOk = null;
    // タイプ
    private String mType = null;
    // コンテンツ識別子
    private String mCrid = null;
    // サービスID
    private String mServiceId = null;
    // イベントID
    private String mEventId = null;
    // タイトルID
    private String mTitleId = null;
    // dtvフラグ
    private String mDtv = null;
    // 番組のパレンタル設定値
    private String mRValue = null;
    // 放送開始日時
    private String mLinearStartDate = null;
    // 放送終了日時
    private String mLinearEndDate = null;
    // 視聴通知判定
    private String mIsNotify = null;

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

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getCrid() {
        return mCrid;
    }

    public void setCrid(String mCrid) {
        this.mCrid = mCrid;
    }

    public String getServiceId() {
        return mServiceId;
    }

    public void setServiceId(String mServiceId) {
        this.mServiceId = mServiceId;
    }

    public String getEventId() {
        return mEventId;
    }

    public void setEventId(String mEventId) {
        this.mEventId = mEventId;
    }

    public String getDtv() {
        return mDtv;
    }

    public void setDtv(String mDtv) {
        this.mDtv = mDtv;
    }

    public String getTitleId() {
        return mTitleId;
    }

    public void setTitleId(String mTitleId) {
        this.mTitleId = mTitleId;
    }

    public String getRValue() {
        return mRValue;
    }

    public void setRValue(String mRValue) {
        this.mRValue = mRValue;
    }

    public String getLinearStartDate() {
        return mLinearStartDate;
    }

    public void setLinearStartDate(String mLinearStartDate) {
        this.mLinearStartDate = mLinearStartDate;
    }

    public String getLinearEndDate() {
        return mLinearEndDate;
    }

    public void setLinearEndDate(String mLinearEndDate) {
        this.mLinearEndDate = mLinearEndDate;
    }

    public String isIsNotify() {
        return mIsNotify;
    }

    public void setIsNotify(String mIsNotify) {
        this.mIsNotify = mIsNotify;
    }
}
