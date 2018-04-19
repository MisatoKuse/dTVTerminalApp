/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;
/**
 * ClipKeyListRequest Activity.
 */
public class ClipKeyListRequest {
    /**DEFAULT_STRING.*/
    public static final String DEFAULT_STRING = "";
    /**CLIP_KEY_LIST_REQUEST_TYPE_TV.*/
    public static final String CLIP_KEY_LIST_REQUEST_TYPE_TV = "tv";
    /**CLIP_KEY_LIST_REQUEST_TYPE_VOD.*/
    public static final String CLIP_KEY_LIST_REQUEST_TYPE_VOD = "vod";

    /**タイプ.*/
    private String mType;
    /** 初回取得フラグ.*/
    private boolean mIsForce = false;
    /**コンテンツ識別子.*/
    private String mCrid = DEFAULT_STRING;
    /**サービスID.*/
    private String mServiceId = DEFAULT_STRING;
    /**イベントID.*/
    private String mEventId = DEFAULT_STRING;
    /**見逃しタイプ.*/
    private String mContentsType = DEFAULT_STRING;
    /**タイトルID（dTV）.*/
    private String mTitleId = DEFAULT_STRING;

    /**
     * リクエストタイプ.
     */
    public enum REQUEST_PARAM_TYPE {
        /**
         * TVタイプ.
         */
        TV,
        /**
         * VODタイプ.
         */
        VOD,
    }
    /**
     * TV種別設定.
     * @param type TV種別
     */
    public ClipKeyListRequest(final REQUEST_PARAM_TYPE type) {
        switch (type) {
            case TV:
                mType = CLIP_KEY_LIST_REQUEST_TYPE_TV;
                break;
            case VOD:
                mType = CLIP_KEY_LIST_REQUEST_TYPE_VOD;
                break;
            default:
                mType = DEFAULT_STRING;
                break;
        }
    }

    /**
     * タイプ取得する.
     * @return タイプ
     */
    public String getType() {
        return mType;
    }
    /**
     * タイプ設定する.
     * @param type タイプ
     */
    public void setType(final String type) {
        mType = type;
    }

    /**
     * 初回取得フラグ取得.
     * @return true　or　false
     */
    public boolean getIsForce() {
        return mIsForce;
    }

    /**
     * 初回取得フラグ設定する.
     * @param isForce 初回取得フラグ
     */
    public void setIsForce(final boolean isForce) {
        mIsForce = isForce;
    }

    /**
     * コンテンツ識別子取得.
     * @return コンテンツ識別子
     */
    public String getCrid() {
        return mCrid;
    }

    /**
     * コンテンツ識別子設定する.
     * @param mCrid コンテンツ識別子
     */
    public void setCrid(final String mCrid) {
        this.mCrid = mCrid;
    }
    /**
     * サービスID取得.
     *@return サービスID
     */
    public String getServiceId() {
        return mServiceId;
    }

    /**
     * サービスID設定 .
     * @param serviceId サービスID
     */
    public void setServiceId(final String serviceId) {
        mServiceId = serviceId;
    }

    /**
     * イベントID取得.
     * @return イベントID
     */
    public String getEventId() {
        return mEventId;
    }

    /**
     * イベントID設定.
     * @param eventId イベントID
     */
    public void setEventId(final String eventId) {
        mEventId = eventId;
    }

    /**
     * 見逃しタイプ取得 .
     * @return 見逃しタイプ
     */
    public String getContentsType() {
        return mContentsType;
    }

    /**
     * 見逃しタイプ設定する.
     * @param contentsType 見逃しタイプ
     */
    public void setContentsType(final String contentsType) {
        mContentsType = contentsType;
    }

    /**
     * タイトルID（dTV）取得.
     * @return イベントID
     */
    public String getTitleId() {
        return mTitleId;
    }

    /**
     * タイトルID（dTV）設定する.
     * @param titleId タイトルID（dTV）
     */
    public void setTitleId(final String titleId) {
        mTitleId = titleId;
    }
}
