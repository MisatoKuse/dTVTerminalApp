/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.os.AsyncTask;
import android.os.Bundle;

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
import java.util.List;
import java.util.Locale;

public class WebApiBasePlala {
    /**
     * データ受け渡しコールバック
     */
    interface WebApiBasePlalaCallback {
        /**
         * 正常終了時のコールバック
         * @param returnCode 値を返す構造体
         */
        void onAnswer(ReturnCode returnCode);

        /**
         * 異常時のコールバック
         */
        void onError();
    }

    /**
     * コールバックのインスタンス
     */
    private WebApiBasePlalaCallback mWebApiBasePlalaCallback;


    //仮のベースURL
    //TODO: 本物のサーバーが提供されるまでは、テストサーバーのアドレスとを指定する
    private static final String baseUrl = "http://192.168.2.224/";
    //private static final String baseUrl = "http://192.168.2.127/";

    //通信停止用コネクション蓄積
    private volatile static List<HttpURLConnection> mUrlConnections = null;

    //全部止まった場合のフラグ
    private static boolean mIsStopAllConnections = false;

    //コネクション
    private HttpURLConnection mUrlConnection = null;

    //結果を受け取るバッファ
    private String mAnswerBuffer = "";

    //リクエスト種別・基本はPOST
    private static final String REQUEST_METHOD = "POST";

    /**
     * API選択表・API名を指定すると、実際に呼び出すときの名前を取得できる
     * TODO: 本物のサーバーが提供されるまでは、テストサーバーの名前を指定する
     */
    protected enum API_NAME_LIST {
        /**
         * VODクリップ一覧の呼び出し先
         */
        VOD_CLIP_LIST("vod_clip/list"),

        /**
         * 視聴中ビデオ一覧
         *
         */
        WATCH_LISTEN_VIDEO_LIST("viewingvideo/list"),

        /**
         * TVクリップ一覧の呼び出し先
         */
        TV_CLIP_LIST("tv_clip/list"),

        /**
         * チャンネル一覧の呼び出し先
         */
        CHANNEL_LIST("channel/list"),

        /**
         * チャンネル毎番組一覧の呼び出し先
         */
        TV_SCHEDULE_LIST("channel/program/get"),

        /**
         * 日毎ランク一覧の呼び出し先
         */
        DAILY_RANK_LIST("dayclip/count/programranking/get"),

        /**
         * 週毎ランク一覧の呼び出し先
         * TODO: 現状の仕様では使わない想定
         */
        WEEKLY_RANK_LIST("weekclip/count/programranking/get"),

        /**
         * ジャンル毎コンテンツ数
         */
        CONTENTS_NUMBER_PER_GENRE_WEB_CLIENT("genre/contents/count/get"),

        /**
         * ジャンル毎コンテンツ一覧
         */
        CONTENTS_LIST_PER_GENRE_WEB_CLIENT("genre/contents/list"),

        /**
         * 購入済みVOD一覧取得(レンタルビデオ用)
         */
        RENTAL_VOD_LIST_WEB_CLIENT("purchasedvod/list"),

        ;   //最後にセミコロンが必要

        //呼び出し先名の控え
        private final String apiName;

        /**
         * 指定されたAPIの呼び出し先を決定する
         * @param text API名
         */
        API_NAME_LIST(final String text) {
            this.apiName = text;
        }

        /**
         * 呼び出し先を聞かれた場合に値を返す
         * @return 呼び出し先名
         */
        public String getString() {
            return this.apiName;
        }
    }
    /**
     * 内部エラー情報
     */
    protected enum ERROR_TYPE {
        /**
         * 成功
         */
        SUCCESS,
        /**
         * 通信エラー
         */
        COMMUNICATION_ERROR,
        /**
         * HTTP通信エラー
         */
        HTTP_ERROR,

        /**
         * データなし
         */
        NO_DATA,

        /**
         * その他エラー
         */
        OTHER_ERROR,
    }


    //指定文字列パラメータ群
    //対外的なパラメータなので、現在は非使用の物にもpublicが必要になる。
    /**
     * フィルター用指定文字列・release
     */
    public static final String FILTER_RELEASE = "release";
    /**
     * フィルター用指定文字列・testa
     */
    public static final String FILTER_TESTA = "testa";
    /**
     * フィルター用指定文字列・demo
     */
    public static final String FILTER_DEMO = "demo";

    /**
     * タイプ用指定文字列・ｄCh
     */
    public static final String TYPE_D_CHANNEL = "dch";
    /**
     * タイプ用指定文字列・ひかりTV
     */
    public static final String TYPE_HIKARI_TV = "hikaritv";
    /**
     * タイプ用指定文字列・ひかりTVのVOD
     */
    public static final String TYPE_HIKARI_TV_VOD = "hikaritv_vod";
    /**
     * タイプ用指定文字列・dTVのVOD
     */
    public static final String TYPE_DTV_VOD = "dtv_vod";
    /**
     * タイプ用指定文字列・ひかりTVのVODとdTVのVOD
     */
    public static final String TYPE_HIKARI_TV_AND_DTV_VOD = "hikaritv_and_dtv_vod";
    /**
     * タイプ用指定文字列・全て（指定なしは全てになる）
     */
    public static final String TYPE_ALL = "";

    /**
     * 日付指定文字列・現在時刻指定
     */
    public static final String DATE_NOW = "now";

    /**
     * ソート用文字列・タイトルルビ昇順
     */
    public static final String SORT_TITLE_RUBY_ASC = "titleruby_asc";
    /**
     * ソート用文字列・配信開始日昇順
     */
    public static final String SORT_AVAIL_S_ASC = "avail_s_asc";
    /**
     * ソート用文字列・配信終了日降順
     */
    public static final String SORT_AVAIL_E_DESC = "avail_e_desc";
    /**
     * ソート用文字列・人気順（前日の視聴回数数降順）
     */
    public static final String SORT_PLAY_COUNT_DESC = "play_count_desc";

    //戻り値用構造体
    static protected class ReturnCode {
        ERROR_TYPE errorType;
        String bodyData;
        Bundle extraData;

        /**
         * コンストラクタ
         */
        ReturnCode() {
            errorType = ERROR_TYPE.SUCCESS;
            bodyData = "";
            extraData = null;
        }
    }
    private ReturnCode mReturnCode = null;

    /**
     * コンストラクタ
     */
    public WebApiBasePlala() {
        //コネクション蓄積が存在しなければ作成する
        if(mUrlConnections == null) {
            mUrlConnections = new ArrayList<>();
        }

        //戻り値の準備
        mReturnCode = new ReturnCode();
    }

    /**
     * コネクションを蓄積して、後で止められるようにする
     * @param mUrlConnection コネクション
     */
    private void addUrlConnections(HttpURLConnection mUrlConnection) {
        //通信が終わり、ヌルが入れられる場合に備えたヌルチェック
        if(mUrlConnections == null) {
            //既に削除されていたので、再度確保を行う
            mUrlConnections = new ArrayList<>();
        }

        //HTTPコネクションを追加する
        mUrlConnections.add(mUrlConnection);
    }

    /**
     * 全ての通信を遮断する
     * TODO:実装予定のすべての通信を遮断するAPIで使用の予定
     */
    static public void stopAllConnections() {
        if(mUrlConnections == null) {
            return;
        }

        //全ての通信を止めることを宣言する
        mIsStopAllConnections = true;

        //全てのコネクションにdisconnectを送る
        for(HttpURLConnection stopConnection: mUrlConnections) {
            stopConnection.disconnect();

            //止めた物は消す
            removeConnections(stopConnection);
        }
    }

    /**
     *  切断済みコネクションを蓄積から削除する
     * @param connection 削除したいコネクション
     */
    static private void removeConnections(HttpURLConnection connection) {
        if (mUrlConnections != null) {

            mUrlConnections.remove(connection);

            //すべて削除済みならばヌルにする
            if (mUrlConnections.size() == 0) {
                mUrlConnections = null;
            }
        }
    }

    /**
     * 指定したAPIで通信を開始する
     * @param sourceUrl                 API呼び出し名
     * @param webApiBasePlalaCallback コールバック
     */
    public void openUrl(final String sourceUrl,String receivedParameters,
                        WebApiBasePlalaCallback webApiBasePlalaCallback) {
        CommunicationTask communicationTask = new CommunicationTask(sourceUrl,receivedParameters);

        //コールバックの準備
        mWebApiBasePlalaCallback = webApiBasePlalaCallback;

        //結果格納構造体の作成
        ReturnCode returnCode = new ReturnCode();

        //通信本体の開始
        communicationTask.execute(returnCode);
    }

    /**
     * 指定したAPIで通信を開始する(拡張情報付き)
     * @param sourceUrl API呼び出し名
     * @param receivedParameters API呼び出し用パラメータ
     * @param webApiBasePlalaCallback 結果のコールバック
     * @param extraDataSrc 拡張情報
     */
    public void openUrlWithExtraData(final String sourceUrl,String receivedParameters,
                                     WebApiBasePlalaCallback webApiBasePlalaCallback,Bundle extraDataSrc) {
        //拡張情報もセットする
        CommunicationTask communicationTask = new CommunicationTask(sourceUrl,
                receivedParameters,extraDataSrc);

        //コールバックの準備
        mWebApiBasePlalaCallback = webApiBasePlalaCallback;

        //結果格納構造体の作成
        ReturnCode returnCode = new ReturnCode();

        //通信本体の開始
        communicationTask.execute(returnCode);
    }

    /**
     * ボディ部の読み込みを行う
     * @param statusCode コネクションの際のステータス
     * @return 読み込んだボディ部
     */
    private String readConnectionBody(int statusCode) {
        if(statusCode != HttpURLConnection.HTTP_OK) {
            //HTTP通信エラーとして元に返す
            mReturnCode.errorType = ERROR_TYPE.HTTP_ERROR;
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
            inputStreamReader = new InputStreamReader(stream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);

            //内容が尽きるまで蓄積する
            while ((lineBuffer = bufferedReader.readLine()) != null) {
                stringBuilder.append(lineBuffer);
                stringBuilder.append("\n");
            }

        } catch (UnsupportedEncodingException e) {
            //HTTP通信エラーとして元に返す
            mReturnCode.errorType = ERROR_TYPE.OTHER_ERROR;
            e.printStackTrace();
        } catch (IOException e) {
            //全通信停止発行済みならば、正常な動作となる
            if(!mIsStopAllConnections) {
                //通信停止ではないので、通信エラー
                mReturnCode.errorType = ERROR_TYPE.OTHER_ERROR;
                e.printStackTrace();
            }
        } finally {
            //ストリームを閉じる
            streamCloser(stream,inputStreamReader,bufferedReader);
        }

        //蓄積したボディー部を返す
        String bodyData = "";
        if(stringBuilder != null) {
            bodyData = stringBuilder.toString();
        }

        return bodyData;
    }

    /**
     * 各ストリームを閉じる
     * @param stream             コネクションから取得したストリーム
     * @param inputStreamReader UTF-8を指定したストリーム
     * @param bufferedReader    バッファーストリーム
     */
    private void streamCloser(InputStream stream,
                              InputStreamReader inputStreamReader,
                              BufferedReader bufferedReader) {

        if(stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                //クローズ失敗は通信エラー
                mReturnCode.errorType = ERROR_TYPE.OTHER_ERROR;
                e.printStackTrace();
            }
        }
        if(inputStreamReader != null) {
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                //クローズ失敗は通信エラー
                mReturnCode.errorType = ERROR_TYPE.OTHER_ERROR;
                e.printStackTrace();
            }
        }
        if(bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                //クローズ失敗は通信エラー
                mReturnCode.errorType = ERROR_TYPE.OTHER_ERROR;
                e.printStackTrace();
            }
        }
    }

    /**
     * 通信本体のクラス
     */
    private class CommunicationTask extends AsyncTask<Object, Object, ReturnCode> {
        //実行するAPIの名前
        final String mSourceUrl;

        //送るパラメータ
        final String mSendParameter;

        //拡張データ
        Bundle mExtraData = null;

        /**
         * コンストラクタ
         * @param sourceUrl             実行するAPIの名前
         * @param receivedParameters   送るパラメータ
         */
        CommunicationTask(String sourceUrl,String receivedParameters) {
            mSourceUrl = sourceUrl;
            mSendParameter =  receivedParameters;
        }

        /**
         * コンストラクタ(拡張情報付き)
         * @param sourceUrl 実行するAPIの名前
         * @param receivedParameters 送るパラメータ
         * @param extraDataSrc 受け渡す拡張情報
         */
        CommunicationTask(String sourceUrl,String receivedParameters,Bundle extraDataSrc) {
            mSourceUrl = sourceUrl;
            mSendParameter =  receivedParameters;

            //拡張データの確保
            mExtraData = extraDataSrc;
        }

        /**
         * 通信本体処理
         * @param strings 不使用
         * @return 不使用
         */
        @Override
        protected ReturnCode doInBackground(Object... strings) {
            try {
                //ベースURLとAPIの名前を組み合わせてURLとして開く
                URL url = new URL(baseUrl + mSourceUrl);
                mUrlConnection = (HttpURLConnection) url.openConnection();

                //事前設定パラメータのセット
                setParameters(mUrlConnection);

                //通信開始
                mUrlConnection.connect();

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
                mReturnCode.errorType = ERROR_TYPE.COMMUNICATION_ERROR;
            } catch (IOException e) {
                //通信エラー扱いとする
                mReturnCode.errorType = ERROR_TYPE.COMMUNICATION_ERROR;
                e.printStackTrace();
            } finally {
                //最後なので初期化
                mUrlConnection = null;
            }

            return mReturnCode;
        }

        /**
         * 通信終了後の処理
         * @param returnCode 結果格納構造体
         */
        @Override
        protected void onPostExecute(ReturnCode returnCode) {
            //拡張情報があればそれも伝える
            if(mExtraData != null) {
                returnCode.extraData = mExtraData;
            }

            //呼び出し元に伝える情報を判断する
            if(returnCode.errorType == ERROR_TYPE.SUCCESS) {
                if(mAnswerBuffer.isEmpty()) {
                    //エラーが無いので、失敗を伝える
                    mWebApiBasePlalaCallback.onError();
                } else {
                    //通信に成功したので、値を伝える
                    // **FindBugs** Bad practice FindBugsは不使用なのでbodyDataは消せと警告するが、
                    // コールバック先では使用するため対応しない
                    returnCode.bodyData = mAnswerBuffer;
                    mWebApiBasePlalaCallback.onAnswer(returnCode);
                }
            } else {
                //エラーがあったので、失敗を伝える
                mWebApiBasePlalaCallback.onError();
            }
        }

        /**
         * HTTPリクエスト用のパラメータを指定する
         * @param urlConnection コネクション
         */
        void setParameters(HttpURLConnection urlConnection) throws ProtocolException {
            //送る文字列長の算出
            byte[] sendParameterByte = mSendParameter.getBytes(StandardCharsets.UTF_8);
            int sendParameterLength = sendParameterByte.length;

            //POSTでJSONを送ることを宣言
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setFixedLengthStreamingMode(sendParameterLength);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            mUrlConnection.setRequestProperty("Connection", "close");
        }

        /**
         * パラメータをストリームに書き込む
         * @param urlConnection 書き込み対象のコネクション
         */
        void setPostData(HttpURLConnection urlConnection) {
            if(urlConnection == null) {
                return;
            }
            // POSTデータ送信処理
            DataOutputStream dataOutputStream = null;
            try {
                dataOutputStream =  new DataOutputStream(urlConnection.getOutputStream());
                dataOutputStream.write( mSendParameter.getBytes("UTF-8") );
                dataOutputStream.flush();

            } catch (IOException e) {
                // POST送信エラー
                e.printStackTrace();
                //result="POST送信エラー";
            } finally {
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * パラメータの比較用などの為に、与えられた文字列をひとまとめにする
     * @param strings ひとまとめにしたい文字列
     * @return ひとまとめになった文字列
     */
    List<String> makeStringArry(String... strings) {
        return  Arrays.asList(strings);
    }

    /**
     * 文字列の日付判定
     * @param dateString 日付(yyyyMMdd)であることが期待される文字列
     * @return 日付ならばtrue
     */
    boolean checkDateString(String dateString) {
        //日付フォーマットの設定
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.JAPAN);
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