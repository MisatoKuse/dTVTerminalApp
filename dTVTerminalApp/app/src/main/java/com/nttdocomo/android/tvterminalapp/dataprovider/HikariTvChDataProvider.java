/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchResponseData;

import java.util.List;
import java.util.Map;

/**
 * ひかりテレビデータプロバイダクラス.
 */
public class HikariTvChDataProvider extends ScaledDownProgramListDataProvider {
    public interface ContentsDataCallback {
        void onContentDataGet(ContentsData data);
    }
    private Context mContext;
    private TvScheduleWebClient mTvScheduleWebClient;
    private ContentsDataCallback mContentsDataCallback;
    private ErrorState mErrorState;
    /**
     * コンストラクタ.
     *
     * @param mContext TvProgramListActivity
     */
    public HikariTvChDataProvider(final Context mContext, ContentsDataCallback callback) {
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

    public void getNowOnAirProgram(final int chno) {
        mTvScheduleWebClient = new TvScheduleWebClient(mContext);
        mTvScheduleWebClient.getTvScheduleApi(new int[]{chno}, new String[]{WebApiBasePlala.DATE_NOW}, "", this);
    }

    @Override
    public void onTvScheduleJsonParsed(List<TvScheduleList> tvScheduleList) {
        if (tvScheduleList == null) { //networkエラー
            mErrorState = mTvScheduleWebClient.getError();
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
            return;
        }
        DTVTLogger.debug("before callback");
        ContentsData dstData = DataConverter.generateContentData(channelProgramList.get(0));
        mContentsDataCallback.onContentDataGet(dstData);
    }

}
