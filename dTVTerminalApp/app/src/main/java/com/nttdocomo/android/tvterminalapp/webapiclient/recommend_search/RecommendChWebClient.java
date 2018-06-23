/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChannelList;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

import java.util.LinkedHashMap;

/**
 * おすすめ番組ウェブクライアント.
 */
public class RecommendChWebClient extends WebApiBase implements WebApiCallback {
    /**
     * 種別・チャンネル.
     */
    //ホーム画面におすすめ番組を表示する為のカテゴリー（仮の値から実際の値に昇格）
    private static final String CHANNEL_CATEGORY = "43:01,44:03,44:04&";
    /**callback.*/
    private final RecommendChannelCallback mRecommendChannelCallback;

    /**SSLチェック用コンテキスト.*/
    private final Context mContext;

    /**
     * コールバック.
     */
    public interface RecommendChannelCallback {
        /**
         * コールバック.
         * @param mRecommendChannelList  おすすめ番組データ
         */
        void onRecommendChannelCallback(RecommendChannelList mRecommendChannelList);
    }

    /**
     * コンストラクタ.
     * @param mRecommendChannelCallback callback
     * @param context コンテキスト
     */
    public RecommendChWebClient(final RecommendChannelCallback mRecommendChannelCallback,
                                final Context context) {
        this.mRecommendChannelCallback = mRecommendChannelCallback;

        //コンテキストの退避
        mContext = context;
    }

    /**
     * おすすめ番組Api取得.
     */
    public void getRecommendChannelApi() {

        DTVTLogger.debug("getRecommendChannelApi");
        LinkedHashMap queryItems = new LinkedHashMap();

        //種別パラメータにテレビチャンネルを指定
        queryItems.put(RecommendWebClient.SERVICE_CATEGORY_ID, CHANNEL_CATEGORY);

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

    /**
     * 通信終了後に呼ばれるコールバック.
     *
     * @param responseData responseData
     */
    @Override
    public void onFinish(final String responseData) {
        //得られたXMLのパースを行って、データを返す
        new RecommendChannelXmlParser(mRecommendChannelCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, responseData);
    }
}
