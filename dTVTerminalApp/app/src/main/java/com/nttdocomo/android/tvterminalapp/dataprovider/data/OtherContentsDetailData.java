/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Parcel;
import android.os.Parcelable;

public class OtherContentsDetailData implements Parcelable {

    //変数名はぷららサーバインタフェース仕様書からの引用
    private String mTitle;
    private String mThumb;
    private int mServiceId;
    private String mDetail;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mThumb);
        dest.writeInt(this.mServiceId);
        dest.writeString(this.mDetail);
    }

    public OtherContentsDetailData() {
    }

    protected OtherContentsDetailData(Parcel in) {
        this.mTitle = in.readString();
        this.mThumb = in.readString();
        this.mServiceId = in.readInt();
        this.mDetail = in.readString();
    }

    public static final Creator<OtherContentsDetailData> CREATOR =
            new Creator<OtherContentsDetailData>() {
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
