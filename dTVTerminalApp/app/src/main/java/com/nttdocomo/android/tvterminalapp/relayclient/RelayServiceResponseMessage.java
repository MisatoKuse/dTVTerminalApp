/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 中継アプリからの処理結果応答を通知する情報.
 */
public class RelayServiceResponseMessage implements Serializable {
    /**result_ok.*/
    public static final int RELAY_RESULT_OK = 0;
    /**result_error.*/
    public static final int RELAY_RESULT_ERROR = 1;
    /**result_success.*/
    private static final int RELAY_RESULT_SUCCESS = 0;
    /**internal_error.*/
    public static final int RELAY_RESULT_INTERNAL_ERROR = 11;
    /**application_not_install.*/
    public static final int RELAY_RESULT_APPLICATION_NOT_INSTALL = 12;
    /**application_id_notexist .*/
    public static final int RELAY_RESULT_APPLICATION_ID_NOTEXIST = 13;
    /**application_start_failed .*/
    public static final int RELAY_RESULT_APPLICATION_START_FAILED = 14;
    /**version_code_incompatible.*/
    public static final int RELAY_RESULT_VERSION_CODE_INCOMPATIBLE = 15;
    /**contents_id_notexist.*/
    private static final int RELAY_RESULT_CONTENTS_ID_NOTEXIST = 16;
    /**crid_notexist.*/
    private static final int RELAY_RESULT_CRID_NOTEXIST = 17;
    /**chno_notexist.*/
    private static final int RELAY_RESULT_CHNO_NOTEXIST = 18;
    /**command_argument_notexist.*/
    private static final int RELAY_RESULT_COMMAND_ARGUMENT_NOTEXIST = 19;

    // ユーザアカウント切り替え要求のエラー応答.
    /**要求受付失敗.*/
    public static final int RELAY_RESULT_SET_DEFAULT_USER_ACCOUNT_REJECTED = 20;
    /**ユーザ切替サービス未登録、又は、署名による呼び出し元不正.*/
    public static final int RELAY_RESULT_NOT_REGISTERED_SERVICE = 21;
    /**指定ユーザIDなし.*/
    public static final int RELAY_RESULT_UNREGISTERED_USER_ID = 22;
    /**要求時とは別docomoIDで指定ユーザに切り替え成功 ※本来は正常終了だが異常終了とする.*/
    public static final int RELAY_RESULT_USER_ID_CHANGED = 23;
    /**ユーザタイムアウト.*/
    public static final int RELAY_RESULT_USER_TIMEOUT = 24;
    /**ユーザ中断.*/
    public static final int RELAY_RESULT_USER_CANCEL = 25;
    /**ユーザ状態異常.*/
    public static final int RELAY_RESULT_USER_INVALID_STATE = 26;
    /**ユーザアカウント切り替えのサーバエラー.*/
    public static final int RELAY_RESULT_USERACCOUNT_SERVER_ERROR = 27;
    /**ユーザアカウント切り替えのネットワークエラー.*/
    public static final int RELAY_RESULT_USERACCOUNT_NETWORK_ERROR = 28;
    /**ユーザアカウント切り替えの内部エラー.*/
    public static final int RELAY_RESULT_USERACCOUNT_INTERNAL_ERROR = 29;

    /**他の端末の要求処理中.*/
    public static final int RELAY_RESULT_RELAY_SERVICE_BUSY = 31;
    /**接続タイムアウト.*/
    public static final int RELAY_RESULT_DISTINATION_UNREACHABLE = 32;
    /**service_category_type_notexist.*/
    private static final int RELAY_RESULT_SERVICE_CATEGORY_TYPE_NOTEXIST = 33;
    /**dtvt_application_version_incompatible.*/
    public static final int RELAY_RESULT_DTVT_APPLICATION_VERSION_INCOMPATIBLE = 34;
    /**stb_relay_service_version_incompatible.*/
    public static final int RELAY_RESULT_STB_RELAY_SERVICE_VERSION_INCOMPATIBLE = 35;
    /** STB側の鍵不一致.*/
    public static final int RELAY_RESULT_STB_KEY_MISMATCH = 36;

    private static final long serialVersionUID = -6651018925196027761L;
    /**result初期化.*/
    private int mResult = RELAY_RESULT_OK;
    /**初期化.*/
    private int mResultCode = RELAY_RESULT_SUCCESS;
    /**アプリケーションタイプ.*/
    private RemoteControlRelayClient.STB_APPLICATION_TYPES mApplicationTypes =
            RemoteControlRelayClient.STB_APPLICATION_TYPES.UNKNOWN;
    /**request_command_types.*/
    private RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES mRequestCommandTypes =
            RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES.COMMAND_UNKNOWN;
    /**dtvchannel_service_category_types.*/
    private RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES mDtvChannelServiceCategoryTypes =
            RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.UNKNOWN;
    /**h4d_service_category_types.*/
    private RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES mHikariTvServiceCategoryTypes =
            RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES.UNKNOWN;

    /**
     * 応答結果の変換.
     */
    final Map<String, Integer> mResultMap = new HashMap<String, Integer>() {
        {
            put(RemoteControlRelayClient.RELAY_RESULT_OK, RELAY_RESULT_OK);
            put(RemoteControlRelayClient.RELAY_RESULT_ERROR, RELAY_RESULT_ERROR);
        }
    };

    /**
     * 応答結果コードの変換.
     */
    final Map<String, Integer> mResultCodeMap = new HashMap<String, Integer>() {
        {
            put(RemoteControlRelayClient.RELAY_RESULT_INTERNAL_ERROR, RELAY_RESULT_INTERNAL_ERROR);
            put(RemoteControlRelayClient.RELAY_RESULT_APPLICATION_NOT_INSTALL, RELAY_RESULT_APPLICATION_NOT_INSTALL);
            put(RemoteControlRelayClient.RELAY_RESULT_APPLICATION_ID_NOTEXIST, RELAY_RESULT_APPLICATION_ID_NOTEXIST);
            put(RemoteControlRelayClient.RELAY_RESULT_CONTENTS_ID_NOTEXIST, RELAY_RESULT_CONTENTS_ID_NOTEXIST);
            put(RemoteControlRelayClient.RELAY_RESULT_CRID_NOTEXIST, RELAY_RESULT_CRID_NOTEXIST);
            put(RemoteControlRelayClient.RELAY_RESULT_CHNO_NOTEXIST, RELAY_RESULT_CHNO_NOTEXIST);
            put(RemoteControlRelayClient.RELAY_RESULT_COMMAND_ARGUMENT_NOTEXIST, RELAY_RESULT_COMMAND_ARGUMENT_NOTEXIST);
            put(RemoteControlRelayClient.RELAY_RESULT_APPLICATION_START_FAILED, RELAY_RESULT_APPLICATION_START_FAILED);
            put(RemoteControlRelayClient.RELAY_RESULT_VERSION_CODE_INCOMPATIBLE, RELAY_RESULT_VERSION_CODE_INCOMPATIBLE);
            put(RemoteControlRelayClient.RELAY_RESULT_SET_DEFAULT_USER_ACCOUNT_REJECTED, RELAY_RESULT_SET_DEFAULT_USER_ACCOUNT_REJECTED);
            put(RemoteControlRelayClient.RELAY_RESULT_NOT_REGISTERED_SERVICE, RELAY_RESULT_NOT_REGISTERED_SERVICE);
            put(RemoteControlRelayClient.RELAY_RESULT_UNREGISTERED_USER_ID, RELAY_RESULT_UNREGISTERED_USER_ID);
            put(RemoteControlRelayClient.RELAY_RESULT_USER_ID_CHANGED, RELAY_RESULT_USER_ID_CHANGED);
            put(RemoteControlRelayClient.RELAY_RESULT_USER_TIMEOUT, RELAY_RESULT_USER_TIMEOUT);
            put(RemoteControlRelayClient.RELAY_RESULT_USER_CANCEL, RELAY_RESULT_USER_CANCEL);
            put(RemoteControlRelayClient.RELAY_RESULT_USER_INVALID_STATE, RELAY_RESULT_USER_INVALID_STATE);
            put(RemoteControlRelayClient.RELAY_RESULT_USERACCOUNT_SERVER_ERROR, RELAY_RESULT_USERACCOUNT_SERVER_ERROR);
            put(RemoteControlRelayClient.RELAY_RESULT_USERACCOUNT_NETWORK_ERROR, RELAY_RESULT_USERACCOUNT_NETWORK_ERROR);
            put(RemoteControlRelayClient.RELAY_RESULT_USERACCOUNT_INTERNAL_ERROR, RELAY_RESULT_USERACCOUNT_INTERNAL_ERROR);
            put(RemoteControlRelayClient.RELAY_RESULT_DISTINATION_UNREACHABLE, RELAY_RESULT_DISTINATION_UNREACHABLE);
            put(RemoteControlRelayClient.RELAY_RESULT_RELAY_SERVICE_BUSY, RELAY_RESULT_RELAY_SERVICE_BUSY);
            put(RemoteControlRelayClient.RELAY_RESULT_SERVICE_CATEGORY_TYPE_NOTEXIST, RELAY_RESULT_SERVICE_CATEGORY_TYPE_NOTEXIST);
            put(RemoteControlRelayClient.RELAY_RESULT_DTVT_APPLICATION_VERSION_INCOMPATIBLE, RELAY_RESULT_DTVT_APPLICATION_VERSION_INCOMPATIBLE);
            put(RemoteControlRelayClient.RELAY_RESULT_STB_RELAY_SERVICE_VERSION_INCOMPATIBLE, RELAY_RESULT_STB_RELAY_SERVICE_VERSION_INCOMPATIBLE);
            put(RemoteControlRelayClient.RELAY_RESULT_STB_RELAY_KEY_ERROR, RELAY_RESULT_STB_KEY_MISMATCH);
        }
    };
    /**
     * リクエストコマンド応答結果コードの変換.
     */
    final Map<String, RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES> mRequestCommandMap
            = new HashMap<String, RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES>() {
        {
            put(RemoteControlRelayClient.RELAY_COMMAND_UNKNOWN,
                    RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES.COMMAND_UNKNOWN);
            put(RemoteControlRelayClient.RELAY_COMMAND_KEYEVENT_KEYCODE_POWER,
                    RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES.KEYEVENT_KEYCODE_POWER);
            put(RemoteControlRelayClient.RELAY_COMMAND_IS_USER_ACCOUNT_EXIST,
                    RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES.IS_USER_ACCOUNT_EXIST);
            put(RemoteControlRelayClient.RELAY_COMMAND_SET_DEFAULT_USER_ACCOUNT,
                    RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES.SET_DEFAULT_USER_ACCOUNT); // エラー応答時
            put(RemoteControlRelayClient.RELAY_COMMAND_CHECK_APPLICATION_VERSION_COMPATIBILITY,
                    RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES.CHECK_APPLICATION_VERSION_COMPATIBILITY); // エラー応答時
            put(RemoteControlRelayClient.RELAY_COMMAND_CHECK_APPLICATION_REQUEST_PROCESSING,
                    RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES.CHECK_APPLICATION_REQUEST_PROCESSING); // エラー応答時
            put(RemoteControlRelayClient.RELAY_COMMAND_TITLE_DETAIL,
                    RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES.TITLE_DETAIL);
            put(RemoteControlRelayClient.RELAY_COMMAND_START_APPLICATION,
                    RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES.START_APPLICATION);
        }
    };

    /**
     * アプリ名シンボルに対するアプリ起動要求種別.固定Mapクラス定義でClientで外部参照される前提のためpublic.
     */
    @SuppressWarnings("PublicField")
    public final Map<String, RemoteControlRelayClient.STB_APPLICATION_TYPES> mStbApplicationEnumMap
            = new HashMap<String, RemoteControlRelayClient.STB_APPLICATION_TYPES>() {
        {
            put(RemoteControlRelayClient.STB_APPLICATION_DTV, RemoteControlRelayClient.STB_APPLICATION_TYPES.DTV);    // dTV
            put(RemoteControlRelayClient.STB_APPLICATION_DANIMESTORE, RemoteControlRelayClient.STB_APPLICATION_TYPES.DANIMESTORE);    // dアニメストア
            put(RemoteControlRelayClient.STB_APPLICATION_DTVCHANNEL, RemoteControlRelayClient.STB_APPLICATION_TYPES.DTVCHANNEL);  // dTVチャンネル
            put(RemoteControlRelayClient.STB_APPLICATION_HIKARITV, RemoteControlRelayClient.STB_APPLICATION_TYPES.HIKARITV);    // ひかりTV
            put(RemoteControlRelayClient.STB_APPLICATION_DAZN, RemoteControlRelayClient.STB_APPLICATION_TYPES.DAZN);    // ダ・ゾーン
        }
    };

    /**
     * dTVチャンネル：サービス・カテゴリー分類シンボルに対するサービス・カテゴリー分類.
     */
    final Map<String, RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES>
            mDtvChannelServiceCategoryTypesMap = new HashMap<String, RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES>() {
        {
            // dTVチャンネル・放送
            put(RemoteControlRelayClient.STB_APPLICATION_DTVCHANNEL_CATEGORY_BROADCAST,
                    RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_BROADCAST);
            // dTVチャンネル・VOD（見逃し）
            put(RemoteControlRelayClient.STB_APPLICATION_DTVCHANNEL_CATEGORY_MISSED,
                    RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_MISSED);
            // dTVチャンネル・VOD（関連番組）
            put(RemoteControlRelayClient.STB_APPLICATION_DTVCHANNEL_CATEGORY_RELATION,
                    RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES.DTVCHANNEL_CATEGORY_RELATION);
        }
    };

    /**
     * ひかりTV：サービス・カテゴリー分類シンボルに対するサービス・カテゴリー分類.
     */

    final Map<String, RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES>
            mHikariTvServiceCategoryTypesMap = new HashMap<String, RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES>() {
        {
            // ひかりTVの番組（地デジ）
            put(RemoteControlRelayClient.STB_APPLICATION_H4D_CATEGORY_TERRESTRIAL_DIGITAL,
                    RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES.H4D_CATEGORY_TERRESTRIAL_DIGITAL);
            // ひかりTVの番組（BS）
            put(RemoteControlRelayClient.STB_APPLICATION_H4D_CATEGORY_SATELLITE_BS,
                    RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES.H4D_CATEGORY_SATELLITE_BS);
            // ひかりTVの番組（IPTV）
            put(RemoteControlRelayClient.STB_APPLICATION_H4D_CATEGORY_IPTV,
                    RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES.H4D_CATEGORY_IPTV);
            // ひかりTV内 dTVチャンネルの番組
            put(RemoteControlRelayClient.STB_APPLICATION_H4D_CATEGORY_DTVCHANNEL_BROADCAST,
                    RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES.H4D_CATEGORY_DTVCHANNEL_BROADCAST);
            // ひかりTV内 dTVチャンネル VOD（見逃し）
            put(RemoteControlRelayClient.STB_APPLICATION_H4D_CATEGORY_DTVCHANNEL_MISSED,
                    RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES.H4D_CATEGORY_DTVCHANNEL_MISSED);
            // ひかりTV内 dTVチャンネル VOD（関連番組）
            put(RemoteControlRelayClient.STB_APPLICATION_H4D_CATEGORY_DTVCHANNEL_RELATION,
                    RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES.H4D_CATEGORY_DTVCHANNEL_RELATION);
            // ひかりTVのVOD
            put(RemoteControlRelayClient.STB_APPLICATION_H4D_CATEGORY_HIKARITV_VOD,
                    RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES.H4D_CATEGORY_HIKARITV_VOD);
            // ひかりTV内 dTVのVOD
            put(RemoteControlRelayClient.STB_APPLICATION_H4D_CATEGORY_DTV_VOD,
                    RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES.H4D_CATEGORY_DTV_VOD);
            // ひかりTV内VOD(dTV含む)のシリーズ
            put(RemoteControlRelayClient.STB_APPLICATION_H4D_CATEGORY_DTV_SVOD,
                    RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES.H4D_CATEGORY_DTV_SVOD);
        }
    };

    /**
     * 応答メッセージ.
     */
    RelayServiceResponseMessage() {
        mResult = RELAY_RESULT_OK;
        mResultCode = RELAY_RESULT_SUCCESS;
    }

    /**
     * 応答メッセージ.
     *
     * @param result     応答結果
     * @param resultCode 応答結果コード
     */
    public RelayServiceResponseMessage(final int result, final int resultCode) {
        mResult = result;
        mResultCode = resultCode;
    }

    /**
     * 応答結果を取得.
     *
     * @return 応答結果
     */
    public int getResult() {
        return mResult;
    }

    /**
     * 応答結果を設定.
     *
     * @param result 応答結果
     */
    public void setResult(final int result) {
        mResult = result;
    }

    /**
     * 応答結果コードを設定.
     *
     * @param resultCode 応答結果コード
     */
    void setResultCode(final int resultCode) {
        mResultCode = resultCode;
    }

    /**
     * 応答結果コードを取得.
     *
     * @return 応答結果コード
     */
    public int getResultCode() {
        return mResultCode;
    }

    /**
     * 起動アプリケーション種別を取得.
     *
     * @return アプリケーション種別
     */
    public RemoteControlRelayClient.STB_APPLICATION_TYPES getApplicationTypes() {
        return mApplicationTypes;
    }

    /**
     * 起動アプリケーション種別を設定.
     *
     * @param applicationTypes 起動アプリケーション種別
     */
    void setApplicationTypes(final RemoteControlRelayClient.STB_APPLICATION_TYPES applicationTypes) {
        mApplicationTypes = applicationTypes;
    }

    /**
     * リクエストコマンド種別を設定.
     *
     * @param requestCommandTypes リクエストコマンド種別
     */
    void setRequestCommandTypes(final RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES requestCommandTypes) {
        mRequestCommandTypes = requestCommandTypes;
    }

    /**
     * dTVチャンネルカテゴリ種別を取得.
     *
     * @return dTVチャンネルカテゴリ種別
     */
    RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES getDtvChannelServiceCategoryTypes() {
        return mDtvChannelServiceCategoryTypes;
    }

    /**
     * dTVチャンネルカテゴリ種別を設定.
     *
     * @param dtvChannelServiceCategoryTypes dTVチャンネルカテゴリ種別
     */
    void setDtvChannelServiceCategoryTypes(final RemoteControlRelayClient.DTVCHANNEL_SERVICE_CATEGORY_TYPES dtvChannelServiceCategoryTypes) {
        mDtvChannelServiceCategoryTypes = dtvChannelServiceCategoryTypes;
    }

    /**
     * ひかりTVカテゴリ種別を取得.
     *
     * @return ひかりTVカテゴリ種別
     */
    RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES getHikariTvServiceCategoryTypes() {
        return mHikariTvServiceCategoryTypes;
    }

    /**
     * ひかりTVカテゴリ種別を設定.
     *
     * @param hikariTvServiceCategoryTypes ひかりTVカテゴリ種別
     */
    void setHikariTvServiceCategoryTypes(final RemoteControlRelayClient.H4D_SERVICE_CATEGORY_TYPES hikariTvServiceCategoryTypes) {
        mHikariTvServiceCategoryTypes = hikariTvServiceCategoryTypes;
    }

    /**
     * リクエスト種別を取得.
     *
     * @return リクエスト種別
     */
    public RemoteControlRelayClient.STB_REQUEST_COMMAND_TYPES getRequestCommandTypes() {
        return mRequestCommandTypes;
    }
}
