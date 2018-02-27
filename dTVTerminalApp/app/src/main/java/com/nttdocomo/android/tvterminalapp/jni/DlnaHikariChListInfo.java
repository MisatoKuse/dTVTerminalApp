/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import java.util.ArrayList;

class DlnaHikariChListInfo {

    private ArrayList<DlnaHikariChListItem> mLists = new ArrayList<>();

    private void addItem(final DlnaHikariChListItem item) {
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

    static DlnaHikariChListInfo fromArrayList(final ArrayList<Object> content) {
        if (null == content) {
            return null;
        }
        DlnaHikariChListInfo info = new DlnaHikariChListInfo();
        for (Object o: content) {
            info.addItem((DlnaHikariChListItem) o);
        }

        return info;
    }

    ArrayList<DlnaHikariChListItem> getHikariChLists() {
        return mLists;
    }
}
