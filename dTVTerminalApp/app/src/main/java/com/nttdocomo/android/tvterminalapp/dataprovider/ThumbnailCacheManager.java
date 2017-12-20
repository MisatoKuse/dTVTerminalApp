/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ThumbnailCacheManager {

    // メモリサイズ 5MB
    private static final int MEM_CACHE_DEFAULT_SIZE = 5 * 1024 * 1024;
    // メモリキャッシュ
    private LruCache<String, Bitmap> memCache;
    // ディスクキャッシュ件数
    private final static int THUMBNAIL_FILE_CASHE_LIMIT = 100;
    // サムネイルキャッシュ保存するフォルダ　packagename/cache/thumbnail_cache
    private static final String THUMBNAIL_CACHE  ="/thumbnail_cache/";

    private Context context;

    public ThumbnailCacheManager (){

    }

    /**
     * コンストラクタ
     *
     * @param context
     */
    public ThumbnailCacheManager (Context context){
        this.context = context;
    }

    /**
     * メモリキャッシュ初期化
     */
    public void initMemCache() {
        //メモリキャッシュサイズ
        memCache = new LruCache<String, Bitmap>(MEM_CACHE_DEFAULT_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    /**
     * ディスクからBitmap取得
     *
     * @param fileName
     */
    public Bitmap getBitmapFromDisk(String fileName) {
        try {
            //ファイルパス
            String dir = context.getCacheDir() + THUMBNAIL_CACHE + hashKeyForDisk(fileName);
            File file = new File(dir);
            Bitmap bitmap = null;
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(dir);
            }
            return bitmap;
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * メモリから取得
     *
     * @param url
     */
    public Bitmap getBitmapFromMem(String url) {
        return memCache.get(url);
    }

    /**
     * メモリにプッシュする
     *
     * @param url
     * @param bitmap
     */
    public void putBitmapToMem(String url, Bitmap bitmap) {
        memCache.put(url, bitmap);
    }

    /**
     * メモリにプッシュする
     *
     * @param filename
     * @param bitmap
     */
    public boolean saveBitmapToDisk(String filename, Bitmap bitmap) {
        //フォルダーパス
        String localPath = context.getCacheDir() + THUMBNAIL_CACHE;
        int quality = 100;
        if (bitmap == null || filename == null) {
            return false;
        }
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        OutputStream stream = null;
        try {
            File myFile = new File(context.getCacheDir() + THUMBNAIL_CACHE);
            if (!myFile.exists()){
                if(!myFile.mkdir()){
                    DTVTLogger.debug("create file fail ");
                }
            }
            File[] files = myFile.listFiles();
            List<File> mListFile = new ArrayList<>();
            boolean isLimitFlg = false;
            if (files!=null && files.length > 0){
                for (int i = 0; i < files.length; i++) {
                    if (files.length >= THUMBNAIL_FILE_CASHE_LIMIT) {
                        isLimitFlg = true;
                    }
                    mListFile.add(files[i]);
                }
            }
            if (isLimitFlg) {
                //ファイル更新時間通り昇順ソート
                Collections.sort(mListFile, new CalendarComparator());
                //古い情報を削除する
                if(mListFile.get(0)!=null && mListFile.get(0).exists()){
                    if(!mListFile.get(0).delete()){
                        DTVTLogger.debug("delete file fail ");
                    }
                }
            }
            //ファイル名を暗号化する
            filename = hashKeyForDisk(filename);
            stream = new FileOutputStream(localPath + filename);
        } catch (FileNotFoundException e) {
            DTVTLogger.debug(e);
        }
        return bitmap.compress(format, quality, stream);
    }

    /**
     *ソート処理
     *
     */
    class CalendarComparator implements Comparator {
        @Override
        public int compare(Object object1, Object object2) {
            File p1 = (File) object1;
            File p2 = (File) object2;
            return new Date(p1.lastModified()).compareTo(new Date(p2.lastModified()));
        }
    }

    /**
     * 画像名前暗号化
     *
     * @param key
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = key.getBytes("UTF-8");
            mDigest.update(bytes);
            cacheKey = bytesToHexString(mDigest.digest());
        }catch (UnsupportedEncodingException e) {
            cacheKey = String.valueOf(key.hashCode());
        }catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * byteをStringに変換
     *
     * @param bytes
     */
    public String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * サムネイルファイルの削除
     *
     * @param context コンテキスト
     */
    public static void clearThumbnailCache(Context context) {
        //サムネイルフォルダの名前を取得
        StringBuilder folderSource = new StringBuilder();
        folderSource.append(context.getCacheDir());
        folderSource.append(THUMBNAIL_CACHE);

        File folder = new File(folderSource.toString());

        //フォルダではないなら帰る
        if(!folder.isDirectory()) {
            return;
        }

        for(File nowFile : folder.listFiles()) {
            try {
                //ファイルが存在していた場合は消す
                if(nowFile != null && nowFile.isFile()) {
                    nowFile.delete();
                }
            } catch(SecurityException e) {
                //削除が行えなくても、特に対策は無いので次へ進む
            }
        }
    }
}
