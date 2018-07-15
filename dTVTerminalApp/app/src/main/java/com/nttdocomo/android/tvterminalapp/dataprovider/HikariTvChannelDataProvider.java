/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.util.List;
import java.util.Map;

/**
 * ひかりテレビデータプロバイダクラス.
 */
public class HikariTvChannelDataProvider extends ScaledDownProgramListDataProvider {
    /**
     * コンテンツデータコールバックインタフェース.
     */
    public interface ContentsDataCallback {
        /**
         * コンテンツデータ取得コールバック.
         * @param data data
         */
        void onContentDataGet(ContentsData data);
    }
    /**コンテキスト.*/
    private final Context mContext;
    /**番組表情報WebClient.*/
    private TvScheduleWebClient mTvScheduleWebClient = null;
    /**callback.*/
    private final ContentsDataCallback mContentsDataCallback;
    /**エラーステータス.*/
    private ErrorState mErrorState = null;
    /**
     * コンストラクタ.
     *
     * @param mContext TvProgramListActivity
     * @param callback  callback
     */
    public HikariTvChannelDataProvider(final Context mContext, final ContentsDataCallback callback) {
        super(mContext);
        this.mContext = mContext;
        mContentsDataCallback = callback;
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

    /**
     * NOW　ON AIR　番組情報を取得.
     * @param chno チャンネルナンバー
     */
    public void getNowOnAirProgram(final int chno) {
        mTvScheduleWebClient = new TvScheduleWebClient(mContext);
        mTvScheduleWebClient.getTvScheduleApi(new int[]{chno}, new String[]{WebApiBasePlala.DATE_NOW}, "", this);
    }

    @Override
    public void onTvScheduleJsonParsed(final List<TvScheduleList> tvScheduleList, int[] chNos) {
        if (tvScheduleList == null) { //networkエラー
            if (mTvScheduleWebClient != null) {
                mErrorState = mTvScheduleWebClient.getError();
            }
            DTVTLogger.error("1");
            return;
        }
        if (tvScheduleList.size() != 1) {
            DTVTLogger.error("2");
            // error
            return;
        }

        List<Map<String, String>> channelProgramList = tvScheduleList.get(0).geTvsList();
        if (channelProgramList == null || channelProgramList.size() != 1) {
            DTVTLogger.error("3");
            // error
            mContentsDataCallback.onContentDataGet(null);
            return;
        }
        DTVTLogger.debug("before callback");
        ContentsData dstData = DataConverter.generateContentData(channelProgramList.get(0));
        mContentsDataCallback.onContentDataGet(dstData);
    }

}
