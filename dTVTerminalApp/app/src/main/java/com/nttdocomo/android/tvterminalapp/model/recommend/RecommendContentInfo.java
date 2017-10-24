/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.recommend;


public class RecommendContentInfo {
    public boolean clipFlag;
    public String contentId;
    public int categoryId;
    public int serviceId;
    public String contentPictureUrl;
    public String title;
    public String startViewing;

    public RecommendContentInfo(String contentId, int categoryId, int serviceId, String contentPictureUrl, String title, String startViewing) {
        this.contentId = contentId;
        this.categoryId = categoryId;
        this.serviceId = serviceId;
        this.contentPictureUrl = contentPictureUrl;
        this.title = title;
        this.startViewing = startViewing;
    }
}
