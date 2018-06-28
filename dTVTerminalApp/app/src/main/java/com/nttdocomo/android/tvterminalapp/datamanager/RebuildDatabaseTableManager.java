/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DataBaseManager;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

/**
 * データベーステーブル再構築用クラス.
 */
public class RebuildDatabaseTableManager {
    /** コンテキスト. */
    private Context mContext;

    /**
     * コンストラクタ.
     * @param context コンテキスト
     */
    public RebuildDatabaseTableManager(final Context context) {
        this.mContext = context;
    }

    /**
     * データベース(db_data)内の全テーブルの再構築.
     */
    @SuppressWarnings("OverlyLongMethod")
    public void allTableRebuild() {
        DTVTLogger.start();
        try {
            //各種オブジェクト作成
            DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);
            DataBaseManager.initializeInstance(dataBaseHelper);
            SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
            database.acquireReference();
            DateUtils.clearLastDate(mContext);
            //チャンネルイスとテーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.CHANNEL_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_CHANNEL_SQL);

            //コンテンツブラウズテーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.DLNA_BROWSE_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_DLNA_BROWSE_SQL);

            //デイリーランクテーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.DAILYRANK_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_DAILY_RANK_SQL);

            //レコメンドチャンネルテーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.RECOMMEND_CHANNEL_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_CHANNEL_SQL);

            //レコメンドビデオテーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.RECOMMEND_VIDEO_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_VIDEO_SQL);

            //番組表テーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.TV_SCHEDULE_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_TV_SCHEDULE_SQL);

            //ユーザ情報テーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.USER_INFO_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_USER_INFO_SQL);

            //週間ランキングテーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.WEEKLYRANK_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_WEEKLYRANK_SQL);

            //ビデオランキングテーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.RANKING_VIDEO_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_RANKING_VIDEO_SQL);

            //レコメンド(DCH)テーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.RECOMMEND_LIST_DCHANNEL_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_DCHANNEL_SQL);

            //レコメンド(DTV)テーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.RECOMMEND_LIST_DTV_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_DTV_SQL);

            //レコメンド(DANIME)テーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.RECOMMEND_LIST_DANIME_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_DANIME_SQL);

            //ロールリストテーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.ROLE_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_ROLE_LIST_SQL);

            //レンタル一覧テーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.RENTAL_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_RENTAL_LIST_SQL);

            //レンタル一覧のactive_listテーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.RENTAL_ACTIVE_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_RENTAL_ACTIVE_LIST_SQL);

            //購入済みCH一覧テーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.RENTAL_CHANNEL_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_RENTAL_CHANNEL_LIST_SQL);

            //購入済みCH一覧のactive_listテーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.RENTAL_CHANNEL_ACTIVE_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_RENTAL_CHANNEL_ACTIVE_LIST_SQL);

            //クリップキー一覧(TV)テーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.TV_CLIP_KEY_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_TV_CLIP_KEY_LIST_SQL);

            //クリップキー一覧(VOD)テーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.VOD_CLIP_KEY_LIST_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_VOD_CLIP_KEY_LIST_SQL);

            //視聴中ビデオ一覧(VOD)テーブル再構築
            database.execSQL(StringUtils.getConnectStrings(DataBaseConstants.DROP_TABLE_TEXT, DataBaseConstants.WATCH_LISTEN_VIDEO_TABLE_NAME));
            database.execSQL(DataBaseConstants.CREATE_TABLE_WATCH_LISTEN_VIDEO_SQL);
        } catch (SQLException e) {
            DTVTLogger.debug("RebuildDatabaseTableManager::allTableRebuild, rebuild " + "table failed, cause=" + e.getCause());
        }
        DTVTLogger.end();
    }
}