/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recommend;

import android.widget.AbsListView;

import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragment;

public interface RecommendBaseFragmentScrollListener {
    public void onScroll(RecommendBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}