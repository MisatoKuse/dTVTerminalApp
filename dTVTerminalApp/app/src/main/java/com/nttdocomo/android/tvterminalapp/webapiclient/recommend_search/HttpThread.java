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
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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
     * ワンタイムパスワード
     */
    private String mOneTimePassword;

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
    private synchronized void commonContractor(final String url, final HttpThreadFinish httpThreadFinish,
                                               final Context context) {
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
    }

    @Override
    public void run() {
        clearStatus();
        final StringBuffer sb = new StringBuffer();
        String str;
        BufferedReader br = null;

        try {
            //必要ならばURLにパスワード認証を付加する
            String srcUrl = addUrlPassword(this.mUrl);
            URL url = new URL(srcUrl);

            mHttpUrlConn = (HttpsURLConnection) url.openConnection();

            mHttpUrlConn.setReadTimeout(DTVTConstants.SEARCH_SERVER_TIMEOUT);
            mHttpUrlConn.setRequestMethod("GET");
            mHttpUrlConn.setRequestProperty("Accept-Charset", "utf-8");
            mHttpUrlConn.setRequestProperty("contentType", "utf-8");

            //コンテキストがあればSSL証明書失効チェックを行う
            if (mContext != null) {
                DTVTLogger.debug(srcUrl);

                //SSL証明書失効チェックライブラリの初期化を行う
                OcspUtil.init(mContext);

                //SSL証明書失効チェックを行う
                OcspURLConnection ocspURLConnection = new OcspURLConnection(mHttpUrlConn);
                ocspURLConnection.connect();
            }

            if (mHttpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream is = mHttpUrlConn.getInputStream();
                if (null == is) {
                    throw new IOException("HttpThread::run, is==null");
                }

                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                if (null == isr) {
                    throw new IOException("HttpThread::run, isr==null");
                }

                br = new BufferedReader(isr);
                if (null == br) {
                    throw new IOException("HttpThread::run, br==null");
                }

                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
            }
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
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    //その他通信エラー処理
                    setErrorStatus(e, DTVTConstants.ERROR_TYPE.COMMUNICATION_ERROR);
                }
            }
        }

        //ハンドラーの有無でコールバックの返し方を変える
        finishSelect(sb);
    }

    /**
     * ワンタイムパスワードの認証を経由する為、指定されたURLをリダイレクト先にして、URLを構築する.
     *
     * @param srcUrl 元のURL
     * @return ワンタイムパスワードの認証付加後のURL
     */
    private String addUrlPassword(String srcUrl) {
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
                encodedUrl = URLEncoder.encode(srcUrl, "UTF-8");
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
     * エラー情報設定.
     *
     * @param e         例外情報
     * @param errorType エラーコード
     */
    private synchronized void setErrorStatus(final Exception e, final DTVTConstants.ERROR_TYPE errorType) {
        //例外種類のログ出力
        DTVTLogger.debug(e);

        //エラーステータスの指定
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
    public void disconnect() {
        DTVTLogger.start();
        if (mHttpUrlConn != null) {
            mHttpUrlConn.disconnect();
            finishSelect(new StringBuffer(""));
        }
    }
}