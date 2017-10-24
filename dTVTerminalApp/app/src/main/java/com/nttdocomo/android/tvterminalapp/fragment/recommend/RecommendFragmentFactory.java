/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recommend;

import java.util.HashMap;
import java.util.Map;

public class RecommendFragmentFactory {

    private static Map<Integer, RecommendBaseFragment> mFragments = new HashMap<Integer, RecommendBaseFragment>();

    public static synchronized RecommendBaseFragment createFragment(int position, RecommendBaseFragmentScrollListener lis) {
        RecommendBaseFragment fragment = null;
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
