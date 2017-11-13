/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class VideoGenreListData implements Parcelable {
    private HashMap<String, String> mCountMap;
    private HashMap<String, String> mTitleMap;

    public VideoGenreListData(HashMap<String, String> count, HashMap<String, String> title) {
        this.mCountMap = count;
        this.mTitleMap = title;
    }

    public HashMap<String, String> getContentMap() {
        return mCountMap;
    }

    public HashMap<String, String> getTitleMap() {
        return mTitleMap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.mCountMap);
        dest.writeSerializable(this.mTitleMap);
    }

    protected VideoGenreListData(Parcel in) {
        this.mCountMap = (HashMap<String, String>) in.readSerializable();
        this.mTitleMap = (HashMap<String, String>) in.readSerializable();
    }

    public static final Parcelable.Creator<VideoGenreListData> CREATOR = new Parcelable.Creator<VideoGenreListData>() {
        @Override
        public VideoGenreListData createFromParcel(Parcel source) {
            return new VideoGenreListData(source);
        }

        @Override
        public VideoGenreListData[] newArray(int size) {
            return new VideoGenreListData[size];
        }
    };
}
