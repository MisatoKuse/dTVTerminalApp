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
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.JsonParserThread;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvClipJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * クリップ(TV)WebClient.
 */
public class TvClipWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback, JsonParserThread.JsonParser {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * 通信status.
     */
    private static final String STATUS  = "status";
    /**
     * 通信status.
     */
    private static final String STATUS_NG  = "NG";
    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public TvClipWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onParserFinished(final Object parsedData) {
        //パース後のデータを返す
        if (null != mTvClipJsonParserCallback && parsedData instanceof List) {
            mTvClipJsonParserCallback.onTvClipJsonParsed((List<TvClipList>) parsedData);
        }
    }

    @Override
    public Object parse(final String body) {
        TvClipJsonParser tvClipJsonParser = new TvClipJsonParser();
        List<TvClipList> pursedData;
        pursedData = tvClipJsonParser.tvClipListSender(body);
        return pursedData;
    }

    /**
     * コールバック.
     */
    public interface TvClipJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param tvClipLists JSONパース後のデータ
         */
        void onTvClipJsonParsed(List<TvClipList> tvClipLists);
    }

    /**
     * コールバックのインスタンス.
     */
    private TvClipJsonParserCallback mTvClipJsonParserCallback;

    /**
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(final ReturnCode returnCode) {
        try {
            JSONObject jsonObject = new JSONObject(returnCode.bodyData);
            if (jsonObject.has(STATUS) && jsonObject.get(STATUS).equals(STATUS_NG)) {
                onError(returnCode);
            } else {
                Handler handler = new Handler();
                try {
                    JsonParserThread thread = new JsonParserThread(returnCode.bodyData, handler, this);
                    thread.start();
                } catch (Exception e) {
                    DTVTLogger.debug(e);
                    onError(returnCode);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        mTvClipJsonParserCallback.onTvClipJsonParsed(null);

    }

    /**
     * TVクリップ取得.
     *
     * @param ageReq                   年齢制限の値 1から17を指定。範囲外の値は1or17に丸める
     * @param upperPagerLimit          結果の最大件数（1以上）
     * @param lowerPagerLimit          結果の最小件数（1以上）
     * @param pagerOffset              取得位置
     * @param pagerDirection           検索方向（prev|next）
     * @param tvClipJsonParserCallback コールバック
     * @return パラメータ等に問題があった場合はfalse
     */
    public boolean getTvClipApi(final int ageReq, final int upperPagerLimit, final int lowerPagerLimit,
                                final int pagerOffset, final String pagerDirection,
                                final TvClipJsonParserCallback tvClipJsonParserCallback) {
        if (mIsCancel) {
            DTVTLogger.error("TvClipWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック
        if (!checkNormalParameter(upperPagerLimit, lowerPagerLimit,
                pagerOffset, pagerDirection, tvClipJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、ヌルで帰る
            return false;
        }

        //コールバックの準備
        mTvClipJsonParserCallback = tvClipJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(
                ageReq, upperPagerLimit, lowerPagerLimit, pagerOffset, pagerDirection);

        //JSONの組み立てに失敗していれば、ヌルで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //TVクリップ一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.TV_CLIP_LIST,
                sendParameter, this, null);

        //今のところ正常なので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param upperPagerLimit          結果の最大件数
     * @param lowerPagerLimit          結果の最小件数
     * @param pagerOffset              取得位置
     * @param pagerDirection           取得方向
     * @param tvClipJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final int upperPagerLimit, final int lowerPagerLimit,
                                         final int pagerOffset, final String pagerDirection,
                                         final TvClipJsonParserCallback tvClipJsonParserCallback) {

        // 各値が下限以下ならばfalse
        if (upperPagerLimit < 1) {
            return false;
        }
        if (lowerPagerLimit < 1) {
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
        return tvClipJsonParserCallback != null;

    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする.
     *
     * @param ageReq          年齢制限の値 1から17を指定。範囲外の値は1or17に丸める
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
            //下のコメントを要確認
            //取得方向は中身も必須なので、省略されていた場合はprevを指定する
            if (TextUtils.isEmpty(pagerDirection)) {
                jsonPagerObject.put(JsonConstants.META_RESPONSE_DIRECTION, ClipUtils.DIRECTION_NEXT);
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
