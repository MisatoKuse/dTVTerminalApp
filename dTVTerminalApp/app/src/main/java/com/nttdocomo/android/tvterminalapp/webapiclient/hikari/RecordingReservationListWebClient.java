/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RecordingReservationListJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class RecordingReservationListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    //実際の値の下限
    private static final int LOWER_LIMIT = 1;


    /**
     * コールバック
     */
    public interface RecordingReservationListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param RecordingReservationListResponse JSONパース後のデータ
         */
        void onRecordingReservationListJsonParsed(
                RecordingReservationListResponse RecordingReservationListResponse);
    }

    //コールバックのインスタンス
    private RecordingReservationListJsonParserCallback
            mRecordingReservationListJsonParserCallback;

    @Override
    public void onAnswer(ReturnCode returnCode) {
        if (mRecordingReservationListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new RecordingReservationListJsonParser(
                    mRecordingReservationListJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    @Override
    public void onError() {
        if (mRecordingReservationListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mRecordingReservationListJsonParserCallback
                    .onRecordingReservationListJsonParsed(null);
        }
    }

    /**
     * 録画予約一覧取得
     *
     * @param limit レスポンスの最大件数・ゼロの場合全件とする
     * @param offset 取得位置・ゼロの場合全件とする
     * @param recordingReservationListJsonParserCallback コールバック
     * TODO: 仕様確定後に基底クラスへサービストークンの処理の追加が必要
     * @return パラメータエラーの場合はfalse
     */
    public boolean getRecordingReservationListApi(int limit, int offset,
                                                  RecordingReservationListJsonParserCallback
                                                          recordingReservationListJsonParserCallback) {
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
        openUrl(API_NAME_LIST.RECORDING_RESERVATION_LIST_WEB_CLIENT.getString(),
                sendParameter, this);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param limit                                      レスポンスの最大件数・ゼロの場合全件とする
     * @param offset                                     取得位置・ゼロの場合全件とする
     * @param recordingReservationListJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int limit, int offset,
                                         RecordingReservationListJsonParserCallback
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
     * 指定されたパラメータをJSONで組み立てて文字列にする
     *
     * @param limit  レスポンスの最大件数・ゼロの場合全件とする
     * @param offset 取得位置・ゼロの場合全件とする
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int limit, int offset) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //ページャーの中身を作成
            JSONObject pager = new JSONObject();

            //offsetが1以上ならばJSONに書き込む・ゼロの場合は全件指定なので、出力しないことで全件になる
            if (limit >= LOWER_LIMIT) {
                pager.put(JsonContents.META_RESPONSE_LIST, limit);
            }

            //limitが1以上ならばJSONに書き込む・ゼロの場合は全件指定なので、出力しないことで全件になる
            if (offset >= LOWER_LIMIT) {
                pager.put(JsonContents.META_RESPONSE_OFFSET, offset);
            }

            //ページャーに値が格納されていれば、出力する
            if (pager.has(JsonContents.META_RESPONSE_LIST) || pager.has(JsonContents.META_RESPONSE_OFFSET)) {
                jsonObject.put(JsonContents.META_RESPONSE_PAGER, pager);
                answerText = jsonObject.toString();
            } else {
                answerText = "";
            }

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }
}
