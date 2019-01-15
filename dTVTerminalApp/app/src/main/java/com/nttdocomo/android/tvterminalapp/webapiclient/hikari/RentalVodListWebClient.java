/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RentalVodListJsonParser;

/**
 * レンタルVODリスト取得用Webクライアント.
 */
public class RentalVodListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コールバック.
     */
    public interface RentalVodListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param RentalVodListResponse JSONパース後のデータ
         */
        void onRentalVodListJsonParsed(PurchasedVodListResponse RentalVodListResponse, ErrorState jsonParseError);
    }

    /**
     * コールバックのインスタンス.
     */
    private RentalVodListJsonParserCallback mRentalVodListJsonParserCallback;
    /**コンテキスト.*/
    private Context mContext = null;
    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public RentalVodListWebClient(final Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        if (mRentalVodListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new RentalVodListJsonParser(mContext, mRentalVodListJsonParserCallback)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, returnCode.bodyData);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        if (mRentalVodListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mRentalVodListJsonParserCallback.onRentalVodListJsonParsed(null, null);
        }
    }

    /**
     * レンタルビデオ情報一覧取得.
     *
     * @param rentalVodListJsonParserCallback コールバック
     * （本WebAPIには通常のパラメータが無く、基底クラスで追加するサービストークのみとなる。）
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getRentalVodListApi(final RentalVodListJsonParserCallback rentalVodListJsonParserCallback) {
        if (mIsCancel) {
            DTVTLogger.error("RentalVodListWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック
        if (!checkNormalParameter(rentalVodListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        mRentalVodListJsonParserCallback = rentalVodListJsonParserCallback;

        //レンタルビデオの情報を読み込むため、購入済みVOD一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.RENTAL_VOD_LIST_WEB_CLIENT, "", this, null);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param purchasedVodListCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final RentalVodListJsonParserCallback purchasedVodListCallback) {
        //コールバックが指定されていないならばfalse
        if (purchasedVodListCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
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
