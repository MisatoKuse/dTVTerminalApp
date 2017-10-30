/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.ScaledDownProgramChannelList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.JsonParserThread;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ScaledDownProgramListChannelListJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class ScaledDownProgramListChannelListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback, JsonParserThread.JsonParser{

    @Override
    public void onParserFinished(Object parsedData) {
        //パース後のデータを返す
        if(null!=mScaledDownProgramChannelListJsonParserCallback){
            mScaledDownProgramChannelListJsonParserCallback.onScaledDownProgramChannelListJsonParsed((List<ScaledDownProgramChannelList>)parsedData);
        }
    }

    @Override
    public Object parse(String body) {
        ScaledDownProgramListChannelListJsonParser scaledDownProgramListJsonParser = new ScaledDownProgramListChannelListJsonParser();
        List<ScaledDownProgramChannelList> pursedData;
        pursedData = scaledDownProgramListJsonParser.ScaledDownProgramListSender(body);
        return pursedData;
    }

    /**
     * コールバック
     */
    public interface ScaledDownProgramChannelListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         * @param scaledDownProgramLists JSONパース後のデータ
         */
        void onScaledDownProgramChannelListJsonParsed(List<ScaledDownProgramChannelList> scaledDownProgramLists);
    }

    //コールバックのインスタンス
    private ScaledDownProgramChannelListJsonParserCallback mScaledDownProgramChannelListJsonParserCallback;

    /**
     * 通信成功時のコールバック
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
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
        mScaledDownProgramChannelListJsonParserCallback.onScaledDownProgramChannelListJsonParsed(null);
    }

    /**
     * 縮小番組データ取得
     * @param pagetLimit
     * @param pagerOffset
     * @param filter
     * @param type
     * @param scaledDownProgramListJsonParserCallback
     * @return
     */
    public boolean getScaledDownProgramListApi(int pagetLimit, int pagerOffset, String filter, String type,
                                 ScaledDownProgramChannelListJsonParserCallback scaledDownProgramListJsonParserCallback) {
        //パラメーターのチェック
        if(!checkNormalParameter(pagetLimit, pagerOffset, filter, type, scaledDownProgramListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、ヌルで帰る
            return false;
        }

        //コールバックの準備
        mScaledDownProgramChannelListJsonParserCallback = scaledDownProgramListJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(pagetLimit, pagerOffset, filter, type);

        //JSONの組み立てに失敗していれば、ヌルで帰る
        if(sendParameter.isEmpty()) {
            return false;
        }

        //URLデータを呼び出す
        openUrl(API_NAME_LIST.CHANNEL_LIST.getString(),
                sendParameter,this);

        //今のところ正常なので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     * @param pagetLimit
     * @param pagerOffset
     * @param filter
     * @param type
     * @param scaledDownProgramListJsonParserCallback コールバック
     * @return
     */
    private boolean checkNormalParameter(int pagetLimit, int pagerOffset, String filter, String type,
                                         ScaledDownProgramChannelListJsonParserCallback scaledDownProgramListJsonParserCallback) {

        // 各値が下限以下ならばfalse
        if(pagetLimit < 1) {
            return false;
        }
        if(pagerOffset < 1) {
            return false;
        }

        //文字列がヌルならfalse
        if(filter == null) {
            return false;
        }
        if(type == null) {
            return false;
        }

        //コールバックが含まれていないならばエラー
        if(scaledDownProgramListJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }
    

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     * @param pagetLimit
     * @param pagerOffset
     * @param filter
     * @param type
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int pagetLimit, int pagerOffset, String filter, String type) {
        JSONObject jsonObject = new JSONObject();
        String answerText="";

        try {
            //ページャー部の作成
            JSONObject jsonPagerObject = new JSONObject();
            jsonPagerObject.put("limit",pagetLimit);
            jsonPagerObject.put("offset",pagerOffset);
            jsonObject.put("pager",jsonPagerObject);

            //その他
            jsonObject.put("filter", filter);
            jsonObject.put("type", type);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }
        return answerText;
    }
}
