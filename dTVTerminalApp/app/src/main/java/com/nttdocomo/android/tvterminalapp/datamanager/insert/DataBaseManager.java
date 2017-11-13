/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class DataBaseManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DataBaseManager instance;
    private static DBHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(DBHelper helper) {
        if (instance == null) {
            instance = new DataBaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DataBaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DataBaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();

        }
    }}
