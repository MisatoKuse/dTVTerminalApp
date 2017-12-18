/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.model.detail.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RemoteRecordingReservationJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class RemoteRecordingReservationClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック
     */
    public interface RemoteRecordingReservationJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param remoteRecordingReservationResultResponse JSONパース後のデータ
         */
        void onRemoteRecordingReservationJsonParsed(
                RemoteRecordingReservationResultResponse remoteRecordingReservationResultResponse);
    }

    //コールバックのインスタンス
    private RemoteRecordingReservationJsonParserCallback
            mRemoteRecordingReservationJsonParserCallback;

    @Override
    public void onAnswer(ReturnCode returnCode) {
        if (mRemoteRecordingReservationJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new RemoteRecordingReservationJsonParser(
                    mRemoteRecordingReservationJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    @Override
    public void onError() {
        if (mRemoteRecordingReservationJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mRemoteRecordingReservationJsonParserCallback
                    .onRemoteRecordingReservationJsonParsed(null);
        }
    }

    /**
     * リモート録画予約
     *
     * @param contentsDetailInfo                           リモート録画予約送信用パラメータ
     * @param remoteRecordingReservationJsonParserCallback コールバック
     *                                                     TODO: 仕様確定後に基底クラスへサービストークンの処理の追加が必要
     * @return パラメータエラーの場合はfalse
     */
    public boolean getRemoteRecordingReservationApi(RecordingReservationContentsDetailInfo contentsDetailInfo,
                                                    RemoteRecordingReservationJsonParserCallback
                                                            remoteRecordingReservationJsonParserCallback) {
        //パラメーターのチェック
        if (!checkNormalParameter(contentsDetailInfo, remoteRecordingReservationJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        mRemoteRecordingReservationJsonParserCallback =
                remoteRecordingReservationJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(contentsDetailInfo);

        //録画一覧の情報を読み込むため、録画一覧APIを呼び出す
        openUrl(UrlConstants.WebApiUrl.REMOTE_RECORDING_RESERVATION_CLIENT,
                sendParameter, this);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param contentsDetailInfo                           リモート録画予約送信用パラメータ
     * @param remoteRecordingReservationJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(RecordingReservationContentsDetailInfo contentsDetailInfo,
                                         RemoteRecordingReservationJsonParserCallback
                                                 remoteRecordingReservationJsonParserCallback) {
        // 放送種別が 1 以外ならばfalse
        if (contentsDetailInfo.getPlatformType() != 1) {
            return false;
        }
        // サービスIDが null ならばfalse
        if (contentsDetailInfo.getServiceId() == null) {
            return false;
        }
        // 定期予約指定値が 0 かつイベントIDがない場合false
        if (contentsDetailInfo.getLoopTypeNum() == 0
                && contentsDetailInfo.getEventId() == null) {
            return false;
        }
        // 番組タイトルが設定されていない場合はfalse
        if (contentsDetailInfo.getTitle() == null) {
            return false;
        }
        // 開始時間が設定されていない場合はfalse
        if (contentsDetailInfo.getStartTime() <= 0) {
            return false;
        }
        // 予約時間の長さが設定されていない場合はfalse
        if (contentsDetailInfo.getDuration() <= 0) {
            return false;
        }
        // 番組のパレンタル設定値が設定されていない場合はfalse
        if (contentsDetailInfo.getRValue() == null) {
            return false;
        }
        //コールバックが指定されていないならばfalse
        if (remoteRecordingReservationJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }


    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     *
     * @param contentsDetailInfo リモート録画予約送信用パラメータ
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(RecordingReservationContentsDetailInfo contentsDetailInfo) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            jsonObject.put(JsonContents.META_RESPONSE_PLATFORM_TYPE, contentsDetailInfo.getPlatformType());
            jsonObject.put(JsonContents.META_RESPONSE_SERVICE_ID, contentsDetailInfo.getServiceId());
            if (contentsDetailInfo.getEventId() != null) {
                jsonObject.put(JsonContents.META_RESPONSE_EVENT_ID, contentsDetailInfo.getEventId());
            }
            jsonObject.put(JsonContents.META_RESPONSE_TITLE, contentsDetailInfo.getTitle());
            jsonObject.put(JsonContents.META_RESPONSE_START_TIME, contentsDetailInfo.getStartTime());
            jsonObject.put(JsonContents.META_RESPONSE_DURATION, contentsDetailInfo.getDuration());
            jsonObject.put(JsonContents.META_RESPONSE_LOOP_TYPE_NUM, contentsDetailInfo.getLoopTypeNum());
            jsonObject.put(JsonContents.META_RESPONSE_R_VALUE, contentsDetailInfo.getRValue());

            answerText = jsonObject.toString();
        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }
}
