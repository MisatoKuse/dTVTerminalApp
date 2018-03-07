/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.channellist;

import com.nttdocomo.android.tvterminalapp.activity.tvprogram.ChannelListActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter;

import java.util.HashMap;
import java.util.Map;

public class ChannelListFragmentFactory {

    private final Map<Integer, ChannelListFragment> mFragments = new HashMap<>();

    public ChannelListFragment createFragment(final int position, final ChannelListFragment.ChannelListFragmentListener lis, final ChannelListActivity.ChListDataType type) {
        ChannelListFragment fragment;
        fragment = mFragments.get(position);
        if (fragment == null) {
            fragment = new ChannelListFragment();
            fragment.setScrollListener(lis);
            fragment.setChListDataType(type);
            mFragments.put(position, fragment);
        }
        return fragment;
    }
}
