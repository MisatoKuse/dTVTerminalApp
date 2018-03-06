/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

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
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ClipKeyListWebClient;

import java.util.ArrayList;
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
     * クリップDBタイプ.
     */
    private ClipKeyListDao.TABLE_TYPE mTableType = null;
    /**
     * クリップレスポンス.
     */
    private ClipKeyListResponse mClipKeyListResponse = null;

    /**
     * クリップ削除種別用定数.
     */
    private static final int CLIP_ROW_DELETE = 0;
    /**
     * クリップ登録種別用定数.
     */
    private static final int CLIP_ROW_INSERT = 1;
    /**
     * クリップ登録種別用定数.
     */
    private static final int CLIP_ALL_INSERT = 2;
    /**
     * video_program.
     */
    private static final String META_DISPLAY_TYPE_VIDEO_PROGRAM = "video_program";
    /**
     * video_series.
     */
    private static final String META_DISPLAY_TYPE_VIDEO_SERIES = "video_series";
    /**
     * video_package.
     */
    private static final String META_DISPLAY_TYPE_VIDEO_PACKAGE = "video_package";
    /**
     * subscription_package.
     */
    private static final String META_DISPLAY_TYPE_SUBSCRIPTION_PACKAGE = "subscription_package";
    /**
     * series_svod.
     */
    private static final String META_DISPLAY_TYPE_SERIES_SVOD = "series_svod";
    /**
     * tv_serviceフラグ.
     */
    private static final String META_TV_SERVICE_FLAG_TRUE = "1";

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
        mTableType = type;
        mClipKeyListResponse = response;
        //DB操作
        Handler handler = new Handler(); //チャンネル情報更新
        try {
            DbThread t = new DbThread(handler, this, CLIP_ALL_INSERT);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
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
     * メソッド内の処理はコンテンツタイプの判定のみのためメンテナンス性に影響なし
     *
     * @param dispType    dispType
     * @param contentType contentType
     * @param dTv         dTvフラグ
     * @param tvService   tvServiceフラグ
     * @return コンテンツタイプ
     */
    @SuppressWarnings("OverlyComplexMethod")
    private ClipKeyListDao.CONTENT_TYPE searchContentsType(
            final String dispType, final String contentType, final String dTv, final String tvService) {
        //ぷららサーバ対応
        if (dispType != null && tvService != null) {
            if (ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                    && (contentType == null || contentType.isEmpty())) {
                return ClipKeyListDao.CONTENT_TYPE.TV;
            }
            //「disp_type」が tv_program 以外、 かつ「dtv」が0
            if (!ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                    && (dTv != null && ClipKeyListDao.META_DTV_FLAG_FALSE.equals(dTv))) {
                return ClipKeyListDao.CONTENT_TYPE.VOD;
            }
            //「disp_type」が tv_program で contents_typeあり、　かつ「dtv」が0
            if (ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                    && (contentType != null && !contentType.isEmpty())
                    && (dTv != null && ClipKeyListDao.META_DTV_FLAG_FALSE.equals(dTv))) {
                return ClipKeyListDao.CONTENT_TYPE.VOD;
            }
            //「disp_type」が tv_program でかつ「tv_service」が「1」
            if (ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                    && META_TV_SERVICE_FLAG_TRUE.equals(tvService)) {
                return ClipKeyListDao.CONTENT_TYPE.DTV;
            }
            //「disp_type」が video_program・video_series・video_package・subscription_package・series_svodのいずれか、 かつ「dtv」が0または未設定
            if (getVodStatus(dispType)
                    && (dTv == null || dTv.isEmpty() || ClipKeyListDao.META_DTV_FLAG_FALSE.equals(dTv))) {
                return ClipKeyListDao.CONTENT_TYPE.DTV;
            }
        }
        return null;
    }

    /**
     * VOD判定.
     *
     * @param dispType dispType
     * @return VOD判定結果
     */
    private boolean getVodStatus(final String dispType) {
        switch (dispType) {
            case META_DISPLAY_TYPE_VIDEO_PROGRAM:
            case META_DISPLAY_TYPE_VIDEO_SERIES:
            case META_DISPLAY_TYPE_VIDEO_PACKAGE:
            case META_DISPLAY_TYPE_SUBSCRIPTION_PACKAGE:
            case META_DISPLAY_TYPE_SERIES_SVOD:
                return true;
            default:
                return false;
        }
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
        if (dispType == null) {
            return null;
        }
        if (TextUtils.isEmpty(contentType)
                && ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)) {
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
        if (mClipRequestData != null) {
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
                    case CLIP_ALL_INSERT:
                        dataManager.insertClipKeyListInsert(mTableType, mClipKeyListResponse);
                        break;
                    default:
                        break;
                }
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
     * @param tvService    tvService
     * @return クリップ状態
     */
    protected boolean getClipStatus(
            final String dispType, final String contentsType, final String dTv, final String crid,
            final String serviceId, final String eventId, final String titleId, final String tvService) {
        DTVTLogger.start();
        boolean clipStatus = false;
        ClipKeyListDao.CONTENT_TYPE contentType = searchContentsType(dispType, contentsType, dTv, tvService);
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
     * ClipStatus チェック.
     *
     * @param channelInfoList コンテンツデータリスト
     * @return Status変更済みコンテンツデータリスト
     */
    public List<ChannelInfo> checkTvProgramClipStatus(final List<ChannelInfo> channelInfoList) {
        DTVTLogger.start();
        List<ChannelInfo> resultList = channelInfoList;

        //Nullチェック
        if (resultList == null || resultList.size() < 1) {
            return resultList;
        }

        for (int i = 0; i < channelInfoList.size(); i++) {
            ArrayList<ScheduleInfo> scheduleInfoList = channelInfoList.get(i).getSchedules();
            ArrayList<ScheduleInfo> scheduleInfoArrayList = new ArrayList<>();
            for (int j = 0; j < scheduleInfoList.size(); j++) {
                ScheduleInfo scheduleInfo = scheduleInfoList.get(j);
                //使用データ抽出
                String dispType = scheduleInfo.getDispType();
                String contentsType = scheduleInfo.getContentType();
                String dTv = scheduleInfo.getDtv();
                String tvService = scheduleInfo.getTvService();
                String serviceId = scheduleInfo.getServiceId();
                String eventId = scheduleInfo.getEventId();
                String crid = scheduleInfo.getCrId();
                String titleId = scheduleInfo.getTitleId();

                //判定はgetClipStatusに一任
                scheduleInfo.setClipStatus(getClipStatus(dispType, contentsType, dTv, crid, serviceId, eventId, titleId, tvService));
                scheduleInfoArrayList.add(scheduleInfo);
            }
            resultList.get(i).setSchedules(scheduleInfoArrayList);
        }
        DTVTLogger.end();
        return resultList;
    }

    /**
     * ClipStatus チェック.
     *
     * @param contentsDetailData コンテンツデータリスト
     * @return Status変更済みコンテンツデータリスト
     */
    public OtherContentsDetailData checkClipStatus(final OtherContentsDetailData contentsDetailData) {
        DTVTLogger.start();
        List<ContentsData> list = new ArrayList<>();

        OtherContentsDetailData otherContentsDetailData = new OtherContentsDetailData();
        //Nullチェック
        if (contentsDetailData == null) {
            return otherContentsDetailData;
        }

        //使用データ抽出
        String dispType = otherContentsDetailData.getDispType();
        String contentsType = otherContentsDetailData.getContentsType();
        String dTv = otherContentsDetailData.getDtv();
        String tvService = otherContentsDetailData.getTvService();
        String serviceId = String.valueOf(otherContentsDetailData.getServiceId());
        String eventId = otherContentsDetailData.getEventId();
        String crid = otherContentsDetailData.getCrId();
        String titleId = otherContentsDetailData.getTitleId();

        //判定はgetClipStatusに一任
        otherContentsDetailData.setClipStatus(getClipStatus(dispType, contentsType, dTv, crid, serviceId, eventId, titleId, tvService));
        DTVTLogger.end();
        return otherContentsDetailData;
    }

    /**
     * ClipStatus チェック.
     *
     * @param contentsDataList コンテンツデータリスト
     * @return Status変更済みコンテンツデータリスト
     */
    public List<ContentsData> checkClipStatus(final List<ContentsData> contentsDataList) {
        DTVTLogger.start();
        List<ContentsData> list = new ArrayList<>();

        //Nullチェック
        if (contentsDataList == null || contentsDataList.size() < 1) {
            return contentsDataList;
        }

        for (int i = 0; i < contentsDataList.size(); i++) {
            ContentsData contentsData = contentsDataList.get(i);
            //使用データ抽出
            String dispType = contentsData.getDispType();
            String contentsType = contentsData.getContentsType();
            String dTv = contentsData.getDtv();
            String tvService = contentsData.getTvService();
            String serviceId = contentsData.getServiceId();
            String eventId = contentsData.getEventId();
            String crid = contentsData.getCrid();
            String titleId = contentsData.getTitleId();

            //判定はgetClipStatusに一任
            contentsData.setClipStatus(getClipStatus(dispType, contentsType, dTv, crid, serviceId, eventId, titleId, tvService));
            list.add(contentsData);
        }
        DTVTLogger.end();
        return list;
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
