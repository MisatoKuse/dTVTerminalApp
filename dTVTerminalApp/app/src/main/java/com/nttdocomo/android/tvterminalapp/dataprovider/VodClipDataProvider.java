/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.VodClipInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.VodClipDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipContentInfo;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.VodClipWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VOD_LAST_INSERT;


public class VodClipDataProvider implements VodClipWebClient.VodClipJsonParserCallback
{

    private Context mContext;

    @Override
    public void onVodClipJsonParsed(List<VodClipList> vodClipLists) {
        if (vodClipLists != null && vodClipLists.size() > 0) {
            VodClipList list = vodClipLists.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
            if(null!=apiDataProviderCallback){
                apiDataProviderCallback.vodClipListCallback(null);
            }
        }
    }

    /**
     * 画面用データを返却するためのコールバック
     */
    public interface ApiDataProviderCallback {
        /**
         * クリップリスト用コールバック
         *
         * @param clipContentInfo
         */
        void vodClipListCallback(VodClipContentInfo clipContentInfo);
    }

    private ApiDataProviderCallback apiDataProviderCallback;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public VodClipDataProvider(Context mContext) {
        this.mContext = mContext;
        this.apiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    /**
     * Activityからのデータ取得要求受付
     */
    public void getClipData(int pagerOffset) {
        List<Map<String, String>> vodClipList = getVodClipListData(pagerOffset);

        if(vodClipList != null && vodClipList.size() > 0){
            sendVodClipListData(vodClipList);
        }
    }

    /**
     * VodクリップリストをActivityに送る
     *
     * @param list
     */
    public void sendVodClipListData(List<Map<String, String>> list) {

        VodClipContentInfo clipContentInfo = new VodClipContentInfo();
        String title="";
        String contentTime="";
        String picUrl ="";
        String contentId="";
        String ratingValue="";
        VodClipContentInfo tmpClipContentInfo=new VodClipContentInfo();

        for (int i = 0; i < list.size(); i++) {
            title = list.get(i).get(VodClipJsonParser.VODCLIP_LIST_TITLE);
            contentTime = list.get(i).get(VodClipJsonParser.VODCLIP_LIST_DISPLAY_START_DATE);
            picUrl = list.get(i).get(VodClipJsonParser.VODCLIP_LIST_THUMB);
            contentId = list.get(i).get(VodClipJsonParser.VODCLIP_LIST_DISP_TYPE);
            ratingValue = list.get(i).get(VodClipJsonParser.VODCLIP_LIST_R_VALUE);

            //ClipContentInfoItem(boolean clipFlag, String contentPictureUrl, String title, String rating)

            VodClipContentInfo.VodClipContentInfoItem item=tmpClipContentInfo.new VodClipContentInfoItem(true, picUrl, title, ratingValue);
            clipContentInfo.add(item);
        }

        apiDataProviderCallback.vodClipListCallback(clipContentInfo);
    }

    /**
     * Vodクリップリストデータ取得開始
     */
    private List<Map<String, String>> getVodClipListData(int pagerOffset) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(VOD_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        boolean fromDb=lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate);
        if (fromDb) {
            //データをDBから取得する
            VodClipDataManager vodClipDataManager = new VodClipDataManager(mContext);
            list = vodClipDataManager.selectVodClipData();
            if(null==list || 0==list.size()){
                fromDb=false;
            }
        }

        if(!fromDb){
            //通信クラスにデータ取得要求を出す
            VodClipWebClient webClient = new VodClipWebClient();
            int ageReq = 1;
            int upperPageLimit = 1;
            int lowerPageLimit = 1;
            //int pagerOffset = 1;
            webClient.getVodClipApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, this);
        }
        return list;
    }

    /**
     * Vodクリップ一覧データをDBに格納する
     *
     * @param vodClipList
     */
    private void setStructDB(VodClipList vodClipList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(VOD_LAST_INSERT);
        VodClipInsertDataManager dataManager = new VodClipInsertDataManager(mContext);
        dataManager.insertVodClipInsertList(vodClipList);
        sendVodClipListData(vodClipList.getVcList());
    }

}
