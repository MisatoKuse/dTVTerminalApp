/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

/**
 * ソート処理(チャンネル番号).
 */
public class ChannelComparator implements Comparator<Object>, Serializable {
    private static final long serialVersionUID = -1L;
    /** データタイプ.*/
    private int mDataType;
    /** データタイプ(マップ).*/
    public static final int MAP_TYPE = 1;
    /** データタイプ(ContentData).*/
    public static final int OBJECT_TYPE = 2;

    /**
     * 構造関数.
     * @param mDataType データタイプ
     */
    public ChannelComparator(final int mDataType) {
        this.mDataType = mDataType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compare(final Object ob1, final Object ob2) {

        Integer result1 = 0;
        Integer result2 = 0;
        String chNo1 = null;
        String chNo2 = null;
        if (mDataType == MAP_TYPE) {
            Map<String, String> map1 = (Map<String, String>) ob1;
            Map<String, String> map2 = (Map<String, String>) ob2;
            chNo1 = map1.get(JsonConstants.META_RESPONSE_CHNO);
            chNo2 = map2.get(JsonConstants.META_RESPONSE_CHNO);
        } else if (mDataType == OBJECT_TYPE) {
            ContentsData contentsData1 = (ContentsData) ob1;
            ContentsData contentsData2 = (ContentsData) ob2;
            chNo1 = contentsData1.getChannelNo();
            chNo2 = contentsData2.getChannelNo();
        }
        try {
            result1 = Integer.parseInt(chNo1);
            result2 = Integer.parseInt(chNo2);
        } catch (NumberFormatException e) {
            DTVTLogger.debug(e);
        }
        return result1.compareTo(result2);
    }
}
