/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

public class VideoGenreList {

    private String mGenreId;
    private String mTitle;
    private String mContentCount;

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

}
