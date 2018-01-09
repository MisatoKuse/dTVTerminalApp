/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

public class UserInfoUtils {

    /**
     * レコメンドサーバ用に、ユーザ年齢情報を＋3して返却する
     *
     * @param userAge
     * @return
     */
    public static String getRecommendUserAge(int userAge) {
        final int RECOMMEND_AGE_MARGIN = 3;
        if (userAge >= 0) {
            userAge = userAge + RECOMMEND_AGE_MARGIN;
            return String.valueOf(userAge);
        } else {
            return null;
        }
    }
}
