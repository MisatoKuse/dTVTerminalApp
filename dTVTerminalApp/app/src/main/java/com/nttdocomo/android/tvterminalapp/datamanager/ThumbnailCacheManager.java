/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.BitmapDecodeUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.ThumbnailDownloadTask;

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
     * メモリキャッシュ算出時の比率.
     */
    private final static int THUMBNAIL_MEMORY_CACHE_RATE = 8;
    /**
     * メモリキャッシュ最小値.
     */
    private final static int THUMBNAIL_MEMORY_CACHE_MINIMUM_SIZE = 1000;
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
        this.mContext = context.getApplicationContext();
    }

    /**
     * メモリキャッシュ初期化.
     */
    public synchronized void initMemCache() {
        //現在の空き容量の8分の1をキャッシュに割り当てる
        long freeMemory = Runtime.getRuntime().freeMemory();
        int cacheSize = (int) (freeMemory / THUMBNAIL_MEMORY_CACHE_RATE);

        //メモリの断片化が進み、まとまったメモリが確保できずにゼロだった場合の確認
        if (cacheSize <= 0) {
            //ガベージコレクションを試す（2回行うのはAndroidのソースでも行われている正当な方法です）
            System.gc();
            System.gc();

            //再度メモリ量を計算する
            freeMemory = Runtime.getRuntime().freeMemory();
            cacheSize = (int) (freeMemory / THUMBNAIL_MEMORY_CACHE_RATE);
        }

        //それでもゼロだった場合、LruCacheはゼロバイトを認めないので、若干数を割り当てる。
        if (cacheSize <= 0) {
            cacheSize = THUMBNAIL_MEMORY_CACHE_MINIMUM_SIZE;
        }

        //メモリ使用量の最大サイズを指定してキャッシュを作成する
        mMemCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(final String key, final Bitmap bitmap) {
                return bitmap.getByteCount();
            }

            @Override
            protected void entryRemoved(final boolean evicted, final String key, Bitmap oldValue, Bitmap newValue) {
                synchronized (ThumbnailCacheManager.this) {
                    //キャッシュから古いデータが排除される場合の処理
                    if (oldValue != null && !oldValue.isRecycled()) {
                        oldValue.recycle();
                        oldValue = null;
                        System.gc();
                    }
                }
            }
        };
    }

    /**
     * ディスクからBitmap取得.
     *
     * @param fileName ファイル名
     * @param mImageSizeType 画像タイプ
     * @return bitmap ディスクから取得画像
     */
    public Bitmap getBitmapFromDisk(final String fileName, final ThumbnailDownloadTask.ImageSizeType mImageSizeType) {
        //コンテキストがヌルの場合は帰る
        if (mContext == null) {
            return null;
        }

        //ファイルパス
        String dir = mContext.getCacheDir() + THUMBNAIL_CACHE + String.valueOf(fileName.hashCode());
        File file = new File(dir);
        Bitmap bitmap = null;
        if (file.exists()) {
            bitmap = BitmapDecodeUtils.compressBitmap(mContext, dir, mImageSizeType);
        }
        return bitmap;
    }

    /**
     * メモリから取得.
     *
     * @param url url
     * @return bitmap メモリから取得画像
     */
    public synchronized Bitmap getBitmapFromMem(final String url) {
        if (mMemCache != null) {
            Bitmap bitmap = mMemCache.get(url);
            if (bitmap != null && !bitmap.isRecycled()) {
                return bitmap.copy(Bitmap.Config.ARGB_8888, true);
            }
        }
        return null;
    }

    /**
     * メモリにプッシュする.
     *
     * @param url url
     * @param bitmap 画像
     */
    public synchronized void putBitmapToMem(final String url, final Bitmap bitmap) {
        if (url != null && bitmap != null && mMemCache != null) {
            //格納先と格納情報がそろっていた場合に蓄積を行う
            if (mMemCache.get(url) == null) {
                Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                mMemCache.put(url, copy);
            }
        }
    }

    /**
     * メモリにプッシュする.
     *
     * @param fileName ファイル名
     * @param bitmap 画像
     * @return result 保存成功、失敗の結果
     */
    public boolean saveBitmapToDisk(final String fileName, final Bitmap bitmap) {
        //コンテキストがヌルの場合は取得失敗で帰る
        if (mContext == null) {
            return false;
        }

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

    /**
     * メモリキャッシュを解放する.
     */
    public synchronized void removeMemCache() {
        DTVTLogger.start();
        //本来のキャッシュのクリア
        if (mMemCache != null) {
            mMemCache.evictAll();
        }
        System.gc();
        DTVTLogger.end();
    }

    /**
     * メモリキャッシュを解放する.
     */
    public synchronized void removeAll() {
        DTVTLogger.start();

        //本来のキャッシュのクリア
        if (mMemCache != null) {
            mMemCache.evictAll();
            mMemCache = null;
        }

        mContext = null;

        System.gc();
        System.gc();

        DTVTLogger.end();
    }
}