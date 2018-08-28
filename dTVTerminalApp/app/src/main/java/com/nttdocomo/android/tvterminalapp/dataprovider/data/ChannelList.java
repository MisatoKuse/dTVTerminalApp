/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * チャンネルリスト情報.
 */
public class ChannelList {
    /** WebAPIレスポンス情報(チャンネルリスト情報以外). */
    private HashMap<String, String> mResponseInfoMap = null;

    /** チャンネルリスト情報. */
    private List<Map<String, String>> mChannelList = null;

    /**
     * WebAPIレスポンス情報(チャンネルリスト情報以外)取得.
     * @return レスポンス情報
     */
    public HashMap getResponseInfoMap() {
        return mResponseInfoMap;
    }

    /**
     * WebAPIレスポンス情報(チャンネルリスト情報以外)設定.
     * @param responseInfoMap  レスポンス情報
     */
    public void setResponseInfoMap(final HashMap<String, String> responseInfoMap) {
        this.mResponseInfoMap = responseInfoMap;
    }

    /**
     * WebAPIレスポンス情報(チャンネルリスト情報以外)情報取得.
     * @return チャンネルリスト情報
     */
    public List<Map<String, String>> getChannelList() {
        return mChannelList;
    }

    /**
     * チャンネルリスト情報設定.
     * @param clList  チャンネルリスト情報
     */
    public void setChannelList(final List clList) {
        this.mChannelList = clList;
    }
}
