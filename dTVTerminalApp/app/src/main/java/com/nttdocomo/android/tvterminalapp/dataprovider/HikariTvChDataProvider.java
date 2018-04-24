/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;

/**
 * ひかりテレビデータプロバイダクラス.
 */
public class HikariTvChDataProvider extends ScaledDownProgramListDataProvider {
    /**
     * コンストラクタ.
     *
     * @param mContext TvProgramListActivity
     */
    public HikariTvChDataProvider(final Context mContext) {
        super(mContext);
    }

    /**
     * CH一覧取得.
     *
     * @param limit  レスポンスの最大件数
     * @param offset 取得位置(1～)
     * @param filter release、testa、demo ※指定なしの場合release
     */
    public void getChannelList(final int limit, final int offset, final String filter) {
        super.getChannelList(limit, offset, filter, JsonConstants.CH_SERVICE_TYPE_INDEX_HIKARI);
    }
}
