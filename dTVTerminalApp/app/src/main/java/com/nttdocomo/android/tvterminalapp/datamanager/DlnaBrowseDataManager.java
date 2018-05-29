/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.DlnaBrowseListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DataBaseManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaObject;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DlnaBrowseDataManager {
    /**
     * コンテキスト.
     */
    private final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
    public DlnaBrowseDataManager(final Context context) {
        mContext = context;
    }
    /**
     * チャンネル一覧の情報をDBに格納する.
     * @param channelList  チャンネルリスト情報
     * @param containerId  containerId
     */
    public void insertChannelInsertList(final DlnaObject[] channelList, String containerId) {

        DTVTLogger.start();

        //取得データが空の場合は更新しないで、有効期限をクリアする
        if (channelList == null || channelList.length <= 0) {
            DateUtils.clearLastProgramDate(mContext, DateUtils.DLNA_BROWSE_UPDATE + containerId);
            return;
        }

        try {
            //各種オブジェクト作成
            DataBaseHelper channelListDataBaseHelper = new DataBaseHelper(mContext);
            DataBaseManager.initializeInstance(channelListDataBaseHelper);
            SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
            database.acquireReference();
            DlnaBrowseListDao browseListDao = new DlnaBrowseListDao(database);

            //DB保存前に前回取得したデータは全消去する
            browseListDao.deleteByContainerId(containerId);

            for (int i = 0; i < channelList.length; i++) {
                //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
                ContentValues values = new ContentValues();
                values.put(DataBaseConstants.DLNA_BROWSE_COLUM_NO, i);
                values.put(DataBaseConstants.DLNA_BROWSE_COLUM_BITRATE, channelList[i].mBitrate);
                values.put(DataBaseConstants.DLNA_BROWSE_COLUM_CHANNEL_NAME, channelList[i].mChannelName);
                values.put(DataBaseConstants.DLNA_BROWSE_COLUM_DURATION, channelList[i].mDuration);
                values.put(DataBaseConstants.DLNA_BROWSE_COLUM_RESOLUTION, channelList[i].mResolution);
                values.put(DataBaseConstants.DLNA_BROWSE_COLUM_RES_URL, channelList[i].mResUrl);
                values.put(DataBaseConstants.DLNA_BROWSE_COLUM_SIZE, channelList[i].mSize);
                values.put(DataBaseConstants.DLNA_BROWSE_COLUM_VIDEO_TYPE, channelList[i].mVideoType);
                values.put(DataBaseConstants.DLNA_BROWSE_COLUM_CONTAINER_ID, containerId);
                browseListDao.insert(values);
            }

            //データ保存日時を格納
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastDate(DateUtils.DLNA_BROWSE_UPDATE + containerId);

        } catch (SQLiteException e) {
            DTVTLogger.debug("ChannelInsertDataManager::insertChannelInsertList, e.cause=" + e.getCause());
        } finally {
            DataBaseManager.getInstance().closeDatabase();
        }
        DTVTLogger.end();
    }

    /**
     * CH一覧データを返却する.
     *
     * @param
     * @return list チャンネルデータ
     */
    public List<Map<String, String>> selectChannelListProgramData(String containerId) {

        List<Map<String, String>> list = new ArrayList<>();

        String[] columns = {DataBaseConstants.DLNA_BROWSE_COLUM_CHANNEL_NAME,
                DataBaseConstants.DLNA_BROWSE_COLUM_BITRATE,
                DataBaseConstants.DLNA_BROWSE_COLUM_DURATION,
                DataBaseConstants.DLNA_BROWSE_COLUM_RESOLUTION,
                DataBaseConstants.DLNA_BROWSE_COLUM_RES_URL,
                DataBaseConstants.DLNA_BROWSE_COLUM_SIZE,
                DataBaseConstants.DLNA_BROWSE_COLUM_VIDEO_TYPE
        };

        try {
            //Daoクラス使用準備
            DataBaseHelper channelListDataBaseHelper = new DataBaseHelper(mContext);
            DataBaseManager.initializeInstance(channelListDataBaseHelper);
            SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
            database.acquireReference();

            //データ存在チェック
            if (!DataBaseUtils.isCachingRecord(database, DataBaseConstants.DLNA_BROWSE_TABLE_NAME)) {
                DataBaseManager.getInstance().closeDatabase();
                return list;
            }

            DlnaBrowseListDao browseListDao = new DlnaBrowseListDao(database);
            list = browseListDao.findByContainerId(columns, containerId);

        } catch (SQLiteException e) {
            DTVTLogger.debug("ProgramDataManager::selectChannelListProgramData, e.cause=" + e.getCause());
        } finally {
            DataBaseManager.getInstance().closeDatabase();
        }
        return list;
    }

}
