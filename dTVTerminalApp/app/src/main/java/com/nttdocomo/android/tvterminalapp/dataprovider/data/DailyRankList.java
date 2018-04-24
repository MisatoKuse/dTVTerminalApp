/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * デイリーランク一覧データ管理クラス.
 */
public class DailyRankList implements Serializable {

    private static final long serialVersionUID = -6794322997308686784L;
    /**デイリーランク一覧マップ.*/
    private HashMap<String, String> mDrMap = new HashMap<String, String>();
    /**デイリーランク一覧リスト.*/
    private List<HashMap<String, String>> mDrList = new ArrayList<>();

    /**
     * デイリーランク一覧マップ取得.
     * @return デイリーランク一覧マップ
     */
    public HashMap getVcMap() {
        return mDrMap;
    }

    /**
     * デイリーランク一覧マップ設定.
     * @param drMap デイリーランク一覧マップ
     */
    public void setDrMap(final HashMap<String, String> drMap) {
        this.mDrMap = drMap;
    }

    /**
     * デイリーランク一覧リスト取得.
     * @return デイリーランク一覧リスト
     */
    public List getDrList() {
        return mDrList;
    }

    /**
     * デイリーランク一覧リスト設定.
     * @param drList デイリーランク一覧リスト
     */
    public void setDrList(final List drList) {
        this.mDrList = drList;
    }


}
