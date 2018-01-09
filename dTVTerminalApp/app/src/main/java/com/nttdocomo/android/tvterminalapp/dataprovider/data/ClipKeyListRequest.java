/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

public class ClipKeyListRequest {

    private final String DEFAULT_STRING = "";

    private String mType = DEFAULT_STRING;
    private String mIsForce = DEFAULT_STRING;
    private String mCrid = DEFAULT_STRING;
    private String mServiceId = DEFAULT_STRING;
    private String mEventId = DEFAULT_STRING;
    private String mContentsType = DEFAULT_STRING;
    private String mTitleId = DEFAULT_STRING;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getIsForce() {
        return mIsForce;
    }

    public void setIsForce(String isForce) {
        mIsForce = isForce;
    }

    public String getCrid() {
        return mCrid;
    }

    public void setCrid(String mCrid) {
        this.mCrid = mCrid;
    }

    public String getServiceId() {
        return mServiceId;
    }

    public void setServiceId(String serviceId) {
        mServiceId = serviceId;
    }

    public String getEventId() {
        return mEventId;
    }

    public void setEventId(String eventId) {
        mEventId = eventId;
    }

    public String getContentsType() {
        return mContentsType;
    }

    public void setContentsType(String contentsType) {
        mContentsType = contentsType;
    }

    public String getTitleId() {
        return mTitleId;
    }

    public void setTitleId(String titleId) {
        mTitleId = titleId;
    }
}
