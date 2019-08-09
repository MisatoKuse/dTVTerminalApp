/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.nttdocomo.android.tvterminalapp.fragment.player;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.ContentDetailUtils;

/**
 * コンテンツ詳細フラグメントファクトリー.
 */
public class DtvContentsDetailFragmentFactory {
    /**フラグメント初期化.*/
    private final SparseArray<Fragment> mFragments = new SparseArray<>();

    /**
     * フラグメントクラスの生成、取得.
     * @param position タブポジション
     * @param tabType タブタイプ
     * @return fragment
     */
    public Fragment createFragment(final int position, final ContentDetailUtils.TabType tabType) {
        DTVTLogger.start();
        Fragment fragment = mFragments.get(position);
        if (fragment == null) {
            if (position == 0) {
                fragment = new DtvContentsDetailFragment();
            } else {
                if (ContentDetailUtils.TabType.VOD_EPISODE == tabType) {
                    fragment = new DtvContentsEpisodeFragment();
                } else {
                    fragment = new DtvContentsChannelFragment();
                }
            }
            mFragments.put(position, fragment);
        }
        return fragment;
    }
}