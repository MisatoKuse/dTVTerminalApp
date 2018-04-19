/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *クリップ(TV)データ管理クラス.
 */
public class TvClipList implements Serializable {

    private static final long serialVersionUID = 3389140211376432479L;
    /**クリップ(TV)データマップ.*/
    private HashMap<String, String> mTcMap = new HashMap<>();
    /**クリップ(TV)データリスト.*/
    private List<HashMap<String, String>> mTcList = new ArrayList<>();

    /**
     * クリップ(TV)データマップを取得.
     * @return クリップ(TV)データマップ
     */
    public HashMap getVcMap() {
        return mTcMap;
    }

    /**
     * クリップ(TV)データマップ設定.
     * @param tcMap クリップ(TV)データマップ
     */
    public void setVcMap(final HashMap<String, String> tcMap) {
        this.mTcMap = tcMap;
    }

    /**
     * クリップ(TV)データリスト取得.
     * @return クリップ(TV)データリスト
     */
    public List getVcList() {
        return mTcList;
    }
    /**
     * クリップ(TV)データリスト設定.
     * @param tcList クリップ(TV)データリスト
     */
    public void setVcList(final List<HashMap<String, String>> tcList) {
        this.mTcList = tcList;
    }
}
