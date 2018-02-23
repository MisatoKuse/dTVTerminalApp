/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Parcel;

import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;

import java.util.List;

public class OtherContentsDetailData extends RecordedContentsDetailData {

    public static final int DTV_CONTENTS_SERVICE_ID = 15;
    public static final int D_ANIMATION_CONTENTS_SERVICE_ID = 17;
    public static final int DTV_CHANNEL_CONTENTS_SERVICE_ID = 43;
    public static final int DTV_HIKARI_CONTENTS_SERVICE_ID = 44;

    public static final String HIKARI_CONTENTS_CATEGORY_ID_DTB = "01";
    public static final String HIKARI_CONTENTS_CATEGORY_ID_BS = "02";
    public static final String HIKARI_CONTENTS_CATEGORY_ID_IPTV = "03";

    private String mThumb;
    private String mChannelDate;
    private String mChannelName;
    private int mServiceId;
    private String mDetail;
    private String mComment;
    private String mHighlight;
    private String mContentsType;
    private int mAge;
    private String[] roleList;
    private String availStartDate;
    private String availEndDate;
    private String displayType;
    private String categoryId;
    private String reserved1;
    private String reserved2;
    private String reserved4;
    private String mobileViewingFlg;
    private List<String> staffList;

    // クリップ情報取得用
    private VodMetaFullData mVodMetaFullData = null;

    //コンテンツIDを追加
    private String mContentId;

    // チャンネルID
    private String mChannelId = "";
    // おすすめ順
    private String mRecommendOrder = "";
    // 画面ID
    private String mPageId = "";
    // ユーザグループID
    private String mGroupId = "";
    // レコメンド手法ID
    private String mRecommendMethodId = "";
    // レコメンドフラグ
    private String mRecommendFlg = "";
    // 表示タイプ
    private String mDispType = "";
    // クリップ情報
    private String mSearchOk = "";
    // dTVフラグ
    private String mDtv = "";
    // dTVタイプ
    private String mDtvType = "";
    // クリップ可否
    private boolean mClipExec = false;
    // クリップ未/済
    private boolean mClipStatus = false;
    // レーティング値
    private double mRating = 0;
    // シーズン名
    private String mEpititle = "";
    // Tvサービス
    private String mTvService = "";

    public String getChannelDate() {
        return mChannelDate;
    }

    public void setChannelDate(String mChannelDate) {
        this.mChannelDate = mChannelDate;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public void setChannelName(String mChannelName) {
        this.mChannelName = mChannelName;
    }

    public String getThumb() {
        return mThumb;
    }

    public void setThumb(String mThumb) {
        this.mThumb = mThumb;
    }

    public int getServiceId() {
        return mServiceId;
    }

    public void setServiceId(int serviceId) {
        this.mServiceId = serviceId;
    }

    public String getMobileViewingFlg() {
        return mobileViewingFlg;
    }

    public void setMobileViewingFlg(String mobileViewingFlg) {
        this.mobileViewingFlg = mobileViewingFlg;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        this.mDetail = detail;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    public String getHighlight() {
        return mHighlight;
    }

    public void setHighlight(String mHighlight) {
        this.mHighlight = mHighlight;
    }

    public String getContentsType() {
        return mContentsType;
    }

    public void setContentsType(String mContentsType) {
        this.mContentsType = mContentsType;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int Age) {
        this.mAge = Age;
    }

    public List<String> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<String> staffList) {
        this.staffList = staffList;
    }

    public String[] getRoleList() {
        return roleList.clone();
    }

    public void setRoleList(String[] roleList) {
        this.roleList = roleList.clone();
    }

    public String getAvailStartDate() {
        return availStartDate;
    }

    public void setAvailStartDate(String availStartDate) {
        this.availStartDate = availStartDate;
    }

    public String getAvailEndDate() {
        return availEndDate;
    }

    public void setAvailEndDate(String availEndDate) {
        this.availEndDate = availEndDate;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getContentId() {
        return mContentId;
    }

    public void setContentId(String mContentId) {
        this.mContentId = mContentId;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    public VodMetaFullData getVodMetaFullData() {
        return mVodMetaFullData;
    }

    public void setVodMetaFullData(VodMetaFullData mVodMetaFullData) {
        this.mVodMetaFullData = mVodMetaFullData;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(String mChannelId) {
        this.mChannelId = mChannelId;
    }

    public String getRecommendOrder() {
        return mRecommendOrder;
    }

    public void setRecommendOrder(String mRecommendOrder) {
        this.mRecommendOrder = mRecommendOrder;
    }

    public String getPageId() {
        return mPageId;
    }

    public void setPageId(String mPageId) {
        this.mPageId = mPageId;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public void setGroupId(String mGroupId) {
        this.mGroupId = mGroupId;
    }

    public String getRecommendMethodId() {
        return mRecommendMethodId;
    }

    public void setRecommendMethodId(String mRecommendMethodId) {
        this.mRecommendMethodId = mRecommendMethodId;
    }

    public String getRecommendFlg() {
        return mRecommendFlg;
    }

    public void setRecommendFlg(String mRecommendFlg) {
        this.mRecommendFlg = mRecommendFlg;
    }

    public String getDispType() {
        return mDispType;
    }

    public void setDispType(String mDispType) {
        this.mDispType = mDispType;
    }

    public String getSearchOk() {
        return mSearchOk;
    }

    public void setSearchOk(String mSearchOk) {
        this.mSearchOk = mSearchOk;
    }

    public String getDtv() {
        return mDtv;
    }

    public void setDtv(String mDtv) {
        this.mDtv = mDtv;
    }

    public String getDtvType() {
        return mDtvType;
    }

    public void setDtvType(String mDtvType) {
        this.mDtvType = mDtvType;
    }

    public boolean isClipExec() {
        return ClipUtils.isCanClip(mDispType, mSearchOk, mDtv, mDtvType);
    }

    public void setClipExec(boolean mClipExec) {
        this.mClipExec = mClipExec;
    }

    public boolean isClipStatus() {
        return mClipStatus;
    }

    public void setClipStatus(boolean mClipStatus) {
        this.mClipStatus = mClipStatus;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double mRating) {
        this.mRating = mRating;
    }

    public String getEpititle() {
        return mEpititle;
    }

    public void setEpititle(String mEpititle) {
        this.mEpititle = mEpititle;
    }

    public String getTvService() {
        return mTvService;
    }

    public void setTvService(String mTvService) {
        this.mTvService = mTvService;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mThumb);
        dest.writeString(this.mChannelDate);
        dest.writeString(this.mChannelName);
        dest.writeInt(this.mServiceId);
        dest.writeString(this.mDetail);
        dest.writeString(this.mComment);
        dest.writeString(this.mHighlight);
        dest.writeString(this.mContentsType);
        dest.writeInt(this.mAge);
        dest.writeStringArray(this.roleList);
        dest.writeString(this.availStartDate);
        dest.writeString(this.availEndDate);
        dest.writeString(this.displayType);
        dest.writeString(this.categoryId);
        dest.writeString(this.reserved1);
        dest.writeString(this.reserved2);
        dest.writeString(this.reserved4);
        dest.writeString(this.mobileViewingFlg);
        dest.writeStringList(this.staffList);
        dest.writeSerializable(this.mVodMetaFullData);
        dest.writeString(this.mContentId);
        dest.writeString(this.mChannelId);
        dest.writeString(this.mRecommendOrder);
        dest.writeString(this.mPageId);
        dest.writeString(this.mGroupId);
        dest.writeString(this.mRecommendMethodId);
        dest.writeString(this.mRecommendFlg);
        dest.writeString(this.mDispType);
        dest.writeString(this.mSearchOk);
        dest.writeString(this.mDtv);
        dest.writeString(this.mDtvType);
        dest.writeDouble(this.mRating);
        dest.writeString(this.mEpititle);
        dest.writeString(this.mTvService);
        dest.writeByte(this.mClipExec ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mClipStatus ? (byte) 1 : (byte) 0);
    }

    public OtherContentsDetailData() {
    }

    private OtherContentsDetailData(Parcel in) {
        super(in);
        this.mThumb = in.readString();
        this.mChannelDate = in.readString();
        this.mChannelName = in.readString();
        this.mServiceId = in.readInt();
        this.mDetail = in.readString();
        this.mComment = in.readString();
        this.mHighlight = in.readString();
        this.mContentsType = in.readString();
        this.mAge = in.readInt();
        this.roleList = in.createStringArray();
        this.availStartDate = in.readString();
        this.availEndDate = in.readString();
        this.displayType = in.readString();
        this.categoryId = in.readString();
        this.reserved1 = in.readString();
        this.reserved2 = in.readString();
        this.reserved4 = in.readString();
        this.mobileViewingFlg = in.readString();
        this.staffList = in.createStringArrayList();
        this.mVodMetaFullData = (VodMetaFullData) in.readSerializable();
        this.mContentId = in.readString();
        this.mChannelId = in.readString();
        this.mRecommendOrder = in.readString();
        this.mPageId = in.readString();
        this.mGroupId = in.readString();
        this.mRecommendMethodId = in.readString();
        this.mRecommendFlg = in.readString();
        this.mDispType = in.readString();
        this.mSearchOk = in.readString();
        this.mDtv = in.readString();
        this.mDtvType = in.readString();
        this.mRating = in.readDouble();
        this.mEpititle = in.readString();
        this.mTvService = in.readString();
        this.mClipExec = in.readByte() != 0;
        this.mClipStatus = in.readByte() != 0;
    }

    public static final Creator<OtherContentsDetailData> CREATOR = new Creator<OtherContentsDetailData>() {
        @Override
        public OtherContentsDetailData createFromParcel(Parcel source) {
            return new OtherContentsDetailData(source);
        }

        @Override
        public OtherContentsDetailData[] newArray(int size) {
            return new OtherContentsDetailData[size];
        }
    };

}
