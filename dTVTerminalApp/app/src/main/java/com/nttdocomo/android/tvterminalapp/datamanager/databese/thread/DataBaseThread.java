/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.thread;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChannelList;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DbThreadクラス
 * 機能： DB操作は時間かかる操作なので、UI Thread以外の新しいThreadで実行するクラスである.
 * <p>
 * 注意点: 一つのDB操作は一つのDbThreadオブジェクトを使うこと推薦.
 */
public class DataBaseThread extends Thread {

    /** コンテキスト. */
    private Context mContext = null;
    /** ハンドラ. */
    private Handler mHandle = null;
    /** callbackInterface. */
    private DataBaseOperation mDataBaseOperationFinish = null;
    /** OperationId. */
    private int mOperationId = 0;
    /** DBから取得するチャンネル番号を格納するリスト. */
    private List<String> mFromDB = new ArrayList<>();
    /** チャンネル番号で仕分けした番組データ. */
    private ChannelInfoList mChannelsInfoList = null;
    /** チャンネルデータ. */
    private ChannelList mChannelList = null;
    /** RecommendChannelList. */
    private RecommendChannelList mRecommendChannelList = null;
    /** ContentsDataList. */
    private List<ContentsData> mContentsDataList = null;
    /** ClipKeyListResponse. */
    private ClipKeyListResponse mClipKeyListResponse = null;
    /** ErrorState. */
    private ErrorState mErrorState = null;

    /**
     * callbackInterface.
     */
    public interface DataBaseOperation {
        /**
         * DB操作完了する時実行される.
         *
         * @param isSuccessful DB操作結果
         * @param resultSet    ResultSetがある場合、ResultSetを戻す。ResultSetはない場合、nullを戻す
         * @param operationId  多Threadオブジェクトを使用する時、Threadオブジェクトを区別する
         */
        void onDbOperationFinished(boolean isSuccessful, final List<Map<String, String>> resultSet, int operationId);

        /**
         * DB操作をThread中で実行、操作内容はクラス外で決める(e.g. "select * from xxx where yy=zz ...,   delete from xxx").
         *
         * @param operationId operationId
         * @return DB取得結果
         */
        List<Map<String, String>> dbOperation(final DataBaseThread dataBaseThread, final int operationId);
    }

    /**
     * @param handle      非同期処理ハンドラー.
     * @param lis         　操作
     * @param operationId 多Threadオブジェクトを使用する時、Threadオブジェクトを区別する
     */
    public DataBaseThread(@NonNull final Handler handle, final DataBaseOperation lis, final int operationId) {

        mHandle = handle;
        mDataBaseOperationFinish = lis;
        mOperationId = operationId;
    }

    @Override
    public synchronized void run() {
        List<Map<String, String>> ret = null;

        if (null != mDataBaseOperationFinish) {
            ret = mDataBaseOperationFinish.dbOperation(DataBaseThread.this, mOperationId);
        }

        final List<Map<String, String>> finalRet = ret;
        mHandle.post(new Runnable() {

            @Override
            public void run() {
                if (null != mDataBaseOperationFinish) {
                    if (finalRet == null || finalRet.size() < 1 || finalRet.get(0).isEmpty()) {
                        mDataBaseOperationFinish.onDbOperationFinished(false, null, mOperationId);
                    } else {
                        mDataBaseOperationFinish.onDbOperationFinished(true, finalRet, mOperationId);
                    }
                }
            }
        });
    }

    /* DataBaseThread用 Getter/Setter */
    /* DataBaseThread内でメンバ変数を参照する場合は 次のように Getter/Setter を使用すること */
    /**
     * DataBaseキャッシュから取得するチャンネル番号リスト.
     * @return channel番号リスト
     */
    public List<String> getFromDB() {
        return mFromDB;
    }

    /**
     * チャンネル番号リストを返却.
     * ※DatabaseThread内でメンバ変数を操作する場合はこのようにGetter/Setterを使用すること
     * @param fromDB チャンネル番号リスト
     */
    public void setFromDB(final List<String> fromDB) {
        this.mFromDB = fromDB;
    }

    /**
     * DataBaseThread内で使用する複数チャンネルクラス.
     * @return 複数チャンネルクラス
     */
    public ChannelInfoList getChannelsInfoList() {
        return mChannelsInfoList;
    }

    /**
     * 複数チャンネルクラスを返却.
     * @param mChannelsInfoList 複数チャンネルクラス
     */
    public void setChannelsInfoList(final ChannelInfoList mChannelsInfoList) {
        this.mChannelsInfoList = mChannelsInfoList;
    }

    /**
     * チャンネルリスト.
     * @return チャンネルリスト
     */
    public ChannelList getChannelList() {
        return mChannelList;
    }

    /**
     * チャンネルリストを返却.
     * @param mChannelList チャンネルリスト
     */
    public void setChannelList(final ChannelList mChannelList) {
        this.mChannelList = mChannelList;
    }

    /**
     * RecommendChannelListを取得.
     *
     * @return RecommendChannelList
     */
    public RecommendChannelList getRecommendChannelList() {
        return mRecommendChannelList;
    }

    /**
     * RecommendChannelListを設定.
     *
     * @param mRecommendChannelList RecommendChannelList
     */
    public void setRecommendChannelList(final RecommendChannelList mRecommendChannelList) {
        this.mRecommendChannelList = mRecommendChannelList;
    }

    /**
     * List<ContentsData>を取得.
     *
     * @return List<ContentsData>
     */
    public List<ContentsData> getContentsDataList() {
        return mContentsDataList;
    }

    /**
     * List<ContentsData>を設定.
     *
     * @param mContentsDataList ContentsDataリスト
     */
    public void setContentsDataList(final List<ContentsData> mContentsDataList) {
        this.mContentsDataList = mContentsDataList;
    }

    public ClipKeyListResponse getClipKeyListResponse() {
        return mClipKeyListResponse;
    }

    public void setClipKeyListResponse(final ClipKeyListResponse mClipKeyListResponse) {
        this.mClipKeyListResponse = mClipKeyListResponse;
    }

    public ErrorState getErrorState() {
        return mErrorState;
    }

    public void setErrorState(final ErrorState mErrorState) {
        this.mErrorState = mErrorState;
    }
}