/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 週間テレビランキングデータ管理クラス.
 */
public class WeeklyRankList {
    /**週間テレビランキングデータマップ.*/
    private HashMap<String, String> mWeeklyRankMap = new HashMap<>();
    /**週間テレビランキングデータ.*/
    private List<HashMap<String, String>> mWeeklyRankList = new ArrayList<>();

    /**
     * 拡張情報.
     **/
    private Bundle mExtraData = null;

    /**
     * 週間テレビランキングデータマップ取得.
     * @return 週間テレビランキングデータマップ
     */
    public HashMap getWeeklyRankMap() {
        return mWeeklyRankMap;
    }

    /**
     * 週間テレビランキングデータマップ設定.
     * @param wrMap 週間テレビランキングデータマップ
     */
    public void setWeeklyRankMap(final HashMap<String, String> wrMap) {
        this.mWeeklyRankMap = wrMap;
    }

    /**
     * 週間テレビランキングデータ取得.
     * @return 週間テレビランキングデータ
     */
    public List getWeeklyRankList() {
        return mWeeklyRankList;
    }

    /**
     * 週間テレビランキングデータ設定.
     * @param weeklyRankList 週間テレビランキングデータ
     */
    public void setWeeklyRankList(final List weeklyRankList) {
        this.mWeeklyRankList = weeklyRankList;
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
     * 拡張情報のセッター.
     *
     * @return 拡張情報
     */
    public Bundle getExtraData() {
        return mExtraData;
    }
}