/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.beans;

import java.io.Serializable;
import java.util.List;

public class HomeBean implements Serializable {

    /**
     * コンテンツ
     */
    private String contentTypeName;

    /**
     * コンテンツ数
     */
    private String contentCount;

    /**
     * コンテンツリスト
     */
    private List<HomeBeanContent> contentList;

    public String getContentTypeName() {
        return contentTypeName;
    }

    public void setContentTypeName(String contentTypeName) {
        this.contentTypeName = contentTypeName;
    }

    public String getContentCount() {
        return contentCount;
    }

    public void setContentCount(String contentCount) {
        this.contentCount = contentCount;
    }

    public List<HomeBeanContent> getContentList() {
        return contentList;
    }

    public void setContentList(List<HomeBeanContent> contentList) {
        this.contentList = contentList;
    }
}
