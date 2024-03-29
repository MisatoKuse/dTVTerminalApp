/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelRegisterResponse;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.MyChannelRegisterJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * マイチャンネル登録WebClient.
 */
public class MyChannelRegisterWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel;

    /**
     * コールバック.
     */
    public interface MyChannelRegisterJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param myChannelRegisterResponse JSONパース後のデータ
         */
        void onMyChannelRegisterJsonParsed(
                MyChannelRegisterResponse myChannelRegisterResponse);
    }

    /**
     * コールバックのインスタンス.
     */
    private MyChannelRegisterJsonParserCallback myChannelRegisterJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public MyChannelRegisterWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        if (myChannelRegisterJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new MyChannelRegisterJsonParser(myChannelRegisterJsonParserCallback)
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
        if (myChannelRegisterJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            myChannelRegisterJsonParserCallback.onMyChannelRegisterJsonParsed(null);
        }
    }

    /**
     * マイチャンネル登録取得.
     *
     * @param serviceId                           サービスID
     * @param title                               　チャンネル名
     * @param rValue                              　チャンネルのパレンタル設定値(G/PG-12/R-15/R-18/R-20のどれか)
     * @param adultType                           　チャンネルのアダルトタイプ(adultか空文字)
     * @param index                               　マイチャンネル登録位置（1以上16以下）
     * @param myChannelRegisterJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getMyChanelRegisterApi(final String serviceId, final String title,
                                          final String rValue, final String adultType,
                                          final int index,
                                          final MyChannelRegisterJsonParserCallback
                                                  myChannelRegisterJsonParserCallback) {
        if (mIsCancel) {
            DTVTLogger.error("MyChannelRegisterWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック
        if (!checkNormalParameter(serviceId, title, rValue, adultType,
                index, myChannelRegisterJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        this.myChannelRegisterJsonParserCallback = myChannelRegisterJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(serviceId, title, rValue, adultType, index);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //リモート録画一覧の情報を読み込むため、リモート録画一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.MY_CHANNEL_SET_WEB_CLIENT,
                sendParameter, this, null);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param serviceId サービスID.
     * @param title     　チャンネル名
     * @param rValue    　チャンネルのパレンタル設定値
     * @param adultType 　チャンネルのアダルトタイプ(adultか空文字)
     * @param index     　マイチャンネル登録位置（1以上16以下）
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final String serviceId, final String title,
                                     final String rValue, final String adultType,
                                     final int index) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //サービスIDの作成
            jsonObject.put(JsonConstants.META_RESPONSE_SERVICE_ID, serviceId);
            jsonObject.put(JsonConstants.META_RESPONSE_TITLE, title);
            jsonObject.put(JsonConstants.META_RESPONSE_R_VALUE, rValue);
            jsonObject.put(JsonConstants.META_RESPONSE_ADULT_TYPE, adultType);
            jsonObject.put(JsonConstants.META_RESPONSE_INDEX, index);

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
     *@param serviceId サービスID
     * @param title    チャンネル名
     * @param rValue   チャンネルのパレンタル設定値
     * @param adultType チャンネルのアダルトタイプ(adultか空文字)
     * @param index     マイチャンネル登録位置（1以上16以下）
     * @param myChannelRegisterJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final String serviceId, final String title, final String rValue, final String adultType, final int index,
                                         final MyChannelRegisterJsonParserCallback myChannelRegisterJsonParserCallback) {

        //serviceIdが指定されていないならばfalse
        if (TextUtils.isEmpty(serviceId)) {
            return false;
        }

        //チャンネル名が指定されていないならばfalse
        if (TextUtils.isEmpty(title)) {
            return false;
        }

        //フィルター用の固定値をひとまとめにする
        List<String> rValueList = makeStringArry(ContentUtils.MY_CHANNEL_R_VALUE_G, ContentUtils.R_VALUE_PG_12, ContentUtils.R_VALUE_R_15,
                ContentUtils.R_VALUE_R_18, ContentUtils.R_VALUE_R_20);

        //指定された文字がひとまとめにした中に含まれるか確認
        if (rValueList.indexOf(rValue) == -1) {
            //含まれていないならばfalse
            return false;
        }

        //フィルター用の固定値をひとまとめにする
        List<String> adultTypeList = makeStringArry(ContentUtils.MY_CHANNEL_ADULT_TYPE_ADULT, ContentUtils.STR_BLANK);

        //指定された文字がひとまとめにした中に含まれるか確認
        if (adultTypeList.indexOf(adultType) == -1) {
            //含まれていないならばfalse
            return false;
        }

        ////index範囲は0 < index ≦　MAX_INDEXなので、範囲外ならばfalse
        if (index > ContentUtils.MY_CHANNEL_MAX_INDEX || index <= 0) {
            return false;
        }

        //コールバックが指定されていないならばfalse
        if (myChannelRegisterJsonParserCallback == null) {
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
