/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChildContentListGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChildContentListGetWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.util.ArrayList;
import java.util.List;

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
    private ChildContentListGetWebClient mWebClient;
    private DataCallback mCallback = null;

    private ChildContentListGetResponse mChildContentListGetResponse;

    private ErrorState mError = null;
    private boolean mIsCancel = false;

    // endregion variable

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public ChildContentDataProvider(Context context) {
        super(context);
        mCallback = (DataCallback)context;
        mWebClient = new ChildContentListGetWebClient(context);
    }

    @Override
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        super.onVodClipKeyListJsonParsed(clipKeyListResponse);
        if (mChildContentListGetResponse != null) {
            sendData(mChildContentListGetResponse);
        }
    }

    @Override
    public void onJsonParsed(@Nullable ChildContentListGetResponse response) {
        if (response == null) {
            if (mWebClient.getError() != null) {
                mError = mWebClient.getError();
            }
            mCallback.childContentListCallback(null);
        } else {
            mChildContentListGetResponse = response;
            sendData(response);
        }
    }

    /**
     *
     * @param offset
     */
    public void getChildContentList(final String crid, final int offset, final String dispType) {
        if (!mIsCancel) {
            if (mRequiredClipKeyList) {
                getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
                mRequiredClipKeyList = false;
            }

            String filter = WebApiBasePlala.FILTER_RELEASE;
            if (dispType.equals(DTVTConstants.DISP_TYPE_SERIES_SVOD)) {
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
        for(VodMetaFullData vodMetaFullData : metaFullData) {
            ContentsData data = new ContentsData();
            String title = vodMetaFullData.getTitle();
            String searchOk = vodMetaFullData.getmSearch_ok();
            String dispType = vodMetaFullData.getDisp_type();
            String dtv = vodMetaFullData.getDtv();
            String dtvType = vodMetaFullData.getDtvType();
            data.setTitle(title);
            //エポック秒から文字に変換
            data.setRatStar(String.valueOf(vodMetaFullData.getRating()));
            data.setThumURL(vodMetaFullData.getmThumb_448_252());
            data.setThumDetailURL(vodMetaFullData.getmThumb_640_360());
            data.setSearchOk(searchOk);
            data.setContentsType(vodMetaFullData.getmContent_type());
            data.setDtv(dtv);
            data.setDtvType(dtvType);
            data.setDispType(dispType);
            data.setClipExec(ClipUtils.isCanClip(userState, dispType, searchOk, dtv, dtvType));
            data.setContentsId(vodMetaFullData.getCrid());

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
    private void sendData(@NonNull ChildContentListGetResponse response) {
        List<ContentsData> list = makeContentsData(response);
        mCallback.childContentListCallback(list);
    }
}
