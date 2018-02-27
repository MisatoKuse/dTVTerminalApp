/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import java.util.ArrayList;

public class DlnaTerChListInfo {

    private ArrayList<DlnaTerChListItem> mLists = new ArrayList<>();

    private void addItem(final DlnaTerChListItem item) {
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

    public DlnaTerChListItem get(int index) {
        int destIndex = index;
        if (index < 0) {
            destIndex = 0;
        }
        if (null == mLists || 0 == mLists.size()) {
            return null;
        }

        return mLists.get(destIndex);
    }

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

    ArrayList<DlnaTerChListItem> getTerChLists() {
        return mLists;
    }
}
