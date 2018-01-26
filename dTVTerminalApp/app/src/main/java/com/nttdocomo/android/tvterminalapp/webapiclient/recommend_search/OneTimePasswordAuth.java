/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOTT;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;

/**
 * ワンタイムパスワードでレコメンドサーバーに認証を行うクラス.
 */
class OneTimePasswordAuth extends WebApiBase implements WebApiCallback,
        DaccountGetOTT.DaccountGetOttCallBack {

    /**
     * コンテキスト
     */
    private Context mContext = null;

    /**
     * 元の呼び出しURL
     */
    private String mSrcUrl = "";

    /**
     * 元の呼び出しパラメータ
     */
    private LinkedHashMap mSrcQueryItems = null;

    /*
     *  各固定値
     *  TODO: 確定後に固定値のクラスへ移動の予定
     */
    /**
     * 遷移先URLの項目名.
     */
    static private final String DESTINATION_URL_NAME = "rl";
    /**
     * 遷移先URLの中身.
     */
    static private final String DESTINATION_URL =
            "https://ve.m.service.smt.docomo.ne.jp/srermd/recommend/index.do";

    /**
     * ワンタイムパスワードの項目名.
     */
    static private final String AUTH_OTP_NAME = "AuthOtp";

    /**
     * コールバック定義.
     */
    private OneTimePasswordAuthCallback mOneTimePasswordAuthCallback;

    public interface OneTimePasswordAuthCallback {

        /**
         * ワンタイムパスワードの取得後のコールバック.
         * WebAPIクライアント側で処理すべきパラメータを返す。
         * ワンタイムパスワードが取得できていれば、元々のURLをリダイレクト先に設定した、認証用URLが指定される。
         * ワンタイムパスワードが取得できなかったり、まだ認証の必要が無い場合は、呼び出されたときの値がそのまま返る
         *
         * @param url                処理を行うWebAPIのURL
         * @param queryItems         与えるパラメータ群
         * @param useOneTimePassword ワンタイムパスワード用の付加をおこなった場合はtrueとなる
         */
        void oneTimePasswordAuthCallback(String url, LinkedHashMap queryItems,
                                         boolean useOneTimePassword);
    }

    public OneTimePasswordAuth(OneTimePasswordAuthCallback oneTimePasswordAuthCallback) {
        mOneTimePasswordAuthCallback = oneTimePasswordAuthCallback;
    }

    /**
     * ワンタイムパスワードを用いた認証を開始する.
     *
     * @param context       コンテキスト
     * @param srcUrl        元のWebAPI用URL
     * @param srcQueryItems 元のWebAPI用パラメータ
     */
    public void authOneTimePasswordStart(Context context, String srcUrl,
                                         LinkedHashMap srcQueryItems) {
        DTVTLogger.start();

        //与えられたパラメータの退避
        mContext = context;
        mSrcUrl = srcUrl;
        mSrcQueryItems = srcQueryItems;

        //dアカウントのワンタイムパスワードの取得を行う
        DaccountGetOTT getOtt = new DaccountGetOTT();
        getOtt.execDaccountGetOTT(mContext, this);

        //以後の処理はワンタイムパスワード取得後のコールバックで行う

        DTVTLogger.end();
    }

    @Override
    public void getOttCallBack(int result, String id, String oneTimePassword) {
        DTVTLogger.start();

        //TODO: dアカウントの環境が停止している際の暫定値
        //result = 0;
        //oneTimePassword = "dummy";

        //TODO: 強制的に既存の処理を行うための暫定値
        result = 9;

        //ワンタイムパスワード処理から帰ってきたので結果を判定
        if (result != 0) {
            //ワンタイムパスワードは取得失敗
            DTVTLogger.end("One Time Password Get failed. Original URL use");

            //元のURLとパラメータをおうむ返しで渡す事で、これまでのレコメンドの動作とする
            mOneTimePasswordAuthCallback.oneTimePasswordAuthCallback(mSrcUrl,
                    mSrcQueryItems, false);
            return;
        }

        String redirectUrl;
        if(mSrcUrl != null && mSrcQueryItems != null) {
            //元のURLとパラメータをリダイレクト先に詰める為に、一つにする
            redirectUrl = createUrlComponents(mSrcUrl, mSrcQueryItems);
        } else {
            redirectUrl = mSrcUrl;
        }

        //パラメータ編集
        LinkedHashMap queryItems = new LinkedHashMap();

        //URLはエンコードする
        String encodedUrl;
        try {
            //encodedUrl = URLEncoder.encode(DESTINATION_URL + "?getPage=1&airtime=2&pageId=101", "UTF-8");
            encodedUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodedUrl = DESTINATION_URL;
        }

        //遷移先URLの指定
        queryItems.put(DESTINATION_URL_NAME, encodedUrl);

        //ワンタイムパスワードの指定(2個目のパラメータなので、&を付加)
        queryItems.put(StringUtils.getConnectStrings("&", AUTH_OTP_NAME), oneTimePassword);

        //TODO: iOS版での返答次第で必要になるかもしれない各パラメータ
        //サービス識別番号
        //queryItems.put("&si", "1111");
        //認証可否
        //queryItems.put("&authif", "1");
        //アーカイブパラメータ
        //queryItems.put("&arcv", "eAHtk9uOmzAQht8FiV61xAeOK61acMwmmyyQkmazuSPgENLlsJxyWPXda7uqetEnqFSLi29mPL-N_rHv-o4HMCHUm5oYGWBqGYaHDIciHejYISbRbY8aPqSmA-0pAYZl69AkFNIpsegXBKAFTGgAud4VoNwp8QNdKx8VKPDY9013N5mMTCu19JBrXdlrWZ3WZa1VTDs1k2Toj5M0LyZJ89qcMxkWVcYufNvn9vVeKqjYVZHPP6nTsXYsUvaXFq93LWvLjEPL-BklqwT_llOxn7M-SnKm4ilUkZkUbV-UIkI8anhhnokSgB9cfq2wb-5PK7Rtb1n1QvL1InDOyRKzdNNvz_Un35hFz-6vPwddvrriJnyox0v8yLK3RG-KJ_2yOBujR8fQ49fAKvKCekmWN3zAIC0Qi9Nt9MI2310UBlHDYt9edtvs2NDWqWkyxN1Qh7vHwyFrRt7Lxm_rIUabUzz2wzyamcX-VKwrZzEM21XND0CrW7JxkpP9_Hb0Z4ve8NrRD65-_LULcFSwZB88PdKdF4VHLndNeItL0g7vVmQ_xml1JZt0LGjEzUNYuAc4SYAcLJHRhbG2ICTIkQSgDSAECCAT2RiLLmSIyn_3_1H3TeGeAyzhJBJcVQLlkxb2GrpIxss5AGI0kJwI6bucCJHjY8C3SJJtkv5MlRQAyo-fjx5epg");

        // ワンタイムパスワード認証を付加したURLを呼び出す
        mOneTimePasswordAuthCallback.oneTimePasswordAuthCallback(mSrcUrl, queryItems, true);

        DTVTLogger.end();
    }

    /**
     * 通信終了後に呼ばれるコールバック
     *
     * @param responseData 帰ってきたレスポンスデータ
     */
    @Override
    public void onFinish(String responseData) {
        DTVTLogger.start();

        //得られたXMLのパースを行って、データを返す
        //new RecommendChannelXmlParser(OneTimePasswordAuthCallback).execute(responseData);
        DTVTLogger.end();
    }
}
