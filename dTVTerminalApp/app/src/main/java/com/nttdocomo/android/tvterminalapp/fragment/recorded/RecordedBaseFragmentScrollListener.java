/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

import android.widget.AbsListView;

public interface RecordedBaseFragmentScrollListener {
    //public void onScrollStateChanged(SearchBaseFragment fragment, AbsListView absListView, int scrollState);
    public void onScroll(RecordedBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}