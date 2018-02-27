/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;



public class ResultType<T> {

    private T mT;
    private boolean mSuccessful = false;

    public T getResultType() {
        return mT;
    }

    public void success(final T t) {
        mT = t;
        mSuccessful = true;
    }

    public void failure(final T error) {
        mT = error;
        mSuccessful = false;
    }

    public boolean isSuccessful(){
        return mSuccessful;
    }

    //success,
    //failure;
}
