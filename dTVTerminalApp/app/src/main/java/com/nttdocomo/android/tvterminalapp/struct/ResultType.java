/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;


/**
 * リザルトタイプクラス.
 * @param <T>
 */
public class ResultType<T> {
    /**リザルトタイプ.*/
    private T mT;
    /**成功フラグ.*/
    private boolean mSuccessful = false;

    /**
     * リザルトタイプ取得.
     * @return リザルトタイプ
     */
    public T getResultType() {
        return mT;
    }

    /**
     * 成功コンストラクタ.
     * @param t リザルトタイプ
     */
    public void success(final T t) {
        mT = t;
        mSuccessful = true;
    }

    /**
     * 失敗コンストラクタ.
     * @param error error
     */
    public void failure(final T error) {
        mT = error;
        mSuccessful = false;
    }

    /**
     * 成功フラグ取得.
     * @return 成功フラグ
     */
    public boolean isSuccessful() {
        return mSuccessful;
    }

    //success,
    //failure;
}
