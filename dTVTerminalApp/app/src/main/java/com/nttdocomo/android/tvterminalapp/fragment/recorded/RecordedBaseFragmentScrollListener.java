/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

import android.widget.AbsListView;

import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragment;

public interface RecordedBaseFragmentScrollListener {
    //public void onScrollStateChanged(SearchBaseFragment fragment, AbsListView absListView, int scrollState);
    public void onScroll(RecordedBaseFrgament fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}