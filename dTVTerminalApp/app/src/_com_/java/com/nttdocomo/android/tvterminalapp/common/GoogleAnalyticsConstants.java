/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

import com.nttdocomo.android.tvterminalapp.BuildConfig;
/**
 * 商用Googleアナリティクス.
 */
public class GoogleAnalyticsConstants {
    /** Googleアナリティクス検証用のID.*/
    private static final String GOOGLE_ANALYTICS_ID_DEVELOP = "UA-117255147-1";
    /** Googleアナリティクス商用のID.*/
    private static final String GOOGLE_ANALYTICS_ID = "UA-117251314-1";
    /** ビルドタイプ.*/
    private static final String BUILD_TYPE_UNSIGNED_OFF = "unsigned_off";
    /**
     * アナリティクストラキングID取得.
     * @return トラキングID
     */
    public static String getGoogleAnalyticsId() {
        if (BUILD_TYPE_UNSIGNED_OFF.equals(BuildConfig.BUILD_TYPE)) {
            return GOOGLE_ANALYTICS_ID;
        } else {
            return GOOGLE_ANALYTICS_ID_DEVELOP;
        }
    }
}
