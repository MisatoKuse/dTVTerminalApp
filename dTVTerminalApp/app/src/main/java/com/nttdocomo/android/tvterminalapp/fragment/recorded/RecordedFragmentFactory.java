/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.fragment.recorded;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * 録画フラグメントファクトリー.
 */
public class RecordedFragmentFactory {
    /** Fragment格納Map..*/
    private final Map<Integer, RecordedBaseFragment> mFragments = new HashMap<>();
    /**
     * コンストラクタ.
     */
    public RecordedFragmentFactory() {

    }

    /**
     * フラグメントクラスの生成、取得.
     * @param position position
     * @return fragment
     */
    public synchronized RecordedBaseFragment createFragment(final int position) {
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