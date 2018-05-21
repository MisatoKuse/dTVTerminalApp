/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 週間ランキング取得用Webクライアント.
 */
public class WeeklyRankWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {
    /**
     * ジャンルID送信用キー.
     */
    public static final String WEEKLY_RANK_CLIENT_BUNDLE_KEY = "genreId";

    /**
     * リクエストジャンル.
     */
    private String mGenreId = "";

    /**
     * コールバック.
     */
    public interface WeeklyRankJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param weeklyRankLists JSONパース後のデータ
         * @param genreId リクエストしたジャンルID
         */
        void onWeeklyRankJsonParsed(List<WeeklyRankList> weeklyRankLists, final String genreId);
    }

    /**
     * コールバックのインスタンス.
     */
    private WeeklyRankJsonParserCallback mWeeklyRankJsonParserCallback;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public WeeklyRankWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        //拡張情報付きでパースを行う
        WeeklyRankJsonParser weeklyRankJsonParser = new WeeklyRankJsonParser(
                mWeeklyRankJsonParserCallback, returnCode.extraData, mGenreId);

        //JSONをパースして、データを返す
        weeklyRankJsonParser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        if (mWeeklyRankJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mWeeklyRankJsonParserCallback.onWeeklyRankJsonParsed(null, mGenreId);
        }
    }


    /**
     * 週間のクリップ数番組ランキング取得.
     *
     * @param limit                        取得する最大件数(値は1以上)
     * @param offset                       　取得位置(値は1以上)
     * @param filter                       フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq                       年齢制限の値 1から17を指定。範囲外の値は1or17に丸める
     * @param genreId                      ジャンルID
     * @param weeklyRankJsonParserCallback コールバック
     * @return パラメータエラー等ならばfalse
     */
    public boolean getWeeklyRankApi(final int limit, final int offset, final String filter,
                                    final int ageReq, final String genreId,
                                    final WeeklyRankJsonParserCallback weeklyRankJsonParserCallback) {
        if (mIsCancel) {
            //通信禁止中はfalseで帰る
            DTVTLogger.error("WeeklyRankWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック(genreIdはヌルを受け付けるので、チェックしない)
        if (!checkNormalParameter(limit, offset, filter, ageReq, weeklyRankJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        mGenreId = genreId;

        //コールバックを呼べるようにする
        mWeeklyRankJsonParserCallback = weeklyRankJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(limit, offset, filter, ageReq, genreId);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //拡張情報の作成
        Bundle bundle = new Bundle();
        bundle.putString(WEEKLY_RANK_CLIENT_BUNDLE_KEY, genreId);

        //週毎ランク一覧を呼び出す
        openUrlWithExtraData(UrlConstants.WebApiUrl.WEEKLY_RANK_LIST, sendParameter, this, bundle);

        //今のところ失敗は無いので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param limit  取得する最大件数(値は1以上)
     * @param offset 取得位置(値は1以上)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq 年齢制限の値 1から17を指定。範囲外の値は1or17に丸めるのでチェックしない
     * @param weeklyRankJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final int limit, final int offset, final String filter, final int ageReq,
                                         final WeeklyRankJsonParserCallback weeklyRankJsonParserCallback) {
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

        //コールバックがヌルならばエラー
        if (weeklyRankJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param limit   取得する最大件数(値は1以上)
     * @param offset  取得位置(値は1以上)
     * @param filter  フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq  年齢制限の値 1から17を指定。範囲外の値は1or17に丸める
     * @param genreId ジャンルID
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final int limit, final int offset, final String filter,
                                     final int ageReq, final String genreId) {
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

            //ヌルや空文字ではないならば、値を出力する
            if (genreId != null && !genreId.isEmpty()) {
                jsonObject.put(JsonConstants.META_RESPONSE_GENRE_ID, genreId);
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
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsCancel = false;
    }
}
