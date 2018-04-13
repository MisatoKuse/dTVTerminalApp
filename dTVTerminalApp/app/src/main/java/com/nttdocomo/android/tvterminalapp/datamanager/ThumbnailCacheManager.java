/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * サムネイルキャッシュ管理.
 */
public class ThumbnailCacheManager {

    /**
     * コンテクスト.
     */
    private Context mContext = null;

    /**
     * メモリキャッシュ.
     */
    private LruCache<String, Bitmap> mMemCache = null;

    /**
     * ディスクキャッシュ件数.
     */
    private final static int THUMBNAIL_FILE_CACHE_LIMIT = 100;

    /**
     * サムネイルキャッシュ保存するフォルダ.
     */
    private static final String THUMBNAIL_CACHE = "/thumbnail_cache/";

    /**
     * コンストラクタ.
     *
     * @param context コンテクスト
     */
    public ThumbnailCacheManager(final Context context) {
        this.mContext = context;
    }

    /**
     * メモリキャッシュ初期化.
     */
    public void initMemCache() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        //メモリキャッシュサイズ
        mMemCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(final String key, final Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    /**
     * ディスクからBitmap取得.
     *
     * @param fileName ファイル名
     * @return bitmap ディスクから取得画像
     */
    public Bitmap getBitmapFromDisk(final String fileName) {
        //ファイルパス
        String dir = mContext.getCacheDir() + THUMBNAIL_CACHE + String.valueOf(fileName.hashCode());
        File file = new File(dir);
        Bitmap bitmap = null;
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(dir);
        }
        return bitmap;
    }

    /**
     * メモリから取得.
     *
     * @param url url
     * @return bitmap メモリから取得画像
     */
    public Bitmap getBitmapFromMem(final String url) {
        return mMemCache.get(url);
    }

    /**
     * メモリにプッシュする.
     *
     * @param url url
     * @param bitmap 画像
     */
    public void putBitmapToMem(final String url, final Bitmap bitmap) {
        mMemCache.put(url, bitmap);
    }

    /**
     * メモリにプッシュする.
     *
     * @param fileName ファイル名
     * @param bitmap 画像
     * @return result 保存成功、失敗の結果
     */
    public boolean saveBitmapToDisk(final String fileName, final Bitmap bitmap) {
        //フォルダーパス
        String localPath = mContext.getCacheDir() + THUMBNAIL_CACHE;
        int quality = 100;
        if (bitmap == null || fileName == null) {
            return false;
        }
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        OutputStream stream = null;
        try {
            File myFile = new File(mContext.getCacheDir() + THUMBNAIL_CACHE);
            if (!myFile.exists()) {
                if (!myFile.mkdir()) {
                    DTVTLogger.debug("create file fail ");
                }
            }
            File[] files = myFile.listFiles();
            List<File> mListFile = new ArrayList<>();
            boolean isLimitFlg = false;
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (files.length >= THUMBNAIL_FILE_CACHE_LIMIT) {
                        isLimitFlg = true;
                    }
                    mListFile.add(file);
                }
            }
            if (isLimitFlg) {
                //ファイル更新時間通り昇順ソート
                Collections.sort(mListFile, new CalendarComparator());
                //古い情報を削除する
                if (mListFile.get(0) != null && mListFile.get(0).exists()) {
                    if (!mListFile.get(0).delete()) {
                        DTVTLogger.debug("old file delete failed ");
                    }
                }
            }
            //ファイル名を暗号化する
            stream = new FileOutputStream(localPath + String.valueOf(fileName.hashCode()));
        } catch (FileNotFoundException e) {
            DTVTLogger.debug(e);
        }
        return bitmap.compress(format, quality, stream);
    }

    /**
     * ソート処理.
     */
   private static class CalendarComparator implements Comparator<Object>, Serializable {

        private static final long serialVersionUID = -1L;

        @Override
        public int compare(final Object object1, final Object object2) {
            File p1 = (File) object1;
            File p2 = (File) object2;
            return new Date(p1.lastModified()).compareTo(new Date(p2.lastModified()));
        }
    }

    /**
     * サムネイルファイルの削除.
     *
     * @param context コンテキスト
     */
    public static void clearThumbnailCache(final Context context) {
        //サムネイルフォルダの名前を取得
        File folder = new File(context.getCacheDir() + THUMBNAIL_CACHE);

        //フォルダではないなら帰る
        if (!folder.isDirectory()) {
            return;
        }

        //ファイル配列の取得
        File[] files = folder.listFiles();

        //配列に1件も存在しなければ帰る
        if (files == null || files.length <= 0) {
            return;
        }

        //ファイルの数だけ回る
        for (File nowFile : files) {
            try {
                //ファイルが存在していた場合は消す
                if (nowFile != null && nowFile.isFile()) {
                    if (!nowFile.delete()) {
                        DTVTLogger.debug("delete file fail ");
                    }
                }
            } catch (SecurityException e) {
                //削除が行えなくても、特に対策は無いので次へ進む
                DTVTLogger.debug(e);
            }
        }
    }
}