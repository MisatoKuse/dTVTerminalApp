/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.GenreCountGetJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * ジャンル毎コンテンツ数取得WebClient.
 */
public class GenreCountGetWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    //JSON作成用固定値
    /**
     * filter.
     */
    private static final String FILTER_STR = "filter";
    /**
     * age_req.
     */
    private static final String AGE_REQ_STR = "age_req";
    /**
     * list.
     */
    private static final String LIST_STR = "list";
    /**
     * genre_id.
     */
    private static final String GENRE_ID_STR = "genre_id";
    /**
     * type_id.
     */
    private static final String TYPE_STR = "type_id";
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コールバック.
     */
    public interface GenreCountGetJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param genreCountGetResponse JSONパース後のデータ
         */
        void onGenreCountGetJsonParsed(
                GenreCountGetResponse genreCountGetResponse);
    }

    /**
     * コールバックのインスタンス.
     */
    private GenreCountGetJsonParserCallback
            mGenreCountGetJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public GenreCountGetWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        if (mGenreCountGetJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new GenreCountGetJsonParser(
                    mGenreCountGetJsonParserCallback)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode.bodyData);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        if (mGenreCountGetJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mGenreCountGetJsonParserCallback
                    .onGenreCountGetJsonParsed(null);
        }
    }

    /**
     * ジャンル毎コンテンツ数取得
     * (パラメータ名は元仕様に準拠）.
     *
     * @param filter                          フィルター（release/testa/demoの何れかを指定可能。ヌルや空文字の場合はreleaseとなる）
     * @param ageReq                          年齢制限の値 1から17を指定。範囲外の値は1or17に丸めるのでチェックしない
     * @param genreId                         ジャンルID
     * @param genreCountGetJsonParserCallback コールバック
     * @return パラメータエラーの場合はfalse
     */
    public boolean getGenreCountGetApi(
            final String filter, final int ageReq, final List<String> genreId,
            final GenreCountGetJsonParserCallback genreCountGetJsonParserCallback) {
        DTVTLogger.start();
        //通信禁止時はfalseで帰る
        if (mIsCancel) {
            return false;
        }

        //パラメーターのチェック
        if (!checkNormalParameter(filter, ageReq, genreId,
                genreCountGetJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            DTVTLogger.end_ret(String.valueOf(false));
            return false;
        }

        //コールバックのセット
        mGenreCountGetJsonParserCallback =
                genreCountGetJsonParserCallback;

        //取得コンテンツタイプを"すべて"に固定
        final String GENRE_CONTENT_COUNT_TYPE = "";
        //送信用パラメータの作成
        String sendParameter = makeSendParameter(filter, ageReq, genreId, GENRE_CONTENT_COUNT_TYPE);

        //送信パラメータがヌルや空文字ならばfalse
        if (sendParameter == null || sendParameter.isEmpty()) {
            DTVTLogger.end_ret(String.valueOf(false));
            return false;
        }

        //ジャンル毎コンテンツ数取得情報を読み込むためにAPIを呼び出す
        openUrl(UrlConstants.WebApiUrl.GENRE_COUNT_GET_WEB_CLIENT,
                sendParameter, this);

        DTVTLogger.end();

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param filter         フィルター（release/testa/demoの何れかを指定可能。ヌルや空文字の場合はreleaseとなる）
     * @param ageReq         年齢制限の値 1から17を指定。範囲外の値は1or17に丸めるのでチェックしない
     * @param genreId        ジャンルID
     * @param parserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(
            final String filter, final int ageReq, final List<String> genreId,
            final GenreCountGetJsonParserCallback parserCallback) {

        //フィルターはヌルや空文字が有効なので、先に判定する
        if (!(filter == null || filter.isEmpty())) {
            //ヌルや空文字以外の場合、正規の三種類以外ならばfalse
            List<String> filterList = makeStringArry(FILTER_RELEASE, FILTER_DEMO, FILTER_TESTA);
            if (filterList.indexOf(filter) == -1) {
                return false;
            }
        }

        //ジャンルIDはヌルや空文字はエラー
        if (genreId == null || genreId.isEmpty()) {
            return false;
        }

        //コールバックが指定されていないならばfalse
        if (parserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }


    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param filter  フィルター（release/testa/demoの何れかを指定可能。ヌルや空文字の場合はreleaseとなる）
     * @param ageReq  年齢制限の値 1から17を指定。範囲外の値は1or17に丸める
     * @param genreId ジャンルID
     * @param type    タイプ（hikaritv_vod/dtv_vod/hikaritv_and_dtv_vodのいずれかを指定。ヌルや空文字の場合は全てのVODとなる）
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(
            final String filter, final int ageReq, final List<String> genreId, final String type) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        String strFilter = filter;
        try {
            //フィルターがヌルや空文字だった場合は"release"とする
            if (strFilter == null || strFilter.isEmpty()) {
                strFilter = WebApiBasePlala.FILTER_RELEASE;
            }

            //フィルターの設定
            jsonObject.put(FILTER_STR, strFilter);

            int intAge = ageReq;
            //数字がゼロの場合は無指定と判断して1にする.また17より大きい場合は17に丸める.
            if (ageReq < WebApiBasePlala.AGE_LOW_VALUE) {
                intAge = 1;
            } else if (ageReq > WebApiBasePlala.AGE_HIGH_VALUE) {
                intAge = 17;
            }

            //年齢設定の設定
            jsonObject.put(JsonConstants.META_RESPONSE_AGE_REQ, intAge);

            //ジャンルIDはリストの中に入れなければならない
            JSONArray array = new JSONArray();
            for (int i = 0; i < genreId.size(); i++) {
                array.put(genreId.get(i));
            }
            jsonObject.put(LIST_STR, array);

            if (!(type == null || type.isEmpty())) {
                //タイプはヌルや空文字以外ならば出力する
                jsonObject.put(TYPE_STR, type);
            }

            //結果をテキスト化
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
    public void stopConnect() {
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