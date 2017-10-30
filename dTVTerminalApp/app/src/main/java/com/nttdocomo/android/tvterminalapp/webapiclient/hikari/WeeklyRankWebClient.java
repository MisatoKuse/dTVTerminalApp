/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WeeklyRankWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック
     */
    public interface WeeklyRankJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param weeklyRankLists JSONパース後のデータ
         */
        void onWeeklyRankJsonParsed(List<WeeklyRankList> weeklyRankLists);
    }

    //コールバックのインスタンス
    private WeeklyRankJsonParserCallback mWeeklyRankJsonParserCallback;


    @Override
    public void onAnswer(ReturnCode returnCode) {
        //拡張情報付きでパースを行う
        WeeklyRankJsonParser weeklyRankJsonParser = new WeeklyRankJsonParser(
                mWeeklyRankJsonParserCallback,returnCode.extraData);

        //JSONをパースして、データを返す
        weeklyRankJsonParser.execute(returnCode.bodyData);
    }

    @Override
    public void onError() {
        if (mWeeklyRankJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mWeeklyRankJsonParserCallback.onWeeklyRankJsonParsed(null);
        }
    }


    /**
     * 週間のクリップ数番組ランキング取得
     *
     * @param limit                        取得する最大件数(値は1以上)
     * @param offset                       　取得位置(値は1以上)
     * @param filter                       フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq                       年齢設定値（ゼロの場合は1扱い）
     * @param genreId                      ジャンルID
     * @param weeklyRankJsonParserCallback コールバック
     * @return パラメータエラー等ならばfalse
     */
    public boolean getWeeklyRankApi(int limit, int offset, String filter,
                                    int ageReq, String genreId,
                                    WeeklyRankJsonParserCallback weeklyRankJsonParserCallback) {
        //パラメーターのチェック(genreIdはヌルを受け付けるので、チェックしない)
        if (!checkNormalParameter(limit, offset, filter, ageReq, weeklyRankJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

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
        bundle.putString("genreId",genreId);

        //週毎ランク一覧を呼び出す
        openUrlWithExtraData(API_NAME_LIST.WEEKLY_RANK_LIST.getString(),sendParameter,this,bundle);

        //今のところ失敗は無いので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param limit  取得する最大件数(値は1以上)
     * @param offset 取得位置(値は1以上)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq 年齢設定値（ゼロの場合は1扱い）
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int limit, int offset, String filter, int ageReq,
                                         WeeklyRankJsonParserCallback weeklyRankJsonParserCallback) {
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

        //年齢情報の件0から17までの間以外はエラー
        if (ageReq < 1 || ageReq > 17) {
            return false;
        }

        //コールバックがヌルならばエラー
        if (weeklyRankJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     *
     * @param limit   取得する最大件数(値は1以上)
     * @param offset  取得位置(値は1以上)
     * @param filter  フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq  年齢設定値（ゼロの場合は1扱い）
     * @param genreId ジャンルID
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int limit, int offset, String filter,
                                     int ageReq, String genreId) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //ページャー部の作成
            JSONObject jsonPagerObject = new JSONObject();
            jsonPagerObject.put("limit", limit);
            jsonPagerObject.put("offset", offset);
            jsonObject.put("pager", jsonPagerObject);

            //その他
            jsonObject.put("filter", filter);

            //数字がゼロの場合は無指定と判断して1にする
            if (ageReq == 0) {
                ageReq = 1;
            }

            jsonObject.put("age_req", ageReq);

            //ヌルや空文字ではないならば、値を出力する
            if (genreId != null && !genreId.isEmpty()) {
                jsonObject.put("genre_id", genreId);
            }

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }

}
