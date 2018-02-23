/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.cliplist;

import android.widget.AbsListView;

public interface ClipListBaseFragmentScrollListener {
    void onScroll(ClipListBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);

    void onScrollStateChanged(ClipListBaseFragment fragment, AbsListView absListView, int scrollState);
}
