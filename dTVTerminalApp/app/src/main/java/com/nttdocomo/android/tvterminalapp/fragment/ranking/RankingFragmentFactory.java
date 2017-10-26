/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListBaseFragmentScrollListener;

import java.util.HashMap;
import java.util.Map;

public class RankingFragmentFactory {

    private  Map<Integer, RankingBaseFragment> mFragments = new HashMap<Integer, RankingBaseFragment>();

    public RankingBaseFragment createFragment(int rankingMode, int position, RankingFragmentScrollListener lis) {
        RankingBaseFragment fragment = null;
        fragment = mFragments.get(position);
        if (fragment == null) {
            fragment = new RankingBaseFragment();
            fragment.initRankingView(rankingMode);
            fragment.setClipListBaseFragmentScrollListener(lis);
            mFragments.put(position, fragment);
        }
        return fragment;
    }
}
