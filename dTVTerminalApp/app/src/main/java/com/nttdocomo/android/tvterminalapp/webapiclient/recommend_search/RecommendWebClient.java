/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.content.Context;
import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendWebXmlParser;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.LinkedHashMap;

public class RecommendWebClient extends WebApiBase implements WebApiCallback,
        OneTimePasswordAuth.OneTimePasswordAuthCallback {

    //コンテキスト
    private Context mContext = null;

    //先頭スイッチ
    private boolean mfirstParmater = false;

    private final RecommendCallback mRecommendCallback;

    //コールバックにエラーを返すためのハンドラー
    private Handler handler;
    private Runnable runnable;

    // 汎用レコメンド情報取得API
    /**
     * リクエスト用・サービスID.
     */
    public static final String SERVICE_ID = "serviceId";
    /**
     * リクエスト用・カテゴリーID.
     * ※本パラメータ指定時は、"サービスID:カテゴリーID"のように、コロンで挟んで一組となるので注意。
     * 複数指定の場合はこの組をカンマで接続して指定する。
     */
    public static final String SERVICE_CATEGORY_ID = "serviceCategoryId";
    /**
     * リクエスト用・１ページの件数.
     */
    public static final String GET_PAGE = "getPage";
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

    //ホーム画面用最大件数.
    public static final String HOME_PAGE_MAX = "10&";

    //ホーム画面用開始位置.
    public static final String HOME_PAGE_START = "1&";

    //ページID. TODO: 現在はダミーの値
    public static final String USE_PAGE_ID = "0";

    @Override
    public void oneTimePasswordAuthCallback(String url, LinkedHashMap queryItems,
                                            boolean useOnetimePass) {
        //ワンタイムパスワードの認証の有無で動作を分ける
        if (useOnetimePass) {
            //ワンタイムパスワードの処理用にクッキーを有効化する
            // CookieManager の生成
            CookieManager manager = new CookieManager();

            // すべての Cookie を受けいれる
            manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            // CookieHandler のデフォルトに設定
            CookieHandler.setDefault(manager);
        }

        //ヌルの場合は初期化を行う
        if(queryItems == null) {
            queryItems = new LinkedHashMap();
        }

        //返ってきたURLとパラメータで通信を行う
        getReccomendInfo(url, queryItems, this);
    }

    public interface RecommendCallback {
        void RecommendCallback(RecommendChList mRecommendChList);
    }

    /**
     * 処理終了後のコールバックの設定.
     *
     * @param mRecommendCallback コールバックの指定
     */
    public RecommendWebClient(RecommendCallback mRecommendCallback, Context context) {
        this.mRecommendCallback = mRecommendCallback;

        //コンテキストの退避
        mContext = context;
    }

    /**
     * レコメンド情報取得.
     *
     * @param recommendRequestData レコメンド情報取得用パラメータ
     */
    public void getRecommendApi(RecommendRequestData recommendRequestData) {

        DTVTLogger.debug("getRecommendApi");

        LinkedHashMap queryItems = new LinkedHashMap();

        //先頭パラメータスイッチをONにする
        mfirstParmater = true;

        //パラメータの追加
        itemAdder(queryItems, SERVICE_ID, recommendRequestData.serviceId);
        itemAdder(queryItems, SERVICE_CATEGORY_ID, recommendRequestData.serviceCategoryId);
        itemAdder(queryItems, GET_PAGE, recommendRequestData.getPage);
        itemAdder(queryItems, START_INDEX, recommendRequestData.startIndex);
        itemAdder(queryItems, MAX_RESULT, recommendRequestData.maxResult);
        itemAdder(queryItems, PAGE_ID, recommendRequestData.pageId);

        if (!queryItems.isEmpty()) {
            //サーバーへおすすめ情報取得を依頼する
            //get(UrlConstants.WebApiUrl.RECOMMEND_LIST_GET_URL, queryItems, this);
            OneTimePasswordAuth oneTimePasswordAuth = new OneTimePasswordAuth(this);
            oneTimePasswordAuth.authOneTimePasswordStart(mContext,
                    UrlConstants.WebApiUrl.RECOMMEND_LIST_GET_URL, queryItems);

        } else {
            //パラメータに誤りがあったので、ヌルを返却する
            if (mRecommendCallback != null) {
                //コールバック処理の定義
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        //ヌルをコールバックに返す
                        mRecommendCallback.RecommendCallback(null);

                        //後始末
                        // **FindBugs** Bad pratice handler初期化されていないとfindbugは警告するが、
                        // handlerに値が入っていなければここに来ることはありえないので、対処は行わない
                        handler.removeCallbacks(runnable);
                        runnable = null;
                        handler = null;
                    }
                };

                //コールバック処理を呼び出す
                handler = new Handler();
                handler.post(runnable);
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
    private void itemAdder(LinkedHashMap items, String keyname, String parameter) {
        //引数にヌルがあれば何もしない
        if (items == null || keyname == null || parameter == null) {
            return;
        }

        //パラメータが空欄ならば何もしない
        if (parameter.isEmpty()) {
            return;
        }

        if (mfirstParmater) {
            //先頭ならば前に？を付加
            keyname = "?" + keyname;

            //先頭0は終わるのでフラグを更新
            mfirstParmater = false;
        } else {
            //2番目以降ならば前に&を付加
            keyname = "&" + keyname;
        }

        //テーブルに蓄積
        items.put(keyname, parameter);
    }

    /**
     * 通信終了後に呼ばれるコールバック.
     *
     * @param responseData 通信レスポンス
     */
    @Override
    public void onFinish(String responseData) {
        //得られたXMLのパースを行って、データを返す
        new RecommendWebXmlParser(mRecommendCallback).execute(responseData);
    }
}