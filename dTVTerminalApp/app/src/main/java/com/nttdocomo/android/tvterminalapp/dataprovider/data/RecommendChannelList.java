/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * おすすめ番組データ管理クラス.
 */
public class RecommendChannelList {
    /**
     * おすすめ番組マップ.
     */
    private HashMap<String, String> mRcMap = new HashMap<String, String>();
    /**
     * おすすめ番組リスト.
     */
    private List<Map<String, String>> mRcList = new ArrayList<>();

    /**
     * おすすめ番組マップ取得.
     * @return おすすめ番組マップ
     */
    public HashMap<String, String> getmRcMap() {
        return mRcMap;
    }

    /**
     * おすすめ番組マップ設定.
     * @param mRcMap  おすすめ番組マップ
     */
    public void setmRcMap(final HashMap<String, String> mRcMap) {
        this.mRcMap = mRcMap;
    }
    /**
     * おすすめ番組リスト取得.
     * @return  おすすめ番組リスト
     */
    public List<Map<String, String>> getmRcList() {
        return mRcList;
    }
    /**
     * おすすめ番組リスト.
     * @param mRcList   おすすめ番組リスト
     */
    public void setmRcList(final List<Map<String, String>> mRcList) {
        this.mRcList = mRcList;
    }

}

