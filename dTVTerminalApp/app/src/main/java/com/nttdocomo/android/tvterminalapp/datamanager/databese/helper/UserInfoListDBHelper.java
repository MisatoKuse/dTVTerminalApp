package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_VERSION;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_LOGGEDIN_ACCOUNT;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class UserInfoListDBHelper extends SQLiteOpenHelper{
    /**
     * DB定義用定数(最終的にはJsonParser側から入手する
     */
    public static final String USER_INFO_LIST_TABLE_NAME = "user_info_list";
    public static final String USER_INFO_LIST_ID_COLUMN = "row_id";

    //Homeキャッシュデータ格納用テーブル
    private static final String CREATE_TABLE_SQL = "" +
            "create table " + USER_INFO_LIST_TABLE_NAME + " (" +
            USER_INFO_LIST_ID_COLUMN + " integer primary key autoincrement, " +
            USER_INFO_LIST_LOGGEDIN_ACCOUNT + " text, " +
            USER_INFO_LIST_CONTRACT_STATUS + " text, " +
            USER_INFO_LIST_DCH_AGE_REQ +" text," +
            USER_INFO_LIST_H4D_AGE_REQ + " text" +
            ")";


    public UserInfoListDBHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
