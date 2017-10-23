/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

import java.util.LinkedHashMap;

public class RecommendWebClient extends WebApiBase implements WebApiCallback {
    // 汎用レコメンド情報取得API
    /**
     * リクエスト用・サービスID
     */
    public static final String SERVICE_ID = "serviceId";
    /**
     * リクエスト用・カテゴリーID:※本パラメータ指定時は、"サービスID:カテゴリーID"のように、
     * コロンで挟んで一組となるので注意。複数指定の場合はこの組をカンマで接続して指定する。
     */
    public static final String SERVICE_CATEGORY_ID = "serviceCategoryId";
    /**
     * リクエスト用・１ページの件数
     */
    public static final String GET_PAGE = "getPage";
    /**
     * リクエスト用・開始位置：※maxResultの値を指定した際は、こちらは省略できないので注意
     */
    public static final String START_INDEX = "startIndex";
    /**
     * リクエスト用・最大件数
     */
    public static final String MAX_RESULT = "maxResult";
    /**
     * リクエスト用・ページID
     */
    public static final String PAGE_ID = "pageId";
    /**
     * リクエスト用・放送時間
     */
    public static final String AIRTIME = "airtime";

    //ホーム画面用最大件数
    public static final String HOME_PAGE_MAX = "10&";

    //ホーム画面用開始位置
    public static final String HOME_PAGE_START = "1&";

    //ページID TODO: 現在はダミーの値
    public static final String USE_PAGE_ID = "0";


    //先頭スイッチ
    private boolean mfirstParmater;

 
    private final RecommendCallback mRecommendCallback;

    //コールバックにエラーを返すためのハンドラー
    private Handler handler;
    private Runnable runnable;

    public interface RecommendCallback {
        void RecommendCallback(RecommendChList mRecommendChList);
    }

    public RecommendWebClient(RecommendCallback mRecommendCallback){
        this.mRecommendCallback = mRecommendCallback;
    }

    public void getRecommendApi(RecommendRequestData recommendRequestData) {

        DTVTLogger.debug("getRecommendApi");

        LinkedHashMap queryItems = new LinkedHashMap();

        //先頭パラメータスイッチをONにする
        mfirstParmater = true;

        //パラメータの追加
        itemAdder(queryItems,SERVICE_ID,recommendRequestData.serviceId);
        itemAdder(queryItems,SERVICE_CATEGORY_ID,recommendRequestData.serviceCategoryId);
        itemAdder(queryItems,GET_PAGE,recommendRequestData.getPage);
        itemAdder(queryItems,START_INDEX,recommendRequestData.startIndex);
        itemAdder(queryItems,MAX_RESULT,recommendRequestData.maxResult);
        itemAdder(queryItems,PAGE_ID,recommendRequestData.pageId);

        if(!queryItems.isEmpty()) {
            //サーバーへおすすめ情報取得を依頼する
            get(UrlConstants.WebApiUrl.RECOMMEND_LIST_GET_URL, queryItems, this);
        } else {
            //パラメータに誤りがあったので、ヌルを返却する
            if(mRecommendCallback != null) {
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
     * パラメータ追加
     * @param items     パラメータ蓄積マップ
     * @param keyname   キー名
     * @param parameter パラメータ
     */
    private void itemAdder(LinkedHashMap items,String keyname,String parameter) {
        //引数にヌルがあれば何もしない
        if(items == null || keyname == null || parameter == null) {
            return;
        }

        //パラメータが空欄ならば何もしない
        if(parameter.isEmpty()) {
            return;
        }

        if(mfirstParmater) {
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
     * 通信終了後に呼ばれるコールバック
     * @param responseData 通信レスポンス
     */
    @Override
    public void onFinish(String responseData) {
        //得られたXMLのパースを行う
        RecommendChannelXmlParser recommendChannelXmlParser = new RecommendChannelXmlParser();
        RecommendChList mRecommendChList =
                recommendChannelXmlParser.getRecommendchannelList(responseData);

        //TODO:テストサーバーが値を返さなくなった際の、ダミーデータを設定する・ダミーデータ削除時に消すこと
//        mRecommendChList =
//                recommendChannelXmlParser.getRecommendchannelList(DUMMY_DATA);

        if(mRecommendCallback != null) {
            mRecommendCallback.RecommendCallback(mRecommendChList);
        }
    }

    //TODO: サーバーが動作していたころのデータ・最終的には必ず消すこと
    private static final String DUMMY_DATA =
            "<Object>\n" +
            "<Result>0</Result>\n" +
            "<PageNum>1</PageNum>\n" +
            "<MaxPageNum>10</MaxPageNum>\n" +
            "<RecommendContentsList>\n" +
            "<RecommendContent>\n" +
            "<recommendOrder>1</recommendOrder>\n" +
            "<serviceId>15</serviceId>\n" +
            "<categoryId>01</categoryId>\n" +
            "<channelId/>\n" +
            "<contentsId>10000181</contentsId>\n" +
            "<title>オンナの噂研究所</title>\n" +
            "<ctPicURL1>\n" +
            "https://image5-a.beetv.jp/basic/img/title/10000181_top_hd_org.jpg\n" +
            "</ctPicURL1>\n" +
            "<ctPicURL2/>\n" +
            "<startViewing>20000101000000</startViewing>\n" +
            "<endViewing>20171020230000</endViewing>\n" +
            "<reserved1/>\n" +
            "<reserved2/>\n" +
            "<reserved3/>\n" +
            "<reserved4/>\n" +
            "<reserved5/>\n" +
            "<agreement>0</agreement>\n" +
            "<viewable>1</viewable>\n" +
            "<pageId>0</pageId>\n" +
            "<groupId>0</groupId>\n" +
            "<recommendMethodId>99</recommendMethodId>\n" +
            "</RecommendContent>\n" +
            "<RecommendContent>\n" +
            "<recommendOrder>2</recommendOrder>\n" +
            "<serviceId>15</serviceId>\n" +
            "<categoryId>02</categoryId>\n" +
            "<channelId/>\n" +
            "<contentsId>10004166</contentsId>\n" +
            "<title>セックス・アンド・ザ・シティ2</title>\n" +
            "<ctPicURL1>\n" +
            "https://image5-a.beetv.jp/basic/img/title/10004166_top_hd_org.jpg\n" +
            "</ctPicURL1>\n" +
            "<ctPicURL2/>\n" +
            "<startViewing>20000101000000</startViewing>\n" +
            "<endViewing>21000101000000</endViewing>\n" +
            "<reserved1/>\n" +
            "<reserved2/>\n" +
            "<reserved3/>\n" +
            "<reserved4/>\n" +
            "<reserved5/>\n" +
            "<agreement>0</agreement>\n" +
            "<viewable>1</viewable>\n" +
            "<pageId>0</pageId>\n" +
            "<groupId>0</groupId>\n" +
            "<recommendMethodId>99</recommendMethodId>\n" +
            "</RecommendContent>\n" +
            "<RecommendContent>\n" +
            "<recommendOrder>3</recommendOrder>\n" +
            "<serviceId>17</serviceId>\n" +
            "<categoryId>01</categoryId>\n" +
            "<channelId/>\n" +
            "<contentsId>10001</contentsId>\n" +
            "<title>とある魔術の禁書目録</title>\n" +
            "<ctPicURL1>\n" +
            "https://cs1.anime.dmkt-sp.jp/anime_kv/img/10/00/1/10001_1_d.png?1427216400000\n" +
            "</ctPicURL1>\n" +
            "<ctPicURL2/>\n" +
            "<startViewing>20120627000000</startViewing>\n" +
            "<endViewing>99991231235959</endViewing>\n" +
            "<reserved1/>\n" +
            "<reserved2/>\n" +
            "<reserved3/>\n" +
            "<reserved4/>\n" +
            "<reserved5/>\n" +
            "<agreement>0</agreement>\n" +
            "<viewable>1</viewable>\n" +
            "<pageId>0</pageId>\n" +
            "<groupId>0</groupId>\n" +
            "<recommendMethodId>99</recommendMethodId>\n" +
            "</RecommendContent>\n" +
            "<RecommendContent>\n" +
            "<recommendOrder>4</recommendOrder>\n" +
            "<serviceId>15</serviceId>\n" +
            "<categoryId>01</categoryId>\n" +
            "<channelId/>\n" +
            "<contentsId>10000180</contentsId>\n" +
            "<title>BE-BOP HIGHSCHOOL</title>\n" +
            "<ctPicURL1>\n" +
            "https://image5-a.beetv.jp/basic/img/title/10000180_top_hd_org.jpg\n" +
            "</ctPicURL1>\n" +
            "<ctPicURL2/>\n" +
            "<startViewing>20000101000000</startViewing>\n" +
            "<endViewing>21000101000000</endViewing>\n" +
            "<reserved1/>\n" +
            "<reserved2/>\n" +
            "<reserved3/>\n" +
            "<reserved4/>\n" +
            "<reserved5/>\n" +
            "<agreement>0</agreement>\n" +
            "<viewable>1</viewable>\n" +
            "<pageId>0</pageId>\n" +
            "<groupId>0</groupId>\n" +
            "<recommendMethodId>99</recommendMethodId>\n" +
            "</RecommendContent>\n" +
            "<RecommendContent>\n" +
            "<recommendOrder>5</recommendOrder>\n" +
            "<serviceId>15</serviceId>\n" +
            "<categoryId>02</categoryId>\n" +
            "<channelId/>\n" +
            "<contentsId>10001454</contentsId>\n" +
            "<title>トワイライト～初恋～</title>\n" +
            "<ctPicURL1>\n" +
            "https://image5-a.beetv.jp/basic/img/title/10001454_top_hd_org.jpg\n" +
            "</ctPicURL1>\n" +
            "<ctPicURL2/>\n" +
            "<startViewing>20000101000000</startViewing>\n" +
            "<endViewing>21000101000000</endViewing>\n" +
            "<reserved1/>\n" +
            "<reserved2/>\n" +
            "<reserved3/>\n" +
            "<reserved4/>\n" +
            "<reserved5/>\n" +
            "<agreement>0</agreement>\n" +
            "<viewable>1</viewable>\n" +
            "<pageId>0</pageId>\n" +
            "<groupId>0</groupId>\n" +
            "<recommendMethodId>99</recommendMethodId>\n" +
            "</RecommendContent>\n" +
            "<RecommendContent>\n" +
            "<recommendOrder>6</recommendOrder>\n" +
            "<serviceId>17</serviceId>\n" +
            "<categoryId>01</categoryId>\n" +
            "<channelId/>\n" +
            "<contentsId>10002</contentsId>\n" +
            "<title>キノの旅-the Beautiful World-</title>\n" +
            "<ctPicURL1>\n" +
            "https://cs1.anime.dmkt-sp.jp/anime_kv/img/10/00/2/10002_1_d.png?1427216400000\n" +
            "</ctPicURL1>\n" +
            "<ctPicURL2/>\n" +
            "<startViewing>20120627000000</startViewing>\n" +
            "<endViewing>99991231235959</endViewing>\n" +
            "<reserved1/>\n" +
            "<reserved2/>\n" +
            "<reserved3/>\n" +
            "<reserved4/>\n" +
            "<reserved5/>\n" +
            "<agreement>0</agreement>\n" +
            "<viewable>1</viewable>\n" +
            "<pageId>0</pageId>\n" +
            "<groupId>0</groupId>\n" +
            "<recommendMethodId>99</recommendMethodId>\n" +
            "</RecommendContent>\n" +
            "<RecommendContent>\n" +
            "<recommendOrder>7</recommendOrder>\n" +
            "<serviceId>15</serviceId>\n" +
            "<categoryId>01</categoryId>\n" +
            "<channelId/>\n" +
            "<contentsId>10000178</contentsId>\n" +
            "<title>トゥルルさまぁ～ず</title>\n" +
            "<ctPicURL1>\n" +
            "https://image5-a.beetv.jp/basic/img/title/10000178_top_hd_org.jpg\n" +
            "</ctPicURL1>\n" +
            "<ctPicURL2/>\n" +
            "<startViewing>20000101000000</startViewing>\n" +
            "<endViewing>21000101000000</endViewing>\n" +
            "<reserved1/>\n" +
            "<reserved2/>\n" +
            "<reserved3/>\n" +
            "<reserved4/>\n" +
            "<reserved5/>\n" +
            "<agreement>0</agreement>\n" +
            "<viewable>1</viewable>\n" +
            "<pageId>0</pageId>\n" +
            "<groupId>0</groupId>\n" +
            "<recommendMethodId>99</recommendMethodId>\n" +
            "</RecommendContent>\n" +
            "<RecommendContent>\n" +
            "<recommendOrder>8</recommendOrder>\n" +
            "<serviceId>15</serviceId>\n" +
            "<categoryId>02</categoryId>\n" +
            "<channelId/>\n" +
            "<contentsId>10004195</contentsId>\n" +
            "<title>プラダを着た悪魔</title>\n" +
            "<ctPicURL1>\n" +
            "https://image5-a.beetv.jp/basic/img/title/10004195_top_hd_org.jpg\n" +
            "</ctPicURL1>\n" +
            "<ctPicURL2/>\n" +
            "<startViewing>20000101000000</startViewing>\n" +
            "<endViewing>21000101000000</endViewing>\n" +
            "<reserved1/>\n" +
            "<reserved2/>\n" +
            "<reserved3/>\n" +
            "<reserved4/>\n" +
            "<reserved5/>\n" +
            "<agreement>0</agreement>\n" +
            "<viewable>1</viewable>\n" +
            "<pageId>0</pageId>\n" +
            "<groupId>0</groupId>\n" +
            "<recommendMethodId>99</recommendMethodId>\n" +
            "</RecommendContent>\n" +
            "<RecommendContent>\n" +
            "<recommendOrder>9</recommendOrder>\n" +
            "<serviceId>17</serviceId>\n" +
            "<categoryId>01</categoryId>\n" +
            "<channelId/>\n" +
            "<contentsId>10003</contentsId>\n" +
            "<title>狼と香辛料</title>\n" +
            "<ctPicURL1>\n" +
            "https://cs1.anime.dmkt-sp.jp/anime_kv/img/10/00/3/10003_1_d.png?1427216400000\n" +
            "</ctPicURL1>\n" +
            "<ctPicURL2/>\n" +
            "<startViewing>20120627000000</startViewing>\n" +
            "<endViewing>99991231235959</endViewing>\n" +
            "<reserved1/>\n" +
            "<reserved2/>\n" +
            "<reserved3/>\n" +
            "<reserved4/>\n" +
            "<reserved5/>\n" +
            "<agreement>0</agreement>\n" +
            "<viewable>1</viewable>\n" +
            "<pageId>0</pageId>\n" +
            "<groupId>0</groupId>\n" +
            "<recommendMethodId>99</recommendMethodId>\n" +
            "</RecommendContent>\n" +
            "<RecommendContent>\n" +
            "<recommendOrder>10</recommendOrder>\n" +
            "<serviceId>15</serviceId>\n" +
            "<categoryId>01</categoryId>\n" +
            "<channelId/>\n" +
            "<contentsId>10000184</contentsId>\n" +
            "<title>今日、恋をはじめます</title>\n" +
            "<ctPicURL1>\n" +
            "https://image5-a.beetv.jp/basic/img/title/10000184_top_hd_org.jpg\n" +
            "</ctPicURL1>\n" +
            "<ctPicURL2/>\n" +
            "<startViewing>20000101000000</startViewing>\n" +
            "<endViewing>20171020230000</endViewing>\n" +
            "<reserved1/>\n" +
            "<reserved2/>\n" +
            "<reserved3/>\n" +
            "<reserved4/>\n" +
            "<reserved5/>\n" +
            "<agreement>0</agreement>\n" +
            "<viewable>1</viewable>\n" +
            "<pageId>0</pageId>\n" +
            "<groupId>0</groupId>\n" +
            "<recommendMethodId>99</recommendMethodId>\n" +
            "</RecommendContent>\n" +
            "</RecommendContentsList>\n" +
            "</Object>";

}
