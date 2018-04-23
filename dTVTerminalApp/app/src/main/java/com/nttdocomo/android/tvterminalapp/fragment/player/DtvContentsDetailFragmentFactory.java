/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.nttdocomo.android.tvterminalapp.fragment.player;

import android.support.v4.app.Fragment;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * DTVコンテンツ詳細フラグメントファクトリー.
 */
public class DtvContentsDetailFragmentFactory {
    /**フラグメント初期化.*/
    private final List<Fragment> mFragments = new ArrayList<>();

    /**
     * コントラクター.
     */
    public DtvContentsDetailFragmentFactory() {
        mFragments.add(null);
        mFragments.add(null);
    }

    /**
     * フラグメントクラスの生成、取得.
     *
     * @param position position
     * @return fragment
     */
    public Fragment createFragment(final int position) {
        DTVTLogger.start();
        Fragment fragment = null;
        if (mFragments.size() > position) {
            fragment = mFragments.get(position);
        }
        if (fragment == null) {
            if (position == 0) {
                fragment = new DtvContentsDetailFragment();
            } else {
                fragment = new DtvContentsChannelFragment();
            }
            mFragments.add(position, fragment);
        }
        return fragment;
    }

    /**
     * フラグメントクラスの削除.
     */
    public void delFragment() {
        mFragments.remove(mFragments.size() - 1);
    }
}