/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.callback;

import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.List;

public interface VideoRankingApiDataProviderCallback {
    /**
     * ジャンル系のコールバック
     *
     * @param videoRankList アダプターで使うデータ
     */
    void onVideoRankListCallback(List<ContentsData> videoRankList);
}