/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelperChannel;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DataBaseManager;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * チャンネル一覧、番組表一覧用DataManager.
 */
public class ProgramDataManager {

    /**
     * コンテキスト.
     */
    private final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param mContext コンテスト
     */
    public ProgramDataManager(final Context mContext) {
        this.mContext = mContext;
    }

    /**
     * CH一覧データを返却する.
     *
     * @param service チャンネルメタのservice.ひかり or dch(空文字の場合は両方)
     * @return list チャンネルデータ
     */
    public List<Map<String, String>> selectChannelListProgramData(final String service) {

        //データ存在チェック
        List<Map<String, String>> list = new ArrayList<>();
        if (!DBUtils.isCachingRecord(mContext, DBConstants.CHANNEL_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_CHNO, JsonConstants.META_RESPONSE_DEFAULT_THUMB, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_AVAIL_END_DATE, JsonConstants.META_RESPONSE_DISP_TYPE,
                JsonConstants.META_RESPONSE_SERVICE, JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_DTV_TYPE,
                JsonConstants.META_RESPONSE_CH_TYPE, JsonConstants.META_RESPONSE_PUID, JsonConstants.META_RESPONSE_SUB_PUID,
                JsonConstants.META_RESPONSE_CHPACK + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_PUID,
                JsonConstants.META_RESPONSE_CHPACK + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_SUB_PUID,
                JsonConstants.META_RESPONSE_CID};

        //Daoクラス使用準備
        DBHelper channelListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(channelListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        ChannelListDao channelListDao = new ChannelListDao(database);

        //ホーム画面用データ取得
        list = channelListDao.findByService(columns, service);
        DataBaseManager.getInstance().closeDatabase();
        database.close();
        return list;
    }

    /**
     * CH毎番組表データを返却する.
     *
     * @param chNos チャンネル番号
     * @param chInfoDate 取得要求日付
     * @return list 番組データ
     */
    public List<List<Map<String, String>>> selectTvScheduleListProgramData(
            final List<String> chNos, final String chInfoDate) {
        DTVTLogger.start();
        String chInfoGetDate = StringUtils.getChDateInfo(chInfoDate);

        List<List<Map<String, String>>> lists = new ArrayList<>();
        for (String chNo : chNos) {
            //データ存在チェック
            //ファイルのタイムスタンプより取得日時を取得済みのため、既に要求日付のフォルダは作成してある.
            String filesDir = mContext.getFilesDir().getPath();
            String databasePath = StringUtils.getConnectStrings(filesDir, "/../databases");
            String dbFilePath = StringUtils.getConnectStrings(databasePath, "/channel/", chInfoGetDate, "/", chNo);
            File dbFile = new File(dbFilePath);
            if (!dbFile.isFile()) {
                //対象の日付フォルダに対象のチャンネル番号DBが存在しない.
                continue;
            }

            //androidではdatabaseフォルダのファイルのみ取り扱えるため、DBをdatabaseフォルダ(2階層上)へコピーする.
            File databaseFile = new File(databasePath, chNo);
            try {
                if (!databaseFile.createNewFile()) {
                    DTVTLogger.error("Failed to create copy file");
                    continue;
                }
                FileChannel inCh = new FileInputStream(dbFile).getChannel();
                FileChannel outCh = new FileOutputStream(databaseFile).getChannel();
                inCh.transferTo(0, inCh.size(), outCh);
            } catch (IOException e) {
                DTVTLogger.error(e.toString());
            }

            //テーブルの存在チェック.
            List<Map<String, String>> list;
            if (!DBUtils.isChCachingRecord(mContext, DBConstants.TV_SCHEDULE_LIST_TABLE_NAME, chNo)) {
                //databaseフォルダにコピーしたファイルを削除
                if (!databaseFile.delete()) {
                    DTVTLogger.error("Failed to delete copy DB file");
                }
                continue;
            }

            //ホーム画面に必要な列を列挙する
            String[] columns = {JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                    JsonConstants.META_RESPONSE_PUBLISH_START_DATE, JsonConstants.META_RESPONSE_PUBLISH_END_DATE,
                    JsonConstants.META_RESPONSE_CHNO, JsonConstants.META_RESPONSE_DISP_TYPE,
                    JsonConstants.META_RESPONSE_SEARCH_OK, JsonConstants.META_RESPONSE_CRID,
                    JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_EVENT_ID,
                    JsonConstants.META_RESPONSE_TITLE_ID, JsonConstants.META_RESPONSE_R_VALUE,
                    JsonConstants.META_RESPONSE_CONTENT_TYPE, JsonConstants.META_RESPONSE_DTV,
                    JsonConstants.META_RESPONSE_TV_SERVICE, JsonConstants.META_RESPONSE_DTV_TYPE,
                    JsonConstants.META_RESPONSE_EPITITLE, JsonConstants.META_RESPONSE_CID};

            //Daoクラス使用準備
            DBHelperChannel channelListDBHelper = new DBHelperChannel(mContext, chNo);
            DataBaseManager.clearChInfo();
            DataBaseManager.initializeInstance(channelListDBHelper);
            SQLiteDatabase database = DataBaseManager.getChInstance().openChDatabase();
            TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(database);

            //ホーム画面用データ取得
            list = tvScheduleListDao.findByTypeAndDate(columns);
            database.close();
            DataBaseManager.getChInstance().closeChDatabase();
            lists.add(list);

            //databaseフォルダにコピーしたファイルを削除
            if (!databaseFile.delete()) {
                DTVTLogger.error("Failed to delete copy DB file");
            }
        }
        return lists;
    }
}
