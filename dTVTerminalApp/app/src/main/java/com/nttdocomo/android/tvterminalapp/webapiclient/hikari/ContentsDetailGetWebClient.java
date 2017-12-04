/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.ContentsDetailGetResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ContentsDetailJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ContentsDetailGetWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック
     */
    public interface ContentsDetailJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param ContentsDetailLists JSONパース後のデータ
         */
        void onContentsDetailJsonParsed(ContentsDetailGetResponse ContentsDetailLists);
    }

    //コールバックのインスタンス
    private ContentsDetailJsonParserCallback mContentsDetailJsonParserCallback;

    /**
     * 通信成功時のコールバック
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new ContentsDetailJsonParser(mContentsDetailJsonParserCallback).
                execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック
     */
    @Override
    public void onError() {
        //エラーが発生したのでヌルを返す
        if (mContentsDetailJsonParserCallback != null) {
            mContentsDetailJsonParserCallback.onContentsDetailJsonParsed(null);
        }
    }

    /**
     * コンテンツ詳細情報取得
     *
     * @param crid                             取得したい情報のコンテンツ識別ID(crid)の配列
     * @param filter                           フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq                           年齢制限の値 1から17を指定。範囲外の値は1(全年齢)とする
     * @param contentsDetailJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getContentsDetailApi(String[] crid, String filter, int ageReq,
                                        ContentsDetailJsonParserCallback
                                                contentsDetailJsonParserCallback) {
        //パラメーターのチェック（ageReqは範囲外が全部1になるので、チェックは行わない）
        if (!checkNormalParameter(crid, filter, contentsDetailJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックの設定
        mContentsDetailJsonParserCallback = contentsDetailJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(crid, filter, ageReq);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //チャンネル一覧を呼び出す
        openUrl(API_NAME_LIST.CONTENTS_DETAIL_GET_WEB_CLIENT.getString(), sendParameter, this);

        //現状失敗は無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param crid                             取得したい情報のコンテンツ識別ID(crid)の配列
     * @param filter                           フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param contentsDetailJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(String[] crid, String filter,
                                         ContentsDetailJsonParserCallback
                                                 contentsDetailJsonParserCallback) {

        //配列にデータが格納されていなければエラー
        if (crid == null || crid.length <= 0) {
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

        if (contentsDetailJsonParserCallback == null) {
            //コールバックがヌルならばfalse
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     *
     * @param crids  取得したい情報のコンテンツ識別ID(crid)の配列
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq 年齢制限の値 1から17を指定。範囲外の値は1(全年齢)とする
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(String[] crids, String filter, int ageReq) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            JSONArray jsonCridDataObject = new JSONArray();

            //コンテンツ識別子配列をJsonArrayにする
            for (String crid : crids) {
                jsonCridDataObject.put(crid);
            }

            JSONObject jsonCridObject = new JSONObject();
            jsonCridObject.put(CRID_STRING, jsonCridDataObject);

            //配列をリストと言う名前で追加
            jsonObject.put(LIST_STRING, jsonCridObject);

            //フィルターの指定が省略されていた場合は、リリースにする
            if (filter == null || filter.isEmpty()) {
                filter = FILTER_RELEASE;
            }

            //フィルターの作成
            jsonObject.put(FILTER_PARAM, filter);

            //年齢の値が範囲外ならば、1にする
            if (ageReq < AGE_LOW_VALUE || ageReq > AGE_HIGH_VALUE) {
                ageReq = AGE_LOW_VALUE;
            }

            //年齢制限値の追加
            jsonObject.put(AGE_REQ_STRING, ageReq);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }

}