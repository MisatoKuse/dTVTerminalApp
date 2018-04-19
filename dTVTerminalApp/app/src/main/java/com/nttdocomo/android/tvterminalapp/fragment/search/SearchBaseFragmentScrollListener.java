/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.search;

import android.widget.AbsListView;

/**サーチフラグメントスクロールリスナー.*/
public interface SearchBaseFragmentScrollListener {
    //public void onScrollStateChanged(SearchBaseFragment fragment, AbsListView absListView, int scrollState);

    /**
     * onScroll callback.
     * @param fragment fragment
     * @param absListView absListView
     * @param firstVisibleItem firstVisibleItem
     * @param visibleItemCount visibleItemCount
     * @param totalItemCount totalItemCount
     */
    void onScroll(SearchBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}