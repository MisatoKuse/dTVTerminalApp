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
import com.nttdocomo.android.tvterminalapp.struct.OneTimeTokenData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOTT;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ServiceTokenClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

public class HttpThread extends Thread implements DaccountGetOTT.DaccountGetOttCallBack {

    private Handler mHandler = null;
    private String mUrl = null;
    private String mXmlStr = "";
    private HttpThreadFinish mHttpThreadFinish = null;
    private boolean mError = false;

    //SSL証明書チェック用コンテキスト
    private Context mContext = null;

    //サービストークン情報
    private OneTimeTokenData mOneTimeTokenData = null;

    //エラー時ステータスの構造体
    public static class ErrorStatus {
        //エラーコード
        DTVTConstants.ERROR_TYPE errType;
        //メッセージ
        String message;

        /**
         * コンストラクタ.
         */
        public ErrorStatus() {
            //内容の初期化
            errType = DTVTConstants.ERROR_TYPE.SUCCESS;
            message = "";
        }
    }

    //エラーコード構造体の作成
    private ErrorStatus mErrorStatus;


    public interface HttpThreadFinish {
        /**
         * 終了コールバック.
         *
         * @param str 読み込んだ情報
         * @param errorStatus エラー情報構造体
         */
        public void onHttpThreadFinish(String str, ErrorStatus errorStatus);
    }

    private void setError(boolean b) {
        synchronized (this) {
            mError = b;
        }
    }

    /**
     * HTTP通信スレッドのコンストラクタ.
     *
     * @param url              URL
     * @param httpThreadFinish 終了コールバック
     * @param context          コンテキスト
     */
    public HttpThread(String url, HttpThreadFinish httpThreadFinish, Context context) {
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
     */
    public HttpThread(String url, Handler handler, HttpThreadFinish httpThreadFinish,
                      Context context) {
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
    private void commonContractor(String url, HttpThreadFinish httpThreadFinish, Context context) {
        //各パラメータの退避
        mUrl = url;
        mHttpThreadFinish = httpThreadFinish;
        mContext = context;

        //エラー情報構造体の宣言
        mErrorStatus = new ErrorStatus();
    }

    private void clearStatus() {
        mXmlStr = "";
        setError(false);
    }

    @Override
    public void run() {
        //先行して、サービストークンの取得を行うため、期限内かどうかをみる
        mOneTimeTokenData = SharedPreferencesUtils.getOneTimeTokenData(mContext);

        //期限内ならば、そのトークンを使用するので、そのまま実行を行う
        if(mOneTimeTokenData.getOneTimeTokenGetTime() > DateUtils.getNowTimeFormatEpoch()) {
            runEntity();
            return;
        }

        //dアカウントのワンタイムパスワードの取得を行う
        DaccountGetOTT getOtt = new DaccountGetOTT();
        getOtt.execDaccountGetOTT(mContext, this);

        //以後の処理はワンタイムパスワード取得後のコールバックで行う
    }


    @Override
    public void getOttCallBack(int result, String id, String oneTimePassword) {
        if(result != 0) {
            //パスワードの取得に失敗したので、サービストークンは取得できない。次に進む
            runEntity();
            return;
        }

        //サービストークンを取得してから実行を行う
        ServiceTokenClient serviceTokenClient = new ServiceTokenClient(mContext);
        serviceTokenClient.getServiceTokenApi(oneTimePassword,
                new ServiceTokenClient.TokenGetCallback() {
            @Override
            public void onTokenGot(boolean success) {
                //成功していれば、トークンはプリファレンスに入っているので、読み込む
                mOneTimeTokenData = SharedPreferencesUtils.getOneTimeTokenData(mContext);

                //次へ進む
                runEntity();
                return;
            }
        });
    }

    /**
     * 通信処理の実体.
     */
    private void runEntity() {
        clearStatus();
        HttpsURLConnection httpUrlConn = null;
        final StringBuffer sb = new StringBuffer();
        String str = null;
        BufferedReader br = null;

        try {
            URL url = new URL(this.mUrl);
            httpUrlConn = (HttpsURLConnection) url.openConnection();

            httpUrlConn.setReadTimeout(DTVTConstants.SEARCH_SERVER_TIMEOUT);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("Accept-Charset", "utf-8");
            httpUrlConn.setRequestProperty("contentType", "utf-8");

            //サービストークンを付加する
            addToken(httpUrlConn);

            //コンテキストがあればSSL証明書失効チェックを行う
            if (mContext != null) {
                DTVTLogger.debug(mUrl);

                //SSL証明書失効チェックライブラリの初期化を行う
                OcspUtil.init(mContext);

                //SSL証明書失効チェックを行う
                OcspURLConnection ocspURLConnection = new OcspURLConnection(httpUrlConn);
                ocspURLConnection.connect();
            }

            if (httpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream is = httpUrlConn.getInputStream();
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
            if(br != null) {
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
     * 取得できていれば、サービストークンを追加する.
     *
     * @param httpsUrlConnection
     */
    private void addToken(HttpsURLConnection httpsUrlConnection) {
        if(!TextUtils.isEmpty(mOneTimeTokenData.getOneTimeToken())) {
            DTVTLogger.debug("set token = " + mOneTimeTokenData.getOneTimeToken());
            //サービストークンが格納されていれば、それを使用する
            httpsUrlConnection.setRequestProperty(WebApiBasePlala.ONE_TIME_TOKEN_KEY,
                    mOneTimeTokenData.getOneTimeToken());

        }
    }

    /**
     * エラー情報設定.
     *
     * @param e 例外情報
     * @param errorType エラーコード
     */
    private void setErrorStatus(Exception e, DTVTConstants.ERROR_TYPE errorType) {
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
    private void finishSelect(final StringBuffer stringBuffer) {
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
}