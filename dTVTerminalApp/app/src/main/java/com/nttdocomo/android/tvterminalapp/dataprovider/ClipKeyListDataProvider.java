/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.activity.launch.LaunchActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.WeeklyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ClipKeyListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ClipKeyListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ClipKeyListWebClient;


public class ClipKeyListDataProvider implements ClipKeyListWebClient.TvClipKeyListJsonParserCallback, ClipKeyListWebClient.VodClipKeyListJsonParserCallback {
    private Context mContext;
    protected boolean requiredClipKeyList = false;
    protected ClipKeyListResponse mResponse = null;

    @Override
    public void onTvClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        if (clipKeyListResponse != null) {
            if (clipKeyListResponse.getIsUpdate()) {
                DTVTLogger.debug("ClipKeyListResponse Insert DB");
                setStructDB(ClipKeyListDao.TABLE_TYPE.TV, clipKeyListResponse);
                mResponse = clipKeyListResponse;
            } else {
                // DBから取得
                DTVTLogger.debug("ClipKeyListResponse Select DB");
                getClipKeyListDbData(ClipKeyListDao.TABLE_TYPE.TV, clipKeyListResponse);
                mResponse = clipKeyListResponse;
            }
        } else {
            // TODO パラメータエラー時の処理を記載
        }
        DTVTLogger.end();
    }

    @Override
    public void onVodClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        if (clipKeyListResponse != null) {
            if (clipKeyListResponse.getIsUpdate()) {
                DTVTLogger.debug("ClipKeyListResponse Insert DB");
                setStructDB(ClipKeyListDao.TABLE_TYPE.VOD, clipKeyListResponse);
                mResponse = clipKeyListResponse;
            } else {
                // DBから取得
                DTVTLogger.debug("ClipKeyListResponse Select DB");
                getClipKeyListDbData(ClipKeyListDao.TABLE_TYPE.VOD, clipKeyListResponse);
                mResponse = clipKeyListResponse;
            }
        } else {
            // TODO パラメータエラー時の処理を記載
        }
        DTVTLogger.end();
    }


    /**
     * コンストラクタ
     *
     * @param context
     */
    public ClipKeyListDataProvider(Context context) {
        this.mContext = context;
        if (checkInstance(context)) {
            requiredClipKeyList = true;
        }
    }

    /**
     * データ取得要求受付(tv|vod)
     */
    public void getClipKeyList() {
        DTVTLogger.start();
        // TVクリップキー一覧を取得
        getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.TV));
        // VODクリップキー一覧を取得
        getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
        DTVTLogger.end();
    }

    /**
     * データ取得要求受付
     * コンテンツを指定する場合は呼び出す前にrequestに設定しておくこと
     */
    public void getClipKeyList(ClipKeyListRequest request) {
        DTVTLogger.start();
        request.setIsForce(isCachingClipKeyListRecord(request.getType()));
        ClipKeyListWebClient client = new ClipKeyListWebClient();
        // リクエストによってコールバックを変える
        if (ClipKeyListRequest.CLIP_KEY_LIST_REQUEST_TYPE_TV.equals(request.getType())) {
            client.getClipKeyListApi(request, this, null);
        } else {
            client.getClipKeyListApi(request, null, this);
        }
        DTVTLogger.end();
    }

    /**
     * DB内のClipKeyListを抽出する
     *
     * @param response ClipKeyListレスポンスデータ
     * @return ListView表示用データ
     */
    private ClipKeyListResponse getClipKeyListDbData(ClipKeyListDao.TABLE_TYPE type, ClipKeyListResponse response) {
        DTVTLogger.start();
        ClipKeyListDataManager dataManager = new ClipKeyListDataManager(mContext);
        response.setCkList(dataManager.selectListData(type));
        DTVTLogger.end();
        return response;
    }

    /**
     * クリップキー一覧データをDBに格納する
     */
    private void setStructDB(ClipKeyListDao.TABLE_TYPE type, ClipKeyListResponse response) {
        DTVTLogger.start();
        ClipKeyListInsertDataManager dataManager = new ClipKeyListInsertDataManager(mContext);
        dataManager.insertClipKeyListInsert(type, response);
        DTVTLogger.end();
    }

    /**
     * クリップキー一覧取得を実行するActivityかを判定
     */
    private boolean checkInstance(Context context) {
        if (context instanceof LaunchActivity
                || context instanceof WeeklyTvRankingActivity) {
            DTVTLogger.debug("Need Getting ClipKeyList");
            return true;
        }
        DTVTLogger.debug("Not Required ClipKeyList");
        return false;
    }

    /**
     * クリップキー一覧キャッシュ中判定
     *
     * @param type
     * @return
     */
    private boolean isCachingClipKeyListRecord(String type) {
        DTVTLogger.start();
        String tableName = null;
        // キャッシュを確認するDBのテーブル名を取得
        if (ClipKeyListRequest.CLIP_KEY_LIST_REQUEST_TYPE_TV.equals(type)) {
            tableName = DBConstants.TV_CLIP_KEY_LIST_TABLE_NAME;
        } else if (ClipKeyListRequest.CLIP_KEY_LIST_REQUEST_TYPE_VOD.equals(type)) {
            tableName = DBConstants.VOD_CLIP_KEY_LIST_TABLE_NAME;
        }

        DTVTLogger.end();
        return DBUtils.isCachingRecord(mContext, tableName);
    }
}
