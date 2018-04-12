/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.support.annotation.Nullable;

import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

/**
 * Activity関連のUtil
 */
public class ContentUtils {

    /**
     * 多階層コンテンツであるか判定する.
     * @param contentsData
     * @return
     */
    public static boolean isChildContentList(@Nullable final ContentsData contentsData) {
        if (null != contentsData) {
            return contentsData.hasChildContentList();
        }
        return false;
    }

}
