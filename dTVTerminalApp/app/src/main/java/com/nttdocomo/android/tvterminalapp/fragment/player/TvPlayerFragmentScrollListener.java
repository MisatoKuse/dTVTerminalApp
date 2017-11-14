/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.player;

import android.widget.AbsListView;

public interface TvPlayerFragmentScrollListener {
    void onScroll(TvPlayerBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    void onScrollStateChanged(TvPlayerBaseFragment fragment, AbsListView absListView, int scrollState);
}
