/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelDeleteResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.MyChannelDeleteJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class MyChannelDeleteWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel;

    /**
     * コールバック.
     */
    public interface MyChannelDeleteJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param myChannelDeleteResponse JSONパース後のデータ
         */
        void onMyChannelDeleteJsonParsed(
                MyChannelDeleteResponse myChannelDeleteResponse);
    }

    //コールバックのインスタンス
    private MyChannelDeleteJsonParserCallback myChannelDeleteJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public MyChannelDeleteWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        if (myChannelDeleteJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new MyChannelDeleteJsonParser(myChannelDeleteJsonParserCallback)
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
        if (myChannelDeleteJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            myChannelDeleteJsonParserCallback.onMyChannelDeleteJsonParsed(null);
        }
    }

    /**
     * マイチャンネル解除取得.
     *
     * @param serviceId                         サービスID
     * @param myChannelDeleteJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getMyChanelDeleteApi(
            final String serviceId,
            final MyChannelDeleteJsonParserCallback myChannelDeleteJsonParserCallback) {
        if (mIsCancel) {
            DTVTLogger.error("MyChannelDeleteWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック
        if (!checkNormalParameter(serviceId, myChannelDeleteJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        this.myChannelDeleteJsonParserCallback = myChannelDeleteJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(serviceId);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //リモート録画一覧の情報を読み込むため、リモート録画一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.MY_CHANNEL_RELEASE_WEB_CLIENT,
                sendParameter, this, null);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param serviceId サービスID
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final String serviceId) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //サービスIDの作成
            jsonObject.put(JsonConstants.META_RESPONSE_SERVICE_ID, serviceId);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        DTVTLogger.debugHttp(answerText);
        return answerText;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param serviceId                         サービスID
     * @param myChannelDeleteJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final String serviceId, final MyChannelDeleteJsonParserCallback
            myChannelDeleteJsonParserCallback) {

        //serviceIdが指定されていないならばfalse
        if (TextUtils.isEmpty(serviceId)) {
            return false;
        }

        //コールバックが指定されていないならばfalse
        if (myChannelDeleteJsonParserCallback == null) {
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
     * 通信可能状態にする.
     */
    public void enableConnection() {
        DTVTLogger.start();
        mIsCancel = false;
    }
}
