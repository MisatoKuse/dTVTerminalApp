/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RemoteRecordingReservationListJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Remoteの録画予約リスト取得Webクライアント.
 */
public class RemoteRecordingReservationListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コールバック.
     */
    public interface RemoteRecordingReservationListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param RemoteRecordingReservationListResponse JSONパース後のデータ
         */
        void onRemoteRecordingReservationListJsonParsed(
                RemoteRecordingReservationListResponse RemoteRecordingReservationListResponse);
    }

    /**
     * コールバックのインスタンス.
     */
    private RemoteRecordingReservationListJsonParserCallback
            mRemoteRecordingReservationListJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public RemoteRecordingReservationListWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        if (mRemoteRecordingReservationListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new RemoteRecordingReservationListJsonParser(
                    mRemoteRecordingReservationListJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        if (mRemoteRecordingReservationListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mRemoteRecordingReservationListJsonParserCallback
                    .onRemoteRecordingReservationListJsonParsed(null);
        }
    }

    /**
     * リモート録画予約一覧取得.
     *
     * @param platFormType                                     プラットフォームタイプ
     * @param remoteRecordingReservationListJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getRemoteRecordingReservationListApi(
            final int platFormType,
            final RemoteRecordingReservationListJsonParserCallback
                    remoteRecordingReservationListJsonParserCallback) {

        if (mIsCancel) {
            //通信禁止状態なのでfalseで帰る
            DTVTLogger.error("RemoteRecordingReservationListWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック(platFormタイプは数値であり、省略不能なのでチェックはひとまず行わない)
        if (!checkNormalParameter(remoteRecordingReservationListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        mRemoteRecordingReservationListJsonParserCallback =
                remoteRecordingReservationListJsonParserCallback;

        //パラメータの作成
        String parameterBuffer;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JsonConstants.META_RESPONSE_PLATFORM_TYPE, platFormType);
            parameterBuffer = jsonObject.toString();
        } catch (JSONException e) {
            //パラメータは一つしかないので、例外の場合はエラーで帰る
            return false;
        }

        //リモート録画一覧の情報を読み込むため、リモート録画一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.REMOTE_RECORDING_RESERVATION_LIST_WEB_CLIENT,
                parameterBuffer, this, null);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param remoteRecordingReservationListJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final RemoteRecordingReservationListJsonParserCallback
                                                 remoteRecordingReservationListJsonParserCallback) {
        //コールバックが指定されていないならばfalse
        if (remoteRecordingReservationListJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
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