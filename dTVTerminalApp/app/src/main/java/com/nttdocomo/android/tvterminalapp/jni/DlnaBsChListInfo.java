/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import java.util.ArrayList;

public class DlnaBsChListInfo {

    private ArrayList<DlnaBsChListItem> mLists = new ArrayList<>();

    private void addItem(final DlnaBsChListItem item) {
        mLists.add(item);
    }

    public void clearAll() {
        mLists.clear();
    }

    public int size() {
        if (null != mLists) {
            return mLists.size();
        }

        return 0;
    }

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

    static DlnaBsChListInfo fromArrayList(final ArrayList<Object> content) {
        if (null == content) {
            return null;
        }
        DlnaBsChListInfo info = new DlnaBsChListInfo();
        for (Object o: content) {
            info.addItem((DlnaBsChListItem) o);
        }

        return info;
    }

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

    ArrayList<DlnaBsChListItem> getBsChLists() {
        return mLists;
    }
}
