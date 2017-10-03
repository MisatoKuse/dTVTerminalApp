package com.nttdocomo.android.tvterminalapp.beans;

public class HomeBeanContent{

    /**
     * コンテンツIMG URL
     */
    private String contentSrcURL;

    /**
     * コンテンツID
     */
    private String contentId;

    /**
     * コンテンツ名前
     */
    private String contentName;

    /**
     * コンテンツ時間
     */
    private String contentTime;

    /**
     * コンテンツ
     */
    private String Flg;

    public String getContentSrcURL() {
        return contentSrcURL;
    }

    public void setContentSrcURL(String contentSrcURL) {
        this.contentSrcURL = contentSrcURL;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentTime() {
        return contentTime;
    }

    public void setContentTime(String contentTime) {
        this.contentTime = contentTime;
    }

    public String getFlg() {
        return Flg;
    }

    public void setFlg(String flg) {
        Flg = flg;
    }
}
