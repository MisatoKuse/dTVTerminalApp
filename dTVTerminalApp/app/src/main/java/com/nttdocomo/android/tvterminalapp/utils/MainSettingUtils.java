/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

public class MainSettingUtils {
    private String mText;
    private String mStateText;
    private Boolean mIsForward;
    private Boolean mIsCategory;

    public MainSettingUtils() {
        mText = "";
        mStateText = "";
        mIsForward = false;
        mIsCategory = false;
    }

    public MainSettingUtils(String text, String stateText, Boolean forward, Boolean category) {
        mText = text;
        mStateText = stateText;
        mIsForward = forward;
        mIsCategory = category;
    }

    public String getText() {
        return mText;
    }

    public String getStateText() {
        return mStateText;
    }

    public Boolean isArrow() {
        return mIsForward;
    }

    public Boolean isCategory() {
        return mIsCategory;
    }

    public void setText(String text) {
        mText = text;
    }

    public void setStateText(String stateText) {
        mStateText = stateText;
    }

    public void setIsArrow(Boolean forward) {
        mIsForward = forward;
    }

    public void setIsCategory(Boolean category) {
        mIsCategory = category;
    }
}
