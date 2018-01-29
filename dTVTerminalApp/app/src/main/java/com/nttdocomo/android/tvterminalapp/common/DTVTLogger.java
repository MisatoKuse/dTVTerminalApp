/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

import android.util.Log;

import com.nttdocomo.android.tvterminalapp.BuildConfig;


/**
 * dTVTアプリ共通ログクラス.
 */
public class DTVTLogger {
    /**
     * ログレベル debug 有効/無効(リリース版ではfalseにする事).
     */
    private static final boolean ENABLE_LOG_DEBUG =
            BuildConfig.BUILD_TYPE.equals("unsigned_on") || BuildConfig.BUILD_TYPE.equals("signed_on") ||
                    BuildConfig.BUILD_TYPE.equals("_unsigned_on") || BuildConfig.BUILD_TYPE.equals("_signed_on");

    /**
     * ログレベル 関数 有効/無効(リリース版ではfalseにする事).
     */
    private static final boolean ENABLE_LOG_METHOD_INOUT =
            BuildConfig.BUILD_TYPE.equals("unsigned_on") || BuildConfig.BUILD_TYPE.equals("signed_on") ||
                    BuildConfig.BUILD_TYPE.equals("_unsigned_on") || BuildConfig.BUILD_TYPE.equals("_signed_on");

    /**
     * ログレベル info 有効/無効.
     */
    private static final boolean ENABLE_LOG_INFO = true;

    /**
     * ログレベル warning 有効/無効.
     */
    private static final boolean ENABLE_LOG_WARNING = true;

    /**
     * ログレベル err 有効/無効.
     */
    private static final boolean ENABLE_LOG_ERR = true;

    /**
     * ログ出力用Tag名
     */
    private static final String PACKAGE_TAG = "[dTVT]";

    /**
     * StackTrace取得要素値
     */
    private static final int TRACE_CALLER_COUNT = 2;

    /**
     * ログ出力（debugレベル、文字列あり）.開発時に動作を確認したい処理に利用する事.※リリース版では無効とする.
     * @param msg ログ出力文字列
     */
    public static void debug(String msg) {
        if (ENABLE_LOG_DEBUG) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + ": " + nonNull(msg));
        }
    }

    /**
     * ログ出力（debugレベル 例外発生）.開発時に動作を確認したい処理に利用する事.※リリース版では無効とする.
     * @param e Exception
     */
    public static void debug(Exception e) {
        if (ENABLE_LOG_DEBUG) {
            java.lang.StackTraceElement[] stack = e.getStackTrace();
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + ": (Exception) : " + "\n\t" + e.getMessage() + "\n\tat " + stack[stack.length - 1]);
        }
    }

    /**
     * ログ出力 (debugレベル 文字列あり/例外発生).開発時に動作を確認したい処理に利用する事.※リリース版では無効とする.
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
     * 関数Startログ 出力 ※リリース版では無効とする.
     */
    public static void start() {
        if (ENABLE_LOG_METHOD_INOUT) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + "START.");
        }
    }

    /**
     * 関数Startログ 出力(出力文字列あり) ※リリース版では無効とする.
     * @param msg ログ出力文字列
     */
    public static void start(String msg) {
        if (ENABLE_LOG_METHOD_INOUT) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + "START : " + nonNull(msg));
        }
    }

    /**
     * 関数Endログ 出力 ※リリース版では無効とする.
     */
    public static void end() {
        if (ENABLE_LOG_METHOD_INOUT) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + "END.");
        }
    }

    /**
     * 関数Endログ 出力(出力文字列あり) ※リリース版では無効とする.
     * @param msg ログ出力文字列
     */
    public static void end(String msg) {
        if (ENABLE_LOG_METHOD_INOUT) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + "END  : " + nonNull(msg));
        }
    }

    /**
     * 関数Endログ 出力(Return値出力用) ※リリース版では無効とする.
     * @param msg Return値出力文字列
     */
    public static void end_ret(String msg) {
        if (ENABLE_LOG_METHOD_INOUT) {
            Log.d(PACKAGE_TAG, getClassName() + getFunctionName() + "END  : RETURN :" + nonNull(msg));
        }
    }

    /**
     * ログ出力（内部情報 任意）.アプリ基幹制御に関わる重要な処理のログに利用する事.
     * @param msg ログ出力文字列
     */
    public static void info(String msg) {
        if (ENABLE_LOG_INFO) {
            Log.i(PACKAGE_TAG, getClassName() + getFunctionName() + ": " + nonNull(msg));
        }
    }


    /**
     * ログ出力（内部情報 任意）.復旧可能なエラー発生時のログに利用する事.
     * @param msg ログ出力文字列
     */
    public static void warning(String msg) {
        if (ENABLE_LOG_WARNING) {
            Log.w(PACKAGE_TAG, getClassName() + getFunctionName() + ": " + nonNull(msg));
        }
    }


    /**
     * ログ出力（内部情報 任意）.復旧不可能なエラー発生時のログに利用する事.
     * @param msg ログ出力文字列
     */
    public static void error(String msg) {
        if (ENABLE_LOG_ERR) {
            Log.e(PACKAGE_TAG, getClassName() + getFunctionName() + ": " + nonNull(msg));
        }
    }

    /* ログ用関数はここまで.下記は内部処理 */

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
        Throwable th = new Throwable();
        String fn = th.getStackTrace()[TRACE_CALLER_COUNT].getClassName();
        fn = fn.substring(fn.lastIndexOf(".") + 1) + " ";
        return fn;
    }

    /**
     * 関数名取得関数
     * @return 関数名
     */
    private static String getFunctionName() {
        Throwable th = new Throwable();
        return th.getStackTrace()[TRACE_CALLER_COUNT].getMethodName() + "() ";
    }
}
