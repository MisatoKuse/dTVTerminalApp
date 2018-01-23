/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.remote;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

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

/**
 * 機能：For Dlna Remote
 */
public class DlnaInterfaceRI {
    //private Handler mHandler= new Handler();

    //Download
//    private DlnaDlListener mDlnaDlListener;
//    private Context mContext;
    private long mNativeDlna = 0;
    private static final int sBufSize=1024;
    private static final String sWhatToReplace="[CONFDIR]";

    /*
     * 機能：libをロードする
     */
    static {
        System.loadLibrary("dtvtlibdrm");
    }

    /**
     * get Drm configure dir
     * @param context context
     * @return dir
     */
    public static String getSrcConPathDir(Context context) {
        //return "file:///android_asset/drm/conf";
        return sConFileDirName;
    }

    public static final String getDestConfPathDir(Context context){
        if(null==context){
            return null;
        }
        return context.getFilesDir().getAbsolutePath() + File.separator + sConFileDirName;
    }

    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        }
    }

    public static long getFileSize(String path) {
        long size = 0;
        File file=new File(path);
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

    public static void listAllConfFilesCopied(String path){
        FileListAll fs=new FileListAll();
        File file= new File(path);
        HashMap<String, String> hm= fs.getList(file);
    }

    /**
     * copy conf files to app
     * @param context context
     * @param assetsConfPath assetsConfPath
     * @param destPath destPath
     * @param forceUpdate 「true」の場合、毎回assetsからコピー
     * @return conf path:    Conf path is not null, error return null
     */
    public static boolean copyConfFiles(Context context, String assetsConfPath, String destPath, boolean forceUpdate){

        if (TextUtils.isEmpty(assetsConfPath) || TextUtils.isEmpty(destPath)) {
            return false;
        }
        String separator = File.separator;
        boolean r=true;
        String[] fileNames=null;
        try {
            fileNames = context.getResources().getAssets().list(assetsConfPath);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        if (fileNames.length > 0) {
            File file = new File(destPath);

            FileListAll fs = new FileListAll();
            HashMap<String, String> hm = fs.getList(file);

            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return false;
                }
            }
            for (String fileName : fileNames) {
                r=copyConfFiles(context, assetsConfPath + File.separator + fileName, destPath + File.separator + fileName, forceUpdate);
                if(!r){
                    return false;
                }
            }
        } else {
            r=copyConfFile(context, assetsConfPath, destPath);
        }

        return r;
    }

    private static String getFileName(String path) {
        if(null==path){
            return path;
        }
        int p=path.lastIndexOf(File.separator);
        if(p<0){
            return path;
        }
        return path.substring(p, path.length());
    }

    private static final String sConFileDirName="drm/conf";

    /**
     * Copy one file
     * @param context context
     * @param oldPath oldPath
     * @param newPath newPath
     * @return  true: ok    false: ng
     */
    private static boolean copyConfFile(Context context, String oldPath, String newPath){
        if (TextUtils.isEmpty(oldPath) || TextUtils.isEmpty(newPath)) {
            return false;
        }
        boolean ret=true;
        String conDir= getDestConfPathDir(context);
        if(null==conDir){
            return false;
        }
        File dest = new File(newPath);
        File parent=dest.getParentFile();
        if(null!=parent && !parent.exists()){
            boolean r=parent.mkdirs();
            if(!r){
                return false;
            }
        }
        InputStream in = null;
        OutputStream out = null;
        InputStream in2=null;
        try {
            in = new BufferedInputStream(context.getAssets().open(oldPath));
            in2= filterSpecial(in, sWhatToReplace, conDir);
            out = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] buffer = new byte[sBufSize];
            int length = 0;
            while ((length = in2.read(buffer)) >0) {
                out.write(buffer, 0, length);
            }
        } catch (Exception e) {
            DTVTLogger.debug(e);
            ret=false;
        } finally {
            try {
                if(null!=out) {
                    out.close();
                }
                if(null!=in){
                    in.close();
                }
                if(null!=in2){
                    in2.close();
                }
            } catch (IOException e) {
                DTVTLogger.debug(e);
                ret=false;
            }
        }
        return ret;
    }

    private static InputStream filterSpecial(InputStream is, final String whatToReplace, final String replace){
        String temp = null;
        InputStream ret=null;
        try {
            temp = InputStreamToString(is, "UTF-8");
            temp = temp.replace(whatToReplace, replace);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ret = StringToInputStream(temp);
        } catch (Exception e) {
            DTVTLogger.debug(e);
            ret=null;
        }
        return ret;
    }

    private static InputStream StringToInputStream(String in) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"));
        return is;
    }

    private static String InputStreamToString(InputStream in, String encoding) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[sBufSize];
        int count = -1;
        while ((count = in.read(data, 0, sBufSize)) != -1){
            outStream.write(data, 0, count);
        }
        data = null;
        return new String(outStream.toByteArray(), "UTF-8");
    }

    /**
     * 機能：デフォールト構造
     */
    public DlnaInterfaceRI() {
    }

    /**
     * 機能：Dlna機能を開始。「stopDtcpDl」とPairで使用しなければならない
     *
     * @return DlnaRemoteRet
     */
    public DlnaRemoteRet startDlnaRI(String confPath){
        if(null==confPath || confPath.isEmpty()){
            return DlnaRemoteRet.DlnaRemoteRet_ParamError;
        }
        if (0 == mNativeDlna) {
            mNativeDlna = nativeCreateDlnaRmObject();
        }
        if(0==mNativeDlna){
            return DlnaRemoteRet.DlnaRemoteRet_Init;
        }

        boolean r= nativeStartDlnaRm(mNativeDlna, confPath);
        if(!r){
            return DlnaRemoteRet.DlnaRemoteRet_Init;
        }

        return DlnaRemoteRet.DlnaRemoteRet_Succeed;
    }

//    /**
//     * 機能：Dlna機能を停止。「startDtcpRI」とPairで使用しなければならない
//     */
//    public void stopDtcpDl(){
//        nativeStopDlna(mNativeDlna);
//    }

    /**
     * 機能：jni関数
     * @return c++ dlna object
     */
    private native long nativeCreateDlnaRmObject();

//    /**
//     * 機能：jni c/c++からの通知を処理
//     * @param msg msg
//     * @param content content
//     */
//    public void notifyFromNative(int msg, String content) {
//        //DTVTLogger.debug("msg=" + msg + ", content=" + content);
//        switch (msg) {
//            case DlnaInterface.DLNA_MSG_ID_DL_PROGRESS:
//                dlProgress(content);
//                break;
//            case DlnaInterface.DLNA_MSG_ID_DL_STATUS:
//                dlStatus(content);
//                break;
//            default:
//                break;
//        }
//    }
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
     * 機能：jni関数
     * @return 操作結果
     */
    private native boolean nativeStartDlnaRm(long prt, String confPath);

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