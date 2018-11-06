/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient;

import android.content.Context;
import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.utils.DaccountUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOtt;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.IDimDefines;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.OttGetAuthSwitch;
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
     * @param queryItems 通信パラメータ
     * @param callback   終了コールバック
     * @param context    コンテキスト
     */
    protected void getNoPassword(final LinkedHashMap<String, String> queryItems,
                              final WebApiCallback callback, final Context context) {
        final Handler handler = new Handler();
        final String url = createUrlComponents(UrlConstants.WebApiUrl.TOTAL_SEARCH_URL, queryItems);
        mWebApiCallback = callback;
        final WebApiBase webApiBase = this;

        //ワンタイムパスワード無しで呼び出す
        mHttpThread = new HttpThread(url, handler, webApiBase, context, "", null);
        mHttpThread.start();
    }

    /**
     * 検レコ情報通信処理.TODO　一時実装 将来的はおすすめ番組・ビデオと同じ関数で使うようにする
     *
     * @param queryItems 通信パラメータ
     * @param callback   終了コールバック
     * @param context    コンテキスト
     */
    protected void getRecommendSearch(final LinkedHashMap<String, String> queryItems,
                                 final WebApiCallback callback, final Context context) {
        final String url = createUrlComponents(UrlConstants.WebApiUrl.TOTAL_SEARCH_URL, queryItems);
        mWebApiCallback = callback;
        final WebApiBase webApiBase = this;
        DTVTLogger.debug("”execDaccountGetOTT” getRecommendSearch");
        //認証画面の表示状況のインスタンスの取得
        final OttGetAuthSwitch ottGetAuthSwitch = OttGetAuthSwitch.INSTANCE;
        //dアカウントのワンタイムパスワードの取得を行う(未認証時は認証画面へ遷移するように変更)
        final DaccountGetOtt getOtt = new DaccountGetOtt();
        if (getOtt != null) {
            //通信キャンセル呼び出し
            getOtt.cancelConnection();
        }
        getOtt.execDaccountGetOTT(context, ottGetAuthSwitch.isNowAuth(), new DaccountGetOtt.DaccountGetOttCallBack() {
            @Override
            public void getOttCallBack(final int result, final String id, final String oneTimePassword) {
                //ワンタイムパスワードの取得後に呼び出す
                DTVTLogger.debug("”execDaccountGetOTT” getRecommendSearch getOttCallBack");
                //TODO CiRCUS経由で検レコサーバへの接続する際に、OTTを使用したサーバ連携については現在申請中のため、OTTを付与した状態での確認は別BLとする
                mHttpThread = new HttpThread(url, webApiBase, context, "", null);
                mHttpThread.start();
            }
        });
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
        DTVTLogger.debug("”execDaccountGetOTT” getRecomendInfo");

        //認証画面の表示状況のインスタンスの取得
        final OttGetAuthSwitch ottGetAuthSwitch = OttGetAuthSwitch.INSTANCE;

        //dアカウントのワンタイムパスワードの取得を行う(未認証時は認証画面へ遷移するように変更)
        final DaccountGetOtt getOtt = new DaccountGetOtt();
        getOtt.execDaccountGetOTT(context, ottGetAuthSwitch.isNowAuth(), new DaccountGetOtt.DaccountGetOttCallBack() {
            @Override
            public void getOttCallBack(final int result, final String id, final String oneTimePassword) {
                if (result == IDimDefines.RESULT_USER_CANCEL) {
                    //認証画面でキャンセルされたので、ログアウトダイアログを呼ぶ
                    ottGetAuthSwitch.showLogoutDialog();
                } else if (result == DaccountUtils.D_ACCOUNT_APP_NOT_FOUND_ERROR_CODE) {
                    //dアカウント設定アプリが見つからなかったので、見つからないダイアログを出す
                    ottGetAuthSwitch.showDAccountApliNotFoundDialog();
                } else if (result == IDimDefines.RESULT_INTERNAL_ERROR) {
                    callback.onFinish("");
                } else {
                    DTVTLogger.debug("”execDaccountGetOTT” getRecomendInfo getOttCallBack");
                    //ワンタイムパスワードの取得後に呼び出す
                    mHttpThread = new HttpThread(url, webApiBase, context, oneTimePassword, getOtt);
                    mHttpThread.start();
                }
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