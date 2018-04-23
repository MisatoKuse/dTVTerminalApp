/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;

/**
 * 番組表リサイクラービュー.
 */
public class ProgramRecyclerView extends RecyclerView implements AbsListView.OnScrollListener {
    /**
     * コンストラクタ.
     * @param context context
     */
    public ProgramRecyclerView(final Context context) {
        super(context);
        initView();
    }

    /**
     *  コンストラクタ.
     * @param context context
     * @param attrs attrs
     */
    public ProgramRecyclerView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * コンストラクタ.
     * @param context context
     * @param attrs attrs
     * @param defStyle defStyle
     */
    public ProgramRecyclerView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /**
     * initView.
     */
    private void initView() {
        //何もしない
    }

    /**
     * forceToDispatchTouchEvent.
     * @param ev ev
     * @return true or false
     */
    public boolean forceToDispatchTouchEvent(final MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(false);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(final AbsListView absListView, final int i) {

    }

    @Override
    public void onScroll(final AbsListView absListView, final int i, final int i1, final int i2) {

    }
}