/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

/**
 * ページャクラス.
 */
public class Pager {
    /**レスポンスの最大件数.*/
    private int limit;
    /**取得位置(1～).*/
    private int offset;
    /**レスポンス(list)件数.*/
    private int count;
    /**全体の件数.*/
    private int total;

    /**
     * レスポンスの最大件数取得.
     * @return レスポンスの最大件数
     */
    public int getLimit() {
        return limit;
    }

    /**
     * レスポンスの最大件数設定.
     * @param limit レスポンスの最大件数
     */
    public void setLimit(final int limit) {
        this.limit = limit;
    }

    /**
     * 取得位置(1～)取得.
     * @return 取得位置
     */
    public int getOffset() {
        return offset;
    }

    /**
     * 取得位置(1～)設定.
     * @param offset 取得位置(1～)
     */
    public void setOffset(final int offset) {
        this.offset = offset;
    }

    /**
     * レスポンス(list)件数取得.
     * @return レスポンス(list)件数
     */
    public int getCount() {
        return count;
    }

    /**
     * レスポンス(list)件数設定.
     * @param count レスポンス(list)件数
     */
    public void setCount(final int count) {
        this.count = count;
    }

    /**
     * 全体の件数取得.
     * @return 全体の件数
     */
    public int getTotal() {
        return total;
    }

    /**
     * 全体の件数設定.
     * @param total 全体の件数
     */
    public void setTotal(final int total) {
        this.total = total;
    }
}
