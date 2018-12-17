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
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 番組表情報WebClient.
 */
public class TvScheduleWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /** 通信禁止判定フラグ.*/
    private boolean mIsCancel = false;
    /** リクエスト日付リスト.*/
    private String[] mRequestDateList;
    /** サービスIdユニーク.*/
    private String[] mServiceIdUniq;
    /** リクエストフィルター.*/
    private String mRequestFilter = null;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public TvScheduleWebClient(final Context context) {
        super(context);
    }

    /**
     * コールバック.
     */
    public interface TvScheduleJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param tvScheduleList JSONパース後のデータ
         * @param mServiceIdUniq サービスユニーク
         */
        void onTvScheduleJsonParsed(final List<TvScheduleList> tvScheduleList, final String[] mServiceIdUniq);
    }

    /**
     * コールバックのインスタンス.
     */
    private TvScheduleJsonParserCallback mTvScheduleJsonParserCallback;

    /**
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(final ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new TvScheduleJsonParser(mTvScheduleJsonParserCallback, mServiceIdUniq).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        //エラーが発生したのでヌルを返す
        mTvScheduleJsonParserCallback.onTvScheduleJsonParsed(null, mServiceIdUniq);
    }

    /**
     * リクエストパラメータをあらかじめセットしておく(番組表用).
     *
     * @param serviceIdUniq   サービスIDユニーク
     * @param requestDateList 日付リスト
     * @param requestFilter   フィルター
     */
    public void setChannelNoList(final String[] serviceIdUniq, final String[] requestDateList, final String requestFilter) {
        mServiceIdUniq = serviceIdUniq;
        mRequestDateList = requestDateList;
        mRequestFilter = requestFilter;
    }

    /**
     * テレビ番組表用リクエストメソッド.
     *
     * @param callback callback
     * @param areaCode エリアコード
     */
    public void getTvScheduleApi(final TvScheduleJsonParserCallback callback, final String areaCode) {
        getTvScheduleApi(mServiceIdUniq, mRequestDateList, mRequestFilter, areaCode, callback);
    }

    /**
     * チャンネル毎番組一覧取得.
     *
     * @param serviceIdUniq  サービスIDユニーク
     * @param date   日付（"now"を指定した場合、現在放送中番組を返却)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param areaCode エリアコード service_id_uniqに地デジを指定する場合は必須
     * @param tvScheduleJsonParserCallback callback
     * @return パラメータエラーならばfalse
     */
    public boolean getTvScheduleApi(final String[] serviceIdUniq, final String[] date, final String filter, final String areaCode,
                                    final TvScheduleJsonParserCallback tvScheduleJsonParserCallback) {

        if (mIsCancel) {
            DTVTLogger.error("TvScheduleWebClient is stopping connection");
            return false;
        }

        if (!checkNormalParameter(serviceIdUniq, date, filter, tvScheduleJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        mTvScheduleJsonParserCallback = tvScheduleJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(serviceIdUniq, date, filter, areaCode);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        mServiceIdUniq = serviceIdUniq;
        //チャンネル毎番組一覧を呼び出す
        openUrl(UrlConstants.WebApiUrl.TV_SCHEDULE_LIST, sendParameter, this);

        //今のところ失敗は無いので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param serviceIdUniq                サービスIDユニーク
     * @param date                         日付（"now"を指定した場合、現在放送中番組を返却)
     * @param filter                       フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param tvScheduleJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final String[] serviceIdUniq, final String[] date, final String filter,
                                         final TvScheduleJsonParserCallback tvScheduleJsonParserCallback) {
        //パラメーターのチェック
        if (serviceIdUniq.length == 0) {
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
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param serviceIdUniq   サービスIDユニーク
     * @param date   日付（"now"を指定した場合、現在放送中番組を返却)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param areaCode エリアコード service_id_uniqに地デジを指定する場合は必須
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final String[] serviceIdUniq, final String[] date, final String filter, final String areaCode) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //チャンネル番号配列の作成
            JSONArray channelArray = new JSONArray();
            for (String singleChannel : serviceIdUniq) {
                channelArray.put(singleChannel);
            }
            jsonObject.put(JsonConstants.META_RESPONSE_CH_LIST, channelArray);
            //日付配列の作成
            JSONArray dateArray = new JSONArray();
            for (String singleDate : date) {
                dateArray.put(singleDate);
            }
            jsonObject.put(JsonConstants.META_RESPONSE_DATE_LIST, dateArray);
            if (filter.isEmpty()) {
                DTVTLogger.debug("filter is empty");
            } else {
                //その他
                jsonObject.put(JsonConstants.META_RESPONSE_FILTER, filter);
            }
            if (!TextUtils.isEmpty(areaCode)) {
                jsonObject.put(JsonConstants.META_RESPONSE_AREA_CODE, areaCode);
            }
            answerText = jsonObject.toString();
        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }
        DTVTLogger.debugHttp(answerText);
        return answerText;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param chno   チャンネル番号
     * @param date   日付（"now"を指定した場合、現在放送中番組を返却)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final int[] chno, final String[] date, final String filter) {
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
            if (filter.isEmpty()) {
                DTVTLogger.debug("filter is empty");
            } else {
                //その他
                jsonObject.put(JsonConstants.META_RESPONSE_FILTER, filter);
            }


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
