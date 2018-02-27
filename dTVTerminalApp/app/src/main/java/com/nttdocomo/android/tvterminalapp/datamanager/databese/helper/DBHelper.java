/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

public class DBHelper extends SQLiteOpenHelper {

    /**
     * DBVersion.
     */
    private static final int DATABASE_VERSION = 1;

    private static final String DROP_TABLE_IF_EXISTS = "drop table if exists ";

    /**
     * 「channel_list」テーブルの削除用SQL.
     */
    private static final String DROP_CHANNEL_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.CHANNEL_LIST_TABLE_NAME);
    private static final String DROP_DAILYRANK_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.DAILYRANK_LIST_TABLE_NAME);
    private static final String DROP_RECOMMEND_CHANNEL_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.RECOMMEND_CHANNEL_LIST_TABLE_NAME);
    private static final String DROP_RECOMMEND_VIDEO_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.RECOMMEND_VIDEO_LIST_TABLE_NAME);
    private static final String DROP_TV_SCHEDULE_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.TV_SCHEDULE_LIST_TABLE_NAME);
    private static final String USER_INFO_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.USER_INFO_LIST_TABLE_NAME);
    private static final String DROP_VODCLIP_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.VODCLIP_LIST_TABLE_NAME);
    private static final String DROP_WEEKLYRANK_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.WEEKLYRANK_LIST_TABLE_NAME);
    private static final String DROP_VIDEORANK_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.RANKING_VIDEO_LIST_TABLE_NAME);
    private static final String DROP_RECOMMEND_DCHANNEL_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.RECOMMEND_LIST_DCHANNEL_TABLE_NAME);
    private static final String DROP_RECOMMEND_DTV_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.RECOMMEND_LIST_DTV_TABLE_NAME);
    private static final String DROP_ROLE_LIST_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.ROLE_LIST_TABLE_NAME);
    private static final String DROP_DOWNLOAD_LIST_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.DOWNLOAD_LIST_TABLE_NAME);
    private static final String DROP_RECOMMEND_DANIME_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.RECOMMEND_LIST_DANIME_TABLE_NAME);
    /**
     * 「tv clip list」テーブルの削除用SQL.
     */
    private static final String DROP_TVCLIP_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.TVCLIP_LIST_TABLE_NAME);
    /**
     * 「レンタル一覧」テーブルの削除用SQL.
     */
    private static final String DROP_RENTAL_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.RENTAL_LIST_TABLE_NAME);
    /**
     * 「レンタル一覧」のactive_listテーブルの削除用SQL.
     */
    private static final String DROP_RENTAL_ACTIVE_LIST_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.RENTAL_ACTIVE_LIST_TABLE_NAME);
    /**
     * 「購入済みCH一覧」テーブルの削除用SQL.
     */
    private static final String DROP_RENTAL_CHANNEL_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.RENTAL_CHANNEL_LIST_TABLE_NAME);
    /**
     * 「購入済みCH一覧」のactive_listテーブルの削除用SQL.
     */
    private static final String DROP_RENTAL_CHANNEL_ACTIVE_LIST_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.RENTAL_CHANNEL_ACTIVE_LIST_TABLE_NAME);
    /**
     * クリップキー一覧 テーブル削除用SQL.
     */
    private static final String DROP_TV_CLIP_KEY_LIST_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.TV_CLIP_KEY_LIST_TABLE_NAME);
    private static final String DROP_VOD_CLIP_KEY_LIST_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.VOD_CLIP_KEY_LIST_TABLE_NAME);

    /**
     * 視聴中ビデオ一覧 テーブル削除用.
     */
    private static final String DROP_WATCH_LISTEN_VIDEO_LIST_TABLE_SQL = StringUtils.getConnectStrings(
            DROP_TABLE_IF_EXISTS, DBConstants.WATCH_LISTEN_VIDEO_TABLE_NAME);

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public DBHelper(final Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_CHANNEL_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_DAILY_RANK_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_TV_SCHEDULE_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_USER_INFO_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_WEEKLYRANK_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RECOMMEND_CHANNEL_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RECOMMEND_VIDEO_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_VODCLIP_LIST_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RANKING_VIDEO_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RECOMMEND_DCHANNEL_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RECOMMEND_DTV_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RECOMMEND_DANIME_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_ROLE_LIST_SQL);
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_DOWNLOAD_LIST_SQL);
        try {
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_WATCH_LISTEN_VIDEO_SQL);   //クリップ一覧画面用
        } catch (Exception e) {
            DTVTLogger.debug("HomeDBHelper::onCreate, create " + DBConstants.CREATE_TABLE_WATCH_LISTEN_VIDEO_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_TVCLIP_LIST_SQL);   //クリップ一覧画面用
        } catch (Exception e) {
            DTVTLogger.debug("HomeDBHelper::onCreate, create " + DBConstants.CREATE_TABLE_TVCLIP_LIST_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RENTAL_LIST_SQL);   //レンタル一覧画面用
        } catch (Exception e) {
            DTVTLogger.debug("HomeDBHelper::onCreate, create " + DBConstants.CREATE_TABLE_RENTAL_LIST_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RENTAL_ACTIVE_LIST_SQL);   //レンタルのactive_list一覧用
        } catch (Exception e) {
            DTVTLogger.debug("HomeDBHelper::onCreate, create " + DBConstants.CREATE_TABLE_RENTAL_ACTIVE_LIST_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RENTAL_CHANNEL_LIST_SQL);   //購入済みCH一覧用
        } catch (Exception e) {
            DTVTLogger.debug("HomeDBHelper::onCreate, create " + DBConstants.CREATE_TABLE_RENTAL_CHANNEL_LIST_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RENTAL_CHANNEL_ACTIVE_LIST_SQL);   //購入済みCHのactive_lis一覧用
        } catch (Exception e) {
            DTVTLogger.debug("HomeDBHelper::onCreate, create " + DBConstants.CREATE_TABLE_RENTAL_CHANNEL_ACTIVE_LIST_SQL
                    + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_TV_CLIP_KEY_LIST_SQL); // クリップキー一覧(TV)
        } catch (Exception e) {
            DTVTLogger.debug("HomeDBHelper::onCreate, create " + DBConstants.CREATE_TABLE_TV_CLIP_KEY_LIST_SQL + " table failed, cause=" + e.getCause());
        }
        try {
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_VOD_CLIP_KEY_LIST_SQL); // クリップキー一覧(VOD)
        } catch (Exception e) {
            DTVTLogger.debug("HomeDBHelper::onCreate, create " + DBConstants.CREATE_TABLE_VOD_CLIP_KEY_LIST_SQL + " table failed, cause=" + e.getCause());
        }

    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
        if (oldVersion < newVersion) {
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_CHANNEL_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_DAILY_RANK_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RECOMMEND_CHANNEL_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RECOMMEND_VIDEO_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_TV_SCHEDULE_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_USER_INFO_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_VODCLIP_LIST_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_WEEKLYRANK_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RANKING_VIDEO_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RECOMMEND_DCHANNEL_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RECOMMEND_DTV_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RECOMMEND_DANIME_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_ROLE_LIST_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_DOWNLOAD_LIST_SQL);
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_TVCLIP_LIST_SQL);  //クリップ一覧画面用
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RENTAL_LIST_SQL);  //レンタル一覧画面用
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RENTAL_ACTIVE_LIST_SQL);  //レンタル一覧のactive_list用
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RENTAL_CHANNEL_LIST_SQL);  //購入済みCH一覧用
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_RENTAL_CHANNEL_ACTIVE_LIST_SQL);  //購入済みCH一覧のactive_list用
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_TV_CLIP_KEY_LIST_SQL); // クリップキー一覧(TV)
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_VOD_CLIP_KEY_LIST_SQL); // クリップキー一覧(VOD)
            sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_WATCH_LISTEN_VIDEO_SQL); // 視聴中ビデオ一覧(VOD)
        }
    }
}
