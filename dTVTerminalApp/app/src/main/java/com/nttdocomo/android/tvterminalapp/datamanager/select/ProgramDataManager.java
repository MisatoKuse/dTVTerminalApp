/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelperChannel;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DataBaseManager;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
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

    /** コンテキスト. */
    private final Context mContext;

    /** チャンネルメタ service値（マイチャンネル）. */
    public static final String CH_SERVICE_MY_CHANNEL = "MY_CHANNEL";
    /** チャンネルメタ service値（ひかり）. */
    public static final String CH_SERVICE_HIKARI = "1";
    /** チャンネルメタ service値（dCH）. */
    public static final String CH_SERVICE_DCH = "2";
    /** チャンネルメタ service値（地テジ）. */
    public static final String CH_SERVICE_TTB = "3";
    /** チャンネルメタ service値（BS）. */
    public static final String CH_SERVICE_BS = "4";

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
     * @param service チャンネルメタのservice.ひかり or dch or 全て
     * @return list チャンネルデータ
     */
    public List<Map<String, String>> selectChannelListProgramData(final int service) {

        List<Map<String, String>> list = new ArrayList<>();

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_CHNO, JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_AVAIL_END_DATE, JsonConstants.META_RESPONSE_DISP_TYPE,
                JsonConstants.META_RESPONSE_SERVICE, JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_DTV_TYPE,
                JsonConstants.META_RESPONSE_CH_TYPE, JsonConstants.META_RESPONSE_PUID, JsonConstants.META_RESPONSE_SUB_PUID,
                JsonConstants.META_RESPONSE_CHPACK + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_PUID,
                JsonConstants.META_RESPONSE_CHPACK + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_SUB_PUID,
                JsonConstants.META_RESPONSE_CID, JsonConstants.META_RESPONSE_ADULT, JsonConstants.META_RESPONSE_SERVICE_ID_UNIQ};

        //Daoクラス使用準備
        DataBaseHelper channelListDataBaseHelper = new DataBaseHelper(mContext);
        DataBaseManager.initializeInstance(channelListDataBaseHelper);
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        synchronized (dataBaseManager) {
            try {
                SQLiteDatabase database = dataBaseManager.openDatabase();
                database.acquireReference();

                //データ存在チェック
                if (!DataBaseUtils.isCachingRecord(database, DataBaseConstants.CHANNEL_LIST_TABLE_NAME)) {
                    DataBaseManager.getInstance().closeDatabase();
                    return list;
                }

                ChannelListDao channelListDao = new ChannelListDao(database);

                switch (service) {
                    case JsonConstants.CH_SERVICE_TYPE_INDEX_MY_CHANNEL:
                        // マイチャンネル
                        list = channelListDao.findByService(columns, CH_SERVICE_MY_CHANNEL);
                        break;
                    case JsonConstants.CH_SERVICE_TYPE_INDEX_ALL:
                        // ひかり・DTV
                        list = channelListDao.findById(columns);
                        break;
                    case JsonConstants.CH_SERVICE_TYPE_INDEX_HIKARI:
                        // ひかりのみ
                        list = channelListDao.findByService(columns, CH_SERVICE_HIKARI);
                        break;
                    case JsonConstants.CH_SERVICE_TYPE_INDEX_DCH:
                        // DCHのみ
                        list = channelListDao.findByService(columns, CH_SERVICE_DCH);
                        break;
                    case JsonConstants.CH_SERVICE_TYPE_INDEX_TTB:
                        // 地テジのみ
                        list = channelListDao.findByService(columns, CH_SERVICE_TTB);
                        break;
                    case JsonConstants.CH_SERVICE_TYPE_INDEX_BS:
                        // BSのみ
                        list = channelListDao.findByService(columns, CH_SERVICE_BS);
                        break;
                    default:
                        DTVTLogger.error("CH_SERVICE_TYPE is incorrect!");
                        return null;
                }
            } catch (SQLiteException e) {
                DTVTLogger.debug("ProgramDataManager::selectChannelListProgramData, e.cause=" + e.getCause());
            } finally {
                DataBaseManager.getInstance().closeDatabase();
            }
        }
        return list;
    }

    /**
     * CH毎番組表データを返却する.
     *
     * @param serviceIdUniqs サービスユニーク
     * @param chInfoDate 取得要求日付
     * @return list 番組データ
     */
    @SuppressWarnings("OverlyLongMethod")
    public synchronized List<List<Map<String, String>>> selectTvScheduleListProgramData(
            final List<String> serviceIdUniqs, final String chInfoDate) {
        DTVTLogger.start();
        String chInfoGetDate = StringUtils.getChDateInfo(chInfoDate);

        List<List<Map<String, String>>> lists;
        lists = new ArrayList<>();
        for (String serviceIdUniq : serviceIdUniqs) {
            //データ存在チェック
            //ファイルのタイムスタンプより取得日時を取得済みのため、既に要求日付のフォルダは作成してある.
            String filesDir = mContext.getFilesDir().getPath();
            String databasePath = StringUtils.getConnectStrings(filesDir, "/../databases");
            String dbFilePath = StringUtils.getConnectStrings(databasePath, "/channel/", chInfoGetDate, "/", serviceIdUniq);
            File dbFile = new File(dbFilePath);
            if (!dbFile.isFile()) {
                //対象の日付フォルダに対象のチャンネル番号DBが存在しない.
                continue;
            }

            //androidではdatabaseフォルダのファイルのみ取り扱えるため、DBをdatabaseフォルダ(2階層上)へコピーする.
            File databaseFile = new File(databasePath, serviceIdUniq);
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
            if (!DataBaseUtils.isChCachingRecord(mContext, DataBaseConstants.TV_SCHEDULE_LIST_TABLE_NAME, serviceIdUniq)) {
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
                    JsonConstants.META_RESPONSE_SYNOP, JsonConstants.META_RESPONSE_CID,
                    JsonConstants.META_RESPONSE_THUMB_640, JsonConstants.META_RESPONSE_SERVICE_ID_UNIQ};


            //Daoクラス使用準備
            DataBaseHelperChannel channelListDBHelper = new DataBaseHelperChannel(mContext, serviceIdUniq);
            DataBaseManager.clearChInfo();
            DataBaseManager.initializeInstance(channelListDBHelper);
            DataBaseManager databaseManager = DataBaseManager.getChInstance();
            synchronized (databaseManager) {
                try {
                    SQLiteDatabase database = databaseManager.openChDatabase();
                    database.acquireReference();
                    TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(database);

                    //ホーム画面用データ取得
                    list = tvScheduleListDao.findByTypeAndDate(columns);
                    lists.add(list);

                    //databaseフォルダにコピーしたファイルを削除
                    if (!databaseFile.delete()) {
                        DTVTLogger.error("Failed to delete copy DB file");
                    }
                } catch (SQLiteException e) {
                    DTVTLogger.debug("ProgramDataManager::selectTvScheduleListProgramData, e.cause=" + e.getCause());
                } finally {
                    DataBaseManager.getChInstance().closeChDatabase();
                }
            }
        }
        return lists;
    }
}
