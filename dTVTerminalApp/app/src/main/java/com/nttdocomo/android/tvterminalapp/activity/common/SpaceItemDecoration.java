/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.common;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 機能
 * 横リサイクルビューの間隔を設定
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    int mSpace = 0;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.left = mSpace;
        }
    }

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
}
