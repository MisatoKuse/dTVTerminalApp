/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.IOException;

/**
 * デバイス状態に関するUtilクラス.
 */
public class DeviceStateUtils {

    /**
     * ペアリング状態.
     */
    public enum PairingState {
        /**
         * 宅内.
         */
        INSIDE_HOUSE,
        /**
         * 宅外.
         */
        OUTSIDE_HOUSE,
        /**
         * 未ペアリング.
         */
        NO_PAIRING
    }

    /**
     * Root化チェック.
     * @return ルート化Status
     */
    public static boolean isRootDevice() {
        boolean isRoot = true;
        try {
            Process process = Runtime.getRuntime().exec("su");
            process.destroy();
            DTVTLogger.debug("DeviceStateUtils::isRootDevice===================true");
        } catch (IOException e) {
            //Exception発生=Root化ではない
            DTVTLogger.debug("DeviceStateUtils::isRootDevice===================false");
            isRoot = false;
        }
        return isRoot;
    }

    /**
     * ペアリング済みかどうか判定.
     *
     * @param context コンテキストファイル
     * @param stbStatus STB接続状態
     * @return ペアリング状態
     */
    public static DeviceStateUtils.PairingState getPairingState(final Context context, final boolean stbStatus) {
        boolean isParingSettled = SharedPreferencesUtils.getSharedPreferencesDecisionParingSettled(context);
        if (stbStatus && isParingSettled) {
            return DeviceStateUtils.PairingState.INSIDE_HOUSE;
        } else if (!stbStatus && isParingSettled) {
            return DeviceStateUtils.PairingState.OUTSIDE_HOUSE;
        } else {
            return DeviceStateUtils.PairingState.NO_PAIRING;
        }
    }
}