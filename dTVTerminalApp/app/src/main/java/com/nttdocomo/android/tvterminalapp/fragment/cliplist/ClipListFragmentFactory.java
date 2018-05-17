/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.cliplist;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * クリップFragment生成.
 */
public class ClipListFragmentFactory {

    /**
     * Fragment格納Map.
     */
    private final Map<Integer, ClipListBaseFragment> mFragments = new HashMap<>();

    /**
     * ポジション.
     */
    public static final String POSITION = "position";
    /**
     * ポジション0.
     */
    private static final int POSITION_ZERO = 0;
    /**
     * ポジション1.
     */
    private static final int POSITION_ONE = 1;

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
            Bundle bundle = new Bundle();
            switch (position) {
                case POSITION_ZERO:
                    fragment = new ClipListBaseFragment();
                    bundle.putInt(POSITION, position);
                    fragment.setArguments(bundle);
                    break;
                case POSITION_ONE:
                    fragment = new ClipListBaseFragment();
                    bundle.putInt(POSITION, position);
                    fragment.setArguments(bundle);
                    break;
                default:
                    break;
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

        return mFragments.size();
    }
}
