/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

import android.util.Log;


/**
 * 計測アプリ共通ログクラス.
 */
public class DTVTLogger {
    /**
     * ログレベル デバッグ 有効/無効(リリース版ではfalseにする事).
     */
    private static final boolean ENABLE_LOG_DEBUG = true;

    /**
     * ログ出力用Tag名
     */
    private static final String PACKAGE_TAG = "[dTVT]";

    /**
     * StackTrace取得要素値
     */
    private static final int TRACE_CALLER_COUNT = 2;

    /**
     * ログ出力（内部情報 任意）.
     * @param msg ログ出力文字列
     */
    public static void debug(String msg) {
        if (ENABLE_LOG_DEBUG) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + ": " + nonNull(msg));
        }
    }

    /**
     * ログ出力（例外発生）.
     * @param e Exception
     */
    public static void debug(Exception e) {
        if (ENABLE_LOG_DEBUG) {
            java.lang.StackTraceElement[] stack = e.getStackTrace();
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + ": (Exception) : " + "\n\t" + e.getMessage() + "\n\tat " + stack[stack.length - 1]);
        }
    }

    /**
     * ログ出力 (内部情報/例外発生)
     * @param msg ログ出力文字列
     * @param e Exception
     */
    public static void debug(String msg, Exception e) {
        if (ENABLE_LOG_DEBUG) {
            java.lang.StackTraceElement[] stack = e.getStackTrace();
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + ": " + nonNull(msg) + "\n\t" + e.getMessage() + "\n\tat " + stack[stack.length - 1]);
        }
    }

    /**
     * Startログ 出力関数
     */
    public static void start() {
        if (ENABLE_LOG_DEBUG) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + "START.");
        }
    }

    /**
     * Startログ 出力関数(出力文字列あり)
     * @param msg ログ出力文字列
     */
    public static void start(String msg) {
        if (ENABLE_LOG_DEBUG) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + "START : " + nonNull(msg));
        }
    }

    /**
     * Endログ 出力関数
     */
    public static void end() {
        if (ENABLE_LOG_DEBUG) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + "END.");
        }
    }

    /**
     * Endログ 出力関数(出力文字列あり)
     * @param msg ログ出力文字列
     */
    public static void end(String msg) {
        if (ENABLE_LOG_DEBUG) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + "END  : " + nonNull(msg));
        }
    }

    /**
     * Endログ 出力関数(Return値出力用)
     * @param msg Return値出力文字列
     */
    public static void end_ret(String msg) {
        if (ENABLE_LOG_DEBUG) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + "END  : RETURN :" + nonNull(msg));
        }
    }

    /**
     * ログ出力（内部情報 任意）.
     * @param msg ログ出力文字列
     */
    public static void info(String msg) {
        Log.i(PACKAGE_TAG, getClassName() + getFunctionName() +  ": " + nonNull(msg));
    }


    /**
     * ログ出力（内部情報 任意）.
     * @param msg ログ出力文字列
     */
    public static void warning(String msg) {
        Log.w(PACKAGE_TAG, getClassName() + getFunctionName() + ": " + nonNull(msg));
    }


    /**
     * ログ出力（内部情報 任意）.
     * @param msg ログ出力文字列
     */
    public static void error(String msg) {
        Log.e(PACKAGE_TAG, getClassName() + getFunctionName() + ": " + nonNull(msg));
    }

    /**
     * 出力文字列Nullチェック関数
     * @param s ログ出力文字列
     * @return 出力文字列
     */
    private static String nonNull(String s) {
        if (s == null) {
            return "(null)";
        }
        return s;
    }


    /**
     * クラス名取得関数
     * @return クラス名
     */
    private static String getClassName() {
        String fn = new Throwable().getStackTrace()[TRACE_CALLER_COUNT].getClassName();
        fn = fn.substring(fn.lastIndexOf(".") + 1) + " ";
        return fn;
    }

    /**
     * 関数名取得関数
     * @return 関数名
     */
    private static String getFunctionName() {
        String fn = new Throwable().getStackTrace()[TRACE_CALLER_COUNT].getMethodName() + "() ";
        return fn;
    }
}
