/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

public class ClipKeyListRequest {

    public static final String DEFAULT_STRING = "";
    public static final String CLIP_KEY_LIST_REQUEST_TYPE_TV = "tv";
    public static final String CLIP_KEY_LIST_REQUEST_TYPE_VOD = "vod";

    private String mType;
    private boolean mIsForce = false;
    private String mCrid = DEFAULT_STRING;
    private String mServiceId = DEFAULT_STRING;
    private String mEventId = DEFAULT_STRING;
    private String mContentsType = DEFAULT_STRING;
    private String mTitleId = DEFAULT_STRING;

    public enum REQUEST_PARAM_TYPE {
        TV,
        VOD,
    }

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

    public String getType() {
        return mType;
    }

    public void setType(final String type) {
        mType = type;
    }

    public boolean getIsForce() {
        return mIsForce;
    }

    public void setIsForce(final boolean isForce) {
        mIsForce = isForce;
    }

    public String getCrid() {
        return mCrid;
    }

    public void setCrid(final String mCrid) {
        this.mCrid = mCrid;
    }

    public String getServiceId() {
        return mServiceId;
    }

    public void setServiceId(final String serviceId) {
        mServiceId = serviceId;
    }

    public String getEventId() {
        return mEventId;
    }

    public void setEventId(final String eventId) {
        mEventId = eventId;
    }

    public String getContentsType() {
        return mContentsType;
    }

    public void setContentsType(final String contentsType) {
        mContentsType = contentsType;
    }

    public String getTitleId() {
        return mTitleId;
    }

    public void setTitleId(final String titleId) {
        mTitleId = titleId;
    }
}
