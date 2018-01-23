/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        return "drm";
    }

    public static final String getDestConfPathDir(Context context){
        if(null==context){
            return null;
        }
        return context.getFilesDir().getAbsolutePath() + File.separator + sConFileDirName;
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
        //String newPath = getConfPathDir(context);

        if (TextUtils.isEmpty(assetsConfPath) || TextUtils.isEmpty(destPath)) {
            return false;
        }
        String separator = File.separator;
        boolean r=false;
        String[] fileNames=null;
        try {
            fileNames = context.getResources().getAssets().list(assetsConfPath);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        if (fileNames.length > 0) {
            File file = new File(destPath);
            if(!file.exists()) {
                if (!file.mkdirs()) {
                    return false;
                }
            }
            for (String fileName : fileNames) {
                copyConfFiles(context, assetsConfPath + File.separator + fileName, destPath+ File.separator + fileName, forceUpdate);
            }
////                File targetFile = new File(destPath);
////                if (!targetFile.exists() && !targetFile.mkdirs()) {
////                    return false;
////                }
//                for (String fileName : fileNames) {
//                    String nextSrcFileName=assetsConfPath + separator + fileName;
//                    String nextDestFileName=destPath + separator + fileName;
//                    File src=new File(fileName);
//                    File targetFile = new File(nextDestFileName);
//                    if(src.isDirectory()){
//                        r=targetFile.mkdirs();
//                        if(!r){
//                            return false;
//                        }
//                    } else {
//                        if(targetFile.exists() && !forceUpdate && targetFile.length()>0){
//                            return true;
//                        }
//                        r = copyConfFiles(context, nextSrcFileName, nextDestFileName, forceUpdate);
//                        if(!r){
//                            return false;
//                        }
//                    }
//                }
        } else {
            String fileName=getFileName(assetsConfPath);
            String nextDestFileName=destPath + separator + fileName;
            r=copyConfFile(context, assetsConfPath, nextDestFileName);
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

//    private static boolean isAssetsDir(Context context, String path){
//        if(null==context){
//            return false;
//        }
//        String[] fileNames = context.getResources().getAssets().list(path);
//    }

    private static final String sConFileDirName="drm";

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
        boolean r=dest.getParentFile().mkdirs();
        if(!r){
            return false;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(context.getAssets().open(oldPath));
            filterSpecial(in, sWhatToReplace, conDir);
            out = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] buffer = new byte[sBufSize];
            int length = 0;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        } catch (Exception e) {
            DTVTLogger.debug(e);
            ret=false;
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                DTVTLogger.debug(e);
                ret=false;
            }
        }
        return ret;
    }

    private static void filterSpecial(InputStream is, final String whatToReplace, final String replace){
        String temp = null;
        try {
            temp = InputStreamToString(is, "UTF-8");
            temp = temp.replace(whatToReplace, replace);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            is = StringToInputStream(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

//    public static void copy(Context context, String zPath, String targetPath) {
//        if (TextUtils.isEmpty(zPath) || TextUtils.isEmpty(targetPath)) {
//            return;
//        }
//        final int BUFFER_SIZE= 1024;
//        File dest = new File(targetPath);
//        boolean r=dest.getParentFile().mkdirs();
//        if(!r){
//            return;
//        }
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//            in = new BufferedInputStream(context.getAssets().open(zPath));
//            out = new BufferedOutputStream(new FileOutputStream(dest));
//            byte[] buffer = new byte[BUFFER_SIZE];
//            int length = 0;
//            while ((length = in.read(buffer)) != -1) {
//                out.write(buffer, 0, length);
//            }
//        } catch (Exception e) {
//            DTVTLogger.debug(e);;
//        } finally {
//            try {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    DTVTLogger.debug(e);;
//                }
//                in.close();
//            } catch (IOException e) {
//                DTVTLogger.debug(e);;
//            }
//        }
//    }

    /**
     * 機能：デフォールト構造
     */
    public DlnaInterfaceRI() {
    }

    /**
     * 機能：Dlna機能を開始。「stopDtcpDl」とPairで使用しなければならない
     *
     * @return boolean
     */
    public DlnaDownloadRet startDtcpRI(String pathToSave){
        if (0 == mNativeDlna) {
            mNativeDlna = nativeCreateDlnaRmObject();
        }
        if(0==mNativeDlna){
            return DlnaDownloadRet.DownloadRet_ParamError;
        }

//        String homeDtcpPath = pathToSave;  //e.g. EnvironmentUtil.getPrivateDataHome(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
//        String homePlayerPath = NewEnvironmentUtil.getPrivateDataHome(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.PLAYER);
//        String db_post = homePlayerPath + "/" + "db_post";
//
//        File f=new File(db_post);
//        boolean has=f.exists();
//        if(!has){
//            DTVTLogger.end();
//            return DlnaDownloadRet.DownloadRet_Unactivated;   //unactivated
//        }
//
//        File homePlayerPathDir=new File(homePlayerPath);
//        File[] allFile=homePlayerPathDir.listFiles();
//        for(File file:allFile){
//            DTVTLogger.debug("player dir dtcp before copy db_post, ---------------->" + file.getName());
//        }
//
//        File homeDtcpPathDir=new File(homeDtcpPath);
//        allFile=homeDtcpPathDir.listFiles();
//        for(File file:allFile){
//            DTVTLogger.debug("dtcp before copy db_post, ---------------->"+file.getName());
//        }
//
//        String homeParent= getParentDir(homeDtcpPath);
//        int ret= NewEnvironmentUtil.copyDeviceKeyFromOtherCMWork(mContext, homeParent, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
//        if(1!=ret && 3!=ret){
//            DTVTLogger.end();
//            return DlnaDownloadRet.DownloadRet_CopyKeyFileFailed;
//        }
//
////        allFile=homeDtcpPathDir.listFiles();
////        for(File file:allFile){
////            DTVTLogger.debug("dtcp after copy db_post, ---------------->"+ file.getName());
////        }
//
//        long id=Thread.currentThread().getId();
//        DTVTLogger.debug("HandlerThread:"+id);

        boolean r= nativeStartDlnaRm(mNativeDlna, pathToSave);
        if(!r){
            return DlnaDownloadRet.DownloadRet_OtherError;
        }

        return DlnaDownloadRet.DownloadRet_Succeed;
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
//     * 機能：端末idを取得
//     * @return String unique id
//     */
//    public String getUniqueId() {
//        if(null==mContext){
//            return "";
//        }
//        return EnvironmentUtil.getCalculatedUniqueId(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
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
//
//    /**
//     * 機能：団ロード進捗のコールバック
//     * @param content size downloaded
//     */
//    private void dlProgress(String content) {
//        DTVTLogger.start();
//        if(null==mDlnaDlListener){
//            DTVTLogger.end();
//            return;
//        }
//        String size=content;
//        int sizeI=0;
//        try{
//            sizeI=Integer.parseInt(size);
//        }catch (Exception e){
//            DTVTLogger.debug(e.getMessage());
//            DTVTLogger.end();
//            return;
//        }
//        mDlnaDlListener.dlProgress(sizeI);
//        DTVTLogger.end();
//    }
//
//    /**
//     * 機能：団ロードステータスのコールバック
//     * @param content
//     */
//    private void dlStatus(String content) {
//        DTVTLogger.start();
//        if(null==mDlnaDlListener){
//            return;
//        }
//        String status=content;
//        DlnaDlStatus statusEnum;
//        try{
//            int i=Integer.parseInt(status);
//            statusEnum = DlnaDlStatus.values()[i];;
//        }catch (Exception e){
//            DTVTLogger.debug(e.getMessage());
//            DTVTLogger.end();
//            return;
//        }
//        mDlnaDlListener.dlStatus(statusEnum);
//        DTVTLogger.end();
//    }
//
//    /**
//     * 機能：download
//     *
//     * @return DownloadRet_Succeed: 成功　
//     *          DownloadRet_Unactivated：unactivated
//     *          DownloadRet_CopyKeyFileFailed: copy key file error
//     *          DownloadRet_OtherError: other error
//     */
//    public DlnaDownloadRet download(final DtcpDownloadParam param/*, final String xml*/) {
//        DTVTLogger.start();
//
//        if(null==mContext){
//            DTVTLogger.end();
//            return DlnaDownloadRet.DownloadRet_OtherError;
//        }
//
//        if(null==param || !param.isParamValid()){
//            return DlnaDownloadRet.DownloadRet_ParamError;
//        }
////        mHandler.post(new Runnable() {
////            @Override
////            public void run() {
////                long id=Thread.currentThread().getId();
////                DTVTLogger.debug("HandlerThread:"+id);
////                download(mNativeDlna, param.getSavePath(), param.getSaveFileName(), param.getDtcp1host(), param.getDtcp1port(), param.getUrl(), param.getCleartextSize(), xml);
////            }
////        });
//        download(mNativeDlna, param.getSaveFileName(), param.getDtcp1host(), param.getDtcp1port(), param.getUrl(), param.getCleartextSize(), param.getXmlToDl());
//        DTVTLogger.end();
//        return DlnaDownloadRet.DownloadRet_Succeed;
//    }
//
//    private String getParentDir(String dir){
//        File f=new File(dir);
//        if(!f.exists()){
//            return "";
//        }
//        return f.getParent();
//    }
//
//    public void downloadCancel(){
//        downloadCancel(mNativeDlna);
//    }
//
//    /**
//     * 機能：jni関数
//     * @param prt prt
//     * @param fileNameToSave fileNameToSave
//     * @param dtcp1host dtcp1host
//     * @param dtcp1port dtcp1port
//     * @param url url
//     * @param cleartextSize cleartextSize
//     */
//    private native void download(long prt, String fileNameToSave, String dtcp1host, int dtcp1port, String url, int cleartextSize, String itemId);

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
//
//    /**
//     * Download Cancel
//     * @param prt prt
//     */
//    private native void downloadCancel(long prt);
//
//    public static native boolean initGlobalDl(String saveDir);
//
//    public static native void uninitGlobalDl();
}