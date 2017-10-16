package com.nttdocomo.android.tvterminalapp.beans;

import java.util.List;

public class HomeBean {

    /**
     * コンテンツ
     */
    private String contentTypeName;

    /**
     * コンテンツ数
     */
    private String contentCount;

    /**
     * コンテンツタイプ
     */
    private int contentType;

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

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public List<HomeBeanContent> getContentList() {
        return contentList;
    }

    public void setContentList(List<HomeBeanContent> contentList) {
        this.contentList = contentList;
    }
}
