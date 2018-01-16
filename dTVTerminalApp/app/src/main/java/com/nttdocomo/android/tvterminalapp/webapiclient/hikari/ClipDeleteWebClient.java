/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ClipDeleteJsonParser;

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
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new ClipDeleteJsonParser(mClipDeleteJsonParserCallback).execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     */
    @Override
    public void onError() {
        //エラーが発生したのでヌルを返す
        mClipDeleteJsonParserCallback.onClipDeleteFailure();
    }

    /**
     * チャンネル一覧取得.
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
        openUrl(UrlConstants.WebApiUrl.CLIP_DELETE_GET_WEB_CLIENT, sendParameter, this);

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
            if (StringUtil.isHikariContents(type)) {
                //ひかりコンテンツ(dCh含む)
                jsonObject.put(JsonContents.META_RESPONSE_CRID, crid);
            } else if (StringUtil.isHikariInDtvContents(type)) {
                //ひかり内dTVコンテンツ(VODメタのdTVフラグが1)
                jsonObject.put(JsonContents.META_RESPONSE_CRID, crid);
                jsonObject.put(JsonContents.META_RESPONSE_TITLE_ID, titleId);
            } else {
                //その他
                jsonObject.put(JsonContents.META_RESPONSE_TYPE, type);
                jsonObject.put(JsonContents.META_RESPONSE_CRID, crid);
                jsonObject.put(JsonContents.META_RESPONSE_TITLE_ID, titleId);
            }

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }
}
