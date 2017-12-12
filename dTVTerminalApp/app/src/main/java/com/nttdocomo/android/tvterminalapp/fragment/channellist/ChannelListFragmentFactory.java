/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.channellist;

import com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter;

import java.util.HashMap;
import java.util.Map;

public class ChannelListFragmentFactory {

    private  Map<Integer, ChannelListFragment> mFragments = new HashMap<Integer, ChannelListFragment>();

    public ChannelListFragment createFragment(int position, ChannelListFragment.ChannelListFragmentListener lis, ChannelListAdapter.ChListDataType type) {
        ChannelListFragment fragment = null;
        fragment = mFragments.get(position);
        if (fragment == null) {
            fragment = new ChannelListFragment();
            if (fragment != null) {
                fragment.setScrollListener(lis);
                fragment.setChListDataType(type);
                mFragments.put(position, fragment);
            }
        }
        return fragment;
    }
}
