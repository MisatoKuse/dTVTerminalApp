/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * クリップ(ビデオ)データ管理クラス.
 */
public class VodClipList implements Serializable {

    private static final long serialVersionUID = -1638810113668563728L;
    /**クリップ(ビデオ)データを収めるマップ.*/
    private HashMap<String, String> mVcMap = new HashMap<String, String>();
    /**クリップ(ビデオ)データ.*/
    private List<HashMap<String, String>> mVcList = new ArrayList<>();
    /**
     * クリップ(ビデオ)マップを取得.
     * @return クリップ(ビデオ)マップ
     * */
    public HashMap getVcMap() {
        return mVcMap;
    }
    /**
     * クリップ(ビデオ)マップを設定.
     * @param vcMap クリップ(ビデオ)マップ
     */
    public void setVcMap(final HashMap<String, String> vcMap) {
        this.mVcMap = vcMap;
    }
    /**
     * クリップ(ビデオ)データ取得.
     * @return クリップ(ビデオ)データ
     */
    public List getVcList() {
        return mVcList;
    }

    /**
     * クリップ(ビデオ)データ設定.
     * @param vcList クリップ(ビデオ)データ
     */
    public void setVcList(final List<HashMap<String, String>> vcList) {
        this.mVcList = vcList;
    }
}
