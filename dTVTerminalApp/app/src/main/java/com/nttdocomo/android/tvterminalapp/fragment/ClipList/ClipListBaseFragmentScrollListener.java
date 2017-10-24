/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ClipList;

import android.widget.AbsListView;

public interface ClipListBaseFragmentScrollListener {
    public void onScroll(ClipListBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    public void onScrollStateChanged(ClipListBaseFragment fragment, AbsListView absListView, int scrollState);
}
