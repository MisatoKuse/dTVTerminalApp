/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelperChannel;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.UPDATE_DATE;

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

        //各種オブジェクト作成
        DBHelper channelListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(channelListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(database);

        List<HashMap<String, String>> hashMaps = tvScheduleList.geTvsList();

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
                    values.put(UPDATE_DATE, !TextUtils.isEmpty(valName) ? valName.substring(0, 10) : "");
                }
                values.put(DBUtils.fourKFlgConversion(keyName), valName);
            }
            tvScheduleListDao.insert(values);
        }
        DataBaseManager.getInstance().closeDatabase();
    }

    /**
     * TvScheduleAPIの解析結果をDBに格納する(番組表用).
     *
     * @param channelInfoList チャンネル詳細情報
     * @param chInfoDate チャンネル情報取得日付
     */
    public synchronized void insertTvScheduleInsertList(final ChannelInfoList channelInfoList, final String chInfoDate) {
        DTVTLogger.start();
        if (channelInfoList.getChannels().size() == 0) {
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

        ArrayList<ChannelInfo> channelInfos = channelInfoList.getChannels();
        for (ChannelInfo channelInfo : channelInfos) {
            //DB名としてチャンネル番号を取得.
            String chNo = String.valueOf(channelInfo.getChNo());

            //各種オブジェクト作成
            DBHelperChannel tvScheduleListDBHelper = new DBHelperChannel(mContext, chNo);
            DataBaseManager.clearChInfo();
            DataBaseManager.initializeInstance(tvScheduleListDBHelper);
            SQLiteDatabase database = DataBaseManager.getChInstance().openChDatabase();
            TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(database);

            //ContentValuesに変換してDBに保存する.
            //TODO パラメータ要確認
            ArrayList<ScheduleInfo> scheduleInfos = channelInfo.getSchedules();
            for (ScheduleInfo scheduleInfo : scheduleInfos) {
                ContentValues values = new ContentValues();
                values.put(JsonConstants.META_RESPONSE_THUMB_448, ""); //imageURL?
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
                tvScheduleListDao.insert(values);
            }
            database.close();
            DataBaseManager.getChInstance().closeChDatabase();

            //保存したDBを所定の場所へ移動する
            String channelFilePath = StringUtils.getConnectStrings(filesDir, "/../databases/", chNo);
            File channelFile = new File(channelFilePath);
            File movedFile = new File(StringUtils.getConnectStrings(dbDir, "/", chNo));
            if (channelFile.isFile()) {
                try {
                    boolean isMovedChannelFile = channelFile.renameTo(movedFile);
                    if (!isMovedChannelFile) {
                        DTVTLogger.error("Failed to move DB file");
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
                    boolean isMovedJournalFile = journalFile.renameTo(movedJournalFile);
                    if (!isMovedJournalFile) {
                        DTVTLogger.error("Failed to move journal file");
                    }
                } catch (SecurityException | NullPointerException e) {
                    DTVTLogger.error(e.toString());
                }
            }
        }
    }
}