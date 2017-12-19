/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordedContentsDetailData implements Parcelable {

    public enum DetailParamFromWhere{
        DetailParamFromWhere_ChList_TabTer,
        DetailParamFromWhere_ChList_TabBs,
        DetailParamFromWhere_Other,
    }
    private String mSize;
    private String mDuration;
    private String mResolution;
    private String mBitrate;
    private String mResUrl;
    private String mUpnpIcon;
    private String mTitle;
    private DetailParamFromWhere mDetailParamFromWhere=DetailParamFromWhere.DetailParamFromWhere_Other;
    private String mVideoType;

    public void setDetailParamFromWhere(DetailParamFromWhere from){
        mDetailParamFromWhere=from;
    }

    public DetailParamFromWhere getDetailParamFromWhere(){
        return mDetailParamFromWhere;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setSize(String mSize) {
        this.mSize = mSize;
    }

    public String getSize() {
        return mSize;
    }

    public void setDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setResolution(String mResolution) {
        this.mResolution = mResolution;
    }

    public String getResolution() {
        return mResolution;
    }

    public void setBitrate(String mBitrate) {
        this.mBitrate = mBitrate;
    }

    public String getBitrate() {
        return mBitrate;
    }

    public String getResUrl() {
        return mResUrl;
    }

    public void setResUrl(String mResUrl) {
        this.mResUrl = mResUrl;
    }

    public String getUpnpIcon() {
        return mUpnpIcon;
    }

    public void setUpnpIcon(String mUpnpIcon) {
        this.mUpnpIcon = mUpnpIcon;
    }

    public String getVideoType() {
        return mVideoType;
    }

    public void setVideoType(String type) {
        this.mVideoType = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSize);
        dest.writeString(this.mDuration);
        dest.writeString(this.mResolution);
        dest.writeString(this.mBitrate);
        dest.writeString(this.mResUrl);
        dest.writeString(this.mUpnpIcon);
        dest.writeString(this.mTitle);
        dest.writeInt(this.mDetailParamFromWhere.ordinal());
        dest.writeString(this.mVideoType);
    }

    public RecordedContentsDetailData() {
    }

    protected RecordedContentsDetailData(Parcel in) {
        this.mSize = in.readString();
        this.mDuration = in.readString();
        this.mResolution = in.readString();
        this.mBitrate = in.readString();
        this.mResUrl = in.readString();
        this.mUpnpIcon = in.readString();
        this.mTitle = in.readString();
        this.mDetailParamFromWhere = DetailParamFromWhere.values()[in.readInt()];
        this.mVideoType = in.readString();
    }

    public static final Parcelable.Creator<RecordedContentsDetailData> CREATOR = new Parcelable.Creator<RecordedContentsDetailData>() {
        @Override
        public RecordedContentsDetailData createFromParcel(Parcel source) {
            return new RecordedContentsDetailData(source);
        }

        @Override
        public RecordedContentsDetailData[] newArray(int size) {
            return new RecordedContentsDetailData[size];
        }
    };
}