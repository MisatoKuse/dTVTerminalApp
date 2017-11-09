/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.Map;

public class VideoGenreList {

    private String mGenreId;
    private String mTitle;
    private String mContentCount;
    private int mAllContentCount;
    private Map<String, String> mMapCountList;

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

    public int getAllContentCount() {
        return mAllContentCount;
    }

    public void setAllContentCount(int allContentCount) {
        this.mAllContentCount = allContentCount;
    }

    public Map<String, String> getMapCountList() {
        return mMapCountList;
    }

    public void setMapCountList(Map<String, String> mapCountList) {
        this.mMapCountList = mapCountList;
    }

}
