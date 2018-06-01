/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.nttdocomo.android.ocsplib.OcspURLConnection;
import com.nttdocomo.android.ocsplib.OcspUtil;
import com.nttdocomo.android.ocsplib.exception.OcspParameterException;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.struct.OneTimeTokenData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOtt;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * 通信処理.
 */
public class WebApiBasePlala {
    /**
     * エラー値.
     */
    private ReturnCode mReturnCode = null;
    /**
     * コンテキスト.
     */
    private Context mContext = null;
    /**
     * コールバックのインスタンス.
     */
    private WebApiBasePlalaCallback mWebApiBasePlalaCallback = null;
    /**
     * 通信停止用コネクション蓄積.
     */
    private volatile static List<HttpURLConnection> mUrlConnections = null;
    /**
     * 全部止まった場合のフラグ.
     */
    private static boolean mIsStopAllConnections = false;
    /**
     * コネクション.
     */
    private HttpURLConnection mUrlConnection = null;
    /**
     * 結果を受け取るバッファ.
     */
    private String mAnswerBuffer = "";
    /**
     * ワンタイムトークン情報.
     */
    private OneTimeTokenData mOneTimeTokenData = null;
    /**
     * ワンタイムトークン取得クラス.
     */
    private DaccountGetOtt mGetOtt = null;
    /**
     * クッキーマネージャー.
     */
    private CookieManager mCookieManager;
    /**
     * リクエスト種別・基本はPOST・子クラスで使われるのでプロテクテッド.
     */
    private static final String REQUEST_METHOD_POST = "POST";
    /**
     * リダイレクト処理用にGETも定義・子クラスで使われるのでプロテクテッド.
     */
    static final String REQUEST_METHOD_GET = "GET";
    /**
     * 文字種別 UTF-8.
     */
    private static final String UTF8_CHARACTER_SET = "UTF-8";

    //POSTでJSONを送信する為のパラメータ群
    /**
     * Content-Type.
     */
    private static final String CONTENT_TYPE_KEY_TEXT = "Content-Type";
    /**
     * application/json: charset=UTF-8.
     */
    private static final String CONTENT_TYPE_TEXT = "application/json; charset="
            + UTF8_CHARACTER_SET; //final同士なので、+での結合こそベスト
    /**
     * Connection.
     */
    private static final String CONNECTION_KEY_TEXT = "Connection";
    /**
     * close.
     */
    private static final String CONTENT_CLOSE_TEXT = "close";

    /**
     * API通信用タスク.
     */
    private CommunicationTask mCommunicationTaskAPI = null;
    /**
     * API通信用フラグ(拡張情報付き).
     */
    private CommunicationTask mCommunicationTaskExtra = null;
    /**
     * ワンタイムトークンの取得コールバックからのタスク呼び出し用.
     */
    private CommunicationTask mCommunicationTaskOtt = null;
    /**
     * タイムアウト時間.
     */
    private int mTimeOut = 0;

    /**
     * 通信停止用フラグ.
     */
    private boolean mIsStop = false;
    /**
     * サービストークンのクッキーでのキー名.
     */
    private static final String SERVICE_TOKEN_KEY_NAME = "daccount_auth";
    /**
     * 日付形式判定用.
     */
    private static final String DATE_PATTERN = "yyyyMMdd";
    /**
     * リダイレクト用飛び先関連情報取得.
     */
    private static final String REDIRECT_JUMP_URL_GET = "Location";
    /**
     * SSL失効チェック時にコンテキストが存在しない場合の例外用テキスト.
     */
    private static final String NO_CONTEXT_ERROR = "No context";

    /**
     * データ受け渡しコールバック.
     */
    interface WebApiBasePlalaCallback {
        /**
         * 正常終了時のコールバック.
         *
         * @param returnCode 値を返す構造体
         */
        void onAnswer(final ReturnCode returnCode);

        /**
         * 通信失敗時のコールバック.
         *
         * @param returnCode 値を返す構造体
         */
        void onError(final ReturnCode returnCode);
    }

    /**
     * サービストークンエラーコールバック.
     */
    interface ServiceTokenErrorCallback {
        /**
         * サービストークン取得失敗のコールバック.
         *
         * @param returnCode 値を返す構造体
         */
        void onTokenError(final ReturnCode returnCode);
    }

    //指定文字列パラメータ群
    //対外的なパラメータなので、現在は非使用の物にもpublicが必要になる。
    /**
     * フィルター用指定文字列・release.
     */
    public static final String FILTER_RELEASE = "release";
    /**
     * フィルター用指定文字列・testa.
     */
    static final String FILTER_TESTA = "testa";
    /**
     * フィルター用指定文字列・demo.
     */
    static final String FILTER_DEMO = "demo";
    /**
     * フィルター用指定文字列・ssvod.
     */
    public static final String FILTER_RELEASE_SSVOD = "release|ssvod";
    /**
     * フィルター用指定文字列・フィルターのパラメータ名.
     */
    static final String FILTER_PARAM = "filter";

    /**
     * タイプ用指定文字列・ｄCh.
     */
    static final String TYPE_D_CHANNEL = "dch";
    /**
     * タイプ用指定文字列・ひかりTV.
     */
    static final String TYPE_HIKARI_TV = "hikaritv";
    /**
     * タイプ用指定文字列・ひかりTVのVOD.
     */
    static final String TYPE_HIKARI_TV_VOD = "hikaritv_vod";
    /**
     * タイプ用指定文字列・dTVのVOD.
     */
    static final String TYPE_DTV_VOD = "dtv_vod";
    /**
     * タイプ用指定文字列・ひかりTVのVODとdTVのVOD.
     */
    static final String TYPE_HIKARI_TV_AND_DTV_VOD = "hikaritv_and_dtv_vod";
    /**
     * タイプ用指定文字列・全て（指定なしは全てになる）.
     */
    static final String TYPE_ALL = "";

    /**
     * 日付指定文字列・現在時刻指定.
     */
    public static final String DATE_NOW = "now";

    /**
     * ソート用文字列・タイトルルビ昇順.
     */
    static final String SORT_TITLE_RUBY_ASC = "titleruby_asc";
    /**
     * ソート用文字列・配信開始日昇順.
     */
    static final String SORT_AVAIL_S_ASC = "avail_s_asc";
    /**
     * ソート用文字列・配信終了日降順.
     */
    static final String SORT_AVAIL_E_DESC = "avail_e_desc";
    /**
     * ソート用文字列・人気順（前日の視聴回数数降順）.
     */
    static final String SORT_PLAY_COUNT_DESC = "play_count_desc";

    /**
     * age_req(年齢設定値)の最小値.
     */
    static final int AGE_LOW_VALUE = 1;

    /**
     * age_req(年齢設定値)の最大値.
     */
    static final int AGE_HIGH_VALUE = 17;

    /**
     * マイチャンネル登録位置の上限値.
     */
    public static final int MY_CHANNEL_MAX_INDEX = 16;

    /**
     * チャンネルのアダルトタイプ(adult).
     */
    public static final String MY_CHANNEL_ADULT_TYPE_ADULT = "adult";

    /**
     * チャンネルのアダルトタイプ(空値).
     */
    static final String MY_CHANNEL_ADULT_TYPE_EMPTY = "";

    /**
     * チャンネルのパレンタル設定値(G).
     */
    public static final String MY_CHANNEL_R_VALUE_G = "G";

    /**
     * チャンネルのパレンタル設定値(PG-12).
     */
    static final String MY_CHANNEL_R_VALUE_PG_12 = "PG-12";

    /**
     * チャンネルのパレンタル設定値(R-15).
     */
    static final String MY_CHANNEL_R_VALUE_PG_15 = "R-15";

    /**
     * チャンネルのパレンタル設定値(R-18).
     */
    static final String MY_CHANNEL_R_VALUE_PG_18 = "R-18";

    /**
     * チャンネルのパレンタル設定値(R-20).
     */
    static final String MY_CHANNEL_R_VALUE_PG_20 = "R-20";

    /**
     * age_req(年齢設定値)のパラメータ作成用文字列.
     */
    public static final String AGE_REQ_STRING = "age_req";

    /**
     * コンテンツ識別ID作成文字列.
     */
    public static final String CRID_STRING = "crid";

    /**
     * リスト作成文字列.
     */
    static final String LIST_STRING = "list";

    /**
     * h4d_iptv：多チャンネル.
     */
    public static final String CLIP_TYPE_H4D_IPTV = "h4d_iptv";

    /**
     * h4d_vod：ビデオ.
     */
    public static final String CLIP_TYPE_H4D_VOD = "h4d_vod";

    /**
     * dch：dTVチャンネル.
     */
    public static final String CLIP_TYPE_DCH = "dch";

    /**
     * dtv_vod：dTV.
     */
    public static final String CLIP_TYPE_DTV_VOD = "dtv_vod";

    /**
     * ワンタイムトークン設定のキー名.
     */
    private static final String ONE_TIME_TOKEN_KEY = "x-service-token";


    /**
     * ワンタイムトークン設定の取得時のコンテントタイプ指定.
     */
    private static final String ONE_TIME_TOKEN_GET_CONTENT_TYPE =
            "application/x-www-form-urlencoded";

    /**
     * 戻り値用構造体.
     */
    static protected class ReturnCode {
        /**
         * 通信時エラー情報.
         */
        ErrorState errorState;
        /**
         * 本体データ.
         */
        String bodyData;
        /**
         * 拡張データ.
         */
        Bundle extraData;

        /**
         * コンストラクタ.
         */
        ReturnCode() {
            errorState = new ErrorState();
            bodyData = "";
            extraData = null;
        }
    }

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    WebApiBasePlala(final Context context) {
        //コンテキストの退避
        mContext = context;

        //コネクション蓄積が存在しなければ作成する
        if (mUrlConnections == null) {
            mUrlConnections = new ArrayList<>();
        }

        //戻り値の準備
        mReturnCode = new ReturnCode();
    }

    /**
     * コネクションを蓄積して、後で止められるようにする.
     *
     * @param mUrlConnection コネクション
     */
    private void addUrlConnections(final HttpURLConnection mUrlConnection) {
        //通信が終わり、ヌルが入れられる場合に備えたヌルチェック
        if (mUrlConnections == null) {
            //既に削除されていたので、再度確保を行う
            mUrlConnections = new ArrayList<>();
        }

        //HTTPコネクションを追加する
        mUrlConnections.add(mUrlConnection);
    }

    /**
     * 全ての通信を遮断する.
     */
    synchronized void stopAllConnections() {
        DTVTLogger.start();
        if (mIsStopAllConnections) {
            return;
        }
        //各通信タスクにキャンセルを通知する
        mIsStop = true;
        if (mCommunicationTaskAPI != null) {
            mCommunicationTaskAPI.cancel(true);
        }
        if (mCommunicationTaskExtra != null) {
            mCommunicationTaskExtra.cancel(true);
        }
        if (mCommunicationTaskOtt != null) {
            mCommunicationTaskOtt.cancel(true);
        }

        if (mUrlConnections == null) {
            return;
        }

        //全ての通信を止めることを宣言する
        mIsStopAllConnections = true;

        //全てのコネクションにdisconnectを送る
        Iterator<HttpURLConnection> iterator = mUrlConnections.iterator();
        while (iterator.hasNext()) {
            final HttpURLConnection stopConnection = iterator.next();
            //findBugsは別クラスに分離せよと言うが、見通しが悪化するので対応しない。
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    stopConnection.disconnect();
                }
            });
            thread.start();

            //止めた物は消す
            iterator.remove();
        }
    }

    /**
     * 切断済みコネクションを蓄積から削除する.
     *
     * @param connection 削除したいコネクション
     */
    static private void removeConnections(final HttpURLConnection connection) {
        if (mUrlConnections != null) {

            mUrlConnections.remove(connection);

            //すべて削除済みならばヌルにする
            if (mUrlConnections.size() == 0) {
                mUrlConnections = null;
            }
        }
    }

    /**
     * WebAPIのエラー情報を取得する.
     *
     * @return エラー情報クラス
     */
    public ErrorState getError() {
        DTVTLogger.start();
        DTVTLogger.debug("get error statue=" + mReturnCode.errorState.getErrorType());
        //エラーメッセージの取得を行う
        mReturnCode.errorState.addErrorMessage(mContext);
        DTVTLogger.debug("get error message=" + mReturnCode.errorState.getErrorMessage());
        DTVTLogger.end();
        //エラーコードを返す
        return mReturnCode.errorState;
    }

    /**
     * 指定したAPIで通信を開始する.
     *
     * @param sourceUrl               API呼び出し名
     * @param receivedParameters      API呼び出し用パラメータ
     * @param webApiBasePlalaCallback コールバック
     */
    void openUrl(final String sourceUrl, final String receivedParameters,
                        final WebApiBasePlalaCallback webApiBasePlalaCallback) {

        mCommunicationTaskAPI = new CommunicationTask(sourceUrl, receivedParameters, getRequestMethod());

        //コールバックの準備
        mWebApiBasePlalaCallback = webApiBasePlalaCallback;

        //結果格納構造体の作成
        ReturnCode returnCode = new ReturnCode();

        //通信本体の開始
        mCommunicationTaskAPI.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode);
    }

    /**
     * 指定したAPIで通信を開始する(タイムアウト指定付き).
     *
     * @param sourceUrl               API呼び出し名
     * @param receivedParameters      API呼び出し用パラメータ
     * @param timeOut                   タイムアウト時間指定(0はAPIデフォルト指定とする)
     * @param webApiBasePlalaCallback コールバック
     */
    void openUrl(final String sourceUrl, final String receivedParameters,
                        final int timeOut,
                        final WebApiBasePlalaCallback webApiBasePlalaCallback) {
        //タイムアウト時間を指定する
        mTimeOut = timeOut;

        //通常の通信開始を呼ぶ
        openUrl(sourceUrl, receivedParameters, webApiBasePlalaCallback);
    }


        /**
         * 指定したAPIで通信を開始する(拡張情報付き).
         *
         * @param sourceUrl               API呼び出し名
         * @param receivedParameters      API呼び出し用パラメータ
         * @param webApiBasePlalaCallback 結果のコールバック
         * @param extraDataSrc            拡張情報
         */
    void openUrlWithExtraData(final String sourceUrl, final String receivedParameters,
                                     final WebApiBasePlalaCallback webApiBasePlalaCallback,
                                     final Bundle extraDataSrc) {
        //拡張情報もセットする
        mCommunicationTaskExtra = new CommunicationTask(sourceUrl,
                receivedParameters, extraDataSrc, getRequestMethod());

        //コールバックの準備
        mWebApiBasePlalaCallback = webApiBasePlalaCallback;

        //結果格納構造体の作成
        ReturnCode returnCode = new ReturnCode();

        //通信本体の開始
        mCommunicationTaskExtra.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode);
    }

    /**
     * 指定したAPIをワンタイムトークン付きで通信を開始する.
     *
     * @param sourceUrl               API呼び出し名
     * @param receivedParameters      API呼び出し用パラメータ
     * @param webApiBasePlalaCallback 結果のコールバック
     * @param extraDataSrc            拡張情報（使用しないときはヌルをセット）
     */
    void openUrlAddOtt(final String sourceUrl, final String receivedParameters,
                              final WebApiBasePlalaCallback webApiBasePlalaCallback,
                              final Bundle extraDataSrc) {
        //タスクを作成する
        mCommunicationTaskOtt = new CommunicationTask(sourceUrl, receivedParameters,
                extraDataSrc, true, getRequestMethod());

        //呼び出し元に戻るコールバックの準備
        mWebApiBasePlalaCallback = webApiBasePlalaCallback;

        //ワンタイムトークン専用のコールバックを作成する
        ServiceTokenErrorCallback serviceTokenErrorCallback = new ServiceTokenErrorCallback() {
            @Override
            public void onTokenError(final ReturnCode returnCode) {
                //ワンタイムトークン側のエラー情報を、WebApi側にも反映する
                mReturnCode = returnCode;

                //呼び出し元にエラーを伝える
                mWebApiBasePlalaCallback.onError(returnCode);
            }
        };

        //ワンタイムトークンの情報を取得する
        mOneTimeTokenData = SharedPreferencesUtils.getOneTimeTokenData(mContext);

        //ワンタイムトークンの期限切れ確認
        if (mOneTimeTokenData.getOneTimeTokenGetTime()
                < DateUtils.getNowTimeFormatEpoch()) {
            //期限切れなので、ワンタイムパスワードの取得を起動
            getOneTimePassword(mContext, serviceTokenErrorCallback);
        } else {
            //有効なワンタイムトークンなので、そのまま使用して処理を呼び出す
            mCommunicationTaskOtt.setOneTimeToken(mOneTimeTokenData.getOneTimeToken());
            ReturnCode returnCode = new ReturnCode();
            DTVTLogger.debug("******mCommunicationTaskOtt.execute at openUrlAddOtt");
            mCommunicationTaskOtt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode);
        }
    }

    /**
     * ワンタイムトークン取得APIの通信を開始する.
     * (使用方法が特異の為、専用とする)
     *
     * @param receivedParameters      API呼び出し用パラメータ
     * @param webApiBasePlalaCallback コールバック
     */
    void openOneTimeTokenGetUrl(final String receivedParameters,
                                       final WebApiBasePlalaCallback webApiBasePlalaCallback) {
        DTVTLogger.start();

        CommunicationTask communicationTask = new CommunicationTask(receivedParameters);

        //コールバックの準備
        mWebApiBasePlalaCallback = webApiBasePlalaCallback;

        //結果格納構造体の作成
        ReturnCode returnCode = new ReturnCode();

        //通信本体の開始
        communicationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode);

        DTVTLogger.end();
    }

    /**
     * ワンタイムトークンを取得する為に、dアカウント設定アプリからワンタイムパスワードを取得する.
     *
     * @param context コンテキスト
     *@param serviceTokenErrorCallback   サービストークンエラーコールバック
     */
    private void getOneTimePassword(final Context context,
                                    final ServiceTokenErrorCallback serviceTokenErrorCallback) {
        //ワンタイムパスワードの取得
        mGetOtt = new DaccountGetOtt();
        mGetOtt.execDaccountGetOTT(context, false, new DaccountGetOtt.DaccountGetOttCallBack() {
            @Override
            public void getOttCallBack(final int result, final String id, final String oneTimePassword) {
                //ワンタイムトークンが期限内ならば、そのまま使用する
                OneTimeTokenData tokenData = SharedPreferencesUtils.getOneTimeTokenData(mContext);

                //期限内ならば、そのまま使用する
                if (tokenData.getOneTimeTokenGetTime() > DateUtils.getNowTimeFormatEpoch()) {
                    //取得済みのトークンを使用する
                    mCommunicationTaskOtt.setOneTimeToken(tokenData.getOneTimeToken());

                    //結果格納構造体の作成
                    ReturnCode returnCode = new ReturnCode();

                    //ワンタイムトークンの取得結果を元にして、通信を開始する
                    DTVTLogger.debug("******mCommunicationTaskOtt.execute at getOttCallBack");
                    mCommunicationTaskOtt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode);
                    return;
                }

                //ワンタイムトークンの取得を行う
                getServiceToken(mContext, mCommunicationTaskOtt,
                        oneTimePassword, serviceTokenErrorCallback);
            }
        });
    }

    /**
     * ワンタイムトークンの取得を行う.
     *
     * @param context           　コンテキスト
     * @param communicationTask 通信処理クラス
     * @param oneTimePassword   ワンタイムパスワード
     * @param serviceTokenErrorCallback サービストークンエラーコールバック
     */
    private static void getServiceToken(final Context context,
                                       final CommunicationTask communicationTask,
                                       final String oneTimePassword,
                                       final ServiceTokenErrorCallback serviceTokenErrorCallback) {
        //ワンタイムトークンとその取得時間を取得する
        ServiceTokenClient tokenClient = new ServiceTokenClient(context);

        boolean answer = tokenClient.getServiceTokenApi(oneTimePassword,
                new ServiceTokenClient.TokenGetCallback() {
                    @Override
                    public void onTokenGot(final boolean successFlag) {
                        //結果に値があるかを確認
                        if (successFlag) {
                            //値があったので、セット
                            communicationTask.setOneTimeToken(
                                    SharedPreferencesUtils.
                                            getOneTimeTokenData(context).getOneTimeToken());

                            ReturnCode returnCode = new ReturnCode();
                            //既に実行されたかどうかの判定
                            if (communicationTask.getStatus().equals(AsyncTask.Status.PENDING)) {
                                //実行されていないので、ワンタイムトークンの取得結果を元にして、通信を開始する
                                communicationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode);
                            } else {
                                //既に実行されていたので、処理をスキップ
                                //(2重起動はsuccessFlagがfalseの場合にしか確認していないので、実行されない筈ですが)
                                DTVTLogger.debug("communicationTask already exec");
                            }

                        } else {
                            //値が無いのでリセット
                            communicationTask.setOneTimeToken("");

                            //通信エラーとして処理を終了する
                            if (serviceTokenErrorCallback != null) {
                                DTVTLogger.debug("token get failed ");
                                //通信エラーのエラーコードを作成する
                                ReturnCode returnCode = new ReturnCode();
                                returnCode.errorState.setErrorType(
                                        DtvtConstants.ErrorType.TOKEN_ERROR);
                                //呼び出し元にエラーを伝える
                                serviceTokenErrorCallback.onTokenError(returnCode);
                            }
                        }
                    }
                }
        );

        DTVTLogger.debug("getServiceToken answer = " + answer);

        if (!answer) {
            //dアカウントが設定されていない場合はこちらが動作する

            //結果格納構造体の作成
            ReturnCode returnCode = new ReturnCode();

            DTVTLogger.debug("communicationTask answer false = " + communicationTask);

            //ワンタイムトークンは取得できなかったので、そのまま通信を開始する
            communicationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode);
        }
    }

    /**
     * ボディ部の読み込みを行う.
     *
     * @param statusCode コネクションの際のステータス
     * @return 読み込んだボディ部
     */
    private String readConnectionBody(final int statusCode) {
        if (statusCode != HttpURLConnection.HTTP_OK) {
            //HTTP通信エラーとして元に返す
            mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.HTTP_ERROR);
            mReturnCode.errorState.setErrorCode(String.valueOf(statusCode));
            DTVTLogger.debug(String.format("statusCode[%s]", statusCode));
            return "";
        }

        //コネクトに成功したので、控えておく
        addUrlConnections(mUrlConnection);

        StringBuilder stringBuilder = null;
        InputStream stream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            //ボディー部の読み込み用ストリームを開く
            stream = mUrlConnection.getInputStream();
            stringBuilder = new StringBuilder();
            String lineBuffer;
            inputStreamReader = new InputStreamReader(stream, UTF8_CHARACTER_SET);
            bufferedReader = new BufferedReader(inputStreamReader);

            //内容が尽きるまで蓄積する
            while ((lineBuffer = bufferedReader.readLine()) != null) {
                stringBuilder.append(lineBuffer);
                stringBuilder.append("\n");
            }

        } catch (UnsupportedEncodingException e) {
            //サーバーエラーとして元に返す
            mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SERVER_ERROR);
            DTVTLogger.debug(e);
        } catch (IOException e) {
            //全通信停止発行済みならば、正常な動作となる
            if (!mIsStopAllConnections) {
                //通信停止ではないので、通信エラー
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SERVER_ERROR);
                DTVTLogger.debug(e);
            }
        } finally {
            //ストリームを閉じる
            streamCloser(stream, inputStreamReader, bufferedReader);
        }

        //蓄積したボディー部を返す
        String bodyData = "";
        if (stringBuilder != null) {
            bodyData = stringBuilder.toString();
        }
        DTVTLogger.debug(String.format("bodyData[%s]", bodyData));
        return bodyData;
    }

    /**
     * ステータスが300番台かどうかのチェック.
     * (リダイレクト判定がレコメンドの認証にも必要になったので流用)
     *
     * @param status HTTPステータス
     * @return 300番台ならばtrue
     */
    public static boolean isRedirectCode(final int status) {
        switch (status) {
            case HttpURLConnection.HTTP_MULT_CHOICE:
            case HttpURLConnection.HTTP_MOVED_PERM:
            case HttpURLConnection.HTTP_MOVED_TEMP:
            case HttpURLConnection.HTTP_SEE_OTHER:
            case HttpURLConnection.HTTP_NOT_MODIFIED:
            case HttpURLConnection.HTTP_USE_PROXY:
                return true;
        }

        return false;
    }

    /**
     * HTTPSのコネクションのレスポンスを取得.
     *
     * @param connection HTTPSのコネクション
     * @return 取得したレスポンス
     */
    private String getAnswer(final HttpsURLConnection connection) {
        InputStream stream = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            stream = connection.getInputStream();
            String lineBuffer;
            InputStreamReader inputStreamReader = new InputStreamReader(stream, UTF8_CHARACTER_SET);
            bufferedReader = new BufferedReader(inputStreamReader);
            //内容が尽きるまで蓄積する
            while ((lineBuffer = bufferedReader.readLine()) != null) {
                stringBuilder.append(lineBuffer);
                stringBuilder.append("\n");
            }
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            //エラーコードを設定する
            mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SERVER_ERROR);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    DTVTLogger.debug(e);
                }
            }
        }

        return stringBuilder.toString();
    }

    /**
     * パラメータをストリームに書き込む.
     *
     * @param urlConnection 書き込み対象のコネクション
     * @param sendData パラメータ
     */
    private void setHttpsPostData(final HttpsURLConnection urlConnection, final String sendData) {
        if (urlConnection == null) {
            return;
        }
        // POSTデータ送信処理
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStream.write(sendData.getBytes(UTF8_CHARACTER_SET));
            dataOutputStream.flush();

        } catch (IOException e) {
            // POST送信エラー
            DTVTLogger.debug(e);
            mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SERVER_ERROR);
        } finally {
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException e1) {
                    DTVTLogger.debug(e1);
                    mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SERVER_ERROR);
                }
            }
        }
    }

    /**
     * 各ストリームを閉じる.
     *
     * @param stream            コネクションから取得したストリーム
     * @param inputStreamReader UTF-8を指定したストリーム
     * @param bufferedReader    バッファーストリーム
     */
    private void streamCloser(final InputStream stream,
                              final InputStreamReader inputStreamReader,
                              final BufferedReader bufferedReader) {

        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                //クローズ失敗はサーバー
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SERVER_ERROR);
                DTVTLogger.debug(e);
            }
        }
        if (inputStreamReader != null) {
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                //クローズ失敗はサーバーエラー
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SERVER_ERROR);
                DTVTLogger.debug(e);
            }
        }
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                //クローズ失敗はサーバーエラー
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SERVER_ERROR);
                DTVTLogger.debug(e);
            }
        }
    }

    /**
     * リクエストメソッド取得.
     * POSTリクエストしない場合はオーバライドしてメソッド名を返却する事.
     *
     * @return メソッド名
     */
    String getRequestMethod() {
        return REQUEST_METHOD_POST;
    }

    /**
     * 通信本体のクラス.
     */
    public class CommunicationTask extends AsyncTask<Object, Object, ReturnCode> {
        /**
         * 実行するAPIの名前.
         */
        final String mSourceUrl;
        /**
         * 送るパラメータ.
         */
        final String mSendParameter;
        /**
         * 拡張データ.
         */
        Bundle mExtraData = null;
        /**
         * ワンタイムトークンの取得の有無.
         */
        private boolean mIsUseOtt = false;
        /**
         * ワンタイムトークンの値.
         */
        private String mOneTimeToken = "";
        /**
         * ワンタイムトークン取得のスイッチ.
         */
        private boolean oneTimeTokenGetSwitch = false;
        /**
         * リクエストに利用するHTTPメソッド.
         */
        private String mRequestMethod = REQUEST_METHOD_POST;

        /**
         * ワンタイムトークンの値を設定する.
         *
         * @param oneTimeToken 設定したいワンタイムトークン
         */
        public void setOneTimeToken(final String oneTimeToken) {
            mOneTimeToken = oneTimeToken;
        }

        /**
         * コンストラクタ.
         *
         * @param sourceUrl          実行するAPIの名前
         * @param receivedParameters 送るパラメータ
         * @param requestMethod      HTTPリクエストメソッド
         */
        CommunicationTask(final String sourceUrl, final String receivedParameters, final String requestMethod) {
            mSourceUrl = sourceUrl;
            mSendParameter = receivedParameters;

            //拡張データとワンタイムトークンは使用しない
            mExtraData = null;
            mIsUseOtt = false;
            oneTimeTokenGetSwitch = false;
            mRequestMethod = requestMethod;
        }

        /**
         * コンストラクタ(拡張情報付き).
         *
         * @param sourceUrl          実行するAPIの名前
         * @param receivedParameters 送るパラメータ
         * @param extraDataSrc       受け渡す拡張情報
         * @param requestMethod      HTTPリクエストメソッド
         */
        CommunicationTask(final String sourceUrl, final String receivedParameters,
                          final Bundle extraDataSrc, final String requestMethod) {
            mSourceUrl = sourceUrl;
            mSendParameter = receivedParameters;

            //拡張データの確保
            mExtraData = extraDataSrc;

            //ワンタイムトークンは使用しない
            mIsUseOtt = false;
            oneTimeTokenGetSwitch = false;
            mRequestMethod = requestMethod;
        }

        /**
         * コンストラクタ（ワンタイムトークを使用する場合）.
         *
         * @param sourceUrl          実行するAPIの名前
         * @param receivedParameters 送るパラメータ
         * @param extraDataSrc       受け渡す拡張情報
         * @param isGetOtt           ワンタイムトークンの使用可否
         * @param requestMethod      HTTPリクエストメソッド
         */
        CommunicationTask(final String sourceUrl, final String receivedParameters,
                          final Bundle extraDataSrc, final boolean isGetOtt, final String requestMethod) {
            mSourceUrl = sourceUrl;
            mSendParameter = receivedParameters;

            if (extraDataSrc != null) {
                //拡張データの確保
                mExtraData = extraDataSrc;
            } else {
                mExtraData = null;
            }

            //ワンタイムトークンの使用可否
            mIsUseOtt = isGetOtt;
            oneTimeTokenGetSwitch = false;
            mRequestMethod = requestMethod;
        }

        /**
         * dアカウント認証要求・認可専用コンストラクタ.
         * このAPIは使用方法が全く違うため、専用の動作とする
         *
         * @param receivedParameters ワンタイムパスワードを含むパラメータ
         */
        CommunicationTask(final String receivedParameters) {
            mSourceUrl = UrlConstants.WebApiUrl.ONE_TIME_TOKEN_GET_URL;
            mSendParameter = receivedParameters;

            //拡張データとワンタイムトークンは使用しない
            mExtraData = null;
            mIsUseOtt = false;

            //ワンタイムトークン取得時スイッチをONにする
            oneTimeTokenGetSwitch = true;
            mRequestMethod = REQUEST_METHOD_POST;
        }

        /**
         * 通信本体処理.
         *
         * @param strings 不使用
         * @return 不使用
         */
        @Override
        protected ReturnCode doInBackground(final Object... strings) {
            if (isCancelled() || mIsStop) {
                return null;
            }

            //圏外等の判定
            if (mContext == null
                    || (mContext != null && !NetWorkUtils.isOnline(mContext))) {
                //そもそも通信のできない状態なので、ネットワークエラーとする
                mReturnCode.errorState.setErrorType(
                        DtvtConstants.ErrorType.NETWORK_ERROR);

                //以下の処理は行わずに帰る
                return mReturnCode;
            }

            //クッキー管理の初期化
            mCookieManager = new CookieManager();
            mCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(mCookieManager);

            //サービストークン取得は専用の処理に移行した。
            // その為、以降の処理からもサービストークン取得用の処理は取り除いた。
            if (oneTimeTokenGetSwitch) {
                DTVTLogger.debug("first Url=" + mSourceUrl);
                DTVTLogger.debug("first param=" + mSendParameter);
                gotoRedirect(mSourceUrl, mSendParameter);
                return mReturnCode;
            }

            //タイムアウト判定用
            boolean isTimeout = false;
            long startTime = System.currentTimeMillis();

            try {
                //指定された名前でURLを作成する
                URL url = new URL(mSourceUrl);

                //指定された名前で開く
                mUrlConnection = (HttpURLConnection) url.openConnection();

                //事前設定パラメータのセット
                setParameters(mUrlConnection);

                //コンテキストがあればSSL証明書失効チェックを行う
                if (mContext != null) {
                    DTVTLogger.debug(mSourceUrl);
                    //SSL証明書失効チェックライブラリの初期化を行う
                    OcspUtil.init(mContext);

                    //通信開始時にSSL証明書失効チェックを併せて行う
                    OcspURLConnection ocspURLConnection = new OcspURLConnection(mUrlConnection);
                    ocspURLConnection.connect();
                } else {
                    DTVTLogger.debug("SSL check error");
                    //将来はSSL専用になるので、コンテキストが無くて証明書チェックが行えないならばエラーとする
                    //SSLチェック初期化失敗の例外を投げる
                    throw new SSLPeerUnverifiedException(NO_CONTEXT_ERROR);
                }

                //パラメータを渡す
                if (mRequestMethod.equals(REQUEST_METHOD_POST)) {
                    setPostData(mUrlConnection);
                }
                //結果を読み込む
                int statusCode = mUrlConnection.getResponseCode();
                mAnswerBuffer = readConnectionBody(statusCode);

                //通信の切断と切断リストからの削除
                mUrlConnection.disconnect();
                removeConnections(mUrlConnection);
            } catch (ConnectException e) {
                DTVTLogger.warning("ConnectException");
                //通信エラー扱いとする
                mReturnCode.errorState.setErrorType(
                        DtvtConstants.ErrorType.NETWORK_ERROR);

                //コネクト時のタイムアウトはこちらに来るので、判定する
                if (System.currentTimeMillis() - startTime
                        > DtvtConstants.SERVER_CONNECT_TIMEOUT) {
                    //エラーメッセージにタイムアウトを示唆する物があればタイムアウトとして扱う
                    isTimeout = true;
                    DTVTLogger.debug("timeout ConnectException:" + e.getMessage());
                } else {
                    isTimeout = false;
                    DTVTLogger.debug("normal ConnectException" + e.getMessage());
                }
                //タイムアウトエラーの設定
                mReturnCode.errorState.setIsTimeout(isTimeout);
            } catch (SSLHandshakeException e) {
                DTVTLogger.warning("SSLHandshakeException");
                //SSL証明書が失効している
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SSL_ERROR);
                DTVTLogger.debug(e);
            } catch (SSLPeerUnverifiedException e) {
                DTVTLogger.warning("SSLPeerUnverifiedException");
                //SSLチェックライブラリの初期化が行われていない
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SSL_ERROR);
                DTVTLogger.debug(e);
            } catch (SocketTimeoutException e) {
                //タイムアウトを明示して取得
                DTVTLogger.warning("SocketTimeoutException");
                DTVTLogger.debug("SocketTimeoutException normal ConnectException:"
                        + e.getMessage());
                //サーバーエラー扱いとする
                mReturnCode.errorState.setErrorType(
                        DtvtConstants.ErrorType.SERVER_ERROR);
                //タイムアウト判定用
                mReturnCode.errorState.setIsTimeout(true);
                DTVTLogger.debug(e);
            } catch (IOException e) {
                DTVTLogger.warning("IOException");
                //サーバーエラー扱いとする
                mReturnCode.errorState.setErrorType(
                        DtvtConstants.ErrorType.SERVER_ERROR);
                DTVTLogger.debug(e);
            } catch (OcspParameterException e) {
                //SSLチェックの初期化に失敗している・通常は発生しないとの事
                DTVTLogger.warning("OcspParameterException");
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SSL_ERROR);
                DTVTLogger.debug(e);
            } finally {
                //最後なので初期化
                mUrlConnection = null;
            }

            DTVTLogger.end();
            return mReturnCode;
        }

        /**
         * 通信終了後の処理.
         *
         * @param returnCode 結果格納構造体
         */
        @Override
        protected void onPostExecute(final ReturnCode returnCode) {
            DTVTLogger.start();
            if (returnCode == null || isCancelled()) {
                return;
            }

            //拡張情報があればそれも伝える
            if (mExtraData != null) {
                returnCode.extraData = mExtraData;
            }

            //呼び出し元に伝える情報を判断する
            switch (returnCode.errorState.getErrorType()) {
                //通信自体は成功している場合
                case SUCCESS:
                    if (mAnswerBuffer.isEmpty()) {
                        //結果の値が無いので、失敗を伝える
                        returnCode.errorState.setErrorType(DtvtConstants.ErrorType.HTTP_ERROR);
                        mWebApiBasePlalaCallback.onError(returnCode);
                    } else {
                        //通信に成功したので、値を伝える
                        // **FindBugs** Bad practice FindBugsは不使用なのでbodyDataは消せと警告するが、
                        // コールバック先では使用するため対応しない
                        returnCode.bodyData = mAnswerBuffer;

                        //エラーコードをセットする
                        if (returnCode.errorState.setErrorCode(mAnswerBuffer)) {
                            //エラーコードが存在したので、HTTPエラーとする
                            returnCode.errorState.setErrorType(
                                    DtvtConstants.ErrorType.HTTP_ERROR);
                        }

                        DTVTLogger.debug("onPostExecute answer = " + returnCode);
                        mWebApiBasePlalaCallback.onAnswer(returnCode);
                    }
                    break;

                //通信自体が失敗している場合
                case NETWORK_ERROR:
                case SERVER_ERROR:
                case TOKEN_ERROR:
                case SSL_ERROR:
                case HTTP_ERROR:
                    //その他のエラーなので、呼び出し元にはエラーを伝える
                    mWebApiBasePlalaCallback.onError(returnCode);
                    break;
            }

            //次のワンタイムトークンの取得を許可する
            if (mGetOtt != null) {
                mGetOtt.allowNext(mContext);
            }

            DTVTLogger.end();
        }

        /**
         * HTTPリクエスト用のパラメータを指定する.
         *
         * @param urlConnection コネクション
         * @throws ProtocolException プロトコルエクセプション
         */
        void setParameters(final HttpURLConnection urlConnection) throws ProtocolException {
            //送る文字列長の算出
            byte[] sendParameterByte = mSendParameter.getBytes(StandardCharsets.UTF_8);
            int sendParameterLength = sendParameterByte.length;

            //ワンタイムトークンに内容があれば、セットする
            if (mIsUseOtt && !mOneTimeToken.isEmpty()) {
                DTVTLogger.debug("set token = [" + mOneTimeToken + "]");
                //ワンタイムトークンをセット
                urlConnection.addRequestProperty(ONE_TIME_TOKEN_KEY, mOneTimeToken);
            }

            //コンテントタイプを指定する
            urlConnection.setRequestProperty(CONTENT_TYPE_KEY_TEXT, CONTENT_TYPE_TEXT);

            //ジャンルID、ロールIDはファイルDLのためGETメソッドリクエストする
            //POSTでJSONを送ることを宣言
            urlConnection.setRequestMethod(mRequestMethod);
            if (mRequestMethod.equals(REQUEST_METHOD_POST)) {
                urlConnection.setDoOutput(true);
            }
            urlConnection.setDoInput(true);
            urlConnection.setFixedLengthStreamingMode(sendParameterLength);

            //タイムアウト指定(0以下ならば、デフォルト値の30秒とする)
            if (mTimeOut > 0) {
                urlConnection.setConnectTimeout(mTimeOut);
                urlConnection.setReadTimeout(mTimeOut);
            } else {
                //接続タイムアウト
                urlConnection.setConnectTimeout(DtvtConstants.SERVER_CONNECT_TIMEOUT);
                //読み込みタイムアウト
                urlConnection.setReadTimeout(DtvtConstants.SERVER_READ_TIMEOUT);
            }
        }

        /**
         * パラメータをストリームに書き込む.
         *
         * @param urlConnection 書き込み対象のコネクション
         */
        void setPostData(final HttpURLConnection urlConnection) {
            if (urlConnection == null) {
                return;
            }
            // POSTデータ送信処理
            DataOutputStream dataOutputStream = null;
            try {
                dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                DTVTLogger.debug(String.format("RequestMethod[%s] mSendParameter[%s] size[%s]",
                        urlConnection.getRequestMethod(), mSendParameter, mSendParameter.length()));
                dataOutputStream.write(mSendParameter.getBytes(UTF8_CHARACTER_SET));
                dataOutputStream.flush();
            } catch (IOException e) {
                // POST送信エラー
                DTVTLogger.debug(e);
                //result="POST送信エラー";
            } finally {
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e1) {
                        DTVTLogger.debug(e1);
                    }
                }
            }
        }

        /**
         * サービストークン取得用のリダイレクト処理.
         *
         * @param newUrlString 飛び先URL
         * @param parameter    使用するパラメータ・使用しない場合はヌルか空文字
         */
        private void gotoRedirect(final String newUrlString, final String parameter) {
            DTVTLogger.start();

            HttpsURLConnection httpsConnection = null;

            //タイムアウト判定用
            boolean isTimeout = false;
            long startTime = System.currentTimeMillis();

            try {
                //指定された名前であらたなコネクションを開く
                httpsConnection = (HttpsURLConnection) new URL(newUrlString).openConnection();
                httpsConnection.setDoInput(true);

                //接続タイムアウト
                httpsConnection.setConnectTimeout(DtvtConstants.SERVER_CONNECT_TIMEOUT);
                //読み込みタイムアウト
                httpsConnection.setReadTimeout(DtvtConstants.SERVER_READ_TIMEOUT);

                //DTVTLogger.debug("newHeader=" + httpsConnection.getHeaderFields().toString());
                //コンテントタイプの設定
                httpsConnection.setRequestProperty(CONTENT_TYPE_KEY_TEXT,
                        ONE_TIME_TOKEN_GET_CONTENT_TYPE);
                if (TextUtils.isEmpty(parameter)) {
                    //パラメータをgetで送る
                    httpsConnection.setRequestMethod(REQUEST_METHOD_GET);
                } else {
                    httpsConnection.setDoOutput(true);
                    //送る文字列長の算出
                    byte[] sendParameterByte = parameter.getBytes(StandardCharsets.UTF_8);
                    int sendParameterLength = sendParameterByte.length;
                    httpsConnection.setFixedLengthStreamingMode(sendParameterLength);

                    //パラメータをpostで送る
                    setHttpsPostData(httpsConnection, parameter);
                    httpsConnection.setRequestMethod(getRequestMethod());
                }

                //自動リダイレクトを有効化する
                HttpsURLConnection.setDefaultAllowUserInteraction(true);
                httpsConnection.setInstanceFollowRedirects(true);

                //SSL失効チェック
                checkSsl(httpsConnection);

                //ステータスを取得する
                int status = httpsConnection.getResponseCode();
                DTVTLogger.debug("status=" + status);

                //新たな飛び先を取得する
                String newUrl = httpsConnection.getHeaderField(REDIRECT_JUMP_URL_GET);

                DTVTLogger.debug("newUrl=" + newUrl);

                //リダイレクトのステータスを判定
                if (isRedirectCode(status)) {
                    if (newUrl.contains(ServiceTokenClient.getOkUrlString())
                            || newUrl.contains(ServiceTokenClient.getNgUrlString())) {
                        //リダイレクトで、ロケーションがOKかNGに指定したURLならば、サービストークン取得処理へ遷移
                        getServiceTokenAnswer(newUrl);
                        //コネクションを閉じる
                        httpsConnection.disconnect();
                    } else {
                        //リダイレクトかつ、OKとNGのURLではないならば、取得したURLで再度呼び出し
                        gotoRedirect(newUrl, "");
                        httpsConnection.disconnect();
                    }
                }

            } catch (SSLHandshakeException e) {
                //SSL証明書が失効している
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SSL_ERROR);
                DTVTLogger.debug(e);
            } catch (SSLPeerUnverifiedException e) {
                //SSLチェックライブラリの初期化が行われていない
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SSL_ERROR);
                DTVTLogger.debug(e);
            } catch (ConnectException e) {
                DTVTLogger.warning("ConnectException");
                //通信エラー扱いとする
                mReturnCode.errorState.setErrorType(
                        DtvtConstants.ErrorType.NETWORK_ERROR);

                //コネクト時のタイムアウトはこちらに来るので、判定する
                if (System.currentTimeMillis() - startTime
                        > DtvtConstants.SERVER_CONNECT_TIMEOUT) {
                    //エラーメッセージにタイムアウトを示唆する物があればタイムアウトとして扱う
                    isTimeout = true;
                    DTVTLogger.debug("timeout ConnectException:" + e.getMessage());
                } else {
                    isTimeout = false;
                    DTVTLogger.debug("normal ConnectException" + e.getMessage());
                }
                //タイムアウトエラーの設定
                mReturnCode.errorState.setIsTimeout(isTimeout);
            } catch (SocketTimeoutException e) {
                //タイムアウトを明示して取得
                DTVTLogger.warning("SocketTimeoutException:" + e.getMessage());
                //サーバーエラー扱いとする
                mReturnCode.errorState.setErrorType(
                        DtvtConstants.ErrorType.SERVER_ERROR);
                //タイムアウト判定用
                mReturnCode.errorState.setIsTimeout(true);
                DTVTLogger.debug(e);
            } catch (IOException e) {
                DTVTLogger.debug(e);
                //エラーコードを設定
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SERVER_ERROR);
            } catch (OcspParameterException e) {
                //SSLチェックの初期化に失敗している・通常は発生しないとの事
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.SSL_ERROR);
                DTVTLogger.debug(e);
            } finally {
                //通信の切断処理
                if (httpsConnection != null) {
                    httpsConnection.disconnect();
                }
                //ヌルを入れてガベージコレクションの動作を助ける
                httpsConnection = null;
            }
        }

        /**
         * SSL失効チェック.
         *
         * @param httpsConnection HTTPSコネクション
         * @throws OcspParameterException SSL証明書失効例外
         * @throws IOException            IO例外
         */
        private void checkSsl(final HttpsURLConnection httpsConnection) throws OcspParameterException, IOException {
            //コンテキストがあればSSL証明書失効チェックを行う
            if (mContext != null) {
                DTVTLogger.debug(httpsConnection.getURL().toString());
                //SSL証明書失効チェックライブラリの初期化を行う
                OcspUtil.init(mContext);

                //通信開始時にSSL証明書失効チェックを併せて行う
                OcspURLConnection ocspURLConnection = new OcspURLConnection(httpsConnection);
                ocspURLConnection.connect();
                DTVTLogger.debug("SSL checked");
            } else {
                //既にSSL専用なので、コンテキストが無くて証明書チェックが行えないならばエラーとする
                //SSLチェック初期化失敗の例外を投げる
                DTVTLogger.debug("SSL NO CONTEXT ERROR");
                throw new SSLPeerUnverifiedException(NO_CONTEXT_ERROR);
            }

            DTVTLogger.debug("header=" + httpsConnection.getHeaderFields().toString());

        }

        /**
         * 返ってきたロケーションが最初にこちらで指定したURLだった場合の処理.
         *
         * @param location 取得したロケーション
         */
        private void getServiceTokenAnswer(final String location) {
            //クッキー情報のバッファ
            String serviceToken = "";
            long serviceTokenMaxAge = 0;

            //事前に判定しないと、なぜかfalse時の動作が不正だった
            boolean locationCheck = location.contains(ServiceTokenClient.getOkUrlString());

            //OK URLが含まれているかどうか
            if (locationCheck && mCookieManager != null) {
                //正常にサービストークンが取得できた場合はクッキーを取得
                CookieStore cookieStore = mCookieManager.getCookieStore();
                List<HttpCookie> cookies = cookieStore.getCookies();

                //取得したクッキーの数だけ回る
                for (HttpCookie cookie : cookies) {
                    //サービストークンを見つける
                    if (cookie.getName().equals(SERVICE_TOKEN_KEY_NAME)) {
                        OneTimeTokenData oneTimeTokenData = new OneTimeTokenData();

                        //見つけたので蓄積
                        serviceToken = cookie.getValue();
                        serviceTokenMaxAge = cookie.getMaxAge();

                        //取得した時間は生存秒数なので、ミリ秒の無効化予定時間を算出する
                        serviceTokenMaxAge = (serviceTokenMaxAge * 1000)
                                + DateUtils.getNowTimeFormatEpoch();

                        oneTimeTokenData.setOneTimeToken(serviceToken);
                        oneTimeTokenData.setOneTimeTokenGetTime(serviceTokenMaxAge);

                        //プリファレンスに書き込み
                        SharedPreferencesUtils.setOneTimeTokenData(mContext, oneTimeTokenData);
                        break;
                    }
                }

                if (TextUtils.isEmpty(serviceToken)) {
                    //サービストークンは見つからなかったので、トークンエラーとする
                    mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.TOKEN_ERROR);
                } else {
                    //サービストークン取得では使用しないが、データを格納しなければエラー扱いになるので、トークンを入れておく
                    mAnswerBuffer = serviceToken;
                }
            } else {
                //トークン取得以前の問題なので、通信エラーとする
                mReturnCode.errorState.setErrorType(DtvtConstants.ErrorType.HTTP_ERROR);
            }
        }
    }

    /**
     * パラメータの比較用などの為に、与えられた文字列をひとまとめにする.
     *
     * @param strings ひとまとめにしたい文字列
     * @return ひとまとめになった文字列
     */
    List<String> makeStringArry(final String... strings) {
        return Arrays.asList(strings);
    }

    /**
     * 文字列の日付判定.
     *
     * @param dateString 日付(yyyyMMdd)であることが期待される文字列
     * @return 日付ならばtrue
     */
    boolean checkDateString(final String dateString) {
        //日付フォーマットの設定
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.JAPAN);
        dateFormat.setLenient(false);
        Date parsedDate;
        try {
            //日付変換
            parsedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            //例外が発生したならば、当然日付ではない
            return false;
        }

        //通常の問題では例外が発生するはずだが、念のため日付変換の前後で値を比べて、違っていた場合は不正な値とみなす。
        String temporaryDateString = dateFormat.format(parsedDate);
        return temporaryDateString.equals(dateString);
    }

}
