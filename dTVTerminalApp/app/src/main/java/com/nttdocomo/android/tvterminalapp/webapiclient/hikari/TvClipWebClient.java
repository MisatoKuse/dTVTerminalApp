/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.JsonParserThread;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvClipJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TvClipWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback, JsonParserThread.JsonParser {

    @Override
    public void onParserFinished(Object parsedData) {
        //パース後のデータを返す
        if(null!=mTvClipJsonParserCallback){
            mTvClipJsonParserCallback.onTvClipJsonParsed((List<TvClipList>)parsedData);
        }
    }

    @Override
    public Object parse(String body) {
        TvClipJsonParser tvClipJsonParser = new TvClipJsonParser();
        List<TvClipList> pursedData;
        pursedData = tvClipJsonParser.tvClipListSender(body);
        return pursedData;
    }

    /**
     * コールバック
     */
    public interface TvClipJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         * @param tvClipLists JSONパース後のデータ
         */
        void onTvClipJsonParsed(List<TvClipList> tvClipLists);
    }

    //コールバックのインスタンス
    private TvClipJsonParserCallback mTvClipJsonParserCallback;

    /**
     * 通信成功時のコールバック
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        /*
        //パース後データ受け取り用
        List<TvClipList> pursedData;

        //JSONをパースする
        TvClipJsonParser tvClipJsonParser = new TvClipJsonParser();
        pursedData = tvClipJsonParser.VodClipListSender(returnCode.bodyData);


        //パース後のデータを返す
        mTvClipJsonParserCallback.onTvClipJsonParsed(pursedData);
        */
        //parse(returnCode.bodyData);
        //public JsonParserThread(String json, Handler handle, JsonParser lis)
        Handler handler =new Handler();
        try {
            JsonParserThread t = new JsonParserThread(returnCode.bodyData, handler, this);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
            onError();
        }
    }

    /**
     * 通信失敗時のコールバック
     */
    @Override
    public void onError() {
        //エラーが発生したのでヌルを返す
        mTvClipJsonParserCallback.onTvClipJsonParsed(null);

    }

    /**
     * VODクリップ取得
     * @param ageReq                         視聴年齢制限値（1から17までの値）
     * @param upperPagetLimit               結果の最大件数（1以上）
     * @param lowerPagetLimit　             結果の最小件数（1以上）
     * @param pagerOffset                    取得位置
     * @param tvClipJsonParserCallback    コールバック
     * @return パラメータ等に問題があった場合はfalse
     */
    public boolean getTvClipApi(int ageReq, int upperPagetLimit, int lowerPagetLimit,
                                int pagerOffset,
                                TvClipJsonParserCallback tvClipJsonParserCallback) {
        //パラメーターのチェック
        if(!checkNormalParameter(ageReq,upperPagetLimit, lowerPagetLimit,
                pagerOffset, tvClipJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、ヌルで帰る
            return false;
        }

        //コールバックの準備
        mTvClipJsonParserCallback = tvClipJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(ageReq,upperPagetLimit,lowerPagetLimit,pagerOffset);

        //JSONの組み立てに失敗していれば、ヌルで帰る
        if(sendParameter.isEmpty()) {
            return false;
        }

        //VODクリップ一覧を呼び出す
        openUrl(API_NAME_LIST.TV_CLIP_LIST.getString(),
                sendParameter,this);

        //今のところ正常なので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     * @param ageReq                        視聴年齢制限値
     * @param upperPagetLimit              結果の最大件数
     * @param lowerPagetLimit　            結果の最小件数
     * @param pagerOffset                   取得位置
     * @param tvClipJsonParserCallback   コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int ageReq,int upperPagetLimit,int lowerPagetLimit,
                                         int pagerOffset,
                                         TvClipJsonParserCallback tvClipJsonParserCallback) {
        if(!(ageReq >= 1 && ageReq <= 17)) {
            //ageReqが1から17ではないならばfalse
            return false;
        }

        // 各値が下限以下ならばfalse
        if(upperPagetLimit < 1) {
            return false;
        }
        if(lowerPagetLimit < 1) {
            return false;
        }
        if(pagerOffset < 0) {
            return false;
        }

        //コールバックが含まれていないならばエラー
        if(tvClipJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     * @param ageReq            視聴年齢制限値
     * @param upperPagetLimit  結果の最大件数
     * @param lowerPagetLimit　結果の最小件数
     * @param pagerOffset      取得位置
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int ageReq,int upperPagetLimit,int lowerPagetLimit,int pagerOffset) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            jsonObject.put("age_req", ageReq);

            JSONObject jsonPagerObject = new JSONObject();

            jsonPagerObject.put("upper_limit",upperPagetLimit);
            jsonPagerObject.put("lower_limit",lowerPagetLimit);
            jsonPagerObject.put("offset",pagerOffset);

            jsonObject.put("pager",jsonPagerObject);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }
}