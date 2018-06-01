/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.digion.dixim.android.activation.helper.ActivationHelper;
import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;

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
    private static final int IMAGE_QUALITY_DEFAULT = 0;
    /** 最高画質.*/
    private static final int IMAGE_QUALITY_HIGH = 1;
    /** 高画質.*/
    private static final int IMAGE_QUALITY_MIDDLE = 2;
    /** 標準画質.*/
    private static final int IMAGE_QUALITY_LOW = 3;
    /** デフォルト.*/
    private static final String IMAGE_QUALITY_DEFAULT_URL = "0/smartphone/";
    /** 最高画質.*/
    private static final String IMAGE_QUALITY_HIGH_URL = "0/remote1/";
    /** 高画質.*/
    private static final String IMAGE_QUALITY_MIDDLE_URL = "0/remote2/";
    /** 標準画質.*/
    private static final String IMAGE_QUALITY_LOW_URL = "0/remote3/";
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
    /** ダイアログ表示フラグ1.*/
    private static final int REGISTER_EXPIREDATE_DIALOG_FLG_ONE = 1;
    /** ダイアログ表示フラグ2.*/
    private static final int REGISTER_EXPIREDATE_DIALOG_FLG_TWO = 2;
    /** ダイアログ表示フラグ3.*/
    private static final int REGISTER_EXPIREDATE_DIALOG_FLG_THREE = 3;
    /** ダイアログ表示フラグ4.*/
    private static final int REGISTER_EXPIREDATE_DIALOG_FLG_FOUR = 4;
    /*ローカルレジストレーション期限表示*/

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
     * ローカルレジストレーション成功且つ期間のチェック.
     *
     * @param context コンテキスト
     * @return true:成功且つ期間内 false:成功or期間外
     */
    public static boolean getLocalRegisterSuccess(final Context context) {
        boolean isSuccess = false;
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(context);
        if (dlnaDmsItem != null) {
            String expireDate =  DlnaManager.shared().GetRemoteDeviceExpireDate(dlnaDmsItem.mUdn);
            if (!TextUtils.isEmpty(expireDate)) { //TODO 期間内の判定追加必要（3か月）
                isSuccess = true;
            }
        }
        return isSuccess;
    }

    /**
     * ローカルレジストレーション期限表示ダイアログメッセージ.
     *
     * @param context コンテキスト
     * @return msg 期限表示ダイアログメッセージ
     */
    public static String getLocalRegisterExpireDateCheckMessage(final Context context) {
        String msg = "";
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(context);
        int dialogFlg = SharedPreferencesUtils.getRegisterExpiredateDialogFlg(context);
        if (dlnaDmsItem != null) {
            String expireDate = DlnaManager.shared().GetRemoteDeviceExpireDate(dlnaDmsItem.mUdn);
            if (!TextUtils.isEmpty(expireDate)) {
                int remainingDays = DateUtils.getRemainingDays(expireDate);
                String[] strings = {context.getString(R.string.remote_remaining_days_message_begin),
                        Integer.toString(remainingDays),
                        context.getString(R.string.remote_remaining_days_message_end)};
                if (REMAINING_SEVEN_DAYS < remainingDays
                        && remainingDays <= REMAINING_FOURTEEN_DAYS
                        && dialogFlg != REGISTER_EXPIREDATE_DIALOG_FLG_ONE) {
                    msg = StringUtils.getConnectString(strings);
                    SharedPreferencesUtils.setRegisterExpiredateDialogFlg(context, REGISTER_EXPIREDATE_DIALOG_FLG_ONE);
                } else if (REMAINING_ONE_DAYS < remainingDays
                        && remainingDays <= REMAINING_SEVEN_DAYS
                        && dialogFlg != REGISTER_EXPIREDATE_DIALOG_FLG_TWO) {
                    msg = StringUtils.getConnectString(strings);
                    SharedPreferencesUtils.setRegisterExpiredateDialogFlg(context, REGISTER_EXPIREDATE_DIALOG_FLG_TWO);
                } else if (remainingDays == REMAINING_ONE_DAYS
                        && dialogFlg != REGISTER_EXPIREDATE_DIALOG_FLG_THREE) {
                    msg = StringUtils.getConnectString(strings);
                    SharedPreferencesUtils.setRegisterExpiredateDialogFlg(context, REGISTER_EXPIREDATE_DIALOG_FLG_THREE);
                } else if (remainingDays < REMAINING_ZERO_DAYS
                        && dialogFlg != REGISTER_EXPIREDATE_DIALOG_FLG_FOUR) {
                    msg = context.getString(R.string.remote_expired_message);
                    SharedPreferencesUtils.setRegisterExpiredateDialogFlg(context, REGISTER_EXPIREDATE_DIALOG_FLG_FOUR);
                }
            }
        }
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
        boolean isRemote = StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.HOME_OUT;
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
     * 設定した画質を取得.
     *
     * @param context コンテキスト
     * @return 設定した画質
     */
    // TODO コンテンツブラウズのマージが終わったら消します
    public static int getImageQualitySetting(final Context context) {
        boolean isRemote = false;
        if (context == null) {
            return IMAGE_QUALITY_DEFAULT;
        }
        String imageQualitySetting = SharedPreferencesUtils.getSharedPreferencesImageQuality(context);
        if (!isRemote || TextUtils.isEmpty(imageQualitySetting)) {
            return IMAGE_QUALITY_DEFAULT;
        } else {
            if (imageQualitySetting.equals(context.getString(R.string.main_setting_image_quality_high))) {
                return IMAGE_QUALITY_HIGH;
            } else if (imageQualitySetting.equals(context.getString(R.string.main_setting_image_quality_middle))) {
                return IMAGE_QUALITY_MIDDLE;
            } else if (imageQualitySetting.equals(context.getString(R.string.main_setting_image_quality_low))) {
                return IMAGE_QUALITY_LOW;
            }
        }
        return IMAGE_QUALITY_DEFAULT;
    }
}
