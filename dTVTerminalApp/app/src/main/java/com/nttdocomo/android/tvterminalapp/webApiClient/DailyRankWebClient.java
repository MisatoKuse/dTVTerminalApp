package com.nttdocomo.android.tvterminalapp.webApiClient;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 * 当日のクリップ数番組ランキング取得処理
 */
public class DailyRankWebClient
        extends WebApiBasePlala {

    /**
     * 当日のクリップ数番組ランキング取得
     * @param limit    取得する最大件数(値は1以上)
     * @param offset   取得位置(値は1以上)
     * @param filter   フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param ageReq   年齢設定値（ゼロの場合は1扱い）
     * @return TODO: ひとまずテスト用にVODクリップのリストを格納する
     */
    public List<VodClipList> getDailyRankApi(int limit,int offset,String filter,int ageReq) {
        //パラメーターのチェック
        if(!checkNormalParameter(limit,offset,filter,ageReq)) {
            //パラメーターがおかしければ通信不能なので、ヌルで帰る
            return null;
        }

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(limit,offset,filter,ageReq);

        //JSONの組み立てに失敗していれば、ヌルで帰る
        if(sendParameter.isEmpty()) {
            return null;
        }

        //VODクリップ一覧を呼び出す
        ReturnCode returnCode = openUrl(API_NAME_LIST.DAILY_RANK_LIST.getString(),sendParameter);

        List<VodClipList> pursedData;

        if(returnCode.errorType == ERROR_TYPE.SUCCESS) {
            //JSONをパースする
            //TODO: ひとまずテスト用にVODクリップ用のパーサーを使用する
            VodClipJsonParser vodClipJsonParser = new VodClipJsonParser();
            pursedData = vodClipJsonParser.VodClipListSender(returnCode.bodyData);

            //パース後のデータを返す
            return pursedData;
        } else {
            //通信に失敗しているので、ヌルを返す
            return null;
        }
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     * @param limit    取得する最大件数(値は1以上)
     * @param offset   取得位置(値は1以上)
     * @param filter   フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int limit,int offset,String filter,int ageReq) {
        // 各値が下限以下ならばfalse
        if(limit < 1) {
            return false;
        }
        if(offset < 1) {
            return false;
        }

        //文字列がヌルならfalse
        if(filter == null) {
            return false;
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

        //年齢情報の件0から17までの間以外はエラー
        if(ageReq < 0 || ageReq > 17) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     * @param limit    取得する最大件数(値は1以上)
     * @param offset   取得位置(値は1以上)
     * @param filter   フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int limit,int offset,String filter,int ageReq) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //ページャー部の作成
            JSONObject jsonPagerObject = new JSONObject();
            jsonPagerObject.put("limit",limit);
            jsonPagerObject.put("offset",offset);
            jsonObject.put("pager",jsonPagerObject);

            //その他
            jsonObject.put("filter", filter);

            //数字がゼロの場合は無指定と判断して1にする
            if(ageReq == 0) {
                ageReq = 1;
            }

            jsonObject.put("age_req", ageReq);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }

}
