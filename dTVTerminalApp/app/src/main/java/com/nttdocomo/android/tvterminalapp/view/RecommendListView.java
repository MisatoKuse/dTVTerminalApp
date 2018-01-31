/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * おすすめ画像・ビデオ画面用リストビュー
 */
public class RecommendListView extends ListView {
    /**
     * XMLのレイアウト定義から呼ばれるコンストラクタ
     *
     * @param context コンテキスト
     * @param attrs   XMLレイアウトの情報
     */
    public RecommendListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            // タブ切り替え時のタイミングによっては、リストビュー側の準備完了前にリストの描画を
            // 開始しようとする場合がある。その場合は処理をスキップすることで、例外を回避する
            if (getAdapter().getItemId(getFirstVisiblePosition()) < 0) {
                //描画開始位置のデータが存在しないので、処理をスキップする
                return;
            }
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
            //上記の判定で回避できる筈だが、フェールセーフ用に設置
            DTVTLogger.debug(e);
        }
    }
}