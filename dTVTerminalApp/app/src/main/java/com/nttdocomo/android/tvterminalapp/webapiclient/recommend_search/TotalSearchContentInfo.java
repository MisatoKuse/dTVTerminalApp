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
    private int mTotalCount;
    /**検索結果詳細.*/
    private ArrayList<SearchContentInfo> mSearchContentInfoList;
    /**.*/
    private ResultType mResultType;
    /**コンテンツデータ.*/
    private List<ContentsData> mContentsDataList = new ArrayList<>();

    /**
     *init.
     * @param totalCount トータル数
     * @param searchContentList  検索結果詳細
     */
    public void init(final int totalCount, final ArrayList<SearchContentInfo> searchContentList) {
        this.mTotalCount = totalCount;
        this.mSearchContentInfoList = searchContentList;
    }

    /**
     *init.
     * @param resultType  resultType
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
     * 検索結果詳細データ数取得.
     * @return 検索結果詳細データ数
     */
    public int getContentsDataListSize() {
        return mSearchContentInfoList.size();
    }

    /**
     * コンテンツデータ設定.
     * @param mContentsDataList コンテンツデータ
     */
    public void setContentsDataList(final List<ContentsData> mContentsDataList) {
        this.mContentsDataList = mContentsDataList;
    }

    public SearchContentInfo getSearchContentInfoIndex(final int index) {
        return mSearchContentInfoList.get(index);
    }

    public int getTotalCount() {
        return mTotalCount;
    }
}
