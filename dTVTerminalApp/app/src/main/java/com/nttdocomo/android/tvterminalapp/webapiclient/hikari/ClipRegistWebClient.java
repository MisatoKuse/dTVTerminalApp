/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ClipRegistJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ClipRegistWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック
     */
    public interface ClipRegistJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         */
        void onClipRegistResult();

        /**
         * 正常に終了した場合に呼ばれるコールバック
         */
        void onClipRegistFailure();
    }

    //コールバックのインスタンス
    private ClipRegistJsonParserCallback mClipRegistJsonParserCallback;

    /**
     * 通信成功時のコールバック
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        //JSONをパースして、データを返す
        new ClipRegistJsonParser(mClipRegistJsonParserCallback).execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック
     */
    @Override
    public void onError() {
        //エラーが発生したのでヌルを返す
        mClipRegistJsonParserCallback.onClipRegistFailure();
    }

    /**
     * チャンネル一覧取得
     *
     * @param type                         タイプ　h4d_iptv：多チャンネル、h4d_vod：ビデオ、dch：dTVチャンネル、dtv_vod：dTV
     * @param crid                         コンテンツ識別子
     * @param serviceId                    サービスID
     * @param eventId                      イベントID
     * @param titleId                      タイトルID
     * @param title                        コンテンツタイトル
     * @param r_value                      番組のパレンタル設定値
     * @param linearStartDate              放送開始日時
     * @param linearEndDate                放送終了日時
     * @param isNotify                     視聴通知するか否か
     * @param clipRegistJsonParserCallback callback
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getClipRegistApi(String type, String crid, String serviceId, String eventId,
                                    String titleId, String title, String r_value,
                                    String linearStartDate, String linearEndDate, boolean isNotify,
                                    ClipRegistJsonParserCallback clipRegistJsonParserCallback) {
        //パラメーターのチェック
        if (!checkParameter(type, crid, serviceId, eventId, titleId, title, r_value,
                linearStartDate, linearEndDate, isNotify, clipRegistJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //パラメータチェック終了後にlinearEndDateをyyyy/MM/dd HH:mm:ss形式に変換する
        if(linearEndDate != null && DBUtils.isNumber(linearEndDate)){
            linearEndDate = DateUtils.formatEpochToString(Long.parseLong(linearEndDate));
        }

        //コールバックの設定
        mClipRegistJsonParserCallback = clipRegistJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(type, crid, serviceId, eventId, titleId, title, r_value,
                linearStartDate, linearEndDate, isNotify);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //チャンネル一覧を呼び出す
        openUrl(UrlConstants.WebApiUrl.CLIP_REGISTER_GET_WEB_CLIENT, sendParameter, this);

        //現状失敗は無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param type                         タイプ　h4d_iptv：多チャンネル、h4d_vod：ビデオ、dch：dTVチャンネル、dtv_vod：dTV
     * @param crid                         コンテンツ識別子
     * @param serviceId                    サービスID
     * @param eventId                      イベントID
     * @param titleId                      タイトルID
     * @param title                        コンテンツタイトル
     * @param r_value                      番組のパレンタル設定値
     * @param linearStartDate              放送開始日時
     * @param linearEndDate                放送終了日時
     * @param isNotify                     視聴通知するか否か
     * @param clipRegistJsonParserCallback callback
     * @return 値がおかしいならばfalse
     */
    private boolean checkParameter(String type, String crid, String serviceId, String eventId,
                                   String titleId, String title, String r_value,
                                   String linearStartDate, String linearEndDate, boolean isNotify,
                                   ClipRegistJsonParserCallback clipRegistJsonParserCallback) {
        //文字列がヌルならfalse
        if (type == null) {
            return false;
        }
        if (crid == null) {
            return false;
        }
        //サービスID type=h4d_iptv、is_notify=true の場合必須
        if ((type.equals(CLIP_TYPE_H4D_IPTV) || isNotify) && (serviceId == null || serviceId.length() < 1)) {
            return false;
        }
        //イベントID type=h4d_iptv の場合必須
        if (type.equals(CLIP_TYPE_H4D_IPTV) && (eventId == null || eventId.length() < 1)) {
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

        //パラメータチェック後半
        return checkLatterHalfParameter(type, title, r_value, linearStartDate, linearEndDate,
                isNotify, clipRegistJsonParserCallback);
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック(FindBugs対応のため2分割)
     *
     * @param type            タイプ　h4d_iptv：多チャンネル、h4d_vod：ビデオ、dch：dTVチャンネル、dtv_vod：dTV
     * @param title           コンテンツタイトル
     * @param r_value         番組のパレンタル設定値
     * @param linearStartDate 放送開始日時
     * @param linearEndDate   放送終了日時
     * @param isNotify        視聴通知するか否か
     * @return 値がおかしいならばfalse
     */
    private boolean checkLatterHalfParameter(String type, String title, String r_value,
                                             String linearStartDate, String linearEndDate, boolean isNotify,
                                             ClipRegistJsonParserCallback clipRegistJsonParserCallback) {

        //コンテンツタイトル、番組のパレンタル設定値 type=h4d_iptv、h4d_vod、is_notify=true の場合必須
        if ((type.equals(CLIP_TYPE_H4D_IPTV) || type.equals(CLIP_TYPE_H4D_VOD)
                || isNotify) && (title == null || r_value == null || title.length() < 1
                || r_value.length() < 1)) {
            return false;
        }
        //放送開始日時、放送終了日時 type=h4d_iptv、dch の場合必須
        if ((type.equals(CLIP_TYPE_H4D_IPTV) || type.equals(CLIP_TYPE_DCH))
                && (linearStartDate == null || linearEndDate == null || linearStartDate.length()
                < 1 || linearEndDate.length() < 1)) {
            return false;
        }

        //タイプ用の固定値をひとまとめにする
        List<String> typeList = makeStringArry(CLIP_TYPE_H4D_IPTV, CLIP_TYPE_H4D_VOD,
                CLIP_TYPE_DCH, CLIP_TYPE_DTV_VOD);

        if (clipRegistJsonParserCallback == null) {
            //コールバックがヌルならばfalse
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     *
     * @param type            タイプ　h4d_iptv：多チャンネル、h4d_vod：ビデオ、dch：dTVチャンネル、dtv_vod：dTV
     * @param crid            コンテンツ識別子
     * @param serviceId       サービスID
     * @param eventId         イベントID
     * @param titleId         タイトルID
     * @param title           コンテンツタイトル
     * @param r_value         番組のパレンタル設定値
     * @param linearStartDate 放送開始日時
     * @param linearEndDate   放送終了日時
     * @param isNotify        視聴通知するか否か
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(String type, String crid, String serviceId, String eventId,
                                     String titleId, String title, String r_value,
                                     String linearStartDate, String linearEndDate, boolean isNotify) {
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
                jsonObject.put(JsonContents.META_RESPONSE_SERVICE_ID, serviceId);
                jsonObject.put(JsonContents.META_RESPONSE_EVENT_ID, eventId);
                jsonObject.put(JsonContents.META_RESPONSE_TITLE_ID, titleId);
                jsonObject.put(JsonContents.META_RESPONSE_TITLE, title);
                jsonObject.put(JsonContents.META_RESPONSE_R_VALUE, r_value);
                jsonObject.put(JsonContents.META_RESPONSE_LINEAR_START_DATE, linearStartDate);
                jsonObject.put(JsonContents.META_RESPONSE_LINEAR_END_DATE, linearEndDate);
                jsonObject.put(JsonContents.META_RESPONSE_IS_NOTIFY, isNotify);
            }
            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }
}
