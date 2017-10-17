package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import java.util.ArrayList;

public class TotalSearchRequestData {

    //required
    public String userId;
    public final SearchFunctionType function = SearchFunctionType.none;
    public String query;       //複数条件の場合は半角スペース

    //optional
    //public String searchFields;
    public final ResponseType responseType = ResponseType.xml;
    public int startIndex = 1;     //省略時は1
    public int maxResult = 1000;   //省略時は1000

    //public String getColumn;
    public int sortKind;

    public ArrayList<SearchFilterType> filterTypeList=new ArrayList<SearchFilterType>();

    public String condition;

    public String serviceId;   //仕様確認QA中
}
