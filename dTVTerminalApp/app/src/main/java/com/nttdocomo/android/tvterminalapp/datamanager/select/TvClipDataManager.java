/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;

import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvClipJsonParser.TVCLIP_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvClipJsonParser.TVCLIP_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvClipJsonParser.TVCLIP_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvClipJsonParser.TVCLIP_LIST_TITLE;

public class TvClipDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public TvClipDataManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * クリップ一覧画面用クリップデータを返却する
     *
     * @return
     */
    public List<Map<String, String>> selectTvClipData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {TVCLIP_LIST_THUMB, TVCLIP_LIST_TITLE,
                TVCLIP_LIST_DISPLAY_START_DATE, TVCLIP_LIST_DISP_TYPE};

        //Daoクラス使用準備
        DBHelper homeDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = homeDBHelper.getWritableDatabase();
        TvClipListDao tvClipListDao = new TvClipListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = tvClipListDao.findById(columns);
        db.close();
        homeDBHelper.close();
        return list;
    }
    
}
