/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.nttdocomo.android.ocsplib.OcspURLConnection;
import com.nttdocomo.android.ocsplib.OcspUtil;
import com.nttdocomo.android.ocsplib.exception.OcspParameterException;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOTT;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
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

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * 通信処理.
 */
public class WebApiBasePlala implements DaccountGetOTT.DaccountGetOttCallBack {
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
     * リクエスト種別・基本はPOST.
     */
    private static final String REQUEST_METHOD = "POST";
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
    private CommunicationTask mCommunicationTask = null;
    /**
     * 通信停止用フラグ.
     */
    private boolean mIsStop = false;
    /**
     * TODO: 当面ワンタイムトークンは固定値とするので、その値.
     */
    private static final String INTERIM_ONE_TIME_TOKEN = "test";
    /**
     * 日付形式判定用.
     */
    private static final String DATE_PATTERN = "yyyyMMdd";
    /**
     * データ受け渡しコールバック.
     */
    interface WebApiBasePlalaCallback {
        /**
         * 正常終了時のコールバック.
         *
         * @param returnCode 値を返す構造体
         */
        void onAnswer(ReturnCode returnCode);

        /**
         * 通信失敗時のコールバック.
         *
         * @param returnCode 値を返す構造体
         */
        void onError(ReturnCode returnCode);
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
    public static final String FILTER_TESTA = "testa";
    /**
     * フィルター用指定文字列・demo.
     */
    public static final String FILTER_DEMO = "demo";

    /**
     * フィルター用指定文字列・フィルターのパラメータ名.
     */
    public static final String FILTER_PARAM = "filter";

    /**
     * タイプ用指定文字列・ｄCh.
     */
    public static final String TYPE_D_CHANNEL = "dch";
    /**
     * タイプ用指定文字列・ひかりTV.
     */
    public static final String TYPE_HIKARI_TV = "hikaritv";
    /**
     * タイプ用指定文字列・ひかりTVのVOD.
     */
    public static final String TYPE_HIKARI_TV_VOD = "hikaritv_vod";
    /**
     * タイプ用指定文字列・dTVのVOD.
     */
    public static final String TYPE_DTV_VOD = "dtv_vod";
    /**
     * タイプ用指定文字列・ひかりTVのVODとdTVのVOD.
     */
    public static final String TYPE_HIKARI_TV_AND_DTV_VOD = "hikaritv_and_dtv_vod";
    /**
     * タイプ用指定文字列・全て（指定なしは全てになる）.
     */
    public static final String TYPE_ALL = "";

    /**
     * 日付指定文字列・現在時刻指定.
     */
    public static final String DATE_NOW = "now";

    /**
     * ソート用文字列・タイトルルビ昇順.
     */
    public static final String SORT_TITLE_RUBY_ASC = "titleruby_asc";
    /**
     * ソート用文字列・配信開始日昇順.
     */
    public static final String SORT_AVAIL_S_ASC = "avail_s_asc";
    /**
     * ソート用文字列・配信終了日降順.
     */
    public static final String SORT_AVAIL_E_DESC = "avail_e_desc";
    /**
     * ソート用文字列・人気順（前日の視聴回数数降順）.
     */
    public static final String SORT_PLAY_COUNT_DESC = "play_count_desc";

    /**
     * age_req(年齢設定値)の最小値.
     */
    public static final int AGE_LOW_VALUE = 1;

    /**
     * age_req(年齢設定値)の最大値.
     */
    public static final int AGE_HIGH_VALUE = 17;

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
    protected static final String MY_CHANNEL_ADULT_TYPE_EMPTY = "";

    /**
     * チャンネルのパレンタル設定値(G).
     */
    public static final String MY_CHANNEL_R_VALUE_G = "G";

    /**
     * チャンネルのパレンタル設定値(PG-12).
     */
    protected static final String MY_CHANNEL_R_VALUE_PG_12 = "PG-12";

    /**
     * チャンネルのパレンタル設定値(R-15).
     */
    protected static final String MY_CHANNEL_R_VALUE_PG_15 = "R-15";

    /**
     * チャンネルのパレンタル設定値(R-18).
     */
    protected static final String MY_CHANNEL_R_VALUE_PG_18 = "R-18";

    /**
     * チャンネルのパレンタル設定値(R-20).
     */
    protected static final String MY_CHANNEL_R_VALUE_PG_20 = "R-20";

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
    public static final String LIST_STRING = "list";

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
     * ワンタイムトークンのキー名.
     */
    private static final String ONE_TIME_TOKEN_KEY = "x-service-token";

    //戻り値用構造体
    static protected class ReturnCode {
        /**
         * 通信時エラー情報.
         */
        DTVTConstants.ERROR_TYPE errorType;
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
            errorType = DTVTConstants.ERROR_TYPE.SUCCESS;
            bodyData = "";
            extraData = null;
        }
    }

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public WebApiBasePlala(final Context context) {
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
     * TODO:実装予定のすべての通信を遮断するAPIで使用の予定
     */
    synchronized void stopAllConnections() {
        //各通信タスクにキャンセルを通知する
        mIsStop = true;
        if (mCommunicationTaskAPI != null) {
            mCommunicationTaskAPI.cancel(true);
        }
        if (mCommunicationTaskExtra != null) {
            mCommunicationTaskExtra.cancel(true);
        }
        if (mCommunicationTask != null) {
            mCommunicationTask.cancel(true);
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
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    stopConnection.disconnect();
                }
            });
            thread.run();

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
     * 指定したAPIで通信を開始する.
     *
     * @param sourceUrl               API呼び出し名
     * @param receivedParameters      API呼び出し用パラメータ
     * @param webApiBasePlalaCallback コールバック
     */
    public void openUrl(final String sourceUrl, final String receivedParameters,
                        final WebApiBasePlalaCallback webApiBasePlalaCallback) {
        mCommunicationTaskAPI = new CommunicationTask(sourceUrl, receivedParameters);

        //コールバックの準備
        mWebApiBasePlalaCallback = webApiBasePlalaCallback;

        //結果格納構造体の作成
        ReturnCode returnCode = new ReturnCode();

        //通信本体の開始
        mCommunicationTaskAPI.execute(returnCode);
    }

    /**
     * 指定したAPIで通信を開始する(拡張情報付き).
     *
     * @param sourceUrl               API呼び出し名
     * @param receivedParameters      API呼び出し用パラメータ
     * @param webApiBasePlalaCallback 結果のコールバック
     * @param extraDataSrc            拡張情報
     */
    public void openUrlWithExtraData(final String sourceUrl, final String receivedParameters,
                                     final WebApiBasePlalaCallback webApiBasePlalaCallback,
                                     final Bundle extraDataSrc) {
        //拡張情報もセットする
        mCommunicationTaskExtra = new CommunicationTask(sourceUrl,
                receivedParameters, extraDataSrc);

        //コールバックの準備
        mWebApiBasePlalaCallback = webApiBasePlalaCallback;

        //結果格納構造体の作成
        ReturnCode returnCode = new ReturnCode();

        //通信本体の開始
        mCommunicationTaskExtra.execute(returnCode);
    }

    /**
     * 指定したAPIをワンタイムトークン付きで通信を開始する.
     *
     * @param sourceUrl               API呼び出し名
     * @param receivedParameters      API呼び出し用パラメータ
     * @param webApiBasePlalaCallback 結果のコールバック
     * @param extraDataSrc            拡張情報（使用しないときはヌルをセット）
     */
    public void openUrlAddOtt(final String sourceUrl, final String receivedParameters,
                              final WebApiBasePlalaCallback webApiBasePlalaCallback,
                              final Bundle extraDataSrc) {
        //タスクを作成する
        mCommunicationTask = new CommunicationTask(sourceUrl, receivedParameters,
                extraDataSrc, true);

        //コールバックの準備
        mWebApiBasePlalaCallback = webApiBasePlalaCallback;

        //ワンタイムパスワードの取得を起動
        getOneTimePassword(mContext);
    }

    /**
     * ワンタイムトークンを取得する為に、dアカウント設定アプリからワンタイムパスワードを取得する.
     *
     * @param context コンテキスト
     */
    private void getOneTimePassword(final Context context) {
        //ワンタイムパスワードの取得
        DaccountGetOTT getOtt = new DaccountGetOTT();
        getOtt.execDaccountGetOTT(context, this);
    }

    @Override
    public void getOttCallBack(final int result, final String id, final String oneTimePassword) {
        //ワンタイムパスワードを元に、ワンタイムトークンを取得する.
        //TODO: 本来、取得したワンタイムパスワードを元にしてワンタイムトークン取得のWebAPIを呼ばねばならない。
        //TODO: しかしこれは別タスクになった。現在はワンタームトークンは固定値にする
        mCommunicationTask.setmOneTimeToken(INTERIM_ONE_TIME_TOKEN);

        //結果格納構造体の作成
        ReturnCode returnCode = new ReturnCode();

        //ワンタイムトークンの取得結果を元にして、通信を開始する
        mCommunicationTask.execute(returnCode);
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
            mReturnCode.errorType = DTVTConstants.ERROR_TYPE.HTTP_ERROR;
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
            //HTTP通信エラーとして元に返す
            mReturnCode.errorType = DTVTConstants.ERROR_TYPE.OTHER_ERROR;
            DTVTLogger.debug(e);
        } catch (IOException e) {
            //全通信停止発行済みならば、正常な動作となる
            if (!mIsStopAllConnections) {
                //通信停止ではないので、通信エラー
                mReturnCode.errorType = DTVTConstants.ERROR_TYPE.OTHER_ERROR;
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

        return bodyData;
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
                //クローズ失敗は通信エラー
                mReturnCode.errorType = DTVTConstants.ERROR_TYPE.OTHER_ERROR;
                DTVTLogger.debug(e);
            }
        }
        if (inputStreamReader != null) {
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                //クローズ失敗は通信エラー
                mReturnCode.errorType = DTVTConstants.ERROR_TYPE.OTHER_ERROR;
                DTVTLogger.debug(e);
            }
        }
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                //クローズ失敗は通信エラー
                mReturnCode.errorType = DTVTConstants.ERROR_TYPE.OTHER_ERROR;
                DTVTLogger.debug(e);
            }
        }
    }

    /**
     * 通信本体のクラス.
     */
    private class CommunicationTask extends AsyncTask<Object, Object, ReturnCode> {
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
        private boolean mIsGetOtt = false;
        /**
         * ワンタイムトークンの値.
         */
        private String mOneTimeToken = "";
        /**
         * ワンタイムトークンの値を設定する.
         *
         * @param mOneTimeToken 設定したいワンタイムトークン
         */
        public void setmOneTimeToken(final String mOneTimeToken) {
            this.mOneTimeToken = mOneTimeToken;
        }

        /**
         * コンストラクタ
         *
         * @param sourceUrl          実行するAPIの名前
         * @param receivedParameters 送るパラメータ
         */
        CommunicationTask(final String sourceUrl, final String receivedParameters) {
            mSourceUrl = sourceUrl;
            mSendParameter = receivedParameters;

            //拡張データとワンタイムトークンは使用しない
            mExtraData = null;
            mIsGetOtt = false;
        }

        /**
         * コンストラクタ(拡張情報付き).
         *
         * @param sourceUrl          実行するAPIの名前
         * @param receivedParameters 送るパラメータ
         * @param extraDataSrc       受け渡す拡張情報
         */
        CommunicationTask(final String sourceUrl, final String receivedParameters,
                          final Bundle extraDataSrc) {
            mSourceUrl = sourceUrl;
            mSendParameter = receivedParameters;

            //拡張データの確保
            mExtraData = extraDataSrc;

            //ワンタイムトークンは使用しない
            mIsGetOtt = false;
        }

        /**
         * コンストラクタ（ワンタイムトークを使用する場合）.
         *
         * @param sourceUrl          実行するAPIの名前
         * @param receivedParameters 送るパラメータ
         * @param extraDataSrc       受け渡す拡張情報
         * @param isGetOtt           ワンタイムトークンの使用可否
         */
        CommunicationTask(final String sourceUrl, final String receivedParameters,
                          final Bundle extraDataSrc, final boolean isGetOtt) {
            mSourceUrl = sourceUrl;
            mSendParameter = receivedParameters;

            if (extraDataSrc != null) {
                //拡張データの確保
                mExtraData = extraDataSrc;
            } else {
                mExtraData = null;
            }

            //ワンタイムトークンの使用可否
            mIsGetOtt = isGetOtt;
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
                    //SSL失効チェックライブラリは動かせないので、既存の通信開始処理
                    mUrlConnection.connect();
                }

                //パラメータを渡す
                setPostData(mUrlConnection);

                //結果を読み込む
                int statusCode = mUrlConnection.getResponseCode();
                mAnswerBuffer = readConnectionBody(statusCode);

                //通信の切断と切断リストからの削除
                mUrlConnection.disconnect();
                removeConnections(mUrlConnection);
            } catch (ConnectException e) {
                //通信エラー扱いとする
                mReturnCode.errorType = DTVTConstants.ERROR_TYPE.COMMUNICATION_ERROR;
            } catch (SSLHandshakeException e) {
                //SSL証明書が失効している
                mReturnCode.errorType = DTVTConstants.ERROR_TYPE.SSL_ERROR;
                DTVTLogger.debug(e);
            } catch (SSLPeerUnverifiedException e) {
                //SSLチェックライブラリの初期化が行われていない
                mReturnCode.errorType = DTVTConstants.ERROR_TYPE.SSL_ERROR;
                DTVTLogger.debug(e);
            } catch (IOException e) {
                //通信エラー扱いとする
                mReturnCode.errorType = DTVTConstants.ERROR_TYPE.COMMUNICATION_ERROR;
                DTVTLogger.debug(e);
            } catch (OcspParameterException e) {
                //SSLチェックの初期化に失敗している・通常は発生しないとの事
                mReturnCode.errorType = DTVTConstants.ERROR_TYPE.SSL_ERROR;
                DTVTLogger.debug(e);
            } finally {
                //最後なので初期化
                mUrlConnection = null;
            }

            return mReturnCode;
        }

        /**
         * 通信終了後の処理.
         *
         * @param returnCode 結果格納構造体
         */
        @Override
        protected void onPostExecute(final ReturnCode returnCode) {
            //拡張情報があればそれも伝える
            if (mExtraData != null) {
                returnCode.extraData = mExtraData;
            }

            //呼び出し元に伝える情報を判断する
            switch (returnCode.errorType) {
                case SUCCESS:
                    if (mAnswerBuffer.isEmpty()) {
                        //結果の値が無いので、失敗を伝える
                        mWebApiBasePlalaCallback.onError(returnCode);
                    } else {
                        //通信に成功したので、値を伝える
                        // **FindBugs** Bad practice FindBugsは不使用なのでbodyDataは消せと警告するが、
                        // コールバック先では使用するため対応しない
                        returnCode.bodyData = mAnswerBuffer;
                        mWebApiBasePlalaCallback.onAnswer(returnCode);
                    }
                    break;
                case SSL_ERROR:
                    //SSLチェックライブラリのエラーなので、呼び出し元にエラーを伝える
                    mWebApiBasePlalaCallback.onError(returnCode);
                    break;
                case HTTP_ERROR:
                case COMMUNICATION_ERROR:
                case OTHER_ERROR:
                case ANALYSIS_ERROR:
                case NO_DATA:
                default:
                    //その他のエラーなので、呼び出し元にはエラーを伝える
                    mWebApiBasePlalaCallback.onError(returnCode);
                    break;
            }
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
            if (mIsGetOtt && !mOneTimeToken.isEmpty()) {
                //ワンタイムトークンをセット
                urlConnection.addRequestProperty(ONE_TIME_TOKEN_KEY, mOneTimeToken);
            }

            //POSTでJSONを送ることを宣言
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setFixedLengthStreamingMode(sendParameterLength);
            urlConnection.setRequestProperty(CONTENT_TYPE_KEY_TEXT, CONTENT_TYPE_TEXT);

            mUrlConnection.setRequestProperty(CONNECTION_KEY_TEXT, CONTENT_CLOSE_TEXT);
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