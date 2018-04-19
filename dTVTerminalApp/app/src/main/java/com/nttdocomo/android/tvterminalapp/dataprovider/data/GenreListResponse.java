/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ジャンル一覧レスポンス.
 */
public class GenreListResponse implements Serializable {
    private static final long serialVersionUID = -1211331811012005279L;
    /**日付更新.*/
    private String mUpdateDate;

    /**複数のジャンルの種別を収めるハッシュマップ.*/
    private Map<String, ArrayList<GenreListMetaData>> mTypeList;
    /**ジャンルの種別を取得する.
     * @return ジャンルの種別
     */
    public Map<String, ArrayList<GenreListMetaData>> getTypeList() {
        return mTypeList;
    }
    /**ジャンルの種別設定する.
     * @param typeList ジャンルの種別
     */
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
    /**日付更新キー.*/
    public static final String GENRE_LIST_RESPONSE_UPDATE_DATE = "UpdateDate";

    /**更新日付を取得する.
     * @return 更新日付
     */
    public String getUpdateDate() {
        return mUpdateDate;
    }
    /** 更新日付を設定する.
     * @param UpdateDate 更新日付
     */
    public void setUpdateDate(final String UpdateDate) {
        mUpdateDate = UpdateDate;
    }

    /**複数ジャンル種別のマップの初期化.*/
    public GenreListResponse() {
        mUpdateDate = GENRE_LIST_RESPONSE_UPDATE_DATE;     //UpdateDate
        mTypeList = new LinkedHashMap<String, ArrayList<GenreListMetaData>>();
    }
}
