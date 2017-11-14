/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;

public class VideoGenreList {
    private String mGenreId;
    private String mTitle;
    private String mContentCount;
    private ArrayList<GenreListMetaData.SubContent> mSubGenre;


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

    public ArrayList<GenreListMetaData.SubContent> getSubGenre() {
        return mSubGenre;
    }

    public void setSubGenre(ArrayList<GenreListMetaData.SubContent> subGenre) {
        this.mSubGenre = subGenre;
    }
}
