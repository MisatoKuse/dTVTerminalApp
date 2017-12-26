/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.callback;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;

import java.util.List;

public interface VideoRankingApiDataProviderCallback {
    /**
     * 取得条件"総合"用コールバック
     *
     * @param videoRankList
     */
    void videoRankSynthesisCallback(List<ContentsData> videoRankList);

    /**
     * 取得条件"海外映画"用コールバック
     *
     * @param videoRankList
     */
    void videoRankOverseasMovieCallback(List<ContentsData> videoRankList);

    /**
     * 取得条件"国内映画"用コールバック
     *
     * @param videoRankList
     */
    void videoRankDomesticMovieCallback(List<ContentsData> videoRankList);

    /**
     * 取得条件"海外TV番組・ドラマ"用コールバック
     *
     * @param videoRankList
     */
    void videoRankOverseasChannelCallback(List<ContentsData> videoRankList);
}