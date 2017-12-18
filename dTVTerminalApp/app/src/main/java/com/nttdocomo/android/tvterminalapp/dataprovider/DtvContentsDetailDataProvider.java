/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RoleListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ProgramDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ContentsDetailGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.model.detail.RecordingReservationContentsDetailInfo;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsDetailGetWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RemoteRecordingReservationClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RoleListWebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.CHANNEL_LAST_UPDATE;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.ROLELIST_LAST_UPDATE;

public class DtvContentsDetailDataProvider implements ContentsDetailGetWebClient.ContentsDetailJsonParserCallback,
        RoleListWebClient.RoleListJsonParserCallback, ChannelWebClient.ChannelJsonParserCallback,
        DbThread.DbOperation, RemoteRecordingReservationClient.RemoteRecordingReservationJsonParserCallback {

    private ApiDataProviderCallback apiDataProviderCallback;
    private static final String DISPLAY_TYPE[] = {"", "hikaritv", "dch"};
    private Context context;
    private static final int CHANNEL_UPDATE = 1;//チャンネル更新
    private static final int CHANNEL_SELECT = 2;//チャンネル検索
    private static final int ROLELIST_UPDATE = 3;//ロールリスト更新
    private static final int ROLELIST_SELECT = 4;//ロールリスト検索
    private int channel_display_type;
    private ChannelList mChannelList;
    private ArrayList<RoleListMetaData> roleListInfo;

    /**
     * コンストラクタ
     *
     * @param context TvProgramListActivity
     */
    public DtvContentsDetailDataProvider(Context context) {
        this.context = context;
        this.apiDataProviderCallback = (ApiDataProviderCallback) context;
    }

    @Override
    public void onContentsDetailJsonParsed(ContentsDetailGetResponse ContentsDetailLists) {
        if (ContentsDetailLists != null) {
            ArrayList<VodMetaFullData> detailListInfo = ContentsDetailLists.getVodMetaFullData();
            if (detailListInfo != null) {
                apiDataProviderCallback.onContentsDetailInfoCallback(detailListInfo);
            }
        }
    }

    @Override
    public void onRoleListJsonParsed(RoleListResponse roleListResponse) {
        if (roleListResponse != null) {
            roleListInfo = roleListResponse.getRoleList();
            if (roleListInfo != null) {
                Handler handler = new Handler();//チャンネル情報更新
                try {
                    DbThread t = new DbThread(handler, this, ROLELIST_UPDATE);
                    t.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (roleListInfo != null) {
                apiDataProviderCallback.onRoleListCallback(roleListInfo);
            }
        }
    }

    @Override
    public void onChannelJsonParsed(List<ChannelList> channelLists) {
        ArrayList<Channel> channels = null;
        if (channelLists != null) {
            mChannelList = channelLists.get(0);
            List<HashMap<String, String>> channelList = mChannelList.getClList();
            if (channelList != null) {
                channels = new ArrayList<>();
                setChannelData(channels, channelList);
                Handler handler = new Handler();//チャンネル情報更新
                try {
                    DbThread t = new DbThread(handler, this, CHANNEL_UPDATE);
                    t.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (null != apiDataProviderCallback) {
            apiDataProviderCallback.channelListCallback(channels);
        }
    }

    @Override
    public void onDbOperationFinished(boolean isSuccessful, List<Map<String, String>> resultSet, int operationId) {
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

                        if (!TextUtils.isEmpty(chNo)) {
                            Channel channel = new Channel();
                            channel.setChNo(Integer.parseInt(chNo));
                            channel.setTitle(title);
                            channel.setServiceId(serviceId);
                            channel.setStartDate(startDate);
                            channel.setEndDate(endDate);
                            channels.add(channel);
                        }
                    }
                    if (null != apiDataProviderCallback) {
                        apiDataProviderCallback.channelListCallback(channels);
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
                    if (null != apiDataProviderCallback) {
                        apiDataProviderCallback.onRoleListCallback(roleListData);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public List<Map<String, String>> dbOperation(int operationId) throws Exception {
        List<Map<String, String>> resultSet = null;
        switch (operationId) {
            case ROLELIST_UPDATE://サーバーから取得したロールリストデータをDBに保存する
                RoleListInsertDataManager roleListInsertDataManager = new RoleListInsertDataManager(context);
                roleListInsertDataManager.insertRoleList(roleListInfo);
                break;
            case ROLELIST_SELECT://DBからロールリストデータを取得して、画面に返却する
                HomeDataManager homeDataManager = new HomeDataManager(context);
                resultSet = homeDataManager.selectRoleListData();
                break;
            case CHANNEL_UPDATE://サーバーから取得したチャンネルデータをDBに保存する
                ChannelInsertDataManager channelInsertDataManager = new ChannelInsertDataManager(context);
                channelInsertDataManager.insertChannelInsertList(mChannelList, DISPLAY_TYPE[channel_display_type]);
                break;
            case CHANNEL_SELECT://DBからチャンネルデータを取得して、画面に返却する
                ProgramDataManager channelDataManager = new ProgramDataManager(context);
                resultSet = channelDataManager.selectChannelListProgramData(DISPLAY_TYPE[channel_display_type]);
                break;
            default:
                break;
        }
        return resultSet;
    }

    @Override
    public void onRemoteRecordingReservationJsonParsed(RemoteRecordingReservationResultResponse response) {
        apiDataProviderCallback.recordingReservationResult(response);
    }

    /**
     * チャンネルデータの整形
     */
    private void setChannelData(ArrayList<Channel> channels, List<HashMap<String, String>> channelList) {
        for (int i = 0; i < channelList.size(); i++) {
            HashMap<String, String> hashMap = channelList.get(i);
            String chNo = hashMap.get(JsonContents.META_RESPONSE_CHNO);
            String title = hashMap.get(JsonContents.META_RESPONSE_TITLE);
            String serviceId = hashMap.get(JsonContents.META_RESPONSE_SERVICE_ID);
            String startDate = hashMap.get(JsonContents.META_RESPONSE_AVAIL_START_DATE);
            String endDate = hashMap.get(JsonContents.META_RESPONSE_AVAIL_END_DATE);
            if (!TextUtils.isEmpty(chNo)) {
                Channel channel = new Channel();
                channel.setTitle(title);
                channel.setChNo(Integer.parseInt(chNo));
                channel.setServiceId(serviceId);
                channel.setStartDate(startDate);
                channel.setEndDate(endDate);
                channels.add(channel);
            }
        }
    }

    /**
     * 画面用データを返却するためのコールバック
     */
    public interface ApiDataProviderCallback {
        /**
         * コンテンツ情報取得
         *
         * @param contentsDetailInfo 画面に渡すチャンネル番組情報
         */
        void onContentsDetailInfoCallback(ArrayList<VodMetaFullData> contentsDetailInfo);

        /**
         * ロールリスト情報取得
         *
         * @param roleListInfo 画面に渡すチャンネルロールリスト情報
         */
        void onRoleListCallback(ArrayList<RoleListMetaData> roleListInfo);

        /**
         * チャンネルリストを戻す
         *
         * @param channels 　画面に渡すチャンネル情報
         */
        void channelListCallback(ArrayList<Channel> channels);

        /**
         * リモート録画予約実行結果を返す
         *
         * @param response 実行結果
         */
        void recordingReservationResult(RemoteRecordingReservationResultResponse response);
    }


    /**
     * コンテンツ詳細取得
     *
     * @param crid   取得したい情報のコンテンツ識別ID(crid)の配列
     * @param filter release、testa、demo ※指定なしの場合release
     * @param ageReq dch：dチャンネル, hikaritv：ひかりTVの多ch, 指定なしの場合：すべて
     */
    public void getContentsDetailData(String[] crid, String filter, int ageReq) {
        ContentsDetailGetWebClient detailGetWebClient = new ContentsDetailGetWebClient();
        detailGetWebClient.getContentsDetailApi(crid, filter, ageReq, this);
    }

    /**
     * ロールリスト取得
     */
    public void getRoleListData() {
        DateUtils dateUtils = new DateUtils(context);
        String lastDate = dateUtils.getLastDate(ROLELIST_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler();//チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, ROLELIST_SELECT);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            dateUtils.addLastProgramDate(ROLELIST_LAST_UPDATE);
            RoleListWebClient roleListWebClient = new RoleListWebClient();
            roleListWebClient.getRoleListApi(this);
        }
    }

    /**
     * CH一覧取得
     *
     * @param limit  レスポンスの最大件数
     * @param offset 取得位置(1～)
     * @param filter release、testa、demo ※指定なしの場合release
     * @param type   dch：dチャンネル, hikaritv：ひかりTVの多ch, 指定なしの場合：すべて
     */
    public void getChannelList(int limit, int offset, String filter, int type) {
        this.channel_display_type = type;
        DateUtils dateUtils = new DateUtils(context);
        String lastDate = dateUtils.getLastDate(CHANNEL_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler();//チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, CHANNEL_SELECT);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            dateUtils.addLastProgramDate(CHANNEL_LAST_UPDATE);
            ChannelWebClient mChannelList = new ChannelWebClient();
            mChannelList.getChannelApi(limit, offset, filter, DISPLAY_TYPE[type], this);
        }
    }

    public void requestRecordingReservation(RecordingReservationContentsDetailInfo info) {
        RemoteRecordingReservationClient client = new RemoteRecordingReservationClient();
        client.getRemoteRecordingReservationApi(info, this);

    }
}
