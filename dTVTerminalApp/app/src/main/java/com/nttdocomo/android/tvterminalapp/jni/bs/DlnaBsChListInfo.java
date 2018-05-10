/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.bs;

import java.util.ArrayList;

/**
 * DlnaBsChListInfo.
 */
public class DlnaBsChListInfo {
    /**BSデジタルチャンネルリスト.*/
    private ArrayList<DlnaBsChListItem> mLists = new ArrayList<>();

    /**
     * addItem.
     * @param item item
     */
    private void addItem(final DlnaBsChListItem item) {
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
     * get.
     * @param index index
     * @return item
     */
    public DlnaBsChListItem get(final int index) {
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
    public static DlnaBsChListInfo fromArrayList(final ArrayList<Object> content) {
        if (null == content) {
            return null;
        }
        DlnaBsChListInfo info = new DlnaBsChListInfo();
        for (Object o: content) {
            info.addItem((DlnaBsChListItem) o);
        }

        return info;
    }

    /**
     * toArrayList.
     * @param info info
     * @return ArrayList
     */
    public static ArrayList<Object> toArrayList(final DlnaBsChListInfo info) {
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
     * getBsChLists.
     * @return BsChLists
     */
    public ArrayList<DlnaBsChListItem> getBsChLists() {
        return mLists;
    }
}
