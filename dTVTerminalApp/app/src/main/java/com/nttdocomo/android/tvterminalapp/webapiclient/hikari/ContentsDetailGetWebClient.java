/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ContentsDetailGetResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ContentsDetailJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * コンテンツ詳細取得WebClient.
 */
public class ContentsDetailGetWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コールバック.
     */
    public interface ContentsDetailJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param ContentsDetailLists JSONパース後のデータ
         */
        void onContentsDetailJsonParsed(ContentsDetailGetResponse ContentsDetailLists);
    }

    /**
     * コンテキスト.
     */
    private Context mContext = null;
    /**
     * コールバックのインスタンス.
     */
    private ContentsDetailJsonParserCallback mContentsDetailJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public ContentsDetailGetWebClient(final Context context) {
        super(context);
        mContext = context;
    }

    /**
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(final ReturnCode returnCode) {
        //JSONをパースして、データを返す
        if (mContentsDetailJsonParserCallback != null) {
            new ContentsDetailJsonParser(mContext, mContentsDetailJsonParserCallback).
                    executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode.bodyData);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        //エラーが発生したのでヌルを返す
        if (mContentsDetailJsonParserCallback != null) {
            mContentsDetailJsonParserCallback.onContentsDetailJsonParsed(null);
        }
    }

    /**
     * コンテンツ詳細情報取得.
     *
     * @param crid                             取得したい情報のコンテンツ識別ID(crid)の配列
     * @param filter                           フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq                           年齢制限の値 1から17を指定。範囲外の値は1or17に丸める
     * @param contentsDetailJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getContentsDetailApi(final String[] crid, final String filter, final int ageReq,
                                        final ContentsDetailJsonParserCallback
                                                contentsDetailJsonParserCallback) {
        if (mIsCancel) {
            DTVTLogger.error("ContentsDetailGetWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック
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
        openUrl(UrlConstants.WebApiUrl.CONTENTS_DETAIL_GET_WEB_CLIENT, sendParameter, this);

        //現状失敗は無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param crid                             取得したい情報のコンテンツ識別ID(crid)の配列
     * @param filter                           フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param contentsDetailJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final String[] crid, final String filter,
                                         final ContentsDetailJsonParserCallback
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
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param crids  取得したい情報のコンテンツ識別ID(crid)の配列
     * @param filter フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq 年齢制限の値 1から17を指定。範囲外の値は1or17に丸める
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final String[] crids, final String filter, final int ageReq) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            JSONArray jsonListObject = new JSONArray();

            //コンテンツ識別子配列をJsonArrayにする
            for (String crid : crids) {
                jsonListObject.put(crid);
            }

            //配列をリストと言う名前で追加
            jsonObject.put(LIST_STRING, jsonListObject);

            //フィルターの指定が省略されていた場合は、リリースにする
            if (filter == null || filter.isEmpty()) {
                //フィルターの作成
                jsonObject.put(FILTER_PARAM, FILTER_RELEASE);
            } else {
                //フィルターの作成
                jsonObject.put(FILTER_PARAM, filter);

            }

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
