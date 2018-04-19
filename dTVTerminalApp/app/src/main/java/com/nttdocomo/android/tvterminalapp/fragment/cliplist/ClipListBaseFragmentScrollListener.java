/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.cliplist;

import android.widget.AbsListView;

/**
 * クリップ一覧スクロールリスナー.
 */
public interface ClipListBaseFragmentScrollListener {
    /**
     * scroll callback.
     * @param fragment fragment
     * @param absListView absListView
     * @param firstVisibleItem firstVisibleItem
     * @param visibleItemCount visibleItemCount
     * @param totalItemCount totalItemCount
     */
    void onScroll(ClipListBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);

    /**
     * scroll state changed callback.
     * @param fragment fragment
     * @param absListView absListView
     * @param scrollState scrollState
     */
    void onScrollStateChanged(ClipListBaseFragment fragment, AbsListView absListView, int scrollState);
}
