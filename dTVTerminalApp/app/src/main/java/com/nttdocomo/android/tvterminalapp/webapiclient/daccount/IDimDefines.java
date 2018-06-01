/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.daccount;

/**
 * 外部IF向け定数定義.
 */
public class IDimDefines {

    /**
     * <pre>
     * 処理結果定義.
     *     ユーザ状態異常.
     * </pre>
     */
    public static final int RESULT_USER_INVALID_STATE = -14;

    /**
     * <pre>
     * 処理結果定義.
     *     端末識別キー未払い出し.
     * </pre>
     */
    public static final int RESULT_DEVICEID_INVALID = -13;

    /**
     * <pre>
     * 処理結果定義.
     *     有効IDなし.
     * </pre>
     */
    public static final int RESULT_NO_AVAILABLE_ID = -12;

    /**
     * <pre>
     * 処理結果定義.
     *     認証状態無効.
     * </pre>
     */
    public static final int RESULT_INVALID_ID = -11;

    /**
     * <pre>
     * 処理結果定義.
     *     サービス未登録.
     * </pre>
     */
    public static final int RESULT_NOT_REGISTERED_SERVICE = -10;

    /**
     * <pre>
     * 処理結果定義.
     *     ブラックリスト一致.
     * </pre>
     */
    public static final int RESULT_MATCH_BLACKLIST = -9;

    /**
     * <pre>
     * 処理結果定義.
     *     ホワイトリスト不一致.
     * </pre>
     */
    public static final int RESULT_NOT_MATCH_WHITELIST = -8;

    /**
     * <pre>
     * 処理結果定義.
     *     利用不可.
     * </pre>
     */
    public static final int RESULT_NOT_AVAILABLE = -7;

    /**
     * <pre>
     * 処理結果定義.
     *     動作対象外.
     * </pre>
     */
    public static final int RESULT_INCOMPATIBLE_ENVIRONMENT = -6;

    /**
     * <pre>
     * 処理結果定義.
     *     ユーザタイムアウト.
     * </pre>
     */
    public static final int RESULT_USER_TIMEOUT = -5;

    /**
     * <pre>
     * 処理結果定義.
     *     ユーザキャンセル.
     * </pre>
     */
    public static final int RESULT_USER_CANCEL = -4;

    /**
     * <pre>
     * 処理結果定義.
     *     サーバエラー.
     * </pre>
     */
    public static final int RESULT_SERVER_ERROR = -3;

    /**
     * <pre>
     * 処理結果定義.
     *     ネットワークエラー.
     * </pre>
     */
    public static final int RESULT_NETWORK_ERROR = -2;

    /**
     * <pre>
     * 処理結果定義.
     *     内部エラー.
     * </pre>
     */
    public static final int RESULT_INTERNAL_ERROR = -1;

    /**
     * <pre>
     * 処理結果定義.
     *     正常終了.
     * </pre>
     */
    public static final int RESULT_COMPLETE = 0;

    /**
     * <pre>
     * 処理結果定義.
     *     サービス利用可能.
     * </pre>
     */
    public static final int RESULT_SERVICE_AVAILABLE = 1;

    /**
     * <pre>
     * 処理結果定義.
     *     サービス登録済み.
     * </pre>
     */
    public static final int RESULT_RESULT_REGISTERED = 2;

    /**
     * <pre>
     * 処理結果定義.
     *     ID利用可.
     * </pre>
     */
    public static final int RESULT_VALID_ID = 3;

    /**
     * <pre>
     * 処理結果定義.
     *     セキュリティコード認証成功.
     * </pre>
     */
    public static final int RESULT_SUCCESS = 0;

    // 要求結果.
    /**
     * <pre>
     * 要求結果.
     *     要求受付成功.
     * </pre>
     */
    public static final int REQUEST_ACCEPTED = 0;

    /**
     * <pre>
     * 要求結果.
     *     要求受付失敗.
     * </pre>
     */
    public static final int REQUEST_REJECTED = -1;

    /**
     * <pre>
     * 再認証オプション.
     * </pre>
     */
    public class CertOption {
        /**
         * <pre>
         * 再認証オプション.
         *     指定なし.
         * </pre>
         */
        public static final int DEFAULT = 0;

        /**
         * <pre>
         * 再認証オプション.
         *     強制再認証.
         * </pre>
         */
        public static final int FORCE = 1;

        /**
         * <pre>
         * 再認証オプション.
         *     認証保留.
         * </pre>
         */
        public static final int RESERVE = 2;
    }

    /**
     * <pre>
     * 呼び出し元フラグ.
     * </pre>
     */
    private class CallerFlag {
        /**
         * <pre>
         * 初期設定アプリ :1.
         * </pre>
         */
        public static final int FIRST_SETTINGS = 1;

        /**
         * <pre>
         * その他 :0.
         * </pre>
         */
        public static final int OTHERS = 0;
    }

    /**
     * <pre>
     * 除外理由.
     * </pre>
     */
    private class DeleteAppReason {
        /**
         * <pre>
         * ユーザ操作による除去 :0.
         * </pre>
         */
        public static final int USER = 0;

        /**
         * <pre>
         * システムからの要求による除去 :1.
         * </pre>
         */
        public static final int SYSTEM = 1;
    }

    /**
     * <pre>
     * IDの設定状態.
     * </pre>
     */
    private class IdState {
        /**
         * <pre>
         * ID設定済み :0.
         * </pre>
         */
        public static final int SET = 0;

        /**
         * <pre>
         * ID未設定 :1.
         * </pre>
         */
        public static final int NOT_SET = 1;
    }

    /**
     * <pre>
     * 生体認証の設定状態.
     * </pre>
     */
    private class BioState {
        /**
         * <pre>
         * 生体認証設定済み :0.
         * </pre>
         */
        public static final int SET = 0;

        /**
         * <pre>
         * 生体認証未設定 :1.
         * </pre>
         */
        public static final int NOT_SET = 1;

        /**
         * <pre>
         * 生体認証非対応 :2.
         * </pre>
         */
        public static final int UNSUPPORTED = 2;

        /**
         * <pre>
         * ID未設定 :-1.
         * </pre>
         */
        public static final int NO_ID = -1;
    }

    /**
     * <pre>
     * セキュリティコード認証の認証オプション.
     * </pre>
     */
    private class SecCodeOption {
        /**
         * <pre>
         * 任意認証 :0.
         * </pre>
         */
        public static final int DEFAULT = 0;

        /**
         * <pre>
         * 強制認証 :1.
         * </pre>
         */
        public static final int FORCE = 1;
    }


}
