/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvScheduleInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ProgramDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClientSync;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClientSync;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * アプリ起動時の番組表取得サービス.
 * APIデータ取得～DB保存までを一括して実施.
 */
public class TvProgramIntentService extends IntentService {

    /** チャンネル一覧取得クラス. */
    private ChannelWebClientSync mChannelWebClientSync = null;
    /** 番組表取得クラス. */
    private TvScheduleWebClientSync mTvScheduleWebClientSync = null;
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
        Intent intent = new Intent(context, TvProgramIntentService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        DTVTLogger.start();
        if (intent != null) {
            DTVTLogger.debug("TvProgramIntentService get tv program list start");
            getChannelData();
        }
        DTVTLogger.end();
    }

    /**
     * チャンネルデータ.
     *
     * @param areaCode エリアコード
     * @return チャンネル一覧
     */
    private void getChannelDataFromApi(final String areaCode) {
        mChannelWebClientSync = new ChannelWebClientSync();
        mChannelWebClientSync.enableConnect();
        List<ChannelList> channelLists = mChannelWebClientSync.getChannelApi(getApplicationContext(), 0, 0, "", "", areaCode);
        //チャンネルリスト(全件)から取得対象のチャンネルを抽出する
        if (channelLists != null && channelLists.size() > 0) {
            ChannelInsertDataManager channelInsertDataManager = new ChannelInsertDataManager(getApplicationContext());
            channelInsertDataManager.insertChannelInsertList(channelLists.get(0));
            if (channelLists.get(0).getChannelList() != null) {
                getBeforeStorageChanelList(channelLists.get(0).getChannelList());
            }
        }
    }

    /**
     * チャンネルリスト(全件取得).
     */
    private void getChannelData() {
        DTVTLogger.start();

        DateUtils dateUtils = new DateUtils(TvProgramIntentService.this);
        String lastDate = dateUtils.getLastDate(DateUtils.CHANNEL_LAST_UPDATE);
        String areaCode = UserInfoUtils.getAreaCode(getApplicationContext());
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) && NetWorkUtils.isOnline(TvProgramIntentService.this)) {
            //非同期処理のチャンネルリスト取得を、同期処理として実行する
            getChannelDataFromApi(areaCode);
        } else {
            ProgramDataManager channelDataManager = new ProgramDataManager(TvProgramIntentService.this);
            if (!TextUtils.isEmpty(areaCode)) {
                List<Map<String, String>> resultSet = channelDataManager.selectChannelListProgramData(JsonConstants.CH_SERVICE_TYPE_INDEX_TTB);
                if (resultSet == null || resultSet.size() == 0) {
                    getChannelDataFromApi(areaCode);
                }
            }
        }
        DTVTLogger.end();
    }

    /**
     * チャンネル一覧から先頭各10件を抽出(h4d,dTv).
     *
     * @param hashMapList チャンネル一覧
     */
    private void getBeforeStorageChanelList(final List<Map<String, String>> hashMapList) {
        List<Map<String, String>> h4dChannelMap = new ArrayList<>();
        List<Map<String, String>> dTvChannelMap = new ArrayList<>();
        List<Map<String, String>> ttbChannelMap = new ArrayList<>();
        List<Map<String, String>> bsChannelMap = new ArrayList<>();
        for (int i = 0; i < hashMapList.size(); i++) {
            String service = hashMapList.get(i).get(JsonConstants.META_RESPONSE_SERVICE);
            if (service != null) {
                switch (service) {
                    case ProgramDataManager.CH_SERVICE_HIKARI:
                        //h4dチャンネルを10件取得
                        if (h4dChannelMap.size() < 10) {
                            h4dChannelMap.add(hashMapList.get(i));
                        }
                        break;
                    case ProgramDataManager.CH_SERVICE_DCH:
                        //dTvチャンネルを10件取得
                        if (dTvChannelMap.size() < 10) {
                            dTvChannelMap.add(hashMapList.get(i));
                        }
                        break;
                    case ProgramDataManager.CH_SERVICE_TTB:
                        //地テジを10件取得
                        if (ttbChannelMap.size() < 10) {
                            ttbChannelMap.add(hashMapList.get(i));
                        }
                        break;
                    case ProgramDataManager.CH_SERVICE_BS:
                        //BSを10件取得
                        if (bsChannelMap.size() < 10) {
                            bsChannelMap.add(hashMapList.get(i));
                        }
                        break;
                    default:
                        break;
                }
            }
            //h4d、dTvチャンネルが両方10件取得出来たらループを抜ける
            if (h4dChannelMap.size() > 9 && dTvChannelMap.size() > 9 && ttbChannelMap.size() > 9 && bsChannelMap.size() > 9) {
                break;
            }
        }
        if (ttbChannelMap.size() > 0) {
            //番組表取得(地テジの先頭10件)
            String areaCode = UserInfoUtils.getAreaCode(getApplicationContext());
            if (!TextUtils.isEmpty(areaCode)) {
                getTvSchedule(ttbChannelMap, UserInfoUtils.getAreaCode(getApplicationContext()));
            }
        }
        if (dTvChannelMap.size() > 0) {
            //番組表取得(dTVチャンネル先頭10件)
            getTvSchedule(dTvChannelMap, null);
        }
        if (bsChannelMap.size() > 0) {
            //番組表取得(BS先頭10件)
            getTvSchedule(bsChannelMap, null);
        }
        if (h4dChannelMap.size() > 0) {
            //番組表取得(h4d先頭10件)
            getTvSchedule(h4dChannelMap, null);
        }
    }

    /**
     * 番組表取得(先頭10件(ひかり、dChを各10件づつ)).
     * @param areaCode エリアコード
     * @param getChannelList 取得対象チャンネルリスト
     */
    private void getTvSchedule(final List<Map<String, String>> getChannelList, final String areaCode) {
        DTVTLogger.start();
        List<String> serviceIdUniqList = new ArrayList<>();
        //チャンネル一覧からチャンネル番号のみを取得
        for (Map<String, String> hashMap : getChannelList) {
            String serviceIdUniq = hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID_UNIQ);
            if (!TextUtils.isEmpty(serviceIdUniq)) {
                serviceIdUniqList.add(serviceIdUniq);
            }
        }
        String[] serviceIdUniqs = new String[serviceIdUniqList.size()];
        for (int i = 0; i < serviceIdUniqList.size(); i++) {
            serviceIdUniqs[i] = serviceIdUniqList.get(i);
        }
        //前回のデータ取得日時を取得
        DateUtils dateUtils = new DateUtils(TvProgramIntentService.this);
        String[] lastDate = dateUtils.getChLastDate(serviceIdUniqs, DateUtils.getStringNowDate());
        //キャッシュ有効期限外のチャンネル番号を抽出する.
        List<String> fromWebAPI = new ArrayList<>();

        for (int i = 0; i < lastDate.length; i++) {
            if (dateUtils.isBeforeLimitChDate(lastDate[i])) {
                fromWebAPI.add(serviceIdUniqs[i]);
            }
        }
        String[] fromWebAPIServiceIdUniqs = new String[fromWebAPI.size()];
        for (int i = 0; i < fromWebAPI.size(); i++) {
            fromWebAPIServiceIdUniqs[i] = fromWebAPI.get(i);
        }

        if (fromWebAPIServiceIdUniqs.length > 0) {
            //アプリ起動時の取得日付は当日固定
            String[] dateList = new String[]{DateUtils.getStringNowDate()};
            mTvScheduleWebClientSync = new TvScheduleWebClientSync(TvProgramIntentService.this);
            mTvScheduleWebClientSync.enableConnect();
            ChannelInfoList channelInfoList = mTvScheduleWebClientSync.getTvScheduleApi(getApplicationContext(), fromWebAPIServiceIdUniqs, dateList, "", areaCode);
            //サービス中断時に channelInfoList が null で返却される可能性があるためチェックを入れる
            if (channelInfoList != null && channelInfoList.getChannels() != null) {
                //最終日付チェックした後に取得
                TvScheduleInsertDataManager scheduleInsertDataManager = new TvScheduleInsertDataManager(getApplicationContext());
                scheduleInsertDataManager.insertTvScheduleInsertList(channelInfoList, WebApiBasePlala.DATE_NOW);
            }
        }
        DTVTLogger.end();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //サービス停止時には通信を中断する
        if (mChannelWebClientSync != null) {
            mChannelWebClientSync.stopConnect();
        }
        if (mTvScheduleWebClientSync != null) {
            mTvScheduleWebClientSync.stopConnect();
        }
    }
}