/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import java.util.ArrayList;

public class DlnaRecVideoInfo {

    private ArrayList<DlnaRecVideoItem> mRecordVideoLists = new ArrayList<>();

    private void addItem(final DlnaRecVideoItem item) {
        mRecordVideoLists.add(item);
    }

    public void addItemByValue(final DlnaRecVideoItem item) {
        DlnaRecVideoItem newItem = new DlnaRecVideoItem();
        newItem.mTitle = item.mTitle;
        newItem.mDate = item.mDate;
        newItem.mAllowedUse = item.mAllowedUse;
        newItem.mBitrate = item.mBitrate;
        newItem.mDuration = item.mDuration;
        newItem.mResolution = item.mResolution;
        newItem.mResUrl = item.mResUrl;
        newItem.mSize = item.mSize;
        newItem.mUpnpIcon = item.mUpnpIcon;
        newItem.mVideoType = item.mVideoType;

        mRecordVideoLists.add(newItem);
    }

    public void clearAll() {
        mRecordVideoLists.clear();
    }

    public int size() {
        if (null != mRecordVideoLists) {
            return mRecordVideoLists.size();
        }

        return 0;
    }

    public DlnaRecVideoItem get(final int index) {
        int destIndex = index;
        if (index < 0) {
            destIndex = 0;
        }
        if (null == mRecordVideoLists || 0 == mRecordVideoLists.size()) {
            return null;
        }

        return mRecordVideoLists.get(destIndex);
    }

    static DlnaRecVideoInfo fromArrayList(final ArrayList<Object> content) {
        if (null == content) {
            return null;
        }
        DlnaRecVideoInfo info = new DlnaRecVideoInfo();
        for (Object o: content) {
            info.addItem((DlnaRecVideoItem) o);
        }

        return info;
    }

    public static ArrayList<Object> toArrayList(final DlnaRecVideoInfo info) {
        ArrayList<Object> ret = new ArrayList<>();
        if (null == info || 0 == info.size()) {
            return ret;
        }
        for (int i = 0; i < info.size(); ++i) {
            ret.add(info.get(i));
        }
        return ret;
    }

    public ArrayList<DlnaRecVideoItem> getRecordVideoLists() {
        return mRecordVideoLists;
    }
}
