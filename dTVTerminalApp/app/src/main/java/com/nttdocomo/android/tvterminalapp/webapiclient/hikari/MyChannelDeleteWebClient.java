/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelDeleteResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.MyChannelDeleteJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class MyChannelDeleteWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック
     */
    public interface MyChannelDeleteJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param myChannelDeleteResponse JSONパース後のデータ
         */
        void onMyChannelDeleteJsonParsed(
                MyChannelDeleteResponse myChannelDeleteResponse);
    }

    //コールバックのインスタンス
    private MyChannelDeleteJsonParserCallback myChannelDeleteJsonParserCallback;

    @Override
    public void onAnswer(ReturnCode returnCode) {
        if (myChannelDeleteJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new MyChannelDeleteJsonParser(myChannelDeleteJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    @Override
    public void onError() {
        if (myChannelDeleteJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            myChannelDeleteJsonParserCallback.onMyChannelDeleteJsonParsed(null);
        }
    }

    /**
     * マイチャンネル解除取得
     *
     * @param myChannelDeleteJsonParserCallback コールバックTODO:
     *                                          本WebAPIには通常のパラメータが無く、基底クラスで追加するサービストークンのみとなる。）
     *                                          TODO: 仕様確定後に基底クラスへサービストークンの処理の追加が必要
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getMyChanelDeleteApi(String serviceId, MyChannelDeleteJsonParserCallback myChannelDeleteJsonParserCallback) {
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
        openUrl(UrlConstants.WebApiUrl.MY_CHANNEL_RELEASE_WEB_CLIENT,
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
    private String makeSendParameter(String serviceId) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //サービスIDの作成
            JSONObject jsonPagerObject = new JSONObject();
            jsonPagerObject.put(JsonContents.META_RESPONSE_SERVICE_ID, serviceId);

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
     * @param serviceId                         サービスID
     * @param myChannelDeleteJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(String serviceId, MyChannelDeleteJsonParserCallback
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
}
