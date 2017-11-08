/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

public class OtherContentsDataProvider {

    private Context mContext;
    // RakingTop画面用コールバック
    private ApiDataProviderCallback apiDataProviderCallback = null;

    /**
     * Ranking Top画面用データを返却するためのコールバック
     */
    public interface ApiDataProviderCallback {
        /**
         * デイリーランキング用コールバック
         */
        void otherContentsDetailCallback();
    }

    /**
     * コンストラクタ
     *
     * @param context
     */
    public OtherContentsDataProvider(Context context) {
        this.mContext = context;
        this.apiDataProviderCallback = (ApiDataProviderCallback) context;
    }

    /**
     * ContentsDetailActivityからのデータ取得要求
     * @param tabPageNo
     */
    public void getContentsDetailData(int tabPageNo) {
        // TODO：WebApi実装後に処理を作成する
    }
    /**
     * コンテンツ詳細データをContentsDetailActivityに送る
     *
     */
    public void sendContentsDetailData() {
        // TODO：WebApi実装後に処理を作成する
    }

    /**
     * コンテンツ詳細データを取得する
     */
    private void getContentsDetailData() {
        // TODO：WebApi実装後に処理を作成する
    }
}
