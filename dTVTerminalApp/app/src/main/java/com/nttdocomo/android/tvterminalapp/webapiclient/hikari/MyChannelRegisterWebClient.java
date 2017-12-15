/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelRegisterResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.MyChannelRegisterJsonParser;

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

    @Override
    public void onAnswer(ReturnCode returnCode) {
        if (myChannelRegisterJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new MyChannelRegisterJsonParser(myChannelRegisterJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    @Override
    public void onError() {
        if (myChannelRegisterJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            myChannelRegisterJsonParserCallback.onMyChannelRegisterJsonParsed(null);
        }
    }

    /**
     * マイチャンネル登録取得
     *
     * @param myChannelRegisterJsonParserCallback コールバックTODO:
     * 本WebAPIには通常のパラメータが無く、基底クラスで追加するサービストークンのみとなる。）
     * TODO: 仕様確定後に基底クラスへサービストークンの処理の追加が必要
     * @return パラメータエラー等が発生した場合はfalse
     */
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

        //リモート録画一覧の情報を読み込むため、リモート録画一覧を呼び出す
        openUrl(UrlConstants.WebApiUrl.MY_CHANNEL_SET_WEB_CLIENT,
                "", this);

        //今のところ失敗していないので、trueを返す
        return true;
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
