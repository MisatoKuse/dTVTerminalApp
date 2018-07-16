/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvClipWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * クリップ(TV)用データプロバイダ.
 */
public class TvClipDataProvider extends ClipKeyListDataProvider
        implements TvClipWebClient.TvClipJsonParserCallback,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback {
    /**
     * コンテキスト.
     */
    private final Context mContext;
    /**
     * クリップリスト(Map).
     */
    private List<Map<String, String>> mClipMapList = null;
    /**
     * チャンネル情報.
     */
    private ArrayList<ChannelInfo> mChannels = null;

    /**
     * callback.
     */
    private final TvClipDataProviderCallback mApiDataProviderCallback;
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
    private TvClipWebClient mWebClient = null;
    /**
     * チャンネルプロバイダー.
     */
    private ScaledDownProgramListDataProvider mScaledDownProgramListDataProvider = null;
    /**
     * ネットワークエラーの控え.
     */
    private ErrorState mNetworkError = null;

    @Override
    public void onTvClipJsonParsed(final List<TvClipList> tvClipLists) {
        DTVTLogger.start();
        if (tvClipLists != null) {
            // 次のリクエストに使用するpagerOffsetを設定
            Map<String, String> clipMap = tvClipLists.get(0).getVcMap();
            Integer offset = Integer.parseInt(clipMap.get(JsonConstants.META_RESPONSE_OFFSET));
            Integer count = Integer.parseInt(clipMap.get(JsonConstants.META_RESPONSE_COUNT));
            if (offset >= 0 && count >= 0) {
                mPagerOffset = offset + count;
            }

            mClipMapList = tvClipLists.get(0).getVcList();
            if (mClipMapList != null) {
                    if (!mRequiredClipKeyList
                            || mResponseEndFlag) {
                        sendTvClipListData(mClipMapList, mChannels, mClipKeyListResponse);
                    } else {
                        //TODO ClipKeyListDataProviderの不具合により、クリップキーリストのコールバックが取得できないための暫定対応
                        sendTvClipListData(mClipMapList, mChannels, mClipKeyListResponse);
                    }
            } else {
                if (null != mApiDataProviderCallback) {
                    //ヌルなので、ネットワークエラーを取得する
                    mNetworkError = mWebClient.getError();

                    mApiDataProviderCallback.tvClipListCallback(null);
                }
            }
        } else {
            //ヌルなので、ネットワークエラーを取得する
            mNetworkError = mWebClient.getError();

            if (null != mApiDataProviderCallback) {
                mApiDataProviderCallback.tvClipListCallback(null);
            }
        }
        DTVTLogger.end();
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo, final int[] chNo) {
        //NOP
    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        this.mChannels = channels;
        sendTvClipListData(mClipMapList, mChannels, mClipKeyListResponse);
        DTVTLogger.end();
    }

    @Override
    public void onTvClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse
            ,final ErrorState errorState) {
        DTVTLogger.start();
        super.onTvClipKeyListJsonParsed(clipKeyListResponse,errorState);
        mClipKeyListResponse = clipKeyListResponse;

        //レスポンスがヌルかどうかの判定
        if (clipKeyListResponse == null) {
            //既にエラーとなっているので、エラー処理を呼ぶ
            mNetworkError = errorState;
            mApiDataProviderCallback.tvClipListCallback(null);
            return;
        }

        getTvClipListData(mPagerOffset);
        DTVTLogger.end();
    }

    /**
     * 画面用データを返却するためのコールバック.
     */
    public interface TvClipDataProviderCallback {
        /**
         * クリップリスト用コールバック.
         *
         * @param clipContentInfo クリップ表示情報
         */
        void tvClipListCallback(List<ContentsData> clipContentInfo);
    }

    /**
     * コンストラクタ.
     *
     * @param context コンテクスト
     */
    public TvClipDataProvider(final Context context) {
        super(context);
        this.mContext = context;
        this.mApiDataProviderCallback = (TvClipDataProviderCallback) context;
    }

    /**
     * Activityからのデータ取得要求受付.
     *
     * @param pagerOffset ページオフセット
     */
    public void getClipData(final int pagerOffset) {
        DTVTLogger.start();
        mPagerOffset = pagerOffset;
        if (!mIsCancel) {
            // クリップキー一覧を取得
            if (mRequiredClipKeyList) {
                getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.TV));
            }
        } else {
            DTVTLogger.error("TvClipDataProvider is stopping connection");
            if (null != mApiDataProviderCallback) {
                mApiDataProviderCallback.tvClipListCallback(null);
            }
        }
        DTVTLogger.end();
    }

    /**
     * チャンネル情報取得.
     */
    private void getChannelList() {
        // チャンネル情報を取得
        if (mScaledDownProgramListDataProvider == null) {
            mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(mContext, this);
        }
        mScaledDownProgramListDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_ALL);
    }

    /**
     * TvクリップリストをActivityに送る.
     *
     * @param list Tvクリップリスト
     * @param channels チャンネル情報
     * @param response クリップキーリスト
     */
    private void sendTvClipListData(final List<Map<String, String>> list, final ArrayList<ChannelInfo> channels, final ClipKeyListResponse response) {
        DTVTLogger.start();
        if (channels != null) {
            List<Map<String, String>> mapList = null;
            if (response != null) {
                mapList = response.getCkList();
            }
            mApiDataProviderCallback.tvClipListCallback(setContentData(list, mapList, channels, false));
        } else {
            getChannelList();
        }
        DTVTLogger.end();
    }
    /**
     * Tvクリップリストデータ取得開始.
     *
     * @param pagerOffset ページオフセット
     */
    private void getTvClipListData(final int pagerOffset) {
        if (!mIsCancel) {
            //通信クラスにデータ取得要求を出す
            mWebClient = new TvClipWebClient(mContext);

            UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
            if (!mWebClient.getTvClipApi(userInfoDataProvider.getUserAge(), DtvtConstants.REQUEST_LIMIT_50,
                    DtvtConstants.REQUEST_LIMIT_1, pagerOffset, DtvtConstants.CLIP_BODY_DIRECTION_NEXT, this)) {
                mApiDataProviderCallback.tvClipListCallback(null);
            }
        } else {
            DTVTLogger.error("TvClipDataProvider is stopping connection");
            if (null != mApiDataProviderCallback) {
                mApiDataProviderCallback.tvClipListCallback(null);
            }
        }
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsCancel = true;
        //親クラスで止める
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
        //親クラスで再開
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

    @Override
    public void clipKeyResult() {
        //Nop 仕様により実装のみ
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
