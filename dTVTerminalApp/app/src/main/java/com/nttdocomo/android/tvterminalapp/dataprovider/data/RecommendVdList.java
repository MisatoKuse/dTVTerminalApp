/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * おすすめビデオデータ管理クラス.
 */
public class RecommendVdList {
    /**おすすめビデオマップ.*/
    private HashMap<String, String> mRvMap = new HashMap<String, String>();
    /**おすすめビデオリスト.*/
    private List<Map<String, String>> mRvList = new ArrayList<>();

    /**
     * おすすめビデオマップ取得.
     * @return おすすめビデオマップ
     */
    public HashMap<String, String> getmRvMap() {
        return mRvMap;
    }

    /**
     * おすすめビデオマップ設定.
     * @param mRvMap おすすめビデオマップ
     */
    public void setmRvMap(final HashMap<String, String> mRvMap) {
        this.mRvMap = mRvMap;
    }

    /**
     *おすすめビデオリスト取得.
     * @return おすすめビデオリスト
     */
    public List<Map<String, String>> getmRvList() {
        return mRvList;
    }

    /**
     *おすすめビデオリスト設定.
     * @param mRvList おすすめビデオリスト
     */
    public void setmRvList(final List<Map<String, String>> mRvList) {
        this.mRvList = mRvList;
    }
}

