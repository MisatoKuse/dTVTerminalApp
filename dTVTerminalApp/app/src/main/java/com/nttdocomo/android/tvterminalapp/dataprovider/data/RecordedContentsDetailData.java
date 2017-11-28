/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordedContentsDetailData implements Parcelable {

    private String mSize;
    private String mDuration;
    private String mResolution;
    private String mBitrate;
    private String mThumbnail;

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

    public void setThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public RecordedContentsDetailData() {
    }

    public RecordedContentsDetailData(Parcel in) {
        mSize = in.readString();
        mDuration = in.readString();
        mResolution = in.readString();
        mBitrate = in.readString();
        mThumbnail = in.readString();
    }

    public static final Creator<RecordedContentsDetailData> CREATOR = new Creator<RecordedContentsDetailData>() {
        @Override
        public RecordedContentsDetailData createFromParcel(Parcel in) {
            return new RecordedContentsDetailData(in);
        }

        @Override
        public RecordedContentsDetailData[] newArray(int size) {
            return new RecordedContentsDetailData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSize);
        dest.writeString(mDuration);
        dest.writeString(mResolution);
        dest.writeString(mBitrate);
        dest.writeString(mThumbnail);
    }
}