/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RemoteRecordingReservationListJsonParser;

public class RemoteRecordingReservationListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

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

    //コールバックのインスタンス
    private RemoteRecordingReservationListJsonParserCallback
            mRemoteRecordingReservationListJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public RemoteRecordingReservationListWebClient(Context context) {
        super(context);
    }

    @Override
    public void onAnswer(ReturnCode returnCode) {
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
    public void onError(ReturnCode returnCode) {
        if (mRemoteRecordingReservationListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mRemoteRecordingReservationListJsonParserCallback
                    .onRemoteRecordingReservationListJsonParsed(null);
        }
    }

    /**
     * リモート録画予約一覧取得.
     *
     * @param remoteRecordingReservationListJsonParserCallback コールバックTODO:
     * 本WebAPIには通常のパラメータが無く、基底クラスで追加するサービストークンのみとなる。）
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getRemoteRecordingReservationListApi(
            RemoteRecordingReservationListJsonParserCallback
                    remoteRecordingReservationListJsonParserCallback) {
        //パラメーターのチェック
        if (!checkNormalParameter(remoteRecordingReservationListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        mRemoteRecordingReservationListJsonParserCallback =
                remoteRecordingReservationListJsonParserCallback;

        //リモート録画一覧の情報を読み込むため、リモート録画一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.REMOTE_RECORDING_RESERVATION_LIST_WEB_CLIENT,
                "", this, null);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param remoteRecordingReservationListJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(RemoteRecordingReservationListJsonParserCallback
                                                 remoteRecordingReservationListJsonParserCallback) {
        //コールバックが指定されていないならばfalse
        if (remoteRecordingReservationListJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }
}
