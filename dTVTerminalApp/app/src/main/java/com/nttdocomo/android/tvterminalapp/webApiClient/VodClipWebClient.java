package com.nttdocomo.android.tvterminalapp.webApiClient;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 * VODクリップ通信処理
 */
public class VodClipWebClient
        extends WebApiBasePlala {
    /**
     * VODクリップ取得
     * @param ageReq            視聴年齢制限値（1から17までの値）
     * @param upperPagetLimit  結果の最大件数（1以上）
     * @param lowerPagetLimit　結果の最小件数（1以上）
     * @param pagerOffset      取得位置
     * @return VODクリップのリスト
     */
    public List<VodClipList> getVodClipApi(int ageReq,int upperPagetLimit,int lowerPagetLimit,int pagerOffset) {
        //パラメーターのチェック
        if(!checkNormalParameter(ageReq,upperPagetLimit,lowerPagetLimit,pagerOffset)) {
            //パラメーターがおかしければ通信不能なので、ヌルで帰る
            return null;
        }

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(ageReq,upperPagetLimit,lowerPagetLimit,pagerOffset);

        //JSONの組み立てに失敗していれば、ヌルで帰る
        if(sendParameter.isEmpty()) {
            return null;
        }

        //VODクリップ一覧を呼び出す
        ReturnCode returnCode = openUrl(API_NAME_LIST.VOD_CLIP_LIST.getString(),sendParameter);

        //パース後データ受け取り用
        List<VodClipList> pursedData;

        if(returnCode.errorType == ERROR_TYPE.SUCCESS) {
            //JSONをパースする
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
     * @param ageReq            視聴年齢制限値
     * @param upperPagetLimit  結果の最大件数
     * @param lowerPagetLimit　結果の最小件数
     * @param pagerOffset      取得位置
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int ageReq,int upperPagetLimit,int lowerPagetLimit,int pagerOffset) {
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
