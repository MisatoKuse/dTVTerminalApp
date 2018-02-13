/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RentalListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;

import java.util.ArrayList;
import java.util.List;

/**
 * レンタル一覧DataProvider.
 */
public class RentalDataProvider extends ClipKeyListDataProvider implements RentalVodListWebClient.RentalVodListJsonParserCallback {

    /**
     * コンテキスト.
     */
    private Context mContext = null;
    /**
     * DB保存フラグ.
     */
    private boolean mSetDB = false;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * レンタルVODリスト取得WebClient.
     */
    private RentalVodListWebClient mWebClient = null;
    /**
     * コールバック.
     */
    private ApiDataProviderCallback mApiDataProviderCallback = null;
    /**
     * 購入済みVODレスポンス.
     */
    private PurchasedVodListResponse mPurchasedVodListResponse = null;

    @Override
    public void onRentalVodListJsonParsed(final PurchasedVodListResponse response) {
        if (response != null) {
            setStructDB(response);
            if (!mRequiredClipKeyList
                    || mResponseEndFlag) {
                sendRentalListData(response);
            } else {
                mPurchasedVodListResponse = response;
            }
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
            mApiDataProviderCallback.rentalListNgCallback();
        }
    }

    @Override
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        super.onVodClipKeyListJsonParsed(clipKeyListResponse);
        // コールバック判定
        if (mPurchasedVodListResponse != null) {
            sendRentalListData(mPurchasedVodListResponse);
        }
        DTVTLogger.end();
    }

    /**
     * 一覧画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {

        /**
         * レンタル一覧用コールバック.
         *
         * @param list コンテンツリスト
         */
        void rentalListCallback(List<ContentsData> list);

        /**
         * データ取得失敗時用コールバック.
         */
        void rentalListNgCallback();
    }

    /**
     * コンストラクタ.
     *
     * @param mContext コンテキスト
     */
    public RentalDataProvider(final Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) mContext;
        this.mSetDB = false;
    }

    /**
     * RentalListActivityからのデータ取得要求受付.
     *
     * @param flg 初回取得時のみDB保存する
     */
    public void getRentalData(final boolean flg) {
        mPurchasedVodListResponse = null;
        if (!mIsCancel) {
            // クリップキー一覧を取得
            if (mRequiredClipKeyList) {
                getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
            }
            mSetDB = flg;
            //レンタル一覧取得
            getRentalListData();
        } else {
            DTVTLogger.error("RentalDataProvider is stopping connection");
            mApiDataProviderCallback.rentalListNgCallback();
        }
    }

    /**
     * レンタル一覧データをRentalListActivityに送る.
     *
     * @param response 購入済みVOD一覧データ
     */
    private void sendRentalListData(final PurchasedVodListResponse response) {

        //ContentsList生成
        List<ContentsData> list = makeContentsData(response);

        //レンタル一覧を送る
        mApiDataProviderCallback.rentalListCallback(list);
    }

    /**
     * レンタル一覧を取得する.
     */
    private void getRentalListData() {
        //通信クラスにデータ取得要求を出す
        mWebClient = new RentalVodListWebClient(mContext);
        mWebClient.getRentalVodListApi(this);
    }

    /**
     * DB保存.
     *
     * @param response 購入済みVOD一覧データ
     */
    private void setStructDB(final PurchasedVodListResponse response) {
        if (mSetDB) {
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastDate(DateUtils.RENTAL_LIST_LAST_INSERT);
            RentalListInsertDataManager dataManager = new RentalListInsertDataManager(mContext);
            dataManager.insertRentalListInsertList(response);
            sendRentalListData(response);
        }
    }

    /**
     * ContentsData生成.
     *
     * @param response 購入済みVOD一覧データ
     * @return コンテンツリスト
     */
    private List<ContentsData> makeContentsData(final PurchasedVodListResponse response) {
        List<ContentsData> list = new ArrayList<>();
        ArrayList<VodMetaFullData> metaFullData = response.getVodMetaFullData();
        for (int i = 0; i < response.getVodMetaFullData().size(); i++) {
            ContentsData data = new ContentsData();

            VodMetaFullData vodMetaFullData = metaFullData.get(i);

            String title = vodMetaFullData.getTitle();
            String searchOk = vodMetaFullData.getmSearch_ok();
            String dispType = vodMetaFullData.getDisp_type();
            String dtv = vodMetaFullData.getDtv();
            String dtvType = vodMetaFullData.getDtvType();
            data.setTitle(title);
            //エポック秒から文字に変換
            data.setTime(DateUtils.formatEpochToString(vodMetaFullData.getAvail_end_date()));
            data.setRatStar(String.valueOf(vodMetaFullData.getRating()));
            data.setThumURL(vodMetaFullData.getmThumb_448_252());
            data.setSearchOk(searchOk);
            data.setContentsType(vodMetaFullData.getmContent_type());
            data.setDtv(dtv);
            data.setDtvType(dtvType);
            data.setDispType(dispType);
            data.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
            data.setContentsId(vodMetaFullData.getCid());

            //クリップリクエストデータ作成
            ClipRequestData requestData = new ClipRequestData();

            String linearEndDate = String.valueOf(vodMetaFullData.getAvail_end_date());

            requestData.setCrid(vodMetaFullData.getCrid());
            requestData.setServiceId(vodMetaFullData.getmService_id());
            requestData.setEventId(vodMetaFullData.getmEvent_id());
            requestData.setTitleId(vodMetaFullData.getEpisode_id());
            requestData.setTitle(title);
            requestData.setRValue(vodMetaFullData.getR_value());
            requestData.setLinearStartDate(String.valueOf(vodMetaFullData.getAvail_start_date()));
            requestData.setLinearEndDate(linearEndDate);
            requestData.setSearchOk(searchOk);

            //視聴通知判定生成
            String contentsType = vodMetaFullData.getmContent_type();
            String tvService = vodMetaFullData.getmTv_service();
            String dTv = vodMetaFullData.getDtv();
            requestData.setIsNotify(dispType, contentsType, linearEndDate, tvService, dTv);
            requestData.setDispType(dispType);
            requestData.setContentType(contentsType);
//            requestData.setTableType(decisionTableType(contentsType, contentsType));
            data.setRequestData(requestData);

            if (mRequiredClipKeyList) {
                // クリップ状態をコンテンツリストに格納
                data.setClipStatus(getClipStatus(dispType, contentsType, dTv,
                        requestData.getCrid(), requestData.getServiceId(),
                        requestData.getEventId(), requestData.getTitleId()));
            }

            list.add(data);
        }
        return list;
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsCancel = true;
        stopConnection();
        if (mWebClient != null) {
            mWebClient.stopConnection();
        }
    }

    /**
     * 通信許可状態にする.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsCancel = false;
        enableConnection();
        if (mWebClient != null) {
            mWebClient.enableConnection();
        }
    }
}
