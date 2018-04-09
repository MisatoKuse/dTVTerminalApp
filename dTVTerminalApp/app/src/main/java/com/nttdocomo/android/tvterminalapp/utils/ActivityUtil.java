/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nttdocomo.android.tvterminalapp.activity.common.ChildContentListActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

/**
 * Activity関連のUtil
 */
public class ActivityUtil {

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

    /**
     * ウイザード（多階層コンテンツ）画面に遷移する
     */
    public static void startChildContentListActivity(@NonNull final Context context, @NonNull final ContentsData contentsData) {
        Intent intent = new Intent(context, ChildContentListActivity.class);
        intent.putExtra(ChildContentListActivity.INTENT_KEY_CRID, contentsData.getCrid());
        intent.putExtra(ChildContentListActivity.INTENT_KEY_TITLE, contentsData.getTitle());
        intent.putExtra(ChildContentListActivity.INTENT_KEY_DISP_TYPE, contentsData.getDispType());
        context.startActivity(intent);
    }

}
