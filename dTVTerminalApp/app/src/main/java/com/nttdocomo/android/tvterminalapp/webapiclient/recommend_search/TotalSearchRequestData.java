/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import java.util.ArrayList;

public class TotalSearchRequestData {

    //required
    public String userId = null;
    public final SearchFunctionType function = SearchFunctionType.none;
    public String query = null;       //複数条件の場合は半角スペース

    //optional
    //public String searchFields;
    public final ResponseType responseType = ResponseType.xml;
    //検索結果返却開始位置
    public int startIndex = 1;     //省略時は1
    //検索結果返却数
    public int maxResult = 1000;   //省略時は1000

    //ソート指定
    public int sortKind = 0;

    public ArrayList<SearchFilterType> filterTypeList = new ArrayList<SearchFilterType>();

    //取得対象サービスID
    public String serviceId = null;

    //検索絞込み用カテゴリーID
    public String categoryId = null;

    //ユーザ年齢フィルタリング情報
    public String filterViewableAge = null;
}
