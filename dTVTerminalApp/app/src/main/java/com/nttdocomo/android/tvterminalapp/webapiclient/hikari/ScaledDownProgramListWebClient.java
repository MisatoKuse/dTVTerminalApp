/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.os.Handler;


import com.nttdocomo.android.tvterminalapp.dataprovider.data.ScaledDownProgramList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.JsonParserThread;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ScaledDownProgramListJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
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
    public boolean getScaledDownProgramListApi(List<String> chList, List<String> dateList, final String filter,
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
    private boolean checkNormalParameter(List<String> chList, List<String> dateList, final String filter,
                                         ScaledDownProgramListJsonParserCallback scaledDownProgramListJsonParserCallback) {

        //パラメーターのチェック
        if(chList.size() == 0) {
            //データが一つもなければエラー
            return false;
        }

        if(dateList == null || dateList.size() == 0) {
            //データが一つもなければエラー
            return false;
        }

        for(String singleDate : dateList) {
            if(!checkDateString(singleDate)) {
                if(!singleDate.equals(DATE_NOW)) {
                    //日付でも"now"でもない文字だったので、エラー
                    return false;
                }
            }
        }

        //フィルター用の固定値をひとまとめにする
        List<String> filterList = makeStringArry(FILTER_RELEASE,FILTER_TESTA,FILTER_DEMO);

        //指定された文字がひとまとめにした中に含まれるか確認
        if(filterList.indexOf(filter) == -1) {
            //空文字ならば有効なので、それ以外はfalse
            if(!filter.isEmpty()) {
                return false;
            }
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
     * @param chList
     * @param dateList
     * @param filter
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(List<String> chList, List<String> dateList, final String filter) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //チャンネル番号配列の作成
            JSONArray channelArray = new JSONArray();
            for(String singleChannel : chList) {
                channelArray.put(singleChannel);
            }

            jsonObject.put("ch_list",channelArray);

            //日付配列の作成
            JSONArray dateArray = new JSONArray();
            for(String singleDate : dateList) {
                dateArray.put(singleDate);
            }

            jsonObject.put("date_list",dateArray);

            //その他
            jsonObject.put("filter", filter);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }
        return answerText;
    }
}
