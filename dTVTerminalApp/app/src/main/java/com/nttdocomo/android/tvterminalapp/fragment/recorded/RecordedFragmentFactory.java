/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.HashMap;
import java.util.Map;

public class RecordedFragmentFactory {

    private Map<Integer, RecordedBaseFragment> mFragments = new HashMap<Integer, RecordedBaseFragment>();

    public RecordedFragmentFactory() {

    }

    public synchronized RecordedBaseFragment createFragment(int position, RecordedBaseFragmentScrollListener lis) {
        DTVTLogger.start();
        RecordedBaseFragment fragment = null;
        fragment = mFragments.get(position);

        if (null == fragment) {
            fragment = new RecordedBaseFragment();
            mFragments.put(position, fragment);
        }

        return fragment;
    }

    public synchronized int getFragmentCount() {
        DTVTLogger.start();
        if (null != mFragments) {
            return mFragments.size();
        }

        return 0;
    }
}