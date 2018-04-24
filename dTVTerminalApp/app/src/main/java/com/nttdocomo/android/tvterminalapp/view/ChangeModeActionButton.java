/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 番組表縮小、拡大版切り替えブタンクラス.
 */
public class ChangeModeActionButton extends RelativeLayout {
    /**
     * コンストラクタ.
     * @param context context
     */
    public ChangeModeActionButton(final Context context) {
        super(context);
    }

    /**
     * コンストラクタ.
     * @param context  context
     * @param attrs attrs
     */
    public ChangeModeActionButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * コンストラクタ.
     * @param context context
     * @param attrs attrs
     * @param defStyleAttr defStyleAttr
     */
    public ChangeModeActionButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}