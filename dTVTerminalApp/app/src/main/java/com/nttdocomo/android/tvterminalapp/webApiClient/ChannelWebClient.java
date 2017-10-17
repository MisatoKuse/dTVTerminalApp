package com.nttdocomo.android.tvterminalapp.webApiClient;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 * チャンネル一覧取得処理
 */
public class ChannelWebClient
        extends WebApiBasePlala {

    /**
     * チャンネル一覧取得
     * @param pagetLimit    取得する最大件数(値は1以上)
     * @param pagerOffset   取得位置(値は1以上)
     * @param filter        フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param type          タイプ　dch：dチャンネル・hikaritv：ひかりTVの多ch・指定なし：全て
     * @return TODO: ひとまずテスト用にVODクリップのリストを格納する
     */
    public List<VodClipList> getChannelApi(int pagetLimit, int pagerOffset, String filter, String type) {
        //パラメーターのチェック
        if(!checkNormalParameter(pagetLimit,pagerOffset,filter,type)) {
            //パラメーターがおかしければ通信不能なので、ヌルで帰る
            return null;
        }

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(pagetLimit,pagerOffset,filter,type);

        //JSONの組み立てに失敗していれば、ヌルで帰る
        if(sendParameter.isEmpty()) {
            return null;
        }

        //VODクリップ一覧を呼び出す
        ReturnCode returnCode = openUrl(API_NAME_LIST.CHANNEL_LIST.getString(),sendParameter);

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
     * @param pagetLimit    取得する最大件数(値は1以上)
     * @param pagerOffset   取得位置(値は1以上)
     * @param filter        フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param type          タイプ　dch：dチャンネル・hikaritv：ひかりTVの多ch・指定なし：全て
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(int pagetLimit,int pagerOffset,String filter,String type) {
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


        //フィルター用の固定値をひとまとめにする
        List<String> filterList = makeStringArry(FILTER_RELEASE,FILTER_TESTA,FILTER_DEMO);

        //指定された文字がひとまとめにした中に含まれるか確認
        if(filterList.indexOf(filter) == -1) {
            //空文字ならば有効なので、それ以外はfalse
            if(!filter.isEmpty()) {
                return false;
            }
        }

        //タイプ用の固定値をひとまとめにする
        List<String> typeList = makeStringArry(TYPE_D_CHANNEL,TYPE_HIKARI_TV,TYPE_ALL);

        if(typeList.indexOf(type) == -1) {
            //含まれていないならばfalse
            return false;
        }


        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     * @param pagetLimit    取得する最大件数(値は1以上)
     * @param pagerOffset   取得位置(値は1以上)
     * @param filter        フィルター　release・testa・demoのいずれかの文字列・指定がない場合はrelease
     * @param type          タイプ　dch：dチャンネル・hikaritv：ひかりTVの多ch・指定なし：全て
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(int pagetLimit,int pagerOffset,String filter,String type) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
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
