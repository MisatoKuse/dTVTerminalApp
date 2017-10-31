/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RentalListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;

import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_THUMB;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_TITLE;

public class RentalListDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public RentalListDataManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * レンタル一覧画面用クリップデータを返却する
     *
     * @return
     */
    public List<Map<String, String>> selectRentalListData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {VOD_META_FULL_DATA_THUMB, VOD_META_FULL_DATA_TITLE,
                VOD_META_FULL_DATA_DISPLAY_START_DATE, VOD_META_FULL_DATA_DISP_TYPE};

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
