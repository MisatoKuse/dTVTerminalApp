/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendVdList;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser;

import java.util.LinkedHashMap;

public class RecommendVdWebClient extends WebApiBase implements WebApiCallback {

    /**
     * 種別・ビデオ
     */
    private static final String VIDEO_CATEGORY =
            //TODO: ホーム画面におすすめビデオを表示する為の仮のカテゴリー
            "15:01,15:02,17:01,43:02,43:03,44:05,44:06,44:08,44:10&";
    //TODO: 正当なカテゴリー値 サーバー側の準備が整った場合はこちらに変更する
    //"44:06,44:08,44:10&";

    private RecommendVideoCallback mRecommendVideoCallback;

    public interface RecommendVideoCallback {
        void RecommendVideoCallback(RecommendVdList mRecommendVdList);
    }

    public RecommendVdWebClient(RecommendVideoCallback mRecommendVideoCallback) {
        this.mRecommendVideoCallback = mRecommendVideoCallback;
    }

    public void getRecommendVideoApi() {

        DTVTLogger.debug("getRecommendVideoApi");
        LinkedHashMap queryItems = new LinkedHashMap();

        //種別パラメータにテレビチャンネルを指定
        queryItems.put(RecommendWebClient.SERVICE_CATEGORY_ID, VIDEO_CATEGORY);

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
        getReccomendInfo(UrlConstants.WebApiUrl.RECOMMEND_LIST_GET_URL + sendUrlAdder, queryItems, this);
    }

    @Override
    public void onFinish(String responseData) {
        //得られたXMLのパースを行って、データを返す
        new RecommendVideoXmlParser(mRecommendVideoCallback).execute(responseData);
    }
}