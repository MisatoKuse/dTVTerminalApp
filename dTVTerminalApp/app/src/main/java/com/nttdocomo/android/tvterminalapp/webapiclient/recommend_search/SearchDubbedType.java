/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import com.nttdocomo.android.tvterminalapp.struct.SearchNarrowCondition;

/**
 * 検索絞込み条件:「吹替」を指定する（複数指定可）.
 */
public class SearchDubbedType extends SearchFilterTypeMappable {
    /**2：吹替のみ.*/
    private final int mDubbed = SearchNarrowCondition.getNextOrdinal();
    /**1：字幕のみ.*/
    private final int mSubtitle = SearchNarrowCondition.getNextOrdinal();
    /**3：字幕/吹替両対応.*/
    private final int mBoth = SearchNarrowCondition.getNextOrdinal();
    /**検索条件初期値.*/
    private int mSelf = SearchNarrowCondition.sEnumOrdinalNil;

    /**
     *コンストラクタ.
     * @param name 絞込みの指定
     */
    public SearchDubbedType(final String name) {
        if ("SearchDubbedTypeDubbed".equals(name)) {
            mSelf = mDubbed;
        } else if ("SearchDubbedTypeSubtitle".equals(name)) {
            mSelf = mSubtitle;
        } else if ("SearchDubbedTypeBoth".equals(name)) {
            mSelf = mBoth;
        }
    }

    @Override
    public SearchFilterType searchFilterType() {
        if (mDubbed == mSelf) {
            return SearchFilterType.dubbedDubbed;
        } else if (mSubtitle == mSelf) {
            return SearchFilterType.dubbedText;
        } else if (mBoth == mSelf) {
            return SearchFilterType.dubbedTextAndDubbing;
        }

        return null;
    }
}
