/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 番組表データ管理クラス.
 */
public class TvScheduleList {
    /**
     * 番組表マップ.
     */
    private Map<String, String> mTvsMap = new HashMap<>();
    /**
     * 番組表リスト.
     */
    private List<Map<String, String>> mTvsList = new ArrayList<>();

    /**
     * 番組表マップを取得する.
     *
     * @return 番組表マップ
     */
    @SuppressWarnings("unused")
    public Map getTvsMap() {
        return mTvsMap;
    }

    /**
     * 番組表リストを設定する.
     *
     * @param tvsMap 番組表リスト.
     */
    public void setTvsMap(final Map<String, String> tvsMap) {
        this.mTvsMap = tvsMap;
    }

    /**
     * 番組表リストを主憶する.
     *
     * @return 番組表リスト
     */
    public List<Map<String, String>> geTvsList() {
        return mTvsList;
    }

    /**
     *　番組表リストを設定する.
     *
     * @param tvsList 番組表リスト
     */
    public void setTvsList(final List<Map<String, String>> tvsList) {
        this.mTvsList = tvsList;
    }

    /**
     * データをクリアして、ガベージコレクションされやすくする
     */
    public void clearData() {
        if(mTvsMap != null) {
            mTvsMap.clear();
        }
        if(mTvsList != null) {
            mTvsList.clear();
        }
    }
}
