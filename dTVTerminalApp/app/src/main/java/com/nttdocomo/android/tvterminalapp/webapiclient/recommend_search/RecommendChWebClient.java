/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

import java.util.LinkedHashMap;

public class RecommendChWebClient extends WebApiBase implements WebApiCallback {
    /**
     * 種別・チャンネル
     */
    private static final String CHANNEL_CATEGORY = "43:01,44:03,44:04&";

    private RecommendChannelCallback mRecommendChannelCallback;

    public interface RecommendChannelCallback {
        void RecommendChannelCallback(RecommendChList mRecommendChList);
    }

    public RecommendChWebClient(RecommendChannelCallback mRecommendChannelCallback) {
        this.mRecommendChannelCallback = mRecommendChannelCallback;
    }

    public void getRecommendChannelApi() {

        DTVTLogger.debug("getRecommendChannelApi");
        LinkedHashMap queryItems = new LinkedHashMap();

        //種別パラメータにテレビチャンネルを指定
        queryItems.put(RecommendWebClient.SERVICE_CATEGORY_ID, CHANNEL_CATEGORY);

        //開始位置
        queryItems.put(RecommendWebClient.START_INDEX, RecommendWebClient.HOME_PAGE_START);

        //最大件数
        queryItems.put(RecommendWebClient.MAX_RESULT, RecommendWebClient.HOME_PAGE_MAX);

        //TODO: ページIDは払い出されていないのでダミー値
        queryItems.put(RecommendWebClient.PAGE_ID, RecommendWebClient.USE_PAGE_ID);

        //パラメータがあるならば、URLの後ろに"?"をつける
        String sendUrlAdder;
        if (queryItems.size() > 0) {
            sendUrlAdder = "?";
        } else {
            sendUrlAdder = "";
        }

        //サーバーへおすすめ情報取得を依頼する
        get(UrlConstants.WebApiUrl.RECOMMEND_LIST_GET_URL + sendUrlAdder, queryItems, this);

    }

    /**
     * 通信終了後に呼ばれるコールバック
     *
     * @param responseData
     */
    @Override
    public void onFinish(String responseData) {
        //得られたXMLのパースを行って、データを返す
        new RecommendChannelXmlParser(mRecommendChannelCallback).execute(responseData);
    }
}
