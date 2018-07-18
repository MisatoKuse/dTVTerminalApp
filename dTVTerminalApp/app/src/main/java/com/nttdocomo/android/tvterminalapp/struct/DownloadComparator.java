/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * ソート処理.
 */
public class DownloadComparator implements Comparator<Object>, Serializable {
    private static final long serialVersionUID = -1L;
    /** 日付format(download). */
    private static final String DATEFORMAT_T = "T";
    /** 日付format(download). */
    private static final String DATEFORMAT_SPACE = " ";

    @Override
    public int compare(final Object s1, final Object s2) {
        String date1Txt = "";
        String date2Txt = "";
        if (s1 instanceof ContentsData) {
            date1Txt = ((ContentsData) s1).getPublishEndDate();
            date2Txt = ((ContentsData) s2).getPublishEndDate();
        } else if (s1 instanceof RecordedContentsDetailData) {
            date1Txt = ((RecordedContentsDetailData) s1).getDate();
            date2Txt = ((RecordedContentsDetailData) s2).getDate();
        }
        date1Txt = date1Txt.replace(DATEFORMAT_T, DATEFORMAT_SPACE);
        date2Txt = date2Txt.replace(DATEFORMAT_T, DATEFORMAT_SPACE);
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_PATTERN_HYPHEN, Locale.JAPAN);
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = format.parse(date1Txt);
            date2 = format.parse(date2Txt);
        } catch (ParseException e) {
            DTVTLogger.debug(e);
        }
        return date2.compareTo(date1);
    }
}
