/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

/**
 * 検証用Googleアナリティクス.
 */
public class GoogleAnalyticsConstants {
    /** Googleアナリティクス用のID. */
    private static final String GOOGLE_ANALYTICS_ID_DEVELOP = "UA-117255147-1";
    /**
     * アナリティクストラキングID取得.
     * @return トラキングID
     */
    public static String getGoogleAnalyticsId() {
        return GOOGLE_ANALYTICS_ID_DEVELOP;
    }
}
