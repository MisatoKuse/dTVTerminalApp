/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model;



public class ResultType<T> {

    private T mT;
    private boolean mSuccessful=false;

    public T getResultType() {
        return mT;
    }

    public void success(T t){
        mT=t;
        mSuccessful=true;
    }

    public void failure(T error) {
        mT=error;
        mSuccessful=false;
    }

    public boolean isSuccessful(){
        return mSuccessful;
    }

    //success,
    //failure;
}
