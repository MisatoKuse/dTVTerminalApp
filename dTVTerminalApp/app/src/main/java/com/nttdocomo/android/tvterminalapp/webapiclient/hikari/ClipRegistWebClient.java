/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ClipRegistJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * クリップ登録処理.
 */
public class ClipRegistWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック.
     */
    public interface ClipRegistJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         */
        void onClipRegistResult();

        /**
         * 正常に終了した場合に呼ばれるコールバック.
         */
        void onClipRegistFailure();
    }

    /**コールバックのインスタンス.*/
    private ClipRegistJsonParserCallback mClipRegistJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public ClipRegistWebClient(final Context context) {
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
        new ClipRegistJsonParser(mClipRegistJsonParserCallback).execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        //エラーが発生したのでヌルを返す(専用のエラーメッセージがあるので、ネットワークエラーは送らない)
        mClipRegistJsonParserCallback.onClipRegistFailure();
    }

    /**
     * クリップ登録取得.
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
    public boolean getClipRegistApi(final String type, final String crid, final String serviceId,
                                    final String eventId, final String titleId, final String title,
                                    final String r_value, final String linearStartDate,
                                    final String linearEndDate, final boolean isNotify,
                                    final ClipRegistJsonParserCallback clipRegistJsonParserCallback) {
        //パラメーターのチェック
        if (!checkParameter(type, crid, serviceId, eventId, titleId, title, r_value,
                linearStartDate, linearEndDate, isNotify, clipRegistJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        String strStartDate = linearStartDate;
        String strEndDate = linearEndDate;
        if (strStartDate != null && DBUtils.isNumber(strStartDate) && !strStartDate.equals("0")) {
            strStartDate = DateUtils.formatEpochToString(Long.parseLong(strStartDate), null);
        } else {
            strStartDate = null;
        }
        if (strEndDate != null && DBUtils.isNumber(strEndDate) && !strEndDate.equals("0")) {
            strEndDate = DateUtils.formatEpochToString(Long.parseLong(strEndDate), null);
        } else {
            strEndDate = null;
        }

        //コールバックの設定
        mClipRegistJsonParserCallback = clipRegistJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(type, crid, serviceId, eventId, titleId, title, r_value,
                strStartDate, strEndDate, isNotify);

        //JSONの組み立てに失敗していれば、falseで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //チャンネル一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.CLIP_REGISTER_GET_WEB_CLIENT, sendParameter,
                this, null);

        //現状失敗は無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
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
    private boolean checkParameter(final String type, final String crid, final String serviceId,
                                   final String eventId, final String titleId, final String title,
                                   final String r_value, final String linearStartDate,
                                   final String linearEndDate, final boolean isNotify,
                                   final ClipRegistJsonParserCallback clipRegistJsonParserCallback) {
        //文字列がヌルならfalse
        if (type == null) {
            return false;
        }
        //crIdは必須
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
     * 指定されたパラメータがおかしいかどうかのチェック(FindBugs対応のため2分割).
     *
     * @param type                         タイプ　h4d_iptv：多チャンネル、h4d_vod：ビデオ、dch：dTVチャンネル、dtv_vod：dTV
     * @param title                        コンテンツタイトル
     * @param r_value                      番組のパレンタル設定値
     * @param linearStartDate              放送開始日時
     * @param linearEndDate                放送終了日時
     * @param isNotify                     視聴通知するか否か
     * @param clipRegistJsonParserCallback callback
     * @return 値がおかしいならばfalse
     */
    private boolean checkLatterHalfParameter(final String type, final String title,
                                             final String r_value, final String linearStartDate,
                                             final String linearEndDate, final boolean isNotify,
                                             final ClipRegistJsonParserCallback clipRegistJsonParserCallback) {

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

        if (clipRegistJsonParserCallback == null) {
            //コールバックがヌルならばfalse
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
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
    private String makeSendParameter(final String type, final String crid, final String serviceId,
                                     final String eventId, final String titleId, final String title,
                                     final String r_value, final String linearStartDate,
                                     final String linearEndDate, final boolean isNotify) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        //リクエストパラメータ(Json)作成
        try {
            putSelect(jsonObject, JsonConstants.META_RESPONSE_CRID, crid);
            putSelect(jsonObject, JsonConstants.META_RESPONSE_SERVICE_ID, serviceId);
            putSelect(jsonObject, JsonConstants.META_RESPONSE_TITLE_ID, titleId);
            putSelect(jsonObject, JsonConstants.META_RESPONSE_TITLE, title);
            putSelect(jsonObject, JsonConstants.META_RESPONSE_TYPE, type);
            putSelect(jsonObject, JsonConstants.META_RESPONSE_EVENT_ID, eventId);
            putSelect(jsonObject, JsonConstants.META_RESPONSE_R_VALUE, r_value);
            //isNotifyは事実上無いことが無い
            jsonObject.put(JsonConstants.META_RESPONSE_IS_NOTIFY, isNotify);
            putSelect(jsonObject, JsonConstants.META_RESPONSE_LINEAR_START_DATE, linearStartDate);
            putSelect(jsonObject, JsonConstants.META_RESPONSE_LINEAR_END_DATE, linearEndDate);

            answerText = jsonObject.toString().replace("\\", "");

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        DTVTLogger.debugHttp(answerText);
        return answerText;
    }

    /**
     * 値があれば追加を行う.
     *
     * 各項目の必須項目の有無はチェック済みで、各項目の必須ではない項目は指定不能とは書いていない。
     * その為、ここで空の項目の追加スキップを行えば済む
     * @param jsonObject 追記対象のjsonObject
     * @param keyString 追記キー
     * @param stringValue 追記内容
     * @throws JSONException JSONのエラーは呼び出し元で処理をする
     */
    private void putSelect(final JSONObject jsonObject,
                           final String keyString, final String stringValue) throws JSONException {
        if (!TextUtils.isEmpty(stringValue)) {
            //値があればjsonObjectに追加する
            jsonObject.put(keyString, stringValue);
        }
    }
}
