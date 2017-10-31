/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RentalListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.ArrayList;

/**
 * レンタル一覧：データ管理
 */
public class RentalListInsertDataManager {

    /*
    public boolean insertRentalList(){
        return false;
    }
    */

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public RentalListInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     * RentalListAPIの解析結果をDBに格納する。
     *
     * @return
     */
    public void insertRentalListInsertList(PurchasedVodListResponse rentalList) {

        //各種オブジェクト作成
        DBHelper rentalListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = rentalListDBHelper.getWritableDatabase();
        RentalListDao rentalListDao = new RentalListDao(db);
        ArrayList<VodMetaFullData> vodMetaFullDataList = rentalList.getVodMetaFullData();

        //DB保存前に前回取得したデータは全消去する
        try{
            rentalListDao.delete();
        }catch (Exception e){
            Log.d(DTVTConstants.LOG_DEF_TAG, "RentalListInsertDataManager::insertRentalListInsertList, e.cause=" + e.getCause());
        }

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        for (int i = 0; i < vodMetaFullDataList.size(); i++) {
            VodMetaFullData vodMetaFullData = vodMetaFullDataList.get(i);
            ContentValues values = new ContentValues();

            for (String item:vodMetaFullData. getRootPara()) {
                String keyName = item;
                String valName = (String)vodMetaFullData.getMember(item);
                values.put(DBUtils.fourKFlgConversion(keyName), valName);
            }
            rentalListDao.insert(values);
        }
    }

}
