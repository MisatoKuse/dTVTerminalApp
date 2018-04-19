/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recommend;

import android.widget.AbsListView;

/**
 * レコメンドフラグメントリスナー.
 */
public interface RecommendBaseFragmentScrollListener {
     /**
      * Scroll call back.
      * @param fragment fragment
      * @param absListView absListView
      * @param firstVisibleItem firstVisibleItem
      * @param visibleItemCount visibleItemCount
      * @param totalItemCount totalItemCount
      */
     void onScroll(RecommendBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}