/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelperChannel;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DownloadDBHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * DataBaseManager.
 */
public class DataBaseManager {
    /**
     * DBの開閉回数を記録.
     */
    private final AtomicInteger mOpenCounter = new AtomicInteger();
    /**
     * DBの開閉回数を記録(番組情報用).
     */
    private final AtomicInteger mOpenCounterCh = new AtomicInteger();
    /**
     * DBManager.
     */
    private static DataBaseManager sInstance;
    /**
     * DBManage(番組情報用).
     */
    private static DataBaseManager sInstanceCh;
    /**
     * DBHelper.
     */
    private static DBHelper sDatabaseHelper;
    /**
     * DBHelper(番組情報用).
     */
    private static DBHelperChannel sDatabaseChannelHelper;
    /**
     * DBHelper(録画リスト用).
     */
    private static DownloadDBHelper sDatabaseDownloadHelper;
    /**
     * Database.
     */
    private SQLiteDatabase mDatabase;

    /**
     * 初期化処理.
     *
     * @param helper DBHelper
     */
    public static synchronized void initializeInstance(final DBHelper helper) {
        if (sInstance == null) {
            sInstance = new DataBaseManager();
            sDatabaseHelper = helper;
        }
    }

    /**
     * 初期化処理(番組情報用).
     *
     * @param helper DBHelper
     */
    public static synchronized void initializeInstance(final DBHelperChannel helper) {
        if (sInstanceCh == null) {
            sInstanceCh = new DataBaseManager();
            sDatabaseChannelHelper = helper;
        }
    }

    /**
     * 初期化処理(ダウンロードコンテンツ情報用).
     *
     * @param helper DBHelper
     */
    public static synchronized void initializeInstance(final DownloadDBHelper helper) {
        if (sInstance == null) {
            sInstance = new DataBaseManager();
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
     * Databaseを開く.
     *
     * @return Database
     */
    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
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
            mDatabase = sDatabaseChannelHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    /**
     * Databaseを開く(ダウンロードコンテンツ情報用).
     *
     * @return Database
     */
    public synchronized SQLiteDatabase openDownloadDatabase() {
        if (mOpenCounterCh.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = sDatabaseDownloadHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    /**
     * Databaseを閉じる.
     */
    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();

        }
    }

    /**
     * Databaseを閉じる(番組情報用).
     */
    public synchronized void closeChDatabase() {
        if (mOpenCounterCh.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();
        }
    }

    /**
     * Databaseを閉じる(ダウンロードコンテンツ情報用).
     */
    public synchronized void closeDownloadDatabase() {
        if (mOpenCounterCh.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();
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
        sInstanceCh = null;
        sDatabaseDownloadHelper = null;
    }
}