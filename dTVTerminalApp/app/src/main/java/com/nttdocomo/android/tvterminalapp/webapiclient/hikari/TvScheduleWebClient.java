/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TvScheduleWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コンテキストを継承元のコンストラクタに送る
     *
     * @param context コンテキスト
     */
    public TvScheduleWebClient(Context context) {
        super(context);
    }

    /**
     * コールバック
     */
    public interface TvScheduleJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param tvScheduleList JSONパース後のデータ
         */
        void onTvScheduleJsonParsed(List<TvScheduleList> tvScheduleList);
    }

    //コールバックのインスタンス
    private TvScheduleJsonParserCallback mTvScheduleJsonParserCallback;

    /**
     * 通信成功時のコールバック
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new TvScheduleJsonParser(mTvScheduleJsonParserCallback).execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(ReturnCode returnCode) {
        //エラーが発生したのでヌルを返す
        mTvScheduleJsonParserCallback.onTvScheduleJsonParsed(null);
    }

    /**
     * チャンネル毎番組一覧取得
     *
     * @param chno   チャンネル番号
     * @param date   日付（"now"を指定した場合、現在放送中番組を返却)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @return パラメータエラーならばfalse
     */
    public boolean getTvScheduleApi(int[] chno, String[] date, String filter,
                                    TvScheduleJsonParserCallback tvScheduleJsonParserCallback) {

        if (!checkNormalParameter(chno, date, filter, tvScheduleJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        mTvScheduleJsonParserCallback = tvScheduleJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(chno, date, filter);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //チャンネル毎番組一覧を呼び出す
        openUrl(UrlConstants.WebApiUrl.TV_SCHEDULE_LIST, sendParameter, this);

        //今のところ失敗は無いので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param chno                         チャンネル番号
     * @param date                         日付（"now"を指定した場合、現在放送中番組を返却)
     * @param filter                       フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param tvScheduleJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int[] chno, String[] date, String filter,
                                         TvScheduleJsonParserCallback tvScheduleJsonParserCallback) {
        //パラメーターのチェック
        if (chno.length == 0) {
            //データが一つもなければエラー
            return false;
        }

        if (date == null || date.length == 0) {
            //データが一つもなければエラー
            return false;
        }

        for (String singleDate : date) {
            if (!checkDateString(singleDate)) {
                if (!singleDate.equals(DATE_NOW)) {
                    //日付でも"now"でもない文字だったので、エラー
                    return false;
                }
            }
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

        //コールバックがヌルならばfalse
        if (tvScheduleJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     *
     * @param chno   チャンネル番号
     * @param date   日付（"now"を指定した場合、現在放送中番組を返却)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int[] chno, String[] date, String filter) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //チャンネル番号配列の作成
            JSONArray channelArray = new JSONArray();
            for (int singleChannel : chno) {
                channelArray.put(singleChannel);
            }

            jsonObject.put(JsonConstants.META_RESPONSE_CH_LIST, channelArray);

            //日付配列の作成
            JSONArray dateArray = new JSONArray();
            for (String singleDate : date) {
                dateArray.put(singleDate);
            }

            jsonObject.put(JsonConstants.META_RESPONSE_DATE_LIST, dateArray);

            //その他
            jsonObject.put(JsonConstants.META_RESPONSE_FILTER, filter);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }

}
