/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.os.Handler;


import com.nttdocomo.android.tvterminalapp.dataprovider.data.ScaledDownProgramList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.JsonParserThread;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ScaledDownProgramListJsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ScaledDownProgramListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback, JsonParserThread.JsonParser{

    @Override
    public void onParserFinished(Object parsedData) {
        //パース後のデータを返す
        if(null!=mScaledDownProgramListJsonParserCallback){
            mScaledDownProgramListJsonParserCallback.onScaledDownProgramListJsonParsed((List<ScaledDownProgramList>)parsedData);
        }
    }

    @Override
    public Object parse(String body) {
        ScaledDownProgramListJsonParser scaledDownProgramListJsonParser = new ScaledDownProgramListJsonParser();
        List<ScaledDownProgramList> pursedData;
        pursedData = scaledDownProgramListJsonParser.ScaledDownProgramListSender(body);
        return pursedData;
    }

    /**
     * コールバック
     */
    public interface ScaledDownProgramListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         * @param scaledDownProgramLists JSONパース後のデータ
         */
        void onScaledDownProgramListJsonParsed(List<ScaledDownProgramList> scaledDownProgramLists);
    }

    //コールバックのインスタンス
    private ScaledDownProgramListJsonParserCallback mScaledDownProgramListJsonParserCallback;

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
        mScaledDownProgramListJsonParserCallback.onScaledDownProgramListJsonParsed(null);
    }

    /**
     * 縮小番組データ取得
     * @param chList
     * @param dateList
     * @param filter
     * @param scaledDownProgramListJsonParserCallback
     * @return
     */
    public boolean getScaledDownProgramListApi(ArrayList<String> chList, ArrayList<String> dateList, final String filter,
                                 ScaledDownProgramListJsonParserCallback scaledDownProgramListJsonParserCallback) {
        //パラメーターのチェック
        if(!checkNormalParameter(chList, dateList, filter, scaledDownProgramListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、ヌルで帰る
            return false;
        }

        //コールバックの準備
        mScaledDownProgramListJsonParserCallback = scaledDownProgramListJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(chList, dateList, filter);

        //JSONの組み立てに失敗していれば、ヌルで帰る
        if(sendParameter.isEmpty()) {
            return false;
        }

        //URLデータを呼び出す
        openUrl(API_NAME_LIST.TV_SCHEDULE_LIST.getString(),
                sendParameter,this);

        //今のところ正常なので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     * @param chList
     * @param dateList
     * @param filter
     * @param scaledDownProgramListJsonParserCallback コールバック
     * @return
     */
    private boolean checkNormalParameter(ArrayList<String> chList, ArrayList<String> dateList, final String filter,
                                         ScaledDownProgramListJsonParserCallback scaledDownProgramListJsonParserCallback) {

        /* 2017/10/30日実装予定 */

        //コールバックが含まれていないならばエラー
        if(scaledDownProgramListJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }
    

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     * @param chList
     * @param dateList
     * @param filter
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(ArrayList<String> chList, ArrayList<String> dateList, final String filter) {
        JSONObject jsonObject = new JSONObject();
        String answerText="";
        
        /* 2017/10/30日実装予定 */
        /*
        try {
            

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }
        */
        return answerText;
    }
}
