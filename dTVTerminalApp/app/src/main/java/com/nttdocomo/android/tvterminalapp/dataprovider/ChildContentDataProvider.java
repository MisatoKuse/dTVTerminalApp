/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DataBaseThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RentalListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RentalListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChildContentListGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChildContentListGetWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 子コンテンツデータプロバイダー.
 */
public class ChildContentDataProvider extends ClipKeyListDataProvider implements
        ChildContentListGetWebClient.JsonParserCallback,
        RentalVodListWebClient.RentalVodListJsonParserCallback,
        DataBaseThread.DataBaseOperation {

    // declaration
    /**
     * DataProviderコールバック.
     */
    public interface DataCallback {
        /**
         *
         * @param list コンテンツリスト
         */
        void childContentListCallback(@Nullable List<ContentsData> list);
    }

    // region variable
    /**子コンテンツ一覧取得ウェブクライアント.*/
    private final ChildContentListGetWebClient mWebClient;
    /**コールバック.*/
    private DataCallback mCallback;
    /**子コンテンツ一覧取得レスポンス.*/
    private ChildContentListGetResponse mChildContentListGetResponse;
    /**WebAPIエラー情報.*/
    private ErrorState mError = null;
    /**通信止めるフラグ.*/
    private boolean mIsCancel = false;
    /**子コンテンツ一覧データ取得位置.*/
    private int mPagerOffset = 0;
    /**子コンテンツ一覧全体の件数.*/
    private int mTotal = 0;
    /** 購入済みチャンネルリスト取得.*/
    private static final int RENTAL_VOD_SELECT = 1;
    /** 購入済みチャンネルリスト更新.*/
    private static final int RENTAL_VOD_UPDATE = 2;
    /** レンタルVOD一覧取得WebClient.*/
    private RentalVodListWebClient mRentalVodListWebClient;
    /** 購入済みVODリスト情報を保持.*/
    private PurchasedVodListResponse mPurchasedVodListResponse = null;
    /** レスポンス終了フラグ.*/
    private boolean mRentalVodResponseEndFlag = false;
    /** 購入情報取得フラグ.*/
    private boolean mIsRental = false;
    /** 購入情報.*/
    private ArrayList<ActiveData> mActiveDatas;
    // endregion variable

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public ChildContentDataProvider(final Context context) {
        super(context);
        mCallback = (DataCallback) context;
        mWebClient = new ChildContentListGetWebClient(context);
        mRentalVodListWebClient = new RentalVodListWebClient(context);
    }

    @Override
    public void onVodClipKeyResult(final ClipKeyListResponse clipKeyListResponse, final ErrorState errorState) {
        super.onVodClipKeyResult(clipKeyListResponse, errorState);
        if (mChildContentListGetResponse != null && (!mIsRental || mRentalVodResponseEndFlag)) {
            sendData(mChildContentListGetResponse);
        }
    }

    @Override
    public void onJsonParsed(final ChildContentListGetResponse response) {
        if (response == null) {
            if (mWebClient.getError() != null) {
                mError = mWebClient.getError();
            }
            mCallback.childContentListCallback(null);
        } else {
            if (response.getPager().getOffset() >= 0 && response.getPager().getCount() >= 0) {
                mPagerOffset = response.getPager().getOffset() + response.getPager().getCount();
            }
            if (response.getPager().getTotal() >= 0) {
                mTotal = response.getPager().getTotal();
            }
            if ((!mRequiredClipKeyList
                    || mResponseEndFlag)
                    && (!mIsRental || mRentalVodResponseEndFlag)) {
                sendData(response);
            } else { // clipキー一覧取得が終わってない場合は待つ
                mChildContentListGetResponse = response;
            }
        }
    }


    @Override
    public void onRentalVodListJsonParsed(final PurchasedVodListResponse purchasedVodListResponse, final ErrorState jsonParseError) {
        mPurchasedVodListResponse = purchasedVodListResponse;
        if (purchasedVodListResponse != null) {
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, RENTAL_VOD_UPDATE);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
            mActiveDatas = mPurchasedVodListResponse.getVodActiveData();
        }
        executeRentalVodListCallback();
    }

    @Override
    public List<Map<String, String>> dbOperation(final DataBaseThread dataBaseThread, final int operationId) {
        super.dbOperation(dataBaseThread, operationId);
        List<Map<String, String>> resultSet = null;
        switch (operationId) {
            case RENTAL_VOD_UPDATE: //サーバーから取得した購入済みVODデータをDBに保存する
                RentalListInsertDataManager rentalListInsertDataManager = new RentalListInsertDataManager(mContext);
                rentalListInsertDataManager.insertRentalListInsertList(mPurchasedVodListResponse);
                break;
            case RENTAL_VOD_SELECT: //DBから購入済みVODデータを取得して返却する
                RentalListDataManager rentalListDataManager = new RentalListDataManager(mContext);
                List<Map<String, String>> purchasedVodActiveList = rentalListDataManager.selectRentalActiveListData();
                if (purchasedVodActiveList != null && purchasedVodActiveList.size() > 0) {
                    mActiveDatas = new ArrayList<>();
                    for (int i = 0; i < purchasedVodActiveList.size(); i++) {
                        Map<String, String> hashMap = purchasedVodActiveList.get(i);
                        String active_list_license_id = hashMap.get(JsonConstants.META_RESPONSE_ACTIVE_LIST
                                + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_LICENSE_ID);
                        String active_list_valid_end_date = hashMap.get(JsonConstants.META_RESPONSE_ACTIVE_LIST
                                + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_VAILD_END_DATE);
                        ActiveData activeDate = new ActiveData();
                        activeDate.setLicenseId(active_list_license_id);
                        activeDate.setValidEndDate(Long.parseLong(active_list_valid_end_date));
                        mActiveDatas.add(activeDate);
                    }
                }
                executeRentalVodListCallback();
                break;
            default:
                break;
        }
        return resultSet;
    }

    /**
     * 子コンテンツ一覧取得.
     * @param crid コンテンツ識別子
     * @param offset 　取得位置
     * @param dispType 表示タイプ
     * @param isRental 購入情報取得フラグ
     */
    public void getChildContentList(final String crid, final int offset, final String dispType, final boolean isRental) {
        mIsRental = isRental;
        mPagerOffset = offset;
        mChildContentListGetResponse = null;
        if (!mIsCancel) {
            if (mRequiredClipKeyList) {
                getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.VOD));
                mRequiredClipKeyList = false;
            }

            String filter = WebApiBasePlala.FILTER_RELEASE;
            if (dispType.equals(DtvtConstants.DISP_TYPE_SERIES_SVOD)) {
                filter = WebApiBasePlala.FILTER_RELEASE_SSVOD;
            }
            UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
            final int ageReq = userInfoDataProvider.getUserAge();
            if (mWebClient.getError() != null) {
                mWebClient.getError().setErrorType(DtvtConstants.ErrorType.SUCCESS);
                mWebClient.getError().setErrorMessage("");
            }
            boolean result = mWebClient.requestChildContentListGetApi(crid, offset, filter, ageReq, this);
            if (!result) {
                mCallback.childContentListCallback(null);
            }
            if (mIsRental) {
                getVodListData();
            }
        } else {
            mCallback.childContentListCallback(null);
        }
    }

    /**
     * 購入済みVOD一覧取得.
     */
    private void getVodListData() {
        mRentalVodResponseEndFlag = false;
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RENTAL_VOD_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, RENTAL_VOD_SELECT);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                executeRentalVodListCallback();
                DTVTLogger.debug(e);
            }
        } else {
            if (!mIsCancel) {
                mRentalVodListWebClient.getRentalVodListApi(this);
            } else {
                executeRentalVodListCallback();
                DTVTLogger.error("ChildContentDataProvider is stopping connect");
            }
        }
    }

    /**
     * 購入済みVOD一覧返却用メソッド.
     */
    private void executeRentalVodListCallback() {
        mRentalVodResponseEndFlag = true;
        if (mChildContentListGetResponse != null
                && (!mRequiredClipKeyList || mResponseEndFlag)) {
            sendData(mChildContentListGetResponse);
        }
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
        if (mRentalVodListWebClient != null) {
            mRentalVodListWebClient.stopConnection();
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
        if (mRentalVodListWebClient != null) {
            mRentalVodListWebClient.enableConnection();
        }
    }
    /**
     * レンタルデータ取得エラーのクラスを返すゲッター.
     *
     * @return レンタルデータ取得エラーのクラス
     */
    public ErrorState getError() {
        return mError;
    }

    /**
     * ContentsData生成.
     * コンテンツデータ作成用メソッドのため行数超過を許容
     *
     * @param response 購入済みVOD一覧データ
     * @return コンテンツリスト
     */
    @SuppressWarnings("OverlyLongMethod")
    private List<ContentsData> makeContentsData(final ChildContentListGetResponse response) {

        List<ContentsData> list = new ArrayList<>();
        ArrayList<VodMetaFullData> metaFullData = response.getVodMetaFullData();
        for (VodMetaFullData vodMetaFullData : metaFullData) {
            ContentsData data = new ContentsData();
            String title = vodMetaFullData.getTitle();
            String epiTitle = vodMetaFullData.getEpititle();
            String searchOk = vodMetaFullData.getmSearch_ok();
            String dispType = vodMetaFullData.getDisp_type();
            String dtv = vodMetaFullData.getDtv();
            String dtvType = vodMetaFullData.getDtvType();
            data.setTitle(title);
            data.setEpisodeTitle(epiTitle);
            //エポック秒から文字に変換
            data.setRatStar(String.valueOf(vodMetaFullData.getRating()));
            if (ContentUtils.DTV_FLAG_ONE.equals(dtv)) {
                data.setThumURL(vodMetaFullData.getmDtv_thumb_448_252());
                data.setThumDetailURL(vodMetaFullData.getmDtv_thumb_640_360());
            } else {
                data.setThumURL(vodMetaFullData.getmThumb_448_252());
                data.setThumDetailURL(vodMetaFullData.getmThumb_640_360());
            }
            data.setSearchOk(searchOk);
            data.setContentsType(vodMetaFullData.getmContent_type());
            data.setDtv(dtv);
            data.setDtvType(dtvType);
            data.setDispType(dispType);
            data.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
            data.setContentsId(vodMetaFullData.getCrid());
            data.setCrid(vodMetaFullData.getCrid());
            data.setEstFlg(vodMetaFullData.getEstFlag());
            data.setChsVod(vodMetaFullData.getmChsvod());
            data.setAvailStartDate(vodMetaFullData.getAvail_start_date());
            data.setSynop(vodMetaFullData.getSynop());
            data.setDurTime(vodMetaFullData.getDur());
            data.setEpisodeId(vodMetaFullData.getEpisode_id());
            data.setTitleId(vodMetaFullData.getTitle_id());
            if (mIsRental) {
                // activeDataList から視聴可能期限を取り出し、配信期限(AvailEndDate)として使用する(DREM-2275の仕様)
                String result = ContentUtils.getRentalVodValidInfo(vodMetaFullData, mActiveDatas, true);
                long activeEndDate = Long.parseLong(result);
                data.setAvailEndDate(activeEndDate == 0 ? vodMetaFullData.getAvail_end_date() : activeEndDate);
                data.setIsRental(mIsRental);
            } else {
                data.setAvailEndDate(vodMetaFullData.getAvail_end_date());
            }
            data.setVodStartDate(vodMetaFullData.getmVod_start_date());
            data.setVodEndDate(vodMetaFullData.getmVod_end_date());

            //クリップリクエストデータ作成
            ClipRequestData requestData = new ClipRequestData();

            requestData.setCrid(vodMetaFullData.getCrid());
            requestData.setServiceId(vodMetaFullData.getmService_id());
            requestData.setEventId(vodMetaFullData.getmEvent_id());
            requestData.setTitleId(vodMetaFullData.getEpisode_id());
            requestData.setTitle(title);
            requestData.setRValue(vodMetaFullData.getR_value());
            requestData.setLinearStartDate(String.valueOf(vodMetaFullData.getPublish_start_date()));
            requestData.setLinearEndDate(String.valueOf(vodMetaFullData.getPublish_end_date()));
            requestData.setSearchOk(searchOk);

            //視聴通知判定生成
            String contentsType = vodMetaFullData.getmContent_type();
            String tvService = vodMetaFullData.getmTv_service();
            String dTv = vodMetaFullData.getDtv();
            requestData.setIsNotify(dispType, contentsType, vodMetaFullData.getmVod_start_date(), tvService, dTv);
            requestData.setDispType(dispType);
            requestData.setContentType(contentsType);
            requestData.setVodStartDate(vodMetaFullData.getmVod_start_date());
            requestData.setTvService(tvService);
            data.setRequestData(requestData);

            if (mRequiredClipKeyList) {
                // クリップ状態をコンテンツリストに格納
                data.setClipStatus(getClipStatus(dispType, contentsType, dTv,
                        requestData.getCrid(), requestData.getServiceId(),
                        requestData.getEventId(), requestData.getTitleId(), tvService, vodMetaFullData.getmVod_start_date()));
            }

            list.add(data);
        }
        return list;
    }

    /**
     * 通信で得たデータをcallbackで返す.
     * @param response 通信で得たデータ
     */
    private void sendData(final ChildContentListGetResponse response) {
        List<ContentsData> list = makeContentsData(response);
        mCallback.childContentListCallback(list);
    }

    /**
     * ページオフセットの取得.
     * @return ページオフセット
     */
    public int getPagerOffset() {
        return mPagerOffset;
    }

    /**
     * 全体の件数取得.
     * @return 全体の件数
     */
    public int getTotal() {
        return mTotal;
    }
}
