/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RentalChListJsonParser;

/**
 * 購入済みCH一覧リストを取得する.
 */
public class RentalChListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

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

    @Override
    public void onAnswer(final ReturnCode returnCode) {
        if (mRentalChListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new RentalChListJsonParser(mRentalChListJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    @Override
    public void onError() {
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
        //パラメーターのチェック
        if (!checkNormalParameter(rentalChListJsonParserCallback)) {
            //パラメーターがおかしければ通信不能なので、falseで帰る
            return false;
        }

        //コールバックのセット
        mRentalChListJsonParserCallback = rentalChListJsonParserCallback;

        //購入済みCH一覧を呼び出す
        openUrl(UrlConstants.WebApiUrl.RENTAL_CH_LIST_WEB_CLIENT, "", this);

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
}
