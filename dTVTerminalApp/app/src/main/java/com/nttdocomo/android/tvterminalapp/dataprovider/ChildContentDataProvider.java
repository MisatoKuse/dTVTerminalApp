/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.support.annotation.Nullable;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChildContentListGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChildContentListGetWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.util.ArrayList;
import java.util.List;

/**
 * 子コンテンツデータプロバイダー.
 */
public class ChildContentDataProvider extends ClipKeyListDataProvider implements ChildContentListGetWebClient.JsonParserCallback {

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
    private DataCallback mCallback = null;
    /**子コンテンツ一覧取得レスポンス.*/
    private ChildContentListGetResponse mChildContentListGetResponse;
    /**WebAPIエラー情報.*/
    private ErrorState mError = null;
    /**通信止めるフラグ.*/
    private boolean mIsCancel = false;
    /**子コンテンツ一覧データ取得位置.*/
    private int mPagerOffset = 0;

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
    }

    @Override
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse
            ,final ErrorState errorState) {
        super.onVodClipKeyListJsonParsed(clipKeyListResponse,errorState);
        if (mChildContentListGetResponse != null) {
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
            if (!mRequiredClipKeyList
                    || mResponseEndFlag) {
                sendData(response);
            } else { // clipキー一覧取得が終わってない場合は待つ
                mChildContentListGetResponse = response;
            }
        }
    }

    /**
     * 子コンテンツ一覧取得.
     * @param crid コンテンツ識別子
     * @param offset 　取得位置
     * @param dispType 表示タイプ
     */
    public void getChildContentList(final String crid, final int offset, final String dispType) {
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
            boolean result = mWebClient.requestChildContentListGetApi(crid, offset, filter, ageReq, this);
            if (!result) {
                mCallback.childContentListCallback(null);
            }
        } else {
            mCallback.childContentListCallback(null);
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

        UserState userState = UserInfoUtils.getUserState(mContext);
        for (VodMetaFullData vodMetaFullData : metaFullData) {
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
            data.setAvailEndDate(vodMetaFullData.getAvail_end_date());
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
            data.setRequestData(requestData);

            if (mRequiredClipKeyList) {
                // クリップ状態をコンテンツリストに格納
                data.setClipStatus(getClipStatus(dispType, contentsType, dTv,
                        requestData.getCrid(), requestData.getServiceId(),
                        requestData.getEventId(), requestData.getTitleId(), tvService));
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
     * ページオフセットの取得
     *
     * @return ページオフセット
     */
    public int getPagerOffset() {
        return mPagerOffset;
    }
}
