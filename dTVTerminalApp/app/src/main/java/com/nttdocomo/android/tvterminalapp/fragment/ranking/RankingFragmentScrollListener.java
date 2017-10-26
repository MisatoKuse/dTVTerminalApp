/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ranking;

import android.widget.AbsListView;

import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListBaseFragment;

public interface RankingFragmentScrollListener {
    public void onScroll(RankingBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    public void onScrollStateChanged(RankingBaseFragment fragment, AbsListView absListView, int scrollState);
}
