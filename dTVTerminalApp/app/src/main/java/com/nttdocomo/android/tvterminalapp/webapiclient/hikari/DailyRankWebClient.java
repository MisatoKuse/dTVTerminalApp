/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 今日のテレビランキング取得用Webクライアント.
 */
public class DailyRankWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック.
     */
    public interface DailyRankJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param dailyRankLists JSONパース後のデータ
         */
        void onDailyRankJsonParsed(List<DailyRankList> dailyRankLists);
    }

    /**
     * コールバックのインスタンス.
     */
    private DailyRankJsonParserCallback mDailyRankJsonParserCallback;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public DailyRankWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new DailyRankJsonParser(mDailyRankJsonParserCallback).execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        if (mDailyRankJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mDailyRankJsonParserCallback.onDailyRankJsonParsed(null);
        }
    }

    /**
     * 当日のクリップ数番組ランキング取得.
     *
     * @param limit                       取得する最大件数(値は1以上)
     * @param offset                      取得位置(値は1以上)
     * @param filter                      フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq                      年齢制限の値 1から17を指定。範囲外の値は1or17に丸める
     * @param dailyRankJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getDailyRankApi(final int limit, final int offset, final String filter, final int ageReq,
                                   final DailyRankJsonParserCallback dailyRankJsonParserCallback) {
        if (mIsCancel) {
            //通信禁止中はfalseで帰る
            DTVTLogger.error("DailyRankWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック
        if (!checkNormalParameter(limit, offset, filter, ageReq, dailyRankJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        mDailyRankJsonParserCallback = dailyRankJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(limit, offset, filter, ageReq);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //日毎ランク一覧を呼び出す
        openUrl(UrlConstants.WebApiUrl.DAILY_RANK_LIST, sendParameter, this);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param limit 取得する最大件数(値は1以上)
     * @param offset 取得位置(値は1以上)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq 年齢制限の値 1から17を指定。範囲外の値は1or17に丸めるのでチェックしない
     * @param dailyRankJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final int limit, final int offset, final String filter, final int ageReq,
                                         final DailyRankJsonParserCallback dailyRankJsonParserCallback) {
        // 各値が下限以下ならばfalse
        if (limit < 1) {
            return false;
        }
        if (offset < 1) {
            return false;
        }

        //文字列がヌルならfalse
        if (filter == null) {
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

        //コールバックが指定されていないならばfalse
        if (dailyRankJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param limit  取得する最大件数(値は1以上)
     * @param offset 取得位置(値は1以上)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq 年齢制限の値 1から17を指定。範囲外の値は1or17に丸める
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final int limit, final int offset, final String filter, final int ageReq) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //ページャー部の作成
            JSONObject jsonPagerObject = new JSONObject();
            jsonPagerObject.put(JsonConstants.META_RESPONSE_PAGER_LIMIT, limit);
            jsonPagerObject.put(JsonConstants.META_RESPONSE_OFFSET, offset);
            jsonObject.put(JsonConstants.META_RESPONSE_PAGER, jsonPagerObject);

            //その他
            jsonObject.put(JsonConstants.META_RESPONSE_FILTER, filter);

            int intAge = ageReq;
            //数字がゼロの場合は無指定と判断して1にする.また17より大きい場合は17に丸める.
            if (intAge < WebApiBasePlala.AGE_LOW_VALUE) {
                intAge = 1;
            } else if (intAge > WebApiBasePlala.AGE_HIGH_VALUE) {
                intAge = 17;
            }
            jsonObject.put(JsonConstants.META_RESPONSE_AGE_REQ, intAge);

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
     * DailyRankWebClientは使用時に毎回newしているため、通信再開処理は不要.
     */
    public void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        stopAllConnections();
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsCancel = false;
    }
}