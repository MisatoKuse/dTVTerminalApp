/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Parcel;

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
    private String[] mRoleList;
    private String mStartDate;
    private String mEndDate;
    private String mDisplayType;
    private String mCategoryId;
    private String mDescription1;
    private String mDescription2;
    private String mDescription3;
    private String mReserved1;
    private String mReserved2;
    private String mReserved3;
    private String mReserved4;
    private String mReserved5;
    private String mobileViewingFlg;
    private List<String> staffList;

    // クリップ情報取得用
    private VodMetaFullData mVodMetaFullData = null;

    //コンテンツIDを追加
    private String mContentsId;

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

    /**
     * crId.
     */
    private String mCrId = null;
    /**
     * イベントID.
     */
    private String mEventId = null;
    /**
     * タイトルID.
     */
    private String mTitleId = null;

    public String getChannelDate() {
        return mChannelDate;
    }

    public void setChannelDate(final String mChannelDate) {
        this.mChannelDate = mChannelDate;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public void setChannelName(final String mChannelName) {
        this.mChannelName = mChannelName;
    }

    public String getThumb() {
        return mThumb;
    }

    public void setThumb(final String mThumb) {
        this.mThumb = mThumb;
    }

    public int getServiceId() {
        return mServiceId;
    }

    public void setServiceId(final int serviceId) {
        this.mServiceId = serviceId;
    }

    public String getMobileViewingFlg() {
        return mobileViewingFlg;
    }

    public void setMobileViewingFlg(final String mobileViewingFlg) {
        this.mobileViewingFlg = mobileViewingFlg;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(final String detail) {
        this.mDetail = detail;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(final String mComment) {
        this.mComment = mComment;
    }

    public String getHighlight() {
        return mHighlight;
    }

    public void setHighlight(final String mHighlight) {
        this.mHighlight = mHighlight;
    }

    public String getContentsType() {
        return mContentsType;
    }

    public void setContentsType(final String mContentsType) {
        this.mContentsType = mContentsType;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(final int Age) {
        this.mAge = Age;
    }

    public List<String> getStaffList() {
        return staffList;
    }

    public void setStaffList(final List<String> staffList) {
        this.staffList = staffList;
    }

    public String[] getmRoleList() {
        return mRoleList.clone();
    }

    public void setmRoleList(final String[] mRoleList) {
        this.mRoleList = mRoleList.clone();
    }

    public String getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(final String mStartDate) {
        this.mStartDate = mStartDate;
    }

    public String getmEndDate() {
        return mEndDate;
    }

    public void setmEndDate(final String mEndDate) {
        this.mEndDate = mEndDate;
    }

    public String getmDisplayType() {
        return mDisplayType;
    }

    public void setmDisplayType(final String mDisplayType) {
        this.mDisplayType = mDisplayType;
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(final String categoryId) {
        this.mCategoryId = categoryId;
    }

    public String getContentsId() {
        return mContentsId;
    }

    public void setContentsId(final String mContentsId) {
        this.mContentsId = mContentsId;
    }

    public String getReserved1() {
        return mReserved1;
    }

    public void setReserved1(final String mReserved1) {
        this.mReserved1 = mReserved1;
    }

    public String getReserved2() {
        return mReserved2;
    }

    public void setReserved2(final String mReserved2) {
        this.mReserved2 = mReserved2;
    }

    public String getReserved4() {
        return mReserved4;
    }

    public void setReserved4(final String mReserved4) {
        this.mReserved4 = mReserved4;
    }

    public String getDescription1() {
        return mDescription1;
    }

    public void setDescription1(String mDescription1) {
        this.mDescription1 = mDescription1;
    }

    public String getDescription2() {
        return mDescription2;
    }

    public void setDescription2(String mDescription2) {
        this.mDescription2 = mDescription2;
    }

    public String getDescription3() {
        return mDescription3;
    }

    public void setDescription3(String mDescription3) {
        this.mDescription3 = mDescription3;
    }

    public String getReserved3() {
        return mReserved3;
    }

    public void setReserved3(String mReserved3) {
        this.mReserved3 = mReserved3;
    }

    public String getReserved5() {
        return mReserved5;
    }

    public void setReserved5(String mReserved5) {
        this.mReserved5 = mReserved5;
    }

    public VodMetaFullData getVodMetaFullData() {
        return mVodMetaFullData;
    }

    public void setVodMetaFullData(final VodMetaFullData mVodMetaFullData) {
        this.mVodMetaFullData = mVodMetaFullData;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(final String mChannelId) {
        this.mChannelId = mChannelId;
    }

    public String getRecommendOrder() {
        return mRecommendOrder;
    }

    public void setRecommendOrder(final String mRecommendOrder) {
        this.mRecommendOrder = mRecommendOrder;
    }

    public String getPageId() {
        return mPageId;
    }

    public void setPageId(final String mPageId) {
        this.mPageId = mPageId;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public void setGroupId(final String mGroupId) {
        this.mGroupId = mGroupId;
    }

    public String getRecommendMethodId() {
        return mRecommendMethodId;
    }

    public void setRecommendMethodId(final String mRecommendMethodId) {
        this.mRecommendMethodId = mRecommendMethodId;
    }

    public String getRecommendFlg() {
        return mRecommendFlg;
    }

    public void setRecommendFlg(final String mRecommendFlg) {
        this.mRecommendFlg = mRecommendFlg;
    }

    public String getDispType() {
        return mDispType;
    }

    public void setDispType(final String mDispType) {
        this.mDispType = mDispType;
    }

    public String getSearchOk() {
        return mSearchOk;
    }

    public void setSearchOk(final String mSearchOk) {
        this.mSearchOk = mSearchOk;
    }

    public String getDtv() {
        return mDtv;
    }

    public void setDtv(final String mDtv) {
        this.mDtv = mDtv;
    }

    public String getDtvType() {
        return mDtvType;
    }

    public void setDtvType(final String mDtvType) {
        this.mDtvType = mDtvType;
    }

    public boolean isClipExec() {
        return mClipExec;
    }

    public void setClipExec(final boolean mClipExec) {
        this.mClipExec = mClipExec;
    }

    public boolean isClipStatus() {
        return mClipStatus;
    }

    public void setClipStatus(final boolean mClipStatus) {
        this.mClipStatus = mClipStatus;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(final double mRating) {
        this.mRating = mRating;
    }

    public String getEpititle() {
        return mEpititle;
    }

    public void setEpititle(final String mEpititle) {
        this.mEpititle = mEpititle;
    }

    public String getTvService() {
        return mTvService;
    }

    public void setTvService(final String mTvService) {
        this.mTvService = mTvService;
    }

    public String getCrId() {
        return mCrId;
    }

    public void setCrId(String mCrId) {
        this.mCrId = mCrId;
    }

    public String getEventId() {
        return mEventId;
    }

    public void setEventId(String mEventId) {
        this.mEventId = mEventId;
    }

    public String getTitleId() {
        return mTitleId;
    }

    public void setTitleId(String mTitleId) {
        this.mTitleId = mTitleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
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
        dest.writeStringArray(this.mRoleList);
        dest.writeString(this.mStartDate);
        dest.writeString(this.mEndDate);
        dest.writeString(this.mDisplayType);
        dest.writeString(this.mCategoryId);
        dest.writeString(this.mDescription1);
        dest.writeString(this.mDescription2);
        dest.writeString(this.mDescription3);
        dest.writeString(this.mReserved1);
        dest.writeString(this.mReserved2);
        dest.writeString(this.mReserved3);
        dest.writeString(this.mReserved4);
        dest.writeString(this.mReserved5);
        dest.writeString(this.mobileViewingFlg);
        dest.writeStringList(this.staffList);
        dest.writeSerializable(this.mVodMetaFullData);
        dest.writeString(this.mContentsId);
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
        dest.writeByte(this.mClipExec ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mClipStatus ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.mRating);
        dest.writeString(this.mEpititle);
        dest.writeString(this.mTvService);
        dest.writeString(this.mCrId);
        dest.writeString(this.mEventId);
        dest.writeString(this.mTitleId);
    }

    public OtherContentsDetailData() {
    }

    protected OtherContentsDetailData(Parcel in) {
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
        this.mRoleList = in.createStringArray();
        this.mStartDate = in.readString();
        this.mEndDate = in.readString();
        this.mDisplayType = in.readString();
        this.mCategoryId = in.readString();
        this.mDescription1 = in.readString();
        this.mDescription2 = in.readString();
        this.mDescription3 = in.readString();
        this.mReserved1 = in.readString();
        this.mReserved2 = in.readString();
        this.mReserved3 = in.readString();
        this.mReserved4 = in.readString();
        this.mReserved5 = in.readString();
        this.mobileViewingFlg = in.readString();
        this.staffList = in.createStringArrayList();
        this.mVodMetaFullData = (VodMetaFullData) in.readSerializable();
        this.mContentsId = in.readString();
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
        this.mClipExec = in.readByte() != 0;
        this.mClipStatus = in.readByte() != 0;
        this.mRating = in.readDouble();
        this.mEpititle = in.readString();
        this.mTvService = in.readString();
        this.mCrId = in.readString();
        this.mEventId = in.readString();
        this.mTitleId = in.readString();
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
