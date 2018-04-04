/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;

public class ProgramRecyclerView extends RecyclerView implements AbsListView.OnScrollListener {

    public ProgramRecyclerView(final Context context) {
        super(context);
        initView();
    }

    public ProgramRecyclerView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ProgramRecyclerView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        //何もしない
    }

    public boolean forceToDispatchTouchEvent(final MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(false);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}