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

    /**
     * ローカルレジストレーションエラータイプ.
     */
    public enum ExcuteLocalRegistrationErrorType {
        /**アクティベーション失敗.*/
        ACTIVATION,
        /**Dirag起動失敗.*/
        START_DIRAG,
        /**dtcp起動失敗.*/
        START_DTCP,
        /**台数超過エラー.*/
        DEVICE_OVER_ERROR,
        /**その他エラー.*/
        UNKOWN,
        /**なし.*/
        NONE
    }

    /**
     * ローカルレジストレーション実行.
     * @param context コンテスト
     * @param mLocalRegisterListener リスナー
     * @return エラータイプ
     */
    public static ExcuteLocalRegistrationErrorType excuteLocalRegistration(final Context context, final DlnaManager.LocalRegisterListener mLocalRegisterListener) {
        ExcuteLocalRegistrationErrorType localRegistrationErrorType = ExcuteLocalRegistrationErrorType.NONE;
        if (context == null || mLocalRegisterListener == null) {
            localRegistrationErrorType = ExcuteLocalRegistrationErrorType.UNKOWN;
        } else if (!DlnaUtils.getActivationState(context)) {
            localRegistrationErrorType = ExcuteLocalRegistrationErrorType.ACTIVATION;
        } else if (!DlnaManager.shared().StartDtcp()) {
            localRegistrationErrorType = ExcuteLocalRegistrationErrorType.START_DTCP;
        } else if (!DlnaManager.shared().RestartDirag()) {
            localRegistrationErrorType = ExcuteLocalRegistrationErrorType.START_DIRAG;
        } else {
            DlnaManager.shared().mLocalRegisterListener = mLocalRegisterListener;
            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(context);
            DlnaManager.shared().RequestLocalRegistration(dlnaDmsItem.mUdn, context);
        }
        return localRegistrationErrorType;
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
     * @return チェック結果
     */
    public static boolean getActivationState(final Context context) {
        final String deviceKey = getPrivateDataHomePath(context);
        ActivationHelper activationHelper = new ActivationHelper(context, deviceKey);
        boolean activationState = activationHelper.activationState(deviceKey);
        if (activationState) {
            return true;
        } else {
            int result = activationHelper.activation(deviceKey);
            return result == ActivationHelper.ACTC_OK;
        }
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
     * @return ローカルレジストレーションの処理結果ダイアログ
     */
    public static CustomDialog getRegistResultDialog(final Context context, final boolean isSuccess, final DlnaUtils.ExcuteLocalRegistrationErrorType errorType) {
        CustomDialog resultDialog = new CustomDialog(context, CustomDialog.DialogType.ERROR);
        resultDialog.setOnTouchOutside(false);
        resultDialog.setCancelable(false);
        resultDialog.setOnTouchBackkey(false);
        if (isSuccess) {
            resultDialog.setContent(context.getString(R.string.common_text_regist_progress_done));
            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(context);
            String expireDate = DlnaManager.shared().GetRemoteDeviceExpireDate(dlnaDmsItem.mUdn);
            SharedPreferencesUtils.setRemoteDeviceExpireDate(context, expireDate);
        } else {
            switch (errorType) {
                case ACTIVATION:
                    resultDialog.setContent(context.getString(R.string.activation_failed_error_local_registration));
                    break;
                case DEVICE_OVER_ERROR:
                    resultDialog.setContent(context.getString(R.string.common_text_regist_over_error_setting));
                    break;
                case START_DTCP:
                case START_DIRAG:
                case UNKOWN:
                case NONE:
                default:
                    resultDialog.setContent(context.getString(R.string.common_text_regist_other_error));
                    break;
            }
            resultDialog.setConfirmText(R.string.common_text_close);
        }
        return resultDialog;
    }

}
