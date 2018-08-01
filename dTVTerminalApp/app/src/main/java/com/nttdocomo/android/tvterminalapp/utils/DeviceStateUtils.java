/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.File;
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
        //suが存在する開発端末で試験が行えなくなるため、デバッグ有効な時のみチェックする.
        if (!DTVTLogger.ENABLE_LOG_DEBUG) {
            boolean isRoot = true;

            //su実行ができるかどうかのチェック.正常実行出来たらRootデバイスとみなす
            try {
                Process process = Runtime.getRuntime().exec("su");
                process.destroy();
                DTVTLogger.debug("DeviceStateUtils::isRootDevice===================true");
            } catch (IOException e) {
                //Exception発生=Root化ではない
                DTVTLogger.debug("DeviceStateUtils::isRootDevice===================false");
                isRoot = false;
            }

            try {
                // suが存在しているかどうかのチェック.指定パスにsuがあればRootデバイスとみなす.
                // 一般の商用デバイスにはsuは存在しない前提.
                // suのパスを下記以外にうまく変えるとすり抜ける事ができるが鼬ごっこのため代表的なパスをチェックする.
                String[] paths = {"/sbin/su", "/system/bin/su", "/system/xbin/su"};
                for (String path : paths) {
                    if (new File(path).exists()) {
                        isRoot = true;
                        break;
                    }
                }
            } catch (SecurityException e) {
                //読み取りパーミッションが無ければ失敗する.Rootデバイスとはみなさない.
                isRoot = false;
            }
            return isRoot;
        } else {
            return false;
        }
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