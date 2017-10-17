package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import com.nttdocomo.android.tvterminalapp.model.search_temp.SearchContentInfo;

import java.util.ArrayList;

public class TotalSearchResponseData {

    public class ServiceCount {
        int serviceId;
        int contentCount;
    }

    public class Content {
        int rank;
        String ctId;
        int serviceId;
        String ctPicURL;
        String title;
        //String person;    //iosソースより、保留
        //int titleKind;    //iosソースより、保留
    }

    public TotalSearchResponseData() {
        serviceCountList = new ArrayList<ServiceCount>();  //[TotalSearchResponseData.ServiceCount]()
        contentList = new ArrayList<Content>();
    }

    public String status;      //処理結果
    public int totalCount;   //検索結果合計件数
    public String query;       //クエリ
    public int startIndex;    //検索結果返却開始位置
    public int resultCount;

    public ArrayList<ServiceCount> serviceCountList;
    public ArrayList<Content> contentList;

    public void map(ArrayList<SearchContentInfo> searchContentInfoArray){
        if(null == searchContentInfoArray){
            searchContentInfoArray=new ArrayList<SearchContentInfo>();
        } else {
            searchContentInfoArray.clear();
        }
        for(Content content: contentList){
            SearchContentInfo info= new SearchContentInfo(false, content.ctId, content.serviceId, content.ctPicURL, content.title);
            searchContentInfoArray.add(info);
        }
    }

    public void appendServiceCount(){
        serviceCountList.add(new ServiceCount());
    }

    public void appendContent(){
        contentList.add(new Content());
    }
}
