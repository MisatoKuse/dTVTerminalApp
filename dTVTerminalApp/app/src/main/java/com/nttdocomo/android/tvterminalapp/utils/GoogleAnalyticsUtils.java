/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.text.TextUtils;
import android.util.SparseArray;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.commonmanager.TrackerManager;

/**
 * GoogleAnalytics送信用.
 */
public class GoogleAnalyticsUtils {

    /**
     * 実行中のクラス名を取得します.
     *
     * @return クラス名.
     */
    private static String getCurrentClassName() {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        if (stackTraceElement.length >= 5) {
            String className = Thread.currentThread().getStackTrace()[4].getClassName();
            className = className.substring(className.lastIndexOf(ContentUtils.STR_DOT) + 1);
            return className;
        } else {
            return null;
        }
    }

    /**
     * 実行中のメソッド名を取得します.
     *
     * @return メソッド名.
     */
    private static String getCurrentMethodName() {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        if (stackTraceElement.length >= 5) {
            return Thread.currentThread().getStackTrace()[4].getMethodName();
        } else {
            return null;
        }
    }

    /**
     * getClassNameAndMethodName.
     * @return nameString
     */
    public static String getClassNameAndMethodName() {
        String nameString;
        nameString = getCurrentClassName() + ContentUtils.STR_COLON + getCurrentMethodName();
        return nameString;
    }

    /**
     * エラーレポートを送信する.
     *
     * @param classMethodName        クラス名とメソッド名
     * @param errorCodeIdentifier 　エラーコードorエラー識別子
     */
    public static void sendErrorReport(final String classMethodName, final String errorCodeIdentifier) {
        DTVTLogger.start();
        DTVTLogger.debug(" classMethodName: " + classMethodName + " errorCodeIdentifier: " + errorCodeIdentifier);
        String category = TrackerManager.shared().getAppContext().getString(R.string.google_analytics_category_event_error_report);
        sendEventInfo(category, errorCodeIdentifier, classMethodName, null);
        DTVTLogger.end();
    }

    /**
     * イベントを送る.
     *
     * @param category イベントのカテゴリ
     * @param action イベントのアクション
     * @param customDimensions カスタム ディメンション
     * @param label ラベル
     */
    public static void sendEventInfo(final String category, final String action, final String label, final SparseArray<String> customDimensions) {
        DTVTLogger.start();
        Tracker tracker = TrackerManager.shared().getDefaultTracker();
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder();
        builder.setCategory(category);
        builder.setAction(action);
        if (TextUtils.isEmpty(label)) {
            builder.setLabel("");
        } else {
            builder.setLabel(label);
        }
        DTVTLogger.debug("[GA][E]:" + " category: " + category + " action: " + action + " label: " + label + " eventValue: " + 1);
        //valueデフォルト値1
        builder.setValue(1);
        if (customDimensions != null) {
            for (int i = 0; i < customDimensions.size(); i++) {
                int key = customDimensions.keyAt(i);
                String value = customDimensions.get(key);
                builder.setCustomDimension(key, value);
                DTVTLogger.debug("[GA][CD]:" + key + ":" + value);
            }
        }
        tracker.send(builder.build());
        DTVTLogger.end();
    }

    /**
     * スクリーン・ビューを送る.
     *
     * @param screenName スクリーン名
     * @param customDimensions カスタム ディメンション
     */
    public static void sendScreenViewInfo(final String screenName, final SparseArray<String> customDimensions) {
        DTVTLogger.start();
        Tracker tracker = TrackerManager.shared().getDefaultTracker();
        tracker.setScreenName(screenName);
        DTVTLogger.debug("[GA][SN]:" + screenName);
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder();
        if (customDimensions != null) {
            for (int i = 0; i < customDimensions.size(); i++) {
                int key = customDimensions.keyAt(i);
                String value = customDimensions.get(key);
                builder.setCustomDimension(key, value);
                DTVTLogger.debug("[GA][CD]:" + key + ":" + value);
            }
        }
        tracker.send(builder.build());
        DTVTLogger.end();
    }
}