/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoGenreList implements Serializable {

    private static final long serialVersionUID = -9201423763597476237L;
    private String mGenreId; // ジャンルID
    private String mTitle; // タイトル
    private String mRValue; // パレンタル設定値
    private String mContentCount; // コンテンツ数
    private List<String> mSubGenreIdList = null; // 本データを親とする子データのサブジャンルIDリスト


    public String getGenreId() {
        return mGenreId;
    }

    public void setGenreId(String genreId) {
        this.mGenreId = genreId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getContentCount() {
        return mContentCount;
    }

    public void setContentCount(String contentCount) {
        this.mContentCount = contentCount;
    }

    public String getRValue() {
        return mRValue;
    }

    public void setRValue(String rValue) {
        this.mRValue = rValue;
    }

    public void addSubGenreList(String subGenreId) {
        if (mSubGenreIdList == null) {
            mSubGenreIdList = new ArrayList<String>();
        }
        mSubGenreIdList.add(subGenreId);
    }

    public List<String> getSubGenre() {
        return mSubGenreIdList;
    }
}
