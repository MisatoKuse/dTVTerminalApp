/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.ocsplib.OcspURLConnection;
import com.nttdocomo.android.ocsplib.OcspUtil;
import com.nttdocomo.android.ocsplib.exception.OcspParameterException;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * HTTP通信スレッド.
 */
public class HttpThread extends Thread {
    /**
     * 遷移先URLの項目名.
     * 先頭パラメータなので?を付ける
     */
    static private final String DESTINATION_URL_NAME = "?rl=";
    /**
     * ワンタイムパスワードの項目名.
     * 2番目のパラメータなので&を付ける
     */
    static private final String AUTH_OTP_NAME = "&AuthOtp=";

    /**
     * 例外用メッセージ.
     */
    static private final String ERROR_MESSAGE_INPUT_STREAM_NULL =
            "HttpThread::run, inputStream==null";
    static private final String ERROR_MESSAGE_INPUT_STREAM_READER_NULL =
            "HttpThread::run, inputStreamReader==null";
    static private final String ERROR_MESSAGE_BUFFER_READER_NULL =
            "HttpThread::run, bufferedReader==null";
    static private final String ERROR_MESSAGE_HTTP_RESPONSE =
            "HttpThread::run, http response=";

    /**
     * ハンドラー.
     */
    private Handler mHandler = null;
    /**
     * 通信を行うURL.
     */
    private String mUrl = null;
    /**
     * 読み込んだデータを保存.
     */
    private String mXmlStr = "";
    /**
     * 通信終了コールバック.
     */
    private HttpThreadFinish mHttpThreadFinish = null;
    /**
     * 通信の成功/失敗フラグ.
     */
    private boolean mError = false;
    /**
     * SSL証明書チェック用コンテキスト.
     */
    private Context mContext = null;
    /**
     * HTTPURLConnection.
     */
    private HttpsURLConnection mHttpUrlConn;
    /**
     * ワンタイムパスワード.
     */
    private String mOneTimePassword;

    /**
     * クッキーマネージャー.
     */
    private CookieManager mCookieManager;

    /**
     * クッキー退避領域
     */
    private List<HttpCookie> mCookies;

    /**
     * エラー時ステータスの構造体.
     */
    public static class ErrorStatus {
        /**
         * エラーコード.
         */
        DTVTConstants.ERROR_TYPE errType;
        /**
         * メッセージ.
         */
        String message;

        /**
         * コンストラクタ.
         */
        ErrorStatus() {
            //内容の初期化
            errType = DTVTConstants.ERROR_TYPE.SUCCESS;
            //未使用の警告が出るが、アクティビティ側でエラー対応が進めば、将来的には使われるはず。
            message = "";
        }
    }

    /**
     * エラーコード構造体の作成.
     */
    private ErrorStatus mErrorStatus;

    /**
     * HTTP通信終了を通知するインターフェイス.
     */
    public interface HttpThreadFinish {
        /**
         * 終了コールバック.
         *
         * @param str         読み込んだ情報
         * @param errorStatus エラー情報構造体
         */
        void onHttpThreadFinish(String str, ErrorStatus errorStatus);
    }

    /**
     * エラーステータスを設定する.
     *
     * @param bool true:エラー未発生 false:エラー発生
     */
    private void setError(final boolean bool) {
        synchronized (this) {
            mError = bool;
        }
    }

    /**
     * HTTP通信スレッドのコンストラクタ(非同期処理から呼ぶ場合).
     *
     * @param url              URL
     * @param httpThreadFinish 終了コールバック
     * @param context          コンテキスト
     * @param oneTimePassword  ワンタイムパスワード
     */
    public HttpThread(final String url, final HttpThreadFinish httpThreadFinish,
                      final Context context, final String oneTimePassword) {
        //ワンタイムパスワードのセット
        mOneTimePassword = oneTimePassword;

        //コンストラクターの共通処理
        commonContractor(url, httpThreadFinish, context);
        clearStatus();
    }

    /**
     * HTTP通信スレッドのコンストラクタ.
     *
     * @param url              URL
     * @param handler          ハンドラー
     * @param httpThreadFinish 終了コールバック
     * @param context          コンテキスト
     * @param oneTimePassword  ワンタイムパスワード
     */
    public HttpThread(final String url, final Handler handler,
                      final HttpThreadFinish httpThreadFinish, final Context context,
                      final String oneTimePassword) {
        //ワンタイムパスワードのセット
        mOneTimePassword = oneTimePassword;

        //コンストラクターの共通処理
        commonContractor(url, httpThreadFinish, context);
        //ハンドラーの退避
        mHandler = handler;
        clearStatus();
    }

    /**
     * コンストラクターの共通処理.
     *
     * @param url              URL
     * @param httpThreadFinish 終了コールバック
     * @param context          コンテキスト
     */
    private synchronized void commonContractor(
            final String url, final HttpThreadFinish httpThreadFinish, final Context context) {
        //各パラメータの退避
        mUrl = url;
        mHttpThreadFinish = httpThreadFinish;
        mContext = context;

        //エラー情報構造体の宣言
        mErrorStatus = new ErrorStatus();
    }

    /**
     * ステータスの初期化.
     */
    private synchronized void clearStatus() {
        mXmlStr = "";
        setError(false);

        //クッキー管理の初期化
        mCookieManager = new CookieManager();
        mCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(mCookieManager);
    }

    @Override
    public void run() {
        //実際の通信処理を呼び出す
        communicationProcess();
    }

    /**
     * 通信処理の本体.
     *
     * (run()を再起処理で呼びだすと、findBugsで警告されるので分離した)
     */
    private synchronized void communicationProcess() {
        clearStatus();
        StringBuffer stringBuffer = new StringBuffer();

        try {
            //必要ならばURLにパスワード認証を付加する
            String srcUrl = addUrlPassword(this.mUrl);
            URL url = new URL(srcUrl);

            mHttpUrlConn = (HttpsURLConnection) url.openConnection();

            //リダイレクトは時間がかかるようなので、タイムアウト時間は延長する
            mHttpUrlConn.setReadTimeout(DTVTConstants.SEARCH_SERVER_TIMEOUT * 2);
            mHttpUrlConn.setRequestMethod(DTVTConstants.REQUEST_METHOD_GET);
            mHttpUrlConn.setRequestProperty(DTVTConstants.ACCEPT_CHARSET,
                    StandardCharsets.UTF_8.name());
            mHttpUrlConn.setRequestProperty(DTVTConstants.CONTENT_TYPE,
                    StandardCharsets.UTF_8.name());

            //クッキー情報の有無を検査
            if (mCookies != null) {
                //蓄積してあるクッキー情報を書き込む
                CookieStore cookieStore = mCookieManager.getCookieStore();
                for (int counter = 0; counter < mCookies.size(); counter++) {
                    DTVTLogger.debug("Cookie(" + counter + ")=" + mCookies.get(counter));
                    cookieStore.add(null, mCookies.get(counter));
                }

                //次に備えてクッキー情報は初期化する
                mCookies = null;
            }

            //自動リダイレクトを無効化する
            HttpsURLConnection.setDefaultAllowUserInteraction(false);
            mHttpUrlConn.setInstanceFollowRedirects(false);

            //コンテキストがあればSSL証明書失効チェックを行う
            if (mContext != null) {
                DTVTLogger.debug(srcUrl);

                //SSL証明書失効チェックライブラリの初期化を行う
                OcspUtil.init(mContext);

                //SSL証明書失効チェックを行う
                OcspURLConnection ocspURLConnection = new OcspURLConnection(mHttpUrlConn);
                ocspURLConnection.connect();
            }

            //通信後の処理を外に出す
            stringBuffer = afterProcess();

        } catch (SSLHandshakeException e) {
            //SSLエラー処理
            setErrorStatus(e, DTVTConstants.ERROR_TYPE.SSL_ERROR);
        } catch (SSLPeerUnverifiedException e) {
            //SSLエラー処理
            setErrorStatus(e, DTVTConstants.ERROR_TYPE.SSL_ERROR);
        } catch (OcspParameterException e) {
            //SSLエラー処理
            setErrorStatus(e, DTVTConstants.ERROR_TYPE.SSL_ERROR);
        } catch (IOException e) {
            //その他通信エラー処理
            setErrorStatus(e, DTVTConstants.ERROR_TYPE.COMMUNICATION_ERROR);
        }

        //ハンドラーの有無でコールバックの返し方を変える
        finishSelect(stringBuffer);
        DTVTLogger.end("answer = " + stringBuffer.toString());
    }

    /**
     * ワンタイムパスワードの認証を経由する為、指定されたURLをリダイレクト先にして、URLを構築する.
     *
     * @param srcUrl 元のURL
     * @return ワンタイムパスワードの認証付加後のURL
     */
    private String addUrlPassword(final String srcUrl) {
        //クッキーが設定済みならばリダイレクトの処理となる。情報の付加は無用なので、こちらでは何もせずに帰る
        if (mCookies != null) {
            return srcUrl;
        }

        //ワンタイムパスワードが無い場合の為、元のURLをあらかじめセットしておく
        String redirectUrl = srcUrl;

        //ワンタイムパスワードの取得に成功していれば、認証付加を開始する
        if (!TextUtils.isEmpty(mOneTimePassword)) {
            DTVTLogger.debug("set Ott = " + mOneTimePassword);

            //パラメータ編集
            StringBuilder queryItems = new StringBuilder();

            //呼び出し先を認証サイトにする
            queryItems.append(UrlConstants.WebApiUrl.ONE_TIME_PASSWORD_AUTH_URL);

            //本来の呼び出し先はリダイレクト先に設定する為、URLエンコードを行う
            String encodedUrl;
            try {
                DTVTLogger.debug("before decode url=" + srcUrl);
                encodedUrl = URLEncoder.encode(srcUrl, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                encodedUrl = srcUrl;
            }

            // リダイレクト先として設定する(先頭パラメータなので?を付加済み)
            queryItems.append(DESTINATION_URL_NAME);
            queryItems.append(encodedUrl);

            // ワンタイムパスワードの指定(2個目のパラメータなので、&を付加済み)
            queryItems.append(AUTH_OTP_NAME);
            queryItems.append(mOneTimePassword);

            // 作成した物を確定する
            redirectUrl = queryItems.toString();
        }

        return redirectUrl;
    }

    /**
     * 通信後の処理.
     *
     * @return 読み込み結果
     * @throws IOException ファイル操作例外は、呼び出し元で処理を行う
     */
    private StringBuffer afterProcess() throws IOException {
        int status = mHttpUrlConn.getResponseCode();
        BufferedReader bufferedReader = null;
        DTVTLogger.debug("response=" + status);
        StringBuffer stringBuffer = new StringBuffer();

        if (status == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = mHttpUrlConn.getInputStream();
            if (null == inputStream) {
                throw new IOException(ERROR_MESSAGE_INPUT_STREAM_NULL);
            }

            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8.name());
            if (null == inputStreamReader) {
                throw new IOException(ERROR_MESSAGE_INPUT_STREAM_READER_NULL);
            }

            try {
                bufferedReader = new BufferedReader(inputStreamReader);
                if (null == bufferedReader) {
                    throw new IOException(ERROR_MESSAGE_BUFFER_READER_NULL);
                }

                String buffer;
                while ((buffer = bufferedReader.readLine()) != null) {
                    stringBuffer.append(buffer);
                }
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        //その他通信エラー処理
                        setErrorStatus(e, DTVTConstants.ERROR_TYPE.COMMUNICATION_ERROR);
                    }
                }
            }
        } else if (WebApiBasePlala.isRedirectCode(status)) {
            //リダイレクトの場合
            mUrl = mHttpUrlConn.getHeaderField(DTVTConstants.LOCATION_KEY);
            DTVTLogger.debug("Location=" + mUrl);

            //クッキーの退避
            CookieStore cookieStore = mCookieManager.getCookieStore();
            mCookies = cookieStore.getCookies();

            //現在のコネクションを止めて、新たな通信を開始する
            mHttpUrlConn.disconnect();
            mHttpUrlConn = null;
            communicationProcess();
        } else {
            //正常とリダイレクト以外の場合は例外発行とする
            String errMessage = StringUtils.getConnectStrings(ERROR_MESSAGE_HTTP_RESPONSE,
                    String.valueOf(status));
            throw new IOException(errMessage);
        }

        //結果を返す
        return stringBuffer;
    }

    /**
     * エラー情報設定.
     *
     * @param e         例外情報
     * @param errorType エラーコード
     */
    private synchronized void setErrorStatus(final Exception e,
                                             final DTVTConstants.ERROR_TYPE errorType) {
        //例外種類のログ出力
        DTVTLogger.debug(e);

        //エラーステータスの指定（未使用の警告が出るが、アクティビティ側でエラー対応が進めば、将来的には使われるはず）
        mErrorStatus.errType = errorType;

        //エラーステータスON
        setError(true);
    }

    /**
     * 終端処理（メソッドの超過警告避け）.
     *
     * @param stringBuffer 読み込みデータ
     */
    private synchronized void finishSelect(final StringBuffer stringBuffer) {
        if (mHandler != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (null != mHttpThreadFinish) {
                        if (mError) {
                            mXmlStr = "";
                        } else {
                            mXmlStr = stringBuffer.toString();
                        }
                        mHttpThreadFinish.onHttpThreadFinish(mXmlStr, mErrorStatus);
                    }
                }
            });
        } else {
            if (null != mHttpThreadFinish) {
                if (mError) {
                    mXmlStr = "";
                } else {
                    mXmlStr = stringBuffer.toString();
                }
                mHttpThreadFinish.onHttpThreadFinish(mXmlStr, mErrorStatus);
            }
        }
    }

    /**
     * HTTP通信を止める.
     */
    public synchronized void disconnect() {
        DTVTLogger.start();
        if (mHttpUrlConn != null) {
            mHttpUrlConn.disconnect();
            finishSelect(new StringBuffer(""));
        }
    }
}