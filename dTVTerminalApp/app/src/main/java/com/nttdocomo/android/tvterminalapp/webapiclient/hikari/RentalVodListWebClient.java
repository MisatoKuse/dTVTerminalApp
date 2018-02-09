/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RentalVodListJsonParser;

public class RentalVodListWebClient
        extends WebApiBasePlala implements WebApiBasePlala.WebApiBasePlalaCallback {

    /**
     * コールバック.
     */
    public interface RentalVodListJsonParserCallback {
        /**
         * 正常に終了した場合に呼ばれるコールバック.
         *
         * @param RentalVodListResponse JSONパース後のデータ
         */
        void onRentalVodListJsonParsed(PurchasedVodListResponse RentalVodListResponse);
    }

    //コールバックのインスタンス
    private RentalVodListJsonParserCallback mRentalVodListJsonParserCallback;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public RentalVodListWebClient(Context context) {
        super(context);
    }

    @Override
    public void onAnswer(ReturnCode returnCode) {
        if(mRentalVodListJsonParserCallback != null) {
            //JSONをパースして、データを返す
            new RentalVodListJsonParser(mRentalVodListJsonParserCallback)
                    .execute(returnCode.bodyData);
        }
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(ReturnCode returnCode) {
        if(mRentalVodListJsonParserCallback != null) {
            //エラーが発生したのでヌルを返す
            mRentalVodListJsonParserCallback.onRentalVodListJsonParsed(null);
        }
    }

    /**
     * レンタルビデオ情報一覧取得.
     *
     * @param rentalVodListJsonParserCallback コールバックTODO:
     *                                           （本WebAPIには通常のパラメータが無く、基底クラスで追加するサービストークのみとなる。）
     *                                           TODO: 仕様確定後に基底クラスへサービストークンの処理の追加が必要
     * @return パラメータエラー等が発生した場合はfalse
     */
    public boolean getRentalVodListApi(RentalVodListJsonParserCallback rentalVodListJsonParserCallback) {
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
    private boolean checkNormalParameter(RentalVodListJsonParserCallback purchasedVodListCallback) {
        //コールバックが指定されていないならばfalse
        if (purchasedVodListCallback == null) {
            return false;
        }

        //何もエラーが無いのでtrue
        return true;
    }
}
