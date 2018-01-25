/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelRegisterResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.MyChannelRegisterJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyChannelRegisterWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック
     */
    public interface MyChannelRegisterJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param myChannelRegisterResponse JSONパース後のデータ
         */
        void onMyChannelRegisterJsonParsed(
                MyChannelRegisterResponse myChannelRegisterResponse);
    }

    //コールバックのインスタンス
    private MyChannelRegisterJsonParserCallback myChannelRegisterJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る
     *
     * @param context コンテキスト
     */
    public MyChannelRegisterWebClient(Context context) {
        super(context);
    }

    @Override
    public void onAnswer(ReturnCode returnCode) {
        if (myChannelRegisterJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new MyChannelRegisterJsonParser(myChannelRegisterJsonParserCallback)
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
        if (myChannelRegisterJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            myChannelRegisterJsonParserCallback.onMyChannelRegisterJsonParsed(null);
        }
    }

    /**
     * マイチャンネル登録取得
     *
     * @param serviceId                           サービスID
     * @param title                               　チャンネル名
     * @param rValue                              　チャンネルのパレンタル設定値
     * @param adultType                           　チャンネルのアダルトタイプ
     * @param index                               　マイチャンネル登録位置
     * @param myChannelRegisterJsonParserCallback コールバック
     *                                            本WebAPIには通常のパラメータが無く、基底クラスで追加するサービストークンのみとなる。）
     *                                            TODO: 仕様確定後に基底クラスへサービストークンの処理の追加が必要
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getMyChanelRegisterApi(String serviceId, String title, String rValue, String adultType, int index,
                                          MyChannelRegisterJsonParserCallback myChannelRegisterJsonParserCallback) {
        //パラメーターのチェック
        if (!checkNormalParameter(serviceId, title, rValue, adultType, index, myChannelRegisterJsonParserCallback)) {
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
        openUrl(UrlConstants.WebApiUrl.MY_CHANNEL_SET_WEB_CLIENT,
                sendParameter, this);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     *
     * @param serviceId サービスID
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(String serviceId, String title, String rValue, String adultType, int index) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //サービスIDの作成
            jsonObject.put(JsonConstants.META_RESPONSE_SERVICE_ID, serviceId);
            jsonObject.put(JsonConstants.META_RESPONSE_TITLE, title);
            jsonObject.put(JsonConstants.META_RESPONSE_R_VALUE, rValue);
            jsonObject.put(JsonConstants.META_RESPONSE_ADULT, adultType);
            jsonObject.put(JsonConstants.META_RESPONSE_INDEX, index);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param myChannelRegisterJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(String serviceId, String title, String rValue, String adultType, int index,
                                         MyChannelRegisterJsonParserCallback myChannelRegisterJsonParserCallback) {

        //serviceIdが指定されていないならばfalse
        if (TextUtils.isEmpty(serviceId)) {
            return false;
        }

        //チャンネル名が指定されていないならばfalse
        if (TextUtils.isEmpty(title)) {
            return false;
        }

        //フィルター用の固定値をひとまとめにする
        List<String> rValueList = makeStringArry(MY_CHANNEL_R_VALUE_G, MY_CHANNEL_R_VALUE_PG_12, MY_CHANNEL_R_VALUE_PG_15,
                MY_CHANNEL_R_VALUE_PG_18, MY_CHANNEL_R_VALUE_PG_20);

        //指定された文字がひとまとめにした中に含まれるか確認
        if (rValueList.indexOf(rValue) == -1) {
            //含まれていないならばfalse
            return false;
        }

        //フィルター用の固定値をひとまとめにする
        List<String> adultTypeList = makeStringArry(MY_CHANNEL_ADULT_TYPE_ADULT, MY_CHANNEL_ADULT_TYPE_EMPTY);

        //指定された文字がひとまとめにした中に含まれるか確認
        if (adultTypeList.indexOf(adultType) == -1) {
            //含まれていないならばfalse
            return false;
        }

        ////index範囲は0 < index ≦　MAX_INDEXが指定されていないならばfalse
        if (index > MY_CHANNEL_MAX_INDEX || index <= 0) {
            return false;
        }

        //コールバックが指定されていないならばfalse
        if (myChannelRegisterJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }
}
