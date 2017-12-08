/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvClipInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.TvClipDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipContentInfo;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvClipWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.TV_LAST_INSERT;


public class TvClipDataProvider implements TvClipWebClient.TvClipJsonParserCallback {
    private Context mContext;

    @Override
    public void onTvClipJsonParsed(List<TvClipList> tvClipLists) {
        if (tvClipLists != null && tvClipLists.size() > 0) {
            TvClipList list = tvClipLists.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
            if (null != apiDataProviderCallback) {
                apiDataProviderCallback.tvClipListCallback(null);
            }
        }
    }

    /**
     * 画面用データを返却するためのコールバック
     */
    public interface TvClipDataProviderCallback {
        /**
         * クリップリスト用コールバック
         */
        void tvClipListCallback(TvClipContentInfo clipContentInfo);
    }

    private TvClipDataProviderCallback apiDataProviderCallback;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public TvClipDataProvider(Context context) {
        this.mContext = context;
        this.apiDataProviderCallback = (TvClipDataProviderCallback) context;
    }

    /**
     * Activityからのデータ取得要求受付
     */
    public void getClipData(int pagerOffset) {
        List<Map<String, String>> tvClipList = getTvClipListData(pagerOffset);

        if (tvClipList != null && tvClipList.size() > 0) {
            sendTvClipListData(tvClipList);
        }
    }

    /**
     * TvクリップリストをActivityに送る
     */
    public void sendTvClipListData(List<Map<String, String>> list) {

        TvClipContentInfo clipContentInfo = new TvClipContentInfo();
        String title = "";
        String contentTime = "";
        String picUrl = "";
        String contentId = "";
        TvClipContentInfo tmpClipContentInfo = new TvClipContentInfo();

        for (int i = 0; i < list.size(); i++) {
            title = list.get(i).get(JsonContents.META_RESPONSE_TITLE);
            contentTime = list.get(i).get(JsonContents.META_RESPONSE_DISPLAY_START_DATE);
            picUrl = list.get(i).get(JsonContents.META_RESPONSE_THUMB_448);
            contentId = list.get(i).get(JsonContents.META_RESPONSE_DISP_TYPE);

            //ClipContentInfoItem(boolean clipFlag, String contentPictureUrl, String title, String rating)

            TvClipContentInfo.TvClipContentInfoItem item = tmpClipContentInfo.new TvClipContentInfoItem(true, picUrl, title, "");
            clipContentInfo.add(item);
        }

        apiDataProviderCallback.tvClipListCallback(clipContentInfo);
    }

    /**
     * Tvクリップリストデータ取得開始
     */
    private List<Map<String, String>> getTvClipListData(int pagerOffset) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(TV_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        //if (true) { //test
        boolean fromDb = lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate);
        if (fromDb) {
            //データをDBから取得する
            TvClipDataManager tvClipDataManager = new TvClipDataManager(mContext);
            list = tvClipDataManager.selectTvClipData();
            if (null == list || 0 == list.size()) {
                fromDb = false;
            }
        }

        if (!fromDb) {
            //通信クラスにデータ取得要求を出す
            TvClipWebClient webClient = new TvClipWebClient();
            int ageReq = 1;
            int upperPageLimit = 1;
            int lowerPageLimit = 1;
            //int pagerOffset = 1;
            String pagerDirection = "";
            webClient.getTvClipApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, pagerDirection, this);
        }
        return list;
    }

    /**
     * Vodクリップ一覧データをDBに格納する
     */
    private void setStructDB(TvClipList tvClipList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(TV_LAST_INSERT);
        TvClipInsertDataManager dataManager = new TvClipInsertDataManager(mContext);
        dataManager.insertTvClipInsertList(tvClipList);
        sendTvClipListData(tvClipList.getVcList());
    }

}
