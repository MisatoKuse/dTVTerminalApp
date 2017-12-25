/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;

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
    // 録画番組用 チャンネル名
    private String recordedChannelName = null;
    // 録画番組用 コピー残り回数
    private int mAllowedUse = 0;
    // クリップ状態
    private String mSearchOk = null;
    // コンテンツ識別子
    private String mCrid = null;
    // サービスID
    private String mServiceId = null;
    // イベントID
    private String mEventId = null;
    // タイトルID
    private String mTitleId = null;
    // 番組種別
    private String mDispType = null;
    // 番組のパレンタル設定値
    private String mRValue = null;
    // 放送開始日時
    private String mLinearStartDate = null;
    // 放送終了日時
    private String mLinearEndDate = null;
    // 視聴通知判定
    private String mIsNotify = null;
    // コンテンツタイプ
    private String mContentsType = null;
    // サービス種別
    private String mTvService = null;
    // dTVフラグ
    private String mDtv = null;
    // クリップボタン
    private TextView mClipButton = null;
    // クリップリクエストデータ
    private ClipRequestData mRequestData = new ClipRequestData();

    //デバイス名
    private String mDeviceName = null;

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        this.mDeviceName = deviceName;
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

    public String getRecordedChannelName() {
        return recordedChannelName;
    }

    public void setRecordedChannelName(String channelName) {
        this.recordedChannelName = channelName;
    }
    public int getAllowedUse() {
        return mAllowedUse;
    }

    public void setAllowedUse(int allowedUse) {
        this.mAllowedUse = allowedUse;
    }

    public String getSearchOk() {
        return mSearchOk;
    }

    public void setSearchOk(String mSearchOk) {
        this.mSearchOk = mSearchOk;
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

    public String getDispType() {
        return mDispType;
    }

    public void setDispType(String mDispType) {
        this.mDispType = mDispType;
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

    public String getContentsType() {
        return mContentsType;
    }

    public void setContentsType(String mContentsType) {
        this.mContentsType = mContentsType;
    }

    public String isIsNotify() {
        return mIsNotify;
    }

    public void setIsNotify(String mIsNotify) {
        this.mIsNotify = mIsNotify;
    }

    public TextView getClipButton() {
        return mClipButton;
    }

    public void setClipButton(TextView mClipButton) {
        this.mClipButton = mClipButton;
    }

    public String getTvService() {
        return mTvService;
    }

    public void setTvService(String mTvService) {
        this.mTvService = mTvService;
    }

    public String getDtv() {
        return mDtv;
    }

    public void setDtv(String mDtv) {
        this.mDtv = mDtv;
    }

    public ClipRequestData getRequestData() {
        return mRequestData;
    }

    public void setRequestData(ClipRequestData ｍRequestData) {
        this.mRequestData = ｍRequestData;
    }}
