/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.HashMap;
import java.util.Map;

public class RecordedFragmentFactory {

    private final Map<Integer, RecordedBaseFragment> mFragments = new HashMap<>();

    public RecordedFragmentFactory() {

    }

    public synchronized RecordedBaseFragment createFragment(final int position, final RecordedBaseFragmentScrollListener lis) {
        DTVTLogger.start();
        RecordedBaseFragment fragment;
        fragment = mFragments.get(position);

        if (null == fragment) {
            fragment = new RecordedBaseFragment();
            mFragments.put(position, fragment);
        }

        return fragment;
    }

}