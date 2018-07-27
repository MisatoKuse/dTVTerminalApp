/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DataBaseThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RentalListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RoleListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ClipKeyListDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ProgramDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RentalListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ContentsDetailGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsDetailGetWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RemoteRecordingReservationWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalChListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RoleListWebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * コンテンツ詳細画面のDataProvider.
 */
public class ContentsDetailDataProvider extends ClipKeyListDataProvider implements
        ContentsDetailGetWebClient.ContentsDetailJsonParserCallback,
        RoleListWebClient.RoleListJsonParserCallback,
        DataBaseThread.DataBaseOperation,
        RemoteRecordingReservationWebClient.RemoteRecordingReservationJsonParserCallback,
        RentalVodListWebClient.RentalVodListJsonParserCallback,
        RentalChListWebClient.RentalChListJsonParserCallback {

    /**declaration.*/
    public enum ErrorType {
        /**コンテンツ詳細取得.*/
        contentsDetailGet,
        /**ロール一覧取得.*/
        roleList,
        /**レンタルチャンネル一覧.*/
        rentalChList,
        /**レンタルVod一覧.*/
        rentalVodList,
    }

    // callback
    /**
     * 画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {
        /**
         * コンテンツ情報取得.
         *
         * @param contentsDetailInfo 画面に渡すチャンネル番組情報
         * @param clipStatus クリープステータス
         */
        void onContentsDetailInfoCallback(VodMetaFullData contentsDetailInfo, boolean clipStatus);

        /**
         * ロールリスト情報取得.
         *
         * @param roleListInfo 画面に渡すチャンネルロールリスト情報
         */
        void onRoleListCallback(ArrayList<RoleListMetaData> roleListInfo);

        /**
         * リモート録画予約実行結果を返す.
         *
         * @param response 実行結果
         */
        void recordingReservationResult(RemoteRecordingReservationResultResponse response);

        /**
         * 購入済みVOD一覧を返す.
         *
         * @param response 購入済みVOD一覧
         */
        void onRentalVodListCallback(PurchasedVodListResponse response);

        /**
         * 購入済みCH一覧を返す.
         *
         * @param response 購入済みCH一覧
         */
        void onRentalChListCallback(final PurchasedChannelListResponse response);
    }

    /** コンテンツ詳細情報取得中. */
    private boolean mIsInContentsDetailRequest = false;
    /** ロールリスト取得中. */
    private boolean mIsInRoleListRequest = false;
    /** レンタルVodリスト取得中. */
    private boolean mIsInRentalVodListRequest = false;
    /** レンタルChリスト取得中. */
    private boolean mIsInRentalChListRequest = false;
    // region variable
    /** ApiDataProviderCallbackのインスタンス. */
    private ApiDataProviderCallback mApiDataProviderCallback = null;
    /** コンテキスト. */
    private Context mContext = null;
    /** 購入済みVODリスト情報を保持. */
    private PurchasedVodListResponse mPurchasedVodListResponse = null;
    /** 購入済みVODのactive_listの情報を保持. */
    private List<Map<String, String>> mPurchasedVodActiveList = null;
    /** 購入済みチャンネルリスト情報を保持. */
    private PurchasedChannelListResponse mPurchasedChannelListResponse = null;
    /** 購入済みチャンネルのactive_listの情報を保持. */
    private List<Map<String, String>> mPurchasedChActiveList = null;
    /** ロールリスト情報を保持. */
    private ArrayList<RoleListMetaData> mRoleListInfo = null;
    /** クリップキーレスポンス保持. */
    private ClipKeyListResponse mClipKeyListResponse = null;

    /** チャンネル検索(親クラスのDbThreadで"0","1","2"を使用しているため使用しない). */
    private static final int CHANNEL_SELECT = 9;
    /** ロールリスト更新. */
    private static final int ROLELIST_UPDATE = 3;
    /** ロールリスト検索. */
    private static final int ROLELIST_SELECT = 4;
    /** 購入済みチャンネルリスト更新. */
    private static final int RENTAL_VOD_UPDATE = 5;
    /** 購入済みチャンネルリスト取得. */
    private static final int RENTAL_VOD_SELECT = 6;
    /** 購入済みチャンネルリスト更新. */
    private static final int RENTAL_CHANNEL_UPDATE = 7;
    /** 購入済みチャンネルリスト取得. */
    private static final int RENTAL_CHANNEL_SELECT = 8;
    /** VodMetaFullData. */
    private VodMetaFullData mVodMetaFullData = null;
    /** コンテンツ詳細取得WebClient. */
    private ContentsDetailGetWebClient mDetailGetWebClient = null;
    /** ロールリスト取得WebClient. */
    private RoleListWebClient mRoleListWebClient = null;
    /** レンタルチャンネル一覧取得WebClient. */
    private RentalChListWebClient mRentalChListWebClient = null;
    /** レンタルVOD一覧取得WebClient. */
    private RentalVodListWebClient mRentalVodListWebClient = null;
    /** 通信禁止判定フラグ. */
    private boolean isStop = false;
    /** tvコンテンツのクリップキーリスト取得済み判定. */
    private boolean mTvClipKeyListResponse = false;
    /** vodコンテンツのクリップキーリスト取得済み判定. */
    private boolean mVodClipKeyListResponse = false;

    // endregion
    /**
     * コンストラクタ.
     *
     * @param context TvProgramListActivity
     */
    public ContentsDetailDataProvider(final Context context) {
        super(context);
        this.mContext = context;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) context;
    }

    // region super class
    @Override
    public void onContentsDetailJsonParsed(final ContentsDetailGetResponse contentsDetailLists) {
        if (contentsDetailLists != null) {
            if (contentsDetailLists.getStatus().equals(mContext.getString(R.string.contents_detail_response_ok))) {
                ArrayList<VodMetaFullData> detailListInfo = contentsDetailLists.getVodMetaFullData();
                if (detailListInfo != null && detailListInfo.size() > 0) {
                    mVodMetaFullData = detailListInfo.get(0);
                    mVodMetaFullData.setContentsType(ContentUtils.getHikariContentsType(mVodMetaFullData));
                    if (!mRequiredClipKeyList) {
                        executeContentsDetailInfoCallback(
                                mVodMetaFullData, getContentsDetailClipStatus(detailListInfo.get(0)));
                    } else {
                        requestGetClipKeyList(mVodMetaFullData);
                    }
                } else {
                    //結果がOKでも0件ならば、エラー扱いとする
                    executeContentsDetailInfoCallback(null, false);
                }
            } else {
                //status = "NG"の場合
                executeContentsDetailInfoCallback(null, false);
            }
        } else {
            executeContentsDetailInfoCallback(null, false);
        }
    }

    /**
     * コンテンツ詳細データcallback実行用.
     * @param contentsDetailInfo コンテンツ詳細情報
     * @param clipStatus　クリップ状態
     */
    private void executeContentsDetailInfoCallback(final VodMetaFullData contentsDetailInfo, boolean clipStatus) {
        mIsInContentsDetailRequest = false;
        if (mApiDataProviderCallback != null) {
            mApiDataProviderCallback.onContentsDetailInfoCallback(contentsDetailInfo, clipStatus);
        }
    }

    @Override
    public void onTvClipKeyResult(final ClipKeyListResponse clipKeyListResponse
            ,final ErrorState errorState) {
        super.onTvClipKeyResult(clipKeyListResponse,errorState);
        DTVTLogger.start();
        if (mVodClipKeyListResponse) {
            addClipStatus();
        } else {
            mTvClipKeyListResponse = true;
            mClipKeyListResponse = clipKeyListResponse;
        }
        DTVTLogger.end();
    }

    @Override
    public void onVodClipKeyResult(final ClipKeyListResponse clipKeyListResponse
            ,final ErrorState errorState) {
        super.onVodClipKeyResult(clipKeyListResponse,errorState);
        DTVTLogger.start();
        if (mTvClipKeyListResponse) {
            addClipStatus();
        } else {
            mVodClipKeyListResponse = true;
            mClipKeyListResponse = clipKeyListResponse;
        }
        DTVTLogger.end();
    }

    /**
     * クリップ状態取得後にコンテンツ詳細情報を送信.
     *
     */
    private void addClipStatus() {
        DTVTLogger.start();
        //クリップ状態取得
        boolean isClipStatus;
        ClipKeyListDataManager manager = new ClipKeyListDataManager(mContext);
        List<Map<String, String>> mapList = manager.selectClipAllList();
        isClipStatus = ClipUtils.setClipStatusVodMetaData(mVodMetaFullData, mapList);
        executeContentsDetailInfoCallback(
                mVodMetaFullData, isClipStatus);
        DTVTLogger.end();
    }

    @Override
    public void onRentalVodListJsonParsed(final PurchasedVodListResponse purchasedVodListResponse) {

        mPurchasedVodListResponse = purchasedVodListResponse;
        if (mPurchasedVodListResponse != null) {
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastProgramDate(DateUtils.RENTAL_VOD_LAST_UPDATE);
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, RENTAL_VOD_UPDATE);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
            executeRentalVodListCallback(mPurchasedVodListResponse);
        } else {
            executeRentalVodListCallback(null);
        }
    }

    /**
     * 購入済みVOD一覧返却用メソッド.
     * @param response 購入済みVod一覧
     */
    private void executeRentalVodListCallback(final PurchasedVodListResponse response) {
        mIsInRentalVodListRequest = false;
        if (mApiDataProviderCallback != null) {
            mApiDataProviderCallback.onRentalVodListCallback(response);
        }
    }

    @Override
    public void onRentalChListJsonParsed(final PurchasedChannelListResponse purchasedChannelListResponse) {
        mPurchasedChannelListResponse = purchasedChannelListResponse;
        if (mPurchasedChannelListResponse != null) {
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, RENTAL_CHANNEL_UPDATE);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
            executeRentalChListCallback(purchasedChannelListResponse);
        } else {
            executeRentalChListCallback(null);
        }
    }

    /**
     * 購入済みCh一覧返却用メソッド.
     * @param response 購入済みCh一覧
     */
    private void executeRentalChListCallback(final PurchasedChannelListResponse response) {
        mIsInRentalChListRequest = false;
        if (mApiDataProviderCallback != null) {
            mApiDataProviderCallback.onRentalChListCallback(response);
        }
    }

    @Override
    public void onRoleListJsonParsed(final RoleListResponse roleListResponse) {
        if (roleListResponse != null) {
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastProgramDate(DateUtils.ROLELIST_LAST_UPDATE);
            mRoleListInfo = roleListResponse.getRoleList();
            if (mRoleListInfo != null) {
                Handler handler = new Handler(); //チャンネル情報更新
                try {
                    DataBaseThread dataBaseThread = new DataBaseThread(handler, this, ROLELIST_UPDATE);
                    dataBaseThread.start();
                } catch (RuntimeException e) {
                    DTVTLogger.debug(e);
                }
            }
            executeRoleListCallback(mRoleListInfo);
        } else {
            //TODO :WEBAPIを取得できなかった時の処理を記載予定
            DTVTLogger.debug("WEBAPIを取得できなかった時の処理を記載予定");
        }
    }

    /**
     * ロールリスト返却用メソッド.
     * @param roleListInfo ロールリスト
     */
    private void executeRoleListCallback(final ArrayList<RoleListMetaData> roleListInfo) {
        mIsInRoleListRequest = false;
        if (mApiDataProviderCallback != null) {
            mApiDataProviderCallback.onRoleListCallback(roleListInfo);
        }
    }

    @SuppressWarnings("OverlyLongMethod")
    @Override
    public void onDbOperationFinished(final boolean isSuccessful, final List<Map<String, String>> resultSet, final int operationId) {
        if (isSuccessful) {
            switch (operationId) {
                case ROLELIST_SELECT:
                    ArrayList<RoleListMetaData> roleListData = new ArrayList<>();
                    for (int i = 0; i < resultSet.size(); i++) {
                        Map<String, String> hashMap = resultSet.get(i);
                        String id = hashMap.get(JsonConstants.META_RESPONSE_CONTENTS_ID);
                        String name = hashMap.get(JsonConstants.META_RESPONSE_CONTENTS_NAME);
                        RoleListMetaData roleData = new RoleListMetaData();
                        roleData.setId(id);
                        roleData.setName(name);
                        roleListData.add(roleData);
                    }
                    executeRoleListCallback(roleListData);
                    break;
                case RENTAL_VOD_SELECT:
                    PurchasedVodListResponse purchasedVodListData = new PurchasedVodListResponse();
                    ArrayList<ActiveData> activeDatas = new ArrayList<>();
                    for (int i = 0; i < mPurchasedVodActiveList.size(); i++) {
                        Map<String, String> hashMap = mPurchasedVodActiveList.get(i);
                        String active_list_license_id = hashMap.get(JsonConstants.META_RESPONSE_ACTIVE_LIST
                                + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_LICENSE_ID);
                        String active_list_valid_end_date = hashMap.get(JsonConstants.META_RESPONSE_ACTIVE_LIST
                                + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_VAILD_END_DATE);
                        ActiveData activeDate = new ActiveData();
                        activeDate.setLicenseId(active_list_license_id);
                        activeDate.setValidEndDate(Long.parseLong(active_list_valid_end_date));
                        activeDatas.add(activeDate);
                    }
                    purchasedVodListData.setVodActiveData(activeDatas);
                    if (null != mApiDataProviderCallback) {
                        executeRentalVodListCallback(purchasedVodListData);
                    }
                    break;
                case RENTAL_CHANNEL_SELECT:
                    PurchasedChannelListResponse purchasedChannelListResponse = new PurchasedChannelListResponse();
                    ChannelList channelList = new ChannelList();
                    List<HashMap<String, String>> list = new ArrayList<>();

                    for (int i = 0; i < resultSet.size(); i++) {
                        Map<String, String> hashMap = resultSet.get(i);
                        HashMap<String, String> vcListMap = new HashMap<>();
                        for (String para : JsonConstants.METADATA_LIST_PARA) {
                            vcListMap.put(para, hashMap.get(para));
                        }
                        list.add(vcListMap);
                    }
                    channelList.setChannelList(list);
                    purchasedChannelListResponse.setChannelListData(channelList);

                    ArrayList<ActiveData> activeChDatas = new ArrayList<>();
                    for (int i = 0; i < mPurchasedChActiveList.size(); i++) {
                        Map<String, String> hashMap = mPurchasedChActiveList.get(i);

                        String active_list_license_id = hashMap.get(JsonConstants.META_RESPONSE_ACTIVE_LIST
                                + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_LICENSE_ID);
                        String active_list_valid_end_date = hashMap.get(JsonConstants.META_RESPONSE_ACTIVE_LIST
                                + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_VAILD_END_DATE);
                        ActiveData activeDate = new ActiveData();
                        activeDate.setLicenseId(active_list_license_id);
                        activeDate.setValidEndDate(Long.parseLong(active_list_valid_end_date));

                        activeChDatas.add(activeDate);
                    }
                    purchasedChannelListResponse.setChActiveData(activeChDatas);
                    if (null != mApiDataProviderCallback) {
                        executeRentalChListCallback(purchasedChannelListResponse);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @SuppressWarnings("OverlyLongMethod")
    @Override
    public List<Map<String, String>> dbOperation(final DataBaseThread dataBaseThread, final int operationId) {
        super.dbOperation(dataBaseThread, operationId);
        List<Map<String, String>> resultSet = null;
        switch (operationId) {
            case ROLELIST_UPDATE: //サーバーから取得したロールリストデータをDBに保存する
                RoleListInsertDataManager roleListInsertDataManager = new RoleListInsertDataManager(mContext);
                roleListInsertDataManager.insertRoleList(mRoleListInfo);
                break;
            case ROLELIST_SELECT: //DBからロールリストデータを取得して、画面に返却する
                HomeDataManager homeDataManager = new HomeDataManager(mContext);
                resultSet = homeDataManager.selectRoleListData();
                break;
            case CHANNEL_SELECT: //DBからチャンネルデータを取得して、画面に返却する
                ProgramDataManager channelDataManager = new ProgramDataManager(mContext);
                resultSet = channelDataManager.selectChannelListProgramData(JsonConstants.CH_SERVICE_TYPE_INDEX_ALL);
                break;
            case RENTAL_VOD_UPDATE: //サーバーから取得した購入済みVODデータをDBに保存する
                RentalListInsertDataManager rentalListInsertDataManager = new RentalListInsertDataManager(mContext);
                rentalListInsertDataManager.insertRentalListInsertList(mPurchasedVodListResponse);
                break;
            case RENTAL_VOD_SELECT: //DBから購入済みVODデータを取得して返却する
                RentalListDataManager rentalListDataManager = new RentalListDataManager(mContext);
                resultSet = rentalListDataManager.selectRentalListData();
                mPurchasedVodActiveList = rentalListDataManager.selectRentalActiveListData();
                if (mPurchasedVodActiveList == null || mPurchasedVodActiveList.size() < 1) {
                    //DBから取得したデータがない場合は初期化したデータを渡す
                    PurchasedVodListResponse purchasedVodListData = new PurchasedVodListResponse();
                    executeRentalVodListCallback(purchasedVodListData);
                }
                break;
            case RENTAL_CHANNEL_UPDATE: //サーバーから取得した購入済みCHデータをDBに保存する
                RentalListInsertDataManager rentalChListInsertDataManager = new RentalListInsertDataManager(mContext);
                rentalChListInsertDataManager.insertChRentalListInsertList(mPurchasedChannelListResponse);
                break;
            case RENTAL_CHANNEL_SELECT: //DBから購入済みCHデータを取得して返却する
                RentalListDataManager rentalChListDataManager = new RentalListDataManager(mContext);
                resultSet = rentalChListDataManager.selectRentalChListData();
                mPurchasedChActiveList = rentalChListDataManager.selectRentalChActiveListData();
                if (mPurchasedChActiveList == null || mPurchasedChActiveList.size() < 1) {
                    //DBから取得したデータがない場合は初期化したデータを渡す
                    PurchasedChannelListResponse purchasedChannelListResponse = new PurchasedChannelListResponse();
                    executeRentalChListCallback(purchasedChannelListResponse);
                }
                break;
            default:
                break;
        }
        return resultSet;
    }

    @Override
    public void onRemoteRecordingReservationJsonParsed(final RemoteRecordingReservationResultResponse response) {
        mApiDataProviderCallback.recordingReservationResult(response);
    }

    /**
     * チャンネルデータの整形.
     *
     * @param channels    チャンネル一覧
     * @param channelList パースされたチャンネル情報
     */
    private void setChannelData(final ArrayList<ChannelInfo> channels, final List<HashMap<String, String>> channelList) {
        for (int i = 0; i < channelList.size(); i++) {
            HashMap<String, String> hashMap = channelList.get(i);
            String chNo = hashMap.get(JsonConstants.META_RESPONSE_CHNO);
            String title = hashMap.get(JsonConstants.META_RESPONSE_TITLE);
            String serviceId = hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID);
            String startDate = hashMap.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE);
            String endDate = hashMap.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE);
            String chType = hashMap.get(JsonConstants.META_RESPONSE_CH_TYPE);
            String puId = hashMap.get(JsonConstants.META_RESPONSE_PUID);
            String subPuId = hashMap.get(JsonConstants.META_RESPONSE_SUB_PUID);
            String chPackPuId = hashMap.get(JsonConstants.META_RESPONSE_CHPACK
                    + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_PUID);
            String chPackSubPuId = hashMap.get(JsonConstants.META_RESPONSE_CHPACK
                    + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_SUB_PUID);
            if (!TextUtils.isEmpty(chNo)) {
                ChannelInfo channel = new ChannelInfo();
                channel.setTitle(title);
                channel.setChannelNo(Integer.parseInt(chNo));
                channel.setServiceId(serviceId);
                channel.setStartDate(startDate);
                channel.setEndDate(endDate);
                channel.setChannelType(chType);
                channel.setPurchaseId(puId);
                channel.setSubPurchaseId(subPuId);
                channel.setChannelPackPurchaseId(chPackPuId);
                channel.setChannelPackSubPurchaseId(chPackSubPuId);
                channels.add(channel);
            }
        }
    }
    // endregion

    // region public method
    /**
     * コンテンツ詳細取得.
     *
     * @param crid   取得したい情報のコンテンツ識別ID(crid)の配列
     * @param filter release、testa、demo ※指定なしの場合release
     * @param ageReq dch：dチャンネル, hikaritv：ひかりTVの多ch, 指定なしの場合：すべて
     */
    public void getContentsDetailData(final String[] crid, final String filter, final int ageReq) {
        mIsInContentsDetailRequest = true;
        if (!isStop) {
            mDetailGetWebClient = new ContentsDetailGetWebClient(mContext);
            mDetailGetWebClient.getContentsDetailApi(crid, filter, ageReq, this);
        } else {
            DTVTLogger.error("ContentsDetailDataProvider is stopping connect");
        }
    }

    /**
     * 購入済みVOD一覧取得.
     */
    public void getVodListData() {
        mIsInRentalVodListRequest = true;
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RENTAL_VOD_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, RENTAL_VOD_SELECT);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
        } else {
            if (!isStop) {
                mRentalVodListWebClient = new RentalVodListWebClient(mContext);
                mRentalVodListWebClient.getRentalVodListApi(this);
            } else {
                DTVTLogger.error("ContentsDetailDataProvider is stopping connect");
            }
        }
    }

    /**
     * 購入済みVOD一覧を強制的にサーバーから取得.
     */
    public void getForceVodListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastProgramDate(DateUtils.RENTAL_VOD_LAST_UPDATE);
        mRentalVodListWebClient = new RentalVodListWebClient(mContext);
        mRentalVodListWebClient.getRentalVodListApi(this);
    }

    /**
     * 購入済みCH一覧取得.
     */
    public void getChListData() {
        mIsInRentalChListRequest = true;
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RENTAL_CHANNEL_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, RENTAL_CHANNEL_SELECT);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
        } else {
            if (!isStop) {
                mRentalChListWebClient = new RentalChListWebClient(mContext);
                mRentalChListWebClient.getRentalChListApi(this);
            } else {
                DTVTLogger.error("ContentsDetailDataProvider is stopping connect");
            }
        }
    }

    /**
     * ロールリスト取得.
     */
    public void getRoleListData() {
        mIsInRoleListRequest = true;
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.ROLELIST_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, ROLELIST_SELECT);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
        } else {
            if (!isStop) {
                mRoleListWebClient = new RoleListWebClient(mContext);
                mRoleListWebClient.getRoleListApi(this);
            } else {
                DTVTLogger.error("ContentsDetailDataProvider is stopping connect");
            }
        }
    }

    /**
     * 録画予約一覧取得.
     *
     * @param info 録画予約コンテンツ詳細
     * @return パラメータエラー等でAPIの実行が行えなかった場合はfalse
     */
    public boolean requestRecordingReservation(final RecordingReservationContentsDetailInfo info) {
        RemoteRecordingReservationWebClient client =
                new RemoteRecordingReservationWebClient(mContext);
        return client.getRemoteRecordingReservationApi(info, this);
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        isStop = true;
        if (mDetailGetWebClient != null) {
            mDetailGetWebClient.stopConnection();
        }
        if (mRoleListWebClient != null) {
            mRoleListWebClient.stopConnection();
        }
        if (mRentalChListWebClient != null) {
            mRentalChListWebClient.stopConnection();
        }
        if (mRentalVodListWebClient != null) {
            mRentalVodListWebClient.stopConnection();
        }
        stopConnection();
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        isStop = false;
        if (mDetailGetWebClient != null) {
            mDetailGetWebClient.enableConnection();
        }
        if (mRoleListWebClient != null) {
            mRoleListWebClient.enableConnection();
        }
        if (mRentalChListWebClient != null) {
            mRentalChListWebClient.enableConnection();
        }
        if (mRentalVodListWebClient != null) {
            mRentalVodListWebClient.enableConnection();
        }
    }

    /**
     * WebClientで発生したエラーを返す.
     * @param type WebClientType(コールしたWebClientType)
     *
     * @return エラー情報
     */
    public ErrorState getError(final ErrorType type) {
        switch (type) {
            case contentsDetailGet:
                return mDetailGetWebClient.getError();
            case roleList:
                return mRoleListWebClient.getError();
            case rentalChList:
                return mRentalChListWebClient.getError();
            case rentalVodList:
                return mRentalVodListWebClient.getError();
            default:
                break;
        }
        return null;
    }
    // endregion

    // region private method
    /**
     * コンテンツ詳細情報のメタデータを元にクリップ状態を取得.
     *
     * @param metaFullData クリップ状態
     * @return コンテンツ詳細データ
     */
    private boolean getContentsDetailClipStatus(final VodMetaFullData metaFullData) {
        return getClipStatus(metaFullData.getDisp_type(),
                metaFullData.getmContent_type(),
                metaFullData.getDtv(),
                metaFullData.getCrid(),
                metaFullData.getmService_id(),
                metaFullData.getmEvent_id(),
                metaFullData.getTitle_id(), metaFullData.getmTv_service());
    }

    /**
     * コンテンツ詳細情報を元にクリップキー一覧の取得を要求.
     *
     * @param metaFullData コンテンツ詳細データ
     */
    private void requestGetClipKeyList(final VodMetaFullData metaFullData) {
        DTVTLogger.start();
        ClipKeyListDao.TableTypeEnum tableType = decisionTableType(metaFullData.getDisp_type(), metaFullData.getmContent_type());
        mTvClipKeyListResponse = false;
        mVodClipKeyListResponse = false;
        //番組でも見逃し等VODデータが入ってい来ることがあるので、全件取得する
        getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.TV));
        getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.VOD));
        DTVTLogger.end();
    }

    /**
     * コンテンツ詳細リクエストステータスフラグ返却.
     * @return リクエストステータス
     */
    public boolean isIsInContentsDetailRequest() {
        return mIsInContentsDetailRequest;
    }

    /**
     * ロールリストリクエストステータスフラグ返却.
     * @return リクエストステータス
     */
    public boolean isIsInRoleListRequest() {
        return mIsInRoleListRequest;
    }

    /**
     * レンタルVodリストリクエストステータスフラグ返却.
     * @return リクエストステータス
     */
    public boolean isIsInRentalVodListRequest() {
        return mIsInRentalVodListRequest;
    }

    /**
     * レンタルChリストリクエストステータスフラグ返却.
     * @return リクエストステータス
     */
    public boolean isIsInRentalChListRequest() {
        return mIsInRentalChListRequest;
    }
    // endregion
}
