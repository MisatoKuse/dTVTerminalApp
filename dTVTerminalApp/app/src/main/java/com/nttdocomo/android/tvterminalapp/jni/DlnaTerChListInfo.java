/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import java.util.ArrayList;

/**
 * 地上波チャンネルリスト情報管理クラス.
 */
public class DlnaTerChListInfo {
    /**地上波チャンネルリスト.*/
    private ArrayList<DlnaTerChListItem> mLists = new ArrayList<>();

    /**
     * addItem.
     * @param item item
     */
    private void addItem(final DlnaTerChListItem item) {
        mLists.add(item);
    }

    /**
     * clearAll.
     */
    public void clearAll() {
        mLists.clear();
    }

    /**
     * size.
     * @return size
     */
    public int size() {
        if (null != mLists) {
            return mLists.size();
        }

        return 0;
    }

    /**
     * 地上波チャンネルリストアイテム取得.
     * @param index index
     * @return  地上波チャンネルリストアイテム
     */
    public DlnaTerChListItem get(final int index) {
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
     * c/c++からチャンネル情報を要求.
     * @param content content
     * @return チャンネル情報
     */
    static DlnaTerChListInfo fromArrayList(final ArrayList<Object> content) {
        if (null == content) {
            return null;
        }
        DlnaTerChListInfo info = new DlnaTerChListInfo();
        for (Object o: content) {
            info.addItem((DlnaTerChListItem) o);
        }

        return info;
    }

    /**
     * チャンネル情報をArrayList化.
     * @param info info
     * @return チャンネル情報をArrayList
     */
    public static ArrayList<Object> toArrayList(final DlnaTerChListInfo info) {
        ArrayList<Object> ret = new ArrayList<>();
        if (null == info || 0 == info.size()) {
            return ret;
        }
        for (int i = 0; i < info.size(); ++i) {
            ret.add(info.get(i));
        }
        return ret;
    }

    /**
     * 地上波チャンネルリスト取得.
     * @return 地上波チャンネルリスト
     */
    ArrayList<DlnaTerChListItem> getTerChLists() {
        return mLists;
    }
}
