/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClientSync;

import java.util.List;

/**
 * アプリ起動時の番組表取得サービス.
 * APIデータ取得～DB保存までを一括して実施.
 */
public class TvProgramIntentService extends IntentService {

    /** コンテキストファイル. */
    private Context mContext = null;
    /** チャンネルリスト. */
    private ChannelList mChannelList = null;
    /** 番組表リスト. */
    private ChannelInfoList mChannelsInfoList = null;
    /** 取得要求日付. */
    private String mProgramSelectDate = null;

    /**
     * ActivityのstartService(intent);で呼び出されるコンストラクタ.
     */
    public TvProgramIntentService() {
        super("TvProgramIntentService");
    }

    /**
     * Activityからサービスを開始する.
     *
     * @param context コンテキストファイル
     */
    public static void startTvProgramService(final Context context) {
        //TODO ActivityからServiceにデータを渡したいときはここでIntentにSetする
        Intent intent = new Intent(context, TvProgramIntentService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        DTVTLogger.start();
        if (intent != null) {
            DTVTLogger.debug("TvProgramIntentService get tv program list start");
            //マイチャンネルリスト取得、DB保存
            //h4dチャンネルリスト取得、DB保存
            //dchチャンネルリスト取得、DB保存
            getChannelData();
            //チャンネルリストDB保存(DbThread使用予定)
            //番組表取得(チャンネルリストの先頭10件)
            getTvSchedule();
        }
        DTVTLogger.end();
    }

    /**
     * チャンネルリスト(全件取得).
     */
    private void getChannelData() {
        DTVTLogger.start();
        ChannelList channelList = new ChannelList();
        //最終日付チェックした後に取得

        //非同期処理のチャンネルリスト取得を、同期処理として実行する
        ChannelWebClientSync channelWebClientSync = new ChannelWebClientSync();
        List<ChannelList> channelLists = channelWebClientSync.getChannelApi(getApplicationContext(),1,1,"","");

        //取得したチャンネルリストを送信
        sendChannelList(channelLists.get(0));
        DTVTLogger.end();
    }

    /**
     * 番組表取得(先頭10件).
     */
    private void getTvSchedule() {
        DTVTLogger.start();
        ChannelInfoList channelInfoList = new ChannelInfoList();
        //最終日付チェックした後に取得

        //取得したチャンネルリストを送信
        sendScheduleList(channelInfoList);
        DTVTLogger.end();
    }

    /**
     * チャンネルリスト送信.
     *
     * @param channelList チャンネルリスト
     */
    private void sendChannelList(final ChannelList channelList) {
        DTVTLogger.start();
        if (channelList != null) {
            DTVTLogger.debug("TvProgramIntentService sendChannelList start");
            //チャンネルリスト送信
            Intent intent = new Intent(ScaledDownProgramListDataProvider.SEND_CHANNEL_LIST);
            intent.putExtra(ScaledDownProgramListDataProvider.SEND_CHANNEL_LIST, channelList);
            sendBroadcast(intent);
        }
        DTVTLogger.end();
    }

    /**
     * 番組表送信.
     *
     * @param channelInfoList 番組表リスト
     */
    private void sendScheduleList(final ChannelInfoList channelInfoList) {
        DTVTLogger.start();
        if (channelInfoList != null) {
            DTVTLogger.debug("TvProgramIntentService sendScheduleList start");
            //番組表DB送信
            Intent intent = new Intent(ScaledDownProgramListDataProvider.SEND_SCHEDULE_LIST);
            intent.putExtra(ScaledDownProgramListDataProvider.SEND_SCHEDULE_LIST, channelInfoList);
            sendBroadcast(intent);
        }
        DTVTLogger.end();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceComplete();
    }

    /**
     * 実行結果通知メソッド.
     */
    private void serviceComplete() {
        // TODO: 完了種別を返すか完了フラグのみ返すかは検討中
        DTVTLogger.start();
        DTVTLogger.end();
    }
}