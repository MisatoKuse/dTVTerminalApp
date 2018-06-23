/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * 各種画面表示関連のUtils クラス.
 */
public class ViewUtils {
    /**
     * 指定されたビューの配下を丸ごとガベージコレクションしやすくする.
     *
     * @param view 解放されやすくしたいビュー
     */
    public static void cleanAllViews(final View view) {
        //各ビューの個別操作を行う
        if (view instanceof ImageButton) {
            ImageButton imageButton = (ImageButton) view;
            imageButton.setImageDrawable(null);
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            imageView.destroyDrawingCache();
            imageView.setImageDrawable(null);
        } else if (view instanceof ListView) {
            ListView listView = (ListView) view;
            listView.setAdapter(null);
        } else {
            DTVTLogger.debug("cleanAllViews other view");
        }
        //他に個別の連携切りが必要なビューは、処理の追加が必要

        //背景にヌルを指定
        view.setBackground(null);

        //配下のビューの有無の検査
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int size = viewGroup.getChildCount();

            //配下のビューがあったので、そちらも操作する
            for (int counter = 0; counter < size; counter++) {
                cleanAllViews(viewGroup.getChildAt(counter));
            }
        }
    }
}
