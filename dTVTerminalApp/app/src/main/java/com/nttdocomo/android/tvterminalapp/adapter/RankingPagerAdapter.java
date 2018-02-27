/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentFactory;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentScrollListener;

/**
 * 検索結果タブ専用アダプター.
 */
public class RankingPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * フラグメント作成クラス.
     */
    private RankingFragmentFactory mRankingFragmentFactory = null;

    /**
     * タブ名.
     */
    private String[] mTabNames = null;

    /**
     * 表示項目のタイプ.
     */
    private ContentsAdapter.ActivityTypeItem mType;

    /**
     * リスナー.
     */
    private RankingFragmentScrollListener mRankingFragmentScrollListener = null;

    /**
     * コンストラクタ.
     *
     * @param fm FragmentManager
     */
    public RankingPagerAdapter(final FragmentManager fm, final ContentsAdapter.ActivityTypeItem mType) {
        super(fm);
        this.mType = mType;
    }

    /**
     * リスナーを設定.
     */
    public void setRankingFragmentScrollListener(final RankingFragmentScrollListener lis) {
        this.mRankingFragmentScrollListener = lis;
    }

    /**
     * リスナーを設定.
     */
    public void setTabNames(String[] mTabNames) {
        this.mTabNames = mTabNames;
    }

    /**
     * フラグメントを設定.
     */
    public void setRankingFragmentFactory(final RankingFragmentFactory mRankingFragmentFactory) {
        this.mRankingFragmentFactory = mRankingFragmentFactory;
    }

    @Override
    public Fragment getItem(final int position) {
        return mRankingFragmentFactory.createFragment(mType, position, mRankingFragmentScrollListener);
    }

    @Override
    public int getCount() {
        return mTabNames.length;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return mTabNames[position];
    }
}
