/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

public class OtherContentsDetailData {

    //変数名はぷららサーバインタフェース仕様書からの引用
    private String mTitle;
    private String mThumb;
    private String mDtv;
    private String mSynop;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getThumb() {
        return mThumb;
    }

    public void setThumb(String mThumb) {
        this.mThumb = mThumb;
    }

    public String getDtv() {
        return mDtv;
    }

    public void setDtv(String mDtv) {
        this.mDtv = mDtv;
    }

    public String getSynop() {
        return mSynop;
    }

    public void setSynop(String mSynop) {
        this.mSynop = mSynop;
    }
}
