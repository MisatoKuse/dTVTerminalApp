/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.FOUR_K_FLG;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.UNDER_BAR_FOUR_K_FLG;

public class DBUtils {

    /**
     * Jsonのキー名の"4kflg"によるDBエラー回避用
     *
     * @param string
     * @return
     */
    public static String fourKFlgConversion(String string) {
        String s = string;
        if (string.equals(FOUR_K_FLG)) {
            s = UNDER_BAR_FOUR_K_FLG;
        }
        return s;
    }
}
