/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;

public class ProgramScrollView extends ScrollView {

    private View mView;
    private TvProgramListActivity onScrollOffsetListener;

    public ProgramScrollView(final Context context) {
        super(context);
    }

    public ProgramScrollView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mView != null) {
            mView.scrollTo(l, t);
        }
        if (onScrollOffsetListener != null) {
            onScrollOffsetListener.onScrollOffset(t);
        }
    }

    public void setScrollView(final View view) {
        mView = view;
    }

    /**
     * スクロール時、リスナー設置.
     * @param onScrollOffsetListener
     */
    public void setOnScrollOffsetListener(final TvProgramListActivity onScrollOffsetListener) {
        this.onScrollOffsetListener = onScrollOffsetListener;
    }

    /**
     * スクロールされた距離リスナー
     */
    public interface OnScrollOffsetListener {
        void onScrollOffset(int offset);
    }
}