/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RentalChListJsonParser;

/**
 * 購入済みCH一覧リストを取得する.
 */
public class RentalChListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コールバック.
     */
    public interface RentalChListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param RentalChListResponse JSONパース後のデータ
         */
        void onRentalChListJsonParsed(PurchasedChListResponse RentalChListResponse);
    }

    /**
     * コールバックのインスタンス.
     */
    private RentalChListJsonParserCallback mRentalChListJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public RentalChListWebClient(final Context context) {
        super(context);
    }

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        if (mRentalChListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new RentalChListJsonParser(mRentalChListJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        if (mRentalChListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mRentalChListJsonParserCallback.onRentalChListJsonParsed(null);
        }
    }

    /**
     * 購入済みCH情報一覧取得.
     *
     * @param rentalChListJsonParserCallback コールバック
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getRentalChListApi(final RentalChListJsonParserCallback rentalChListJsonParserCallback) {

        if (mIsCancel) {
            DTVTLogger.error("RentalChListWebClient is stopping connection");
            return false;
        }

        //パラメーターのチェック
        if (!checkNormalParameter(rentalChListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        mRentalChListJsonParserCallback = rentalChListJsonParserCallback;

        //購入済みCH一覧を呼び出す
        openUrlAddOtt(UrlConstants.WebApiUrl.RENTAL_CH_LIST_WEB_CLIENT, "", this, null);

        //今のところ失敗していないので、trueを返す
        return true;
    }

    /**
     * 指定されたパラメータがおかしいかどうかのチェック.
     *
     * @param rentalChListJsonParserCallback コールバック
     * @return 値がおかしいならばfalse
     */
    private boolean checkNormalParameter(final RentalChListJsonParserCallback rentalChListJsonParserCallback) {
        //コールバックが指定されていないならばfalse
        if (rentalChListJsonParserCallback == null) {
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
