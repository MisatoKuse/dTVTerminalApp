/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


import java.util.ArrayList;

/**
 * 機能：カレントのすべてのDMS設備を表示するクラス.
 */
public class DlnaDmsInfo {

    /**DMSの配列.*/
    private ArrayList<DlnaDmsItem> mDmsList = new ArrayList();

    /**
     * 機能：DlnaDmsItemを取得.
     * @param index 配列の番号
     * @return DlnaDmsItem
     */
    public DlnaDmsItem get(final int index) {
        if (null == mDmsList || 0 == mDmsList.size()) {
            return null;
        }

        return mDmsList.get(index);
    }

    /**
     * 機能：DlnaDmsItemを追加.
     * @param item 追加するDMS
     */
    public void add(final DlnaDmsItem item) {
        if (null == mDmsList) {
            mDmsList = new ArrayList();
        }
        mDmsList.add(item);
    }

    /**
     * 機能：DlnaDmsItemを追加.
     * @param udn DMSのudn名で削除
     */
    public void remove(final String udn) {
        for (DlnaDmsItem item: mDmsList) {
            if (item.mUdn.equals(udn)) {
                mDmsList.remove(item);
                return;
            }
        }
    }

    /**
     * 機能：DlnaDmsItemの数を戻す.
     * @return size
     */
    public int size() {
        if (null == mDmsList) {
            return 0;
        }

        return mDmsList.size();
    }

    /**
     * 機能：udnでDmsが存在するかを戻す.
     * @param udn und name
     * @return 成功true
     */
    public boolean exists(final String udn) {
        if (null == mDmsList) {
            return false;
        }

        boolean ret = false;

        for (DlnaDmsItem item: mDmsList) {
            if (udn.equals(item.mUdn)) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    /**
     * 機能：DlnaDmsItemを全部削除.
     */
    public void clear() {
        if (null == mDmsList) {
            return;
        }
        mDmsList.clear();
    }
}
