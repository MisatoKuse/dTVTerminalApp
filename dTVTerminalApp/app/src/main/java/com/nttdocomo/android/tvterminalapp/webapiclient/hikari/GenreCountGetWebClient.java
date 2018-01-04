/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.GenreCountGetJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GenreCountGetWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    //JSON作成用固定値
    private static final String FILTER_STR = "filter";
    private static final String AGE_REQ_STR = "age_req";
    private static final String LIST_STR = "list";
    private static final String GENRE_ID_STR = "genre_id";
    private static final String TYPE_STR = "type_id";

    /**
     * コールバック
     */
    public interface GenreCountGetJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param genreCountGetResponse JSONパース後のデータ
         */
        void onGenreCountGetJsonParsed(
                GenreCountGetResponse genreCountGetResponse);
    }

    //コールバックのインスタンス
    private GenreCountGetJsonParserCallback
            mGenreCountGetJsonParserCallback;

    @Override
    public void onAnswer(ReturnCode returnCode) {
        if (mGenreCountGetJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new GenreCountGetJsonParser(
                    mGenreCountGetJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    @Override
    public void onError() {
        if (mGenreCountGetJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mGenreCountGetJsonParserCallback
                    .onGenreCountGetJsonParsed(null);
        }
    }

    /**
     * ジャンル毎コンテンツ数取得
     * (パラメータ名は元仕様に準拠）
     *
     * @param filter                          フィルター（release/testa/demoの何れかを指定可能。ヌルや空文字の場合はreleaseとなる）
     * @param ageReq                          年齢設定値（1から17を指定。それ以外を指定した場合1(全年齢)となる）
     * @param genreId                         ジャンルID
     * @param type                            タイプ（hikaritv_vod/dtv_vod/hikaritv_and_dtv_vodのいずれかを指定。ヌルや空文字の場合は全てのVODとなる）
     * @param genreCountGetJsonParserCallback コールバック
     * @return パラメータエラーの場合はfalse
     */
    public boolean getGenreCountGetApi(String filter, int ageReq, List<String> genreId, String type,
                                       GenreCountGetJsonParserCallback
                                               genreCountGetJsonParserCallback) {

        DTVTLogger.start();

        //パラメーターのチェック
        if (!checkNormalParameter(filter, ageReq, genreId, type,
                genreCountGetJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            DTVTLogger.end_ret(String.valueOf(false));
            return false;
        }

        //コールバックのセット
        mGenreCountGetJsonParserCallback =
                genreCountGetJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(filter, ageReq, genreId, type);

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
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param filter         フィルター（release/testa/demoの何れかを指定可能。ヌルや空文字の場合はreleaseとなる）
     * @param ageReq         年齢設定値（1から17を指定。それ以外を指定した場合1(全年齢)となる）
     * @param genreId        ジャンルID
     * @param type           タイプ（hikaritv_vod/dtv_vod/hikaritv_and_dtv_vodのいずれかを指定。ヌルや空文字の場合は全てのVODとなる）
     * @param parserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(String filter, int ageReq, List<String> genreId, String type,
                                         GenreCountGetJsonParserCallback parserCallback) {

        //フィルターはヌルや空文字が有効なので、先に判定する
        if (!(filter == null || filter.isEmpty())) {
            //ヌルや空文字以外の場合、正規の三種類以外ならばfalse
            List<String> filterList = makeStringArry(FILTER_RELEASE, FILTER_DEMO, FILTER_TESTA);
            if (filterList.indexOf(filter) == -1) {
                return false;
            }
        }

        //年齢設定値が負数か上限越えならばfalse
        if (ageReq < 0 || ageReq > WebApiBasePlala.AGE_HIGH_VALUE) {
            return false;
        }

        //ジャンルIDはヌルや空文字はエラー
        if (genreId == null || genreId.isEmpty()) {
            return false;
        }

        //タイプはヌルや空文字が有効なので、先に判定する
        if (!(type == null || type.isEmpty())) {
            //ヌルや空文字以外の場合、正規の三種類以外ならばfalse
            List<String> typeList = makeStringArry(
                    TYPE_HIKARI_TV_VOD, TYPE_DTV_VOD, TYPE_HIKARI_TV_AND_DTV_VOD);
            if (typeList.indexOf(type) == -1) {
                return false;
            }
        }

        //コールバックが指定されていないならばfalse
        if (parserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }


    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     *
     * @param filter  フィルター（release/testa/demoの何れかを指定可能。ヌルや空文字の場合はreleaseとなる）
     * @param ageReq  年齢設定値（1から17を指定。それ以外を指定した場合1(全年齢)となる）
     * @param genreId ジャンルID
     * @param type    タイプ（hikaritv_vod/dtv_vod/hikaritv_and_dtv_vodのいずれかを指定。ヌルや空文字の場合は全てのVODとなる）
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(String filter, int ageReq, List<String> genreId, String type) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //フィルターがヌルや空文字だった場合は"release"とする
            if (filter == null || filter.isEmpty()) {
                filter = WebApiBasePlala.FILTER_RELEASE;
            }

            //フィルターの設定
            jsonObject.put(FILTER_STR, filter);

            //年齢設定値がゼロの場合は1になる
            if (ageReq < WebApiBasePlala.AGE_LOW_VALUE) {
                ageReq = WebApiBasePlala.AGE_LOW_VALUE;
            }

            //年齢設定の設定
            jsonObject.put(AGE_REQ_STR, ageReq);

            //ジャンルIDはリストの中に入れなければならない
            JSONArray array = new JSONArray();
            for(int i = 0;i<genreId.size();i++){
                JSONObject genreJson = new JSONObject();
                genreJson.put(GENRE_ID_STR, genreId.get(i));
                array.put(genreJson);
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

        return answerText;
    }
}
