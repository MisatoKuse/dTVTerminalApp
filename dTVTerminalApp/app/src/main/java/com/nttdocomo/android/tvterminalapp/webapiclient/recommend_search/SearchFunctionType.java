/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 * 検索機能指定.
 */
public enum SearchFunctionType {
    /**1:パーソナル検索機能.*/
    //1以外が指定されている場合はERMD08001（リクエストエラー）を返却する。
    none(1);
    /**検索機能タイプ値.*/
    private final int value;

    /**
     * コンストラクタ.
     * @param value 検索機能タイプ値
     */
    private SearchFunctionType(final int value) {
        this.value = value;
    }

    /**
     * valueOf.
     * @param value 検索機能タイプ値
     * @return 検索機能タイプ
     */
    public SearchFunctionType valueOf(final int value) {
        switch (value) {
            case 1:
                return SearchFunctionType.none;
            default:
                return null;
        }
    }

    /**
     * value.
     * @return 検索機能タイプ値
     */
    public int value() {
        return this.value;
    }
}
