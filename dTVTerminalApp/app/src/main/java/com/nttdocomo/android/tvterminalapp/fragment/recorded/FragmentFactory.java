/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {

    private Map<Integer, RecordedBaseFrgament> mFragments = new HashMap<Integer, RecordedBaseFrgament>();

    public FragmentFactory() {

    }

    public synchronized RecordedBaseFrgament createFragment(int position, RecordedBaseFragmentScrollListener lis) {
        RecordedBaseFrgament fragment = null;
        fragment = mFragments.get(position);

        if (null == fragment) {
            fragment = new RecordedBaseFrgament();
            if (fragment != null) {
                mFragments.put(position, fragment);
//                fragment.setSearchBaseFragmentScrollListener(lis);
            }
        }

        return fragment;
    }

    public synchronized int getFragmentCount() {
        if (null != mFragments) {
            return mFragments.size();
        }

        return 0;
    }
}