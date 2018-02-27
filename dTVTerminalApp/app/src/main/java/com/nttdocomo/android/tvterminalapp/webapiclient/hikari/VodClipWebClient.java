/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.JsonParserThread;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class VodClipWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback,
        JsonParserThread.JsonParser {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public VodClipWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onParserFinished(final Object parsedData) {
        //パース後のデータを返す
        if (null != mVodClipJsonParserCallback && parsedData instanceof List) {
            mVodClipJsonParserCallback.onVodClipJsonParsed((List<VodClipList>) parsedData);
        }
    }

    @Override
    public Object parse(final String body) {
        VodClipJsonParser vodClipJsonParser = new VodClipJsonParser();
        List<VodClipList> pursedData;
        pursedData = vodClipJsonParser.VodClipListSender(body);
        return pursedData;
    }

    /**
     * コールバック.
     */
    public interface VodClipJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param vodClipLists JSONパース後のデータ
         */
        void onVodClipJsonParsed(List<VodClipList> vodClipLists);
    }

    //コールバックのインスタンス
    private VodClipJsonParserCallback mVodClipJsonParserCallback;

    /**
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(final ReturnCode returnCode) {
        /*
        //パース後データ受け取り用
        List<VodClipList> pursedData;

        //JSONをパースする
        VodClipJsonParser vodClipJsonParser = new VodClipJsonParser();
        pursedData = vodClipJsonParser.VodClipListSender(returnCode.bodyData);

        //パース後のデータを返す
        mVodClipJsonParserCallback.onVodClipJsonParsed(pursedData);
        */
        Handler handler = new Handler();
        try {
            JsonParserThread t = new JsonParserThread(returnCode.bodyData, handler, this);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
            onError(returnCode);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        //エラーが発生したのでヌルを返す
        mVodClipJsonParserCallback.onVodClipJsonParsed(null);

    }

    /**
     * VODクリップ取得.
     *
     * @param ageReq                    視聴年齢制限値（1から17までの値）
     * @param upperPagetLimit           結果の最大件数（1以上）
     * @param lowerPagetLimit           結果の最小件数（1以上）
     * @param pagerOffset               取得位置
     * @param pagerDirection            取得方向（prev|next）
     * @param vodClipJsonParserCallback コールバック
     * @return パラメータ等に問題があった場合はfalse
     */
    public boolean getVodClipApi(final int ageReq, final int upperPagetLimit, final int lowerPagetLimit,
                                 final int pagerOffset, final String pagerDirection,
                                 final VodClipJsonParserCallback vodClipJsonParserCallback) {
        if (mIsCancel) {
            DTVTLogger.error("VodClipWebClient is stopping connection");
            return false;
        }
        //パラメーターのチェック
        if (!checkNormalParameter(ageReq, upperPagetLimit, lowerPagetLimit,
                pagerOffset, pagerDirection, vodClipJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、ヌルで帰る
            return false;
        }

        //コールバックの準備
        mVodClipJsonParserCallback = vodClipJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(ageReq, upperPagetLimit, lowerPagetLimit,
                pagerOffset, pagerDirection);

        //JSONの組み立てに失敗していれば、ヌルで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //VODクリップ一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.VOD_CLIP_LIST,
                sendParameter, this, null);

        //今のところ正常なので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param ageReq                    視聴年齢制限値
     * @param upperPagetLimit           結果の最大件数
     * @param lowerPagetLimit           　            結果の最小件数
     * @param pagerOffset               取得位置
     * @param pagerDirection            取得方向
     * @param vodClipJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final int ageReq, final int upperPagetLimit, final int lowerPagetLimit,
                                         final int pagerOffset, final String pagerDirection,
                                         final VodClipJsonParserCallback vodClipJsonParserCallback) {
        if (!(ageReq >= AGE_LOW_VALUE && ageReq <= AGE_HIGH_VALUE)) {
            //ageReqが1から17ではないならばfalse
            return false;
        }

        // 各値が下限以下ならばfalse
        if (upperPagetLimit < 1) {
            return false;
        }
        if (lowerPagetLimit < 1) {
            return false;
        }
        if (pagerOffset < 0) {
            return false;
        }

        //固定値をひとまとめにする
        List<String> typeList = makeStringArry(
                ClipUtils.DIRECTION_PREV, ClipUtils.DIRECTION_NEXT, "");

        if (typeList.indexOf(pagerDirection) == -1) {
            //含まれていないならばfalse
            return false;
        }

        //コールバックが含まれていないならばエラー
        if (vodClipJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param ageReq          視聴年齢制限値
     * @param upperPagetLimit 結果の最大件数
     * @param lowerPagetLimit 　結果の最小件数
     * @param pagerOffset     取得位置
     * @param pagerDirection  取得方向
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(final int ageReq, final int upperPagetLimit, final int lowerPagetLimit,
                                     final int pagerOffset, final String pagerDirection) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            jsonObject.put(JsonConstants.META_RESPONSE_AGE_REQ, ageReq);

            JSONObject jsonPagerObject = new JSONObject();

            jsonPagerObject.put(JsonConstants.META_RESPONSE_UPPER_LIMIT, upperPagetLimit);
            jsonPagerObject.put(JsonConstants.META_RESPONSE_LOWER_LIMIT, lowerPagetLimit);
            jsonPagerObject.put(JsonConstants.META_RESPONSE_OFFSET, pagerOffset);

            //取得方向は中身も必須なので、省略されていた場合はprevを指定する
            if (TextUtils.isEmpty(pagerDirection)) {
                jsonPagerObject.put(JsonConstants.META_RESPONSE_DIRECTION, ClipUtils.DIRECTION_PREV);
            } else {
                jsonPagerObject.put(JsonConstants.META_RESPONSE_DIRECTION, pagerDirection);
            }


            jsonObject.put(JsonConstants.META_RESPONSE_PAGER, jsonPagerObject);

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        DTVTLogger.debugHttp(answerText);
        return answerText;
    }

    /**
     * 通信を止める.
     */
    public void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        stopAllConnections();
    }

    /**
     * 通信可能状態にする.
     */
    public void enableConnection() {
        DTVTLogger.start();
        mIsCancel = false;
    }
}
