/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * チャンネルクラス.
 * 　　機能： チャンネルクラスである
 */
public class ChannelInfo implements Parcelable {

    /**
     * Scheduleの配列を保存.
     */
    private ArrayList<ScheduleInfo> mSchedules = null;
    /**
     * チャンネルのアダルトフラグ.
     */
    private String mAdult;
    /**
     * チャンネルのタイトル.
     */
    private String mTitle;
    /**
     * チャンネルのチャンネルID.
     */
    private int mChannelNo = -1;
    /**
     * チャンネルのサービスID.
     */
    private String mServiceId;
    /**
     * チャンネルの開始時間.
     */
    private String mStartDate;
    /**
     * チャンネルの開始時間.
     */
    private String mEndDate;
    /**
     * チャンネルのアイコン.
     */
    private String mThumbnail;
    /**
     * チャンネルのタイプ.
     */
    private String mChannelType;
    /**
     * パーチャスID.
     */
    private String mPurchaseId;
    /**
     * サブパーチャスID.
     */
    private String mSubPurchaseId;
    /**
     * チャンネルパックのパーチャスID.
     */
    private String mChannelPackPurchaseId;
    /**
     * チャンネルパックのサブパーチャスID.
     */
    private String mChannelPackSubPurchaseId;
    /**
     * チャンネルのサービス識別子.
     */
    private String mService;
    /**
     * Channelアダルトフラグを取得.
     * @return mAdult アダルトフラグ
     */
    public String getAdult() {
        return mAdult;
    }

    /**
     * Channelアダルトフラグを設定.
     * @param adult アダルトフラグ
     */
    public void setAdult(final String adult) {
        this.mAdult = adult;
    }
    /**
     * Channelタイトルを取得.
     * @return title タイトル
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Channelタイトルを設定.
     * @param title タイトル
     */
    public void setTitle(final String title) {
        this.mTitle = title;
    }

    /**
     * chNoを取得.
     * @return chNo
     */
    public int getChannelNo() {
        return mChannelNo;
    }

    /**
     * ｍChnを設定.
     * @param chNo チャンネルID
     */
    public void setChannelNo(final int chNo) {
        this.mChannelNo = chNo;
    }

    /**
     * serviceIdを取得.
     * @return serviceId
     */
    public String getServiceId() {
        return mServiceId;
    }

    /**
     * serviceIdを設定.
     * @param serviceId チャンネルID
     */
    public void setServiceId(final String serviceId) {
        this.mServiceId = serviceId;
    }

    /**
     * startDateを取得.
     * @return startDate
     */
    public String getStartDate() {
        return mStartDate;
    }
    /**
     * startDateを設定.
     * @param startDate チャンネルID
     */
    public void setStartDate(final String startDate) {
        this.mStartDate = startDate;
    }

    /**
     * endDateを取得.
     * @return endDate
     */
    public String getEndDate() {
        return mEndDate;
    }
    /**
     * endDateを設定.
     * @param endDate チャンネルID
     */
    public void setEndDate(final String endDate) {
        this.mEndDate = endDate;
    }
    /**
     * thumbnailを取得.
     * @return thumbnail
     */
    public String getThumbnail() {
        return mThumbnail;
    }
    /**
     * thumbnail.
     * @param thumbnail チャンネルID
     */
    public void setThumbnail(final String thumbnail) {
        this.mThumbnail = thumbnail;
    }
    /**
     * chTypeを取得.
     * @return chType
     */
    public String getChannelType() {
        return mChannelType;
    }
    /**
     * chTypeを設定.
     * @param chType チャンネルタイプ
     */
    public void setChannelType(final String chType) {
        this.mChannelType = chType;
    }
    /**
     * puidを取得.
     * @return パーチャスID
     */
    public String getPurchaseId() {
        return mPurchaseId;
    }
    /**
     * puidを設定.
     * @param puId パーチャスID
     */
    public void setPurchaseId(final String puId) {
        this.mPurchaseId = puId;
    }
    /**
     * sub_puidを取得.
     * @return サブパーチャスID
     */
    public String getSubPurchaseId() {
        return mSubPurchaseId;
    }
    /**
     * sub_puidを設定.
     * @param subPuId サブパーチャスID
     */
    public void setSubPurchaseId(final String subPuId) {
        this.mSubPurchaseId = subPuId;
    }
    /**
     * CHPACKのpuidを取得.
     * @return チャンネルパックのパーチャスID
     */
    public String getChannelPackPurchaseId() {
        return mChannelPackPurchaseId;
    }
    /**
     * CHPACKのpuidを設定.
     * @param chPackPuId チャンネルパックのパーチャスID
     */
    public void setChannelPackPurchaseId(final String chPackPuId) {
        this.mChannelPackPurchaseId = chPackPuId;
    }
    /**
     * CHPACKのsub_puidを取得.
     * @return チャンネルパックのサブパーチャスID
     */
    public String getChannelPackSubPurchaseId() {
        return mChannelPackSubPurchaseId;
    }
    /**
     * CHPACKのsub_puidを設定.
     * @param chPackSubPuId チャンネルパックのサブパーチャスID
     */
    public void setChannelPackSubPurchaseId(final String chPackSubPuId) {
        this.mChannelPackSubPurchaseId = chPackSubPuId;
    }

    /**
     * チャンネル番組を取得.
     * @return schedules　チャンネル番組
     */
    public ArrayList<ScheduleInfo> getSchedules() {
        return mSchedules;
    }
    /**
     * チャンネル番組を設定.
     * @param schedules チャンネル
     */
    public void setSchedules(final ArrayList<ScheduleInfo> schedules) {
        this.mSchedules = schedules;
    }

    /**
     * サービス識別子を取得.
     * @return service　ひかり"1" or dCH"2"
     */
    public String getService() {
        return mService;
    }
    /**
     * サービス識別子を設定.
     * @param service ひかり"1" or dCH"2"
     */
    public void setService(final String service) {
        this.mService = service;
    }
    /**
     * チャンネルを比較.
     * @param ch2 比較対象チャンネル
     * @return true:同じ false:違う
     */
    public boolean equalTo(final ChannelInfo ch2) {
        return !(null == ch2 || null == this.mTitle || null == ch2.mTitle)
                && this.mChannelNo == ch2.mChannelNo && this.mTitle.equals(ch2.mTitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeList(this.mSchedules);
        dest.writeString(this.mTitle);
        dest.writeInt(this.mChannelNo);
        dest.writeString(this.mServiceId);
        dest.writeString(this.mStartDate);
        dest.writeString(this.mEndDate);
        dest.writeString(this.mThumbnail);
        dest.writeString(this.mChannelType);
        dest.writeString(this.mPurchaseId);
        dest.writeString(this.mSubPurchaseId);
        dest.writeString(this.mChannelPackPurchaseId);
        dest.writeString(this.mChannelPackSubPurchaseId);
        dest.writeString(this.mService);
    }

    public ChannelInfo() {
    }

    private ChannelInfo(final Parcel in) {
        this.mSchedules = new ArrayList<ScheduleInfo>();
        in.readList(this.mSchedules, ScheduleInfo.class.getClassLoader());
        this.mTitle = in.readString();
        this.mChannelNo = in.readInt();
        this.mServiceId = in.readString();
        this.mStartDate = in.readString();
        this.mEndDate = in.readString();
        this.mThumbnail = in.readString();
        this.mChannelType = in.readString();
        this.mPurchaseId = in.readString();
        this.mSubPurchaseId = in.readString();
        this.mChannelPackPurchaseId = in.readString();
        this.mChannelPackSubPurchaseId = in.readString();
        this.mService = in.readString();
    }

    public static final Parcelable.Creator<ChannelInfo> CREATOR = new Parcelable.Creator<ChannelInfo>() {
        @Override
        public ChannelInfo createFromParcel(final Parcel source) {
            return new ChannelInfo(source);
        }

        @Override
        public ChannelInfo[] newArray(final int size) {
            return new ChannelInfo[size];
        }
    };
}
