/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.ResultType;
import com.nttdocomo.android.tvterminalapp.struct.SearchContentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * トータルサーチコンテンツ情報管理クラス.
 */
public class TotalSearchContentInfo {
    /**トータル数.*/
    public int totalCount;
    /**検索結果詳細.*/
    public ArrayList<SearchContentInfo> searchContentInfo;
    /**.*/
    private ResultType mResultType;
    /**コンテンツデータ.*/
    private List<ContentsData> mContentsDataList = new ArrayList<>();

    /**
     *
     * @param totalCount
     * @param searchContentInfo
     */
    public void init(final int totalCount, final ArrayList<SearchContentInfo> searchContentInfo) {
        this.totalCount = totalCount;
        this.searchContentInfo = searchContentInfo;
    }

    /**
     *
     * @param resultType
     */
    public void init(final ResultType resultType) {
        mResultType = resultType;
    }

    /**
     * コンテンツデータ取得.
     * @return コンテンツデータ
     */
    public List<ContentsData> getContentsDataList() {
        return mContentsDataList;
    }

    /**
     * コンテンツデータ設定.
     * @param mContentsDataList コンテンツデータ
     */
    public void setContentsDataList(final List<ContentsData> mContentsDataList) {
        this.mContentsDataList = mContentsDataList;
    }
}
