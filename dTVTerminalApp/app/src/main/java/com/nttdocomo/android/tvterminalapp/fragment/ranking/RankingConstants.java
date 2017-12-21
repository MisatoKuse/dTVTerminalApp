/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.ranking;

public class RankingConstants {

    public static final int RANKING_PAGE_NO_OF_SYNTHESIS = 0;                                                     // 総合
    public static final int RANKING_PAGE_NO_OF_OVERSEAS_MOVIE = RANKING_PAGE_NO_OF_SYNTHESIS + 1;          // 海外映画
    public static final int RANKING_PAGE_NO_OF_DOMESTIC_MOVIE = RANKING_PAGE_NO_OF_SYNTHESIS + 2;   // 国内映画
    public static final int RANKING_PAGE_NO_OF_OVERSEAS_CHANNEL = RANKING_PAGE_NO_OF_SYNTHESIS + 3;            // 海外TV番組・ドラマ


    public static final int RANKING_MODE_NO_OF_WEEKLY = 0; // 週間ランキング
    public static final int RANKING_MODE_NO_OF_VIDEO = RANKING_MODE_NO_OF_WEEKLY + 1; // ビデオランキング


    public static final String RANKING_GENRE_ID_SYNTHESIS = ""; // 総合（指定なし）
    public static final String RANKING_GENRE_ID_OVERSEAS_MOVIE = ""; //
    public static final String RANKING_GENRE_ID_DOMESTIC_MOVIE = "";
    public static final String RANKING_GENRE_ID_OVERSEAS_CHANNEL = "";

    private RankingConstants() {
        // nop.
    }
}