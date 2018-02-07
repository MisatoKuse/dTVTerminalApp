/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.search;

import android.util.SparseArray;

/**
 * 検索用Fragment作成クラス.
 */
public class SearchFragmentFactory {

    /**
     * Fragmentを保持するArray.
     */
    private SparseArray<SearchBaseFragment> mFragments = new SparseArray<>();

    /**
     * コンストラクタ.
     */
    public SearchFragmentFactory() {
    }

    /**
     * フラグメントを作成する.
     *
     * @param position 番号
     * @param listener リスナー
     * @return フラグメント
     */
    public synchronized SearchBaseFragment createFragment(final int position,
                                                          final SearchBaseFragmentScrollListener listener) {
        SearchBaseFragment fragment = mFragments.get(position);

        if (null == fragment) {
            fragment = new SearchBaseFragment();
            mFragments.put(position, fragment);
            fragment.setSearchBaseFragmentScrollListener(listener);
        }
        return fragment;
    }

    /**
     * フラグメント数を返却する.
     *
     * @return フラグメント数
     */
    public synchronized int getFragmentCount() {
        if (null != mFragments) {
            return mFragments.size();
        }
        return 0;
    }
}
