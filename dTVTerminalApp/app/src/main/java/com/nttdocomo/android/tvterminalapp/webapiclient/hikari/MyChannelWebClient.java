/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.MyChannelListJsonParser;

public class MyChannelWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック
     */
    public interface MyChannelListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param myChannelListResponse JSONパース後のデータ
         */
        void onMyChannelListJsonParsed(
                MyChannelListResponse myChannelListResponse);
    }

    //コールバックのインスタンス
    private MyChannelListJsonParserCallback myChannelListJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る
     *
     * @param context コンテキスト
     */
    public MyChannelWebClient(Context context) {
        super(context);
    }

    @Override
    public void onAnswer(ReturnCode returnCode) {
        if (myChannelListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new MyChannelListJsonParser(myChannelListJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    @Override
    public void onError() {
        if (myChannelListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            myChannelListJsonParserCallback.onMyChannelListJsonParsed(null);
        }
    }

    /**
     * マイチャンネル一覧取得
     *
     * @param myChannelListJsonParserCallback コールバックTODO:
     * 本WebAPIには通常のパラメータが無く、基底クラスで追加するサービストークンのみとなる。）
     * TODO: 仕様確定後に基底クラスへサービストークンの処理の追加が必要
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getMyChanelListApi(MyChannelListJsonParserCallback myChannelListJsonParserCallback) {
        //パラメーターのチェック
        if (!checkNormalParameter(myChannelListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        this.myChannelListJsonParserCallback = myChannelListJsonParserCallback;

        //リモート録画一覧の情報を読み込むため、リモート録画一覧を呼び出す
        openUrl(UrlConstants.WebApiUrl.MY_CHANNEL_LIST_WEB_CLIENT,
                "", this);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param myChannelListJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(MyChannelListJsonParserCallback
                                                 myChannelListJsonParserCallback) {
        //コールバックが指定されていないならばfalse
        if (myChannelListJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }
}
