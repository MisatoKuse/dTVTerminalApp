/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;

public class OtherContentsDataProvider {

    private HomeDataProvider.ApiDataProviderCallback apiDataProviderCallback;

    public interface ApiDataProviderCallback {
        /**
         * コンテンツ詳細表示データのコールバック
         *
         * @param detailData 　コンテンツ詳細データ
         */
        void otherContentsDataCallback(OtherContentsDetailData detailData);
    }

    /**
     * Activityからのデータ取得要求
     */
    public void getOtherDetailContentsData() {

    }

    /**
     * コンテンツ詳細データをActivityに送る
     */
    private void sendOtherDetailContentsData() {

    }
}
