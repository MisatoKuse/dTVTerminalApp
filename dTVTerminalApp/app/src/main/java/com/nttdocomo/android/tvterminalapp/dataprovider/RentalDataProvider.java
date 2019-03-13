/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DataBaseThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RentalListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RentalListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    /**
     * 呼び出し元画面タイプ.
     */
    private RentalType mActivityType = null;
    /**
     * 期限付きレンタルコンテンツフラグ.
     */
    private static final int ENABLE_VOD_WATCH_CONTENTS_UNLIMITED = 0;
    /**
     * 無期限レンタルコンテンツ.
     */
    private static final int ENABLE_VOD_WATCH_CONTENTS_LIMITED = 1;
    /**
     * 配信終了レンタルコンテンツ.
     */
    private static final int DELIVERY_END_CONTENTS = -1;
    /**
     * 無期限レンタルコンテンツ判定文字列.
     */
    public static final String ENABLE_VOD_WATCH_CONTENTS_UNLIMITED_HYPHEN = "-";
    /**
     * レンタルコンテンツ判定(video_program).
     */
    private static final String DISP_TYPE_VIDEO_PROGRAM = "video_program";
    /**
     * レンタルコンテンツ判定(video_package).
     */
    private static final String DISP_TYPE_VIDEO_PACKAGE = "video_package";
    /**
     * レンタルコンテンツ判定(0).
     */
    private static final String EST_FLAG_FALSE = "0";
    /**
     * プレミアムビデオコンテンツ判定(0).
     */
    private static final String CHSVOD_FLAG_FALSE = "0";
    /**
     * プレミアムビデオコンテンツ判定(subscription_package).
     */
    private static final String DISP_TYPE_SUBSCRIPTION_PACKAGE = "subscription_package";
    /**
     * プレミアムビデオコンテンツ判定(series_svod).
     */
    private static final String DISP_TYPE_SERIES_SVOD = "series_svod";
    /**
     * レンタルデータInsert判定(親クラスのDbThreadで"0","1","2"を使用しているため使用しない).
     */
    private static final int RENTAL_VIDEO_LIST_INSERT = 3;
    /**
     * レンタルデータSelect判定(親クラスのDbThreadで"0","1","2"を使用しているため使用しない).
     */
    private static final int RENTAL_VIDEO_LIST_SELECT = 4;
    /**
     * レンタルデータ用エラー情報バッファ.
     */
    private ErrorState mError = null;
    /** コンマ.*/
    private static final String COMMA = ",";

    /**
     * Activity指定.
     */
    public enum RentalType {
        /**
         * レンタル一覧.
         */
        RENTAL_LIST,
        /**
         * プレミアムビデオ.
         */
        PREMIUM_VIDEO
    }

    @Override
    public void onRentalVodListJsonParsed(final PurchasedVodListResponse response, final ErrorState jsonParseError) {
        if (response == null) {
            if (mWebClient.getError() != null && mWebClient.getError().getErrorType() != DtvtConstants.ErrorType.SUCCESS) {
                mError = mWebClient.getError();
            }
            mApiDataProviderCallback.rentalListNgCallback();
            return;
        }

        setStructDB(response);
        if (!mRequiredClipKeyList
                || mResponseEndFlag) {
            sendRentalListData(response);
        } else { // clipキー一覧取得が終わってない場合は待つ
            mPurchasedVodListResponse = response;
        }
    }

    @Override
    public void onVodClipKeyResult(final ClipKeyListResponse clipKeyListResponse
            ,final ErrorState errorState) {
        super.onVodClipKeyResult(clipKeyListResponse, errorState);
        // コールバック判定
        if (mPurchasedVodListResponse != null) {
            sendRentalListData(mPurchasedVodListResponse);
        }
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
     * @param mContext     コンテキスト
     * @param activityType 呼び出し元画面
     */
    public RentalDataProvider(final Context mContext, final RentalType activityType) {
        super(mContext);
        this.mContext = mContext;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) mContext;
        this.mSetDB = false;
        mActivityType = activityType;
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
                getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.VOD));
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
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RENTAL_VOD_LAST_UPDATE);
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        boolean fromDb = lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate);
        if (fromDb) {
            //レンタル一覧を送る
            Handler handler = new Handler();
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, RENTAL_VIDEO_LIST_SELECT);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
                mApiDataProviderCallback.rentalListNgCallback();
            }
        } else {
            //通信クラスにデータ取得要求を出す
            mWebClient = new RentalVodListWebClient(mContext);
            if (!mWebClient.getRentalVodListApi(this)) {
                mApiDataProviderCallback.rentalListNgCallback();
            }
        }
    }

    /**
     * DBからレンタル一覧を取得する.
     */
    public void getDbRentalList() {
        Handler handler = new Handler();
        try {
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, RENTAL_VIDEO_LIST_SELECT);
            dataBaseThread.start();
        } catch (RuntimeException e) {
            DTVTLogger.debug(e);
            mApiDataProviderCallback.rentalListNgCallback();
        }
    }

    /**
     * DBから取得した値からPurchasedVodListResponseを作成する.
     *
     * @param vodMetaList 購入済VOD一覧メタデータ
     * @param activeList  購入済VOD一覧ActiveList
     * @return PurchasedVodListResponse
     */
    @SuppressWarnings("OverlyLongMethod")
    PurchasedVodListResponse makeVodMetaData(final List<Map<String, String>> vodMetaList, final List<Map<String, String>> activeList) {
        PurchasedVodListResponse response = new PurchasedVodListResponse();
        ArrayList<VodMetaFullData> vodMetaFullDataList = new ArrayList<>();
        for (int i = 0; i < vodMetaList.size(); i++) {
            VodMetaFullData vodMetaFullData = new VodMetaFullData();
            String dispType = vodMetaList.get(i).get(JsonConstants.META_RESPONSE_DISP_TYPE);
            String estFlg = vodMetaList.get(i).get(JsonConstants.META_RESPONSE_EST_FLAG);
            vodMetaFullData.setTitle(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_TITLE));
            vodMetaFullData.setmThumb_448_252(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_THUMB_448));
            vodMetaFullData.setmThumb_640_360(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_THUMB_640));
            vodMetaFullData.setmDtv_thumb_448_252(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_DTV_THUMB_448));
            vodMetaFullData.setmDtv_thumb_640_360(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_DTV_THUMB_640));
            vodMetaFullData.setAvail_start_date(Long.parseLong(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_AVAIL_START_DATE)));
            vodMetaFullData.setAvail_end_date(Long.parseLong(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_AVAIL_END_DATE)));
            vodMetaFullData.setDisp_type(dispType);
            vodMetaFullData.setmService_id(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_SERVICE_ID));
            vodMetaFullData.setPublish_end_date(Long.parseLong(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_PUBLISH_END_DATE)));
            vodMetaFullData.setmSearch_ok(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_SEARCH_OK));
            vodMetaFullData.setCrid(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_CRID));
            vodMetaFullData.setmEvent_id(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_EVENT_ID));
            vodMetaFullData.setTitle_id(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_TITLE_ID));
            vodMetaFullData.setR_value(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_R_VALUE));
            vodMetaFullData.setDtv(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_DTV));
            vodMetaFullData.setmTv_service(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_TV_SERVICE));
            vodMetaFullData.setmContent_type(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
            vodMetaFullData.setDtvType(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_DTV_TYPE));
            vodMetaFullData.setRating(Double.parseDouble(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_RATING)));
            vodMetaFullData.setCid(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_CID));
            vodMetaFullData.setEpisode_id(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_EPISODE_ID));
            vodMetaFullData.setEstFlag(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_EST_FLAG));
            vodMetaFullData.setmChsvod(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_CHSVOD));
            vodMetaFullData.setmVod_start_date(Long.parseLong(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_VOD_START_DATE)));
            vodMetaFullData.setmVod_end_date(Long.parseLong(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_VOD_END_DATE)));
            vodMetaFullData.setEstFlag(estFlg);
            vodMetaFullData.setPuid(vodMetaList.get(i).get(JsonConstants.META_RESPONSE_PUID));
            String liinf_array = vodMetaList.get(i).get(JsonConstants.META_RESPONSE_LIINF_ARRAY);
            if (!TextUtils.isEmpty(liinf_array) && liinf_array.contains(COMMA)) {
                vodMetaFullData.setmLiinf_array(liinf_array.split(COMMA));
            } else {
                vodMetaFullData.setmLiinf_array(new String[]{liinf_array});
            }
            vodMetaFullDataList.add(vodMetaFullData);
        }
        ArrayList<ActiveData> activeDataList = new ArrayList<>();
        for (int i = 0; i < activeList.size(); i++) {
            ActiveData activeData = new ActiveData();
            activeData.setLicenseId(activeList.get(i).get(StringUtils.getConnectStrings(
                    JsonConstants.META_RESPONSE_ACTIVE_LIST, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_LICENSE_ID)));
            activeData.setValidEndDate(Long.parseLong(activeList.get(i).get(StringUtils.getConnectStrings(
                    JsonConstants.META_RESPONSE_ACTIVE_LIST, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_VAILD_END_DATE))));
            activeDataList.add(activeData);
        }
        response.setVodMetaFullData(vodMetaFullDataList);
        response.setVodActiveData(activeDataList);
        return response;
    }

    /**
     * DB保存.
     *
     * @param response 購入済みVOD一覧データ
     */
    private void setStructDB(final PurchasedVodListResponse response) {
        mPurchasedVodListResponse = response;
        if (mSetDB) {
            Handler handler = new Handler();
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, RENTAL_VIDEO_LIST_INSERT);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
        }
    }

    /**
     * ContentsData生成.
     * コンテンツデータ作成用メソッドのため行数超過を許容
     *
     * @param response 購入済みVOD一覧データ
     * @return コンテンツリスト
     */
    @SuppressWarnings("OverlyLongMethod")
    List<ContentsData> makeContentsData(final PurchasedVodListResponse response) {
        List<ContentsData> list = new ArrayList<>();
        ArrayList<VodMetaFullData> metaFullData = response.getVodMetaFullData();
        ArrayList<ActiveData> activeDataList = response.getVodActiveData();
        //activeDataListとmetaFullDataは別系統のJsonから取得するため、サイズチェックを入れる
        DTVTLogger.warning("metaFullData.mSize() = " + metaFullData.size() + ", activeDataList.mSize() = " + activeDataList.size());
        for (int i = 0; i < metaFullData.size(); i++) { //indexをactiveDataListで使うため、foreachを使いません
            VodMetaFullData vodMetaFullData = metaFullData.get(i);
            if (displayFlg(vodMetaFullData)) {
                ContentsData data = new ContentsData();
                String title = vodMetaFullData.getTitle();
                String searchOk = vodMetaFullData.getmSearch_ok();
                String dispType = vodMetaFullData.getDisp_type();
                String dtv = vodMetaFullData.getDtv();
                String dtvType = vodMetaFullData.getDtvType();
                data.setTitle(title);
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
                // activeDataList から視聴可能期限を取り出し、配信期限(AvailEndDate)として使用する(DREM-2275の仕様)
                String result = ContentUtils.getRentalVodValidInfo(vodMetaFullData, activeDataList, true);
                long activeEndDate = Long.parseLong(result);
                data.setIsRental(true);
                data.setAvailEndDate(activeEndDate);
                data.setVodStartDate(vodMetaFullData.getmVod_start_date());
                data.setVodEndDate(vodMetaFullData.getmVod_end_date());
                data.setEventId(vodMetaFullData.getmEvent_id());
                data.setServiceId(vodMetaFullData.getmService_id());
                data.setTitleId(vodMetaFullData.getTitle_id());
                data.setTvService(vodMetaFullData.getmTv_service());

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
                requestData.setTvService(tvService);
                requestData.setVodStartDate(vodMetaFullData.getmVod_start_date());
                data.setRequestData(requestData);

                if (mRequiredClipKeyList) {
                    // クリップ状態をコンテンツリストに格納
                    data.setClipStatus(getClipStatus(dispType, contentsType, dTv,
                            requestData.getCrid(), requestData.getServiceId(),
                            requestData.getEventId(), requestData.getTitleId(), tvService, vodMetaFullData.getmVod_start_date()));
                }

                list.add(data);
            }
        }
        return list;
    }

    /**
     * レンタル/プレミアムビデオコンテンツ判定.
     *
     * @param vodMetaFullData コンテンツメタデータ
     * @return レンタル/プレミアムビデオコンテンツ判定結果
     */
    private boolean displayFlg(final VodMetaFullData vodMetaFullData) {

        switch (mActivityType) {
            case RENTAL_LIST:
                //「estflg」が「0」または未設定かつ「disp_type」が「video_program」または「video_package」
                return (vodMetaFullData.getEstFlag() == null
                        || vodMetaFullData.getEstFlag().length() == 0
                        || vodMetaFullData.getEstFlag().equals(EST_FLAG_FALSE))
                        && (vodMetaFullData.getDisp_type() != null
                        && (vodMetaFullData.getDisp_type().equals(DISP_TYPE_VIDEO_PROGRAM)
                        || vodMetaFullData.getDisp_type().equals(DISP_TYPE_VIDEO_PACKAGE)));
            case PREMIUM_VIDEO:
                //「chsvod」が「0」または未設定かつ「disp_type」が「subscription_package」または「series_svod」のコンテンツ
                return (vodMetaFullData.getmChsvod() == null
                        || vodMetaFullData.getmChsvod().length() == 0
                        || vodMetaFullData.getmChsvod().equals(CHSVOD_FLAG_FALSE))
                        && (vodMetaFullData.getDisp_type() != null
                        && (vodMetaFullData.getDisp_type().equals(DISP_TYPE_SUBSCRIPTION_PACKAGE)
                        || vodMetaFullData.getDisp_type().equals(DISP_TYPE_SERIES_SVOD)));
            default:
                return false;
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

    @Override
    public List<Map<String, String>> dbOperation(final DataBaseThread dataBaseThread, final int operationId) {
        super.dbOperation(dataBaseThread, operationId);
        switch (operationId) {
            case RENTAL_VIDEO_LIST_INSERT:
                RentalListInsertDataManager dataManager = new RentalListInsertDataManager(mContext);
                dataManager.insertRentalListInsertList(mPurchasedVodListResponse);
                sendRentalListData(mPurchasedVodListResponse);
                break;
            case RENTAL_VIDEO_LIST_SELECT:
                List<Map<String, String>> vodMetaList;
                List<Map<String, String>> activeList;
                RentalListDataManager rentalListDataManager = new RentalListDataManager(mContext);
                vodMetaList = rentalListDataManager.selectRentalListData();
                activeList = rentalListDataManager.selectRentalActiveListData();
                PurchasedVodListResponse purchasedVodListResponse = makeVodMetaData(vodMetaList, activeList);
                sendRentalListData(purchasedVodListResponse);
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * レンタルデータ取得エラーのクラスを返すゲッター.
     *
     * @return レンタルデータ取得エラーのクラス
     */
    public ErrorState getError() {
        return mError;
    }
}
