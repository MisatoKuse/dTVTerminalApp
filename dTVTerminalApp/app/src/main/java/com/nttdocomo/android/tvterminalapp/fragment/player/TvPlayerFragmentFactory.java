/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.player;

import java.util.HashMap;
import java.util.Map;

public class TvPlayerFragmentFactory {

    private Map<Integer, TvPlayerBaseFragment> mFragmentMap = new HashMap<>();

    public TvPlayerBaseFragment createFragment(int position, TvPlayerFragmentScrollListener lis){
        TvPlayerBaseFragment fragment = null;
        fragment = mFragmentMap.get(position);
        if (fragment == null) {
            fragment = new TvPlayerBaseFragment();
//            fragment.switchRankingMode(mode);
//            fragment.setRankingBaseFragmentScrollListener(lis);
            mFragmentMap.put(position, fragment);
        }
        return fragment;
    }
}
