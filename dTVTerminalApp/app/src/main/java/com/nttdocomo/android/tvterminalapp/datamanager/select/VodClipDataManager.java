/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VodClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;

import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_R_VALUE;

public class VodClipDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public VodClipDataManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * クリップ一覧画面用クリップデータを返却する
     *
     * @return
     */
    public List<Map<String, String>> selectVodClipData() {
        //ホーム画面に必要な列を列挙する
        //String[] columns = {VODCLIP_LIST_THUMB, VODCLIP_LIST_TITLE,
        //        VODCLIP_LIST_DISPLAY_START_DATE, VODCLIP_LIST_DISP_TYPE};
        String[] columns = {VODCLIP_LIST_THUMB, VODCLIP_LIST_TITLE,
                VODCLIP_LIST_DISPLAY_START_DATE, VODCLIP_LIST_R_VALUE};

        //Daoクラス使用準備
        DBHelper homeDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = homeDBHelper.getWritableDatabase();
        VodClipListDao vodClipListDao = new VodClipListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = vodClipListDao.findById(columns);
        db.close();
        homeDBHelper.close();
        return list;
    }
    
}
