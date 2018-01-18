/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

/**
 * 設定画面用データ.
 */
public class MainSettingUtils {
    /**
     * 項目名.
     */
    private String mText;
    /**
     * ステータス.
     */
    private String mStateText;

    /**
     * コンストラクタ.
     */
    public MainSettingUtils() {
        mText = "";
        mStateText = "";
    }

    /**
     * コンストラクタ.
     *
     * @param text 項目名
     * @param stateText ステータス
     */
    public MainSettingUtils(final String text, final String stateText) {
        mText = text;
        mStateText = stateText;
    }

    /**
     * 項目名を取得.
     *
     * @return 項目名
     */
    public String getText() {
        return mText;
    }

    /**
     * ステータスを取得.
     *
     * @return ステータス
     */
    public String getStateText() {
        return mStateText;
    }

    /**
     * 項目名を保存.
     *
     * @param text 項目名
     */
    public void setText(final String text) {
        mText = text;
    }

    /**
     * ステータスを保存.
     *
     * @param stateText ステータス
     */
    public void setStateText(final String stateText) {
        mStateText = stateText;
    }
}
