/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;

import java.util.List;
import java.util.Map;

public class ClipKeyListDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public ClipKeyListDataManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * クリップ一覧画面用クリップデータを返却する
     *
     * @return
     */
    public List<Map<String, String>> selectClipKeyListData() {
        //必要な列を列挙する
        String[] columns = {JsonContents.META_RESPONSE_CRID,
                JsonContents.META_RESPONSE_SERVICE_ID,
                JsonContents.META_RESPONSE_EVENT_ID,
                JsonContents.META_RESPONSE_TYPE,
                JsonContents.META_RESPONSE_TITLE_ID};

        //Daoクラス使用準備
        DBHelper homeDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = homeDBHelper.getWritableDatabase();
        ClipKeyListDao clipKeyListDao = new ClipKeyListDao(db);

        //データ取得
        List<Map<String, String>> list = clipKeyListDao.findById(columns);
        db.close();
        homeDBHelper.close();
        return list;
    }
    
}
