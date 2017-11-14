/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.UPDATE_DATE;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATE_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_LINEAR_START_DATE;

public class TvScheduleInsertDataManager {

    private Context mContext;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * コンストラクタ
     *
     * @param context Activity
     */
    public TvScheduleInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     * TvScheduleAPIの解析結果をDBに格納する。
     */
    public void insertTvScheduleInsertList(TvScheduleList tvScheduleList) {

        //各種オブジェクト作成
        DBHelper channelListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(channelListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(database);

        List<HashMap<String,String>> hashMaps = tvScheduleList.geTvsList();

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
                if(TV_SCHEDULE_LIST_LINEAR_START_DATE.equals(keyName)){
                    values.put(UPDATE_DATE, !TextUtils.isEmpty(valName)?valName.substring(0,10):"");
                }
                values.put(DBUtils.fourKFlgConversion(keyName), valName);
            }
            values.put(DATE_TYPE, "home");
            tvScheduleListDao.insert(values);
        }
        DataBaseManager.getInstance().closeDatabase();
    }

    /**
     * TvScheduleAPIの解析結果をDBに格納する
     */
    public void insertTvScheduleInsertList( TvScheduleList tvScheduleList, String display_type) {

        //各種オブジェクト作成
        DBHelper tvScheduleListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(tvScheduleListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(database);
        List<HashMap<String,String>> hashMaps = tvScheduleList.geTvsList();

        //DB保存前に前回取得したデータは全消去する
        tvScheduleListDao.deleteByType(display_type);

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        for (int i = 0; i < hashMaps.size(); i++) {
            Iterator entries = hashMaps.get(i).entrySet().iterator();
            ContentValues values = new ContentValues();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyName = (String) entry.getKey();
                String valName = (String) entry.getValue();
                if(TV_SCHEDULE_LIST_LINEAR_START_DATE.equals(keyName)){
                    if(!TextUtils.isEmpty(valName)){
                        String update = valName.substring(0,10);
                        int hour = Integer.parseInt(valName.substring(11,13));
                        if( hour>=0 && hour < 4){
                            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.JAPAN);
                            Date date = new Date();
                            try {
                                date=sdf.parse(update);
                            }catch (ParseException e){
                                e.printStackTrace();
                            }
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                            update = sdf.format(calendar.getTime());
                            values.put(UPDATE_DATE, update);
                        }else{
                            values.put(UPDATE_DATE, update);
                        }
                    }
                }
                values.put(DBUtils.fourKFlgConversion(keyName), valName);
            }
            values.put(DATE_TYPE, "program");
            tvScheduleListDao.insert(values);
        }
        DataBaseManager.getInstance().closeDatabase();
    }
}
