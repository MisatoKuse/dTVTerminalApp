/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recommend;

import java.util.HashMap;
import java.util.Map;

public class RecommendFragmentFactory {

    private static final Map<Integer, RecommendBaseFragment> mFragments = new HashMap<>();

    public static synchronized RecommendBaseFragment createFragment(final int position, final RecommendBaseFragmentScrollListener lis) {
        RecommendBaseFragment fragment;
        fragment = mFragments.get(position);

        if (null == fragment) {
            fragment = new RecommendBaseFragment();
            if (fragment != null) {
                mFragments.put(position, fragment);
                fragment.setRecommendBaseFragmentScrollListener(lis);
            }
        }

        return fragment;
    }

    public static synchronized int getFragmentCount() {
        if (null != mFragments) {
            return mFragments.size();
        }

        return 0;
    }


}
