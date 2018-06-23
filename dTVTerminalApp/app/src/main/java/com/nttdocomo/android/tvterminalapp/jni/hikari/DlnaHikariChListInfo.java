/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.hikari;

import java.util.ArrayList;

/**
 * ひかりチャンネルリスト情報.
 */
public class DlnaHikariChListInfo {
    /**ひかりチャンネルリスト.*/
    private final ArrayList<DlnaHikariChListItem> mLists = new ArrayList<>();

    /**
     * addItem.
     * @param item item
     */
    private void addItem(final DlnaHikariChListItem item) {
        mLists.add(item);
    }

    /**
     * 全リストclear.
     */
    public void clearAll() {
        mLists.clear();
    }

    /**
     * リストsize.
     * @return size
     */
    public int size() {
        if (null != mLists) {
            return mLists.size();
        }

        return 0;
    }

    /**
     * インデックス取得.
     * @param index インデックス
     * @return インデックス
     */
    public DlnaHikariChListItem get(final int index) {
        int destIndex = index;
        if (index < 0) {
            destIndex = 0;
        }
        if (null == mLists || 0 == mLists.size()) {
            return null;
        }

        return mLists.get(destIndex);
    }

    /**
     * fromArrayList.
     * @param content content
     * @return info
     */
    public static DlnaHikariChListInfo fromArrayList(final ArrayList<Object> content) {
        if (null == content) {
            return null;
        }
        DlnaHikariChListInfo info = new DlnaHikariChListInfo();
        for (Object o: content) {
            info.addItem((DlnaHikariChListItem) o);
        }

        return info;
    }

    /**
     * getHikariChLists.
     * @return HikariChLists
     */
    public ArrayList<DlnaHikariChListItem> getHikariChLists() {
        return mLists;
    }
}
