/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendVideoList;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser;

import java.util.LinkedHashMap;

/**
 * おすすめビデオWebClient.
 */
public class RecommendVdWebClient extends WebApiBase implements WebApiCallback {

    /**
     * 種別・ビデオ.
     */
    private static final String VIDEO_CATEGORY =
            //ホーム画面におすすめビデオを表示する為のカテゴリー（仮の値から実際の値に昇格）
            "15:01,15:02,17:01,43:02,43:03,44:05,44:06,44:08,44:10&";
    /**RecommendVideoCallback.*/
    private RecommendVideoCallback mRecommendVideoCallback;

    /**SSLチェック用コンテキスト.*/
    private Context mContext;

    /**
     * おすすめビデオ一覧用callback.
     */
    public interface RecommendVideoCallback {
        /**
         * RecommendVideoCallback.
         * @param mRecommendVideoList おすすめビデオ一覧リスト
         */
        void onRecommendVideoCallback(RecommendVideoList mRecommendVideoList);
    }

    /**
     * コンストラクタ.
     * @param mRecommendVideoCallback コールバック.
     * @param context コンテキスト.
     */
    public RecommendVdWebClient(final RecommendVideoCallback mRecommendVideoCallback, final Context context) {
        this.mRecommendVideoCallback = mRecommendVideoCallback;

        //コンテキストの退避
        mContext = context;
    }

    /**
     * おすすめビデオApi取得.
     */
    public void getRecommendVideoApi() {

        DTVTLogger.debug("getRecommendVideoApi");
        LinkedHashMap queryItems = new LinkedHashMap();

        //種別パラメータにテレビチャンネルを指定
        queryItems.put(RecommendWebClient.SERVICE_CATEGORY_ID, VIDEO_CATEGORY);

        //開始位置
        queryItems.put(RecommendWebClient.START_INDEX, RecommendWebClient.HOME_PAGE_START);

        //最大件数(おすすめ画面用の先読みもここで行うので、件数を100件に変更)
        queryItems.put(RecommendWebClient.MAX_RESULT, RecommendWebClient.HOME_PAGE_MAX);

        //TODO : ページIDは払い出されていないのでダミー値
        queryItems.put(RecommendWebClient.PAGE_ID, RecommendWebClient.USE_PAGE_ID);

        //パラメータがあるならば、URLの後ろに"?"をつける
        String sendUrlAdder;
        if (queryItems.size() > 0) {
            sendUrlAdder = "?";
        } else {
            sendUrlAdder = "";
        }

        //サーバーへおすすめ情報取得を依頼する
        getRecomendInfo(UrlConstants.WebApiUrl.RECOMMEND_LIST_GET_URL + sendUrlAdder,
                queryItems, this, mContext);
    }

    @Override
    public void onFinish(final String responseData) {
        //得られたXMLのパースを行って、データを返す
        new RecommendVideoXmlParser(mRecommendVideoCallback).execute(responseData);
    }
}