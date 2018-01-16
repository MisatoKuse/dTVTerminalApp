/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.activity.home.ClipListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RentalListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.WatchingVideoListActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.DailyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.WeeklyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.activity.video.VideoContentListActivity;
import com.nttdocomo.android.tvterminalapp.activity.video.VideoPurchListActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ClipKeyListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ClipKeyListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ClipKeyListWebClient;

import java.util.List;
import java.util.Map;


public class ClipKeyListDataProvider implements ClipKeyListWebClient.TvClipKeyListJsonParserCallback,
        ClipKeyListWebClient.VodClipKeyListJsonParserCallback, DbThread.DbOperation {
    private Context mContext;
    protected boolean mRequiredClipKeyList = false;
    protected boolean mResponseEndFlag = false;
    private static final String CLIP_KEY_LIST_TYPE_OTHER_CHANNEL = "h4d_iptv";

    private ClipRequestData mClipRequestData = null;

    private static final int CLIP_ROW_DELETE = 0;
    private static final int CLIP_ROW_INSERT = 1;

    @Override
    public void onTvClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        if (clipKeyListResponse != null) {
            if (clipKeyListResponse.getIsUpdate()) {
                DTVTLogger.debug("ClipKeyListResponse Insert DB");
                setStructDB(ClipKeyListDao.TABLE_TYPE.TV, clipKeyListResponse);
                mResponseEndFlag = true;
            } else {
                // DBから取得
                DTVTLogger.debug("ClipKeyListResponse Select DB");
                getClipKeyListDbData(ClipKeyListDao.TABLE_TYPE.TV, clipKeyListResponse);
                mResponseEndFlag = true;
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
                mResponseEndFlag = true;
            } else {
                // DBから取得
                DTVTLogger.debug("ClipKeyListResponse Select DB");
                getClipKeyListDbData(ClipKeyListDao.TABLE_TYPE.VOD, clipKeyListResponse);
                mResponseEndFlag = true;
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
            mRequiredClipKeyList = true;
        } else {
            mRequiredClipKeyList = false;
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
        mResponseEndFlag = false;
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
        if (context instanceof WeeklyTvRankingActivity
                || context instanceof DailyTvRankingActivity
                || context instanceof VideoRankingActivity
                || context instanceof ClipListActivity
                || context instanceof RentalListActivity
                || context instanceof WatchingVideoListActivity
                || context instanceof DtvContentsDetailActivity
                || context instanceof TvProgramListActivity
                || context instanceof VideoContentListActivity
                || context instanceof VideoPurchListActivity) {
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

    /**
     * コンテンツタイプ判定 TV or VOD or dTV
     *
     * @param dispType
     * @param contentType
     * @param dTv
     * @return
     */
    protected ClipKeyListDao.CONTENT_TYPE searchContentsType(String dispType, String contentType, String dTv) {
        // TODO DREM-767 QA回答により別BLにて判定処理を修正
        if (ClipKeyListDao.META_DISP_TYPE_TV_PROGRAM.equals(dispType)
                && contentType.isEmpty()) {
            return ClipKeyListDao.CONTENT_TYPE.TV;
        }
        if (!ClipKeyListDao.META_DISP_TYPE_TV_PROGRAM.equals(dispType)
                && ClipKeyListDao.META_DTV_FLAG_TRUE.equals(dTv)) {
            return ClipKeyListDao.CONTENT_TYPE.VOD;
        }
        if (ClipKeyListDao.META_DISP_TYPE_TV_PROGRAM.equals(dispType)
                && !contentType.isEmpty()
                && ClipKeyListDao.META_DTV_FLAG_TRUE.equals(dTv)) {
            return ClipKeyListDao.CONTENT_TYPE.VOD;
        }
        if (!ClipKeyListDao.META_DISP_TYPE_TV_PROGRAM.equals(dispType)
                && ClipKeyListDao.META_DTV_FLAG_FALSE.equals(dTv)) {
            return ClipKeyListDao.CONTENT_TYPE.DTV;
        }
        if (ClipKeyListDao.META_DISP_TYPE_TV_PROGRAM.equals(dispType)
                && !contentType.isEmpty()
                && ClipKeyListDao.META_DTV_FLAG_FALSE.equals(dTv)) {
            return ClipKeyListDao.CONTENT_TYPE.DTV;
        }
        return null;
    }

    /**
     * 使用テーブル判定 TV or VOD
     *
     * @param dispType
     * @param contentType
     * @return
     */
    protected ClipKeyListDao.TABLE_TYPE decisionTableType(String dispType, String contentType) {
        if (ClipKeyListDao.META_DISP_TYPE_TV_PROGRAM.equals(dispType)
                && contentType == null) {
            return ClipKeyListDao.TABLE_TYPE.TV;
        }
        return ClipKeyListDao.TABLE_TYPE.VOD;
    }

    /**
     * DB内に該当するCridのレコードが存在するかを判定
     *
     * @param type
     * @param crid
     * @return ListView表示用データ
     */
    protected boolean findDbVodClipKeyData(ClipKeyListDao.TABLE_TYPE type, String crid) {
        DTVTLogger.start();
        ClipKeyListDataManager dataManager = new ClipKeyListDataManager(mContext);
        List<Map<String, String>> clipKeyList = dataManager.selectClipKeyDbVodData(type, crid);
        DTVTLogger.end();
        return clipKeyList != null && clipKeyList.size() > 0;
    }

    /**
     * DB内に該当するServiceId,EventId,ContentsTypeのレコードが存在するかを判定
     *
     * @param tableType
     * @param serviceId
     * @param eventId
     * @param type
     * @return ListView表示用データ
     */
    protected boolean findDbTvClipKeyData(ClipKeyListDao.TABLE_TYPE tableType,
                                          String serviceId, String eventId, String type) {
        DTVTLogger.start();
        ClipKeyListDataManager dataManager = new ClipKeyListDataManager(mContext);
        List<Map<String, String>> clipKeyList = dataManager.selectClipKeyDbTvData(tableType, serviceId, eventId, type);
        DTVTLogger.end();

        return clipKeyList != null && clipKeyList.size() > 0;
    }

    /**
     * DB内に該当するTitleIdのレコードが存在するかを判定
     *
     * @param type
     * @param titleId
     * @return ListView表示用データ
     */
    protected boolean findDbDtvClipKeyData(ClipKeyListDao.TABLE_TYPE type, String titleId) {
        DTVTLogger.start();
        ClipKeyListDataManager dataManager = new ClipKeyListDataManager(mContext);
        List<Map<String, String>> clipKeyList = dataManager.selectClipKeyDbDtvData(type, titleId);
        DTVTLogger.end();
        return clipKeyList != null && clipKeyList.size() > 0;
    }

    public void clipResultDelete(ClipRequestData data) {
        mClipRequestData = data;
        //DB操作
        Handler handler = new Handler(); //チャンネル情報更新
        try {
            DbThread t = new DbThread(handler, this, CLIP_ROW_DELETE);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    public void clipResultInsert(ClipRequestData data) {
        mClipRequestData = data;
        //DB操作
        Handler handler = new Handler(); //チャンネル情報更新
        try {
            DbThread t = new DbThread(handler, this, CLIP_ROW_INSERT);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    @Override
    public void onDbOperationFinished(boolean isSuccessful, List<Map<String, String>> resultSet, int operationId) {
        //TODO:DB保存後の処理があればここに記載
    }

    @Override
    public List<Map<String, String>> dbOperation(int operationId) throws Exception {
        ClipKeyListInsertDataManager dataManager = new ClipKeyListInsertDataManager(mContext);
        String dispType = mClipRequestData.getDispType();
        String contentType = mClipRequestData.getContentType();
        String serviceId = mClipRequestData.getServiceId();
        String eventId = mClipRequestData.getEventId();
        String type = mClipRequestData.getType();
        String titleId = mClipRequestData.getTitleId();
        ClipKeyListDao.TABLE_TYPE tableType = decisionTableType(dispType, contentType);

        switch (operationId) {
            case CLIP_ROW_DELETE:
                dataManager.deleteRowSqlStart(tableType, serviceId, eventId, titleId);
                break;
            case CLIP_ROW_INSERT:
                dataManager.insertRowSqlStart(tableType, serviceId, eventId, type, titleId);
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * DBから取得したキー値を元にクリップ状態を判定する
     *
     * @param dispType
     * @param contentsType
     * @param dTv
     * @param crid
     * @param serviceId
     * @param eventId
     * @param titleId
     * @return
     */
    protected boolean getClipStatus(String dispType, String contentsType, String dTv,
                                    String crid, String serviceId, String eventId, String titleId) {
        DTVTLogger.start();
        boolean clipStatus = false;
        ClipKeyListDao.CONTENT_TYPE contentType = searchContentsType(dispType, contentsType, dTv);
        ClipKeyListDao.TABLE_TYPE tableType = decisionTableType(dispType, contentsType);
        switch (contentType) {
            case TV:
                clipStatus = findDbTvClipKeyData(tableType,
                        serviceId, eventId, CLIP_KEY_LIST_TYPE_OTHER_CHANNEL);
                break;
            case VOD:
                clipStatus = findDbVodClipKeyData(tableType, crid);
                break;
            case DTV:
                clipStatus = findDbDtvClipKeyData(tableType, titleId);
                break;
        }
        DTVTLogger.end();
        return clipStatus;
    }
}
