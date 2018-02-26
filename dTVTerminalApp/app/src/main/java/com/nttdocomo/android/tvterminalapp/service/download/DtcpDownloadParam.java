/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

/**
 * todo 作成中.
 */
public class DtcpDownloadParam extends DownloadParam {
    private String mItemId;
    private String mDtcp1host;  //e.g. 192.168.11.5
    private int mDtcp1port; //e.g. 57343
    private String mUrl;    //e.g. "http://192.168.11.5:58890/web/video/pvr?id=15131320570000000041&quality=mobile"
    private int mCleartextSize;
    private String mXmlToDl;

    public String getDtcp1host() {
        return mDtcp1host;
    }

    public void setDtcp1host(final String mDtcp1host) {
        this.mDtcp1host = mDtcp1host;
    }

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(final String itemId) {
        this.mItemId = itemId;
    }

    public int getDtcp1port() {
        return mDtcp1port;
    }

    public void setDtcp1port(final int mDtcp1port) {
        this.mDtcp1port = mDtcp1port;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(final String mUrl) {
        this.mUrl = mUrl;
    }

    public int getCleartextSize() {
        return mCleartextSize;
    }

    public void setCleartextSize(final int mCleartextSize) {
        this.mCleartextSize = mCleartextSize;
    }

    @Override
    public boolean isParamValid() {
        if (null == mDtcp1host || 7 > mDtcp1host.length()
                || 0 == mDtcp1port
                || null == mUrl || 1 > mUrl.length()
                || null == mXmlToDl || mXmlToDl.isEmpty()) {
            return false;
        }
        return super.isParamValid();
    }

    public String getXmlToDl() {
        return mXmlToDl;
    }

    public void setXmlToDl(final String xmlToDl) {
        mXmlToDl = xmlToDl;
    }

    /**
     * DLサイズを戻す.
     * @return dl size
     */
    @Override
    public int getTotalSizeToDl() {
        return getCleartextSize();
    }
}
