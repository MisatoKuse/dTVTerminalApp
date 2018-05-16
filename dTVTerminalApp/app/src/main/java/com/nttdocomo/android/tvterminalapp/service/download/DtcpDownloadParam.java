/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

/**
 * DtcpDownloadParam.
 */
public class DtcpDownloadParam extends DownloadParam {
    /**アイテムID.*/
    private String mItemId;
    /**Dtcp1hos.*/
    private String mDtcp1host;  //e.g. 192.168.11.5
    /**Dtcp1port.*/
    private int mDtcp1port; //e.g. 57343
    /**Url.*/
    private String mUrl;    //e.g. "http://192.168.11.5:58890/web/video/pvr?id=15131320570000000041&quality=mobile"
    /**CleartextSize.*/
    private int mCleartextSize;
    /**ダウンロードするためのxml.*/
    private String mXmlToDownLoad;

    /**
     * Dtcp1host取得.
     * @return  Dtcp1host
     */
    public String getDtcp1host() {
        return mDtcp1host;
    }

    /**
     * Dtcp1host設定.
     * @param mDtcp1host  mDtcp1host
     */
    public void setDtcp1host(final String mDtcp1host) {
        this.mDtcp1host = mDtcp1host;
    }

    /**
     *アイテムID取得.
     * @return アイテムID
     */
    public String getItemId() {
        return mItemId;
    }

    /**
     * アイテムID設定.
     * @param itemId アイテムID
     */

    public void setItemId(final String itemId) {
        this.mItemId = itemId;
    }

    /**
     *Dtcp1port設定.
     * @return Dtcp1port
     */
    public int getDtcp1port() {
        return mDtcp1port;
    }

    /**
     *Dtcp1port設定.
     * @param mDtcp1port Dtcp1port
     */
    public void setDtcp1port(final int mDtcp1port) {
        this.mDtcp1port = mDtcp1port;
    }

    /**
     * Url取得.
     * @return Url
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Url設定.
     * @param mUrl Url
     */
    public void setUrl(final String mUrl) {
        this.mUrl = mUrl;
    }

    /**
     * ダウンロードサイズ取得.
     * @return ダウンロードサイズ
     */
    public int getCleartextSize() {
        return mCleartextSize;
    }

    /**
     * ダウンロードサイズ設定.
     * @param mCleartextSize ダウンロードサイズ
     */
    public void setCleartextSize(final int mCleartextSize) {
        this.mCleartextSize = mCleartextSize;
    }

    @Override
    public boolean isParamValid() {
        if (null == mDtcp1host || 7 > mDtcp1host.length()
                || 0 == mDtcp1port
                || null == mUrl || 1 > mUrl.length()
                || null == mXmlToDownLoad || mXmlToDownLoad.isEmpty()) {
            return false;
        }
        return super.isParamValid();
    }

    /**
     *ダウンロードするためのXml取得.
     * @return ダウンロードするためのXml
     */
    public String getXmlToDownLoad() {
        return mXmlToDownLoad;
    }

    /**
     * ダウンロードするためのXml設定.
     * @param xmlToDl ダウンロードするためのXml
     */
    public void setXmlToDownLoad(final String xmlToDl) {
        mXmlToDownLoad = xmlToDl;
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
