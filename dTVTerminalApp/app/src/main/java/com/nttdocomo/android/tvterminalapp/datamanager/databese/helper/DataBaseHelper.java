/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.HomeDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

/**
 * データベースヘルパー.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    /**
     * DBVersion.
     */
    private static final int DATABASE_VERSION = 2;
    /**
     * drop table if exists.
     */
    private static final String DROP_TABLE_IF_EXISTS = "drop table if exists ";

    /**
     * コンテキストファイル.
     */
    private Context mContext = null;

    /**
     * 「channel_list」テーブルの削除用SQL.
     */
    private static final String DROP_CHANNEL_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.CHANNEL_LIST_TABLE_NAME);
    /**
     * 「コンテンツブラウズ」テーブルの削除用SQL.
     */
    private static final String DROP_DLNA_BROWSE_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.DLNA_BROWSE_TABLE_NAME);
    /**
     * 「デイリーランキング」テーブルの削除用SQL.
     */
    private static final String DROP_DAILYRANK_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.DAILYRANK_LIST_TABLE_NAME);
    /**
     * 「ユーザ情報」テーブルの削除用SQL.
     */
    private static final String DROP_USER_INFO_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.USER_INFO_LIST_TABLE_NAME);
    /**
     * 「レコメンド（ホームおすすめ番組）」テーブルの削除用SQL.
     */
    private static final String DROP_RECOMMEND_HOME_CHANNEL_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RECOMMEND_HOME_CHANNEL_LIST_TABLE_NAME);
    /**
     * 「レコメンド（ホームおすすめ番組）」テーブルの削除用SQL.
     */
    private static final String DROP_RECOMMEND_HOME_VIDEO_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RECOMMEND_HOME_VIDEO_LIST_TABLE_NAME);
    /**
     * 「おすすめチャンネル」テーブルの削除用SQL.
     */
    private static final String DROP_RECOMMEND_CHANNEL_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RECOMMEND_CHANNEL_LIST_TABLE_NAME);
    /**
     * 「おすすめビデオ」テーブルの削除用SQL.
     */
    private static final String DROP_RECOMMEND_VIDEO_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RECOMMEND_VIDEO_LIST_TABLE_NAME);
    /**
     * 「TV番組表」テーブルの削除用SQL.
     */
    private static final String DROP_TV_SCHEDULE_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.TV_SCHEDULE_LIST_TABLE_NAME);
    /**
     * 「週間ランキングリスト」テーブルの削除用SQL.
     */
    private static final String DROP_WEEKLYRANK_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.WEEKLYRANK_LIST_TABLE_NAME);
    /**
     * 「ビデオランキング」テーブルの削除用SQL.
     */
    private static final String DROP_RANKING_VIDEO_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RANKING_VIDEO_LIST_TABLE_NAME);
    /**
     * 「レコメンド（dチャンネル）」テーブルの削除用SQL.
     */
    private static final String DROP_RECOMMEND_DCHANNEL_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RECOMMEND_LIST_DCHANNEL_TABLE_NAME);
    /**
     * 「レコメンド（dTV）」テーブルの削除用SQL.
     */
    private static final String DROP_RECOMMEND_DTV_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RECOMMEND_LIST_DTV_TABLE_NAME);
    /**
     * 「レコメンド（dアニメ）」テーブルの削除用SQL.
     */
    private static final String DROP_RECOMMEND_DANIME_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RECOMMEND_LIST_DANIME_TABLE_NAME);
    /**
     * 「レコメンド（DAZN）」テーブルの削除用SQL.
     */
    private static final String DROP_RECOMMEND_DAZN_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RECOMMEND_LIST_DAZN_TABLE_NAME);
    /**
     * 「ロールリスト」テーブルの削除用SQL.
     */
    private static final String DROP_ROLE_LIST_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.ROLE_LIST_TABLE_NAME);
    /**
     * 視聴中ビデオ一覧 テーブル削除用.
     */
    private static final String DROP_WATCH_LISTEN_VIDEO_LIST_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.WATCH_LISTEN_VIDEO_TABLE_NAME);
    /**
     * 「レンタル一覧」テーブルの削除用SQL.
     */
    private static final String DROP_RENTAL_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RENTAL_LIST_TABLE_NAME);
    /**
     * 「レンタル一覧」のactive_listテーブルの削除用SQL.
     */
    private static final String DROP_RENTAL_ACTIVE_LIST_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RENTAL_ACTIVE_LIST_TABLE_NAME);
    /**
     * 「購入済みCH一覧」テーブルの削除用SQL.
     */
    private static final String DROP_RENTAL_CHANNEL_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RENTAL_CHANNEL_LIST_TABLE_NAME);
    /**
     * 「購入済みCH一覧」のactive_listテーブルの削除用SQL.
     */
    private static final String DROP_RENTAL_CHANNEL_ACTIVE_LIST_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.RENTAL_CHANNEL_ACTIVE_LIST_TABLE_NAME);
    /**
     * クリップキー一覧 テーブル削除用SQL.
     */
    private static final String DROP_TV_CLIP_KEY_LIST_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.TV_CLIP_KEY_LIST_TABLE_NAME);
    /**
     * 「クリップキー一覧 キャッシュデータ」テーブル削除用SQL.
     */
    private static final String DROP_VOD_CLIP_KEY_LIST_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DataBaseConstants.VOD_CLIP_KEY_LIST_TABLE_NAME);

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public DataBaseHelper(final Context context) {
        super(context, DataBaseConstants.DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context.getApplicationContext();
    }

    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        DTVTLogger.start();
        createAllTable(sqLiteDatabase);
        DTVTLogger.end();
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
        DTVTLogger.start();
        //DB更新時に
        if (oldVersion < newVersion) {
            DateUtils.clearLastDate(mContext);

            dropAllTable(sqLiteDatabase);
            createAllTable(sqLiteDatabase);
            DTVTLogger.debug("DatabaseHelper::onUpgrade completed.");
        }
        DTVTLogger.end();
    }

    /**
     * すべてのテーブルを作成する.
     *
     * @param sqLiteDatabase データベース
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public static void createAllTable(final SQLiteDatabase sqLiteDatabase) {
        DTVTLogger.start();
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_CHANNEL_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_CHANNEL_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_DLNA_BROWSE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_DLNA_BROWSE_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_DAILY_RANK_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_DAILY_RANK_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_TV_SCHEDULE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_TV_SCHEDULE_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_USER_INFO_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_USER_INFO_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_WEEKLYRANK_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_WEEKLYRANK_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_HOME_CHANNEL_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RECOMMEND_HOME_CHANNEL_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_CHANNEL_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RECOMMEND_CHANNEL_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_HOME_VIDEO_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RECOMMEND_HOME_VIDEO_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_VIDEO_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RECOMMEND_VIDEO_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RANKING_VIDEO_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RANKING_VIDEO_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_DCHANNEL_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RECOMMEND_DCHANNEL_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_DTV_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RECOMMEND_DTV_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_DANIME_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RECOMMEND_DANIME_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RECOMMEND_DAZN_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RECOMMEND_DAZN_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_ROLE_LIST_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_ROLE_LIST_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_WATCH_LISTEN_VIDEO_SQL);   //クリップ一覧画面用
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_WATCH_LISTEN_VIDEO_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RENTAL_LIST_SQL);   //レンタル一覧画面用
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RENTAL_LIST_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RENTAL_ACTIVE_LIST_SQL);   //レンタルのactive_list一覧用
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RENTAL_ACTIVE_LIST_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RENTAL_CHANNEL_LIST_SQL);   //購入済みCH一覧用
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_RENTAL_CHANNEL_LIST_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_RENTAL_CHANNEL_ACTIVE_LIST_SQL);   //購入済みCHのactive_lis一覧用
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.CREATE_TABLE_RENTAL_CHANNEL_ACTIVE_LIST_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_TV_CLIP_KEY_LIST_SQL); // クリップキー一覧(TV)
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_TV_CLIP_KEY_LIST_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_VOD_CLIP_KEY_LIST_SQL); // クリップキー一覧(VOD)
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::onCreate, create " + DataBaseConstants.
                    CREATE_TABLE_VOD_CLIP_KEY_LIST_SQL + " table failed, cause=" + e.getCause());
        }
        DTVTLogger.end();
    }

    /**
     * 全てのテーブルを削除.
     * @param sqLiteDatabase データベース
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public static void dropAllTable(final SQLiteDatabase sqLiteDatabase) {
        DTVTLogger.start();
        try {
            sqLiteDatabase.execSQL(DROP_CHANNEL_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_CHANNEL_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_DLNA_BROWSE_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_DLNA_BROWSE_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_DAILYRANK_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_DAILYRANK_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_TV_SCHEDULE_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_TV_SCHEDULE_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_USER_INFO_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_USER_INFO_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_WEEKLYRANK_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_WEEKLYRANK_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RECOMMEND_HOME_CHANNEL_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RECOMMEND_HOME_CHANNEL_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RECOMMEND_HOME_VIDEO_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RECOMMEND_HOME_VIDEO_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RECOMMEND_CHANNEL_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RECOMMEND_CHANNEL_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RECOMMEND_DAZN_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RECOMMEND_DAZN_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RECOMMEND_VIDEO_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RECOMMEND_VIDEO_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RANKING_VIDEO_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RANKING_VIDEO_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RECOMMEND_DCHANNEL_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RECOMMEND_DCHANNEL_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RECOMMEND_DTV_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RECOMMEND_DTV_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RECOMMEND_DANIME_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RECOMMEND_DANIME_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_ROLE_LIST_TABLE_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_ROLE_LIST_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_WATCH_LISTEN_VIDEO_LIST_TABLE_SQL);   //視聴中ビデオ画面用
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_WATCH_LISTEN_VIDEO_LIST_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RENTAL_TABLE_SQL);   //レンタル一覧画面用
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RENTAL_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RENTAL_ACTIVE_LIST_SQL);   //レンタルのactive_list一覧用
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RENTAL_ACTIVE_LIST_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RENTAL_CHANNEL_TABLE_SQL);   //購入済みCH一覧用
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RENTAL_CHANNEL_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_RENTAL_CHANNEL_ACTIVE_LIST_SQL);   //購入済みCHのactive_lis一覧用
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_RENTAL_CHANNEL_ACTIVE_LIST_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_TV_CLIP_KEY_LIST_TABLE_SQL); // クリップキー一覧(TV)
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_TV_CLIP_KEY_LIST_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DROP_VOD_CLIP_KEY_LIST_TABLE_SQL); // クリップキー一覧(VOD)
        } catch (SQLiteException e) {
            DTVTLogger.debug("DatabaseHelper::dropAllTable, drop " + DROP_VOD_CLIP_KEY_LIST_TABLE_SQL
                    + " table failed, cause=" + e.getCause());
        }
        DTVTLogger.end();
    }
}
