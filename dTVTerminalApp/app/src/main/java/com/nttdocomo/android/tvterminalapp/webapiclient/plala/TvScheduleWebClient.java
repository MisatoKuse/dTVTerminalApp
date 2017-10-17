package com.nttdocomo.android.tvterminalapp.webapiclient.plala;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 * チャンネル毎番組一覧取得処理
 */
public class TvScheduleWebClient
        extends WebApiBasePlala  implements WebApiBasePlala.WebApiBasePlalaCallback{

    /**
     * コールバック
     */
    public interface TvScheduleJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         * @param tvScheduleList JSONパース後のデータ
         */
        void onTvScheduleJsonParsed(List<TvScheduleList> tvScheduleList);
    }

    //コールバックのインスタンス
    private TvScheduleJsonParserCallback mTvScheduleJsonParserCallback;

    /**
     * 通信成功時のコールバック
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        //パース後データ受け取り用
        List<TvScheduleList> pursedData;

        //JSONをパースする
        TvScheduleJsonParser tvScheduleJsonParser = new TvScheduleJsonParser();
        pursedData = tvScheduleJsonParser.TV_SCHEDULEListSender(returnCode.bodyData);

        //パース後のデータを返す
        mTvScheduleJsonParserCallback.onTvScheduleJsonParsed(pursedData);
    }

    /**
     * 通信失敗時のコールバック
     */
    @Override
    public void onError() {
        //エラーが発生したのでヌルを返す
        mTvScheduleJsonParserCallback.onTvScheduleJsonParsed(null);
    }


    /**
     * チャンネル一覧取得
     * @param chno    チャンネル番号
     * @param date    日付（"now"を指定した場合、現在放送中番組を返却)
     * @param filter  フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @return パラメータエラーならばfalse
     */
    public boolean getTvScheduleApi(int[] chno, String[] date, String filter,
                                   TvScheduleJsonParserCallback tvScheduleJsonParserCallback) {

        if(!checkNormalParameter(chno,date,filter,tvScheduleJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        mTvScheduleJsonParserCallback = tvScheduleJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(chno,date,filter);

        //JSONの組み立てに失敗していれば、falseで帰る
        if(sendParameter.isEmpty()) {
            return false;
        }

        //TVスケジュール一覧を呼び出す
        //TODO: 内部では暫定的にVODクリップ一覧を呼び出す
        openUrl(API_NAME_LIST.TV_SCHEDULE_LIST.getString(),sendParameter,this);

        //今のところ失敗は無いので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     * @param chno    チャンネル番号
     * @param date    日付（"now"を指定した場合、現在放送中番組を返却)
     * @param filter  フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param tvScheduleJsonParserCallback  コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int[] chno,String[] date,String filter,
                                      TvScheduleJsonParserCallback tvScheduleJsonParserCallback) {
        //パラメーターのチェック
        if(chno.length == 0) {
            //データが一つもなければエラー
            return false;
        }

        if(date == null || date.length == 0) {
            //データが一つもなければエラー
            return false;
        }

        for(String singleDate : date) {
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

        //コールバックがヌルならばfalse
        if(tvScheduleJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     * @param chno    チャンネル番号
     * @param date    日付（"now"を指定した場合、現在放送中番組を返却)
     * @param filter  フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int[] chno,String[] date,String filter) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //チャンネル番号配列の作成
            JSONArray channelArray = new JSONArray();
            for(int singleChannel : chno) {
                channelArray.put(singleChannel);
            }

            jsonObject.put("ch_list",channelArray);

            //日付配列の作成
            JSONArray dateArray = new JSONArray();
            for(String singleDate : date) {
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
