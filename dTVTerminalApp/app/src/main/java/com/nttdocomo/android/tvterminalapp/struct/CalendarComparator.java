/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * ソート処理
 */
public class CalendarComparator implements Comparator<ScheduleInfo>, Serializable {
    private static final long serialVersionUID = -1L;

    @Override
    public int compare(ScheduleInfo s1, ScheduleInfo s2) {
        StringBuilder time1 = new StringBuilder();
        time1.append(s1.getStartTime().substring(0, 10));
        time1.append(s1.getStartTime().substring(11, 19));
        StringBuilder time2 = new StringBuilder();
        time2.append(s2.getStartTime().substring(0, 10));
        time2.append(s2.getStartTime().substring(11, 19));
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DDHHMMSS, Locale.JAPAN);
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = format.parse(time1.toString());
            date2 = format.parse(time2.toString());
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        return date1.compareTo(date2);
    }
}
