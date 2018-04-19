/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.channellist;

import com.nttdocomo.android.tvterminalapp.activity.tvprogram.ChannelListActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * チャンネルリスト用フラグメントファクトリー.
 */
public class ChannelListFragmentFactory {
    /**fragment格納マップ.*/
    private final Map<Integer, ChannelListFragment> mFragments = new HashMap<>();

    /**
     *  Fragment生成.
     * @param position タブPosition
     * @param lis  ScrollListener
     * @param type データタイプ
     * @return fragment
     */
    public ChannelListFragment createFragment(final int position, final ChannelListFragment.ChannelListFragmentListener lis,
                                              final ChannelListActivity.ChListDataType type) {
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
