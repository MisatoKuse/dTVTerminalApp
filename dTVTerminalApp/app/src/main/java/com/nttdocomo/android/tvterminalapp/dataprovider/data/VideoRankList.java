/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ビデオランキング一覧データ管理クラス.
 */
public class VideoRankList {
    /**ビデオランキング一覧データを収めるマップ.*/
    private HashMap<String, String> mVrMap = new HashMap<>();
    /**ビデオランキング一覧データ.*/
    private List<HashMap<String, String>> mVrList = new ArrayList<>();

    /**
     * 拡張情報.
     **/
    private Bundle mExtraData = null;

    /**
     * ビデオランキング一覧データを収めるマップを取得.
     * @return ビデオランキング一覧データを収めるマップ
     */
    public HashMap<String, String> getVrMap() {
        return mVrMap;
    }

    /**
     * ビデオランキング一覧データを収めるマップ設定.
     * @param vrMap ビデオランキング一覧データを収めるマップ
     */
    public void setVrMap(final HashMap<String, String> vrMap) {
        this.mVrMap = vrMap;
    }

    /**
     * ビデオランキング一覧データ取得.
     * @return ビデオランキング一覧データ
     */
    public List getVrList() {
        return mVrList;
    }

    /**
     * ビデオランキング一覧データ設定.
     * @param vrList ビデオランキング一覧データ
     */
    public void setVrList(final List vrList) {
        this.mVrList = vrList;
    }

    /**
     * 拡張情報のセッター.
     *
     * @param extraDataSrc 代入する拡張情報
     */
    public void setExtraData(final Bundle extraDataSrc) {
        mExtraData = extraDataSrc;
    }

    /**
     * 拡張情報のゲッター.
     *
     * @return 拡張情報
     */
    public Bundle getExtraData() {
        return mExtraData;
    }

}
