/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.List;

/**
 * レコメンドデータ先読みクラス
 */
public class RecommendDataPreloader extends AsyncTask<Context, Void, Void>
        implements RecommendDataProvider.RecommendApiDataProviderCallback {
    // 退避用コンテキスト
    Context mContext = null;

    //レコメンドデータプロバイダーの宣言
    RecommendDataProvider mRecommendDataProvider;

    //レコメンドデータ先読みの各処理の終了判定用フラグ
    private static boolean sChannelEnd = false;
    private static boolean sVideoEnd = false;
    private static boolean sDtvEnd = false;
    private static boolean sDanimeEnd = false;
    private static boolean sDchannelEnd = false;

    @Override
    protected synchronized Void doInBackground(Context... contexts) {
        DTVTLogger.start();

        //コンテキストの退避
        mContext = contexts[0];
        if (mContext == null) {
            return null;
        }

        //事前読み込み処理の呼び出し
        recommendDataPreload();

        DTVTLogger.end();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        DTVTLogger.start();

        if (mContext == null) {
            //コンテキストがヌルならば帰る
            DTVTLogger.end();
            return;
        }

        DTVTLogger.end();
    }

    /**
     * あらかじめレコメンド情報を先読みする処理.
     */
    public void recommendDataPreload() {
        //各先読みの終了フラグをクリア
        sChannelEnd = false;
        sVideoEnd = false;
        sDtvEnd = false;
        sDanimeEnd = false;
        sDchannelEnd = false;

        //処理を行う件数を数えるため、レコメンド画面のタブ用の文字列を取得する
        String[] mTabNames = mContext.getResources().getStringArray(
                R.array.recommend_list_tab_names);

        //レコメンドデータプロバイダーの宣言
        mRecommendDataProvider = new RecommendDataProvider(mContext, this);

        //タブの数だけ回る
        for (int requestService = 0; requestService < mTabNames.length; requestService++) {
            //それぞれのタブ用のデータを事前に読み込む
            mRecommendDataProvider.startGetRecommendData(
                    requestService, 1,
                    SearchConstants.RecommendList.REQUEST_MAX_CCOUNT_RECOMMEND_PRELOAD);
        }
    }

    @Override
    public void RecommendChannelCallback(List<ContentsData> recommendContentInfoList) {
        DTVTLogger.start();

        //チャンネルの先読みが終了した
        sChannelEnd = true;

        //終了判定
        endPreloadClear();

        DTVTLogger.end();
    }

    @Override
    public void RecommendVideoCallback(List<ContentsData> recommendContentInfoList) {
        DTVTLogger.start();

        //ビデオの先読みが終了した
        sVideoEnd = true;

        //終了判定
        endPreloadClear();

        DTVTLogger.end();
    }

    @Override
    public void RecommendDTVCallback(List<ContentsData> recommendContentInfoList) {
        DTVTLogger.start();

        //DTVの先読みが終了した
        sDtvEnd = true;

        //終了判定
        endPreloadClear();

        DTVTLogger.end();
    }

    @Override
    public void RecommendDAnimeCallback(List<ContentsData> recommendContentInfoList) {
        DTVTLogger.start();

        //Dアニメの先読みが終了した
        sDanimeEnd = true;

        //終了判定
        endPreloadClear();

        DTVTLogger.end();
    }

    @Override
    public void RecommendDChannelCallback(List<ContentsData> recommendContentInfoList) {
        DTVTLogger.start();

        //Dチャンネルの先読みが終了した
        sDchannelEnd = true;
        //終了判定
        endPreloadClear();

        DTVTLogger.end();
    }

    @Override
    public void RecommendNGCallback() {
        DTVTLogger.start();

        //フェールセーフとして、NGが発生した場合は、先読みが終わったことにして、レコメンド時の再処理にゆだねる
        sChannelEnd = true;
        sVideoEnd = true;
        sDtvEnd = true;
        sDanimeEnd = true;
        sDchannelEnd = true;

        //ヌルを入れて解放されやすくする
        mRecommendDataProvider = null;

        DTVTLogger.end();
    }

    /**
     * 先読み情報の終了状況を取得する
     *
     * @return
     */
    public static boolean isEndPreload() {
        //先読みが全て終了していた場合はtrue
        if (sChannelEnd && sVideoEnd && sDtvEnd && sDanimeEnd && sDchannelEnd) {
            return true;
        }

        //一つでも終わっていなければfalse
        return false;
    }

    /**
     * 終了後は先読みのクラスにヌルを入れる
     */
    private void endPreloadClear() {
        //終了判定
        if (isEndPreload()) {
            //ヌルを入れて解放されやすくする
            mRecommendDataProvider = null;
        }
    }
}
