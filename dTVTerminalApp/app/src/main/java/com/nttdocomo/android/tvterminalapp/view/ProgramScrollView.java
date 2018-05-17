/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;

/**
 * 番組表スクロールビュー管理クラス.
 */
public class ProgramScrollView extends ScrollView {
    /**View.*/
    private View mView;
    /**番組表activity.*/
    private TvProgramListActivity mOnScrollOffsetListener;

    /**
     * コンストラクタ.
     * @param context context
     */
    public ProgramScrollView(final Context context) {
        super(context);
    }

    /**
     * コンストラクタ.
     * @param context context
     * @param attrs attrs
     */
    public ProgramScrollView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mView != null) {
            mView.scrollTo(l, t);
        }
        if (mOnScrollOffsetListener != null) {
            mOnScrollOffsetListener.onScrollOffset(t);
        }
    }

    /**
     * スクロールビュー設定.
     * @param view スクロール対象view
     */
    public void setScrollView(final View view) {
        mView = view;
    }

    /**
     * スクロール時、リスナー設置.
     * @param onScrollOffsetListener activity
     */
    public void setOnScrollOffsetListener(final TvProgramListActivity onScrollOffsetListener) {
        this.mOnScrollOffsetListener = onScrollOffsetListener;
    }

    /**
     * スクロールされた距離リスナー.
     */
    public interface OnScrollOffsetListener {
        /**
         * スクロール距離コールバック.
         * @param offset スクロールされた距離
         */
        void onScrollOffset(int offset);
    }
}