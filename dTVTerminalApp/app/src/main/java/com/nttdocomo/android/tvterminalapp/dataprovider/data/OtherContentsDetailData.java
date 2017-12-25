/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OtherContentsDetailData extends RecordedContentsDetailData implements Parcelable {

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
    private String reserved4;
    private List<String> staffList;

    // クリップ情報取得用
    private VodMetaFullData mVodMetaFullData = null;

    //コンテンツIDを追加
    private String mContentId;

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
        return roleList;
    }

    public void setRoleList(String[] roleList) {
        this.roleList = roleList;
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

        //contentIdを追加
        dest.writeString(this.mContentId);
        dest.writeInt(this.mAge);
        dest.writeStringArray(this.roleList);
        dest.writeString(this.availStartDate);
        dest.writeString(this.availEndDate);
        dest.writeString(this.displayType);
        dest.writeString(this.categoryId);
        dest.writeString(this.reserved4);
        dest.writeStringList(this.staffList);
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

        //contentIdを追加
        this.mContentId = in.readString();
        this.mAge = in.readInt();
        this.roleList = in.createStringArray();
        this.availStartDate = in.readString();
        this.availEndDate = in.readString();
        this.displayType = in.readString();
        this.categoryId = in.readString();
        this.reserved4=in.readString();
        this.staffList = in.createStringArrayList();
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
