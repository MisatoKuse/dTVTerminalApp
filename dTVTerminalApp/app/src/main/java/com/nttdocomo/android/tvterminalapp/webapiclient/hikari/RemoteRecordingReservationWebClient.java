/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.struct.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RemoteRecordingReservationJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * リモート録画予約登録WebClient.
 */
public class RemoteRecordingReservationWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック.
     */
    public interface RemoteRecordingReservationJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param remoteRecordingReservationResultResponse JSONパース後のデータ
         */
        void onRemoteRecordingReservationJsonParsed(
                RemoteRecordingReservationResultResponse remoteRecordingReservationResultResponse);
    }

    /**
     * コールバックのインスタンス.
     */
    private RemoteRecordingReservationJsonParserCallback
            mRemoteRecordingReservationJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public RemoteRecordingReservationWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        DTVTLogger.debug("Client onAnswer");
        if (mRemoteRecordingReservationJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new RemoteRecordingReservationJsonParser(
                    mRemoteRecordingReservationJsonParserCallback)
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
        DTVTLogger.debug("Client onError");
        if (mRemoteRecordingReservationJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mRemoteRecordingReservationJsonParserCallback
                    .onRemoteRecordingReservationJsonParsed(null);
        }
    }

    /**
     * リモート録画予約.
     *
     * @param contentsDetailInfo                           リモート録画予約送信用パラメータ
     * @param remoteRecordingReservationJsonParserCallback コールバック
     * @return パラメータエラーの場合はfalse
     */
    public boolean getRemoteRecordingReservationApi(final RecordingReservationContentsDetailInfo contentsDetailInfo,
                                                    final RemoteRecordingReservationJsonParserCallback
                                                            remoteRecordingReservationJsonParserCallback) {
        //パラメーターのチェック
        if (!checkNormalParameter(contentsDetailInfo, remoteRecordingReservationJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            DTVTLogger.debug("Parameter Error");
            return false;
        }

        //コールバックのセット
        mRemoteRecordingReservationJsonParserCallback =
                remoteRecordingReservationJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(contentsDetailInfo);

        //録画一覧の情報を読み込むため、録画一覧APIを呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.REMOTE_RECORDING_RESERVATION_CLIENT,
                sendParameter, this, null);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param contentsDetailInfo                           リモート録画予約送信用パラメータ
     * @param remoteRecordingReservationJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final RecordingReservationContentsDetailInfo contentsDetailInfo,
                                         final RemoteRecordingReservationJsonParserCallback
                                                 remoteRecordingReservationJsonParserCallback) {
        // サービスIDが null ならばfalse
        if (contentsDetailInfo.getServiceId() == null) {
            DTVTLogger.debug("ServiceId:error");
            return false;
        }
        // 定期予約指定値が 0 かつイベントIDがない場合false
        if (contentsDetailInfo.getLoopTypeNum() == 0
                && contentsDetailInfo.getEventId() == null) {
            DTVTLogger.debug("EventId:error");
            return false;
        }
        // 番組タイトルが設定されていない場合はfalse
        if (contentsDetailInfo.getTitle() == null) {
            DTVTLogger.debug("Title:error");
            return false;
        }
        // 開始時間は0以上が有効なので負数の場合はfalse
        if (contentsDetailInfo.getStartTime() < 0) {
            DTVTLogger.debug("StartTime:error");
            return false;
        }
        // 予約時間の長さは0以上が有効なので負数の場合はfalse
        if (contentsDetailInfo.getDuration() < 0) {
            DTVTLogger.debug("Duration:error");
            return false;
        }
        // 番組のパレンタル設定値が設定されていない場合はfalse
        if (contentsDetailInfo.getRValue() == null) {
            DTVTLogger.debug("R_Value:error");
            return false;
        }
        //コールバックが指定されていないならばfalse
        if (remoteRecordingReservationJsonParserCallback == null) {
            DTVTLogger.debug("setCallback:error");
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }


    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param contentsDetailInfo リモート録画予約送信用パラメータ
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final RecordingReservationContentsDetailInfo contentsDetailInfo) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            jsonObject.put(JsonConstants.META_RESPONSE_PLATFORM_TYPE, contentsDetailInfo.getPlatformType());
            jsonObject.put(JsonConstants.META_RESPONSE_SERVICE_ID, contentsDetailInfo.getServiceId());
            //地テジ・BSの場合設定しない
            if (contentsDetailInfo.getPlatformType() == 1 && contentsDetailInfo.getEventId() != null) {
                jsonObject.put(JsonConstants.META_RESPONSE_EVENT_ID, contentsDetailInfo.getEventId());
            }
            jsonObject.put(JsonConstants.META_RESPONSE_TITLE, contentsDetailInfo.getTitle());
            jsonObject.put(JsonConstants.META_RESPONSE_START_TIME, contentsDetailInfo.getStartTime());
            jsonObject.put(JsonConstants.META_RESPONSE_DURATION, contentsDetailInfo.getDuration());
            jsonObject.put(JsonConstants.META_RESPONSE_LOOP_TYPE_NUM, contentsDetailInfo.getLoopTypeNum());
            jsonObject.put(JsonConstants.META_RESPONSE_R_VALUE, contentsDetailInfo.getRValue());
            jsonObject.put(JsonConstants.META_RESPONSE_RESV_TYPE, contentsDetailInfo.getResvType());

            answerText = jsonObject.toString();
        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }
        DTVTLogger.debug(answerText);
        return answerText;
    }
}
