
/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.player;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.HashMap;
import java.util.Map;

public class DtvContentsDetailFragmentFactory {

    private Map<Integer, DtvContentsDetailBaseFragment> mFragments = new HashMap<>();

    /**
     * フラグメントクラスの生成、取得
     *
     * @param position
     * @return
     */
    public DtvContentsDetailBaseFragment createFragment(int position) {
        DTVTLogger.start();
        DtvContentsDetailBaseFragment fragment = mFragments.get(position);
        if (fragment == null) {
            fragment = new DtvContentsDetailBaseFragment();
            mFragments.put(position, fragment);
        }
        return fragment;
    }
}