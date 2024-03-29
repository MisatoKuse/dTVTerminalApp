/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RecordingReservationListJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 録画予約リスト取得用Webクライアント.
 */
public class RecordingReservationListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 実際の値の下限.
     */
    private static final int LOWER_LIMIT = 1;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;


    /**
     * コールバック.
     */
    public interface RecordingReservationListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param RecordingReservationListResponse JSONパース後のデータ
         */
        void onRecordingReservationListJsonParsed(
                RecordingReservationListResponse RecordingReservationListResponse);
    }

    /**
     * コールバックのインスタンス.
     */
    private RecordingReservationListJsonParserCallback
            mRecordingReservationListJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public RecordingReservationListWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        if (mRecordingReservationListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new RecordingReservationListJsonParser(
                    mRecordingReservationListJsonParserCallback)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode.bodyData);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        if (mRecordingReservationListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mRecordingReservationListJsonParserCallback
                    .onRecordingReservationListJsonParsed(null);
        }
    }

    /**
     * 録画予約一覧取得.
     *
     * @param limit レスポンスの最大件数・ゼロの場合全件とする
     * @param offset 取得位置・ゼロの場合全件とする
     * @param recordingReservationListJsonParserCallback コールバック
     * @return パラメータエラーの場合はfalse
     */
    public boolean getRecordingReservationListApi(final int limit, final int offset,
                                                  final RecordingReservationListJsonParserCallback
                                                          recordingReservationListJsonParserCallback) {
        if (mIsCancel) {
            //通信禁止状態なのでfalseで帰る
            DTVTLogger.error("RecordingReservationListWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック
        if (!checkNormalParameter(limit, offset, recordingReservationListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        mRecordingReservationListJsonParserCallback =
                recordingReservationListJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(limit, offset);

        //録画一覧の情報を読み込むため、録画一覧APIを呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.RECORDING_RESERVATION_LIST_WEB_CLIENT,
                sendParameter, this, null);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param limit                                      レスポンスの最大件数・ゼロの場合全件とする
     * @param offset                                     取得位置・ゼロの場合全件とする
     * @param recordingReservationListJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final int limit, final int offset,
                                         final RecordingReservationListJsonParserCallback
                                                 recordingReservationListJsonParserCallback) {
        //リミットが負数ならばfalse
        if (limit < 0) {
            return false;
        }

        //取得位置が負数ならばfalse
        if (offset < 0) {
            return false;
        }

        //コールバックが指定されていないならばfalse
        if (recordingReservationListJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }


    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param limit  レスポンスの最大件数・ゼロの場合全件とする
     * @param offset 取得位置・ゼロの場合全件とする
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final int limit, final int offset) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //ページャーの中身を作成
            JSONObject pager = new JSONObject();

            //offsetが1以上ならばJSONに書き込む・ゼロの場合は全件指定なので、出力しないことで全件になる
            if (limit >= LOWER_LIMIT) {
                pager.put(JsonConstants.META_RESPONSE_LIST, limit);
            }

            //limitが1以上ならばJSONに書き込む・ゼロの場合は全件指定なので、出力しないことで全件になる
            if (offset >= LOWER_LIMIT) {
                pager.put(JsonConstants.META_RESPONSE_OFFSET, offset);
            }

            //ページャーに値が格納されていれば、出力する
            if (pager.has(JsonConstants.META_RESPONSE_LIST) || pager.has(JsonConstants.META_RESPONSE_OFFSET)) {
                jsonObject.put(JsonConstants.META_RESPONSE_PAGER, pager);
                answerText = jsonObject.toString();
            } else {
                answerText = "{}";
            }

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        DTVTLogger.debugHttp(answerText);
        return answerText;
    }

    /**
     * 通信を止める.
     */
    public void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        stopAllConnections();
    }

    /**
     * 通信を許可する.
     */
    public void enableConnection() {
        mIsCancel = false;
        DTVTLogger.start();
    }
}