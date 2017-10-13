package com.nttdocomo.android.tvterminalapp.DataProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
public class ThumbnailCacheManager {

    // メモリサイズ 5MB
    private static final int MEM_CACHE_DEFAULT_SIZE = 5 * 1024 * 1024;
    // メモリキャッシュ
    private LruCache<String, Bitmap> memCache;
    // ディスクキャッシュ件数
    private final static int limit = 100;

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
    public Bitmap getBitmapFromDiskByStream(String fileName) {
        try {
            String dir = context.getCacheDir() + "/" + hashKeyForDisk(fileName);
            FileInputStream fs = new FileInputStream(dir);
            BufferedInputStream bs = new BufferedInputStream(fs);
            Bitmap btp = BitmapFactory.decodeStream(bs);
            bs.close();
            fs.close();
            return btp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ディスクからBitmap取得
     *
     * @param fileName
     */
    public Bitmap getBitmapFromDisk(String fileName) {
        try {
            String dir = context.getCacheDir() + "/" + hashKeyForDisk(fileName);
            File file = new File(dir);
            Bitmap btp = null;
            if (file.exists()) {
                btp = BitmapFactory.decodeFile(dir);
            }
            return btp;
        } catch (Exception e) {
            e.printStackTrace();
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
        String localPath = context.getCacheDir() + "/";
        int quality = 100;
        if (bitmap == null || filename == null) {
            return false;
        }
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        OutputStream stream = null;
        try {
            File[] files = context.getCacheDir().listFiles();
            int count = files.length;
            List<File> mListFile = new ArrayList<>();
            boolean isLimitFlg = false;
            for (int i = 0; i < count; i++) {
                if (count >= limit) {
                    isLimitFlg = true;
                }
                mListFile.add(files[i]);
            }
            Collections.sort(mListFile, new CalendarComparator());
            if (isLimitFlg) {
                mListFile.get(0).delete();
            }
            filename = hashKeyForDisk(filename);
            stream = new FileOutputStream(localPath + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap.compress(format, quality, stream);
    }

    /**
     *ソートクラス
     *
     */
    class CalendarComparator implements Comparator {
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
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * Byte to String
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
}
