/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.callback;

import java.util.List;
import java.util.Map;

public interface VideoRankingApiDataProviderCallback {
    /**
     * 取得条件"総合"用コールバック
     *
     * @param videoRankMapList
     */
    void videoRankSynthesisCallback(List<Map<String, String>> videoRankMapList);

    /**
     * 取得条件"海外映画"用コールバック
     *
     * @param videoRankMapList
     */
    void videoRankOverseasMovieCallback(List<Map<String, String>> videoRankMapList);

    /**
     * 取得条件"国内映画"用コールバック
     *
     * @param videoRankMapList
     */
    void videoRankDomesticMovieCallback(List<Map<String, String>> videoRankMapList);

    /**
     * 取得条件"海外TV番組・ドラマ"用コールバック
     *
     * @param videoRankMapList
     */
    void videoRankOverseasChannelCallback(List<Map<String, String>> videoRankMapList);
}