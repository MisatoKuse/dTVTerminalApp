/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

/**
 * 色設定関連のUtilsクラス
 */
public class ColorUtils {

    private Context mContext;

    public ColorUtils(Context context) {
        mContext = context;
    }

    /**
     * getColor非推奨対応
     *
     * @param textView 変更View
     * @param colorId  色設定
     */
    public void setTextViewColor(TextView textView, int colorId) {
        Resources resources = mContext.getResources();
        final int SDK_VERSION_MARSHMALLOW = 23;
        if (Build.VERSION.SDK_INT < SDK_VERSION_MARSHMALLOW) {
            textView.setTextColor(resources.getColor(colorId));
        } else {
            textView.setTextColor(ContextCompat.getColor(mContext, colorId));
        }
    }

    /**
     * 色のリソースから色コードを返す.
     *
     * @param context コンテキスト
     * @param colorId 色のリソース番号
     * @return 色コード
     */
    public static int getColor(Context context, int colorId) {
        //色の結果
        int color;

        //コンテキストがヌルならば、色は取れないのでゼロを返す
        if(context == null) {
            return 0;
        }

        //バージョン番号で処理を分ける
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //OSが古いので、古い方法を使用する
            color = context.getResources().getColor(colorId);
        } else {
            //OSが新しいので、新しい方法を使用する
            color = ContextCompat.getColor(context, colorId);
        }

        //色コードを返す
        return color;
    }
}
