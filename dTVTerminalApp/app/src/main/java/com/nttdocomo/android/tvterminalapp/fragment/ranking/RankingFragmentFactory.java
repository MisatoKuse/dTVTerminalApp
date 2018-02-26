/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;

import java.util.HashMap;
import java.util.Map;

public class RankingFragmentFactory {

    private final Map<Integer, RankingBaseFragment> mFragments = new HashMap<>();

    /**
     * フラグメントクラスの生成、取得.
     *
     * @param position
     * @param lis
     * @return
     */
    public RankingBaseFragment createFragment(final ContentsAdapter.ActivityTypeItem mode, final int position, final RankingFragmentScrollListener lis) {
        RankingBaseFragment fragment;
        fragment = mFragments.get(position);
        if (fragment == null) {
            fragment = new RankingBaseFragment();
            fragment.switchRankingMode(mode);
            fragment.setRankingBaseFragmentScrollListener(lis);
            mFragments.put(position, fragment);
        }
        return fragment;
    }
}