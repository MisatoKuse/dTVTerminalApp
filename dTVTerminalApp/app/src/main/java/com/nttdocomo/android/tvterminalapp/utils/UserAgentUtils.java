/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.os.Build;

import com.nttdocomo.android.tvterminalapp.BuildConfig;

import java.util.Formatter;

public class UserAgentUtils {
    /**端末情報（デバイス名）.*/
    private final static String BUILD_MODEL = Build.MODEL;
    /**アプリバージョン（バージョン名）.*/
    private final static String VERSION_NAME = BuildConfig.VERSION_NAME;
    /**端末情報（AndroidOSのバージョン番号）.*/
    private final static String BUILD_VERSION_RELEASE = Build.VERSION.RELEASE;
    /**端末情報（Android SDKのバージョン）.*/
    private final static String BUILD_VERSION_SDK_INT = String.valueOf(Build.VERSION.SDK_INT);
    /**UserAgentフォーマット.*/
    private final static String USER_AGENT_FORMAT = "DOCOMO/2.0 %s(DTVT;%s;Android;%s;%s);";

    /*
    * UserAgentを生成し、取得する
    *
    * @param なし
    * @return UserAgent文字列
    * */
    public static String getCustomUserAgent() {
        Formatter userAgentFormatter = new Formatter();
        String customUserAgent = userAgentFormatter.format(USER_AGENT_FORMAT, BUILD_MODEL, VERSION_NAME, BUILD_VERSION_RELEASE, BUILD_VERSION_SDK_INT).toString();

        return  customUserAgent;
    }
}
