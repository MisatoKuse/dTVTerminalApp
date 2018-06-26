/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.VodClipWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * クリップ(ビデオ)データプロバイダ.
 */
public class VodClipDataProvider extends ClipKeyListDataProvider implements VodClipWebClient.VodClipJsonParserCallback {

    /**
     * コンテキスト.
     */
    private final Context mContext;
    /**
     * クリップ一覧データ.
     */
    private VodClipList mClipList = null;

    /**
     * callback.
     */
    private final ApiDataProviderCallback mApiDataProviderCallback;
    /**
     * クリップ一覧データ取得位置.
     */
    private int mPagerOffset = 0;
    /**
     * クリップキーリストレスポンス.
     */
    private ClipKeyListResponse mClipKeyListResponse = null;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * 視聴中ビデオリスト取得WebClient.
     */
    private VodClipWebClient mWebClient = null;
    /**
     * ネットワークエラーの控え.
     */
    private ErrorState mNetworkError = null;

    @Override
    public void onVodClipJsonParsed(final List<VodClipList> vodClipLists) {
        DTVTLogger.start();
        if (vodClipLists != null) {
            List vclist = vodClipLists.get(0).getVcList();
            if (vclist != null) {
                VodClipList list = vodClipLists.get(0);
                if (!mRequiredClipKeyList
                        || mResponseEndFlag) {
                    sendVodClipListData(list.getVcList(), mClipKeyListResponse);
                } else {
                    mClipList = list;
                    sendVodClipListData(list.getVcList(), mClipKeyListResponse);
                }
            } else {
                if (null != mApiDataProviderCallback) {
                    //ヌルなので、ネットワークエラーを取得する
                    mNetworkError = mWebClient.getError();

                    mApiDataProviderCallback.vodClipListCallback(null);
                }
            }
        } else {
            //ヌルなので、ネットワークエラーを取得する
            mNetworkError = mWebClient.getError();

            if (null != mApiDataProviderCallback) {
                mApiDataProviderCallback.vodClipListCallback(null);
            }
        }
        DTVTLogger.end();
    }

    @Override
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        super.onVodClipKeyListJsonParsed(clipKeyListResponse);
        DTVTLogger.start();
        mClipKeyListResponse = clipKeyListResponse;
        getVodClipListData(mPagerOffset);
        DTVTLogger.end();
    }

    /**
     * 画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {
        /**
         * クリップリスト用コールバック.
         *
         * @param clipContentInfo クリップ表示用データ
         */
        void vodClipListCallback(List<ContentsData> clipContentInfo);
    }

    /**
     * コンストラクタ.
     *
     * @param mContext コンテクスト
     */
    public VodClipDataProvider(final Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    /**
     * Activityからのデータ取得要求受付.
     *
     * @param pagerOffset ページオフセット
     */
    public void getClipData(final int pagerOffset) {
        DTVTLogger.start();
        mClipList = null;
        mPagerOffset = pagerOffset;
        if (!mIsCancel) {
            // クリップキー一覧を取得
            if (mRequiredClipKeyList) {
                getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.VOD));
            }
        } else {
            DTVTLogger.error("VodClipDataProvider is stopping connection");
            if (null != mApiDataProviderCallback) {
                mApiDataProviderCallback.vodClipListCallback(null);
            }
        }
        DTVTLogger.end();
    }

    /**
     * VodクリップリストをActivityに送る.
     *
     * @param list Vodクリップリスト
     * @param clipKeyListResponse クリップキーリスト
     */
    private void sendVodClipListData(final List<Map<String, String>> list, final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        List<Map<String, String>> clipList = null;
        if (clipKeyListResponse != null) {
            clipList = clipKeyListResponse.getCkList();
        }
        mApiDataProviderCallback.vodClipListCallback(setContentData(list, clipList, null, true));
        DTVTLogger.end();
    }

    @Override
    protected void setResponseId(final ContentsData contentInfo, final  Map<String, String> map) {
       DTVTLogger.start();
        DTVTLogger.end();
    }

    @Override
    protected void setChannelInfo(final Map<String, String> map, final ContentsData contentInfo, final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    @Override
    protected void setRequestType(final ClipRequestData requestData, final String dispType, final String contentsType) {
        DTVTLogger.start();
        DTVTLogger.end();
    }

    /**
     * Vodクリップリストデータ取得開始.
     *
     * @param pagerOffset ページオフセット
     */
    private void getVodClipListData(final int pagerOffset) {
        if (!mIsCancel) {
            //通信クラスにデータ取得要求を出す
            mWebClient = new VodClipWebClient(mContext);

            String direction = "";
            UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
            if (!mWebClient.getVodClipApi(userInfoDataProvider.getUserAge(), DtvtConstants.REQUEST_LIMIT_50,
                    DtvtConstants.REQUEST_LIMIT_1, pagerOffset, direction, this)) {
                mApiDataProviderCallback.vodClipListCallback(null);
            }
        } else {
            DTVTLogger.error("VodClipDataProvider is stopping connection");
            if (null != mApiDataProviderCallback) {
                mApiDataProviderCallback.vodClipListCallback(null);
            }
        }
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsCancel = true;
        //親クラスのstopConnectionで止める
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
        //親クラスのenableConnectionで再開
        enableConnection();
        if (mWebClient != null) {
            mWebClient.enableConnection();
        }
    }

    /**
     * ネットワークエラーのゲッター.
     *
     * @return ネットワークエラーのクラス
     */
    public ErrorState getNetworkError() {
        return mNetworkError;
    }
}
