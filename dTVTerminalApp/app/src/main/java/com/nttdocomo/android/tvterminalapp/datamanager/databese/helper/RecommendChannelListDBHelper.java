package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_VERSION;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDORDER;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CHANNELID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL2;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_ENDVIEWING;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED1;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED2;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED3;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED4;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED5;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_AGREEMENT;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_VIEWABLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_PAGEID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_GROUPID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class RecommendChannelListDBHelper extends SQLiteOpenHelper {

    /**
     * DB定義用定数(最終的にはXmlParser側から入手する
     */
    public static final String RECOMMEND_CHANNEL_LIST_TABLE_NAME = "recommend_channel_list";
    public static final String RECOMMEND_CHANNEL_LIST_ID_COLUMN = "row_id";

    //Homeキャッシュデータ格納用テーブル
    private static final String CREATE_TABLE_SQL = "" +
            "create table " + RECOMMEND_CHANNEL_LIST_TABLE_NAME + " (" +
            RECOMMEND_CHANNEL_LIST_ID_COLUMN + " integer primary key autoincrement, " +
            RECOMMENDCHANNEL_LIST_RECOMMENDORDER + " text, " +
            RECOMMENDCHANNEL_LIST_SERVICEID + " text, " +
            RECOMMENDCHANNEL_LIST_CATEGORYID + " text, " +
            RECOMMENDCHANNEL_LIST_CHANNELID + " text, " +
            RECOMMENDCHANNEL_LIST_CONTENTSID + " text, " +
            RECOMMENDCHANNEL_LIST_TITLE + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL1 + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL2 + " text, " +
            RECOMMENDCHANNEL_LIST_STARTVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_ENDVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED1 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED2 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED3 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED4 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED5 + " text, " +
            RECOMMENDCHANNEL_LIST_AGREEMENT + " text, " +
            RECOMMENDCHANNEL_LIST_VIEWABLE + " text, " +
            RECOMMENDCHANNEL_LIST_PAGEID + " text " +
            RECOMMENDCHANNEL_LIST_GROUPID + " text " +
            RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID + " text " +
            ")";

    /**
     * 「channel_list」テーブルの削除用SQL
     */
    private static final String DROP_TABLE_SQL = "drop table if exists " + RECOMMEND_CHANNEL_LIST_TABLE_NAME;

    /**
     * コンストラクタ
     * @param context
     */
    public RecommendChannelListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_SQL);
    }
}
