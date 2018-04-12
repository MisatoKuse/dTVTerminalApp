/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ClipDeleteJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * クリップ削除処理.
 */
public class ClipDeleteWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック.
     */
    public interface ClipDeleteJsonParserCallback {
        /**
         * 登録が正常に終了した場合に呼ばれるコールバック.
         */
        void onClipDeleteResult();

        /**
         * 登録が正常に終了した場合に呼ばれるコールバック.
         */
        void onClipDeleteFailure();
    }

    //コールバックのインスタンス
    private ClipDeleteJsonParserCallback mClipDeleteJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public ClipDeleteWebClient(final Context context) {
        super(context);
    }

    /**
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(final ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new ClipDeleteJsonParser(mClipDeleteJsonParserCallback).execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        //エラーが発生したのでヌルを返す(専用のエラーメッセージがあるので、ネットワークエラーは送らない)
        mClipDeleteJsonParserCallback.onClipDeleteFailure();
    }

    /**
     * クリップ削除.
     *
     * @param type                         タイプ　h4d_iptv：多チャンネル、h4d_vod：ビデオ、dch：dTVチャンネル、dtv_vod：dTV
     * @param crid                         コンテンツ識別子
     * @param titleId                      タイトルID
     * @param clipDeleteJsonParserCallback callback
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getClipDeleteApi(final String type, final String crid, final String titleId,
                                    final ClipDeleteJsonParserCallback clipDeleteJsonParserCallback) {
        //パラメーターのチェック
        if (!checkParameter(type, crid, titleId, clipDeleteJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックの設定
        mClipDeleteJsonParserCallback = clipDeleteJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(type, crid, titleId);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //チャンネル一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.CLIP_DELETE_GET_WEB_CLIENT,
                sendParameter, this, null);

        //現状失敗は無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param type                         タイプ　h4d_iptv：多チャンネル、h4d_vod：ビデオ、dch：dTVチャンネル、dtv_vod：dTV
     * @param crid                         コンテンツ識別子
     * @param titleId                      タイトルID
     * @param clipDeleteJsonParserCallback callback
     * @return 値がおかしいならばfalse
     */
    private boolean checkParameter(final String type, final String crid, final String titleId,
                                   final ClipDeleteJsonParserCallback clipDeleteJsonParserCallback) {
        //文字列がヌルならfalse
        if (type == null) {
            return false;
        }
        if (crid == null) {
            return false;
        }
        //タイトルID type=dtv_vod の場合必須
        if (type.equals(CLIP_TYPE_DTV_VOD) && (titleId == null || titleId.length() < 1)) {
            return false;
        }

        //タイプ用の固定値をひとまとめにする
        List<String> typeList = makeStringArry(CLIP_TYPE_H4D_IPTV, CLIP_TYPE_H4D_VOD,
                CLIP_TYPE_DCH, CLIP_TYPE_DTV_VOD);

        if (typeList.indexOf(type) == -1) {
            //含まれていないならばfalse
            return false;
        }

        if (clipDeleteJsonParserCallback == null) {
            //コールバックがヌルならばfalse
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param type    タイプ　h4d_iptv：多チャンネル、h4d_vod：ビデオ、dch：dTVチャンネル、dtv_vod：dTV
     * @param crid    コンテンツ識別子
     * @param titleId タイトルID
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final String type, final String crid, final String titleId) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            //リクエストパラメータ(Json)作成
            JSONObject listItemJsonObject = new JSONObject();
            //タイプとコンテンツ識別子は常に必須
            listItemJsonObject.put(JsonConstants.META_RESPONSE_TYPE, type);
            listItemJsonObject.put(JsonConstants.META_RESPONSE_CRID, crid);

            //タイトルIDはdtv_vodの時のみ必須。dtv_vodで空文字の場合は既にエラーになっているので、ここでは空文字判定だけで良い
            if(!TextUtils.isEmpty(titleId)) {
                listItemJsonObject.put(JsonConstants.META_RESPONSE_TITLE_ID, titleId);
            }

            //統合して文字列化する
            JSONArray jsonListObject = new JSONArray();
            jsonListObject.put(0, listItemJsonObject);
            jsonObject.put(JsonConstants.META_RESPONSE_LIST, jsonListObject);
            answerText = jsonObject.toString().replace("\\", "");

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        DTVTLogger.debugHttp(answerText);
        return answerText;
    }
}
