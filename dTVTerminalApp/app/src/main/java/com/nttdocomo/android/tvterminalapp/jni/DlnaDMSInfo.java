/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


import java.util.ArrayList;

/**
 * 機能：カレントのすべてのDMS設備を表示するクラス.
 */
public class DlnaDMSInfo {

    /**DMSの配列.*/
    private ArrayList<DlnaDmsItem> mDMSList = new ArrayList();

    /**
     * 機能：DlnaDmsItemを取得.
     * @param index 配列の番号
     * @return DlnaDmsItem
     */
    public DlnaDmsItem get(final int index) {
        if (null == mDMSList || 0 == mDMSList.size()) {
            return null;
        }

        return mDMSList.get(index);
    }

    /**
     * 機能：DlnaDmsItemを追加.
     * @param item 追加するDMS
     */
    public void add(final DlnaDmsItem item) {
        if (null == mDMSList) {
            mDMSList = new ArrayList();
        }
        mDMSList.add(item);
    }

    /**
     * 機能：DlnaDmsItemを追加.
     * @param udn DMSのudn名で削除
     */
    public void remove(final String udn) {
        for (DlnaDmsItem item: mDMSList) {
            if (item.mUdn.equals(udn)) {
                mDMSList.remove(item);
                return;
            }
        }
    }

    /**
     * 機能：DlnaDmsItemの数を戻す.
     * @return size
     */
    public int size() {
        if (null == mDMSList) {
            return 0;
        }

        return mDMSList.size();
    }

    /**
     * 機能：udnでDmsが存在するかを戻す.
     * @param udn und name
     * @return 成功true
     */
    public boolean exists(final String udn) {
        if (null == mDMSList) {
            return false;
        }

        boolean ret = false;

        for (DlnaDmsItem item: mDMSList) {
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
        if (null == mDMSList) {
            return;
        }
        mDMSList.clear();
    }
}
