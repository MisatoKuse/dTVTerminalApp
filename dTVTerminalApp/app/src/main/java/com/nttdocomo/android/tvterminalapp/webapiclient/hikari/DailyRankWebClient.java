/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.DailyRankJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DailyRankWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック
     */
    public interface DailyRankJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param dailyRankLists JSONパース後のデータ
         */
        void onDailyRankJsonParsed(List<DailyRankList> dailyRankLists);
    }

    //コールバックのインスタンス
    private DailyRankJsonParserCallback mDailyRankJsonParserCallback;

    @Override
    public void onAnswer(ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new DailyRankJsonParser(mDailyRankJsonParserCallback).execute(returnCode.bodyData);
    }

    @Override
    public void onError() {
        if (mDailyRankJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mDailyRankJsonParserCallback.onDailyRankJsonParsed(null);
        }
    }

    /**
     * 当日のクリップ数番組ランキング取得
     *
     * @param limit                       取得する最大件数(値は1以上)
     * @param offset                      取得位置(値は1以上)
     * @param filter                      フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq                      年齢設定値（ゼロの場合は1扱い）
     * @param dailyRankJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getDailyRankApi(int limit, int offset, String filter, int ageReq,
                                   DailyRankJsonParserCallback dailyRankJsonParserCallback) {
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
        openUrl(API_NAME_LIST.DAILY_RANK_LIST.getString(), sendParameter, this);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param limit  取得する最大件数(値は1以上)
     * @param offset 取得位置(値は1以上)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int limit, int offset, String filter, int ageReq,
                                         DailyRankJsonParserCallback dailyRankJsonParserCallback) {
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

        //コールバックが指定されていないならばfalse
        if (dailyRankJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     *
     * @param limit  取得する最大件数(値は1以上)
     * @param offset 取得位置(値は1以上)
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int limit, int offset, String filter, int ageReq) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //ページャー部の作成
            JSONObject jsonPagerObject = new JSONObject();
            jsonPagerObject.put(JsonContents.META_RESPONSE_PAGER_LIMIT, limit);
            jsonPagerObject.put(JsonContents.META_RESPONSE_OFFSET, offset);
            jsonObject.put(JsonContents.META_RESPONSE_PAGER, jsonPagerObject);

            //その他
            jsonObject.put(JsonContents.META_RESPONSE_FILTER, filter);

            //数字がゼロの場合は無指定と判断して1にする
            if (ageReq == 0) {
                ageReq = 1;
            }

            jsonObject.put(JsonContents.META_RESPONSE_AGE_REQ, ageReq);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }
}
