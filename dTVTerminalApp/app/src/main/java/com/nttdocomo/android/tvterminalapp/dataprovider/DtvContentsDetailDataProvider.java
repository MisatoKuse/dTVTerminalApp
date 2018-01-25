/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RentalListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RoleListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ProgramDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RentalListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ContentsDetailGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
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
public class DtvContentsDetailDataProvider extends ClipKeyListDataProvider implements ContentsDetailGetWebClient.ContentsDetailJsonParserCallback,
        RoleListWebClient.RoleListJsonParserCallback, ChannelWebClient.ChannelJsonParserCallback,
        DbThread.DbOperation, RemoteRecordingReservationWebClient.RemoteRecordingReservationJsonParserCallback,
        RentalVodListWebClient.RentalVodListJsonParserCallback, RentalChListWebClient.RentalChListJsonParserCallback {

    /**
     * ディスプレイタイプを保持.
     */
    private int mChannelDisplayType = 0;
    /**
     * ApiDataProviderCallbackのインスタンス.
     */
    private ApiDataProviderCallback mApiDataProviderCallback = null;
    /**
     * コンテキスト.
     */
    private Context mContext = null;
    /**
     * 購入済みVODリスト情報を保持.
     */
    private PurchasedVodListResponse mPurchasedVodListResponse = null;
    /**
     * 購入済みVODのactive_listの情報を保持.
     */
    private List<Map<String, String>> mPurchasedVodActiveList = null;
    /**
     * 購入済みチャンネルリスト情報を保持.
     */
    private PurchasedChListResponse mPurchasedChListResponse = null;
    /**
     * 購入済みチャンネルのactive_listの情報を保持.
     */
    private List<Map<String, String>> mPurchasedChActiveList = null;
    /**
     * チャンネルリスト情報を保持.
     */
    private ChannelList mChannelList = null;
    /**
     * ロールリスト情報を保持.
     */
    private ArrayList<RoleListMetaData> mRoleListInfo = null;

    /**
     * チャンネル更新.
     */
    private static final int CHANNEL_UPDATE = 1;
    /**
     * チャンネル検索.
     */
    private static final int CHANNEL_SELECT = 2;
    /**
     * ロールリスト更新.
     */
    private static final int ROLELIST_UPDATE = 3;
    /**
     * ロールリスト検索.
     */
    private static final int ROLELIST_SELECT = 4;
    /**
     * 購入済みチャンネルリスト更新.
     */
    private static final int RENTAL_VOD_UPDATE = 5;
    /**
     * 購入済みチャンネルリスト取得.
     */
    private static final int RENTAL_VOD_SELECT = 6;
    /**
     * 購入済みチャンネルリスト更新.
     */
    private static final int RENTAL_CHANNEL_UPDATE = 7;
    /**
     * 購入済みチャンネルリスト取得.
     */
    private static final int RENTAL_CHANNEL_SELECT = 8;
    /**
     * ディスプレイタイプ.
     */
    private static final String[] DISPLAY_TYPE = {"", "hikaritv", "dch"};

    private ArrayList<VodMetaFullData> mVodMetaFullDataList = null;

    /**
     * コンストラクタ.
     *
     * @param context TvProgramListActivity
     */
    public DtvContentsDetailDataProvider(final Context context) {
        super(context);
        this.mContext = context;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) context;
    }

    @Override
    public void onContentsDetailJsonParsed(final ContentsDetailGetResponse ContentsDetailLists) {
        if (ContentsDetailLists != null) {
            ArrayList<VodMetaFullData> detailListInfo = ContentsDetailLists.getVodMetaFullData();
            if (detailListInfo != null) {
                if (!mRequiredClipKeyList) {
                    mApiDataProviderCallback.onContentsDetailInfoCallback(
                            detailListInfo, getContentsDetailClipStatus(detailListInfo.get(0)));
                } else {
                    mVodMetaFullDataList = detailListInfo;
                    requestGetClipKeyList(detailListInfo.get(0));
                }
            }
        }
    }

    @Override
    public void onTvClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse) {
        super.onTvClipKeyListJsonParsed(clipKeyListResponse);
        mApiDataProviderCallback.onContentsDetailInfoCallback(
                mVodMetaFullDataList, getContentsDetailClipStatus(mVodMetaFullDataList.get(0)));
    }

    @Override
    public void onVodClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse) {
        super.onVodClipKeyListJsonParsed(clipKeyListResponse);
        mApiDataProviderCallback.onContentsDetailInfoCallback(
                mVodMetaFullDataList, getContentsDetailClipStatus(mVodMetaFullDataList.get(0)));
    }

    @Override
    public void onRentalVodListJsonParsed(final PurchasedVodListResponse purchasedVodListResponse) {
        mPurchasedVodListResponse = purchasedVodListResponse;
        if (mPurchasedVodListResponse != null) {
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, RENTAL_VOD_UPDATE);
                t.start();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        }
        if (mPurchasedVodListResponse != null) {
            mApiDataProviderCallback.onRentalVodListCallback(mPurchasedVodListResponse);
        }
    }

    @Override
    public void onRentalChListJsonParsed(final PurchasedChListResponse purchasedChListResponse) {
        mPurchasedChListResponse = purchasedChListResponse;
        if (mPurchasedChListResponse != null) {
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, RENTAL_CHANNEL_UPDATE);
                t.start();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        }
        if (mPurchasedChListResponse != null) {
            mApiDataProviderCallback.onRentalChListCallback(purchasedChListResponse);
        }
    }

    @Override
    public void onRoleListJsonParsed(final RoleListResponse roleListResponse) {
        if (roleListResponse != null) {
            mRoleListInfo = roleListResponse.getRoleList();
            if (mRoleListInfo != null) {
                Handler handler = new Handler(); //チャンネル情報更新
                try {
                    DbThread t = new DbThread(handler, this, ROLELIST_UPDATE);
                    t.start();
                } catch (Exception e) {
                    DTVTLogger.debug(e);
                }
            }
            if (mRoleListInfo != null) {
                mApiDataProviderCallback.onRoleListCallback(mRoleListInfo);
            }
        }
    }

    @Override
    public void onChannelJsonParsed(final List<ChannelList> channelLists) {
        ArrayList<ChannelInfo> channels = null;
        if (channelLists != null) {
            mChannelList = channelLists.get(0);
            List<HashMap<String, String>> channelList = mChannelList.getChannelList();
            if (channelList != null) {
                channels = new ArrayList<>();
                setChannelData(channels, channelList);
                Handler handler = new Handler(); //チャンネル情報更新
                try {
                    DbThread t = new DbThread(handler, this, CHANNEL_UPDATE);
                    t.start();
                } catch (Exception e) {
                    DTVTLogger.debug(e);
                }
            }
        }
        if (null != mApiDataProviderCallback) {
            mApiDataProviderCallback.channelListCallback(channels);
        }
    }

    @Override
    public void onDbOperationFinished(final boolean isSuccessful, final List<Map<String, String>> resultSet, final int operationId) {
        if (isSuccessful) {
            switch (operationId) {
                case CHANNEL_SELECT:
                    ArrayList<ChannelInfo> channels = new ArrayList<>();
                    for (int i = 0; i < resultSet.size(); i++) {
                        Map<String, String> hashMap = resultSet.get(i);
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
                            channel.setChNo(Integer.parseInt(chNo));
                            channel.setTitle(title);
                            channel.setServiceId(serviceId);
                            channel.setStartDate(startDate);
                            channel.setEndDate(endDate);
                            channel.setChType(chType);
                            channel.setPuId(puId);
                            channel.setSubPuId(subPuId);
                            channel.setChPackPuId(chPackPuId);
                            channel.setChPackSubPuId(chPackSubPuId);
                            channels.add(channel);
                        }
                    }
                    if (null != mApiDataProviderCallback) {
                        mApiDataProviderCallback.channelListCallback(channels);
                    }
                    break;
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
                    if (null != mApiDataProviderCallback) {
                        mApiDataProviderCallback.onRoleListCallback(roleListData);
                    }
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
                        mApiDataProviderCallback.onRentalVodListCallback(purchasedVodListData);
                    }
                    break;
                case RENTAL_CHANNEL_SELECT:
                    PurchasedChListResponse purchasedChListResponse = new PurchasedChListResponse();
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
                    purchasedChListResponse.setChannelListData(channelList);

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
                    purchasedChListResponse.setChActiveData(activeChDatas);
                    if (null != mApiDataProviderCallback) {
                        mApiDataProviderCallback.onRentalChListCallback(purchasedChListResponse);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public List<Map<String, String>> dbOperation(final int operationId) throws Exception {
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
            case CHANNEL_UPDATE: //サーバーから取得したチャンネルデータをDBに保存する
                ChannelInsertDataManager channelInsertDataManager = new ChannelInsertDataManager(mContext);
                channelInsertDataManager.insertChannelInsertList(mChannelList, DISPLAY_TYPE[mChannelDisplayType]);
                break;
            case CHANNEL_SELECT: //DBからチャンネルデータを取得して、画面に返却する
                ProgramDataManager channelDataManager = new ProgramDataManager(mContext);
                resultSet = channelDataManager.selectChannelListProgramData(DISPLAY_TYPE[mChannelDisplayType]);
                break;
            case RENTAL_VOD_UPDATE: //サーバーから取得した購入済みVODデータをDBに保存する
                RentalListInsertDataManager rentalListInsertDataManager = new RentalListInsertDataManager(mContext);
                rentalListInsertDataManager.insertRentalListInsertList(mPurchasedVodListResponse);
                break;
            case RENTAL_VOD_SELECT: //DBから購入済みVODデータを取得して返却する
                RentalListDataManager rentalListDataManager = new RentalListDataManager(mContext);
                resultSet = rentalListDataManager.selectRentalListData();
                mPurchasedVodActiveList = rentalListDataManager.selectRentalActiveListData();
                break;
            case RENTAL_CHANNEL_UPDATE: //サーバーから取得した購入済みCHデータをDBに保存する
                RentalListInsertDataManager rentalChListInsertDataManager = new RentalListInsertDataManager(mContext);
                rentalChListInsertDataManager.insertChRentalListInsertList(mPurchasedChListResponse);
                break;
            case RENTAL_CHANNEL_SELECT: //DBから購入済みCHデータを取得して返却する
                RentalListDataManager rentalChListDataManager = new RentalListDataManager(mContext);
                resultSet = rentalChListDataManager.selectRentalChListData();
                mPurchasedChActiveList = rentalChListDataManager.selectRentalChActiveListData();
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
     * @param channels チャンネル一覧
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
                channel.setChNo(Integer.parseInt(chNo));
                channel.setServiceId(serviceId);
                channel.setStartDate(startDate);
                channel.setEndDate(endDate);
                channel.setChType(chType);
                channel.setPuId(puId);
                channel.setSubPuId(subPuId);
                channel.setChPackPuId(chPackPuId);
                channel.setChPackSubPuId(chPackSubPuId);
                channels.add(channel);
            }
        }
    }

    /**
     * 画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {
        /**
         * コンテンツ情報取得.
         *
         * @param contentsDetailInfo 画面に渡すチャンネル番組情報
         */
        void onContentsDetailInfoCallback(ArrayList<VodMetaFullData> contentsDetailInfo,boolean clipStatus);

        /**
         * ロールリスト情報取得.
         *
         * @param roleListInfo 画面に渡すチャンネルロールリスト情報
         */
        void onRoleListCallback(ArrayList<RoleListMetaData> roleListInfo);

        /**
         * チャンネルリストを戻す.
         *
         * @param channels 　画面に渡すチャンネル情報
         */
        void channelListCallback(ArrayList<ChannelInfo> channels);

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
        void onRentalChListCallback(final PurchasedChListResponse response);
    }

    /**
     * コンテンツ詳細取得.
     *
     * @param crid   取得したい情報のコンテンツ識別ID(crid)の配列
     * @param filter release、testa、demo ※指定なしの場合release
     * @param ageReq dch：dチャンネル, hikaritv：ひかりTVの多ch, 指定なしの場合：すべて
     */
    public void getContentsDetailData(final String[] crid, final String filter, final int ageReq) {
        ContentsDetailGetWebClient detailGetWebClient = new ContentsDetailGetWebClient(mContext);
        detailGetWebClient.getContentsDetailApi(crid, filter, ageReq, this);
    }

    /**
     * 購入済みVOD一覧取得.
     */
    public void getVodListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RENTAL_VOD_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, RENTAL_VOD_SELECT);
                t.start();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        } else {
            dateUtils.addLastProgramDate(DateUtils.RENTAL_VOD_LAST_UPDATE);
            RentalVodListWebClient rentalVodListWebClient = new RentalVodListWebClient(mContext);
            rentalVodListWebClient.getRentalVodListApi(this);
        }
    }

    /**
     * 購入済みVOD一覧を強制的にサーバーから取得.
     */
    public void getForceVodListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastProgramDate(DateUtils.RENTAL_VOD_LAST_UPDATE);
        RentalVodListWebClient rentalVodListWebClient = new RentalVodListWebClient(mContext);
        rentalVodListWebClient.getRentalVodListApi(this);
    }

    /**
     * 購入済みCH一覧取得.
     */
    public void getChListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RENTAL_CHANNEL_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, RENTAL_CHANNEL_SELECT);
                t.start();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        } else {
            dateUtils.addLastProgramDate(DateUtils.RENTAL_CHANNEL_LAST_UPDATE);
            RentalChListWebClient rentalChListWebClient = new RentalChListWebClient(mContext);
            rentalChListWebClient.getRentalChListApi(this);
        }
    }

    /**
     * 購入済みCH一覧を強制的にサーバーから取得.
     */
    public void getForceChListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastProgramDate(DateUtils.RENTAL_CHANNEL_LAST_UPDATE);
        RentalChListWebClient rentalChListWebClient = new RentalChListWebClient(mContext);
        rentalChListWebClient.getRentalChListApi(this);
    }

    /**
     * ロールリスト取得.
     */
    public void getRoleListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.ROLELIST_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, ROLELIST_SELECT);
                t.start();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        } else {
            dateUtils.addLastProgramDate(DateUtils.ROLELIST_LAST_UPDATE);
            RoleListWebClient roleListWebClient = new RoleListWebClient(mContext);
            roleListWebClient.getRoleListApi(this);
        }
    }

    /**
     * CH一覧取得.
     *
     * @param limit  レスポンスの最大件数
     * @param offset 取得位置(1～)
     * @param filter release、testa、demo ※指定なしの場合release
     * @param type   dch：dチャンネル, hikaritv：ひかりTVの多ch, 指定なしの場合：すべて
     */
    public void getChannelList(final int limit, final int offset, final String filter, final int type) {
        this.mChannelDisplayType = type;
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.CHANNEL_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, CHANNEL_SELECT);
                t.start();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        } else {
            dateUtils.addLastProgramDate(DateUtils.CHANNEL_LAST_UPDATE);
            ChannelWebClient mChannelList = new ChannelWebClient(mContext);
            mChannelList.getChannelApi(limit, offset, filter, DISPLAY_TYPE[type], this);
        }
    }

    /**
     * 録画予約一覧取得.
     *
     * @param info 録画予約コンテンツ詳細
     */
    public void requestRecordingReservation(final RecordingReservationContentsDetailInfo info) {
        RemoteRecordingReservationWebClient client =
                new RemoteRecordingReservationWebClient(mContext);
        client.getRemoteRecordingReservationApi(info, this);
    }

    /**
     * コンテンツ詳細情報のメタデータを元にクリップ状態を取得
     */
    private boolean getContentsDetailClipStatus(VodMetaFullData metaFullData) {
        return getClipStatus(metaFullData.getDisp_type(),
                metaFullData.getmContent_type(),
                metaFullData.getDtv(),
                metaFullData.getCrid(),
                metaFullData.getmService_id(),
                metaFullData.getmEvent_id(),
                metaFullData.getTitle_id());
    }

    /**
     * コンテンツ詳細情報を元にクリップキー一覧の取得を要求
     */
    private void requestGetClipKeyList(VodMetaFullData metaFullData) {
        ClipKeyListDao.TABLE_TYPE tableType = decisionTableType(metaFullData.getDisp_type(),metaFullData.getmContent_type());
        switch (tableType) {
            case TV:
                getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.TV));
                break;
            case VOD:
                getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
                break;
        }
    }
}