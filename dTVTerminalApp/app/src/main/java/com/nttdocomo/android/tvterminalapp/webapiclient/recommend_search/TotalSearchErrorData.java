/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;

//response

/**
 *検索エラー情報.
 */
@SuppressWarnings("PublicField")
public class TotalSearchErrorData {
    //単純な構造体としての役割のクラスの為、メンバはpublicとする
    /**.*/
    public String status;
    /**エラー情報.*/
    public final ErrorResultData error;

    /**
     * エラー結果データクラス.
     */
    public static class ErrorResultData {
        /**エラーID.*/
        public String id;
        /**エラー原因.*/
        public String param;
    }

    /**
     *インスタンス.
     */
    public TotalSearchErrorData() {
        error = new ErrorResultData();
    }

    /**
     * コンストラクタ.
     * @param id エラーID
     * @param param エラー原因
     */
    public TotalSearchErrorData(final String id, final String param) {
        error = new ErrorResultData();
        error.id = id;
        error.param = param;
        status = DtvtConstants.SEARCH_STATUS_NG;
    }

}
