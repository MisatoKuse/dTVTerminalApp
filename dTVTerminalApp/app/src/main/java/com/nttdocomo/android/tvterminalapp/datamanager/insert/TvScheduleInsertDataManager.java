package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.TvScheduleListDBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class TvScheduleInsertDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public TvScheduleInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     * TvScheduleAPIの解析結果をDBに格納する。
     *
     * @return
     */
    public void insertTvScheduleInsertList( TvScheduleList tvScheduleList) {

        //各種オブジェクト作成
        TvScheduleListDBHelper tvScheduleListDBHelper = new TvScheduleListDBHelper(mContext);
        SQLiteDatabase db = tvScheduleListDBHelper.getWritableDatabase();
        TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(db);
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
                values.put(DBUtils.fourKFlgConversion(keyName), valName);
            }
            tvScheduleListDao.insert(values);
        }
    }
}
