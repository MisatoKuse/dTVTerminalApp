/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

import android.os.Parcel;
import android.os.Parcelable;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;

import java.util.HashMap;
import java.util.Map;


public class VideoGenreListDataInfo implements Parcelable {
    private String mGenreId;
    private Map<String, VideoGenreList> mContentsTree;

    public VideoGenreListDataInfo() {
    }

    public Map<String, VideoGenreList> getSubGenre() {
        return mContentsTree;
    }

    public void setSubGenre(final Map<String, VideoGenreList> subGenre) {
        this.mContentsTree = subGenre;
    }

    public String getGenreId() {
        return mGenreId;
    }

    public void setGenreId(final String genreId) {
        this.mGenreId = genreId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeSerializable(this.mGenreId);
        dest.writeMap(this.mContentsTree);
    }

    protected VideoGenreListDataInfo(final Parcel in) {
        this.mGenreId = (String) in.readSerializable();
        this.mContentsTree = new HashMap<String, VideoGenreList>();
        in.readMap(this.mContentsTree, getClass().getClassLoader());
    }

    public static final Parcelable.Creator<VideoGenreListDataInfo> CREATOR = new Parcelable.Creator<VideoGenreListDataInfo>() {
        @Override
        public VideoGenreListDataInfo createFromParcel(final Parcel source) {
            return new VideoGenreListDataInfo(source);
        }

        @Override
        public VideoGenreListDataInfo[] newArray(final int size) {
            return new VideoGenreListDataInfo[size];
        }
    };

    /**
     * このクラスのジャンルデータを取得.
     * @return
     */
    public VideoGenreList getVideoGenreListShowData() {
        VideoGenreList videoGenreList = null;
        if (mContentsTree != null) {
            videoGenreList = mContentsTree.get(mGenreId);
        }
        return videoGenreList;
    }

    /**
     * 引数で指定されたジャンルIDのデータを取得.
     * @param genreId
     * @return
     */
    public VideoGenreList getVideoGenreListData(final String genreId) {
        VideoGenreList videoGenreList = null;
        if (mContentsTree != null) {
            videoGenreList = mContentsTree.get(genreId);
        }
        return videoGenreList;
    }
}