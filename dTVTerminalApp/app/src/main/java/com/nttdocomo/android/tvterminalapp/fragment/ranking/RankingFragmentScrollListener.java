/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import android.widget.AbsListView;

public interface RankingFragmentScrollListener {
    void onScroll(RankingBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);

    void onScrollStateChanged(RankingBaseFragment fragment, AbsListView absListView, int scrollState);
}