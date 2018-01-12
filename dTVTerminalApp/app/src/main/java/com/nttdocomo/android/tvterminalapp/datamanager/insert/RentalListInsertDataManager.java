/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RentalListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.UPDATE_DATE;

/**
 * レンタル一覧：データ管理.
 */
public class RentalListInsertDataManager {

    /*
    public boolean insertRentalList(){
        return false;
    }
    */

    /**
     * コンテキスト.
     */
    private Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public RentalListInsertDataManager(final Context context) {
        mContext = context;
    }

    /**
     * RentalListAPIの解析結果をDBに格納する.
     *
     * @param rentalList 購入済みVOD一覧
     */
    public void insertRentalListInsertList(final PurchasedVodListResponse rentalList) {

        //各種オブジェクト作成
        DBHelper rentalListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = rentalListDBHelper.getWritableDatabase();
        RentalListDao rentalListDao = new RentalListDao(db);
        String status = rentalList.getStatus();
        ArrayList<VodMetaFullData> vodMetaFullDataList = rentalList.getVodMetaFullData();
        ArrayList<ActiveData> activeDataList = rentalList.getVodActiveData();

        //DB保存前に前回取得したデータは全消去する
        try {
            rentalListDao.delete();
        } catch (Exception e) {
            DTVTLogger.debug("RentalListInsertDataManager::insertRentalListInsertList, e.cause=" + e.getCause());
        }

        ContentValues values = new ContentValues();
        values.put(JsonContents.META_RESPONSE_STATUS, status);
        rentalListDao.insert(values);

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        for (int i = 0; i < vodMetaFullDataList.size(); i++) {
            VodMetaFullData vodMetaFullData = vodMetaFullDataList.get(i);
            ContentValues vodValues = new ContentValues();

            for (String item:vodMetaFullData.getRootPara()) {
                String keyName = item;
                //データがString型だけではなくなったので、変換を行ってから蓄積する
                String valName = StringUtil.changeObject2String(vodMetaFullData.getMember(item));
                vodValues.put(DBUtils.fourKFlgConversion(keyName), valName);
            }
            rentalListDao.insert(vodValues);
        }

        for (int i = 0; i <  activeDataList.size(); i++) {
            ActiveData activeData = activeDataList.get(i);
            ContentValues activeValues = new ContentValues();

            String keyName = JsonContents.META_RESPONSE_ACTIVE_LIST + JsonContents.UNDER_LINE
                    + JsonContents.META_RESPONSE_LICENSE_ID;
            String valName = StringUtil.changeObject2String(activeData.getLicenseId());
            activeValues.put(DBUtils.fourKFlgConversion(keyName), valName);

            String keyName2 = JsonContents.META_RESPONSE_ACTIVE_LIST + JsonContents.UNDER_LINE
                    + JsonContents.META_RESPONSE_VAILD_END_DATE;
            String valName2 = StringUtil.changeObject2String(activeData.getValidEndDate());
            activeValues.put(DBUtils.fourKFlgConversion(keyName2), valName2);

            rentalListDao.insert(activeValues);
        }
    }

    /**
     * 購入済みCH取得APIの解析結果をDBに保存する.
     *
     * @param rentalChList 購入済みCH一覧
     */
    public void insertChRentalListInsertList(final PurchasedChListResponse rentalChList) {
        //各種オブジェクト作成
        DBHelper rentalListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = rentalListDBHelper.getWritableDatabase();
        RentalListDao rentalListDao = new RentalListDao(db);
        String status = rentalChList.getStatus();
        List<HashMap<String, String>> clList = rentalChList.getChannelListData().getClList();
        ArrayList<ActiveData> activeDataList = rentalChList.getChActiveData();

        //DB保存前に前回取得したデータは全消去する
        try {
            rentalListDao.deleteCh();
        } catch (Exception e) {
            DTVTLogger.debug(e.toString());
        }

        //statusの保存
        ContentValues values = new ContentValues();
        values.put(JsonContents.META_RESPONSE_STATUS, status);
        rentalListDao.insertCh(values);

        //チャンネルメタデータの保存
        for (int i = 0; i < clList.size(); i++) {
            Iterator entries = clList.get(i).entrySet().iterator();
            ContentValues chValues = new ContentValues();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyName = (String) entry.getKey();
                String valName = (String) entry.getValue();
                if (JsonContents.META_RESPONSE_AVAIL_START_DATE.equals(keyName)) {
                    values.put(UPDATE_DATE, !TextUtils.isEmpty(valName) ? valName.substring(0, 10) : "");
                }
                chValues.put(DBUtils.fourKFlgConversion(keyName), valName);
            }
            rentalListDao.insertCh(chValues);
        }

        //active_listの保存
        for (int i = 0; i <  activeDataList.size(); i++) {
            ActiveData activeData = activeDataList.get(i);
            ContentValues activeValues = new ContentValues();

            String keyName = JsonContents.META_RESPONSE_ACTIVE_LIST + JsonContents.UNDER_LINE
                    + JsonContents.META_RESPONSE_LICENSE_ID;
            String valName = StringUtil.changeObject2String(activeData.getLicenseId());
            activeValues.put(DBUtils.fourKFlgConversion(keyName), valName);

            String keyName2 = JsonContents.META_RESPONSE_ACTIVE_LIST + JsonContents.UNDER_LINE
                    + JsonContents.META_RESPONSE_VAILD_END_DATE;
            String valName2 = StringUtil.changeObject2String(activeData.getValidEndDate());
            activeValues.put(DBUtils.fourKFlgConversion(keyName2), valName2);

            rentalListDao.insertCh(activeValues);
        }
    }
 }
