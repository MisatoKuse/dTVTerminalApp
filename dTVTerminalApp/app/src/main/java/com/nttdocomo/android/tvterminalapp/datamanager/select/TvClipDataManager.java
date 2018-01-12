/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;

import java.util.List;
import java.util.Map;

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
        String[] columns = {JsonContents.META_RESPONSE_THUMB_448, JsonContents.META_RESPONSE_TITLE,
                JsonContents.META_RESPONSE_DISPLAY_START_DATE, JsonContents.META_RESPONSE_DISP_TYPE,
                JsonContents.META_RESPONSE_SEARCH_OK, JsonContents.META_RESPONSE_CRID,
                JsonContents.META_RESPONSE_SERVICE_ID, JsonContents.META_RESPONSE_EVENT_ID,
                JsonContents.META_RESPONSE_TITLE_ID, JsonContents.META_RESPONSE_R_VALUE,
                JsonContents.META_RESPONSE_AVAIL_START_DATE, JsonContents.META_RESPONSE_AVAIL_END_DATE,
                JsonContents.META_RESPONSE_DTV, JsonContents.META_RESPONSE_TV_SERVICE,
                JsonContents.META_RESPONSE_CONTENT_TYPE, JsonContents.META_RESPONSE_RATING,
                JsonContents.META_RESPONSE_DTV_TYPE};

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
