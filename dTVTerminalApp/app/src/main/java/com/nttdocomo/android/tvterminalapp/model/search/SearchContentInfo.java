package com.nttdocomo.android.tvterminalapp.model.search;


public class SearchContentInfo {
    public boolean clipFlag; 
    public String contentId;
    public int  serviceId;
    public String contentPictureUrl;
    public String title;

    public SearchContentInfo(boolean clipFlag, String contentId, int serviceId, String contentPictureUrl, String title){
        this.clipFlag=clipFlag;
        this.contentId=contentId;
        this.serviceId=serviceId;
        this.contentPictureUrl=contentPictureUrl;
        this.title=title;
    }
}
