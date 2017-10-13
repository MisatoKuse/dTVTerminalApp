package com.nttdocomo.android.tvterminalapp.Fragment.Search;

import android.widget.AbsListView;

public interface SearchBaseFragmentScrollListener{
    public void onScrollStateChanged(SearchBaseFragment fragment, AbsListView absListView, int scrollState);
    public void onScroll(SearchBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}