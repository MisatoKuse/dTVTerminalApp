/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.nttdocomo.android.tvterminalapp.fragment.player;
import android.support.v4.app.Fragment;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

public class DtvContentsDetailFragmentFactory {

    private final Fragment[] mFragments = new Fragment[2];

    /**
     * フラグメントクラスの生成、取得
     *
     * @param position position
     * @return fragment
     */
    public Fragment createFragment(int position) {
        DTVTLogger.start();
        Fragment fragment = mFragments[position];
        if (fragment == null) {
            if(position == 0){
                fragment = new DtvContentsDetailFragment();
            } else {
                fragment = new DtvContentsChannelFragment();
            }
            mFragments[position] = fragment;
        }
        return fragment;
    }
}