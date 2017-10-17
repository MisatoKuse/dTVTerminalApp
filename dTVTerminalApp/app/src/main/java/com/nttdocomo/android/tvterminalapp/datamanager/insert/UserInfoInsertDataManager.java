/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.UserInfoListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.HomeDBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserInfoInsertDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public UserInfoInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     * UserinfoAPIの解析結果をDBに格納する。
     *
     * @return
     */
    public void insertUserInfoInsertList(UserInfoList userInfoList) {

        //各種オブジェクト作成
        HomeDBHelper userInfoListDBHelper = new HomeDBHelper(mContext);
        SQLiteDatabase db = userInfoListDBHelper.getWritableDatabase();
        UserInfoListDao userInfoListDao = new UserInfoListDao(db);
        List<HashMap<String, String>> hashMaps1 = userInfoList.getLoggedinAccountList();
        List<HashMap<String, String>> hashMaps2 = userInfoList.getH4dAccountList();

        //DB保存前に前回取得したデータは全消去する
        userInfoListDao.delete();

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        for (int i = 0; i < hashMaps1.size(); i++) {
            Iterator entries = hashMaps1.get(i).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyName = (String) entry.getKey();
                String valName = (String) entry.getValue();
                userInfoListDao.insert(DBUtils.fourKFlgConversion(keyName), valName);
            }
        }
        for (int i = 0; i < hashMaps2.size(); i++) {
            Iterator entries = hashMaps2.get(i).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyName = (String) entry.getKey();
                String valName = (String) entry.getValue();
                userInfoListDao.insert(keyName, valName);
            }
        }
    }
}