/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.MyChannelListJsonParser;

/**
 * マイチャンネル一覧WebClient.
 */
public class MyChannelWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コールバック.
     */
    public interface MyChannelListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param myChannelListResponse JSONパース後のデータ
         */
        void onMyChannelListJsonParsed(
                MyChannelListResponse myChannelListResponse);
    }

    /**
     * コールバックのインスタンス.
     */
    private MyChannelListJsonParserCallback myChannelListJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public MyChannelWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        if (myChannelListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new MyChannelListJsonParser(myChannelListJsonParserCallback)
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
        if (myChannelListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            myChannelListJsonParserCallback.onMyChannelListJsonParsed(null);
        }
    }

    /**
     * マイチャンネル一覧取得.
     * 本WebAPIには通常のパラメータが無く、基底クラスで追加するサービストークンのみとなる
     * @param myChannelListJsonParserCallback callback
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getMyChanelListApi(final MyChannelListJsonParserCallback myChannelListJsonParserCallback) {
        if (mIsCancel) {
            DTVTLogger.error("MyChannelWebClient is stopping connection");
            return false;
        }
        //パラメーターのチェック
        if (!checkNormalParameter(myChannelListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        this.myChannelListJsonParserCallback = myChannelListJsonParserCallback;

        //リモート録画一覧の情報を読み込むため、リモート録画一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.MY_CHANNEL_LIST_WEB_CLIENT,
                "", this, null);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param myChannelListJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final MyChannelListJsonParserCallback
                                                 myChannelListJsonParserCallback) {
        //コールバックが指定されていないならばfalse
        if (myChannelListJsonParserCallback == null) {
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
