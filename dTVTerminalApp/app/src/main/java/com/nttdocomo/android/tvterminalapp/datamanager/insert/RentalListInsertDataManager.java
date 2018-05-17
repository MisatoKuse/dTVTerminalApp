/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RentalListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * レンタル一覧：データ管理.
 */
public class RentalListInsertDataManager {

    /**
     * コンテキスト.
     */
    private  final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context Activity
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

        //取得データが空の場合は更新しないで、有効期限をクリアする
        if (rentalList == null || rentalList.getVodActiveData() == null || rentalList.getVodActiveData().size() < 1
                || rentalList.getVodMetaFullData() == null || rentalList.getVodMetaFullData().size() < 1) {
            DateUtils.clearLastProgramDate(mContext, DateUtils.RENTAL_VOD_LAST_UPDATE);
            return;
        }

        try {
            //各種オブジェクト作成
            DataBaseHelper rentalListDataBaseHelper = new DataBaseHelper(mContext);
            DataBaseManager.initializeInstance(rentalListDataBaseHelper);
            SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
            database.acquireReference();
            RentalListDao rentalListDao = new RentalListDao(database);
            ArrayList<VodMetaFullData> vodMetaFullDataList = rentalList.getVodMetaFullData();
            ArrayList<ActiveData> activeDataList = rentalList.getVodActiveData();

            //DB保存前に前回取得したデータは全消去する
            rentalListDao.delete();

            ContentValues values = new ContentValues();

            //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
            for (int i = 0; i < vodMetaFullDataList.size(); i++) {
                VodMetaFullData vodMetaFullData = vodMetaFullDataList.get(i);

                for (String item:vodMetaFullData.getRootPara()) {
                    //データがString型だけではなくなったので、変換を行ってから蓄積する
                    String valName = StringUtils.changeObject2String(vodMetaFullData.getMember(item));
                    values.put(DataBaseUtils.fourKFlgConversion(item), valName);
                }
                rentalListDao.insert(values);
            }

            for (int i = 0; i <  activeDataList.size(); i++) {
                ActiveData activeData = activeDataList.get(i);
                ContentValues activeValues = new ContentValues();

                String keyName = JsonConstants.META_RESPONSE_ACTIVE_LIST + JsonConstants.UNDER_LINE
                        + JsonConstants.META_RESPONSE_LICENSE_ID;
                String valName = StringUtils.changeObject2String(activeData.getLicenseId());
                activeValues.put(DataBaseUtils.fourKFlgConversion(keyName), valName);

                String keyName2 = JsonConstants.META_RESPONSE_ACTIVE_LIST + JsonConstants.UNDER_LINE
                        + JsonConstants.META_RESPONSE_VAILD_END_DATE;
                String valName2 = StringUtils.changeObject2String(activeData.getValidEndDate());
                activeValues.put(DataBaseUtils.fourKFlgConversion(keyName2), valName2);

                rentalListDao.insertActiveList(activeValues);
            }
            //データ保存日時を格納
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastDate(DateUtils.RENTAL_VOD_LAST_UPDATE);
        } catch (SQLiteException e) {
            DTVTLogger.debug("RentalListInsertDataManager::insertRentalListInsertList, e.cause=" + e.getCause());
        } finally {
            DataBaseManager.getInstance().closeDatabase();
        }
    }

    /**
     * 購入済みCH取得APIの解析結果をDBに保存する.
     *
     * @param rentalChList 購入済みCH一覧
     */
    @SuppressWarnings("OverlyLongMethod")
    public void insertChRentalListInsertList(final PurchasedChannelListResponse rentalChList) {
        try {
            //各種オブジェクト作成
            DataBaseHelper rentalListDataBaseHelper = new DataBaseHelper(mContext);
            DataBaseManager.initializeInstance(rentalListDataBaseHelper);
            SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
            database.acquireReference();
            RentalListDao rentalListDao = new RentalListDao(database);
            List<HashMap<String, String>> clList = rentalChList.getChannelListData().getChannelList();
            ArrayList<ActiveData> activeDataList = rentalChList.getChActiveData();

            //DB保存前に前回取得したデータは全消去する
            try {
                rentalListDao.deleteCh();
            } catch (SQLiteException e) {
                DTVTLogger.debug(e.toString());
            }

            ContentValues values = new ContentValues();

            //チャンネルメタデータの保存
            for (int i = 0; i < clList.size(); i++) {
                Iterator entries = clList.get(i).entrySet().iterator();
                ContentValues chValues = new ContentValues();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    String keyName = (String) entry.getKey();
                    String valName = (String) entry.getValue();
                    if (JsonConstants.META_RESPONSE_AVAIL_START_DATE.equals(keyName)) {
                        values.put(DataBaseConstants.UPDATE_DATE, !TextUtils.isEmpty(valName) ? valName.substring(0, 10) : "");
                    }
                    chValues.put(DataBaseUtils.fourKFlgConversion(keyName), valName);
                }
                rentalListDao.insertCh(chValues);
            }

            //active_listの保存
            for (int i = 0; i <  activeDataList.size(); i++) {
                ActiveData activeData = activeDataList.get(i);
                ContentValues activeValues = new ContentValues();

                String keyName = JsonConstants.META_RESPONSE_ACTIVE_LIST + JsonConstants.UNDER_LINE
                        + JsonConstants.META_RESPONSE_LICENSE_ID;
                String valName = StringUtils.changeObject2String(activeData.getLicenseId());
                activeValues.put(DataBaseUtils.fourKFlgConversion(keyName), valName);

                String keyName2 = JsonConstants.META_RESPONSE_ACTIVE_LIST + JsonConstants.UNDER_LINE
                        + JsonConstants.META_RESPONSE_VAILD_END_DATE;
                String valName2 = StringUtils.changeObject2String(activeData.getValidEndDate());
                activeValues.put(DataBaseUtils.fourKFlgConversion(keyName2), valName2);

                rentalListDao.insertChActiveList(activeValues);
            }
            //データ保存日時を格納
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastDate(DateUtils.RENTAL_CHANNEL_LAST_UPDATE);
        } catch (SQLiteException e) {
            DTVTLogger.debug("RentalListInsertDataManager::insertChRentalListInsertList, e.cause=" + e.getCause());
        } finally {
            DataBaseManager.getInstance().closeDatabase();
        }
    }
}
