/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient;

import android.content.Context;
import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOtt;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.HttpThread;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.WebApiCallback;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 通信処理クラス.
 */
public class WebApiBase implements HttpThread.HttpThreadFinish {

    /**
     * 通信処理終了のコールバック.
     */
    private WebApiCallback mWebApiCallback = null;
    /**
     * HTTP通信スレッド.
     */
    private HttpThread mHttpThread = null;

    /**
     * 情報通信処理（ワンタイムパスワード認証等が不要の場合）.
     *
     * @param urlString  URL
     * @param queryItems 通信パラメータ
     * @param callback   終了コールバック
     * @param context    コンテキスト
     */
    protected void getNoPassword(final String urlString, final LinkedHashMap<String, String> queryItems,
                              final WebApiCallback callback, final Context context) {
        final Handler handler = new Handler();
        final String url = createUrlComponents(urlString, queryItems);
        mWebApiCallback = callback;
        final WebApiBase webApiBase = this;

        //ワンタイムパスワード無しで呼び出す
        mHttpThread = new HttpThread(url, handler, webApiBase, context, "", null);
        mHttpThread.start();
    }

    /**
     * 情報通信処理・Handlerが使用できないASyncTaskの処理内で使用する.
     *
     * @param urlString  URL
     * @param queryItems 通信用パラメータ
     * @param callback   終了コールバック
     * @param context    コンテキスト
     */
    protected void getRecomendInfo(final String urlString, final LinkedHashMap<String, String> queryItems,
                                   final WebApiCallback callback, final Context context) {
        final String url = createUrlComponents(urlString, queryItems);
        mWebApiCallback = callback;
        final WebApiBase webApiBase = this;
        //Log.d(DCommon.LOG_DEF_TAG, "WebApiBase::get, url= " + url);
        //dアカウントのワンタイムパスワードの取得を行う
        final DaccountGetOtt getOtt = new DaccountGetOtt();
        getOtt.execDaccountGetOTT(context, false, new DaccountGetOtt.DaccountGetOttCallBack() {
            @Override
            public void getOttCallBack(final int result, final String id, final String oneTimePassword) {
                //ワンタイムパスワードの取得後に呼び出す
                mHttpThread = new HttpThread(url, webApiBase, context, oneTimePassword, getOtt);
                mHttpThread.start();
            }
        });
    }

    /**
     * get呼び出し用に、URLとパラメータを統合する.
     *
     * @param url        呼び出し用URL
     * @param queryItems 呼び出し用パラメータ
     * @return 統合後文字列
     */
    private String createUrlComponents(final String url, final Map<String, String> queryItems) {
        StringBuffer stringBuffer = new StringBuffer("");
        if (null != url) {

            stringBuffer = new StringBuffer(url);

            for (String key : queryItems.keySet()) {
                if (null != key && 0 < key.length()) {
                    if (queryItems.containsKey(key)) {
                        String v = "";
                        try {
                            v = queryItems.get(key);
                        } catch (Exception e) {
                            DTVTLogger.debug(e);
                        }
                        if (null != v) {
                            stringBuffer.append(key);
                            stringBuffer.append("=");
                            stringBuffer.append(queryItems.get(key));
                        } else {
                            DTVTLogger.debug("WebApiBase::createUrlComponents, queryItems.get(key) is NULL");
                        }
                    } else {
                        DTVTLogger.debug("WebApiBase::createUrlComponents, queryItems has no key " + key);
                    }
                }

            }
            DTVTLogger.debug("WebApiBase::createUrlComponents, url=" + stringBuffer.toString());
        }
        return stringBuffer.toString();
    }



    /**
     * エラーステータスを返す.
     *
     * @return エラーステータス
     */
    public ErrorState getError() {
        //エラー情報の有無を判定
        if (mHttpThread != null && mHttpThread.getError() != null) {
            //エラー情報があるならば横流しする
            return mHttpThread.getError();
        } else {
            //エラー情報がまだない場合は、新規に作成して返す
            return new ErrorState();
        }
    }

    /**
     * 継承先から受け取ったエラーコードを横流しする.
     *
     * @param errorCode エラーコード
     */
    public void setErrorCode(final String errorCode) {
        mHttpThread.setErrorCode(errorCode);
    }

    @Override
    public void onHttpThreadFinish(final String str, final ErrorState errorStatus) {
        //エラーコードだけ抜き出してエラー情報に入れる
        mHttpThread.setXmlErrorCode(str);

        if (null != mWebApiCallback) {
            mWebApiCallback.onFinish(str);
        }
    }

    /**
     * 通信処理を停止する.
     */
    protected void stopHTTPConnection() {
        if (mHttpThread != null) {
            mHttpThread.disconnect();
        }
    }
}