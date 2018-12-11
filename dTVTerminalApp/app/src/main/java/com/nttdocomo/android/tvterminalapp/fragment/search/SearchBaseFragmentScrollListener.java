/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.search;

import android.widget.AbsListView;

/**サーチフラグメントスクロールリスナー.*/
public interface SearchBaseFragmentScrollListener {
    /**
     * onScroll callback.
     * @param fragment fragment
     * @param absListView absListView
     * @param scrollState scrollState
     */
    void onScrollChanged(SearchBaseFragment fragment, AbsListView absListView, int scrollState);
    /**
     * Fragment見えるのコールバック.
     * @param isVisibleToUser true:表示 false:非表示
     * @param searchBaseFragment フラグメント
     */
    void onUserVisibleHint(boolean isVisibleToUser, SearchBaseFragment searchBaseFragment);
}