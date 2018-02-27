/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class GenreListResponse implements Serializable {
    private static final long serialVersionUID = -1211331811012005279L;
    private String mUpdateDate; //UpdateDate

    //複数のジャンルの種別を収めるハッシュマップ
    private Map<String, ArrayList<GenreListMetaData>> mTypeList;

    public Map<String, ArrayList<GenreListMetaData>> getTypeList() {
        return mTypeList;
    }

    public void setTypeList(final Map<String, ArrayList<GenreListMetaData>> typeList) {
        mTypeList = typeList;
    }

    /**
     * 種別リストに今の種別配列を追加する.
     *
     * @param itemName リスト名
     * @param typeList リスト情報
     */
    public void addTypeList(final String itemName, final ArrayList<GenreListMetaData> typeList) {
        mTypeList.put(itemName, typeList);
    }

    public static final String GENRE_LIST_RESPONSE_UPDATE_DATE = "UpdateDate";

    public String getUpdateDate() {
        return mUpdateDate;
    }

    public void setUpdateDate(final String UpdateDate) {
        mUpdateDate = UpdateDate;
    }


    public GenreListResponse() {
        mUpdateDate = GENRE_LIST_RESPONSE_UPDATE_DATE;     //UpdateDate

        //複数ジャンル種別のマップの初期化
        mTypeList = new LinkedHashMap<String, ArrayList<GenreListMetaData>>();
    }
}
