/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ClipKeyListJsonParser;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.JsonParserThread;

import org.json.JSONException;
import org.json.JSONObject;

public class ClipKeyListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback, JsonParserThread.JsonParser {
    private ClipKeyListRequest mRequest = null;

    /**
     * コンテキストを継承元のコンストラクタに送る
     *
     * @param context コンテキスト
     */
    public ClipKeyListWebClient(Context context) {
        super(context);
    }

    @Override
    public void onParserFinished(Object parsedData) {
        //パース後のデータを返す
        if (null != mTvClipKeyListJsonParserCallback) {
            mTvClipKeyListJsonParserCallback.onTvClipKeyListJsonParsed((ClipKeyListResponse) parsedData);
        }
        //パース後のデータを返す
        if (null != mVodClipKeyListJsonParserCallback) {
            mVodClipKeyListJsonParserCallback.onVodClipKeyListJsonParsed((ClipKeyListResponse) parsedData);
        }
    }

    @Override
    public Object parse(String body) {
        ClipKeyListJsonParser clipKeyListJsonParser = new ClipKeyListJsonParser();
        ClipKeyListResponse pursedRespData;
        pursedRespData = clipKeyListJsonParser.clipKeyListSender(body);

        return pursedRespData;
    }

    /**
     * コールバック
     */
    public interface TvClipKeyListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param clipKeyListResponse JSONパース後のデータ
         */
        void onTvClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse);
    }

    /**
     * コールバック
     */
    public interface VodClipKeyListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック
         *
         * @param clipKeyListResponse JSONパース後のデータ
         */
        void onVodClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse);
    }

    //コールバックのインスタンス(TV)
    private TvClipKeyListJsonParserCallback mTvClipKeyListJsonParserCallback;
    //コールバックのインスタンス(VOD)
    private VodClipKeyListJsonParserCallback mVodClipKeyListJsonParserCallback;

    /**
     * 通信成功時のコールバック
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(ReturnCode returnCode) {
        Handler handler = new Handler();
        try {
            JsonParserThread thread = new JsonParserThread(returnCode.bodyData, handler, this);
            thread.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
            onError();
        }
    }

    /**
     * 通信失敗時のコールバック
     */
    @Override
    public void onError() {
        //エラーが発生したのでレスポンスデータにnullを設定してを返す
        if (null != mTvClipKeyListJsonParserCallback) {
            mTvClipKeyListJsonParserCallback.onTvClipKeyListJsonParsed(null);
        }
        if (null != mVodClipKeyListJsonParserCallback) {
            mVodClipKeyListJsonParserCallback.onVodClipKeyListJsonParsed(null);
        }
    }

    /**
     * クリップキー一覧取得
     *
     * @param requestParam                     クリップキー一覧リクエスト
     * @param tvClipKeyListJsonParserCallback  コールバック
     * @param vodClipKeyListJsonParserCallback コールバック
     * @return パラメータ等に問題があった場合はfalse
     */
    public boolean getClipKeyListApi(ClipKeyListRequest requestParam,
                                     TvClipKeyListJsonParserCallback tvClipKeyListJsonParserCallback,
                                     VodClipKeyListJsonParserCallback vodClipKeyListJsonParserCallback) {
        mRequest = requestParam;
        //パラメーターのチェック
        if (!checkNormalParameter(requestParam, tvClipKeyListJsonParserCallback, vodClipKeyListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、ヌルで帰る
            return false;
        }

        //コールバックの準備
        mTvClipKeyListJsonParserCallback = tvClipKeyListJsonParserCallback;
        mVodClipKeyListJsonParserCallback = vodClipKeyListJsonParserCallback;

        //送信用パラメータの作成
        String sendParameter = makeSendParameter(requestParam);

        //JSONの組み立てに失敗していれば、ヌルで帰る
        if (sendParameter.isEmpty()) {
            return false;
        }

        //クリップキー一覧を呼び出す
        openUrl(UrlConstants.WebApiUrl.CLIP_KEY_LIST_WEB_CLIENT,
                sendParameter, this);

        //今のところ正常なので、trueで帰る
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック
     *
     * @param requestParam                     リクエストパラメータ
     * @param tvClipKeyListJsonParserCallback  コールバック
     * @param vodClipKeyListJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(ClipKeyListRequest requestParam,
                                         TvClipKeyListJsonParserCallback tvClipKeyListJsonParserCallback,
                                         VodClipKeyListJsonParserCallback vodClipKeyListJsonParserCallback) {
        // 必須項目に値が入っていなければエラー
        if (ClipKeyListRequest.DEFAULT_STRING.equals(requestParam.getType())) {
            return false;
        }

        //コールバックが含まれていないならばエラー
        if (tvClipKeyListJsonParserCallback == null
                && vodClipKeyListJsonParserCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }

    /**
     * 指定されたパラメータをJSONで組み立てて文字列にする
     *
     * @param requestParam リクエストパラメータ
     * @return 組み立て後の文字列
     */
    private String makeSendParameter(ClipKeyListRequest requestParam) {
        JSONObject jsonObject = new JSONObject();
        String answerText;
        try {
            jsonObject.put(JsonContents.META_RESPONSE_TYPE, requestParam.getType());
            jsonObject.put(JsonContents.META_RESPONSE_IS_FORCE, requestParam.getIsForce());

            answerText = jsonObject.toString();

        } catch (JSONException e) {
            //JSONの作成に失敗したので空文字とする
            answerText = "";
        }

        return answerText;
    }
}
