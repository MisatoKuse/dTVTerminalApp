/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.activity.common.ChildContentListActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.ClipListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.RentalListActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.WatchingVideoListActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.DailyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.WeeklyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.video.VideoContentListActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DataBaseThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ClipKeyListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ClipKeyListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ClipKeyListWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * クリップキー取得管理.
 */
public class ClipKeyListDataProvider implements ClipKeyListWebClient.TvClipKeyListJsonParserCallback,
        ClipKeyListWebClient.VodClipKeyListJsonParserCallback, DataBaseThread.DataBaseOperation {
    /**
     * コンテキストファイル.
     */
    Context mContext;
    /**
     * クリップキーリスト.
     */
    boolean mRequiredClipKeyList = false;
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
    private ClipKeyListDao.TableTypeEnum mTableType = null;
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
     * コンテンツ種別判定1.
     */
    private static final String CONTENTS_TYPE_FLAG_ONE = "1";
    /**
     * コンテンツ種別判定2.
     */
    private static final String CONTENTS_TYPE_FLAG_TWO = "2";

    @Override
    public void onTvClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        mResponseEndFlag = true;
        if (clipKeyListResponse != null && clipKeyListResponse.getIsUpdate()) {
            DTVTLogger.debug("ClipKeyListResponse Insert DB");
            setStructDataBase(ClipKeyListDao.TableTypeEnum.TV, clipKeyListResponse);
        }
        DTVTLogger.end();
    }

    @Override
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        mResponseEndFlag = true;
        if (clipKeyListResponse != null && clipKeyListResponse.getIsUpdate()) {
            DTVTLogger.debug("ClipKeyListResponse Insert DB");
            setStructDataBase(ClipKeyListDao.TableTypeEnum.VOD, clipKeyListResponse);
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
        DTVTLogger.debug("research tv program display speed getClipKeyList");
        if (!mIsCancel) {
            // TVクリップキー一覧を取得
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.TV));
            // VODクリップキー一覧を取得
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.VOD));
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
    void getClipKeyList(final ClipKeyListRequest request) {
        DTVTLogger.start();
        DTVTLogger.debug("research tv program display speed getClipKeyList");
        if (!mIsCancel) {
            mResponseEndFlag = false;
            request.setIsForce(!isCachingClipKeyListRecord(request.getType()));
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
    private void setStructDataBase(final ClipKeyListDao.TableTypeEnum type, final ClipKeyListResponse response) {
        DTVTLogger.start();
        mTableType = type;
        mClipKeyListResponse = response;
        //DB操作
        Handler handler = new Handler(); //チャンネル情報更新
        try {
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, CLIP_ALL_INSERT);
            dataBaseThread.start();
        } catch (RuntimeException e) {
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
                || context instanceof VideoContentListActivity
                || context instanceof ChildContentListActivity) {
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
        switch (type) {
            case ClipKeyListRequest.CLIP_KEY_LIST_REQUEST_TYPE_TV:
                tableName = DataBaseConstants.TV_CLIP_KEY_LIST_TABLE_NAME;
                break;
            case ClipKeyListRequest.CLIP_KEY_LIST_REQUEST_TYPE_VOD:
                tableName = DataBaseConstants.VOD_CLIP_KEY_LIST_TABLE_NAME;
                break;
            default:
                break;
        }
        DTVTLogger.end();
        return DataBaseUtils.isCachingRecord(mContext, tableName);
    }

    /**
     * コンテンツタイプ判定 TV or VOD or dTV.
     * メソッド内の処理はコンテンツタイプの判定のみのためメンテナンス性に影響なし
     *
     * @param dispType    dispType
     * @param dTv         dTvフラグ
     * @param tvService   tvServiceフラグ
     * @return コンテンツタイプ
     */
    @SuppressWarnings("OverlyComplexMethod")
    public static ClipKeyListDao.ContentTypeEnum searchContentsType(
            final String dispType, final String dTv, final String tvService) {
        //ぷららサーバ対応
        if (dispType != null) {
            //「disp_type」が tv_program でかつ「tv_service」が「1」となります。
            if (ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                    && tvService != null && CONTENTS_TYPE_FLAG_ONE.equals(tvService)) {
                return ClipKeyListDao.ContentTypeEnum.TV;
            }
            //「tv_program」かつ「tv_service」が「2」
            if (ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)
                    && tvService != null && CONTENTS_TYPE_FLAG_TWO.equals(tvService)) {
                return ClipKeyListDao.ContentTypeEnum.VOD;
            }
            //「disp_type」が video_program・video_series・video_package・subscription_package・series_svodのいずれか、 かつ「dtv」が0または未設定
            if (getVodStatus(dispType)
                    && (dTv == null || dTv.isEmpty() || ClipKeyListDao.META_DTV_FLAG_FALSE.equals(dTv))) {
                return ClipKeyListDao.ContentTypeEnum.VOD;
            }
            //「disp_type」が video_program・video_series・video_package・subscription_package・series_svodのいずれか、 かつ「dtv」が1
            if (getVodStatus(dispType)
                    && (dTv != null || CONTENTS_TYPE_FLAG_ONE.equals(dTv))) {
                return ClipKeyListDao.ContentTypeEnum.DTV;
            }
            //「video_program・video_series・video_package・subscription_package・series_svodのいずれか」
            if (getVodStatus(dispType)) {
                return ClipKeyListDao.ContentTypeEnum.VOD;
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
    private static boolean getVodStatus(final String dispType) {
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
    ClipKeyListDao.TableTypeEnum decisionTableType(
            final String dispType, final String contentType) {
        if (dispType == null) {
            return null;
        }
        if ((contentType == null || contentType.isEmpty())
                && ClipKeyListDao.META_DISPLAY_TYPE_TV_PROGRAM.equals(dispType)) {
            return ClipKeyListDao.TableTypeEnum.TV;
        }
        return ClipKeyListDao.TableTypeEnum.VOD;
    }

    /**
     * DB内に該当するCridのレコードが存在するかを判定.
     *
     * @param type tテーブル種別
     * @param crid crid
     * @return ListView表示用データ
     */
    private boolean findDbVodClipKeyData(
            final ClipKeyListDao.TableTypeEnum type, final String crid) {
//        DTVTLogger.start();//必要な時にコメントを解除して使用
        ClipKeyListDataManager dataManager = new ClipKeyListDataManager(mContext);
        List<Map<String, String>> clipKeyList = dataManager.selectClipKeyDbVodData(type, crid);
//        DTVTLogger.end();//必要な時にコメントを解除して使用
        return clipKeyList != null && clipKeyList.size() > 0;
    }

    /**
     * DB内に該当するServiceId,EventId,ContentsTypeのレコードが存在するかを判定.
     *
     * @param tableType tableType
     * @param serviceId serviceId
     * @param eventId   eventId
     * @return ListView表示用データ
     */
    private boolean findDbTvClipKeyData(
            final ClipKeyListDao.TableTypeEnum tableType, final String serviceId,
            final String eventId) {
//        DTVTLogger.start();//必要な時にコメントを解除して使用
        ClipKeyListDataManager dataManager = new ClipKeyListDataManager(mContext);
        List<Map<String, String>> clipKeyList = dataManager.selectClipKeyDbTvData(tableType, serviceId, eventId);
//        DTVTLogger.end();//必要な時にコメントを解除して使用

        return clipKeyList != null && clipKeyList.size() > 0;
    }

    /**
     * DB内に該当するTitleIdのレコードが存在するかを判定.
     *
     * @param type    tableType
     * @param titleId titleId
     * @return ListView表示用データ
     */
    private boolean findDbDtvClipKeyData(final ClipKeyListDao.TableTypeEnum type, final String titleId) {
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
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, CLIP_ROW_DELETE);
            dataBaseThread.start();
        } catch (RuntimeException e) {
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
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, CLIP_ROW_INSERT);
            dataBaseThread.start();
        } catch (RuntimeException e) {
            DTVTLogger.debug(e);
        }
    }

    @Override
    public void onDbOperationFinished(final boolean isSuccessful, final List<Map<String, String>> resultSet, final int operationId) {
        //TODO :DB保存後の処理があればここに記載
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
            ClipKeyListDao.TableTypeEnum tableType = decisionTableType(dispType, contentType);

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
        } else if (mClipKeyListResponse != null) {
            switch (operationId) {
                case CLIP_ALL_INSERT:
                    dataManager.insertClipKeyListInsert(mTableType, mClipKeyListResponse);
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
     * @param tvService    tvService
     * @return クリップ状態
     */
    boolean getClipStatus(
            final String dispType, final String contentsType, final String dTv, final String crid,
            final String serviceId, final String eventId, final String titleId, final String tvService) {
        boolean clipStatus = false;
        ClipKeyListDao.ContentTypeEnum contentType = searchContentsType(dispType, dTv, tvService);
        ClipKeyListDao.TableTypeEnum tableType = decisionTableType(dispType, contentsType);
        if (contentType != null && tableType != null) {
            switch (contentType) {
                case TV:
                    clipStatus = findDbTvClipKeyData(tableType, serviceId, eventId);
                    break;
                case VOD:
                    clipStatus = findDbVodClipKeyData(tableType, crid);
                    break;
                case DTV:
                    clipStatus = findDbDtvClipKeyData(tableType, titleId);
                    break;
                default:
                    break;
            }
        } else {
            // TODO : 大量のログが発生するため必要な場合のみ解除して使ってください
            DTVTLogger.debug("contentType is null");
        }
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
        ClipKeyListDataManager keyListDataManager = new ClipKeyListDataManager(mContext);
        List<Map<String, String>> mapList = keyListDataManager.selectClipAllList();

        for (int i = 0; i < channelInfoList.size(); i++) {
            ArrayList<ScheduleInfo> scheduleInfoList = channelInfoList.get(i).getSchedules();
            ArrayList<ScheduleInfo> scheduleInfoArrayList = new ArrayList<>();
            if (scheduleInfoList != null) {
                scheduleInfoArrayList = new ArrayList<>();
                for (int j = 0; j < scheduleInfoList.size(); j++) {
                    ScheduleInfo scheduleInfo = scheduleInfoList.get(j);

                    //判定はgetClipStatusに一任
                    scheduleInfo.setClipStatus(ClipUtils.setClipStatusFromMap(scheduleInfo, mapList));
                    scheduleInfoArrayList.add(scheduleInfo);
                }
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

        OtherContentsDetailData otherContentsDetailData = contentsDetailData;
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
     * 取得したリストマップをContentsDataクラスへ入れる.
     *
     * @param contentMapList コンテンツリストデータ
     * @param channels チャンネル情報
     * @param isVodClipData クリップ(ビデオ)フラグ
     * @return ListView表示用データ
     */
    @SuppressWarnings("OverlyLongMethod")
    List<ContentsData> setContentData(final List<Map<String, String>> contentMapList, final ArrayList<ChannelInfo> channels, final boolean isVodClipData) {
        List<ContentsData> contentsDataList = new ArrayList<>();

        ContentsData contentInfo;

        for (int i = 0; i < contentMapList.size(); i++) {
            contentInfo = new ContentsData();

            Map<String, String> map = contentMapList.get(i);
            String title = map.get(JsonConstants.META_RESPONSE_TITLE);
            String searchOk = map.get(JsonConstants.META_RESPONSE_SEARCH_OK);
            String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
            String dtv = map.get(JsonConstants.META_RESPONSE_DTV);
            String dtvType = map.get(JsonConstants.META_RESPONSE_DTV_TYPE);
            String crId = map.get(JsonConstants.META_RESPONSE_CRID);
            String contentsType = map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE);
            String tvService = map.get(JsonConstants.META_RESPONSE_TV_SERVICE);

            contentInfo.setRank(String.valueOf(i + 1));
            if (ContentUtils.IS_DTV_FLAG.equals(dtv)) {
                contentInfo.setThumURL(map.get(JsonConstants.META_RESPONSE_DTV_THUMB_448));
                contentInfo.setThumDetailURL(map.get(JsonConstants.META_RESPONSE_DTV_THUMB_640));
            } else {
                contentInfo.setThumURL(map.get(JsonConstants.META_RESPONSE_THUMB_448));
                contentInfo.setThumDetailURL(map.get(JsonConstants.META_RESPONSE_THUMB_640));
            }
            contentInfo.setTitle(title);
            contentInfo.setSearchOk(searchOk);
            contentInfo.setRatStar(map.get(JsonConstants.META_RESPONSE_RATING));
            contentInfo.setTvService(tvService);
            contentInfo.setContentsType(map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
            contentInfo.setDtv(dtv);
            contentInfo.setDtvType(dtvType);
            contentInfo.setDispType(dispType);
            setResponseId(contentInfo, map);
            contentInfo.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
            contentInfo.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
            contentInfo.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
            contentInfo.setContentsId(crId);
            contentInfo.setCrid(crId);
            contentInfo.setContentsType(contentsType);
            contentInfo.setTitleId(map.get(JsonConstants.META_RESPONSE_TITLE_ID));
            contentInfo.setPublishStartDate(String.valueOf(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_PUBLISH_START_DATE))));
            contentInfo.setAvailStartDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE)));
            contentInfo.setAvailEndDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE)));
            contentInfo.setVodStartDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_VOD_START_DATE)));
            contentInfo.setVodEndDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_VOD_END_DATE)));
            setChannelInfo(map, contentInfo, channels);
            //クリップリクエストデータ作成
            ClipRequestData requestData = new ClipRequestData();
            setClipRequestData(map, requestData, title, searchOk);
            //視聴通知判定生成
            String dTv = map.get(JsonConstants.META_RESPONSE_DTV);
            requestData.setIsNotify(dispType, contentsType, contentInfo.getVodStartDate(), tvService, dTv);
            setRequestType(requestData, dispType, contentsType);
            contentInfo.setRequestData(requestData);
            DTVTLogger.debug("RankingContentInfo " + contentInfo.getRank());
            if (mRequiredClipKeyList) {
                // クリップ状態をコンテンツリストに格納
                contentInfo.setClipStatus(getClipStatus(dispType, contentsType, dTv,
                        requestData.getCrid(), requestData.getServiceId(),
                        requestData.getEventId(), requestData.getTitleId(), tvService));
            }
            if (isVodClipData && dispType == null) {
                //DREM-1882 期限切れコンテンツのクリップ対応により dispType==nullなら一律クリップ可なのでフラグを立てる
                //対象はクリップ一覧(ビデオ)のみ
                contentInfo.setIsAfterLimitContents(true);
                //期限切れコンテンツの評価値とサムネイルURLを再設定する
                contentInfo.setThumURL("");
                contentInfo.setRatStar("");
                contentInfo.setClipExec(true);
                requestData.setIsAfterLimitContents(true);
            }
            //生成した contentInfo をリストに格納する
            contentsDataList.add(contentInfo);
        }

        return contentsDataList;
    }

    /**
     * クリップリクエストデータを設定する.
     * @param map コンテンツ一覧用マップ
     * @param requestData クリップリクエストデータ
     * @param title タイトル
     * @param searchOk クリップ可否
     */
    void setClipRequestData(final Map<String, String> map, final ClipRequestData requestData,
                            final String title, final String searchOk) {
        requestData.setCrid(map.get(JsonConstants.META_RESPONSE_CRID));
        requestData.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
        requestData.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
        requestData.setTitleId(map.get(JsonConstants.META_RESPONSE_TITLE_ID));
        requestData.setTitle(title);
        requestData.setRValue(map.get(JsonConstants.META_RESPONSE_R_VALUE));
        requestData.setLinearStartDate(map.get(JsonConstants.META_RESPONSE_PUBLISH_START_DATE));
        requestData.setLinearEndDate(map.get(JsonConstants.META_RESPONSE_PUBLISH_END_DATE));
        requestData.setSearchOk(searchOk);
    }

    /**
     * リクエストタイプ設定する.
     * @param requestData クリップリクエストデータ
     * @param dispType 表示タイプ.
     * @param contentsType コンテンツタイプ
     */
    void setRequestType(final ClipRequestData requestData, final String dispType, final String contentsType) {
        requestData.setDispType(dispType);
        requestData.setContentType(contentsType);
    }

    /**
     * チャンネル情報設定する.
     * @param map コンテンツ一覧用マップ
     * @param contentInfo  クリップデータ一覧
     * @param channels チャンネル情報
     */
    void setChannelInfo(final Map<String, String> map, final ContentsData contentInfo, final ArrayList<ChannelInfo> channels) {
        String chNo = map.get(JsonConstants.META_RESPONSE_CHNO);
        if (channels != null && !TextUtils.isEmpty(chNo)) {
            for (ChannelInfo channelInfo : channels) {
                if (chNo.equals(String.valueOf(channelInfo.getChannelNo()))) {
                    contentInfo.setChannelName(channelInfo.getTitle());
                    break;
                }
            }
        }
    }

    /**
     * レスポンスIDデータを設定する.
     * @param contentInfo クリップデータ一覧
     * @param map コンテンツ一覧用マップ
     */
    void setResponseId(final ContentsData contentInfo, final Map<String, String> map) {
        contentInfo.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
        contentInfo.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
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
