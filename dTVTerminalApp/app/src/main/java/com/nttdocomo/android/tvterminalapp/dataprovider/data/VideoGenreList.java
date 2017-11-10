/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.List;
import java.util.Map;

public class VideoGenreList {

    private String mGenreId;
    private String mTitle;
    private String mContentCount;
    private String mAllContentCount;
    private List<Map<String, String>> mCountList;
    private List<Map<String, String>> mTitleList;

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

    public String getAllContentCount() {
        return mAllContentCount;
    }

    public void setAllContentCount(String allContentCount) {
        this.mAllContentCount = allContentCount;
    }

    public List<Map<String, String>> getCountList() {
        return mCountList;
    }

    public void setCountList(List<Map<String, String>> countList) {
        this.mCountList = countList;
    }

    public List<Map<String, String>> getTitleList() {
        return mTitleList;
    }

    public void setTitleList(List<Map<String, String>> titleList) {
        this.mTitleList = titleList;
    }

}
