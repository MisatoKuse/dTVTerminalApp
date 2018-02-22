/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.DownLoadListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.service.download.DlData;

import java.util.List;
import java.util.Map;

/**
 * DownLoadListDataManager.
 */
public class DownLoadListDataManager {
    /**
     * コンテキスト.
     */
    private final Context mContext;
    /**
     * ダウンロードOK.
     */
    private final static String DOWNLOAD_OK = "OK";

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
    public DownLoadListDataManager(final Context context) {
        mContext = context;
    }

    /**
     * 持ち出しのダウンロード情報をDBに格納する.
     * @param dlData ダウンロード情報
     */
    public void insertDownload(final DlData dlData) {

        //各種オブジェクト作成
        DBHelper downLoadListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(downLoadListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        DownLoadListDao downloadListDao = new DownLoadListDao(database);

        //DB保存前に前回取得したデータは全消去する
        downloadListDao.deleteByItemId(dlData.getItemId());

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        ContentValues values = new ContentValues();
        values.put(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID, dlData.getItemId());
        values.put(DBConstants.DOWNLOAD_LIST_COLUM_URL, dlData.getUrl());
        values.put(DBConstants.DOWNLOAD_LIST_COLUM_TITLE, dlData.getTitle());
        values.put(DBConstants.DOWNLOAD_LIST_COLUM_SIZE, dlData.getTotalSize());
        values.put(DBConstants.DOWNLOAD_LIST_COLUM_DURATION, dlData.getDuration());
        values.put(DBConstants.DOWNLOAD_LIST_COLUM_RESOLUTION, dlData.getResolution());
        values.put(DBConstants.DOWNLOAD_LIST_COLUM_BITRATE, dlData.getBitrate());
        values.put(DBConstants.DOWNLOAD_LIST_COLUM_UPNP_ICON, dlData.getUpnpIcon());
        values.put(DBConstants.DOWNLOAD_LIST_COLUM_SAVE_URL, dlData.getSaveFile());
        values.put(DBConstants.DOWNLOAD_LIST_COLUM_TYPE, dlData.getVideoType());
        downloadListDao.insert(values);
        DataBaseManager.getInstance().closeDatabase();
    }

    /**
     * 持ち出しのダウンロード情報をDBから削除する.
     *
     */
    public void deleteDownloadAllContents() {

        //各種オブジェクト作成
        DBHelper downLoadListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(downLoadListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        DownLoadListDao downloadListDao = new DownLoadListDao(database);

        downloadListDao.delete();
        DataBaseManager.getInstance().closeDatabase();
    }

    /**
     * 持ち出しのダウンロード情報をDBに格納する.
     * @param itemId  アイテムID.
     */
    public void deleteDownloadContentByItemId(final String itemId) {

        //各種オブジェクト作成
        DBHelper downLoadListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(downLoadListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        DownLoadListDao downloadListDao = new DownLoadListDao(database);

        downloadListDao.deleteByItemId(itemId);
        DataBaseManager.getInstance().closeDatabase();
    }

    /**
     * 持ち出しのダウンロード情報をDBに更新する.
     * @param itemId アイテムID.
     */
    public void updateDownloadByItemId(final String itemId) {

        //各種オブジェクト作成
        DBHelper downLoadListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(downLoadListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        DownLoadListDao downloadListDao = new DownLoadListDao(database);

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        ContentValues values = new ContentValues();
        values.put(DBConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS, DOWNLOAD_OK);
        downloadListDao.updateByItemId(values, itemId);
        DataBaseManager.getInstance().closeDatabase();
    }

    /**
     * 録画一覧データを返却する.
     *
     * @return list ダウンロードしたデータ状態.
     */
    public List<Map<String, String>> selectDownLoadListVideoData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {DBConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS, DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID,
        DBConstants.DOWNLOAD_LIST_COLUM_SAVE_URL, DBConstants.DOWNLOAD_LIST_COLUM_TITLE};

        //Daoクラス使用準備
        DBHelper downLoadListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(downLoadListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        DownLoadListDao downLoadListDao = new DownLoadListDao(database);

        //持ち出し画面用データ取得
        List<Map<String, String>> list = downLoadListDao.findDownLoadList(columns);
        DataBaseManager.getInstance().closeDatabase();
        return list;
    }

    /**
     * 録画一覧データを返却する.
     *
     * @return list ダウンロードしたデータ状態.
     */
    public List<Map<String, String>> selectDownLoadList() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID, DBConstants.DOWNLOAD_LIST_COLUM_URL,
                DBConstants.DOWNLOAD_LIST_COLUM_SAVE_DIDL, DBConstants.DOWNLOAD_LIST_COLUM_SAVE_HOST,
                DBConstants.DOWNLOAD_LIST_COLUM_SAVE_PORT, DBConstants.DOWNLOAD_LIST_COLUM_SAVE_URL,
                DBConstants.DOWNLOAD_LIST_COLUM_TYPE, DBConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_SIZE,
                DBConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS, DBConstants.DOWNLOAD_LIST_COLUM_SIZE,
                DBConstants.DOWNLOAD_LIST_COLUM_DURATION, DBConstants.DOWNLOAD_LIST_COLUM_RESOLUTION,
                DBConstants.DOWNLOAD_LIST_COLUM_UPNP_ICON, DBConstants.DOWNLOAD_LIST_COLUM_BITRATE,
                DBConstants.DOWNLOAD_LIST_COLUM_IS_SUPPORTED_BYTE_SEEK, DBConstants.DOWNLOAD_LIST_COLUM_IS_SUPPORTED_TIME_SEEK,
                DBConstants.DOWNLOAD_LIST_COLUM_IS_AVAILABLE_CONNECTION_STALLING, DBConstants.DOWNLOAD_LIST_COLUM_IS_LIVE_MODE,
                DBConstants.DOWNLOAD_LIST_COLUM_IS_REMOTE, DBConstants.DOWNLOAD_LIST_COLUM_TITLE,
                DBConstants.DOWNLOAD_LIST_COLUM_CONTENTFORMAT};

        //Daoクラス使用準備
        DBHelper downLoadListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(downLoadListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        DownLoadListDao downLoadListDao = new DownLoadListDao(database);

        //持ち出し画面用データ取得
        List<Map<String, String>> list = downLoadListDao.findAllDownloadList(columns);
        DataBaseManager.getInstance().closeDatabase();
        return list;
    }

    /**
     * @param itemId 項目ID.
     * @return list ダウンロードしたデータ状態.
     */
    public List<Map<String, String>> selectDownLoadByItemId(final String itemId) {

        //Daoクラス使用準備
        DBHelper downLoadListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(downLoadListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        DownLoadListDao downLoadListDao = new DownLoadListDao(database);

        //持ち出し画面用データ取得
        List<Map<String, String>> list = downLoadListDao.findByItemId(itemId);
        DataBaseManager.getInstance().closeDatabase();
        return list;
    }
}