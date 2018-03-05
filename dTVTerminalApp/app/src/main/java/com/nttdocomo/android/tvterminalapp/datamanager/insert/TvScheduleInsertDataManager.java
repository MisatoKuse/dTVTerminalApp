/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelperChannel;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 番組情報保存用DataManager.
 */
public class TvScheduleInsertDataManager {

    /**
     * コンテキスト.
     */
    private Context mContext = null;

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
    public TvScheduleInsertDataManager(final Context context) {
        mContext = context;
    }

    /**
     * TvScheduleAPIの解析結果をDBに格納する(Now On Air用).
     *
     * @param tvScheduleList チャンネル詳細情報
     */
    public void insertTvScheduleInsertList(final TvScheduleList tvScheduleList) {

        //取得データが空の場合は更新しないで、有効期限をクリアする
        if (tvScheduleList == null || tvScheduleList.geTvsList() == null
                || tvScheduleList.geTvsList().size() < 1 || tvScheduleList.geTvsList().get(0).isEmpty()) {
            DateUtils.clearLastProgramDate(mContext, DateUtils.TV_SCHEDULE_LAST_INSERT);
            return;
        }

        try {
            //各種オブジェクト作成
            DBHelper channelListDBHelper = new DBHelper(mContext);
            DataBaseManager.initializeInstance(channelListDBHelper);
            SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
            database.acquireReference();
            TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(database);
            @SuppressWarnings("unchecked")
            List<Map<String, String>> hashMaps = tvScheduleList.geTvsList();

            //DB保存前に前回取得したデータは全消去する
            tvScheduleListDao.delete();

            //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
            for (int i = 0; i < hashMaps.size(); i++) {
                Iterator entries = hashMaps.get(i).entrySet().iterator();
                ContentValues values = new ContentValues();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    String keyName = (String) entry.getKey();
                    String valName = (String) entry.getValue();
                    if (JsonConstants.META_RESPONSE_AVAIL_START_DATE.equals(keyName)) {
                        values.put(DBConstants.UPDATE_DATE, !TextUtils.isEmpty(valName) ? valName.substring(0, 10) : "");
                    }
                    values.put(DBUtils.fourKFlgConversion(keyName), valName);
                }
                tvScheduleListDao.insert(values);
            }
            //データ保存日時を格納
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastDate(DateUtils.TV_SCHEDULE_LAST_INSERT);
        } catch (SQLiteException e) {
            DTVTLogger.debug("TvScheduleInsertDataManager::insertTvScheduleInsertList, e.cause=" + e.getCause());
        } finally {
            DataBaseManager.getInstance().closeDatabase();
        }
    }

    /**
     * TvScheduleAPIの解析結果をDBに格納する(番組表用).
     *
     * @param channelInfoList チャンネル詳細情報
     * @param chInfoDate      チャンネル情報取得日付
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public synchronized void insertTvScheduleInsertList(final ChannelInfoList channelInfoList, final String chInfoDate) {
        DTVTLogger.start();
        if (channelInfoList.getChannels().size() == 0) {
            //保存するチャンネル情報が無いため保存せず戻る
            return;
        }
        String chInfoGetDate = StringUtils.getChDateInfo(chInfoDate);

        //チャンネル情報取得日のDBフォルダが作成されているか確認する
        String filesDir = mContext.getFilesDir().getAbsolutePath();
        String dbChannelDir = StringUtils.getConnectStrings(filesDir, "/../databases/channel");
        String dbDir = StringUtils.getConnectStrings(dbChannelDir, "/" + chInfoGetDate);
        File fileDir = new File(dbDir);
        if (!fileDir.isDirectory()) {
            //取得日のフォルダが存在しないためフォルダを作成する.
            DTVTLogger.debug("Folder not exist, make folder.");
            try {
                File channelDir = new File(dbChannelDir);
                boolean isChannelDir = channelDir.mkdir();
                boolean isCreateDir = fileDir.mkdir();
                if (!isChannelDir || !isCreateDir) {
                    //フォルダの作成に失敗
                    DTVTLogger.error("Failed to create cache directory");
                }
            } catch (SecurityException | NullPointerException e) {
                DTVTLogger.error(e.toString());
            }
        }

        try {
            List<ChannelInfo> channelInformation = channelInfoList.getChannels();
            synchronized (channelInformation) {
                for (ChannelInfo channelInfo : channelInformation) {
                    //DB名としてチャンネル番号を取得.
                    String chNo = String.valueOf(channelInfo.getChNo());

                    //各種オブジェクト作成
                    DBHelperChannel tvScheduleListDBHelper = new DBHelperChannel(mContext, chNo);
                    DataBaseManager.clearChInfo();
                    DataBaseManager.initializeInstance(tvScheduleListDBHelper);
                    SQLiteDatabase database = DataBaseManager.getChInstance().openChDatabase();
                    database.acquireReference();
                    TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(database);

                    //ContentValuesに変換してDBに保存する.
                    ArrayList<ScheduleInfo> scheduleInformation = channelInfo.getSchedules();
                    for (ScheduleInfo scheduleInfo : scheduleInformation) {
                        ContentValues values = convertScheduleInfoToContentValues(scheduleInfo);
                        tvScheduleListDao.insert(values);
                    }

                    //保存したDBを所定の場所へ移動する( HOME/database/channel/yyyyMMdd/ )
                    String channelFilePath = StringUtils.getConnectStrings(filesDir, "/../databases/", chNo);
                    File channelFile = new File(channelFilePath);
                    File movedFile = new File(StringUtils.getConnectStrings(dbDir, "/", chNo));
                    if (channelFile.isFile()) {
                        try {
                            if (!channelFile.renameTo(movedFile)) {
                                DTVTLogger.error("Failed to move DB file");
                                //移動に失敗したため元ファイルを削除する
                                if (!channelFile.delete()) {
                                    DTVTLogger.error("Failed to remove DB file");
                                }
                            }
                        } catch (SecurityException | NullPointerException e) {
                            DTVTLogger.error(e.toString());
                        }
                    }

                    //journalファイルも同じ場所へ移動させる.
                    String journalFilePath = StringUtils.getConnectStrings(filesDir, "/../databases/", chNo, "-journal");
                    File journalFile = new File(journalFilePath);
                    File movedJournalFile = new File(StringUtils.getConnectStrings(dbDir, "/", chNo, "-journal"));
                    if (journalFile.isFile()) {
                        try {
                            if (!journalFile.renameTo(movedJournalFile)) {
                                DTVTLogger.error("Failed to move journal file");
                                //移動に失敗したため元ファイルを削除する
                                if (!journalFile.delete()) {
                                    DTVTLogger.error("Failed to remove journal file");
                                }
                            }
                        } catch (SecurityException | NullPointerException e) {
                            DTVTLogger.error(e.toString());
                        }
                    }
                }
            }
        } catch (SQLiteException e) {
            DTVTLogger.debug("ProgramDataManager::insertTvScheduleInsertList, e.cause=" + e.getCause());
        } finally {
            DataBaseManager.getInstance().closeDatabase();
        }

        //古いDBファイルを削除するため、日付フォルダ一覧を取得
        File[] files = new File(dbChannelDir).listFiles();
        //前後一週間を超えるDBフォルダは無条件で削除する.
        if (files != null) {
            for (File folder : files) {
                if (folder.isDirectory()) {
                    Date folderDate;
                    try {
                        String folderName = StringUtils.getConnectStrings(folder.getName(), "000000");
                        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DD_HH_MM_SS, Locale.JAPAN);
                        folderDate = sdf.parse(folderName);
                    } catch (ParseException e) {
                        DTVTLogger.error(e.toString());
                        continue;
                    }
                    Calendar folderCalendar = Calendar.getInstance();
                    folderCalendar.setTime(folderDate);

                    Date nowDate = new Date();
                    Calendar afterEightDay = Calendar.getInstance();
                    Calendar beforeEightDay = Calendar.getInstance();
                    afterEightDay.setTime(nowDate);
                    beforeEightDay.setTime(nowDate);
                    afterEightDay.add(Calendar.DAY_OF_MONTH, 8);
                    beforeEightDay.add(Calendar.DAY_OF_MONTH, -8);

                    if (folderCalendar.compareTo(afterEightDay) > 0
                            || folderCalendar.compareTo(beforeEightDay) < 0) {
                        //キャッシュ期限範囲外の日付のフォルダなので削除する.
                        recursiveDeleteFile(folder);
                    }
                }
            }
        }
    }

    /**
     * フォルダを中身ごと削除する.
     *
     * @param file 削除対象フォルダ
     */
    private static void recursiveDeleteFile(final File file) {
        // 存在しない場合は処理終了
        if (!file.exists()) {
            return;
        }
        // 対象がディレクトリの場合は再帰処理
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    recursiveDeleteFile(child);
                }
            }
        }
        // 対象がファイルもしくは配下が空のディレクトリの場合は削除する
        if (!file.delete()) {
            DTVTLogger.error("Failed to delete folder or file");
        }
    }

    /**
     * ContentValuesに変換する.
     * //TODO パラメータ要確認
     *
     * @param scheduleInfo ScheduleInfo
     * @return ContentValues
     */
    private ContentValues convertScheduleInfoToContentValues(final ScheduleInfo scheduleInfo) {
        ContentValues values = new ContentValues();
        values.put(JsonConstants.META_RESPONSE_THUMB_448, scheduleInfo.getImageUrl());
        values.put(JsonConstants.META_RESPONSE_THUMB_640, scheduleInfo.getImageDetailUrl());
        values.put(JsonConstants.META_RESPONSE_TITLE, scheduleInfo.getTitle());
        values.put(JsonConstants.META_RESPONSE_PUBLISH_START_DATE, scheduleInfo.getStartTime());
        values.put(JsonConstants.META_RESPONSE_PUBLISH_END_DATE, scheduleInfo.getEndTime());
        values.put(JsonConstants.META_RESPONSE_CHNO, scheduleInfo.getChNo());
        values.put(JsonConstants.META_RESPONSE_DISP_TYPE, scheduleInfo.getDispType());
        values.put(JsonConstants.META_RESPONSE_SEARCH_OK, scheduleInfo.getSearchOk());
        values.put(JsonConstants.META_RESPONSE_CRID, "");
        values.put(JsonConstants.META_RESPONSE_SERVICE_ID, "");
        values.put(JsonConstants.META_RESPONSE_EVENT_ID, "");
        values.put(JsonConstants.META_RESPONSE_TITLE_ID, "");
        values.put(JsonConstants.META_RESPONSE_R_VALUE, scheduleInfo.getRValue());
        values.put(JsonConstants.META_RESPONSE_CONTENT_TYPE, scheduleInfo.getContentType());
        values.put(JsonConstants.META_RESPONSE_DTV, scheduleInfo.getDtv());
        values.put(JsonConstants.META_RESPONSE_TV_SERVICE, "");
        values.put(JsonConstants.META_RESPONSE_DTV_TYPE, scheduleInfo.getDtvType());
        values.put(JsonConstants.META_RESPONSE_EPITITLE, "");
        values.put(JsonConstants.META_RESPONSE_CID, "");
        return values;
    }
}