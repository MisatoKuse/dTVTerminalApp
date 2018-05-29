/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.dlna;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.DlnaBrowseDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DataBaseThread;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaObject;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;

import java.util.List;
import java.util.Map;

public class DlnaContentTerChennelDataProvider implements DlnaManager.BrowseListener,
        DataBaseThread.DataBaseOperation {

    // declaration
    /**
     * DataProviderコールバック.
     */
    public interface ContentsDataCallback {
        /**
         * コンテンツブラウズコールバック.
         * @param objs コンテンツリスト
         */
        void onContentBrowseCallback(final DlnaObject[] objs);
    }

    /**
     * チャンネル検索.
     */
    private static final int CHANNEL_SELECT = 0;
    private static final int CHANNEL_UPDATE = 1;
    private DlnaObject[] mChannelList = null;
    private String mContainerId = null;
    /**コールバック.*/
    private ContentsDataCallback mCallback = null;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public DlnaContentTerChennelDataProvider(final Context context) {
        mCallback = (ContentsDataCallback) context;
    }

    /**
     *  コンテンツブラウズコールバック.
     * @param objs コンテンツリスト
     */
    @Override
    public void onContentBrowseCallback(final DlnaObject[] objs) {
        if (objs != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            mChannelList = objs;
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, CHANNEL_UPDATE);
                dataBaseThread.start();
                if (mCallback != null) {
                    mCallback.onContentBrowseCallback(objs);
                }
            } catch (IllegalThreadStateException e) {
                DTVTLogger.debug(e);
            }
        } else {
            DTVTLogger.error("no ContentBrowseCallback");
        }
    }

    @Override
    public void onDbOperationFinished(final boolean isSuccessful,
                                      final List<Map<String, String>> resultSet,
                                      final int operationId) {
        if (isSuccessful) {
            switch (operationId) {
                case CHANNEL_SELECT:
                    DlnaObject[] objs = new DlnaObject[resultSet.size()];
                    for (int i = 0; i < resultSet.size(); i++) {
                        DlnaObject obj = new DlnaObject();
                        Map<String, String> map = resultSet.get(i);
                        obj.mBitrate = map.get(DataBaseConstants.DLNA_BROWSE_COLUM_BITRATE);
                        obj.mChannelName = map.get(DataBaseConstants.DLNA_BROWSE_COLUM_CHANNEL_NAME);
                        obj.mDuration = map.get(DataBaseConstants.DLNA_BROWSE_COLUM_DURATION);
                        obj.mResolution = map.get(DataBaseConstants.DLNA_BROWSE_COLUM_RESOLUTION);
                        obj.mResUrl = map.get(DataBaseConstants.DLNA_BROWSE_COLUM_RES_URL);
                        obj.mSize = map.get(DataBaseConstants.DLNA_BROWSE_COLUM_SIZE);
                        obj.mVideoType = map.get(DataBaseConstants.DLNA_BROWSE_COLUM_VIDEO_TYPE);
                        objs[i] = obj;
                    }

                    if (mCallback != null) {
                        mCallback.onContentBrowseCallback(objs);
                    }

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public List<Map<String, String>> dbOperation(final int mOperationId) {
        List<Map<String, String>> resultSet = null;
        switch (mOperationId) {
            case CHANNEL_UPDATE://サーバーから取得したチャンネルデータをDBに保存する
                DlnaBrowseDataManager dlnaBrowseInsertDataManager = new DlnaBrowseDataManager(DlnaManager.shared().mContext);
                dlnaBrowseInsertDataManager.insertChannelInsertList(mChannelList, mContainerId);
                break;
            case CHANNEL_SELECT://DBからチャンネルデータを取得して、画面に返却する
                DlnaBrowseDataManager dlnaBrowseDataManager = new DlnaBrowseDataManager(DlnaManager.shared().mContext);
                resultSet = dlnaBrowseDataManager.selectChannelListProgramData(mContainerId);
                break;
            default:
                break;
        }
        return resultSet;
    }

    /**
     * BrowseContentWithContainerId.
     * @param context コンテキスト
     */
    public void browseContentWithContainerId(final Context context) {
        DlnaManager.shared().mBrowseListener = this;
        DlnaManager.shared().mContext = context;
        mContainerId = DlnaUtils.getContainerIdByImageQuality(context, DlnaUtils.DLNA_DMS_TER_CHANNEL);
        DateUtils dateUtils = new DateUtils(DlnaManager.shared().mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.DLNA_BROWSE_UPDATE + mContainerId);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler(DlnaManager.shared().mContext.getMainLooper());
            //チャンネル情報更新
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, CHANNEL_SELECT);
                dataBaseThread.start();
            } catch (IllegalThreadStateException e) {
                DTVTLogger.debug(e);
                //TODO　:エラー返却した上でUI上に通知が必要
            }
        } else {
            DlnaManager.shared().BrowseContentWithContainerId(mContainerId);
        }
    }

}
