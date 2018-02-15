/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ChannelWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コールバック
     */
    public interface ChannelJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param channelLists JSONパース後のデータ
         */
        void onChannelJsonParsed(List<ChannelList> channelLists);
    }

    //コールバックのインスタンス
    private ChannelJsonParserCallback mChannelJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る
     *
     * @param context コンテキスト
     */
    public ChannelWebClient(Context context) {
        super(context);
    }

    /**
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new ChannelJsonParser(mChannelJsonParserCallback).execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(ReturnCode returnCode) {
        //エラーが発生したのでヌルを返す
        mChannelJsonParserCallback.onChannelJsonParsed(null);
    }

    /**
     * チャンネル一覧取得.
     *
     * @param pagetLimit                取得する最大件数(値は1以上)
     * @param pagerOffset               取得位置(値は1以上)
     * @param filter                    フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param type                      タイプ　dch：dチャンネル・hikaritv：ひかりTVの多ch・指定なし：全て
     * @param channelJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getChannelApi(int pagetLimit, int pagerOffset,
                                 String filter, String type,
                                 ChannelJsonParserCallback channelJsonParserCallback) {

        if (mIsCancel) {
            DTVTLogger.error("ChannelWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック
        if (!checkNormalParameter(pagetLimit, pagerOffset, filter, type, channelJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックの設定
        mChannelJsonParserCallback = channelJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(pagetLimit, pagerOffset, filter, type);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //チャンネル一覧を呼び出す
        openUrl(UrlConstants.WebApiUrl.CHANNEL_LIST,sendParameter,this);

        //現状失敗は無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param pagetLimit                取得する最大件数(値は1以上)
     * @param pagerOffset               取得位置(値は1以上)
     * @param filter                    フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param type                      タイプ　dch：dチャンネル・hikaritv：ひかりTVの多ch・指定なし：全て
     * @param channelJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int pagetLimit, int pagerOffset,
                                         String filter, String type,
                                         ChannelJsonParserCallback channelJsonParserCallback) {
        // 各値が下限以下ならばfalse
        if (pagetLimit < 0) {
            return false;
        }
        if (pagerOffset < 0) {
            return false;
        }

        //文字列がヌルならfalse
        if (filter == null) {
            return false;
        }
        if (type == null) {
            return false;
        }


        //フィルター用の固定値をひとまとめにする
        List<String> filterList = makeStringArry(FILTER_RELEASE, FILTER_TESTA, FILTER_DEMO);

        //指定された文字がひとまとめにした中に含まれるか確認
        if (filterList.indexOf(filter) == -1) {
            //空文字ならば有効なので、それ以外はfalse
            if (!filter.isEmpty()) {
                return false;
            }
        }

        //タイプ用の固定値をひとまとめにする
        List<String> typeList = makeStringArry(TYPE_D_CHANNEL, TYPE_HIKARI_TV, TYPE_ALL);

        if (typeList.indexOf(type) == -1) {
            //含まれていないならばfalse
            return false;
        }

        if (channelJsonParserCallback == null) {
            //コールバックがヌルならばfalse
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param pagetLimit  取得する最大件数(値は1以上)
     * @param pagerOffset 取得位置(値は1以上)
     * @param filter      フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param type        タイプ　dch：dチャンネル・hikaritv：ひかりTVの多ch・指定なし：全て
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int pagetLimit, int pagerOffset, String filter, String type) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //ページャー部の作成
            //TODO:ページング指定を外してもぷららサーバ側からMax10件しか取得できない(実際にはレスポンスのtotal件数は200件以上になっている)
            if (pagetLimit > 0 && pagerOffset > 0) {
                JSONObject jsonPagerObject = new JSONObject();
                jsonPagerObject.put(JsonConstants.META_RESPONSE_PAGER_LIMIT, pagetLimit);
                jsonPagerObject.put(JsonConstants.META_RESPONSE_OFFSET, pagerOffset);
                jsonObject.put(JsonConstants.META_RESPONSE_PAGER, jsonPagerObject);
            }

            //その他
            jsonObject.put(JsonConstants.META_RESPONSE_FILTER, filter);
            jsonObject.put(JsonConstants.META_RESPONSE_TYPE, type);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

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
