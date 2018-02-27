/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recommend;

import java.util.HashMap;
import java.util.Map;

/**
 * レコメンド用Fragment生成.
 */
public class RecommendFragmentFactory {

    /**
     * Fragment格納用.
     */
    private static final Map<Integer, RecommendBaseFragment> mFragments = new HashMap<>();

    /**
     * Fragment生成.
     *
     * @param position position
     * @return fragment
     */
    public static synchronized RecommendBaseFragment createFragment(final int position) {
        RecommendBaseFragment fragment;
        fragment = mFragments.get(position);

        if (null == fragment) {
            fragment = new RecommendBaseFragment();
            mFragments.put(position, fragment);
        }

        return fragment;
    }

    /**
     * Fragment数取得.
     *
     * @return FragmentCount
     */
    public static synchronized int getFragmentCount() {
        return mFragments.size();
    }


}
