/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ClipList;

import java.util.HashMap;
import java.util.Map;

public class ClipListFragmentFactory {

    private  Map<Integer, ClipListBaseFragment> mFragments = new HashMap<Integer, ClipListBaseFragment>();

    public ClipListBaseFragment createFragment(int position, ClipListBaseFragmentScrollListener lis) {
        ClipListBaseFragment fragment = null;
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
}
