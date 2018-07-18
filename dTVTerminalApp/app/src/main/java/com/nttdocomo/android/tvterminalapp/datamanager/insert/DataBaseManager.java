/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelperChannel;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelperDownload;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * DataBaseManager.
 */
public class DataBaseManager {
    /** DBの開閉回数を記録. */
    private final AtomicInteger mOpenCounter = new AtomicInteger();
    /** DBの開閉回数を記録(番組情報用). */
    private final AtomicInteger mOpenCounterCh = new AtomicInteger();
    /** DBの開閉回数を記録(番組情報用). */
    private final AtomicInteger mOpenCounterDownload = new AtomicInteger();
    /** DBManager. */
    private static DataBaseManager sInstance;
    /** DBManage(番組情報用). */
    private static DataBaseManager sInstanceCh;
    /** DBManage(持ち出し用). */
    private static DataBaseManager sInstanceDownLoad;
    /** DataBaseHelper. */
    private static DataBaseHelper sDatabaseHelper;
    /** DataBaseHelper(番組情報用). */
    private static DataBaseHelperChannel sDatabaseChannelHelper;
    /** DataBaseHelper(録画リスト用). */
    private static DataBaseHelperDownload sDatabaseDownloadHelper;
    /** Database. */
    private SQLiteDatabase mDatabase;
    /** チャンネル用Database. */
    private SQLiteDatabase mChannelDatabase;
    /** ダウンロード用Database. */
    private SQLiteDatabase mDownLoadDatabase;
    /** Database Close 時の初期値. */
    private static int ATOMIC_INTEGER_DEFAULT_CLOSE = 0;
    /** Database Open 時の初期値. */
    private static int ATOMIC_INTEGER_DEFAULT_OPEN = 1;

    /**
     * 初期化処理.
     *
     * @param helper DataBaseHelper
     */
    public static synchronized void initializeInstance(final DataBaseHelper helper) {
        if (sInstance == null) {
            sInstance = new DataBaseManager();
            sDatabaseHelper = helper;
        }
    }

    /**
     * 初期化処理(番組情報用).
     *
     * @param helper DataBaseHelper
     */
    public static synchronized void initializeInstance(final DataBaseHelperChannel helper) {
        if (sInstanceCh == null) {
            sInstanceCh = new DataBaseManager();
            sDatabaseChannelHelper = helper;
        }
    }

    /**
     * 初期化処理(ダウンロードコンテンツ情報用).
     *
     * @param helper DataBaseHelper
     */
    public static synchronized void initializeInstance(final DataBaseHelperDownload helper) {
        if (sInstanceDownLoad == null) {
            sInstanceDownLoad = new DataBaseManager();
            sDatabaseDownloadHelper = helper;
        }
    }

    /**
     * DataBaseManagerを取得.
     *
     * @return DataBaseManager
     */
    public static synchronized DataBaseManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DataBaseManager.class.getSimpleName()
                    + " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    /**
     * DataBaseManagerを取得(番組情報用).
     *
     * @return DataBaseManager
     */
    public static synchronized DataBaseManager getChInstance() {
        if (sInstanceCh == null) {
            throw new IllegalStateException(DataBaseManager.class.getSimpleName()
                    + " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstanceCh;
    }

    /**
     * DataBaseManagerを取得(持ち出し情報用).
     *
     * @return DataBaseManager
     */
    public static synchronized DataBaseManager getDownloadInstance() {
        if (sInstanceDownLoad == null) {
            throw new IllegalStateException(DataBaseManager.class.getSimpleName()
                    + " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstanceDownLoad;
    }

    /**
     * Databaseを開く.
     *
     * @return Database
     */
    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = sDatabaseHelper.getWritableDatabase();
        } else if (mDatabase != null && !mDatabase.isOpen()) {
            // mOpenCounter がずれた場合はここでキャッチする
            mOpenCounter.set(ATOMIC_INTEGER_DEFAULT_OPEN);
            mDatabase = sDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    /**
     * Databaseを開く(番組情報用).
     *
     * @return Database
     */
    public synchronized SQLiteDatabase openChDatabase() {
        if (mOpenCounterCh.incrementAndGet() == 1) {
            // Opening new database
            mChannelDatabase = sDatabaseChannelHelper.getWritableDatabase();
        } else if (mChannelDatabase != null && !mChannelDatabase.isOpen()) {
            // mOpenCounterCh がずれた場合はここでキャッチする
            mOpenCounterCh.set(ATOMIC_INTEGER_DEFAULT_OPEN);
            mChannelDatabase = sDatabaseChannelHelper.getWritableDatabase();
        }
        return mChannelDatabase;
    }

    /**
     * Databaseを開く(ダウンロードコンテンツ情報用).
     *
     * @return Database
     */
    public synchronized SQLiteDatabase openDownloadDatabase() {
        if (mOpenCounterDownload.incrementAndGet() == 1) {
            // Opening new database
            mDownLoadDatabase = sDatabaseDownloadHelper.getWritableDatabase();
        } else if (mDownLoadDatabase != null && !mDownLoadDatabase.isOpen()) {
            // mOpenCounterDownload がずれた場合はここでキャッチする
            mOpenCounterDownload.set(ATOMIC_INTEGER_DEFAULT_OPEN);
            mDownLoadDatabase = sDatabaseDownloadHelper.getWritableDatabase();
        }
        return mDownLoadDatabase;
    }

    /**
     * Databaseを閉じる.
     */
    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();
        } else if (mDatabase != null && mDatabase.isOpen()) {
            // mOpenCounter がずれた場合はここでキャッチする
            mOpenCounter.set(ATOMIC_INTEGER_DEFAULT_CLOSE);
            mDatabase.close();
        }
    }

    /**
     * Databaseを閉じる(番組情報用).
     */
    public synchronized void closeChDatabase() {
        if (mOpenCounterCh.decrementAndGet() == 0) {
            // Closing database
            mChannelDatabase.close();
        } else if (mChannelDatabase != null && mChannelDatabase.isOpen()) {
            // mOpenCounterCh がずれた場合はここでキャッチする
            mOpenCounterCh.set(ATOMIC_INTEGER_DEFAULT_CLOSE);
            mChannelDatabase.close();
        }
    }

    /**
     * Databaseを閉じる(ダウンロードコンテンツ情報用).
     */
    public synchronized void closeDownloadDatabase() {
        if (mOpenCounterDownload.decrementAndGet() == 0) {
            // Closing database
            mDownLoadDatabase.close();
        } else if (mDownLoadDatabase != null && mDownLoadDatabase.isOpen()) {
            // mOpenCounterDownload がずれた場合はここでキャッチする
            mOpenCounterDownload.set(ATOMIC_INTEGER_DEFAULT_CLOSE);
            mDownLoadDatabase.close();
        }
    }

    /**
     * 情報を削除する.
     */
    public static void clearChInfo() {
        sInstanceCh = null;
        sDatabaseChannelHelper = null;
    }

    /**
     * 情報を削除する(ダウンロードコンテンツ情報用).
     */
    public static void clearDownloadInfo() {
        sInstanceDownLoad = null;
        sDatabaseDownloadHelper = null;
    }
}