/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 * WebApiコールバック.
 */
public interface WebApiCallback {
        /**
         * 処理完了コールバック.
         * @param responseData レスポンスデータ
         */
        void onFinish(String responseData);
}
