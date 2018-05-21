/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.R;


public class HomeRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    public HomeRecyclerViewItemDecoration(Context context) {
        space = context.getResources().getDimensionPixelSize(R.dimen.home_contents_item_left_margin);
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.left = space;
        } else {
            outRect.left = space/2;
        }

    }

}
