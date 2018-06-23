/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.struct.Pager;

import java.util.ArrayList;

/**
 * 子コンテンツ一覧取得APIパース用.
 */
public class ChildContentListGetResponse {

    /**
     * ステータス.
     */
    private String mStatus;
    /**
     * ページャー.
     */
    private final Pager mPager;
    /**
     * フルメタデータリスト.
     */
    private ArrayList<VodMetaFullData> mVodMetaFullData;
    /**
     * 構造体.
     */
    public ChildContentListGetResponse() {
        mStatus = "";
        mPager = new Pager();
        mVodMetaFullData = new ArrayList<>();
    }

    /**
     * ステータスを取得する.
     * @return ステータス
     */
    public String getStatus() {
        return mStatus;
    }

    /**
     * ステータスを設定する.
     * @param status ステータス
     */
    public void setStatus(final String status) {
        mStatus = status;
    }

    /**
     * ページャーを取得する.
     * @return ページャー
     */
    public Pager getPager() {
        return mPager;
    }

    /**
     * ページャーを設定する.
     * @param  limit レスポンスの最大件数
     * @param count レスポンス(list)件数
     * @param offset 取得位置(0～)
     * @param total 全体の件数
     */
    public void setPager(final int limit, final int offset, final int count, final int total) {
        mPager.setLimit(limit);
        mPager.setOffset(offset);
        mPager.setCount(count);
        mPager.setTotal(total);
    }

    /**
     * フルメタデータを取得する.
     * @return フルメタデータリスト
     */

    public ArrayList<VodMetaFullData> getVodMetaFullData() {
        return mVodMetaFullData;
    }
    /**
     * フルメタデータを設定する.
     * @param vodMetaFullData フルメタデータリスト
     */
    public void setVodMetaFullData(final ArrayList<VodMetaFullData> vodMetaFullData) {
        mVodMetaFullData = vodMetaFullData;
    }
}

