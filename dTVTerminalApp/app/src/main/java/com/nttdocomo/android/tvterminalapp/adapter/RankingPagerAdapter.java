/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentFactory;

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
    private final ContentsAdapter.ActivityTypeItem mType;

    /**
     * コンストラクタ.
     *
     * @param fm FragmentManager
     * @param mType タイプ
     */
    public RankingPagerAdapter(final FragmentManager fm, final ContentsAdapter.ActivityTypeItem mType) {
        super(fm);
        this.mType = mType;
    }

    /**
     * リスナーを設定.
     * @param mTabNames  タブ名.
     */
    public void setTabNames(final String[] mTabNames) {
        this.mTabNames = mTabNames;
    }

    /**
     * フラグメントを設定.
     * @param mRankingFragmentFactory  RankingFragmentFactory
     */
    public void setRankingFragmentFactory(final RankingFragmentFactory mRankingFragmentFactory) {
        this.mRankingFragmentFactory = mRankingFragmentFactory;
    }

    @Override
    public Fragment getItem(final int position) {
        return mRankingFragmentFactory.createFragment(mType, position);
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
