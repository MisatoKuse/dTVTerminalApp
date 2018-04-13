/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.remote;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaInterface;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * 機能：For Dlna Remote.
 */
public class DlnaInterfaceRI {
    private static final String sFileEncoce = "UTF-8";
    /** コンテクスト.*/
    private Context mContext;
    private long mNativeDlna = 0;
    private static final int sBufSize = 1024;
    private static final String sWhatToReplace = "[CONFDIR]";
    private static final String sConFileDirName = "drm/conf";
    private static final String sRadaRelayDestPost = "/dirag/rada/rada_relay";
    /** コピー元（assets）.*/
    private static final String DRM_CONF_PRIVATE = "drm/conf/private_data_home";

    /*
     * 機能：libをロードする
     */
    static {
        System.loadLibrary("dtvtlib");
    }

    /**
     * get Drm configure dir.
     *
     * @return dir
     */
    private static String getSrcConPathDir() {
        return sConFileDirName;
    }

    /**
     * get Drm configure dir.
     *
     * @param context context
     * @return dir
     */
    private static String getDestConfPathDir(final Context context) {
        return EnvironmentUtil.getPrivateDataHome(context,
                EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER);
    }

    public static long getFileSize(final String path) {
        long size = 0;
        File file = new File(path);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        }
        return size;
    }

    public static void listAllConfFilesCopied(String path) {
        FileListAll fs = new FileListAll();
        File file = new File(path);
        HashMap<String, String> hm = fs.getList(file);
    }

    /**
     * copy conf files to app
     *
     * @param context        context
     * @param assetsConfPath assetsConfPath
     * @param destPath       destPath
     * @param forceUpdate    「true」の場合、毎回assetsからコピー
     * @return conf path:    Conf path is not null, error return null
     */
    public static boolean copyConfFiles(Context context, String assetsConfPath, String destPath, boolean forceUpdate) {
        if (null == context || TextUtils.isEmpty(assetsConfPath) || TextUtils.isEmpty(destPath)) {
            return false;
        }
        boolean r = true;
        String[] fileNames = null;
        AssetManager am = context.getResources().getAssets();
        try {
            fileNames = am.list(assetsConfPath);
        } catch (IOException e) {
            DTVTLogger.debug(e);
        }
        if (fileNames != null && fileNames.length > 0) {
            File file = new File(destPath);

            FileListAll fs = new FileListAll();
            HashMap<String, String> hm = fs.getList(file);

            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return false;
                }
            }
            for (String fileName : fileNames) {
                r = copyConfFiles(context, assetsConfPath + File.separator + fileName, destPath + File.separator + fileName, forceUpdate);
                if (!r) {
                    return false;
                }
            }
        } else {
            File file = new File(destPath);
            boolean doesExists = file.exists();
            if (!doesExists || forceUpdate) {
                r = copyConfFile(context, assetsConfPath, destPath);
            }
        }

        return r;
    }

    private static String getFileName(String path) {
        if (null == path) {
            return path;
        }
        int p = path.lastIndexOf(File.separator);
        if (p < 0) {
            return path;
        }
        return path.substring(p, path.length());
    }

    /**
     * Copy one file
     *
     * @param context context
     * @param oldPath oldPath
     * @param newPath newPath
     * @return true: ok    false: ng
     */
    private static boolean copyConfFile(final Context context, final String oldPath, final String newPath) {
        if (TextUtils.isEmpty(oldPath) || TextUtils.isEmpty(newPath)) {
            return false;
        }
        boolean ret = true;
        String conDir = getDestConfPathDir(context);
        if (null == conDir) {
            return false;
        }
        File dest = new File(newPath);
        File parent = dest.getParentFile();
        if (null != parent && !parent.exists()) {
            boolean r = parent.mkdirs();
            if (!r) {
                return false;
            }
        }
        InputStream in = null;
        OutputStream out = null;
        InputStream in2 = null;
        try {
            in = new BufferedInputStream(context.getAssets().open(oldPath));
            in2 = filterSpecial(in, sWhatToReplace, conDir);
            out = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] buffer = new byte[sBufSize];
            int length = 0;
            while ((length = in2.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            DTVTLogger.debug(e);
            ret = false;
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
                if (null != in2) {
                    in2.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
                ret = false;
            }
        }
        return ret;
    }

    private static InputStream filterSpecial(final InputStream is, final String whatToReplace, final String replace) {
        String temp = null;
        InputStream ret = null;
        try {
            temp = InputStreamToString(is, sFileEncoce);
            temp = temp.replace(whatToReplace, replace);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(temp != null) {
            try {
                ret = StringToInputStream(temp);
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        }
        return ret;
    }

    private static InputStream StringToInputStream(final String in) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes(sFileEncoce));
        return is;
    }

    private static String InputStreamToString(final InputStream in, final String encoding) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[sBufSize];
        int count = -1;
        while ((count = in.read(data, 0, sBufSize)) != -1) {
            outStream.write(data, 0, count);
        }
        data = null;
        return new String(outStream.toByteArray(), sFileEncoce);
    }

    private static String printFile(final Context context, final String fullPath) {
        InputStream in = null;
        String ret = null;
        try {
            File file = new File(fullPath);
            in = new FileInputStream(file);
            ret = InputStreamToString(in, sFileEncoce);
        } catch (Exception e) {
            DTVTLogger.debug(e);
            ret = null;
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
                ret = null;
            }
        }
        return ret;
    }

    /**
     * 機能：デフォールト構造.
     */
    public DlnaInterfaceRI() {
    }

    /**
     * 機能：端末idを取得.
     *
     * @return String unique id
     */
    public String getUniqueId() {
        if (null == mContext) {
            return "";
        }
        return EnvironmentUtil.getCalculatedUniqueId(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
    }

    /**
     * 機能：Dlna機能を開始.
     * @param context コンテクスト
     * @return DlnaRemoteRet
     */
    public DlnaRemoteRet startDlnaRI(final Context context) {
        if (null == context) {
            return DlnaRemoteRet.DlnaRemoteRet_ParamError;
        }
        this.mContext = context;

        String assetsDir = getSrcConPathDir();
        String destDir = getDestConfPathDir(context);
        if (null == assetsDir || assetsDir.isEmpty() || null == destDir || destDir.isEmpty()) {
            return DlnaRemoteRet.DlnaRemoteRet_CopyDrmConfFile;
        }
        copyFileFromAssets(context, DRM_CONF_PRIVATE, destDir);
        boolean ret = copyConfFiles(context, assetsDir, destDir, true);
        if (!ret) {
            return DlnaRemoteRet.DlnaRemoteRet_CopyDrmConfFile;
        }

        if (!makeRedaRelayDir(destDir)) {
            return DlnaRemoteRet.DlnaRemoteRet_CopyDrmConfFile;
        }
        if (0 == mNativeDlna) {
            mNativeDlna = nativeCreateDlnaRmObject();
        }
        if (0 == mNativeDlna) {
            return DlnaRemoteRet.DlnaRemoteRet_Init;
        }
        boolean r = nativeStartDlnaRm(mNativeDlna, destDir);
        if (!r) {
            return DlnaRemoteRet.DlnaRemoteRet_Init;
        }

        return DlnaRemoteRet.DlnaRemoteRet_Succeed;
    }

    private String getUdn() {
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(mContext);
        if (dlnaDmsItem != null) {
            return dlnaDmsItem.mUdn;
        } else {
            return null;
        }
    }

    public void regist() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nativeStartDlnaRmRegist(mNativeDlna, getUdn());
            }
        }, 3000);
    }

    public void stop() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nativeStartDlnaRmStop(mNativeDlna);
            }
        }, 5000);
    }

    public void connect() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nativeStartDlnaRmConnect(mNativeDlna, getUdn());
            }
        }, 25000);
    }

    private static void copyFileFromAssets(final Context context, final String oldPath, final String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);
            // フォルダーの判断
            if (fileNames.length > 0) {
                File file = new File(newPath);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        DTVTLogger.debug("mkdir failed");
                    }
                }
                for (String fileName : fileNames) {
                    copyFileFromAssets(context, oldPath + File.separator + fileName, newPath + File.separator + fileName);
                }
            } else {
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    // buffer
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean makeRedaRelayDir(final String destDir) {
        if (null == destDir || destDir.isEmpty()) {
            return false;
        }
        String radaRelayDest = destDir + sRadaRelayDestPost;
        File radaRelayDestFile = new File(radaRelayDest);
        if (!radaRelayDestFile.exists()) {
            return radaRelayDestFile.mkdirs();
        }
        return true;
    }

    /**
     * 機能：Dlna機能を停止.
     */
    public void stopDtcpDl() {
        nativeStartDlnaRmStop(mNativeDlna);
    }

    /**
     * 機能：jni関数.
     *
     * @return c++ dlna object
     */
    private native long nativeCreateDlnaRmObject();

    /**
     * 機能：jni c/c++からの通知を処理.
     * @param msg メッセージID
     * @param content content
     */
    public void notifyFromNative(int msg, String content) {
        switch (msg) {
            case DlnaInterface.DLNA_MSG_ID_RM_STATUS:
//                dlProgress(content);
                break;
            default:
                break;
        }
    }
//
//
//    /**
//     * todo: WiFiは切ると、対応する
//     */
//    public void toDoWithWiFiLost(){
//
//    }

//
//    /**
//     * 機能：Dlna download listenerを設定
//     * @param lis lis
//     */
//    public void setDlnaDlListener(DlnaDlListener lis, Context context){
//        mDlnaDlListener=lis;
//        mContext= context;
//    }


    /**
     * 機能：jni関数.
     *
     * @return 操作結果
     */
    private native boolean nativeStartDlnaRm(long prt, String confPath);

    private native boolean nativeStartDlnaRmRegist(long prt, String udn);

    private native boolean nativeStartDlnaRmConnect(long prt, String udn);

    private native boolean nativeStartDlnaRmStop(long prt);

//    /**
//     * 機能：jni関数
//     */
//    private native void nativeStopDlna(long prt);
//
////    /**
////     * 機能：jni関数
////     * @return 操作結果
////     */
////    private native boolean nativeStop(long prt);


}