/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.activity.home.ClipListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RentalListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.WatchingVideoListActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.DailyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.WeeklyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.tvprogram.TvProgramListActivity;
import com.nttdocomo.android.tvterminalapp.activity.video.VideoContentListActivity;
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

/**
 * クリップキー取得管理.
 */
public class ClipKeyListDataProvider implements ClipKeyListWebClient.TvClipKeyListJsonParserCallback,
        ClipKeyListWebClient.VodClipKeyListJsonParserCallback, DbThread.DbOperation {
    /**
     * コンテキストファイル.
     */
    private Context mContext;
    /**
     * クリップキーリスト.
     */
    protected boolean mRequiredClipKeyList = false;
    /**
     * レスポンス終了フラグ.
     */
    boolean mResponseEndFlag = false;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * クリップ種別(ひかりTV).
     */
    public static final String CLIP_KEY_LIST_TYPE_OTHER_CHANNEL = "h4d_iptv";

    /**
     * クリップリクエスト用データ.
     */
    private ClipRequestData mClipRequestData = null;
    /**
     * クリップリクエスト用Webクライアント.
     */
    private ClipKeyListWebClient mClient = null;

    /**
     * クリップ削除種別用定数.
     */
    private static final int CLIP_ROW_DELETE = 0;
    /**
     * クリップ登録種別用定数.
     */
    private static final int CLIP_ROW_INSERT = 1;

    @Override
    public void onTvClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        mResponseEndFlag = true;
        if (clipKeyListResponse != null && clipKeyListResponse.getIsUpdate()) {
            DTVTLogger.debug("ClipKeyListResponse Insert DB");
            setStructDB(ClipKeyListDao.TABLE_TYPE.TV, clipKeyListResponse);
        }
        DTVTLogger.end();
    }

    @Override
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        mResponseEndFlag = true;
        if (clipKeyListResponse != null && clipKeyListResponse.getIsUpdate()) {
            DTVTLogger.debug("ClipKeyListResponse Insert DB");
            setStructDB(ClipKeyListDao.TABLE_TYPE.VOD, clipKeyListResponse);
        }
        DTVTLogger.end();
    }


    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public ClipKeyListDataProvider(final Context context) {
        this.mContext = context;
        mRequiredClipKeyList = checkInstance(context);
    }

    /**
     * データ取得要求受付(tv|vod).
     */
    public void getClipKeyList() {
        DTVTLogger.start();
        if (!mIsCancel) {
            // TVクリップキー一覧を取得
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.TV));
            // VODクリップキー一覧を取得
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
        } else {
            DTVTLogger.error("ClipKeyListDataProvider is stopping connection");
        }
        DTVTLogger.end();
    }

    /**
     * データ取得要求受付
     * コンテンツを指定する場合は呼び出す前にrequestに設定しておくこと.
     *
     * @param request リクエストパラメータ
     */
    public void getClipKeyList(final ClipKeyListRequest request) {
        DTVTLogger.start();
        if (!mIsCancel) {
            mResponseEndFlag = false;
            request.setIsForce(isCachingClipKeyListRecord(request.getType()));
            mClient = new ClipKeyListWebClient(mContext);
            // リクエストによってコールバックを変える
            if (ClipKeyListRequest.CLIP_KEY_LIST_REQUEST_TYPE_TV.equals(request.getType())) {
                mClient.getClipKeyListApi(request, this, null);
            } else {
                mClient.getClipKeyListApi(request, null, this);
            }
        } else {
            DTVTLogger.error("ClipKeyListDataProvider stop connection");
        }
        DTVTLogger.end();
    }

    /**
     * クリップキー一覧データをDBに格納する.
     *
     * @param type     テーブル種別
     * @param response ClipKeyListレスポンスデータ
     */
    private void setStructDB(final ClipKeyListDao.TABLE_TYPE type, final ClipKeyListResponse response) {
        DTVTLogger.start();
        ClipKeyListInsertDataManager dataManager = new ClipKeyListInsertDataManager(mContext);
        dataManager.insertClipKeyListInsert(type, response);
        DTVTLogger.end();
    }

    /**
     * クリップキー一覧取得を実行するActivityかを判定.
     *
     * @param context コンテキスト
     * @return Activityフラグ
     */
    private boolean checkInstance(final Context context) {
        if (context instanceof WeeklyTvRankingActivity
                || context instanceof DailyTvRankingActivity
                || context instanceof VideoRankingActivity
                || context instanceof HomeActivity
                || context instanceof ClipListActivity
                || context instanceof RentalListActivity
                || context instanceof WatchingVideoListActivity
                || context instanceof ContentDetailActivity
                || context instanceof TvProgramListActivity
                || context instanceof VideoContentListActivity) {
            DTVTLogger.debug("Need Getting ClipKeyList");
            return true;
        }
        DTVTLogger.debug("Not Required ClipKeyList");
        return false;
    }

    /**
     * クリップキー一覧キャッシュ中判定.
     *
     * @param type クリップコンテンツ種別
     * @return DB保存フラグ
     */
    private boolean isCachingClipKeyListRecord(final String type) {
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
     * コンテンツタイプ判定 TV or VOD or dTV.
     *
     * @param dispType    dispType
     * @param contentType contentType
     * @param dTv         dTvフラグ
     * @return コンテンツタイプ
     */
    private ClipKeyListDao.CONTENT_TYPE searchContentsType(
            final String dispType, final String contentType, final String dTv) {
        //ぷららサーバ対応
        if (dispType != null && contentType != null && dTv != null) {
            // TODO DREM-767 QA回答により別BLにて判定処理を修正
            if (ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                    && contentType.isEmpty()) {
                return ClipKeyListDao.CONTENT_TYPE.TV;
            }
            if (!ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                    && ClipKeyListDao.META_DTV_FLAG_TRUE.equals(dTv)) {
                return ClipKeyListDao.CONTENT_TYPE.VOD;
            }
            if (ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                    && !contentType.isEmpty()
                    && ClipKeyListDao.META_DTV_FLAG_TRUE.equals(dTv)) {
                return ClipKeyListDao.CONTENT_TYPE.VOD;
            }
            if (!ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                    && ClipKeyListDao.META_DTV_FLAG_FALSE.equals(dTv)) {
                return ClipKeyListDao.CONTENT_TYPE.DTV;
            }
            if (ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                    && !contentType.isEmpty()
                    && ClipKeyListDao.META_DTV_FLAG_FALSE.equals(dTv)) {
                return ClipKeyListDao.CONTENT_TYPE.DTV;
            }
        }
        return null;
    }

    /**
     * 使用テーブル判定 TV or VOD.
     *
     * @param dispType    dispType
     * @param contentType contentType
     * @return テーブル種別
     */
    ClipKeyListDao.TABLE_TYPE decisionTableType(
            final String dispType, final String contentType) {
        if (dispType == null || contentType == null) {
            return null;
        }
        if (ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                && contentType.length() < 1) {
            return ClipKeyListDao.TABLE_TYPE.TV;
        }
        return ClipKeyListDao.TABLE_TYPE.VOD;
    }

    /**
     * DB内に該当するCridのレコードが存在するかを判定.
     *
     * @param type tテーブル種別
     * @param crid crid
     * @return ListView表示用データ
     */
    private boolean findDbVodClipKeyData(
            final ClipKeyListDao.TABLE_TYPE type, final String crid) {
        DTVTLogger.start();
        ClipKeyListDataManager dataManager = new ClipKeyListDataManager(mContext);
        List<Map<String, String>> clipKeyList = dataManager.selectClipKeyDbVodData(type, crid);
        DTVTLogger.end();
        return clipKeyList != null && clipKeyList.size() > 0;
    }

    /**
     * DB内に該当するServiceId,EventId,ContentsTypeのレコードが存在するかを判定.
     *
     * @param tableType tableType
     * @param serviceId serviceId
     * @param eventId   eventId
     * @param type      type
     * @return ListView表示用データ
     */
    private boolean findDbTvClipKeyData(
            final ClipKeyListDao.TABLE_TYPE tableType, final String serviceId,
            final String eventId, final String type) {
        DTVTLogger.start();
        ClipKeyListDataManager dataManager = new ClipKeyListDataManager(mContext);
        List<Map<String, String>> clipKeyList = dataManager.selectClipKeyDbTvData(tableType, serviceId, eventId, type);
        DTVTLogger.end();

        return clipKeyList != null && clipKeyList.size() > 0;
    }

    /**
     * DB内に該当するTitleIdのレコードが存在するかを判定.
     *
     * @param type    tableType
     * @param titleId titleId
     * @return ListView表示用データ
     */
    private boolean findDbDtvClipKeyData(final ClipKeyListDao.TABLE_TYPE type, final String titleId) {
        DTVTLogger.start();
        ClipKeyListDataManager dataManager = new ClipKeyListDataManager(mContext);
        List<Map<String, String>> clipKeyList = dataManager.selectClipKeyDbDtvData(type, titleId);
        DTVTLogger.end();
        return clipKeyList != null && clipKeyList.size() > 0;
    }

    /**
     * クリップ削除.
     *
     * @param data リクエストデータ
     */
    public void clipResultDelete(final ClipRequestData data) {
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

    /**
     * クリップ追加.
     *
     * @param data リクエストデータ
     */
    public void clipResultInsert(final ClipRequestData data) {
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
    public void onDbOperationFinished(final boolean isSuccessful, final List<Map<String, String>> resultSet, final int operationId) {
        //TODO:DB保存後の処理があればここに記載
    }

    @Override
    public List<Map<String, String>> dbOperation(final int operationId) {
        ClipKeyListInsertDataManager dataManager = new ClipKeyListInsertDataManager(mContext);
        String crid = mClipRequestData.getCrid();
        String dispType = mClipRequestData.getDispType();
        String contentType = mClipRequestData.getContentType();
        String serviceId = mClipRequestData.getServiceId();
        String eventId = mClipRequestData.getEventId();
        String titleId = mClipRequestData.getTitleId();
        ClipKeyListDao.TABLE_TYPE tableType = decisionTableType(dispType, contentType);

        if (tableType != null) {
            switch (operationId) {
                case CLIP_ROW_DELETE:
                    dataManager.deleteRowSqlStart(tableType, crid, serviceId, eventId, titleId);
                    break;
                case CLIP_ROW_INSERT:
                    dataManager.insertRowSqlStart(tableType, crid, serviceId, eventId, titleId);
                    break;
                default:
                    break;
            }
        }
        return null;
    }

    /**
     * DBから取得したキー値を元にクリップ状態を判定する.
     *
     * @param dispType     dispType
     * @param contentsType contentsType
     * @param dTv          dTvフラグ
     * @param crid         crid
     * @param serviceId    serviceId
     * @param eventId      eventId
     * @param titleId      titleId
     * @return クリップ状態
     */
    protected boolean getClipStatus(final String dispType, final String contentsType, final String dTv,
                                    final String crid, final String serviceId, final String eventId, final String titleId) {
        DTVTLogger.start();
        boolean clipStatus = false;
        ClipKeyListDao.CONTENT_TYPE contentType = searchContentsType(dispType, contentsType, dTv);
        ClipKeyListDao.TABLE_TYPE tableType = decisionTableType(dispType, contentsType);
        if (contentType != null && tableType != null) {
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
        } else {
            DTVTLogger.debug("contentType is null");
        }
        DTVTLogger.end();
        return clipStatus;
    }

    /**
     * 通信を止める.
     */
    void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        if (mClient != null) {
            mClient.stopConnection();
        }
    }

    /**
     * 通信可能状態にする.
     */
    void enableConnection() {
        DTVTLogger.start();
        mIsCancel = false;
        if (mClient != null) {
            mClient.enableConnection();
        }
    }
}
