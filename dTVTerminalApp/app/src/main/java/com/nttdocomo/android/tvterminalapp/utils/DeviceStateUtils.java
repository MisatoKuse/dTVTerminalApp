/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.IOException;

/**
 * デバイス状態に関するUtilクラス.
 */
public class DeviceStateUtils {

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
}
