/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

import java.util.Comparator;

/**
 * 番組表向け番組情報並べ替えクラス.
 */
public class ScheduleInfoComparator implements Comparator<ScheduleInfo> {

    @Override
    public int compare(final ScheduleInfo c1, final ScheduleInfo c2) {

        if (c1.getDrawOffset() == c2.getDrawOffset()) {
            return 0;
        } else if (c1.getDrawOffset() < c2.getDrawOffset()) {
            return -1;
        } else {
            return 1;
        }
    }
}
