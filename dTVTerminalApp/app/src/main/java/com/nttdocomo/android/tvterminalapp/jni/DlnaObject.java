/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

public class DlnaObject {
    /**
     * このクラスにて、フィールドは「public」に設定している理由は、.
     * １．JNIのC側で操作便利
     * ２．フィールドは追加すると、setとget方法をjavaとc側両方で増やさないよう
     */
    @SuppressWarnings("PublicField")
    public String mObjectId;
    @SuppressWarnings("PublicField")
    public String mCleartextSize;
    @SuppressWarnings("PublicField")
    public String mDate;
    @SuppressWarnings("PublicField")
    public String mXml;
    @SuppressWarnings("PublicField")
    public String mTitle;
    @SuppressWarnings("PublicField")
    public String mBitrate;
    @SuppressWarnings("PublicField")
    public String mChannelName;
    @SuppressWarnings("PublicField")
    public String mChannelNr;
    @SuppressWarnings("PublicField")
    public String mDuration;
    @SuppressWarnings("PublicField")
    public String mResolution;
    @SuppressWarnings("PublicField")
    public String mResUrl;
    @SuppressWarnings("PublicField")
    public String mSize;
    @SuppressWarnings("PublicField")
    public String mVideoType;
    @SuppressWarnings("PublicField")
    public String mRating;
    //増やす
    //public String name;
}
