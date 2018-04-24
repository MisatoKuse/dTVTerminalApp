/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 * サーチフィルタータイプ.
 */
public enum SearchFilterType {
    /**:「ジャンル」:映画.*/
    genreMovie,
    /**:「吹替」：字幕のみ.*/
    dubbedText,
    /**:「吹替」:吹替のみ.*/
    dubbedDubbed,
    /**「吹替」:字幕/吹替両対応.*/
    dubbedTextAndDubbing,
    /**「課金方法」:見放題.*/
    chargeUnlimited,
    /**「課金方法」:レンタル.*/
    chargeRental,
    /**「その他絞込み条件」：HD作品.*/
    otherHdWork;
}
