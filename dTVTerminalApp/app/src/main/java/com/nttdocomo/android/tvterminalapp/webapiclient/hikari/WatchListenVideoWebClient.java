/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WatchListenVideoList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.JsonParserThread;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WatchListenVideoListJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WatchListenVideoWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback, JsonParserThread.JsonParser {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public WatchListenVideoWebClient(Context context) {
        super(context);
    }

    @Override
    public void onParserFinished(Object parsedData) {
        //パース後のデータを返す
        if (null != mWatchListenVideoJsonParserCallback) {
            mWatchListenVideoJsonParserCallback.onWatchListenVideoJsonParsed((List<WatchListenVideoList>) parsedData);
        }
    }

    @Override
    public Object parse(String body) {
        WatchListenVideoListJsonParser watchListenVideoListJsonParser = new WatchListenVideoListJsonParser();
        List<WatchListenVideoList> pursedData;
        pursedData = watchListenVideoListJsonParser.watchListenVideoListSender(body);
        return pursedData;
    }

    /**
     * コールバック.
     */
    public interface WatchListenVideoJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param watchListenVideoList JSONパース後のデータ
         */
        void onWatchListenVideoJsonParsed(List<WatchListenVideoList> watchListenVideoList);
    }

    //コールバックのインスタンス
    private WatchListenVideoJsonParserCallback mWatchListenVideoJsonParserCallback;

    /**
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        Handler handler = new Handler();
        try {
            JsonParserThread thread = new JsonParserThread(returnCode.bodyData, handler, this);
            thread.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
            onError(returnCode);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(ReturnCode returnCode) {
        //エラーが発生したのでヌルを返す
        mWatchListenVideoJsonParserCallback.onWatchListenVideoJsonParsed(null);

    }

    /**
     * 視聴中ビデオ一覧取得.
     *
     * @param ageReq                             視聴年齢制限値（1から17までの値）
     * @param upperPagetLimit                    結果の最大件数（1以上）
     * @param lowerPagetLimit                    　             結果の最小件数（1以上）
     * @param pagerOffset                        取得位置
     * @param watchListenVideoJsonParserCallback コールバック
     * @return パラメータ等に問題があった場合はfalse
     */
    public boolean getWatchListenVideoApi(int ageReq, int upperPagetLimit, int lowerPagetLimit,
                                          int pagerOffset, String pagerDirection,
                                          WatchListenVideoJsonParserCallback watchListenVideoJsonParserCallback) {
        if (mIsCancel) {
            DTVTLogger.error("WatchListenVideoWebClient is stopping connection");
            return false;
        }
        //パラメーターのチェック
        if (!checkNormalParameter(ageReq, upperPagetLimit, lowerPagetLimit,
                pagerOffset, pagerDirection, watchListenVideoJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、ヌルで帰る
            return false;
        }

        //コールバックの準備
        mWatchListenVideoJsonParserCallback = watchListenVideoJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(ageReq, upperPagetLimit, lowerPagetLimit, pagerOffset, pagerDirection);

        //JSONの組み立てに失敗していれば、ヌルで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //視聴中ビデオ一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.WATCH_LISTEN_VIDEO_LIST,
                sendParameter, this, null);

        //今のところ正常なので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param ageReq                             視聴年齢制限値
     * @param upperPagetLimit                    結果の最大件数
     * @param lowerPagetLimit                    　            結果の最小件数
     * @param pagerOffset                        取得位置
     * @param pagerDirection                    取得方向
     * @param watchListenVideoJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int ageReq, int upperPagetLimit, int lowerPagetLimit,
                                         int pagerOffset, String pagerDirection,
                                         WatchListenVideoJsonParserCallback watchListenVideoJsonParserCallback) {
        if (!(ageReq >= 1 && ageReq <= 17)) {
            //ageReqが1から17ではないならばfalse
            return false;
        }

        // 各値が下限以下ならばfalse
        if (upperPagetLimit < 1) {
            return false;
        }
        if (lowerPagetLimit < 1) {
            return false;
        }
        if (pagerOffset < 0) {
            return false;
        }

        //コールバックが含まれていないならばエラー
        if (watchListenVideoJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param ageReq          視聴年齢制限値
     * @param upperPagetLimit 結果の最大件数
     * @param lowerPagetLimit 　結果の最小件数
     * @param pagerOffset     取得位置
     * @param pagerDirection    取得方向
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int ageReq, int upperPagetLimit, int lowerPagetLimit,
                                     int pagerOffset, String pagerDirection) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            jsonObject.put(JsonConstants.META_RESPONSE_AGE_REQ, ageReq);

            JSONObject jsonPagerObject = new JSONObject();

            jsonPagerObject.put(JsonConstants.META_RESPONSE_UPPER_LIMIT, upperPagetLimit);
            jsonPagerObject.put(JsonConstants.META_RESPONSE_LOWER_LIMIT, lowerPagetLimit);
            jsonPagerObject.put(JsonConstants.META_RESPONSE_OFFSET, pagerOffset);
            jsonPagerObject.put(JsonConstants.META_RESPONSE_DIRECTION, pagerDirection);

            jsonObject.put(JsonConstants.META_RESPONSE_PAGER, jsonPagerObject);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        DTVTLogger.debugHttp(answerText);
        return answerText;
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
