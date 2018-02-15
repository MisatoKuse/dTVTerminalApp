/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.cliplist;

import java.util.HashMap;
import java.util.Map;

/**
 * クリップFragment生成.
 */
public class ClipListFragmentFactory {

    /**
     * Fragment格納Map.
     */
    private Map<Integer, ClipListBaseFragment> mFragments = new HashMap<>();

    /**
     * Fragment生成.
     *
     * @param position タブPosition
     * @param lis      ScrollListener
     * @return ClipListBaseFragment
     */
    public ClipListBaseFragment createFragment(final int position, final ClipListBaseFragmentScrollListener lis) {
        ClipListBaseFragment fragment;
        fragment = mFragments.get(position);
        if (fragment == null) {
            if (position == 0) {
                fragment = new ClipListBaseFragment();
            } else if (position == 1) {
                fragment = new ClipListBaseFragment();
            }
            if (fragment != null) {
                fragment.setClipListBaseFragmentScrollListener(lis);
                mFragments.put(position, fragment);
            }
        }
        return fragment;
    }

    /**
     * 生成したフラグメント数を返却する.
     *
     * @return fragmentCount
     */
    public synchronized int getFragmentCount() {
        if (null != mFragments) {
            return mFragments.size();
        }
        return 0;
    }
}
