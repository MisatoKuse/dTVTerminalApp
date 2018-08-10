/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.userinfolist.SerializablePreferencesData;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.struct.OneTimeTokenData;

import java.util.List;

/**
 * ユーザ情報やペアリング情報等の保存/取得を管理するクラス.
 */
public class SharedPreferencesUtils {

    /**
     * SharedPreferences ペアリング情報保存キー 親キー.
     */
    private static final String SHARED_KEY_PAIRING_INFOMATION = "pairing_info";
    /**
     * STB選択画面"次回以降表示しない" 選択保存キー.
     */
    private static final String SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME = "unnecessary_next_time";
    /**
     * STB接続画面 接続成功 保存キー.
     */
    private static final String SHARED_KEY_STB_CONNECT_SUCCESS = "connect_success";
    /**
     * ペアリング勧誘画面表示済み判定情報 保存キー.
     */
    private static final String SHARED_KEY_IS_DISPLAYED_PARING_INVITATION = "is_displayed_paring_invitation";
    /**
     * ホーム画面ペアリング済み判定 保存キー.
     */
    private static final String SHARED_KEY_DECISION_PARING_SETTLED = "decision_paring_settled";
    // 接続済み STB情報 保存キー
    /**
     * STB情報 親キー.
     */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION = "stb_data_info";
    /**
     * UDN.
     */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_UDN = "selected_stb_udn";
    /**
     * コントロールURL.
     */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_CONTROL_URL = "selected_stb_control_url";
    /**
     * HTTP.
     */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_HTTP = "selected_stb_http";
    /**
     * Friendly名.
     */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_FRIENDLY_NAME = "selected_stb_friendly_name";
    /**
     * IPv6アドレス.
     */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_IPADDRESS = "selected_stb_ipaddress";
    /**
     * getString 初期値.
     */
    private static final String SHARED_GET_STRING_DEFAULT = "";
    /**
     * 画面情報保存 親キー.
     */
    private static final String SHARED_KEY_SCREEN_INFORMATION = "screen_information";
    /**
     * LaunchActivity チュートリアル表示済み判定 保存キー.
     */
    private static final String SHARED_KEY_IS_DISPLAYED_TUTORIAL = "is_displayed_tutorial";
    /**
     * SettingActivity 画質設定の設定値.
     */
    private static final String SHARED_KEY_IMAGE_QUALITY = "image_quality";
    /**
     * ローカルレジストレーション有効期限.
     */
    private static final String SHARED_KEY_EXPIREDATE = "remote_expire_date";
    /**
     * 持ち出しコンテンツダウンロード先 ture:内部 false:外部.
     */
    private static final String SHARED_KEY_STORAGE_PATH = "storage_path";
    /**
     * 最後に取得したdアカウントのID.
     */
    private static final String LAST_D_ACCOUNT_ID = "BEFORE_D_ACCOUNT_ID";
    /**
     * 最後に取得したワンタイムパスワード.
     */
    private static final String LAST_ONE_TIME_PASSWORD = "LAST_ONE_TIME_PASSWORD";
    /**
     * アプリ再起動フラグ.
     */
    private static final String RESTART_FLAG = "RESTART_FLAG";
    /**
     * ユーザ契約情報キー.
     */
    private static final String USER_CONTRACT_INFO_SHARED_KEY = "USER_CONTRACT_INFO_SHARED_KEY";
    /**
     * ユーザ年齢情報キー.
     */
    private static final String USER_AGE_REQ_SHARED_KEY = "USER_AGE_REQ_SHARED_KEY";
    /**
     * ユーザー情報取得日時.
     */
    private static final String LAST_USER_INFO_DATE = "LAST_USER_INFO_DATE";
    /**
     * ビデオジャンル一覧データ.
     */
    private static final String VIDEO_GENRE_LIST_DATA = "video_genre_list_data";
    /**
     * ワンタイムトークン.
     */
    private static final String ONE_TIME_TOKEN = "ONE_TIME_TOKEN";
    /**
     * ユーザ情報永続化キー.
     */
    private static final String USER_INFO_SERIALIZABLE_DATA_KEY
            = "user_info_serializable_data_key";
    /**
     * 設定ファイル取得日時.
     */
    private static final String LAST_SETTING_FILE_DATE = "LAST_SETTING_FILE_DATE";
    /**
     * 設定ファイルアプリ動作停止.
     */
    private static final String LAST_SETTING_FILE_IS_STOP = "LAST_SETTING_FILE_IS_STOP";
    /**
     * 設定ファイル読み込み失敗.
     */
    private static final String LAST_SETTING_FILE_IS_FILE_READ_FAIL
            = "LAST_SETTING_FILE_IS_FILE_READ_FAIL";
    /**
     * 設定ファイル動作停止時表示文言.
     */
    private static final String LAST_SETTING_FILE_STOP_MESSAGE
            = "LAST_SETTING_FILE_STOP_MESSAGE";
    /**
     * 設定ファイル強制アップデートバージョン.
     */
    private static final String LAST_SETTING_FILE_FORCE_UPDATE
            = "LAST_SETTING_FILE_FORCE_UPDATE";
    /**
     * 設定ファイル任意アップデートバージョン.
     */
    private static final String LAST_SETTING_FILE_OPTIONAL_UPDATE
            = "LAST_SETTING_FILE_OPTIONAL_UPDATE";
    /**
     * パーミッションチェックダイアログの表示設定.
     */
    private static final String PERMISSION_DIALOG_DISPLAYED_TWICE
            = "PERMISSION_DIALOG_DISPLAYED_TWICE";
    /**
     * ローカルレジストレーション期限表示ダイアログフラグ.
     */
    private static final String REGISTER_EXPIREDATE_DIALOG_FLG
            = "REGISTER_EXPIREDATE_DIALOG_FLG";
    /**
     * ローカルレジストレーションの成功日時.
     */
    private static final String LOCAL_REGIST_SUCCCES_TIME = "LOCAL_REGIST_SUCCCES_TIME";

    /**
     * dアカウントの未認証状態を保存する.
     */
    private static final String D_ACCOUNT_UNCERTIFIED_DACCOUNT
            = "D_ACCOUNT_UNCERTIFIED_DACCOUNT";

    /**
     * 独自の削除メソッドがある接続済みSTB情報以外の、dアカウントユーザー切り替え時の削除対象
     * 新しい物を追加した場合は、基本的にこの配列に名前を追加してください。
     * (チュートリアル表示済み判定以外は全て消すことになった。アプリ再起動フラグは自動で消えるので対象外).
     */
    private final static String[] DELETE_PREFERENCES_NAME = {
            // SharedPreferences ペアリング情報保存キー 親キー
            SHARED_KEY_PAIRING_INFOMATION,
            // STB選択画面"次回以降表示しない" 選択保存キー
            SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME,
            // STB接続画面 接続成功 保存キー
            SHARED_KEY_STB_CONNECT_SUCCESS,
            // ペアリング勧誘画面表示済み判定情報 保存キー
            SHARED_KEY_IS_DISPLAYED_PARING_INVITATION,
            // ホーム画面ペアリング済み判定 保存キー
            SHARED_KEY_DECISION_PARING_SETTLED,

            //下記は別途削除しているのでここに記す必要は無い。しかし後日、指定漏れと誤認したので、混乱を避ける為にコメントとして追記。
            //SHARED_KEY_SELECTED_STB_DATA_INFOMATION,
            //SHARED_KEY_SELECTED_STB_DATA_INFOMATION_UDN,
            //SHARED_KEY_SELECTED_STB_DATA_INFOMATION_CONTROL_URL,
            //SHARED_KEY_SELECTED_STB_DATA_INFOMATION_HTTP,
            //SHARED_KEY_SELECTED_STB_DATA_INFOMATION_FRIENDLY_NAME,
            //SHARED_KEY_SELECTED_STB_DATA_INFOMATION_IPADDRESS,

            //下記は削除を行わないチュートリアル関連フラグなので、本来必要は無い。しかし後日、指定漏れと誤認したのでコメントとして追記。
            //SHARED_KEY_SCREEN_INFORMATION,
            //SHARED_KEY_IS_DISPLAYED_TUTORIAL,

            // SettingActivity 画質設定の設定値
            SHARED_KEY_IMAGE_QUALITY,
            // 持ち出しコンテンツダウンロード先
            SHARED_KEY_STORAGE_PATH,
            //最後に取得したdアカウントのID
            LAST_D_ACCOUNT_ID,
            //最後に取得したワンタイムパスワード
            LAST_ONE_TIME_PASSWORD,
            // アプリ再起動フラグは自動消去なので必要ないが、後日、指定漏れと誤認したのでコメントとして追記。
            //RESTART_FLAG,
            //ユーザ契約情報キー
            USER_CONTRACT_INFO_SHARED_KEY,
            //ユーザ年齢情報キー
            USER_AGE_REQ_SHARED_KEY,
            //ユーザー情報取得日時
            LAST_USER_INFO_DATE,
            //ビデオジャンル一覧データ
            VIDEO_GENRE_LIST_DATA,
            // ワンタイムトークン
            ONE_TIME_TOKEN,
            //ユーザ情報
            USER_INFO_SERIALIZABLE_DATA_KEY,
            // 初回dアカウント取得フラグはdアカウント切り替え時も削除無用。指定漏れとの誤認防止用にコメントで記載
            //FIRST_D_ACCOUNT_GET_KEY,
            // 設定ファイル取得日時
            LAST_SETTING_FILE_DATE,
            // 設定ファイル停止スイッチ
            LAST_SETTING_FILE_IS_STOP,
            // 設定ファイル読み込み失敗スイッチ
            LAST_SETTING_FILE_IS_FILE_READ_FAIL,
            // 設定ファイル停止時メッセージ
            LAST_SETTING_FILE_STOP_MESSAGE,
            // 設定ファイル強制アップデートバージョン
            LAST_SETTING_FILE_FORCE_UPDATE,
            //  設定ファイル任意アップデートバージョン
            LAST_SETTING_FILE_OPTIONAL_UPDATE,
            // パーミッションチェックダイアログの表示設定
            PERMISSION_DIALOG_DISPLAYED_TWICE,
            //ローカルレジストレーション期限表示ダイアログフラグ
            REGISTER_EXPIREDATE_DIALOG_FLG,
            //ローカルレジストレーションの成功日時
            LOCAL_REGIST_SUCCCES_TIME,
            //dアカウント未認証状態
            D_ACCOUNT_UNCERTIFIED_DACCOUNT,
    };


    /**
     * STB選択画面"次回以降表示しない" 状態を保存.
     *
     * @param context             コンテキスト
     * @param selectedUnnecessary true:チェック済み false:チェックなし
     */
    public static void setSharedPreferencesStbSelect(final Context context, final boolean selectedUnnecessary) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME, selectedUnnecessary);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * STB接続完了状態を保存.
     *
     * @param context           コンテキスト
     * @param stbConnectSuccess true:接続完了済み false:接続未完了
     */
    public static void setSharedPreferencesStbConnect(final Context context, final boolean stbConnectSuccess) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_STB_CONNECT_SUCCESS, stbConnectSuccess);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * ペアリング勧誘画面表示済み判定情報を保存.
     *
     * @param context     コンテキスト
     * @param isDisplayed true:表示済み false:未表示
     */
    public static void setSharedPreferencesIsDisplayedParingInvitation(final Context context, final boolean isDisplayed) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_IS_DISPLAYED_PARING_INVITATION, isDisplayed);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 接続済みSTBの情報を保存.
     *
     * @param context コンテキスト
     * @param item    STB情報
     */
    public static void setSharedPreferencesStbInfo(final Context context, final DlnaDmsItem item) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_SELECTED_STB_DATA_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_UDN, item.mUdn);
        editor.putString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_CONTROL_URL, item.mControlUrl);
        editor.putString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_HTTP, item.mHttp);
        editor.putString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_FRIENDLY_NAME, item.mFriendlyName);
        editor.putString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_IPADDRESS, item.mIPAddress);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * STB選択画面"次回以降表示しない" 状態を取得.
     *
     * @param context コンテキスト
     * @return true:表示なし false:表示
     */
    public static boolean getSharedPreferencesStbSelect(final Context context) {
        DTVTLogger.debug("getSharedPreferencesStbSelect");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME, false);

    }

    /**
     * STB接続完了状態を取得.
     *
     * @param context コンテキスト
     * @return true:完了済み false:未完了
     */
    public static boolean getSharedPreferencesStbConnect(final Context context) {
        if (context == null) {
            //コンテキストの指定が無ければ無条件で未完了にする
            return false;
        }

        DTVTLogger.debug("getSharedPreferencesStbConnect");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_STB_CONNECT_SUCCESS, false);
    }

    /**
     * ペアリング勧誘画面表示済み判定情報を取得.
     *
     * @param context コンテキスト
     * @return true:表示済み false:未表示
     */
    public static boolean getSharedPreferencesIsDisplayedParingInvitation(final Context context) {
        DTVTLogger.debug("getSharedPreferencesParingInvitationIsDisplayed");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_IS_DISPLAYED_PARING_INVITATION, false);
    }

    /**
     * ペアリング状態を保存(Home画面用).
     *
     * @param context      コンテキスト
     * @param paringStatus ペアリング状態
     */
    public static void setSharedPreferencesDecisionParingSettled(final Context context, final boolean paringStatus) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_DECISION_PARING_SETTLED, paringStatus);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * ペアリング状態を取得(Home画面用).
     *
     * @param context コンテキスト
     * @return ペアリング状態
     */
    public static boolean getSharedPreferencesDecisionParingSettled(final Context context) {
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_DECISION_PARING_SETTLED, false);
    }

    /**
     * 接続済みSTB情報を取得.
     *
     * @param context コンテキスト
     * @return STB情報
     */
    public static DlnaDmsItem getSharedPreferencesStbInfo(final Context context) {
        DTVTLogger.debug("getSharedPreferencesStbInfo");
        DlnaDmsItem item = new DlnaDmsItem();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_SELECTED_STB_DATA_INFOMATION, Context.MODE_PRIVATE);

        item.mUdn = data.getString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_UDN, SHARED_GET_STRING_DEFAULT);
        item.mControlUrl = data.getString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_CONTROL_URL, SHARED_GET_STRING_DEFAULT);
        item.mHttp = data.getString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_HTTP, SHARED_GET_STRING_DEFAULT);
        item.mFriendlyName = data.getString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_FRIENDLY_NAME, SHARED_GET_STRING_DEFAULT);
        item.mIPAddress = data.getString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_IPADDRESS, SHARED_GET_STRING_DEFAULT);

        return item;
    }

    /**
     * ローカルレジストレーション有効期限を保存.
     *
     * @param context コンテキスト
     * @param expireDate 有効期限
     */
    public static void setRemoteDeviceExpireDate(final Context context, final String expireDate) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(SHARED_KEY_SELECTED_STB_DATA_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(SHARED_KEY_EXPIREDATE, expireDate);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * ローカルレジストレーション有効期限を取得.
     *
     * @param context コンテキスト
     * @return 有効期限
     */
    public static String getRemoteDeviceExpireDate(final Context context) {
        DTVTLogger.debug("getSharedPreferencesIsDisplayedTutorial");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_SELECTED_STB_DATA_INFOMATION, Context.MODE_PRIVATE);

        return data.getString(SHARED_KEY_EXPIREDATE, null);
    }

    /**
     * チュートリアル画面表示済み情報を設定.
     *
     * @param context     コンテキスト
     * @param isDisplayed true:表示済み false:未表示
     */
    public static void setSharedPreferencesIsDisplayedTutorial(final Context context, final boolean isDisplayed) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_SCREEN_INFORMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_IS_DISPLAYED_TUTORIAL, isDisplayed);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * チュートリアル画面表示済み情報を取得.
     *
     * @param context コンテキスト
     * @return チュートリアル表示済みフラグ
     */
    public static boolean getSharedPreferencesIsDisplayedTutorial(final Context context) {
        DTVTLogger.debug("getSharedPreferencesIsDisplayedTutorial");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_SCREEN_INFORMATION, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_IS_DISPLAYED_TUTORIAL, false);
    }

    /**
     * SharedPreferences内の接続済みSTBのデータをクリア.
     *
     * @param context コンテキスト
     */
    public static void resetSharedPreferencesStbInfo(final Context context) {
        DTVTLogger.debug("resetSharedPreferencesStbInfo");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_SELECTED_STB_DATA_INFOMATION, Context.MODE_PRIVATE);
        data.edit().clear().apply();
        SharedPreferencesUtils.setSharedPreferencesDecisionParingSettled(context, false);
        SharedPreferencesUtils.setSharedPreferencesStbConnect(context, false);
    }

    /**
     * 設定画面の"外出先視聴時の画質設定"の設定値を保存.
     *
     * @param context コンテキスト
     * @param quality 画質の設定値
     */
    public static void setSharedPreferencesImageQuality(final Context context, final String quality) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_IMAGE_QUALITY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(SHARED_KEY_IMAGE_QUALITY, quality);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 設定画面の"外出先視聴時の画質設定"の設定値を取得.
     *
     * @param context コンテキスト
     * @return "外出先視聴時の画質設定"の設定値
     */
    public static String getSharedPreferencesImageQuality(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_IMAGE_QUALITY, Context.MODE_PRIVATE);

        return data.getString(SHARED_KEY_IMAGE_QUALITY, context.getString(R.string.main_setting_image_quality_high));
    }

    /**
     * 持ち出しコンテンツのダウンロード先を保存.
     *
     * @param context コンテキスト
     * @param path    ダウンロード先 true:内部 false:外部
     */
    public static void setSharedPreferencesStoragePath(final Context context, final Boolean path) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_STORAGE_PATH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_STORAGE_PATH, path);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 持ち出しコンテンツのダウンロード先を取得.
     *
     * @param context コンテキスト
     * @return 持ち出しコンテンツのダウンロード先
     */
    public static Boolean getSharedPreferencesStoragePath(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_STORAGE_PATH, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_STORAGE_PATH, true);
    }

    /**
     * 取得したdアカウントを保存.
     *
     * @param context コンテキスト
     * @param id      保存するdアカウントID
     */
    public static void setSharedPreferencesDaccountId(final Context context, final String id) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_D_ACCOUNT_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(LAST_D_ACCOUNT_ID, id);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 最後に保存したdアカウントを取得.
     *
     * @param context コンテキスト
     * @return dアカウント
     */
    public static String getSharedPreferencesDaccountId(final Context context) {
        SharedPreferences data = context.getSharedPreferences(
                LAST_D_ACCOUNT_ID, Context.MODE_PRIVATE);

        return data.getString(LAST_D_ACCOUNT_ID, "");
    }

    /**
     * 取得したワンタイムパスワードを保存.
     *
     * @param context コンテキスト
     * @param pass    保存するワンタイムパスワード
     */
    public static void setSharedPreferencesOneTimePass(final Context context, final String pass) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_ONE_TIME_PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(LAST_ONE_TIME_PASSWORD, pass);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 最後に保存したワンタイムパスワードを取得.
     *
     * @param context コンテキスト
     * @return ワンタイムパスワード
     */
    public static String getSharedPreferencesOneTimePass(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_ONE_TIME_PASSWORD, Context.MODE_PRIVATE);

        return data.getString(LAST_ONE_TIME_PASSWORD, "");
    }

    /**
     * ユーザー情報の最終取得日時を保存.
     *
     * @param context コンテキスト
     * @param getTime 取得日時
     */
    private static void setSharedPreferencesUserInfoDate(final Context context, final long getTime) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_USER_INFO_DATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putLong(LAST_USER_INFO_DATE, getTime);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * ユーザー情報の最終取得日時を取得.
     *
     * @param context コンテキスト
     * @return 最終取得日時
     */
    public static long getSharedPreferencesUserInfoDate(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_USER_INFO_DATE, Context.MODE_PRIVATE);

        //最終取得日時を返す。存在していなければ、最小値を返す
        return data.getLong(LAST_USER_INFO_DATE, Long.MIN_VALUE);
    }

    /**
     * 一部の設定値以外のプリファレンスを削除する.
     *
     * @param context コンテキスト
     */
    public static void clearAlmostSharedPreferences(final Context context) {
        //接続済みSTB情報系列には削除処理があるので使用する(一見一つしか削除していないが、元締めを消しているのですべて消える)
        resetSharedPreferencesStbInfo(context);

        //他の情報の削除を行う
        for (String deleteKey : DELETE_PREFERENCES_NAME) {
            SharedPreferences deleteData = context.getSharedPreferences(deleteKey, Context.MODE_PRIVATE);
            deleteData.edit().clear().apply();
        }
    }

    /**
     * 再起動フラグを設定.
     *
     * @param context     コンテキスト
     * @param restartFlag 再起動フラグの設定値
     */
    public static void setSharedPreferencesRestartFlag(final Context context, final boolean restartFlag) {
        DTVTLogger.start("set restart flag = " + restartFlag);
        SharedPreferences data = context.getSharedPreferences(
                RESTART_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(RESTART_FLAG, restartFlag);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 再起動フラグを取得.
     *
     * @param context コンテキスト
     * @return 再起動フラグ
     */
    public static boolean getSharedPreferencesRestartFlag(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                RESTART_FLAG, Context.MODE_PRIVATE);

        //デフォルト値はfalseで取得して返す
        return data.getBoolean(RESTART_FLAG, false);
    }

    /**
     * 取得したユーザ契約情報を保存.
     *
     * @param context コンテキスト
     * @param contractInfo 保存するユーザ契約情報
     */
    public static void setSharedPreferencesContractInfo(final Context context, final String contractInfo) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                USER_CONTRACT_INFO_SHARED_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(USER_CONTRACT_INFO_SHARED_KEY, contractInfo);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 最後に保存した契約情報を取得.
     *
     * @param context コンテキスト
     * @return 契約情報
     */
    public static String getSharedPreferencesContractInfo(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                USER_CONTRACT_INFO_SHARED_KEY, Context.MODE_PRIVATE);

        //保存した契約情報がない場合は「none」を返却
        return data.getString(USER_CONTRACT_INFO_SHARED_KEY, UserInfoUtils.CONTRACT_INFO_NONE);
    }

    /**
     * 取得したユーザ視聴制限情報を保存.
     *
     * @param context コンテキスト
     * @param age     保存するユーザ年齢制限情報
     */
    public static void setSharedPreferencesAgeReq(final Context context, final int age) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                USER_AGE_REQ_SHARED_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt(USER_AGE_REQ_SHARED_KEY, age);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 最後に保存した年齢情報を取得.
     *
     * @param context コンテキスト
     * @return 年齢情報
     */
    public static int getSharedPreferencesAgeReq(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                USER_AGE_REQ_SHARED_KEY, Context.MODE_PRIVATE);

        //保存した年齢情報がない場合はPG12を返却
        return data.getInt(USER_AGE_REQ_SHARED_KEY, StringUtils.USER_AGE_REQ_PG12);
    }

    /**
     * ビデオジャンル一覧データ保存.
     *
     * @param context   コンテキスト
     * @param genreData ジャンルデータ
     */
    public static void setSharedPreferencesVideoGenreData(final Context context, final String genreData) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                VIDEO_GENRE_LIST_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(VIDEO_GENRE_LIST_DATA, genreData);
        editor.apply();
        DateUtils dateUtils = new DateUtils(context);
        dateUtils.addLastProgramDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
        DTVTLogger.end();
    }

    /**
     * ビデオジャンル一覧データ取得.
     *
     * @param context コンテキスト
     * @return ジャンルデータ
     */
    public static String getSharedPreferencesVideoGenreData(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                VIDEO_GENRE_LIST_DATA, Context.MODE_PRIVATE);

        //保存したビデオジャンルが無いときはnullを返却
        return data.getString(VIDEO_GENRE_LIST_DATA, null);
    }

    /**
     * ワンタイムトークン関連情報の取得.
     *
     * @param context コンテキスト
     * @return ワンタイムトークン情報構造体
     */
    public static OneTimeTokenData getOneTimeTokenData(final Context context) {
        //プリファレンスから読み込む
        SharedPreferences data = context.getSharedPreferences(
                ONE_TIME_TOKEN, Context.MODE_PRIVATE);
        String buffer = data.getString(ONE_TIME_TOKEN, "");

        //復号する
        String afterBuffer = StringUtils.getClearString(context, buffer);

        //読み込んだ物を分割
        OneTimeTokenData tokenData = new OneTimeTokenData(afterBuffer);


        return tokenData;
    }

    /**
     * ワンタイムトークン関連情報の書き込み.
     *
     * @param context   コンテキスト
     * @param tokenData 書き込むワンタイムトークン
     */
    public static void setOneTimeTokenData(final Context context, final OneTimeTokenData tokenData) {
        DTVTLogger.start();
        //書き込み用の文字列を作成する
        String buffer = tokenData.makeOneTimeTokenString();

        //暗号化
        String afterBuffer = StringUtils.getCipherString(context, buffer);

        //書き込む
        SharedPreferences data = context.getSharedPreferences(
                ONE_TIME_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(ONE_TIME_TOKEN, afterBuffer);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * ワンタイムトークンの削除を行う.
     *
     * @param context コンテキスト
     */
    public static void deleteOneTimeTokenData(final Context context) {
        DTVTLogger.start();
        SharedPreferences deleteData = context.getSharedPreferences(
                ONE_TIME_TOKEN, Context.MODE_PRIVATE);
        deleteData.edit().clear().apply();
        DTVTLogger.end();
    }

    /**
     * ユーザ情報永続化.
     *
     * @param context                     コンテキスト
     * @param serializablePreferencesData ユーザ情報
     */
    public static void setSharedPreferencesSerializableData(
            final Context context,
            final SerializablePreferencesData serializablePreferencesData) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                USER_INFO_SERIALIZABLE_DATA_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        String preferencesData = StringUtils.toPreferencesDataBase64(serializablePreferencesData);
        editor.putString(USER_INFO_SERIALIZABLE_DATA_KEY, preferencesData);
        editor.apply();
        //今の日時を取得日時とする
        SharedPreferencesUtils.setSharedPreferencesUserInfoDate(context, System.currentTimeMillis() / 1000);
        DTVTLogger.end();
    }

    /**
     * 永続化ユーザ情報取得.
     *
     * @param context コンテキスト
     * @return ユーザ情報
     */
    public static List<UserInfoList> getSharedPreferencesUserInfo(final Context context) {
        SharedPreferences data = context.getSharedPreferences(
                USER_INFO_SERIALIZABLE_DATA_KEY, Context.MODE_PRIVATE);

        SerializablePreferencesData preferencesData
                = StringUtils.toPreferencesData(data.getString(USER_INFO_SERIALIZABLE_DATA_KEY, null));
        if (preferencesData == null) {
            return null;
        }
        //保存したひかりTVfordocomo視聴年齢値が無いときはnullを返却
        return preferencesData.getUserInfoList();
    }

//    /**
//     * 最初のdアカウント取得処理を行ったかどうかを問い合わせる.
//     *
//     * @param context コンテキスト
//     * @return 最初のdアカウント取得処理は既に行っていたならばfalse
//     */
//    public static boolean isFirstDaccountGetProcess(final Context context) {
//        DTVTLogger.start();
//        SharedPreferences data = context.getSharedPreferences(
//                FIRST_D_ACCOUNT_GET_KEY, Context.MODE_PRIVATE);
//
//        //現在のステータスを取得する
//        int status = data.getInt(FIRST_D_ACCOUNT_GET_KEY, FIRST_D_ACCOUNT_GET_BEFORE);
//
//        //実行後ならば、falseを返却
//        if (status == FIRST_D_ACCOUNT_GET_AFTER) {
//            DTVTLogger.end();
//            return false;
//        }
//
//        DTVTLogger.end();
//        //実行後以外ならばtrueを返却
//        return true;
//    }
//
//    /**
//     * dアカウント取得実行状況の書き込みを行う.
//     *
//     * @param context コンテキスト
//     * @param status  実行状況
//     */
//    public static void setFirstExecFlag(final Context context, final int status) {
//        DTVTLogger.start();
//
//        //既に初回のdアカウント取得処理を行ったかどうかの確認
//        SharedPreferences data = context.getSharedPreferences(
//                FIRST_D_ACCOUNT_GET_KEY, Context.MODE_PRIVATE);
//        int nowStatus = data.getInt(FIRST_D_ACCOUNT_GET_KEY, FIRST_D_ACCOUNT_GET_BEFORE);
//
//        //実行後ならば、何もせずに帰る
//        if (nowStatus == FIRST_D_ACCOUNT_GET_AFTER) {
//            DTVTLogger.end();
//            return;
//        }
//        int modifyStatus = status;
//        //強制的にリセット
//        if (modifyStatus == FIRST_D_ACCOUNT_FORCE_RESET) {
//            modifyStatus = 0;
//        }
//
//        //新たなステータスを書き込む
//        SharedPreferences.Editor editor = data.edit();
//        editor.putInt(FIRST_D_ACCOUNT_GET_KEY, modifyStatus);
//        editor.apply();
//        DTVTLogger.end();
//    }
//
//    /**
//     * 初回のdアカウント取得処理を行う前に呼び出す.
//     *
//     * @param context コンテキスト
//     */
//    public static void setFirstExecStart(final Context context) {
//        DTVTLogger.start();
//        setFirstExecFlag(context, FIRST_D_ACCOUNT_GET_NOW);
//        DTVTLogger.end();
//    }
//
//    /**
//     * 初回のdアカウント取得処理を行った後に呼び出す.
//     *
//     * @param context コンテキスト
//     */
//    public static void setFirstExecEnd(final Context context) {
//        DTVTLogger.start();
//        setFirstExecFlag(context, FIRST_D_ACCOUNT_GET_AFTER);
//        DTVTLogger.end();
//    }

    /**
     * セッティングファイルの最終取得日時を保存.
     *
     * @param context コンテキスト
     * @param getTime 取得日時
     */
    public static void setSharedPreferencesSettingFileDate(final Context context,
                                                           final long getTime) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_DATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putLong(LAST_SETTING_FILE_DATE, getTime);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * セッティングファイルの最終取得日時を取得.
     *
     * @param context コンテキスト
     * @return 最終取得日時
     */
    public static long getSharedPreferencesSettingFileDate(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_DATE, Context.MODE_PRIVATE);

        //最終取得日時を返す。存在していなければ、最小値を返す
        return data.getLong(LAST_SETTING_FILE_DATE, Long.MIN_VALUE);
    }

    /**
     * セッティングファイルの停止情報を保存.
     *
     * @param context コンテキスト
     * @param isStop アプリ停止ならばtrue
     */
    public static void setSharedPreferencesSettingFileIsStop(final Context context,
                                                           final boolean isStop) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_IS_STOP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(LAST_SETTING_FILE_IS_STOP, isStop);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * セッティングファイルの停止情報を取得.
     *
     * @param context コンテキスト
     * @return アプリ停止ならばtrue
     */
    public static boolean getSharedPreferencesSettingFileIsStop(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_IS_STOP, Context.MODE_PRIVATE);

        //アプリ停止スイッチを返す。存在していなければ、trueを返す（ファイル読み込み失敗は停止扱いなので）
        return data.getBoolean(LAST_SETTING_FILE_IS_STOP, true);
    }

    /**
     * セッティングファイルの読み込みエラー情報を保存.
     *
     * @param context コンテキスト
     * @param isReadFail 読み込み失敗ならばtrue
     */
    public static void setSharedPreferencesSettingFileIsReadFail(final Context context,
                                                                 final boolean isReadFail) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_IS_FILE_READ_FAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(LAST_SETTING_FILE_IS_FILE_READ_FAIL, isReadFail);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * セッティングファイルの読み込みエラー情報を取得.
     *
     * @param context コンテキスト
     * @return 読み込み失敗ならばtrue
     */
    public static boolean getSharedPreferencesSettingFileIsReadFail(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_IS_FILE_READ_FAIL, Context.MODE_PRIVATE);

        //ファイルの読み込みに失敗していればtrueを返す
        return data.getBoolean(LAST_SETTING_FILE_IS_FILE_READ_FAIL, true);
    }

    /**
     * セッティングファイルの停止メッセージを保存.
     *
     * @param context コンテキスト
     * @param message 停止メッセージ
     */
    public static void setSharedPreferencesSettingFileStopMessage(final Context context,
                                                             final String message) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_STOP_MESSAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(LAST_SETTING_FILE_STOP_MESSAGE, message);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * セッティングファイルの停止メッセージを取得.
     *
     * @param context コンテキスト
     * @return アプリ停止メッセージ
     */
    public static String getSharedPreferencesSettingFileStopMessage(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_STOP_MESSAGE, Context.MODE_PRIVATE);

        //停止時メッセージを返す。存在していなければ、空文字
        return data.getString(LAST_SETTING_FILE_STOP_MESSAGE, "");
    }

    /**
     * セッティングファイルの強制アップデートバージョンを保存.
     *
     * @param context コンテキスト
     * @param forceUpdate 取得日時
     */
    public static void setSharedPreferencesSettingFileForceUpdate(final Context context,
                                                                  final int forceUpdate) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_FORCE_UPDATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt(LAST_SETTING_FILE_FORCE_UPDATE, forceUpdate);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * セッティングファイルの強制アップデートバージョンを取得.
     *
     * @param context コンテキスト
     * @return アプリ停止メッセージ
     */
    public static int getSharedPreferencesSettingFileForceUpdate(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_FORCE_UPDATE, Context.MODE_PRIVATE);

        //バージョン番号を返す。存在していなければ、0を返す
        return data.getInt(LAST_SETTING_FILE_FORCE_UPDATE, 0);
    }

    /**
     * セッティングファイルの任意アップデートバージョンを保存.
     *
     * @param context コンテキスト
     * @param optionalUpdate 取得日時
     */
    public static void setSharedPreferencesSettingFileOptionalUpdate(final Context context,
                                                                  final int optionalUpdate) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_OPTIONAL_UPDATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt(LAST_SETTING_FILE_OPTIONAL_UPDATE, optionalUpdate);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * セッティングファイルの任意アップデートバージョンを取得.
     *
     * @param context コンテキスト
     * @return アプリ停止メッセージ
     */
    public static int getSharedPreferencesSettingFileOptionalUpdate(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_SETTING_FILE_OPTIONAL_UPDATE, Context.MODE_PRIVATE);

        //バージョン番号を返す。存在していなければ、0を返す
        return data.getInt(LAST_SETTING_FILE_OPTIONAL_UPDATE, 0);
    }

    /**
     * パーミッションチェックダイアログの表示設定.
     *
     * @param context     コンテキスト
     */
    public static void setSharedPreferencesIsDisplayedPermissionDialogTwice(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                PERMISSION_DIALOG_DISPLAYED_TWICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(PERMISSION_DIALOG_DISPLAYED_TWICE, true);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * パーミッションチェックダイアログの表示設定情報を取得.
     *
     * @param context コンテキスト
     * @return パーミッションチェックダイアログの表示設定フラグ
     */
    public static boolean getSharedPreferencesIsDisplayedPermissionDialogTwice(final Context context) {
        DTVTLogger.debug("getSharedPreferencesIsDisplayedPermissionDialogTwice");
        SharedPreferences data = context.getSharedPreferences(
                PERMISSION_DIALOG_DISPLAYED_TWICE, Context.MODE_PRIVATE);

        return data.getBoolean(PERMISSION_DIALOG_DISPLAYED_TWICE, false);
    }

    /**
     * ローカルレジストレーション期限表示ダイアログフラグを保存.
     *
     * @param context コンテキスト
     * @param dialogFlg ローカルレジストレーション期限表示ダイアログフラグ
     */
    public static void setRegisterExpiredateDialogFlg(final Context context, final int dialogFlg) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                REGISTER_EXPIREDATE_DIALOG_FLG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt(REGISTER_EXPIREDATE_DIALOG_FLG, dialogFlg);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * ローカルレジストレーション期限表示ダイアログを取得.
     *
     * @param context コンテキスト
     * @return ローカルレジストレーション期限表示ダイアログ進捗
     */
    public static int getRegisterExpiredateDialogFlg(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                REGISTER_EXPIREDATE_DIALOG_FLG, Context.MODE_PRIVATE);

        //保存したローカルレジストレーション期限表示ダイアログ進捗を返却
        return data.getInt(REGISTER_EXPIREDATE_DIALOG_FLG, DlnaUtils.REGISTER_EXPIREDATE_DIALOG_FLG_INIT);
    }

    /**
     * ローカルレジストレーションが成功したので、現在時刻を記録する.
     *
     * @param context コンテキスト
     */
    public static void setRegistTime(final Context context) {
        DTVTLogger.start();

        if (context != null) {
            SharedPreferences data = context.getSharedPreferences(
                    LOCAL_REGIST_SUCCCES_TIME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = data.edit();
            editor.putLong(LOCAL_REGIST_SUCCCES_TIME, DateUtils.getNowTimeFormatEpoch());
            editor.apply();
        }

        DTVTLogger.end();
    }

    /**
     * ローカルレジストレーション時刻を削除する.
     *
     * @param context コンテキスト
     */
    public static void deleteRegistTime(final Context context) {
        DTVTLogger.start();
        SharedPreferences deleteData = context.getSharedPreferences(
                LOCAL_REGIST_SUCCCES_TIME, Context.MODE_PRIVATE);
        deleteData.edit().clear().apply();
        DTVTLogger.end();
    }

    /**
     * 前回ローカルレジストから24時間経過しているかどうかを見る.
     *
     * @param context コンテキスト
     * @return 前回ローカルレジストから24時間以上経過していればtrue
     */
    public static boolean isLocalRegistAfter24Hour(final Context context) {
        DTVTLogger.start();

        //結果
        boolean answer = false;

        //比較用のエポック秒を取得
        long epochNowTime = DateUtils.getNowTimeFormatEpoch();

        //前回のローカルレジスト成功時間を取得(値が無かった場合は、ローカルレジストを行わないように、現在日時-1時間を設定)
        SharedPreferences data = context.getSharedPreferences(
                LOCAL_REGIST_SUCCCES_TIME, Context.MODE_PRIVATE);
        long beforeTime = data.getLong(LOCAL_REGIST_SUCCCES_TIME,
                epochNowTime - DateUtils.EPOCH_TIME_ONE_HOUR);

        if (beforeTime + DateUtils.EPOCH_TIME_ONE_DAY < epochNowTime
                || beforeTime > epochNowTime) {
            // 前回成功時から24時間経過した場合は、trueにする。
            // また、前回成功時の時刻が今より未来の場合、端末の時刻の操作が行われたのでtrueとする
            answer = true;
        }

        DTVTLogger.end();
        return answer;
    }

    /**
     * dアカウントが未認証である事を明示する処理.
     *
     * dアカウントの値が空白か否かでは、ログオフとログオンは判定できるが、未認証は判定できない。
     * dアカウントアプリから未認証を返された際にtrueをセットして、直接分かるようにする
     * @param context コンテキスト
     * @param certifiedFlag dアカウントが未認証ならばtrue
     */
    public static void setUncertifiedDaccount(Context context, final boolean certifiedFlag) {
        DTVTLogger.start();

        if (context != null) {
            SharedPreferences data = context.getSharedPreferences(
                    D_ACCOUNT_UNCERTIFIED_DACCOUNT, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = data.edit();
            editor.putBoolean(D_ACCOUNT_UNCERTIFIED_DACCOUNT, certifiedFlag);
            editor.apply();
        }

        DTVTLogger.end();
    }

    /**
     * dアカウントが未認証かどうかを取得する処理.
     *
     * @param context コンテキスト
     * @return dアカウントが未認証ならばtrue
     */
    public static boolean isUncertifiedDaccount(final Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                D_ACCOUNT_UNCERTIFIED_DACCOUNT, Context.MODE_PRIVATE);

        //保存したdアカウント未認証状態を返却
        return data.getBoolean(D_ACCOUNT_UNCERTIFIED_DACCOUNT, false);
    }
}