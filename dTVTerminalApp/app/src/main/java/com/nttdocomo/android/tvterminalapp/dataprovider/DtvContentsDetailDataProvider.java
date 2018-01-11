/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RoleListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ProgramDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ContentsDetailGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.model.detail.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
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
public class DtvContentsDetailDataProvider implements ContentsDetailGetWebClient.ContentsDetailJsonParserCallback,
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
     * ディスプレイタイプ.
     */
    private static final String[] DISPLAY_TYPE = {"", "hikaritv", "dch"};

    /**
     * コンストラクタ.
     *
     * @param context TvProgramListActivity
     */
    public DtvContentsDetailDataProvider(final Context context) {
        this.mContext = context;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) context;
    }

    @Override
    public void onContentsDetailJsonParsed(final ContentsDetailGetResponse ContentsDetailLists) {
        if (ContentsDetailLists != null) {
            ArrayList<VodMetaFullData> detailListInfo = ContentsDetailLists.getVodMetaFullData();
            if (detailListInfo != null) {
                mApiDataProviderCallback.onContentsDetailInfoCallback(detailListInfo);
            }
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
        ArrayList<Channel> channels = null;
        if (channelLists != null) {
            mChannelList = channelLists.get(0);
            List<HashMap<String, String>> channelList = mChannelList.getClList();
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
                    ArrayList<Channel> channels = new ArrayList<>();
                    for (int i = 0; i < resultSet.size(); i++) {
                        Map<String, String> hashMap = resultSet.get(i);
                        String chNo = hashMap.get(JsonContents.META_RESPONSE_CHNO);
                        String title = hashMap.get(JsonContents.META_RESPONSE_TITLE);
                        String serviceId = hashMap.get(JsonContents.META_RESPONSE_SERVICE_ID);
                        String startDate = hashMap.get(JsonContents.META_RESPONSE_AVAIL_START_DATE);
                        String endDate = hashMap.get(JsonContents.META_RESPONSE_AVAIL_END_DATE);
                        String chType = hashMap.get(JsonContents.META_RESPONSE_CH_TYPE);
                        String puId = hashMap.get(JsonContents.META_RESPONSE_PUID);
                        String subPuId = hashMap.get(JsonContents.META_RESPONSE_SUB_PUID);
                        String chPackPuId = hashMap.get(JsonContents.META_RESPONSE_CHPACK
                                + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PUID);
                        String chPackSubPuId = hashMap.get(JsonContents.META_RESPONSE_CHPACK
                                + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_SUB_PUID);

                        if (!TextUtils.isEmpty(chNo)) {
                            Channel channel = new Channel();
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
                        String id = hashMap.get(JsonContents.META_RESPONSE_CONTENTS_ID);
                        String name = hashMap.get(JsonContents.META_RESPONSE_CONTENTS_NAME);
                        RoleListMetaData roleData = new RoleListMetaData();
                        roleData.setId(id);
                        roleData.setName(name);
                        roleListData.add(roleData);
                    }
                    if (null != mApiDataProviderCallback) {
                        mApiDataProviderCallback.onRoleListCallback(roleListData);
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
            case ROLELIST_UPDATE://サーバーから取得したロールリストデータをDBに保存する
                RoleListInsertDataManager roleListInsertDataManager = new RoleListInsertDataManager(mContext);
                roleListInsertDataManager.insertRoleList(mRoleListInfo);
                break;
            case ROLELIST_SELECT://DBからロールリストデータを取得して、画面に返却する
                HomeDataManager homeDataManager = new HomeDataManager(mContext);
                resultSet = homeDataManager.selectRoleListData();
                break;
            case CHANNEL_UPDATE://サーバーから取得したチャンネルデータをDBに保存する
                ChannelInsertDataManager channelInsertDataManager = new ChannelInsertDataManager(mContext);
                channelInsertDataManager.insertChannelInsertList(mChannelList, DISPLAY_TYPE[mChannelDisplayType]);
                break;
            case CHANNEL_SELECT://DBからチャンネルデータを取得して、画面に返却する
                ProgramDataManager channelDataManager = new ProgramDataManager(mContext);
                resultSet = channelDataManager.selectChannelListProgramData(DISPLAY_TYPE[mChannelDisplayType]);
                break;
            default:
                break;
        }
        return resultSet;
    }

    @Override
    public void onRentalVodListJsonParsed(final PurchasedVodListResponse response) {
        mApiDataProviderCallback.onRentalVodListCallback(response);
    }

    @Override
    public void onRentalChListJsonParsed(final PurchasedChListResponse response) {
        mApiDataProviderCallback.onRentalChListCallback(response);
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
    private void setChannelData(final ArrayList<Channel> channels, final List<HashMap<String, String>> channelList) {
        for (int i = 0; i < channelList.size(); i++) {
            HashMap<String, String> hashMap = channelList.get(i);
            String chNo = hashMap.get(JsonContents.META_RESPONSE_CHNO);
            String title = hashMap.get(JsonContents.META_RESPONSE_TITLE);
            String serviceId = hashMap.get(JsonContents.META_RESPONSE_SERVICE_ID);
            String startDate = hashMap.get(JsonContents.META_RESPONSE_AVAIL_START_DATE);
            String endDate = hashMap.get(JsonContents.META_RESPONSE_AVAIL_END_DATE);
            String chType = hashMap.get(JsonContents.META_RESPONSE_CH_TYPE);
            String puId = hashMap.get(JsonContents.META_RESPONSE_PUID);
            String subPuId = hashMap.get(JsonContents.META_RESPONSE_SUB_PUID);
            String chPackPuId = hashMap.get(JsonContents.META_RESPONSE_CHPACK
                    + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PUID);
            String chPackSubPuId = hashMap.get(JsonContents.META_RESPONSE_CHPACK
                    + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_SUB_PUID);
            if (!TextUtils.isEmpty(chNo)) {
                Channel channel = new Channel();
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
        void onContentsDetailInfoCallback(ArrayList<VodMetaFullData> contentsDetailInfo);

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
        void channelListCallback(ArrayList<Channel> channels);

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
        ContentsDetailGetWebClient detailGetWebClient = new ContentsDetailGetWebClient();
        detailGetWebClient.getContentsDetailApi(crid, filter, ageReq, this);
    }

    /**
     * 購入済みVOD一覧取得.
     */
    public void getVodListData() {
        RentalVodListWebClient rentalVodListWebClient = new RentalVodListWebClient();
        rentalVodListWebClient.getRentalVodListApi(this);
    }

    /**
     * 購入済みCH一覧取得.
     */
    public void getChListData() {
        RentalChListWebClient rentalChListWebClient = new RentalChListWebClient();
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
            RoleListWebClient roleListWebClient = new RoleListWebClient();
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
            ChannelWebClient mChannelList = new ChannelWebClient();
            mChannelList.getChannelApi(limit, offset, filter, DISPLAY_TYPE[type], this);
        }
    }

    /**
     * 録画予約一覧取得.
     *
     * @param info 録画予約コンテンツ詳細
     */
    public void requestRecordingReservation(final RecordingReservationContentsDetailInfo info) {
        RemoteRecordingReservationWebClient client = new RemoteRecordingReservationWebClient();
        client.getRemoteRecordingReservationApi(info, this);
    }
}