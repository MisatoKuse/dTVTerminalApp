/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nttdocomo.android.tvterminalapp.R;

/**
 * ホーム画面リサイクラービューアイテムスペース値設定用アクティビティ.
 */
public class HomeRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    /**スペースサイズ.*/
    private final int mSpace;
    /**
     * コンストラクタ.
     * @param context コンテキスト
     */
    public HomeRecyclerViewItemDecoration(final Context context) {
        mSpace = context.getResources().getDimensionPixelSize(R.dimen.home_contents_item_left_margin);
    }
    @Override
    public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.left = mSpace;
        } else {
            outRect.left = mSpace / 2;
        }

    }

}
