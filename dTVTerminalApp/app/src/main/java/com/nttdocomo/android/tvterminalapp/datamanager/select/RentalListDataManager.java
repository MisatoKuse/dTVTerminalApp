/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RentalListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;

import java.util.List;
import java.util.Map;

public class RentalListDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param mContext コンテキスト
     */
    public RentalListDataManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * レンタル一覧画面用クリップデータを返却する
     *
     * @return クリップデータリスト
     */
    public List<Map<String, String>> selectRentalListData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonContents.META_RESPONSE_THUMB_448, JsonContents.META_RESPONSE_TITLE,
                JsonContents.META_RESPONSE_PUBLISH_END_DATE, JsonContents.META_RESPONSE_DISP_TYPE};

        //Daoクラス使用準備
        DBHelper homeDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = homeDBHelper.getWritableDatabase();
        RentalListDao rentalListDao = new RentalListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = rentalListDao.findById(columns);
        db.close();
        homeDBHelper.close();
        return list;
    }
    
}
