package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_VERSION;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_AGREEMENT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CATEGORYID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CHANNELID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CONTENTSID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CTPICURL1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CTPICURL2;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_ENDVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_GROUPID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_PAGEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RECOMMENDMETHODID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RECOMMENDORDER;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED2;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED3;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED4;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED5;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_SERVICEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_STARTVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_VIEWABLE;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class RecommendVideoListDBHelper extends SQLiteOpenHelper {

    /**
     * DB定義用定数(最終的にはXMLParser側から入手する
     */
    public static final String RECOMMEND_VIDEO_LIST_TABLE_NAME = "recommend_video_list";
    public static final String RECOMMEND_VIDEO_LIST_ID_COLUMN = "row_id";

    //Homeキャッシュデータ格納用テーブル
    private static final String CREATE_TABLE_SQL = "" +
            "create table " + RECOMMEND_VIDEO_LIST_TABLE_NAME + " (" +
            RECOMMEND_VIDEO_LIST_ID_COLUMN + " integer primary key autoincrement, " +
            RECOMMENDVIDEO_LIST_RECOMMENDORDER + " text, " +
            RECOMMENDVIDEO_LIST_SERVICEID + " text, " +
            RECOMMENDVIDEO_LIST_CATEGORYID + " text, " +
            RECOMMENDVIDEO_LIST_CHANNELID + " text, " +
            RECOMMENDVIDEO_LIST_CONTENTSID + " text, " +
            RECOMMENDVIDEO_LIST_TITLE + " text, " +
            RECOMMENDVIDEO_LIST_CTPICURL1 + " text, " +
            RECOMMENDVIDEO_LIST_CTPICURL2 + " text, " +
            RECOMMENDVIDEO_LIST_STARTVIEWING + " text, " +
            RECOMMENDVIDEO_LIST_ENDVIEWING + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED1 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED2 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED3 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED4 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED5 + " text, " +
            RECOMMENDVIDEO_LIST_AGREEMENT + " text, " +
            RECOMMENDVIDEO_LIST_VIEWABLE + " text, " +
            RECOMMENDVIDEO_LIST_PAGEID + " text, " +
            RECOMMENDVIDEO_LIST_GROUPID + " text, " +
            RECOMMENDVIDEO_LIST_RECOMMENDMETHODID + " text " +
            ")";

    /**
     * 「channel_list」テーブルの削除用SQL
     */
    private static final String DROP_TABLE_SQL = "drop table if exists " + RECOMMEND_VIDEO_LIST_TABLE_NAME;

    /**
     * コンストラクタ
     * @param context
     */
    public RecommendVideoListDBHelper(Context context) {
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
