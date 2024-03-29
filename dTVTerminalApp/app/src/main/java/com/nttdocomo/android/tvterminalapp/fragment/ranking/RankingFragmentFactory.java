/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * ランキングフラグメントファクトリー.
 */
public class RankingFragmentFactory {
    /**
     * Fragment格納Map.
     */
    private final Map<Integer, RankingBaseFragment> mFragments = new HashMap<>();

    /**
     * フラグメントクラスの生成、取得.
     *@param mode データタイプ
     * @param position position
     * @return fragment
     */
    public RankingBaseFragment createFragment(final ContentsAdapter.ActivityTypeItem mode, final int position) {
        RankingBaseFragment fragment;
        fragment = mFragments.get(position);
        if (fragment == null) {
            fragment = new RankingBaseFragment();
            fragment.switchRankingMode(mode);
            mFragments.put(position, fragment);
        }
        return fragment;
    }
}