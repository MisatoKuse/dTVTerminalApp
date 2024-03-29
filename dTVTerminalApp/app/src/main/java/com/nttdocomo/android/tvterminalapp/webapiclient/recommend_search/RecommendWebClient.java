/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChannelList;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendWebXmlParser;

import java.util.LinkedHashMap;

/**
 * レコメンド情報取得用Webクライアント.
 */
public class RecommendWebClient extends WebApiBase implements WebApiCallback {

    /**
     * コンテキスト.
     */
    private Context mContext = null;
    /**
     * 先頭スイッチ.
     */
    private boolean mFirstParameter = false;
    /**
     * コールバック.
     */
    private final RecommendCallback mRecommendCallback;

    // 汎用レコメンド情報取得API
    /**
     * リクエスト用・サービスID.
     */
    private static final String SERVICE_ID = "serviceId";
    /**
     * リクエスト用・カテゴリーID.
     * ※本パラメータ指定時は、"サービスID:カテゴリーID"のように、コロンで挟んで一組となるので注意。
     * 複数指定の場合はこの組をカンマで接続して指定する。
     */
    public static final String SERVICE_CATEGORY_ID = "serviceCategoryId";
    /**
     * リクエスト用・１ページの件数.
     */
    private static final String GET_PAGE = "getPage";
    /**
     * リクエスト用・開始位置.
     * ※maxResultの値を指定した際は、こちらは省略できないので注意
     */
    public static final String START_INDEX = "startIndex";
    /**
     * リクエスト用・最大件数.
     */
    public static final String MAX_RESULT = "maxResult";
    /**
     * リクエスト用・ページID.
     */
    public static final String PAGE_ID = "pageId";
    /**
     * リクエスト用・放送時間.
     */
    public static final String AIRTIME = "airtime";

    /**ホーム画面用最大件数（レコメンド情報の先読み分を含むので、100件に増加）.*/
    public static final String HOME_PAGE_MAX = "100&";

    /**ホーム画面用開始位置..*/
    public static final String HOME_PAGE_START = "1&";

    /**ページID. .*/ //TODO : 現在はダミーの値
    public static final String USE_PAGE_ID = "0";

    /**R指定コンテンツ許可フラグ*/
    public static final String ALLOW_R_RATED_CONTENTS = "allowRratedContents";

    /**R指定コンテンツ許可フラグ:視聴可能下限年齢がN歳以上のコンテンツの返却を許可*/
    public static final String ALLOW_R_RATED_CONTENTS_VALUE = "1";
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * おすすめタブページ番号.
     */
    private int mRequestPageNo;

    /**
     * レコメンド用コールバック.
     */
    public interface RecommendCallback {
        /**
         * コールバック.
         * @param recommendChannelList mRecommendChannelList
         */
        void recommendCallback(RecommendChannelList recommendChannelList);
    }

    /**
     * 処理終了後のコールバックの設定.
     *
     * @param mRecommendCallback コールバックの指定
     * @param context コンテキスト
     */
    public RecommendWebClient(final RecommendCallback mRecommendCallback, final Context context, final int requestPageNo) {
        this.mRecommendCallback = mRecommendCallback;
        this.mRequestPageNo = requestPageNo;
        //コンテキストの退避
        mContext = context;
    }

    /**
     * レコメンド情報取得.
     *
     * @param recommendRequestData レコメンド情報取得用パラメータ
     */
    public void getRecommendApi(final RecommendRequestData recommendRequestData) {

        DTVTLogger.debug("getRecommendApi");

        LinkedHashMap queryItems = new LinkedHashMap();

        //先頭パラメータスイッチをONにする
        mFirstParameter = true;

        //パラメータの追加
        itemAdder(queryItems, SERVICE_ID, recommendRequestData.getServiceId());
        itemAdder(queryItems, SERVICE_CATEGORY_ID, recommendRequestData.getServiceCategoryId());
        itemAdder(queryItems, GET_PAGE, recommendRequestData.getGetPage());
        itemAdder(queryItems, START_INDEX, recommendRequestData.getStartIndex());
        itemAdder(queryItems, MAX_RESULT, recommendRequestData.getMaxResult());
        itemAdder(queryItems, PAGE_ID, recommendRequestData.getPageId());
        itemAdder(queryItems, ALLOW_R_RATED_CONTENTS, ALLOW_R_RATED_CONTENTS_VALUE);

        if (!queryItems.isEmpty()) {
            //通信停止中ならば通信処理への遷移は行わない
            if (!mIsCancel) {
                //サーバーへおすすめ情報取得を依頼する
                //get(UrlConstants.WebApiUrl.RECOMMEND_LIST_GET_URL, queryItems, this, mContext);
                getRecomendInfo(UrlConstants.WebApiUrl.RECOMMEND_LIST_GET_URL, queryItems, this, mContext);
            } else {
                DTVTLogger.error("RecommendWebClient is stopping connection");
            }
        } else {
            //パラメータに誤りがあったので、ヌルを返却する
            if (mRecommendCallback != null) {
                mRecommendCallback.recommendCallback(null);
            }
        }
    }

    /**
     * 指定されたパラメータをマップに蓄積する.
     *
     * @param items     パラメータ蓄積マップ
     * @param keyname   キー名
     * @param parameter パラメータ
     */
    private void itemAdder(final LinkedHashMap items, final String keyname, final String parameter) {
        //引数にヌルがあれば何もしない
        if (items == null || keyname == null || parameter == null) {
            return;
        }

        //パラメータが空欄ならば何もしない
        if (parameter.isEmpty()) {
            return;
        }

        String destKeyname;
        if (mFirstParameter) {
            //先頭ならば前に？を付加
            destKeyname = StringUtils.getConnectStrings("?", keyname);

            //先頭0は終わるのでフラグを更新
            mFirstParameter = false;
        } else {
            //2番目以降ならば前に&を付加
            destKeyname = StringUtils.getConnectStrings("&", keyname);
        }

        //テーブルに蓄積
        items.put(destKeyname, parameter);
    }

    /**
     * 通信終了後に呼ばれるコールバック.
     *
     * @param responseData 通信レスポンス
     */
    @Override
    public void onFinish(final String responseData) {
        //得られたXMLのパースを行って、データを返す
        new RecommendWebXmlParser(mRecommendCallback, this.mRequestPageNo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, responseData);
    }

    /**
     * 通信を止める.
     */
    public void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        stopHTTPConnection();
    }

    /**
     * 通信可能状態にする.
     */
    public void enableConnection() {
        DTVTLogger.start();
        mIsCancel = false;
    }
}