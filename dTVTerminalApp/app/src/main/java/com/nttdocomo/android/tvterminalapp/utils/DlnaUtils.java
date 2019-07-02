/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Pair;

import com.digion.dixim.android.activation.helper.ActivationHelper;
import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * DLNA共通Utilクラス.
 */
public class DlnaUtils {

    /**証明書ファイル、登録情報などのファイル.*/
    private static final String DIAG_ASSETS_DIR_ROOT = "drm/conf";
    /**リモート接続時、デバイスの情報が保持されます.*/
    private static final String DIAG_ASSETS_DIR_RADA_RELAY = "/dirag/rada/rada_relay";
    /** DiRAGコンフィグファイルパス. */
    private static final String DIRAG_CONF_FILE = "/dirag/drag_configuration.xml-turn";

    /**UTF-8.*/
    private static final String FILE_ENCODE = "UTF-8";
    /**書き換える文字列.*/
    private static final String REPLACE_STR = "[CONFDIR]";
    /**バファサイズ.*/
    private static final int BUF_SIZE = 1024;
    /*視聴画質設定*/
    /** デフォルト.*/
    public static final String IMAGE_QUALITY_DEFAULT_URL = "0/smartphone/";
    /** 最高画質.*/
    public static final String IMAGE_QUALITY_HIGH_URL = "0/remote1/";
    /** 高画質.*/
    public static final String IMAGE_QUALITY_MIDDLE_URL = "0/remote2/";
    /** 標準画質.*/
    public static final String IMAGE_QUALITY_LOW_URL = "0/remote3/";
    /** 録画一覧.*/
    public static final String DLNA_DMS_RECORD_LIST = "rec/all";
    /** 多チャンネル.*/
    public static final String DLNA_DMS_MULTI_CHANNEL = "ip";
    /** 地上デジタル.*/
    public static final String DLNA_DMS_TER_CHANNEL = "tb";
    /** BSデジタル.*/
    public static final String DLNA_DMS_BS_CHANNEL = "bs";
    /*視聴画質設定*/
    /*ローカルレジストレーション期限表示*/
    /** 残日数0.*/
    private static final int REMAINING_ZERO_DAYS = 0;
    /** 残日数1.*/
    private static final int REMAINING_ONE_DAYS = 1;
    /** 残日数7.*/
    private static final int REMAINING_SEVEN_DAYS = 7;
    /** 残日数14.*/
    private static final int REMAINING_FOURTEEN_DAYS = 14;

    /** ローカルレジストレーション期限ダイアログ表示進捗 初期値.*/
    public static final int REGISTER_EXPIREDATE_DIALOG_FLG_INIT = 0;
    /** ローカルレジストレーション期限ダイアログ表示進捗1(14日を切った).*/
    private static final int REGISTER_EXPIREDATE_DIALOG_FLG_ONE = 1;
    /** ローカルレジストレーション期限ダイアログ表示進捗2(7日を切った).*/
    private static final int REGISTER_EXPIREDATE_DIALOG_FLG_TWO = 2;
    /** ローカルレジストレーション期限ダイアログ表示進捗3(前日).*/
    private static final int REGISTER_EXPIREDATE_DIALOG_FLG_THREE = 3;
    /** ローカルレジストレーション期限ダイアログ表示進捗4(当日).*/
    private static final int REGISTER_EXPIREDATE_DIALOG_FLG_FOUR = 4;
    /** ローカルレジストレーション期限ダイアログ表示進捗5(期限切れ).*/
    private static final int REGISTER_EXPIREDATE_DIALOG_FLG_FIVE = 5;
    /*ローカルレジストレーション期限表示*/
    /**ダウンロード通知チャンネルID.*/
    private static final String DOWNLOAD_NOTIFICATION_ID = "downloadProgress";

    //DDTCP Success
    public static final int INT_DDTCP_RET_SUCCESS = 0;

    //エラーコード細分化により定数定義
    private static final String STR_SPACE = " ";
    private static final String STR_PADDING_CHARACTER = "0";
    private static final String STR_CODE_HEAD_SOAP = "1";
    private static final String STR_CODE_HEAD_HTTP = "2";
    private static final String STR_CODE_HEAD_SOCKET = "3";
    private static final String STR_CODE_HEAD_OTHER = "4";
    private static final String STR_CODE_HEAD_START_DTCP = "5";
    private static final String STR_CODE_HEAD_START_DIRAG = "6";
    private static final String STR_CODE_HEAD_REQUEST = "7";
    private static final String STR_CODE_REQUEST_ERROR = "99996";
    private static final String STR_CODE_UNKNOWN_ERROR = "99999";
    public static final String STR_CODE_USER_INFO_GET_ERROR = "899998";
    public static final String STR_CODE_NOT_CONTRACT_ERROR = "899999";

    /**リモート接続エラーコード.*/
    private static final int RATUN_MANAGER_ERROR_CODE_NOT_INITIALIZED = 1201;
    private static final int RATUN_MANAGER_ERROR_CODE_AUTH_ERROR = 2026;
    private static final int RATUN_MANAGER_ERROR_CODE_INTERNAL = 1200;
    private static final int RATUN_MANAGER_ERROR_CODE_NETTYPE_UNKNOWN = 1501;
    private static final int RATUN_MANAGER_ERROR_CODE_NETTYPE_BLOCKED = 1502;
    private static final int RATUN_MANAGER_ERROR_CODE_NETTYPE_SYMMETRIC = 1503;
    private static final int RATUN_MANAGER_ERROR_CODE_AUTH_ERROR_PROHIBITED_SERVICE_P = 2014;
    private static final int RATUN_MANAGER_ERROR_CODE_AUTH_ERROR_EXCEEDED_CERT_D = 2019;
    private static final int RATUN_MANAGER_ERROR_CODE_AUTH_ERROR_EXCEEDED_TICKET_1 = 2020;
    private static final int RATUN_MANAGER_ERROR_CODE_AUTH_ERROR_EXCEEDED_TICKET_2 = 2021;
    private static final int RATUN_SC_BUSY_HERE = 486;
    private static final int RATUN_SC_NOT_ACCEPTABLE_HERE = 488;
    private static final int RATUN_SC_NOT_ACCEPTABLE_ANYWHERE = 606;
    private static final int RATUN_SC_NOT_FOUND = 404;
    private static final int RATUN_SC_REQUEST_TIMEOUT = 408;
    private static final int RATUN_SC_TEMPORARILY_UNAVAILABLE = 480;
    public static final int ERROR_CODE_NOT_INITIALIZED = 0;
    public static final int ERROR_CODE_REMOTE_CONNECT_FAILED_DIRAG = 9997;
    public static final int ERROR_CODE_LOCAL_REGISTRATION_UNSET = 9998;
    public static final int ERROR_CODE_REMOTE_CONNECT_TIME_OUT = 9999;

    /**
     * ローカルレジストレーションエラータイプ.
     */
    public enum ExecuteLocalRegistrationErrorType {
        /**未契約(h4d)エラー.*/
        NO_H4D_CONTRACT,
        /**jniからのリクエストエラー.*/
        REQUEST_ERROR_FROM_JNI,
        /**Javaのリクエストエラー.*/
        REQUEST_ERROR_FROM_JAVA,
        /**アクティベーション失敗.*/
        ACTIVATION,
        /**Dirag起動失敗.*/
        START_DIRAG,
        /**dtcp起動失敗.*/
        START_DTCP,
        /**dtcp台数超過エラー.*/
        DTCP_DEVICE_OVER,
        /**dtcpその他エラー.*/
        DTCP_OTHER,
        /**SOCKETエラー.*/
        SOCKET,
        /**台数超過エラー.*/
        SOAP_DEVICE_OVER,
        /**SOAPエラー.*/
        SOAP,
        /**HTTPエラー.*/
        HTTP,
        /**その他エラー.*/
        OTHER,
        /**なし.*/
        NONE
    }

    /**
     * リモート接続エラータイプ.
     */
    public enum RemoteConnectErrorType {
        /**dtcp起動失敗.*/
        START_DTCP,
        /**Dirag起動失敗.*/
        START_DIRAG,
        /**リモート接続状態エラー.*/
        REMOTE_CONNECT_STATUS,
        /**なし.*/
        NONE
    }

    /**
     * ローカルレジストレーション実行.
     * @param context コンテスト
     * @param localRegisterListener リスナー
     * @return エラータイプ
     */
    public static ExecuteLocalRegistrationErrorType executeLocalRegistration(final Context context, final DlnaManager.LocalRegisterListener localRegisterListener) {
        int result;
        DlnaManager.shared().clearErrorCode();
        if (context == null || localRegisterListener == null) {
            // エラーコード設定
            DlnaManager.shared().setLocalRegistrationErrorCode(STR_CODE_REQUEST_ERROR);
            return ExecuteLocalRegistrationErrorType.REQUEST_ERROR_FROM_JAVA;
        }

        Pair<Boolean, Integer> activationState = DlnaUtils.getActivationState(context);
        if (!activationState.first) {
            // エラーコード設定
            DlnaManager.shared().setLocalRegistrationErrorCode(String.valueOf(activationState.second));
            return ExecuteLocalRegistrationErrorType.ACTIVATION;
        }

        result = DlnaManager.shared().StartDtcp();
        if (result != INT_DDTCP_RET_SUCCESS) {
            // エラーコード設定
            DlnaManager.shared().setLocalRegistrationErrorCode(String.valueOf(result));
            return  ExecuteLocalRegistrationErrorType.START_DTCP;
        }

        if (!DlnaManager.shared().RestartDirag()) {
            // エラーコード設定
            DlnaManager.shared().setLocalRegistrationErrorCode(STR_CODE_UNKNOWN_ERROR);
            return ExecuteLocalRegistrationErrorType.START_DIRAG;
        }

        DlnaManager.shared().mLocalRegisterListener = localRegisterListener;
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(context);
        DlnaManager.shared().RequestLocalRegistration(dlnaDmsItem.mUdn, context);
        //エラーコードはDlnaManager.RegistResultCallBackで渡している。
        return ExecuteLocalRegistrationErrorType.NONE;
    }

    /**
     * アクティベーションのチェック、実行.
     * @param location location
     * @return ホスト
     */
    public static String getHost(final String location) {
        URL hostUrl;
        String hostString = "";
        try {
            hostUrl = new URL(location);
            hostString = hostUrl.getHost();
        } catch (MalformedURLException e) {
            DTVTLogger.debug(e);
        }
        return hostString;
    }

    /**
     * アクティベーションのチェック、実行.
     * @param context コンテキスト
     * @return チェック結果： first:Activate結果、second:エラーコード
     */
    public static Pair<Boolean, Integer> getActivationState(final Context context) {
        final String deviceKey = getPrivateDataHomePath(context);
        ActivationHelper activationHelper = new ActivationHelper(context, deviceKey);
        boolean activationState = activationHelper.activationState(deviceKey);
        if (activationState) {
            return new Pair<>(true, ActivationHelper.ACTC_OK);
        }

        int result = activationHelper.activation(deviceKey);
        return new Pair<>(result == ActivationHelper.ACTC_OK, result);
    }

    /**
     * アクティベーションパス（devicekeyPath）取得.
     * @param context コンテキスト
     * @return アクティベーションパス
     */
    public static String getPrivateDataHomePath(final Context context) {
        return EnvironmentUtil.getPrivateDataHome(context, EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER);
    }

    /**
     * DiRAGコンフィグファイルパス取得.
     * @param context コンテキスト
     * @return DiRAGコンフィグファイルパス
     */
    public static String getDiragConfileFilePath(final Context context) {
        return getPrivateDataHomePath(context).concat(DIRAG_CONF_FILE);
    }
    /**
     * ローカルレジストレーション、リモート接続事前準備.
     * @param context コンテキスト
     */
    public static void copyDiragConfigFile(final Context context) {
        //アクティベーションパス（devicekeyPath）
        String destDir = getPrivateDataHomePath(context);
        //ローカルレジストレーション、接続時に使うファイルのコピー
        copyConfFiles(context, DIAG_ASSETS_DIR_ROOT, destDir);
        //リモート接続時、情報が保持されるパース
        makeRedaRelayDir(destDir);
    }

    /**
     * リモート接続時、フォルダに仮想デバイスの情報が保持されます.
     * @param destDir destDir
     */
    private static void makeRedaRelayDir(final String destDir) {
        String radaRelayDest = destDir + DIAG_ASSETS_DIR_RADA_RELAY;
        File redaRelayFolder = new File(radaRelayDest);
        if (!redaRelayFolder.exists()) {
            if (!redaRelayFolder.mkdirs()) {
                DTVTLogger.debug("リモート接続デバイスの情報保持パース作成失敗しました。");
            }
        }
    }

    /**
     * ローカルレジストレーション、接続時に使うファイルのコピーを行う.
     *
     * @param context   コンテキスト
     * @param assetsConfPath コピー元
     * @param destPath  コピー先
     */
    private static void copyConfFiles(final Context context, final String assetsConfPath, final String destPath) {
        String[] fileNames = null;
        AssetManager am = context.getResources().getAssets();
        try {
            fileNames = am.list(assetsConfPath);
        } catch (IOException e) {
            DTVTLogger.debug(e);
        }
        if (fileNames != null && fileNames.length > 0) {
            File file = new File(destPath);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    DTVTLogger.debug("リモート接続の事前コピーのファイルパース作成失敗しました。");
                }
            }
            for (String fileName : fileNames) {
                copyConfFiles(context, assetsConfPath + File.separator + fileName, destPath + File.separator + fileName);
            }
        } else {
            File file = new File(destPath);
            if (!file.exists()) {
                copyConfFile(context, assetsConfPath, destPath);
            }
        }
    }

    /**
     * 書換えが必要なファイルのコピー.
     *
     * @param context コンテキスト
     * @param oldPath コピー元
     * @param newPath コピー先
     */
    private static void copyConfFile(final Context context, final String oldPath, final String newPath) {
        File dest = new File(newPath);
        File parent = dest.getParentFile();
        if (null != parent && !parent.exists()) {
            if (!parent.mkdirs()) {
                DTVTLogger.debug("書換えが必要なファイルのコピーパース作成失敗しました。");
            }
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(context.getAssets().open(oldPath));
            String conDir = getPrivateDataHomePath(context);
            in = filterSpecial(in, conDir);
            out = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] buffer = new byte[BUF_SIZE];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            DTVTLogger.debug(e);
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
            }
        }
    }

    /**
     * 特別な配列を書き換える.
     * @param is is
     * @param replace replace
     * @return InputStream
     */
    private static InputStream filterSpecial(final InputStream is, final String replace) {
        InputStream ret = null;
        try {
            String temp = inputStreamToString(is);
            if (temp != null) {
                temp = temp.replace(REPLACE_STR, replace);
                ret = new ByteArrayInputStream(temp.getBytes(FILE_ENCODE));
            }
        } catch (UnsupportedEncodingException e) {
            DTVTLogger.debug(e);
        }
        return ret;
    }

    /**
     * inputStreamをStringに変換.
     * @param in InputStream
     * @return string
     */
    private static String inputStreamToString(final InputStream in) {
        String result = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUF_SIZE];
        int count;
        try {
            while ((count = in.read(data, 0, BUF_SIZE)) != -1) {
                outStream.write(data, 0, count);
            }
            result = new String(outStream.toByteArray(), FILE_ENCODE);
        } catch (IOException e) {
            DTVTLogger.debug(e);
        }
        return result;
    }

    /**
     * ローカルレジストレーション期限表示ダイアログメッセージ.
     *
     * @param context コンテキスト
     * @return msg 期限表示ダイアログメッセージ
     */
    public static String getLocalRegisterExpireDateCheckMessage(final Context context) {
        DTVTLogger.start();
        String msg = "";
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(context);
        int dialogFlg = SharedPreferencesUtils.getRegisterExpiredateDialogFlg(context);
        if (dlnaDmsItem != null) {
            String expireDate = SharedPreferencesUtils.getRemoteDeviceExpireDate(context);
            DTVTLogger.debug("Local Registration expireDate:" + expireDate);
            if (!TextUtils.isEmpty(expireDate)) {
                int remainingDays = DateUtils.getRemainingDays(expireDate);
                DTVTLogger.debug("Local Registration remainDays:" + remainingDays + "dialogFlg:" + dialogFlg);
                String[] strings = {context.getString(R.string.remote_remaining_days_message_begin),
                        Integer.toString(remainingDays),
                        context.getString(R.string.remote_remaining_days_message_end)};
                if (REMAINING_FOURTEEN_DAYS < remainingDays) {
                    //１５日以上前
                    // 時刻を進めてしまってから戻した場合向けに15日以上ならフラグクリアしておく
                    SharedPreferencesUtils.setRegisterExpiredateDialogFlg(context, REGISTER_EXPIREDATE_DIALOG_FLG_INIT);
                } else if (REMAINING_SEVEN_DAYS < remainingDays && remainingDays <= REMAINING_FOURTEEN_DAYS) {
                    //１４～７日前（時刻変更した場合向けにダイアログ出さない場合もフラグは上書き）
                    if (dialogFlg < REGISTER_EXPIREDATE_DIALOG_FLG_ONE) {
                        msg = StringUtils.getConnectString(strings);
                    }
                    SharedPreferencesUtils.setRegisterExpiredateDialogFlg(context, REGISTER_EXPIREDATE_DIALOG_FLG_ONE);
                } else if (REMAINING_ONE_DAYS < remainingDays && remainingDays <= REMAINING_SEVEN_DAYS) {
                    //７～２日前（時刻変更した場合向けにダイアログ出さない場合もフラグは上書き）
                    if (dialogFlg < REGISTER_EXPIREDATE_DIALOG_FLG_TWO) {
                        msg = StringUtils.getConnectString(strings);
                    }
                    SharedPreferencesUtils.setRegisterExpiredateDialogFlg(context, REGISTER_EXPIREDATE_DIALOG_FLG_TWO);
                } else if (remainingDays == REMAINING_ONE_DAYS) {
                    //前日（時刻変更した場合向けにダイアログ出さない場合もフラグは上書き）
                    if (dialogFlg < REGISTER_EXPIREDATE_DIALOG_FLG_THREE) {
                        msg = StringUtils.getConnectString(strings);
                    }
                    SharedPreferencesUtils.setRegisterExpiredateDialogFlg(context, REGISTER_EXPIREDATE_DIALOG_FLG_THREE);
                } else if (remainingDays <= REMAINING_ZERO_DAYS) {
                    // 残り0日を切った場合、期限切れ又は当日メッセージ.期限切れは接続できなくなるので厳密に判定しておく
                    if (DateUtils.isExpired(expireDate)) {
                        if (dialogFlg < REGISTER_EXPIREDATE_DIALOG_FLG_FIVE) {
                            msg = context.getString(R.string.remote_expired_message);
                        }
                        SharedPreferencesUtils.setRegisterExpiredateDialogFlg(context, REGISTER_EXPIREDATE_DIALOG_FLG_FIVE);
                    } else {
                        if (dialogFlg < REGISTER_EXPIREDATE_DIALOG_FLG_FOUR) {
                            msg = context.getString(R.string.remote_remaining_zero_day_message);
                        }
                        SharedPreferencesUtils.setRegisterExpiredateDialogFlg(context, REGISTER_EXPIREDATE_DIALOG_FLG_FOUR);
                    }
                }
            }
        }
        DTVTLogger.end();
        return msg;
    }

    /**
     * 設定した画質を取得.
     *
     * @param context コンテキスト
     * @param channel チャンネル種別
     * @return 設定した画質のURL
     */
    public static String getContainerIdByImageQuality(final Context context, final String channel) {
        StbConnectionManager.ConnectionStatus connectionStatus = StbConnectionManager.shared().getConnectionStatus();
        boolean isRemote = connectionStatus == StbConnectionManager.ConnectionStatus.HOME_OUT
                || connectionStatus == StbConnectionManager.ConnectionStatus.HOME_OUT_CONNECT;
        if (context == null) {
            return IMAGE_QUALITY_DEFAULT_URL + channel;
        }
        String imageQualitySetting = SharedPreferencesUtils.getSharedPreferencesImageQuality(context);
        if (!isRemote || TextUtils.isEmpty(imageQualitySetting)) {
            return IMAGE_QUALITY_DEFAULT_URL + channel;
        } else {
            if (imageQualitySetting.equals(context.getString(R.string.main_setting_image_quality_high))) {
                return IMAGE_QUALITY_HIGH_URL + channel;
            } else if (imageQualitySetting.equals(context.getString(R.string.main_setting_image_quality_middle))) {
                return IMAGE_QUALITY_MIDDLE_URL + channel;
            } else if (imageQualitySetting.equals(context.getString(R.string.main_setting_image_quality_low))) {
                return IMAGE_QUALITY_LOW_URL + channel;
            }
        }
        return IMAGE_QUALITY_DEFAULT_URL + channel;
    }

    /**
     * 通知表示.
     * @param title ダウンロード中番組タイトル
     * @param text テキスト
     * @param context コンテキスト
     * @return 通知
     */
    public static Notification getNotification(final String title, final String text, final Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "");
        builder.setSmallIcon(R.mipmap.icon_normal_tv);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icd_app_tvterminal));
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(DOWNLOAD_NOTIFICATION_ID,
                    context.getString(R.string.record_download_notification_channel_name), NotificationManager.IMPORTANCE_HIGH);
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(mChannel);
            builder.setChannelId(DOWNLOAD_NOTIFICATION_ID);
        }
        return builder.build();
    }

    /**
     * ローカルレジストレーションの処理結果.
     *
     * @param context コンテキスト
     * @param isSuccess true 成功 false 失敗
     * @param errorType エラータイプ
     * @param fixErrorCode エラーコード
     * @return ローカルレジストレーションの処理結果Pair first：エラーダイアログ、second:エラーコード
     */
    public static Pair<CustomDialog, String> getRegistResultDialog(final Context context, final boolean isSuccess, final ExecuteLocalRegistrationErrorType errorType, final String fixErrorCode) {
        CustomDialog resultDialog = new CustomDialog(context, CustomDialog.DialogType.ERROR);
        resultDialog.setOnTouchOutside(false);
        resultDialog.setCancelable(false);
        resultDialog.setOnTouchBackkey(false);
        String formatErrorCode = ContentUtils.STR_BLANK;
        if (isSuccess) {
            resultDialog.setContent(context.getString(R.string.common_text_regist_progress_done));
            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(context);
            String expireDate = DlnaManager.shared().GetRemoteDeviceExpireDate(dlnaDmsItem.mUdn);
            SharedPreferencesUtils.setRemoteDeviceExpireDate(context, expireDate);
        } else {
            String dialogMessage;
            switch (errorType) {
                case NO_H4D_CONTRACT:
                    //ユーザ情報取得失敗アラートエラー採番LR-899998
                    if (DlnaUtils.STR_CODE_USER_INFO_GET_ERROR.equals(fixErrorCode)) {
                        dialogMessage = context.getString(R.string.common_text_regist_no_h4d_contract_error, fixErrorCode);
                        formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error, fixErrorCode);
                    } else {
                        //そもそも未契約アラートエラー採番LR-899999
                        dialogMessage = context.getString(R.string.common_text_regist_no_h4d_contract_error, DlnaUtils.STR_CODE_NOT_CONTRACT_ERROR);
                        formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error, DlnaUtils.STR_CODE_NOT_CONTRACT_ERROR);
                    }
                    break;
                case ACTIVATION:
                    dialogMessage = context.getString(R.string.activation_failed_error_local_registration, fixErrorCode);
                    formatErrorCode = context.getString(R.string.error_prefix_activated_error, fixErrorCode);
                    break;
                case SOAP_DEVICE_OVER:
                    dialogMessage = context.getString(R.string.common_text_regist_over_error_setting, STR_CODE_HEAD_SOAP + fixErrorCode);
                    formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error, STR_CODE_HEAD_SOAP + fixErrorCode);
                    break;
                case SOAP:
                    dialogMessage = context.getString(R.string.common_text_regist_other_error, STR_CODE_HEAD_SOAP + fixErrorCode);
                    formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error, STR_CODE_HEAD_SOAP + fixErrorCode);
                    break;
                case HTTP:
                    dialogMessage = context.getString(R.string.common_text_regist_other_error, STR_CODE_HEAD_HTTP + fixErrorCode);
                    formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error, STR_CODE_HEAD_HTTP + fixErrorCode);
                    break;
                case SOCKET:
                    dialogMessage = context.getString(R.string.common_text_regist_other_error,  STR_CODE_HEAD_SOCKET + fixErrorCode);
                    formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error, STR_CODE_HEAD_SOCKET + fixErrorCode);
                    break;
                case OTHER:
                    dialogMessage = context.getString(R.string.common_text_regist_other_error,  STR_CODE_HEAD_OTHER + STR_CODE_UNKNOWN_ERROR);
                    formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error,
                            STR_CODE_HEAD_OTHER + STR_CODE_UNKNOWN_ERROR);
                    break;
                case DTCP_DEVICE_OVER:
                    dialogMessage = context.getString(R.string.common_text_regist_over_error_setting, STR_CODE_HEAD_START_DTCP + fixErrorCode);
                    formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error,
                            STR_CODE_HEAD_START_DTCP + fixErrorCode);
                    break;
                case START_DTCP:
                case DTCP_OTHER:
                    dialogMessage = context.getString(R.string.common_text_regist_other_error,  STR_CODE_HEAD_START_DTCP + fixErrorCode);
                    formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error,
                            STR_CODE_HEAD_START_DTCP + fixErrorCode);
                    break;
                case START_DIRAG:
                    dialogMessage = context.getString(R.string.common_text_regist_other_error,  STR_CODE_HEAD_START_DIRAG + fixErrorCode);
                    formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error,
                            STR_CODE_HEAD_START_DIRAG + fixErrorCode);
                    break;
                case REQUEST_ERROR_FROM_JNI:
                    dialogMessage = context.getString(R.string.common_text_regist_other_error,  STR_CODE_HEAD_REQUEST + fixErrorCode);
                    formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error,
                            STR_CODE_HEAD_REQUEST + fixErrorCode);
                    break;
                case REQUEST_ERROR_FROM_JAVA:
                    dialogMessage = context.getString(R.string.common_text_regist_other_error,  STR_CODE_HEAD_REQUEST + fixErrorCode);
                    formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error,
                            STR_CODE_HEAD_REQUEST + fixErrorCode);
                    break;
                default:
                    dialogMessage = context.getString(R.string.common_text_regist_other_error, STR_CODE_UNKNOWN_ERROR);
                    formatErrorCode = context.getString(R.string.error_prefix_type_local_registration_error, STR_CODE_UNKNOWN_ERROR);
                    break;
            }
            resultDialog.setContent(dialogMessage);
            resultDialog.setConfirmText(R.string.common_text_close);
        }
        return new Pair<>(resultDialog, formatErrorCode);
    }

    /**
     * エラーコードを整形する.
     * @param errorCode .
     * @return errorCode
     */
    public static String formatErrorCode(final String errorCode) {
        String formatErrorCode = errorCode;
        if (DlnaUtils.STR_CODE_USER_INFO_GET_ERROR.equals(formatErrorCode)) {
            return formatErrorCode;
        }

        if (TextUtils.isEmpty(formatErrorCode)) {
            formatErrorCode = STR_CODE_UNKNOWN_ERROR;
        }

        formatErrorCode = String.format("%5s", formatErrorCode).replace(STR_SPACE, STR_PADDING_CHARACTER); //エラーコード桁数調整、0埋め込み
        return formatErrorCode;
    }

    /**
     * リモート接続エラーメッセージ取得.
     *
     * @param context コンテキスト
     * @param errorType エラータイプ
     * @param errorCode エラーコード
     * @return Pair first：エラーメッセージ、second:エラーコード
     */
    public static  Pair<String, String> getDlnaErrorMessage(final Context context, final RemoteConnectErrorType errorType, final int errorCode) {
        String message;
        String formatErrorCode;
        switch (errorType) {
            case REMOTE_CONNECT_STATUS:
                switch (errorCode) {
                    case RATUN_MANAGER_ERROR_CODE_NOT_INITIALIZED:
                    case RATUN_MANAGER_ERROR_CODE_AUTH_ERROR:
                    case RATUN_MANAGER_ERROR_CODE_INTERNAL:
                    case RATUN_MANAGER_ERROR_CODE_NETTYPE_UNKNOWN:
                    case RATUN_MANAGER_ERROR_CODE_NETTYPE_BLOCKED:
                    case RATUN_MANAGER_ERROR_CODE_NETTYPE_SYMMETRIC:
                        message = context.getString(R.string.remote_connect_error_network_failed, String.valueOf(errorCode));
                        formatErrorCode = context.getString(R.string.error_prefix_type_remote_connect_error, String.valueOf(errorCode));
                        break;
                    case RATUN_MANAGER_ERROR_CODE_AUTH_ERROR_PROHIBITED_SERVICE_P:
                        message = context.getString(R.string.remote_connect_error_service_unavailable, String.valueOf(errorCode));
                        formatErrorCode = context.getString(R.string.error_prefix_type_remote_connect_error, String.valueOf(errorCode));
                        break;
                    case RATUN_SC_BUSY_HERE:
                        message = context.getString(R.string.remote_connect_error_already_connected, String.valueOf(errorCode));
                        formatErrorCode = context.getString(R.string.error_prefix_type_remote_connect_error, String.valueOf(errorCode));
                        break;
                    case RATUN_SC_NOT_ACCEPTABLE_HERE:
                        message = context.getString(R.string.remote_connect_error_local_registration_unset, String.valueOf(errorCode));
                        formatErrorCode = context.getString(R.string.error_prefix_type_remote_connect_error, String.valueOf(errorCode));
                        break;
                    case RATUN_SC_NOT_ACCEPTABLE_ANYWHERE:
                        message = context.getString(R.string.remote_connect_error_local_registration_expired, String.valueOf(errorCode));
                        formatErrorCode = context.getString(R.string.error_prefix_type_remote_connect_error, String.valueOf(errorCode));
                        break;
                    case RATUN_SC_NOT_FOUND:
                    case RATUN_SC_REQUEST_TIMEOUT:
                    case RATUN_SC_TEMPORARILY_UNAVAILABLE:
                        message = context.getString(R.string.remote_connect_error_stb_power_off, String.valueOf(errorCode));
                        formatErrorCode = context.getString(R.string.error_prefix_type_remote_connect_error, String.valueOf(errorCode));
                        break;
                    case RATUN_MANAGER_ERROR_CODE_AUTH_ERROR_EXCEEDED_CERT_D:
                    case RATUN_MANAGER_ERROR_CODE_AUTH_ERROR_EXCEEDED_TICKET_1:
                    case RATUN_MANAGER_ERROR_CODE_AUTH_ERROR_EXCEEDED_TICKET_2:
                    default:
                        message = context.getString(R.string.remote_connect_error_connect_failed, String.valueOf(errorCode));
                        formatErrorCode = context.getString(R.string.error_prefix_type_remote_connect_error, String.valueOf(errorCode));
                        break;
                }
                break;
            case START_DTCP:
                message = context.getString(R.string.remote_connect_error_connect_failed_ddtcp, String.valueOf(errorCode));
                formatErrorCode = context.getString(R.string.error_prefix_type_connect_failed_ddtcp_error, String.valueOf(errorCode));
                break;
            case START_DIRAG:
                message = context.getString(R.string.remote_connect_error_connect_failed_dirag, String.valueOf(DlnaUtils.ERROR_CODE_REMOTE_CONNECT_FAILED_DIRAG));
                formatErrorCode = context.getString(R.string.error_prefix_type_remote_connect_error, String.valueOf(DlnaUtils.ERROR_CODE_REMOTE_CONNECT_FAILED_DIRAG));
                break;
            case NONE:
            default:
                message = context.getString(R.string.remote_connect_error_connect_failed, String.valueOf(errorCode));
                formatErrorCode = context.getString(R.string.error_prefix_type_remote_connect_error, String.valueOf(errorCode));
                break;
        }
        return new Pair<>(message, formatErrorCode);
    }
}
