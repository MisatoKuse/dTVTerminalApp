/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ビデオ一覧用データクラス
 */
public class VideoGenreListData implements Parcelable {

    private HashMap<String, String> mTitleMap;
    private ArrayList<GenreListMetaData.SubContent> mSubGenre;

    public VideoGenreListData(HashMap<String, String> title) {
        this.mTitleMap = title;
    }

    public ArrayList<GenreListMetaData.SubContent> getSubGenre() {
        return mSubGenre;
    }

    public void setSubGenre(ArrayList<GenreListMetaData.SubContent> subGenre) {
        this.mSubGenre = subGenre;
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
        dest.writeSerializable(this.mTitleMap);
        dest.writeList(this.mSubGenre);
    }

    protected VideoGenreListData(Parcel in) {
        this.mTitleMap = (HashMap<String, String>) in.readSerializable();
        this.mSubGenre = new ArrayList<GenreListMetaData.SubContent>();
        in.readList(this.mSubGenre, GenreListMetaData.SubContent.class.getClassLoader());
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
